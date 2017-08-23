package com.chengtech.chengtechmt.activity.business;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.adapter.business.DiseaseRegAdapter;
import com.chengtech.chengtechmt.entity.business.DiseaseRegistration;
import com.chengtech.chengtechmt.util.ObjectSaveUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DiseaseRegistrationListActivity extends AppCompatActivity {
    public static final String DISEASE_REGISTRATION_LIST = "disease_registration_list";
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<DiseaseRegistration> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_registration_list);
        EventBus.getDefault().register(this);
        initView();
        initDataAndEvent();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        int pos = messageEvent.position;
        DiseaseRegistration diseaseRegistration = messageEvent.t;
        if (messageEvent.isNew) {
            data.add(pos, diseaseRegistration);
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            data.set(pos, diseaseRegistration);
            recyclerView.getAdapter().notifyItemChanged(pos);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        if (recyclerView != null) {
//            data = (List<DiseaseRegistration>) ObjectSaveUtils.getObject(this, DISEASE_REGISTRATION_LIST);
//            recyclerView.setAdapter(new DiseaseRegAdapter(this, data));
//
//        }
//    }


    private void initDataAndEvent() {
        toolbar.setTitle("病害登记");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiseaseRegistrationActivity.startAction(DiseaseRegistrationListActivity.this, null, -1);
            }
        });

        data = (List<DiseaseRegistration>) ObjectSaveUtils.getObject(this, DISEASE_REGISTRATION_LIST);
        if (data == null) {
            data = new ArrayList<>();
        }
        recyclerView.setAdapter(new DiseaseRegAdapter(this, data));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public static class MessageEvent {
        public int position;
        public DiseaseRegistration t;
        public boolean isNew;

        public MessageEvent(DiseaseRegistration t, int position, boolean isNew) {
            this.position = position;
            this.t = t;
            this.isNew = isNew;
        }
    }

}
