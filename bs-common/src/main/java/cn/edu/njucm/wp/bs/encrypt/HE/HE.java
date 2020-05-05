package cn.edu.njucm.wp.bs.encrypt.HE;

import java.math.BigInteger;
import java.util.Random;

public class HE {

    // p,q 随机大素数
    private BigInteger p, q;

    // 随机数
    private BigInteger r1;

    // n = p * q
    public BigInteger n;

    public HE() {
        KeyGeneration(512, 64);
    }

    // bitLength 随机素数长度
    public HE(int bitLength, int certainty) {
        KeyGeneration(bitLength, certainty);
    }

    private void KeyGeneration(int bitLength, int certainty) {
        // 512位素数
        p = new BigInteger(bitLength, certainty, new Random());
        q = new BigInteger(bitLength, certainty, new Random());
        n = p.multiply(q);
        r1 = new BigInteger(String.valueOf(new Random().nextInt(10) + 1));

    }

    public BigInteger encrypt(BigInteger plaintext) {
        return plaintext.add(p.multiply(r1)).mod(n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        return ciphertext.mod(p);
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public BigInteger getR1() {
        return r1;
    }

    public BigInteger getN() {
        return n;
    }

    public void setR1(BigInteger r1) {
        this.r1 = r1;
    }

    public static void main(String[] args) {
        String text = "测";
        HE he = new HE();
        BigInteger encrypt = he.encrypt(new BigInteger("20"));
        System.out.println("明文大数: 20");
        System.out.println("密文大数: " + encrypt.toString());
        BigInteger decrypt = he.decrypt(encrypt);
        System.out.println("解密: " + decrypt.toString());
    }
}
