package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.ImageView;


/**
 * Created by OwenSong on 2017-10-16.
 */

public class MainActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView) ;
        imageView1.setImageResource(R.drawable.main_icon) ;

        //로그인버튼
        Button sign_in_button =(Button)findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {

                        //===========================================변수
                        int checkid=0;
                        EditText idcheck_edit = (EditText)findViewById(R.id.login_id);
                        EditText pwcheck_edit = (EditText)findViewById(R.id.login_pw);
                        RelativeServer rs = null;
                        //===========================================
                        checkid = idcheck_edit.getText().toString().length();
                        if(checkid == 0) {
                            Toast.makeText(getApplicationContext(), "아이디를 입력해주세요!", Toast.LENGTH_LONG).show();
                        }
                        if(checkid != 0 && pwcheck_edit.getText().toString().length() == 0){
                            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show();
                        }else if(checkid != 0){
                            final String idcheck = idcheck_edit.getText().toString();
                            final String pwcheck = pwcheck_edit.getText().toString();
                            new Thread(new Runnable() {
                                public void run() {
                                    runnningThread(idcheck,pwcheck);
                                }
                            }).start();
                        }
                    }
                }
        );
        //회원가입버튼
        Button register_button =(Button)findViewById(R.id.register_button);
        register_button.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intent);
                }
            }
        );
    }

    public void runnningThread(String id,String pw){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/logincheck?id=\""+id+"\"&pw=\""+pw+"\"");

            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            String jsonstring = new String(bText);
            isText.close();
            conn.disconnect();

            JSONObject json = new JSONObject(jsonstring);
            final String sendid = json.getString("id");
            final String sendnum = json.getString("num") ;
            final String result = json.getString("result") ;

            if(result.equals("true")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), BookMenuActivity.class);
                        intent.putExtra("id", sendid);
                        intent.putExtra("num", sendnum);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 확인해주세요!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

