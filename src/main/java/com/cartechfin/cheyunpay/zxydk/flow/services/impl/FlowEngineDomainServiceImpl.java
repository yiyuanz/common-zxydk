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
 * zyiyuan 5:01:39 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.services.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;
import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowEngine;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.engine.FlowEngineWapper;
import com.cartechfin.cheyunpay.zxydk.flow.repertory.FlowBaseActionCreator;
import com.cartechfin.cheyunpay.zxydk.flow.repertory.FlowDocumentReader;
import com.cartechfin.cheyunpay.zxydk.flow.services.FlowEngineDomainService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("all")
public class FlowEngineDomainServiceImpl<T extends Object> extends PathMatchingResourcePatternResolver implements FlowEngineDomainService<T> {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8916292066481798925L;
	
	// 日志
	private static Logger logger = LoggerFactory.getLogger(FlowEngineDomainServiceImpl.class);
	
	@Autowired
	private FlowBaseActionCreator flowBaseActionCreator;
	
	/** 流程引擎 */
	private Map< ActionFlow.Key , FlowEngine > engines = Maps.newHashMap();
	
	@Autowired
	private FlowDocumentReader flowDocumentReader;
	
	
	private AutowireCapableBeanFactory capableBeanFactory;
	
	/** 静态的流程文件 */
	private List<Resource> resources = Lists.newArrayList();

	private ApplicationContext applicationContext;
	
	@Autowired
	private DomainFactory domainFactory;
	
	@Autowired
	private EventBus eventBus;
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
		this.capableBeanFactory = this.applicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		if( null == this.resources || 0 == this.resources.size() ) {
			
			return;
		}
		
		logger.info("********************************** load flow file begin now! ********************************************************************");
		
		for( Resource res : this.resources ) {
			
			logger.info("******************************************************load flow file is {} !****************************************************", res.getFilename() );
		
			ActionFlow flow = flowDocumentReader.readFlowFile(res);
			
			FlowEngine flowEngine = new FlowEngineWapper();
			
			flowEngine.manualRegistActionFlow( flow );
			
			this.capableBeanFactory.autowireBeanProperties(flowEngine, 0, false);
			
			manualAddFlowEngine( flowEngine );
		}
	}
	
	public void manualAddFlowEngine( FlowEngine flowEngine ) {
		
		if( null == this.engines ) {
			
			this.engines = Maps.newHashMap();
		}
		
		this.engines.put(flowEngine.getUniqueKey(), flowEngine);
	}
	
	
	/**
	 * @category 填充 
	 * 
	 */
	public void intoUrlPartten( String urlPartten ) {
		
		try {
			
			String[] urls = urlPartten.split(";");
			
			for( String url : urls ) {
				
				Resource[] rs = super.getResources( url );
				
				if( null == this.resources ) {
					
					this.resources = Lists.newArrayList();
				}
				
				Lists.newArrayList(rs).forEach( (r) ->  this.resources.add(r) ); 
				
			}
		}catch (Exception e) {
			
			logger.error("urlPartten is illegaled , ex is :{}!" , e); 
			
			throw new RuntimeException( String.format("%s urlPartten is illegaled , please check it now !", urlPartten) );
		}
	}

	@Override
	public void start(String flowName, String flowVersion, T target, Map<String , Object> vals) {
		
		FlowEngine engine = obtainFlowEngine( new ActionFlow.Key(flowName, flowVersion) );

		engine.start( target, vals);
	}

	@Override
	public void execute(String flowName, String flowVersion, String nodeName, T target,
						Map<String , Object> vals) {

		FlowEngine engine = obtainFlowEngine( new ActionFlow.Key(flowName, flowVersion) );
		
		engine.execute(nodeName, target, vals);
	}
	
	
	private FlowEngine obtainFlowEngine( ActionFlow.Key key ) {
		
		FlowEngine engine = this.engines.get(key);
		
		if( null == engine ) {
			
			throw new RuntimeException("流程引擎搜索失败，未找到需要执行得流程图！");
		}
		
		engine.confirmFlow(key);
		
		return engine;
		
	}

	@Override
	public void manualRegistFlowResource(Resource res) {
		// TODO 等待 可视化的 流程图 的注入。 动态执行的 ？？
	}
	
}
