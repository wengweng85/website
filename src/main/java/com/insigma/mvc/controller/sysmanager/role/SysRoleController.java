package com.insigma.mvc.controller.sysmanager.role;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.model.SRole;

/**
 * 角色管理及角色角色分配管理
 * @author wengsh
 *
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController extends MvcHelper<SRole>  {
	
	
	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	 }

	 public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	 }
	
	private String URL="/sys/role";
	
	/**
	 * 页面初始化
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	@RequiresRoles("admin")
	public ModelAndView draglist(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/role/sysRoleIndex");
        return modelAndView;
	}
	
	/**
	 * 角色列表查询
	 * @param request
	 * @return
	 */
	@RequestMapping("/querylist")
	@ResponseBody
	@RequiresRoles("admin")
	public String querylist(HttpServletRequest request,Model model,SRole srole) throws Exception {
		JSONObject jsonParam=JSONObject.fromObject(srole);
		String url=API_BASE_URL+URL+"/querylist/";
		return  HttpRequestUtils.httpPostReturnObject(url, jsonParam).toString();
	}
	
	
	/**
	 * 通过角色编号获取角色数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/getRoleData/{id}")
	@RequiresRoles("admin")
	@ResponseBody
	public String  getPermDataByid(HttpServletRequest request, HttpServletResponse response,Model model,@PathVariable String id) throws Exception {
		String url=API_BASE_URL+URL+"/getRoleData/"+id;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	
	/**
	 * 页面初始化
	 * @param request
	 * @return
	 */
	@RequestMapping("/toRoleEdit/{id}")
	public ModelAndView toRoleEdit(HttpServletRequest request,@PathVariable String id) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/role/sysRoleEdit");
		String url=API_BASE_URL+URL+"/toRoleEdit/"+id;
		JSONObject jsonresult= HttpRequestUtils.httpGetReturnObject(url);
		modelAndView.addObject("srole",JSONObject.toBean(jsonresult, SRole.class));  
        return modelAndView;
	}
	/**
	 * 删除角色相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteRoleDataById/{id}")
	@ResponseBody
	@RequiresRoles("admin")
	public String  deleteRoleDataById(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		String url=API_BASE_URL+URL+"/deleteRoleDataById/"+id;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	/**
	 * 更新或保存角色
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveorupdate")
	@ResponseBody
	@RequiresRoles("admin")
	public String  saveorupdate(HttpServletRequest request,Model model,@Valid SRole srole,BindingResult result) throws Exception {
		//检验输入
		if (result.hasErrors()){
			return validate(result);
		}
		JSONObject jsonParam=JSONObject.fromObject(srole);
		String url=API_BASE_URL+URL+"/saveorupdate";
		return HttpRequestUtils.httpPost(url,jsonParam).toString();
	}
	
	/**
	 * 角色-权限树加载
	 * @param request
	 * @return
	 */
	@RequestMapping("/treedata")
	@RequiresRoles("admin")
	@ResponseBody
	public  List<SRole>  treedata(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
		String id=request.getParameter("id");
		String url=API_BASE_URL+URL+"/treedata/"+id;
		return (List<SRole>)HttpRequestUtils.httpGetReturnList(url,SRole.class);
	}
	
	
	/**
	 * 更新或保存权限
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveroleperm")
	@ResponseBody
	@RequiresRoles("admin")
	public String  saveroleperm(HttpServletRequest request,Model model,SRole srole) throws Exception {
		JSONObject jsonParam=JSONObject.fromObject(srole);
		String url=API_BASE_URL+URL+"/saveroleperm";
		return HttpRequestUtils.httpPost(url,jsonParam).toString();
	}
}
