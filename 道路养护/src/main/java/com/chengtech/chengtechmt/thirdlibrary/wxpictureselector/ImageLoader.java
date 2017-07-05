package com.chengtech.chengtechmt.thirdlibrary.wxpictureselector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ImageLoader {

    public static ImageLoader mInstance;
    public class ImageBeanHolder{
        ImageView imageView;
        String path;
        Bitmap bitmap;
    }

    public Semaphore threadHandlerSemaphore = new Semaphore(0);
    public Semaphore threadPoolSemaphore;
    public class ImageSize{
        int width;
        int height;
    }

    /**
     * 图片的缓存
     */
    private LruCache<String, Bitmap> lruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;  //线程数量

    /**
     * 队列的方式
     */
    private Type type = Type.LIFO;

    /**
     * 后台轮训线程
     */
    private Thread mPoolThread ;
    private Handler mPoolThreadHandler;  //通知线程去获取任务的handler
    private Handler mUIHandler;     //通知ui更新的handler

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    public enum Type {
        LIFO, FIFO,
    }

    private ImageLoader(int threadCount ,Type type) {
        init(threadCount,type);
    }

    private void init(int threadCount, Type type) {

        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        //通知线程池取任务执行
                        try {
                            threadPoolSemaphore.acquire();
                            mThreadPool.execute(getTask());
                        } catch (InterruptedException e) {
                        }
                    }
                };
                threadHandlerSemaphore.release();
                Looper.loop();
            }
        };

        mPoolThread.start();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory /8;
        lruCache = new LruCache<String,Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        this.type = type;
        threadPoolSemaphore = new Semaphore(threadCount);
    }



    public static ImageLoader getmInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }

        return mInstance;
    }

    public static ImageLoader getmInstance(int threadCount,Type type) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(threadCount,type);
                }
            }
        }

        return mInstance;
    }

    //从任务队列中获取任务
    private Runnable getTask() {
        if(type== Type.LIFO) {
            return mTaskQueue.removeLast();
        }else if (type== Type.FIFO ){
            return mTaskQueue.removeFirst();
        }
        return null;
    }

    /**
     * 加载图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);

        if (mUIHandler ==null) {
            mUIHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                   ImageBeanHolder holder= (ImageBeanHolder) msg.obj;
                    ImageView imageview = holder.imageView;
                    String path = holder.path;
                    Bitmap bitmap = holder.bitmap;
                    if (imageview.getTag().toString().equals(path)) {
                        imageview.setImageBitmap(bitmap);
                    }
                }
            };
        }
        
        Bitmap bm = getBitmapFromLruCache(path);
        if (bm!=null) {
            refreshImageView(path, imageView, bm);
        }else {
            addTask(new Runnable(){
                @Override
                public void run() {
                    //加载图片
                    //1.根据控件获取图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //压缩图片
                    Bitmap bitmap = compressBitmapFromPath(path,imageSize.width,imageSize.height);
                    //把bitmap添加到lurcache
                    addBitmapToLurCache(bitmap,path);
                    //加载图片到imageview上
                    refreshImageView(path, imageView, bitmap);
                    threadPoolSemaphore.release();
                }
            });
        }
    }

    //更新控件
    private void refreshImageView(String path, ImageView imageView, Bitmap bm) {
        Message msg = Message.obtain();
        ImageBeanHolder holder=  new ImageBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.path =path;
        msg.obj = holder;
        mUIHandler.sendMessage(msg);
    }

    /**
     * 把图片加到缓存中
     * @param bitmap
     * @param path
     */
    private void addBitmapToLurCache(Bitmap bitmap, String path) {
        if (getBitmapFromLruCache(path) ==null) {
            if (bitmap!=null) {
                lruCache.put(path,bitmap);
            }
        }
    }

    /**
     * 压缩图片
     * @param path
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap compressBitmapFromPath(String path, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width >reqWidth || height> reqHeight) {
            int widthRadio = Math.round(width *1.0f / reqWidth);
            int heightRadio = Math.round(height *1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio,heightRadio);
        }
        return inSampleSize;
    }

    //获取控件的宽和高
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();
        int width = imageView.getWidth();//获取控件的宽
        if (width<0) {
            width = lp.width;  //获取控件在layout中申明的宽度
        }
        if (width <0) {
            width = imageView.getMaxWidth();
        }
        if (width <0) {
            width = metrics.widthPixels;
        }

        int height = imageView.getHeight();
        if (height<0 ){
            height = lp.height;
        }
        if (height < 0) {
            height = imageView.getMaxHeight();
        }
        if (height < 0) {
            height = metrics.heightPixels;
        }

        imageSize.width =  width;
        imageSize.height = height;
        return imageSize;
    }

    /**
     * 添加任务到任务队列，同时通知后台轮训线程让线程池获取任务
     * @param runnable
     */
    private void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if (mPoolThreadHandler==null) {
                threadHandlerSemaphore.acquire();
            }
        } catch (InterruptedException e) {
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    private Bitmap getBitmapFromLruCache(String key) {

        return lruCache.get(key);
    }
}
