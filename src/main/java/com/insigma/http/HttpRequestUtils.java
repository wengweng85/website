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
	 
    private static Log log = LogFactory.getLog(HttpRequestUtils.class);    //��־��¼
 
    private static String appkey="faaaac26-8f96-11e7-bb31-be2e44b06b34";
    
    /**
     * ����get���� ����json����
     * @param url    ·��
     * @return
     */
    public static JSONObject httpPostReturnObject(String url,JSONObject jsonParam) throws AppException{
        return httpPost(url,jsonParam).getJSONObject("obj");
    }
    
    /**
     * ����get���� ����json����
     * @param url    ·��
     * @return
     */
    public static JSONArray httpPostReturnArray(String url,JSONObject jsonParam) throws AppException{
    	return httpPost(url,jsonParam).getJSONArray("obj");
    }
    
    /**
     * ���ض���list
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
     * ����get���� ����json����
     * @param url    ·��
     * @return
     */
    public static JSONObject httpGetReturnObject(String url) throws AppException{
        return httpGet(url).getJSONObject("obj");
    }
    
    /**
     * ����get���� ����json����
     * @param url    ·��
     * @return
     */
    public static JSONArray httpGetReturnArray(String url) throws AppException{
    	return httpGet(url).getJSONArray("obj");
    }
    
    /**
     * ���ض���list
     * @param url
     * @param beanclass
     * @return
     * @throws AppException
     */
    public static Object httpGetReturnList(String url,Class beanclass) throws AppException{
    	return toList(httpGetReturnArray(url),beanclass);
    }
  
    
    /**
     * post����
     * @param url         url��ַ
     * @param jsonParam     ����
     * @param noNeedResponse    ����Ҫ���ؽ��
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam) throws AppException{
    	//post���󷵻ؽ��
    	DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        String str = "";
        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("appkey", appkey);
        try {
            if (null != jsonParam) {
                //���������������
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httppost.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(httppost);
            url = URLDecoder.decode(url, "UTF-8");
            /**�����ͳɹ������õ���Ӧ**/
            if (result.getStatusLine().getStatusCode() == 200) {
                try {
                    /**��ȡ���������ع�����json�ַ�������**/
                    str = EntityUtils.toString(result.getEntity());
                    /**��json�ַ���ת����json����**/
                    jsonResult = JSONObject.fromObject(str);
                    /**�Ƿ�ɹ�*/
                    String success= jsonResult.getString("success");
                    if(success.equals("true")){
                    	log.info("���ýӿ�"+url+"�ɹ�");
                    }else{
                    	log.info("���ýӿ�"+url+"ʧ��,����ԭ��:"+jsonResult.getString("message"));
                    	throw new AppException("���ýӿ�"+url+"ʧ��,����ԭ��:"+jsonResult.getString("message"));
                    }
                } catch (Exception e) {
                    log.error("post�����ύʧ��:" + url, e);
                    throw new AppException("get�����ύʧ��:" + url, e);
                }
            }else{
            	 log.error("post�����ύʧ��:" +url+result );
            	 throw new AppException("post�����ύʧ��:" +url+result);
            }
        } catch (IOException e) {
            log.error("post�����ύʧ��:" + url, e);
            throw new AppException("get�����ύʧ��:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * ����get����
     * @param url    ·��
     * @return
     */
    public static JSONObject httpGet(String url) throws AppException{
        //get���󷵻ؽ��
        JSONObject jsonResult = null;
        String strResult="";
        try {
        	DefaultHttpClient client = new DefaultHttpClient();
            //����get����
            HttpGet request = new HttpGet(url);
            
            request.setHeader("appkey", appkey);
            //����httpGet��ͷ��������Ϣ
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
 
            /**�����ͳɹ������õ���Ӧ**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**��ȡ���������ع�����json�ַ�������**/
                 strResult = EntityUtils.toString(response.getEntity());
                /**��json�ַ���ת����json����**/
                jsonResult = JSONObject.fromObject(strResult);
                /**�Ƿ�ɹ�*/
                String success= jsonResult.getString("success");
                url = URLDecoder.decode(url, "UTF-8");
                if(success.equals("true")){
                	log.info("���ýӿ�"+url+"�ɹ�");
                }else{
                	log.info("���ýӿ�"+url+"ʧ��,����ԭ��:"+jsonResult.getString("message"));
                	throw new AppException("���ýӿ�"+url+"ʧ��,����ԭ��:"+jsonResult.getString("message"));
                }
            } else {
                log.error("get�����ύʧ��:" + url+response.getStatusLine().getStatusCode());
                throw new AppException("get�����ύʧ��:" + url+response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
        	log.error("get�����ύʧ��:" + url, e);
        	throw new AppException("get�����ύʧ��:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * ����ת����jsonobject
     * @param t
     * @return
     */
    public  static JSONObject toJsonObject(Object t){
		JSONObject jsonParam=JSONObject.fromObject(t,jsonConfig);
		return jsonParam;
	}
    
    /**
     * jsonobjectת���ɶ���
     * @param t
     * @return
     */
    public  static Object tobean(JSONObject jsonobject,Class beanclass){
		return JSONObject.toBean(jsonobject, beanclass);
	}
    
    /**
     * jsonobjectת���ɶ�������
     * @param t
     * @return
     */
    @SuppressWarnings("deprecation")
	public  static  Object toList(JSONArray jsonarray,Class beanclass){
    	return JSONArray.toList(jsonarray, beanclass);
	}
    
    /**
     * ����ת����url k=v��ʽ
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
