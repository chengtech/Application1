package com.chengtech.chengtechmt.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.ProjectManagementActivity;
import com.chengtech.chengtechmt.activity.dbm.OnePictureDisplayActivity;
import com.chengtech.chengtechmt.picasso.CompressTransFormation;
import com.chengtech.chengtechmt.thirdlibrary.wxpictureselector.PictureSelectorActivity;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.ViewHolder;
import com.chengtech.chengtechmt.view.SquareRelativeLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 作者: LiuFuYingWang on 2017/5/22 18:27.
 */

public class ImageAddAdapter extends RecyclerView.Adapter {
    public static final int SELECT_IMG_RESULT = 0x00;
    public ArrayList<String> picturePaths;
    public Context mContext;
    public int targetWidth;
    private String cameraCachePath;

    public ImageAddAdapter(Context context, ArrayList<String> picturePaths) {
        this.mContext = context;
        this.picturePaths = picturePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        ImageView imageView = new ImageView(parent.getContext());
        View view = LayoutInflater.from(mContext).inflate(R.layout.image_add, parent, false);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150,
//                150);

//        RecyclerView recyclerView = (RecyclerView) parent;
//        int spanCount = ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
//        int itemWidth = parent.getWidth()/3;
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(itemWidth,
//                itemWidth);
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        imageView.setLayoutParams(params);
        RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {

        };
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SquareRelativeLayout parentView = (SquareRelativeLayout) holder.itemView;
        ImageView imageView = (ImageView) parentView.findViewById(R.id.image);
        ImageView delete_iv = (ImageView) parentView.findViewById(R.id.delete);
        if (picturePaths.size() == 0) {

            imageView.setImageURI(null);
            imageView.setBackgroundResource(R.drawable.ic_add_picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = showSelectDialog(mContext);
                    bottomSheetDialog.show();
                }
            });
            delete_iv.setVisibility(View.GONE);
        } else if (position < getItemCount() - 1) {
            ViewGroup.LayoutParams lp = imageView.getLayoutParams();
            DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();

            int width = imageView.getWidth();//获取控件的宽
            if (width < 0) {
                width = lp.width;  //获取控件在layout中申明的宽度
            }
            if (width < 0) {
                width = imageView.getMaxWidth();
            }
            if (width < 0) {
                width = metrics.widthPixels;
            }
            if (width != 0)
                targetWidth = width;
            String picPath = picturePaths.get(position);
            imageView.setTag(picPath);
            Picasso.with(mContext).load(new File(picPath)).error(R.mipmap.placeholder2).transform(new CompressTransFormation(targetWidth)).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String picPath2 = (String) v.getTag();
                    Intent intent = new Intent(mContext, OnePictureDisplayActivity.class);
                    intent.putExtra("url", picPath2);
                    Activity activity = (Activity) mContext;
                    activity.startActivity(intent);
                }
            });
            delete_iv.setVisibility(View.VISIBLE);
            delete_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picturePaths.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, picturePaths.size());
                }
            });
        } else {
            imageView.setImageURI(null);
            imageView.setBackgroundResource(R.drawable.ic_add_picture);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = showSelectDialog(mContext);
                    bottomSheetDialog.show();

                }
            });
            delete_iv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 1 + picturePaths.size();
    }

    private BottomSheetDialog showSelectDialog(final Context context) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.sheetdialog_carmera_or_gallery, null, false);
        TextView camera_tv = (TextView) contentView.findViewById(R.id.camera);
        TextView album_tv = (TextView) contentView.findViewById(R.id.album);
        TextView cancel_tv = (TextView) contentView.findViewById(R.id.cancel);
        camera_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCameraCachePath(CommonUtils.camera(context));
                bottomSheetDialog.dismiss();
            }
        });
        album_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PictureSelectorActivity.class);
                Activity activity = (Activity) mContext;
                activity.startActivityForResult(intent, SELECT_IMG_RESULT);
                bottomSheetDialog.dismiss();
            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(contentView);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        return bottomSheetDialog;
    }


    public String getCameraCachePath() {
        return cameraCachePath;
    }

    public void setCameraCachePath(String cameraCachePath) {
        this.cameraCachePath = cameraCachePath;
    }
}
