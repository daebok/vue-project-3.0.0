package com.rd.ifaes.core.protocol.service;

import java.util.List;
import java.util.Map;

import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.util.bond.BondProtocolBean;
import com.rd.ifaes.core.protocol.util.invest.AbstractProtocolBean;
import com.rd.ifaes.core.user.domain.User;

/**
 * 
 *  协议服务接口
 * @version 3.0
 * @author jxx
 * @date 2016年7月27日
 */
public interface ProtocolService extends BaseService<Protocol>{
	/**
	 * 后台预览协议初始化处理
	 * @author QianPengZhan
	 * @param protocol
	 * @return
	 */
	String getView(final Protocol protocol);
	/**
	 * 债权协议预览
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param user
	 * @param projectId
	 * @param protocolId
	 * @return
	 */
	Map<String,Object> getBondProtocol(final User user,final String projectId,final String protocolId);
	/**
	 * 借款协议预览
	 * @author QianPengZhan
	 * @date 2016年10月19日
	 * @param user
	 * @param projectId
	 * @param protocolId
	 * @return
	 */
	Map<String,Object> getInvestProtocol(final User user,final String projectId,final String protocolId);
	/**
	 * 
	 * 生成理财投资协议
	 * @author jxx
	 * @date 2016年7月27日
	 * @param investId
	 * @param nid
	 * @param userId
	 * @return
	 */
	AbstractProtocolBean buildProtocol(String investId,String userId);
	
	/**
	 * 
	 * 生成理财投资协议
	 * @author jxx
	 * @date 2016年7月27日
	 * @param investId
	 * @param nid
	 * @param userId
	 * @return
	 */
	AbstractProtocolBean buildProtocol(String investId,String nid,String userId);
	
	/**
	 * 生成债权投资协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param investId
	 * @param userId
	 * @return
	 */
	BondProtocolBean buildBondProtocol(String bondInvestId,String userId);
	/**
	 * 生成债权投资协议
	 * @author QianPengZhan
	 * @date 2016年9月29日
	 * @param bondInvestId
	 * @param nid
	 * @param userId
	 * @return
	 */
	BondProtocolBean buildBondProtocol(String bondInvestId,String nid,String userId);
	
	/**
	 * 
	 * 根据协议类型获取协议模板
	 * @author jxx
	 * @date 2016年8月2日
	 * @param type
	 * @return
	 */
	List<Protocol> getProtocolListByType(String type);
	
	/**
	 * 
	 * 保存协议
	 * @author jxx
	 * @date 2016年8月5日
	 * @param protocol
	 * @return
	 */
	String saveProtocol(Protocol protocol);
	
	/**
	 * 
	 * 编辑协议
	 * @author jxx
	 * @date 2016年8月5日
	 * @param protocol
	 * @return
	 */
	String editProtocol(Protocol protocol);
	
	String updateStatus(String uuid);
	/**
	 * 下载协议
	 * @param projectId 项目ID
	 * @param investId 投资ID
	 * @param protocolName 协议名称
	 * @param url 跨域访问文件是否存在的地址
	 * @param webUrl 客户端返回地址
	 * @return
	 */
	String downloadProtocol(String projectId,String investId,String protocolName,String url,String webUrl);
	
	/**
	 * 获得协议模板
	 * @author xhf
	 * @date 2016年9月11日
	 * @param uuid
	 * @param param
	 * @return
	 */
	String getProtocolContent(final String uuid, final Map<String, Object> param);
	
	/**
	 * 债权协议下载
	 * @param bondId 债权项目ID
	 * @param bondInvestId 投资ID
	 * @param protocolName 协议名称
	 * @param url 跨域访问文件是否存在的地址
	 * @param webUrl 客户端返回地址
	 * @return
	 */
	String downloadBondProtocol(final String bondId,final String bondInvestId,final String protocolName,String url,String webUrl);
	
	/**
	 * 变现协议预览
	 * @author fxl
	 * @date 2016年12月20日
	 * @param user
	 * @return
	 */
	Map<String, Object> getRealizeProtocol(final User user);
}