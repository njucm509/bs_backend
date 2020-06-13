package cn.edu.njucm.wp.bs.release.anonymity.fanhua;

import cn.edu.njucm.wp.bs.release.anonymity.util.tree.GeneralizationTree;
import cn.edu.njucm.wp.bs.release.anonymity.util.tree.TreeNode;

public class Generalization {

    private static TreeNode<String> root;

    private static void init() {
        if (root == null) {
            root = GeneralizationTree.init();
        }
    }

    public static String ageGeneralization(int age, int range) {
        init();
        return GeneralizationTree.generate(root, age, range);
    }

    public static String addressGeneralization(String address, int level) {
        int start = 2;
        int end = 6;
        char change = "*".charAt(0);
        char[] result = address.toCharArray();
        for (int i = 0; i < result.length; i++) {
            if (start <= i && i < end) {
                result[i] = change;
            }
        }

        return new String(result);
    }

    public static TreeNode<String> root() {
        if (root == null) {
            return GeneralizationTree.init();
        }
        return root;
    }
}
