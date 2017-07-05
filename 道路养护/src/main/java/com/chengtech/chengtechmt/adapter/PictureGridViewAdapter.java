package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.chengtech.chengtechmt.picasso.RoundeCornerTransformation;
import com.squareup.picasso.Picasso;

/**
 * 作者: LiuFuYingWang on 2016/5/25 15:37.
 */
public class PictureGridViewAdapter extends ArrayAdapter<String> {

    public String[] objects;
    public Context mContext;
    public int resourceId;
    private int windowWidth;

    public PictureGridViewAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.mContext = context;
        resourceId = resource;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        windowWidth = metrics.widthPixels;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
        }

        final ImageView imageView = new ImageView(mContext);
//        final ImageView imageView = ViewHolder.get(convertView, R.id.my_image_view);
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = (windowWidth - 40) / 3;
        params.height = (windowWidth - 40) / 3;
        imageView.setLayoutParams(params);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Bitmap bitmap = Picasso.with(mContext).load(objects[position]).get();
                    Bitmap bitmap = imageView.getDrawingCache();
                    imageView.setDrawingCacheEnabled(false);
//                    Intent intent = new Intent(mContext, OnePictureDisplayActivity.class);
//                    intent.putExtra("fileName",  bitmap);
//                    mContext.startActivity(intent);

            }
        });
        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().
                        getMainDiskStorageCache().getResource(new SimpleCacheKey(Uri.parse(objects[position]).toString()));
                File file = resource.getFile();
//                        String fileName  = file.getName();
//                        fileName = fileName.substring(0,fileName.lastIndexOf("."));
//                try {
//                            File f = new File(getCacheDir(),fileName+".jpg");
//                            FileInputStream input = new FileInputStream(file);
//                            //创建流文件读入与写出类
//                            FileOutputStream outStream = new FileOutputStream(f);
//                            //通过available方法取得流的最大字符数
//                            byte[] inOutb = new byte[input.available()];
//                            input.read(inOutb);  //读入流,保存在byte数组
//                            outStream.write(inOutb);  //写出流,保存在文件newFace.gif中
//                            input.close();
//                            outStream.close();
//

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Intent intent = new Intent(mContext, OnePictureDisplayActivity.class);
                intent.putExtra("fileName", file);
                mContext.startActivity(intent);
            }
        });*/

        if (!TextUtils.isEmpty(objects[position])) {
            Picasso.with(mContext).load(objects[position]).transform(new RoundeCornerTransformation()).into(imageView);

        }
        return convertView;
    }
}


