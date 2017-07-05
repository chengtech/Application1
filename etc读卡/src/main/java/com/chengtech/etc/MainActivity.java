package com.chengtech.etc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.recharge.ObuInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import etc.obu.data.CardConsumeRecord;
import etc.obu.data.CardInformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity implements Runnable {
    private static final int STATE_CARD_INFO_READ_OK = 4;
    private static final int STATE_CARD_INFO_READ_FAIL = 5;
    private static final int STATE_CONNECT_HOST_FAILED = 8;
    private static final int STATE_CONNECT_HOST_OK = 9;
    private String HOST_IP;
    private int HOST_PORT;
    private static final int STATE_RESULT_FROM_HOST_OK = 6;
    private static final int STATE_HOST_RESPONE_FAIL = 7;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Button read_card_info;
    private Button read_consume_recorder;
    private Button entry_et, exit_et, police_et;
    private TextView result_show;
    private String resultString;
    private int ret = 0;
    private SweetAlertDialog sweetAlertDialog;
    private CardConsume cardConsume;
    private ObuInterface mObuInterface = null;
    private int mCosNums;
    private Socket socket;
    private CardInformation cardInfo;
    private MenuItem netWork_menu;

    private String[] mCosCmds = new String[10];
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tip = (String) msg.obj;
            switch (msg.what) {
                case STATE_CARD_INFO_READ_OK:
                    result_show.setText(Html.fromHtml(resultString));
                    if (sweetAlertDialog != null) {
                        sweetAlertDialog.setTitleText("成功").setContentText(tip).setConfirmClickListener(null).setConfirmText("确定")
                                .setCancelText(null).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                    break;
                case STATE_CARD_INFO_READ_FAIL:
                    result_show.setText(Html.fromHtml("<font color=red>读取失败</font>"));
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        sweetAlertDialog.setTitleText("失败").setContentText(tip).setConfirmText("确定").setConfirmClickListener(null)
                                .setCancelText(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    } else {
                        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("错误");
                        sweetAlertDialog.setContentText(tip);
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();
                    }
                    break;
                case STATE_RESULT_FROM_HOST_OK:
//                    this.removeMessages(STATE_HOST_RESPONE_FAIL);
                    this.removeMessages(STATE_CONNECT_HOST_FAILED);
                    result_show.setText(tip);
                    CardInformation cardInformation = new CardInformation();
                    mObuInterface.getCardInformation(cardInformation);
                    if (!cardInfo.getCardId().equals(cardInformation.getCardId())) {
                        MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "不是同一张卡片，写入失败");
                        return;
                    }

                    if (tip.toUpperCase().equals("FF")) {
                        MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "计算交易出错");
                        return;
                    }
                    if (!TextUtils.isEmpty(tip)) {
                        final String[] split = tip.split("\\+");
                        //01表示是写入口信息

                        if ("01".equals(split[0])) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    int money = 0;
                                    String str0019 = split[2];
                                    String timeStampNow = split[1];
                                    CardConsumeResult result = new CardConsumeResult();
                                    //把接收到的信息，执行消费金额为0的扣费并且写到0019文件上
                                    // TODO: 2017/4/17
                                    ret = cardConsume.cardConsume(CardConsume.CONSUME_METHOD_COMPLEX, CardConsume.CHANNEL_TYPE_DSRC,
                                            0, new ConfigBean(), cardInfo, money, str0019, result, timeStampNow);

                                    if (ret != 0) {
                                        MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "写卡失败");
                                        return;
                                    }
                                    //把tac，mac1码之类的发送到服务器
                                    // TODO: 2017/4/17
                                    String data = "0102+" + result.getPsamTerminateID() + "+" + result.getIccPaySerial()
                                            + "+" + result.getPsamPaySerial() + "+" + result.getTac();
                                    sendMsgToHost(data);
                                    MainActivity.this.sendMessage(STATE_CARD_INFO_READ_OK, "入口信息写入完成");
                                }
                            }).start();
                        } else if ("02".equals(split[0])) {
                            final int money = Integer.parseInt(split[2]);
                            final String str0019 = split[3];
                            final String timeStampNow = split[1];
                            final CardConsumeResult result = new CardConsumeResult();
                            if (sweetAlertDialog != null) {
                                sweetAlertDialog.setTitleText("本次消费：" + (money / 100.0) + "元").setContentText("是否扣费？")
                                        .setConfirmText("确定").setCancelText("取消").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //把接收到的信息，执行消费金额为0的扣费并且写到0019文件上
                                                // TODO: 2017/4/17
                                                ret = cardConsume.cardConsume(CardConsume.CONSUME_METHOD_COMPLEX, CardConsume.CHANNEL_TYPE_DSRC,
                                                        0, new ConfigBean(), cardInfo, money, str0019, result, timeStampNow);

                                                if (ret != 0) {
                                                    MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "写卡失败");
                                                    return;
                                                }

                                                //把tac码之类的发送到服务器
                                                // TODO: 2017/4/17
                                                String data = "0202+" + result.getPsamTerminateID() + "+" + result.getIccPaySerial()
                                                        + "+" + result.getPsamPaySerial() + "+" + result.getTac();
                                                sendMsgToHost(data);
                                                MainActivity.this.sendMessage(STATE_CARD_INFO_READ_OK, "扣费成功");
                                            }
                                        }).start();
                                    }
                                }).changeAlertType(SweetAlertDialog.WARNING_TYPE);

                            }

                        }else {
                            MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "获取到的信息不符合写入要求！");
                        }
                    }
                    break;
                case STATE_HOST_RESPONE_FAIL:
                    if (sweetAlertDialog != null) {
                        int alerType = sweetAlertDialog.getAlerType();
                        if (alerType == SweetAlertDialog.PROGRESS_TYPE) {
                            MainActivity.this.sendMessage(STATE_CARD_INFO_READ_FAIL, "服务器无反应，请重新操作。");
                        }
                    }
                    break;

                case STATE_CONNECT_HOST_FAILED:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        sweetAlertDialog.setTitleText("服务器响应超时").setConfirmText("确定").setConfirmClickListener(null)
                                .setCancelText(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);

                    } else {
                        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("连接错误");
                        sweetAlertDialog.setContentText("错误原因：\n    1.服务器中断。\n    2.端口号或者ip地址不正确。");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();
                    }
                    if (netWork_menu != null) {
                        netWork_menu.setIcon(R.drawable.ic_network_failed);
                    }
                    break;

                case STATE_CONNECT_HOST_OK:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        sweetAlertDialog.setTitleText("连接服务器成功").setConfirmText("确定").setConfirmClickListener(null)
                                .setCancelText(null).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                    } else {
                        sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("连接服务器成功");
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.show();
                    }
                    if (netWork_menu != null) {
                        netWork_menu.setIcon(R.drawable.ic_network_ok);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mObuInterface = new ObuInterface(this);
        cardConsume = new CardConsume(mObuInterface);
        mObuInterface.dsrcOpen();
        SharedPreferences preferences = getSharedPreferences("host", MODE_PRIVATE);
        HOST_IP = preferences.getString("host_ip", "192.1.40.44");
        HOST_PORT = preferences.getInt("host_port", 51706);
        new Thread(new Runnable() {
            @Override
            public void run() {
                createSocketClinet();
            }
        }).start();
        initView();
    }

    /**
     * 创建socket实例
     */
    private void createSocketClinet() {
        try {
            if (socket != null)
                socket.close();
            socket = null;
            socket = new Socket(HOST_IP, HOST_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            //启动线程，接收服务器发送过来的数据
            new Thread(MainActivity.this).start();
            sendMessage(STATE_CONNECT_HOST_OK, "");
            //启动线程，来监听连接状况，发送心跳包
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        socket.sendUrgentData(0xff);
//                        handler.postDelayed(this, 5000);
//
//                    } catch (Exception e) {
//                        try {
//                            socket.close();
//                            sendMessage(STATE_HOST_RESPONE_FAIL, "服务器中断连接");
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    createSocketClinet();
//                                }
//                            }).start();
//                        } catch (IOException e1) {
//                            e1.printStackTrace();
//                        }
//
//
//                    }
//                }
//            }).start();

        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(STATE_CONNECT_HOST_FAILED, "");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mObuInterface.dsrcClose();

        try {
            if (socket!=null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initView() {
        read_card_info = (Button) findViewById(R.id.read_card_info);
        read_consume_recorder = (Button) findViewById(R.id.read_consume_recorder);
        result_show = (TextView) findViewById(R.id.textShow);
        entry_et = (Button) findViewById(R.id.entry);
        exit_et = (Button) findViewById(R.id.exit);
        police_et = (Button) findViewById(R.id.police);
        entry_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeEntryInfomation();
            }
        });

        exit_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeExitInfoAndConsume();
            }
        });
        read_card_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readCardInformation();
            }
        });

        read_consume_recorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readConsumeRecorder();
            }
        });

        police_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socket == null) {
                    sendMessage(STATE_CONNECT_HOST_FAILED, "");
                    return;
                }
                sweetAlertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                sweetAlertDialog.setTitleText("军警免费通行！");
                sweetAlertDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMsgToHost("03");
                    }
                }).start();

            }
        });
    }

    //写出口信息，同时进行扣费
    private void writeExitInfoAndConsume() {
        if (socket == null) {
            sendMessage(STATE_CONNECT_HOST_FAILED, "");
            return;
        }
        cardInfo = null;
        //弹出进度框
        showLoadingDialog("正在查询扣费...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cardInfo = new CardInformation();
                    ret = mObuInterface.getCardInformation(cardInfo);
                    if (ret != 0) {
                        sendMessage(STATE_CARD_INFO_READ_FAIL, "读取卡片信息失败");
                        return;
                    }
                    String file0015[] = new String[1];
                    String file0019[] = new String[1];
                    String file0002[] = new String[1];
                    ret = cardConsume.readICC(CardConsume.CHANNEL_TYPE_DSRC, 0, file0015, file0019, file0002, cardInfo);
                    if (ret != 0) {
                        sendMessage(STATE_CARD_INFO_READ_FAIL, "读取卡片信息失败");
                        return;
                    }

                    resultString = file0019[0].toString();
                    //读取成功后把0015,0019文件发送到服务器
                    // TODO: 2017/4/17
                    String data = "0201+" + file0015[0] + "+" + file0019[0];
                    sendMsgToHost(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).run();

    }

    //写入口信息
    private void writeEntryInfomation() {

        if (socket == null) {
            sendMessage(STATE_CONNECT_HOST_FAILED, "");
            return;
        }
        cardInfo = null;
        //弹出进度框
        showLoadingDialog("正在写入口信息...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cardInfo = new CardInformation();
                    ret = mObuInterface.getCardInformation(cardInfo);
                    if (ret != 0) {
                        sendMessage(STATE_CARD_INFO_READ_FAIL, "读取卡片信息失败");
                        return;
                    }
                    //读取0015文件，
                    String file0015[] = new String[1];
                    int readICC0015Length = 43;
                    mCosNums = 0;
                    mCosCmds[mCosNums++] = String.format("00B09500%02x", (byte) readICC0015Length); //指令1：00B095002B 读取IC Card 0015文件43个字节
                    ret = cardConsume.cosChannel(CardConsume.CHANNEL_TYPE_DSRC, 0, mCosNums, mCosCmds);
                    if (ret != 0) {
                        sendMessage(STATE_CARD_INFO_READ_FAIL, "读取0015卡片失败");
                        return;
                    }

                    file0015[0] = mCosCmds[0].substring(0, mCosCmds[0].length() - 4);
                    resultString = file0015[0].toString();
                    //读取成功后把0015文件发送到服务器
                    // TODO: 2017/4/17
                    String data = "0101+" + file0015[0];
                    sendMsgToHost(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }

    //弹出进度框
    private void showLoadingDialog(String contentText) {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText(contentText);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        handler.sendEmptyMessageDelayed(STATE_CONNECT_HOST_FAILED, 15000);
//        handler.sendEmptyMessageDelayed(STATE_HOST_RESPONE_FAIL, 15000);
    }

    //读取消费记录
    private void readConsumeRecorder() {
        try {
            result_show.setText("读取消费记录...");
            resultString = "";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {

                            List<CardConsumeRecord> cardRecordList = new ArrayList<CardConsumeRecord>();
                            int result1 = mObuInterface
                                    .readCardConsumeRecord(0, cardRecordList);
                            if (result1 == 0) {
                                resultString = "";
                                for (int i = 0; i < cardRecordList.size(); i++) {
                                    resultString += " <font color=blue>第" + (i + 1) + "次消费</font>"
                                            + "<br/>入/出口收费路网号: " + cardRecordList.get(i).getTollRoadNetworkId()
                                            + "<br/>入/出口收费站号 " + cardRecordList.get(i).getTollStationId()
                                            + "<br/>入/出口收费车道号: " + cardRecordList.get(i).getTollLaneId()
                                            + "<br/>入/出口时间: " + cardRecordList.get(i).getTimeUnix()
                                            + "<br/>车型: " + cardRecordList.get(i).getvehicleModel()
                                            + "<br/>车牌号码: " + cardRecordList.get(i).getVehicleNumber()
                                            + "<br/>入/出口状态: " + cardRecordList.get(i).getPassStatus() + "<br/>";
                                }
                                sendMessage(STATE_CARD_INFO_READ_OK, "读取卡片信息成功");
                            } else {
                                sendMessage(STATE_CARD_INFO_READ_FAIL, "读取失败");
                            }
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL, "读取失败");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取ic卡片信息
    private void readCardInformation() {
        try {
            result_show.setText("读卡...");
            resultString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {
                            resultString = "";
                            resultString = "卡号: " + cardInfo.getCardId()
                                    + "<br/>卡片类型: " + cardInfo.getCardType()
                                    + "<br/>卡片版本号: " + cardInfo.getCardVersion()
                                    + "<br/>卡片类型: " + cardInfo.getCardType()
                                    + "<br/>发卡方标识: " + cardInfo.getProvider()
                                    + "<br/>启用时间: " + cardInfo.getSignedDate()
                                    + "<br/>到期时间: " + cardInfo.getExpiredDate()
                                    + "<br/>车牌号码: " + cardInfo.getVehicleNumber()
                                    + "<br/>车牌颜色: " + cardInfo.getPlateColor()
                                    + "<br/>车型: " + cardInfo.getVehicleModel()
                                    + "<br/><font color=blue size=20>余额: " + String.valueOf(cardInfo.getBalance() / 100.0) + " (元)</font>";
                            sendMessage(STATE_CARD_INFO_READ_OK, "读取卡片信息成功");
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL, "读取失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            mObuInterface = new ObuInterface(this);
            cardConsume = new CardConsume(mObuInterface);
            mObuInterface.dsrcOpen();
        }

        if (resultCode == 0x22 && requestCode == 0x01) {
            //重新创建socket
            SharedPreferences preferences = getSharedPreferences("host", MODE_PRIVATE);
            HOST_IP = preferences.getString("host_ip", "10.1.1.1");
            HOST_PORT = preferences.getInt("host_port", 51706);
            socket = null;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createSocketClinet();
                }
            }).start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendMessage(int flag, String tip) {
        Message msg = Message.obtain();
        msg.what = flag;
        msg.obj = tip;
        handler.sendMessage(msg);
//        handler.sendMessageDelayed(msg, 1000);
    }

    private void sendMsgToHost(String msg) {
        try {
            if (socket == null)
                createSocketClinet();
            if (socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println(msg);
                    out.flush();
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void run() {
        try {
            String content = null;
//            InputStream inputStream = socket.getInputStream();
//            byte[] buff = new byte[1024*4];
//            int len = 0;
//            while ((len = inputStream.read(buff)) != -1) {
//                content = new String(buff, 0, len);
//                Log.i("tag", content);
//                content = content.replace("\r\n","");
//                sendMessage(STATE_RESULT_FROM_HOST_OK, content);
//            }
            while ((content = in.readLine()) != null) {
                sendMessage(STATE_RESULT_FROM_HOST_OK, content);
            }
//            while (true) {
//
//                if (!socket.isClosed()) {
//                    if (socket.isConnected()) {
//                        if (!socket.isInputShutdown()) {
//                            if ((content = in.readLine()) != null) {
//                                if (!TextUtils.isEmpty(content)) {
//
//                                }
//
//                            }
//                        }
//                    }
//                }
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        netWork_menu = menu.findItem(R.id.network);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, 0x01);
            return true;
        } else if (itemId == R.id.network) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    createSocketClinet();
                }
            }).start();
        }
        return super.onOptionsItemSelected(item);
    }


}
