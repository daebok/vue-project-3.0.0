package com.rd.ifaes.common.util;

/**
 * 
 * ZTree节点类
 * @version 3.0
 * @author xxb
 * @date 2016年10月29日
 */
public class ZTreeNode {

	private String id;
	
	private String pId;
	
	private String name;
	
	private boolean open;
	
	private boolean checked;
	
	private String type;
	
	/**
	 * 虚拟根节点id
	 */
	public static final String TREE_ROOT_ID = "1";
	/**
	 *  虚拟根节点pid
	 */
	public static final String TREE_ROOT_PID = "0";
	/**
	 * 虚拟根节点
	 */
	public static final String TYPE_V = "1";
	/**
	 * 类型：菜单
	 */
	public static final String TYPE_M = "2";
	/**
	 * 类型：权限（按钮）
	 */
	public static final String TYPE_P = "3";
	

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

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
