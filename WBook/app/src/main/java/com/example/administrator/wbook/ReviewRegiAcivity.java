package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Created by Administrator on 2017-10-19.
 */

public class ReviewRegiAcivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_regi);
        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id_pf");
        final String loginnum = getIntent().getExtras().getString("num_pf");
        final int num_bk = getIntent().getExtras().getInt("num_bk");

        Button save_button =(Button)findViewById(R.id.save_button);
        save_button.setOnClickListener(
            new Button.OnClickListener() {
                public void onClick(View v) {
                    EditText isbntitle = (EditText)findViewById(R.id.write_article_editText_title);
                    final String texttitle= isbntitle.getText().toString();
                            new Thread(new Runnable() {
                                public void run() {
                                    EditText isbncontent = (EditText)findViewById(R.id.write_article_editText_content);
                                    final String textcontent= isbncontent.getText().toString();
                                    runnningSaveThread(num_bk, loginnum, loginid, texttitle, textcontent);
                                }
                            }).start();
                }
            }
        );
    }



    public void runnningSaveThread(final int num_bk,final String loginnum,final String loginid, String texttitle, String textcontent){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+
                    "/reviewsave?num_pf_s="+loginnum+"&num_bk_s="+num_bk+"&texttitle="+texttitle+"&textcontent="+textcontent);

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
            final String sendid = json.getString("checkdata");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "저장이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id_pf", sendid);
                    intent.putExtra("num_pf", loginnum);
                    intent.putExtra("num_bk", num_bk);
                    startActivity(intent);
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
