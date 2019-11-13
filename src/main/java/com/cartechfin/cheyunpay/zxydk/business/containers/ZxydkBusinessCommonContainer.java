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
 * zyiyuan 下午5:36:21 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.containers;


/**
 *  @category 系统业务执行容器实现抽象类 【 执行容器定义构造器 】
 *  
 */
public class ZxydkBusinessCommonContainer extends ZxydkBusinessInvokeContainer {

	/**
	 * serialVersionUID 
	 * 
	 */
	private static final long serialVersionUID = 3447889134992896083L;

	
	
	public ZxydkBusinessCommonContainer( String moduleName ) {
		
		super();
		
		// TODO Auto-generated constructor stub
		super.moduleName = moduleName;
	}
	
	
}
