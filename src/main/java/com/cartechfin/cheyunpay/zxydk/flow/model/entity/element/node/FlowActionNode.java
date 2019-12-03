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
 * zyiyuan 5:25:27 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;
import com.cartechfin.cheyunpay.zxydk.flow.contants.FlowContants;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.condition.ActionCondition;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.condition.ActionConditionSelector;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;
import com.cartechfin.cheyunpay.zxydk.flow.repertory.FlowBaseActionCreator;
import com.google.common.collect.Lists;

import lombok.Data;

/**
 * @category 流程图的元素 -  节点 - 定义
 * 
 */
@Data
@SuppressWarnings("all")
public abstract class FlowActionNode< T extends Object > implements ActionNode< T >{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8987145746093696353L;
	
	// 日志
	protected static final Logger logger = LoggerFactory.getLogger(FlowActionNode.class);
	
	/** 流程节点 */
	private String name;
	
	/** 流程节点所属 流程图名称（唯一标识） */
	private String flowName;
	
	/** 流程节点所属 流程图版本号 */
	private String flowVersion;
	
	/** 是否开启事务 */
	private Boolean hasTransaction = Boolean.FALSE;
	
	/** 条件 */
	private ActionCondition<T> condition = new ActionConditionSelector();
	
	/** 是否打开日志跟踪器 默认为不打开，是由配置文件决定 */
	private Boolean hasOpenLogger = Boolean.FALSE;
	
	/** 领域驱动工厂 */
	@Autowired
	protected DomainFactory domainFactory;
	
	@Autowired
	protected FlowBaseActionCreator flowCreator;
	
	/** 事件总线 */
	@Autowired
	protected EventBus eventBus;
	
	/** 事务模板 */
	@Autowired
	protected TransactionTemplate template;
	
	@Override
	public void setNodeName(String name) {
		this.name = name;
	}

	@Override
	public String getNodeName() {
		return this.name;
	}

	@Override
	public void setTransaction(boolean isTransaction) {
		this.hasTransaction = isTransaction;
	}

	@Override
	public boolean isTransaction() {
		return this.hasTransaction;
	}

	@Override
	public void setFlowVersion(String flowVersion) {
		this.flowVersion = flowVersion;
	}

	@Override
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	@Override
	public String getFlowVersion() {
		return this.flowVersion;
	}

	@Override
	public String getFlowName() {
		return this.flowName;
	}

	@Override
	public void action(T target, Map<String, Object> vals) {
		long now = System.currentTimeMillis();
		/** 打印前置日志 */ 
		loggerBeforeTrack( target, vals, now );
		if( this.hasTransaction ) {
			// 有节点事务
			this.template.execute(new TransactionCallback<Void>() {
				@Override
				public Void doInTransaction(TransactionStatus status) {
					// 事前处理
					before( target, vals );
					// 核心处理
					getCondition().setResultEvent( execute( target, vals ) );
					// 事后处理
					after( target, vals );
					
					return null;
				}
			});
		}else {
			// 无节点事务
			// 事前处理
			before( target, vals );
			// 核心处理
			getCondition().setResultEvent( execute( target, vals ) );
			// 事后处理
			after( target, vals );
		}
		/** 打印后置置日志 */ 
		loggerAfterTrack( target, vals , now );
		/** 流程推进 */
		pushForward( target, vals );
	}

	/**
	 * @category  流程推进
	 * 
	 * @param target T  流程推进传递信息
	 * 
	 * @param vals (Map<String,Object> ) 流程推进传递的扩展信息
 	 * 
	 */
	private void pushForward(T target, Map<String, Object> vals) {

		if( FlowContants.FLOW_END_LABEL.equals(this.name) ) {
			// 是否到了最后节点 flow_end
			return;
		}
		// 推进节点
		this.getCondition().action(target, vals);
	}

	/**
	 * @category 日志跟踪打印  before track
	 * 
	 * @param target T  流程推进传递信息
	 * 
	 * @param vals (Map<String,Object> ) 流程推进传递的扩展信息
	 * 
	 * @param now long 
	 * 
	 */
	private  void loggerBeforeTrack(T target, Map<String, Object> vals, long now) {
		
		if( this.hasOpenLogger ) {
			
			logger.info("--- 流程【{}】--- 版本【{}】 --- 节点【{}】 --- 执行开始！", this.flowName, this.flowVersion , this.name );
		}
	}
	
	/**
	 * @category 日志跟踪打印  after track
	 */
	private void loggerAfterTrack( T target , Map<String, Object> vals , long now ){
		 
		if( this.hasOpenLogger ) {
			
			logger.info("--- 流程【{}】--- 版本【{}】 --- 节点【{}】 --- 执行结束， 节点耗时：【{}】ms！", this.flowName, this.flowVersion , this.name, System.currentTimeMillis() - now);
		}
	}

	@Override
	public void reflushTransition(Map<String, ActionNode<T>> actionNodes) {
		
		if( !Lists.newArrayList( FlowContants.FLOW_END_LABEL, FlowContants.FLOW_START_LABEL ).contains(this.name) ) {
			
			if( null == this.condition || null == this.condition.getTransactionLines() || 0 == this.condition.getTransactionLines().size() ) {
				
				logger.error("为节点：{}刷新线路关系时，发现未配置条件选择器！流程：{}，版本：{}！", this.name, this.flowName, this.flowVersion);
				
				throw new RuntimeException(String.format("为节点：{%s}刷新线路关系时，发现未配置条件选择器！流程：{%s}，版本：{%s}！", this.name, this.flowName, this.flowVersion ));
			}
		}
		
		Iterator<Entry<String, ActionTransition<T>>> ite = this.condition.getTransactionLines().entrySet().iterator();
		
		while( ite.hasNext() ) {
			
			ActionTransition<T> transaction = ite.next().getValue();
			
			ActionNode<T> node = actionNodes.get(transaction.getTo());
			
			if( null == node || StringUtils.isBlank(node.getNodeName()) ) {
				
				logger.error("为节点：{}刷新线路关系时, To节点无法找到，toName is {}！", this.name, null == node ? null : node.getNodeName() );
				
				throw new RuntimeException(String.format( "为节点：{}刷新线路关系时, To节点无法找到，toName is {}！",this.name, null == node ? null : node.getNodeName()) );
			}
			
			transaction.setToNode(node);
			
			transaction.setFromNode(this);
		}
	}

	@Override
	public void registTransition(ActionTransition<T> transaction) {
		
		if( null == this.condition ) {
			
			this.condition = flowCreator.createInstance(ActionConditionSelector.class);
		}
		
		this.condition.addTransaction(transaction);
	}

	

	@Override
	public void before(T target, Map<String, Object> vars) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void after(T target, Map<String, Object> vars) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOpenLogger(Boolean hasOpenLogger) {
		this.hasOpenLogger = hasOpenLogger;
	}
}
