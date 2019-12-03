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
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.utils.Assert;
import com.acooly.module.event.EventBus;
import com.cartechfin.cheyunpay.zxydk.business.executor.interfaces.ZxydkBusinessExecutor;
import com.cartechfin.cheyunpay.zxydk.common.enums.FlowEngineTypeEnum;
import com.cartechfin.cheyunpay.zxydk.domain.factory.DomainFactory;
import com.cartechfin.cheyunpay.zxydk.flow.annotation.FlowChat;
import com.cartechfin.cheyunpay.zxydk.flow.annotation.FlowNode;
import com.cartechfin.cheyunpay.zxydk.flow.annotation.impl.FlowChatHandle;
import com.cartechfin.cheyunpay.zxydk.flow.annotation.impl.FlowNodeHandle;
import com.cartechfin.cheyunpay.zxydk.flow.model.arggreroot.FlowEngine;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow.Key;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlowChart;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.FlowActionNode;
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

	public FlowEngineDomainServiceImpl( String urlPattens ) {

		super();
		
		this.intoUrlPartten( urlPattens );
	}

	public FlowEngineDomainServiceImpl() {
		super();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
		this.applicationContext = applicationContext;
		
		this.capableBeanFactory = this.applicationContext.getAutowireCapableBeanFactory();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		/** 1. 通过xml流程文件，加载流程 */
		loadFlowXmlResources();
		/** 2. 通用@annotation加载流程  annotation 不能和 xml文件交叉*/
		loadFlowAnnotation();
	}
	
	private void loadFlowAnnotation() {
		logger.info("********************************** load flow node begin now! ********************************************************************");
		Map<String , FlowActionNode> flowActionNodes = this.applicationContext.getBeansOfType(FlowActionNode.class, false, true);
		if( null == flowActionNodes || 0 == flowActionNodes.size() ) {
			logger.info("********************************** load flow node  has no node end now! ********************************************************************");
			return;
		}
		for( Entry<String, FlowActionNode> nodes : flowActionNodes.entrySet() ) {
			FlowActionNode node = nodes.getValue();
			if( !node.getClass().isAnnotationPresent(FlowChat.class) ) {
				throw new BusinessException( String.format(" load flow by annotation error , node:%s,must has @FlowChat!" , node.getClass().getName() ) );
			}
			if( !node.getClass().isAnnotationPresent(FlowNode.class) ) {
				throw new BusinessException( String.format("load flow by annotation error , node :%s , must has @FlowNode!", node.getClass().getName()) );
			}
			FlowChat annotationChat = node.getClass().getAnnotation(FlowChat.class);
			FlowNode flowNode = node.getClass().getAnnotation(FlowNode.class);
			FlowChatHandle chatHandle = new FlowChatHandle(annotationChat.name(), annotationChat.version(), annotationChat.hasOpenLogger(), annotationChat.hasTransaction() ); 
			FlowEngine engine = this.engines.get( chatHandle.actionFlowKey() ); 
			if( null == engine ) {
				/** 新流程 */ 
				engine = new FlowEngineWapper( FlowEngineTypeEnum.ANNOTATION );
				this.capableBeanFactory.autowireBeanProperties( engine, 0, false );
				// 流程图总定义节点
				ActionFlow flow = chatHandle.fillIntoFlowBaseInfo( this.flowBaseActionCreator );
				FlowNodeHandle nodeHandle = new FlowNodeHandle( flowNode.name(), flow.getHasOpenTransaction()? Boolean.FALSE : flowNode.hasTransaction(), flowNode.conditions() );
				flow.addActionNode( nodeHandle.createActionNode( flow , node , this.flowBaseActionCreator) );
				engine.manualRegistActionFlow( flow );
				manualAddFlowEngine( engine );
			}else {
				/** 老流程 */ 
				Assert.isTrue( FlowEngineTypeEnum.ANNOTATION == engine.getEngineType(), "annotation flow create error , flowengine type must by annotation !" ); 
				Assert.isTrue( engine.getFlowInfo().getHasOpenLogger().equals(chatHandle.getHasOpenLogger()) , String.format( " annotation flow create error , node : %s , openlogger is diffrent now! ", node.getClass().getName()) );
				Assert.isTrue(engine.getFlowInfo().getHasOpenTransaction().equals(chatHandle.getHasTransaction()) , String.format( " annotation flow create error , node : %s , hastransaction is diffrent now! ", node.getClass().getName()) );
				FlowNodeHandle nodeHandle = new FlowNodeHandle( flowNode.name(), engine.getFlowInfo().getHasOpenTransaction()? Boolean.FALSE : flowNode.hasTransaction(), flowNode.conditions() );
				engine.getFlowInfo().addActionNode( nodeHandle.createActionNode( engine.getFlowInfo() , node , this.flowBaseActionCreator) );
			}
		}
		for( Entry<Key, FlowEngine> es : this.engines.entrySet() ) {
			if( es.getValue().getEngineType() == FlowEngineTypeEnum.XML ) {
				continue;
			}
			es.getValue().getFlowInfo().reflushTransaction();
		}
		logger.info("**********************************  load flow node end now! ********************************************************************");
	}

	
	private void loadFlowXmlResources() {
		logger.info("********************************** load flow file begin now! ********************************************************************");
		
		if( null == this.resources || 0 == this.resources.size() ) {
			
			logger.info("********************************** load no file end now! ********************************************************************");
			
			return;
		}
		
		for( Resource res : this.resources ) {
			
			logger.info("******************************************************load flow file is {} !****************************************************", res.getFilename() );
		
			ActionFlow flow = flowDocumentReader.readFlowFile(res);
			
			FlowEngine flowEngine = new FlowEngineWapper( FlowEngineTypeEnum.XML );
			
			this.capableBeanFactory.autowireBeanProperties(flowEngine, 0, false);
			
			this.capableBeanFactory.autowireBeanProperties(flow, 0, false);
			
			flowEngine.manualRegistActionFlow( flow );
			
			manualAddFlowEngine( flowEngine );
		}
		
		logger.info("********************************** load  files end now! ********************************************************************");
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
	public void execute(String flowName, String flowVersion, String nodeName, T target, Map<String , Object> vals) {

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
