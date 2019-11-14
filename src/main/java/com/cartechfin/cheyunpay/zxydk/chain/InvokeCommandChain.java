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
 * zyiyuan 上午11:56:15 创建
 */
package com.cartechfin.cheyunpay.zxydk.chain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cartechfin.cheyunpay.zxydk.chain.interfaces.Command;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.CommandChain;
import com.google.common.collect.Lists;

import io.jsonwebtoken.lang.Assert;

@SuppressWarnings("all")
public class InvokeCommandChain<R extends Object > implements CommandChain<R> {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -5304016649606654636L;
	// 日志
	protected static final Logger logger = LoggerFactory.getLogger(InvokeCommandChain.class);
	
	// command chain
	private Iterator<Command<R>> commands;
	
	// spring transaction template
	protected TransactionTemplate template;
	
	//  golbal transaction
	protected Boolean hasGlobalTransAction = Boolean.FALSE;
	

	@Override
	public void execute(R object, Map<String, Object> vals) {
		
		if( this.hasGlobalTransAction ) {
			
			Assert.notNull(this.template , " 无法发起责任链模式，因为开启了全局事务，事务模板不能为空！"); 
			
			this.template.execute(new TransactionCallback<Void>() {
				
				@Override
				public Void doInTransaction(TransactionStatus status) {
					
					process( object, vals );
					
					return null;
				}
			});
		}else {
			
			process( object, vals ); 
		}
	}

	@Override
	public void process(R object, Map<String, Object> vals) {
		
		if( this.commands.hasNext() ) {
			//  have a command
			Command<R> command = this.commands.next();
			
			if( command.match(object, vals) ) {
				// command match is true
				if( command.hasOpenTrAnsaction() ) {
					Assert.notNull(this.template , "责任链节点执行失败，无法发起执行，事务已开，事务模板为空！"); 
					// open single command transaction 
					this.template.execute(new TransactionCallback<Void>() {
						@Override
						public Void doInTransaction(TransactionStatus status) {
							command.execute(object, vals);
							return null;
						}
					});
					command.transmit(object, vals, this);
				}else {
					// close single command transaction
					command.execute(object, vals);
					command.transmit(object, vals, this);
				}
			}else {
				// command match is false  -> to next command
				this.process(object, vals);
			}
		}
	}

	@Override
	public void registCommand(Command<R> command) {
		
		List<Command<R>> temps = Lists.newArrayList();
		
		temps.add( command );
		
		if( null == this.commands ) {
			
			this.commands = temps.iterator();
			
		}else {
			
			while( this.commands.hasNext() ) {
				
				temps.add( this.commands.next() );
			}
			
			temps.sort((c1, c2) -> ( c1.compareTo(c2) ) );
			
			this.commands = temps.iterator();
		}
	}
	 
	@Override
	public void setTransactionSign(Boolean hasOpen) {
		// TODO Auto-generated method stub
		if( null == hasOpen ) {
			return; // 默认为false
		}
		this.hasGlobalTransAction = hasOpen;
	}

	@Override
	public void setTransactionTemplate(TransactionTemplate template) {
		
		this.template = template;
	}
}
