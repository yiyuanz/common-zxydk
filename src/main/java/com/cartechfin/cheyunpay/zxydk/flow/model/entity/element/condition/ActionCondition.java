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
 * zyiyuan 2:33:10 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.condition;

import java.util.List;
import java.util.Map;

import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowBaseAction;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;
import com.google.common.collect.Maps;

/**
 *  @category 流程图的元素 -  条件选择器
 * 
 */
@SuppressWarnings("all")
public interface ActionCondition<T extends Object> extends FlowBaseAction<T>{

	/**
	 * @category 添加预判的结果关系 
	 * 
	 * @param transaction
	 * 
	 */
	public void addTransaction( ActionTransition transaction );
	
	
	/**
	 * @category 获得 选择器所挂的节点 
	 * 
	 */
	public ActionNode getActionNode();
	
	
	/**
	 * @category 设置 选择器所挂的节点 
	 */
	public void setActionNode( ActionNode node );
	
	
	/**
	 * @category 设置 节点执行后，选择器的路由结果
	 */
	public void setResultEvent( String event );
	
	
	/**
	 * @category  设置 获得选择器的路由结果
	 */
	public String getResultEvent();
	
	/**
	 * @category  获得 该选择器下的 所有路由的线
	 */
	public Map<String, ActionTransition<T>> getTransactionLines();
	
	/**
	 * @category  设置 该选择器下的 所有路由的线
	 */
	public void setTransactionLines( Map<String, ActionTransition<T>> maps );
	
}
