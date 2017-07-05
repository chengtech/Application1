package com.chengtech.etc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: Utils
 * @Description: 工具类
 * @author: zengxiaojiang
 * @date: 2017年2月13日
 * @copyright: Copyright © 2010 GENVICT. All rights reserved.
 */
@SuppressLint("SimpleDateFormat")
public class Utils {

    private static final String LOG_TAG = "Genvict";

    // 获得本机ＣＰＵ大小端
    public static boolean isBigendian() {
        short i = 0x1;
        boolean bRet = ((i >> 8) == 0x0);

        //Log.i(tags, "bRet = " + bRet);

        return bRet;
    }

    // int to byte array
    // 小端模式int转byte数组
    public static byte[] littleIntToBytes(int i) {
        byte[] abyte = new byte[4];

        abyte[0] = (byte) (0xff & i);
        abyte[1] = (byte) ((0xff00 & i) >> 8);
        abyte[2] = (byte) ((0xff0000 & i) >> 16);
        abyte[3] = (byte) ((0xff000000 & i) >> 24);

        return abyte;
    }

    // 大端模式int转byte数组
    public static byte[] bigIntToBytes(int i) {
        byte[] abyte = new byte[4];

        abyte[0] = (byte) ((i >> 24) & 0xff);
        abyte[1] = (byte) ((i >> 16) & 0xff);
        abyte[2] = (byte) ((i >> 8) & 0xff);
        abyte[3] = (byte) (i & 0xff);

        return abyte;
    }

    //byte array to int
    // byte数组转小端模式int
    public static int bytesToLittleInt(byte[] bytes) {
        int tmp = 0;

        tmp = bytes[0] & 0xFF;
        tmp |= (((int) bytes[1] << 8) & 0xFF00);
        tmp |= (((int) bytes[2] << 16) & 0xFF0000);
        tmp |= (((int) bytes[3] << 24) & 0xFF000000);

        return tmp;
    }

    // byte数组转大端模式int
    public static int bytesToBigInt(byte[] bytes) {
        int tmp = 0;

        tmp = bytes[0] & 0xFF;
        tmp = (tmp << 8) | (bytes[1] & 0xff);
        tmp = (tmp << 8) | (bytes[2] & 0xff);
        tmp = (tmp << 8) | (bytes[3] & 0xff);

        return tmp;
    }

    public static long fourBytesToLong(byte[] bytes) {

        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = 0;

        firstByte = (0x000000FF & ((int) bytes[index++]));
        secondByte = (0x000000FF & ((int) bytes[index++]));
        thirdByte = (0x000000FF & ((int) bytes[index++]));
        fourthByte = (0x000000FF & ((int) bytes[index++]));

        return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
    }

	/*
	public static byte[] intToBytes(int number)
	{
		int temp = number;
		byte[] b = new byte[4];
		
		for(int i=0;i<4;i++)
		{
			b[i] = new Integer(temp & 0xff).byteValue();
			temp >>= 8;
		}
		return b;
	}*/

    // 整数到字节数组转换
    public static byte[] intToBytes(int n) {
        byte[] ab = new byte[4];

        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);
        ab[2] = (byte) ((0xff0000 & n) >> 16);
        ab[3] = (byte) ((0xff000000 & n) >> 24);

        return ab;
    }

    /**
     * 小端整数到字节数组转换
     *
     * @param n
     * @return
     */
    public static byte[] littleIntToBytes2(int n) {
        byte[] ab = new byte[2];

        ab[0] = (byte) (0xff & n);
        ab[1] = (byte) ((0xff00 & n) >> 8);

        return ab;
    }

    /**
     * 大端整数到字节数组转换
     *
     * @param n
     * @return
     */
    public static byte[] BigIntToBytes2(int n) {
        byte[] ab = new byte[2];

        ab[1] = (byte) (0xff & n);
        ab[0] = (byte) ((0xff00 & n) >> 8);

        return ab;
    }

    /**
     * byte数组转大端模式int
     *
     * @param bytes
     * @return int
     */
    public static int bytes2TobigInt(byte[] bytes) {
        int tmp = 0;

        tmp = bytes[0] & 0xFF;
        tmp = (tmp << 8) | (bytes[1] & 0xff);

        return tmp;
    }

    /**
     * byte数组转小端模式int
     *
     * @param bytes
     * @return int
     */
    public static int bytes2ToLittleInt(byte[] bytes) {
        int tmp = 0;

        tmp = bytes[1] & 0xFF;
        tmp = (tmp << 8) | (bytes[0] & 0xff);

        return tmp;
    }

    /**
     * 省份编码十六进制转化为十进制显示使用
     *
     * @param hexString
     * @return
     */
    public static String hexStringToIntString(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return "";
        }

        String number = "";
        for (int i = 0; i < hexString.length() / 2; i++) {
            number += String.format("%02d", Integer.parseInt(hexString.substring(i * 2, i * 2 + 2), 16));
        }
        return number;
    }

    // 字节数组到整数的转换
    public static int bytesToInt(byte b[]) {
        int s = 0;
        s = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8 | (b[3] & 0xff);

        return s;
    }


    private final static byte[] hex = "0123456789ABCDEF".getBytes();

    private static int parse(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    // 从字节数组到十六进制字符串转换
    public static String bytesToHexString(byte[] b, int bytesLen) {
        byte[] buff = new byte[2 * bytesLen];
        for (int i = 0; i < bytesLen; i++) {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] hexStringToBytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    // 字节数组转字符串
    public static String byteToString(byte[] buf, int index, int num) {
        int i = 0;

        if (buf[index] == 0) {
            return null;
        }

        for (i = 0; i < num; i++) {
            if (buf[index + i] == 0) {
                break;
            }
        }

        byte[] dest = new byte[i];

        System.arraycopy(buf, index, dest, 0, i);

        String s = null;
        try {
            s = new String(dest, "gbk");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Log.i(LOG_TAG, "byteToString:len = " + i);
        //Log.i(LOG_TAG, "byteToString:" + s);

        return s;
    }

    //获取Unix时间戳
    public static long getUnixTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    //将Unix时间戳转换成指定格式日期
    //example:
    //当调用timeStampToDate(1252639886, "yyyy-MM-dd HH:mm:ss");
    //返回值：2009-11-09 11:31:26
    public static String timeStampToDateString(long timestamp, String formats) {
        //Long timestamp = Long.parseLong(timestampString) * 1000;
        timestamp *= 1000; //timestamp is seconds, so multiply 1000

        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));

        return date;
    }

    public static byte[] getBCDTimestamp() {

        String timeNowStr = Utils.timeStampToDateString(Utils.getUnixTimestamp(), "yyyyMMddHHmmss");

        return Utils.hexStringToBytes(timeNowStr);
    }

    public static String bcdDateToStr(String HexDate) {//日期格式从：20130203 －》2013年02月03日
        int index = 0;
        char[] Date = new char[12];
        char ch;

        Log.i(LOG_TAG, "DateHexToStr0:" + HexDate);
        for (int i = 0; i < HexDate.length(); i++) {
            ch = HexDate.charAt(i);

            if (ch >= '0' && ch <= '9') {
                Date[index++] = ch;

            }
            if (index == 4) Date[index++] = '年';
            if (index == 7) Date[index++] = '月';
            if (index == 10) Date[index++] = '日';

        }

        if (index != 11) return null;

        String Str = new String(Date, 0, 11);

        //Log.i(LOG_TAG, "DateHexToStr1:" + Str);

        return Str;

    }

    public static Boolean checkIPFormat(String ip) {
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    //运行普通linux命令
    public static String runCommand(String command) {

        Process process = null;

        try {

            process = Runtime.getRuntime().exec(command);
            process.waitFor();

            InputStream localInputStream = process.getInputStream();
            byte[] arrayOfByte = new byte[localInputStream.available()];

            localInputStream.read(arrayOfByte);

            return (new String(arrayOfByte));

        } catch (Exception e) {

            return null;
        }
    }

    public static void copyFile(File f1, File f2) {
        int length = 2097152;
        FileOutputStream out;
        FileInputStream in;
        byte[] buffer;

        try {
            in = new FileInputStream(f1);
            out = new FileOutputStream(f2);
            buffer = new byte[length];

            while (true) {
                int ins;

                try {
                    ins = in.read(buffer);

                    if (ins == -1) {
                        in.close();
                        out.flush();
                        out.close();

                        return;
                    } else
                        out.write(buffer, 0, ins);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static long readSDCard1Free() //MB
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();

            Log.i(LOG_TAG, "readSDCard:blockSize=" + blockSize + " ,blockCount=" + blockCount + " ,availCount=" + availCount);

            return availCount * blockSize / 0x100000;
        }
        return 0;
    }

    public static long readSDCard2Free() //MB
    {

        File sdcardDir = new File("/mnt/sdcard2/");
        StatFs sf = new StatFs(sdcardDir.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();

        Log.i(LOG_TAG, "readSDCard2:blockSize=" + blockSize + " ,blockCount=" + blockCount + " ,availCount=" + availCount);

        return availCount * blockSize / 0x100000;
    }

    public static long readSystemFree()//MB
    {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();

        Log.i(LOG_TAG, "readSystem:blockSize=" + blockSize + " ,blockCount=" + blockCount + " ,availCount=" + availCount);

        return availCount * blockSize / 0x100000;
    }

    public static boolean deleteFile(String filename) {
        if (filename == null) {
            return false;
        }

        File f = new File(filename);
        if (f.exists()) {
            f.delete();
            return true;
        }

        return false;
    }

    public static boolean checkFileExist(String filePath) {

        File f = new File(filePath);

        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean fileRename(String OldImageName, String NewImageName) {
        Boolean result = false;

        result = checkFileExist(OldImageName);
        if (!result) {
            Log.i(LOG_TAG, "File [" + OldImageName + "] has not existed!");
            return false;
        }

        result = checkFileExist(NewImageName);
        if (result) {
            Log.i(LOG_TAG, "File [" + NewImageName + "] has existed, now delete it and rename by its name!");
            deleteFile(NewImageName);
        }

        File oldFile = new File(OldImageName);
        File newFile = new File(NewImageName);

        result = oldFile.renameTo(newFile);

        return result;
    }

    public static int setPictureRotate(String OldImageName, String NewImageName) {//90度旋转图片

        try {
            Log.i(LOG_TAG, "SetPictureRotate");

            File file = new File(OldImageName);
            if (!file.exists()) {
                Log.i(LOG_TAG, file + " has not existed.");
                return ResultCode.ETC_OPT_PIC_NOT_EXIST;
            }

            int size = (int) file.length();
            Log.i(LOG_TAG, "File size is : " + size);

            if (size > 750000) {
                Log.i(LOG_TAG, OldImageName + "图片文件太大");
            }

            Bitmap bm0 = null;

            try {
                //modify by jayz, 2015-07-28
                //start//////////////////////////////////////////////////////////////////

                BitmapFactory.Options options = new BitmapFactory.Options();
                // 先设置为TRUE不加载到内存中，但可以得到宽和高
                options.inJustDecodeBounds = true;
                bm0 = BitmapFactory.decodeFile(OldImageName, options); // 此时返回bm为空
                options.inJustDecodeBounds = false;
                // 计算缩放比
                int be = (int) (options.outHeight / (float) 960);
                if (be <= 0) {
                    be = 1;
                }
                options.inSampleSize = be;
                // 这样就不会内存溢出了
                bm0 = BitmapFactory.decodeFile(OldImageName, options);

                //bm0 = BitmapFactory.decodeFile(OldImageName);

                if (bm0 == null) {
                    Log.i(LOG_TAG, "bm0 is null,  BitmapFactory.decodeFile failed");
                    System.gc();

                    return ResultCode.ETC_OPT_BITMAP_FACTORY_RETURN_NULL;
                }
                Log.i(LOG_TAG, "bm0 width: " + bm0.getWidth() + " height: " + bm0.getHeight());

                //end//////////////////////////////////////////////////////////////////
            } catch (OutOfMemoryError e) {
                Log.i(LOG_TAG, "处理图片:decodeFile内存错误！");
                e.printStackTrace();
                return ResultCode.ETC_OPT_DECODE_FILE_OUT_OF_MEMORY;
            }

            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            matrix.postScale(1.0f, 1.0f); //长和宽放大缩小的比例

            Bitmap bm = null;

            try {
                bm = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), matrix, true);
            } catch (OutOfMemoryError e) {
                Log.i(LOG_TAG, "处理图片:createBitmap内存错误！");
                e.printStackTrace();
                return ResultCode.ETC_OPT_DECODE_FILE_OUT_OF_MEMORY;
            } catch (IllegalArgumentException e) {
                // TODO: handle exception
                e.printStackTrace();
                return ResultCode.ETC_OPT_CREATE_BITMAP_ILLEGAL_ARGUMENT;
            }

            if (bm == null) {
                Log.i(LOG_TAG, "bm is null, Bitmap.createBitmap failed");
                System.gc();

                return ResultCode.ETC_OPT_CREATE_BITMAP_RETURN_NULL;
            }

            BitmapRecycle(bm0);

            File jpgFile = new File(NewImageName);

            if (jpgFile.exists()) {
                jpgFile.delete();
            }

            FileOutputStream b = new FileOutputStream(jpgFile);
            //四川使用30%压缩率，北京使用80％，湖南使用80％
            bm.compress(Bitmap.CompressFormat.JPEG, 100, b);

            b.flush();
            b.close();

            BitmapRecycle(bm);

            if (jpgFile.exists()) {
                Log.i(LOG_TAG, "SetPictureRotate success。");
                return 0;
            } else {
                Log.i(LOG_TAG, "SetPictureRotate failed, file [" + NewImageName + "] not existed.");
                return ResultCode.ETC_OPT_PIC_NOT_EXIST;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return ResultCode.ETC_OPT_SET_PIC_ROTATE_ERROR;
        }
    }

    private static void BitmapRecycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        System.gc();
    }

    public static byte[] readFileByBytes(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        long fileLen = file.length();
        byte[] fileBytes = new byte[(int) fileLen];
        InputStream in = null;
        int byteRead = 0;

        try {

            in = new FileInputStream(file);
            byteRead = in.read(fileBytes);
            if (byteRead == -1) {
                fileBytes = null;
                Log.i(LOG_TAG, "Read file[" + fileName + "] error, length: -1");
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return fileBytes;
    }

    public static String base64EncodeFile(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = null;
            int count = 0, filesize = 0;

            filesize = fis.available();
            Log.i(LOG_TAG, "upload picture size = " + filesize);

            buffer = new byte[filesize];

            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }

            String uploadBuffer = new String(Base64.encode(baos.toByteArray(), filesize));

            fis.close();

            return uploadBuffer;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString();
    }

    public static Boolean saveBytesToFile(String fileName, byte[] fileBytes) {
        if (fileName.equals("") || fileName == null || fileBytes == null || fileBytes.length <= 0) {
            return false;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }

        try {

            FileOutputStream os = new FileOutputStream(file);
            os.write(fileBytes);
            os.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Boolean saveStrToFile(String fileName, String strContent) {
        if (fileName.equals("") || fileName == null || strContent.equals("") || strContent == null) {
            return false;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }

        try {

            FileOutputStream os = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(os, "GBK");
            writer.write(strContent);
            writer.flush();
            os.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @return List<String>
     * @throws IOException
     * @Title: getExtSDCardPaths
     * @Description: to obtain storage paths, the first path is theoretically
     * the returned value of
     * Environment.getExternalStorageDirectory(), namely the
     * primary external storage. It can be the storage of internal
     * device, or that of external sdcard. If paths.size() >1,
     * basically, the current device contains two type of storage:
     * one is the storage of the device itself, one is that of
     * external sdcard. Additionally, the paths is directory.
     */
    public static List<String> getExtSDCardPaths() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                paths.add(mountPath);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paths;
    }

    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static void memset(byte[] buf, int num) {
        for (int i = 0; i < num; i++)
            buf[i] = 0;
    }

    public static byte[] stringToByte(String str) {
        byte[] b = null;
        int buf_len = 0;
        try {
            b = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("stringToByte:" + str + ",length=" + b.length);
        return b;
    }

    public static String binToHex(byte[] bin, int index, int len) {
        int b;
        byte[] hex_str = new byte[len * 2];
        int index_str = 0;
        byte[] bin2hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        String str = null;

        if (len <= 0) return null;
        for (int i = 0; i < len; i++) {
            if (bin[index] >= 0) b = bin[index];//*bin ++;
            else b = 256 + bin[index];
            index++;
            hex_str[index_str++] = bin2hex[b >> 4];
            hex_str[index_str++] = bin2hex[b & 0xf];
        }
        try {
            str = new String(hex_str, "GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("binToHex:" + str);
        hex_str = null;
        return str;
    }

    public static byte getBcc(byte[] data, int len) {
        byte bcc = 0x00;
        int i;
        for (i = 0; i < len; i++) {
            bcc ^= data[i];
        }
        return bcc;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        System.out.println("density: " + Float.toString(scale));
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 字符串转换为Ascii
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            sbu.append((int) chars[i]);
        }

        return sbu.toString();
    }

    /**
     * @param strPart 字符串
     * @return 16进制字符串
     * @throws
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    public static String stringPlateColor2HexString(String strPlate_color) {
        int nPlate_color = 0;
        if (strPlate_color.equals("蓝色")) {
            nPlate_color = 0;
        } else if (strPlate_color.equals("黄色")) {
            nPlate_color = 1;
        } else if (strPlate_color.equals("黑色")) {
            nPlate_color = 2;
        } else if (strPlate_color.equals("白色")) {
            nPlate_color = 3;
        } else if (strPlate_color.equals("小型新能源汽车号牌颜色")) {
            nPlate_color = 4;
        } else if (strPlate_color.equals("大型新能源汽车号牌颜色")) {
            nPlate_color = 5;
        }
        return String.format("%02x", nPlate_color);
    }

    public static String hexStringPlateColor2String(String strPlate_color) {
        if (strPlate_color == null || strPlate_color.equals("")) {
            return "";
        }

        String licencePlateColor = "蓝色";
        int nPlate_color = Integer.parseInt(strPlate_color, 16);
        switch (nPlate_color) {
            case 0:
                licencePlateColor = "蓝色";
                break;
            case 1:
                licencePlateColor = "黄色";
                break;
            case 2:
                licencePlateColor = "黑色";
                break;
            case 3:
                licencePlateColor = "白色";
                break;
            case 4:
                licencePlateColor = "小型新能源汽车号牌颜色";
                break;
            case 5:
                licencePlateColor = "大型新能源汽车号牌颜色";
                break;
            default:
                licencePlateColor = "未定义";
                break;
        }
        return licencePlateColor;
    }

    public static String stringVehicleType2HexString(String strType) {
        int nType = 0;
        if (strType.equals("一型车")) {
            nType = 1;
        } else if (strType.equals("二型车")) {
            nType = 2;
        } else if (strType.equals("三型车")) {
            nType = 3;
        } else if (strType.equals("四型车")) {
            nType = 4;
        } else if (strType.equals("五型车")) {
            nType = 5;
        } else if (strType.equals("六型车")) {
            nType = 6;
        } else if (strType.equals("一型货车")) {
            nType = 11;
        } else if (strType.equals("二型货车")) {
            nType = 12;
        } else if (strType.equals("三型货车")) {
            nType = 13;
        } else if (strType.equals("四型货车")) {
            nType = 14;
        } else if (strType.equals("五型货车")) {
            nType = 15;
        } else if (strType.equals("六型货车")) {
            nType = 16;
        }
        return String.format("%02x", nType);
    }

    public static String hexStringVehicleType2String(String strType) {
        String vehicleType = "";
        switch (Integer.parseInt(strType, 16)) {
            case 1:
                vehicleType = "一型车";
                break;
            case 2:
                vehicleType = "二型车";
                break;
            case 3:
                vehicleType = "三型车";
                break;
            case 4:
                vehicleType = "四型车";
                break;
            case 5:
                vehicleType = "五型车";
                break;
            case 6:
                vehicleType = "六型车";
                break;
            case 11:
                vehicleType = "一型货车";
                break;
            case 12:
                vehicleType = "二型货车";
                break;
            case 13:
                vehicleType = "三型货车";
                break;
            case 14:
                vehicleType = "四型货车";
                break;
            case 15:
                vehicleType = "五型货车";
                break;
            case 16:
                vehicleType = "六型货车";
                break;
            default:
                vehicleType = "未定义";
                break;
        }
        return vehicleType;
    }

    public static String stringUserType2HexString(String strType) {
        int userType = 0;
        switch (strType) {
            case "普通车":
                userType = 0;
                break;
            case "公务车":
                userType = 6;
                break;
            case "军警车":
                userType = 8;
                break;
            case "紧急车":
                userType = 10;
                break;
            case "免费":
                userType = 12;
                break;
            case "车队":
                userType = 14;
                break;
        }
        return String.format("%02x", userType);
    }

    public static String hexStringUserType2String(String strType) {
        String userType = "";
        switch (Integer.parseInt(strType, 16)) {
            case 0:
                userType = "普通车";
                break;
            case 6:
                userType = "公务车";
                break;
            case 8:
                userType = "军警车";
                break;
            case 10:
                userType = "紧急车";
                break;
            case 12:
                userType = "免费";
                break;
            case 14:
                userType = "车队";
                break;
            default:
                userType = "未定义";
                break;
        }
        return userType;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFromAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFromAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //如果捕捉到错误则通知UI线程
        }
    }

//    public static String getLocalIpAddress() {
//		/*
//		 * if( bWifi ) { return getWifiIpAddress();
//		 *
//		 * }
//		 */
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()
//                            && InetAddressUtils.isIPv4Address(inetAddress
//                            .getHostAddress())) {
//                        return inetAddress.getHostAddress().toString();
//
//                    }
//                }
//            }
//        } catch (SocketException e) { // TODO: handle exception
//            // Log.e("WifiPreference IpAddress---error-" + e.toString());
//        }
//        return "";
//    }

    public static byte[] readFileBytes(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = null;
            int count = 0, filesize = 0;

            filesize = fis.available();
            System.out.println("upload picture size = " + filesize);

            buffer = new byte[filesize];

            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }

            return baos.toByteArray();

        } catch (Exception e) {
            return null;
        }
    }

}
