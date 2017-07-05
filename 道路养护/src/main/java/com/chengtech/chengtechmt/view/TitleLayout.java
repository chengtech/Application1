package com.chengtech.chengtechmt.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;

public class TitleLayout extends LinearLayout {
    private Context mContext;
    private TextView title_tv;
    private ImageButton upload;

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            mContext = context;
        }
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.title_layout, this);
        title_tv = (TextView) view.findViewById(R.id.title_name);
        upload = (ImageButton) view.findViewById(R.id.title_upload);
        ImageButton imageButton = (ImageButton) view
                .findViewById(R.id.title_back);
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });


    }

    public void setTitle(String mTitle) {
        title_tv.setText(mTitle);
    }

    public ImageButton getUpload() {
        return upload;
    }

    public void showUploadButton() {
        upload.setVisibility(View.VISIBLE);
    }

    public void dismissUploadButton() {
        upload.setVisibility(View.GONE);
    }

    public String getTitle() {
        return title_tv.getText().toString();
    }


}
