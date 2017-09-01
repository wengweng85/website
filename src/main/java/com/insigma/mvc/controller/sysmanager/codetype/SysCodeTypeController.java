package com.insigma.mvc.controller.sysmanager.codetype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.model.CodeType;
import com.insigma.mvc.model.CodeValue;
import com.insigma.resolver.AppException;

/**
 * Created by wengsh on 2015-01-14.
 */
@Controller
@RequestMapping("/codetype")
public class SysCodeTypeController extends MvcHelper<CodeValue> {

	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	 }

	 public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	 }
	
	private String URL="/codetype";
	
	
	/**
	 * ��ת����������ҳ��
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/toCodeValuesuggest")
	public ModelAndView toCodeValuesuggest(HttpServletRequest request, HttpServletResponse response,CodeValue codevalue) throws AppException {
		String callback_fun_name=request.getParameter("callback_fun_name");
		String codetype=request.getParameter("codetype");
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/codeValueSelect");
        modelAndView.addObject("callback_fun_name", callback_fun_name);
        modelAndView.addObject("codetype", codetype);
        return modelAndView;
	}
	
	/**
	 *codevalue ������
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/treedata/{code_type}")
	@ResponseBody
	public String treedata(HttpServletRequest request, HttpServletResponse response,@PathVariable String code_type) throws AppException {
		String id=request.getParameter("id");
		if(StringUtils.isEmpty(id)){
			id="000000";
		}
		CodeValue codevalue=new CodeValue();
		codevalue.setPar_code_value(id);
		codevalue.setCode_type(code_type.toUpperCase());
		
		JSONObject jsonParam=JSONObject.fromObject(codevalue);
		String url=API_BASE_URL+URL+"/codetype/treedata";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
		
	}
	
	/**
	 * ���ݴ������ͼ����븸������ȡ����ֵ
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/queryByCodeTypeAndParent")
	@ResponseBody
	public String queryByCodeTypeAndParent(HttpServletRequest request, HttpServletResponse response,CodeValue codevalue) throws AppException {
		JSONObject jsonParam=JSONObject.fromObject(codevalue);
		String url=API_BASE_URL+URL+"/queryByCodeTypeAndParent";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	/**
	 * �ӻ����л�ȡ����ֵ
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/getCodeValueFromCache")
	@ResponseBody
	public String getCodeValueFromCache(HttpServletRequest request, HttpServletResponse response,CodeValue codevalue) throws AppException {
		JSONObject jsonParam=JSONObject.fromObject(codevalue);
		String url=API_BASE_URL+URL+"/getCodeValueFromCache";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	
	/**
	 * ��ת���������ҳ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	@RequiresRoles("admin")
	public ModelAndView draglist(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeIndex");
        return modelAndView;
	}
	
	
	/**
	 *codetype ������
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/codetype_treedata")
	@ResponseBody
	@RequiresRoles("admin")
	public String codetype_treedata(HttpServletRequest request, HttpServletResponse response,CodeType codetype) throws AppException {
		JSONObject jsonParam=JSONObject.fromObject(codetype);
		String url=API_BASE_URL+URL+"/codetype_treedata";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	/**
	 *��ת������ֵ���༭ҳ��
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/toCodeValueTreePage/{id}")
	@ResponseBody
	@RequiresRoles("admin")
	public ModelAndView toCodeValueTreePage(HttpServletRequest request, HttpServletResponse response,@PathVariable String id) throws AppException {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeEdit");
		String url=API_BASE_URL+URL+"/toCodeValueTreePage/"+id;
		JSONObject jsonresult=HttpRequestUtils.httpGet(url);
		modelAndView.addObject("codetype", JSONObject.toBean(jsonresult, CodeType.class));
        return modelAndView;
	}
	
	/**
	 *codevalue ������
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws com.insigma.resolver.AppException
	 */
	@RequestMapping(value = "/codevalue_treedata")
	@ResponseBody
	@RequiresRoles("admin")
	public String codevalue_treedata(HttpServletRequest request, HttpServletResponse response,CodeType  codetype) throws AppException {
		JSONObject jsonParam=JSONObject.fromObject(codetype);
		String url=API_BASE_URL+URL+"/codevalue_treedata";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	
	/**
	 * ��ת���������������༭ҳ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCodeTypeEdit/{id}")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeEdit(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeInfoEdit");
		String url=API_BASE_URL+URL+"/toCodeTypeEdit/"+id;
		JSONObject jsonresult= HttpRequestUtils.httpGet(url);
		modelAndView.addObject("codetype", JSONObject.toBean(jsonresult, CodeType.class));
        return modelAndView;
	}
	
	
	/**
	 * ��ת��������������ҳ��
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toCodeTypeAdd")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeAdd(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeInfoAdd");
        return modelAndView;
	}
	
	
	/**
	 * ���»򱣴��������
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveOrUpdateCodeType")
	@ResponseBody
	@RequiresRoles("admin")
	public String saveOrUpdateCodeTypedata(HttpServletRequest request,Model model,@Valid CodeType codetype,BindingResult result) throws Exception {
		//��������
		if (result.hasErrors()){
			return validate(result);
		}
		JSONObject jsonParam=JSONObject.fromObject(codetype);
		String url=API_BASE_URL+URL+"/saveOrUpdateCodeType";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	/**
	 * ��ת������ֵ��ϸ�༭ҳ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/toCodeTypeDetailEdit/{id}")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeDetailEdit(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeDetailInfoEdit");
		String url=API_BASE_URL+URL+"/toCodeTypeDetailEdit";
		JSONObject jsonresult= HttpRequestUtils.httpGet(url);
		modelAndView.addObject("codevalue", JSONObject.toBean(jsonresult,CodeValue.class));
        return modelAndView;
	}
	
	
	/**
	 * ��ת������ֵ��ϸҳ��
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toCodeTypeDetailAddFromRoot/{code_type}")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeDetailAddFromRoot(HttpServletRequest request,Model model,@PathVariable String code_type) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeDetailInfoAdd");
		//ͨ���������ͻ�ȡ������ϸ
		String url=API_BASE_URL+URL+"/toCodeTypeDetailAddFromRoot/"+code_type;
		JSONObject jsonresult= HttpRequestUtils.httpGet(url);
		CodeType codetype=(CodeType)JSONObject.toBean(jsonresult, CodeType.class);
		CodeValue codevalue=new CodeValue();
	    //�ڸ�����������Ӵ���ֵ��ϸʱĬ�ϵĸ��ڵ����ֵΪ�������ͱ��е�code_root_value
		codevalue.setCode_type(code_type);
		codevalue.setPar_code_value(codetype.getCode_root_value());
		modelAndView.addObject("codevalue", codevalue);
        return modelAndView;
	}
	
	/**
	 * ��ת������ֵ��ϸҳ��
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toCodeTypeDetailAddFromNode/{par_code_seq}")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeDetailAddFromNode(HttpServletRequest request,Model model,@PathVariable String par_code_seq) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeDetailInfoAdd");
		//ͨ���������ͻ�ȡ������ϸ
		String url=API_BASE_URL+URL+"/toCodeTypeDetailAddFromNode/"+par_code_seq;
		JSONObject jsonresult= HttpRequestUtils.httpGet(url);
		CodeValue codevalue=(CodeValue)JSONObject.toBean(jsonresult, CodeValue.class);
		//���õ�ǰ�ڵ�ĸ������ϢΪѡ�еĽ�����Ϣ
		codevalue.setPar_code_value(codevalue.getCode_value());
		codevalue.setPar_code_name(codevalue.getCode_name());
		modelAndView.addObject("codevalue", codevalue);
        return modelAndView;
	}
	
	
	/**
	 * ���»򱣴��������
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveOrUpdateCodeTypeDetail")
	@ResponseBody
	@RequiresRoles("admin")
	public String saveOrUpdateCodeTypeDetail(HttpServletRequest request,Model model,@Valid CodeValue codevalue,BindingResult result) throws Exception {
		//��������
		if (result.hasErrors()){
			return validate(result);
		}
		JSONObject jsonParam=JSONObject.fromObject(codevalue);
		String url=API_BASE_URL+URL+"/saveOrUpdateCodeTypeDetail";
		return HttpRequestUtils.httpPost(url, jsonParam).toString();
	}
	
	
	/**
	 * ɾ����������
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteCodeType/{code_type}")
	@ResponseBody
	@RequiresRoles("admin")
	public String deleteCodeType(HttpServletRequest request,Model model,@PathVariable String code_type) throws Exception {
		String url=API_BASE_URL+URL+"/deleteCodeType/"+code_type;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	
	/**
	 * ɾ������ֵ
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteCodeValue/{code_seq}")
	@ResponseBody
	@RequiresRoles("admin")
	public String deleteCodeValue(HttpServletRequest request,Model model,@PathVariable String code_seq) throws Exception {
		String url=API_BASE_URL+URL+"/deleteCodeValue/"+code_seq;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	
	/**
	 * ��ת���������Ͳ�ѯҳ��
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toCodeTypeQuery")
	@RequiresRoles("admin")
	public ModelAndView toCodeTypeQuery(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeInfoQuery");
        return modelAndView;
	}
	
	
	/**
	 * ��ת���������Ͳ�ѯҳ��
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/toCodeValueQuery")
	@RequiresRoles("admin")
	public ModelAndView toCodeValueQuery(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/codevalue/sysCodeTypeDetailInfoQuery");
        return modelAndView;
	}
	
	  
	 /**
	  * ͨ���������͡�����������ȡ���� 
	  * @param request
	  * @param response
	  * @param codevalue
	  * @return
	  * @throws AppException
	  */
	 @RequestMapping(value = "/getCodeValueList")
	 @ResponseBody
	 public String getCodeValueList(HttpServletRequest request, HttpServletResponse response,CodeType codetype) throws AppException {
			JSONObject jsonParam=JSONObject.fromObject(codetype);
			String url=API_BASE_URL+URL+"/getCodeValueList";
			return HttpRequestUtils.httpPost(url, jsonParam).getJSONArray("obj").toString();
	 } 
	
}