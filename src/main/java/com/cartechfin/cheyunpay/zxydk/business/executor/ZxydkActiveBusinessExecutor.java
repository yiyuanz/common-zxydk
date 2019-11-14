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
 * zyiyuan 下午2:07:22 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.executor;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.ToString;
import com.acooly.core.utils.mapper.BeanCopier;
import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.business.contexts.ServiceContext;
import com.cartechfin.cheyunpay.zxydk.business.executor.interfaces.ZxydkBusinessExecutor;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;

import lombok.Data;

@Data
@SuppressWarnings("all")
public abstract class ZxydkActiveBusinessExecutor< ORDER extends OrderBase , RESULT extends ResultBase > implements  ZxydkBusinessExecutor<ORDER, RESULT>{

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 4372387421172722618L;

	/** spring的注册时的beanName */
	protected String processorName;
	
	/** 日志 */
	protected Logger logger = LoggerFactory.getLogger(ZxydkActiveBusinessExecutor.class);
	
	/** 领域驱动工厂 */
	@Autowired
	protected DomainFactory domainFactory;
	
	/** 事件总线 */
	@Autowired
	protected EventBus eventBus;
	
	/** 处理的结果集合 */
	protected RESULT result;
	
	/** 处理的结果集合的class */
	protected Class<RESULT> resultClz;
	
	/**  Spring的处理上下文  */
	protected ApplicationContext applicationContext;
	
	
	@Override
	public void setBeanName(String name) {
		
		this.processorName = name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		Class<RESULT> resultClz = (Class<RESULT>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		
		this.resultClz = resultClz;
		
		logger.info("【{}】执行器的返回结果result对象构建完成！ resultClass : {}", this.getBeanName(), this.resultClz ); 	
	}

	@Override
	public RESULT initResult(ORDER order) {
		try {
			this.result = this.resultClz.newInstance();
			
			BeanCopier.copy(order, result);
			
		}catch( Exception ex ) {
			
			logger.error("【{}】执行器的result的resultClass【{}】 初始化 失败！原因：{}", this.getBeanName(), this.resultClz, ex);
			
			throw new RuntimeException( String.format("【{%s}】result 初始化失败！", this.resultClz) );
		}
		
		return this.result;
	}

	@Override
	public String getBeanName() {

		return this.processorName;
	}

	
	@Override
	public void invoke(ServiceContext<ORDER, RESULT> serviceContext) {
		
		logger.info("【{}】执行器开始执行业务, 开始时间：{}!", this.getBeanName() , serviceContext.getStartTime() );
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
