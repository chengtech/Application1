package com.chengtech.chengtechmt.activity.expertdecision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chengtech.chengtechmt.BaseActivity;
import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.expertdecision.EvaluationSummary;
import com.chengtech.chengtechmt.entity.expertdecision.EvaluationSummarySub;
import com.chengtech.chengtechmt.util.CommonUtils;
import com.chengtech.chengtechmt.util.HttpclientUtil;
import com.chengtech.chengtechmt.util.MyConstants;
import com.chengtech.nicespinner.NiceSpinner;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.chengtech.chengtechmt.R.id.fourthSpinner;
import static com.chengtech.chengtechmt.R.id.thirdSpinner;

public class EvaluationSummaryActivity extends BaseActivity {

    private NiceSpinner firstSpinner, secondSpinner, thirdSpinner, fourthSpinner;
    private List<String> fristData, secondData, thirdData, fourthData;
    private EditText et1, et2;
    private String urlParams;
    private LinearLayout parent_layout;
    private AlertDialog deptDialog;
    private List<EvaluationSummarySub> summarySubs = new ArrayList<>();
    private String url = MyConstants.PRE_URL + "mt/expertdecision/roadtechevaluation/evaluationsummary/listEvaluationSummaryJsonByMobile.action";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Gson gson = new Gson();
            switch (msg.what) {
                case 0:
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            EvaluationSummarySub evaluationSummarySub = gson.fromJson(jsonObject.toString(),
                                    EvaluationSummarySub.class);
                            summarySubs.add(evaluationSummarySub);
                        }

                        EvaluationSummary evaluationSummary = summarySubs.get(0).evaluationSummary;
                        List<String> content = evaluationSummary.getContent();
                        List<String> titles = evaluationSummary.getTitles();
                        for (int i=0;i<titles.size();i++) {
                            TextView textView = new TextView(EvaluationSummaryActivity.this);
                            textView.setText(titles.get(i)+content.get(i));
                            textView.setPadding(CommonUtils.dp2px(EvaluationSummaryActivity.this,8),10,10,8);
                            parent_layout.addView(textView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.activity_evaluation_summary);

        setNavigationIcon(true);
        hidePagerNavigation(true);
        initData();
        initView();
    }

    private void initView() {
        parent_layout = (LinearLayout) findViewById(R.id.parent_container);
    }

    private void initData() {
        fristData = Arrays.asList(new String[]{"请选择", "G105", "G106", "G107", "G324", "G325"});
        secondData = Arrays.asList(new String[]{"请选择", "车道1", "车道2", "车道3", "车道4"});
        thirdData = Arrays.asList(new String[]{"请选择", "上行", "下行"});
        fourthData = Arrays.asList(new String[]{"请选择", "2014", "2015", "2016", "2017", "2018", "2019", "2020"});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_section, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            inflateSpnnier();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflateSpnnier() {
        if (deptDialog!=null) {
            deptDialog.show();
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.evaluation_detail_spinner, null);
        firstSpinner = (NiceSpinner) view.findViewById(R.id.firstSpinner);
        secondSpinner = (NiceSpinner) view.findViewById(R.id.secondSpinner);
        thirdSpinner = (NiceSpinner) view.findViewById(R.id.thirdSpinner);
        fourthSpinner = (NiceSpinner) view.findViewById(R.id.fourthSpinner);
        et1 = (EditText) view.findViewById(R.id.et1);
        et2 = (EditText) view.findViewById(R.id.et2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String routeCode = firstSpinner.getText().toString();
                String lane = secondSpinner.getText().toString();
                String direction = thirdSpinner.getText().toString();
                String year = fourthSpinner.getText().toString();
                String startStake = et1.getText().toString();
                String endStake = et2.getText().toString();
                if ("请选择".equals(routeCode)) {
                    Toast.makeText(EvaluationSummaryActivity.this, "请选择路线！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(lane)) {
                    Toast.makeText(EvaluationSummaryActivity.this, "请选择车道！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(direction)) {
                    Toast.makeText(EvaluationSummaryActivity.this, "请选择方向！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }
                if ("请选择".equals(year)) {
                    Toast.makeText(EvaluationSummaryActivity.this, "请选择年份！", Toast.LENGTH_SHORT).show();
                    cancleDialog(dialog, false);
                    return;
                }

                urlParams = "?year=" + year + "&investigateDirection=" + direction + "&startStake=" + startStake + "&endStake=" + endStake
                        + "&routeCode=" + routeCode + "&laneType=" + lane;
                HttpclientUtil.getData(EvaluationSummaryActivity.this, url + urlParams, handler, 0);

            }
        });

        firstSpinner.attachDataSource(fristData);
        secondSpinner.attachDataSource(secondData);
        thirdSpinner.attachDataSource(thirdData);
        fourthSpinner.attachDataSource(fourthData);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancleDialog(dialog, true);
            }
        });
        deptDialog = builder.create();
        deptDialog.show();
    }

    private void cancleDialog(DialogInterface dialog, boolean isShow) {
        try {
            Field mShowing = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            mShowing.setAccessible(true);
            mShowing.set(dialog, isShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
