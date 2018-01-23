package com.example.administrator.wbook;


import android.content.Intent;
import android.util.Log;
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
 * Created by sws28 on 2018-01-22.
 */

public class ConnectionTemplate {
    void runContext(Strategy strategy){
        String serveraddress= null;
        String eachfilepath= null;
        URL url = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        JSONObject jsonob =null;

        try {
            serveraddress = RelativeHttp.http_protocol+RelativeHttp.http_local_server+":"+RelativeHttp.port;
            eachfilepath = serveraddress + strategy.urlStrategy();  //전략

            url= new URL(eachfilepath);
            //connection setting
            conn = RelativeServer.connectionReturn(url);
            conn.connect();
            //from server to app
            InputStream isText = conn.getInputStream();
            byte[] bText = new byte[2048];
            int readSize = isText.read(bText);
            String jsonstring = new String(bText);
            isText.close();
            conn.disconnect();

            jsonob=new JSONObject(jsonstring);

            strategy.uiStrategy(jsonob); //전략

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
