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
 * zyiyuan 下午3:26:48 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.containers.interfaces;

import java.io.Serializable;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.cartechfin.cheyunpay.zxydk.business.executor.interfaces.ZxydkBusinessExecutor;

/** 
 *  @category 系统业务执行容器
 * 
 */
public interface ZxydkBusinessContainer extends ApplicationContextAware , InitializingBean ,  Serializable {
	
	
	/**
	 *  container active core method 
	 *  
	 *  @category 业务执行容器的主要执行方法，业务的发起入口 
	 * 
	 *  @param ORDER 	【 ORDER extends OrderBase 】
	 *  
	 *  @param serviceName 【 String, 对应的执行器的代名词 】
	 *  
	 *  @return RESULT 	【 RESULT extends ResultBase 】
	 * 
	 */
	public < ORDER extends OrderBase , RESULT extends ResultBase > RESULT accept( final ORDER order , final String serviceName );
	
	
	/**
	 *  container register executor
	 * 
	 *  @category 业务执行容器的主要注册功能方法，用于注册容器内的所有执行器 
	 * 
	 *  @param executor  【 ZdkBusinessExecutor 执行器 】
	 *
	 * 
	 */
	public < ORDER extends OrderBase , RESULT extends ResultBase > void registerExecutorElement( ZxydkBusinessExecutor<ORDER, RESULT> executor );
}
