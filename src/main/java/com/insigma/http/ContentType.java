package com.insigma.http;


/**
 * http«Î«ÛContent-Type
 * @author wengsh
 *
 */
public enum ContentType {
	
	JSON("application/json"),
	X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");
	
	private String type;
	
	private ContentType(String type){
		this.type=type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
   
	
	

}
