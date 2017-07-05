package com.chengtech.chengtechmt.thirdlibrary.wxpictureselector;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.chengtech.chengtechmt.R;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class ListDirPopupWindow extends PopupWindow {

    private int width;
    private int height;
    private View mContentView;
    private List<FloderBean> mData;
    private ListView listView;
    private DirListAdapter adapter;
    public interface OnSelectDirListener {
        void onSelected(FloderBean floderBean);
    }

    private OnSelectDirListener myOnSelectDirListener;

    public ListDirPopupWindow(Context context, List<FloderBean> mData) {
        this.mData = mData;
        calWidthAndHeight(context);
        mContentView = LayoutInflater.from(context).inflate(R.layout.popup_main,null);

        setWidth(width);
        setHeight(height);
        setContentView(mContentView);
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.DirPopupWindowAnimation);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView(context);
        initEvent(context);
    }

    private void initEvent(Context context) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myOnSelectDirListener!=null) {
                    myOnSelectDirListener.onSelected(mData.get(position));
                }
            }
        });

    }

    public void setOnSelectDirListener(OnSelectDirListener listener) {
        this.myOnSelectDirListener = listener;
    }
    private void initView(Context context) {
        listView = (ListView) mContentView.findViewById(R.id.popup_listView);
        adapter = new DirListAdapter(context,mData);
        listView.setAdapter(adapter);

    }

    private void calWidthAndHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = (int) (displayMetrics.heightPixels*0.7);
    }

    private class DirListAdapter extends ArrayAdapter<FloderBean> {

        private Context mContext;

        public DirListAdapter(Context context,  List<FloderBean> objects) {
            super(context,0, objects);
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView==null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popup_listview, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.img = (ImageView) convertView.findViewById(R.id.item_dir_image);
                viewHolder.name_tv = (TextView) convertView.findViewById(R.id.item_dir_name);
                viewHolder.count_tv = (TextView) convertView.findViewById(R.id.item_dir_count);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.img.setImageResource(R.mipmap.pictures_no);
            FloderBean floderBean = getItem(position);
            ImageLoader.getmInstance().loadImage(floderBean.getFirstImgPath(), viewHolder.img);
            viewHolder.name_tv.setText(floderBean.getName());
            viewHolder.count_tv.setText(floderBean.getCount()+"");

            return convertView;
        }

        private class ViewHolder{
            ImageView img;
            TextView name_tv,count_tv;
        }
    }
}
