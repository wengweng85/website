package com.insigma.mvc.controller.sysmanager.userrole;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.insigma.http.HttpRequestUtils;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.model.SGroup;
import com.insigma.mvc.model.SRole;

/**
 * �û������û�����
 * @author wengsh
 *
 */
@Controller
@RequestMapping("/sys/userrole")
public class SysUserRoleController extends MvcHelper {
	
	
	private String API_BASE_URL="http://127.0.0.1:8091/myapi";
	
	
	public String getAPI_BASE_URL() {
		return API_BASE_URL;
	 }

	 public void setAPI_BASE_URL(String aPI_BASE_URL) {
		API_BASE_URL = aPI_BASE_URL;
	 }
	 
	private String URL="/sys/userrole";
	/**
	 * ҳ���ʼ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/index")
	@RequiresRoles("admin")
	public ModelAndView draglist(HttpServletRequest request,Model model) throws Exception {
		ModelAndView modelAndView=new ModelAndView("sysmanager/userrole/sysUserRoleIndex");
        return modelAndView;
	}
	
	
	/**
	 * ����������
	 * @param request
	 * @return
	 */
	@RequestMapping("/treedata")
	@RequiresRoles("admin")
	@ResponseBody
	public  List<SGroup>  getGroupTreeData(HttpServletRequest request,Model model) throws Exception {
		String parentid=request.getParameter("id");
		if(parentid.equals("")){
			parentid="G001";
		}
		String url=API_BASE_URL+URL+"/treedata/"+parentid;
		return new HttpRequestUtils<SGroup>().httpGetReturnList(url,SGroup.class);
	}
	
	
	/**
	 * ͨ��������Ż�ȡ������Ϣ
	 * @param request
	 * @return
	 */
	@RequestMapping("/getgroupdatabyid/{id}")
	@RequiresRoles("admin")
	@ResponseBody
	public String getgroupdata(HttpServletRequest request,Model model,@PathVariable String id ) throws Exception {
		String url=API_BASE_URL+URL+"/getgroupdatabyid/"+id;
		return new HttpRequestUtils<String>().httpGet(url).toString();
	}
	
	/**
	 * ��ȡ��Ա��Ϣ�б�
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserListDataByid")
	@RequiresRoles("admin")
	@ResponseBody
	public String  getUserListByGroupid(HttpServletRequest request,Model model,SGroup sgroup ) throws Exception {
		if(StringUtils.isEmpty(sgroup.getGroupid())){
			sgroup.setGroupid("G001");
		}
		String url=API_BASE_URL+URL+"/getUserListDataByid/";
		return new HttpRequestUtils<SGroup>().httpPost(url,sgroup).toString();
	}
	
	
	/**
	 * ͨ���û�id��ȡ��ɫ�б�ѡ��״̬
	 * @param request
	 * @return
	 */
	@RequestMapping("/getRoleByUserId")
	@RequiresRoles("admin")
	@ResponseBody
	public String  getRoleByUserId(HttpServletRequest request,Model model,SRole srole ) throws Exception {
		if(StringUtils.isEmpty(srole.getUserid())){
			srole.setUserid("");
		}
		String url=API_BASE_URL+URL+"/getRoleByUserId/";
		return new HttpRequestUtils<SRole>().httpPost(url,srole).toString();
	}
	
	
	/**
	 * �����û���Ӧ��ɫ
	 * @param request
	 * @param model
	 * @param srole
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveUserRole")
	@RequiresRoles("admin")
	@ResponseBody
	public String saveUserRole(HttpServletRequest request,Model model,SRole srole ) throws Exception {
		String url=API_BASE_URL+URL+"/saveUserRole/";
		return new HttpRequestUtils<SRole>().httpPost(url,srole).toString();
	}
}
