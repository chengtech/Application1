package com.chengtech.chengtechmt.activity.dbm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.chengtech.chengtechmt.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class OnePictureDisplayActivity extends Activity {
    PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_picture_display);

        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Picasso.with(this).load(url).placeholder(R.mipmap.placeholder).error(R.mipmap.placeholder2).into(imageView);
        photoViewAttacher = new PhotoViewAttacher(imageView);
//        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                onBackPressed();
//            }
//        });
    }
}
