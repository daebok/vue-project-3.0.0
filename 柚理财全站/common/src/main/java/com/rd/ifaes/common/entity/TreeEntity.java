package com.rd.ifaes.common.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 树- 基类
 * @author lh
 * @version 3.0
 */
public abstract class TreeEntity<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4026515233466583112L;

	/**
	 * 主键
	 */
	protected String id;
	
	/**
	 * 父级id
	 */
	protected String pid;
	
	/**
	 * 类别标识
	 */
	protected String flag;
	
	/**
	 * 编码
	 */
	protected String code;
	
	/**
	 * 选中状态
	 */
	protected TreeState state;
	
	/**
	 * 排序值
	 */
	protected int sort;
	
	/**
	 * 显示名称
	 */
	protected String text;
	
	/**
	 * 子节点
	 */
	protected transient List<T> nodes;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

	public TreeState getState() {
		return state;
	}

	public void setState(boolean checked) {
		this.state = new TreeState(checked);
	}
	
	public void setSelected(boolean selected) {
		this.state = new TreeState();
		this.state.setSelected(selected);
	}
	
	public void setExpanded(boolean expanded) {
		this.state = new TreeState();
		this.state.setExpanded(expanded);
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<T> getNodes() {
		return nodes;
	}

	public void setNodes(List<T> nodes) {
		this.nodes = nodes;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}


	/**
	 * 树节点状态
	 * @author 
	 *
	 */
	class TreeState implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4026515233466583110L;
		/**
		 * 展开状态
		 */
		private boolean expanded;
		/**
		 * 选中状态
		 */
		private boolean selected;
		/**
		 * 选中状态
		 */
		private boolean checked;
		

		public TreeState() {
			super();
		}

		public TreeState(boolean checked) {
			super();
			this.checked = checked;
		}
		
		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public boolean isExpanded() {
			return expanded;
		}

		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
		}
		
		
	}
	
	
	

}
