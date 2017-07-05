package com.chengtech.imooc_festsms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class ChooseMsgActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_msg);

        initView();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.choose_lv);

    }

}
