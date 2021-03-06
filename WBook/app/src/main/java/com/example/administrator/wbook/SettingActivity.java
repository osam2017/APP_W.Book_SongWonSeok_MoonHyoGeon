package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-10-18.
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        final String loginid = getIntent().getExtras().getString("id");
        final String loginnum = getIntent().getExtras().getString("num");

        Button recom_button =(Button)findViewById(R.id.button6);
        recom_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), RecommLoadingActivity.class);
                        intent.putExtra("id", loginid);
                        intent.putExtra("num", loginnum);
                        startActivity(intent);
                    }
                }
        );


        Button mybook_button =(Button)findViewById(R.id.button7);
        mybook_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), MyBookActivity.class);
                        intent.putExtra("id", loginid);
                        intent.putExtra("num", loginnum);
                        startActivity(intent);
                    }
                }
        );


        Button deverlop_button =(Button)findViewById(R.id.button8);
        deverlop_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DeverloperAcitivity.class);
                        intent.putExtra("id", loginid);
                        intent.putExtra("num", loginnum);
                        startActivity(intent);
                    }
                }
        );


        Button source_button =(Button)findViewById(R.id.button12);
        source_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ResourceActivity.class);
                        intent.putExtra("id", loginid);
                        intent.putExtra("num", loginnum);
                        startActivity(intent);
                    }
                }
        );

        Button back_button =(Button)findViewById(R.id.button9);
        back_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        Button logout_button =(Button)findViewById(R.id.logout_button);
        logout_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
        );
    }
}
