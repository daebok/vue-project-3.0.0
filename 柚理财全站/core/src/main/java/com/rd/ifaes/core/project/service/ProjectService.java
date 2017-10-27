package com.rd.ifaes.core.project.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.core.base.service.BaseService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.model.ProjectRecord;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBatInvestCancleModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * Service Interface:ProjectService
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
public interface ProjectService extends BaseService<Project>{
	/**
	 * 批量投资撤销
	 * @author QianPengZhan
	 * @date 2017年3月13日
	 * @param cbhbModel
	 */
	void cbhbBatInvestCancleNotify(final CbhbBatInvestCancleModel cbhbModel);
	/**
	 *  渤海银行 放款回调处理
	 * @author QianPengZhan
	 * @date 2017年3月8日
	 * @param cbhbModel
	 */
	void cbhbLoanNotify(final CbhbFileReleaseModel cbhbModel);
	/**
	 * 获取项目名称
	 * @author QianPengZhan
	 * @param project
	 * @return
	 */
	String getProjectName(final Project project);
	/**
	 * 获取项目实体并校验
	 * @author QianPengZhan
	 * @date 2016年11月5日
	 * @param projectId
	 * @param backUrl  校验返回地址
	 * @return
	 */
	Project getProjectValid(final String projectId,final String backUrl);
	
	/**
	 * 每隔30分钟  搜索前30分钟  已经成立审核的产品和借贷 进行生成协议处理 
	 * @author QianPengZhan
	 * @date 2016年11月4日
	 */
	void  projectCreateProtocol();
	
	/**
	 * 成立审核的时候生成协议
	 * @author QianPengZhan
	 * @date 2016年11月3日
	 * @param project
	 */
	void createProjectProtocol(final Project project);
	
	/**
	 * 根据riskLevelVal 查询产品的个数
	 * @author QianPengZhan
	 * @date 2016年10月21日
	 * @param riskLevel
	 * @return
	 */
	int getProjectCount(final String riskLevel);
	/**
	 *  投资项目详情
	 * @author  FangJun
	 * @date 2016年8月26日
	 * @param projectId 项目UUID
	 * @param userId 当前用户ID
	 * @return 投资项目的相关信息
	 */
	Map<String ,Object> getDetail(String projectId,String userId);
	
	/**
	 * 取得当日下一个借款编号<br>
	 * 	生成的格式是: 2016062800001 前面几位为当前的日期,后面五位为系统自增长类型的编号<br>
	 *  原理: 1.获取当前日期格式化值; 2.读取数据库,上次编号的值+1即为当前此次编号的值<br>
	 *  (新的一天会重新从00001开始编号)
	 * @param createDate
	 * @return
	 */
	String nextProjectNo(String createDate);
	
	/**
	 * 更新状态
	 * @param uuid
	 * @param status
	 * @param preStatus
	 * @return
	 */
	void updateStatus( String uuid, String status, String preStatus);
	/**
	 * 不可编辑数量
	 * @param ids
	 * @return
	 */
	int getNotEditableCount(String[] ids);
	
	/**
	 * 根据项目编号
	 * @param projectNo 项目编号
	 * @return 指定编号的项目
	 */
   Project  getProjectByNo(String projectNo);
  
   /**
    *  更新已投金额
    * @param amount 新增投资金额
    * @param projectId 项目编号
    * @return 成功 1 失败0
    */
  void  updateAccountYes(double addAmount,String projectId);
   
  /**
   * 指定项目不成立（撤销，解冻资金）
   * @param uuid 项目ID
   * @return  
   */
  void cancel(String uuid);
  /**
   *  项目成立
   * @param uuid 项目ID
   * @return  
   */
  void verifySuccess(String uuid);
  /**
   * 
   * 担保用户个人中心统计项目数量
   * @author wyw
   * @date 2016-8-8
   * @param project
   * @return
   */
  Map<String,String> countVouchProject(String vouchId );
  /**
   * 查询项目
   * @param project
   * @return
   */
  Page<Project>  findProject(Project project);
  /**
   * 审核项目
   * @param model 审核信息 : projectId 审核项目ID,vouchId 当前操作人UUID（需校验）
   * @return
   */
  int vouchProject(Project model,String remark);
  /**
   * 审核项目
   * @param project
   * @return
   */
  int updateVouchStatus(Project project);

  /**
   * 根据userId查询借款总额
   * @param userId
   * @return
   */
  BigDecimal getAccountTotalByUserId(String userId);
  /**
   * 查询项目的产品详情或借款详情
   * @param projectId  项目UUID
   * @return 产品详情或借款详情, 借款用途
   */
  Map<String,Object>  getContent(String projectId );
  /**
   * 查询项目的借款人相关信息（基本信息、借款资质、担保机构）
   * @param projectId
   * @return
   */
  Map<String,Object> getBorrowerInfo(String projectId );
  
  /**
   * 
   * 根据日期查询产品列表
   * @author xhf
   * @date 2016年8月2日
   * @param model
   * @return
   */
  Page<Project> findByDate(Project model);
  /**
   * 
   * 统计借款金额
   * @author wyw
   * @date 2016-8-10
   * @param projectStatus
   * @return
   */
  BigDecimal sumProjectAccount(String[] projectStatus);
  
	/**
	 * 根据状态和开售时间获取产品
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 * @param value
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<Project> findListByStatusTime(String status, Date startDate,
			Date endDate);
	
	/**
	 * 根据投标ID获取项目名称
	 * @author fxl
	 * @date 2016年8月19日
	 * @param investId
	 * @return
	 */
	String getProjectNameByInvestId(String investId);
	/**
	 *  产品列表查询
	 * @author  FangJun
	 * @date 2016年8月20日
	 * @param entity 产品查询条件
	 * @return  ProjectRecord分页列表数据
	 */ 
	  Page<ProjectRecord> findProjectPage(ProjectRecord entity) ;	 
	  
	/**
	 * 更新已还金额
	 * @author fxl
	 * @date 2016年8月23日
	 * @param project
	 * @param repaymentAmount
	 * @param repaymentInterest
	 */
	void updateRepayAmount(Project project,BigDecimal repaymentAmount,BigDecimal repaymentInterest);
	
	/**
	 * 查询募集到期的变现的项目
	 * @author fxl
	 * @date 2016年8月24日
	 * @param endTime
	 * @return
	 */
	List<Project> findExpireRealize( Date endTime);
	
	/**
	 * 获取所持资产信息(缓存)
	 * @author fxl
	 * @date 2016年8月29日
	 * @param investId
	 * @return
	 */
	Map<String,Object> getProductInfo(String investId);
	/**
	 *  自动下架使用（修改项目状态为募集结束，已投为0改为未成立）
	 * @author  FangJun
	 * @date 2016年8月31日
	 * @return 修改条数
	 */
	int autoStopSale();
	
	/**
	 * 
	 * 查询首页项目
	 * @author wyw
	 * @date 2016-9-8
	 * @param project
	 * @return
	 */
	List<Project> findWebProjectList(Project project);

	/**
	 * 
	 * 根据id查询-获取数据库中的数据，非从缓存中获取
	 * @author ZhangBiao
	 * @date 2016年9月9日
	 * @param uuid
	 * @return
	 */
	Project getProjectByUuid(String uuid);
	
	/**
	 * 
	 * 首页项目列表
	 * @author wyw
	 * @date 2016-9-9
	 * @param model
	 * @return
	 */
	List<Project> findIndexProjectList(Project model);
	
	/**
	 * 自动清除项目缓存
	 */
	void autoRemoveProjectCache(int delaySeconds);

	/**
	 * 统计日期范围内借款金额
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月27日
	 * @param projectStatus
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	BigDecimal sumProjectAccountByDate(String[] projectStatus,
			String dateStart, String dateEnd);

	/**
	 * 根据状态获取数据
	 * 
	 * @author ZhangBiao
	 * @date 2016年9月29日
	 * @param value
	 * @return
	 */
	List<Project> findListByStatus(String status);
	
	/**
	 *  投资项目详情
	 * @author  FangJun
	 * @date 2016年8月26日
	 * @param projectId 项目UUID
	 * @return 投资项目剩余可投相关信息
	 */
	Map<String ,Object> getRemainAccount(String projectId);
	
	/**
	 *  查询指定项目的预约信息（预约人数，指定用户是否预约）
	 * @author  FangJun
	 * @date 2016年10月11日
	 * @param projectId 项目ID
	 * @param userId 用户ID
	 * @return  预约信息
	 */
	Map<String,Object> getBespeakInfo(String projectId,String userId);

	/**
	 * 添加担保审核记录
	 * 
	 * @author ZhangBiao
	 * @date 2016年10月13日
	 * @param model
	 * @param remark
	 * @param user 
	 */
	void vouchVerify(Project model, String remark, User user);
	
	/**
	 * 更新项目投资阶段
	 * @param stage		项目阶段
	 * @param projectNo	项目编号	
	 * @return
	 */
	void updateStage(int stage, String projectNo);
	
	/**
	 * 产品下架后移除已经预约的投资
	 * @author ywt
	 * @date 2016-10-25
	 * @param projectId	项目编号	
	 * @return
	 */
	void removeInvestBespeak(String projectNo);
	
	/**
	 * 根据用户id获取借款个数
	 * @author ZhangBiao
	 * @date 2016年10月27日
	 * @param userId
	 * @return
	 */
	int getProjectNumByUserId(String userId);
	
	/**
	 * 封装第三方参数
	 * @author fxl
	 * @date 2016年11月1日
	 * @param investUser
	 * @param project
	 * @param projectUser
	 * @param amount
	 * @param invest
	 * @param realAmount
	 * @param manageFee
	 */
	TppTrade fillTppTrade(User investUser, Project project, User projectUser, BigDecimal amount, ProjectInvest invest,
			BigDecimal realAmount, BigDecimal manageFee);
	
	/**
	 * 获取项目详情页链接
	 * @author fxl
	 * @date 2016年11月16日
	 * @param uuid
	 * @param projectName
	 * @param realizeFlag
	 * @return
	 */
	String getProjectInfo(String uuid,String projectName,String realizeFlag);
	
	
	 /**
     * 即息理财-单笔投资放款
     * @param invest 投资记录信息
     */
	  void handleInvestLoan(ProjectInvest invest);
	  
	/**
	 * 处理单个投资的待收
	 * @param project 项目
	 * @param projectWorker 项目工具类
	 * @param allCollectionList 待收收集列表
	 * @param invest 投资记录
	 */
	  void handleInvestCollection(final Project project, final ProjectWorker projectWorker,
				final List<ProjectCollection> allCollectionList, ProjectInvest invest);
	  
	  /**
	   * 统计状态数
	   * @param borrowFlag
	   * @param projectStatus
	   * @return
	   */
	  List<Project> getCountByStatus(String borrowFlag, String[] projectStatus);
}
