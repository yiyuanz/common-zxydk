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
 * zyiyuan 上午11:23:47 创建
 */
package com.cartechfin.cheyunpay.zxydk.chain.interfaces;

import java.io.Serializable;
import java.util.Map;

import org.springframework.transaction.support.TransactionTemplate;

public interface CommandChain< R > extends   Serializable {
	
	/**
	 *  @category main method 
	 * 
	 *  @param R 传递信息
	 *  
	 *  @param vals 传递的扩展信息
	 * 
	 */
	public void execute( R object , Map<String, Object> vals );
	
	/**
	 *  @category pass command
	 * 
	 *  @param R 传递信息
	 *  
	 *  @param vals 传递的扩展信息
	 * 
	 */
	public void process( R object , Map<String, Object> vals );
	
	/**
	 *  @category creating chain with regist command , asyn set grade and transaction  and manual spring DI...
	 * 
	 *  @param Command<R> command 
	 */
	public void registCommand( Command<R> command );
	
	
	/**
	 * @category 设置 开启全局事务
	 * 
	 * <pre> 设置开启全局事务后，执行指令节点事务已失效 </pre>
	 * 
	 */
	public void  setTransactionSign( Boolean hasOpen );
	
	
	/**
	 * 设置事务模板 
	 */
	public void setTransactionTemplate( TransactionTemplate template );
	
}
