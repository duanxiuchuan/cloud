package com.erongdu.server.activiti.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;

//import com.erongdu.modules.activiti.modeler.converter.BpmnJsonConverter;
//import com.erongdu.modules.activiti.modeler.converter.CallActivityXMLConverter;
//import com.erongdu.modules.activiti.modeler.converter.UserTaskXMLConverter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * @author lh@erongdu.com
 * @since 2020-07-17
 *
 */
public class ActivitiUtil {

	static final String LIKE = "%";
	
    public static String text(InputStream fin) {
        byte[] buf = new byte[512];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            do {
                int size = fin.read(buf);
                if (size <= 0) {
                    break;
                }
                bout.write(buf, 0, size);
            } while (true);
            String text = new String(bout.toByteArray(), "UTF-8");
            bout.close();
            return text;
        } catch (Exception ex) {
            throw new RuntimeException("convert error");
        }
    }
    
    public static String like(String param) {
    	return LIKE.concat(param).concat(LIKE);
    }
    
    /**
     * 将xml转为jsonnode
     *
     * @param xml
     * @return com.fasterxml.jackson.databind.JsonNode
     * @author jx
     * @date 2019/10/24 17:31
     */
    public static JsonNode converterXmlToJson(String xml) {
//        // 创建转换对象
//        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
//        // XMLStreamReader读取XML资源
//        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
//        StringReader stringReader = new StringReader(xml);
//        XMLStreamReader xmlStreamReader = null;
//        try {
//            xmlStreamReader = xmlInputFactory.createXMLStreamReader(stringReader);
//        } catch (XMLStreamException e) {
//            e.printStackTrace();
//        }
//        BpmnXMLConverter.addConverter(new UserTaskXMLConverter());
//        BpmnXMLConverter.addConverter(new CallActivityXMLConverter());
//        // 把xml转换成BpmnModel对象
//        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(xmlStreamReader);
//        // 创建转换对象
//        BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();
//        // 把BpmnModel对象转换成json
//        JsonNode jsonNodes = bpmnJsonConverter.convertToJson(bpmnModel);
        
        InputStream bpmnStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xtr = null;
		try {
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			xtr = xif.createXMLStreamReader(in);
		} catch (Exception e) {
		}
        BpmnModel bpmnModel2 = new BpmnXMLConverter().convertToBpmnModel(xtr);
        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel2);
        
        return modelNode;
    }
}
