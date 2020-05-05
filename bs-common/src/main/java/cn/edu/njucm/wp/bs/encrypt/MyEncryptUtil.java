package cn.edu.njucm.wp.bs.encrypt;

import cn.edu.njucm.wp.bs.encrypt.aes.AESUtil;
import cn.edu.njucm.wp.bs.encrypt.md5.MD5Util;
import cn.edu.njucm.wp.bs.encrypt.rsa.RSAUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MyEncryptUtil {

    private static final String AES_KEY = "1234567812345678";
    private static Map<String, String> RSA_MAP = new HashMap<String, String>() {
        {
            this.put("publicKey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApTMf7BdTSfOcFYgoFHbf2haSuy3gGxqXaUvW/rLOWfUO49FSY8mDDeA3QmO2I4G5cLuIsRWQ32So95JbQAvWfMbqF57XoYAkTIo5MkRiAEAIACSWhMDqWb+weNJmyB/JjHnWgIXiLPmJxRJfwUgnXsKLXAMcqlKJfBLyVyHAHrLKzTU5Xg9yaHbKabTQ37lB9Y7vyqkPtWm7m1Kru2iFCweJvCTxK+n+USBRIz4NcS5RPfUJimbEa4NE8fpgcsYFEhIxGqkPcYYF7JqbXrC3ZNeUIiCMHo75Ql8FCs8wDt+z6umXDQ/hhSJWfXrQC5wKJXEDVDHGQ60ZVjy5TdKLHwIDAQAB");
            this.put("privateKey", "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQClMx/sF1NJ85wViCgUdt/aFpK7LeAbGpdpS9b+ss5Z9Q7j0VJjyYMN4DdCY7Yjgblwu4ixFZDfZKj3kltAC9Z8xuoXntehgCRMijkyRGIAQAgAJJaEwOpZv7B40mbIH8mMedaAheIs+YnFEl/BSCdewotcAxyqUol8EvJXIcAessrNNTleD3JodspptNDfuUH1ju/KqQ+1abubUqu7aIULB4m8JPEr6f5RIFEjPg1xLlE99QmKZsRrg0Tx+mByxgUSEjEaqQ9xhgXsmptesLdk15QiIIwejvlCXwUKzzAO37Pq6ZcND+GFIlZ9etALnAolcQNUMcZDrRlWPLlN0osfAgMBAAECggEBAJougwaoJnYIxY5bC30+DWLQWpYUFOt3AAWoYLpKS8ktrH336Tt04bnbn/d4dI/jDR4oCUWtaQ72NbUKhdvasI64X9c3yuzSWg6NmyBNLmIpYM4xV8IDAzZMb/nz9TWmZUeUlxxQ2G09YNXrdi9Alm4ciFCSHvjgG60PSdsEhvieROQYAim84QYcQxdyyruII/s3BXy7sxXBn3FWgsxod14CClHyqkySN11Z5UmEk18aEgbWG0fPHd0XEgFKflRY5lkVbq3DwcpYRCOVUANCkqXWsNG6BdGuu6SUD72P5EbYqP2sQjCyqabEWY9vYynNLhaCmqoExkzkNC807d6TKFECgYEA8xfJxE9/6FUeRP/9ZrhimgTkoWHXCcRj/Xmha/8RZl4iuu9KPLoaEzWEEu2JxvyaAOScA2d9hOENLnhSNFkxtzx1qgtqRQ7aokvcGvUjxLEfo9obBFnk7FG4sRd28/hhAKZboicEm7e3jtubSa35CpB/rpTwlAViJHzUAfo1vV0CgYEArfiVCqiPT2bFU4zU9lxlf84y9FVyGsL48oLeG1YSFFxYaeZ5CPD13OVyZTgzANrLvVs7CAlwAkZt0kUA3GynKynHhTI9vwA68auaatjaU6yoDF8c3H0rCXPNcVVKlTUNYyR0xzuOHxUFDuJhwvKZf4Uz9ORbFdwcdhYd5yrCZqsCgYB61d5ikDl10O0GuMP3D9zjLu3IjoP2r4JHJxrmU3EbZR3vj9LXdMKQSk5+sgEYx7OaF+qt3bGTX5Ulki8XvKpeiPJ8BmCgnKzlrad/B7gXP9lZFnDodfQzZegJtG2rQiriBS3wdf7iIKdkTJvfU0WwjOu38BLQ+RdqkJkglqppEQKBgCBTDNZTJXWaYdW5WCSCXHWXyY3uhmoEih79Q+mOX4Oa+O6MXTbVFsm5dDzsaUWiYbLITbqbjk1N6r/kBFnwcksp0yGjiSeBEnKu8N0jd8WOUhciYQly1FlIRAbn0xjQJ7GhJ3WRUkHiNPNkCsgfLXgtNs8s0uPH5x3l1B6MqlxLAoGBAJf0oP1EbKE6J7uzOY+RPU3K8dCeo2bvEWbKpIVoT66PikJlKxH0YQvuwb7Tj8V0Yx65BoLBQ8AaGXAUBGYX4ElxmMi8yb9dCffYEeA+OpTg7NagHVn+dVLQttfQ8bRLbWTgmRilMkw57oHJyCK7jyo33ebNuNW8RSaWLxcy/bWj");
        }
    };

    private static Map<String, String> ECC_MAP = new HashMap<String, String>() {
        {
            this.put("publicKey", "MEAwEAYHKoZIzj0CAQYFK4EEAAEDLAAEAv4TwFN7vBGsqgfXk95ObV5clO7oAokHD7BdOP9YMh8ugAU21TjM2qPZ");
            this.put("privateKey", "MDMCAQAwEAYHKoZIzj0CAQYFK4EEAAEEHDAaAgEBBBUA2CbEdwTe0xcOyR3ABxZMDZn4pe8=");
        }
    };

    public static String encrypt(String content, String enc, List<HashMap<String, String>> keyList) throws Exception {
        HashMap<String, String> keyMap = keyList.get(0);
        String aesKey = keyMap.get("aes");
        int length = aesKey.length();
        byte[] bytes = aesKey.getBytes();
        int len = bytes.length;
        log.info("ase key len: {}", length);
        String publicKey = keyMap.get("rsa_publicKey");
        String res = null;
        switch (enc.toUpperCase()) {
            case "AES":
                res = AESUtil.encrypt(content.getBytes(), aesKey.getBytes());
                break;
            case "RSA":
                res = RSAUtil.encrypt(content, publicKey);
                break;
            case "ECC":
//                res = ECCUtil.encrypt(content.getBytes(), ECC_MAP.get("privateKey"));
                res = content;
                break;
            case "MD5":
                res = MD5Util.encrypt(content);
                break;
        }
        return res;
    }

    public static String decrypt(String encContent, String enc, List<HashMap<String, String>> keyList) throws Exception {
        HashMap<String, String> keyMap = keyList.get(0);
        String aesKey = keyMap.get("aes");
        String privateKey = keyMap.get("rsa_privateKey");
        String content = null;
        switch (enc.toUpperCase()) {
            case "AES":
                content = AESUtil.decrypt(encContent, aesKey.getBytes());
                break;
            case "RSA":
                content = RSAUtil.decrypt(encContent, privateKey);
                break;
            case "ECC":
                break;
        }
        return content;
    }

    public static String decrypt(String encContent, String enc) throws Exception {
        String content = null;
        switch (enc.toUpperCase()) {
            case "AES":
                content = AESUtil.decrypt(encContent, AES_KEY.getBytes());
                break;
            case "RSA":
                content = RSAUtil.decrypt(encContent, RSA_MAP.get("privateKey"));
                break;
            case "ECC":
                break;
        }
        return content;
    }

    public static void main(String[] args) throws Exception {
//        String con = "xxxxx";
//        String encrypt = encrypt(con, "rsa", param.getUser().getId());
//        System.out.println(encrypt);
//        String res = decrypt(encrypt, "rsa");
//        System.out.println(res);

    }
}
