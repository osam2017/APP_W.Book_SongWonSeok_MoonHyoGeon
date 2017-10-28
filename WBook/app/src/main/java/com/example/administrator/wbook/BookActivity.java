package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class BookActivity extends Activity{
    ArrayList<String> items=null;
    ArrayList<Book> lastlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

//        ImageView imageView1 = (ImageView) findViewById(R.id.imageView) ;
//        imageView1.setImageResource(R.drawable.book_icon) ;
//
        Intent intent = getIntent();

        final String loginid = getIntent().getExtras().getString("id_pf");
        final String loginnum = getIntent().getExtras().getString("num_pf");
        final int num_bk = getIntent().getExtras().getInt("num_bk");

        final FileDownloader fileDownloader = new FileDownloader(getApplicationContext());

        //===============================책내용 불러오기
        new Thread(new Runnable() {
            public void run() {
                runnningThread(num_bk,fileDownloader);
            }
        }).start();
        //==============================
        //===============================책기록장 리스트 불러오기
        new Thread(new Runnable() {
            public void run() {
                runnningListThread(num_bk,loginnum,loginid);
            }
        }).start();
        //==============================
        Button like_button =(Button)findViewById(R.id.like_button2);
        like_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            public void run() {
                                runnningLikeThread(num_bk,loginid,loginnum);
                            }
                        }).start();
                    }
                }
        );

        Button regibtn =(Button)findViewById(R.id.button3);
        regibtn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ReviewRegiAcivity.class);
                        intent.putExtra("num_bk", num_bk);
                        intent.putExtra("id_pf", loginid);
                        intent.putExtra("num_pf", loginnum);
                        startActivity(intent);
                    }
                }
        );
    }
    //===================================================================================================책내용
    public void runnningThread(final int num_bk,final FileDownloader fileDownloader){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/book?num_bk="+num_bk);

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
            final String title = json.getString("title");
            final String isbn = json.getString("isbn") ;
            final String imageUrl = json.getString("imageUrl") ;
            final String likenum = json.getString("likenum") ;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fileDownloader.downFile(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/image/"+imageUrl, imageUrl);
                    ImageView imageView = (ImageView)findViewById(R.id.imageView);
                    String img_path = getApplicationContext().getFilesDir().getPath() + "/" + imageUrl;

                    File img_load_path = new File(img_path);
                    //Toast.makeText(getApplicationContext(), img_load_path.toString(), Toast.LENGTH_LONG).show();
                    if(img_load_path.exists()){
                        Bitmap bitmap = BitmapFactory.decodeFile(img_path);
                        imageView.setImageBitmap(bitmap);
                    }

                    TextView titletv=(TextView)findViewById(R.id.textView6);
                    titletv.setText(title);
                    TextView isbntv=(TextView)findViewById(R.id.textView9);
                    isbntv.setText(isbn);
                    TextView liketv=(TextView)findViewById(R.id.textView10);
                    liketv.setText(likenum);
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


    //=======================================================================================================리스트
    public void runnningListThread(final int num_bk,final String num_pf,final String loginid){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/reviewlist?num_bk=\""+num_bk+"\"");

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
                    items = new ArrayList<String>() ;
                    for (int i = 0; i < lastlist.size(); i++){
                        items.add(lastlist.get(i).getTitle());
                    }

                    ArrayAdapter customAdapter =new ArrayAdapter(getBaseContext(), simple_list_item_1, items);
                    ListView listview = (ListView) findViewById(R.id.main_listView) ;
                    listview.setAdapter(customAdapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            int num_review = lastlist.get(position).getNum();
                            int num_pf_s = lastlist.get(position).getNum_pf_s();

                            Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);

                            intent.putExtra("num_pf_s", num_pf_s);
                            intent.putExtra("num_review", num_review);
                            intent.putExtra("num_bk", num_bk);
                            intent.putExtra("id_pf", loginid);
                            intent.putExtra("num_pf", num_pf);
                            startActivity(intent);
                        }
                    });
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


    //=======================================================================================================좋아요
    public void runnningLikeThread(final int num_bk, final String loginid, final String loginnum){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        Boolean result=null;

        try {
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/likebk?num_bk="+num_bk+"&num_pf="+loginnum);
            //Log.i(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/likebk?num_bk="+num_bk+"&num_pf="+loginnum," aaaaaaaaaa");
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                    intent.putExtra("num_bk", num_bk);
                    intent.putExtra("id_pf", loginid);
                    intent.putExtra("num_pf", loginnum);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}