package com.chengtech.chengtechmt.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.impl.OnItemClickListener;
import com.chengtech.chengtechmt.util.CommonUtils;

import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/6/22 14:50.
 */
public class RecycleViewAdapter2 extends RecyclerView.Adapter<RecycleViewAdapter2.MyViewHolder> {
    private Context mContext;
    private List<String[]> data;
    private LayoutInflater inflater;
    public OnItemClickListener onItemClickListener;
    public int item_res;
    public boolean isShow = false; //是否显示下来箭头

    public int hideLayoutHeight;
    public List<String[]> planData;


    public RecycleViewAdapter2(Context context, List<String[]> data, int item_res) {
        this.mContext = context;
        this.data = data;
        this.item_res = item_res;
        inflater = LayoutInflater.from(context);
//        holderMap = new HashMap<>();
        float denisty = context.getResources().getDisplayMetrics().density;
        hideLayoutHeight = (int) (denisty * 100 + 0.5);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(item_res, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String[] titleArray = data.get(position);
//        List<TextView> tvs = holderMap.get(holder);
//        for (int i = 0; i < titleArray.length; i++) {
//            tvs.get(i).setText(Html.fromHtml(titleArray[i]));
//            tvs.get(i).setVisibility(View.VISIBLE);
//        }
        holder.count.setText(String.valueOf(position+1));
//        Log.i("tag", CommonUtils.px2dp(mContext,((int)holder.count.getTextSize()))+"aaaa");
//        if (position>=100) {
//            holder.count.setTextSize(12);
//        }
//        else{}
        StringBuffer sb= new StringBuffer();
        holder.tv1.setText(titleArray[0]);
        holder.tv1.setVisibility(View.VISIBLE);
        for (int i = 1; i < titleArray.length; i++) {
            sb.append(titleArray[i]);
        }
        holder.tv2.setText(Html.fromHtml(sb.toString()));
        holder.tv2.setVisibility(View.VISIBLE);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_vector_ellipsis);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        holder.tv2.setCompoundDrawables(null,null,drawable,null);

        if (isShow && planData != null) {
            //添加下拉后的内容的布局文件
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            View contentView = inflater.inflate(R.layout.item_planinside, null, false);
//            TextView planLayout = (TextView) contentView.findViewById(R.id.plan_layout);
//            TextView plan_tv = (TextView) contentView.findViewById(R.id.plan_tv);
            holder.plan_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
//            TextView add_tv = (TextView) contentView.findViewById(R.id.add_tv);
//            TextView addLayout = (TextView) contentView.findViewById(R.id.add_layout);
            holder.add_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.planLayout.setText("");
            holder.addLayout.setText("");
            for (int i = 0; i < planData.get(0).length; i++) {
                if (i < planData.get(0).length/2) {
                    String s = holder.planLayout.getText().toString();
                    s += planData.get(position)[i];
                    holder.planLayout.setText(s + "\t\t\t");
                } else {
                    String s = holder.addLayout.getText().toString();
                    s += planData.get(position)[i];
                    holder.addLayout.setText(s + "\t\t\t");
                }
            }

            Log.i("tag",holder.planLayout.getText().toString()+":"+position);
            Log.i("tag",holder.addLayout.getText().toString()+":"+position);


            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.hideLayout.getVisibility() == View.GONE) {
                        holder.hideLayout.setVisibility(View.VISIBLE);
                        int height = holder.hideLayout.getHeight();
                        ValueAnimator anima = createDropAnimator(holder.hideLayout, 0, hideLayoutHeight);
                        anima.start();
                        RotateAnimation animation = new RotateAnimation(0, 180,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                0.5f);
                        animation.setFillAfter(true);
                        animation.setDuration(100);
                        v.startAnimation(animation);
                    } else {
                        int height = holder.hideLayout.getHeight();
                        ValueAnimator anima = createDropAnimator(holder.hideLayout, height, 0);
                        anima.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holder.hideLayout.setVisibility(View.GONE);
                            }
                        });
                        anima.start();

                        RotateAnimation animation = new RotateAnimation(180, 0,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                                0.5f);
                        animation.setFillAfter(true);
                        animation.setDuration(100);
                        v.startAnimation(animation);
                    }
                }
            });
        }

        holder.viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    public void setPlanData(List<String[]> data) {
        planData = data;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void showMoreFlag(boolean isShow) {
        this.isShow = isShow;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7,count;
        public View viewGroup;
        public ImageView imageView;
        public LinearLayout hideLayout;
        public TextView planLayout;
        public TextView plan_tv;
        public TextView add_tv;
        public TextView addLayout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.viewGroup = itemView;
            tv1 = (TextView) itemView.findViewById(R.id.title);
            tv2 = (TextView) itemView.findViewById(R.id.subtitle);
            count = (TextView) itemView.findViewById(R.id.count);
            imageView = (ImageView) itemView.findViewById(R.id.down_arraow);
            hideLayout = (LinearLayout) itemView.findViewById(R.id.hideLayout);
            planLayout = (TextView) itemView.findViewById(R.id.plan_layout);
            plan_tv = (TextView) itemView.findViewById(R.id.plan_tv);
            add_tv = (TextView) itemView.findViewById(R.id.add_tv);
            addLayout = (TextView) itemView.findViewById(R.id.add_layout);
        }
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

}



