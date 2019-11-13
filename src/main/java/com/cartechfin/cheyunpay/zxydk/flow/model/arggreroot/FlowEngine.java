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
 * zyiyuan 5:55:45 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;

/**
 * @category 流程处理核心聚合根 --- 流程引擎 
 * 
 */
public interface FlowEngine< T extends Object> extends ApplicationContextAware, InitializingBean, Serializable{
	
	
	/**
	 * @category 启动流程 
	 * 
	 * @param  object T  流程引擎的传递信息
	 * 
	 * @param  vals [Map<String, Object>] 流程引擎的传递扩展信息
	 * 
	 */
	public void start(  T object, Map<String,Object> vals );
	
	
	/**
	 * @category 流程引擎的执行 从某个节点执行
	 * 
	 * @param  nodeName  流程图中存在的某个节点
	 * 
	 * @param  object T  流程引擎的传递信息
	 * 
	 * @param  vals [Map<String, Object>] 流程引擎的传递扩展信息 
	 * 
	 */
	public void execute( String nodeName, T object, Map< String , Object > vals );
	
	
	/**
	 * @category 手动注册流程图
	 * 
	 *  @param actonFlow [ActionFlow<T>] 流程图
	 * 
	 */
	public void manualRegistActionFlow( ActionFlow<T> actionFlow );
	
	
	/**
	 *  @category 确认 流程引擎 
	 * 
	 *  @param ActionFlow.Key
	 * 
	 */
	public void confirmFlow( ActionFlow.Key key );
	
	/**
	 *  @category 获得 流程引擎 
	 * 
	 *  @param ActionFlow.Key
	 * 
	 */
	public ActionFlow.Key getUniqueKey();
	
}
