package com.insigma.json;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * json解析工具类
 * @author wengsh
 *
 * @param <T>
 */
public class JsonParseUtil<T> {
	
	
    public  static JsonConfig jsonConfig;
	
	public JsonParseUtil(){
		jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
	}
	
	public  JSONObject toJsonObject(T t){
		JSONObject jsonParam=JSONObject.fromObject(t,jsonConfig);
		return jsonParam;
	}

}
