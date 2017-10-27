package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.ObjectUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.TppWarnMsg;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeFreezeService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.mapper.TppTradeMapper;
import com.rd.ifaes.core.tpp.model.TppTradeModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBatInvestCancleModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbExperCapModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAdditionalOrderModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.tpp.service.tpp.UfxLoanService;
import com.rd.ifaes.core.tpp.service.tpp.UfxRepayService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:TppTradeServiceImpl
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-18
 */
@Service("tppTradeService")
public class TppTradeServiceImpl extends BaseServiceImpl<TppTradeMapper, TppTrade> implements TppTradeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TppTradeServiceImpl.class);
    @Resource
    private ProjectInvestService projectInvestService;
    @Resource
    private RealizeFreezeService realizeFreezeService;
    @Resource
    private ProjectService projectService;
    @Resource
    private UserService userService;
    @Resource
    private TppMerchantLogService tppMerchantLogService;
    @Resource
    private UfxLoanService ufxLoanService;
    @Resource
    private UfxRepayService ufxRepayService;
    @Resource
    private ProjectRepaymentService projectRepaymentService;
    @Resource
    private ProjectCollectionService projectCollectionService;

    //tppTrade 调度重做时间间隔
    private static final long TPPTRADE_REDO_INTERVAL_TIME = 5 * 60 * 1000;

    @Override
    public void doTppTask(List<TppTradeModel> taskList) {
        //是否执行成功标识
        boolean flag = true;
        StringBuilder errorUserNameList = new StringBuilder();
        StringBuilder orderNoList = new StringBuilder();
        StringBuilder operatorNameList = new StringBuilder();
        for (TppTradeModel tppModel : taskList) {
            try {
                if (TppEnum.TPP_TYPE_INVEST_FAIL.eq(tppModel.getTppType())) {
                    // 投资失败
                    doInvestFail(tppModel);
                } else if (TppEnum.TPP_TYPE_UNFREEZE.eq(tppModel.getTppType())) {
                    // 解冻
                    doUnfreeze(tppModel);
                } else if (TppEnum.TPP_TYPE_FREEZE.eq(tppModel.getTppType())) {
                    // 冻结
                    doFreeze(tppModel);
                } else if (TppEnum.TPP_TYPE_LOAN.eq(tppModel.getTppType())) {
                    // 放款
                    doInvestLoan(tppModel);
                } else if (TppEnum.TPP_TYPE_REPAY.eq(tppModel.getTppType())) {
                    // 还款
                    doRepay(tppModel);
                }
                tppModel.setStatus(TppEnum.STATUS_SUCEESS.getValue());
                tppModel.setRespDesc(TppEnum.STATUS_SUCEESS.getName());
                LOGGER.info("三方回调成功, 订单号:{}, uuid:{},money:{},serv_fee:{}", tppModel.getOrderNo(), tppModel.getUuid(), tppModel.getMoney(), tppModel.getServFee());
            } catch (Exception e) {
                flag = false;
                final User user = userService.get(tppModel.getUserId());
                if (!errorUserNameList.toString().contains(user.getUserName())) {
                    errorUserNameList.append(user.getUserName()).append(",");
                }
                orderNoList.append(tppModel.getOrderNo()).append(",");
                if (!operatorNameList.toString().contains(tppModel.getTppType())) {
                    operatorNameList.append(tppModel.getTppType()).append(",");
                }
                tppModel.setStatus(TppEnum.STATUS_FAIL.getValue());
                tppModel.setRespDesc(e.getMessage());
                LOGGER.error("三方回调失败：{}, 订单号:{}, uuid:{}", e.getMessage(), tppModel.getOrderNo(), tppModel.getUuid(), e);
            }

        }

        if (taskList.size() > 0) {
            this.updateCallback(taskList);
        }
        //如果有失败的情形  发送预警信息   走队列
        if (!flag) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("operUserName", ConfigUtils.getValue(ConfigConstant.ACCOUNT_WARN_CONTACTS));
            params.put("web_name", ConfigUtils.getValue(ConfigConstant.WEB_NAME));
            params.put("errorUserNameList", errorUserNameList);
            params.put("createTime", DateUtils.getDateTime());
            params.put("orderNoList", orderNoList);
            params.put("operatorNameList", operatorNameList);
            if (ConfigUtils.isOpenMq()) {
                RabbitUtils.tppWarn(params);
            } else {
                TppWarnMsg message = new TppWarnMsg(MessageConstant.TPP_WARN, params);
                message.doEvent();
            }
        }
    }

    @Transactional(readOnly = false)
    public void updateCallback(List<TppTradeModel> list) {
        dao.updateCallback(list);
    }

    @Override
    public int updateStatusByTradeNo(String tradeNo, String status, String preStatus, String remark) {
        return dao.updateStatusByTradeNo(tradeNo, status, preStatus, remark);
    }

    @Override
    public TppTrade findByTradeNoAndType(String tradeNo, String type, String status) {
        return dao.findByTradeNoAndType(tradeNo, type, status);
    }

    @Override
    public List<TppTrade> getListByTradeNoAndType(String tradeNo, String type, String status) {
        return dao.getListByTradeNoAndType(tradeNo, type, status);
    }

    @Override
    public int countByTradeNoAndType(String tradeNo, String type, String status) {
        return dao.countByTradeNoAndType(tradeNo, type, status);
    }

    @Override
    @Transactional
    public String doJxTppTask(String id) {
        TppTrade trade = dao.get(id);
        if (trade == null) {
            return "找不到该交易！";
        }
        String tppType = "";
        if (TppEnum.TPP_TYPE_BATCH_LOAN.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_LOAN.getValue();
        } else if (TppEnum.TPP_TYPE_BATCH_REPAY.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_REPAY.getValue();
        } else if (TppEnum.TPP_TYPE_BATCH_BAIL_REPAY.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_BAIL_REPAY.getValue();
        } else if (TppEnum.TPP_TYPE_BATCH_VOUCHER.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_VOUCHER.getValue();
        } else if (TppEnum.TPP_TYPE_BATCH_REPAY_BAIL.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_REPAY_BAIL.getValue();
        } else if (TppEnum.TPP_TYPE_BATCH_CREDIT_END.getValue().equals(trade.getTppType())) {
            tppType = TppEnum.TPP_TYPE_CREDIT_END.getValue();
        } else {
            return "无法重新触发该交易！";
        }
        if (TppEnum.STATUS_SUCEESS.getValue().equals(trade.getStatus())) {
            return "交易已成功，请勿重复触发！";
        }
        int countUndo = dao.countByTradeNoAndType(trade.getTradeNo(), tppType, TppEnum.STATUS_UNDO.getValue());
        if (countUndo > 0) {
            return "还有未处理的交易，请等待交易全部处理完成！";
        }
        //获得该批量交易的所有子交易
        List<TppTrade> list = dao.getListByTradeNoAndType(trade.getTradeNo(), tppType, TppEnum.STATUS_FAIL.getValue());
        List<TppTrade> updateList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            Map<String, Object> params = new HashMap<>();
            BigDecimal txAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal manageFeeTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
            int txCounts = 0;
            List<Map> subPacks = new ArrayList<>();
            Date now = new Date();
            String batchNo = OrderNoUtils.getRandomStr(6);
            String tradeNo = DateUtils.dateStr7(now).concat(batchNo);
            for (TppTrade tpp : list) {
                tpp.setTradeNo(tradeNo);
                tpp.setOrderNo(OrderNoUtils.getSerialNumber());
                tpp.setOrderDate(now);
                tpp.setUpdateTime(now);
                tpp.setRespDesc("重新触发");
                tpp.setStatus(TppEnum.STATUS_UNDO.getValue());
                Map<String, Object> subPack = new HashMap<>();
                if (TppEnum.SERVICE_TYPE_LOAN.getValue().equals(tpp.getServiceType())) {
                    BigDecimal money = tpp.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
                    subPack.put("accountId", tpp.getTppUserCustId());
                    subPack.put("orderId", tpp.getOrderNo());
                    subPack.put("txAmount", money);
//                    if (tpp.getServFee().compareTo(BigDecimal.ZERO) > 0) {
//                        manageFeeTotal = manageFeeTotal.add(tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
//                        subPack.put("debtFee", tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
//                    }
                    subPack.put("forAccountId", tpp.getToTppUserCustId());
                    subPack.put("productId", tpp.getProjectId());
                    subPack.put("authCode", tpp.getAuthCode());
                    txAmount = txAmount.add(money);
                } else if (TppEnum.SERVICE_TYPE_REPAY.getValue().equals(tpp.getServiceType())) {
                    JSONObject object = JSON.parseObject(tpp.getParams());
                    BigDecimal capital = object.getBigDecimal("capital");
                    BigDecimal interest = object.getBigDecimal("interest");
                    BigDecimal lateInterest = object.getBigDecimal("lateInterest");
                    BigDecimal merchantLateInterest = object.getBigDecimal("merchantLateInterest");
                    BigDecimal borrowManageFee = object.getBigDecimal("borrowManageFee");
                    subPack.put("orderId", tpp.getOrderNo());
                    subPack.put("accountId", tpp.getTppUserCustId());
                    subPack.put("txAmount", capital.setScale(2, BigDecimal.ROUND_HALF_UP));
                    subPack.put("intAmount", interest.add(lateInterest).setScale(2, BigDecimal.ROUND_HALF_UP));
                    //加上借款人管理费
                	merchantLateInterest = BigDecimalUtils.validAmount(merchantLateInterest) ? merchantLateInterest : BigDecimal.ZERO;
                	borrowManageFee = BigDecimalUtils.validAmount(borrowManageFee) ? borrowManageFee : BigDecimal.ZERO;
                    if (merchantLateInterest.compareTo(BigDecimal.ZERO) > 0 || borrowManageFee.compareTo(BigDecimal.ZERO) > 0) {
//                        subPack.put("txFeeOut", merchantLateInterest.setScale(2, BigDecimal.ROUND_HALF_UP));//还款罚息
                    	subPack.put("txFeeOut",
								BigDecimalUtils.round(BigDecimalUtils.add(borrowManageFee, merchantLateInterest)));
						LOGGER.info("borrowManageFee:{},merchantLateInterest:{}", borrowManageFee, merchantLateInterest);
                    }
                    if (tpp.getServFee().compareTo(BigDecimal.ZERO) > 0) {
                        manageFeeTotal = manageFeeTotal.add(tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
                        subPack.put("txFeeIn", tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    subPack.put("forAccountId", tpp.getToTppUserCustId());
                    subPack.put("productId", tpp.getProjectId());
                    subPack.put("authCode", tpp.getAuthCode());
                    txAmount = txAmount.add(capital.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else if (TppEnum.TPP_TYPE_BAIL_REPAY.getValue().equals(tpp.getServiceType())) {
                    JSONObject object = JSON.parseObject(tpp.getParams());
                    BigDecimal capital = object.getBigDecimal("capital");
                    BigDecimal interest = object.getBigDecimal("interest");
                    BigDecimal lateInterest = object.getBigDecimal("lateInterest");
                    String orgOrderId = object.getString("orgOrderId");
                    BigDecimal orgTxAmount = object.getBigDecimal("orgTxAmount");
                    BigDecimal capitalInterestSum = capital.add(interest).add(lateInterest);
                    subPack.put("orderId", tpp.getOrderNo());
                    subPack.put("txCapAmout", capital.setScale(2, BigDecimal.ROUND_HALF_UP));
                    subPack.put("txIntAmount", interest.add(lateInterest).setScale(2, BigDecimal.ROUND_HALF_UP));
                    subPack.put("txAmount", capitalInterestSum.setScale(2, BigDecimal.ROUND_HALF_UP));
                    subPack.put("forAccountId", tpp.getToTppUserCustId());
                    subPack.put("orgOrderId", orgOrderId);
                    subPack.put("orgTxAmount", orgTxAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                    if (tpp.getServFee().compareTo(BigDecimal.ZERO) > 0) {
                        manageFeeTotal = manageFeeTotal.add(tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
                        subPack.put("txFee", tpp.getServFee().setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    txAmount = txAmount.add(capitalInterestSum.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else if (TppEnum.SERVICE_TYPE_VOUCHER.getValue().equals(tpp.getServiceType())) {
                    BigDecimal money = tpp.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
                    JSONObject object = JSON.parseObject(tpp.getParams());
                    String voucherType = object.getString("voucherType");
                    String name = object.getString("name");
                    subPack.put("orderId", tpp.getOrderNo());
                    subPack.put("txAmount", money);
                    subPack.put("forAccountId", tpp.getToTppUserCustId());
                    subPack.put("voucherType", voucherType);
                    subPack.put("name", name);
                    txAmount = txAmount.add(money.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else if (TppEnum.SERVICE_TYPE_REPAY_BAIL.getValue().equals(tpp.getServiceType())) {
                    subPack = JSON.parseObject(tpp.getParams(), new TypeReference<Map<String, Object>>() {
                    });
                    BigDecimal capital = new BigDecimal(StringUtils.isNull(subPack.get("txAmount")));
                    txAmount = txAmount.add(capital.setScale(2, BigDecimal.ROUND_HALF_UP));
                } else if (TppEnum.TPP_TYPE_CREDIT_END.getValue().equals(tpp.getServiceType())) {
                    subPack = JSON.parseObject(tpp.getParams(), new TypeReference<Map<String, Object>>() {
                    });
                }
                subPacks.add(subPack);
                txCounts++;
                updateList.add(tpp);
            }
            if (TppEnum.SERVICE_TYPE_BATCH_BAIL_REPAY.getValue().equals(trade.getServiceType())) {
                params.put("accountId", trade.getTppUserCustId());
                params.put("productId", trade.getProjectId());
            }
            params.put("txAmount", txAmount);
            params.put("txCounts", txCounts);
            params.put("subPacks", subPacks);
            params.put("batchNo", batchNo);
            //更新批量交易记录信息
            trade.setRespDesc("重新触发");
            trade.setTradeNo(tradeNo);
            trade.setTradeDate(now);
            trade.setMoney(txAmount);
            trade.setUpdateTime(now);
            trade.setStatus(TppEnum.STATUS_UNDO.getValue());
            trade.setServFee(manageFeeTotal);
            updateList.add(trade);
            dao.updateBatch(updateList);
            //新批量交易请求
            TppService tppService = (TppService) TppUtil.tppService();
            Map<String, Object> map = new HashMap<>();
            if (TppEnum.SERVICE_TYPE_BATCH_LOAN.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.tppLoan(params);
            } else if (TppEnum.SERVICE_TYPE_BATCH_REPAY.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.tppRepayment(params);
            } else if (TppEnum.SERVICE_TYPE_BATCH_BAIL_REPAY.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.batchBailRepay(params);
            } else if (TppEnum.SERVICE_TYPE_BATCH_VOUCHER.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.batchVoucherPay(params);
            } else if (TppEnum.SERVICE_TYPE_BATCH_REPAY_BAIL.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.batchRepayBail(params);
            } else if (TppEnum.SERVICE_TYPE_BATCH_CREDIT_END.getValue().equals(trade.getServiceType())) {
                //noinspection unchecked
                map = (Map<String, Object>) tppService.batchCreditEnd(params);
            }
            String received = StringUtils.isNull(map.get("received"));
            if (!"success".equals(received)) {
                dao.updateStatusByTradeNo(tradeNo, TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(), "存管方接收失败！");
            }
        }
        return null;
    }

    @Override
    public int updateStatusByTradeNoAndType(String tradeNo, String type, String status) {
        return dao.updateStatusByTradeNoAndType(tradeNo, type, status);
    }

    @Override
    public int countUnSuccessByTradeNoAndType(String tradeNo, String type) {
        return dao.countUnSuccessByTradeNoAndType(tradeNo, type);
    }

    @Override
    public int countByTypeAndProjectId(String type, String projectId) {
        return dao.countByTypeAndProjectId(type, projectId);
    }

    @Override
    public int bailRepaySuccessHandle(String uuid, String authCode) {
        return dao.bailRepaySuccessHandle(uuid, authCode);
    }

    @Override
    public List<TppTrade> findListByTypeAndRepaymentId(String type, String repaymentId) {
        return dao.findListByTypeAndRepaymentId(type, repaymentId);
    }

    /**
     * 退回投资资金（解冻 investModel接口）
     *
     * @param tppModel
     */
    public void doInvestFail(TppTradeModel model) {
        LOGGER.info("队列处理-退回投资资金,投资记录ID：{}", model.getInvestId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("accountId", model.getToTppUserCustId());
        map.put("txAmount", model.getMoney().setScale(2, RoundingMode.HALF_UP));
        map.put("productId", model.getProjectId());
        map.put("orgOrderId", model.getPreTradeId());
        TppService tppService = (TppService) TppUtil.tppService();
        tppService.tppInvestFail(map);
    }

    /**
     * 资金冻结
     *
     * @param model
     * @author fxl
     * @date 2016年8月18日
     */
    public void doFreeze(TppTradeModel model) {
        LOGGER.info("资金冻结报文准备开始--orderNo:{}", model.getOrderNo());
        // 参数封装
        TppService tppService = (TppService) TppUtil.tppService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("projectId", model.getProjectId());
        map.put("amount", BigDecimalUtils.round(model.getMoney()));
        map.put("userCustId", model.getTppUserCustId());
        User user = userService.get(model.getUserId());
        map.put("userId", user.getUserNo());
        if (model.getFreezeType() != null) {
            map.put("freezeType", model.getFreezeType());
        } else {
            map.put("freezeType", UfxFreezeModel.FREEZE_NORMAL);
        }
        UfxFreezeModel freeze = (UfxFreezeModel) tppService.tppFreeze(map);
        // 更新冻结状态和订单号 冻结流水
        if (model.getParams() != null && UfxConstant.UFX_CODE_SUCCESS.equals(freeze.getRespCode())) {
            JsonObject paramsObj = new JsonParser().parse(model.getParams()).getAsJsonObject();
            realizeFreezeService.updateFreeze(paramsObj.get("collectionId").getAsString(), paramsObj.get("realizeId").getAsString(), paramsObj.get("type").getAsString(), freeze.getFreezeNo(), freeze.getOrderNo());
        }
    }

    /**
     * 解冻业务
     *
     * @param tppModel
     */
    public void doUnfreeze(TppTradeModel model) {
        // 参数封装
        TppService tppService = (TppService) TppUtil.tppService();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("freezeNo", model.getParams());
        map.put("userCustId", model.getTppUserCustId());
        map.put("amount", model.getMoney());
        User user = userService.get(model.getUserId());
        map.put("userId", user.getUserNo());
        map.put("projectId", model.getProjectId());
        if (model.getUnFreezeType() != null) {
            map.put("unFreezeType", model.getUnFreezeType());
        } else {
            map.put("unFreezeType", UfxUnFreezeModel.FREEZE_NORMAL);
        }
        tppService.tppUnFreeze(map);
    }

    /**
     * 投资放款
     *
     * @param tppModel
     */
    public void doInvestLoan(TppTradeModel model) {
        LOGGER.info("放款报文准备开始--orderNo:{}", model.getOrderNo());
        // 参数封装
        Map<String, Object> map = new HashMap<String, Object>();
        JsonObject paramsObj = new JsonParser().parse(model.getParams()).getAsJsonObject();
        map.put("projectId", model.getProjectId());
        map.put("sponsorer", model.getToTppUserCustId()); // 借款人
        map.put("projectAmount", paramsObj.get("projectAmount").getAsString());
        map.put("projectFee", BigDecimalUtils.round(model.getServFee())); // 借款管理费
        map.put("userCustId", model.getTppUserCustId()); //投资人在资金存管平台的帐号
        map.put("amount", BigDecimalUtils.round(model.getMoney()));
        map.put("voucher", paramsObj.get("voucher").getAsString());
        map.put("investNo", paramsObj.get("investNo").getAsString());
        map.put("investDate", paramsObj.get("investDate").getAsString());
        map.put("freezeNo", paramsObj.get("freezeNo").getAsString());
        map.put("loanType", UfxLoansModel.LOAN_TYPE_PROJECT);
        map.put("userId", paramsObj.get("userNo").getAsString());
        map.put("realName", paramsObj.get("realName").getAsString());
        map.put("orderNo", model.getOrderNo());
        TppService tppService = (TppService) TppUtil.tppService();
        tppService.tppLoan(map);
        /*UfxLoansModel ulmModel = UfxHelper.loans(map);
        boolean ret = ulmModel.validSign(ulmModel);
		if (ret) { // 验签成功
			// 重复处理判断(缓存标记)
			LOGGER.info("放款回调进入本地处理，orderNo：{}，freezeNo:{}，返回状态：{}", 
					ulmModel.getOrderNo(), ulmModel.getFreezeNo(),EncodeUtils.urlDecode(ulmModel.getRespCode()));
		   if (UfxConstant.UFX_CODE_SUCCESS.equals(ulmModel.getRespCode())) { 
				ufxLoanService.ufxLoanHandle(ulmModel);
			} 
		} else {
			LOGGER.error("放款回调验签失败，orderNo：{}，freezeNo:{}", ulmModel.getOrderNo(), ulmModel.getFreezeNo());
			throw new UfxException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_CLOSE);
		}*/
    }

    /**
     * 还款
     *
     * @param model
     * @author fxl
     * @date 2016年8月18日
     */
    public void doRepay(TppTradeModel model) {
        LOGGER.info("还款报文准备开始--orderNo:{}", model.getOrderNo());
        long startTime = System.currentTimeMillis();
        // 参数封装
        Map<String, Object> map = new HashMap<String, Object>();
        JsonObject paramsObj = new JsonParser().parse(model.getParams()).getAsJsonObject();
        map.put("orderNo", model.getOrderNo());
        map.put("repayType", paramsObj.get("repayType").getAsString());
        map.put("fundFlow", paramsObj.get("fundFlow").getAsString());
        map.put("outCustId", model.getTppUserCustId());
        map.put("inCustId", model.getToTppUserCustId());
        map.put("projectId", paramsObj.get("projectNo").getAsString());
        map.put("investNo", paramsObj.get("investNo").getAsString());
        map.put("investDate", DateUtils.formatDate(model.getTradeDate(), DateUtils.DATEFORMAT_STR_012));
        map.put("amount", BigDecimalUtils.round(model.getMoney()));
        map.put("fee", model.getServFee());
        //userType
        map.put("userId", paramsObj.get("userId").getAsString()); //商户平台用户号
        map.put("realName", paramsObj.get("realName").getAsString());
        map.put("sumIncome", paramsObj.get("sumIncome").getAsString()); //汇付平台出账必填
        map.put("sponsorer", paramsObj.get("sponsorer").getAsString()); // 借款人
        map.put("investCustId", model.getToTppUserCustId()); //投资人账户

        TppService tppService = (TppService) TppUtil.tppService();
        tppService.tppRepayment(map);
        LOGGER.info("还款报文发送结束--orderNo:{}，耗时(ms): {}", model.getOrderNo(), System.currentTimeMillis() - startTime);
    }

    public void sendTppTrade(List<TppTrade> tppList, String key) {
        if (CollectionUtils.isNotEmpty(tppList)) {
            // 2 发送三方
            final List<TppTradeModel> taskList = new ArrayList<>();
            for (TppTrade tpp : tppList) {
                taskList.add(TppTradeModel.instance(tpp));
            }
            //消息切分
            List<List<TppTradeModel>> splitList = ObjectUtils.splitList(taskList, Constant.BATCH_OPERATION_COUNT);
            for (List<TppTradeModel> list : splitList) {
                if (ConfigUtils.isOpenMq()) {
                    LOGGER.info("报文发送到队列，条数：{}.........", CollectionUtils.size(list));
                    if (MqConstant.ROUTING_KEY_TRADE_LOAN.equals(key)) {
                        RabbitUtils.tppLoan(list);
                    } else if (MqConstant.ROUTING_KEY_TRADE_REPAY.equals(key)) {
                        RabbitUtils.tppRepay(list);
                    } else if (MqConstant.ROUTING_KEY_TRADE_INVESTFAIL.equals(key)) {
                        RabbitUtils.tppInvestFail(list);
                    } else {
                        RabbitUtils.tppTrade(list);
                    }
                } else {
                    LOGGER.info("本地发送报文，条数：{}.......", CollectionUtils.size(list));
                    this.doTppTask(list);
                }
            }
        }
    }


    @Override
    public Page<TppTrade> findRecordPage(TppTrade model) {
        Page<TppTrade> page = model.getPage() == null ? new Page<TppTrade>() : model.getPage();
        page.setRows(dao.findRecord(model));
        return page;
    }

    @Override
    @Transactional
    public void doTppTask(String[] ids) {
        if (ids == null || ids.length == 0) {
            return;
        }
        List<TppTrade> list = dao.findTodoList(ids);
        if (list != null && !list.isEmpty()) {
            String tppName = ConfigUtils.getTppName();
            if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
                this.ufxTppTask(list);
            } else if (TppServiceEnum.CBHB.getName().equals(tppName)) {
                LOGGER.info("渤海银行调度任务...Start");
                this.cbhbTppTask(list);
                LOGGER.info("渤海银行调度任务...End");
            } else if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
                LOGGER.info("江西银行调度任务...Start");
                LOGGER.info("江西银行调度任务...End");
            }
        }
    }

    /**
     * 渤海银行 调度任务处理
     *
     * @param list
     * @author QianPengZhan
     * @date 2017年3月9日
     */
    private void cbhbTppTask(List<TppTrade> list) {
        TppService tppService = (TppService) TppUtil.tppService();
        for (TppTrade tppTrade : list) {
            LOGGER.info("tradeId:{}", tppTrade.getUuid());
            if (!TppEnum.STATUS_SUCEESS.eq(tppTrade.getStatus())) {
                if (TppEnum.SERVICE_TYPE_LOAN.eq(tppTrade.getServiceType())) {//放款
                    Map<String, Object> map = StringUtils.mapToStringtoMap(tppTrade.getParams());
                    LOGGER.info("调度任务:{},borrowId:{},feeAmt:{}", map.toString(), map.get("borrowId"), map.get("feeAmt"));
                    List<ProjectInvest> investList = projectInvestService.findSuccessInvest(tppTrade.getInvestProjectId());
                    String[][] detailList = new String[investList.size()][];//初始化渤海银行放款所需数组
                    if (CollectionUtils.isNotEmpty(investList)) {
                        for (int i = 0; i < investList.size(); i++) {
                            ProjectInvest invest = investList.get(i);//投资记录
                            final User investUser = userService.get(invest.getUserId());//投资人
                            final BigDecimal amount = invest.getAmount();//投资金额
                            //1-- 渤海银行所需数组录入
                            String[] detail = new String[4];
                            detail[0] = invest.getInvestOrderNo().substring(invest.getInvestOrderNo().length() - 8, invest.getInvestOrderNo().length());
                            detail[1] = investUser.getTppUserCustId();
                            detail[2] = amount.toString();
                            detail[3] = invest.getFreezeNo();
                            detailList[i] = detail;
                        }
                    }
                    map.put("detailList", detailList);
                    CbhbFileReleaseModel model = (CbhbFileReleaseModel) tppService.tppLoan(map);
                    if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {//重新发送请求会改变流水号 重新存入商户流水号
                        tppTrade.setTradeNo(tppTrade.getOrderNo());//原有订单号 放入交易号
                        tppTrade.setOrderNo(model.getMerBillNo());//重新的流水号放入订单号
                        dao.update(tppTrade);
                        LOGGER.info("重新调度请求成功");
                    } else {
                        LOGGER.info("重新调度请求失败");
                    }
                } else if (TppEnum.SERVICE_TYPE_REPAY.eq(tppTrade.getServiceType())) {
                    Map<String, Object> map = JSON.parseObject(tppTrade.getParams());
                    final String repaymentId = map.get("repaymentId").toString();// 还款信息id
                    String[][] detailList = createRepayDetail(repaymentId);
                    map.put("detailList", detailList);

                    CbhbFileRepaymentModel model = (CbhbFileRepaymentModel) tppService.tppRepayment(map);
                    if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {//重新发送请求会改变流水号 重新存入商户流水号
                        tppTrade.setTradeNo(tppTrade.getOrderNo());//原有订单号 放入交易号
                        tppTrade.setOrderNo(model.getMerBillNo());//重新的流水号放入订单号
                        dao.update(tppTrade);
                        LOGGER.info("重新调度请求成功");
                    } else {
                        LOGGER.info("重新调度请求失败");
                    }
                } else if (TppEnum.SERVICE_TYPE_PROJECT_CANCEL.eq(tppTrade.getServiceType())) {//项目的成立审核失败不成立
                    Map<String, Object> map = StringUtils.mapToStringtoMap(tppTrade.getParams());
                    LOGGER.info("调度任务:{}", map.toString());
                    List<ProjectInvest> investList = projectInvestService.findSuccessInvest(tppTrade.getInvestProjectId());
                    String[][] detailList = new String[investList.size()][];//初始化渤海银行放款所需数组
                    if (CollectionUtils.isNotEmpty(investList)) {
                        for (int i = 0; i < investList.size(); i++) {
                            ProjectInvest invest = investList.get(i);//投资记录
                            final User investUser = userService.get(invest.getUserId());//投资人
                            //1-- 渤海银行所需数组录入撤销的明细
                            String[] detail = new String[5];
                            detail[0] = invest.getInvestOrderNo().substring(invest.getInvestOrderNo().length() - 8, invest.getInvestOrderNo().length());
                            detail[1] = invest.getInvestNo();
                            detail[2] = investUser.getTppUserCustId();
                            detail[3] = invest.getAmount().toString();
                            detail[4] = invest.getFreezeNo();
                            detailList[i] = detail;
                        }
                    }
                    map.put("detailList", detailList);
                    CbhbBatInvestCancleModel model = (CbhbBatInvestCancleModel) tppService.tppBatInvestCancle(map);
                    if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {//重新发送请求会改变流水号 重新存入商户流水号
                        tppTrade.setTradeNo(tppTrade.getOrderNo());//原有订单号 放入交易号
                        tppTrade.setOrderNo(model.getMerBillNo());//重新的流水号放入订单号
                        dao.update(tppTrade);
                        LOGGER.info("重新调度请求成功");
                    } else {
                        LOGGER.info("重新调度请求失败");
                    }
                } else if (TppEnum.TPP_TYPE_EXPERCAP.eq(tppTrade.getServiceType())) {
                    Map<String, Object> map = JSON.parseObject(tppTrade.getParams());
                    final String repaymentId = map.get("repaymentId").toString();// 还款信息id
                    String[][] detailList = createExpercapDetail(repaymentId);
                    map.put("detailList", detailList);
                    CbhbExperCapModel model = (CbhbExperCapModel) tppService.tppExperCap(map);
                    if (CbhbConstant.CBHB_CODE_SUCCESS.equals(model.getRespCode())) {//重新发送请求会改变流水号 重新存入商户流水号
                        tppTrade.setTradeNo(tppTrade.getOrderNo());//原有订单号 放入交易号
                        tppTrade.setOrderNo(model.getMerBillNo());//重新的流水号放入订单号
                        dao.update(tppTrade);
                        LOGGER.info("重新调度请求成功");
                    } else {
                        LOGGER.info("重新调度请求失败");
                    }
                }
            }
        }
    }

    /**
     * 生成加息利息列表信息
     *
     * @param repaymentId
     * @return
     */
    private String[][] createExpercapDetail(String repaymentId) {
        final ProjectRepayment repay = projectRepaymentService.get(repaymentId);
        final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdAndPeriod(repay.getProjectId(),
                repay.getPeriod(), repay.getInvestId());
        String[][] detailList = new String[collectionList.size()][];
        int i = 1;
        for (ProjectCollection collection : collectionList) {
            if (BigDecimalUtils.validAmount(collection.getRaiseInterest())) {
                final User user = userService.get(collection.getUserId());
                // 封装渤海银行发放加息参数
                String[] arrayExperCap = new String[5];
                arrayExperCap[0] = String.valueOf(i);
                arrayExperCap[1] = user.getTppUserCustId();
                arrayExperCap[2] = collection.getRaiseInterest().toString();
                arrayExperCap[3] = CbhbConstant.STRING_ZERO;
                arrayExperCap[4] = CbhbConstant.STRING_ZERO;
                detailList[i - 1] = arrayExperCap;
                i++;
            }
        }
        return detailList;
    }

    /**
     * 生成还款信息详情
     *
     * @param repaymentId
     * @return
     */
    private String[][] createRepayDetail(String repaymentId) {
        final ProjectRepayment repay = projectRepaymentService.get(repaymentId);
        final Project project = projectService.get(repay.getProjectId());
        final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdAndPeriod(repay.getProjectId(),
                repay.getPeriod(), repay.getInvestId());
        String[][] detailList = new String[collectionList.size()][];
        int i = 1;
        for (ProjectCollection collection : collectionList) {
            // 本金
            final BigDecimal capital = collection.getCapital();
            // 利息
            final BigDecimal interest = collection.getInterest();
            // 逾期罚息
            final BigDecimal lateInterest = collection.getLateInterest();
            // 利息管理费
            final BigDecimal interestFee = BigDecimalUtils.div(BigDecimalUtils.mul(interest, project.getInterestManageRate()),
                    BigDecimal.valueOf(100), 2);
            // 封装渤海银行还款参数
            User user = userService.get(collection.getUserId());
            String[] array = new String[5];
            array[0] = String.valueOf(i);
            array[1] = user.getTppUserCustId();
            array[2] = String.valueOf(capital);
            array[3] = String.valueOf(BigDecimalUtils.add(interest, lateInterest));
            array[4] = String.valueOf(interestFee);
            detailList[i - 1] = array;
            i++;
        }
        return detailList;
    }

    /**
     * ufx汇付  调度任务处理
     *
     * @param list
     * @author QianPengZhan
     * @date 2017年3月9日
     */
    private void ufxTppTask(List<TppTrade> list) {
        //失败重做任务ID
        List<String> redoList = new ArrayList<String>();
        //失败新建任务
        List<TppTrade> newTaskList = new ArrayList<TppTrade>();
        Date now = DateUtils.getNow();
        List<TppTradeModel> doUnList = new ArrayList<TppTradeModel>();
        //当前时间
        Long nowTime = (new Date()).getTime();
        for (TppTrade tppTrade : list) {
            boolean redo = true;
            if (TppEnum.STATUS_UNDO.eq(tppTrade.getStatus())) {//补单
                //三方收到报文，处理中 或 处理完成
                redo = this.doUndoTask(tppTrade);
                if (!TppEnum.STATUS_UNDO.eq(tppTrade.getStatus())) {
                    doUnList.add(TppTradeModel.instance(tppTrade));
                }
            }
            //失败、三方未收到情况，需重做，新生成orderNo
            if (redo) {
                final String preTradeId = tppTrade.getUuid();

                /**
                 * 调度操作频度检查
                 */
                long createTime = tppTrade.getCreateTime().getTime();
                if (nowTime - createTime < TPPTRADE_REDO_INTERVAL_TIME) {
                    throw new BussinessException(ResourceUtils.get(ResourceConstant.TPPTRADE_DISPATCHTASK_TIMEINTERVAL_TOOSHORT));
                }

                redoList.add(preTradeId);
                tppTrade.setPreTradeId(preTradeId);
                tppTrade.setUuid(null);
                tppTrade.setOrderNo(OrderNoUtils.getSerialNumber());
                tppTrade.setStatus(TppEnum.STATUS_UNDO.getValue());
                tppTrade.setRespDesc(null);
                tppTrade.setNotifyTime(null); //异步回调使用
                tppTrade.setCreateTime(now);
                tppTrade.setOrderDate(now);
                newTaskList.add(tppTrade);
            }
        }
        if (CollectionUtils.isNotEmpty(doUnList)) {
            //三方已收，记录结果
            this.updateCallback(doUnList);
        }
        if (CollectionUtils.isNotEmpty(redoList)) {
            //标记失败任务
            dao.updateRedoFlagBatch(redoList);
            //保存新建任务
            this.insertBatch(newTaskList);
        }
        //发送重做任务到三方
        this.sendTppTrade(list, MqConstant.ROUTING_KEY_TRADE);
    }

    /**
     * 对未处理的订单执行补单操作
     *
     * @param trade
     */
    private boolean doUndoTask(TppTrade trade) {
        boolean redo = false;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loanNo", trade.getOrderNo());
        map.put("loanDate", DateUtils.dateStr7(trade.getTradeDate() == null ? trade.getOrderDate() : trade.getTradeDate()));
        map.put("isRepeatSend", UfxAdditionalOrderModel.REPEAT_SEND_YES);
        TppService tppService = (TppService) TppUtil.tppService();
        final UfxAdditionalOrderModel model = (UfxAdditionalOrderModel) tppService.tppAdditionOrder(map);
        if (model != null) {
            if (UfxConstant.UFX_CODE_DOING.equals(model.getRespCode())) {
                trade.setStatus(TppEnum.STATUS_UNDO.getValue());
            } else if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
                trade.setStatus(TppEnum.STATUS_SUCEESS.getValue());
            } else {
                redo = true;
                trade.setStatus(TppEnum.STATUS_FAIL.getValue());
            }
            trade.setRespDesc(model.getRespDesc());
        } else {
            redo = true;
        }
        return redo;
    }

    @Override
    public TppTrade findByOrderNo(String orderNo) {
        return dao.findByOrderNo(orderNo);
    }

    @Override
    public void updateStatus(String uuid, String status, String preStatus) {
        int result = dao.updateStatus(uuid, status, preStatus);
        if (result == Constant.INT_ZERO) {
            String msg = "三方交易记录状态更新失败 [uuid=" + uuid + ", status=" + status + ", preStatus=" + preStatus + "]";
            LOGGER.error(msg);
            throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
        }
    }

    @Override
    public int callbackHandle(String uuid, String status, String respDesc) {
        LOGGER.info("-- uuid : {}, status: {} , respDesc: {}", uuid, status, respDesc);
        return dao.callbackHandle(uuid, status, respDesc);
    }

	@Override
	public int countByTypeAndProjectIdAndStatus(String type, String projectId) {
		return dao.countByTypeAndProjectIdAndStatus(type, projectId);
	}

}