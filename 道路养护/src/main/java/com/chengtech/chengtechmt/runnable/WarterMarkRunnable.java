package com.chengtech.chengtechmt.runnable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/8/10 16:33.
 */

public class WarterMarkRunnable implements Runnable {
    public static final int WATER_MARK_SUCCESS = 0x99;
    public static final String KEY_IMG_NAME = "picName"; //添加水印后的图片名称
    public static final String KEY_WARTER_MARK_MSG = "waterMarkMsg"; //添加水印的内容，如果分行，内容用；隔开
    public static final String KEY_ORIGINAL_IMG_PATH = "imgPath"; //原图片的地址
    public static final String KEY_EDITED_IMG_PATH_ARRAY = "imgPaths"; //添加水印后图片路径的集合
    public static final String KEY_HANDLER = "handler"; //添加水印后图片路径的集合
    private Context mContext;
    private Map<String, Object> warterMarkParams;

    public WarterMarkRunnable(Context context, Map<String, Object> warterMarkParams) {
        mContext = context;
        this.warterMarkParams = warterMarkParams;
    }

    @Override
    public void run() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile((String) warterMarkParams.get(KEY_ORIGINAL_IMG_PATH), options);
        Log.i("tag",(String) warterMarkParams.get(KEY_ORIGINAL_IMG_PATH));
        Bitmap bitmap2 = createWaterMark(bitmap, (String) warterMarkParams.get(KEY_WARTER_MARK_MSG));
        File mfile = new File(mContext.getExternalCacheDir(), (String) warterMarkParams.get(KEY_IMG_NAME));
        if (mfile.exists()) {
            mfile.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mfile);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            bitmap2.recycle();
            ((List<String>) warterMarkParams.get(KEY_EDITED_IMG_PATH_ARRAY)).add(mContext.getExternalCacheDir() + "/" + warterMarkParams.get(KEY_IMG_NAME));
            ((Handler) warterMarkParams.get(KEY_HANDLER)).sendEmptyMessage(WATER_MARK_SUCCESS);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap createWaterMark(Bitmap bitmap, String mark) {
        String[] split = mark.split(";");
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        //设置水印背景paint
        Paint paint_b = new Paint();
        paint_b.setDither(true);
        paint_b.setFilterBitmap(true);
        paint_b.setColor(Color.BLACK);
        paint_b.setAlpha(150);


        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawRect(0, (float) (newBitmap.getHeight() * (0.75)), newBitmap.getWidth(), newBitmap.getHeight(), paint_b);

        //设置字体paint
        Paint paint_t = new Paint();
        paint_t.setColor(Color.RED);
        int textSize = width / 30;
        paint_t.setTextSize(textSize);
        paint_t.setAntiAlias(true);

        for (int i = 0; i < split.length; i++) {
//            canvas.drawText(split[i], CommonUtils.dp2px(this, textSize), ((float) (newBitmap.getHeight() * (0.75)) + CommonUtils.dp2px(this, textSize + i * textSize+20)), paint_t);
            canvas.drawText(split[i], textSize, ((float) (newBitmap.getHeight() * (0.75)) + textSize + i * textSize + 20), paint_t);
        }
        return newBitmap;
    }
}
