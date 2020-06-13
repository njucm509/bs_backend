package cn.edu.njucm.wp.bs.release.anonymity.util.tree;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TreeNode<T> {
    private T data;
    private TreeNode<T> left;
    private TreeNode<T> right;
    private int level;

    public TreeNode(T data) {
        this.data = data;
    }

    public TreeNode(T data, int level) {
        this.data = data;
        this.level = level;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "data='" + data + '\'' +
                ", level=" + level +
                '}';
    }
}
