package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.CapitalSource;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/10/20 15:33.
 * 大中修模块--资金来源adapter
 */

public class CapitalSourceAdapter extends RecyclerView.Adapter {
    private List<CapitalSource> data;
    private Context mContext;

    public CapitalSourceAdapter(List<CapitalSource> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_capital_source, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        CapitalSource capitalSource = data.get(position);
        viewHolder.fileName_tv.setText(capitalSource.referenceNumber);
        viewHolder.money_tv.setText(CommonUtils.getCommalFormat(new BigDecimal(capitalSource.moneyCounts)));
        viewHolder.date_tv.setText(DateUtils.convertDate(capitalSource.capitalSoureDate));
        viewHolder.memo_tv.setText(capitalSource.memo);
        viewHolder.session_tv.setTag(capitalSource.sessionId);
        viewHolder.session_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.getAttachmentsFromSessionId(mContext, (String) v.getTag());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView fileName_tv, money_tv, date_tv, memo_tv, session_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            fileName_tv = (TextView) itemView.findViewById(R.id.fileName);
            money_tv = (TextView) itemView.findViewById(R.id.money);
            date_tv = (TextView) itemView.findViewById(R.id.date);
            memo_tv = (TextView) itemView.findViewById(R.id.memo);
            session_tv = (TextView) itemView.findViewById(R.id.session);
        }
    }
}
