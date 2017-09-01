package com.insigma.common.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * ��ҳ��ǩ
 * @author wengsh
 * @date 2012-6-5
 *
 */
public class PagingTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	/**
	 * �����ַ
	 */
	private String url;
	/**
	 * �ܹ��ļ�¼����
	 */
	private int totalCount;
	/**
	 * ��ǰҳ
	 */
	private int curPage;
	/**
	 * ҳ��С
	 */
	@SuppressWarnings("unused")
	private int pageNumber;

	/**
	 * ÿҳ����
	 */
	private int pageSize;
	/**
	 * ��ѯ������
	 */
	private String queryFormId;

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public String getQueryFormId() {
		return queryFormId;
	}

	public void setQueryFormId(String queryFormId) {
		this.queryFormId = queryFormId;
	}



	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();

		// �õ���ҳ���ҳ��,(��ҳ��/ҳ��С)+1
		if (pageContext.getRequest().getAttribute("pagesize") == null || pageContext.getRequest().getAttribute("pagesize").equals("")) {
			pageSize = 15;
		}else{
			pageSize=Integer.parseInt((String)pageContext.getRequest().getAttribute("pagesize").toString());
		}
		if (pageContext.getRequest().getAttribute("curPage") == null || pageContext.getRequest().getAttribute("curPage").equals("")) {
			curPage = 0;
		}else{
			curPage=Integer.parseInt(pageContext.getRequest().getAttribute("curPage").toString());
		}
		if (pageContext.getRequest().getAttribute("totalCount") == null || pageContext.getRequest().getAttribute("totalCount").equals("")) {
			totalCount = 0;
		}else{
			totalCount=Integer.parseInt((String) pageContext.getRequest().getAttribute("totalCount").toString());
		}
		int pageNumber = 0;// ����ҳ��
		if (totalCount % pageSize == 0) {
			pageNumber = totalCount/ pageSize;
		} else {
			pageNumber = totalCount/ pageSize + 1;
		}
		if (curPage < 0) {
			curPage = 0;
		}
		if(curPage>pageNumber-1){
			curPage=pageNumber-1;
		}
		try {
		//if (pageNumber > 1) {
			out.println("<form action='"+url+"' method='post' id='pageForm' style='display: inline;'>");
			if(totalCount>0){
				out.println("�ܼ�¼��:<font style='color:red;font-weight:bold;'>"+totalCount+"</font>&nbsp;&nbsp;");
			}
			else{
				out.println("<font style='color:red;font-weight:bold;'>û���ҵ���صļ�¼!</font>");
			}
			if (pageNumber > 1) {
				out.println("|");
				out.println("<a href='#location'></a>");
				if (curPage - 1 < 0) {
					out.println("<font style='color:#ccc;cursor:pointer;' >��&nbsp;ҳ</font>&nbsp;");
					out.println("<font style='color:#ccc;cursor:pointer;' >��һҳ</font>&nbsp;");
				} else {
					out.println("<a href='javascript:;' title='��ҳ' onclick='page(" + 0 + ")'>��&nbsp;ҳ</a>&nbsp;");
					out.println("<a href='javascript:;' title='��һҳ' onclick='page(" + (curPage - 1)+ ")'>��һҳ</a>&nbsp;");
				}
				int begin =curPage-5;
				int end=curPage+5;
				if(curPage<5){
					begin=0;
					end=pageNumber>10?10:pageNumber;
				}
				if(curPage>pageNumber-5){
					begin=pageNumber-10<0?0:pageNumber-10;
					end=pageNumber;
				}
				for (int i=begin;i<end;i++){
					if(curPage==i){
					    out.println("<a href='javascript:;' class='page' style='color:red;font-weight:bold;' onclick='page(" + i+ ")'>"+(i+1)+"</a>");
					}else{
					    out.println("<a href='javascript:;' class='page' onclick='page(" + i+ ")'>"+(i+1)+"</a>");
					}
				}
				// ��һҳ
				if (curPage >= pageNumber - 1) {
					out.println("<font style='color:#ccc;cursor:pointer;' >��һҳ</font>&nbsp;");
					//out.println("<font style='color:#ccc;cursor:pointer;' >β&nbsp;ҳ</font>&nbsp;");
				} else {
					out.println("<a href='javascript:;' title='��һҳ' onclick='page(" + (curPage + 1)+ ")'>��һҳ</a>&nbsp;");
					//out.println("<a href='javascript:;' title='βҳ' onclick='page(" + (pageNumber- 1)+ ")'>β&nbsp;ҳ</a>&nbsp;");
				}
				//out.println("|");
				//out.println("��ǰҳ��<font style='color:red;font-weight:bold;'>"+(curPage+1)+"</font>/"+pageNumber+"ҳ");
				//out.println("|");
				/**
				out.println("ת��<input type='text' id='gtop' style='width:28px;height:14px;line-height:14px;text-align:center;' maxlength='3'>ҳ</input>&nbsp;");
				out.println("<a href='javascript:void(0);' onclick='gotoP()'>ȷ��</a>");
				 */
				out.println("<input type=hidden id='p' name='p'>");
				out.println("<script type='text/javascript'>");
				/**
				out.println("function gotoP(){ var n=$('#gtop').val();if(n){if(isNaN(n)){alert('��������ȷ��ҳ�룡');}");
				out.println("else{if(n>"+pageNumber+"||n<=0){alert('��ҳ�벻���ڣ�')}else{page(n-1);}}}else{alert('��������ȷ��ҳ�룡');}}");
				*/
				out.println("function page(n){ ");
				out.print("$('#p').val(n);");
				if(getQueryFormId()!=null&&!("").equals(getQueryFormId())){
					out.print("var action=$('#"+queryFormId+"').attr('action'); var param='&p='+$('#p').val(); ");
					out.println("$('#"+queryFormId+"').attr('action',action+param); $('#"+queryFormId+"').submit();");
				}else{
					out.print("$('#pageForm').submit();");
				}
				out.print("}");
				out.println("</script>");
			}
			out.println("</form>");
		//}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}







}
