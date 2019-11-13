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
 * zyiyuan 上午11:30:30 创建
 */
package com.cartechfin.cheyunpay.zxydk.domain.repertory;

import java.io.Serializable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.support.TransactionTemplate;

import com.acooly.core.utils.ToString;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;

import lombok.Data;


/**
 *  @category 领域驱动的仓储能力的base基类
 * 
 */
@Data
public abstract class DomainBaseRepertory implements InitializingBean, ApplicationContextAware,Serializable {

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -5692090308593883992L;

	/** 领域启动工厂 */
	@Autowired
	private DomainFactory factory;
	
	/** Spring的处理上下文 */
	private ApplicationContext applicationContext;

	/** spring的事务模板 */
	@Autowired
	private TransactionTemplate transactionTemplate;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO ？ 特殊的初始化处理
		doSpringInit( applicationContext );
		
	}

	/**
	 *  @category 用于特殊的处理，有些仓储需要做特别的前置处理操作
	 * 
	 *  @param context [ Spring的ApplicationContext 处理上下文 ]
	 * 
	 */
	protected void doSpringInit(ApplicationContext context) {
		//TODO ?? 特殊处理
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
