package com.insigma.mvc.controller.demo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.insigma.http.HttpRequestUtils;
import com.insigma.json.JsonParseUtil;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.model.DemoAc01;


/**
 * demo测试程序
 * @author wengsh
 *
 */
@Controller
@RequestMapping("/demo")
public class DemoController extends MvcHelper<DemoAc01> {

	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	
	
	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	}


	public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	}


	public String getURL() {
		return URL;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}

	private String URL="/demo";
	
	
	/**
	 * 跳转至查询页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toquery")
	public ModelAndView draglist(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("/demo/demoQuery");
        return modelAndView;
	}
	
	
	/**
	 * 获取人员信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getAc01List")
	@ResponseBody
	public String getAc01List(HttpServletRequest request,Model model, DemoAc01 ac01 ) throws Exception {
		//转换成json对象并调用远程接口
		JsonParseUtil<DemoAc01> jsonParseUtil=new JsonParseUtil<DemoAc01>();
		String url=API_BASE_URL+URL+"/getAc01List";
		return HttpRequestUtils.httpPost(url, jsonParseUtil.toJsonObject(ac01)).getJSONObject("obj").toString();
	}
	

	
	/**
	 * 通过id删除人员demo信息
	 * @param request
	 * @param model
	 * @param aac001
	 * @return
	 */
	@RequestMapping("/deletebyid/{id}")
	@ResponseBody
	public String deleteDemoDataById(HttpServletRequest request,Model model,@PathVariable String id) throws  Exception{
		//用户get方法
		String url=API_BASE_URL+URL+"/deletebyid/"+id;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	
	/**
	 * 跳转至编辑页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toedit/{id}")
	public ModelAndView toedit(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		ModelAndView modelAndView=new ModelAndView("/demo/demoEdit");
		String url=API_BASE_URL+URL+"/getDemoById/"+id;
		JSONObject jsonobject=HttpRequestUtils.httpGet(url);
		//返回jsonobject并转换成相应对象
		modelAndView.addObject("ac01",JSONObject.toBean(jsonobject.getJSONObject("obj"),DemoAc01.class));  
        return modelAndView;
	}
	
	
	/**
	 * 跳转至查看页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toview/{id}")
	public ModelAndView toview(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		ModelAndView modelAndView=new ModelAndView("/demo/demoView");
		String url=API_BASE_URL+URL+"/getDemoById/"+id;
		JSONObject jsonobject=HttpRequestUtils.httpGet(url);
		//返回jsonobject并转换成相应对象
		modelAndView.addObject("ac01",JSONObject.toBean(jsonobject.getJSONObject("obj"),DemoAc01.class));  
        return modelAndView;
	}
	
	
	/**
	 * 批量删除
	 * @param request
	 * @param model
	 * @param aac001
	 * @return
	 */
	@RequestMapping("/batdelete")
	@ResponseBody
	public String batDeleteDemodata(HttpServletRequest request,Model model,DemoAc01 ac01) throws  Exception{
		//转换成json对象并调用远程接口
		JsonParseUtil<DemoAc01> jsonParseUtil=new JsonParseUtil<DemoAc01>();
		String url=API_BASE_URL+URL+"/batdelete";
		return HttpRequestUtils.httpPost(url, jsonParseUtil.toJsonObject(ac01)).toString();
	}
	
	
	/**
	 * 跳转至编辑(新增)页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toadd")
	public ModelAndView toadd(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("/demo/demoAdd");
        return modelAndView;
	}
	
	/**
	 * 人员选择框
	 * @param request
	 * @return
	 */
	@RequestMapping("/toselect")
	public ModelAndView selectindex(HttpServletRequest request,Model model) throws Exception {
		String callback_fun_name=request.getParameter("callback_fun_name");
		ModelAndView modelAndView=new ModelAndView("demoSelect");
        modelAndView.addObject("callback_fun_name", callback_fun_name);
        return modelAndView;
	}
	
	/**
	 * 跳转至编辑(新增)页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDemoById/{id}")
	@ResponseBody
	public String getDemoById(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		//转换成json对象并调用远程接口
		String url=API_BASE_URL+URL+"/getDemoById/"+id;
		return HttpRequestUtils.httpGet(url).getJSONObject("obj").toString();
	}
	
	
	/**
	 * 更新或保存
	 * @param request
	 * @return
	 */
	@RequestMapping("/savedata")
	@ResponseBody
	public String savedata(HttpServletRequest request,Model model,@Valid DemoAc01 ac01,BindingResult result) throws Exception {
		//检验输入
		if (result.hasErrors()){
			return validate(result);
		}
		//转换成json对象并调用远程接口
		JsonParseUtil<DemoAc01> jsonParseUtil=new JsonParseUtil<DemoAc01>();
		String url=API_BASE_URL+URL+"/savedata";
		return HttpRequestUtils.httpPost(url, jsonParseUtil.toJsonObject(ac01)).toString();
				
	}
	
	/**
	 * 更新个人图片编号
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatefile/{id}/{bus_uuid}")
	@ResponseBody
	public String updatefile(HttpServletRequest request,Model model,@PathVariable String id,@PathVariable String bus_uuid) throws Exception {
		DemoAc01 ac01=new DemoAc01();
		ac01.setAac001(id);
		ac01.setBus_uuid (bus_uuid);
		String url=API_BASE_URL+URL+"/updatefile/"+id+"/"+bus_uuid;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	/**
	 * 删除个人图片
	 * @param request
	 * @return
	 */
	@RequestMapping("/deletefile/{id}/{bus_uuid}")
	@ResponseBody
	public String deletefile(HttpServletRequest request,Model model,@PathVariable String id,@PathVariable String bus_uuid) throws Exception {
		DemoAc01 ac01=new DemoAc01();
		ac01.setAac001(id);
		ac01.setBus_uuid(bus_uuid);
		//转换成json对象并调用远程接口
		String url=API_BASE_URL+URL+"/deletefile/"+id+"/"+bus_uuid;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	/**
	 * api测试页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/open_api_test_page")
	public ModelAndView open_api_test_page(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("demo/api_test_page");
        return modelAndView;
	}

}
