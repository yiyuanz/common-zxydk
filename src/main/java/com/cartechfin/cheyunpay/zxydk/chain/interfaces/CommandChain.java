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

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

public interface CommandChain< R > extends ApplicationContextAware, InitializingBean, Serializable {
	
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
	public <T extends Command<R>> T createInstance( Class<T> tclz , Boolean isOpenTransaction , int grade );
	
	
	/**
	 * @category create command instance with self annotation by classloader reflact
	 * 
	 * @param Class<T> tclz  
	 *  
	 * @return  T    class of command or command sub type ...
	 * 
	 */
	public <T extends Command<R>> T createInstanceWithAnnotation( Class<T> tclz );
	
	
	
	/**
	 *  @category creating chain with regist command , asyn set grade and transaction  and manual spring DI...
	 * 
	 *  @param Command<R> command 
	 */
	public void registCommand( Command<R> command );
	
	
	/**
	 * @category close transaction 
	 * 
	 */
	public void closeTransaction( );
	
}
