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
 * zyiyuan 下午3:08:57 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.executor.elements;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.ToString;
import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.business.executor.interfaces.ZxydkBusinessExecutor;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;

/**
 * @category 业务执行器的链接适配器
 * 
 */
@SuppressWarnings("all")
public class ZxydkBusinessExecutorElement< ORDER extends OrderBase , RESULT extends ResultBase > implements Serializable {

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 4826398744546158063L;
	
	/** 日志 */
	private Logger logger = LoggerFactory.getLogger(ZxydkBusinessExecutorElement.class);
	
	/** name of executor [ 执行器的执行代名词 ]  */
	private String serviceName;
	
	/** check order by JSR303... [ 验证check ] */
	private Class<?>[] validateGroup;
	
	/** entity of executor 【 执行器的执行领域模型 】 */
	private Class< ? extends AbstractEntity > entityType;
	
	/** memo of executor 【 执行器的描述 】 */
	private String actionMemo;
	
	/** transaction of executor 【 执行器是否开启大事务 默认不开 】 */
	private boolean isTransaction = Boolean.FALSE;
	
	/** SOA分布式系统结构， 模块描述 来至系统配置 */
	private String moduleName;
	
	/** 领域驱动工厂 */
	@Autowired
	protected DomainFactory domainFactory;
	
	/** 事件总线 */
	@Autowired
	protected EventBus eventBus;
	
	/** 执行的名字beanName */
	private String beanName;
	
	/** 执行器对象 */
	private ZxydkBusinessExecutor executor;
	

	/**
	 * @category default constructor 
	 */
	public ZxydkBusinessExecutorElement() {
		super();
	}


	public ZxydkBusinessExecutorElement(	String serviceName, Class<?>[] validateGroup,
										Class<? extends AbstractEntity> entityType,
										String actionMemo, boolean isTransaction, String moduleName,
										String beanName, ZxydkBusinessExecutor executor) {
		super();
		
		this.serviceName = serviceName;
		
		this.validateGroup = validateGroup;
		
		this.entityType = entityType;
		
		this.actionMemo = actionMemo;
		
		this.isTransaction = isTransaction;
		
		this.moduleName = moduleName;
		
		this.beanName = beanName;
		
		this.executor = executor;
	}
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Class<?>[] getValidateGroup() {
		return validateGroup;
	}

	public void setValidateGroup(Class<?>[] validateGroup) {
		this.validateGroup = validateGroup;
	}

	public Class<? extends AbstractEntity> getEntityType() {
		return entityType;
	}

	public void setEntityType(Class<? extends AbstractEntity> entityType) {
		this.entityType = entityType;
	}

	public String getActionMemo() {
		return actionMemo;
	}

	public void setActionMemo(String actionMemo) {
		this.actionMemo = actionMemo;
	}

	public boolean isTransaction() {
		return isTransaction;
	}

	public void setTransaction(boolean isTransaction) {
		this.isTransaction = isTransaction;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public DomainFactory getDomainFactory() {
		return domainFactory;
	}

	public void setDomainFactory(DomainFactory domainFactory) {
		this.domainFactory = domainFactory;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public ZxydkBusinessExecutor getExecutor() {
		return executor;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setExecutor(ZxydkBusinessExecutor executor) {
		this.executor = executor;
	}

	@Override
	public String toString() {
		return ToString.toString(this);
	}
	
}
