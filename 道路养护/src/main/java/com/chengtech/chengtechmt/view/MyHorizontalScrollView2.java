package com.chengtech.chengtechmt.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.HorizotalScrollAdapter;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2016/11/15 9:05.
 */

public class MyHorizontalScrollView2 extends LinearLayout {
    private Context mContext;
    public RecyclerView nameRV, contentRV;
    private MyHorizontalScrollView titleSV, contentSV;
    private LinearLayout titleLayout;
    private int DEFAULT_WIDTH = 60;
    private int DEFAULT_HEIGHT = 40;
    private List<MyHorizontalScrollView> scrollViewList;
    private boolean isLeftListEnable, isRightListEnable;
    private SparseIntArray sparseIntArray = new SparseIntArray();
    private onItemClickListener onItemClickListener;
    private LinearLayout container;
    private boolean isSetData; //是否设置了数据；

    public interface onItemClickListener {
        public void onClick(View view, int position);
    }


    public MyHorizontalScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_horizontal_scroll, this);
        nameRV = (RecyclerView) view.findViewById(R.id.nameRv);
        contentRV = (RecyclerView) view.findViewById(R.id.contentRv);
        titleSV = (MyHorizontalScrollView) view.findViewById(R.id.titleScrollView);
        contentSV = (MyHorizontalScrollView) view.findViewById(R.id.contentSV);
        titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        scrollViewList = new ArrayList<>();
        scrollViewList.add(titleSV);
        scrollViewList.add(contentSV);
        container = (LinearLayout) view.findViewById(R.id.container);

        combination();
    }

    private void combination() {
        titleSV.SetOnMyScrollViewListener(new MyHorizontalScrollView.OnMyScrollViewListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                for (MyHorizontalScrollView scrollView : scrollViewList) {
                    scrollView.smoothScrollTo(l, t);
                }
            }
        });
        contentSV.SetOnMyScrollViewListener(new MyHorizontalScrollView.OnMyScrollViewListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                for (MyHorizontalScrollView scrollView : scrollViewList) {
                    scrollView.smoothScrollTo(l, t);
                }
            }
        });


        nameRV.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
//        nameRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            //防止死循环，因为双方都设置了对方进行滑动
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCREEN_STATE_ON) {
//                    isRightListEnable = false;
//                    isLeftListEnable = true;
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    isRightListEnable = true;
//                }
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.i("tag","name:"+dy);
//                if (isLeftListEnable)
//                    contentRV.scrollBy(dx,dy);
//            }
//        });
        contentRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCREEN_STATE_ON){
//                    isLeftListEnable = false;
//                    isRightListEnable = true;
//                }else if (newState==RecyclerView.SCROLL_STATE_IDLE) {
//                    isLeftListEnable = true;
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("tag", "content:" + dy);
//                if (isRightListEnable)
                nameRV.scrollBy(dx, dy);
            }
        });

    }

    /**
     * 设置标题
     *
     * @param titles
     */
    public void setTitle(List<String> titles) {
        LinearLayout layout = (LinearLayout) titleSV.getChildAt(0);
        if (titles != null && titles.size() > 0) {
            for (int i = 0; i < titles.size(); i++) {
                if (i == 0) {
                    TextView tv1 = (TextView) titleLayout.getChildAt(0);
                    tv1.setText(titles.get(i));
                } else {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                            CommonUtils.dp2px(mContext, sparseIntArray.get(i) == 0 ? DEFAULT_WIDTH : sparseIntArray.get(i)), ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    TextView textView = new TextView(mContext);
                    textView.setLayoutParams(lp);
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(titles.get(i));
                    textView.setTextColor(Color.WHITE);
//                    int padding = CommonUtils.dp2px(mContext,5);
//                    textView.setPadding(padding,padding,padding,padding);
                    layout.addView(textView);
                }
            }
        }
    }

    public void setData(List<List<String>> data) {
        isSetData = true;
        if (data != null && data.size() > 0)
            setTitle(data.get(0));
        if (data != null && data.size() > 1) {
            //去掉标题栏的list
            data.remove(0);
            setTitleAdapter(data);
            setContentAdapter(data);
        }
    }

    private void setContentAdapter(final List<List<String>> data) {
        contentRV.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(HORIZONTAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT
                        CommonUtils.dp2px(mContext, DEFAULT_HEIGHT)
                );
                linearLayout.setLayoutParams(lp);
//                linearLayout.setMinimumHeight(CommonUtils.dp2px(mContext, 40));
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(linearLayout) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                List<String> content = data.get(position);
                if (content != null && content.size() > 0) {
                    LinearLayout container = (LinearLayout) holder.itemView;
                    if (position % 2 == 0) {
                        container.setBackgroundColor(Color.WHITE);
                    } else {
                        container.setBackgroundColor(Color.parseColor("#F0F0F0"));
                    }
                    for (int i = 1; i < content.size(); i++) {
                        if (container.getChildAt(i) != null) {
                            TextView content_tv = (TextView) container.getChildAt(i - 1);
                            content_tv.setText(content.get(i));
                        } else {
                            final TextView content_tv = new TextView(mContext);
                            ViewGroup.LayoutParams lp = new RecyclerView.LayoutParams(
                                    CommonUtils.dp2px(mContext, sparseIntArray.get(i) == 0 ? DEFAULT_WIDTH : sparseIntArray.get(i))
                                    , ViewGroup.LayoutParams.MATCH_PARENT);
                            content_tv.setGravity(Gravity.CENTER);
                            content_tv.setEllipsize(TextUtils.TruncateAt.END);
                            content_tv.setLayoutParams(lp);
                            content_tv.setTextSize(10);
                            content_tv.setEllipsize(TextUtils.TruncateAt.END);
//                        int padding = CommonUtils.dp2px(mContext,5);
//                        content_tv.setPadding(padding,padding,padding,padding);
                            content_tv.setText(content.get(i));
//                        if (i==2 || i==3) {
//                            title.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Toast.makeText(mContext, title.getText().toString(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
                            content_tv.setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    TextView textview = new TextView(mContext);
                                    textview.setMinHeight(CommonUtils.dp2px(mContext, 100));
                                    textview.setMinWidth(CommonUtils.dp2px(mContext, 100));
                                    textview.setPadding(4, 4, 4, 4);
                                    textview.setGravity(Gravity.CENTER);
                                    textview.setText(content_tv.getText().toString());
                                    builder.setView(textview);
                                    AlertDialog dialog = builder.create();
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.show();
                                    return true;
                                }
                            });
                            container.addView(content_tv);

                        }
                    }

                    container.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null)
                                onItemClickListener.onClick(v, position);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });
        contentRV.setLayoutManager(new LinearLayoutManager(mContext));
    }


    /**
     * 设置固定列的recycleview
     *
     * @param data
     */
    private void setTitleAdapter(final List<List<String>> data) {
        nameRV.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        CommonUtils.dp2px(mContext, DEFAULT_WIDTH),
                        CommonUtils.dp2px(mContext, DEFAULT_HEIGHT)
                );
                linearLayout.setLayoutParams(params);

                TextView textViewCount = new TextView(mContext);
                ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(
                        CommonUtils.dp2px(mContext, 20), CommonUtils.dp2px(mContext, 20)
                );
                textViewCount.setLayoutParams(lp1);
                textViewCount.setGravity(Gravity.CENTER);
                textViewCount.setTextColor(Color.WHITE);
                textViewCount.setTextSize(10);
                textViewCount.setBackgroundResource(R.drawable.small_red_point);
                linearLayout.addView(textViewCount);

                TextView textView = new TextView(mContext);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        CommonUtils.dp2px(mContext, DEFAULT_WIDTH-20), CommonUtils.dp2px(mContext, DEFAULT_HEIGHT)
                );
                textView.setLayoutParams(lp);
//                int padding = CommonUtils.dp2px(mContext,5);
//                textView.setPadding(padding,padding,padding,padding);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(10);
                textView.setMaxLines(1);
                textView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        TextView textview = new TextView(mContext);
                        textview.setMinHeight(CommonUtils.dp2px(mContext, 100));
                        textview.setMinWidth(CommonUtils.dp2px(mContext, 100));
                        textview.setPadding(4, 4, 4, 4);
                        textview.setGravity(Gravity.CENTER);
                        textview.setText(((TextView)view).getText().toString());
                        builder.setView(textview);
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        return true;
                    }
                });
                textView.setEllipsize(TextUtils.TruncateAt.END);
                linearLayout.addView(textView);

                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(linearLayout) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (data.get(position) != null) {
                    ((TextView)((LinearLayout) holder.itemView).getChildAt(0)).setText(String.valueOf(position+1));
                    ((TextView)((LinearLayout) holder.itemView).getChildAt(1)).setText(data.get(position).get(0));
                    if (position % 2 == 0) {
                        ((LinearLayout) holder.itemView).setBackgroundColor(Color.WHITE);
                    } else {
                        ((LinearLayout) holder.itemView).setBackgroundColor(Color.parseColor("#F0F0F0"));
                    }
                }
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        });
        nameRV.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void setRectWidthAndHeight(SparseIntArray sparseIntArray) {
        this.sparseIntArray = sparseIntArray;
    }

    public void setOnItemClickListener(MyHorizontalScrollView2.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public boolean hasData() {
        return isSetData;
    }

    public void removeData() {
        setTitleAdapter(new ArrayList<List<String>>());
        setContentAdapter(new ArrayList<List<String>>());
    }
}
