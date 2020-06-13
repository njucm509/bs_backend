package cn.edu.njucm.wp.bs.release.anonymity.k_;


import cn.edu.njucm.wp.bs.release.anonymity.fanhua.GeneralizeSecondProperties;
import cn.edu.njucm.wp.bs.release.anonymity.util.ArrayMaxOrMin;
import cn.edu.njucm.wp.bs.release.anonymity.util.MyRandom;
import cn.edu.njucm.wp.bs.release.anonymity.util.Similarity;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KAnonymous {
    public static Object[][] k_anonymous(Object[][] data, int k, double sim) {
//        获得原始数据表
//        第一行是列名
        Object[][] tmp = data;

        int tableLength = data.length - 1;
        int classifiedGroupNum = tableLength % k == 0 ? tableLength / k : tableLength / k + 1;

        Object[][][] center = new Object[classifiedGroupNum][k][tmp[0].length];

        int[] all = new int[tableLength];
        for (int i = 0; i < tableLength; i++) {
            all[i] = i + 1;
        }
        //随机中心行号
        int[] rand = MyRandom.getRandonInRange(1, tableLength, classifiedGroupNum);
        List<Integer> list = new ArrayList();
        for (int i = 0; i < rand.length; i++) {
            list.add(rand[i]);
        }
        //剩余的行号
        List<Integer> rest = new ArrayList<>();
        for (int i : all) {
            if (!list.contains(i)) {
                rest.add(i);
            }
        }

        //        每个组分配一个中心
        for (int i = 0; i < classifiedGroupNum; i++) {
            for (int j = 0; j < tmp[0].length; j++) {
                center[i][0][j] = tmp[rand[i]][j];
            }
        }


        Object[][] restObj = new Object[rest.size() + 1][tmp[0].length];
        for (int i = 0; i < restObj.length - 1; i++) {
            for (int j = 0; j < restObj[i].length; j++) {
                restObj[i + 1][j] = tmp[rest.get(i)][j];
            }
        }
        //剩余未分类的 第一行为列名
        for (int i = 0; i < restObj[0].length; i++) {
            restObj[0][i] = tmp[0][i];
        }

        int age_index = -1;
        int csrq_index = -1;
        int sex_index = -1;
        for (int i = 0; i < restObj[0].length; i++) {
            if (restObj[0][i].equals("AGE")) {
                age_index = i;
            }

            if (restObj[0][i].equals("CSRQ")) {
                csrq_index = i;
            }

            if (restObj[0][i].equals("SEX")) {
                sex_index = i;
            }
        }

        if (age_index == -1) {
            return null;
        }

        List<Object[]> list1 = new ArrayList<>();
        for (int i = 1; i < restObj.length; i++) {
            list1.add(restObj[i]);
        }

        //初始化rest是否被使用矩阵
        int[] isUsed = new int[restObj.length - 1];

        //初始化center分类
        int[][] isInCenter = new int[classifiedGroupNum][k - 1];

        //获取全部距离相似度 分类中心index对应全部未分类
        double[][] ageSimilarity = new double[classifiedGroupNum][restObj.length - 1];

        for (int i = 0; i < ageSimilarity.length; i++) {
            for (int j = 0; j < list1.size(); j++) {
                ageSimilarity[i][j] = Similarity.getAgeSimilarity((String) center[i][0][age_index], (String) list1.get(j)[age_index]);
            }
        }

        //把相似度矩阵中 每行前k-1大且大于sim的索引放入index中并将isUsed[]置为一
//        System.out.println("age index:" + age_index);
        for (int i = 0; i < ageSimilarity.length; i++) {
            for (int j = 0; j < k - 1; j++) {
                int max = ArrayMaxOrMin.getMaxOrMin(ageSimilarity[i], sim, "max", isUsed);
                if (max != -1 && isUsed[max] == 0) {
                    isUsed[max] = 1;
                    for (int l = 0; l < center[i][j + 1].length; l++) {
                        center[i][j + 1][l] = restObj[max + 1][l];
                    }
                    isInCenter[i][j] = 1;
                }
            }
        }

        List<Object[]> no = new ArrayList<>();
        for (int i = 0; i < isUsed.length; i++) {
            if (isUsed[i] == 0) {
                Object[] o = new Object[data[0].length];
                for (int j = 0; j < o.length; j++) {
                    o[j] = restObj[i + 1][j];
                }
                no.add(o);
            }
        }

        int[] noUse = new int[no.size()];
        double[][] noSim = new double[classifiedGroupNum][no.size()];
        for (int i = 0; i < noSim.length; i++) {
            for (int j = 0; j < no.size(); j++) {
                noSim[i][j] = Similarity.getAgeSimilarity(String.valueOf(center[i][0][age_index]), String.valueOf(no.get(j)[age_index]));
            }
        }

        //将未分类的补位
        for (int i = 0; i < center.length; i++) {
            for (int j = 0; j < center[i].length - 1; j++) {
                if (isInCenter[i][j] == 0 && hasNoUse(noUse)) {
                    int index = ArrayMaxOrMin.getMax(noSim[i], noUse);
                    if (index == -1) {
                        break;
                    }

                    for (int l = 0; l < center[i][j + 1].length; l++) {
                        center[i][j + 1][l] = no.get(index)[l];
                    }
                    noUse[index] = 1;
                }
            }
        }

        Object[][] result = new Object[classifiedGroupNum * k + 1][data[0].length];

        for (int i = 0; i < restObj[0].length; i++) {
            result[0][i] = restObj[0][i];
        }

        int t = -1;
        for (int i = 0; i < classifiedGroupNum; i++) {
            if (sex_index != -1) {
                t = Math.random() > 0.5 ? 1 : 0;
            }
            for (int j = 0; j < k; j++) {
                for (int l = 0; l < center[i][j].length; l++) {
                    if (t == 1) {
                        center[i][j][sex_index] = "*";
                    }

                    if (csrq_index != -1) {
                        center[i][j][csrq_index] = "*";
                    }

                    if (i * k + j + 1 <= data.length - 1) {
                        result[i * k + j + 1][l] = center[i][j][l];
                    } else {
                        break;
                    }
                }
            }
        }

        return result;
    }

    private static boolean hasNoUse(int[] use) {
        for (int i = 0; i < use.length; i++) {
            if (use[i] == 0) {
                return true;
            }
        }
        return false;
    }

    private static void print(Object[][] obj) {
        int rows = obj.length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < obj[i].length; j++) {
                System.out.print(obj[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        String prefix = "/Users/weilin/Downloads/";
        String _120_34kb = "field_120_34kb.csv";
        String _1200_335kb = "field_1200_335kb.csv";
        String _3000_972kb = "field_3000_972kb.csv";
        String _6000_1_9mb = "field_6000_1_9mb.csv";
        String _12000_3_3mb = "field_12000_3_3mb.csv";
        String _24000_6_7mb = "field_24000_6_7mb.csv";

        String test = prefix + _6000_1_9mb;
        int len = 6001;

        File file = new File(test);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
        String line;
        Object[][] obj = new Object[len][];
        int i = 0;
        while ((line = br.readLine()) != null) {
            String[] ss = line.split(",");
            obj[i++] = ss;
        }

        Object[][] ages = GeneralizeSecondProperties.generalizeSecondProperty(obj, "age", 10);

        long start = System.nanoTime();
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMem = runtime.totalMemory() - runtime.freeMemory();


        Object[][] res = k_anonymous(ages, 8, 0.6);

        long end = System.nanoTime();
        long endMem = runtime.totalMemory() - runtime.freeMemory();
        //输出
        System.out.println("TimeCost: " + (end - start) / 1000000 + "ms");
        System.out.println("MemoryCost: " + String.valueOf((endMem - startMem) / 1024 / 1024) + "MB");
//        print(res);

    }

}
