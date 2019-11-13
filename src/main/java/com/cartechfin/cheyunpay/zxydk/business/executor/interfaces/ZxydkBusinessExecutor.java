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
 * zyiyuan 下午1:59:21 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.executor.interfaces;

import java.io.Serializable;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.cartechfin.cheyunpay.zxydk.business.contexts.ServiceContext;

/**
 * @category 通用执行器的接口 
 * 
 */
public interface ZxydkBusinessExecutor< ORDER extends OrderBase , RESULT extends ResultBase > extends BeanNameAware , Serializable , ApplicationContextAware, InitializingBean {
	
	
	/**
	 *  @category 初始化 结果集
	 * 
	 *  @param  ORDER  【 ORDER extends OrderBase 】
	 *  
	 *  @return RESULT 【RESULT extends ResultBase】
	 *  
	 */
	public RESULT initResult( ORDER order );
	
	
	/**
	 *  @category 执行业务
	 *  
	 *  @param  serviceContext 【 ServiceContext< ORDER , RESULT > 】
	 *  
	 */
	public void invoke( ServiceContext< ORDER , RESULT > serviceContext );
	
	/**
	 *  @category 执行器 的spring的注册名
	 */
	public String getBeanName();

}
