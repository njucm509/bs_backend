package cn.edu.njucm.wp.bs.encrypt.HE;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;

public class HEUtil {
    private HE he = new HE();

    // 分组长度
    private int groupLength = 1;

    private int groupNum;

    private int[] map;

    /**
     * 同态加密
     *
     * @param plaintext
     * @return
     */
    public String encrypt(String plaintext) {
        initMap(plaintext);
        ArrayList<BigInteger> groups = groupByPlaintext(plaintext);
        StringBuilder sb = new StringBuilder();
        for (BigInteger group : groups) {
            sb.append(fixBin(new BigInteger(he.encrypt(group).toString(), 10).toString(2)));
        }
        return sb.toString();
    }

    private void initMap(String plaintext) {
        char[] chs = plaintext.toCharArray();
        int length = chs.length;
        map = new int[length];
        for (int i = 0; i < length; i++) {
            int len = Integer.toBinaryString(chs[i]).length();
            if (len > 0 && len <= 8) {
                map[i] = 8;
            }

            if (len > 8 && len <= 16) {
                map[i] = 16;
            }

            if (len > 16 && len <= 24) {
                map[i] = 24;
            }

            if (len > 24 && len <= 32) {
                map[i] = 32;
            }
        }
    }

    public String decrypt(String ciphertext) {
        ArrayList<BigInteger> groups = groupByCiphertext(ciphertext);
        StringBuilder sb = new StringBuilder();
        for (BigInteger group : groups) {
            sb.append(fixBin(new BigInteger(he.decrypt(group).toString(), 10).toString(2), groupLength));
        }

        char tmp;
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            tmp = (char) Integer.parseInt(new BigInteger(sb.substring(0, map[i]), 2).toString(10));
            sb.delete(0, map[i]);
            res.append(tmp);
        }

        return res.toString();
    }

    private String fixBin(String bin, int groupLength) {
        StringBuilder sb = new StringBuilder();
        int len = bin.length();
        for (int i = 0; i < groupLength - len; i++) {
            sb.append("0");
        }
        return sb.append(bin).toString();
    }

    private String fixBin(String bin) {
        StringBuilder sb = new StringBuilder();
        int len = bin.length();
        if (len > 0 && len <= 8) {
            for (int i = 0; i < 8 - len; i++) {
                sb.append("0");
            }
        }

        if (len > 8 && len <= 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }

        if (len > 16 && len <= 24) {
            for (int i = 0; i < 24 - len; i++) {
                sb.append("0");
            }
        }

        if (len > 24 && len <= 32) {
            for (int i = 0; i < 32 - len; i++) {
                sb.append("0");
            }
        }

        return sb.append(bin).toString();
    }

    private String toDecimal(String decrypt) {
        return new BigInteger(decrypt, 2).toString(10);
    }

    /**
     * 密文分组
     *
     * @param ciphertext
     * @return
     */
    private ArrayList<BigInteger> groupByCiphertext(String ciphertext) {
        ArrayList<BigInteger> list = new ArrayList<>();
        int len = ciphertext.length();
        int groupNum = this.groupNum;
        int groupLength = len % groupNum == 0 ? len / groupNum : len / groupNum + 1;
        StringBuilder sb = new StringBuilder(ciphertext);
        for (int i = 0; i < groupNum; i++) {
            String item;
            if (sb.length() < groupLength) {
                item = sb.substring(0);
                sb.delete(0, sb.length());
            } else {
                item = sb.substring(0, groupLength);
                sb.delete(0, groupLength);
            }
            list.add(new BigInteger(toDecimal(item)));
        }
        return list;
    }

    /**
     * 明文分组
     *
     * @param plaintext
     * @return
     */
    private ArrayList<BigInteger> groupByPlaintext(String plaintext) {
        ArrayList<BigInteger> list = new ArrayList<>();
        char[] chs = plaintext.toCharArray();
        int len = chs.length;
        int groupLength = getGroupLength();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(toBin(chs[i]));
        }
        len = sb.length();
        int groupNum = len % groupLength == 0 ? len / groupLength : len / groupLength + 1;
        this.groupNum = groupNum;
        for (int i = 0; i < groupNum; i++) {
            String item;
            if (sb.length() < groupLength) {
                item = sb.substring(0);
                sb.delete(0, sb.length());
            } else {
                item = sb.substring(0, groupLength);
                sb.delete(0, groupLength);
            }
            list.add(toBigInteger(item));
        }
        return list;
    }

    /**
     * 二进制转为大数
     *
     * @param bin
     * @return
     */
    private BigInteger toBigInteger(String bin) {
        return new BigInteger(bin, 2);
    }

    /**
     * char转为二进制
     *
     * @param ch
     * @return
     */
    private String toBin(char ch) {
        String s = Integer.toBinaryString(ch);

        return fixBin(s);

    }

    public void setGroupLength(int groupLength) {
        this.groupLength = groupLength;
    }

    public int getGroupLength() {
        return this.groupLength;
    }

    public HE getHe() {
        return he;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String plaintext_100 = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        String plaintext_1000 = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        String plaintext_2000 = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        String plaintext_3000 = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";
        String plaintext_4000 = "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试" +
                "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试";


        HEUtil heUtil = new HEUtil();

        heUtil.setGroupLength(16);
        long start = System.currentTimeMillis();
        String _1_100_enc = heUtil.encrypt(plaintext_4000);
        long end = System.currentTimeMillis();
        System.out.println("enc : " + (end - start) + "ms");

//        System.out.println("len: " + _1_100_enc.length() + " 密文: " + _1_100_enc);
        start = System.currentTimeMillis();
        String decrypt = heUtil.decrypt(_1_100_enc);
        end = System.currentTimeMillis();
        System.out.println("dec : " + (end - start) + "ms");
        System.out.println(decrypt);

//        start = System.currentTimeMillis();
//        heUtil.encrypt(plaintext_1000);
//        end = System.currentTimeMillis();
//
//        System.out.println("_1_1000: " + (end - start) + "ms");
//
//        start = System.currentTimeMillis();
//        heUtil.encrypt(plaintext_2000);
//        end = System.currentTimeMillis();
//
//        System.out.println("_1_2000: " + (end - start) + "ms");
//
//        start = System.currentTimeMillis();
//        heUtil.encrypt(plaintext_3000);
//        end = System.currentTimeMillis();
//
//        System.out.println("_1_3000: " + (end - start) + "ms");
//
//        start = System.currentTimeMillis();
//        String _1_4000_enc = heUtil.encrypt(plaintext_4000);
//        end = System.currentTimeMillis();
//
//        System.out.println("_1_4000: " + (end - start) + "ms");


//        start = System.currentTimeMillis();
//        heUtil.encrypt(plaintext_40000);
//        end = System.currentTimeMillis();
//
//        System.out.println("_1_40000: " + (end - start) + "ms");

//        String text = "测试123";
//        System.out.println("====");
//
//        System.out.println(text.charAt(0));
//        System.out.println(heUtil.toBin(text.charAt(0)));
//        System.out.println("明文: " + text);
//        String enc = heUtil.encrypt(text);
//        System.out.println("密文: " + enc);
//
//        String decrypt = heUtil.decrypt(enc);
//        System.out.println("解密: " + decrypt);
//        System.out.println("----");

    }
}
