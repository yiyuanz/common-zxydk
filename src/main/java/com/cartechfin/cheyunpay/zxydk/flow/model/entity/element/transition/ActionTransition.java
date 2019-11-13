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
 * zyiyuan 8:56:29 AM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition;

import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowBaseAction;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;

/**
 * @category 流程图的元素 - 线
 * 
 */
@SuppressWarnings("all")
public interface ActionTransition<T extends Object> extends FlowBaseAction<T> {
	
	/**
	 * @category 设置 线 的描述信息 
	 * 
	 * @param desc [String]
	 * 
	 */
	public void setDesc( String desc );
	
	/**
	 * @category 获得 线的描述信息 
	 */
	public String getDesc();
	
	/**
	 * @category 设置 线的唯一标识名
	 * 
	 * @param eventName
	 * 
	 */
	public void setEvent( String eventName );
	
	/**
	 * @category  获得 线的唯一标识名
	 */
	public String getEvent();
	
	/**
	 * @category 设置 线的 To的标识值 
	 * 
	 * @param toName
 	 *  
	 */
	public void setTo( String toName );
	
	
	/**
	 * @category 获得 线的To的标识值 
	 */
	public String getTo( );
	
	/**
	 * @category 设置 线的 from的标识值 
	 * 
	 * @param fromName
 	 *  
	 */
	public void setFrom( String fromName );
	
	
	/**
	 * @category 获得 线的From的标识值 
	 */
	public String getFrom( );
	
	/**
	 * @category 设置 To的节点信息
	 * 
	 * @param toNode  [ ActionNode ]
	 * 
	 */
	public void setToNode( ActionNode toNode );

	/**
	 *  @category 获得 To的节点信息
	 *  
	 */
	public ActionNode getToNode();
	
	/**
	 * @category 设置 from的节点信息
	 * 
	 * @param fromNode  [ ActionNode ]
	 * 
	 */
	public void setFromNode( ActionNode fromNode );

	/**
	 *  @category 获得 from的节点信息
	 *  
	 */
	public ActionNode getFromNode();
	
	/**
	 * @category  流程版本号 
	 * 
	 * @param flowVersion
	 * 
	 */
	public void intoFlowVersion( String flowVersion );
	
	/**
	 *  @category 流程名称
	 * 
	 *  @param flowName
	 *  
	 */
	public void intoFlowName( String flowName );
	
	/**
	 *  @category 获得流程版本
	 * 
	 *  @return flowName
	 *  
	 */
	public String flowVersion();
	
	/**
	 *  @category 获得流程名称
	 * 
	 *  @return flowName
	 *  
	 */
	public String flowName();
	
}
