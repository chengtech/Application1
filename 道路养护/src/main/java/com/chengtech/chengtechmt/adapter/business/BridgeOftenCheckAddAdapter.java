package com.chengtech.chengtechmt.adapter.business;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.divider.GridViewDivider;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.entity.patrol.BriOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.BridgeRecord;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.ViewHolder;

import java.io.File;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/5/12 14:52.
 */

public class BridgeOftenCheckAddAdapter extends RecyclerView.Adapter {

    private int HEAD_TYPE = 0;
    private int NORMAL_TYPE = 1;
    private int FOOT_TYPE = 2;
    private BriOftenCheck briOftenCheck;
    private Context mContext;
    private ImageAddAdapter imageAdapter;
    private ArrayList<String> picturePaths;

    public BridgeOftenCheckAddAdapter(Context context, BriOftenCheck briOftenCheck) {
        this.briOftenCheck = briOftenCheck;
        this.mContext = context;
        picturePaths = new ArrayList<>();
    }

    public BridgeOftenCheckAddAdapter(Context context, BriOftenCheck briOftenCheck, ArrayList<String> picturePaths) {
        this.briOftenCheck = briOftenCheck;
        this.mContext = context;
        this.picturePaths = picturePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View bri_often_add_head = LayoutInflater.from(parent.getContext()).inflate(R.layout.bridge_often_add_head, parent, false);
        ViewHolder1 viewHolder1 = new ViewHolder1(bri_often_add_head);

        View bri_often_add_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.bridge_often_add_item, parent, false);
        ViewHolder2 viewHolder2 = new ViewHolder2(bri_often_add_item);

        View bri_often_add_foot = LayoutInflater.from(parent.getContext()).inflate(R.layout.bridge_often_add_foot, parent, false);
        ViewHolder3 viewHolder3 = new ViewHolder3(bri_often_add_foot);
        if (viewType == HEAD_TYPE)
            return viewHolder1;

        if (viewType == FOOT_TYPE)
            return viewHolder3;
        return viewHolder2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder1) {
            final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.secondDept.setText(briOftenCheck.secondDeptName);
            viewHolder1.thirdDept.setText(briOftenCheck.thirdDeptName);
            viewHolder1.routeName.setText(briOftenCheck.routeName);
            viewHolder1.head.setText(briOftenCheck.head);
            viewHolder1.head.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    briOftenCheck.head = s.toString();
                }
            });
            viewHolder1.record.setText(briOftenCheck.record);
            viewHolder1.record.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    briOftenCheck.record = s.toString();
                }
            });
            if (!TextUtils.isEmpty(briOftenCheck.checkDate)) {
                viewHolder1.checkDate.setText(briOftenCheck.checkDate.substring(0, 10));
                final int year = Integer.parseInt(briOftenCheck.checkDate.substring(0, 4));
                final int month = Integer.parseInt(briOftenCheck.checkDate.substring(5, 7));
                final int day = Integer.parseInt(briOftenCheck.checkDate.substring(8, 10));
                viewHolder1.checkDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthStr = String.format("%02d", monthOfYear + 1);
                                viewHolder1.checkDate.setText(year + "-" + monthStr + "-" + dayOfMonth);
                                briOftenCheck.checkDate = year + "-" + monthStr + "-" + dayOfMonth;
                            }
                        }, year, month - 1, day);
                        dialog.show();
                    }
                });
            } else {
                Date date = new Date();
                viewHolder1.checkDate.setText(DateUtils.convertDate2(date));
                final int year = date.getYear();
                final int month = date.getMonth();
                final int day = date.getDay();
                viewHolder1.checkDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String monthStr = String.format("%02d", monthOfYear + 1);
                                viewHolder1.checkDate.setText(year + "-" + monthStr + "-" + dayOfMonth);
                                briOftenCheck.checkDate = year + "-" + monthStr + "-" + dayOfMonth;
                            }
                        }, year, month - 1, day);
                        dialog.show();
                    }
                });
            }
            final List<String> data = new ArrayList<>();
            data.add("");
            for (int i = 0; i < briOftenCheck.listBridge.size(); i++) {
                Bridge bridge = briOftenCheck.listBridge.get(i);
                data.add(bridge.code + "---" + bridge.name + "---" + bridge.pileId);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, data);
            viewHolder1.spinner.setAdapter(adapter);
            Integer briNo = briOftenCheck.savePosition.get("桥梁编码");
            if (briNo != null)
                viewHolder1.spinner.setSelection(briNo, true);
            viewHolder1.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String content = data.get(position);
                    String[] split = content.split("---");
                    if (split != null && split.length == 3) {
                        viewHolder1.briName.setText(split[1]);
                        viewHolder1.briPeg.setText(split[2]);
                        briOftenCheck.briName = split[1];
                        briOftenCheck.brino = split[0];
                        briOftenCheck.bripeg = split[2];
                    }
                    briOftenCheck.savePosition.put("桥梁编码", position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (holder instanceof ViewHolder2) {
            final ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            //这里要position减一，因为有表头占了一位
            if (briOftenCheck != null && briOftenCheck.listBridgeCheckRecord != null && briOftenCheck.listBridgeCheckRecord.size() > 0) {
                List<BridgeRecord> bridgeRecords = briOftenCheck.listBridgeCheckRecord;
                final BridgeRecord bridgeRecord = bridgeRecords.get(position - 1);
                viewHolder2.partNo.setText("部件编号：" + bridgeRecord.partNo);
                viewHolder2.partName.setText("部件名称：" + bridgeRecord.partName);
                if (viewHolder2.defectArea.getTag() instanceof TextWatcher) {
                    viewHolder2.defectArea.removeTextChangedListener((TextWatcher) viewHolder2.defectArea.getTag());
                }
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        bridgeRecord.defectArea = s.toString();
                    }
                };
                viewHolder2.defectArea.setText(bridgeRecord.defectArea);
                viewHolder2.defectArea.addTextChangedListener(textWatcher);
                viewHolder2.defectArea.setTag(textWatcher);

                if (viewHolder2.repairView.getTag() instanceof TextWatcher) {
                    viewHolder2.repairView.removeTextChangedListener((TextWatcher) viewHolder2.repairView.getTag());
                }
                TextWatcher textWatcher2 = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        bridgeRecord.repairView = s.toString();
                    }
                };
                viewHolder2.repairView.setText(bridgeRecord.repairView);
                viewHolder2.repairView.addTextChangedListener(textWatcher2);
                viewHolder2.repairView.setTag(textWatcher2);


                String[] listDefectTypeByConstantDD = bridgeRecord.listDefectTypeByConstantDD;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, listDefectTypeByConstantDD);
                viewHolder2.defectType.setAdapter(adapter);
                if (briOftenCheck.savePosition.get("部件编号：" + bridgeRecord.partNo) != null)
                    viewHolder2.defectType.setSelection(briOftenCheck.savePosition.get("部件编号：" + bridgeRecord.partNo));
                viewHolder2.defectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        bridgeRecord.defectType = bridgeRecord.listDefectTypeByConstantDD[position];
                        briOftenCheck.savePosition.put("部件编号：" + bridgeRecord.partNo, position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else if (holder instanceof ViewHolder3) {
            final ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            RecyclerView recyclerView = viewHolder3.recyclerView;
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            imageAdapter = new ImageAddAdapter(mContext, picturePaths);
//            recyclerView.setAdapter(imageAdapter);
//            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//                @Override
//                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                    //不是第一个的格子都设一个左边和底部的间距
//                    outRect.left = 20;
//                    outRect.bottom = 20;
//                    //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
//                    if (parent.getChildLayoutPosition(view) %4==0) {
//                        outRect.left = 0;
//                    }
//                }
//            });
            recyclerView.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 2 + (briOftenCheck == null ? 0 : briOftenCheck.listBridgeCheckRecord.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEAD_TYPE;
        if (position == getItemCount() - 1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView secondDept, thirdDept, routeName, briPeg, briName,
                checkDate;

        public EditText head, record;
        public Spinner spinner;

        public ViewHolder1(View itemView) {
            super(itemView);

            secondDept = (TextView) itemView.findViewById(R.id.secondDept);
            thirdDept = (TextView) itemView.findViewById(R.id.thirdDept);
            routeName = (TextView) itemView.findViewById(R.id.routeName);
            briPeg = (TextView) itemView.findViewById(R.id.briPeg);
            briName = (TextView) itemView.findViewById(R.id.briName);
            head = (EditText) itemView.findViewById(R.id.head);
            record = (EditText) itemView.findViewById(R.id.record);
            checkDate = (TextView) itemView.findViewById(R.id.checkDate);
            spinner = (Spinner) itemView.findViewById(R.id.briCode);

        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView partNo, partName;

        public Spinner defectType;
        public EditText defectArea, repairView;


        public ViewHolder2(View itemView) {
            super(itemView);
            partNo = (TextView) itemView.findViewById(R.id.partNo);
            partName = (TextView) itemView.findViewById(R.id.partName);
            defectArea = (EditText) itemView.findViewById(R.id.defectArea);
            repairView = (EditText) itemView.findViewById(R.id.repairView);
            defectType = (Spinner) itemView.findViewById(R.id.defectType);

        }
    }

    private class ViewHolder3 extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;

        public ViewHolder3(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);

        }
    }

    public ImageAddAdapter getImageAdapter() {
        return imageAdapter;
    }
}
