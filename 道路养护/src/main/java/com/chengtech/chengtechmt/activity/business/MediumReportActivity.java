package com.chengtech.chengtechmt.activity.business;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.entity.MediumPlanprogressItem;
import com.chengtech.chengtechmt.util.DateUtils;
import com.chengtech.chengtechmt.util.ViewHolder;
import com.chengtech.chengtechmt.view.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 小额专修的报表页面
 */
public class MediumReportActivity extends Activity implements MyHorizontalScrollView.OnMyScrollViewListener {

    //    public RecyclerView recyclerView;
    public ListView recyclerView;
    private MyHorizontalScrollView scrollView;
    private List<MyHorizontalScrollView> scrollViewList;
    public List<MediumPlanprogressItem> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_report);

        Intent intent = getIntent();
        data = (List<MediumPlanprogressItem>) intent.getSerializableExtra("data");
        initView();

        initData();
        initEvent();
    }


    private void initData() {
        MediumReportAdapter adapter = new MediumReportAdapter(this, data);
        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initEvent() {
        scrollView.SetOnMyScrollViewListener(this);
    }

    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView = (ListView) findViewById(R.id.recycleview);
        scrollView = (MyHorizontalScrollView) findViewById(R.id.scrollView_maintitle);
        scrollViewList = new ArrayList<>();
        // 把第一个scrollView加进集合
        scrollViewList.add(scrollView);
    }


    //内部adapter类
//    public class MediumReportAdapter extends RecyclerView.Adapter<MediumReportAdapter.MyViewHolder> {
//
//        public LayoutInflater inflater;
//        List<MediumPlanprogressItem> data;
//
//        public MediumReportAdapter(Context context, List<MediumPlanprogressItem> data) {
//            this.inflater = LayoutInflater.from(context);
//            this.data = data;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = inflater.inflate(R.layout.item_medium_report, parent, false);
//            MyViewHolder holder = new MyViewHolder(view);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            MediumPlanprogressItem item = data.get(position);
//            holder.tv0.setText(item.projectName);
//            holder.tv1.setText(item.subitemType);
//            holder.tv2.setText(item.routeCode);
//            holder.tv3.setText(item.startPeg);
//            holder.tv4.setText(item.endPeg);
//            holder.tv5.setText(item.upOrDown);
//            holder.tv6.setText(item.maintainLength);
//            holder.tv7.setText(item.projectContent);
//            holder.tv8.setText(item.budgetFund);
//            holder.tv9.setText(item.replyFund);
//            holder.tv10.setText(item.paidFund);
//            holder.tv11.setText(item.notPaidFund);
//            holder.tv12.setText(item.beginDate);
//            holder.tv13.setText(item.finishDate);
//            holder.tv14.setText(item.progressSituation);
//            holder.tv15.setText(item.checkSituation);
//            holder.tv16.setText(item.memo);
//            holder.tv17.setText(item.projectNum);
//        }
//
//        @Override
//        public int getItemCount() {
//            return data == null ? 0 : data.size();
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView tv0, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8,
//                    tv9, tv10, tv11, tv12, tv13, tv14, tv15, tv16,tv17;
//            public MyHorizontalScrollView scrollView;
//
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                tv0 = (TextView) itemView.findViewById(R.id.tv0);
//                tv1 = (TextView) itemView.findViewById(R.id.tv1);
//                tv2 = (TextView) itemView.findViewById(R.id.tv2);
//                tv3 = (TextView) itemView.findViewById(R.id.tv3);
//                tv4 = (TextView) itemView.findViewById(R.id.tv4);
//                tv5 = (TextView) itemView.findViewById(R.id.tv5);
//                tv6 = (TextView) itemView.findViewById(R.id.tv6);
//                tv7 = (TextView) itemView.findViewById(R.id.tv7);
//                tv8 = (TextView) itemView.findViewById(R.id.tv8);
//                tv9 = (TextView) itemView.findViewById(R.id.tv9);
//                tv10 = (TextView) itemView.findViewById(R.id.tv10);
//                tv11 = (TextView) itemView.findViewById(R.id.tv11);
//                tv12 = (TextView) itemView.findViewById(R.id.tv12);
//                tv13 = (TextView) itemView.findViewById(R.id.tv13);
//                tv14 = (TextView) itemView.findViewById(R.id.tv14);
//                tv15 = (TextView) itemView.findViewById(R.id.tv15);
//                tv16 = (TextView) itemView.findViewById(R.id.tv16);
//                tv17 = (TextView) itemView.findViewById(R.id.tv17);
//                scrollView = (MyHorizontalScrollView) itemView.findViewById(R.id.scrollView_item);
//                addHView(scrollView);
//            }
//        }
//    }

    public class MediumReportAdapter extends BaseAdapter {
        public LayoutInflater inflater;
        public List<MediumPlanprogressItem> data;

        public MediumReportAdapter(Context context, List<MediumPlanprogressItem> data) {
            this.inflater = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MediumPlanprogressItem item = data.get(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_medium_report, parent, false);
            }
            TextView tv0 = ViewHolder.get(convertView, R.id.tv0);
            TextView tv1 = ViewHolder.get(convertView, R.id.tv1);
            TextView tv2 = ViewHolder.get(convertView, R.id.tv2);
            TextView tv3 = ViewHolder.get(convertView, R.id.tv3);
            TextView tv4 = ViewHolder.get(convertView, R.id.tv4);
            TextView tv5 = ViewHolder.get(convertView, R.id.tv5);
            TextView tv6 = ViewHolder.get(convertView, R.id.tv6);
            TextView tv7 = ViewHolder.get(convertView, R.id.tv7);
            TextView tv8 = ViewHolder.get(convertView, R.id.tv8);
            TextView tv9 = ViewHolder.get(convertView, R.id.tv9);
            TextView tv10 = ViewHolder.get(convertView, R.id.tv10);
            TextView tv11 = ViewHolder.get(convertView, R.id.tv11);
            TextView tv12 = ViewHolder.get(convertView, R.id.tv12);
            TextView tv13 = ViewHolder.get(convertView, R.id.tv13);
            TextView tv14 = ViewHolder.get(convertView, R.id.tv14);
            TextView tv15 = ViewHolder.get(convertView, R.id.tv15);
            TextView tv16 = ViewHolder.get(convertView, R.id.tv16);
            TextView tv17 = ViewHolder.get(convertView, R.id.tv17);
            MyHorizontalScrollView scrollView = ViewHolder.get(convertView, R.id.scrollView_item);
            addHView(scrollView);

            if (item!=null) {
                tv0.setText(item.projectName);
                tv1.setText(item.subitemType);
                tv2.setText(item.routeCode);
                tv3.setText(item.startPeg);
                tv4.setText(item.endPeg);
                tv5.setText(item.upOrDown);
                tv6.setText(item.maintainLength);
                tv7.setText(item.projectContent);
                tv8.setText(item.budgetFund);
                tv9.setText(item.replyFund);
                tv10.setText(item.paidFund);
                tv11.setText(item.notPaidFund);
                tv12.setText(item.beginDate==null?"": DateUtils.convertDate2(item.beginDate));
                tv13.setText(item.finishDate==null?"": DateUtils.convertDate2(item.finishDate));
                tv14.setText(item.progressSituation);
                tv15.setText(item.checkSituation);
                tv16.setText(item.memo);
                tv17.setText(item.projectNum);
            }
            return convertView;
        }
    }

    private void addHView(final MyHorizontalScrollView sv) {
        if (!scrollViewList.isEmpty()) {
            int size = scrollViewList.size();
            MyHorizontalScrollView hscrollView = scrollViewList.get(size - 1);
            final int scrollX = hscrollView.getScrollX();
            if (scrollX != 0) {
                recyclerView.post(new Runnable() {

                    @Override
                    public void run() {
                        sv.scrollTo(scrollX, 0);

                    }

                });
            }
        }
        sv.SetOnMyScrollViewListener(this);
        scrollViewList.add(sv);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        for (MyHorizontalScrollView scrollView : scrollViewList) {
            scrollView.smoothScrollTo(l, t);
        }
    }
}
