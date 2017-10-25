package com.example.administrator.wbook;

import android.content.Context;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by OwenSong on 2017-10-24.
 */

public class FileDownloader {
    private final Context context;

    public FileDownloader(Context context){
        this.context=context;
    }

    //LoopJ라이브러리
    private static AsyncHttpClient client = new AsyncHttpClient();

    public void downFile(String fileUrl, String fileName){

        final File filePath = new File(context.getFilesDir().getPath()+"/"+fileName);

        if(!filePath.exists()){
            client.get(fileUrl, new FileAsyncHttpResponseHandler(context) {
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    file.renameTo(filePath);
                }
            });
        }
    }
}
