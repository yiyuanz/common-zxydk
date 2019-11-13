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
 * zyiyuan 上午10:55:10 创建
 */
package com.cartechfin.cheyunpay.zxydk.domain.factory;

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

import lombok.Data;



/**
 * @category 领域驱动工厂
 * 
 */
@Data
public class DomainFactory implements ApplicationContextAware , InitializingBean , Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -5219285222663001983L;
	
	/** 日志 */
	public static Logger logger = LoggerFactory.getLogger(DomainFactory.class);
	
	/** spring处理上下文 */
	private ApplicationContext context;
	
	/** spring手动注入bean工厂 */
	private AutowireCapableBeanFactory autowireFactory;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO ?? 有些自定义的工厂前置设计，后续项目只要继承即可
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
		
		this.autowireFactory = applicationContext.getAutowireCapableBeanFactory();
	}
	
	/**
	 * @category 构建领域模型中的实例 
	 * 
	 * @param clz [Class<T extends AbstractEntity>]
	 * 
	 * @return T [<T extends AbstractEntity>]
	 */
	public <T extends AbstractEntity> T newInstance( Class<T> clz ){
		try {
			T t = clz.getConstructor().newInstance();
			
			if( clz.isAnnotationPresent(ZxydkEntity.class) ) {
				
				ZxydkEntity  zdkEntity = clz.getAnnotation(ZxydkEntity.class);
				
				if( null != zdkEntity && zdkEntity.isOpenAutowired() ) {
					
					this.autowireFactory.autowireBeanProperties(t, 0, false);
				}
			}
			return t;
		} catch (Exception e) {
			
			logger.error("领域驱动工厂实例化领域模型:{}时，发生初始化异常，异常信息：{}！",clz, e);
			
			throw new RuntimeException( String.format("领域模型：%s,初始化异常！", clz) );
		} 
	}
	
	/**
	 *  @category 使用spring的手动刷新工厂 刷新领域模型
	 * 
	 *  @param T 【领域模型的实例】
	 * 
	 *  @return T [刷新后的实例]
	 */
	public <T extends AbstractEntity> T reflush( T t ) {
		try {
			
			Class<?> tclz = t.getClass();
			
			if( !tclz.isAnnotationPresent(ZxydkEntity.class) ) {
				
				return t;
			}
			
			ZxydkEntity zdkEntity = tclz.getAnnotation(ZxydkEntity.class);
			
			if( !zdkEntity.isOpenAutowired() ) {
				
				return t;
			}
			
			this.autowireFactory.autowireBeanProperties(t, 0, false);
			
			return t;
			
		}catch( Exception ex ) {
			
			logger.error("领域工厂刷新领域模型：{}（手动注入bean）的过程时，发生未知异常：{}！", t.getClass(), ex);
			
			throw new RuntimeException("领域工厂刷新领域模型时，发生未知异常！");
		}
	}
	
}
