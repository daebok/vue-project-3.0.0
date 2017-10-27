package com.rd.ifaes.core.bond.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.BondInvestEnum;
import com.rd.ifaes.common.dict.BondRuleEnum;
import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.DictTypeEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.UserResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.Bond;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.domain.BondRule;
import com.rd.ifaes.core.bond.mapper.BondMapper;
import com.rd.ifaes.core.bond.model.BondInvestModel;
import com.rd.ifaes.core.bond.model.BondModel;
import com.rd.ifaes.core.bond.model.BondRuleModel;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondRuleService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.cache.BondCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.DictUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.model.ProtocolModel;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserIdentify;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 债权标业务处理类
 *
 * @author QianPengZhan
 * @version 3.0
 * @date 2016年7月28日
 */
@Service("bondService")
public class BondServiceImpl extends BaseServiceImpl<BondMapper, Bond> implements BondService {

    /**
     * BondServiceImpl的日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BondServiceImpl.class);

    /**
     * 构造器
     */
    public BondServiceImpl() {
        super();
    }

    /**
     * 协议业务
     */
    @Resource
    private transient ProtocolService protocolService;
    /**
     * 认证业务
     */
    @Resource
    private UserIdentifyService userIdentifyService;

    /**
     * 用户业务类
     */
    @Resource
    private transient UserService userService;

    /**
     * 风险等级业务类
     */
    @Resource
    private transient LevelConfigService levelConfigService;

    /**
     * 数据字段业务类
     */
    @Resource
    private transient DictDataService dictDataService;

    /**
     * 债权规则业务类
     */
    @Resource
    private transient BondRuleService bondRuleService;

    /**
     * 债权投资业务类
     */
    @Resource
    private transient BondInvestService bondInvestService;

    /**
     * 原始标投资业务类
     */
    @Resource
    private transient ProjectInvestService projectInvestService;

    /**
     * 原始标待收业务类
     */
    @Resource
    private transient ProjectCollectionService projectCollectionService;

    /**
     * 原始标待还业务类
     */
    @Resource
    private transient ProjectRepaymentService projectRepaymentService;

    /**
     * 借款标业务类
     */
    @Resource
    private transient BorrowService borrowService;

    /**
     * 项目业务类
     */
    @Resource
    private transient ProjectService projectService;

    /**
     * 项目类型业务类
     */
    @Resource
    private transient ProjectTypeService projectTypeService;
    /**
     * UserFreezeService业务类
     */
    @Resource
    private transient UserFreezeService userFreezeService;
    
    @Resource
    private transient TppTradeService tppTradeService;


    @Override
    public Bond get(final String id) {
        return dao.get(id);
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_BOND_CONDITION, expire = ExpireTime.FIVE_MIN)//债权专区  筛选条件缓存   时效时间 5分钟
    public Map<String, Object> queryBondCondition() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(BondEnum.BOND_TYPE_BONDMONEYTYPE.getValue(), dictDataService.findListByDictType(BondEnum.BOND_TYPE_BONDMONEYTYPE.getValue()));
        map.put(BondEnum.BOND_TYPE_REMAINDAYSTYPE.getValue(), dictDataService.findListByDictType(BondEnum.BOND_TYPE_REMAINDAYSTYPE.getValue()));
        map.put(BondEnum.BOND_TYPE_APRTYPE.getValue(), dictDataService.findListByDictType(BondEnum.BOND_TYPE_APRTYPE.getValue()));
        map.put(BondEnum.BOND_TYPE_REPAYSTYLETYPE.getValue(), dictDataService.findListByDictType(BondEnum.BOND_TYPE_REPAYSTYLETYPE.getValue()));
        return map;
    }

    @Override
    @Cacheable(key = CacheConstant.KEY_BOND_LIST, expire = ExpireTime.FIVE_MIN)//债权列表缓存  时效时间：5分钟
    public Page<BondModel> findPageData(final BondModel bond) {
        bond.setStatusSet(new String[]{BondEnum.STATUS_PUBLISH.getValue(), BondEnum.STATUS_COMPLETE.getValue()});
        return this.findModelPage(bond);
    }

    @Override
    public Page<BondModel> findModelPage(final BondModel bond) {
        final List<BondModel> list = dao.findModelList(bond);
        bond.getPage().setRows(list);
        return bond.getPage();
    }

    /**
     * 根据债权规则和原始标的最小受让金额限制去算出  债权的最小起投金额   转让限制金额
     *
     * @param rule
     * @param project
     * @return
     * @author QianPengZhan
     * @date 2016年8月4日
     */
    public Map<String, Object> getBondLimitMoney(final BondRule rule, final Project project, final Bond bond, final ProjectInvest projectInvest
            , final String backUrl) {
        //1、初始化参数
        final Map<String, Object> map = new HashMap<String, Object>();// 返回的map
        BigDecimal buyLowestMoney = BigDecimal.ZERO;// 受让最低金额
        BigDecimal sellLimitMoney = BigDecimal.ZERO;// 转让最低金额
        final BigDecimal bondMoney = BigDecimalUtils.defaultIfNull(bond.getBondMoney());// 债权转让金额（用户录入可能未录入）
        final BigDecimal oriLowest = BigDecimalUtils.defaultIfNull(project.getLowestAccount());// 原始标的起投金额
        final BigDecimal oriTenderMoney = BigDecimalUtils.defaultIfNull(projectInvest.getAmount());// 债权可转让本金（原始标投资金额）
        final BigDecimal setLowestMoney = BigDecimal.valueOf(rule.getBuyAmountMin());// 债权规则设定的最低受让金额
        final String repayStyle = project.getRepayStyle();//原始标还款方式
        final int sellMul = rule.getSellMultiple();// 受让倍率

        //2、开始计算
        if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(repayStyle) || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(repayStyle)) {
            LOGGER.info("--债权规则之外的处理 -------等额本息 等额本金不受规则限制  一律全部转让 全部受让，并且这里才存在扣除已还本金的情形");
            //剩余多少转出多少，且一次性转出;当投资后没有还款，即转出投资额；当投资后有还款，即转出投资额 - 已还部分
            buyLowestMoney = BigDecimalUtils.sub(oriTenderMoney, this.getRepayedMoney(project, projectInvest, false));// 最低转让金额为oriTenderMoney
            sellLimitMoney = buyLowestMoney;// 最低受让金额为oriTenderMoney
        } else {
            LOGGER.info("--债权规则的处理 ，不存在会扣除已还本金的情形-------start");
            if (BondRuleEnum.RULE_SELL_ALL.eq(rule.getSellStyle()) && BondRuleEnum.RULE_BUY_ALL.eq(rule.getBuyStyle())) {
                LOGGER.info("--债权规则的处理 ----一次性全额转让,一次性全额受让的情形下,最低转让金额和最低受让金额 全为原始标投资金额");
                buyLowestMoney = oriTenderMoney;
                sellLimitMoney = oriTenderMoney;
            } else if (BondRuleEnum.RULE_SELL_ALL.eq(rule.getSellStyle()) && BondRuleEnum.RULE_BUY_PART.eq(rule.getBuyStyle())) {
                LOGGER.info("--债权规则的处理 ----一次性全额转让,部分受让的情形下");
                sellLimitMoney = oriTenderMoney;
                if (oriLowest.compareTo(setLowestMoney) > Constant.INT_ZERO) {
                    if (oriTenderMoney.compareTo(BigDecimalUtils.mul(BigDecimal.valueOf(rule.getSellMultiple()), oriLowest)) > Constant.INT_ZERO) {
                        buyLowestMoney = oriTenderMoney;
                    } else {
                        buyLowestMoney = BigDecimalUtils.mul(BigDecimal.valueOf(rule.getSellMultiple()), oriLowest);
                    }
                } else {
                    if (oriTenderMoney.compareTo(BigDecimalUtils.mul(BigDecimal.valueOf(rule.getSellMultiple()), setLowestMoney))
                            > Constant.INT_ZERO) {
                        buyLowestMoney = oriTenderMoney;
                    } else {
                        buyLowestMoney = BigDecimalUtils.mul(BigDecimal.valueOf(rule.getSellMultiple()), setLowestMoney);
                    }
                }
            } else if (BondRuleEnum.RULE_SELL_PART.eq(rule.getSellStyle()) && BondRuleEnum.RULE_BUY_ALL.eq(rule.getBuyStyle())) {
                LOGGER.info("--债权规则的处理 ----部分转让,一次性全额受让的情形下");
                if (oriTenderMoney.compareTo(BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO) {//a < 2x
                    sellLimitMoney = oriTenderMoney;//最低转让金额
                    buyLowestMoney = oriTenderMoney;//最低受让金额
                    LOGGER.info("--债权规则的处理 ----部分转让,一次性全额受让的情形下，第一次");
                } else {// a > 2x
                    sellLimitMoney = BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul));
                    LOGGER.info("--债权规则的处理 ----部分转让,一次性全额受让的情形下，第二次");
                    if (bondMoney.compareTo(sellLimitMoney) < Constant.INT_ZERO) {
                        throw new BussinessException(ResourceUtils.get(BondResource.BONDMONEY_LESS_LOWEST), backUrl, BussinessException.TYPE_JSON);
                    }
                    buyLowestMoney = bondMoney;//最低受让金额
                }
            } else if (BondRuleEnum.RULE_SELL_PART.eq(rule.getSellStyle())
                    && BondRuleEnum.RULE_BUY_PART.eq(rule.getBuyStyle())) {
                LOGGER.info("--债权规则的处理 ----部分转让,部分受让的情形下");
                if (oriLowest.compareTo(setLowestMoney) >= 0) {//条件一   x>z，最低受让金额为x
                    if (oriTenderMoney.compareTo(BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO) {//a < 2x
                        sellLimitMoney = oriTenderMoney;
                        buyLowestMoney = oriTenderMoney;
                        LOGGER.info("--债权规则的处理 ----部分转让,部分受让的情形下, 第一个 oriTenderMoney.compareTo(BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO");
                    } else {
                        sellLimitMoney = BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul));
                        buyLowestMoney = oriLowest;
                        LOGGER.info("--债权规则的处理 ----部分转让,部分受让的情形下, 第二个 不是  oriTenderMoney.compareTo(BigDecimalUtils.mul(oriLowest, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO");
                    }
                } else {//条件一   x<z，最低受让金额为z
                    if (oriTenderMoney.compareTo(BigDecimalUtils.mul(setLowestMoney, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO) {//a < 2x
                        sellLimitMoney = oriTenderMoney;
                        buyLowestMoney = oriTenderMoney;
                        LOGGER.info("--债权规则的处理 ----部分转让,部分受让的情形下, 第三个 oriTenderMoney.compareTo(BigDecimalUtils.mul(setLowestMoney, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO");
                    } else {
                        sellLimitMoney = BigDecimalUtils.mul(setLowestMoney, BigDecimal.valueOf(sellMul));
                        buyLowestMoney = setLowestMoney;
                        LOGGER.info("--债权规则的处理 ----部分转让,部分受让的情形下, 第四个 不是  oriTenderMoney.compareTo(BigDecimalUtils.mul(setLowestMoney, BigDecimal.valueOf(sellMul))) <= Constant.INT_ZERO");
                    }
                }
            }
            LOGGER.info("--债权规则的处理 -------end");
        }
        LOGGER.info("--债权规则的处理 -------sellLimitMoney:{}, buyLowestMoney:{}", sellLimitMoney, buyLowestMoney);

        map.put("sellLimitMoney", sellLimitMoney);
        map.put("buyLowestMoney", buyLowestMoney);
        return map;
    }

    /**
     * 校验折溢价率的超出限制
     *
     * @param ruleMap
     * @author QianPengZhan
     * @date 2016年11月28日
     */
    private void validBondAprError(final Map<String, Object> ruleMap, final String backUrl) {
        final boolean bondAprError = (boolean) ruleMap.get("bondAprError");
        if (bondAprError) {
            //当后台设置的溢价范围为[a,b],而债权本身最高溢价率c=剩余期限利息（1-利息管理费）/受让本金，c<a时不允许转让，此时进入转让设置页面时给予提示“您的债权溢价率超出了限制范围，不允许转让”
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_MAXAPR_LOW_RULE), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 获取最新的债权规则并校验
     *
     * @param backUrl
     * @return
     * @author QianPengZhan
     * @date 2016年12月1日
     */
    private BondRuleModel getBondRule(final String backUrl) {
        final BondRuleModel bondRule = bondRuleService.getRecentBondRuleModel();//获取当前最新的债权规则
        if (bondRule == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        return bondRule;
    }

    /**
     * 校验原始标是否已经还款
     *
     * @param project
     * @param backUrl
     * @author QianPengZhan
     * @date 2016年12月1日
     */
    private void validIsRepayed(final Project project, final String backUrl) {
        if (!LoanEnum.STATUS_REPAY_START.eq(project.getStatus())) {
            throw new BussinessException(ResourceUtils.get(BondResource.NOT_REPAY_START), backUrl, BussinessException.TYPE_JSON);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> handleBondSet(final String id, final String backUrl) {
        //1、获取信息和校验
        final ProjectInvest projectInvest = projectInvestService.getProjectInvest(id, backUrl);//查询投资记录   获取债权价值;
        final BondRuleModel bondRule = this.getBondRule(backUrl);
        final Borrow borrow = borrowService.getBorrow(projectInvest.getProjectId(), backUrl);//根据项目ID查询借款标信息
        final Project project = projectService.getProjectValid(projectInvest.getProjectId(), backUrl);//获取项目信息
        final ProjectInvestModel model = projectInvest.prototype();
        final Map<String, Object> dataMap = model.getAbleBondData(model);//获取可转让的某些特定的信息
        this.validBondRule(dataMap, bondRule, borrow, projectInvest, backUrl);//校验债权规则
        this.validIsRepayed(project, backUrl);//校验原始标是否已经还款

        //2、录入参数可转让债权
        final Map<String, Object> data = new HashMap<String, Object>();
        final BigDecimal repayedCapital = this.getRepayedMoney(project, projectInvest, false);
        data.put("bondValue", projectInvest.getAmount().subtract(repayedCapital));//可转让债权  (录入最大最小折溢价率用的到)   这个金额为投资金额-已还本金
        data.put("repayedCapital", repayedCapital);//已还本金

        //3、录入最大、最小折溢价率 和开始期数
        final Map<String, Object> map = this.getParamsByCurrentRule(dataMap, data, bondRule);
        this.validBondAprError(map, backUrl);

        //4、录入协议模板id
        data.put("protocolId", this.getBondProtocolId());

        //5、录入其他信息
        boolean isSellAll = BondRuleEnum.RULE_SELL_ALL.eq(bondRule.getSellStyle());
        if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle()) || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())) {
            //若原始标是等额本息和等额本金的  必须一次性全部转让 无视债权规则
            isSellAll = true;
        }
        data.put("isSellAll", isSellAll);//是否全部转让
        data.put("projectId", projectInvest.getProjectId());//隐藏域   项目ID
        data.put("investId", projectInvest.getUuid());//隐藏域   原始标投资ID
        data.put("userId", projectInvest.getUserId());//隐藏域   转让人用户ID
        data.put("ruleId", bondRule.getUuid());//隐藏域中的规则ID
        return data;
    }

    /**
     * 获取对应投资记录的已还本金
     *
     * @param project
     * @param pInvest
     * @return
     * @author QianPengZhan
     * @date 2016年10月14日
     */
    @Override
    public BigDecimal getRepayedMoney(final Project project, final ProjectInvest pInvest, final boolean isPart) {
        //计算已还的本金
        BigDecimal repayedCapital = BigDecimal.ZERO;
        String[] collection = new String[]{ProjectCollectionEnum.COLLECTION_TYPE_COMMON.getValue(),
                ProjectCollectionEnum.COLLECTION_TYPE_INVEST.getValue(),
                ProjectCollectionEnum.COLLECTION_TYPE_REMOVE.getValue()};
        if (isPart) {
            collection = new String[]{ProjectCollectionEnum.COLLECTION_TYPE_COMMON.getValue(),
                    ProjectCollectionEnum.COLLECTION_TYPE_INVEST.getValue()};
        }
        if (LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle()) || LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())) {
            repayedCapital = BigDecimalUtils.defaultIfNull(projectCollectionService.getRepayedAccountTotal(project.getUuid(),
                    ProjectCollectionEnum.STATUS_PAID.getValue(), pInvest.getUuid(), collection));
        }
        LOGGER.info("已还款本金：{}", repayedCapital);
        return repayedCapital;
    }

    /**
     * 获取债权协议ID
     *
     * @return
     * @author QianPengZhan
     * @date 2016年10月9日
     */
    private String getBondProtocolId() {
        String protocolId = StringUtils.EMPTY;
        final List<Protocol> protocolList = protocolService.getProtocolListByType(Constant.PROTOCOL_TYPE_BOND_INVEST);
        if (CollectionUtils.isNotEmpty(protocolList)) {
            for (final Protocol protocol : protocolList) {
                if (ProtocolModel.STATUS_VALID.equals(protocol.getStatus())) {
                    protocolId = protocol.getUuid();
                    break;
                }
            }
        }
        return protocolId;
    }


    /**
     * 根据转让时的对应的原始标下的投资记录来判断有几次
     *
     * @param projectId
     * @return
     * @author QianPengZhan
     * @date 2016年8月16日
     */
    public int calculateCounts(final String projectInvestId, final int realCounts) {
        int count = realCounts;
        final ProjectInvest projectInvest = projectInvestService.get(projectInvestId);
        if (projectInvest != null && projectInvest.getOriginInvestId() != null) {
            final ProjectInvest projectInvestOri = projectInvestService.get(projectInvest.getOriginInvestId());
            //当前投资记录 和 上一级的投资记录的  金额不一致 说明是部分转让  需要扣掉一次次数
            if (projectInvest.getAmount().compareTo(projectInvestOri.getAmount()) == Constant.INT_ZERO) {
                count = calculateCounts(projectInvest.getOriginInvestId(), realCounts + Constant.INT_ONE);
            }
        }
        return count;
    }

    /**
     * 发布债权规则校验
     *
     * @param dataMap
     * @author QianPengZhan
     * @date 2016年8月12日
     */
    private void validBondRule(final Map<String, Object> dataMap, final BondRuleModel bondRule, final Borrow borrow, final ProjectInvest projectInvest
            , final String backUrl) {
        //1、转让可否校验
        bondTimesValid(borrow, projectInvest.getUuid(), backUrl);

        //2、转让申请期校验
        final BondInvest bondInvest = bondInvestService.getBondInvestByOrderNo(projectInvest.getInvestOrderNo());
        int holdDay = Constant.INT_ZERO;
        if (bondInvest == null) {
            holdDay = Integer.parseInt(StringUtils.isNull(dataMap.get("holdDay")));//持有待收天数
        } else {//再次转让的  计息日 从受让成功的时间点开始
            holdDay = DateUtils.daysBetween(bondInvest.getCreateTime(), DateUtils.getNow());
        }
        final int periodDay = Integer.parseInt(StringUtils.isNull(dataMap.get("periodDay")));//距离本期还款日天数
        final int remainDay = Integer.parseInt(StringUtils.isNull(dataMap.get("remainDay")));//距离最后到期日天数
        validTime(bondRule, holdDay, periodDay, remainDay, backUrl);

        //3、一次性转让 受让校验
        validBuyBond(bondRule, backUrl);
    }

    /**
     * 转让可否校验
     *
     * @param borrow
     * @param porjectInvestId
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void bondTimesValid(final Borrow borrow, final String porjectInvestId, final String backUrl) {
        if (CommonEnum.YES.eq(borrow.getBondUseful())) {//借款标允许转让
            //a)转让次数校验
            final int ableCounts = borrow.getBondMaxTurn();//默认为1,单次转让，0为无限次转让  设置的次数
            int realCounts = Constant.INT_ZERO;//计算得来  默认是1
            realCounts = calculateCounts(porjectInvestId, realCounts);
            LOGGER.info("实际上已经转让次数：{}", realCounts);
            if (Constant.INT_ZERO == ableCounts) {//无限次转让  不受限制
                LOGGER.info("无限次转让");
            } else if (realCounts >= ableCounts) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_COUNT_OVER), backUrl, BussinessException.TYPE_JSON);
            }
        } else {//借款标不允许转让
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_TRANSFER_ABLE), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 一次性转让 受让校验
     *
     * @param bondRule
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void validBuyBond(final BondRule bondRule, final String backUrl) {
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())) {
            if (BondRuleEnum.RULE_SELL_PART.eq(StringUtils.isNull(bondRule.getSellStyle()))) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_UPDATE_TPP_SELL_LIMIT), backUrl, BussinessException.TYPE_JSON);
            }
            if (BondRuleEnum.RULE_BUY_PART.eq(StringUtils.isNull(bondRule.getBuyStyle()))) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_UPDATE_TPP_BUY_LIMIT), backUrl, BussinessException.TYPE_JSON);
            }
        }
    }

    /**
     * 转让申请期校验
     *
     * @param bondRule
     * @param holdDay
     * @param periodDay
     * @param remainDay
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void validTime(final BondRule bondRule, final int holdDay, final int periodDay, final int remainDay, final String backUrl) {
        if (remainDay <= Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_DAYS_LOW_ZERO), backUrl, BussinessException.TYPE_JSON);
        }
        if (bondRule.getRuleStatus() == null || bondRule.getRuleStatus().equals(CommonEnum.NO.getValue())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_STATUS_NOT_START), backUrl, BussinessException.TYPE_JSON);
        }
        if (bondRule.getHoldDays() > Integer.parseInt(CommonEnum.NO.getValue()) && holdDay < bondRule.getHoldDays()) {//持有天数校验
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_HOLDDAY_NOT_ENOUGH), backUrl, BussinessException.TYPE_JSON);
        }
        if (bondRule.getRemainDays() > Integer.parseInt(CommonEnum.NO.getValue()) && remainDay < bondRule.getRemainDays()) {//到期日天数校验
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_REMAINDAY_NOT_ENOUGH), backUrl, BussinessException.TYPE_JSON);
        }
        if (bondRule.getPeriodRemainDays() > Integer.parseInt(CommonEnum.NO.getValue()) && periodDay < bondRule.getPeriodRemainDays()) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_PERIODREMAINDAY_NOT_ENOUGH), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 计算最高溢价率=剩余期限利息*(1-利息管理费)/受让债权本金(债权价值)
     *
     * @param pi
     * @param data
     * @return
     * @author QianPengZhan
     * @date 2016年8月1日
     */
    private Map<String, Object> getParamsByCurrentRule(final Map<String, Object> dataMap, final Map<String, Object> data, final BondRuleModel bondRule) {
        //1、取出需要用到的值   开始期数  总天数  剩余天数 未还的总代收
        final int startPeriod = Integer.parseInt(StringUtils.isNull(dataMap.get("startPeriod")));//开始期数
        final Integer total = Integer.parseInt(StringUtils.isNull(dataMap.get("total"))) == Constant.INT_ZERO
                ? Constant.INT_ONE : Integer.parseInt(StringUtils.isNull(dataMap.get("total")));//总天数
        final Integer remainDay = Integer.parseInt(StringUtils.isNull(dataMap.get("remainDay")));//距离到期日天数
        final BigDecimal totalCollection = BigDecimalUtils.valueOf(StringUtils.isNull(dataMap.get("totalCollection")));
        final BigDecimal interestManageRate = (BigDecimal) dataMap.get("interestManageRate");
        final BigDecimal bondValue = BigDecimalUtils.defaultIfNull((BigDecimal) data.get("bondValue"));
        final double ruleMax = bondRule.getDiscountRateMax();
        final double ruleMin = bondRule.getDiscountRateMin();

        //2、计算剩余期限利息
        final BigDecimal remainInterest = caluRemainInterest(remainDay, totalCollection, total);

        //3、计算最高溢价率
        final BigDecimal maxApr = caluMaxBondApr(ruleMax, ruleMin, bondValue, interestManageRate, remainInterest);
        if (maxApr.compareTo(BigDecimal.valueOf(ruleMin)) < Constant.INT_ZERO) {
            data.put("bondAprError", true);
        } else {
            data.put("bondAprError", false);
        }

        //4、手续费
        if (BondRuleEnum.RULE_FEE_STYLE_RATE.eq(bondRule.getFeeStyle())) {//按照比例
            data.put("fee", new StringBuffer().append(bondRule.getFeeRate()).append("%"));
        } else if (BondRuleEnum.RULE_FEE_STYLE_FIXED.eq(bondRule.getFeeStyle())) {
            data.put("fee", new StringBuffer().append(bondRule.getFeeFixed()).append("元"));
        }
        LOGGER.info("maxApr:{},min:{},startPeriod:{}", maxApr, ruleMin, startPeriod);

        //5、录入信息
        data.put("maxApr", maxApr);
        data.put("max", BigDecimalUtils.decimal(maxApr, Constant.INT_ONE));//最大溢价率
        data.put("min", BigDecimalUtils.round(BigDecimal.valueOf(ruleMin), Constant.INT_ONE));//最小溢价率
        data.put("startPeriod", startPeriod);//开始期数    分期的时候有用处
        return data;
    }

    /**
     * 计算剩余期限利息
     *
     * @return
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private BigDecimal caluRemainInterest(final Integer remainDay, final BigDecimal totalCollection, final Integer total) {
        final BigDecimal remainInterest = remainDay == Constant.INT_ZERO ? BigDecimal.ZERO : BigDecimalUtils.div(totalCollection, BigDecimalUtils.valueOf(total.toString()), Constant.INT_TEN)
                .multiply(BigDecimalUtils.valueOf(remainDay.toString()));//计算的时候保留10位  入库变为4为有效数字
        LOGGER.info("剩余期限天数：{},总待收：{},总天数：{}", remainDay, totalCollection, total);
        LOGGER.info("计算公式：剩余期限利息 = 总待收 * 剩余期限天数 / 总天数 =  {} * {} / {} =  {}", totalCollection, remainDay, total, remainInterest);
        return remainInterest;
    }

    /**
     * 计算最高溢价率 精度10位
     *
     * @param ruleMax
     * @param ruleMin
     * @param bondValue
     * @param interestManageRate
     * @param remainInterest
     * @return
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private BigDecimal caluMaxBondApr(final double ruleMax, final double ruleMin, BigDecimal bondValue, final BigDecimal interestManageRate, final BigDecimal remainInterest) {
        LOGGER.info("规则中的最高溢价率：{},最低溢价率：{},利息管理费率：{},受让本金：{},剩余期限利息：{}", ruleMax, ruleMin, interestManageRate, bondValue, remainInterest);
        BigDecimal maxApr = BigDecimal.valueOf(ruleMax);
        BigDecimal getApr = BigDecimal.ZERO;
        if (bondValue.compareTo(BigDecimal.ZERO) != Constant.INT_ZERO) {
            //先除法 保留住精度  乘法会忽略精度
            getApr = BigDecimalUtils.div(remainInterest, bondValue, Constant.INT_TEN).multiply(BigDecimalUtils.sub(BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED), interestManageRate));//百分比
            if (BigDecimal.valueOf(ruleMax).compareTo(getApr) > Constant.INT_ZERO) {
                maxApr = getApr;
            } else {
                maxApr = BigDecimal.valueOf(ruleMax);
            }
        }
        LOGGER.info("最高溢价率计算：剩余期限利息*(100-利息管理费率)/受让本金  %= {} * (100-{}) / {} = {}", remainInterest, interestManageRate, bondValue, getApr);
        return maxApr;
    }

    /**
     * 添加的提交处理获取对象信息
     *
     * @param bond
     * @return
     * @author QianPengZhan
     * @date 2016年8月23日
     */
    private Map<String, Object> checkInsertObject(final Bond bond, final String backUrl) {
        final Map<String, Object> map = new HashMap<String, Object>();
        //原始标项目信息
        if (StringUtils.isNotBlank(bond.getProjectId())) {
            final Project project = projectService.getProjectValid(bond.getProjectId(), backUrl);
            map.put("project", project);
            //获取借款标信息
            final Borrow borrow = borrowService.getBorrow(project.getUuid(), backUrl);
            map.put("borrow", borrow);
            //获取项目类型
            final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
            if (projectType == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_TYPE_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("pt", projectType);
        } else {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }

        //获取原始标投资记录
        if (StringUtils.isNotBlank(bond.getInvestId())) {
            final ProjectInvest projectInvest = projectInvestService.get(bond.getInvestId());
            if (projectInvest == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("projectInvest", projectInvest);
        } else {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }

        //获取债权规则
        if (StringUtils.isNotBlank(bond.getRuleId())) {
            final BondRule bondRule = bondRuleService.getBondRule(bond.getRuleId());
            if (bondRule == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
            }
            map.put("bondRule", bondRule);
        } else {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = false)
    public void insert(final Bond entity, final String backUrl) {
        //重复处理判断(缓存标记) 设置该investId的缓存处理计数 失效时间： 5分钟 撤回的时候自动删除缓存
        final String handleKey = String.format(CacheKeys.PREFIX_BOND_INSERT_HANDLE_NUM.getKey(), entity.getInvestId());
        if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
            LOGGER.info("{}----债权发布已处理", StringUtils.isNull(entity.getInvestId()));
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_INSERT_HAS_HANDLE), backUrl, BussinessException.TYPE_JSON);
        }
        CacheUtils.expire(handleKey, ExpireTime.FIVE_MIN);

        try {
            //1、提交的参数校验   获取必要的参数对象
            BeanValidators.validate(entity);
            final Map<String, Object> objectMap = checkInsertObject(entity, backUrl);
            final Project project = (Project) objectMap.get("project");//获取原始标的信息
            final ProjectInvest pInvest = (ProjectInvest) objectMap.get("projectInvest");//获取原始投资记录信息
            final BondRule bondRule = (BondRule) objectMap.get("bondRule");//获取当前最新的债权规则
            final BondRuleModel bondRuleModel = bondRule.instanceModel(bondRule);
            final Borrow borrow = (Borrow) objectMap.get("borrow");//根据项目ID查询borrow
            final ProjectType projectType = (ProjectType) objectMap.get("pt");//获取类型
            final String protocolId = this.getBondProtocolId();

            //判断存管方放款是否结束
    		int i = tppTradeService.countByTypeAndProjectIdAndStatus(TppEnum.SERVICE_TYPE_BATCH_LOAN.getValue(), project.getUuid());
    		LOGGER.info("看放款是否到账:{}" , i > 0 ? "否" : "是");
    		if (i > 0 ) {
    			throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_LOAN_NOT_SUCCESS), BussinessException.TYPE_JSON);
    		}
    		
    		//判断存管方还款是否结束
    		int repayed = tppTradeService.countByTypeAndProjectIdAndStatus(TppEnum.SERVICE_TYPE_BATCH_REPAY.getValue(), project.getUuid());
    		LOGGER.info("看放款是否到账:{}" , repayed > 0 ? "否" : "是");
    		if (repayed > 0 ) {
    			throw new BussinessException("还款未结束", BussinessException.TYPE_JSON);
    		}
            
            //2、校验是否可以发布部分转让   转让金额  < 原始标投资本金-已还本金  校验是否已经发布债权
            this.valid(pInvest, entity, project, backUrl);

            //4、获取必要的信息
            final ProjectInvestModel model = pInvest.prototype();
            final Map<String, Object> dataMap = model.getAbleBondData(model);//获取必要的信息
            final Date repayTime = (Date) dataMap.get("repayTime");
            final Integer remainDay = Integer.parseInt(StringUtils.isNull(dataMap.get("remainDay")));
            final Integer startPeriod = Integer.parseInt(StringUtils.isNull(dataMap.get("startPeriod")));

            //5、判断是否冻结该功能
            this.bondInvestValidFreeze(entity, backUrl);

            //6、债权发布时对债权规则的校验start
            this.validBondRule(dataMap, bondRuleModel, borrow, pInvest, backUrl);
            final Map<String, Object> data = new HashMap<>();
            data.put("bondValue", entity.getBondMoney());
            final Map<String, Object> ruleMap = getParamsByCurrentRule(dataMap, data, bondRuleModel);
            this.validBondApr(ruleMap, entity, backUrl);
            this.validBondAprError(ruleMap, backUrl);

            //7、债权金额的校验
            final Map<String, Object> bondMoneydata = getBondLimitMoney(bondRule, project, entity, pInvest, backUrl);
            this.validBondMoney(bondMoneydata, entity, backUrl);

            //8、录入债权标的部分参数  封装
            final BigDecimal buyLowestMoney = BigDecimalUtils.valueOf(StringUtils.isNull(bondMoneydata.get("buyLowestMoney")).toString());
            final Bond bond = fillParams(entity, repayTime, remainDay, buyLowestMoney, projectType, bondRuleModel, protocolId, startPeriod, project, pInvest);

            //10、插入数据
            TokenUtils.validShiroToken(TokenConstants.TOKEN_BOND_COMMIT);//录入信息前进行token的校验
            dao.insert(bond);

            //11、更新原始标的投资记录的发布债权标识   发布的时候债权类型不变 保持原来的  等到受让成功 改变债权类型为  转让债权
            projectInvestService.updateBondFlagAndInvestStyle(pInvest.getInvestStyle(), InvestEnum.BOND_FLAG_PUBLISH.getValue(), pInvest.getUuid());

            //12、 缓存处理    ---   剩余债权（刚发布的时候就是总债权金额）放入缓存
            BondCache.setBondRemainAccount(bond.getBondNo(), bond.getBondMoney().doubleValue());

            //13、添加债权  删除缓存 重新来
            CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
        } catch (Exception e) {
            CacheUtils.del(handleKey);
            throw e;
        }
    }

    /**
     * 校验是否可以发布部分转让   转让金额  < 原始标投资本金-已还本金
     * 校验是否已经发布债权
     *
     * @param pInvest
     * @param entity
     * @param project
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void valid(final ProjectInvest pInvest, final Bond entity, final Project project, final String backUrl) {
        //1、查询当前的原始标投资记录 对应的正在转让中或者转让成功的债权标只会有1个  这里若大于等于1  则不能再次转让
        final int bondCount = dao.getBondCountByProjectInvestId(pInvest.getUuid());//查询当前的原始标投资记录 对应的正在转让中或者转让成功的债权标的个数
        LOGGER.info("【债权发布提交】--查询当前的原始标投资记录 对应的正在转让中或者转让成功的债权标的个数：{}", bondCount);
        if (bondCount >= Constant.INT_ONE) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INSERT_REPEAT), backUrl, BussinessException.TYPE_JSON);
        }

        //2、校验是否可以发布部分转让   转让金额  < 原始标投资本金-已还本金
        if (UfxConstant.UFX_TPP_NAME_CHINAPNR.equals(ConfigUtils.getTppName())
                && entity.getBondMoney().compareTo(pInvest.getAmount().subtract(this.getRepayedMoney(project, pInvest, false))) < Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_UFX_HUIFU_LIMIT), backUrl, BussinessException.TYPE_JSON);
        }

        //3、校验是否已经发布债权
        LOGGER.info("【债权发布提交】--原始标投资记录ID：{},此原始标投资记录的标识：bondFlag={}", pInvest.getUuid(), pInvest.getBondFlag());
        if (InvestEnum.BOND_FLAG_PUBLISH.eq(pInvest.getBondFlag())) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_ALREADY_RELEASED), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 判断是否冻结该功能
     *
     * @param entity
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void bondInvestValidFreeze(final Bond entity, final String backUrl) {
        final UserFreezeService userFreezeService = (UserFreezeService) SpringContextHolder.getBean("userFreezeService");
        final boolean isFreezed = userFreezeService.isFreezed(entity.getUserId(), UserFreezeModel.STATUS_USER_FREEZE_BOND);
        if (isFreezed) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.BOND_IS_FREEZE), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 折溢价率的校验
     *
     * @param ruleMap
     * @param entity
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private void validBondApr(final Map<String, Object> ruleMap, final Bond entity, final String backUrl) {
        BigDecimal max = BigDecimalUtils.valueOf(StringUtils.isNull(ruleMap.get("max")));
        BigDecimal min = BigDecimalUtils.valueOf(StringUtils.isNull(ruleMap.get("min")));
        if (entity.getBondApr().compareTo(min) < Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_APR_LO_MIN), backUrl, BussinessException.TYPE_JSON);
        }
        if (entity.getBondApr().compareTo(max) > Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_APR_UP_MAX), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 录入债权标的部分参数  封装
     *
     * @param entity
     * @param repayTime
     * @param remainDay
     * @param buyLowestMoney
     * @param projectType
     * @param bondRule
     * @param protocolList
     * @param startPeriod
     * @param project
     * @param pInvest
     * @return
     * @author QianPengZhan
     * @date 2016年11月5日
     */
    private Bond fillParams(final Bond entity, final Date repayTime, final Integer remainDay, final BigDecimal buyLowestMoney, final ProjectType projectType
            , final BondRule bondRule, final String protocolId, final Integer startPeriod, final Project project, final ProjectInvest pInvest) {
        Bond bond = new Bond();
        bond = entity.instance();
        bond.preInsert();//setUUID
        bond.setRemainDays(remainDay);//剩余期限录入
        bond.setStatus(BondEnum.STATUS_PUBLISH.getValue());//设置状态发布中
        bond.setBondEndTime(repayTime);//债权还款时间
        bond.setBondNo(this.nextBondNo(DateUtils.getDate()));//债权的编号
        bond.setBondLowestMoney(buyLowestMoney);//发布时设置最低受让起投金额
        bond.setBondMostMoney(entity.getBondMoney());//最大受让金额为发布的债权总金额
        bond.setBondName(ConfigUtils.getValue(ConfigConstant.BOND_PREFIX) + StringUtils.HYPHEN + ObjectUtils.defaultIfNull(projectType.getTypeName(), StringUtils.EMPTY) + StringUtils.HYPHEN + bond.getBondNo());//设置债权名称
        bond.setLimitHours(bondRule.getDeadline());//设置有效时间   小时为单位
        bond.setRuleId(bondRule.getUuid());//填入当时发布债权的债权规则
        bond.setStartPeriod(startPeriod);
        bond.setProtocolId(protocolId);
        if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle()) || LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())) {
            LOGGER.info("还款方式为{}的情形下不允许拆分债权转让,只能全部转让!", project.getRepayStyle());
            bond.setSellStyle(CommonEnum.YES.getValue());
        } else {
            bond = doSplitProjectInvestAndProjectCollection(pInvest, bond, project);
        }
        return bond;
    }

    /**
     * 部分转让的时候 分离债权
     *
     * @param pi
     * @param entity
     * @return
     * @author QianPengZhan
     * @date 2016年8月12日
     */
    private Bond doSplitProjectInvestAndProjectCollection(final ProjectInvest pInvest, final Bond entity, final Project project) {
        LOGGER.info("--------债权发布，部分转让，拆分投资记录-------（原始标的还款方式只能是一次性还款、每月还息到期还本、每季还息到期还本）start");
        final ProjectWorker projectWor = new ProjectWorker(project);//生成项目工程
        final User bondUser = userService.get(pInvest.getUserId());//转让人信息
        entity.setSellStyle(CommonEnum.YES.getValue());
        //判断此次转让的金额  是否 等于   原始标投资记录的 待收本金    如果等于 就是一次性全部转让   不用拆分  如果不等于 那么就是部分转让需要拆分
        if (entity.getBondMoney().compareTo(pInvest.getAmount().subtract(this.getRepayedMoney(project, pInvest, false))) < Constant.INT_ZERO) {
            final BigDecimal remainBond = BigDecimalUtils.sub(pInvest.getAmount(), entity.getBondMoney());//剩余的债权本金

            //1、拆分投资记录
            //a)、转让金额   转让的金额按照利息计算去算
            //根据转让金额生成待收利息 待收本金
            final InterestCalculator icPublish = projectWor.interestCalculator(entity.getBondMoney(), project.getApr(), BigDecimal.ZERO, pInvest.getCreateTime());
            final BigDecimal capitalPublish = entity.getBondMoney();//本金（剩余）
            final BigDecimal interestPublish = icPublish.repayInterest();//利息

            //插入转让金额对应新的投资记录
            ProjectInvest projectInvestPublish = new ProjectInvest();//标识债权投资记录
            projectInvestPublish = pInvest.instance();
            projectInvestPublish.setUuid(IdGen.uuid());//修改uuid
            projectInvestPublish.setMoney(capitalPublish);
            projectInvestPublish.setAmount(capitalPublish);
            projectInvestPublish.setRealAmount(capitalPublish);
            projectInvestPublish.setPayment(BigDecimalUtils.add(capitalPublish, interestPublish));
            projectInvestPublish.setInterest(interestPublish);
            projectInvestPublish.setOriginInvestId(pInvest.getUuid());
            
            //已还修改start
//            BigDecimal repayInterest = BigDecimal.ZERO;
//            BigDecimal otherRepayInterest = BigDecimal.ZERO;
//            BigDecimal repayAmount = BigDecimal.ZERO;
//            BigDecimal otherRepayAmount = BigDecimal.ZERO;
//            BigDecimal repayedAmount = projectInvestPublish.getRepayedAmount();
//            BigDecimal repayedInterest = projectInvestPublish.getRepayedInterest();
//            if (repayedAmount.compareTo(BigDecimal.ZERO) > 0) {
//            	repayAmount = BigDecimalUtils.mul(repayedAmount, BigDecimalUtils.div(capitalPublish, pInvest.getAmount()));
//            	otherRepayAmount = BigDecimalUtils.sub(repayedAmount, repayAmount);
//            	projectInvestPublish.setRepayedAmount(repayAmount);
//            	
//            	
//            	repayInterest = BigDecimalUtils.mul(repayedInterest, BigDecimalUtils.div(capitalPublish, pInvest.getAmount()));
//            	otherRepayInterest = BigDecimalUtils.sub(repayedInterest, repayInterest);
//            	projectInvestPublish.setRepayedInterest(repayInterest);
//            	LOGGER.info("-------------已还修改-----------repayInterest: {}", repayAmount);
//            }
//            projectInvestPublish.setWaitAmount(BigDecimalUtils.add(capitalPublish, interestPublish, repayAmount.negate()));
//            projectInvestPublish.setWaitInterest(BigDecimalUtils.add(interestPublish, repayAmount.negate()));
            //重新计算repayedAmount repayedInterest waitAmount waitInterest
            BigDecimal repayedAmount = BigDecimal.ZERO;
            BigDecimal repayedInterest = BigDecimal.ZERO;
            BigDecimal waitAmount = BigDecimal.ZERO;
            BigDecimal waitInterest = BigDecimal.ZERO;
            
            //已还修改end
            
            projectInvestPublish.setWaitRaiseInterest(BigDecimal.ZERO);
            projectInvestPublish.setRaiseInterest(BigDecimal.ZERO);
            projectInvestPublish.setBondFlag(InvestEnum.BOND_FLAG_PUBLISH.getValue());//已转让
            projectInvestPublish.setInvestStyle(pInvest.getInvestStyle());//保持和原来的一样  等到成功转让完毕或者部分转让成功撤回  改为 转让投资
            projectInvestPublish.setCreateTime(DateUtils.getNow());

            // 根据投资记录生成待收记录    重新生成原始标待收记录   几期就是几条   生成的是普通待收
            final List<ProjectCollection> collectionListPublish = projectWor.createCollectionList(projectInvestPublish, icPublish);
            if (CollectionUtils.isNotEmpty(collectionListPublish)) {
                for (final ProjectCollection pc : collectionListPublish) {
                    //处理转让金额部分的待收属性
                    final ProjectCollection oriPc = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(pInvest.getUuid(),
                            pc.getPeriod());//查询当前期数的原始标对应的待收记录
                    pc.setParentId(oriPc.getUuid());
                    pc.setRepayTime(oriPc.getRepayTime());
                    pc.setCollectionType(oriPc.getCollectionType());//保持和原来的一样  等到成功转让完毕或者部分转让成功撤回  改为转让待收
//                    pc.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());//变为未还的状态

                    pc.setStatus(oriPc.getStatus());
                    if (ProjectCollectionEnum.STATUS_PAID.getValue().equals(oriPc.getStatus())) {
                    	pc.setInterest(BigDecimal.ZERO);
                    	pc.setCapital(BigDecimal.ZERO);
                    	pc.setPayment(BigDecimal.ZERO);
                    	pc.setLastRepayTime(oriPc.getLastRepayTime());
                    	pc.setRepayedAmount(BigDecimal.ZERO);
                    	pc.setManageFee(BigDecimal.ZERO);
                    	pc.setBorrowManageFee(BigDecimal.ZERO);
                    } else {
                    	waitAmount = BigDecimalUtils.add(waitAmount, pc.getCapital(), pc.getInterest());
                    	waitInterest = BigDecimalUtils.add(waitInterest, pc.getInterest());
                    }
//                    pc.setRaiseInterest(oriPc.getRaiseInterest());//拆分的时候 需要将加息加上 防止撤回
                    pc.setRaiseInterest(BigDecimal.ZERO);
                }
            }
			LOGGER.info("-------repayedAmount:{},repayedInterest:{},waitAmount:{},waitInterest:{}---------", repayedAmount,
					repayedInterest, waitAmount, waitInterest);
			projectInvestPublish.setRepayedAmount(repayedAmount);
			projectInvestPublish.setRepayedInterest(repayedInterest);
			projectInvestPublish.setWaitAmount(waitAmount);
			projectInvestPublish.setWaitInterest(waitInterest);

            //债权标  使用对应的  债权标识的投资记录
            entity.setInvestId(projectInvestPublish.getUuid());
            entity.setSellStyle(CommonEnum.NO.getValue());

            //b)、剩余金额
            //根据剩余金额生成待收利息和待收本金
            final InterestCalculator interestCalculator = projectWor.interestCalculator(remainBond, project.getApr(), BigDecimal.ZERO, pInvest.getCreateTime());
            final BigDecimal capital = remainBond;//本金（剩余）
            final BigDecimal interest = interestCalculator.repayInterest();//利息

            //插入剩余金额对应新的投资记录
            ProjectInvest projectInvest = new ProjectInvest();
            projectInvest = pInvest.instance();
            projectInvest.setUuid(IdGen.uuid());//修改uuid
            projectInvest.setMoney(capital);
            projectInvest.setAmount(capital);
            projectInvest.setRealAmount(capital);
            projectInvest.setPayment(BigDecimalUtils.add(capital, interest));
            projectInvest.setInterest(interest);
            projectInvest.setOriginInvestId(pInvest.getUuid());
            
            //已还修改start
          //重新计算repayedAmount repayedInterest waitAmount waitInterest
            BigDecimal otherRepayedAmount = BigDecimal.ZERO;
            BigDecimal otherRepayedInterest = BigDecimal.ZERO;
            BigDecimal otherWaitAmount = BigDecimal.ZERO;
            BigDecimal otherWInterest = BigDecimal.ZERO;
//            projectInvest.setRepayedAmount(otherRepayAmount);
//            projectInvest.setRepayedInterest(otherRepayInterest);
//            projectInvest.setWaitAmount(BigDecimalUtils.add(capital, interest, otherRepayAmount.negate()));
//            projectInvest.setWaitInterest(BigDecimalUtils.add(interest, otherRepayAmount.negate()));
            //已还修改end

            projectInvest.setWaitRaiseInterest(BigDecimal.ZERO);
            projectInvest.setRaiseInterest(BigDecimal.ZERO);
            projectInvest.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());//未转让转让
            projectInvest.setInvestStyle(pInvest.getInvestStyle());//保持和原来的一样  等到成功转让完毕或者部分转让成功撤回  改为 转让投资   可能为受让投资
            projectInvest.setCreateTime(DateUtils.getNow());


            // 根据投资记录生成待收记录    重新生成原始标待收记录   几期就是几条   生成的是普通待收
            final List<ProjectCollection> collectionList = projectWor.createCollectionList(projectInvest, interestCalculator);
            if (CollectionUtils.isNotEmpty(collectionList)) {
                for (final ProjectCollection pc : collectionList) {
                    //处理剩余金额部分的待收属性
                    final ProjectCollection oriPc = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(pInvest.getUuid(),
                            pc.getPeriod());//查询当前期数的原始标对应的待收记录
                    pc.setParentId(oriPc.getUuid());
                    pc.setRepayTime(oriPc.getRepayTime());
                    pc.setCollectionType(oriPc.getCollectionType());
//                    pc.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
                    pc.setStatus(oriPc.getStatus());
                    if (ProjectCollectionEnum.STATUS_PAID.getValue().equals(oriPc.getStatus())) {
                    	pc.setInterest(oriPc.getInterest());
                    	pc.setCapital(oriPc.getCapital());
                    	pc.setPayment(oriPc.getPayment());
                    	pc.setLastRepayTime(oriPc.getLastRepayTime());
                    	pc.setRepayedAmount(oriPc.getRepayedAmount());
                    	pc.setManageFee(oriPc.getManageFee());
                    	
                    	otherRepayedAmount = BigDecimalUtils.add(otherRepayedAmount, pc.getRepayedAmount());
                    	otherRepayedInterest = otherRepayedAmount;
                    }else {
                    	otherWaitAmount = BigDecimalUtils.add(otherWaitAmount, pc.getCapital(), pc.getInterest());
                    	otherWInterest = BigDecimalUtils.add(otherWInterest, pc.getInterest());
                    }
                	pc.setBorrowManageFee(oriPc.getBorrowManageFee());
//                    pc.setRaiseInterest(oriPc.getRaiseInterest());//拆分的时候 需要将加息加上 防止撤回
                    pc.setRaiseInterest(BigDecimal.ZERO);
                }
            }
            LOGGER.info("-------otherRepayedAmount:{},otherRepayedInterest:{},otherWaitAmount:{},otherWInterest:{}---------", 
            		otherRepayedAmount, otherRepayedInterest, otherWaitAmount, otherWInterest);
            projectInvest.setRepayedAmount(otherRepayedAmount);
            projectInvest.setRepayedInterest(repayedInterest);
            projectInvest.setWaitAmount(otherWaitAmount);
            projectInvest.setWaitInterest(otherWInterest);

            //资金平衡处理
            final BigDecimal interestAdd = interest.add(interestPublish);//拆分的2笔投资记录 的利息和
            if (interestAdd.compareTo(pInvest.getInterest()) == Constant.INT_ZERO && LOGGER.isInfoEnabled()) {//债权资金平衡    判断拆分的2笔的利息和  是否等于未拆分之前的利息   如果等于 说明资金平衡
                LOGGER.info("-------投资记录uuid:{},部分转让资金平衡.---------", pInvest.getInterest());
                LOGGER.info("-------投资记录uuid1:{},部分转让资金平衡.---------", projectInvestPublish.getInterest());
                LOGGER.info("-------投资记录uuid2:{},部分转让资金平衡.---------", projectInvest.getInterest());
            } else {//若不平衡 转让金额的不处理 保持不变  对剩余金额的进行处理
                LOGGER.info("-------投资记录uuid:{},部分转让资金不平衡.---------", pInvest.getInterest());
                LOGGER.info("-------投资记录uuid1:{},部分转让资金不平衡.---------", projectInvestPublish.getInterest());
                LOGGER.info("-------投资记录uuid2:{},部分转让资金不平衡.---------", projectInvest.getInterest());
                final BigDecimal diffMoney = pInvest.getInterest().subtract(interestAdd);//差额 可能为正 也可能为负 但是这个差额是以分为单位的  不然就是利息计算器的大错误了
                if (CollectionUtils.isNotEmpty(collectionList)) {
                    for (int i = 0; i < collectionList.size(); i++) {
                        final ProjectCollection collection = collectionList.get(i);
                        if (i == collectionList.size() - 1) {
                            collection.setInterest(collection.getInterest().add(diffMoney));
                            collection.setPayment(collection.getPayment().add(diffMoney));
                        }
                    }
                }
                projectInvest.setInterest(projectInvest.getInterest().add(diffMoney));
                projectInvest.setPayment(projectInvest.getPayment().add(diffMoney));
            }

            //2、作废原始标投资记录
            bondInvestService.cancleOriProjectInvestAndProjectCollection(pInvest, bondUser, true);

            //最后录入数据库
            //转让部分金额  处理入库
            projectInvestService.insert(projectInvestPublish);
            projectCollectionService.insertBatch(collectionListPublish);
            //剩余金额部分  处理入库
            projectInvestService.insert(projectInvest);
            projectCollectionService.insertBatch(collectionList);
        }
        LOGGER.info("--------债权发布，部分转让，拆分投资记录-------（原始标的还款方式只能是一次性还款、每月还息到期还本、每季还息到期还本）end");
        return entity;
    }

    /**
     * 债权规则校验
     *
     * @param entity
     * @return
     * @author QianPengZhan
     * @date 2016年8月10日
     */
    private void validBondMoney(final Map<String, Object> bondMoneydata, final Bond entity, final String backUrl) {
        final BigDecimal sellLimitMoney = StringUtils.toBigDecimal(StringUtils.isNull(bondMoneydata.get("sellLimitMoney")));
        if (sellLimitMoney.compareTo(entity.getBondMoney()) > Constant.INT_ZERO) {//转让金额 小于最低转让金额
            throw new BussinessException(ResourceUtils.get(BondResource.BONDMONEY_LESS_LOWEST), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 校验债权详情
     *
     * @param bond
     * @param project
     * @param map
     * @author QianPengZhan
     * @date 2016年8月17日
     */
    private void validInfo(final BondModel bond, final Project project, final BondRule rule, final String backUrl) {
        if (project == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        if (rule == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
    }

    /**
     * 详情共有方法
     *
     * @param id
     * @return
     * @author QianPengZhan
     * @date 2016年9月28日
     */
    private Map<String, Object> getBondProjectDetailInfo(final BondModel bondModel, final Project project, final BondRule rule, final String backUrl) {
        //1、建立Map
        final Map<String, Object> map = new HashMap<>();

        //2、校验信息
        validInfo(bondModel, project, rule, backUrl);

        //3、录入基础的信息
        inputBaseInfo(map, bondModel, project, rule);

        //4、录入原始标产品风险等级和投资条件
        inputRiskInfo(map, project);

        //5、剩余可投录入
        inputRemainMoney(map, bondModel);

        //6、录入转让方式和受让方式
        inputSellAndBuyStyle(map, bondModel, project, rule);

        //7、录入投资笔数和售罄用时
        inputSecondsAndCount(map, bondModel);
        return map;
    }

    /**
     * 录入投资笔数和售罄用时
     *
     * @param map
     * @param bondModel
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void inputSecondsAndCount(final Map<String, Object> map, final BondModel bondModel) {
        long soldSeconds = Constant.INT_ZERO;//售罄用时
        int investCount = Constant.INT_ZERO;//投资笔数
        if (BondEnum.STATUS_COMPLETE.eq(bondModel.getStatus())) {
            final long publishTime = bondModel.getCreateTime().getTime();
            final BondInvest invest = new BondInvest();
            invest.setBondId(bondModel.getUuid());
            invest.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
            final List<BondInvest> list = bondInvestService.findList(invest);
            if (CollectionUtils.isNotEmpty(list)) {
                investCount = list.size();
                final BondInvest last = list.get(list.size() - Constant.INT_ONE);
                final long lastTime = last.getCreateTime().getTime();
                soldSeconds = lastTime - publishTime;
            }
        }
        map.put("soldSeconds", soldSeconds);
        map.put("investCount", investCount);
    }

    /**
     * 录入转让方式和受让方式
     *
     * @param map
     * @param bondModel
     * @param project
     * @param rule
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void inputSellAndBuyStyle(final Map<String, Object> map, final BondModel bondModel, final Project project, final BondRule rule) {
        String sellStyle = bondModel.getSellStyle();//默认取数据库中是否全部转让的值
        String buyStyle = rule.getBuyStyle();//默认取规则表中的受让方式
        if (LoanEnum.STYLE_INSTALLMENT_REPAYMENT.eq(project.getRepayStyle())
                || LoanEnum.STYLE_AVERAGE_CAPITAL_REPAYMENT.eq(project.getRepayStyle())) {
            //如果是等额本息和等额本金还款方式  无视规则  只能全额受让
            buyStyle = CommonEnum.YES.getValue();
        }
        map.put("sellStyle", sellStyle);//债权转让方式
        map.put("buyStyle", buyStyle);//债权受让方式
    }

    /**
     * 剩余可投录入
     *
     * @param map
     * @param bondModel
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void inputRemainMoney(final Map<String, Object> map, final BondModel bondModel) {
        final double remainMoeny = BondCache.getBondRemainAccount(bondModel.getBondNo());
        map.put("bondMostMoney", BigDecimalUtils.defaultIfNull(BigDecimal.valueOf(remainMoeny)));//起投最大金额
        map.put("remainMoney", remainMoeny);
    }

    /**
     * 录入原始标产品风险等级和投资条件
     *
     * @param map
     * @param project
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void inputRiskInfo(final Map<String, Object> map, final Project project) {
        final LevelConfig levelConfig = levelConfigService.findByRiskLevel(project.getRiskLevel());
        map.put("riskLevel", levelConfig.getRiskLevelName());//安全等级
        map.put("tenderCondition", levelConfig.getUserRiskLevelName());//投资条件
    }

    /**
     * 基础信息录入
     *
     * @param map
     * @param bondModel
     * @param project
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void inputBaseInfo(final Map<String, Object> map, final BondModel bondModel, final Project project, final BondRule rule) {
        map.put("result", true);
        map.put("msg", StringUtils.EMPTY);
        map.put("projectName", project.getProjectName());
        map.put("isVouch", project.getIsVouch());
        map.put("protocolId", StringUtils.isNull(bondModel.getProtocolId()));
        map.put("status", bondModel.getStatus());//债权状态
        map.put("bondName", bondModel.getBondName());//债权名称
        map.put("apr", project.getApr());//预期年化收益率
        map.put("remainDay", bondModel.getRemainDays());//剩余期限
        map.put("bondApr", bondModel.getBondApr());//折溢价率
        map.put("bondLowestMoney", bondModel.getBondLowestMoney());//起投金额
        map.put("bondMoney", bondModel.getBondMoney());//债权总额
        //录入还款方式   查询出还款方式的种类
        String interestStyle = StringUtils.EMPTY;
        if (LoanEnum.INTEREST_STYLE_EV.eq(project.getInterestStyle())) {
            interestStyle = DictUtils.getData(DictTypeEnum.INTEREST_STYLE.getValue(), project.getInterestStyle()).getItemName();
        } else if (LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle())) {
            interestStyle = "T+" + project.getInterestStartDays() + "计息";
        } else {
            interestStyle = "其他计息方式";
        }
        map.put("interestStyle", interestStyle);
        String repayStyle = StringUtils.EMPTY;
        if (project.getFixedRepayDay() == 0) {
            repayStyle = DictUtils.getData(DictTypeEnum.REPAY_STYLE.getValue(), project.getRepayStyle()).getItemName();
        } else {
            repayStyle = DictUtils.getData(DictTypeEnum.REPAY_STYLE.getValue(), project.getRepayStyle()).getItemName() + "(固定还款日" + project.getFixedRepayDay() + "日)";
        }
        map.put("repayStyle", repayStyle);//还款方式  收益方式
        map.put("deadTime", DateUtils.rollMinute(bondModel.getCreateTime(), rule.getDeadline() * 60));//剩余时间点
        map.put("projectId", project.getUuid());//项目ID
        map.put("projectUserId", project.getUserId());//项目借款人
        map.put("ruleId", rule.getUuid());//规则ID
        map.put("investId", bondModel.getInvestId());//原始标投资ID
        map.put("bondUserId", bondModel.getUserId());//债权发布人
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(key = CacheConstant.KEY_BOND_DETAIL, expire = ExpireTime.FIVE_MIN)
    public Map<String, Object> getBondProjectDetail(String id, final String backUrl) {
        //1、获取债权和项目信息
        final Bond bond = this.getBond(id, backUrl);
        final BondModel bondModel = bond.prototype();
        final Project project = projectService.get(bondModel.getProjectId());
        final BondRule rule = bondRuleService.get(bondModel.getRuleId());
        return getBondProjectDetailInfo(bondModel, project, rule, backUrl);
    }

    @Override
    @Transactional(readOnly = false)
    @Cacheable(key = CacheConstant.KEY_BOND_DETAIL_USER, expire = ExpireTime.FIVE_MIN)
    public Map<String, Object> getBondProjectDetail(final String id, final User user, final String backUrl) {
        final Map<String, Object> map = getBondProjectDetail(id, backUrl);
        //是否已经登录
        boolean isLogin = true;
        //是否已经实名认证
        boolean isRealName = true;
        //当前用户是否是借款人 (提示借款人无法进行投资)
        boolean isBorrower = false;
        //当前用户是否是债权发布人
        boolean isBonder = false;
        if (user == null) {
            isLogin = false;
        } else {
            final UserIdentify identify = userIdentifyService.findByUserId(user.getUuid());
            if (!Constant.FLAG_YES.equals(identify.getRealNameStatus())) {
                CacheUtils.del(CacheConstant.KEY_PREFIX_USER_IDENTIFY_USER_ID + user.getUuid());
                isRealName = false;
            }
            final User projectUser = userService.get(StringUtils.isNull(map.get("projectUserId")));//原始标借款人
            if (user.getUserName().equals(projectUser.getUserName())) {
                isBorrower = true;
            }
            final User bondUser = userService.get(StringUtils.isNull(map.get("bondUserId")));//债权发布人
            if (user.getUserName().equals(bondUser.getUserName())) {
                isBonder = true;
            }
        }
        map.put("isLogin", isLogin);
        map.put("isRealName", isRealName);
        map.put("isBorrower", isBorrower);
        map.put("isBonder", isBonder);
        return map;
    }


    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> getInvestData(final BigDecimal amount, final String id) {
        final String backUrl = URLConstant.BOND_DETAIL_PAGE_PREFIX + id;
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("amount", amount);//app需要
        
        //传参的校验
        if (amount == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INTEREST_AMOUNT_NULL), backUrl, BussinessException.TYPE_JSON);
        }
        //1、获取缓存中的对象信息
        final Bond bond = this.getBond(id, backUrl);
        final BondModel bondModel = bond.prototype();
        final ProjectInvest pInvest = projectInvestService.get(bondModel.getInvestId());//查询原始标信息
        final Project project = projectService.getProjectValid(bond.getProjectId(), backUrl);
        //2、获取计算时用到的数据
        final ProjectInvestModel model = pInvest.prototype();
        final Map<String, Object> data = model.getAbleBondData(model);

        //3、原始待收本金的待收利息
        final BigDecimal totalInterest = BigDecimalUtils.valueOf(StringUtils.isNull(data.get("totalCollection")));

        //4、原始待收本金持有天数利息
        final BigDecimal holdInterest = BigDecimalUtils.valueOf(StringUtils.isNull(data.get("holdDayCollection")));

        //5、利息管理费率
        final BigDecimal feeRate = BigDecimalUtils.valueOf(StringUtils.isNull(data.get("interestManageRate")));

        //6、原始项目的待收本金
        final BigDecimal capital = pInvest.getAmount().subtract(this.getRepayedMoney(project, pInvest, false));

        //7、受让比例 =  受让本金 / 原始项目的待收本金
        final BigDecimal bondRate = BigDecimalUtils.div(amount, capital, Constant.INT_TEN);

        LOGGER.info("1-原始参数 ----原始待收本金的待收利息 ：{},原始待收本金持有天数利息:{},利息管理费率:{},原始项目的待收本金:{},受让比例：{}", totalInterest, holdInterest, feeRate, capital, bondRate);
        caluPreInterest(map, totalInterest, holdInterest, capital, feeRate, bondModel, bondRate);
        caluRealPayAmount(map, capital, feeRate, holdInterest, bondRate);
        return map;
    }

    /**
     * 计算实际支付
     *
     * @param map
     * @param capital
     * @param feeRate
     * @param holdInterest
     * @param bondRate
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void caluRealPayAmount(final Map<String, Object> map, final BigDecimal capital, final BigDecimal feeRate, final BigDecimal holdInterest, final BigDecimal bondRate) {
        /**
         * 实际支付
         * 	= 受让本金 + 受让本金 * 折溢价率 + 应付利息 - 应付利息 * 利息管理费率
         *  = [原始标待收本金 + 原始标待收本金折溢价金额   + (原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)]* 受让比例
         */
        final BigDecimal changeMoney = BigDecimalUtils.valueOf(StringUtils.isNull(map.get("changeMoney")));
        //13、原始标待收本金 + 原始标待收本金折溢价金额
        final BigDecimal capitalAddChangeMoney = BigDecimalUtils.add(capital, changeMoney);
        LOGGER.info("7-原始标待收本金 + 原始标待收本金折溢价金额  = {}+{}={}", capital, changeMoney, capitalAddChangeMoney);

        //14、(原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)
        final BigDecimal holdInterestSubRateInterest = BigDecimalUtils.sub(holdInterest, BigDecimalUtils.div(feeRate, BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED), Constant.INT_TEN).multiply(holdInterest));
        LOGGER.info("8-(原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)  = {}-{}*{}/100={}", holdInterest, holdInterest, feeRate, holdInterestSubRateInterest);

        //15、[原始标待收本金 + 原始标待收本金折溢价金额   + (原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)]
        final BigDecimal totalRealPayAmount = BigDecimalUtils.add(capitalAddChangeMoney, holdInterestSubRateInterest);
        LOGGER.info("9-[原始标待收本金 + 原始标待收本金折溢价金额   + (原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)]  = {}+{}={}", capitalAddChangeMoney, holdInterestSubRateInterest, totalRealPayAmount);

        //16、[原始标待收本金 + 原始标待收本金折溢价金额   + (原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)]* 受让比例
        final BigDecimal realPayAmount = totalRealPayAmount.multiply(bondRate);
        LOGGER.info("10-、[原始标待收本金 + 原始标待收本金折溢价金额   + (原始标待收本金持有利息 - 原始标待收本金持有利息 * 利息管理费率)]* 受让比例  = {}*{}={}", totalRealPayAmount, bondRate, realPayAmount);
        map.put("realPayAmount", BigDecimalUtils.round(realPayAmount, Constant.INT_TWO));//实际支付金额
    }

    /**
     * 计算预期收益
     *
     * @param totalInterest
     * @param holdInterest
     * @param capital
     * @param feeRate
     * @param bondModel
     * @param bondRate
     * @param map
     * @return
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void caluPreInterest(final Map<String, Object> map, final BigDecimal totalInterest, final BigDecimal holdInterest, final BigDecimal capital, final BigDecimal feeRate, final BondModel bondModel, final BigDecimal bondRate) {
        /**预期收益
         * = 受让本金的待收利息 - 应付利息 - 受让后持有天数利息 * 利息管理费率-折溢价金额  (溢价扣除 折价加上)
         * =[(原始待收本金的待收利息 - 原始待收本金持有天数利息) - (原始待收本金持有天数利息*利息管理费 +原始标待收本金折溢价金额)]* 受让比例
         */
        //8、原始待收本金的待收利息 - 原始待收本金持有天数利息
        final BigDecimal totalSubHoldInterest = totalInterest.subtract(holdInterest);
        LOGGER.info("2-原始待收本金的待收利息 - 原始待收本金持有天数利息={}-{}={}", totalInterest, holdInterest, totalSubHoldInterest);

        //9、计算原始标待收本金折溢价金额    = 原始项目的待收本金 * 折溢价率 /100   保留10位有效数字
        BigDecimal changeMoney = BigDecimalUtils.div(bondModel.getBondApr(), BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED), Constant.INT_TEN).multiply(capital);
        map.put("changeMoney", BigDecimalUtils.round(changeMoney, Constant.INT_TWO));//折溢价金额
        LOGGER.info("3-原始标待收本金折溢价金额    = 原始项目的待收本金 * 折溢价率 /100={}*{}/100={}", capital, bondModel.getBondApr(), changeMoney);

        //10、原始待收本金持有天数利息*利息管理费  +原始标待收本金折溢价金额
        final BigDecimal holdMulRateSubChange = BigDecimalUtils.add(BigDecimalUtils.div(feeRate, BigDecimal.valueOf(Constant.DOUBLE_ONE_HUNDRED), Constant.INT_TEN).multiply(holdInterest), changeMoney);
        LOGGER.info("4-原始待收本金持有天数利息*利息管理费  +原始标待收本金折溢价金额={}*{}/100+{}={}", holdInterest, feeRate, changeMoney, holdMulRateSubChange);

        //11、[(原始待收本金的待收利息 - 原始待收本金持有天数利息) - (原始待收本金持有天数利息*利息管理费 -原始标待收本金折溢价金额)]
        final BigDecimal totalPreInterest = totalSubHoldInterest.subtract(holdMulRateSubChange);
        LOGGER.info("5-[(原始待收本金的待收利息 - 原始待收本金持有天数利息) - (原始待收本金持有天数利息*利息管理费 +原始标待收本金折溢价金额)]={}-{}={}", totalSubHoldInterest, holdMulRateSubChange, totalPreInterest);

        //12、[(原始待收本金的待收利息 - 原始待收本金持有天数利息) - (原始待收本金持有天数利息*利息管理费 -原始标待收本金折溢价金额)]*(受让比例)
        final BigDecimal preInterest = totalPreInterest.multiply(bondRate);
        LOGGER.info("6-[(原始待收本金的待收利息 - 原始待收本金持有天数利息) - (原始待收本金持有天数利息*利息管理费 +原始标待收本金折溢价金额)]*(受让比例) ={}*{}={}", totalPreInterest, bondRate, preInterest);
        map.put("preInterest", BigDecimalUtils.round(preInterest, Constant.INT_TWO));//预期收益
    }

    /**
     * 校验撤回的债权ID
     *
     * @param id
     * @return
     * @author QianPengZhan
     * @date 2016年8月18日
     */
    private Bond validCancleBond(final String id, final String backUrl) {
        //1、更新债权标的状态
        final Bond bond = this.getBond(id, backUrl);
        if (BondEnum.STATUS_CANCLE_AUTO.eq(bond.getStatus()) || BondEnum.STATUS_CANCLE_WEB.eq(bond.getStatus())) {
            //重复撤回校验
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_IS_CANCLE), backUrl, BussinessException.TYPE_JSON);
        }

        //2、已经售完 但是状态还没变为结束 不处理
        if (bond.getBondMoney().compareTo(BigDecimalUtils.defaultIfNull(bond.getSoldCapital())) <= Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_IS_SALE_OK), backUrl, BussinessException.TYPE_JSON);
        }
        return bond;
    }

    /**
     * 校验转让人
     *
     * @param bond
     * @param backUrl
     * @return
     * @author QianPengZhan
     * @date 2016年12月1日
     */
    private User validBondUser(final Bond bond, final String backUrl) {
        final User bondUser = userService.get(bond.getUserId());//查询转让人信息
        if (bondUser == null) {
            throw new BussinessException(ResourceUtils.get(UserResource.USER_NOT_LOGIN), backUrl, BussinessException.TYPE_JSON);
        }

        return bondUser;
    }

    /**
     * 校验待支付的订单
     *
     * @param id
     * @param backUrl
     * @author QianPengZhan
     * @date 2016年12月1日
     */
    private void validCount(final String id, final String backUrl) {
        final BondInvest model = new BondInvest();
        model.setStatus(BondInvestEnum.STATUS_INIT.getValue());
        model.setBondId(id);
        final int count = bondInvestService.getCount(model);//待支付的订单的数量
        if (count > Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_INIT_STATUS_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void cancleBond(final String id, final boolean isAuto, final String backUrl) {
        //1、校验
        final Bond bond = validCancleBond(id, backUrl);
        this.validBondUser(bond, backUrl);
        this.validCount(id, backUrl);

        //2、处理债权状态
        if (isAuto) {
            bond.setStatus(BondEnum.STATUS_CANCLE_AUTO.getValue());
        } else {
            bond.setStatus(BondEnum.STATUS_CANCLE_WEB.getValue());
        }

        //3、处理撤回逻辑
        handleCancelActivty(bond, backUrl);
    }

    /**
     * 处理撤回逻辑
     *
     * @param bond
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private void handleCancelActivty(final Bond bond, final String backUrl) {
        LOGGER.info("----------------------处理债权撤回--------------------start--");
        //1、获取信息和校验
        final BondRule rule = bondRuleService.getBondRule(bond.getRuleId());//查询当前债权标的对应债权规则
        if (rule == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_RULE_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        final ProjectInvest projectInvest = projectInvestService.get(bond.getInvestId());//查询当前债权对应的原始标投资记录
        if (projectInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        LOGGER.info("----------------------获取债权规则:{}和原始标投资记录:{}----------------------", rule.getUuid(), projectInvest.getUuid());

        //2、查询债权投资的最后一笔债权记录  看是否存在债权投资 若有投资 那么按照以下处理  否则需要做截标处理
        final BondInvest bondInvest = bondInvestService.getLastBondInvestByBondId(bond.getUuid(), BondInvestEnum.STATUS_COMPLETE.getValue());
        LOGGER.info("----------------------查询是否存在部分投资记录：{}----------------------", (bondInvest == null));
        if (bondInvest == null) {//没人投资情形下
            handleProjectWithNoInvest(rule, bond, projectInvest);
        } else {// 有人投资的情形下进行截标处理
            handleProjectWithnvest(rule, bond, projectInvest, bondInvest);
        }

        //3、数据库处理
        dao.update(bond);

        //4、缓存处理
        handleCacheWithCancle(bond);

        LOGGER.info("----------------------处理债权撤回--------------------end--");
    }

    /**
     * 债权撤回--缓存处理
     *
     * @param bond
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void handleCacheWithCancle(final Bond bond) {
        // 删除剩余可投债权金额
        BondCache.delBondRemainAccount(bond.getBondNo());
        CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
        CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND.concat(bond.getUuid()));
        CacheUtils.del(String.format(CacheKeys.PREFIX_BOND_INSERT_HANDLE_NUM.getKey(), bond.getInvestId()));
    }

    /**
     * 有人投资的情形下进行撤回
     *
     * @param rule
     * @param bond
     * @param projectInvest
     * @param bondInvest
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void handleProjectWithnvest(final BondRule rule, final Bond bond, final ProjectInvest projectInvest, final BondInvest bondInvest) {
        if (bondInvest.getAmount().compareTo(bond.getBondMoney()) >= Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_HAS_OK), BussinessException.TYPE_JSON);
        }
        // 1、将债权标截标 变为转让完成状态
        final BigDecimal overMoney = bond.getBondMoney().subtract(bond.getSoldCapital());
        bond.setStatus(BondEnum.STATUS_COMPLETE.getValue());//截标  改变状态
        bond.setBondMoney(bond.getSoldCapital());//截取已经售出的金额
        //2、截标
        if (BondRuleEnum.RULE_SELL_ALL.eq(rule.getSellStyle())) {
            LOGGER.info("---------------------有人投资、全额转让的情形下撤回,{}， 先将当前原始投资记录作废   然后 没有转让的金额进行新增----------------------", bondInvest.getUuid());
            //全部转让  有人投资人部分情形    先将当前原始投资记录作废   然后 没有转让的金额进行新增
            stopBond(projectInvest, overMoney);
        } else {
            if (CommonEnum.YES.eq(bond.getSellStyle())) {
                LOGGER.info("---------------------有人投资、部分转让（仍旧全额转让）的情形下撤回,{}， 先将当前原始投资记录作废   然后 没有转让的金额进行新增----------------------", bondInvest.getUuid());
                //全部转让  有人投资人部分情形    先将当前原始投资记录作废   然后 没有转让的金额进行新增
                stopBond(projectInvest, overMoney);
            } else {
                LOGGER.info("---------------------有人投资、部分转让的情形下撤回,{}，  先将拆分的2个原始标投资记录权都作废    再讲没有转让的金额进行新增----------------------", bondInvest.getUuid());
                //部分转让  有人投资部分情形    先将拆分的2个原始标投资记录权都作废    再讲没有转让的金额进行新增
//                final ProjectInvest otherProjectInvest = handleTwoProject(projectInvest);
                
                final ProjectInvest otherProjectInvest = projectInvestService.getInvestByOriIdAndUuid(projectInvest.getOriginInvestId(), projectInvest.getUuid());
                if (otherProjectInvest == null) {
                    throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), BussinessException.TYPE_JSON);
                }

                BigDecimal remainBond = overMoney.add(otherProjectInvest.getAmount());
                handleAddProjectInvest(otherProjectInvest, remainBond);
                
                //1、当前原始投资记录作废
                handleCurrentProjectInvest(projectInvest);
                //2、另一笔拆分的记录作废
                otherProjectInvest.setStatus(InvestEnum.STATUS_DELETE.getValue());
                List<ProjectCollection> otherProjectInvestCollection = handleProjectCollection(otherProjectInvest, CommonEnum.YES.getValue());
                projectInvestService.update(otherProjectInvest);
                projectCollectionService.updateBatch(otherProjectInvestCollection);
            }
        }
    }

    /**
     * 无人投资的情形下进行撤回
     *
     * @param rule
     * @param bond
     * @param projectInvest
     * @author QianPengZhan
     * @date 2016年11月7日
     */
    private void handleProjectWithNoInvest(final BondRule rule, final Bond bond, final ProjectInvest projectInvest) {
        ProjectInvest newProjectInvest = new ProjectInvest();
        if (BondRuleEnum.RULE_SELL_ALL.eq(rule.getSellStyle())) {
            LOGGER.info("----------------------没人投资、全额转让的情形下撤回,只改变原始标投资记录的bondFlag值为0 即可----------------------");
            //全部转让、无人投资的情形下   对应原始标的投资记录改为可转让即可
            newProjectInvest = handleProjectInvest(projectInvest);
        } else {
            if (CommonEnum.YES.eq(bond.getSellStyle())) {
                LOGGER.info("----------------------没人投资、部分转让（仍旧全部转让）的情形下撤回,只改变原始标投资记录的bondFlag值为0 即可----------------------");
                //全部转让、无人投资的情形下 对应原始标的投资记录改为可转让即可
                newProjectInvest = handleProjectInvest(projectInvest);
            } else {
                LOGGER.info("----------------------没人投资、部分转让的情形下撤回,已经拆分的2笔投资记录和待收均作废处理，再还原原来的投资记录和待收----------------------");
                //部分转让  无人投资的情形下   因为已经拆分 先将拆分的2个原始标投资记录权都作废  然后再还原最原始的投资记录
                handleTwoProject(projectInvest);
                newProjectInvest = handleReturnProjectInvest(projectInvest);
                
                //状态重新处理
                final ProjectInvest oriProjectInvest = projectInvestService.get(projectInvest.getOriginInvestId());//查询最初的原始投资记录
                final ProjectCollection model = new ProjectCollection();
                model.setInvestId(oriProjectInvest.getUuid());
                model.setUserId(oriProjectInvest.getUserId());
                model.setProjectId(oriProjectInvest.getProjectId());
                final List<ProjectCollection> projectCollectionList = projectCollectionService.findPcByPIdAndUserId(model);
                for (final ProjectCollection collection : projectCollectionList) {
                	if (collection.getPeriod() >= bond.getStartPeriod()) {
                		LOGGER.info("--------------没人投资、部分转让的情形下撤回,状态重新处理-------------uuid:{}", collection.getUuid());
                		collection.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());//未还
                		collection.setLastRepayTime(null);
                    }
                }
                projectCollectionService.updateBatch(projectCollectionList);
            }
        }
        projectInvestService.update(newProjectInvest);
    }

    /**
     * 先将拆分的2个原始标投资记录权都作废
     *
     * @param projectInvest
     * @return
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private ProjectInvest handleTwoProject(final ProjectInvest projectInvest) {
        //1、当前原始投资记录作废
        handleCurrentProjectInvest(projectInvest);
        //2、另一笔拆分的记录作废
        return handleOtherProjectInvest(projectInvest);
    }

    /**
     * 截标
     *
     * @param projectInvest
     * @param remainBond
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private void stopBond(final ProjectInvest projectInvest, final BigDecimal remainBond) {
        //1、没有转让的金额进行新增
        handleAddProjectInvest(projectInvest, remainBond);
        //2、当前原始投资记录作废
        handleCurrentProjectInvest(projectInvest);
    }

    /**
     * 没有转让的金额进行新增
     *
     * @param projectInvest
     * @param remainBond
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private void handleAddProjectInvest(final ProjectInvest projectInvest, final BigDecimal remainBond) {
        final Project project = projectService.get(projectInvest.getProjectId());
        final ProjectWorker projectWor = new ProjectWorker(project);//生成项目工程
        //根据金额生成待收利息 待收本金
        final InterestCalculator icPublish = projectWor.interestCalculator(remainBond, project.getApr(), BigDecimal.ZERO, projectInvest.getCreateTime());
        final BigDecimal capitalPublish = remainBond;//本金（剩余）
        final BigDecimal interestPublish = icPublish.repayInterest();//利息

        //插入金额对应新的投资记录
        ProjectInvest projectInvestPublish = new ProjectInvest();//标识债权投资记录
        projectInvestPublish = projectInvest.instance();
        projectInvestPublish.setUuid(IdGen.uuid());//修改uuid
        projectInvestPublish.setMoney(capitalPublish);
        projectInvestPublish.setAmount(capitalPublish);
        projectInvestPublish.setRealAmount(capitalPublish);
        projectInvestPublish.setPayment(BigDecimalUtils.add(capitalPublish, interestPublish));
        projectInvestPublish.setInterest(interestPublish);
        
        //重新计算repayedAmount repayedInterest waitAmount waitInterest
        BigDecimal repayedAmount = BigDecimal.ZERO;
        BigDecimal repayedInterest = BigDecimal.ZERO;
        BigDecimal waitAmount = BigDecimal.ZERO;
        BigDecimal waitInterest = BigDecimal.ZERO;
//        projectInvestPublish.setWaitAmount(BigDecimalUtils.add(capitalPublish, interestPublish));//TODO
//        projectInvestPublish.setWaitInterest(interestPublish);
        
        projectInvestPublish.setWaitRaiseInterest(BigDecimal.ZERO);
        projectInvestPublish.setCreateTime(DateUtils.getNow());
        projectInvestPublish.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
        projectInvestPublish.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());
        projectInvestPublish.setInvestStyle(projectInvest.getInvestStyle());
        projectInvestPublish.setRaiseInterest(BigDecimal.ZERO);//剩余部分+ 拆分部分租在一起   依旧要让加息利息为0
        // 根据投资记录生成待收记录    重新生成原始标待收记录   几期就是几条   生成的是普通待收
        final List<ProjectCollection> collectionListPublish = projectWor.createCollectionList(projectInvestPublish, icPublish);
        for (final ProjectCollection pc : collectionListPublish) {
            //处理转让金额部分的待收属性
            final ProjectCollection oriPc = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(projectInvest.getUuid(),
                    pc.getPeriod());//查询当前期数的原始标对应的待收记录
            pc.setParentId(oriPc.getUuid());
            pc.setRepayTime(oriPc.getRepayTime());
            pc.setCollectionType(oriPc.getCollectionType());
//            pc.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());
            pc.setStatus(oriPc.getStatus());
            if (ProjectCollectionEnum.STATUS_PAID.getValue().equals(oriPc.getStatus())) {
            	pc.setInterest(oriPc.getInterest());
            	pc.setCapital(oriPc.getCapital());
            	pc.setPayment(oriPc.getPayment());
            	pc.setLastRepayTime(oriPc.getLastRepayTime());
            	pc.setRepayedAmount(oriPc.getRepayedAmount());
            	pc.setManageFee(oriPc.getManageFee());
            	
            	repayedAmount = BigDecimalUtils.add(repayedAmount, pc.getRepayedAmount());
            	repayedInterest = repayedAmount;
            } else {
            	waitAmount = BigDecimalUtils.add(waitAmount, pc.getCapital(), pc.getInterest());
            	waitInterest = BigDecimalUtils.add(waitInterest, pc.getInterest());
            }
//            pc.setRepayedAmount(oriPc.getRepayedAmount());
            pc.setRaiseInterest(BigDecimal.ZERO);//剩余部分+ 拆分部分租在一起   依旧要让加息利息为0
        	pc.setBorrowManageFee(oriPc.getBorrowManageFee());//TODO 不确定
        }
        LOGGER.info("-------撤销repayedAmount:{},repayedInterest:{},waitAmount:{},waitInterest:{}---------", repayedAmount, repayedInterest, waitAmount, waitInterest);
        projectInvestPublish.setRepayedAmount(repayedAmount);
        projectInvestPublish.setRepayedInterest(repayedInterest);
        projectInvestPublish.setWaitAmount(waitAmount);
        projectInvestPublish.setWaitInterest(waitInterest);
        
        //父投资id增加
        String originInvestId = projectInvest.getUuid();
        ProjectInvest originInvest = null;
        if (projectInvest.getOriginInvestId() != null) {
        	originInvest = projectInvestService.get(projectInvest.getOriginInvestId());
        	if (originInvest.getInvestOrderNo().equals(projectInvest.getInvestOrderNo())) {
        		originInvestId = projectInvest.getOriginInvestId();
        		LOGGER.info("----------撤销originInvestId确认---------------", originInvestId);
        	}
        }
        projectInvestPublish.setOriginInvestId(originInvestId);
        
        projectInvestService.insert(projectInvestPublish);
        projectCollectionService.insertBatch(collectionListPublish);
    }


    /**
     * 处理最初的原始投资记录   取消作废变为投资成功
     *
     * @param projectInvest
     * @return
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private ProjectInvest handleReturnProjectInvest(final ProjectInvest projectInvest) {
        ProjectInvest newProjectInvest = new ProjectInvest();
        final ProjectInvest oriProjectInvest = projectInvestService.get(projectInvest.getOriginInvestId());//查询最初的原始投资记录
        if (oriProjectInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        newProjectInvest = handleProjectInvest(oriProjectInvest);
        newProjectInvest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
        List<ProjectCollection> oriProjectInvestCollection = handleProjectCollection(oriProjectInvest, CommonEnum.NO.getValue());
        projectCollectionService.updateBatch(oriProjectInvestCollection);
        return newProjectInvest;
    }

    /**
     * 另一笔拆分的记录作废
     *
     * @param projectInvest 当前原始投资记录
     * @return
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private ProjectInvest handleOtherProjectInvest(final ProjectInvest projectInvest) {
        final ProjectInvest otherProjectInvest = projectInvestService.getInvestByOriIdAndUuid(projectInvest.getOriginInvestId(), projectInvest.getUuid());
        if (otherProjectInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_INVEST_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        otherProjectInvest.setStatus(InvestEnum.STATUS_DELETE.getValue());
        List<ProjectCollection> otherProjectInvestCollection = handleProjectCollection(otherProjectInvest, CommonEnum.YES.getValue());
        projectInvestService.update(otherProjectInvest);
        projectCollectionService.updateBatch(otherProjectInvestCollection);
        return otherProjectInvest;
    }

    /**
     * 当前原始投资记录作废
     *
     * @param projectInvest 当前原始投资记录
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private void handleCurrentProjectInvest(final ProjectInvest projectInvest) {
        projectInvest.setStatus(InvestEnum.STATUS_DELETE.getValue());
        List<ProjectCollection> projectInvestCollection = handleProjectCollection(projectInvest, CommonEnum.YES.getValue());
        projectInvestService.update(projectInvest);
        projectCollectionService.updateBatch(projectInvestCollection);
    }

    /**
     * 拆分的情形下 处理待收
     *
     * @param projectInvest 原始标投资记录
     * @param flag          作废还是还原的标识  1 作废 0 还原
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private List<ProjectCollection> handleProjectCollection(final ProjectInvest projectInvest, final String flag) {
        final ProjectCollection model = new ProjectCollection();
        model.setInvestId(projectInvest.getUuid());
        model.setUserId(projectInvest.getUserId());
        model.setProjectId(projectInvest.getProjectId());
        final List<ProjectCollection> projectCollectionList = projectCollectionService.findPcByPIdAndUserId(model);
        for (ProjectCollection projectCollection : projectCollectionList) {
            if (CommonEnum.YES.eq(flag)) {
                projectCollection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());//已还
                projectCollection.setCollectionType(ProjectCollectionEnum.COLLECTION_TYPE_REMOVE.getValue());//作废待收类型
            } else if (CommonEnum.NO.eq(flag)) {
//                projectCollection.setStatus(ProjectCollectionEnum.STATUS_NOT_REPAY.getValue());//未还
//                projectCollection.setLastRepayTime(null);
            }
        }
        return projectCollectionList;
    }

    /**
     * 没有拆分的情形下处理原始投资记录
     *
     * @param projectInvest
     * @return
     * @author QianPengZhan
     * @date 2016年9月27日
     */
    private ProjectInvest handleProjectInvest(final ProjectInvest projectInvest) {
        final ProjectInvest newProjectInvest = new ProjectInvest();
        newProjectInvest.setUuid(projectInvest.getUuid());
        newProjectInvest.setBondFlag(InvestEnum.BOND_FLAG_NORMAL.getValue());
        newProjectInvest.setInvestStyle(projectInvest.getInvestStyle());//处理原始投资记录   保持投资类型不变
        newProjectInvest.setEndDate(null);
        return newProjectInvest;
    }

    @Override
    @Transactional(readOnly = false)
    public void autoCancleBond() {
        //1、找出超过有效期的债权标
        List<Bond> cancleList = new ArrayList<Bond>();  //正在债权转让的超时的列表
        cancleList = findCancleList();

        //2、进行批量处理超时撤回
        if (CollectionUtils.isNotEmpty(cancleList)) {
            handleCancleBond(cancleList);
        } else {
            LOGGER.info("没有需要自动撤回的债权标");
        }
    }


    /**
     * 找出超过有效期的债权标
     *
     * @return
     * @author QianPengZhan
     * @date 2016年8月18日
     */
    private List<Bond> findCancleList() {
        final List<Bond> cancleList = new ArrayList<Bond>();  //正在债权转让的超时的列表
        final Bond model = new Bond();
        model.setStatus(BondEnum.STATUS_PUBLISH.getValue());//查出正在债权转让的标
        final List<Bond> bondList = dao.findList(model);
        if (CollectionUtils.isNotEmpty(bondList)) {
            for (final Bond bond : bondList) {
                final Date now = DateUtils.getNow();//当前时间
                final Project project = projectService.getProjectByUuid(bond.getProjectId());
                final ProjectCollection projectCollection = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(bond.getInvestId(), bond.getStartPeriod());
                int diffPeriodHours = Constant.INT_ONE;
                int diffPeriodRealHours = Constant.INT_ONE;
                if (projectCollection != null) {
                    //本期还款日
                    if (projectCollection.getRepayTime() != null) {
                        final Date periodRepayTime = projectCollection.getRepayTime();//本期应还款日期
                        diffPeriodHours = (int) DateUtils.getDistanceOfTwoHours(now, periodRepayTime);
                    }
                    //本期实际还款日
                    if (projectCollection.getLastRepayTime() != null) {
                        final Date periodRealRepayTime = projectCollection.getLastRepayTime();//本期已经还款日期
                        diffPeriodRealHours = (int) DateUtils.getDistanceOfTwoHours(now, periodRealRepayTime);
                    }
                }
                //还款日
                final Date repayTime = bond.getBondEndTime() == null ? DateUtils.getNow() : bond.getBondEndTime();//债权最终应还款时间
                final int diffHours = (int) DateUtils.getDistanceOfTwoHours(now, repayTime);
                //实际还款日
                int diffLastHours = Constant.INT_ONE;
                if (!LoanEnum.STYLE_ONETIME_REPAYMENT.eq(project.getRepayStyle())) {
                    final int totalPeriods = project.getTimeLimit();
                    final ProjectCollection lastProjectCollection = projectCollectionService.getProjectCollectionByInvestIdAndPeriod(bond.getInvestId(), totalPeriods);
                    if (lastProjectCollection != null && lastProjectCollection.getLastRepayTime() != null) {
                        final Date lastRepayTime = lastProjectCollection.getLastRepayTime();//实际还款日
                        diffLastHours = (int) DateUtils.getDistanceOfTwoHours(now, lastRepayTime);
                    }
                }
                //有效期
                final Date createTime = bond.getCreateTime();//发布时间
                final int limitHours = bond.getLimitHours();//有效时长
                final int timeOverSeconds = limitHours * Constant.INT_SIXTEN * Constant.INT_SIXTEN;//有效期  到秒
                final long pastSeconds = DateUtils.pastSeconds(createTime);
                LOGGER.info("【超时自动撤回债权】：" + bond.getUuid() + ",设置的有效时长："
                        + limitHours + ",实际时长：" + timeOverSeconds + ",是否超时：" + (timeOverSeconds < pastSeconds));
                if (timeOverSeconds < pastSeconds || diffHours < Constant.INT_ONE || diffLastHours < Constant.INT_ONE
                        || diffPeriodHours < Constant.INT_ONE || diffPeriodRealHours < Constant.INT_ONE) {
                    /**
                     * 1\过了有效期 加入撤回列
                     * 2\还款日具体时间前1小时   不允许有转让中的债权标
                     * 3\实际还款日具体时间前1小时   不允许有转让中的债权标
                     * 4\本期还款日具体时间前1小时   不允许有转让中的债权标
                     * 5\本期实际还款日具体时间前1小时   不允许有转让中的债权标
                     */

                    cancleList.add(bond);
                }
            }
        }
        return cancleList;
    }

    /**
     * 批量处理需要撤回的债权标
     *
     * @param cancleList
     * @author QianPengZhan
     * @date 2016年8月18日
     */
    private void handleCancleBond(final List<Bond> cancleList) {
        for (final Bond bond : cancleList) {
            //判断已经撤回的不再处理
            if (BondEnum.STATUS_CANCLE_AUTO.eq(bond.getStatus()) || BondEnum.STATUS_CANCLE_WEB.eq(bond.getStatus())) {
                continue;
            }
            if (bond.getBondMoney().compareTo(BigDecimalUtils.defaultIfNull(bond.getSoldCapital())) <= Constant.INT_ZERO) {
                continue;
            }
            this.cancleBond(bond.getUuid(), true, StringUtils.EMPTY);
        }
    }


    @Override
    //@Transactional(readOnly=false)
    public void bondRemainDaysChangeByDay() {
        //1、查询出所有需要更新剩余期限的债权标
        final List<BondModel> changeList = searchBondList();

        //2、更新剩余期限
        final List<Bond> bList = new ArrayList<Bond>();
        if (CollectionUtils.isNotEmpty(changeList)) {
            final Date now = DateUtils.getNow();//当前时间
            for (final BondModel bondModel : changeList) {
                final Bond bond = new Bond();
                final Date repayTime = bondModel.getBondEndTime() == null ? now : bondModel.getBondEndTime();//还款时间
                final int newRemainDays = (int) DateUtils.daysBetween(now, repayTime);
                if (newRemainDays <= Constant.INT_ZERO || newRemainDays == bondModel.getRemainDays()) {
                    continue;
                }
                LOGGER.info("【更新剩余期限】：{},原来的剩余期限为:{},现在的剩余期限：{}", bondModel.getUuid(), bondModel.getRemainDays(), newRemainDays);
                bond.setUuid(bondModel.getUuid());
                bond.setRemainDays(newRemainDays);
                bList.add(bond);
            }
        }
        if (CollectionUtils.isNotEmpty(bList)) {
            dao.updateRemainDaysBatch(bList);
        }
    }

    /**
     * 查询出所有需要更新剩余期限的债权标
     *
     * @return
     * @author QianPengZhan
     * @date 2016年8月18日
     */
    private List<BondModel> searchBondList() {
        //只更新转让中、和转让完成的债权标    撤回和过期撤回的标 不更新剩余期限
        final BondModel model = new BondModel();
        model.setStatusSet(new String[]{BondEnum.STATUS_COMPLETE.getValue(),
                BondEnum.STATUS_PUBLISH.getValue()});//只处理转让中和转让成功  撤回的不处理
        return dao.findModelList(model);
    }

    @Override
    public Bond checkBondInfo(final BondInvestModel invest, final String backUrl) {
        if (invest.getAmount() == null) {
            throw new BussinessException("请输入投资金额", backUrl, BussinessException.TYPE_CLOSE);
        }
        // 投资功能冻结
        if (userFreezeService.isFreezed(invest.getUserId(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
            throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), backUrl, BussinessException.TYPE_CLOSE);
        }
        //直接根据缓存取到信息
        final Bond bond = this.getBond(invest.getBondId(), backUrl);
        final BondModel bondModel = bond.prototype();
        if (StringUtils.isBlank(bondModel.getProjectId())) {
            throw new BussinessException(ResourceUtils.get(BondResource.PROJECT_ID_NOT_EXISTS), backUrl,
                    BussinessException.TYPE_JSON);
        }

        //订单页面 剩余债权金额的校验
        final double bondRemainAccount = BondCache.getBondRemainAccount(bond.getBondNo());
        LOGGER.info("债权剩余可投金额为：{},用户投资金额为:{}", bondRemainAccount, invest.getAmount());
        if (invest.getAmount().compareTo(BigDecimal.valueOf(bondRemainAccount)) > Constant.INT_ZERO) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ACCOUNTNOTENOUGH), backUrl,
                    BussinessException.TYPE_JSON);
        }
        return bond;
    }

    @Override
    public Map<String, Object> getRepayPlan(final String bondInvestId, final String backUrl) {
        final Map<String, Object> map = new HashMap<String, Object>();
        //1、校验传参
        if (StringUtils.isBlank(bondInvestId)) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_ID_NOT_EXISTS), backUrl,
                    BussinessException.TYPE_JSON);
        }

        //2、校验受让记录
        final BondInvest bondInvest = bondInvestService.get(bondInvestId);
        if (bondInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOT_EXISTS), backUrl,
                    BussinessException.TYPE_JSON);
        }

        //3、查询受让记录对应的原始标下的待收信息
        final ProjectCollection model = new ProjectCollection();
        model.setInvestId(bondInvest.getInvestId());
        final List<ProjectCollection> list = projectCollectionService.findList(model);
        final List<ProjectCollection> showList = new ArrayList<ProjectCollection>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (final ProjectCollection projectCollection : list) {
                final ProjectCollection showProjectCollection = new ProjectCollection();
                showProjectCollection.setPeriod(projectCollection.getPeriod());//期数
                showProjectCollection.setPeriods(list.size());//总期数
                showProjectCollection.setRepayTime(projectCollection.getRepayTime());//预期回款日期
                showProjectCollection.setPayment(projectCollection.getPayment());//预计回资金额
                showProjectCollection.setCapital(projectCollection.getCapital());//回款本金
                showProjectCollection.setStatus(projectCollection.getStatus());//状态
                showProjectCollection.setLastRepayTime(projectCollection.getLastRepayTime());//实际回款日期
                showProjectCollection.setRepayedAmount(projectCollection.getRepayedAmount());//实际回款金额
                showList.add(showProjectCollection);
            }
        }

        //4、查询产品信息
        final Project project = projectService.getProjectValid(bondInvest.getProjectId(), backUrl);

        //5、录入数据
        map.put("projectName", project.getProjectName());
        map.put("result", true);
        map.put("msg", StringUtils.EMPTY);
        map.put("list", showList);//回款计划列表
        return map;
    }


    @Override
    public Bond getBondByBondNo(final String bondNo) {
        return dao.getBondByBondNo(bondNo);
    }


    @Override
    @Cacheable(key = CacheConstant.KEY_PREFIX_BOND_UUID, expire = ExpireTime.FIVE_MIN)
    public Bond getBond(final String bondId, final String backUrl) {
        if (StringUtils.isBlank(bondId)) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_ID_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        final Bond bond = dao.get(bondId);
        if (bond == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS), backUrl, BussinessException.TYPE_JSON);
        }
        return bond;
    }

    @Override
    public void updateSoldCapitalById(final double money, final String uuid) {
        dao.updateSoldCapitalById(money, uuid);
    }

    @Override
    public void updateBondOtherInfo(final BigDecimal soldInterest,
                                    final BigDecimal bondFee, final Date successTime, final String uuid) {
        dao.updateBondOtherInfo(soldInterest, bondFee, successTime, uuid);
    }

    @Override
    public void updateBondStatusById(final String newStatus, final String oldStatus,
                                     final String uuid) {
        dao.updateBondStatusById(newStatus, oldStatus, uuid);
    }

    /**
     * 债权投资人单份下载债权协议
     *
     * @param bondInvestId
     * @param user
     * @param userCache
     * @return
     * @author QianPengZhan
     * @date 2016年9月29日
     */
    @Override
    @Transactional(readOnly = false)
    public String downloadBondProtocol(final String bondInvestId) {
        final BondInvest bondInvest = bondInvestService.get(bondInvestId);
        if (bondInvest == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        final Bond bond = dao.get(bondInvest.getBondId());
        if (bond == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        String fileName = bond.getBondName() + "_" + bondInvest.getInvestOrderNo() + ".pdf";
        String url = "/upload/downloadBondContract.html";
        String webUrl = ConfigUtils.getValue(ConfigConstant.WEB_URL);
        return protocolService.downloadBondProtocol(bond.getUuid(), bondInvest.getUuid(), fileName, url, webUrl);
    }

    /**
     * 债权转让人下载全部协议
     */
    @Override
    public String downloadAllBondProtocol(final String bondId) {
        final Bond bond = dao.get(bondId);
        if (bond == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        String fileName = bond.getBondName() + "_" + bond.getBondNo() + ".zip";
        String url = "/upload/downloadBondContract.html";
        String webUrl = ConfigUtils.getValue(ConfigConstant.WEB_URL);
        return protocolService.downloadBondProtocol(bond.getUuid(), bond.getUuid(), fileName, url, webUrl);
    }

    /**
     * 协议不存在的时候 重新生成协议
     */
    @Override
    public void handleAllBondProtocol(final String bondId, final String bondInvestId) {
        final Bond bond = dao.get(bondId);
        if (bond == null) {
            throw new BussinessException(ResourceUtils.get(BondResource.BOND_NOT_EXISTS), BussinessException.TYPE_JSON);
        }
        if (bondId.equals(bondInvestId)) {
            final BondInvest model = new BondInvest();
            model.setBondId(bondId);
            model.setStatus(BondInvestEnum.STATUS_COMPLETE.getValue());
            final List<BondInvest> bondInvestList = bondInvestService.findList(model);
            if (CollectionUtils.isNotEmpty(bondInvestList)) {
                for (final BondInvest invest : bondInvestList) {
                    protocolService.buildBondProtocol(invest.getUuid(), bond.getUserId());
                    protocolService.buildBondProtocol(invest.getUuid(), invest.getUserId());
                }
            }
        } else {
            final BondInvest bondInvest = bondInvestService.get(bondInvestId);
            if (bondInvest == null) {
                throw new BussinessException(ResourceUtils.get(BondResource.BOND_INVEST_NOT_EXISTS), BussinessException.TYPE_JSON);
            }
            protocolService.buildBondProtocol(bondInvest.getUuid(), bond.getUserId());
            protocolService.buildBondProtocol(bondInvest.getUuid(), bondInvest.getUserId());
        }
    }

    @Override
    public int getCountByStatus(String projectId, String status) {
        return dao.getCountByStatus(projectId, status);
    }

    @Override
    public void updateStage(Integer stage, String uuid) {
        dao.updateStage(stage, uuid);
    }

    @Override
    public void bondMaxBondAprChangeByDay() {
        //查询所有在投的债权标
        final Bond model = new Bond();
        model.setStatus(BondEnum.STATUS_PUBLISH.getValue());//发布中
        final List<Bond> newBondList = new ArrayList<Bond>();
        final List<Bond> bondList = dao.findList(model);
        if (CollectionUtils.isNotEmpty(bondList)) {
            for (final Bond bond : bondList) {
                final Map<String, Object> data = new HashMap<>();
                data.put("bondValue", bond.getBondMoney());
                final ProjectInvest pInvest = projectInvestService.get(bond.getInvestId());
                final ProjectInvestModel proModel = pInvest.prototype();
                final Map<String, Object> dataMap = proModel.getAbleBondData(proModel);
                final BondRule bondRule = bondRuleService.get(bond.getRuleId());
                final BondRuleModel bondRuleModel = bondRule.instanceModel(bondRule);
                if (pInvest == null || bondRule == null || BondEnum.STATUS_COMPLETE.eq(bond.getStatus())) {
                    continue;//若债权标完成了就不再改变
                }
                LOGGER.info("是否继续{}", bond.getUuid());
                final Map<String, Object> ruleMap = getParamsByCurrentRule(dataMap, data, bondRuleModel);
                final BigDecimal max = BigDecimalUtils.valueOf(StringUtils.isNull(ruleMap.get("max")));
                //判断当前的最高溢价率 和  标的折溢价率
                if (max.compareTo(bond.getBondApr()) < Constant.INT_ZERO) {
                    //若标的折溢价率超过当前的最高折溢价率  那么则强制修改标的折溢价率为max
                    bond.setBondApr(max);
                    newBondList.add(bond);
                    LOGGER.info("债权标ID{}的折溢价率为{},今天的最高折溢价率为{},超过了，那么则强制修改标的折溢价率为max!,债权标的折溢价率为：{}", bond.getUuid(), bond.getBondApr(), max, bond.getBondApr());
                } else {
                    LOGGER.info("债权标ID{}的折溢价率为{},今天的最高折溢价率为{},没超过，无需修改!", bond.getUuid(), bond.getBondApr(), max);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(newBondList)) {
            dao.updateBatch(newBondList);
        }
    }

    @Override
    public String nextBondNo(String createDate) {
        return BondCache.getNextBondNo(createDate);
    }

    @Override
    public boolean judgeBondAprLimit(final ProjectInvestModel projectInvestModel) {
        boolean flag = false;
        final Map<String, Object> data = new HashMap<String, Object>();
        final Map<String, Object> dataMap = projectInvestModel.getAbleBondData(projectInvestModel);//获取可转让的某些特定的信息
        final int holdDay = (int) dataMap.get("holdDay");//持有天数
        projectInvestModel.setRemainDays((int) dataMap.get("remainDay"));//剩余期限
        final Project project = projectService.get(projectInvestModel.getProjectId());
        final BondRuleModel bondRule = bondRuleService.getRecentBondRuleModel();
        if (project == null || bondRule == null) {
            flag = true;
        } else {
            final BigDecimal repayedCapital = this.getRepayedMoney(project, projectInvestModel.prototype(), false);
            final BigDecimal bondValue = projectInvestModel.getAmount().subtract(repayedCapital);
            if (bondValue.compareTo(BigDecimal.ZERO) == Constant.INT_ZERO) {
                flag = true;
            } else {
                data.put("bondValue", bondValue);//可转让债权  (录入最大最小折溢价率用的到)   这个金额为投资金额-已还本金
                data.put("repayedCapital", repayedCapital);//已还本金
                final Map<String, Object> map = this.getParamsByCurrentRule(dataMap, data, bondRule);
                flag = (boolean) map.get("bondAprError");
            }
        }
        //持有待收天数不足，不允许发布债权校验  不显示
        if (flag == false && holdDay < bondRule.getHoldDays()) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Page<BondModel> findManagePage(BondModel bond) {
        final List<BondModel> list = dao.findManagePage(bond);
        bond.getPage().setRows(list);
        return bond.getPage();
    }

    @Override
    public BigDecimal getRepayedMoney(ProjectInvestModel pInvestInvestModel) {
        final Project project = projectService.get(pInvestInvestModel.getProjectId());
        final ProjectInvest projectInvest = pInvestInvestModel.prototype();
        return this.getRepayedMoney(project, projectInvest, false);
    }
}