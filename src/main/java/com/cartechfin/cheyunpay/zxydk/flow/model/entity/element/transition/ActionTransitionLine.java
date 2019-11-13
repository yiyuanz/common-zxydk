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
 * zyiyuan 3:08:20 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition;

import java.util.Map;

import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;

import lombok.Data;


/**
 * @category 流程图的元素 - 线 - 的定义 
 * 
 */
@Data
@SuppressWarnings("all")
public class ActionTransitionLine<T extends Object> implements ActionTransition<T> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -10420024006834653L;
	
	/** 流程图中 对 线的描述 */
	private String desc;
	
	/** 流程图中 对 线的矢量指向定义 */
	private String event;
	
	/** 流程图中 对 线的to的节点指向 */
	private ActionNode<T> toNode;
	
	/** 流程图中 对 线的to头的描述信息  */
	private String to;
	
	/** 流程图中 对 线的to的节点指向 */
	private ActionNode<T> fromNode;
	
	/** 流程图中 对 线的from头的描述信息  */
	private String from;
	
	/**  所在的流程图的版本号  */
	private String flowVersion;
	
	/** 所在流程图的名称 */
	private String flowName;
	
	@Override
	public void action(T target, Map<String, Object> vals) {
		this.getToNode().action(target, vals);
	}

	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public void setEvent(String eventName) {
		this.event = eventName;
	}

	@Override
	public String getEvent() {
		return this.event;
	}

	@Override
	public void setTo(String toName) {
		this.to = toName;
	}

	@Override
	public String getTo() {
		return this.to;
	}

	@Override
	public void setToNode(ActionNode toNode) {
		this.toNode = toNode;  
	}

	@Override
	public ActionNode getToNode() {
		return this.toNode;
	}

	@Override
	public void setFromNode(ActionNode fromNode) {
		this.fromNode = fromNode;
	}
	
	@Override
	public ActionNode getFromNode() {
		return this.fromNode;
	}

	@Override
	public void intoFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}

	@Override
	public void intoFlowName(String flowName) {
		this.flowName = flowName;
	}

	@Override
	public String flowVersion() {
		return this.flowVersion;
	}

	@Override
	public String flowName() {
		return this.flowName;
	}
}
