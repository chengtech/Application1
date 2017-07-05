package com.chengtech.chengtechmt.adapter.business;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.ImageAddAdapter;
import com.chengtech.chengtechmt.entity.Culvert;
import com.chengtech.chengtechmt.entity.bridge.Bridge;
import com.chengtech.chengtechmt.entity.patrol.BriOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.BridgeRecord;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenCheck;
import com.chengtech.chengtechmt.entity.patrol.CulvertOftenRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2017/5/12 14:52.
 * 涵洞经常检查新增页面-adapter
 */

public class CulvertOftenCheckAddAdapter extends RecyclerView.Adapter {

    private int HEAD_TYPE = 0;
    private int NORMAL_TYPE = 1;
    private int FOOT_TYPE = 2;
    private CulvertOftenCheck culvertOftenCheck;
    private Context mContext;
    private ImageAddAdapter imageAdapter;
    private ArrayList<String> picturePaths;

    public CulvertOftenCheckAddAdapter(Context context, CulvertOftenCheck culvertOftenCheck) {
        this.culvertOftenCheck = culvertOftenCheck;
        this.mContext = context;
    }

    public CulvertOftenCheckAddAdapter(Context context, CulvertOftenCheck culvertOftenCheck,List<String> filePaths) {
        this.culvertOftenCheck = culvertOftenCheck;
        this.mContext = context;
        this.picturePaths = (ArrayList<String>) filePaths;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cul_often_add_head = LayoutInflater.from(parent.getContext()).inflate(R.layout.culvert_often_add_head, parent, false);
        ViewHolder1 viewHolder1 = new ViewHolder1(cul_often_add_head);

        View cul_often_add_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.culvert_often_add_item, parent, false);
        ViewHolder2 viewHolder2 = new ViewHolder2(cul_often_add_item);

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
            viewHolder1.routeCode.setText(culvertOftenCheck.routeCode == null ? "" : culvertOftenCheck.routeCode);
            viewHolder1.routeName.setText(culvertOftenCheck.routeName == null ? "" : culvertOftenCheck.routeName);
            viewHolder1.thirdDept.setText(culvertOftenCheck.thirdDeptName);
            viewHolder1.head.setText(culvertOftenCheck.head);
            viewHolder1.head.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    culvertOftenCheck.head = s.toString();
                }
            });
            viewHolder1.record.setText(culvertOftenCheck.checkMan);
            viewHolder1.record.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    culvertOftenCheck.checkMan = s.toString();
                }
            });
            viewHolder1.checkDate.setText(culvertOftenCheck.checkDate.substring(0, 10));
            final int year = Integer.parseInt(culvertOftenCheck.checkDate.substring(0, 4));
            final int month = Integer.parseInt(culvertOftenCheck.checkDate.substring(5, 7));
            final int day = Integer.parseInt(culvertOftenCheck.checkDate.substring(8, 10));
            viewHolder1.checkDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String monthStr = String.format("%02d", monthOfYear + 1);
                            viewHolder1.checkDate.setText(year + "-" + monthStr + "-" + dayOfMonth);
                            culvertOftenCheck.checkDate = year + "-" + monthStr + "-" + dayOfMonth;
                        }
                    }, year, month - 1, day);
                    dialog.show();
                }
            });
            final List<String> data = new ArrayList<>();
            data.add("");
            for (int i = 0; i < culvertOftenCheck.listCulvert.size(); i++) {
                Culvert culvert = culvertOftenCheck.listCulvert.get(i);
                data.add(culvert.code + "---" + culvert.mileageStake + "---" + culvert.materialType);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, data);
            viewHolder1.spinner.setAdapter(adapter);
            Integer briNo = culvertOftenCheck.savePosition.get("涵洞编码");
            if (briNo != null)
                viewHolder1.spinner.setSelection(briNo, true);
            viewHolder1.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String content = data.get(position);
                    String[] split = content.split("---");
                    if (split != null && split.length == 3) {
                        viewHolder1.culPeg.setText(split[1]);
                        culvertOftenCheck.culvertCode = split[0];
                        culvertOftenCheck.culvertPeg = split[1];
                    }
                    culvertOftenCheck.savePosition.put("涵洞编码", position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (holder instanceof ViewHolder2) {
            final ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            //这里要position减一，因为有表头占了一位
            if (culvertOftenCheck != null && culvertOftenCheck.listCulvertoftencheck != null && culvertOftenCheck.listCulvertoftencheck.size() > 0) {
                List<CulvertOftenRecord> culvertRecords = culvertOftenCheck.listCulvertoftencheck;
                final CulvertOftenRecord culvertRecord = culvertRecords.get(position - 1);
                viewHolder2.partNo.setText("序号：" + position);
                viewHolder2.partName.setText("部件名称：" + culvertRecord.culvertName);
                if (viewHolder2.diseaseSituation.getTag() instanceof TextWatcher) {
                    viewHolder2.diseaseSituation.removeTextChangedListener((TextWatcher) viewHolder2.diseaseSituation.getTag());
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
                        culvertRecord.diseaseSituation = s.toString();
                    }
                };
                viewHolder2.diseaseSituation.setText(culvertRecord.diseaseSituation);
                viewHolder2.diseaseSituation.addTextChangedListener(textWatcher);
                viewHolder2.diseaseSituation.setTag(textWatcher);

//                String []listDefectTypeByConstantDD = culvertRecord.listCheckSituationByConstantDD;
                final String[] listCheckSituationByConstantDD = new String[]{"否", "是"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, listCheckSituationByConstantDD);
                viewHolder2.defectType.setAdapter(adapter);
                if (culvertOftenCheck.savePosition.get(culvertRecord.culvertName) != null)
                    viewHolder2.defectType.setSelection(culvertOftenCheck.savePosition.get(culvertRecord.culvertName));
                viewHolder2.defectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        culvertRecord.checkSituation = (listCheckSituationByConstantDD[position].equals("否") ? "0" : "1");
                        culvertOftenCheck.savePosition.put(culvertRecord.culvertName, position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final String[] listTreatmentOpinion = culvertRecord.listTreatmentOpinion;

                viewHolder2.oneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            culvertRecord.treatmentOpinionOne = listTreatmentOpinion[0];
                        }else {
                            culvertRecord.treatmentOpinionOne = "";
                        }
                    }
                });

                if (TextUtils.isEmpty(culvertRecord.treatmentOpinionOne)) {
                    viewHolder2.oneCheckBox.setChecked(false);
                } else {
                    viewHolder2.oneCheckBox.setChecked(true);
                }

                viewHolder2.twoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            culvertRecord.treatmentOpinionTwo = listTreatmentOpinion[1];
                        }else {
                            culvertRecord.treatmentOpinionTwo = "";
                        }
                    }
                });

                if (TextUtils.isEmpty(culvertRecord.treatmentOpinionTwo)) {
                    viewHolder2.twoCheckBox.setChecked(false);
                } else {
                    viewHolder2.twoCheckBox.setChecked(true);
                }

                viewHolder2.threeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            culvertRecord.treatmentOpinionThree = listTreatmentOpinion[2];
                        }else {
                            culvertRecord.treatmentOpinionThree = "";
                        }
                    }
                });

                if (TextUtils.isEmpty(culvertRecord.treatmentOpinionThree)) {
                    viewHolder2.threeCheckBox.setChecked(false);
                } else {
                    viewHolder2.threeCheckBox.setChecked(true);
                }
            }
        }else if (holder instanceof ViewHolder3) {
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            RecyclerView recyclerView =  viewHolder3.recyclerView;
            recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
            imageAdapter = new ImageAddAdapter(mContext,picturePaths);
            recyclerView.setAdapter(imageAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 2 + (culvertOftenCheck == null ? 0 : culvertOftenCheck.listCulvertoftencheck.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEAD_TYPE;
        if (position==getItemCount()-1) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    private class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView routeCode, routeName, thirdDept, culPeg,
                checkDate;

        public EditText head, record;
        public Spinner spinner;

        public ViewHolder1(View itemView) {
            super(itemView);

            routeCode = (TextView) itemView.findViewById(R.id.routeCode);
            routeName = (TextView) itemView.findViewById(R.id.routeName);
            thirdDept = (TextView) itemView.findViewById(R.id.thirdDept);
            culPeg = (TextView) itemView.findViewById(R.id.culPeg);
            head = (EditText) itemView.findViewById(R.id.head);
            record = (EditText) itemView.findViewById(R.id.record);
            checkDate = (TextView) itemView.findViewById(R.id.checkDate);
            spinner = (Spinner) itemView.findViewById(R.id.culvertCode);

        }
    }

    private class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView partNo, partName;

        public Spinner defectType;
        public EditText diseaseSituation;
        public CheckBox oneCheckBox, twoCheckBox, threeCheckBox;


        public ViewHolder2(View itemView) {
            super(itemView);
            partNo = (TextView) itemView.findViewById(R.id.partNo);
            partName = (TextView) itemView.findViewById(R.id.partName);
            diseaseSituation = (EditText) itemView.findViewById(R.id.diseaseSituation);
            defectType = (Spinner) itemView.findViewById(R.id.defectType);
            oneCheckBox = (CheckBox) itemView.findViewById(R.id.checkboxOne);
            twoCheckBox = (CheckBox) itemView.findViewById(R.id.checkboxTwo);
            threeCheckBox = (CheckBox) itemView.findViewById(R.id.checkboxThree);

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
