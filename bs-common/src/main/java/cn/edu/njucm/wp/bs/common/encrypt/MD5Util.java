package cn.edu.njucm.wp.bs.common.encrypt;

import java.security.MessageDigest;

public class MD5Util {
    /**
     * 对字符串进行md5加密
     *
     * @param str
     * @return
     */
    public static String encrypt(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return byteToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param data
     * @return
     */
    public static String byteToHex(byte[] data) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : data) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
