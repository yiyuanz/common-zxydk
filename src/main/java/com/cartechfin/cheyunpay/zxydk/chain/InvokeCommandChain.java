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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.chain.annotation.CommandOrder;
import com.cartechfin.cheyunpay.zxydk.chain.annotation.CommandTransAction;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.Command;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.CommandChain;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;
import com.google.common.collect.Lists;

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
	
	// spring context
	protected ApplicationContext context;
	
	// manual autowire to java Bean of factory
	protected AutowireCapableBeanFactory autowireBeanFactory;
	
	// DDD domain factory
	@Autowired
	protected DomainFactory domainFactory;
	
	// acooly event bus
	@Autowired
	protected EventBus eventBus;
	
	// spring transaction template
	@Autowired
	protected TransactionTemplate template;
	
	//  golbal transaction
	protected Boolean hasGlobalTransAction = Boolean.FALSE;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.context = applicationContext;
	
		this.autowireBeanFactory = this.context.getAutowireCapableBeanFactory();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(R object, Map<String, Object> vals) {
		
		if( this.hasGlobalTransAction ) {
			
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
					this.process(object, vals);
				}
			}else {
				// command match is false  -> to next command
				this.process(object, vals);
			}
		}
	}

	@Override
	public <T extends Command<R>> T createInstance(	Class<T> tclz, Boolean isOpenTransaction, int grade) {
		
		try {
			//获取有参构造
			Constructor<T> c = tclz.getConstructor();
			
			T t = c.newInstance();
			
			t.setOpenTransAction( isOpenTransaction );
			
			t.appoitOrder(grade);
			
			return t;
			
		} catch (NoSuchMethodException e) {
			
			logger.error("责任链构建指令时，未找到构建指令的构造器函数！class:{}!, ex:{}", tclz, e);
			
			throw new RuntimeException("责任链构建指令时，未找到构建指令的构造器函数!");
			
		} catch (SecurityException ex) {
			
			logger.error("责任链构建指令时，发生JDK的安全异常！class:{}!, ex:{}", tclz, ex);
			
			throw new RuntimeException("责任链构建指令时，发生JDK的安全异常！");
			
		}catch (InstantiationException iex) {
			
			logger.error("责任链构建指令时，发生发射失败异常！class:{}!, ex:{}", tclz, iex );
			
			throw new RuntimeException("责任链构建指令时，发生发射失败异常！");
			
		} catch (IllegalAccessException lex) {
			
			logger.error("责任链构建指令时，发生参数验证失败异常！class:{}!, ex:{}", tclz, lex );
			
			throw new RuntimeException("责任链构建指令时，发生参数验证失败异常！");
			
		} catch (IllegalArgumentException iaex) {
			
			logger.error("责任链构建指令时，发生参数转换失败异常！class:{}!, ex:{}", tclz, iaex );
			
			throw new RuntimeException("责任链构建指令时，发生参数转换失败异常！");
			
		} catch (InvocationTargetException itaex) {
			
			logger.error("责任链构建指令时，发生运行时参数异常！class:{}!, ex:{}", tclz, itaex );
			
			throw new RuntimeException("责任链构建指令时，发生运行时参数异常！");
			
		}catch( Exception allex ){
			
			logger.error("责任链构建指令时，发生未知业务异常！class:{}!, ex:{}", tclz, allex );
			
			throw new RuntimeException("责任链构建指令时，发生未知业务异常！");
		}
	}

	@Override
	public <T extends Command<R>> T createInstanceWithAnnotation(Class<T> tclz) {
		
		if( !tclz.isAnnotationPresent(CommandOrder.class) ) {
			
			logger.error("责任链构建指令时，使用默认构造器时，class:{} 缺失PayCommandOrder的定义！", tclz);
			
			throw new RuntimeException("责任链构建指令时，使用默认构造器时，缺失PayCommandOrder的定义");
		}
		
		CommandOrder payCommandOrder = tclz.getAnnotation(CommandOrder.class);
		
		if( !tclz.isAnnotationPresent(CommandTransAction.class) ) {
			
			logger.warn("责任链构建指令时，使用默认构造器时，class:{} 缺失PayCommandTransaction的定义，已被默认为关闭（false）！", tclz);
			
			return this.createInstance(tclz, Boolean.FALSE, payCommandOrder.order() ); 
			
		}else {
			
			CommandTransAction transaction = tclz.getAnnotation(CommandTransAction.class);
			
			return this.createInstance(tclz, transaction.isOpen(), payCommandOrder.order() ); 
		}
	}

	@Override
	public void registCommand(Command<R> command) {
		
		List<Command<R>> temps = Lists.newArrayList();
		
		this.autowireBeanFactory.autowireBeanProperties(command, 0, false);
		
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
	public void closeTransaction() {
		this.hasGlobalTransAction = Boolean.FALSE;
	}
}
