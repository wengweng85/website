package com.insigma.mvc.controller.sysmanager.perm;

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
import com.insigma.mvc.model.SPermission;

/**
 * 权限管理
 * @author wengsh
 *
 */
@Controller
@RequestMapping("/sys/perm")
public class SysPermController extends MvcHelper<SPermission> {
	
	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	 }

	 public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	 }
	
	private String URL="/sys/perm";
	
	/**
	 * 页面初始化
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	@RequiresRoles("admin")
	public ModelAndView draglist(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/perm/sysPermIndex");
        return modelAndView;
	}
	
	
	/**
	 * 权限树数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/treedata")
	@RequiresRoles("admin")
	@ResponseBody
	public List<SPermission>  treedata(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception {
		String url=API_BASE_URL+URL+"/treedata";
		return new HttpRequestUtils<SPermission>().httpGetReturnList(url,SPermission.class);
	}
	
	
	/**
	 * 通过权限编号获取权限数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPermData/{id}")
	@RequiresRoles("admin")
	@ResponseBody
	public String  getPermDataByid(HttpServletRequest request, HttpServletResponse response,Model model,@PathVariable String id) throws Exception {
		String url=API_BASE_URL+URL+"/getPermData/"+id;
		return new HttpRequestUtils<String>().httpGet(url).toString();
	}
	
	
	/**
	 * 更新或保存权限
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveorupdate")
	@ResponseBody
	@RequiresRoles("admin")
	public String saveorupdate(HttpServletRequest request,Model model,@Valid SPermission spermission,BindingResult result) throws Exception {
		//检验输入
		if (result.hasErrors()){
			return validate(result);
		}
		String url=API_BASE_URL+URL+"/saveorupdate/";
		return new HttpRequestUtils<SPermission>().httpPost(url,spermission).toString();
		
	}
	
	/**
	 * 删除权限相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/deletePermDataById/{id}")
	@ResponseBody
	@RequiresRoles("admin")
	public String  deletePermDataById(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		String url=API_BASE_URL+URL+"/deletePermDataById/"+id;
		return new HttpRequestUtils<String>().httpGet(url).toString();
	}
	
	/**
	 * 权限树移动排序
	 * @param request
	 * @return
	 */
	@RequestMapping("/moveNode/{id}")
	@ResponseBody
	@RequiresRoles("admin")
	public String  moveNode(HttpServletRequest request,Model model,@PathVariable String id) throws Exception {
		String url=API_BASE_URL+URL+"/moveNode/"+id;
		return new HttpRequestUtils<String>().httpGet(url).toString();
	}
}
