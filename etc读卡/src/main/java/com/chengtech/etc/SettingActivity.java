package com.chengtech.etc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private Button button;
    private EditText host_ip_et, host_port_et;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();

        preferences = getSharedPreferences("host", MODE_PRIVATE);
        String host_ip = preferences.getString("host_ip", "192.1.40.44");
        int host_port = preferences.getInt("host_port", 51706);
        host_ip_et.setText(host_ip);
        host_port_et.setText(host_port+"");

    }

    private void initView() {
        button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = preferences.edit();
                String ip = host_ip_et.getText().toString().trim();
                String port = host_ip_et.getText().toString().trim();

                if (TextUtils.isEmpty(ip)) {
                    Toast.makeText(SettingActivity.this, "主机ip地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(port)) {
                    Toast.makeText(SettingActivity.this, "主机端口号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                edit.putString("host_ip", host_ip_et.getText().toString().trim());
                edit.putInt("host_port", Integer.parseInt(host_port_et.getText().toString().trim()));
                edit.commit();
                setResult(0x22);
                finish();
            }
        });

        host_ip_et = (EditText) findViewById(R.id.host_ip);
        host_port_et = (EditText) findViewById(R.id.host_port);
    }

}
