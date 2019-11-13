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
 * zyiyuan 下午4:11:10 创建
 */
package com.cartechfin.cheyunpay.zxydk.business.containers;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.dao.DataAccessException;

import com.acooly.core.common.facade.OrderBase;
import com.acooly.core.common.facade.ResultBase;
import com.acooly.core.utils.enums.ResultStatus;
import com.cartechfin.cheyunpay.zxydk.business.contexts.ServiceContext;
import com.cartechfin.cheyunpay.zxydk.business.contexts.ServiceContextHolder;
import com.cartechfin.cheyunpay.zxydk.business.executor.elements.ZxydkBusinessExecutorElement;
import com.cartechfin.cheyunpay.zxydk.common.transactions.ZxydkTransactionTemplate;


/**
 *  @category 系统业务执行容器实现抽象类 【 执行容器定义通用执行 】
 *  
 * 
 */
@SuppressWarnings("all")
public abstract class ZxydkBusinessInvokeContainer  extends ZxydkBusinessActiveContainer {

	/**
	 * serialVersionUID 
	 */
	private static final long serialVersionUID = -7294483126443364906L;

	@Override
	public <ORDER extends OrderBase, RESULT extends ResultBase> RESULT accept(	ORDER order, String serviceName) {
		
		ServiceContext<ORDER, RESULT> serviceContext = this.initServiceContext( order );

		try {
			ZxydkBusinessExecutorElement< ORDER, RESULT > element = this.queryExecutorByServiceName( serviceName );
			
			logger.info(" 收到请求： action = {} , order = {} 开始时间：{}！", element.getActionMemo() , order, new Date() );
			
			this.setDefaultResult( order, element );
			
			this.checkOrder( order, element.getValidateGroup() );
			
			doExecute(serviceContext);
			
			return serviceContext.getResult();
			
		}catch( IllegalArgumentException iex ) {
			
			logger.error(" order:{}.参数验证异常，业务失败，异常描述：{} ！", order, iex);
			
			ServiceContextHolder.get().getResult().setStatus(ResultStatus.failure);
			
			ServiceContextHolder.get().getResult().setCode(ResultStatus.failure.code());
			
			ServiceContextHolder.get().getResult().setDetail( iex.getMessage().length() < 255 ? iex.getMessage() : iex.getMessage().substring(0, 255) );
			
			return (RESULT) ServiceContextHolder.get().getResult();
			
		}catch( DataAccessException daex ) {
			
			logger.error( " order:{}.数据库异常，业务失败，异常描述：{} ！", order, daex );
			
			ServiceContextHolder.get().getResult().setStatus(ResultStatus.failure);
			
			ServiceContextHolder.get().getResult().setCode(ResultStatus.failure.code());
			
			ServiceContextHolder.get().getResult().setDetail( daex.getMessage().length() < 255 ? daex.getMessage() : daex.getMessage().substring(0, 255) );
			
			return (RESULT) ServiceContextHolder.get().getResult();
			
		}catch( RuntimeException rex ) {
			
			logger.error( " order:{}.发生业务未知异常，业务失败，异常描述：{} ！", order, rex );
			
			ServiceContextHolder.get().getResult().setStatus(ResultStatus.failure);
			
			ServiceContextHolder.get().getResult().setCode(ResultStatus.failure.code());
			
			ServiceContextHolder.get().getResult().setDetail( rex.getMessage().length() < 255 ? rex.getMessage() : rex.getMessage().substring(0, 255) );
			
			return (RESULT) ServiceContextHolder.get().getResult();
			
		}catch( Exception ex ) {
			
			logger.error( " order:{}.发生未知异常，业务失败，异常描述：{} ！", order, ex );
			
			ServiceContextHolder.get().getResult().setStatus(ResultStatus.failure);
			
			ServiceContextHolder.get().getResult().setCode(ResultStatus.failure.code());
			
			ServiceContextHolder.get().getResult().setDetail( ex.getMessage().length() < 255 ? ex.getMessage() : ex.getMessage().substring(0, 255) );	
			
			return (RESULT) ServiceContextHolder.get().getResult();
			
		}finally {
			logger.info("处理结果 order = {} , result = {}, 耗时 time = {}ms" , 
				order, ServiceContextHolder.get().getResult(), System.currentTimeMillis() - ServiceContextHolder.get().getBeginTime());
			/**  clear cache */
			ServiceContextHolder.clear();
		}
	}

	
	
	private <ORDER extends OrderBase, RESULT extends ResultBase>  void doExecute(ServiceContext<ORDER, RESULT> serviceContext) {
		
		if( serviceContext.getElement().isTransaction() ) {
			
			new ZxydkTransactionTemplate<Void>( this.transactionTemplate ) {
				// serialVersionUID
				private static final long serialVersionUID = 4687910696504983246L;
				
				@Override
				protected Void justDoIt() {
				
					serviceContext.getElement().getExecutor().invoke(serviceContext);
					
					ServiceContextHolder.set(serviceContext);
					
					return null;
				}
				
			}.template();
			
		}else {
			
			serviceContext.getElement().getExecutor().invoke(serviceContext);
		
			ServiceContextHolder.set(serviceContext);
		}
	}



	private < ORDER extends OrderBase >  void checkOrder(ORDER order, Class<?>... validateGroup) {
		
		if( null == order ) {
			
			throw new IllegalArgumentException( "order can not be null !" );
		}
		
		try {
			if( null != validateGroup && validateGroup.clone().length > 1 ) {
				// JSR303 分组校验
				order.checkWithGroup(validateGroup);
			}else {
				// 原生 JSR303 校验
				order.check();
			}
		} catch( Exception ex ) {
			
			logger.error("请求参数不完整：" + ex.getMessage() + order + ", 异常信息：" + ex);
			
			throw new IllegalArgumentException( "公共参数合法性校验检查未通过：" + ex.getMessage() );
		}
	}



	private <ORDER extends OrderBase, RESULT extends ResultBase> RESULT setDefaultResult(ORDER order, ZxydkBusinessExecutorElement<ORDER, RESULT> element) {
		
		RESULT result = (RESULT) element.getExecutor().initResult(order);
		
		ServiceContextHolder.get().setResult(result);
		
		return result;
	}



	private <ORDER extends OrderBase, RESULT extends ResultBase> ZxydkBusinessExecutorElement<ORDER, RESULT> queryExecutorByServiceName(String serviceName) {

		ZxydkBusinessExecutorElement< ORDER, RESULT > element = this.elements.get( serviceName );
		
		if( null == element || StringUtils.isBlank(element.getServiceName()) ) {
			
			throw new RuntimeException( String.format("container is finded error ,[%s] is find none now !", serviceName ) );
			
		}
		
		ServiceContextHolder.get().setElement(element);
		/** 初始化领域模型实例 */
		ServiceContextHolder.get().setEntity(this.domainFactory.newInstance(element.getEntityType()));
		
		return element;
	}



	private <ORDER extends OrderBase, RESULT extends ResultBase> ServiceContext<ORDER, RESULT> initServiceContext(ORDER order) {

		ServiceContext<ORDER, RESULT> serviceContext = new ServiceContext<ORDER, RESULT>(order);
		
		serviceContext.setStartTime( new Date() );
		
		serviceContext.setBeginTime( serviceContext.getStartTime().getTime() );
		
		ServiceContextHolder.set( serviceContext );
		
		return serviceContext;
	}
	
}
