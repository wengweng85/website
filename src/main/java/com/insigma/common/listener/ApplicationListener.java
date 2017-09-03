package com.insigma.common.listener;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.ehcache.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.insigma.common.util.DateUtil;
import com.insigma.common.util.EhCacheUtil;
import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.model.CodeType;
import com.insigma.mvc.model.CodeValue;
import com.insigma.mvc.model.DemoAc01;

/**
 * ��Ŀ��ʼ��
 * 
 * @author wengsh
 * 
 */
public class ApplicationListener implements   ServletContextListener  {
	Log log=LogFactory.getLog(ApplicationListener.class);

	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * ����ehcache
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//�Ƿ�ͬ����־ �����һ��ͬ��ʱ����1Сʱ֮�ڣ���ͬ�����ش���
		boolean syn_flag=true;
		Element element=EhCacheUtil.getManager().getCache("webcache").get("code_value_last_update_time");
		if(element!=null){
			Date code_value_last_update_time=(Date)element.getValue();
			if(code_value_last_update_time!=null){
				if(!DateUtil.compare(new Date(), code_value_last_update_time, 3600*1000)){
					syn_flag=false;
				}
			}
		}
		if(syn_flag){
			String url=API_BASE_URL+"/codetype/getInitcodetypeList";
			try{
				    List <CodeType> list_code_type=new HttpRequestUtils<CodeType>().httpGetReturnList(url,CodeType.class);
					//code_type code_valueͬ��
					for(CodeType codetype : list_code_type){
						String code_type=codetype.getCode_type();
						url=API_BASE_URL+"/codetype/getInitCodeValueList/"+code_type;
						List<CodeValue> list_code_value=new HttpRequestUtils<CodeValue>().httpGetReturnList(url,CodeValue.class);
						if (list_code_value.size() > 0) {
							//������μӼ��ص�redis������
								//������μӼ��ص�ehcache������
								EhCacheUtil.getManager().getCache("webcache").put(new Element(code_type,list_code_value));
						}
					}
			}catch(Exception e){
				e.printStackTrace();
			}
			//��һ�θ���ʱ��
			EhCacheUtil.getManager().getCache("webcache").put(new Element("code_value_last_update_time",new Date()));
			
		}
	}

}
