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
 * zyiyuan 4:59:00 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.services;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;


/**
 * @category 
 * 
 */
public interface FlowEngineDomainService< T > extends ApplicationContextAware , InitializingBean , Serializable {
	
	/**
	 * @category 启动流程引擎
	 * 
	 *  @param flowName  流程标识
	 *  
	 *  @param flowVersion 流程版本号
	 *  
	 *  @param target     流程传递信息
	 *  
	 *  @param vals       流程传递的扩展信息
	 * 
	 */
	public  void start( String flowName, String flowVersion , T  target , Map<String, Object> vals);
	
	/**
	 * @category 执行流程引擎
	 * 
	 *  @param flowName  流程标识
	 *  
	 *  @param flowVersion 流程版本号
	 *  
	 *  @param nodeName   流程中的某个节点
	 *  
	 *  @param target     流程传递信息
	 *  
	 *  @param vals       流程传递的扩展信息
	 * 
	 */
	public void execute( String flowName, String flowVersion ,  String nodeName, T  target , Map<String, Object> vals);
	
	
	
	/**
	 * @category 手动 动态注册 流程图
	 * 
	 *  @param Resource   流程图资源
	 * 
	 */
	public void manualRegistFlowResource( Resource res );
	
	/**
	 * @category 手动设置 流程图的路径
	 * 
	 *  @param urlPartten   路径
	 * 
	 */
	public void intoUrlPartten( String urlPartten );
	
	
	
	
	
	
}
