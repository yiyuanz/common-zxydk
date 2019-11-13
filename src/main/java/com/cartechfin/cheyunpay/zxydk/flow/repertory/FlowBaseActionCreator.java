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
 * zyiyuan 1:51:29 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.repertory;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.acooly.core.common.domain.AbstractEntity;
import com.cartechfin.cheyunpay.zxydk.domain.model.annotation.ZxydkEntity;
import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowBaseAction;

/**
 *  @category 流程引擎 - 元素创建器
 *  
 */
@SuppressWarnings("all")
public class FlowBaseActionCreator implements ApplicationContextAware, InitializingBean, Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2523188435374192572L;
	
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(FlowBaseActionCreator.class);
	
	private ApplicationContext context;
	
	private AutowireCapableBeanFactory autowireFactory;
	
	/**
	 * @category 流程引擎 - 元素创建器 创建元素实例 
	 * 
	 * @param clz [Class<T extends FlowBaseAction>]
	 * 
	 * @return T [<T extends FlowBaseAction>]
	 */
	public < T extends FlowBaseAction > T newInstance(String className){
		
		T t = null;
		
		try{
			
			Class<?> clz = Class.forName(className);
			
			Object o = clz.getConstructor().newInstance();
			
			if( o instanceof FlowBaseAction){
				
				t = (T)o;
				
				this.autowireFactory.autowireBeanProperties(t, 0, false);
				
				return t;
			}else {
				throw new RuntimeException("流程引擎创建器创建元素失败，【{}】不是对应的元素类型！");
			}
		}catch( Exception ex ){
			
			logger.error("流程引擎创建器创建元素:{}时，发生初始化异常，异常信息：{}！",className, ex);
			
			throw new RuntimeException( String.format("流程引擎创建器创建元素：%s,初始化异常！", className) );
		}
		
	}
	
	
	/**
	 * @category 流程引擎 - 元素创建器 创建元素实例 
	 * 
	 * @param clz [Class<T extends FlowBaseAction>]
	 * 
	 * @return T [<T extends FlowBaseAction>]
	 */
	public <T extends FlowBaseAction> T createInstance( Class<T> clz ){
		try {
			T t = clz.getConstructor().newInstance();
			
			this.autowireFactory.autowireBeanProperties(t, 0, false);
			
			return t;
		} catch (Exception e) {
			
			logger.error("流程引擎创建器创建元素:{}时，发生初始化异常，异常信息：{}！",clz, e);
			
			throw new RuntimeException( String.format("流程引擎创建器创建元素：%s,初始化异常！", clz) );
		} 
	}
	
	/**
	 *  @category 流程引擎 - 元素创建器 刷新元素实例 
	 * 
	 *  @param T 【元素实例】
	 * 
	 *  @return T [刷新后的元素实例]
	 */
	public <T extends FlowBaseAction> T reflush( T t ) {
		try {
			
			Class<?> tclz = t.getClass();
			
			if( !tclz.isAnnotationPresent(ZxydkEntity.class) ) {
				
				return t;
			}
			
			this.autowireFactory.autowireBeanProperties(t, 0, false);
			
			return t;
			
		}catch( Exception ex ) {
			
			logger.error("流程引擎创建器刷新元素：{}（手动注入bean）的过程时，发生未知异常：{}！", t.getClass(), ex);
			
			throw new RuntimeException("流程引擎创建器刷新元素时，发生未知异常！");
		}
	}
	

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
		
		this.autowireFactory = this.context.getAutowireCapableBeanFactory();
	}
	
}
