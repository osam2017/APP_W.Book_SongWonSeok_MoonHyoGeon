package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
 * Created by Administrator on 2017-10-19.
 */

public class MyBookActivity extends Activity {
    ArrayList<Book> lastlist;
    ArrayList<String> items=null;
    String sendloginid=null;
    String sendloginnum=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybook);

        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id");
        final String loginnum = getIntent().getExtras().getString("num");
        sendloginid=loginid;
        sendloginnum=loginnum;
        new Thread(new Runnable() {
            public void run() {
                runnningThread(loginid,loginnum);
            }
        }).start();

    }

    public void runnningThread(final String loginid, final String loginnum){
        final String id_pf = loginid;
        final String num_pf = loginnum;

        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/myreviewlist?loginnum="+num_pf);

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
                articledata.setNum_pf_s(Integer.parseInt(obj.getString("num_pf_s")));
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
                        ListView listview = (ListView) findViewById(R.id.myrevielist);
                        listview.setAdapter(customAdapter);
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int num_bk = lastlist.get(position).getNum();
                                Intent intent = new Intent(getApplicationContext(), MyReviewActivity.class);
                                int num_review = lastlist.get(position).getNum();
                                int num_pf_s = lastlist.get(position).getNum_pf_s();

                                intent.putExtra("num_pf_s", num_pf_s);
                                intent.putExtra("num_review", num_review);
                                intent.putExtra("num_bk", num_bk);
                                intent.putExtra("id_pf", loginid);
                                intent.putExtra("num_pf", num_pf);

                                startActivity(intent);
                            }
                        });
                        registerForContextMenu(listview);

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

    //================================================================================================popupmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);

    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position; //AdapterView안에서 ContextMenu를 보여즈는 항목의 위치
        switch( item.getItemId() ){
            case R.id.delete:
                Toast.makeText(this, items.get(index)+" Delete", Toast.LENGTH_SHORT).show();
                final int num_bk = lastlist.get(index).getNum();
                new Thread(new Runnable() {
                    public void run() {
                        runnningDeleteThread(num_bk);
                    }
                }).start();
                break;
        }
        return true;
    };
    //================================================================================================popupmenu
    //===============================================================================================================================================================================
    public void runnningDeleteThread(int num_bk){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/deletereview?num_bk=\""+num_bk+"\"");

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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MyBookActivity.class);

                    intent.putExtra("id", sendloginid);
                    intent.putExtra("num", sendloginnum);
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
    //===============================================================================================================================================================================

}
