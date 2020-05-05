package cn.edu.njucm.wp.bs.search;

import cn.edu.njucm.wp.bs.encrypt.HE.HE;
import cn.edu.njucm.wp.bs.encrypt.HE.HEUtil;
import cn.edu.njucm.wp.bs.encrypt.rsa.RSAUtil;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

public class Search {
    public static void main(String[] args) throws Exception {
        String plaintext = "测试数据来自加拿大";
        HashMap<String, String> p = RSAUtil.generateKeyPair();
        String publicKey = p.get("publicKey");
        String privateKey = p.get("privateKey");
        HEUtil heUtil = new HEUtil();
        HE he = heUtil.getHe();
        he.getQ();
        heUtil.setGroupLength(8);
        String key = "测试";
    }
}
