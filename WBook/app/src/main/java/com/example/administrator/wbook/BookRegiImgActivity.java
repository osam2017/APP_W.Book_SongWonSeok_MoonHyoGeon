package com.example.administrator.wbook;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by sws28 on 2017-10-25.
 */

public class BookRegiImgActivity extends Activity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    final static int SELECT_IMAGE = 1;
    Bitmap bitmap =null;
    Uri image=null;
    String filePath;
    String fileName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_regi);


//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            } else {
//                 ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//            }
//        }else{
//            //initLayout();
//        }

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        Toast.makeText(this, permissionCheck+"   "+permissionCheck2, Toast.LENGTH_SHORT).show();
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
        Button sendtButton = (Button)findViewById(R.id.button10);
        sendtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //File fe = new File(filePath);
                //Toast.makeText(getApplicationContext(), fe.getName().toString(), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    public void run() {
                        uses();
                    }
                }).start();

            }
        });


        //=========================================================================
    }


    //=============================================================================등록 결과값
    //너무 큰 사진 이미지는 OutOfMemory가 발생할수도 있음(이미지 최적화, 메모리 관리가 필요)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if(resultCode == RESULT_OK && requestCode == SELECT_IMAGE){
            image = intent.getData();
            Uri uri=getRealPathUri(intent.getData());
            filePath = uri.toString();
            fileName = uri.getLastPathSegment();
            try {
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

    public void uses(){
        try {
            AndroidUploader uploader = new AndroidUploader("user3", "userPwd434344");
            String path = filePath;
            //Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();
            uploader.uploadPicture(path);
        } catch (Exception e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }
}
