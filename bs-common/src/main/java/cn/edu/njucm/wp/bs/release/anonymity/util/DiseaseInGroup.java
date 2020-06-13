package cn.edu.njucm.wp.bs.release.anonymity.util;

public class DiseaseInGroup {
    public static boolean exist(String[] str, String disease) {
        boolean flag = false;
        for (int i = 0; i < str.length; i++) {
            if (str[i] != null) {
                if (str[i].equals(disease)) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
