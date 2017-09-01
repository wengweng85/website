package com.insigma.http;

import java.io.IOException;
import java.net.URLDecoder;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.insigma.resolver.AppException;

public class HttpRequestUtils {
    private static Log log = LogFactory.getLog(HttpRequestUtils.class);    //日志记录
 
 
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
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
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
  
}
