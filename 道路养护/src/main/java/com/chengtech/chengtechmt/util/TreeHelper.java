package com.chengtech.chengtechmt.util;

import com.chengtech.chengtechmt.R;
import com.chengtech.chengtechmt.util.annotation.TreeNodeId;
import com.chengtech.chengtechmt.util.annotation.TreeNodeName;
import com.chengtech.chengtechmt.util.annotation.TreeNodePid;
import com.chengtech.chengtechmt.util.annotation.TreeNodeSectionId;
import com.chengtech.chengtechmt.util.annotation.TreeNodeType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class TreeHelper {


	/**
	 * 将用户数据转换成树形数据
	 * @author liufuyingwang
	 * 2015-5-19 下午2:22:01
	 * @param datas
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T>List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException, IllegalArgumentException {

		List<Node> nodes = new ArrayList<Node>();
		Node node = null ;
		for(T t:datas) {
			String id = null;
			String pId = null;
			String name=null;
			String type=null;
			String sectionId=null;
			Class clazz = t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for(Field field:fields) {
				if (field.getAnnotation(TreeNodeId.class)!=null) {
					field.setAccessible(true);
					id = (String) field.get(t);
				}
				if (field.getAnnotation(TreeNodePid.class)!=null) {
					field.setAccessible(true);
					pId = (String) field.get(t);
				}
				if (field.getAnnotation(TreeNodeName.class)!=null) {
					field.setAccessible(true);
					name = (String) field.get(t);
				}
				if (field.getAnnotation(TreeNodeType.class)!=null) {
					field.setAccessible(true);
					type = (String) field.get(t);
				}
				if (field.getAnnotation(TreeNodeType.class)!=null) {
					field.setAccessible(true);
					type = (String) field.get(t);
				}
				if (field.getAnnotation(TreeNodeSectionId.class)!=null) {
					field.setAccessible(true);
					sectionId = (String) field.get(t);
				}
			}
			node = new Node(id,pId,name,type,sectionId);
			nodes.add(node);
		}

		/**
		 * 设置节点间的关系
		 */

		for (int i=0;i<nodes.size();i++) {
			Node n = nodes.get(i);
			for (int j=i+1;j<nodes.size();j++) {
				Node m = nodes.get(j);
				if (m.getpId()!=null && m.getpId().equals(n.getId())) {
					n.getChildren().add(m);
					m.setParent(n);
				}else if(m.getId().equals(n.getpId())) {
					m.getChildren().add(n);
					n.setParent(m);
				}

			}
		}
		for (Node n:nodes) {
			setNodeIcon(n);
		}
		return nodes;
	}

	/**
	 * 为节点之间进行排序
	 * @author liufuyingwang
	 * 2015-5-19 下午2:51:40
	 * @param datas
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static<T> List<Node> getSortedNodes(List<T> datas,int defaultExpandLevel) throws IllegalAccessException, IllegalArgumentException {
		List<Node> result = new ArrayList<Node>();
		List<Node> nodes = convertDatas2Nodes(datas);
		List<Node> roots = getRootNode (nodes);
		for (Node node :roots) {
			addNode(result,node,defaultExpandLevel,1);
		}
		return result;
	}

	/**
	 * 把一个节点的所有孩子节点都放入result
	 * @author liufuyingwang
	 * 2015-5-19 下午2:58:44
	 * @param result
	 * @param node
	 * @param defaultExpandLevel
	 * @param
	 */
	private static void addNode(List<Node> result, Node node,
								int defaultExpandLevel, int currentLevel) {
		result.add(node);
		if (defaultExpandLevel>=currentLevel) {
			node.setExpand(true);
		}
		if (node.isLeaf()) {
			return ;
		}

		for (int i=0;i<node.getChildren().size();i++) {
			addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel+1);
		}
	}

	public static List<Node> filterVisibleNodes(List<Node> nodes) {
		List<Node> result = new ArrayList<Node> ();
		for (Node node : nodes) {
			if (node.isRoot() || node.isParentExpand()) {
				setNodeIcon(node);
				result.add(node);
			}
		}
		return result;
	}

	/**
	 * 从全部节点中过滤出根节点
	 * @author liufuyingwang
	 * 2015-5-19 下午2:53:27
	 * @param nodes
	 */
	private static List<Node> getRootNode(List<Node> nodes) {
		List<Node> root = new ArrayList<Node>();
		for (Node n:nodes) {
			if (n.isRoot()) {
				root.add(n);
			}
		}
		return root;

	}

	/**
	 * 为节点设置图标
	 * @author liufuyingwang
	 * 2015-5-19 下午2:49:01
	 * @param n
	 */
	private static void setNodeIcon(Node n) {
		if (n.getChildren().size()>0 && n.isExpand()) {
			n.setIcon(R.drawable.tree_ex);
		}else if (n.getChildren().size()>0 && !n.isExpand()) {
			n.setIcon(R.drawable.tree_ec);
		}else {
			n.setIcon(-1);
		}
	}
}
