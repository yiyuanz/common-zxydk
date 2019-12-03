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
 * zyiyuan 3:49:29 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.entity.engine;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.cartechfin.cheyunpay.zxydk.common.enums.FlowEngineTypeEnum;
import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowEngine;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow.Key;

public class FlowEngineWapper<T extends Object> implements FlowEngine< T > {

	
	/** serialVersionUID */
	private static final long serialVersionUID = -176162057689415898L;


	private ApplicationContext context;
	
	
	private ActionFlow< T >  actionFlow;
	
	
	private FlowEngineTypeEnum engineType;
	
	
	public FlowEngineWapper(FlowEngineTypeEnum engineType) {
		super();
		this.engineType = engineType;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void manualRegistActionFlow(ActionFlow<T> actionFlow) {
		
		this.actionFlow = actionFlow;
	}

	@Override
	public void start(T object, Map<String, Object> vals) {
		
		this.actionFlow.action( object, vals );
		
	}

	@Override
	public void execute(String nodeName, T object, Map<String, Object> vals) {
		
		this.actionFlow.actionFromNode(nodeName, object, vals);
		
	}

	@Override
	public void confirmFlow(Key key) {
		
		if( null != key && key.equals( this.actionFlow.getUniqueKey() ) ) {
			return;
		}
		
		throw new RuntimeException(String.format( " confirm flow fail , key : %s !", key) );
	}

	@Override
	public ActionFlow.Key getUniqueKey() {
		
		return this.actionFlow.getUniqueKey();
	}

	@Override
	public ActionFlow getFlowInfo() {
		// TODO Auto-generated method stub
		return this.actionFlow;
	}

	@Override
	public FlowEngineTypeEnum getEngineType() {
		// TODO Auto-generated method stub
		return this.engineType;
	}
	
	
	
}
