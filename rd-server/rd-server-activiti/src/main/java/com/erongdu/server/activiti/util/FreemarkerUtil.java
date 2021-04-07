package com.erongdu.server.activiti.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
/**
 * Freemarker工具类
 * @author lh@erongdu.com
 * @since  2020年8月7日
 *
 */
@Slf4j
public class FreemarkerUtil {
	
	/**
	 * 配置属性
	 */
	private static Configuration CONFIG = new Configuration(Configuration.VERSION_2_3_0);
	static {
		CONFIG.setDefaultEncoding("UTF-8");
	}
	
	public static String renderTemplate(String s, Map<String, Object> data) {
		StringWriter w = new StringWriter();
		try {
			Template t = new Template(null, new StringReader(s), CONFIG);
			t.process(data, w);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return w.getBuffer().toString();
	}

	public static String renderFileTemplate(String file, Map<String, Object> data){
		StringWriter w = new StringWriter();
		Template t;
		try {
			Configuration cfg = CONFIG;
			String path = FreemarkerUtil.class.getResource("/templates").getPath();
			cfg.setDirectoryForTemplateLoading(new File(path));
			t = cfg.getTemplate(file);
			t.process(data, w);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return w.getBuffer().toString();
	}
	
	

}
