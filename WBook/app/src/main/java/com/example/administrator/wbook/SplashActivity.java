package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017-10-20.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1 = (ImageView) findViewById(R.id.imageView3) ;
        imageView1.setImageResource(R.drawable.main_icon) ;

        try {
            Thread.sleep(3000);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
