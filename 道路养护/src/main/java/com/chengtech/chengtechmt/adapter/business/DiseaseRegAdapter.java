package com.chengtech.chengtechmt.adapter.business;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.business.DiseaseRegistrationActivity;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.picasso.CompressTransFormation;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.chengtech.chengtechmt.activity.business.DiseaseRegistrationListActivity.DISEASE_REGISTRATION_LIST;

/**
 * 作者: LiuFuYingWang on 2017/8/17 16:06.
 */

public class DiseaseRegAdapter extends RecyclerView.Adapter {


    private boolean editMode = false;
    private SparseBooleanArray checkState = new SparseBooleanArray();
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
            myViewHolder.tag_tv.setBackgroundColor(Color.GREEN);
            myViewHolder.checkBox.setEnabled(false);
//            myViewHolder.checkBox.setClickable(false);
        } else {
            myViewHolder.tag_iv.setBackgroundResource(R.mipmap.ia_exclamationplot);
            myViewHolder.tag_tv.setText("未上传");
            myViewHolder.tag_tv.setBackgroundColor(Color.RED);
            myViewHolder.checkBox.setEnabled(true);
//            myViewHolder.checkBox.setClickable(true);
        }

        myViewHolder.recordMan_tv.setText("巡查人：" + diseaseRegistration.patrolMan);
        myViewHolder.recordDate_tv.setText("日期：" + diseaseRegistration.patrolTime);
        myViewHolder.showMsg.setText("\u3000\u3000" + diseaseRegistration.diseaseDescription);
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
                        }).start();
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.setCancelClickListener(null);
                sweetAlertDialog.show();

            }
        });

        if (!editMode) {
            myViewHolder.checkBox.setVisibility(View.GONE);
        } else {
            myViewHolder.checkBox.setVisibility(View.VISIBLE);
        }

        myViewHolder.checkBox.setOnCheckedChangeListener(null);
        myViewHolder.checkBox.setChecked(checkState.get(position, false));
        myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkState.put(position, isChecked);
            }
        });
        if (diseaseRegistration.picPaths.size() > 0) {
            myViewHolder.thumbnail.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(new File(diseaseRegistration.picPaths.get(0))).transform(new CompressTransFormation(CommonUtils.dp2px(mContext, 80))).into(myViewHolder.thumbnail);
        } else {
            myViewHolder.thumbnail.setVisibility(View.GONE);
        }
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
        public TextView showMsg;
        public CheckBox checkBox;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            count_tv = (TextView) itemView.findViewById(R.id.count);
            tag_iv = (ImageView) itemView.findViewById(R.id.tag);
            tag_tv = (TextView) itemView.findViewById(R.id.tv1);
            recordDate_tv = (TextView) itemView.findViewById(R.id.recordDate);
            recordMan_tv = (TextView) itemView.findViewById(R.id.recordMan);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete);
            container_layout = (RelativeLayout) itemView.findViewById(R.id.container);
            showMsg = (TextView) itemView.findViewById(R.id.showMsg);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        notifyDataSetChanged();
    }

    public void checkAll(boolean checkAll) {
        int count = data.size();
        if (checkAll) {
            for (int i = 0; i < count; i++) {
                checkState.put(i, true);
            }
        } else {
            for (int i = 0; i < count; i++) {
                checkState.put(i, false);
            }
        }
        notifyDataSetChanged();
    }

    public List getCheckedIndex() {
        List<Integer> checkedIndexs = new ArrayList<>();
        for (int i = 0; i < checkState.size(); i++) {
            if (checkState.valueAt(i)) {
                checkedIndexs.add(checkState.keyAt(i));
            }
        }
        return checkedIndexs;
    }

    public boolean getEditMode() {
        return editMode;
    }
}
