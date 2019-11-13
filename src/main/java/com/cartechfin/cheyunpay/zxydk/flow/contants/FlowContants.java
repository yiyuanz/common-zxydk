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
 * zyiyuan 4:22:03 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.contants;

import java.io.Serializable;

public class FlowContants implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 83037270739256837L;

	/** --------------------------------------------------流程引擎 - 流程图元定义的xsd文件------------------------------------------------------------ */
	public static String FLOW_XSD_URL ="/META-INF/flow.xsd";
	
	/** --------------------------------------------------流程引擎 - 流程图总节点常量------------------------------------------------------------ */
	/** 流程图 - 元素 - 流程总节点 */
	public static String FLOW_ELEMENT_FLOW_NODE = "flow";
	
	/** 流程图 - 元素 - 流程总节点属性  流程名 */
	public static String FLOW_ELEMENT_FLOW_NODE_ATTR_NAME = "name";
	
	/** 流程图 - 元素 - 流程总节点属性  流程版本 */
	public static String FLOW_ELEMENT_FLOW_NODE_ATTR_VERSION = "version";
	
	/** 流程图 - 元素 - 流程总节点属性  流程是否开启大事务 */
	public static String FLOW_ELEMENT_FLOW_NODE_ATTR_TRANSACTION = "hasOpenAllTransaction";
	
	
	/** 流程图 - 元素 - 流程总节点属性  流程是否开启跟踪日志 */
	public static String FLOW_ELEMENT_FLOW_NODE_ATTR_FOLLOW_LOGGER = "hasOpenLogger";
	
	/** --------------------------------------------------流程引擎 - 流程图描述节点常量------------------------------------------------------------ */
	public static String FLOW_ELEMENT_FLOW_DESC_NODE = "descrition";
	
	/** --------------------------------------------------流程引擎 - 流程图执行节点常量------------------------------------------------------------ */
	/** 流程图 - 元素 - 流程节点 */
	public static String FLOW_ELEMENT_FLOW_ACTION_NODE = "action_node";
	
	/** 流程图 - 元素 - 流程节点 - 属性 - 名 */
	public static String FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_NAME = "name";

	/** 流程图 - 元素 - 流程节点 - 属性 - 实现类型 */
	public static String FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_CLASS = "class";
	
	/** 流程图 - 元素 - 流程节点 - 属性 - 是否开启事务 */
	public static String FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_transaction = "isTransaction";
	
	/** 流程引擎 启动节点标识 */
	public static final String FLOW_START_LABEL = "flow_begin";
	
	/** 流程引擎 完成节点标识 */
	public static final String FLOW_END_LABEL = "flow_end";

	/** --------------------------------------------------流程引擎 - 流程图条件选择器常量------------------------------------------------------------ */
	/** 流程图 - 元素 -  条件选择器名 */
	public static String FLOW_ELEMENT_FLOW_CONDITION = "condition";
	
	/** --------------------------------------------------流程引擎 - 流程图条件配置元素常量------------------------------------------------------------ */
	/** 流程图 - 元素 -  条件配置元素名 */
	public static String FLOW_ELEMENT_FLOW_TRANSITION = "transition";
	
	/** 流程图 - 元素 -  条件配置元素 - 属性 - 选择值 */
	public static String FLOW_ELEMENT_FLOW_TRANSITION_ATTR_EVENT = "event";
	
	/** 流程图 - 元素 -  条件配置元素 - 属性 - 描述 */
	public static String FLOW_ELEMENT_FLOW_TRANSITION_ATTR_DESC = "desc";
	
	/** 流程图 - 元素 -  条件配置元素 - 属性 - 调整节点名 */
	public static String FLOW_ELEMENT_FLOW_TRANSITION_ATTR_TO = "to";
	
}
