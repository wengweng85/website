package com.insigma.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

public class MyAccessControlFilter extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest arg0, ServletResponse arg1, Object arg2) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletrequest, ServletResponse servletresponse) throws Exception {
		 HttpServletRequest request = (HttpServletRequest) servletrequest;  
	     HttpServletResponse response = (HttpServletResponse) servletresponse;  
	     Subject subject = getSubject(request, response);  
	     //�û�ӵ��admin��ɫ��û��admin2��ɫ
	     if(subject.hasRole("admin2")&&!subject.hasRole("admin3")){
	    	 System.out.println("�û�ӵ��admin2��ɫ��û��admin3��ɫ");
	    	 //�ض��򵽶�Ӧҳ��
	    	 response.sendRedirect(request.getContextPath()+"/404");
	     }
		 return true;
	}

}
