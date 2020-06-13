package cn.edu.njucm.wp.bs.util;

import cn.edu.njucm.wp.bs.data.pojo.FileArg;
import cn.edu.njucm.wp.bs.encrypt.HE.HE;
import cn.edu.njucm.wp.bs.encrypt.aes.AESUtil;
import cn.edu.njucm.wp.bs.encrypt.rsa.RSAUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FileArgGenerater {
    public static FileArg initKey(Integer fileId) throws Exception {
        FileArg fileArg = new FileArg();
        fileArg.setFileId(fileId);

        String aesKey = AESUtil.generateKey(fileId);
        fileArg.setAesKey(aesKey);

        HashMap<String, String> rsa = RSAUtil.generateKeyPair();

        ObjectMapper objectMapper = new ObjectMapper();
        String rsaStr = objectMapper.writeValueAsString(rsa);
        fileArg.setRsaKey(rsaStr);

        HE he = new HE();

        HashMap<String, String> h = new HashMap<>();
        h.put("p", he.getP().toString());
        h.put("q", he.getQ().toString());
        h.put("n", he.getN().toString());
        h.put("r1", he.getR1().toString());
        String heStr = objectMapper.writeValueAsString(h);
        fileArg.setHeArg(heStr);

        fileArg.setCreatedAt(new Date());
        fileArg.setUpdatedAt(new Date());

        return fileArg;
    }

    public static HashMap<String, String> getFileArg(FileArg arg) {
        if (arg == null) {
            return null;
        }

        String aesKey = arg.getAesKey();


        HashMap<String, String> res = new HashMap<String, String>() {
            {
                this.put("aes", aesKey);
            }
        };

        String rsaKey = arg.getRsaKey();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap rsa = null;
        try {
            rsa = objectMapper.readValue(rsaKey, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String publicKey = (String) rsa.get("publicKey");
        String privateKey = (String) rsa.get("privateKey");

        res.put("rsa_publicKey", publicKey);
        res.put("rsa_privateKey", privateKey);

        String heArg = arg.getHeArg();
        HashMap he = null;
        try {
            he = objectMapper.readValue(heArg, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String p = (String) he.get("p");
        String q = (String) he.get("q");
        String n = (String) he.get("n");
        String r1 = (String) he.get("r1");

        res.put("he_p", p);
        res.put("he_q", q);
        res.put("he_n", n);
        res.put("he_r1", r1);

        return res;
    }

    public static void main(String[] args) throws Exception {
        FileArg arg = initKey(1);
        System.out.println(arg);

        HashMap<String, String> res = getFileArg(arg);
        System.out.println(res);
    }

}
