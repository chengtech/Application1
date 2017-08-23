package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.DiseaseRegistrationActivity;
import com.chengtech.chengtechmt.activity.business.DiseaseRegistrationListActivity;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.chengtech.chengtechmt.activity.business.DiseaseRegistrationListActivity.DISEASE_REGISTRATION_LIST;

/**
 * 作者: LiuFuYingWang on 2017/8/17 16:06.
 */

public class DiseaseRegAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DiseaseRegistration> data;

    public DiseaseRegAdapter(Context context, List<DiseaseRegistration> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease_reg, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DiseaseRegistration diseaseRegistration = data.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.count_tv.setText(String.valueOf(position + 1));
        if (diseaseRegistration.isUpload) {
            myViewHolder.tag_iv.setBackgroundResource(R.mipmap.ia_correct);
            myViewHolder.tag_tv.setText("已上传");
            myViewHolder.tag_tv.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            myViewHolder.tag_iv.setBackgroundResource(R.mipmap.ia_exclamationplot);
            myViewHolder.tag_tv.setText("未上传");
            myViewHolder.tag_tv.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_light));
        }

        myViewHolder.recordMan_tv.setText("登记人：" + diseaseRegistration.recordMan);
        myViewHolder.recordDate_tv.setText("登记日期：" + diseaseRegistration.recordDate);
        myViewHolder.container_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiseaseRegistrationActivity.startAction(mContext, diseaseRegistration, position);
            }
        });


        myViewHolder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("删除当前项？").setConfirmText("删除").setCancelText("取消");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        data.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, data.size());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ObjectSaveUtils.saveObject(mContext, DISEASE_REGISTRATION_LIST, data);
                            }
                        }).run();
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.setCancelClickListener(null);
                sweetAlertDialog.show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView count_tv;
        public ImageView tag_iv;
        public TextView tag_tv, recordDate_tv, recordMan_tv;
        public LinearLayout delete_layout;
        public RelativeLayout container_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            count_tv = (TextView) itemView.findViewById(R.id.count);
            tag_iv = (ImageView) itemView.findViewById(R.id.tag);
            tag_tv = (TextView) itemView.findViewById(R.id.tv1);
            recordDate_tv = (TextView) itemView.findViewById(R.id.recordDate);
            recordMan_tv = (TextView) itemView.findViewById(R.id.recordMan);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete);
            container_layout = (RelativeLayout) itemView.findViewById(R.id.container);

        }
    }
}
