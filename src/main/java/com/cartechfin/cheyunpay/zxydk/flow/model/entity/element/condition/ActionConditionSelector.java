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
 * zyiyuan 3:18:17 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.condition;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @category 流程图的元素 -  条件选择器 - 定义
 * 
 */
@Data
@SuppressWarnings("all")
public class ActionConditionSelector< T extends Object > implements ActionCondition< T > {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8379019526620581985L;
	
	// 日志
	private static Logger logger = LoggerFactory.getLogger(ActionConditionSelector.class);
	
	/** 选择器 所挂载节点 */
	private ActionNode<T> node;
	
	/** 选择器 所承载的流程图的路由线路 */
	private Map<String, ActionTransition<T>> transactionLines = Maps.newHashMap();
	
	/** 选择器执行的结果 */
	private String resultEvent;

	@Override
	public void action(T target, Map<String, Object> vals) {
		
		ActionTransition<T> transaction = this.transactionLines.get(this.resultEvent);
		
		if( null == this.resultEvent ) {
			
			logger.error("流程推进时，选择器的执行结果为空！name:{}", this.getClass().getName());
			
			throw new RuntimeException("流程推进时，选择器的执行结果为空！");
		}
		
		if( null == transaction ) {
			
			logger.error("流程推进时，选择器的执行结果无法匹配到具体的线路！name:{}", this.getClass().getName() );
			
			throw new RuntimeException("流程推进时，选择器的执行结果无法匹配到具体的线路！");
		}
		
		transaction.action(target, vals);
	}

	@Override
	public void addTransaction(ActionTransition transaction) {
		
		if( null == this.transactionLines ) {
			
			this.transactionLines = Maps.newHashMap();
		}
		
		this.transactionLines.put( transaction.getEvent(), transaction );
	}

	@Override
	public ActionNode getActionNode() {
		return this.node;
	}

	@Override
	public void setActionNode(ActionNode node) {
		this.node = node;
	}

	@Override
	public void setResultEvent(String event) {
		this.resultEvent = event;
	}

	@Override
	public String getResultEvent() {
		return this.resultEvent;
	}
	
	/**
	 * @category  获得 该选择器下的 所有路由的线
	 */
	@Override
	public Map<String, ActionTransition<T>> getTransactionLines(){
		return this.transactionLines;
	}
	
	/**
	 * @category  设置 该选择器下的 所有路由的线
	 */
	@Override
	public void setTransactionLines( Map<String, ActionTransition<T>> maps ) {
		this.transactionLines = maps;
	}
}
