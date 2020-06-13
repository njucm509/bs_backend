package cn.edu.njucm.wp.bs.encrypt.service.impl;

import cn.edu.njucm.wp.bs.encrypt.HE.HE;
import cn.edu.njucm.wp.bs.encrypt.HE.HEUtil;
import cn.edu.njucm.wp.bs.encrypt.MyEncryptUtil;
import cn.edu.njucm.wp.bs.encrypt.client.DataClient;
import cn.edu.njucm.wp.bs.encrypt.config.HDFSConfig;
import cn.edu.njucm.wp.bs.params.FileHeaderParam;
import cn.edu.njucm.wp.bs.params.FileParam;
import cn.edu.njucm.wp.bs.encrypt.pojo.EncRecord;
import cn.edu.njucm.wp.bs.encrypt.service.EncRecordService;
import cn.edu.njucm.wp.bs.encrypt.service.EncryptionService;
import cn.edu.njucm.wp.bs.search.Search;
import cn.edu.njucm.wp.bs.util.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EncryptionServiceImpl implements EncryptionService, Serializable {

    @Autowired
    transient ServletContext context;

    @Autowired
    transient SparkSession sparkSession;

    @Autowired
    private transient EncRecordService encRecordService;

    @Autowired
    private transient DataClient dataClient;

    private static final String SEPARATOR = File.separator;

    @Override
    public List<List<String>> encrypt(FileParam param) throws IOException {
        String filename = param.getFilename();
        String userFilePath = HDFSConfig.userFilePath;
        if (userFilePath == null) {
            userFilePath = "/input";
        }
        List<String> encOptions = new ArrayList<String>() {
            {
                this.add("RSA");
                this.add("AES");
                this.add("ECC");
                this.add("MD5");
            }
        };

        List<HashMap<String, List<String>>> tmp = new ArrayList<>();
        List<List<String>> res = new ArrayList<>();
        List<FileHeaderParam> list = param.getList();
        List<String> colNames = list.stream().map(FileHeaderParam::getContent).collect(Collectors.toList());
        HashMap<String, String> convert = ParamUtil.convert(list);

        ObjectMapper objectMapper = new ObjectMapper();
        // 操作记录
        EncRecord record = new EncRecord();
//        record.setUserId(param.getUser().getId());
        record.setFilename(param.getFilename());
        record.setRecord(objectMapper.writeValueAsString(convert));
        encRecordService.create(record);

        // 查找密钥
        List<HashMap<String, String>> keyList = null;
//                secretKeyService.getSecretKeyByUser(param.getUser().getId().intValue());


        log.info("key list: {}", keyList);

        System.out.println(colNames);
        sparkSession.udf().register("encryptByAES", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                log.info("s: {} ", s);
                return MyEncryptUtil.encrypt(s, "aes", keyList);
            }
        }, DataTypes.StringType);

        sparkSession.udf().register("encryptByECC", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                log.info("s: {} ", s);
                return MyEncryptUtil.encrypt(s, "ecc", keyList);
            }
        }, DataTypes.StringType);

        sparkSession.udf().register("encryptByMD5", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                log.info("s: {} ", s);
                return MyEncryptUtil.encrypt(s, "md5", keyList);
            }
        }, DataTypes.StringType);

        sparkSession.udf().register("encryptByRSA", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                log.info("s: {} ", s);
                return MyEncryptUtil.encrypt(s, "rsa", keyList);
            }
        }, DataTypes.StringType);

        sparkSession.udf().register("encryptByNO", new UDF1<String, String>() {
            @Override
            public String call(String s) throws Exception {
                log.info("s: {} ", s);
                return s;
            }
        }, DataTypes.StringType);
        Dataset<Row> csv = null;
        if (filename.endsWith("csv")) {
            csv = sparkSession.read().option("header", "true").csv(userFilePath + SEPARATOR + filename);
        }
        if (filename.endsWith("xls") || filename.endsWith("xlsx")) {

        }
        csv.createOrReplaceTempView("temp");
        SQLContext context = csv.sqlContext();
        for (String colName : colNames) {
            char[] chars = colName.toCharArray();
            System.out.println("colName: " + chars.length);
            String realColName = "";
            for (int i = 0; i < chars.length; i++) {
                if (i == 0 && (byte) chars[0] == -1) {
                    continue;
                }
                realColName += chars[i];
            }
            System.out.println("realColName: " + realColName.length());
            log.info("当前列名:{}", colName);
            HashMap<String, List<String>> map = new HashMap<>();
            String enc = convert.get(colName).toUpperCase();
            Dataset<Row> col = null;
            if (encOptions.contains(enc)) {
                col = context.sql("select encryptBy" + enc + "(`" + realColName + "`) from temp");
            } else if (enc.equals("NO")) {
                col = context.sql("select encryptBy" + enc + "(`" + realColName + "`) from temp");
            }
            col.show();
            List<Row> rows = col.collectAsList();
            List<String> inner = new ArrayList<>();
            for (Row row : rows) {
                inner.add((String) row.get(0));
            }
            map.put(colName, inner);
            tmp.add(map);
        }
        log.info("tmp: {}", tmp);
        // 列转行
        List<String> headers = new ArrayList<>();
        List<List<String>> con = new ArrayList<>();
        for (HashMap<String, List<String>> in : tmp) {
            Map.Entry<String, List<String>> next = in.entrySet().iterator().next();
            headers.add(next.getKey());
            con.add(next.getValue());
        }
        String[][] str = new String[con.get(0).size()][con.size()];
        for (int i = 0; i < con.size(); i++) {
            List<String> cur = con.get(i);
//                str = new String[cur.size()][];
            for (int j = 0; j < cur.size(); j++) {
//                    str[j] = new String[con.size()];
                log.debug("cur ..{}", cur.get(j));
                str[j][i] = cur.get(j);
                System.out.println(str[j][i]);
            }
        }
        res.add(headers);
        for (int i = 0; i < str.length; i++) {
            res.add(Arrays.asList(str[i]));
        }
        log.info("res: {}", res);
        return res;
    }

    @Override
    public Boolean encryptByHe(Long userId, Integer fileId, String filename) {
        cn.edu.njucm.wp.bs.data.pojo.File file = new cn.edu.njucm.wp.bs.data.pojo.File();
        file.setUserId(userId);
        file.setName(filename);
        File tmp = dataClient.downFileFromHDFS(file);
        if (tmp == null) {
            return false;
        }

        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(tmp));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sb.length() > 0) {
            HashMap<String, String> fileArg = dataClient.getFileArg(fileId);
            String p = fileArg.get("he_p");
            String q = fileArg.get("he_q");
            String n = fileArg.get("he_n");
            String r1 = fileArg.get("he_r1");
            HE h = new HE(new BigInteger(p), new BigInteger(q), new BigInteger(r1));
            HEUtil he = new HEUtil();
            he.setHe(h);
            String encrypt = he.encrypt(sb.toString());
            try {
                File fi = new File("bs-data-security/bs-data-encryption/HE/");
                if (!fi.exists()) {
                    fi.mkdirs();
                }

                System.out.println(fi.getAbsolutePath() + "/" + fileId + "_" + filename);

                FileWriter f = new FileWriter(fi.getAbsolutePath() + "/" + fileId + "_" + filename);
                f.write(encrypt);
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            return false;
        }

        return true;
    }

    @Override
    public List<String> searchByHe(String key) {
        List<String> res = new ArrayList<>();
        File fi = new File("bs-data-security/bs-data-encryption/HE/");
        File[] files = fi.listFiles();
        for (File file : files) {
            String text = null;
            try {
                text = readFile2Str(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] ss = file.getName().split("_");
            Integer fileId = Integer.parseInt(ss[0]);
            HashMap<String, String> fileArg = dataClient.getFileArg(fileId);
            String p = fileArg.get("he_p");
            String q = fileArg.get("he_q");
            String n = fileArg.get("he_n");
            String r1 = fileArg.get("he_r1");

            HE h = new HE(new BigInteger(p), new BigInteger(q), new BigInteger(r1));
            HEUtil he = new HEUtil();
            he.setHe(h);
            String encryptKey = he.encrypt(key);
            boolean flag = Search.compute(new BigInteger(q), new BigInteger(r1), new BigInteger(n), encryptKey, text);
            if (flag) {
                res.add(file.getName().substring(ss[0].length() + 1));
            }
        }

        return res;
    }

    private String readFile2Str(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        File fi = new File("bs-data-security/bs-data-encryption/HE/");
        System.out.println(fi.getAbsolutePath());

        System.out.println("66_test.txt".substring(3));
    }
}
