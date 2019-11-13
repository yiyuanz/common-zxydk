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
 * zyiyuan 上午11:47:01 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.contexts;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.ToString;
import com.beust.jcommander.internal.Maps;
import com.cartechfin.cheyunpay.zxydk.business.executor.elements.ZxydkBusinessExecutorElement;

import io.jsonwebtoken.lang.Assert;


@SuppressWarnings("all")
public class ServiceContext<PARAM , RESULT extends ResultBase> implements Serializable {
	
	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -7006967172343456025L;
	
	/** 日志 */
	protected static final Logger logger = LoggerFactory.getLogger(ServiceContext.class);
	
	/** 开始时间 */
	private Date startTime = new Date();
	
	/** 开始时间（请求时间） */
	private long beginTime;

	/**  请求的订单信息  */
	private PARAM param;
	
	/**  返回的结果信息  */
	private RESULT result;

	/** 扩展信息 */
	private Map<String , Object> attributes = Maps.newHashMap();
	
	/**  实体对象信息  */
	private AbstractEntity entity;
	
	/**  幂等操作标识位 TRUE:幂等验证通过 | FALSE:幂等验证失败  */
	private Boolean  isSuccessIdepotent = Boolean.TRUE;
	
	/** 业务执行器 */
	private ZxydkBusinessExecutorElement element;

	/**
	 * @category   Constructor 
	 * 
	 * @param PARAM 	【 PARAM 】
	 * 
	 */
	public ServiceContext(PARAM param) {
		super();

		this.param = param;
	}

	/**
	 * @category   Constructor 
	 * 
	 * @param  param 	【 PARAM 】
	 * 
	 * @param  result 	【 RESULT 】
	 * 
	 */
	public ServiceContext(PARAM param, RESULT result) {
		super();
		this.param = param;
		this.result = result;
	}
	
	
	public < VALUE extends Object > void addAttributes( String key , VALUE value ) {
		
		Assert.notNull( key , " add serviceContext key is null!" );
		
		Assert.notNull( value , "add serviceContext value is null!" );
		
		if( null == this.attributes ) {
			this.attributes = Maps.newHashMap();
		}
		
		this.attributes.put( key, value );
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public PARAM getParam() {
		return param;
	}

	public void setParam(PARAM param) {
		this.param = param;
	}

	public RESULT getResult() {
		return result;
	}

	public void setResult(RESULT result) {
		this.result = result;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public < T extends AbstractEntity >  T getEntity() {
		return (T) entity;
	}

	public < T extends AbstractEntity > void setEntity(T entity) {
		this.entity = entity;
	}

	public Boolean getIsSuccessIdepotent() {
		return isSuccessIdepotent;
	}

	public void setIsSuccessIdepotent(Boolean isSuccessIdepotent) {
		this.isSuccessIdepotent = isSuccessIdepotent;
	}

	public ZxydkBusinessExecutorElement getElement() {
		return element;
	}

	public void setElement(ZxydkBusinessExecutorElement element) {
		this.element = element;
	}

	/**
	 * @return String 
	 */
	@Override
	public String toString() {
		return ToString.toString( this );
	}
}

