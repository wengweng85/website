package com.insigma.shiro.filter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.insigma.dto.AjaxReturnMsg;


/**
 * �Զ���authcУ����
 * @author wengsh
 *
 */
public class SimpleFormAuthenticationFilter extends FormAuthenticationFilter {

	protected boolean onAccessDenied(ServletRequest servletrequest, ServletResponse servletresponse) throws Exception {  
		  
        HttpServletRequest request = (HttpServletRequest) servletrequest;  
        HttpServletResponse response = (HttpServletResponse) servletresponse;  
  
        Subject subject = getSubject(request, response);  
  
        //���û�е�¼
        if (subject.getPrincipal() == null) {  
        	//�����ajax����
        	if (isAjax(request)){
        		/*PrintWriter writer = response.getWriter();
                AjaxReturnMsg dto = new AjaxReturnMsg();
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("statuscode", "session_expired");//����δ��¼���¼ʱ�����,�����µ�¼!
                map.put("redirecturl", "/gotologin");;
                dto.setObj(map);
                writer.write(JSONObject.fromObject(dto).toString());
                writer.flush();*/
        		
        		//δ��¼��ǿ����ת����¼ҳ��
                response.setHeader("statuscode", "session_expired");
                response.setHeader("redirecturl", "/gotologin");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {  
                saveRequestAndRedirectToLogin(request, response);  
            } 
        }else{
        	//�����ajax����
        	if (isAjax(request)){
        		/*PrintWriter writer = response.getWriter();
                AjaxReturnMsg dto = new AjaxReturnMsg();
                Map<String, Object> map=new HashMap<String, Object>();
                map.put("statuscode", "unauthorized");//��û���㹻��Ȩ��ִ�иò���
                map.put("redirecturl", "/index");;
                dto.setObj(map);
                writer.write(JSONObject.fromObject(dto).toString());
                writer.flush();*/
                
                response.setHeader("statuscode", "unauthorized");
                response.setHeader("redirecturl", "/index");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else {  
                saveRequestAndRedirectToLogin(request, response);  
            } 
        }
        return false;
    } 
	
	/**
	 * �Ƿ���ajax����
	 * @param request
	 * @return
	 */
	public boolean isAjax( HttpServletRequest request){
	      return request.getHeader("accept").indexOf("application/json") > -1 || (request.getHeader("X-Requested-With")!= null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1); 
	}
}
