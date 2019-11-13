/*
 * www.cnvex.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 *  ======`-.____`-.___\_____/___.-`____.-'======
 *                     `=---='
 *  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *           佛祖保佑       永无BUG
 */

/*
 * 修订记录：
 * zyiyuan 9:38:30 AM 创建
 */
package com.cartechfin.cheyunpay.zxydk.common.utils;

import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;

public class XmlDocumentReader  implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5598640323802550243L;

	// 日志
	private static Logger logger = LoggerFactory.getLogger(XmlDocumentReader.class);
	
	/**
	 * @category 验证resouce 文件 
	 * 
	 * @param  xsdUrl   【xml文件的验证xsd定义】
	 *  
	 * @param  Resource 【 流程图文件 】 
	 * 
	 */
	public static void validatorResouce( String xsdUrl , Resource res ) {
		try {
			// validator
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			
		    ClassPathResource resource = new ClassPathResource( xsdUrl );
		    
		    Schema schema = schemaFactory.newSchema(resource.getURL());
		    
		    Validator validator = schema.newValidator();
		    
		    Source source = new StreamSource(res.getURL().openStream());
			
		    validator.validate(source);
		    
		} catch( Exception ex ) {
			
			logger.error("{} flow xml file validator error , ex:{}!", res.getFilename() , ex);
			
			throw new RuntimeException(String.format("流程引擎验证resouce文件异常，validator %s : resource file error!", res.getFilename()));
		}
	}
	
	/**
	 * @category 根据流程图 获得图中根节点 
	 * 
	 * @param  Resource 【 流程图文件 】 
	 * 
	 * @return Element 【 根节点 】
	 * 
	 */
	public static Element getRootElementWithResource( Resource res ) {
		
		try {
			 // domBuilder
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();  
		    
		    DocumentBuilder domBuilder = builderFactory.newDocumentBuilder();  
			
		    Element root = domBuilder.parse(res.getInputStream()).getDocumentElement();
		    
		    return root;
		    
		} catch( Exception ex ) {
			
			logger.error("流程引擎初始化流程图xml的解析器失败，ex：{}！" , ex);

			throw new RuntimeException("流程引擎初始化xml解析器失败!");
		}
		
	}
}
