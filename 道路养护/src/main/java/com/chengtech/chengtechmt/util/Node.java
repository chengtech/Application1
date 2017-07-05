package com.chengtech.chengtechmt.util;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String id;
    private String pId;
    private String name;
    private int level;
    private boolean isExpand;
    private int icon;
    private Node parent;
    private String type;
    private String sectionId;


    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private List<Node> children = new ArrayList<Node>();

    public Node() {
        super();
    }

    public Node(String id, String pId, String name, String type, String sectionId) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.type = type;
        this.sectionId = sectionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 得到当前节点的层级
     *
     * @author liufuyingwang
     * 2015-5-19 下午2:17:43
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {
            for (Node node : children) {
                node.setExpand(false);
            }
        }

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * 判断当前是否根节点
     */
    public boolean isRoot() {
        return parent == null;
    }


    /**
     * 判断当前父节点的收缩状态
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 判断当前是否叶子节点
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    @Override
    public String toString() {
        return "Node [id=" + id + ", pId=" + pId + ", name=" + name
                + ", level=" + level + ", isExpand=" + isExpand + ", icon="
                + icon + ", parent=" + parent + ", children=" + children + "]";
    }


}
