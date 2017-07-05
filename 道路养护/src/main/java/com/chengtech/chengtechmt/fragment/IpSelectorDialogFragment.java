package com.chengtech.chengtechmt.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.chengtechmt.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者: LiuFuYingWang on 2016/9/5 16:24.
 */
public class IpSelectorDialogFragment extends DialogFragment {

    private TextView textView;
    private ListView listView;

    private List<String> itemList;
    private int checkedPosition;
    public static final String IP_ADDRESS_LIST = "IP_ADDRESS_LIST";
    public static final String IP_ADDRESS_CHECKED_POS = "IP_ADDRESS_CHECKED_POS";
    public static final String IP_ADDRESS = "IP_ADDRESS";
    private ArrayAdapter<String> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        init();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_ip_selector, null, false);
        textView = (TextView) view.findViewById(R.id.add);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice
                , itemList);
        listView.setAdapter(adapter);
        listView.setItemChecked(checkedPosition, true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });

        builder.setView(view);
        builder.setTitle("IP配置列表");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.saveList(getActivity(), IP_ADDRESS_LIST, itemList);
                SharedPreferencesUtil.saveData(getActivity(), IP_ADDRESS_CHECKED_POS, listView.getCheckedItemPosition());
                SharedPreferencesUtil.saveData(getActivity(), IP_ADDRESS, itemList.get(listView.getCheckedItemPosition()));
                //TODO
                MyConstants.PRE_URL = itemList.get(listView.getCheckedItemPosition());
                MyConstants.setConstant();
            }
        }).setNegativeButton("取消", null);
//                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i("tag", "单选：" + which);
//                    }
//                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除当前IP地址？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemList.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("取消",null).show();

    }

    private void init() {
        itemList = SharedPreferencesUtil.getList(getActivity(), IP_ADDRESS_LIST, "");
        checkedPosition = (int) SharedPreferencesUtil.getData(getActivity(), IP_ADDRESS_CHECKED_POS, 0);
        if (itemList == null) {
            itemList = new ArrayList<>();
            itemList.add("http://14.23.99.106:8666/");
            itemList.add("http://192.1.40.44:8080/chengtechmt/");
        }
    }


    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加新的IP地址");
        final EditText et = new EditText(getActivity());
        builder.setView(et).setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editMsg = et.getText().toString();
                itemList.add(editMsg);
                adapter.notifyDataSetChanged();

            }
        })
                .setNegativeButton("取消", null)
                .show();
    }
}
