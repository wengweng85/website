package com.insigma.mvc.controller.common.fileupload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.insigma.http.HttpRequestUtils;
import com.insigma.json.JsonParseUtil;
import com.insigma.mvc.MvcHelper;
import com.insigma.mvc.controller.common.suggest.SuggestSearchController;
import com.insigma.mvc.model.CodeValue;
import com.insigma.mvc.model.SFileRecord;
import com.insigma.resolver.AppException;

/**
 * �ļ��ϴ�������
 */
@Controller
@RequestMapping(value = "/common/fileload")
public class FileLoadController extends MvcHelper<SFileRecord> {

	
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

	private String URL="/common/fileload";

    Log log = LogFactory.getLog(SuggestSearchController.class);

    
    
    
    /**
   	 * ��ת���ϴ�ҳ��
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping("/tofilelist")
   	public ModelAndView tofilelist(HttpServletRequest request,Model model,SFileRecord sFileRecord ) throws Exception {
   		ModelAndView modelAndView=new ModelAndView("common/fileupload/filelist");
   		if(sFileRecord.getFile_bus_id()==null){
   			throw new Exception("ҵ���Ų���Ϊ��");
   		}
   		if(sFileRecord.getFile_bus_type()==null){
   			throw new Exception("ҵ��ͼƬ���Ͳ���Ϊ��");
   		}
   		CodeValue codevalue=new CodeValue();
   		codevalue.setCode_type("FILE_BUS_TYPE");
   		codevalue.setCode_value(sFileRecord.getFile_bus_type());
   		/*if(syscodetypeservice.getCodeValueByValue(codevalue)==null){
   			throw new Exception("ҵ��ͼƬ���ͱ���"+sFileRecord.getFile_bus_type()+"������,�����������FILE_BUS_TYPE���Ƿ����!");
   		}*/
   		sFileRecord.setFile_name("����");
   		modelAndView.addObject("filerecord", sFileRecord);
   		return modelAndView;
   	}
   	
	/**
	 * ͨ����Աid��ȡ�ļ��б�
	 * @param request
	 * @return
	 */
	@RequestMapping("/getFileList")
	@ResponseBody
	public String getUserListByGroupid(HttpServletRequest request,Model model,SFileRecord sFileRecord ) throws Exception {
		JsonParseUtil<SFileRecord> jsonParseUtil=new JsonParseUtil<SFileRecord>();
		String url=API_BASE_URL+URL+"/getFileList";
		return HttpRequestUtils.httpPost(url, jsonParseUtil.toJsonObject(sFileRecord)).toString();
	}
	
    
    /**
	 * ��ת��ͼƬ�ϴ�ҳ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/toImgUpload")
	public ModelAndView toupload(HttpServletRequest request,Model modell,SFileRecord sFileRecord) throws Exception {
		ModelAndView modelAndView=new ModelAndView("common/fileupload/imgUpload");
		modelAndView.addObject("filerecord", sFileRecord);
		return modelAndView;
	}
	
   /**
	 * ��ת���ļ��ϴ�ҳ��
	 * @param request
	 * @return
	 */
	@RequestMapping("/toFileUpload")
	public ModelAndView toFileUpload(HttpServletRequest request,Model modell,SFileRecord sFileRecord) throws Exception {
		ModelAndView modelAndView=new ModelAndView("common/fileupload/fileUpload");
		modelAndView.addObject("filerecord", sFileRecord);
		return modelAndView;
	}
	
	
	/**
     * �ļ��ϴ�
     *
     * @param request
     * @param response
     * @return
     * @throws AppException
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String  upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file_bus_id=request.getParameter("file_bus_id");
		String file_bus_type=request.getParameter("file_bus_type");
        //���ҵ���Ų���
        /*if(null==file_bus_id||file_bus_id.equals("")){
        	this.error( "ҵ���Ų���Ϊ��,����");
        	return ;
        }
        
      //���ҵ���Ų���
        if(null==file_bus_type||file_bus_type.equals("")){
        	this.error( "�ļ�ҵ�����Ͳ���Ϊ��,����");
        	return ;
        }
        
        CodeValue codevalue=new CodeValue();
   		codevalue.setCode_type("FILE_BUS_TYPE");
   		codevalue.setCode_value(file_bus_type);
   		if(syscodetypeservice.getCodeValueByValue(codevalue)==null){
   			this.error("ҵ��ͼƬ���ͱ���"+file_bus_type+"������,�����������FILE_BUS_TYPE���Ƿ����!");
   			return ;
   		}*/
        
        
		long MAX_SIZE = 20* 1024 * 1024L;//100m
		
    	try {
            //����һ��ͨ�õĶಿ�ֽ�����
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
            if (multipartResolver.isMultipart(request)) {
                //ת���ɶಿ��request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                //ȡ��request�е������ļ���
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    //ȡ���ϴ��ļ�
                    MultipartFile multipartFile = multiRequest.getFile(iter.next());
                    if (multipartFile.getSize() > MAX_SIZE) {
                    	return this.error( "�ļ��ߴ糬���涨��С:" + MAX_SIZE / 1024 / 1024 + "M");
                    } else {
                       
                        // �õ�ȥ��·�����ļ���
                        String originalFilename = multipartFile.getOriginalFilename();
                        int indexofdoute = originalFilename.lastIndexOf(".");
                        
                        /**��ȡ�ļ��ĺ�׺**/
                        String endfix = "";
                        if (indexofdoute != -1) {
                            // β
                            endfix = originalFilename.substring(indexofdoute).toLowerCase();
                            if(endfix.equals(".jpg")||endfix.equals(".jpeg")||endfix.equals(".gif")||endfix.equals(".png") ||endfix.equals(".pdf")||endfix.equals(".doc")||endfix.equals(".docx")||endfix.equals(".xls")||endfix.equals(".xlsx")||endfix.equals(".rar")||endfix.equals(".zip")) {
	     						//�ϴ�����¼��־
                            	//String recordjson=fileloadservice.upload(originalFilename,file_bus_id,file_bus_type,multipartFile.getInputStream() );
                            	String recordjson="";
                            	System.out.println(recordjson);
                                return this.success(recordjson);
                            }else{
                            	return this.error("�ļ���ʽ����ȷ,��ȷ��,ֻ�����ϴ���ʽΪjpg��jpeg��gif��png��pdf��doc��docx��xls��xlsx��rar��zip��ʽ���ļ�");
                            }
                        }else{
                        	return this.error("�ļ���ʽ����");
                        }
                    }
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
			// �����ļ��ߴ�����쳣
			if (e instanceof SizeLimitExceededException) {
				return this.error( "�ļ��ߴ糬���涨��С:" + MAX_SIZE / 1024 / 1024 + "M");
			}
			return this.error(e.getMessage());
        }
    	return null;

    }

    
    /**
	 * ͨ��idɾ���ļ���Ϣ
	 * @param request
	 * @param model
	 * @param aac001
	 * @return
	 */
	@RequestMapping("/deletebyid/{id}")
	@ResponseBody
	public String deleteFileByid(HttpServletRequest request,Model model,@PathVariable String id) throws  Exception{
		String url=API_BASE_URL+URL+"/deletebyid/"+id;
		return HttpRequestUtils.httpGet(url).toString();
	}
	
	/**
	 * ����ɾ��
	 * @param request
	 * @param model
	 * @param aac001
	 * @return
	 */
	@RequestMapping("/batdelete")
	@ResponseBody
	public String batdelete(HttpServletRequest request,Model model,SFileRecord sFileRecord) throws  Exception{
		JsonParseUtil<SFileRecord> jsonParseUtil=new JsonParseUtil<SFileRecord>();
		String url=API_BASE_URL+URL+"/batdelete";
		return HttpRequestUtils.httpPost(url, jsonParseUtil.toJsonObject(sFileRecord)).toString();
	}
	
    /**
     * �ļ�����
     * @param request
     * @param response
     * @throws AppException
     */
    @RequestMapping(value = "/download/{bus_uuid}")
    public void download(@PathVariable(value="bus_uuid") String bus_uuid, HttpServletRequest request ,HttpServletResponse response) throws  AppException{
        try{
        	String url=API_BASE_URL+URL+"/getFileInfo/"+bus_uuid;
    		JSONObject jsonobject= HttpRequestUtils.httpGetReturnObject(url);
    		SFileRecord filerecord=(SFileRecord)JSONObject.toBean(jsonobject, SFileRecord.class);
    		
        	if(filerecord!=null){
        		 byte[] temp=download(filerecord.getFile_path());
                 if(temp!=null){
                 	//���д����Ƿ�ֹ��������Ĺؼ�����
                     response.setHeader("Content-disposition","attachment; filename="+ new String(filerecord.getFile_name().getBytes("GBK"),"iso-8859-1"));
                     BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(temp));
                     BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                     //�½�һ��2048�ֽڵĻ�����
                     byte[] buff = new byte[2048];
                     int bytesRead=0;
                     while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                         bos.write(buff,0,bytesRead);
                     }
                     bos.flush();
                     if (bis != null)
                         bis.close();
                     if (bos != null)
                         bos.close();
                 }else{
                 	throw new Exception("���ش���,�����ڵ�id");
                 }
        	}
        }catch(Exception e){
            //log.error(e.getMessage());
        }
    }
    /**
	 * ����
	 */
	public byte[] download(String file_path) {
		InputStream data = null;
		try {
			data = new FileInputStream(file_path);
			int size = data.available();
			byte[] buffer = new byte[size];
			IOUtils.read(data, buffer);
			return buffer;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(data);
		}
	}

}
