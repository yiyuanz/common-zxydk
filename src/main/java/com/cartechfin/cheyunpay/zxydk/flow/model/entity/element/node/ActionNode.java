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
 * zyiyuan 2:50:20 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node;

import java.util.Map;

import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowBaseAction;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;

/**
 *  @category  流程图的元素 -  节点 
 *  <p> 对应的是 流程图中 的 node节点的配置信息 </p>
 *  
 *  
 */
public interface ActionNode<T extends Object> extends FlowBaseAction<T>{
	
	
	/** 设置 node的名字属性 name  */
	public void setNodeName(String name);
	
	/** 获得 node的名字属性 name */
	public String getNodeName();
	
	/** 设置 node的事务标识  */
	public void setTransaction(boolean isTransaction);
	
	/** 获得事务标识 */
	public boolean isTransaction();
	
	/** 设置flow的版本 */
	public void setFlowVersion(String flowVersion);
	
	/** 设置flow的空间命名 */
	public void setFlowName(String flowName);
	
	/** 获得 flow的版本 */
	public String getFlowVersion();
	
	/** 获得flow的空间命名 */
	public String getFlowName();
	
	/** 是否开启流程跟踪日志 */
	public void setOpenLogger( Boolean hasOpenLogger );
	
	/**
	 * @category 核心执行方法 
	 * 
	 * @param T target 流程节点传递信息
	 * 
	 * @param vals Map<String, Object> 流程节点传递的扩展信息
	 * 
	 */
	public String execute( T target , Map<String, Object>  vals );
	
	/**
	 * @category 前置处理方法
	 * 
	 * @param T target 流程节点传递信息
	 * 
	 * @param vals Map<String, Object> 流程节点传递的扩展信息
	 * 
	 */
	public void before(T target, Map<String, Object> vars) ;

	/**
	 * @category 后置护理方法 
	 * 
	 * @param T target 流程节点传递信息
	 * 
	 * @param vals Map<String, Object> 流程节点传递的扩展信息
	 */
	public void after(T target, Map<String, Object> vars) ;
	
	/**
	 * @category  刷新关系
	 * 
	 * @param actionsNodes  Map<String, ActionNode<T>>
	 *  
	 */
	public void reflushTransition( Map< String, ActionNode<T>>  actionNodes );
	
	/**
	 * @category  节点注册 节点的相关 线条
	 * 
	 * @param transaction  ActionTransaction<T>
	 *  
	 */
	public void registTransition( ActionTransition<T> transaction );
}
