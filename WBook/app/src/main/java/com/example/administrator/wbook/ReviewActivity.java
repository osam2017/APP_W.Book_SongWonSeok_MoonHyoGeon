package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class ReviewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final String loginid = getIntent().getExtras().getString("id_pf");
        final String loginnum = getIntent().getExtras().getString("num_pf");
        final int num_bk = getIntent().getExtras().getInt("num_bk");
        final int num_review = getIntent().getExtras().getInt("num_review");
        final int num_pf_s = getIntent().getExtras().getInt("num_pf_s");
        new Thread(new Runnable() {
            public void run() {
                runnningThread(num_review, num_pf_s);
            }
        }).start();


        Button bt=(Button)findViewById(R.id.button4);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void runnningThread(final int num_review,final int num_pf_s){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/reviewview?num_review="+num_review+"&num_pf_s="+num_pf_s);
            //Log.i(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/reviewview?num_review="+num_review+"&num_pf_s="+num_pf_s,"            aaaaaaaaa");
            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2051];
            int readSize = isText.read(bText);
            final String modistring = new String(bText);
            isText.close();
            conn.disconnect();

            JSONObject json = new JSONObject(modistring);
            final String retitle = json.getString("title");
            final String recontent = json.getString("content") ;
            final String rename = json.getString("name") ;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView writer =(TextView)findViewById(R.id.textView7);
                    writer.setText(rename);
                    TextView title =(TextView)findViewById(R.id.textView5);
                    title.setText(retitle);
                    TextView content =(TextView)findViewById(R.id.textView4);
                    content.setText(recontent);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
