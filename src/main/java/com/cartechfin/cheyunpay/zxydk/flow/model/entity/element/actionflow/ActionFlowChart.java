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
 * zyiyuan 3:34:59 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cartechfin.cheyunpay.zxydk.flow.contants.FlowContants;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;
import com.google.common.collect.Maps;

import lombok.Data;

/**
 * @category 流程图元素定义 - 流程矢量图 
 * 
 */
@Data
@SuppressWarnings("all")
public class ActionFlowChart< T extends Object > implements ActionFlow< T > {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4368774242876366712L;
	
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(ActionFlowChart.class);
	
	/**
	 * @category 流程标识 
	 */
	private ActionFlow.Key  key;
	
	@Autowired
	private TransactionTemplate template;
	
	/**
	 * @category 是否开启日志
	 */
	private Boolean hasOpenLogger = Boolean.TRUE;
	
	
	/** 是否开启流程外层大事务 */
	private Boolean hasOpeanTransaction = Boolean.FALSE;
	
	/**
	 * @category 流程图中所有节点信息 
	 */
	private Map<String, ActionNode<T>> nodes = Maps.newHashMap() ;
	
	/**
	 * @category spring 处理上下文 
	 */
	private ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
	}
	
	/**
	 * @category  流程执行  从某个节点执行
	 * 
	 * @param nodeName 流程图中某个节点
	 * 
	 * @param target [T] 
	 * 
	 * @param vals [Map<String,Object>]
	 * 
	 */
	@Override
	public void actionFromNode(String nodeName, T target, Map<String, Object> vals) {
		
		ActionNode node = this.nodes.get(nodeName);
		
		if( null == node || StringUtils.isBlank(nodeName) ) {
			
			logger.error(" 流程引擎从节点执行时，发生空异常，nodeName：{} 非该：{}流程图中的节点信息！", nodeName , node );
			
			throw new RuntimeException( String.format( "流程引擎从节点执行时，发生空异常,nodeName:%s 非流程图节点！", nodeName ) ) ;
		}
		
		if( null != this.hasOpeanTransaction && this.hasOpeanTransaction ) {
			template.execute( new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(TransactionStatus status) {
					
					node.action(target, vals);
					
					return null;
				}
			});
		}else {
			node.action(target, vals);
		}
	}
	

	/**
	 * @category  流程图执行 - 默认从流程开始节点推进
	 * 
	 */
	@Override
	public void action(T target, Map<String, Object> vals) {
		
		if( null != this.hasOpeanTransaction && this.hasOpeanTransaction ) {
			
			template.execute(new TransactionCallback<Void>() {

				@Override
				public Void doInTransaction(TransactionStatus status) {
					
					ActionNode actionNode = nodes.get(FlowContants.FLOW_START_LABEL);
					
					actionNode.action( target, vals );
					
					return null;
				}
			});
		} else {
		
			ActionNode actionNode = nodes.get(FlowContants.FLOW_START_LABEL);
			
			actionNode.action( target, vals );
		}
	}
	
	@Override
	public void reflushTransaction() {
		
		for( Entry< String, ActionNode<T>> entry : this.nodes.entrySet() ) {
			
			ActionNode<T> node = entry.getValue();
			
			if( FlowContants.FLOW_END_LABEL.equals( node.getNodeName()) ) {
				continue;
			}
			
			node.reflushTransition(nodes);
		}
	}

	@Override
	public void addActionNode(ActionNode<T> actionNode) {
		
		if( null == nodes ) {
			
			nodes = Maps.newHashMap();
		}
		
		actionNode.setOpenLogger( this.hasOpenLogger );
		
		nodes.put( actionNode.getNodeName(), actionNode );
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void setHasOpenTransaction(Boolean isOpen) {
		this.hasOpeanTransaction = isOpen;
	}

	@Override
	public Boolean getHasOpenTransaction() {
		return this.hasOpeanTransaction;
	}

	@Override
	public void setUniqueKey(Key key) {
		this.key = key;
	}

	@Override
	public Key getUniqueKey() {
		return this.key;
	}
}
