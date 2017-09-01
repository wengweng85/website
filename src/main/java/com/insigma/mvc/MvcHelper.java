package com.insigma.mvc;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.insigma.dto.AjaxReturnMsg;
import com.insigma.json.JsonDateValueProcessor;


/**
 * mvc�����࣬��Ҫ���ڰ�װcontroller�Լ�serviceimp�㷵�ص�����
 * �����װ���෵�س�json��ʽ
 * @author wengsh
 *
 */
public class MvcHelper<T> {
	
	
	
	public String validate(BindingResult result){
		 FieldError fielderror=result.getFieldErrors().get(result.getErrorCount()-1);
		 AjaxReturnMsg<String> dto = new AjaxReturnMsg<String>();
	     dto.setSuccess(false);
	     dto.setMessage(fielderror.getDefaultMessage());
	     dto.setObj(fielderror.getField());
	     return JSONObject.fromObject(dto).toString();
	}
	
	
	 /**
     * ����
     * @param message
     * @return
     */
    public String success(String message) {
        AjaxReturnMsg<String> dto = new AjaxReturnMsg<String>();
        dto.setSuccess(true);
        dto.setMessage(message);
        return JSONObject.fromObject(dto).toString();
    }
    
	 /**
     * ���󷵻�
     * @param message
     * @return
     */
    public String error(String message) {
        AjaxReturnMsg<String> dto = new AjaxReturnMsg<String>();
        dto.setSuccess(false);
        dto.setMessage(message);
        return JSONObject.fromObject(dto).toString();
    }

    
}
