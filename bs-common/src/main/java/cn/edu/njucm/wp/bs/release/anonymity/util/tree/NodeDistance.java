package cn.edu.njucm.wp.bs.release.anonymity.util.tree;

import java.util.LinkedList;

public class NodeDistance {
    public static int getTwoTreeNodeDistence(TreeNode<String> root, TreeNode<String> nodeOne, TreeNode<String> nodeTwo) {
        if (nodeOne.getData().equals(nodeTwo.getData())) {
            return 0;
        }

        TreeNode<String> fatherNode = findCommonFatherNode(root, nodeOne, nodeTwo);
        int x = distance(root, nodeOne) + distance(root, nodeTwo) - 2 * distance(root, fatherNode) - 1;
        return x;
    }

    private static int distance(TreeNode<String> root, TreeNode<String> node) {
        // 找到node所在的层数
        int level = 0;
        LinkedList<TreeNode<String>> queue = new LinkedList<>();
        queue.addLast(root);
        while (queue.size() > 0) {
            int size = queue.size();
            level++;
            for (int i = 0; i < size; i++) {
                TreeNode<String> tempNode = queue.removeFirst();
                if (tempNode == node) {
                    return level;
                }
                if (tempNode.getLeft() != null) {
                    queue.addLast(tempNode.getLeft());
                }
                if (tempNode.getRight() != null) {
                    queue.addLast(tempNode.getRight());
                }
            }
        }
        return -1;
    }

    private static TreeNode<String> findCommonFatherNode(TreeNode<String> root, TreeNode<String> nodeOne, TreeNode<String> nodeTwo) {
        // 递归寻找公共父节点
        if (root == null || root == nodeOne || root == nodeTwo) {
            return root;
        }
        TreeNode<String> left = findCommonFatherNode(root.getLeft(), nodeOne, nodeTwo); // 去root的左子树寻找
        TreeNode<String> right = findCommonFatherNode(root.getRight(), nodeOne, nodeTwo); // 去root的右子树寻找
        if (left == null && right == null) {
            // 都没有找到，返回null，说明没有公共祖先
            return null;
        }
        if (left == null) {
            // 左边没找到，右边找到了，说明，right就是公共祖先
            return right;
        }
        if (right == null) {
            return left;
        }
        return root;
    }

    public static void main(String[] args) {
//        NodeDistance x = new NodeDistance();
//        TreeNode root = new TreeNode(1);
//
//        TreeNode left_1 = new TreeNode(2);
//        TreeNode right_1 = new TreeNode(2);
//
//        root.setLeft(left_1);
//        root.setRight(right_1);
//
//        TreeNode left_2 = new TreeNode(3);
//        TreeNode right_2 = new TreeNode(3);
//
//        left_1.setLeft(left_2);
//        left_1.setRight(right_2);
//
//        TreeNode left_3 = new TreeNode(3);
//        TreeNode right_3 = new TreeNode(3);
//
//        right_1.setLeft(left_3);
//        right_1.setRight(right_3);
//
//        int distance = x.getTwoTreeNodeDistence(root, left_2, right_3);
//        System.out.println(distance);
    }
}
