package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class RecommActivity extends Activity {
    ArrayList<Book> lastlist;
    String id = null;
    String num = null;
    ArrayList<String> items=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomm);

        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id");
        final String loginnum = getIntent().getExtras().getString("num");

        new Thread(new Runnable() {
            public void run() {
                runnningThread(loginid,loginnum);
            }
        }).start();

    }
    public void runnningThread(String id,String num){
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
            //==============================================================================
            JSONArray jsnobject = new JSONArray(modistring);
            ArrayList<Book> articlelist =new ArrayList<Book>();


            for (int i = 0; i < jsnobject.length(); i++){

                JSONObject obj = jsnobject.getJSONObject(i);
                Book articledata = new Book();
                articledata.setNum(Integer.parseInt(obj.getString("num")));
                articledata.setTitle( obj.getString("title"));
                articlelist.add(articledata);

            }
            lastlist = articlelist;
            final String nullcheck=lastlist.get(0).getTitle();
            //==============================================================================
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!nullcheck.equals("not")) {
                        items = new ArrayList<String>();
                        for (int i = 0; i < lastlist.size(); i++) {
                            items.add(lastlist.get(i).getTitle());
                        }
                        ArrayAdapter customAdapter = new ArrayAdapter(getBaseContext(), simple_list_item_1, items);
                        ListView listview = (ListView) findViewById(R.id.reommlist);
                        listview.setAdapter(customAdapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int num_bk = lastlist.get(position).getNum();
                                Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                                intent.putExtra("num_bk", num_bk);
                                intent.putExtra("id_pf", id_pf);
                                intent.putExtra("num_pf", num_pf);
                                startActivity(intent);
                            }
                        });
                    }else{
                        items = new ArrayList<String>();
                        items.add("항목이 없습니다.");
                    }
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
