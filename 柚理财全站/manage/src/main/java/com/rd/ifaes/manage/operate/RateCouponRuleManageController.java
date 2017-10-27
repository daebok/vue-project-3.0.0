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
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.SysLogConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.RateCouponRule;
import com.rd.ifaes.core.operate.domain.RateCouponRuleDetail;
import com.rd.ifaes.core.operate.model.RateCouponRuleModel;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.operate.service.RateCouponRuleDetailService;
import com.rd.ifaes.core.operate.service.RateCouponRuleService;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.manage.sys.SystemController;

/**
 * 
 *  加息券规则设置
 * @version 3.0
 * @author wyw
 * @date 2016-7-27
 */
@Controller
public class RateCouponRuleManageController  extends SystemController {
	/**
	 * 活动方案业务处理
	 */
	@Resource
	private transient ActivityPlanService activityPlanService;
	/**
	 * 加息券规则业务处理
	 */
	@Resource
	private transient RateCouponRuleService   rateCouponRuleService;
	/**
	 * 加息券规则明细业务处理
	 */
	@Resource
	private transient RateCouponRuleDetailService   rateCouponRuleDetailService;
	/**项目分类*/
	@Resource
	private transient ProjectTypeService projectTypeService;
	/**
	 * 
	 *  加息券规则设置管理页面
	 * @author wyw
	 * @date 2016-7-28
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/rateRuleManage")
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RATE_RULE)
	public String rateRuleManage(){
		return "/operate/rateCoupon/rateRuleManage";
	}
	
	/**
	 * 
	 * 加息券数据接口
	 * @author wyw
	 * @date 2016-7-28
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprRule:query")
	@RequestMapping(value = "/operate/rateCoupon/rateRuleList", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.QUERY,content=SysLogConstant.RATE_RULE)
	public Object redRuleList( final RateCouponRuleModel model ){
		 model.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		 if(model.getPage()!=null&&ObjectUtils.isEmpty(model.getPage().getSort())){
			 model.getPage().setSort("update_time");
			 model.getPage().setOrder("desc");
		 }
		 return rateCouponRuleService.findPage(model);
	}
    /**
	 * 
	 * 启用或则禁用加息券规则
	 * @author wyw
	 * @date 2016-7-28
	 * @param model
	 * @param id
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprRule:cancel")
	@RequestMapping(value = "/operate/rateCoupon/rateRuleStatus")
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RATE_RULE)
	public Object rateRuleStatus(final RateCouponRule model,final String id){
		model.setUuid(id);
	    rateCouponRuleService.updateStatus(model);
		return renderSuccessResult();
	}
	/**
	 * 
	 * 加息券规则添加页面
	 * @author wyw
	 * @date 2016-7-28
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/rateRuleAddPage")
	@RequiresPermissions("oper:addApr:addAprRule:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RATE_RULE)
	public String rateRuleAddPage(final Model model) {
		initPageDate(model);
		return "/operate/rateCoupon/rateRuleAddPage";
	}
	
	
	/**
	 * 加息券查看
	 * @author fxl
	 * @date 2016年10月17日
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/rateRuleViewPage")
	@SystemLog(method=SysLogConstant.VIEW,content=SysLogConstant.RATE_RULE)
	public String rateRuleViewPage(final String id ,final Model model){
		if(StringUtils.isNotBlank(id)){
			//获取加息券规则
			final RateCouponRule ratecouponRule =rateCouponRuleService.get(id);
			//获取红包规则详情
			final List<RateCouponRuleDetail> ruleDetailList=rateCouponRuleDetailService.getListByRuleId(id);
			final RateCouponRuleModel ratecouponRuleModel = RateCouponRuleModel.instance(ratecouponRule);
			ratecouponRuleModel.reFreshStatus();
			 model.addAttribute("rateRule", ratecouponRuleModel);
			 model.addAttribute("ruleDetailList", ruleDetailList);
			//加载活动方案
			 model.addAttribute("activityList", activityPlanService.findAll());
			//加载项目类别
			final ProjectType  query = new ProjectType();
			query.setExceptTypeLevel(Constant.INT_ZERO);//排除根节点
		    query.setDeleteFlag(DeleteFlagEnum.NO.getValue());
		    model.addAttribute("projectTypeList", projectTypeService.findList(query));
		}
		return "/operate/rateCoupon/rateRuleViewPage";
	}
	/**
	 * 
	 * 页面初始化数据
	 * @author wyw
	 * @date 2016-9-8
	 */
	private void initPageDate(final Model model){
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
	 * 加息券规则添加操作
	 * @author wyw
	 * @date 2016-7-28
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/operate/rateCoupon/rateRuleAdd", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("oper:addApr:addAprRule:add")
	@SystemLog(method=SysLogConstant.ADD,content=SysLogConstant.RATE_RULE)
	@TokenValid(value = TokenConstants.TOKEN_ADD_RATE_RULE, dispatch = true)
	public Map<String, Object> rateRuleAdd(final RateCouponRuleModel model ,final String userIds,final String ruleDetail,final HttpServletRequest request){	
		checkRule(model);
		List<RateCouponRuleDetail> detailList = null;
		if (OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT_RATE.eq(model.getGrantType())) {
			detailList=fillRuleDetail(ruleDetail);
		}
		else{
			detailList=getRateCouponRuleDetailList(model,request);
		}
		rateCouponRuleService.saveRule(model, detailList,userIds);
		return renderSuccessResult();			
	}
	/**
	 * 
	 * 删除规则
	 * @author wyw
	 * @date 2016-7-28
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprRule:del")
	@RequestMapping(value = "/operate/rateCoupon/rateRuleDel", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.DEL,content=SysLogConstant.RATE_RULE)
	public Map<String, Object> rateRuleDel(final String[] ids){
		Map<String, Object>  retMap= renderResult(false, ResourceUtils.get(ResourceConstant.GLOBAL_DEL_NO_RECORD));;
		if(ids ==null || ids.length==0){
			return retMap;
		}
	    rateCouponRuleService.checkAndDeleteBatch(ids);
	    retMap = renderSuccessResult();
		return retMap;
	}
	/**
	 * 
	 * 加息券规则修改页面
	 * @author wyw
	 * @date 2016-7-28
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprRule:edit")
	@RequestMapping(value = "/operate/rateCoupon/rateRuleEditPage")
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RATE_RULE)
	public String redRuleEditPage(final String id,final Model model){
		if(StringUtils.isNotBlank(id)){
			//获取加息券规则
			final RateCouponRule ratecouponRule =rateCouponRuleService.get(id);
			//获取加息券规则详情
			final List<RateCouponRuleDetail> ruleDetailList=rateCouponRuleDetailService.getListByRuleId(id);
			final RateCouponRuleModel ratecouponRuleModel = RateCouponRuleModel.instance(ratecouponRule);
			ratecouponRuleModel.reFreshStatus();
			if(OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT_RATE.eq(ratecouponRule.getGrantType())){
				model.addAttribute("groupList", rateCouponRuleDetailService.getTenderGroup(ratecouponRule.getUuid()));
			}
			model.addAttribute("rateRule", ratecouponRuleModel);
			model.addAttribute("ruleDetailList", ruleDetailList);
			initPageDate(model);
		}
		return "/operate/rateCoupon/rateRuleEditPage";
	}
	 /**
	 * 
	 * 规则修改操作
	 * @author wyw
	 * @date 2016-7-28
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("oper:addApr:addAprRule:edit")
	@RequestMapping(value = "/operate/rateCoupon/rateRuleEdit", method = RequestMethod.POST)
	@ResponseBody
	@SystemLog(method=SysLogConstant.EDIT,content=SysLogConstant.RATE_RULE)
	public Map<String, Object> redRuleEdit(final  RateCouponRuleModel model,String ruleDetail,final HttpServletRequest request) {	
		checkRule(model);
		    //设置栏目属性
		List<RateCouponRuleDetail> detailList = null;
		if (OperateEnum.GRANT_TYPE_INTERVAL_AMOUNT_RATE.eq(model.getGrantType())) {
			detailList=fillRuleDetail(ruleDetail);
		}
		else{
			detailList=getRateCouponRuleDetailList(model,request);
		}
		rateCouponRuleService.updateRule(model, detailList);
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
	private List<RateCouponRuleDetail> getRateCouponRuleDetailList(final RateCouponRuleModel model,final HttpServletRequest request) {
		final String grantType = model.getGrantType();// 发放类型
		final List<RateCouponRuleDetail> list = new ArrayList<>();
		final String[] useTenderMoneyArray = request.getParameterValues("useTenderMoney");// 最低投资金额
		final String[] upAprArray = request.getParameterValues("upApr");// 加息利率
		RateCouponRuleDetail ratecouponRuleDetail;
		if (RateCouponRuleModel.GRANT_TYPE_FIXED_AMOUNT.equalsIgnoreCase(grantType)) {//固定值	
			for (int i = 0; i < upAprArray.length; i++){
				if(StringUtils.isBlank(upAprArray[i])){//固定金额的发放金额不能为空
					continue;
				}	
				ratecouponRuleDetail =new RateCouponRuleDetail();	
				ratecouponRuleDetail.setUpApr(BigDecimalUtils.valueOf(upAprArray[i]));
				ratecouponRuleDetail.setUseTenderMoney(BigDecimalUtils.round(BigDecimalUtils.valueOf(useTenderMoneyArray[i])));
				list.add(ratecouponRuleDetail);
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
	private List<RateCouponRuleDetail> fillRuleDetail(String details){
		JSONArray detail=JSONObject.parseArray(details);
		List<RateCouponRuleDetail> ruleDetailList = new ArrayList<>();
		ListIterator<Object> iterator= detail.listIterator();
		for(int i =0;iterator.hasNext();i++){
			iterator.next();
			String[] upApr=detail.getJSONObject(i).getString("upApr").split(",");
			String[] useTenderMoneys=detail.getJSONObject(i).getString("useTenderMoney").split(",");
			for(int j=0;j<upApr.length;j++){
				if (StringUtils.isBlank(upApr[j]) || StringUtils.isBlank(useTenderMoneys[j]) 
						|| StringUtils.isBlank(detail.getJSONObject(i).getString("tenderMin")) || StringUtils.isBlank(detail.getJSONObject(i).getString("tenderMax"))) {
					continue;
				}
				RateCouponRuleDetail item=new RateCouponRuleDetail();
				item.setUpApr(BigDecimalUtils.valueOf(upApr[j]));
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
	private void checkRule(final RateCouponRuleModel model){
		if (!(OperateEnum.ACTIVITYPLAN_SELECT_USERS.getValue().equals(model.getActivityCode())
				|| OperateEnum.ACTIVITYPLAN_VIP_LEVEL.getValue().equals(model.getActivityCode())
				|| OperateEnum.ACTIVITYPLAN_SCORE_SHOP.getValue().equals(model.getActivityCode()))
				&&(model.getTotalNum() != null && model.getTotalNum() <= 0)) {
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
				if(StringUtils.isNotBlank(projectTypeId) && rateCouponRuleService.checkGrantProjectType(projectTypeId)){
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
	@RequestMapping(value = "/operate/rateCoupon/rateCouponForProjectType", method = RequestMethod.POST)
	@ResponseBody
	public Object redRuleList( final String projectTypeId){
		RateCouponRule  rule= rateCouponRuleService.findInvestRateCouponRuleByProjectTypeId(projectTypeId);
		 Map<String,Object> result=this.renderSuccessResult();
		    result.put("ruleId", rule!=null? rule.getUuid() : "");
		  return  result;
	}
}
