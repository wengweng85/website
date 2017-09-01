package com.insigma.mvc.controller.common.suggest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.model.SuggestKey;
import com.insigma.resolver.AppException;

/**
 * 建议搜索controller
 */
@Controller
@RequestMapping(value = "/common/suggest")
public class SuggestSearchController extends MvcHelper<SuggestKey> {

	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	

    public String getAPI_BASE_URL() {
		return API_BASE_URL;
	}

	public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	}

	Log log = LogFactory.getLog(SuggestSearchController.class);
    
    /* 
	 * 通过搜索关键字
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/searchcode")
	@ResponseBody
	public String searchcodebykey(HttpServletRequest request, HttpServletResponse response,SuggestKey key) throws AppException {
		JSONObject jsonParam=JSONObject.fromObject(key);
		String url=API_BASE_URL+"/common/suggest/searchcode";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}

}
