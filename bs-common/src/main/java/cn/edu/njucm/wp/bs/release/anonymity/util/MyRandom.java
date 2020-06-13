package cn.edu.njucm.wp.bs.release.anonymity.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyRandom {
    public static int[] getRandonInRange(int start, int end, int num) {
        int[] all = new int[end - start + 1];
        int[] result = new int[num];
        for (int i = 0; i < all.length; i++) {
            all[i] = start++;
        }
        List list = new ArrayList();
        for (int i = 0; i < all.length; i++) {
            list.add(all[i]);
        }
        Collections.shuffle(list);
        for (int i = 0; i < result.length; i++) {
            result[i] = (int) list.get(i);
        }
        return result;
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(MyRandom.getRandonInRange(4, 25, 10)[i]);
        }
    }
}
