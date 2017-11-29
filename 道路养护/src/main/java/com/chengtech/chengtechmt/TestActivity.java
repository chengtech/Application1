package com.chengtech.chengtechmt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengtech.chengtechmt.view.MyHorizontalScrollView2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity {

    TextView tv;
    ImageView iv1,iv2,iv3;
    LinearLayout hideLayout;
    private int hideLayoutHeight;
    RecyclerView recyclerView;
MyHorizontalScrollView2 scrollView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("url",
//                "http://14.23.99.106:8666/map/index.html");
//        startActivity(intent);
//        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://14.23.99.106:8666/map/index.html?type=qy&data=[{\"code\":\"S111\",\"from\":10.574,\"to\":20.363,\"color\":\"0x00fffff\",\"width\":4,\"里程起点\":\"K10+574\",\"里程终点\":\"K20+363\",\"管理单位\":\"南城养护所\",\"养护单位\":\"东片养护站\",\"长度\":\"9.789\",\"路面均宽\":\"60\",\"路面类型\":\"沥青路面\",\"技术等级\":\"一级公路\"}]&sort=里程起点,里程终点,管理单位,养护单位,长度,路面均宽,路面类型,技术等级&roadalph=0&menu=true"));
//        intent2.addCategory(Intent.CATEGORY_DEFAULT);
//        startActivity(intent2);
//        Intent installIntent = new Intent("android.intent.action.VIEW");
//        installIntent.setData(Uri
//                .parse("market://details?id=com.adobe.flashplayer"));
//        startActivity(installIntent);
//        tv = (TextView) findViewById(R.id.tv);
//        iv1 = (ImageView) findViewById(R.id.img1);
//        iv2 = (ImageView) findViewById(R.id.img2);
//        iv3 = (ImageView) findViewById(R.id.img3);
//        Picasso.with(this).load(R.mipmap.banner1).placeholder(null).transform(new RoundeCornerTransformation()).into(iv1);
//        Picasso.with(this).load(R.mipmap.login_bg2).placeholder(null).transform(new RoundeCornerTransformation()).into(iv2);
//        Picasso.with(this).load(R.mipmap.login_bg2).placeholder(null).transform(new RoundeCornerTransformation()).into(iv3);
//        String str = "http://www.baidu.com";
//        SpannableString spannableString = new SpannableString(str);
//
//        spannableString.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Toast.makeText(TestActivity.this, "打开一个网址", Toast.LENGTH_SHORT).show();
//            }
//        },
//                0,
//                str.length(),
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
//
//        );
//
//        spannableString.setSpan(new ForegroundColorSpan(Color.RED),5,str.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv.setText(spannableString);
//        tv.append("\n");
//        tv.append("哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈");
//        tv.append("\n");
//        String str2 = "http://google.com";
//        SpannableString spannableString2 = new SpannableString(str2);
//        spannableString2.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//                Toast.makeText(TestActivity.this, "google", Toast.LENGTH_SHORT).show();
//            }
//        },0,str2.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv.append(spannableString2);
//        tv.setMovementMethod(LinkMovementMethod.getInstance());


        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
        float density = getResources().getDisplayMetrics().density;
        hideLayoutHeight = (int) (120*density+0.5);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        scrollView2 = (MyHorizontalScrollView2) findViewById(R.id.scrollView2);
        initData();

    }

    public void onClick(View view) {

        try {
            String address = "/storage/emulated/0/Download/养护ip.txt";
            File file = new File(address);
            if (file.exists()) {
                Log.i("tag",address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        File file = new File("file:///storage/emulated/0/Download/%E5%85%BB%E6%8A%A4ip.txt");
//        String name = file.getName();
//        if (!file .exists()) {
//            Uri uri = Uri.parse("file:///storage/emulated/0/Download/%E5%85%BB%E6%8A%A4ip.txt");
//            String type = CommonUtils.getMIMEType(name);
//            String type2 = CommonUtils.getMIMEType("4545.jpg");
//            String type3 = CommonUtils.getMIMEType("6767.pdf");
//            String type4 = CommonUtils.getMIMEType("6767.doc");
//            intent.setDataAndType(uri, "text/plain");
//            startActivity(intent);
//        }else {
//            Toast.makeText(this, "文件不存在或者已修改", Toast.LENGTH_SHORT).show();
//        }


//        if (hideLayout.getVisibility() == View.GONE) {
//            showLayout(hideLayout);
//        }else {
//            hideLayout(hideLayout);
//        }

    }

    private void hideLayout(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private void showLayout(View view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator anima = createDropAnimator(view,0,hideLayoutHeight);
        anima.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

    public void initData(){
        String a="abcdefghijklmnopqrstuvwxyz";
        String[] nums=a.split("");
        List<List<String>> data = new ArrayList<>();
        for (int i=0;i<nums.length;i++) {
            List<String> row = new ArrayList<>();
            for (int j=0;j<20;j++) {
//                row[j] = nums[i]+j;
                row.add(nums[i]+j);
            }
            data.add(row);
        }

//        HorizotalScrollAdapter adapter = new HorizotalScrollAdapter(this,data);
//        adapter.onItemClick(new HorizotalScrollAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(View parent, int position) {
//                Toast.makeText(TestActivity.this, "位置"+position, Toast.LENGTH_SHORT).show();
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scrollView2.setData(data);
    }
}
