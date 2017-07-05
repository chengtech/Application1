package com.chengtech.etc;

import android.util.Log;

import com.android.recharge.ObuInterface;

import java.io.UnsupportedEncodingException;

import etc.obu.data.CardInformation;

/**
 * 作者: LiuFuYingWang on 2017/4/12 15:52.
 */

public class CardConsume {
    //消费模式
    public static final int CONSUME_METHOD_COMPLEX = 0; //复合消费
    public static final int CONSUME_METHOD_SIMPLE = 1; //简单消费
    //通道类型：OBU通道
    public static final int CHANNEL_TYPE_OBU = 1;
    //OBU IC卡通道，sdk指定为1
    public static final int OBU_CHANNEL_ICC = 1;
    //OBU ESAM通道，sdk指定为2
    public static final int OBU_CHANNEL_ESAM = 2;

    private ObuInterface mObuInterface;

    //DSRC通道相关定义
    //通道类型：DSRC模块通道
    public static final int CHANNEL_TYPE_DSRC = 0;
    //DSRC模块13.56M通道ID，sdk指定为0，可直接操作IC卡
    public static final int DSRC_CHANNEL_ICC = 0;
    //DSRC模块SAM卡通道， 1~4: sam1~sam4，通过配置文件直接读取通道的ID，此处不进行定义

    private int mCosNums;
    private String[] mCosCmds = new String[10];
    private String LOG_TAG = "tag";
    private int mTryTimes = 5;

    public CardConsume(ObuInterface mObuInterface) {
        this.mObuInterface = mObuInterface;
    }

    /**
     * @param channelType : 通道类型
     * @param channelID   : 通道ID
     * @param config      : 配置文件对象
     * @param cardInfo    : 卡片信息对象
     * @param money       : 消费金额
     * @return int : 0(成功) | 其他(失败)
     * @Title cardConsume
     * @Description 卡片消费
     * @author ZengXiaoJiang
     */
    public int cardConsume(int consumeMethod, int channelType, int channelID, ConfigBean config, CardInformation cardInfo, int money, String str0019,CardConsumeResult cardConsumeResult,String timeStampNow) {

        if (cardInfo == null) {
            return ResultCode.ETC_OPT_PURCHASE_PARAM_ERROR;
        }

        if (cardInfo.getBalance() < money) {
            Log.i("tag", "Insufficient balance, card balance: " + cardInfo.getBalance());
            return ResultCode.OBU_BIZ_CARD_INSUFFICIENT_BALANCE;
        }

        String payMoney = Utils.bytesToHexString(Utils.bigIntToBytes(money), 4);
        Log.i("tag", "payMoney: " + payMoney);


        int ret = -1;
        String psamTerminateID = "";

        mCosNums = 0;
        mCosCmds[mCosNums++] = "00A40000023F00"; //指令1：进入3F00 目录 00A40000023F00
        mCosCmds[mCosNums++] = "00B0960006"; //指令2，读取0016文件	6字节终端机编号

        ret = this.cosChannel(CHANNEL_TYPE_DSRC, config.getPsamSlotPurchase(), mCosNums, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Enter psam dir " + config.getPsamDirPurchase() + " return failed. error code : " + ret);
            return ret;
        }
        psamTerminateID = mCosCmds[1].substring(0, 12);
        if (cardConsumeResult!=null)
            cardConsumeResult.setPsamTerminateID(psamTerminateID);
        Log.i("tag", "psamTerminateID: " + psamTerminateID);

        mCosNums = 0;
        mCosCmds[mCosNums++] = "00A40000023F00"; //指令1：进入3F00 目录 00A40000023F00
        mCosCmds[mCosNums++] = "00A4000002" + config.getPsamDirPurchase(); //指令2，进入PSAM消费目录

        ret = this.cosChannel(CHANNEL_TYPE_DSRC, config.getPsamSlotPurchase(), mCosNums, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Enter psam dir " + config.getPsamDirPurchase() + " return failed. error code : " + ret);
            return ret;
        }

        //读取卡片信息
        String file0015[] = new String[1];
        String file0019[] = new String[1];
        String file0002[] = new String[1];

        ret = this.readICC(channelType, channelID, file0015, file0019, file0002, cardInfo);
        if (ret != 0) {
            Log.i("tag", "readICC failed. error code : " + ret);
            return ret;
        }

        //消费初始化
        String iccPaySerial = ""; //2字节ICC交易序号
        String iccRand = ""; //4字节伪随机数
        byte keyVersion = 0x01;//0x00; //秘钥版本号
        byte algorithmId = 0x00; //算法标识

        Log.i("tag", "mCosCmds[0] : " + mCosCmds[0]);
        mCosNums = 0;

        if (consumeMethod == this.CONSUME_METHOD_COMPLEX) {
            //指令1：复合消费初始化 805003020B01+ 4字节 Money+ 6字节 psamsn
            mCosCmds[0] = String.format("805003020B01%s%s", payMoney, psamTerminateID);
        } else if (consumeMethod == this.CONSUME_METHOD_SIMPLE) {
            //简单消费： 805001020B01+ 4字节 Money+ 6字节 psamsn
            mCosCmds[0] = String.format("805001020B01%s%s", payMoney, psamTerminateID);
        }


        Log.i("tag", "mCosCmds[0] : " + mCosCmds[0]);

        ret = this.cosChannel(channelType, channelID, 1, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Consume init failed. error code : " + ret);
            return ret;
        } else {
            //rsp data: 4bytes money + 2bytes iccPaySerial + 3bytes overdraft extent + 1byte keyversion + 1byte algorithmId + 4bytes iccrand + 9000
            iccPaySerial = mCosCmds[0].substring(8, 12);
            iccRand = mCosCmds[0].substring(22, 30);

            Log.i("tag", "iccPaySerial : " + iccPaySerial);
            Log.i("tag", "iccRand : " + iccRand);
            if (cardConsumeResult!=null)
                cardConsumeResult.setIccPaySerial(iccPaySerial);
        }

        //计算MAC1
        //指令1： 8070000024 + 4bytes iccRand + 2bytes iccPaySerial + 4bytes payMoney + "09" 计算模式 + 7bytes BCD Timestamp + 1byte KeyVersion + 1byte algorithmId + 8bytes iccardId + 8bytes iccIssureId;
        //指令1返回：4byte psamPaySerial + 4bytes mac1
        String iccardId = cardInfo.getCardId().substring(4);
        Log.i("tag", "Calc mac1, iccardId : " + iccardId);
        String iccIssureId = "";
        try {
            Log.i("tag", "cardInfo.getProvider(): " + cardInfo.getProvider());
            byte[] providerBytes = cardInfo.getProvider().getBytes("GBK");
            if (providerBytes != null) {
                iccIssureId = Utils.bytesToHexString(providerBytes, 4);
                iccIssureId += iccIssureId;
                Log.i("tag", "Calc mac1, iccIssureId : " + iccIssureId);
            } else {
                Log.i("tag", "Change cardInfo.getProvider() to bytes failed, providerBytes is null");
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("tag", e.getMessage());
        }
        // cardInfo.getProvider().getBytes();//.substring(0, 7) + cardInfo.getProvider().substring(0, 7);

//        String timeStampNow = Utils.timeStampToDateString(Utils.getUnixTimestamp(), "yyyyMMddHHmmss");
//        Log.i("tag", "Calc mac1, timeStampNow : " + timeStampNow);

        if (consumeMethod == this.CONSUME_METHOD_COMPLEX) {
            mCosCmds[0] = String.format("8070000024%s%s%s09%s%02x%02x%s%s", iccRand, iccPaySerial, payMoney, timeStampNow, keyVersion, algorithmId, iccardId, iccIssureId);
        } else if (consumeMethod == this.CONSUME_METHOD_SIMPLE) {
            mCosCmds[0] = String.format("8070000024%s%s%s06%s%02x%02x%s%s", iccRand, iccPaySerial, payMoney, timeStampNow, keyVersion, algorithmId, iccardId, iccIssureId);
        }

        String psamPaySerial = ""; //4字节
        String mac1 = ""; //4字节

        ret = this.cosChannel(CHANNEL_TYPE_DSRC, config.getPsamSlotPurchase(), 1, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Calc mac1 failed. error code : " + ret);
            return ret;
        } else {
            //rsp data: 4byte psamPaySerial + 4bytes mac1 + 9000
            psamPaySerial = mCosCmds[0].substring(0, 8);
            mac1 = mCosCmds[0].substring(8, 16);

            if (cardConsumeResult!=null)
                cardConsumeResult.setPsamPaySerial(psamPaySerial);
            Log.i("tag", "Calc mac1, psamPaySerial : " + psamPaySerial);
            Log.i("tag", "Calc mac1, mac1 : " + mac1);
        }

        if (consumeMethod == this.CONSUME_METHOD_COMPLEX) {
            //复合消费+写0019文件
            //指令1：复合写0019：
            //根据卡的信息来判断是新国标还是旧国标，是广东卡还是外省卡
            String provinceId = cardInfo.getCardId().substring(0, 4);
            int cardVersion = Integer.parseInt(cardInfo.getCardVersion());
            if (cardVersion >= 40) {
                mCosCmds[0] = String.format("80DCAAC82BAA2900%s", str0019);
            } else if (provinceId.equals("4401")) {
                //广东旧国标
                mCosCmds[0] = String.format("80DC44C83C443A00%s", str0019);
            } else {
                //外省旧国标
                mCosCmds[0] = String.format("80DCC1C83FC13D00%s", str0019);
            }
            ret = this.cosChannel(channelType, channelID, 1, mCosCmds);
            if (ret != 0) {
                Log.i("tag", "Write 0019 failed. error code : " + ret);
                return ret;
            }
        }

        //指令2：执行消费 805401000F + 4bytes psamPaySerial + 7bytes timestamp + 4bytes MAC1
        mCosCmds[0] = String.format("805401000F%s%s%s", psamPaySerial, timeStampNow, mac1);

        String tac = "";
        String mac2 = "";

        ret = this.cosChannel(channelType, channelID, 1, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Consume failed. error code : " + ret);
            return ret;
        } else {
            //rsp data: 4bytes tac + 4bytes mac2 + 9000
            tac = mCosCmds[0].substring(0, 8);
            mac2 = mCosCmds[0].substring(8, 16);

            if (cardConsumeResult!=null)
                cardConsumeResult.setTac(tac);
            Log.i("tag", "tac : " + tac);
            Log.i("tag", "mac2 : " + mac2);
        }

        //消费校验MAC2码
        //指令1:8072000004 + 4bytes mac2
        // 测试的时候，不做MAC2校验，以防锁卡
        mCosCmds[0] = String.format("8072000004%s", mac2);

        ret = this.cosChannel(CHANNEL_TYPE_DSRC, config.getPsamSlotPurchase(), 1, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Check MAC2 failed. error code : " + ret);
        } else {
            Log.i("tag", "Check MAC2 success.");
        }

        //获取消费后的余额,读取卡片0002文件
        int[] newBalance = new int[1];
        ret = this.readICCBalance(channelType, channelID, newBalance);
        if (ret != 0) {
//            mLogger.info("Read icc balance failed, error code: " + ret);
            return ret;
        }

        cardInfo.setBalance(newBalance[0]);

        return ret;
    }

    /**
     * 判断pasm卡槽是位于哪个位置
     *
     * @param channelType 通道模块类型
     * @return
     */
    private int judgePsamSlot(int channelType) {

        for (int i = 1; i < 5; i++) {
            //Todo
        }
        return -1;
    }

    /**
     * @param num     cos指令返回的条数
     * @param cosRsps 输出cos指令返回的数据，不包括2个字节的状态码
     * @return int : 0成功，非0失败
     * @Title judgeCosRspStatus
     * @Description 判断cos指令执行状态码
     * @author ZengXiaoJiang
     */
    private int judgeCosRspStatus(int num, String[] cosRsps) {
        for (int i = 0; i < num; i++) {
            Log.i("tag", String.format("cosRsps[%d]: %s", i, cosRsps[i]));
            String cosRspStatus = cosRsps[i].substring(cosRsps[i].length() - 4, cosRsps[i].length());
            if (!cosRspStatus.equals("9000")) {
                Log.i("tag", "judgeCosRspStatus, cos response status code: " + cosRspStatus);
                return ResultCode.ETC_OPT_COS_RESULT_ERROR;
            }
        }

        return 0;
    }

    /**
     * @param channelType 通道类型，DSRC模块：CHANNEL_TYPE_DSRC， OBU模块：CHANNEL_TYPE_OBU
     * @param channelID   DSRC通道：0-ICC通道，1-SAM1，2-SAM2, 3-SAM3，4-SAM4；OBU通道：1-ICC，2-ESAM
     * @param num         cos指令条数
     * @param commands    存放cos指令的String数组，改参数同时输出执行cos指令后返回的String数组
     * @return int : 0成功，非0失败
     * @Title cosChannel
     * @Description cos指令通道
     * @author ZengXiaoJiang
     */
    public int cosChannel(int channelType, int channelID, int num, String[] commands) {
        int ret = -1;
        Log.e(LOG_TAG, "channelType = " + channelType + " ChannelID = " + channelID);
        for (int i = 0; i < mTryTimes; i++) {
            for (int j = 0; j < num; j++) {
                String rsp = String.format("cosChannel, commands[%d]: %s", j, commands[j]);
                Log.e(LOG_TAG, rsp);
            }

            switch (channelType) {
                case CHANNEL_TYPE_DSRC: //dsrc模块通道
                    ret = mObuInterface.dsrcCosCommand((byte) channelID, num, commands);
                    break;

                case CHANNEL_TYPE_OBU:    //obu通道
                    ret = mObuInterface.obuCosCommand((byte) channelID, num, commands);
                    break;

                default:
                    break;
            }

            if (ret == 0) {
                //判断cos执行状态码
                ret = this.judgeCosRspStatus(num, commands);
                break;
            } else {
                Log.e(LOG_TAG, "cosChannel return:" + ret);
                ret = ResultCode.ETC_OPT_COS_CHANNEL_ERROR;
                Log.e(LOG_TAG, "cosChannel failed, try again");
                continue;
            }
        }

        return ret;
    }

    /**
     * @param channelType : 通道类型
     * @param channelID   : 通道ID
     * @param file0015    : 0015文件
     * @param file0019    : 0019文件
     * @param file0002    : 0002文件
     * @return int : 0成功，非0失败
     * @Title readICC
     * @Description 读取IC卡文件信息，包括0015、0019、0002文件
     * @author ZengXiaoJiang
     */
    public int readICC(int channelType, int channelID, String[] file0015, String[] file0019, String[] file0002, CardInformation cardInfo) {

        if (file0015 == null || file0019 == null || file0002 == null) {
            return ResultCode.ETC_OPT_READ_ICC_PARAM_ERROR;
        }

        int ret = -1;
        int readICC0015Length = 43, readICC0019Length = 60, readICC0006Length = 4;

        mCosNums = 0;
        mCosCmds[mCosNums++] = String.format("00B09500%02x", (byte) readICC0015Length); //指令1：00B095002B 读取IC Card 0015文件43个字节
        //根据卡的信息来判断是新国标还是旧国标，是广东卡还是外省卡
        String provinceId = cardInfo.getCardId().substring(0, 4);
        int cardVersion = Integer.parseInt(cardInfo.getCardVersion());
        if (cardVersion >= 40) {
            mCosCmds[mCosNums++] = String.format("00B207CC%02x", (byte) 63); //指令2：00B207CC3F 若是新国标卡,读0019文件的第六个记录，读取63个字节
        } else if (provinceId.equals("4401")) {
            //广东旧国标
            mCosCmds[mCosNums++] = String.format("00B202CC%02x", (byte) readICC0019Length); //指令2：00B201CC3c 读取IC Card 0019文件60个字节
        } else {
            //外省旧国标
            mCosCmds[mCosNums++] = String.format("00B201CC%02x", (byte) 43); //指令2：00B201CC2B 读取IC Card 0019文件43个字节
        }
        mCosCmds[mCosNums++] = String.format("805C0002%02x", (byte) readICC0006Length); //指令3，805C000204 读0002钱包文件

        ret = this.cosChannel(channelType, 0, mCosNums, mCosCmds);
        if (ret == 0) {
            file0015[0] = mCosCmds[0].substring(0, mCosCmds[0].length() - 4);
            file0019[0] = mCosCmds[1].substring(0, mCosCmds[1].length() - 4);
            file0002[0] = mCosCmds[2].substring(0, mCosCmds[2].length() - 4);
        }

        return ret;
    }

    /**
     * @param channelType : 通道类型
     * @param channelID   : 通道ID
     * @param balance     : 读取的余额
     * @return int : 0成功，非0失败
     * @Title readICCBalance
     * @Description 读取IC卡余额（0002文件）
     * @author ZengXiaoJiang
     */
    public int readICCBalance(int channelType, int channelID, int[] balance) {
        if (balance == null) {
            return ResultCode.ETC_OPT_CARD_READ_BALANCE_PARAM_ERROR;
        }

        int ret = -1;

        //读取卡片0002文件
        mCosNums = 0;
        mCosCmds[mCosNums++] = String.format("805C0002%02x", (byte) 4); //指令3，805C000204 读0002钱包文件

        ret = this.cosChannel(channelType, channelID, mCosNums, mCosCmds);
        if (ret != 0) {
            Log.i("tag", "Read balance(file 0002) failed");
            return ret;
        }

        String newBalanceStr = mCosCmds[0].substring(0, mCosCmds[0].length() - 4);
        balance[0] = Utils.bytesToBigInt(Utils.hexStringToBytes(newBalanceStr));

        return 0;
    }

}
