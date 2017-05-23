package com.guangyao.bluetoothtest.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * 数据操作工具类
 */
public class DataHandlerUtils {

    /**
     * byte[] --> 十六进制的字符串
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(" " + 0 + hv);
            }else{
                stringBuilder.append(" " + hv);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转字节数组
     * @param hexRepresentation
     * @return
     */
    public static byte[] hexToBytes(Context context, String hexRepresentation) {
        if (hexRepresentation.length() % 2 == 1) {
//            throw new IllegalArgumentException("hxToBytes requires an even-length String parameter");
            Toast.makeText(context, "发送的指令有误", Toast.LENGTH_LONG).show();
            return null;
        }
        int len = hexRepresentation.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexRepresentation.charAt(i), 16) << 4)
                    + Character.digit(hexRepresentation.charAt(i + 1), 16));
        }
        return data;
    }
}