package com.insigma.mvc.model;

public class PageInfo implements java.io.Serializable {

	/**
	 * ����ͨ������
	 */
	private static final long serialVersionUID = 1L;
	//private Integer curpage =1;
	private Integer limit=10;
	private Integer offset=0;
	private String appkey;
	
	
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
}
