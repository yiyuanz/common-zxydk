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
 * zyiyuan 上午10:55:10 创建
 */
package com.cartechfin.cheyunpay.zxydk.domain.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.acooly.core.common.domain.AbstractEntity;
import com.acooly.core.utils.ToString;
import com.acooly.module.event.EventBus;
import com.beust.jcommander.internal.Maps;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;
import com.cartechfin.cheyunpay.zxydk.domain.model.annotation.ZxydkEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@SuppressWarnings("all")
@ZxydkEntity
public abstract class DomainBaseModel extends AbstractEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2855500316702827443L;
	
	/** 事件分发机制 */
	@Autowired
	private EventBus eventBus;
	
	/** 领域驱动工厂 */
	@Autowired
	private DomainFactory domainFactory;
	
	
	/** 扩展信息 Map */
	private Map<String , Object> exts = Maps.newHashMap();
	
	
	/**
	 *  @category 转换到 TO 中去
	 *  
	 *  @param TO 【 <TO extends Object> 】
	 *  
	 */
	public <TO extends Object>  void convertTo(TO to) {
		
	}
	
	/**
	 *  @category 转换到 FROM 中去
	 *  
	 *  @param from 【 <FROM extends Object> 】
	 *  
	 */
	public <FROM extends Object> void convertFrom( FROM from ) {
		
	}
	
	/**
	 *  @category 添加扩展信息
	 * 
	 *  @param key
	 *  
	 *  @param value
	 *  
	 */
	public <VALUE extends Object> void addExts( String key , VALUE value ) {
		if( null == exts ) {
			
			exts = Maps.newHashMap();
		}
		
		exts.put(key, value);
	}
	
	/**
	 * @category 获取扩展信息的值 
	 * 
	 * @param key
	 * 
	 * @return object
	 * 
	 */
	public Object getExts( String key ) {
		
		return this.exts.get(key);
	}
	
	@Override
	public String toString() {
		return ToString.toString(this);
	}
}
