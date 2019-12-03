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
 * zyiyuan 10:46:54 AM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.annotation.impl;

import java.io.Serializable;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.cartechfin.cheyunpay.zxydk.flow.annotation.FlowTransaction;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.FlowActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransitionLine;
import com.cartechfin.cheyunpay.zxydk.flow.repertory.FlowBaseActionCreator;

import lombok.Data;

@Data
public class FlowNodeHandle implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5097870036447534890L;
	
	/** 流程节点名 */
	private String name;
	
	/** 流程节点事务标识 */
	private Boolean hasTransaction = Boolean.TRUE;
	
	/** 流程的各个 */
	private List<FlowTransactionHandle> conditions = Lists.newArrayList();

	public FlowNodeHandle(String name, Boolean hasTransaction, FlowTransaction[] trans ) {
		super();
		this.name = name;
		this.hasTransaction = hasTransaction;
		fillTransactionHandle(trans);
	}
	
	
	public void fillTransactionHandle( FlowTransaction[] trans ) {
		
		for( FlowTransaction tran : trans ) {
			
			FlowTransactionHandle transHandle = new FlowTransactionHandle( tran.event(), tran.desc() , tran.to() );
			
			this.addFlowTransactionHandle(transHandle);
		}
	}
	
	
	public void addFlowTransactionHandle( FlowTransactionHandle transaction ) {
		
		if( null == this.conditions ) {
			
			this.conditions = Lists.newArrayList();
		}
		
		this.conditions.add(transaction);
	}

	public FlowActionNode createActionNode(	ActionFlow actionFlow , FlowActionNode node, FlowBaseActionCreator flowBaseActionCreator) {
		 
		node.setNodeName(this.getName());
		
		node.setOpenLogger( actionFlow.getHasOpenLogger() );
		
		node.setFlowName(actionFlow.getUniqueKey().getName());
		
		node.setFlowVersion(actionFlow.getUniqueKey().getVersion());
		
		for( FlowTransactionHandle actionHandle : this.conditions ) {
			
			ActionTransition transition = flowBaseActionCreator.createInstance(ActionTransitionLine.class); 
			
			transition.setEvent(actionHandle.getEvent());
			
			transition.setDesc(actionHandle.getDesc());
			
			transition.setTo(actionHandle.getTo());
			
			transition.setFrom( node.getNodeName() );
			
			transition.setFromNode(node);
			
			transition.intoFlowName(node.getFlowName() );
			
			transition.intoFlowVersion( node.getFlowVersion() ); 
			
			node.registTransition(transition);
		}
		
		return node;
	}
	
}
