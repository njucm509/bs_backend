package cn.edu.njucm.wp.bs.encrypt.ecc;

import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.pkcs.PKCS8Key;
import sun.security.x509.X509Key;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ECCUtil {
    public static final String ALGORITHM = "EC";
    private static final String PUBLIC_KEY = "ECCPublicKey";
    private static final String PRIVATE_KEY = "ECCPrivateKey";

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = ECCUtil.decryptBASE64(key);
        // jdk1.7:http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7-b147/sun/security/ec/ECKeyFactory.java/
        // jdk1.8:http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/java/security/interfaces/ECKey.java#ECKey
        // 取得私钥
        // jdk<=1.7
        // PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KeyFactory keyFactory = ECKeyFactory.INSTANCE;
        // ECPrivateKey priKey = (ECPrivateKey)keyFactory.generatePrivate(pkcs8KeySpec);
        // jdk=1.8
        PKCS8Key pkcs8Key = new PKCS8Key();
        pkcs8Key.decode(keyBytes);
        ECPrivateKey priKey = (ECPrivateKey) ECKeyFactory.toECKey(pkcs8Key);

        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(), priKey.getParams());

        // 对数据解密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String privateKey) throws Exception {
        // 对公钥解密
        byte[] keyBytes = ECCUtil.decryptBASE64(privateKey);

        // 取得公钥
        //// jdk<=1.7
        // X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        // KeyFactory keyFactory = ECKeyFactory.INSTANCE;
        // ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);

        // jdk=1.8
        X509Key x509key = new X509Key();
        x509key.decode(keyBytes);
        ECPublicKey pubKey = (ECPublicKey) ECKeyFactory.toECKey(x509key);

        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(), pubKey.getParams());

        // 对数据加密
        // TODO Chipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        // Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(ECCUtil.PRIVATE_KEY);

        return ECCUtil.encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(ECCUtil.PUBLIC_KEY);

        return ECCUtil.encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        BigInteger x1 = new BigInteger("2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
        BigInteger x2 = new BigInteger("289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);

        ECPoint g = new ECPoint(x1, x2);

        // the order of generator
        BigInteger n = new BigInteger("5846006549323611672814741753598448348329118574063", 10);
        // the cofactor
        int h = 2;
        int m = 163;
        int[] ks = { 7, 6, 3 };
        ECFieldF2m ecField = new ECFieldF2m(m, ks);
        // y^2+xy=x^3+x^2+1
        BigInteger a = new BigInteger("1", 2);
        BigInteger b = new BigInteger("1", 2);

        EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);

        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g, n, h);
        // 公钥
        ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);

        BigInteger s = new BigInteger("1234006549323611672814741753598448348329118574063", 10);
        // 私钥
        ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(ECCUtil.PUBLIC_KEY, publicKey);
        keyMap.put(ECCUtil.PRIVATE_KEY, privateKey);

        return keyMap;
    }

    public static byte[] decryptBASE64(String data) {
        return Base64.getDecoder().decode(data);// Base64..decodeBase64(data);
    }

    public static String encryptBASE64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);// new String(Base64.encodeBase64(data));
    }

    public static void main(String[] args) throws Exception {
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();

        Map<String, Object> keyMap = ECCUtil.initKey();

        String publicKey = ECCUtil.getPublicKey(keyMap);
//        String publicKey = "MEAwEAYHKoZIzj0CAQYFK4EEAAEDLAAEAv4TwFN7vBGsqgfXk95ObV5clO7oAokHD7BdOP9YMh8ugAU21TjM2qPZ";
        System.out.println(publicKey);
        String privateKey = ECCUtil.getPrivateKey(keyMap);
//        String privateKey = "MDMCAQAwEAYHKoZIzj0CAQYFK4EEAAEEHDAaAgEBBBUA2CbEdwTe0xcOyR3ABxZMDZn4pe8=";
        System.out.println(privateKey);

        byte[] encodedData = ECCUtil.encrypt(data, publicKey);
        System.out.println("encode: " + new String(encodedData));

        byte[] decodedData = ECCUtil.decrypt(encodedData, privateKey);

        System.out.println("decode: " + new String(decodedData));

    }

}
