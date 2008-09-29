package org.codelutin.tapestry.beans;

import java.util.List;

public class TreeNode {

    public static final int TYPE_DOCUMENT = 0;
    public static final int TYPE_FOLDER = 1;

    private List<TreeNode> children;
    private String[] columns;
    private int type;
    private Object object;

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
