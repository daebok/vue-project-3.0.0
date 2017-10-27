package com.rd.ifaes.common.util;

import java.util.List;
import com.google.common.collect.Lists;


/**
 * 
 *  组装父子对象
 * @version 3.0
 * @author xxb
 * @date 2016年8月20日
 */
public class TreeNode {
	
	private String isLeaf;

	private Node node;
	
	private List<Node> childNode;

	public TreeNode() {
		childNode = Lists.newArrayList();
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public List<Node> getChildNode() {
		return childNode;
	}

	public void setChildNode(List<Node> childNode) {
		this.childNode = childNode;
	}
	
	public void addChildNode(Node node) {
		 childNode.add(node);
	}

	public String getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	
}
