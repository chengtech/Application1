package com.chengtech.chengtechmt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/11/14 11:52.
 */

public class HorizotalScrollAdapter extends RecyclerView.Adapter<HorizotalScrollAdapter.MyViewHolder> {
    private Context mContext;
    private List<String[]> data;
    private List<MyHorizontalScrollView> scrollViewList;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;


    public HorizotalScrollAdapter(Context context, List<String[]> data) {
        this.mContext = context;
        this.data = data;
        scrollViewList = new ArrayList<>();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        recyclerView = (RecyclerView) parent;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_horizotal_scroll, null, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        String[] row = data.get(position);
        holder.textView.setText(row[0]);
        for (int i = 1; i < row.length; i++) {
            if (holder.container.getChildAt(i - 1) != null) {
                TextView title = (TextView) holder.container.getChildAt(i - 1);
                title.setText(row[i]);
            } else {
                final TextView title = new TextView(mContext);
                ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(CommonUtils.dp2px(mContext, 60)
                        , ViewGroup.LayoutParams.MATCH_PARENT);
                title.setGravity(Gravity.CENTER);
                title.setLayoutParams(lp);
                title.setText(row[i]);
                if (i==2 || i==3) {
                    title.setClickable(true);
                    title.setFocusable(true);
                    Log.i("tag",title.hasFocus()+"");
                    holder.container.requestDisallowInterceptTouchEvent(true);
                    title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, title.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                holder.container.addView(title);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null)
                    onItemClickListener.onClick(holder.itemView,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private void addHView(final MyHorizontalScrollView sv) {
        if (!scrollViewList.isEmpty()) {
            int size = scrollViewList.size();
            MyHorizontalScrollView hscrollView = scrollViewList.get(size - 1);
            final int scrollX = hscrollView.getScrollX();
            if (scrollX != 0) {
                recyclerView.post(new Runnable() {

                    @Override
                    public void run() {
                        sv.scrollTo(scrollX, 0);

                    }

                });
            }
        }
        sv.SetOnMyScrollViewListener(new MyHorizontalScrollView.OnMyScrollViewListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                for (MyHorizontalScrollView scrollView : scrollViewList) {
                    scrollView.smoothScrollTo(l, t);
                }
            }
        });
        sv.SetOnMyClickListener(new MyHorizontalScrollView.OnMyClickListener() {
            @Override
            public void onMyScrollViewClick() {
                Toast.makeText(mContext, "fffffffffff", Toast.LENGTH_SHORT).show();
            }
        });
        scrollViewList.add(sv);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private LinearLayout container;
        private MyHorizontalScrollView scrollView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            scrollView = (MyHorizontalScrollView) itemView.findViewById(R.id.scrollView);
            addHView(scrollView);
        }
    }

    public interface OnItemClickListener{
        public void onClick(View parent,int position);
    }
}
