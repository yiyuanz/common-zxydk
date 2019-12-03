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
 * zyiyuan 10:21:19 AM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.annotation.impl;

import java.io.Serializable;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.cartechfin.cheyunpay.zxydk.flow.contants.FlowContants;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlowChart;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.FlowActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.valueobject.FlowEndActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.repertory.FlowBaseActionCreator;

import lombok.Data;

@Data
public class FlowChatHandle implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1750806894359019795L;
	
	/** 流程图名 */
	private String name;
	
	/** 流程图版本 */
	private String version;
	
	/** 流程图中是否打开流程日志跟踪 */
	private Boolean hasOpenLogger = Boolean.FALSE;
	
	/** 流程图中是否开启事务 */
	private Boolean hasTransaction = Boolean.FALSE;
	
	/** 流程图中节点信息 */
	private List<FlowNodeHandle> nodeHandles = Lists.newArrayList();

	// 
	public FlowChatHandle(	String name, String version, Boolean hasOpenLogger,  Boolean hasTransaction) {
		super();
		this.name = name;
		this.version = version;
		this.hasOpenLogger = hasOpenLogger;
		this.hasTransaction = hasTransaction;
	}

	public FlowChatHandle(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}
	
	public void addFlowNode( FlowNodeHandle flowNode ) {
		
		if( null == this.nodeHandles ) {
			
			this.nodeHandles = Lists.newArrayList();
		}
		
		this.nodeHandles.add(flowNode);
	}
	
	public ActionFlow fillIntoFlowBaseInfo( FlowBaseActionCreator creator ) {
		
		ActionFlow flow = creator.createInstance(ActionFlowChart.class);
		
		flow.setUniqueKey(new ActionFlow.Key( this.getName() , this.getVersion()) ); 
		
		flow.setHasOpenLogger( this.getHasOpenLogger() ); 
		
		flow.setHasOpenTransaction( this.getHasTransaction() );
		//  init end node
		FlowActionNode endNode = creator.createInstance(FlowEndActionNode.class);
		
		endNode.setNodeName(FlowContants.FLOW_END_LABEL);
		
		endNode.setFlowVersion(this.getVersion());

		endNode.setHasOpenLogger(hasOpenLogger);
		
		endNode.setTransaction(Boolean.FALSE);
		
		endNode.setFlowName(this.getName());
		
		flow.addActionNode(endNode);
		 
		return flow;
	}
	
	
	
	public ActionFlow.Key actionFlowKey(){
		
		return new ActionFlow.Key( this.name, this.version );
	}
}
