package org.codelutin.tapestry.beans;

public class TreeNode {

    public static final int TYPE_DOCUMENT = 0;
    public static final int TYPE_FOLDER = 1;

    private TreeNode[] children;
    private String[] columns;
    private int type;

    public TreeNode[] getChildren() {
        return children;
    }

    public void setChildren(TreeNode[] children) {
        this.children = children;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
