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
 * zyiyuan 11:59:02 AM 创建
 */
package com.cartechfin.cheyunpay.zxydk.chain.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.support.TransactionTemplate;

import com.cartechfin.cheyunpay.zxydk.chain.InvokeCommandChain;
import com.cartechfin.cheyunpay.zxydk.chain.annotation.CommandOrder;
import com.cartechfin.cheyunpay.zxydk.chain.annotation.CommandTransAction;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.Command;
import com.cartechfin.cheyunpay.zxydk.chain.interfaces.CommandChain;

public class CommandChainFactory implements ApplicationContextAware , InitializingBean,  Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 916179000318970124L;
	
	// 日志
	private static Logger logger = LoggerFactory.getLogger(CommandChainFactory.class);
	
	
	private ApplicationContext context;
	
	
	private AutowireCapableBeanFactory autoWireFactory;
	
	@Autowired
	private TransactionTemplate template;
	
	/**
	 * @category create command instance by calssloader reflact
	 * 
	 * @param Class<T> tclz  class of command or command sub type ...  
	 *  
	 * @param Boolean isOpenTransaction  transaction param
	 * 
	 * @param int grade  command order by grade asc
	 * 
	 * @return  T    class of command or command sub type ...
	 * 
	 */
	public <T extends Command > T createInstance(	Class<T> tclz, Boolean isOpenTransaction, int grade) {
		
		try {
			//获取有参构造
			Constructor<T> c = tclz.getConstructor();
			
			T t = c.newInstance();
			
			t.setOpenTransAction( isOpenTransaction );
			
			t.appoitOrder(grade);
			
			autoWireFactory.autowireBeanProperties(t, 0, false);
			
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
	
	
	/**
	 * @category create command instance with self annotation by classloader reflact
	 * <pre> 配合自定义的注解 联合使用 </pre>
	 * 
	 * 
	 * @param Class<T> tclz  
	 *  
	 * @return  T    class of command or command sub type ...
	 * 
	 */
	public < T extends Command > T createInstanceWithAnnotation(Class<T> tclz) {
		
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

	/**
	 * @category 创建责任链 - 关闭全局事务 
	 */
	public CommandChain newChainInstance() {
		
		CommandChain chain = new InvokeCommandChain();
		
		chain.setTransactionTemplate(template);
		
		return chain;
	}
	
	/**
	 * @category 创建责任链 - 开启全局事务 
	 */
	public CommandChain newChainInstranceWithTransaction() {
		
		CommandChain chain = this.newChainInstance();
		
		chain.setTransactionSign(Boolean.TRUE);
		
		return chain;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context = applicationContext;
		
		this.autoWireFactory = this.context.getAutowireCapableBeanFactory();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
