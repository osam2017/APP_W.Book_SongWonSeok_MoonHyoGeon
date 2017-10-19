package com.example.administrator.wbook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017-10-20.
 */

public class DeverloperAcitivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deverloper);
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView4) ;
        imageView1.setImageResource(R.drawable.abc) ;

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView5) ;
        imageView1.setImageResource(R.drawable.ab1) ;

        ImageView imageView3 = (ImageView) findViewById(R.id.imageView7) ;
        imageView1.setImageResource(R.drawable.airforce) ;


    }
}
