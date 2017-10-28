package com.example.administrator.wbook;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by OwenSong on 2017-10-19.
 */

public class BookRegiActivity extends Activity {
    final static int SELECT_IMAGE = 1;
    int isbncehck=0;
    int imagecheck=0;
    Bitmap bitmap =null;
    Uri image=null;
    String filePath;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_regi);
        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id");
        final String loginnum = getIntent().getExtras().getString("num");

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView2) ;
        imageView1.setImageResource(R.mipmap.ic_launcher) ;

        //=========================================================================내부 카메라 구해오기
        Button selectButton = (Button)findViewById(R.id.button5);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri= MediaStore.Images.Media.INTERNAL_CONTENT_URI;
                Intent intent =new Intent(Intent.ACTION_PICK,uri);
                startActivityForResult(intent, SELECT_IMAGE);

            }
        });
        //=========================================================================
        //=========================================================================ISBN중복체크
        Button idcheck_button =(Button)findViewById(R.id.button4);
        idcheck_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        EditText et = (EditText)findViewById(R.id.editText);
                        et.requestFocus();
                        final String textet= et.getText().toString();
                        if(textet.isEmpty()){
                            Toast.makeText(getApplicationContext(), "ISBN을 입력해주세요", Toast.LENGTH_LONG).show();
                        }else{
                            new Thread(new Runnable() {
                                public void run() {
                                    isbncehck = 0;
                                    runnningCheckThread(textet);
                                }
                            }).start();
                        }
                    }
                }
        );
        //=========================================================================
        //=========================================================================책등록
        Button regi_button =(Button)findViewById(R.id.button10);
        regi_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        EditText et = (EditText)findViewById(R.id.editText2);
                        final String textet= et.getText().toString();

                        EditText isbnet = (EditText)findViewById(R.id.editText);
                        final String textisbnet= isbnet.getText().toString();

                        if(imagecheck==0){
                            Toast.makeText(getApplicationContext(), "이미지를 등록해주세요!", Toast.LENGTH_LONG).show();
                        }
                        if(textet.isEmpty()){
                            Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_LONG).show();
                        }else{
                            if(isbncehck==1 && imagecheck==1) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        runnningBookRegThread(textisbnet,textet,loginid,loginnum);
                                    }
                                }).start();
                            }else{
                                Toast.makeText(getApplicationContext(), "ISBN체크를 해주세요", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
        //=========================================================================
        //=========================================================================돌아가기 버튼
        Button return_button =(Button)findViewById(R.id.return_button);
        return_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        //=========================================================================
    }


    //=============================================================================사진 등록 결과값
    //너무 큰 사진 이미지는 OutOfMemory가 발생할수도 있음(이미지 최적화, 메모리 관리가 필요)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(resultCode == RESULT_OK && requestCode == SELECT_IMAGE){
            image = intent.getData();
            Uri uri=getRealPathUri(intent.getData());
            filePath = uri.toString();
            fileName = uri.getLastPathSegment();
            try {
                imagecheck=1;
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView imageView = (ImageView)findViewById(R.id.imageView2);
            imageView.setImageBitmap(bitmap);
        }
    }
    //실제 uri주소 알아오기
    private Uri getRealPathUri(Uri uri){
        Uri filePathUri = uri;
        if(uri.getScheme().toString().compareTo("content")==0){
            Cursor cursor = getApplicationContext().getContentResolver().query(uri,null,null,null,null);
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePathUri = Uri.parse(cursor.getString(column_index));
            }
        }
        return filePathUri;
    }
    //=============================================================================





    //=============================================================================
    public void runnningCheckThread(final String textisbn){
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jo = null;
        String num=null;

        try {
            //url(http://localhost:1338)
            url = new URL(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/checkisbn?isbn=\""+textisbn+"\"");
            //connection기본설정
            conn = RelativeServer.connectionReturn(url);
            conn.connect();

            //서버에서 받는 데이터
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            String jsonstring = new String(bText);
            JSONObject json = new JSONObject(jsonstring);
            final String checkid_result = json.getString("checkisbn");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(checkid_result.equals("good")) {
                        isbncehck = 1;
                        Toast.makeText(getApplicationContext(), "중복된 ISBN이 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }else{
                        isbncehck = 2;
                        Toast.makeText(getApplicationContext(), "중복된 ISBN이 존재합니다. 재설정 해주세요", Toast.LENGTH_LONG).show();
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
    //=============================================================================




//    //=============================================================================책등록err
//    private static AsyncHttpClient client =  new AsyncHttpClient();
//    public static void uploadArticle(String texttitle,String textisbn, String id,String num ,String filePath, AsyncHttpResponseHandler responseHandler)  {
//        File fe = new File(filePath);
//        RequestParams params = new RequestParams();
//        try {
//            params.put("imageurl", fe,"multipart/form-data",fe.getName());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        params.setForceMultipartEntityContentType(true);
//        params.put("title",texttitle);
//        params.put("isbn",textisbn);
//
//
//        client.post(RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port+"/regibookimg",params,responseHandler);
//    }
//    //===================================================================================

//    //=============================================================================책등록
    public void runnningBookRegThread(final String textisbn, final String title, final String loginid, final String loginnum){
        try {
            AndroidUploader uploader = new AndroidUploader(title, textisbn);
            String path = filePath;
            //Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            uploader.uploadPicture(path);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "등록되었습니다!", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(), BookMenuActivity.class);
//                    intent.putExtra("id", loginid);
//                    intent.putExtra("num", loginnum);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }
//    //=============================================================================
}
