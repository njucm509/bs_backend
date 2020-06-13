package cn.edu.njucm.wp.bs.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IDConvert {

    public static String convert(List<Long> ids) {
        Collections.sort(ids);
        int len = ids.size();
        Long start = ids.get(0);
        Long end = ids.get(len - 1);
        Long count = start;
        StringBuilder ss = new StringBuilder();
        ss.append("[").append(start);
        // 1 2 4 5 7 8 9
        for (int i = 0; i < len; i++) {
            Long id = ids.get(i);
            if (count.equals(id) && !ss.toString().endsWith("[") && count < end) {
                count++;
                continue;
            }

            if (count < id && !ss.toString().endsWith("[")) {
                ss.append("-").append(count - 1).append("],").append("[").append(id);
                i--;
                count++;
            }

            if (count.equals(end)) {
                ss.append("-").append(count).append("]");
            }
        }

        return ss.toString();
    }

    public static String convert2db(List<Long> ids) {
        return convert(ids).replaceAll("\\[", "").replaceAll("\\]", "");
    }

    public static void main(String[] args) {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);
//        ids.add(4L);
        ids.add(5L);
        ids.add(6L);
        ids.add(7L);
        ids.add(8L);
        ids.add(9L);

        System.out.println(convert2db(ids));

        String text = "[xxx]";
        System.out.println(text.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]"));

    }
}
