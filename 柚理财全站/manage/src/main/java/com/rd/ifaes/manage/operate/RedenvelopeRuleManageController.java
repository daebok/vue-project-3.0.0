package com.rd.ifaes.manage.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.annotation.SystemLog;
import com.rd.ifaes.common.annotation.TokenValid;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.dict.DeleteFlagEnum;
import com.rd.ifaes.common.dict.OperateEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RedenvelopeRule;
import com.rd.ifaes.core.operate.domain.RedenvelopeRuleDetail;
import com.rd.ifaes.core.operate.model.RedenvelopeRuleModel;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleDetailService;
import com.rd.ifaes.core.operate.service.RedenvelopeRuleService;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 * 后台 - 红包规则管理 Controller
 * @version 3.0
 * @author wyw
 * @date 2016-7-26
 */
@Controller
public class RedenvelopeRuleManageController  extends SystemController {
	/**活动方案业务处理*/
	@Resource
	private transient ActivityPlanService activityPlanService;
	/**红包规则业务处理*/
	@Resource
	private transient RedenvelopeRuleService   redenvelopeRuleService;
	/**红包规则明细业务处理*/
	@Resource
	private transient RedenvelopeRuleDetailService redenvelopeRuleDetailService;
	/**项目分类*/
	@Resource
	private transient ProjectTypeService projectTypeService;
	/**
	 * 
	 * 红包管理页面
	 * @author wyw
	 * @date 2016-7-26
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/redRuleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RED_RULE)
	public String redRuleManage(){
		return "/operate/redEnvelope/redRuleManage";
	}
	
	/**
	 * 
	 * 红包规则数据接口
	 * @author wyw
	 * @date 2016-7-26
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:query")
	@RequestMapping(value = "/operate/redEnvelope/redRuleList", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RED_RULE)
	public Object redRuleList( final RedenvelopeRuleModel model , final HttpServletRequest request){
		 model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		 if(model.getPage()!=null&&ObjectUtils.isEmpty(model.getPage().getSort())){
			 model.getPage().setSort("update_time");
			 model.getPage().setOrder("desc");
		 }
		 return redenvelopeRuleService.findPage(model);
	}
	/**
	 * 
	 * 启用或则禁用规则
	 * @author wyw
	 * @date 2016-7-26
	 * @param model
	 * @param id
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:cancle")
	@RequestMapping(value = "/operate/redEnvelope/ruleStatus")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RED_RULE)
	public Object ruleStatus(final RedenvelopeRuleModel model,final String id){
		model.setUuid(id);
	    redenvelopeRuleService.updateStatus(model);
		return renderSuccessResult();
	}
	
	/**
	 * 红包规则查看
	 * @author fxl
	 * @date 2016年10月17日
	 * @return
	 */
	@RequestMapping(value = "/operate/redEnvelope/redRuleViewPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.RED_RULE)
	public String redRuleViewPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			//获取红包规则
			final RedenvelopeRule redenvelopeRule =redenvelopeRuleService.get(id);
			//获取红包规则详情
			final List<RedenvelopeRuleDetail> ruleDetailList=redenvelopeRuleDetailService.getListByRuleId(redenvelopeRule.getUuid());
			final RedenvelopeRuleModel redenvelopeRuleModel = RedenvelopeRuleModel.instance(redenvelopeRule);
			redenvelopeRuleModel.reFreshStatus();
			model.addAttribute("redRule", redenvelopeRuleModel);
			model.addAttribute("ruleDetailList", ruleDetailList);
			//加载活动方案
			model.addAttribute("activityList", activityPlanService.findAll());
			//加载项目类别
			final ProjectType  query = new ProjectType();
			query.setExceptTypeLevel(Constant.INT_ZERO);//排除根节点
		    query.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		    model.addAttribute("projectTypeList",projectTypeService.findList(query));
		}
		return "/operate/redEnvelope/redRuleViewPage";
	}
	
	/**
	 * 
	 * 红包规则新增
	 * @author wyw
	 * @date 2016-7-26
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:add")
	@RequestMapping(value = "/operate/redEnvelope/redRuleAddPage")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RED_RULE)
	public String redRuleAddPage(final Model model) {
		initPageDate(model);
		return "/operate/redEnvelope/redRuleAddPage";
	}
	/**
	 * 
	 * 页面初始化数据
	 * @author wyw
	 * @date 2016-9-8
	 */
	private void initPageDate(final Model model) {
		//加载活动方案
		 model.addAttribute("activityList", activityPlanService.findAll());
		//加载项目类别
		final ProjectType  query = new ProjectType();
		//query.setExceptTypeLevel(Constant.INT_ZERO);//排除根节点
	    query.setDeleteFlag(DeleteFlagEnum.NO.getValue());
	    List<ProjectType> list = projectTypeService.findList(query);
	    for (ProjectType projectType : list) {
    		projectType.setIsLeaf(Constant.FLAG_NO);
	    	if(projectTypeService.countChilds(projectType.getUuid())==0){
	    		projectType.setIsLeaf(Constant.LEAF_VALUE);
	    	}
		}
	     model.addAttribute("projectTypeList",  JSONArray.toJSON(list).toString());
	}
	
	/**
	 * 
	 * 红包规则 添加操作
	 * @author wyw
	 * @date 2016-7-26
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:add")
	@RequestMapping(value = "/operate/redEnvelope/redRuleAdd", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RED_RULE)
	@TokenValid(value = TokenConstants.TOKEN_ADD_RED_RULE, dispatch = true)
	public Map<String, Object> redRuleAdd(final  RedenvelopeRuleModel model,String ruleDetail,final HttpServletRequest request){	
		checkRule(model);
		List<RedenvelopeRuleDetail> detailList=null;
		if (OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT.eq(model.getGrantType())) {
			detailList=fillRuleDetail(ruleDetail);
		}
		else{
			detailList=getRedenvelopeRuleDetailList(model,request);
		}
		redenvelopeRuleService.saveRule(model, detailList);
		return renderSuccessResult();			
	}
	/**
	 * 
	 * 删除规则
	 * @author wyw
	 * @date 2016-7-26
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:del")
	@RequestMapping(value = "/operate/redEnvelope/redRuleDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RED_RULE)
	public Map<String, Object> redRuleDel(final String[] ids){
		if(ids ==null || ids.length==0){
			return renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));
		}
	    redenvelopeRuleService.checkAndDeleteBatch(ids);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 规则修改页面
	 * @author wyw
	 * @date 2016-7-26
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:edit")
	@RequestMapping(value = "/operate/redEnvelope/redRuleEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RED_RULE)
	public String redRuleEditPage(final String id,final Model model) {
		if(StringUtils.isNotBlank(id)){
			//获取红包规则
			final RedenvelopeRule redenvelopeRule =redenvelopeRuleService.get(id);
			/*if (!StringUtils.isNotBlank(redenvelopeRule.getStatus()) || RedenvelopeRuleModel.STATUS_SERVICE.equals(redenvelopeRule.getStatus())) {
				throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
			}*/
			//获取红包规则详情
			final List<RedenvelopeRuleDetail> ruleDetailList=redenvelopeRuleDetailService.getListByRuleId(redenvelopeRule.getUuid());
			final RedenvelopeRuleModel redenvelopeRuleModel = RedenvelopeRuleModel.instance(redenvelopeRule);
			if(OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT.eq(redenvelopeRule.getGrantType())){
				final List<RedenvelopeRuleDetail> groupList = redenvelopeRuleDetailService.getTenderGroup(redenvelopeRule.getUuid());
				model.addAttribute("groupList", groupList);
			}
			redenvelopeRuleModel.reFreshStatus();
			model.addAttribute("redRule", redenvelopeRuleModel);
			model.addAttribute("ruleDetailList", ruleDetailList);
			initPageDate(model);
		}
		return "/operate/redEnvelope/redRuleEditPage";
	}
	/**
	 * 
	 * 规则修改操作
	 * @author wyw
	 * @date 2016-7-27
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:redpacket:redpacketRule:edit")
	@RequestMapping(value = "/operate/redEnvelope/redRuleEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RED_RULE)
	public Map<String, Object> redRuleEdit(final RedenvelopeRuleModel model,final String  ruleDetail, final HttpServletRequest request){		
		checkRule(model);
		//设置栏目属性
		List<RedenvelopeRuleDetail> detailList = null;
		if (OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT.eq(model.getGrantType())) {
			detailList=fillRuleDetail(ruleDetail);
		}
		else{
			detailList=getRedenvelopeRuleDetailList(model,request);
		}
		redenvelopeRuleService.updateRule(model, detailList);
		return renderSuccessResult();			
	}
	
	/**
	 * 
	 * 根据规则明细参数 封装集合
	 * @author wyw
	 * @date 2016-7-27
	 * @param model
	 * @return
	 */
	private List<RedenvelopeRuleDetail> getRedenvelopeRuleDetailList(final RedenvelopeRuleModel model,final HttpServletRequest request) {
		final String grantType = model.getGrantType();// 发放类型
		final List<RedenvelopeRuleDetail> list = new ArrayList<>();
		final String[] useTenderMoneyArray = request.getParameterValues("useTenderMoney");// 最低投资金额
		final String[] amountArray = request.getParameterValues("amount");// 红包金额
		final String[] tenderMinArray = request.getParameterValues("tenderMin");// 投资区间金额 低值
		final String[] tenderMaxArray = request.getParameterValues("tenderMax");// 投资区间金额 高值
		final String[] grantRateArray = request.getParameterValues("grantRateDetail");// 红包比例
		RedenvelopeRuleDetail redenvelopeRuleDetail ;
		if (useTenderMoneyArray != null) {
			for (String amount : useTenderMoneyArray) {
				if (StringUtils.isNotBlank(amount) && !NumberUtils.isInteger(amount)) {
					throw new BussinessException(ResourceUtils.get(ResourceConstant.TENDER_AMOUNT_INTEGER_ERROR));
				}
			}
		}
		
		if (RedenvelopeRuleModel.GRANT_TYPE_FIXED_AMOUNT.equalsIgnoreCase(grantType)) {//固定金额
			for (int i = 0; i < amountArray.length; i++){
				if(StringUtils.isBlank(amountArray[i])){//固定金额的发放金额不能为空
					continue;
				}	
				redenvelopeRuleDetail =new RedenvelopeRuleDetail();
				redenvelopeRuleDetail.setAmount(BigDecimalUtils.valueOf(amountArray[i]));
				redenvelopeRuleDetail.setUseTenderMoney(BigDecimalUtils.valueOf(useTenderMoneyArray[i]));
				list.add(redenvelopeRuleDetail);
			}
		}else if(RedenvelopeRuleModel.GRANT_TYPE_INTERVAL_RATE.equalsIgnoreCase(grantType)){//比例满返
			for (int i = 0; i < grantRateArray.length; i++){
				if(StringUtils.isBlank(grantRateArray[i])||StringUtils.isBlank(tenderMinArray[i])||StringUtils.isBlank(tenderMaxArray[i])){
					continue;
				}
				redenvelopeRuleDetail =new RedenvelopeRuleDetail();
				redenvelopeRuleDetail.setGrantRate(BigDecimalUtils.valueOf(grantRateArray[i]));
				redenvelopeRuleDetail.setTenderMin(BigDecimalUtils.valueOf(tenderMinArray[i]));
				redenvelopeRuleDetail.setTenderMax(BigDecimalUtils.valueOf(tenderMaxArray[i]));
				if ((!tenderMaxArray[i].equals(Constant.TENDER_MAX_VALUE)) && BigDecimalUtils.valueOf(tenderMinArray[i]).compareTo(BigDecimalUtils.valueOf(tenderMaxArray[i])) == Constant.INT_ONE) {
					throw new BussinessException(ResourceUtils.get(ResourceConstant.REDENVELOPE_TENDER_AMOUNT_ERROR));
				}
				list.add(redenvelopeRuleDetail);
			}
			
		}
		return list;
	}
	/**
	 * @author ywt
	 * @date 2016-12-19
	 * @todo 填充金额满返
	 * @param String(JSON字符串) details
	 * @return
	 */
	private List<RedenvelopeRuleDetail> fillRuleDetail(String details){
		JSONArray detail=JSONObject.parseArray(details);
		List<RedenvelopeRuleDetail> ruleDetailList = new ArrayList<>();
		ListIterator<Object> iterator= detail.listIterator();
		for(int i =0;iterator.hasNext();i++){
			iterator.next();
			String[] amouts=detail.getJSONObject(i).getString("amount").split(",");
			String[] useTenderMoneys=detail.getJSONObject(i).getString("useTenderMoney").split(",");
			for(int j=0;j<amouts.length;j++){
				if (StringUtils.isBlank(amouts[j]) || StringUtils.isBlank(useTenderMoneys[j]) 
						|| StringUtils.isBlank(detail.getJSONObject(i).getString("tenderMin")) || StringUtils.isBlank(detail.getJSONObject(i).getString("tenderMax"))) {
					continue;
				}
				if (StringUtils.isNotBlank(useTenderMoneys[j]) && !NumberUtils.isInteger(useTenderMoneys[j])) {
					throw new BussinessException(ResourceUtils.get(ResourceConstant.TENDER_AMOUNT_INTEGER_ERROR));
				}
				RedenvelopeRuleDetail item=new RedenvelopeRuleDetail();
				item.setAmount(BigDecimalUtils.valueOf(amouts[j]));
				item.setTenderMin(BigDecimalUtils.valueOf(detail.getJSONObject(i).getString("tenderMin")));
				item.setTenderMax(BigDecimalUtils.valueOf(detail.getJSONObject(i).getString("tenderMax")));
				item.setUseTenderMoney(BigDecimalUtils.valueOf(useTenderMoneys[j]));
				ruleDetailList.add(item);
			}
			
		}
		return ruleDetailList;
	}
	
	
	/**
	 * 规则参数校验
	 * @author fxl
	 * @date 2016年12月30日
	 * @param model
	 */
	private void checkRule(final RedenvelopeRuleModel model){
		if(!(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue().equals(model.getActivityCode()) 
				|| OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue().equals(model.getActivityCode())
				|| OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue().equals(model.getActivityCode()))
				&& (model.getTotalNum() != null && model.getTotalNum() <= 0)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GRANT_QUANTITY_INTEGER_ERROR));
		}
		if(model.getGrantStartTime()!=null && model.getUseExpireTime()!=null && DateUtils.compareDate(model.getGrantStartTime(),model.getUseExpireTime())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.EXPIRETIME_MUST_GT_STARTTIME));
		}
		if(model.getGrantStartTime()!=null){
			model.setGrantStartTime(DateUtils.valueOf(DateUtils.getDateStart((model.getGrantStartTime()))));
		}
		if(model.getGrantEndTime()!=null){
			model.setGrantEndTime(DateUtils.valueOf(DateUtils.getDateEnd((model.getGrantEndTime()))));
		}
		if(model.getUseExpireTime()!=null){
			model.setUseExpireTime(DateUtils.valueOf(DateUtils.getDateEnd((model.getUseExpireTime()))));
		}
		// 投资奖励
		if(OperateEnum.ACTIVITYPLAN_TENDER_SUC.getValue().equals(model.getActivityCode()) && StringUtils.isNotBlank(model.getGrantProjectType())){
			final String[] projectTypeSet = model.getGrantProjectType().split(",");//发放类型集合
			for (String projectTypeId : projectTypeSet) {
				if(StringUtils.isNotBlank(projectTypeId) && redenvelopeRuleService.checkGrantProjectType(projectTypeId)){
					ProjectType projectType = projectTypeService.get(projectTypeId);
					throw new BussinessException(ResourceUtils.get(ResourceConstant.PORJECT_TYPE_IS_ASSOCIATED,projectType.getTypeName()));
				}
			}
		}
	}
	
	/**
	 * 查询指定项目类别的红包规则 
	 * @return 指定项目类别的红包规则ID
	 */
	@RequestMapping(value = "/operate/redEnvelope/redRuleForProjectType", method = RequestMethod.POST)
	@ResponseBody
	public Object redRuleList( final String projectTypeId){
		 RedenvelopeRule  rule= redenvelopeRuleService.findInvestRedenvelopeRuleByProjectTypeId(projectTypeId);
		 Map<String,Object> result=this.renderSuccessResult();
		    result.put("ruleId", rule!=null? rule.getUuid() : "");
		  return  result;
	}
}
