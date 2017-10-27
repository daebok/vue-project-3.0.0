package com.rd.ifaes.core.bond.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.BondCollectionEnum;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.BondRuleEnum;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.AccountResource;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.account.domain.TppMerchantLog;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.mapper.BondInvestMapper;
import com.rd.ifaes.core.bond.model.BondCollectionSum;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.service.BondCollectionService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.cache.BondCache;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.sys.domain.LogTemplate;
import com.rd.ifaes.core.sys.service.LogTemplateService;
import com.rd.ifaes.core.tpp.model.jx.JxCreditInvestModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:BondInvestServiceImpl
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("bondInvestService")
public class BondInvestServiceImpl extends BaseServiceImpl<BondInvestMapper, BondInvest> implements BondInvestService {
    /**
     * 每个类的日志层面
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BondInvestServiceImpl.class);

    /**
     * TppMerchantLogService
     */
    @Resource
    private transient TppMerchantLogService tppMerchantLogService;
    /**
     * tppTradeService
     */
    @Resource
    private transient TppTradeService tppTradeService;
    /**
     * UserAuthSignLogService 业务
     */
    @Resource
    private transient UserAuthSignLogService userAuthSignLogService;

    /**
     * 获取日志模板内容的业务
     */
    @Resource
    private transient LogTemplateService logTemplateService;

    /**
     * 项目投资业务
     */
    @Resource
    private transient ProjectInvestService projectInvestService;

    /**
     * 项目还款业务
     */
    @Resource
    private transient ProjectRepaymentService projectRepaymentService;

    /**
     * UserCacheService 业务
     */
    @Resource
    private transient UserCacheService userCacheService;
    /**
     * 项目待收业务
     */
    @Resource
    private transient ProjectCollectionService projectCollectionService;

    /**
     * 用户业务
     */
    @Resource
    private transient UserService userService;

    /**
     * 项目业务
     */
    @Resource
    private transient ProjectService projectService;

    /**
     * 债权业务
     */
    @Resource
    private transient BondService bondService;

    /**
     * 债权待收业务
     */
    @Resource
    private transient BondCollectionService bondCollectionService;

    /**
     * 债权规则业务
     */
    @Resource
    private transient BondRuleService bondRuleService;

    /**
     * 用户认证业务
     */
    @Resource
    private transient UserIdentifyService userIdentifyService;

    /**
     * 资金处理业务
     */
    @Resource
    private transient AccountService accountService;
    @Resource
    private PlatformTransactionManager transactionManager;

    /**
     * 查询扩展属性的分页数据
     */
    @Override
    public Page<BondInvestModel> findPageModel(final BondInvestModel model) {
        //查询数据
        if (model.getPage() == null) {
            model.setPage(new Page<BondInvestModel>());
        }
        final List<BondInvestModel> list = dao.findModelList(model);
        final List<BondInvestModel> showList = new ArrayList<BondInvestModel>();
        final int investExpireSeconds = ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT) * 60;
        if (CollectionUtils.isNotEmpty(list)) {
            for (final BondInvestModel bi : list) {
                bi.setUserName(StringUtils.hideUserName(bi.getUserName()));
                final BigDecimal sumCapAndIn = bondCollectionService.getSumByInvestId(bi.getUuid());
                bi.setWaitMoney(BigDecimalUtils.defaultIfNull(sumCapAndIn));//待收本息
                //待支付 ---剩余时间
                if (BondInvestEnum.STATUS_INIT.eq(bi.getStatus())) {
                    final int pastSeconds = (int) DateUtils.pastSeconds(bi.getCreateTime());
                    final int remainTimes = investExpireSeconds - pastSeconds;
                    if (remainTimes > 0) {
                        bi.setRemainTimes(remainTimes);
                    } else {
                        bi.setStatus(BondInvestEnum.STATUS_OVERTIME.getValue());
                        continue;
                    }
                }
                /**
                 * 以受让列表  下列情形下不显示  转让按钮
                 * 1、超过转让次数
                 * 2、这溢价率超出限制
                 * 3、剩余期限为0
                 * 4、已经发布转让了的
                 */
                bi.setShowBond(true);
                final ProjectInvestModel investModel = projectInvestService.getProjectInvestModel(bi.getProjectInvestId());
                if (investModel != null && (investModel.judge() || bondService.judgeBondAprLimit(investModel)
                        || Integer.parseInt(bi.getRemainDays()) < Constant.INT_ZERO || InvestEnum.BOND_FLAG_PUBLISH.eq(investModel.getBondFlag()))) {
                    bi.setShowBond(false);
                }
                showList.add(bi);
            }
        }
        model.getPage().setCount(showList.size());
        model.getPage().setRows(showList);
        return model.getPage();
    }

    /**
     * 校验债权投资参数
     *
     * @param model
     * @param backUrl 校验返回地址
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private Map<String, Object> checkBondInvestParams(final BondInvestModel model, final String backUrl) {
        final Map<String, Object> map = new HashMap<>();
        //债权校验
        if (StringUtils.isBlank(model.getBondId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        } else {
            final Bond bond = bondService.getBond(model.getBondId(), backUrl);//获取缓存中的债权标信息
            map.put("bond", bond);
        }
        //原始标校验
        checkSourceProject(model, map, backUrl);
        //原始投资记录校验
        checkSourceInvest(model, map, backUrl);
        return map;
    }

    private void checkSourceInvest(final BondInvestModel model, final Map<String, Object> map, final String backUrl) {
        //原始标投资记录信息
        if (StringUtils.isBlank(model.getInvestId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_PROJECTINVEST_IDISNULL), backUrl, BussinessException.TYPE_JSON);
        } else {
            final ProjectInvest oriProjectInvest = projectInvestService.get(model.getInvestId());//获取原始投资记录信息
            if (oriProjectInvest == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_PROJECTINVEST_ISNULL), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("pi", oriProjectInvest);
            final User investUser = userService.get(oriProjectInvest.getUserId());//获取原始投资人信息
            if (investUser == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVESTUSER_ISNULL), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("investUser", investUser);
        }
    }

    private void checkSourceProject(final BondInvestModel model, final Map<String, Object> map, final String backUrl) {
        //原始标校验
        if (StringUtils.isBlank(model.getProjectId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        } else {
            final Project project = projectService.get(model.getProjectId());
            if (project == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("project", project);
            final User borrowUser = userService.get(project.getUserId());//获取原始标借款人信息
            if (borrowUser == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_BORROWUSER_NULL), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("borrowUser", borrowUser);
        }
    }


    /**
     * 校验债权可投情形 和  用户可投情形
     *
     * @param bond
     * @param user
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private void checkBond(final Bond bond, final User user, final User borrowUser, final BondInvestModel model, final String backUrl) {

        //00 -- 校验投资人准备情况（是否开户等）
        UserIdentify identify = userIdentifyService.findByUserId(user.getUuid());
        if (!Constant.FLAG_YES.equals(identify.getMobilePhoneStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_MOBILE_NOT_AUTH), backUrl, BussinessException.TYPE_JSON);
        }
        if (!Constant.FLAG_YES.equals(identify.getRealNameStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_TPP_STATUS_NOT_OPEN), backUrl, BussinessException.TYPE_JSON);
        }
        if (userAuthSignLogService.checkAuthSign() && !Constant.FLAG_YES.equals(identify.getAuthSignStatus())) {
            CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_AUTH_SIGN_STATUS), backUrl, BussinessException.TYPE_JSON);
        }

        //1.项目状态检查
        if (!BondEnum.STATUS_PUBLISH.eq(bond.getStatus())) {//不是转让中状态抛出异常
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_STATSU_ISNOTPUBLISH), backUrl, BussinessException.TYPE_JSON);
        }

        // 2. 用户投资功能是否被冻结
        UserFreezeService userFreezeService = (UserFreezeService) SpringContextHolder.getBean("userFreezeService");
        if (userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), backUrl, BussinessException.TYPE_JSON);
        }

        //3.自己不能投自己发的借款
        if (user.getUuid().equals(bond.getUserId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOTMIME), backUrl, BussinessException.TYPE_JSON);
        }

        //4、原始标借款人不能投资该债权标
        if (user.getUuid().equals(borrowUser.getUuid())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_BORROWUSER_NOTINVEST), backUrl, BussinessException.TYPE_JSON);
        }

        //5、非待支付状态  不能支付
        if (StringUtils.isNotBlank(model.getStatus()) && !BondInvestEnum.STATUS_INIT.eq(model.getStatus())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_PAY_STATUS), backUrl, BussinessException.TYPE_JSON);
        }

        //6、由于是汇付接口则必须一次性全部受让
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName()) && model.getAmount().compareTo(bond.getBondMoney()) != Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_UFX_HUIFU_LIMIT), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 债券投资封装参数
     *
     * @param model
     * @param bond
     * @param investUser
     * @param project
     * @param borrowUser
     * @param user
     * @param pi
     * @return
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private Map<String, Object> doPackageUfxCreditTransferModel(final BondInvestModel model, final Bond bond, final User investUser,
                                                                final Project project, final User borrowUser, final User user, final ProjectInvest oriProjectInvest) {
        final Map<String, Object> map = new HashMap<>();
        int portion = Integer.parseInt(CommonEnum.YES.getValue());//是否全额受让 默认是
        if (model.getAmount().compareTo(bond.getBondMoney()) == Integer.parseInt(CommonEnum.NO.getValue())) {
            portion = Integer.parseInt(CommonEnum.YES.getValue());
        } else if (model.getAmount().compareTo(bond.getBondMoney()) < Integer.parseInt(CommonEnum.NO.getValue())) {
            portion = Integer.parseInt(CommonEnum.NO.getValue());
        }
        map.put("projectId", project.getUuid());//原始标ID
        map.put("sponesorer", borrowUser.getTppUserCustId());//  借款人
        map.put("projectAmount", BigDecimalUtils.round(project.getAccount()));
        map.put("assignorCustId", investUser.getTppUserCustId());//  投资转让人
        map.put("assignAmount", BigDecimalUtils.round(model.getAmount()));
        map.put("investNo", oriProjectInvest.getInvestNo());
        map.put("investDate", oriProjectInvest.getInvestDate());
        LOGGER.info("债权投资userCustId：{}", user.getTppUserCustId());
        map.put("userCustId", user.getTppUserCustId());
        map.put("amount", BigDecimalUtils.round(model.getReceivedAccount()));//实际支付金额
        map.put("creditFee", BigDecimalUtils.round(model.getBondFee()));
        map.put("portion", portion);
        map.put("userId", user.getUserNo());//用户号  user_no
        map.put("idNo", StringUtils.EMPTY);//选填
        map.put("endAmt", StringUtils.EMPTY);//选填
        map.put("apr", StringUtils.EMPTY);//选填
        map.put("feeWay", StringUtils.EMPTY);//选填
        map.put("superviseNo", StringUtils.EMPTY);//选填
        map.put("authCode", StringUtils.EMPTY);//选填
        map.put("smsCode", StringUtils.EMPTY);//选填
        map.put("repaymentYesAccount", BigDecimalUtils.round(bondService.getRepayedMoney(project, oriProjectInvest, true)));
        map.put("phone", StringUtils.EMPTY);//选填
        map.put("reqExt", StringUtils.EMPTY);//选填
        map.put("merPriv", StringUtils.EMPTY);//选填
        return map;
    }

    /**
     * 投资的金额判断
     *
     * @param bond
     * @param model
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private void validInvestMoney(final Bond bond, final BondInvestModel model, final double currentBondAmount, final Project project) {
        //用户投资金额
        final BigDecimal investMoney = model.getAmount();
        //最低投资金额
        BigDecimal lowestMoney = bond.getBondLowestMoney();
        //剩余债权金额
        final BigDecimal remainMoney = investMoney.add(BigDecimal.valueOf(currentBondAmount));//投资金额+已经减掉投资金额的剩余债权金额
        //最高投资金额
        BigDecimal mostMoney = bond.getBondMostMoney().compareTo(remainMoney) > Constant.INT_ZERO ? remainMoney : bond.getBondMostMoney();

        //获取债权规则信息
        final BondRule rule = bondRuleService.get(bond.getRuleId());
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName()) && !BondRuleEnum.RULE_BUY_ALL.eq(rule.getBuyStyle())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_RULEBUYALLLIMIT), BussinessException.TYPE_JSON);
        }
        //转让规则校验
        checkBondRule(investMoney, lowestMoney, remainMoney, mostMoney, rule);

        if ((LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())
                || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle()))
                && investMoney.compareTo(remainMoney) != Constant.INT_ZERO) {
            //等额本息或者等额本金  必须一次性全部受让
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_MUST_BUY_ALL), BussinessException.TYPE_JSON);
        }
    }

    /**
     * 转让规则校验
     *
     * @param investMoney
     * @param lowestMoney
     * @param remainMoney
     * @param mostMoney
     * @param rule
     */
    private void checkBondRule(final BigDecimal investMoney, BigDecimal lowestMoney, final BigDecimal remainMoney,
                               BigDecimal mostMoney, final BondRule rule) {
        if (BondRuleEnum.RULE_BUY_ALL.eq(rule.getBuyStyle()) && BigDecimalUtils.validAmount(investMoney)
                && investMoney.compareTo(remainMoney) != Constant.INT_ZERO) {//一次性全额受让  投资金额必须等于剩余债权总额   无需判断最大最小
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_RULEBUYALLLIMIT), BussinessException.TYPE_JSON);
        } else if (BondRuleEnum.RULE_BUY_PART.eq(rule.getBuyStyle())) {//部分转让
            //判断   剩余金额小于2倍的最小投资金额的时候    投资金额必须是全部的剩余债权金额
            final BigDecimal doubleLowest = lowestMoney.multiply(BigDecimal.valueOf(Constant.INT_TWO));//2倍起投金额
            if (BigDecimalUtils.validAmount(doubleLowest) && doubleLowest.compareTo(remainMoney) > Constant.INT_ZERO
                    && BigDecimalUtils.validAmount(investMoney) && investMoney.compareTo(remainMoney) != Constant.INT_ZERO) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_DOUBLELOWESTLIMIT), BussinessException.TYPE_JSON);
            }

            //在投资金额有效的情形下  判断投资金额  起投金额  和 最高投资金额的判断
            if (BigDecimalUtils.validAmount(investMoney)) {
                //不能低于起投金额
                if (BigDecimalUtils.validAmount(lowestMoney) && investMoney.compareTo(lowestMoney) < Constant.INT_ZERO) {
                    throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_LOWESTLIMIT), BussinessException.TYPE_JSON);
                }
                //不能超过最高投资金额（剩余债权金额）
                if (BigDecimalUtils.validAmount(mostMoney) && investMoney.compareTo(mostMoney) > Constant.INT_ZERO) {
                    throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_MOSTLIMIT), BussinessException.TYPE_JSON);
                }
            } else {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ACCOUNTERROR), BussinessException.TYPE_JSON);
            }
        }
    }


    /**
     * 投资前期通用校验
     *
     * @param model
     * @param user
     * @param backUrl 校验返回地址
     * @return
     * @author QianPengZhan
     * @date 2016年8月25日
     */
    private Map<String, Object> checkAllInfo(final BondInvestModel model, final User user, final String backUrl) {
        //1、校验投资参数有效性
        final Map<String, Object> obj = checkBondInvestParams(model, backUrl);
        final Bond bond = (Bond) obj.get("bond");//获取债权标信息
        final User borrowUser = (User) obj.get("borrowUser");//获取原始标借款人信息
        final Project project = (Project) obj.get("project");//获取原始标信息

        //2.校验投资人准备情况（是否开户等）
        userIdentifyService.validIdentifyForInvest(user, backUrl);

        //3、校验债权产品的信息
        checkBond(bond, user, borrowUser, model, backUrl);

        //4、债权投资用户资金判断
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        final BigDecimal validMoney = userService.getValidMoneyByUser(user, userCache);
        final Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE)));
        LOGGER.info("用户userId:{},本地余额：{},第三方余额：{}", user.getUuid(), account.getUseMoney(), validMoney);
        if (account.getUseMoney().compareTo(model.getReceivedAccount()) < Constant.INT_ZERO
                || validMoney.compareTo(model.getReceivedAccount()) < Constant.INT_ZERO) {//此处拿实际支付金额和本地可用金额、第三方的可用金额进行比较
            throw new BussinessException(ResourceUtils.get(AccountResource.ACCOUNT_USEMONEY_NOTENOUGHERROR), backUrl, BussinessException.TYPE_CLOSE);
        }

        //5、校验风险评估
        model.setIsSelectedTip(Constant.INT_ONE);//提交订单默认已经选择继续投资
        validUserRiskTip(model, user, project, backUrl);
        return obj;
    }

    /**
     * 债权投资
     */
    @Override
    @Transactional(readOnly = false)
    public Object doBondInvest(final BondInvestModel model, final User user) {
        final String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + model.getBondId();
        final Map<String, Object> obj = checkAllInfo(model, user, backUrl);
        Bond bond = (Bond) obj.get("bond");//获取债权标信息
        Project project = (Project) obj.get("project");//获取原始标信息
        User borrowUser = (User) obj.get("borrowUser");//获取原始标借款人信息
        ProjectInvest projectInvest = (ProjectInvest) obj.get("pi");//获取原始投资记录信息
        User investUser = (User) obj.get("investUser");//获取原始投资人信息
        
        //判断存管方放款是否结束
//		StringBuffer sb = new StringBuffer();
//		sb.append("'").append(TppEnum.STATUS_UNDO.getValue()).append("','").append(TppEnum.STATUS_FAIL.getValue()).append("','")
//				.append(TppEnum.STATUS_QUERY.getValue()).append("'");
//		int i = tppTradeService.countByTypeAndProjectIdAndStatus(TppEnum.SERVICE_TYPE_BATCH_LOAN.getValue(), project.getUuid(),
//				sb.toString());
//		LOGGER.info("看放款是否到账:{}" , i > 0 ? "否" : "是");
//		if (i > 0 ) {
//			throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_LOAN_NOT_SUCCESS), BussinessException.TYPE_JSON);
//		}

        //1、未支付订单数校验(点击去支付  提交到第三方之前  增加一笔未支付订单数量   如果有多笔就提示 )
        if (!InvestCache.incrUserInvestUnpay(user.getTppUserCustId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_UNPAY_TOOMUCH), BussinessException.TYPE_JSON);
        }

        /**
         * 2、超投校验 判断剩余可投是否超过了债权总金额
         * 此时已经将剩余可投减掉了投资金额(点击去支付  提交到第三方之前
         * 先扣除可投份额   如果扣完小于0 就超投  )
         */
        final double currentBondAmount = BondCache.checkOverBondInvest(bond.getBondNo(), model.getAmount().doubleValue());
        if (currentBondAmount < Constant.DOUBLE_ZERO) {
            //超投抛出异常  在抛出异常之前 需要回退支付次数
            InvestCache.decrUserInvestUnpay(user.getTppUserCustId());
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ACCOUNT_NOT_ENOUGH), BussinessException.TYPE_JSON);
        }
        JxCreditInvestModel jxCreditInvestModel = null;
        try {

            //3、投资的金额判断
            validInvestMoney(bond, model, currentBondAmount, project);

            //4、加入ufx债权投资参数 进行封装
//            Map<String, Object> map = new HashMap<String, Object>();
            final BigDecimal fee = handleBondFee(model.getAmount(), bond);//中间计算一下手续费
            model.setBondFee(fee);
//            map = doPackageUfxCreditTransferModel(model, bond, investUser, project, borrowUser, user, projectInvest);

            //5、第三方交互  封装债权投资参数
            Map<String, Object> jxMap = new HashMap<>();
            jxMap.put("accountId", user.getTppUserAccId());
            jxMap.put("txAmount", BigDecimalUtils.round(model.getReceivedAccount()));
            if (fee.compareTo(BigDecimal.ZERO) > 0) {
                jxMap.put("txFee", BigDecimalUtils.round(fee));
            }
            jxMap.put("tsfAmount", BigDecimalUtils.round(model.getAmount()));
            jxMap.put("forAccountId", investUser.getTppUserCustId());
            jxMap.put("orgOrderId", projectInvest.getInvestOrderNo());
            
            //再次转让修改
            ProjectInvest oriInvest = null;
            ProjectInvest projectInvestTmp = projectInvest;
            String orgOrderId = projectInvestTmp.getInvestOrderNo();
            BigDecimal orgTxAmount = projectInvestTmp.getAmount();
            while (projectInvestTmp.getOriginInvestId() != null) {
            	oriInvest = projectInvestService.get(projectInvestTmp.getOriginInvestId());
            	if (!orgOrderId.equals(oriInvest.getInvestOrderNo())) {
            		//这里可能出现循环，所以重新赋值
            		orgTxAmount = projectInvestTmp.getAmount();
            		LOGGER.info("------------债权投资不相等，说明已经是重新投资,projectInvestTmp:{},oriInvest:{}, orgTxAmount:{}------------------", projectInvestTmp, oriInvest.getUuid(), orgTxAmount);
            		break;
            	}
            	if (oriInvest.getOriginInvestId() == null) {
            		orgTxAmount = oriInvest.getAmount();
            		LOGGER.info("------------债权投资orgTxAmount修改,oriInvest:{}, orgTxAmount:{}------------------", oriInvest.getUuid(), orgTxAmount);
            		break;
            	}
            	projectInvestTmp = oriInvest;
            }
            LOGGER.info("------------债权投资orgTxAmount修改后显示, orgTxAmount:{}------------------",  orgTxAmount);
            jxMap.put("orgTxAmount", BigDecimalUtils.round(orgTxAmount));
            jxMap.put("productId", project.getUuid());

            TppService tppService = (TppService) TppUtil.tppService();
            jxCreditInvestModel = (JxCreditInvestModel) tppService.tppCreditAssign(jxMap);
//            LOGGER.debug("投资报文组装完毕,({})", ufxCreditModel);


            //6、保存债权投资订单记录  PS:下订单即入库
            final BondInvest invest = new BondInvest();
            BeanUtils.copyProperties(model, invest);
            LOGGER.info("投资渠道：{}", invest.getInvestChannel());
            invest.preInsert();
            invest.setInvestOrderNo(jxCreditInvestModel.getOrderId());//存入投资订单号
            invest.setInvestNo(projectInvest.getInvestNo());
            invest.setInvestDate(projectInvest.getInvestDate());
            invest.setUserId(user.getUuid());
            invest.setBondFee(fee);
            this.insert(invest);

            //投资记录+1
            userCacheService.addUserInvestNum(user.getUuid());

            //7、债权提交需要修改列表缓存和详情的缓存
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND.concat(bond.getUuid()));
        } catch (Exception e) {

            //7、债权投资未跳转第三方即失败    归还债权剩余可投金额
            BondCache.incrBondRemainAccount(bond.getBondNo(), model.getAmount().doubleValue());

            //8 、回退用户未支付次数
            InvestCache.decrUserInvestUnpay(user.getTppUserCustId());

            throw e;
        }
        return jxCreditInvestModel;
    }

    /**
     * 计算单笔转让人手续费
     *
     * @param validInvestMoney 有效投资金额
     * @param bond             债权标
     * @return
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private BigDecimal handleBondFee(final BigDecimal validInvestMoney, final Bond bond) {
        BigDecimal fee = BigDecimal.ZERO;//初始手续费
        //根据债权规则计算债权手续费
        final BondRule rule = bondRuleService.get(bond.getRuleId());
        if (validInvestMoney.compareTo(BigDecimal.valueOf(rule.getFeeInitiateAmount())) < Constant.INT_ZERO) {    //手续费限制
            fee = BigDecimal.ZERO;//初始手续费
        } else {
            if (BondRuleEnum.RULE_FEE_STYLE_RATE.eq(rule.getFeeStyle())) {//按照比例收取手续费
                fee = BigDecimalUtils.mul(validInvestMoney, BigDecimal.valueOf(rule.getFeeRate())).divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED));
            } else if (BondRuleEnum.RULE_FEE_STYLE_FIXED.eq(rule.getFeeStyle())) {//按照固定金额
                fee = BigDecimal.valueOf(rule.getFeeFixed());
            } else if (BondRuleEnum.RULE_FEE_STYLE_FREE.eq(rule.getFeeStyle())) {
                fee = BigDecimal.ZERO;//初始手续费
            }
            //单笔手续费限额
            if (fee.compareTo(BigDecimal.valueOf(rule.getFeeSingleMax())) > Constant.INT_ZERO) {
                fee = BigDecimal.valueOf(rule.getFeeSingleMax());
            }
            //最终的手续费需要受到第三方的限制  会进行改变      汇付天下  --限制每笔的转让手续费不能超过承接金额的10%  若超过 则按照限制的金额 收取手续费
            if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())) {
                final BigDecimal maxFee = BigDecimalUtils.mul(validInvestMoney.divide(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED),
                        Constant.INT_TEN, BigDecimal.ROUND_HALF_DOWN), ConfigUtils.getBigDecimal(ConfigConstant.UFX_BOND_FEE_LIMIT));
                LOGGER.info("汇付天下--转让手续费限额--当前购买债权价值：{},限制不能朝超过承接金额的10%={},规则计算的手续费为：{}", validInvestMoney, maxFee, fee);
                if (fee.compareTo(maxFee) > Constant.INT_ZERO) {
                    fee = maxFee;//规则计算出来的手续费大于限制的金额  那么就收取最大限制的金额作为手续费
                }
            }
        }
        return fee;
    }

    /**
     * 处理债权投资之后的本地资金
     */
    @Override
    @Transactional(readOnly = false)
    public void handleBondInvestAccount(final Project project, final User bondUser,
                                        final User investUser, final BondInvest invest, final BigDecimal bondColMoney, final boolean fullFlag, final ProjectInvest pInvest
            , final String info) {
        final Bond bond = bondService.get(invest.getBondId());
        List<AccountModel> accountList = new ArrayList<>();
        List<AccountLog> logList = new ArrayList<>();
        //1、处理债权受让人   扣除实际支付金额  生成日志
        LOGGER.info("用户({})--支付债权投资实际金额，金额：{}------start---------", investUser.getUuid(), invest.getReceivedAccount().doubleValue());
        if (BigDecimalUtils.validAmount(invest.getReceivedAccount())) {
            //a)获取债权转让受让人投资日志 内容
            final Map<String, Object> paramBuy = new HashMap<String, Object>();
            paramBuy.put("bondBuyName", bond.getBondName());
            paramBuy.put("tenderMoney", invest.getAmount());
            paramBuy.put("realPay", invest.getReceivedAccount());
            paramBuy.put("bondCollectionMoney", bondColMoney);
            paramBuy.put("bondInfo", info);
            final String bondBuyRemark = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT,
                    Constant.BOND_BUY, paramBuy);
            //b)添加日志记录
            final AccountLog bondBuylog = new AccountLog(ConfigUtils.getValue("account_code"), Constant.BOND_BUY,
                    investUser.getUuid(), invest.getReceivedAccount(), bondBuyRemark);
            bondBuylog.setToUser(bondUser.getUuid());
            bondBuylog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
            logList.add(bondBuylog);

            //c)放入dubbo服务处理资金
            final AccountModel bondBuyAccountModel = new AccountModel(investUser.getUuid(), ConfigUtils.getValue("account_code"),
                    invest.getReceivedAccount().negate(), BigDecimal.ZERO, invest.getReceivedAccount().negate());
            accountList.add(bondBuyAccountModel);
        }
        LOGGER.info("用户({})--支付债权投资实际金额完毕 -----end---------", investUser.getUuid());

        //2、处理债权转让人
        LOGGER.info("用户({})--获得债权投资实际金额，金额：{}", bondUser.getUuid(), invest.getReceivedAccount());
        final BigDecimal capital = invest.getAmount();//本金
        final BigDecimal receivedMoney = invest.getReceivedAccount();//实际支付金额
        final BigDecimal earnMoney = BigDecimalUtils.sub(receivedMoney, capital);//赚取收益
        if (BigDecimalUtils.validAmount(capital)) {//本金
            //a)获取债权转让转让人本金获取资金日志 内容
            final Map<String, Object> paramSellCapital = new HashMap<String, Object>();
            paramSellCapital.put("capital", capital);
            paramSellCapital.put("bondInfo", info);
            final String paramSellCapitalRemark = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT,
                    Constant.BOND_SELL_CAPITAL, paramSellCapital);
            //b)添加日志记录
            final AccountLog bondSellCapitalLog = new AccountLog(ConfigUtils.getValue("account_code"), Constant.BOND_SELL_CAPITAL,
                    bondUser.getUuid(), capital, paramSellCapitalRemark);
            bondSellCapitalLog.setToUser(bondUser.getUuid());
            bondSellCapitalLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
            if (StringUtils.isBlank(pInvest.getOriginInvestId()) && CommonEnum.YES.eq(pInvest.getBondFlag())) {
                //原始债权人转出，计算红包份额
                BigDecimal redPacket = BigDecimalUtils.sub(pInvest.getAmount(), pInvest.getRealAmount());//红包收益
                if (redPacket.doubleValue() == 0.0) {//未使用红包
                    bondSellCapitalLog.setEarn(BigDecimal.ZERO);
                } else {
                    // 红包=本次转让金额*原始红包额度/原始投资金额
                    bondSellCapitalLog.setEarn(BigDecimalUtils.div(redPacket.multiply(invest.getAmount()), pInvest.getAmount()));
                }
            } else {
                bondSellCapitalLog.setEarn(BigDecimal.ZERO);
            }
            logList.add(bondSellCapitalLog);
            //c)放入dubbo服务处理资金
            final AccountModel bondSellAccountModel = new AccountModel(bondUser.getUuid(), ConfigUtils.getValue("account_code"), capital, BigDecimal.ZERO);
            accountList.add(bondSellAccountModel);
        }
        if (earnMoney.compareTo(BigDecimal.ZERO) > Constant.INT_ZERO) {//如果是正值  是收益
            //a)获取债权转让转让人赚取收益获取资金日志 内容
            final Map<String, Object> paramSellEarn = new HashMap<String, Object>();
            paramSellEarn.put("earnMoney", earnMoney);
            paramSellEarn.put("bondInfo", info);
            final String paramSellEarnRemark = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT,
                    Constant.BOND_SELL_EARN, paramSellEarn);
            //b)添加日志记录
            final AccountLog bondSellEarnLog = new AccountLog(ConfigUtils.getValue("account_code"), Constant.BOND_SELL_EARN,
                    bondUser.getUuid(), earnMoney, paramSellEarnRemark);
            bondSellEarnLog.setToUser(bondUser.getUuid());
            bondSellEarnLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
            bondSellEarnLog.setEarn(bondSellEarnLog.getMoney());
            logList.add(bondSellEarnLog);
            //c)放入dubbo服务处理资金
            final AccountModel bondSellEarnAccountModel = new AccountModel(bondUser.getUuid(), ConfigUtils.getValue("account_code"), earnMoney, BigDecimal.ZERO);
            accountList.add(bondSellEarnAccountModel);
        } else if (earnMoney.compareTo(BigDecimal.ZERO) < Constant.INT_ZERO) {//如果是负值  不是收益
            //a)获取债权转让转让人赚取收益获取资金日志 内容
            final Map<String, Object> paramSellDuct = new HashMap<String, Object>();
            paramSellDuct.put("earnMoney", earnMoney.negate());
            paramSellDuct.put("bondInfo", info);
            final String paramSellDuctRemark = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT,
                    Constant.BOND_SELL_DUCT, paramSellDuct);
            //b)添加日志记录
            final AccountLog bondSellDuctLog = new AccountLog(ConfigUtils.getValue("account_code"), Constant.BOND_SELL_DUCT,
                    bondUser.getUuid(), earnMoney.negate(), paramSellDuctRemark);
            bondSellDuctLog.setToUser(bondUser.getUuid());
            bondSellDuctLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
            logList.add(bondSellDuctLog);
            //c)放入dubbo服务处理资金
            final AccountModel bondSellDuctAccountModel = new AccountModel(bondUser.getUuid(), ConfigUtils.getValue("account_code"), earnMoney, BigDecimal.ZERO);
            accountList.add(bondSellDuctAccountModel);
        }
        LOGGER.info("用户({})--获得债权投资实际金额完毕", bondUser.getUuid());

        //3、处理转手续费
        List<TppMerchantLog> merchantLogs = new ArrayList<>();
        LOGGER.info("用户({})--支付债权转让手续费，金额：{}", bondUser.getUuid(), invest.getBondFee().doubleValue());
        if (BigDecimalUtils.validAmount(invest.getBondFee())) {
            //a)获取债权转让转让人手续费日志 内容
            final Map<String, Object> paramFee = new HashMap<String, Object>();
            paramFee.put("fee", invest.getBondFee());
            paramFee.put("bondInfo", info);
            final String bondBuyFeeRemark = logTemplateService.getTemplateContent(LogTemplate.LOG_TYPE_ACCOUNT,
                    Constant.BOND_BUY_FEE, paramFee);
            //b)添加日志记录
            final AccountLog bondBuyFeeLog = new AccountLog(ConfigUtils.getValue("account_code"), Constant.BOND_BUY_FEE,
                    bondUser.getUuid(), invest.getBondFee(), bondBuyFeeRemark);
            bondBuyFeeLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
            bondBuyFeeLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
            logList.add(bondBuyFeeLog);
            //c)放入dubbo服务处理资金
            final AccountModel bondBuyFeeAccountModel = new AccountModel(bondUser.getUuid(), ConfigUtils.getValue("account_code"),
                    invest.getBondFee().negate(), BigDecimal.ZERO);
            accountList.add(bondBuyFeeAccountModel);

            //债权投资接口 第三方自行处理了手续费给平台商户  本地需要自己添加这个记录
            final String remark = ResourceUtils.get(ResourceConstant.GLOBAL_MSG_SUCCESS);
            TppMerchantLog merchantLog = new TppMerchantLog(TppMerchantLogModel.TYPE_BOND_FEE, bondUser.getUserName(),
                    ConfigUtils.getValue(ConfigConstant.WEB_NAME), invest.getBondFee(), invest.getInvestOrderNo(), remark);
            merchantLog.setStatus(TppMerchantLogModel.STATUS_SUCEESS);
            if (merchantLog != null) {
                merchantLogs.add(merchantLog);
            }
        }
        LOGGER.info("用户({})--支付债权转让手续费完毕", bondUser.getUuid());
        //1、批量调用dubbo
        accountService.saveBatch(new AccountBatchModel(accountList, logList));

        //平台资金变动记录入库
        if (merchantLogs.size() > Constant.INT_ZERO) {
            LOGGER.info("用户({})--支付债权转让手续费完毕--平台资金变动记录入库", bondUser.getUuid());
            tppMerchantLogService.insertBatch(merchantLogs);
        }
    }


    /**
     * 插入受让投资记录  全新的记录
     *
     * @param pInvest
     * @param invest
     * @param project
     * @param capital
     * @param interest
     * @param model
     * @return
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    private ProjectInvest createProjectInvest(final ProjectInvest pInvest, final BondInvest invest, final Project project
            , final BigDecimal capital, final BigDecimal interest, final JxCreditInvestModel model) {
        ProjectInvest newProjectInvest = new ProjectInvest();
        newProjectInvest = pInvest.instance();
        newProjectInvest.setUuid(IdGen.uuid());
        newProjectInvest.setCreateTime(DateUtils.getNow());
        newProjectInvest.setUserId(invest.getUserId());//债权投资人
        newProjectInvest.setProjectId(project.getUuid());//原始投资项目ID
        newProjectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
        newProjectInvest.setMoney(capital);
        newProjectInvest.setAmount(capital);
        newProjectInvest.setRealAmount(capital);
        newProjectInvest.setPayment(BigDecimalUtils.add(capital, interest));
        newProjectInvest.setInterest(interest);
//        newProjectInvest.setWaitAmount(BigDecimalUtils.defaultIfNull(newProjectInvest.getWaitAmount()).subtract(BigDecimalUtils.defaultIfNull(newProjectInvest.getRaiseInterest())));
//        newProjectInvest.setWaitInterest(BigDecimalUtils.defaultIfNull(newProjectInvest.getWaitInterest()).subtract(BigDecimalUtils.defaultIfNull(newProjectInvest.getRaiseInterest())));
        newProjectInvest.setWaitAmount(BigDecimalUtils.add(capital, interest));//TODO
        newProjectInvest.setWaitInterest(interest);
        
        newProjectInvest.setInvestChannel(InvestEnum.INVEST_CHANNEL_PC.getValue());
        newProjectInvest.setInvestType(InvestEnum.INVEST_TYPE_WEB.getValue());
        newProjectInvest.setInvestOrderNo(model.getOrderId());//债权投资回调的订单号
        newProjectInvest.setUserFirstInvest(CommonEnum.NO.getValue());
        newProjectInvest.setRealizeFlag(InvestEnum.REALIZE_FLAG_NORMAL.getValue());
        newProjectInvest.setInvestNo(model.getOrderId());//债权投资的投资回调流水号
        newProjectInvest.setInvestDate(model.getTxDate());//债权投资的投资流水号日期
        newProjectInvest.setOriginInvestId(pInvest.getUuid());//受让人投资记录 关联  原始投资ID
        newProjectInvest.setInvestStyle(InvestEnum.INVEST_STYLE_BUY.getValue());//受让投资
        newProjectInvest.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());//正常债权 没转让
        newProjectInvest.setAddIp(invest.getAddIp());//存入投资人的IP
        newProjectInvest.setFreezeNo(null);//债权投资的没有冻结一说-----可以作为是否是债权投资的标识
        newProjectInvest.setInterestDate(pInvest.getInterestDate());
        newProjectInvest.setEndDate(pInvest.getEndDate());
        newProjectInvest.setRaiseInterest(BigDecimal.ZERO);//转让成功  受让记录对应的原始标下的投资记录加息收益为0
        newProjectInvest.setAuthCode(model.getAuthCode());
        LOGGER.info("------ 受让人生成新的原始标投资记录 ------projectInvestId:{},capital:{},interest:{},investStyle:{},bondFlag:{}",
                newProjectInvest.getUuid(), newProjectInvest.getRealAmount(), newProjectInvest.getInterest(), newProjectInvest.getInvestStyle(), newProjectInvest.getBondFlag());
        return newProjectInvest;
    }


    /**
     * 生成新的投资待收和债权待收  处理已还本息
     *
     * @param projectWorker
     * @param newProjectInvest
     * @param interestCalculator
     * @param pInvest
     * @param bond
     * @param invest
     * @param projectRepayment
     * @param repayedInterest
     * @param repayedCapital
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    private void createProAndBondCollection(final ProjectWorker projectWorker, final ProjectInvest newProjectInvest, final InterestCalculator interestCalculator, final ProjectInvest pInvest, final Bond bond, final BondInvest invest,
                                            final ProjectRepayment projectRepayment, final List<BondCollection> bcList, List<ProjectCollection> collectionList, BondCollectionSum bcs) {
    	//重新计算repayedAmount repayedInterest waitAmount waitInterest
//        BigDecimal repayedAmount = BigDecimal.ZERO;
//        BigDecimal repayedInterest = BigDecimal.ZERO;
        BigDecimal waitAmount = BigDecimal.ZERO;
        BigDecimal waitInterest = BigDecimal.ZERO;
    	
    	List<ProjectCollection> createCollectionList = projectWorker.createCollectionList(newProjectInvest, interestCalculator);
        if (CollectionUtils.isNotEmpty(createCollectionList)) {
            for (final ProjectCollection collection : createCollectionList) {
                //生成原始标下面的待收记录
                final ProjectCollection projectCollection = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(pInvest.getUuid(), collection.getPeriod());//查询当前期数的原始标对应的待收记录
                collection.setParentId(projectCollection.getUuid());
                collection.setRepayTime(projectCollection.getRepayTime());
                collection.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
                collection.setCollectionType(ProjectCollectionEnum.COLLECTION_TYPE_INVEST.getValue());
                if (collection.getPeriod() < bond.getStartPeriod()) {
                    collection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());
                    collection.setLastRepayTime(projectCollection.getLastRepayTime());
                    collection.setRepayedAmount(projectCollection.getRepayedAmount());
                    collection.setBondCapital(collection.getCapital());
                    collection.setBondInterest(collection.getInterest());
                    collection.setCollectionType(ProjectCollectionEnum.COLLECTION_TYPE_REMOVE.getValue());
                } else {
                	waitAmount = BigDecimalUtils.add(waitAmount, collection.getCapital(), collection.getInterest());
                	waitInterest = BigDecimalUtils.add(waitInterest, collection.getInterest());
                }
                collectionList.add(collection);
                LOGGER.info("------ 生成原始标下面的待收记录 ------collectionId:{},status:{},collectionType:{},待收list:{}",
                        collection.getUuid(), collection.getStatus(), collection.getCollectionType(), collectionList);
                //生成债权待收
                this.createBondCollection(collection, invest, bond, bcList, projectRepayment, bcs);
            }
            LOGGER.info("已还本金2：{},已还利息2：{}", bcs.getRepayedCapital(), bcs.getRepayedInterest());
            
            LOGGER.info("-------waitAmount:{},waitInterest:{}---------", waitAmount, waitInterest);
            newProjectInvest.setWaitAmount(waitAmount);
            newProjectInvest.setWaitInterest(waitInterest);
        }
    }

    /**
     * 生成债权待收
     *
     * @param collection
     * @param invest
     * @param bond
     * @param bcList
     * @param projectRepayment
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    private void createBondCollection(final ProjectCollection collection, final BondInvest invest, final Bond bond
            , final List<BondCollection> bcList, final ProjectRepayment projectRepayment
            , BondCollectionSum bcs) {
        final BondCollection bondCollection = new BondCollection();
        bondCollection.preInsert();
        bondCollection.setProjectId(collection.getProjectId());
        bondCollection.setInvestId(collection.getInvestId());
        bondCollection.setUserId(collection.getUserId());
        bondCollection.setBondId(invest.getBondId());
        bondCollection.setBondInvestId(invest.getUuid());
        bondCollection.setRepaymentId(projectRepayment.getUuid());
        bondCollection.setCapital(collection.getCapital());
        bondCollection.setInterest(collection.getInterest());
        bondCollection.setCollectionAmount(BigDecimalUtils.add(collection.getCapital(), collection.getInterest()));
        bondCollection.setCollectionTime(collection.getRepayTime());
        bondCollection.setLateDays(collection.getLateDays());
        bondCollection.setLateInterest(collection.getLateInterest());
        bondCollection.setPeriod(collection.getPeriod().byteValue());
        bondCollection.setStatus(BondCollectionEnum.STATUS_NOT_REPAY.getValue());
        if (collection.getPeriod() < bond.getStartPeriod()) {
            bondCollection.setStatus(BondCollectionEnum.STATUS_PAID.getValue());
            bondCollection.setBondCapital(collection.getCapital());
            bondCollection.setBondInterest(collection.getInterest());
            bondCollection.setReceivedAmount(BigDecimalUtils.add(collection.getCapital(), collection.getInterest()));
            bondCollection.setReceivedTime(DateUtils.getNow());
            bcs.setRepayedInterest(bcs.getRepayedInterest().add(collection.getInterest()));
            bcs.setRepayedCapital(bcs.getRepayedCapital().add(collection.getCapital()));
        }
        bcList.add(bondCollection);
        LOGGER.info("------ 生成债权待收记录 ------bondCollectionId:{}", bondCollection.getUuid());
    }

    /**
     * 债权资金平衡处理
     *
     * @param project
     * @param bondRule
     * @param bcList
     * @param collectionList
     * @param newProjectInvest
     * @param interest
     * @param bond
     * @param pInvest
     * @param flag
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    private void handleAccountBalance(final Project project, final BondRule bondRule, List<BondCollection> bcList
            , List<ProjectCollection> collectionList, final ProjectInvest newProjectInvest, BigDecimal interest
            , final Bond bond, final ProjectInvest pInvest, final boolean flag) {
        if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())
                || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())
                || BondRuleEnum.RULE_BUY_ALL.eq(bondRule.getBuyStyle())) {
            LOGGER.info("------  等额本息、等额本金、全部受让的时候不需要考虑资金平衡 ------");
        } else {
            if (flag) {//最后一笔受让才处理
                //查询当前债权标的所有的债权投资记录下对应的原始标下的投资记录
                BigDecimal oldInterestTotal = BigDecimal.ZERO;
                final List<ProjectInvest> list = projectInvestService.findListByBondId(bond.getUuid());
                if (CollectionUtils.isNotEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        final ProjectInvest projectInvest = list.get(i);
                        oldInterestTotal = oldInterestTotal.add(projectInvest.getInterest());
                    }
                }
                //判断前面的所有受让记录的利息之和 加上当前这笔受让记录的利息 总和  是不是 和 原始标的投资记录的额利息  一致  查看资金是否平衡
                LOGGER.info("-------前面投资记录的利息总和：{},当前受让投资记录的利息：{},原始标下的受让记录的利息：{}---------", oldInterestTotal, interest, pInvest.getInterest());
                final BigDecimal diffMoney = oldInterestTotal.add(interest).subtract(pInvest.getInterest());
                if (diffMoney.compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
                    LOGGER.info("-------资金差额：{} ,资金平衡---------", diffMoney);
                } else {
                    LOGGER.info("-------资金差额：{} ,资金不平衡,此时，处理待收记录和债权待收记录---------", diffMoney);
                    if (CollectionUtils.isNotEmpty(collectionList)) {
                        for (int j = 0; j < collectionList.size(); j++) {
                            final ProjectCollection collection = collectionList.get(j);
                            if (j == collectionList.size() - 1) {
                                collection.setInterest(collection.getInterest().add(diffMoney));
                                collection.setPayment(collection.getPayment().add(diffMoney));
                            }
                        }
                    }
                    for (int i = 0; i < bcList.size(); i++) {
                        final BondCollection bCollection = bcList.get(i);
                        if (i == bcList.size() - 1) {
                            bCollection.setInterest(bCollection.getInterest().subtract(diffMoney));
                        }
                    }
                    newProjectInvest.setInterest(newProjectInvest.getInterest().add(diffMoney));
                    newProjectInvest.setPayment(newProjectInvest.getPayment().add(diffMoney));
                    interest = interest.add(diffMoney);
                }
            }
        }
    }

    /**
     * 受让人生成新的原始标投资记录
     */
    @Override
    @Transactional(readOnly = false)
    public BigDecimal createProjectInvestAndProjectCollection(final BondInvest invest, final JxCreditInvestModel model, final boolean flag) {
        //1、必要信息的准备
        final Bond bond = bondService.get(invest.getBondId());
        final BondRule bondRule = bondRuleService.get(bond.getRuleId());//债权规则
        final ProjectInvest pInvest = projectInvestService.get(invest.getInvestId());//找到最原始的投资记录
        final ProjectRepayment projectRepayment = projectRepaymentService.getRepayByProject(pInvest.getProjectId());//找到待还记录信息
        final Project project = projectService.get(pInvest.getProjectId());//找到原始项目
        final ProjectWorker projectWorker = new ProjectWorker(project);
        final BigDecimal repayedMoney = bondService.getRepayedMoney(project, pInvest, false);//已还本金
        //*******注意等额本息和等额本金的时候 可能会还了一部分的本金
        BigDecimal investMoney = invest.getAmount();
        if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle()) || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())) {
            investMoney = investMoney.add(repayedMoney);
        }
        //根据投资金额  计算待收本金和利息
        final InterestCalculator interestCalculator = projectWorker.interestCalculator(investMoney, project.getApr(), BigDecimal.ZERO, pInvest.getCreateTime());//转让 加息收益为0
        BigDecimal capital = investMoney;//本金
        BigDecimal interest = interestCalculator.repayInterest();//利息
        LOGGER.info("受让投资对应的原始标投资记录：本金：{},利息：{},已还本金：{}", capital, interest, repayedMoney);

        //2、插入受让投资记录  全新的记录
        final ProjectInvest newProjectInvest = this.createProjectInvest(pInvest, invest, project, capital, interest, model);

        //3、根据投资记录生成待收记录    重新生成原始标待收记录   几期就是几条   生成的是受让待收
        BondCollectionSum bcs = new BondCollectionSum(BigDecimal.ZERO, BigDecimal.ZERO);
        LOGGER.info("已还本金1：{},已还利息1：{}", bcs.getRepayedCapital(), bcs.getRepayedInterest());
        List<BondCollection> bcList = new ArrayList<BondCollection>();
        List<ProjectCollection> collectionList = new ArrayList<ProjectCollection>();
        this.createProAndBondCollection(projectWorker, newProjectInvest, interestCalculator, pInvest, bond, invest, projectRepayment, bcList, collectionList, bcs);

        //4、债权受让的时候 资金平衡校验
        handleAccountBalance(project, bondRule, bcList, collectionList, newProjectInvest, interest, bond, pInvest, flag);

        //5、入库处理
        //生成债权投资对应的原始标投资记录  然后更新债权投资记录中的对应的project_invest_id
        dao.updateBondInvestProjectInvestId(newProjectInvest.getUuid(), invest.getUuid());
        projectInvestService.insert(newProjectInvest);
        LOGGER.info("原始标下的待收:{},债权待收列表:{}", collectionList, bcList);
        projectCollectionService.insertBatch(collectionList);
        bondCollectionService.insertBatch(bcList);

        //6、日志中的最终的待收本息处理
        LOGGER.info("已还本金3：{},已还利息3：{}", bcs.getRepayedCapital(), bcs.getRepayedInterest());
        LOGGER.info("原来的本金：{},利息：{}", capital, interest);
        interest = interest.subtract(bcs.getRepayedInterest());//如果有还款 要减掉已经还款的金利息
        capital = capital.subtract(bcs.getRepayedCapital());
        LOGGER.info("已还本金5：{},已还利息：{},现在的本金：{},利息：{}", bcs.getRepayedCapital(), bcs.getRepayedInterest(), capital, interest);
        return BigDecimalUtils.add(capital, interest);//扣除已还本金和以还利息的最后的待收本息
    }

    /**
     * 根据ufx订单号查询债权投资
     */
    @Override
    public BondInvest getBondInvestByOrderNo(final String orderNo) {
        return dao.getBondInvestByOrderNo(orderNo);
    }

    /**
     * 获取债权标扩展信息
     */
    @Override
    public BondModel getBondModel(final Bond bond, final Project project) {
        final BondModel model = bond.prototype();
        model.setApr(project.getApr());
        model.setTimeLimit(project.getTimeLimit());
        model.setRepayStyle(project.getRepayStyle());
        model.setRemainMoney(BigDecimalUtils.sub(model.getBondMoney(), BigDecimalUtils.defaultIfNull(bond.getSoldCapital())));
        model.setBorrowStatus(project.getStatus());
        final int noRepayPeriod = projectRepaymentService.getRepayPeriodsByProjectIdAndStatus(project.getUuid(), CommonEnum.NO.getValue());
        final int repayedPeriod = projectRepaymentService.getRepayPeriodsByProjectIdAndStatus(project.getUuid(), CommonEnum.YES.getValue());
        model.setTotalPeriod(repayedPeriod + noRepayPeriod);//总期数  = 借款期限数
        model.setRepayedPeriod(repayedPeriod);//已经还了的期数
        model.setLastRepayTime(model.getBondEndTime());
        return model;
    }


    /**
     * 转让人原始标投资记录和原始标待收 作废
     */
    @Override
    public void cancleOriProjectInvestAndProjectCollection(final ProjectInvest projectInvest,
                                                           final User bondUser, final boolean flag) {
        //1、原始标投资记录作废
        this.cancelProjectInvest(projectInvest, flag);

        //2、原始标待收作废
        cancleProjectCollection(projectInvest, bondUser, flag);
    }


    /**
     * 作废投资记录
     *
     * @param projectInvest
     * @param flag
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    public void cancelProjectInvest(final ProjectInvest projectInvest, final boolean flag) {
        final ProjectInvest newProjectInvest = new ProjectInvest();
        newProjectInvest.setUuid(projectInvest.getUuid());
        newProjectInvest.setStatus(InvestEnum.STATUS_DELETE.getValue());//投资状态改为  投资作废状态 4
        newProjectInvest.setEndDate(DateUtils.getToday());
        if (flag) {//若转让的时候拆分作废   只需要状态作废   投资类型不变
            newProjectInvest.setInvestStyle(projectInvest.getInvestStyle());
        } else {//受让投资的时候作废 说明就是转让投资 不仅状态作废  类型变为转让投资类型
            newProjectInvest.setInvestStyle(InvestEnum.INVEST_STYLE_SELL.getValue());
        }
        projectInvestService.update(newProjectInvest);
        LOGGER.info("------ 原始标投资记录作废------作废的投资记录：projectInvestId:{},status:{},bondflag:{},investStyle{}",
                projectInvest.getUuid(), projectInvest.getStatus(), projectInvest.getBondFlag(), projectInvest.getInvestStyle());
    }

    /**
     * 原始标待收作废
     *
     * @param projectInvest
     * @param bondUser
     * @param flag
     * @author QianPengZhan
     * @date 2016年11月30日
     */
    private void cancleProjectCollection(final ProjectInvest projectInvest, final User bondUser, final boolean flag) {
        final ProjectCollection projectCollection = new ProjectCollection();
        projectCollection.setInvestId(projectInvest.getUuid());
        projectCollection.setUserId(bondUser.getUuid());
        projectCollection.setCollectionTypeSet(new String[]{ProjectCollectionEnum.COLLECTION_TYPE_COMMON.getValue(),
                ProjectCollectionEnum.COLLECTION_TYPE_INVEST.getValue()});//查询是普通待收和转让人待收
        projectCollection.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());//未还
        final List<ProjectCollection> pcList = projectCollectionService.findList(projectCollection);
        if (CollectionUtils.isNotEmpty(pcList)) {
            for (final ProjectCollection lPtCollection : pcList) {
                if (flag) {//拆分的时候待收类型不变化
                    lPtCollection.setCollectionType(lPtCollection.getCollectionType());
                } else {//投资成功的时候将其真正的改为转让待收
                    lPtCollection.setCollectionType(ProjectCollectionEnum.COLLECTION_TYPE_BOND.getValue());//转让投资对应转让待收
                }
                if (ProjectCollectionEnum.STATUS_PAID.getValue().equals(lPtCollection.getStatus())) {
                	 LOGGER.info("-----本身已还，不做处理----------------");
                } else {
                	lPtCollection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());//已还
                    lPtCollection.setLastRepayTime(DateUtils.getNow());
                    lPtCollection.setRepayedAmount(lPtCollection.getCapital().add(lPtCollection.getInterest()));
                }
                
                LOGGER.info("------ 原始标待收作废  ------作废的待收记录：collectionId:{},collectiontype:{},lastRepaytime:{},repayedAmount{}",
                        lPtCollection.getUuid(), lPtCollection.getCollectionType(), lPtCollection.getStatus(),
                        lPtCollection.getLastRepayTime(), lPtCollection.getRepayedAmount());
            }
            projectCollectionService.updateBatch(pcList);
        }
    }


    /**
     * 修改转让投资状态
     */
    @Override
    public void updateBondInvstStatus(final String investId, final String newStatus,
                                      final String oldStatus) {
        dao.updateBondInvstStatus(investId, newStatus, oldStatus);
    }

    /**
     * 债权投资订单超时处理
     */
    @Override
    public void bondInvestOverTimeHandle() {
        //债权订单超时时间必须大于10分钟
        final int iExpireSec = ConfigUtils.getInt(ConfigConstant.INVEST_TIMEOUT, ConfigConstant.INVEST_DEFAULT_ORDER_TIME_OUT) * Constant.INT_SIXTEN;
        final BondInvest model = new BondInvest();
        model.setStatus(BondInvestEnum.STATUS_INIT.getValue());
        final List<BondInvest> list = dao.findList(model);
        final List<BondInvest> newList = new ArrayList<BondInvest>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (final BondInvest bondInvest : list) {
                if (iExpireSec > Constant.INT_ZERO && DateUtils.pastSeconds(bondInvest.getCreateTime()) >= iExpireSec) {
                    itemBondInvestOverTimeHandle(bondInvest, newList);
                }
            }
            if (CollectionUtils.isNotEmpty(newList)) {
                dao.updateBatch(newList);//批量修改投资状态为超时
            }
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);//清除列表缓存
        }
    }

    /**
     * 债权投资订单超时处理
     *
     * @param bondInvest
     * @param newList
     */
    private void itemBondInvestOverTimeHandle(BondInvest bondInvest, List<BondInvest> newList) {
        try {
            //修改投资状态
            bondInvest.setStatus(BondInvestEnum.STATUS_OVERTIME.getValue());
            //归还债权标的剩余债权金额
            final Bond bond = bondService.getBond(bondInvest.getBondId(), StringUtils.EMPTY);
            final double remainMoney = BondCache.incrBondRemainAccount(bond.getBondNo(), bondInvest.getAmount().doubleValue());
            if (remainMoney > Constant.DOUBLE_ZERO) {//投资中
                bondService.updateStage(Integer.parseInt(BondEnum.STAGE_INVESTING.getValue()), bond.getUuid());
                CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND.concat(bond.getUuid()));//清除单个债权项目缓存
            }
            LOGGER.info("订单超时bondInvest:{}，订单号：{},剩余可投变为：{}", bondInvest.getBondId(), bondInvest.getInvestOrderNo(), remainMoney);

            //在投记录-1
            userCacheService.subUserInvestNum(bond.getUserId());
            newList.add(bondInvest);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

    }

    /**
     * 去支付  业务处理
     */
    @Override
    public Object doBondPay(final BondInvestModel model, final User user, final String backUrl) {
        //1、校验投资参数有效性
        final Map<String, Object> obj = checkAllInfo(model, user, backUrl);
        final Bond bond = (Bond) obj.get("bond");//获取债权标信息
        final Project project = (Project) obj.get("project");//获取原始标信息
        final User borrowUser = (User) obj.get("borrowUser");//获取原始标借款人信息
        final ProjectInvest projectInvest = (ProjectInvest) obj.get("pi");//获取原始投资记录信息
        final User investUser = (User) obj.get("investUser");//获取原始投资人信息

        //2、封装Map
        Map<String, Object> map = new HashMap<String, Object>();
        map = doPackageUfxCreditTransferModel(model, bond, investUser, project, borrowUser, user, projectInvest);

        //3、第三方交互  封装债权投资参数
//        TppService tppService = (TppService) TppUtil.tppService();
//        final UfxCreditTransferModel ufxCreditModel = (UfxCreditTransferModel) tppService.tppCreditAssign(map);
//        LOGGER.debug("投资报文组装完毕,({})", ufxCreditModel);
      //5、第三方交互  封装债权投资参数
        final BigDecimal fee = handleBondFee(model.getAmount(), bond);//中间计算一下手续费
        model.setBondFee(fee);
        
        JxCreditInvestModel jxCreditInvestModel = null;
        Map<String, Object> jxMap = new HashMap<>();
        jxMap.put("accountId", user.getTppUserAccId());
        jxMap.put("txAmount", BigDecimalUtils.round(model.getReceivedAccount()));
        if (fee.compareTo(BigDecimal.ZERO) > 0) {
            jxMap.put("txFee", BigDecimalUtils.round(fee));
        }
        jxMap.put("tsfAmount", BigDecimalUtils.round(model.getAmount()));
        jxMap.put("forAccountId", investUser.getTppUserCustId());
        jxMap.put("orgOrderId", projectInvest.getInvestOrderNo());
        jxMap.put("orgTxAmount", BigDecimalUtils.round(projectInvest.getAmount()));
        jxMap.put("productId", project.getUuid());

        TppService tppService = (TppService) TppUtil.tppService();
        jxCreditInvestModel = (JxCreditInvestModel) tppService.tppCreditAssign(jxMap);

        //4、旧订单   从待支付状态改为投资作废
        dao.updateBondInvstStatus(model.getUuid(), BondInvestEnum.STATUS_DELETE.getValue(), BondInvestEnum.STATUS_INIT.getValue());

        //5、生成新的投资记录  状态待支付
        final BondInvest invest = new BondInvest();
        BeanUtils.copyProperties(model, invest);
        invest.setUuid(IdGen.uuid());
        invest.setStatus(BondInvestEnum.STATUS_INIT.getValue());
        invest.setInvestOrderNo(jxCreditInvestModel.getOrderId());//存入投资订单号
        dao.insert(invest);

        return jxCreditInvestModel;
    }

    /**
     * 风险评估校验
     */
    @Override
    public void validUserRiskTip(final BondInvestModel invest, final User user, final Project project, final String backUrl) {
        // 风险评估判断
        final UserCache userCache = userCacheService.findByUserId(user.getUuid());
        if (StringUtils.isBlank(userCache.getRiskLevel())) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_RISK_LEVEL_IS_EMPTY), Constant.USER_RISK_PAPERS_URL, Constant.USER_RISK_PAPERS_CONTENT);
        } else if (StringUtils.isNotBlank(project.getRiskLevel())
                && (NumberUtils.toInt(project.getRiskLevel()) - Constant.INT_ONE > NumberUtils.toInt(userCache.getRiskLevel()))
                && (invest.getIsSelectedTip() == Constant.INT_ZERO)) {
            // 风险承受能力不足
            throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_RISK_LEVEL_TOO_LOW), backUrl, BussinessException.TYPE_JSON);
        }
    }

    @Override
    public BondInvest getLastBondInvestByBondId(final String bondId, final String status) {
        return dao.getLastBondInvestByBondId(bondId, status);
    }

    /**
     * 根据投资日期统计投资订单
     *
     * @param investDate
     * @return
     */
    @Override
    public List<BondInvest> findByInvestDate(String investDate) {
        return dao.findByInvestDate(investDate, new String[]{BondInvestEnum.STATUS_COMPLETE.getValue(), BondInvestEnum.STATUS_DELETE.getValue()});
    }

    @Override
    public int updateInvestIdByUuid(String investId, String uuid) {
        return dao.updateInvestIdByUuid(investId, uuid);
    }


}