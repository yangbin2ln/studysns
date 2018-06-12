package com.eeesns.tshow.dao;

import java.io.Serializable;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * 分页Bean
 * @author yb
 * 
 */
public class Page implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected int pageNo;
	protected int pageSize;
	protected boolean autoCount;
	protected List result;
	protected long totalCount;

	public Page() {
		pageNo = 1;
		pageSize = 10;
		autoCount = true;
		result = Lists.newArrayList();
		totalCount = -1L;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1)
			this.pageNo = 1;
	}

	public Page pageNo(int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page pageSize(int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	public int getFirst() {
		return (pageNo - 1) * pageSize + 1;
	}

	public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public Page autoCount(boolean theAutoCount) {
		setAutoCount(theAutoCount);
		return this;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalPages() {
		if (totalCount < 0L)
			return -1L;
		long count = totalCount / (long) pageSize;
		if (totalCount % (long) pageSize > 0L)
			count++;
		return count;
	}

	public boolean isHasNext() {
		return (long) (pageNo + 1) <= getTotalPages();
	}

	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	public boolean isHasPre() {
		return pageNo - 1 >= 1;
	}

	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}

}
