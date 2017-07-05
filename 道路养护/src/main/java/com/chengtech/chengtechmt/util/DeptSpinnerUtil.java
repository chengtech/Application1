package com.chengtech.chengtechmt.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.activity.dbm.DbmListActivity;
import com.chengtech.chengtechmt.activity.dbm.RoadSideFacilitiesActivity;
import com.chengtech.chengtechmt.entity.Tree;
import com.chengtech.nicespinner.NiceSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: LiuFuYingWang on 2017/3/13 16:19.
 */

public class DeptSpinnerUtil {
    public static final int FILTER_SUCCESS = 101;

    public static List<NiceSpinner> niceSpinners = new ArrayList<>();

    public static AlertDialog createDeptSpinner(final Context context, final List<Tree> trees,
                                                final String listUrl, final Handler handler, final int type) {
        final List<String> firstDept = new ArrayList<>();
        final Map<String, List<String>> secondDept = new HashMap<>();
        final int[] firstPosition = new int[1];
        final String[] deptIds = new String[1];
        for (Tree t : trees) {
            if (t.id.equals(t.secondDeptId)) {
                firstDept.add(t.text);
                List<String> temp = new ArrayList<String>();
                temp.add("全选");
                for (Tree t2 : trees) {
                    if (t.id.equals(t2.parentId)) {
                        temp.add(t2.text);
                    }
                }
                secondDept.put(t.text, temp);
            }
        }
        View contentView = LayoutInflater.from(context).inflate(R.layout.dept_spinner_selector, null);
        LinearLayout parent = (LinearLayout) contentView.findViewById(R.id.parent);
        parent.getChildAt(4).setVisibility(View.VISIBLE);
        parent.getChildAt(5).setVisibility(View.VISIBLE);
        parent.getChildAt(0).setVisibility(View.GONE);
        parent.getChildAt(1).setVisibility(View.GONE);
        NiceSpinner firstS = (NiceSpinner) contentView.findViewById(R.id.secondDept);
        final NiceSpinner secondS = (NiceSpinner) contentView.findViewById(R.id.thirdDept);
        firstS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                secondS.attachDataSource(secondDept.get(firstDept.get(position)));
                firstPosition[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secondS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDept = secondDept.get(firstDept.get(firstPosition[0])).get(position);
                if (selectedDept.equals("全选")) {
                    for (Tree t : trees) {
                        if (t.text.equals(firstDept.get(firstPosition[0]))) {
                            deptIds[0] = t.id;
                            for (Tree t2 : trees) {
                                if (t.id.equals(t2.parentId))
                                    deptIds[0] = deptIds[0] + "\',\'" + t2.id;

                            }
                            break;
                        }
                    }
                } else {
                    for (Tree t : trees) {
                        if (t.text.equals(selectedDept)) {
                            deptIds[0] = t.id;
                            break;
                        }
                    }
                }

                //重新开始计算页码
//                pageNo = 1;
//                maxPage = 1;
//                pageNo_tv.setText(pageNo + "");
//                flag = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firstS.attachDataSource(firstDept);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(contentView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = handler.obtainMessage();
                message.what = 5;
                message.obj = deptIds[0];
                handler.sendMessage(message);
                HttpclientUtil.getData(context, listUrl + deptIds[0] + "&pager.pageSize=100&pager.pageNo=1&direction=asc&sort=sortOrder", handler, type);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }

    /**
     * @param titleName     选择框的标题栏
     * @param selectContent 选择框可以选择的内容
     * @return
     */
    public static AlertDialog createDeptSpinner2(final Context context, final Handler handler, final String[] titleName, final Map<String, List<String>> selectContent, boolean[] linkedable) {
        if (selectContent.size()==0)
            return null;
        niceSpinners.clear();
        LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < titleName.length; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.dept_spinner_item, null, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = 10;
            view.setLayoutParams(layoutParams);
            TextView title_name = (TextView) view.findViewById(R.id.deptName);
            title_name.setText(titleName[i]);
            NiceSpinner spinner = (NiceSpinner) view.findViewById(R.id.niceSpinner);
            niceSpinners.add(spinner);
            parent.addView(view);
        }

        for (int i = 0; i < niceSpinners.size(); i++) {
            if (i < (niceSpinners.size() - 1)) {
                if (linkedable[i]) {
                    final NiceSpinner nextSpinner = niceSpinners.get(i + 1);
                    final NiceSpinner currentSpinner = niceSpinners.get(i);
                    NiceSpinner preSpinner = null;
                    NiceSpinner prepreSpinner = null;
                    if (i != 0) {
                        preSpinner = niceSpinners.get(i - 1);
                    }

                    final NiceSpinner finalPreSpinner = preSpinner;
                    final int j=i;
                    currentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (finalPreSpinner == null) {
                                nextSpinner.attachDataSource(selectContent.get(currentSpinner.getText().toString()));
                            } else {
                                String text = finalPreSpinner.getText().toString();
                                List<String> content = selectContent.get(text);
                                if (content==null){
                                    List<String> selectAll = new ArrayList<>();
                                    selectAll.add("");
                                    nextSpinner.attachDataSource(selectAll);
                                    return ;
                                }
                                String text1 = content.get(position);

                                List<String> content2 =null;
                                if (titleName[j].equals("路线")) {
                                    content2 = selectContent.get(text1+text);
                                }else {
                                    content2 = selectContent.get(text1);
                                }
                                if (content2==null) {
                                    List<String> selectAll = new ArrayList<>();
                                    selectAll.add("");
                                    nextSpinner.attachDataSource(selectAll);
                                }else {
                                    nextSpinner.attachDataSource(content2);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (i == 0) {
                        currentSpinner.attachDataSource(selectContent.get(titleName[i]));
                    }
                } else {
                    //设置下一项的数据
                    if (niceSpinners.get(i + 1) != null)
                        niceSpinners.get(i + 1).attachDataSource(selectContent.get(titleName[i + 1]));
                }
            }
//            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(parent);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.sendEmptyMessage(FILTER_SUCCESS);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
