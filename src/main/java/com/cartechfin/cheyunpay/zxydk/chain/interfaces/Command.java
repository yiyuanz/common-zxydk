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
 * zyiyuan 上午11:26:07 创建
 */
package com.cartechfin.cheyunpay.zxydk.chain.interfaces;

import java.io.Serializable;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

public interface Command<R extends Object> extends Comparable<Ordered>, Ordered, Serializable {
	
	/**
	 * @category match executeAble  
	 * 
	 * @param R 传递信息
	 * 
	 * @param vals 传递扩展信息
	 * 
	 * @return Boolean (TRUE : able | false : disable )
	 * 
	 */
	public Boolean match( R object , Map<String, Object> vals);
	
	
	/**
	 * @category main method  do business....
	 * 
	 * @param R 传递信息
	 * 
	 * @param vals 传递扩展信息
	 *  
	 */
	public void execute( R object , Map<String , Object> vals );
	
	/**
	 *  @category pass command with chain....
	 * 
	 *  @param  R 传递信息
	 *  
	 *  @param  vals 传递扩展信息
	 *  
	 *  @param chain 当前该指令所在的链路
	 * 
	 */
	public void transmit( R object , Map<String, Object> vals, CommandChain<R> chain );
	
	/**
	 *  @category is open transaction of single command
	 * 
	 *  @return  Boolean (True : isOpened | false : isClosed)
	 * 
	 */
	public Boolean hasOpenTrAnsaction();
	
	/**
	 * @category set open transaction value of boolean type
	 * 
	 *  @param Boolean ( Boolean.TRUE : Open | Boolean.FALSE: CLOSE )
	 */
	public void setOpenTransAction( Boolean hasOpen );
	
	
	/**
	 * @category appoit order of asc  (1 > 10)
	 * 
	 *  @param grade (order grade of integer type)
	 *   
	 */
	public void appoitOrder( int grade );
	
	
	
	
	
}
