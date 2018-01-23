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

    public void runnningThread(final String id,final String pw){
        ConnectionTemplate template = new ConnectionTemplate();
        template.runContext(new Strategy() {

            @Override
            public String urlStrategy() {
                return "/logincheck?id=\""+id+"\"&pw=\""+pw+"\"";
            }

            @Override
            public void uiStrategy(final JSONObject jsonobject) throws JSONException{

                final String sendid = jsonobject.getString("id");
                final String sendnum = jsonobject.getString("num") ;
                final String result = jsonobject.getString("result") ;

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
            }
        });
    }
}

