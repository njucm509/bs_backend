package cn.edu.njucm.wp.bs.util;


import cn.edu.njucm.wp.bs.params.FileHeaderParam;

import java.util.HashMap;
import java.util.List;

public class ParamUtil {
    /**
     * 加密列转换
     * @param list
     * @return
     */
    public static HashMap<String, String> convert(List<FileHeaderParam> list) {
        HashMap<String, String> map = new HashMap<>();
        for (FileHeaderParam p : list) {
            if (p.getDefaultEnc() == 0) {
                map.put(p.getContent(), "no");
            } else {
                map.put(p.getContent(), p.getEncryption().toUpperCase());
            }
        }
        return map;
    }
}
