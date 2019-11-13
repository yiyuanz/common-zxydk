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
 * zyiyuan 2:36:27 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot;

import java.io.Serializable;
import java.util.Map;


/**
 * 
 *  @category  流程定义的的总元素
 *  
 * 
 */
public interface FlowBaseAction<T> extends Serializable {
	
	/**
	 * 
	 *  @category 流程每个元素的推进方法
	 * 
	 *  @param T traget 流程节点传递的信息
	 *   
	 *  @param vals [Map<String , Object> ] 流程节点传递的扩展信息
	 * 
	 */
	public void action( T target , Map<String , Object> vals );
	
	
}
