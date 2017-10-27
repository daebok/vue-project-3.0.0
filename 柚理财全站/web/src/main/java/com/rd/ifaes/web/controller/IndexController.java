package com.rd.ifaes.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.ArticleResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.base.web.BaseController;
import com.rd.ifaes.core.column.domain.Article;
import com.rd.ifaes.core.column.domain.Section;
import com.rd.ifaes.core.column.service.ArticleService;
import com.rd.ifaes.core.column.service.SectionService;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.home.service.HomeService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectInvestBespeak;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.user.domain.User;

/**
 * 首页
 * @version 3.0
 * @since 2016-6-16
 */
@Controller
public class IndexController extends BaseController {
	/**
	 * 常量NOWTIME :当前时间
	 */
	private static final String NOWTIME = "nowTime"; 
	/**
	 * 常量BESPEAKSTATUS :预约状态
	 */
	private static final String  BESPEAKSTATUS = "bespeakStatus";
	/**
	 * 常量projectName:项目名称
	 */
	private static final String PROJECTNAME = "projectName";
	/**
	 * 常量TIMELIMIT：借款期限
	 */
	private static final String TIMELIMIT  =  "timeLimit";
	/**
	 * 文章业务
	 */
	@Resource
	private transient ArticleService articleService;
	/**
	 * sectionService 业务
	 */
	@Resource
	private transient SectionService sectionService;
	/**
	 * 项目业务
	 */
	@Resource
	private transient ProjectService projectService;
 
	/**
	 * 项目投资业务
	 */
	@Resource
	private transient ProjectInvestService projectInvestService;
	/**
	 * 项目预约投资业务
	 */
	@Resource
	private transient ProjectInvestBespeakService projectInvestBespeakService; 
	@Resource
	private transient HomeService homeService;
	
	@RequestMapping(value="/index")
	public String loginPage(Model model){
		return  "/index";
	}
	/**
	 * 
	 * 首页统计信息
	 * @author wyw
	 * @date 2016-8-14
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index/countInfo")
	@ResponseBody
	public Object countInfo() throws Exception {
		Map<String, Object> data = this.renderSuccessResult();
		 data.putAll(homeService.webCountInfo());
		return data;
	}
	/**
	 * 
	 * 首页--新手专享投资列表
	 * @author wyw
	 * @date 2016-8-14
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index/getNoviceProject")
	@ResponseBody
	@Cacheable(expire=ExpireTime.TWO_SEC)
	public Object getNoviceProject() throws Exception {
		final Project queryProject = new Project();
		final Map<String, Object> ret = new HashMap<>();
		final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		//募集中、 成立待审核、成立审核成功
		String[] projectStatus={LoanEnum.STATUS_RAISING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(), LoanEnum.STATUS_ESTABLISH.getValue()};
		queryProject.setNovice(LoanEnum.NOVICE_FLAG_YES.getValue());
		queryProject.setSaleChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
		//排序 
		queryProject.setStatusSet(projectStatus);
		final Page<Project> page = new Page<>();
		page.setPageSize(1);
		page.setSort("status, show_time");
		page.setOrder("desc");
		queryProject.setPage(page);
		final List<Map<String, Object>> dataInfo=  new ArrayList<>() ;
		final List<Project> projectlist = projectService.findIndexProjectList(queryProject);
		//处理数据
		final int size = projectlist.size();
		for(int i=Constant.INT_ZERO;i<size;i++){
			Map<String,Object> data = getData(projectlist.get(i),user);
			dataInfo.add(data);
		}
		ret.put("noviceProjectList", dataInfo);
		return ret;
	}
	/**
	 * 
	 * 首页--精选投资列表
	 * @author wyw
	 * @date 2016-8-14
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index/getChoiceProject")
	@ResponseBody
	@Cacheable(expire=ExpireTime.TWO_SEC)
	public Object getChoiceProject() throws Exception {
		Map<String, Object> ret = new HashMap<>();
		Project queryProject = new Project();
		//募集中、 成立待审核、成立审核成功
		String[] projectStatus={LoanEnum.STATUS_RAISING.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue(), LoanEnum.STATUS_ESTABLISH.getValue()};
		queryProject.setChoice(LoanEnum.CHOICE_FLAG_YES.getValue());
		//排序  
		queryProject.setStatusSet(projectStatus);
		queryProject.setSaleChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
		List<Map<String, Object>> dataInfo=  new ArrayList<>() ;
		Page<Project> page = new Page<>();
		page.setPageSize(6);
		page.setSort("status, show_time");
		page.setOrder("desc");
		queryProject.setPage(page);
		List<Project> projectlist = projectService.findIndexProjectList(queryProject);
		User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		//处理数据
		int size = projectlist.size();
		for(int i=0;i<size;i++){
			Map<String,Object> data = getData(projectlist.get(i),user);
			dataInfo.add(data);
		}
		ret.put("choiceProjectList", dataInfo);
		return ret;
	}
	
	/**
	 * 循环封装data
	 * @author QianPengZhan
	 * @date 2016年9月11日
	 * @param project
	 * @param user
	 * @return
	 */
	private Map<String,Object> getData(final Project project,final User user){
		Map<String, Object> data = new HashMap<>();
		data.put("id", project.getUuid());
		data.put("status", project.getStatus());
		data.put("saleTime",project.getSaleTime());
		data.put("lowestAccount", project.getLowestAccount());
		data.put(TIMELIMIT, project.getTimeLimit());
		data.put("timeType", project.getTimeType());
		data.put("apr", project.getApr());
		data.put(PROJECTNAME, project.getProjectName());
		data.put("novice", project.getNovice());
		data.put("account", project.getAccount());
		data.put("projectNo", project.getProjectNo());
		data.put("projectTypeName", project.getProjectTypeName());
		data.put("accountYes", project.getAccountYes());
		if(LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag())){
			data.put("bondUseful", project.getBondUseful());
		}else{
			data.put("realizeUseful", project.getRealizeUseful());
		}
		if(user!=null){//已经登陆
			ProjectInvestBespeak projectInvestBespeak=new ProjectInvestBespeak();
			projectInvestBespeak.setProjectId(project.getUuid());
			projectInvestBespeak.setUserId(user.getUuid());
			data.put(BESPEAKSTATUS, projectInvestBespeakService.getCount(projectInvestBespeak)>0?Constant.INT_ONE:Constant.INT_ZERO);//已经预约
		}else{
			data.put(BESPEAKSTATUS, Constant.INT_ZERO);
		}
		data.put(NOWTIME, DateUtils.getNow());
		return data;
	}
	/**
	 * 
	 * 首页稳赚系列数据接口
	 * @author wyw
	 * @date 2016-8-28
	 * @return
	 */
	@RequestMapping(value="/index/getSureProfit")
	@ResponseBody
	public Object getSureProfit(){
		final Map<String, Object> data = renderSuccessResult();
		data.put("data",homeService.getSureProfit());
		return data;	
	}
	
	
	@RequestMapping(value="/index/articleList")
	@ResponseBody
	@Cacheable(expire=ExpireTime.TWO_SEC)
	public Object articleList(String code,int pageSize) throws Exception {
		if(StringUtils.isBlank(code)){
			 throw new BussinessException( ResourceUtils.get(ArticleResource.ARTICLE_CODE_BLANK), BussinessException.TYPE_JSON);
		}
		Section section = sectionService.getByCode(code);
		if (section == null) {
			throw new BussinessException(ResourceUtils.get(ArticleResource.ARTICLE_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		Map<String,Object> data = new HashMap<>();
		List<Article> list = articleService.findArticleBySection(section,pageSize);
		data.put("result", true);
		data.put("list", list);
		return data;
	}
	/**
	 *
	 * 首页最新投资
	 * @author wyw
	 * @date 2016-8-17
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index/getHomeInvest")
	@ResponseBody
	@Cacheable(expire=ExpireTime.TWO_SEC)
	public Object getHomeInvest() throws Exception{
		ProjectInvest projectInvest = new ProjectInvest();
		projectInvest.setPage(new Page<ProjectInvest>());
		projectInvest.getPage().setSort("create_time");
		projectInvest.getPage().setOrder("desc");
		projectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		List<ProjectInvest> investList=projectInvestService.findList(projectInvest);
		Map<String, Object> retData= new HashMap<>();
		List<Map<String, Object>> invest = new ArrayList<>();
		//数据处理
		if(CollectionUtils.isNotEmpty(investList)){
			for (ProjectInvest pi : investList) {
				Map<String, Object> data = new HashMap<>();
				data.put("userName", pi.getUserName());
				data.put("realName", pi.getRealName());
				data.put("mobile", pi.getMobile());
				data.put(PROJECTNAME, pi.getProjectName());
				data.put("amount", pi.getAmount());
				data.put("createTime", pi.getCreateTime());
				data.put(NOWTIME, DateUtils.getNow());
				data.put("projectId", pi.getProjectId());
				invest.add(data);
			}			
		}
		retData.put("data", invest);
		retData.put("result", true);
		return retData;
	}
	
	/**
	 * 投资预约
	 * @param projectId 预约的项目UUID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/index/investBespeak")
	@ResponseBody
	public Object investBespeak(String projectId) throws Exception{
		
		User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
		if(user==null ){
		      final Map<String,Object>  data=renderResult(false, ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN));
			  data.put("redirect", true);
			 return data;
		}
		Project project = projectService.get(projectId);
		projectInvestBespeakService.investBespeak(user.getUuid(), project);
		return renderResult(true, ResourceUtils.get(LoanResource.INVEST_BESPEAK_SUCCESS));
	}
	
	/**
	 * 浏览器版本升级提示
	 * @author fxl
	 * @date 2016年11月28日
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/browserUpdate")
	public String browserUpdate(Model model){
		return  "/errorPage/browserUpdate";
	}
	
	/**
	 * 渤海移动端开户注册测试
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/cbhb/tppNetLoanRegisterTest")
	public String tppNetLoanRegisterTest(final HttpServletResponse response) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usrFlag", "1");
		map.put("mobielNo", "13856910009");
		map.put("merPriv", "私有域");
		map.put("identType", "1");
		map.put("identNo", "340823199002236844");
		map.put("usrName", "钱鹏展");
		map.put("openBankId", "CMB");
		map.put("openAcctId", "6225885866780885");
		TppService tppService = (TppService) TppUtil.tppService();
		String url = (String) tppService.tppAppRegister(map);
		response.sendRedirect(url);
		return null;
	}
	
	/**
	 * 渤海移动端修改银行卡测试
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/cbhb/tppNetLoanBindCardTest")
	public String tppNetLoanBindCardTest(final HttpServletResponse response) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("plaCustId", "0001100000096009");
		TppService tppService = (TppService) TppUtil.tppService();
		String url = (String) tppService.tppAppUpdateBankCard(map);
		response.sendRedirect(url);
		return null;
	}
	
	/**
	 * 渤海移动端充值测试
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/cbhb/tppNetLoanRechargeTest")
	public String tppNetLoanRechargeTest(final HttpServletResponse response) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("plaCustId", "0001100000096009");
		map.put("feeType", "1");
		map.put("merFeeAmt", "0");
		map.put("marketAmt", "0");
		map.put("transAmt", "10000");//100元
		TppService tppService = (TppService) TppUtil.tppService();
		String url = (String) tppService.tppAppRecharge(map);
		response.sendRedirect(url);
		return null;
	}
	
	
	/**
	 * 渤海移动端提现测试
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="/cbhb/tppNetLoanWithDrawTest")
	public String tppNetLoanWithDrawTest(final HttpServletResponse response) throws IOException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("plaCustId", "0001100000096009");
		map.put("feeType", "2");
		map.put("merFeeAmt", "100");
		map.put("transAmt", "10000");//100元
		TppService tppService = (TppService) TppUtil.tppService();
		String url = (String) tppService.tppAppCash(map);
		response.sendRedirect(url);
		return null;
	}
}
