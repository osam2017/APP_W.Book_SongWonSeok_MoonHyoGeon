package com.example.administrator.wbook;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017-10-17.
 */

public class RelativeServer{

    public static HttpURLConnection connectionReturn(URL url) throws IOException{
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10*1000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        return conn;
    }

}
