package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;


/**
 * Created by OwenSong on 2017-10-16.
 */

public class RegisterActivity extends Activity{
    int idchecknumber=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView2) ;
        imageView1.setImageResource(R.drawable.main_icon) ;

        Button idcheck_button =(Button)findViewById(R.id.button2);
        idcheck_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        EditText et = (EditText)findViewById(R.id.check_id);
                        final String textet= et.getText().toString();
                        if(textet.isEmpty() || textet.length()<4){
                            Toast.makeText(getApplicationContext(), "아이디는 4자리 이상 설정해주세요", Toast.LENGTH_LONG).show();
                            et.requestFocus();
                        }else{
                            new Thread(new Runnable() {
                                public void run() {
                                    idchecknumber = 0;
                                    runnningCheckThread(textet);
                                }
                            }).start();
                        }
                    }
                }
        );

        Button register_button =(Button)findViewById(R.id.register_action_button);
        register_button.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    if(idchecknumber==1){
                        EditText et = (EditText)findViewById(R.id.check_id);
                        EditText pw_et = (EditText)findViewById(R.id.password);
                        EditText pw_et_check = (EditText)findViewById(R.id.password_confirm);
                        EditText name_et = (EditText)findViewById(R.id.nameedt);

                        final String textid= et.getText().toString();
                        final String textpw_et= pw_et.getText().toString();
                        final String textname_et= name_et.getText().toString();
                        final String textpw_et_check = pw_et_check.getText().toString();

                        if(!textpw_et.isEmpty()) {
                            if (!textpw_et.equals(textpw_et_check)) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다!", Toast.LENGTH_LONG).show();
                            } else {
                                if(!textname_et.isEmpty()){
                                    new Thread(new Runnable() {
                                        public void run() {
                                            runnningThread(textid, textpw_et, textname_et);
                                        }
                                    }).start();
                                }else{
                                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요!", Toast.LENGTH_LONG).show();
                                    name_et.requestFocus();
                                }
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show();
                            pw_et.requestFocus();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디 체크를 해주세요!", Toast.LENGTH_LONG).show();
                    }

                }
            }
        );
    }


    public void runnningCheckThread(final String textid){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/checkid?id=\""+textid+"\"");
            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            String jsonstring = new String(bText);
            JSONObject json = new JSONObject(jsonstring);
            final String checkid_result = json.getString("checkid");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(checkid_result.equals("good")) {
                        idchecknumber = 1;
                        Toast.makeText(getApplicationContext(), "중복된 아이디가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }else{
                        idchecknumber = 2;
                        Toast.makeText(getApplicationContext(), "중복된 아이디가 존재합니다. 재설정 해주세요", Toast.LENGTH_LONG).show();
                    }
                }
            });
            isText.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void runnningThread(final String textet,final String password,final String nameedt){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/regi?id="+textet+"&pw="+password+"&name="+nameedt+"");
            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            final String jsonstring = new String(bText);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "가입이 완료되었습니다!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            isText.close();
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


