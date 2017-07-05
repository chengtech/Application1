package com.chengtech.etcdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.recharge.ObuInterface;
import com.genvict.bluetooth.manage.ConfigFile;

import java.util.ArrayList;
import java.util.List;

import etc.obu.data.CardConsumeRecord;
import etc.obu.data.CardInformation;
import etc.obu.data.CardOwner;
import etc.obu.data.CardTransactionRecord;
import etc.obu.data.DeviceInformation;
import etc.obu.data.DsrcConfig;
import etc.obu.data.ObuSystemInformation;
import etc.obu.data.ServiceStatus;
import etc.obu.util.XData;

public class MainActivity extends Activity {

    private ObuInterface mObuInterface = null;
    Context mContext = null;

    private Button card_information;
    private Button card_owner;
    private Button obu_information;
    private Button transaction_record;
    private Button consume_record;
    private Button credit_for_load;


    private TextView result_txt;


    private final int STATE_CARD_INFO_READ_OK = 4;
    private final int STATE_CARD_INFO_READ_FAIL = 5;
    private final int STATE_OBU_INFO_READ_OK = 6;
    private final int STATE_OBU_INFO_READ_FAIL = 7;
    private final int STATE_CREDIT_FOR_LOAD_OK = 8;
    private final int STATE_CREDIT_FOR_LOAD_FAIL = 9;
    private final int STATE_AUTHORIZE_STEP_1_OK = 11;
    private final int STATE_AUTHORIZE_STEP_1_FAIL = 12;
    private final int STATE_AUTHORIZE_STEP_2_OK = 13;
    private final int STATE_AUTHORIZE_STEP_2_FAIL = 14;
    private final int STATE_SESSION_KEY = 15;
    private final int STATE_CREDIT_STEP_1_OK = 16;
    private final int STATE_CREDIT_STEP_1_FAIL = 17;
    private final int STATE_CREDIT_STEP_2_OK = 18;
    private final int STATE_CREDIT_STEP_2_FAIL = 19;
    private final int STATE_NO_FIND_DEVICE = 20;
    private final int STATE_FAILURE = 21;


    private boolean mProcessing = false;
    private String mConnectFailString = "";
    private String mCardInfoString = "";
    private String mObuInfoString = "";
    private String mCreditString = "";
    private String mAuthorizeString = "";
    // private String mCardId = "";
    private String line_feed = "\r\n";

    private String sInitializeForLoadUrl = "";


    private int time = 0;
    private boolean bOnOff_CheckCard = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.test_exdevice);


            mObuInterface = new ObuInterface(this);
            mObuInterface.dsrcOpen();

            card_information = (Button) findViewById(R.id.card_information);
//            card_owner = (Button) findViewById(R.id.card_owner);
            obu_information = (Button) findViewById(R.id.obu_information);
            transaction_record = (Button) findViewById(R.id.transaction_record);
            consume_record = (Button) findViewById(R.id.consume_record);
            credit_for_load = (Button) findViewById(R.id.credit_for_load);
            result_txt = (TextView) findViewById(R.id.result_txt);


            obu_information.setOnClickListener(new Button.OnClickListener() {

                public void onClick(View v) {
                    readDevInformation();

                }
            });

            card_information.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    readCardInformation();

                }
            });

//            card_owner.setOnClickListener(new Button.OnClickListener() {
//                public void onClick(View v) {
//                    readObuSysInformation();
//
//                }
//            });
//
            transaction_record.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
//                    readObuCardInformation();
                    readTransactionRecord();
                }
            });
//
            consume_record.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
//                    readObuRelease();
                    readConsumeRecord();
                }
            });
//
            credit_for_load.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
//                    creditForLoad();
                    cardConsume();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            mObuInterface = new ObuInterface(this);
            mObuInterface.dsrcOpen();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        // mExDevice.release();

        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        mObuInterface.dsrcClose();

    }


    @SuppressLint("HandlerLeak")
    private Handler mMessageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                processMessage(msg.what);
            }
            super.handleMessage(msg);
        }

        ;
    };

    private void sendMessage(int flag) {
        Message msg = Message.obtain();
        msg.what = flag;
        mMessageHandler.sendMessage(msg);
    }

    private void processMessage(int msg) {

        switch (msg) {

            case STATE_CARD_INFO_READ_OK:
                setResultText(true, mCardInfoString);
                break;

            case STATE_CARD_INFO_READ_FAIL:
                setResultText(false, "读卡失败");
                break;

            case STATE_OBU_INFO_READ_OK:
                setResultText(true, mObuInfoString);
                break;

            case STATE_OBU_INFO_READ_FAIL:
                setResultText(false, "读设备信息失败");
                break;

            case STATE_CREDIT_FOR_LOAD_OK:
                setResultText(true, mCreditString);
                break;

            case STATE_CREDIT_FOR_LOAD_FAIL:
                setResultText(false, "圈存失败");
                break;

            case STATE_CREDIT_STEP_1_OK:
                setResultText(true, mCreditString);
                break;

            case STATE_CREDIT_STEP_1_FAIL:
                setResultText(false, "圈存初始化失败");
                break;

            case STATE_CREDIT_STEP_2_OK:
                setResultText(true, mCreditString);
                break;

            case STATE_CREDIT_STEP_2_FAIL:
                setResultText(false, "圈存写卡失败");
                break;

            case STATE_AUTHORIZE_STEP_1_OK:

                setResultText(true, mAuthorizeString);
                break;

            case STATE_AUTHORIZE_STEP_1_FAIL:

                setResultText(false, mAuthorizeString);
                break;

            case STATE_AUTHORIZE_STEP_2_OK:

                setResultText(true, mAuthorizeString);
                break;

            case STATE_AUTHORIZE_STEP_2_FAIL:

                setResultText(false, mAuthorizeString);
                break;

            case STATE_SESSION_KEY:


                setResultText(true, mAuthorizeString);
                break;

            case STATE_FAILURE:
                setResultText(false, mCardInfoString);
                break;
            case 20:
                time++;
                if (time == 110) {
                    //			mObuInterface.doShakeHands((byte) 0);
                }
                setResultText(true, "" + time);
                break;
        }
    }

    private void setResultText(boolean ok, String s) {
        try {
            if (ok) {
                result_txt.setTextColor(getResources().getColor(
                        R.color.recharge_blue_txt));
            } else {
                result_txt.setTextColor(getResources().getColor(
                        R.color.recharge_error_txt));
            }
            // s = line_feed + "use_esam=" + use_esam.isChecked();
            // s += line_feed;
            result_txt.setText(s);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    Handler handler = new Handler();
    @SuppressLint("HandlerLeak")
//    Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//
//        }
//
//        ;
//    };
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            sendMessage(20);
//            handler.postDelayed(this, 1000);
//        }
//    };

    public class CardParameter {
        public String terminalNo;
        public String pinCode;
        public String procType;
        public String keyIndex;

        public CardParameter() {
            terminalNo = "";
            pinCode = "";
            procType = "";
            keyIndex = "";
        }
    }

    private void readCardInformation() {
        try {
            setResultText(true, "读卡...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {

                            mCardInfoString = "";
                            mCardInfoString += line_feed
                                    + cardInfo.CardInfoString();
                            sendMessage(STATE_CARD_INFO_READ_OK);
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readObuSysInformation() {
        try {
            setResultText(true, "读OBU系统信息...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        ObuSystemInformation sysInfo = new ObuSystemInformation();
                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface.findObu(sysInfo, cardInfo);
                        if (result == 0 && sysInfo.getStatus()) {

                            mCardInfoString = "运营商：" + sysInfo.getProvider();
                            mCardInfoString += line_feed + "标签号码:" + sysInfo.getSerialNumber();
                            mCardInfoString += line_feed + "有效期:" + sysInfo.getSignedDate() + "-" + sysInfo.getExpiredDate();

                            sendMessage(STATE_CARD_INFO_READ_OK);
                        } else {
                            mCardInfoString = "唤醒OBU失败！";
                            sendMessage(STATE_FAILURE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readObuCardInformation() {
        try {
            setResultText(true, "读OBU卡片信息...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        ObuSystemInformation sysInfo = new ObuSystemInformation();
                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface.obuReadCpuCard(sysInfo, cardInfo);
                        if (result == 0 && cardInfo.getStatus()) {

                            mCardInfoString = "";
                            mCardInfoString += line_feed
                                    + cardInfo.CardInfoString();

                            sendMessage(STATE_CARD_INFO_READ_OK);
                        } else {
                            mCardInfoString = "获取OBU卡片信息失败！";
                            sendMessage(STATE_FAILURE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readObuRelease() {
        try {
            setResultText(true, "读OBU卡片信息...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        int result = mObuInterface.obuRelease(true, true);
                        if (result == 0) {

                            mCardInfoString = "释放OBU资源成功！";
                            sendMessage(STATE_CARD_INFO_READ_OK);
                        } else {
                            mCardInfoString = "释放OBU资源失败！";
                            sendMessage(STATE_FAILURE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readCardOwner() {
        try {
            setResultText(true, "读持卡人...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {
                            CardOwner owner = new CardOwner();

                            String pinCode = "123456";
                            int result1 = mObuInterface
                                    .readCardOwnerRecord(pinCode, owner);
                            if (result1 == 0) {
                                mCardInfoString = "";
                                mCardInfoString += line_feed
                                        + owner.getString();
                                sendMessage(STATE_CARD_INFO_READ_OK);
                            } else {
                                sendMessage(STATE_CARD_INFO_READ_FAIL);
                            }
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readDevInformation() {
        try {
            setResultText(true, "读设备信息...");
            mObuInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        DeviceInformation obuInfo = mObuInterface
                                .getDeviceInformation();
                        {

                            mObuInfoString = "";
                            mObuInfoString += line_feed + "版本: "
                                    + obuInfo.Version;

                            mObuInfoString += line_feed + obuInfo.Battery;
                            mObuInfoString += line_feed + obuInfo.Sn;


                            sendMessage(STATE_OBU_INFO_READ_OK);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readTransactionRecord() {
        try {
            setResultText(true, "读交易记录...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {

                            String pinCode = "123456";

                            List<CardTransactionRecord> cardRecordList = new ArrayList<CardTransactionRecord>();
                            int result1 = mObuInterface
                                    .readCardTransactionRecord(pinCode,
                                            10, cardRecordList);
                            if (result1 == 0) {
                                mCardInfoString = "";
                                mCardInfoString += line_feed
                                        + "record.length = "
                                        + cardRecordList.size();
                                for (int i = 0; i < cardRecordList.size(); i++) {
                                    mCardInfoString += line_feed + "[" + i
                                            + "]";
                                    mCardInfoString += line_feed
                                            + cardRecordList.get(i).getString();
                                }
                                sendMessage(STATE_CARD_INFO_READ_OK);
                            } else {
                                sendMessage(STATE_CARD_INFO_READ_FAIL);
                            }
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readConsumeRecord() {
        try {
            setResultText(true, "读消费记录...");
            mCardInfoString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);
                        if (result == 0) {

                            List<CardConsumeRecord> cardRecordList = new ArrayList<CardConsumeRecord>();
                            int result1 = mObuInterface
                                    .readCardConsumeRecord(0, cardRecordList);
                            if (result1 == 0) {
                                mCardInfoString = "";
                                mCardInfoString += line_feed
                                        + "record.length = "
                                        + cardRecordList.size();
                                for (int i = 0; i < cardRecordList.size(); i++) {
                                    mCardInfoString += line_feed + "[" + i
                                            + "]";
                                    mCardInfoString += line_feed
                                            + cardRecordList.get(i).getString();
                                }

                                sendMessage(STATE_CARD_INFO_READ_OK);
                            } else {
                                sendMessage(STATE_CARD_INFO_READ_FAIL);
                            }
                        } else {
                            sendMessage(STATE_CARD_INFO_READ_FAIL);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void cardConsume() {
        CardConsume cardConsume = new CardConsume(mObuInterface);
        CardInformation cardInfo = new CardInformation();
        int result = mObuInterface
                .getCardInformation(cardInfo);
        if (result == 0) {
            cardConsume.cardConsume(CardConsume.CONSUME_METHOD_COMPLEX, 0,
                    0, new ConfigBean(), cardInfo, 1);
        }
    }

    private void creditForLoad() {
        try {
            setResultText(true, "圈存...");
            mCreditString = "";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mProcessing = true;

                        CardInformation cardInfo = new CardInformation();
                        int result = mObuInterface
                                .getCardInformation(cardInfo);

                        if (result == 0) {
                            int credit = 1;

//                            String terminalNo = "112233445566";
                            String terminalNo = cardInfo.getCardId();
                            String pinCode = "123456";
                            String procType = "02";
                            String keyIndex = "01";

                            ServiceStatus result1 = mObuInterface
                                    .loadCreditGetMac1(cardInfo.getCardId(),
                                            credit, terminalNo, pinCode,
                                            procType, keyIndex);

                            if (result1.getServiceCode() == 0) {
                                mCreditString = "";
                                mCreditString += line_feed + "圈存初始化金额："
                                        + credit;
                                mCreditString += line_feed + "圈存初始化返回："
                                        + result1.getServiceInfo();

                                sInitializeForLoadUrl = result1
                                        .getServiceInfo();

                                String sDateMac2 = mObuInterface.FormDataMac2(
                                        cardInfo.getCardId(), terminalNo,
                                        procType, XData.urlFindValueByKey(
                                                sInitializeForLoadUrl, "a_pt"),
                                        XData.urlFindValueByKey(
                                                sInitializeForLoadUrl, "a_rnd"),
                                        XData.urlFindValueByKey(
                                                sInitializeForLoadUrl, "a_cbb"),
                                        XData.urlFindValueByKey(
                                                sInitializeForLoadUrl, "a_m1"),
                                        XData.urlFindValueByKey(
                                                sInitializeForLoadUrl, "a_on"));

                                ServiceStatus result2 = mObuInterface
                                        .loadCreditWriteCard(sDateMac2);
                                if (result2.getServiceCode() == 0) {
                                    mCreditString = "";
                                    mCreditString += line_feed + "圈存金额："
                                            + credit;
                                    mCreditString += line_feed + "sDateMac2 = "
                                            + sDateMac2;
                                    mCreditString += line_feed + "圈存写卡成功";

                                    sendMessage(STATE_CREDIT_STEP_2_OK);
                                } else {
                                    sendMessage(STATE_CREDIT_STEP_2_FAIL);
                                }
                            } else {
                                sendMessage(STATE_CREDIT_STEP_1_FAIL);
                            }

                        } else {
                            sendMessage(STATE_CREDIT_STEP_1_FAIL);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mProcessing = false;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
