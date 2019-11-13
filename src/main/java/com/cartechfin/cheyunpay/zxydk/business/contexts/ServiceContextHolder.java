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
 * zyiyuan 下午4:19:49 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.contexts;

import java.io.Serializable;

import com.acooly.core.common.facade.ResultBase;

/**
 * @category 业务处理上下文的静态缓存 
 * 
 */
@SuppressWarnings("all")
public class ServiceContextHolder  implements Serializable {

	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -4231406570305457074L;
	
	/**
	 * 缓存 
	 */
	private static ThreadLocal<ServiceContext> threadLocal = new ThreadLocal<ServiceContext>();
	
	/**
	 * @category create  
	 * 
	 */
	public static < PARAM , RESULT extends ResultBase > ServiceContext< PARAM , RESULT >  get(){
		return (ServiceContext)threadLocal.get();
	}
	
	public static void set( ServiceContext serviceContext ) {
		threadLocal.set(serviceContext);
	}
	
	public static void clear() {
		threadLocal.remove();
	}
}
