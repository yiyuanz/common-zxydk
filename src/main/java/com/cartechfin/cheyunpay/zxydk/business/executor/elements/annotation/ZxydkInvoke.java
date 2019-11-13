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
 * zyiyuan 下午2:58:35 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.executor.elements.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

import com.acooly.core.common.domain.AbstractEntity;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface ZxydkInvoke {
	
	/** name of executor [ 执行器的执行代名词 ]  */
	public String serviceName();
	
	/** check order by JSR303... [ 验证check ] */
	public Class<?>[] validateGroup() default {};
	
	/** entity of executor 【 执行器的执行领域模型 】 */
	public Class< ? extends AbstractEntity > entityType();
	
	/** memo of executor 【 执行器的描述 】 */
	public String actionMemo();
	
	/** transaction of executor 【 执行器是否开启大事务 默认不开 】 */
	public boolean isTransaction() default false;
}
