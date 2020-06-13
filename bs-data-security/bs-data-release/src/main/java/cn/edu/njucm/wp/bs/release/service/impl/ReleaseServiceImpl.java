package cn.edu.njucm.wp.bs.release.service.impl;

import cn.edu.njucm.wp.bs.data.pojo.Field;
import cn.edu.njucm.wp.bs.data.pojo.Released;
import cn.edu.njucm.wp.bs.data.pojo.UploadDataRecord;
import cn.edu.njucm.wp.bs.release.anonymity.fanhua.GeneralizeSecondProperties;
import cn.edu.njucm.wp.bs.release.anonymity.k_.KAnonymous;
import cn.edu.njucm.wp.bs.release.client.DataClient;
import cn.edu.njucm.wp.bs.release.service.ReleaseService;
import cn.edu.njucm.wp.bs.release.vo.HandleForm;
import cn.edu.njucm.wp.bs.release.vo.ReleaseBean;
import cn.edu.njucm.wp.bs.util.IDConvert;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService, Serializable {

    @Autowired
    transient DataClient dataClient;

    @Autowired
    transient JavaSparkContext sc;

    @Override
    public HashMap<String, Object> kAnonymous(HandleForm form) {
        Long id = form.getUserId();
        List<UploadDataRecord> curData = form.getCurData();
        List<String> date = curData.stream().map(UploadDataRecord::getUploadDate).distinct().collect(Collectors.toList());
        HashMap<String, Object> m = new HashMap<>();
        m.put("id", id);
        m.put("curDate", date);
        List<Map<String, Object>> data = dataClient.getUserDataByIdAndDate(m);
        List<Field> fields = form.getFields();

        // 过滤主标识属性
        Object[] headers = fields.stream().filter((field -> field.getTypeId() != 1)).map(Field::getSysName).distinct().toArray();
        Object[][] contents = new Object[data.size() + 1][];

        contents[0] = new Object[headers.length];
        System.arraycopy(headers, 0, contents[0], 0, headers.length);

        int count;
        int row = 1;
        for (Map<String, Object> line : data) {
            count = 0;
            Object[] content = new Object[line.size()];
            for (Object header : headers) {
                if (line.containsKey(header)) {
                    content[count++] = line.get(header);
                } else {
                    count++;
                }
            }
            contents[row++] = content;
        }


        Object[][] obj_age = GeneralizeSecondProperties.generalizeSecondProperty(contents, "AGE", form.getAgeLevel());
        Object[][] result = KAnonymous.k_anonymous(obj_age, form.getK(), form.getSim());

        StringBuilder cols = new StringBuilder();
        cols.append("user_id,date,");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDate = sdf.format(new Date());
        List<String> items = new ArrayList<>();
        for (int i = 0; i < result.length; i++) {
            if (i == 0) {
                String header = Arrays.toString(result[0]);
                header = header.substring(1, header.length() - 1);
                cols.append(header);
            } else {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < result[i].length; j++) {
                    sb.append(",'" + result[i][j] + "'");
                }
                String item = sb.toString();
                item = "('" + form.getUserId() + "','" + nowDate + "'" + item + ")";
                items.add(item);
            }
        }

        HashMap<String, Object> body = new HashMap<>();
        body.put("cols", cols);
        body.put("items", items);
        body.put("curDate", nowDate);
        body.put("userId", form.getUserId());
        List<Long> ids = dataClient.insert2db(body);
        String lines = IDConvert.convert2db(ids);
        System.out.println(lines);

        HashMap<String, Object> res = new HashMap<>();
        res.put("curDate", nowDate);
        res.put("lines", lines);

        return res;
    }

    @Override
    public String kAnonymousBySpark(HandleForm form) {
        Long id = form.getUserId();
        List<Map<String, Object>> data = dataClient.getUserDataById(id);
        List<Field> fields = form.getFields();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowDate = sdf.format(new Date());

        return nowDate;
    }

    @Override
    public Integer handleRelease(ReleaseBean bean) {
        String releaseDate = bean.getReleaseDate();
        HandleForm form = bean.getForm();
        Released released = new Released();
        List<Field> fields = form.getFields();
        List<Long> fieldIds = fields.stream().map(Field::getId).distinct().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (Long id : fieldIds) {
            sb.append(id).append(",");
        }

        released.setUsedFields(sb.toString());

        if (form.getUserId() != null) {
            released.setUserId(form.getUserId());
        }

        released.setHandleFormId(form.getId());

        List<UploadDataRecord> curData = form.getCurData();
        List<String> lines = curData.stream().map(UploadDataRecord::getUploadLines).collect(Collectors.toList());
        List<String> date = curData.stream().map(UploadDataRecord::getUploadDate).collect(Collectors.toList());
        StringBuilder originData = new StringBuilder(String.valueOf(form.getUserId()));
        originData.append("|").append(date.get(0)).append("|");
        for (String line : lines) {
            originData.append(line);
        }

        released.setOriginData(originData.toString());

        List<Long> ids = dataClient.getIdsByDateAndUserId("init_data_all_field_k", releaseDate, form.getUserId());
        StringBuilder releasedData = new StringBuilder(String.valueOf(form.getUserId()));
        releasedData.append("|").append(releaseDate).append("|").append(IDConvert.convert2db(ids));
        released.setReleasedData(releasedData.toString());

        System.out.println(released);

        Integer releaseId = dataClient.create(released);

        return releaseId;
    }

    private static void print(Object[][] contents) {
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                System.out.print(contents[i][j] + "\t|");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
//        Object[][] obj = {
//                {"XB", "CSRQ", "AGE", "GMS"},
//                {"0", "1", "[0-10)", "1"},
//                {"1", "11", "[10-20)", "1"},
//                {"1", "12", "[20-30)", "1"},
//                {"1", "13", "[30-40)", "1"},
//                {"1", "14", "[40-50)", "1"},
//                {"1", "15", "[50-60)", "1"},
//                {"1", "16", "[60-70)", "1"},
//                {"0", "17", "[70-80)", "1"},
//                {"0", "18", "[80-90)", "1"},
//                {"1", "19", "[0-10)", "1"},
//                {"0", "10", "[10-20)", "1"},
//                {"1", "111", "[20-30)", "1"},
//                {"0", "123", "[30-40)", "1"},
//                {"1", "142", "[40-50)", "1"},
//                {"0", "15234", "[50-60)", "1"},
//                {"0", "222", "[60-70)", "1"},
//                {"0", "234", "[70-80)", "1"},
//                {"0", "245", "[80-90)", "1"},
//                {"1", "19", "[0-10)", "1"},
//                {"0", "10", "[10-20)", "1"},
//                {"1", "1151", "[20-30)", "1"},
//                {"0", "1423", "[30-40)", "1"},
//                {"1", "1462", "[40-50)", "1"},
//                {"0", "175234", "[50-60)", "1"},
//                {"0", "2272", "[60-70)", "1"},
//                {"1", "2384", "[70-80)", "1"},
//                {"0", "2495", "[80-90)", "1"},
//        };

        Object[][] obj = {
                {"XB", "CSRQ", "AGE", "ID"},
                {"0", "1", "1", "1"},
                {"1", "11", "11", "2"},
                {"1", "12", "22", "3"},
                {"1", "13", "33", "4"},
                {"1", "14", "94", "5"},
                {"1", "15", "55", "6"},
                {"1", "16", "66", "7"},
                {"0", "17", "77", "8"},
                {"0", "17", "88", "9"},
                {"1", "19", "1", "10"},
                {"0", "10", "11", "11"},
                {"1", "111", "22", "12"},
                {"0", "123", "33", "13"},
                {"1", "142", "44", "14"},
                {"0", "15234", "55", "15"},
                {"0", "222", "66", "16"},
                {"0", "234", "77", "17"},
                {"0", "234", "85", "18"},
                {"1", "19", "1", "19"},
                {"0", "10", "11", "20"},
                {"1", "1151", "102", "21"},
                {"0", "1423", "113", "22"},
                {"1", "1462", "44", "23"},
                {"0", "175234", "55", "24"},
                {"0", "2272", "66", "25"},
                {"1", "2384", "77", "26"},
        };

        Object[][] obj1 = GeneralizeSecondProperties.generalizeSecondProperty(obj, "age", 5);

//        print(obj1);

//        System.out.println("===============");

        Object[][] objects = KAnonymous.k_anonymous(obj1, 3, 0.7);

//        GeneralizationTree nodeByAgeName = ForeachTree.getNodeByAgeName("[60-70)");
//        System.out.println("[60-70):" + nodeByAgeName);

        print(objects);
    }

}
