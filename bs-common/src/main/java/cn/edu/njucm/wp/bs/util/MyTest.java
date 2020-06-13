package cn.edu.njucm.wp.bs.util;

import java.math.BigDecimal;
import java.util.Arrays;

public class MyTest {
    public static void main(String[] args) {
        String text = "1|20200524224744|1-120";
        String[] ss = text.split("\\|");
        System.out.println(Arrays.toString(ss));

    }
}
