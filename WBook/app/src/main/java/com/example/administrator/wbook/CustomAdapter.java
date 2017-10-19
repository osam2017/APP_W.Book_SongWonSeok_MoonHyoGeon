package com.example.administrator.wbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-10-18.
 */

public class CustomAdapter extends ArrayAdapter<Book> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Book> articleData;

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<Book> articleData){
        super(context, layoutResourceId, articleData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleData = articleData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

        }


        TextView tvTitle = (TextView) row.findViewById(R.id.custom_row_textView1);
     //   TextView tvContent = (TextView) row.findViewById(R.id.custom_row_textView2);


        tvTitle.setText(articleData.get(position).getTitle());
       // tvContent.setText(articleData.get(position).getLikenum());

//        ImageView imageView = (ImageView) row.findViewById(R.id.custom_row_imageView);
//
//        try {
//
//            InputStream is = context.getAssets().open(articleData.get(position).getImgName());
//            Drawable d = Drawable.createFromStream(is, null);
//            imageView.setImageDrawable(d);
//        } catch(IOException e){
//            Log.e("ERROR", "ERROR: " + e);
//        }
        return row;
    }
}
