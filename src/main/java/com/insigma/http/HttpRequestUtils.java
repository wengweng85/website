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
    private static Log log = LogFactory.getLog(HttpRequestUtils.class);    //��־��¼
 
 
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
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //���������������
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
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
  
}
