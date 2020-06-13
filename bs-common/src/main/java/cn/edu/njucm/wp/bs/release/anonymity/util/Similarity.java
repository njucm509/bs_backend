package cn.edu.njucm.wp.bs.release.anonymity.util;


import cn.edu.njucm.wp.bs.release.anonymity.fanhua.Generalization;
import cn.edu.njucm.wp.bs.release.anonymity.util.tree.GeneralizationTree;
import cn.edu.njucm.wp.bs.release.anonymity.util.tree.NodeDistance;
import cn.edu.njucm.wp.bs.release.anonymity.util.tree.TreeNode;

import static java.lang.Math.log10;

public class Similarity {
    public static double getAgeSimilarity(String center, String arround) {
        TreeNode<String> root = Generalization.root();
        TreeNode<String> nodeCenter = GeneralizationTree.getNodeByData(root, center);
        TreeNode<String> nodeArround = GeneralizationTree.getNodeByData(root, arround);
        if (nodeCenter == null || nodeArround == null) {
            return 0;
        }

        int p = NodeDistance.getTwoTreeNodeDistence(root, nodeCenter, nodeArround);
        if (p == 0) {
            return 1;
        }

        double depth = Math.min(nodeCenter.getLevel(), nodeArround.getLevel());
        return -(log10(p / (2 * depth)));
    }

    public static void main(String[] args) {
        double sim = getAgeSimilarity("[0-5)", "[10-15)");
        System.out.println(sim);
    }
}
