package com.insigma.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.insigma.json.JsonDateValueProcessor;
import com.insigma.resolver.AppException;

public class HttpRequestUtils<T> {
	
	public  static JsonConfig jsonConfig;
	
	static{
		jsonConfig=new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
	}
	 
    private static Log log = LogFactory.getLog(HttpRequestUtils.class);    //日志记录
 
    private static String appkey="faaaac26-8f96-11e7-bb31-be2e44b06b34";
    
    /**
     * 发送get请求 返回json对象
     * @param url    路径
     * @return
     */
    public static JSONObject httpPostReturnObject(String url,JSONObject jsonParam) throws AppException{
        return httpPost(url,jsonParam).getJSONObject("obj");
    }
    
    /**
     * 发送get请求 返回json数组
     * @param url    路径
     * @return
     */
    public static JSONArray httpPostReturnArray(String url,JSONObject jsonParam) throws AppException{
    	return httpPost(url,jsonParam).getJSONArray("obj");
    }
    
    /**
     * 返回对象list
     * @param url
     * @param jsonParam
     * @param beanclass
     * @return
     * @throws AppException
     */
    public static Object httpPostReturnList(String url,JSONObject jsonParam,Class beanclass) throws AppException{
    	return toList(httpPostReturnArray(url,jsonParam),beanclass);
    }
    
 
    /**
     * 发送get请求 返回json对象
     * @param url    路径
     * @return
     */
    public static JSONObject httpGetReturnObject(String url) throws AppException{
        return httpGet(url).getJSONObject("obj");
    }
    
    /**
     * 发送get请求 返回json数组
     * @param url    路径
     * @return
     */
    public static JSONArray httpGetReturnArray(String url) throws AppException{
    	return httpGet(url).getJSONArray("obj");
    }
    
    /**
     * 返回对象list
     * @param url
     * @param beanclass
     * @return
     * @throws AppException
     */
    public static Object httpGetReturnList(String url,Class beanclass) throws AppException{
    	return toList(httpGetReturnArray(url),beanclass);
    }
  
    
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam) throws AppException{
    	//post请求返回结果
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        String str = "";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("appkey", appkey);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httppost.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(httppost);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
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
                } catch (Exception e) {
                    log.error("post请求提交失败:" + url, e);
                    throw new AppException("get请求提交失败:" + url, e);
                }
            }else{
            	 log.error("post请求提交失败:" +url+result );
            	 throw new AppException("post请求提交失败:" +url+result);
            }
        } catch (IOException e) {
            log.error("post请求提交失败:" + url, e);
            throw new AppException("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url) throws AppException{
        //get请求返回结果
        JSONObject jsonResult = null;
        String strResult="";
        try {
        	DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            
            request.setHeader("appkey", appkey);
            //设置httpGet的头部参数信息
            //request.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");    
            /*request.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");    
            request.setHeader("Accept-Encoding", "gzip, deflate");    
            request.setHeader("Accept-Language", "zh-cn,zh;q=0.5");    
            request.setHeader("Connection", "keep-alive");    
            request.setHeader("Cookie", "__utma=226521935.73826752.1323672782.1325068020.1328770420.6;");    
            request.setHeader("Host", "www.cnblogs.com");    
            request.setHeader("refer", "http://www.baidu.com/s?tn=monline_5_dg&bs=httpclient4+MultiThreadedHttpConnectionManager");    
            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");    */
            HttpResponse response = client.execute(request);
 
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
        }
        return jsonResult;
    }
    
    /**
     * 对象转换成jsonobject
     * @param t
     * @return
     */
    public  static JSONObject toJsonObject(Object t){
		JSONObject jsonParam=JSONObject.fromObject(t,jsonConfig);
		return jsonParam;
	}
    
    /**
     * jsonobject转换成对象
     * @param t
     * @return
     */
    public  static Object tobean(JSONObject jsonobject,Class beanclass){
		return JSONObject.toBean(jsonobject, beanclass);
	}
    
    /**
     * jsonobject转换成对象数组
     * @param t
     * @return
     */
    @SuppressWarnings("deprecation")
	public  static  Object toList(JSONArray jsonarray,Class beanclass){
    	return JSONArray.toList(jsonarray, beanclass);
	}
    
    /**
     * 对象转换成url k=v方式
     * @param o
     * @return
     * @throws Exception
     */
    public static String parseURLPair(Object o) throws Exception{  
	        Class<? extends Object> c = o.getClass();  
	        Field[] fields = c.getDeclaredFields();  
	        Map<String, Object> map = new TreeMap<String, Object>();  
	        for (Field field : fields) {  
	            field.setAccessible(true);  
	            String name = field.getName();  
	            Object value = field.get(o);  
	            if(value != null)  
	                map.put(name, value);  
	        }  
	        Set<Entry<String, Object>> set = map.entrySet();  
	        Iterator<Entry<String, Object>> it = set.iterator();  
	        StringBuffer sb = new StringBuffer();  
	        while (it.hasNext()) {  
	             Entry<String, Object> e = it.next();  
	             sb.append(e.getKey()).append("=").append(e.getValue()).append("&");  
	        }  
	        return sb.deleteCharAt(sb.length()-1).toString();  
    } 
}
