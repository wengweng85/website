package com.insigma.screen.resolver;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.insigma.screen.BaseScreen;
import com.insigma.screen.factory.ScreenFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * �Զ��嵼��ģ�������<br>
 * <li>����ģ��ʱ��Ⱦģ����</li>
 * 
 * @author zbq
 * @date 2010-05-28
 */
public class ScreenResolver {

	private static final String EMPTY_VIEW = "";
	private static final String SCREEN_SUFFIX = "Screen";
	private static final Pattern FTL_NAME_PTN = Pattern.compile(".*/([\\w]*)$");

	private final Map<String, Object> staticAttributes = new HashMap<String, Object>();

	private String suffix;

	private FreeMarkerConfigurer freemarkerConfigurer;

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public FreeMarkerConfigurer getFreemarkerConfigurer() {
		return freemarkerConfigurer;
	}

	public void setFreemarkerConfigurer( FreeMarkerConfigurer freemarkerConfigurer) {
		this.freemarkerConfigurer = freemarkerConfigurer;
	}

	@SuppressWarnings("rawtypes")
	public void setAttributes(Map attributes) {
		setAttributesMap(attributes);
	}

	@SuppressWarnings("rawtypes")
	public void setAttributesMap(Map attributes) {
		if (attributes == null) {
			return;
		}
		Iterator it = attributes.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			if (key instanceof String) {
				addStaticAttribute((String) key, entry.getValue());
			}
		}
	}

	
	public void addStaticAttribute(String name, Object value) {
		this.staticAttributes.put(name, value);
	}

	/**
	 * ȡ����Ⱦ��(����ģ��ϲ�)����ͼ
	 * 
	 * @param template
	 * @return ��Ⱦ����ͼ
	 */
	public String renderView(String template) {
		if (StringUtils.isBlank(template)) {
			return EMPTY_VIEW;
		}
		return renderViewByTemplate(template);
	}

	/**
	 * ��Ⱦ��ͼ(ȡ������, �ϲ�����ģ��)
	 * 
	 * @param template
	 * @return ��Ⱦ����ͼ
	 */
	private String renderViewByTemplate(String template) {

		String result = EMPTY_VIEW;
		// ȡ�ö�Ӧ��Screen��
		// ȡ�����ݺ���Ⱦ��ͼ, ��ɺ󷵻�, ���ʧ�ܷ���"".
		try {

			BaseScreen screen = getScreenBean(template);
			if (screen == null) {
				return result;
			}

			HttpServletRequest request = getContextRequest();

			Map<String, Object> model = screen.referenceData(request);
			result = mergeModelToTemlate(template, getMergeModel(model));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ȡ��Screen��Ⱦҳ����������(���õķ���������)
	 * 
	 * @param model
	 * @return
	 */
	private Map<String, Object> getMergeModel(Map<String, Object> model) {
		// ������ȾScreenҳ���������ݡ�Freemarker�����ļ�������
		Map<String, Object> mergeModel = new HashMap<String, Object>();
		mergeModel.putAll(this.staticAttributes);
		mergeModel.putAll(model);
		
		return mergeModel;
	}

	private HttpServletRequest getContextRequest() {
		RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
		return ((ServletRequestAttributes) requestAttr).getRequest();
	}

	/**
	 * ��Ⱦ��ͼ, �����ݺϲ���ģ����.
	 * 
	 * @param ftlName
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	private String mergeModelToTemlate(String ftlName, Map<String, Object> model) throws Exception {

		Template template = null;
		StringWriter swriter = new StringWriter();

		Configuration configuration = freemarkerConfigurer.getConfiguration();

		template = configuration.getTemplate(ftlName + getSuffix());
		template.process(model, swriter);

		return swriter.toString();
	}

	/**
	 * ��������ȡ��Screenʵ��<br>
	 * <li>��: /demo.ftl => demoScreen.java</li>
	 * 
	 * @param template
	 * @return BaseScreen
	 */
	private BaseScreen getScreenBean(String name) {
		ScreenFactory factory = ScreenFactory.getInstance();
		return (BaseScreen) factory.getScreenBean(getScreenName(name));
	}

	/**
	 * ����ģ������ȡ�ö�Ӧ��Screen������
	 * 
	 * @param template
	 * @return screenName
	 */
	private String getScreenName(String template) {
		Matcher matcher = FTL_NAME_PTN.matcher(template);
		if (matcher.find()) {
			template = matcher.group(1);
		}

		return template + SCREEN_SUFFIX;
	}

}
