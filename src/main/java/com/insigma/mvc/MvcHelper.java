package com.insigma.mvc;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.insigma.dto.AjaxReturnMsg;
import com.insigma.json.JsonDateValueProcessor;


/**
 * mvc帮助类，主要用于包装controller以及serviceimp层返回的数据
 * 将其包装成类返回成json格式
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
     * 返回
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
     * 错误返回
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
