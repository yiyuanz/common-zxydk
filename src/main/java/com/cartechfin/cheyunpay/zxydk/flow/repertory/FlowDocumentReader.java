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
 * zyiyuan 2:30:04 PM 创建
 */
package com.cartechfin.cheyunpay.zxydk.flow.repertory;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.cartechfin.cheyunpay.zxydk.common.utils.XmlDocumentReader;
import com.cartechfin.cheyunpay.zxydk.flow.contants.FlowContants;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlow;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.actionflow.ActionFlowChart;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.node.ActionNode;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransition;
import com.cartechfin.cheyunpay.zxydk.flow.model.entity.element.transition.ActionTransitionLine;


/**
 * @category  流程图解析器 
 * 
 */
@SuppressWarnings("all")
public class FlowDocumentReader  implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6724063307492533853L;
	
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(FlowDocumentReader.class);
	
	
	/** 流程图元素 实例创建器 */
	@Autowired
	private FlowBaseActionCreator actionCreator;
	
	/**
	 *  @category 解析流程矢量图
	 * 
	 *  @param  resource  流程矢量图【 resouce spring的形态 】 
	 *  
	 *  @return actionFlow 解析为流程实例
	 *  
	 */
	public ActionFlow readFlowFile( Resource res ) {
		
		XmlDocumentReader.validatorResouce( FlowContants.FLOW_XSD_URL, res );
		
		Element root = XmlDocumentReader.getRootElementWithResource(res);
		// 流程图总定义节点
		ActionFlow flow = this.actionCreator.createInstance(ActionFlowChart.class);
		// 全局事务
		Boolean isOpenAllTransaction = Boolean.parseBoolean(root.getAttribute(FlowContants.FLOW_ELEMENT_FLOW_NODE_ATTR_TRANSACTION));
		
		flow.setHasOpenTransaction( null == isOpenAllTransaction ? Boolean.FALSE : isOpenAllTransaction );
		// 流程跟踪日志
		Boolean isOpenLogger = Boolean.parseBoolean(root.getAttribute(FlowContants.FLOW_ELEMENT_FLOW_NODE_ATTR_FOLLOW_LOGGER));
		
		flow.setHasOpenLogger(null == isOpenLogger ? Boolean.FALSE : isOpenLogger );
		
		flow.setUniqueKey( new ActionFlow.Key(root.getAttribute(FlowContants.FLOW_ELEMENT_FLOW_NODE_ATTR_NAME), root.getAttribute(FlowContants.FLOW_ELEMENT_FLOW_NODE_ATTR_VERSION)) );
		// 流程图中子节点信息
		for( int subNodeIndex = 0 ; subNodeIndex < root.getChildNodes().getLength(); subNodeIndex++  ) {
			
			Node node = root.getChildNodes().item( subNodeIndex );

			if( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(FlowContants.FLOW_ELEMENT_FLOW_DESC_NODE) ){
				// 描述节点
				logger.info("this is flow file ,flow's name is {} , begin loading flow file now! flow's  descition is {}!", root.getAttribute("name"), node.getTextContent() );
					
				continue;
			}
			
			if( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(FlowContants.FLOW_ELEMENT_FLOW_ACTION_NODE)) {
				// 执行节点
				ActionNode actionNode = this.actionCreator.newInstance( node.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_CLASS).getNodeValue() );
				
				actionNode.setNodeName( node.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_NAME).getNodeValue() );
				
				if( null != node.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_transaction) ) {
					
					actionNode.setTransaction(Boolean.parseBoolean(node.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_ACTION_NODE_ATTR_transaction).getNodeValue()));
				}
				
				actionNode.setFlowName(flow.getUniqueKey().getName());
				
				actionNode.setFlowVersion(flow.getUniqueKey().getVersion());
				
				actionNode.setOpenLogger(flow.getHasOpenLogger() );
				// TODO 填充 条件选择
				fillTransition(node, actionNode);
				
				flow.addActionNode(actionNode);
			}
		}
		// 刷新流程图中所有节点与节点的关系
		flow.reflushTransaction();
		
		return flow;
	}

	
	/**
	 * @category 填充条件选择 
	 * 
	 * @param node [ w3c NODE ]
	 * 
	 * @param actionNode [自定义的流程节点] 
	 * 
	 */
	private  void fillTransition(Node node, ActionNode  actionNode) {
		
		if( FlowContants.FLOW_END_LABEL.equals(node.getNodeName()) ) {
			// 默认 结束节点
			return;
		}

		for( int conditionIndex = 0 ; conditionIndex < node.getChildNodes().getLength(); conditionIndex++ ){
			
			Node conditionNode = node.getChildNodes().item(conditionIndex);
			
			if( conditionNode.getNodeType() == Node.ELEMENT_NODE ){
				
				for( int transitionIndex = 0 ; transitionIndex < conditionNode.getChildNodes().getLength(); transitionIndex++ ){
					
					Node transitionNode = conditionNode.getChildNodes().item(transitionIndex);
					
					if( transitionNode.getNodeType() == Node.ELEMENT_NODE && transitionNode.getNodeName().equals(FlowContants.FLOW_ELEMENT_FLOW_TRANSITION) ){
						
						ActionTransition transition  = this.actionCreator.createInstance(ActionTransitionLine.class); 
						
						transition.setDesc(transitionNode.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_TRANSITION_ATTR_DESC).getNodeValue());
						
						transition.setEvent(transitionNode.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_TRANSITION_ATTR_EVENT).getNodeValue());
						
						transition.setTo(transitionNode.getAttributes().getNamedItem(FlowContants.FLOW_ELEMENT_FLOW_TRANSITION_ATTR_TO).getNodeValue());
						
						transition.setFrom(actionNode.getNodeName());
						
						transition.setFromNode( actionNode );
						
						transition.intoFlowName( actionNode.getFlowName() );
						
						transition.intoFlowVersion( actionNode.getFlowVersion() );
						
						actionNode.registTransition(transition);
					}
				}
			}
		}
	}
}
