package com.chengtech.chengtechmt.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.RecycleViewAdapter;
import com.chengtech.chengtechmt.divider.RecycleViewDivider;
import com.chengtech.chengtechmt.entity.business.FellowMen;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * 作者: LiuFuYingWang on 2017/11/23 15:10.
 * 病害登记中-陪同人员添加弹出框
 */

public class FellowMenDialogFragment extends DialogFragment {
    public static final String FELLOW_MEN_LIST_1 = "fellow_men_list_1";
    private Button add_bt;
    private EditText input_et;
    private RecyclerView recyclerView;
    private List<FellowMen> data;
    private RecyclerView.Adapter adapter;
    private SparseBooleanArray checkState = new SparseBooleanArray();
    private OnDismissListener onDismissListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_fellow_men, null, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        input_et = (EditText) view.findViewById(R.id.input);
        add_bt = (Button) view.findViewById(R.id.add);

        data = (List<FellowMen>) ObjectSaveUtils.getObject(getActivity(), FELLOW_MEN_LIST_1);
        if (data == null)
            data = new ArrayList<>();
        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fellow_men, parent, false);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(view) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
                TextView fellow_tv = (TextView) holder.itemView.findViewById(R.id.fellowMen);
                ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.delete);
                final CheckBox checkBox = (CheckBox) holder.itemView.findViewById(R.id.checkbox);
                fellow_tv.setText(data.get(position).name);
                fellow_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox.setChecked(!checkBox.isChecked());
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(data.get(position).isChecked);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        data.get(position).isChecked = isChecked;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }
        };

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initEvent();
        builder.setView(view);
        builder.setTitle("添加陪同人员");
        builder.setCancelable(true);
        return builder.create();
    }

    private void initEvent() {
        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputContent = input_et.getText().toString().trim();
                if (TextUtils.isEmpty(inputContent)) {
                    Toast.makeText(getActivity(), "内容为空", Toast.LENGTH_SHORT).show();
                } else {
                    data.add(new FellowMen(inputContent, false));
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemRangeChanged(0, data.size());
                    ObjectSaveUtils.saveObject(getActivity(), FELLOW_MEN_LIST_1, data);
                    input_et.setText("");
                }


            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismissListener.onDismiss(data);
        ObjectSaveUtils.saveObject(getActivity(), FELLOW_MEN_LIST_1, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onDismissListener = (OnDismissListener) activity;
    }

    public interface OnDismissListener {
        public void onDismiss(Object data);
    }
}
