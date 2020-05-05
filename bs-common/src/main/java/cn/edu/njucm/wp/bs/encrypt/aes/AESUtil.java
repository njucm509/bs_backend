package cn.edu.njucm.wp.bs.encrypt.aes;

import cn.edu.njucm.wp.bs.encrypt.CheckUtils;
import cn.edu.njucm.wp.bs.encrypt.extra.ConfigureEncryptAndDecrypt;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
public class AESUtil {
    private static final String defaultCharset = "UTF-8";
    //    private static final String KEY_AES = "AES";
    private static final String KEY = "1234567812345678";

    private static String parseByte2HexStr(byte[] buf) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static String encrypt(byte[] data, byte[] key) {
        CheckUtils.notEmpty(data, "data");
        CheckUtils.notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.AES_ALGORITHM);// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, seckey, iv);// 初始化
            byte[] result = cipher.doFinal(data);
            return parseByte2HexStr(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("encrypt fail!", e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static String decrypt(String data, byte[] key) {
        CheckUtils.notEmpty(data, "data");
        CheckUtils.notEmpty(key, "key");
        if (key.length != 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.AES_ALGORITHM);// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(key);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, seckey, iv);// 初始化
            byte[] result = cipher.doFinal(parseHexStr2Byte(data));
            return new String(result); // 解密
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("decrypt fail!", e);
        }
    }

    public static String generateKey(Integer userId) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String user = String.valueOf(userId);
        int len = user.length();
        if (len > 16) {
            user = user.substring(0, 16);
        } else {
            Random random = new Random();
            for (int i = 0; i < 16 - len; i++) {
                int number = random.nextInt(52);
                user += str.charAt(number);
            }
        }
        return user;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String content = "xxxxxxxxxxxx";
//        System.out.println("加密前" + content);
//        System.out.println("加密密钥和解密密钥" + KEY);
//        String encrypt = encrypt(content.getBytes(defaultCharset), KEY.getBytes(defaultCharset));
//        System.out.println("加密后" + new String(encrypt));
//        String decrypt = decrypt(encrypt, KEY.getBytes(defaultCharset));
//        System.out.println("解密后" + new String(decrypt));

        System.out.println(generateKey(123456));

    }
}
