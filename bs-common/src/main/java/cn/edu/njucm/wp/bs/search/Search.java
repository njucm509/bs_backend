package cn.edu.njucm.wp.bs.search;

import cn.edu.njucm.wp.bs.encrypt.HE.HE;
import cn.edu.njucm.wp.bs.encrypt.HE.HEUtil;
import cn.edu.njucm.wp.bs.encrypt.rsa.RSAUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Search {
    public static void main(String[] args) throws Exception {
        String plaintext = "测试数据来自加拿大";
//        HashMap<String, String> p = RSAUtil.generateKeyPair();
//        String publicKey = p.get("publicKey");
//        String privateKey = p.get("privateKey");
        HEUtil heUtil = new HEUtil();
        heUtil.setGroupLength(8);
//        List<String> ciphertext = heUtil.encrypt2Group(plaintext);
        String ciphertext = heUtil.encrypt(plaintext);
        System.out.println(ciphertext);

        HE he = heUtil.getHe();
        BigInteger q = he.getQ();
        BigInteger r1 = he.getR1();
        System.out.println("r1: " + r1);
        BigInteger n = he.getN();
        BigInteger r2 = new BigInteger(String.valueOf(new Random().nextInt(10) + 1));
        he.setR1(r2);
//        System.out.println("r2: " + r2);
        String key = "测试数据来自";
        String encrypt_key = heUtil.encrypt(key);
        System.out.println(encrypt_key);

        System.out.println("原文: " + plaintext);
        System.out.println("检索关键词: " + key);

        long start = System.currentTimeMillis();
        int len = encrypt_key.length();
//        for (String enc : ciphertext) {
//            BigInteger subtract = new BigInteger(new BigInteger(encrypt_key, 2).toString(10)).subtract(new BigInteger(new BigInteger(enc, 2).toString(10)));
//            BigInteger multiply = subtract.multiply(q).multiply(r1);
//            BigInteger mod = multiply.mod(n);
//            System.out.println(mod);
//        }

        BigInteger rt = new BigInteger(String.valueOf(new Random().nextInt(10) + 1));

        System.out.println("rt: " + rt);
        System.out.println(compute(q, r1, n, encrypt_key, ciphertext));
//        if (ciphertext.contains(encrypt_key)) {
//            System.out.println("检索成功");
//        } else {
//            System.out.println("检索失败");
//        }
        long end = System.currentTimeMillis();

        System.out.println("time: " + (end - start) + "ms");
    }

    private static boolean compute(BigInteger q, BigInteger r1, BigInteger n, String encryptKey, String ciphertext) {
        int total = ciphertext.length();
        System.out.println("total: " + total);
        int len = encryptKey.length();
        System.out.println("len: " + len);

        for (int i = 0; i < total - len; i++) {
            String enc = ciphertext.substring(i, i + len);
            BigInteger subtract = new BigInteger(new BigInteger(encryptKey, 2).toString(10)).subtract(new BigInteger(new BigInteger(enc, 2).toString(10)));
            BigInteger multiply = subtract.multiply(q).multiply(r1);
            BigInteger mod = multiply.mod(n);
//            System.out.println(mod);
            if (mod.compareTo(new BigInteger("0")) == 0) {
                return true;
            }
        }

        return false;
    }

}
