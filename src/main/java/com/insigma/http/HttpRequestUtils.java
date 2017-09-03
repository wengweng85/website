package com.insigma.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.insigma.json.JsonDateValueProcessor;
import com.insigma.resolver.AppException;

public class HttpRequestUtils<T> {
	
	
	private  Log log = LogFactory.getLog(HttpRequestUtils.class);    //日志记录
	 
	public   JsonConfig jsonConfig;
	private  String appkey="faaaac26-8f96-11e7-bb31-be2e44b06b34";
	
	public HttpRequestUtils(){
		jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
	}
    
    /**
     * 发送get请求 返回json对象
     * @param url    路径
     * @return
     */
    public  JSONObject httpPostReturnObject(String url,T t) throws AppException{
        return httpPost(url,t).getJSONObject("obj");
    }
    
    /**
     * 发送get请求 返回json数组
     * @param url    路径
     * @return
     */
    public  JSONArray httpPostReturnArray(String url,T t) throws AppException{
    	return httpPost(url,t).getJSONArray("obj");
    }
    
    /**
     * 返回对象list
     * @param url
     * @param jsonParam
     * @param beanclass
     * @return
     * @throws AppException
     */
    public  List<T> httpPostReturnList(String url,T t) throws AppException{
    	return toList(httpPostReturnArray(url,t),t);
    }
    
 
    /**
     * 发送get请求 返回json对象
     * @param url    路径
     * @return
     */
    public  JSONObject httpGetReturnObject(String url) throws AppException{
        return httpGet(url).getJSONObject("obj");
    }
    
    /**
     * 发送get请求 返回json数组
     * @param url    路径
     * @return
     */
    public JSONArray httpGetReturnArray(String url) throws AppException{
    	return httpGet(url).getJSONArray("obj");
    }
    
    /**
     * 返回对象list
     * @param url
     * @param beanclass
     * @return
     * @throws AppException
     */
    public  List<T> httpGetReturnList(String url,Class c) throws AppException{
    	return toList(httpGetReturnArray(url),c);
    }

    
    
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public JSONObject httpPost(String url,T t) throws AppException{
    	   return httpPost(url,t,ContentType.JSON);
    }
    
	/**
	 *  post请求
	 * @param url 地址
	 * @param t 对象
	 * @param contentType 请求类型 json或者x-www-form
	 * @return
	 * @throws AppException
	 */
    public JSONObject httpPost(String url,T t,ContentType contentType) throws AppException{
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	CloseableHttpResponse response =null;
        JSONObject jsonResult = null;
        String str = "";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("appkey", appkey);
        try {
            if (null != t) {
            	if(contentType.equals(ContentType.X_WWW_FORM_URLENCODED)){
            		//解决中文乱码问题
                    StringEntity entity = new StringEntity(parseURLPair(t), "utf-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/x-www-form-urlencoded");
                    httppost.setEntity(entity);
            	}else if(contentType.equals(ContentType.JSON)){
            		//解决中文乱码问题
                    StringEntity entity = new StringEntity(toJsonObject(t).toString(), "utf-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    httppost.setEntity(entity);
                   }
            }
            /**返回结果*/
            response = httpClient.execute(httppost);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == 200) {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(response.getEntity());
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                    /**是否成功*/
                    String success= jsonResult.getString("success");
                    if(success.equals("true")){
                    	 log.info("调用接口"+url+"成功");
                    }else{
                    	 log.info("调用接口"+url+"失败,错误原因:"+jsonResult.getString("message"));
                    	 throw new AppException("调用接口"+url+"失败,错误原因:"+jsonResult.getString("message"));
                    }
            }else{
            	 log.error("post请求提交失败:" +url+response );
            	 throw new AppException("post请求提交失败:" +url+response);
            }
        } catch (Exception e) {
            log.error("post请求提交发生异常:" + url, e);
            throw new AppException("post请求提交发生异常:" + url, e);
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResult;
    }
    
   /**
    * 发送get请求
    * @param url
    * @return
    * @throws AppException
    */
    public  JSONObject httpGet(String url) throws AppException{
    	CloseableHttpClient httpClient =null;
    	CloseableHttpResponse response =null;
    	JSONObject jsonResult = null;
        String strResult="";
        try {
        	 httpClient = HttpClients.createDefault();
        	 response =null;
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.setHeader("appkey", appkey);
            response = httpClient.execute(request);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                 strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                /**是否成功*/
                String success= jsonResult.getString("success");
                url = URLDecoder.decode(url, "UTF-8");
                if(success.equals("true")){
                	log.info("调用接口"+url+"成功");
                }else{
                	log.info("调用接口"+url+"失败,错误原因:"+jsonResult.getString("message"));
                	throw new AppException("调用接口"+url+"失败,错误原因:"+jsonResult.getString("message"));
                }
            } else {
                log.error("get请求提交失败:" + url+response.getStatusLine().getStatusCode());
                throw new AppException("get请求提交失败:" + url+response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
        	log.error("get请求提交失败:" + url, e);
        	throw new AppException("get请求提交失败:" + url, e);
        }finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResult;
    }
    
    /**
     * 对象转换成jsonobject
     * @param t
     * @return
     */
    public   JSONObject toJsonObject(T t){
		JSONObject jsonParam=JSONObject.fromObject(t,jsonConfig);
		return jsonParam;
	}
    
    /**
     * jsonobject转换成对象
     * @param t
     * @return
     */
    public  T tobean(JSONObject jsonobject,T t){
		return (T)JSONObject.toBean(jsonobject, t.getClass());
	}
    
    /**
     * jsonobject转换成对象数组
     * @param t
     * @return
     */
	public   List<T> toList(JSONArray jsonarray,T t){
    	return (List<T>)JSONArray.toList(jsonarray,t.getClass());
	}
	
	 /**
     * jsonobject转换成对象数组
     * @param t
     * @return
     */
	public   List<T> toList(JSONArray jsonarray,Class c){
    	return (List<T>)JSONArray.toList(jsonarray,c);
	}
    
    /**
     * 对象转换成url k=v方式
     * @param o
     * @return
     * @throws Exception
     */
    public  String parseURLPair(T t) throws Exception{  
    	    Class<? extends Object>c = t.getClass();
	        Field[] fields = c.getDeclaredFields();  
	        Map<String, Object> map = new TreeMap<String, Object>();  
	        for (Field field : fields) {  
	            field.setAccessible(true);  
	            String name = field.getName();  
	            Object value = field.get(t);  
	            if(value != null)  
	                map.put(name, value);  
	        }  
	        Set<Entry<String, Object>> set = map.entrySet();  
	        Iterator<Entry<String, Object>> it = set.iterator();  
	        StringBuffer sb = new StringBuffer("");  
	        while (it.hasNext()) {  
	             Entry<String, Object> e = it.next();  
	             sb.append(e.getKey()).append("=").append(e.getValue()).append("&");  
	        }  
	        System.out.println(sb.toString()+"length="+sb.length());
	        if(sb.length()>0){
	        	 System.out.println(sb.toString());
	        	 return sb.deleteCharAt(sb.length()-1).toString();  
	        }else{
	        	return "";
	        }
    } 
}
