package cn.edu.njucm.wp.bs.release.anonymity.util.tree;

import java.io.*;
import java.util.*;

public class GeneralizationTree {

    private static TreeNode<String> root;

    public static TreeNode<String> init() {
        if (root != null) {
            return root;
        }

        List<String[]> list = null;

        try {
            list = readFromTxt("./bs-common/ageTree.txt");
        } catch (Exception e) {
            list = new ArrayList<>();
            String line = "age|0|0-160";
            list.add(line.split("\\|"));
            line = "age|1|0-80,80-160";
            list.add(line.split("\\|"));
            line = "age|2|0-40,40-80,80-120,120-160";
            list.add(line.split("\\|"));
            line = "age|3|0-20,20-40,40-60,60-80,80-100,100-120,120-140,140-160";
            list.add(line.split("\\|"));
            line = "age|4|0-10,10-20,20-30,30-40,40-50,50-60,60-70,70-80,80-90,90-100,100-110,110-120,120-130,130-140,140-150,150-160";
            list.add(line.split("\\|"));
            line = "age|5|0-5,5-10,10-15,15-20,20-25,25-30,30-35,35-40,40-45,45-50,50-55,55-60,60-65,65-70,70-75,75-80,80-85,85-90,90-95,95-100,100-105,105-110,110-115,115-120,120-125,125-130,130-135,135-140,140-145,145-150,150-155,155-160";
            list.add(line.split("\\|"));
        }

        HashMap<String, Integer> map = new HashMap<>();
        StringBuilder tree = new StringBuilder();
        for (String[] line : list) {
            String[] ss = line[2].split(",");
            for (String s : ss) {
                map.put(s, Integer.parseInt(line[1]));
            }

            if (!line[2].endsWith(",")) {
                tree.append(line[2]);
                tree.append(",");
            } else {
                tree.append(line[2]);
            }
        }

        return createTree(tree.toString(), map);
    }

    private static TreeNode<String> createTree(String tree, HashMap<String, Integer> map) {
        String[] nodes = tree.split(",");
        String data = nodes[0];
        root = new TreeNode<>(fixData(data), map.get(data));
        Queue<TreeNode<String>> queue = new LinkedList<>();
        queue.add(root);
        int len = nodes.length;

        int index = 1;
        while (!queue.isEmpty()) {
            TreeNode<String> node = queue.remove();
            if (index == len) {
                break;
            }

            data = nodes[index++].trim();

            if (!data.equals("")) {
                node.setLeft(new TreeNode<>(fixData(data), map.get(data)));
                queue.add(node.getLeft());
            }

            if (index == len) {
                break;
            }

            data = nodes[index++].trim();
            if (!data.equals("")) {
                node.setRight(new TreeNode<>(fixData(data), map.get(data)));
                queue.add(node.getRight());
            }
        }
        return root;
    }


    private static List<String[]> readFromTxt(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        List<String[]> list = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            list.add(line.split("\\|"));
        }

        return list;
    }

    public static void foreach(TreeNode<String> root) {
        Queue<TreeNode<String>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode<String> node = queue.remove();
            System.out.println(node);
            if (node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node.getRight() != null) {
                queue.add(node.getRight());
            }
        }
    }

    public static TreeNode<String> getNodeByData(TreeNode<String> root, String data) {
        Queue<TreeNode<String>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode<String> node = queue.remove();

            if (node.getData().equals(fixData(data))) {
                return node;
            }

            if (node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node.getRight() != null) {
                queue.add(node.getRight());
            }
        }

        return null;
    }

    private static String fixData(String data) {
        if (data.startsWith("[") && data.endsWith(")")) {
            return data;
        }

        if (data.startsWith("[") && !data.endsWith(")")) {
            return data + ")";
        }

        if (!data.startsWith("[") && data.endsWith(")")) {
            return "[" + data;
        }

        return "[" + data + ")";
    }

    public static String generate(TreeNode<String> root, int age, int range) {
        Queue<TreeNode<String>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode<String> node = queue.remove();

            String data = node.getData();
            data = data.substring(1, data.length() - 1);
            String[] ss = data.split("-");
            int down = Integer.parseInt(ss[0]);
            int up = Integer.parseInt(ss[1]);

            int interval = up - down;
            if (interval == range && age >= down && age < up) {
                return node.getData();
            }

            if (node.getLeft() != null) {
                queue.add(node.getLeft());
            }

            if (node.getRight() != null) {
                queue.add(node.getRight());
            }
        }

        if (age >= 160) {
            return "[160以上]";
        }

        return "-1";
    }

    public static void main(String[] args) throws IOException {
//        File ageTree = new File("./bs-common/ageTree.txt");
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ageTree)));
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] s = line.split("\\|");
//            System.out.println(Arrays.toString(s));
//        }
        TreeNode<String> root = init();
        System.out.println(root);
        String str = generate(root, 5, 20);
        System.out.println(str);
//        foreach(root);
        NodeDistance x = new NodeDistance();
        int dis = x.getTwoTreeNodeDistence(root, getNodeByData(root, "10-15"), getNodeByData(root, "10-15"));
        System.out.println(dis);
    }
}
