package com.yzkj.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Mr.Zhou on 2016/12/29.
 */
public class HashKit {

    /**
     * SHA-256 加密
     * @param str 字符串
     * @return 加密字符串
     */
    public static String sha256(String str) {
        return encrypt(str,"SHA-256");
    }

    /**
     * SHA-512加密
     * @param str 字符串
     * @return 加密字符串
     */
    public static String sha512(String str) {
        return encrypt(str,"SHA-512");
    }

    /**
     * MD5加密
     * @param str 字符串
     * @return 加密字符串
     */
    public static String md5(String str) {
        return encrypt(str,"MD5");
    }

    /**
     *  字符串加密
     * @param str 字符串
     * @param type 加密类型
     * @return 加密字符串
     */
    private static String encrypt(String str , String type){
        String strResult = null;
        if (str != null && str.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象
                MessageDigest messageDigest = MessageDigest.getInstance(type);
                messageDigest.update(str.getBytes());
                byte byteBuffer[] = messageDigest.digest();
                // 將 byte 转换为 string
                StringBuffer strHexString = new StringBuffer();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回结果
                strResult = strHexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
