package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

/**
 * Created by sws28 on 2017-10-23.
 */

public class RecommLoadingActivity extends Activity {
    int idchecknumber=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomm_loading);

        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id");
        final String loginnum = getIntent().getExtras().getString("num");


        new Thread(new Runnable() {
            public void run() {
                runnningThread(loginid,loginnum);
            }
        }).start();

    }
    public void runnningThread(final String id, final String num){
        final String id_pf = id;
        final String num_pf = num;
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/recommendbook?num_pf="+num);


            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            final String modistring = new String(bText);
            isText.close();
            conn.disconnect();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(),RecommActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("num", num);
                    intent.putExtra("jsonstring", modistring);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}