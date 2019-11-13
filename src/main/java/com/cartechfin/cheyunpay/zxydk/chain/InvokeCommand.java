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
 * zyiyuan 上午11:36:59 创建
 */
package com.cartechfin.cheyunpay.zxydk.chain;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.Command;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.CommandChain;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;

import lombok.Data;

@Data
@SuppressWarnings("all")
public abstract class InvokeCommand<R extends Object> implements Command<R> {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 8801812560044504414L;
	
	// 日志
	protected static final Logger logger = LoggerFactory.getLogger(InvokeCommand.class);
	
	// spring context
	protected ApplicationContext context;
	
	// manual into Object of javaBean
	protected AutowireCapableBeanFactory autoBeanFactory;
	
	// DDD domainFactory
	@Autowired
	protected DomainFactory domainFactory;
	
	// eventBus
	@Autowired
	protected EventBus eventBus;
	
	// transaction
	private Boolean hasOpenTransaction = Boolean.FALSE;
	
	// order by grade of command execute 
	private int orderGrade;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
		
		this.autoBeanFactory = this.context.getAutowireCapableBeanFactory();
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	@Override
	public void transmit(R object, Map<String, Object> vals, CommandChain<R> chain) {
		chain.process(object, vals);
	}

	@Override
	public int compareTo(Ordered o) {
		return this.getOrder() - o.getOrder();
	}

	@Override
	public int getOrder() {
		return this.orderGrade;
	}

	@Override
	public Boolean match(R object, Map<String, Object> vals) {
		return Boolean.TRUE; // default to do execute 
	}

	@Override
	public Boolean hasOpenTrAnsaction() {
		return this.hasOpenTransaction;
	}

	@Override
	public void setOpenTransAction(Boolean hasOpen) {
		this.hasOpenTransaction = hasOpen;
	}

	@Override
	public void appoitOrder(int grade) {
		this.orderGrade = grade;
	}
	
}
