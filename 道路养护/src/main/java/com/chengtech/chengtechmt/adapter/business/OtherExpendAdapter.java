package com.chengtech.chengtechmt.adapter.business;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.CapitalSource;
import com.chengtech.chengtechmt.entity.OtherExpend;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.DateUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/10/20 15:33.
 * 大中修模块--资金支出adapter
 */

public class OtherExpendAdapter extends RecyclerView.Adapter {
    private List<OtherExpend> data;

    public OtherExpendAdapter(List<OtherExpend> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_expend, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        OtherExpend otherExpend = data.get(position);
        viewHolder.expendType_tv.setText(otherExpend.expendType);
        viewHolder.money_tv.setText(CommonUtils.getCommalFormat(new BigDecimal(otherExpend.money)));
        viewHolder.expendRate_tv.setText(otherExpend.expendContractRate);

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView expendType_tv, money_tv, expendRate_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            expendType_tv = (TextView) itemView.findViewById(R.id.tv1);
            money_tv = (TextView) itemView.findViewById(R.id.tv2);
            expendRate_tv = (TextView) itemView.findViewById(R.id.tv3);
        }
    }
}
