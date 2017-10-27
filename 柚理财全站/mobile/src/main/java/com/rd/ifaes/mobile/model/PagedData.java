/**
 * 分页数据
 */
package com.rd.ifaes.mobile.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yoseflin
 * 分页数据
 */
public class PagedData<T> {

	/**
	 * 分页大小
	 */
	private int pageSize;
	
	/**
	 * 当前页
	 */
	private int page;
	
	/**
	 * 总条数
	 */
	private int dataCount;
	
	/**
	 * 总页数
	 */
	private int totalPage;
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 本页数据
	 */
	private List<T> list;

	//--------------------------华丽分割线----------------------------------
	public PagedData() {
		
		pageSize = 0;
		page = 0;
		list = new LinkedList<T>();
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	
}
