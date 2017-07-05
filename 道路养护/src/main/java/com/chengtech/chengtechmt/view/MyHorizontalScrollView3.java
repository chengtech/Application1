package com.chengtech.chengtechmt.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * 作者: LiuFuYingWang on 2017/4/1 10:59.
 */

public class MyHorizontalScrollView3 extends HorizontalScrollView {
    public Context context;
    public List<List<String>> data = new ArrayList<>();
    public RecyclerView.Adapter adapter;
    public int screen_width;
    public double[] percentage;
    public MyHorizontalScrollView3(Context context) {
        super(context);
        this.context = context;
        screen_width = CommonUtils.getWindowsWidth(context);
        initView();
    }

    public MyHorizontalScrollView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        screen_width = CommonUtils.getWindowsWidth(context);
        int viewWidth = getWidth();
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.WHITE);
        RecyclerView recyclerView = new RecyclerView(context);
        addView(recyclerView);
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RecyclerView.ViewHolder viewHolder = null;
                if (data != null && data.size() > 0) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    linearLayout.setLayoutParams(lp);

                    List<String> list = data.get(0);
                    if (list != null && list.size() > 0) {
                        double defaultRatio = 0.0;
                        if (list.size()>4) {
                            defaultRatio = 0.3;
                        }else {
                            defaultRatio = (1-0.1)/(list.size()-1);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            TextView textView = new TextView(context);
                            if (i==0) {
                                ViewGroup.LayoutParams lp2 = new RecyclerView.LayoutParams((int) (
                                        screen_width * (percentage==null?0.1:(percentage.length>i?percentage[i]:0.1))),
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                                textView.setLayoutParams(lp2);
                            }else {
                                ViewGroup.LayoutParams lp2 = new RecyclerView.LayoutParams((int) (
                                        screen_width * (percentage==null?defaultRatio:(percentage.length>i?percentage[i]:defaultRatio))),
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                                textView.setLayoutParams(lp2);
                            }

//                            textView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                            textView.setTextSize(10);
                            textView.setPadding(10, 10, 10, 10);
                            textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                            linearLayout.addView(textView);
                        }
                        viewHolder = new RecyclerView.ViewHolder(linearLayout) {
                            @Override
                            public String toString() {
                                return super.toString();
                            }
                        };
                    }

                }
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder == null)
                    return;
                List<String> content = data.get(position);
                if (content != null && content.size() > 0) {
                    LinearLayout linearLayout = (LinearLayout) holder.itemView;
                    if (position % 2 ==0) {
                        linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                    }else {
                        linearLayout.setBackgroundColor(Color.parseColor("#E4EEF9"));
                    }
                    for (int i = 0; i < content.size(); i++) {
                        TextView textView = (TextView) linearLayout.getChildAt(i);
                        if (position == 0) {
                            textView.setTextSize(12);
                            textView.setTextColor(Color.parseColor("#F0C285"));
                        } else {
                            textView.setTextSize(10);
                            textView.setTextColor(Color.parseColor("#000000"));
                        }
                        textView.setText(content.get(i));
                    }
                }

            }

            @Override
            public int getItemCount() {
                return data == null ? 0 : data.size();
            }
        };

        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void setData(List<List<String>> data) {
        this.data = data;
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置每个textview的宽度占屏幕的比例，如0.1，表示占屏幕宽度的10%
     */
    public void setPercentage(double[] percentage){
        this.percentage = percentage;

    }


}
