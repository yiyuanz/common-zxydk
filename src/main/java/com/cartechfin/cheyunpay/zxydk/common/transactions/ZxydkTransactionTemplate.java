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
 * zyiyuan 下午5:18:55 创建
 */
package com.cartechfin.cheyunpay.zxydk.common.transactions;

import java.io.Serializable;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public abstract class ZxydkTransactionTemplate< T extends Object> implements Serializable  {

	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = 8865366226153650051L;
	
	/** Spring 事务模板 */
	private TransactionTemplate transactionTemplate;

	
	/**
	 * @category contructor 
	 * 
	 */
	public ZxydkTransactionTemplate(TransactionTemplate transactionTemplate) {
		
		super();
		
		this.transactionTemplate = transactionTemplate;
	}

	
	public T template() {
		return this.transactionTemplate.execute(new TransactionCallback<T>() {

			@Override
			public T doInTransaction(TransactionStatus status) {
				try {
					
					return justDoIt();
					
				}catch( Exception exc ) {
					
					status.setRollbackOnly();
					
					throw exc;
				}
			}
		});
	}
	
	protected abstract T justDoIt();

}
