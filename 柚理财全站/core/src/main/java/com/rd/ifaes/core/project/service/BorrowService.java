package com.rd.ifaes.core.project.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:BorrowService
 * @author FangJun
 * @version 3.0
 * @date 2016-6-20
 */
public interface BorrowService extends BaseService<Borrow>{
	
	/**
	 * 校验并查询借款标
	 * @author QianPengZhan
	 * @date 2016年8月30日
	 * @param projectId
	 * @param backUrl 校验错误返回地址
	 * @return
	 */
	Borrow getBorrow(final String projectId,final String backUrl);
	
	/**
	 * 借款审核
	 * @param borrow
	 */
	void verify(Borrow model);
	
	/**
	 * 上架
	 * @param model
	 * @return
	 */
	void sale(Borrow model);
	
	/**
	 * 下架
	 * @param model
	 * @return
	 */
	void stop(Borrow model);
	
	/**
	 * 成立审核
	 * @param model
	 * @return
	 */
	void establishVerify(Borrow model);

	/**
	 * 借款记录
	 * @param entity
	 * @return
	 */
	Page<Borrow> findRecordPage(Borrow entity);
	
	/**
	 * 
	 * 贷后跟踪数据
	 * @author wyw
	 * @date 2016-8-17
	 * @param entity
	 * @return
	 */
	Page<Borrow> findBorrowFollowPage(Borrow entity);

	/**
	 * 
	 * 前台借款申请
	 * @author ZhangBiao
	 * @date 2016年8月2日
	 * @param borrow
	 * @param user 
	 * @param request 
	 * @return
	 */
	Borrow addBorrow(Borrow borrow, User user);

	/**
	 * 
	 * 添加借款申请
	 * @author ZhangBiao
	 * @param entity 
	 * @param request 
	 * @date 2016年8月2日
	 * @return
	 */
	void doAddBorrow(Borrow entity);
	
	/**
	 * 流程驱动
	 * @author lh
	 * @date 2016年8月4日
	 * @param borrow
	 * @return
	 */
	void executeProcess(Borrow borrow);

	/**
	 * 
	 * 获取已保存数据
	 * @author ZhangBiao
	 * @param borrow 
	 * @date 2016年8月12日
	 * @return
	 */
	Map<String, Object> getBorrowDetail(Borrow borrow);

	/**
	 * 根据产品id获取是否可转让
	 * @author ZhangBiao
	 * @date 2016年10月19日
	 * @param projectId
	 * @return
	 */
	String getBondUsefulByProjectId(String projectId);

	/**
	 * 前台借款校验用户资格
	 * @author ZhangBiao
	 * @date 2016年10月24日
	 * @param user
	 * @return
	 */
	Map<String, Object> checkUserBorrow(User user);

	/**
	 * 查询分页数据
	 * @author ZhangBiao
	 * @date 2016年10月24日
	 * @param entity
	 * @return
	 */
	Page<Borrow> findBasePage(Borrow entity);
	
	
	/**
	 * 根据日期查询当日复审借款
	 * @author fxl
	 * @date 2017年2月22日
	 * @param date
	 * @return
	 */
	List<Borrow> findBorrowListByDate(Date date);

	/**
	 * 根据projectId查询borrow
	 * @param projectId
	 * @return
	 */
	Borrow getByProjectId(String projectId);
	
	/**
	 * 查询合同记录
	 * @param entity 
	 * @param flag 判断是否是查询合同管理
	 * @return
	 */
	public Page<Borrow> findRecordPage(Borrow entity, boolean flag) ;
	
}