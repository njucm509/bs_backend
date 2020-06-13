package cn.edu.njucm.wp.bs.release.anonymity.util;

public class ArrayMaxOrMin {
    private static int count = 0;

    public static int getMaxOrMin(double[] a, double sim, String extreme, int[] isUsed) {
        int index = -1;
        double min = sim;
        double max = sim;
        for (int i = 0; i < a.length; i++) {
            if (extreme.equals("min") && isUsed[i] == 0) {
                if (min > a[i]) {
                    min = a[i];
                    index = i;
                    count++;
                }
            }
            if (extreme.equals("max") && isUsed[i] == 0) {
                if (max < a[i]) {
                    max = a[i];
                    index = i;
                    count++;
                }
            }
        }
        return index;
    }

    public static int getCount() {
        return count;
    }

    public static int getMax(double[] sims, int[] noUse) {
        double max = -1;
        int index = -1;
        for (int i = 0; i < noUse.length; i++) {
            if (noUse[i] == 0) {
                max = sims[i];
                index = i;
                break;
            }
        }

        if (max == -1) {
            return index;
        }

        for (int i = index; i < sims.length; i++) {
            if (noUse[i] == 1) {
                continue;
            }

            if (max < sims[i]) {
                index = i;
            }
        }

        return index;
    }

    public static void main(String[] args) {
        double[] a = {1.0, 0.6, 0.6, 1.0, 0.6};
        int[] no = {1, 1, 1, 1, 1};
        int index = getMax(a, no);
        System.out.println(index);
    }

}
