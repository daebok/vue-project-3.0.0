package com.rd.ifaes.core.tpp.model.jx;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.ReflectionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.SpringHttpClient;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.JxbankException;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.tpp.util.RSAHelper;
import com.rd.ifaes.core.tpp.util.RSAKeyUtil;

public class JxBaseModel {
    protected static final Logger LOGGER = LoggerFactory.getLogger(JxBaseModel.class);

    /**
     * 请求参数 返回参数
     */
    private String version; //版本号	A	2	M	目前为10
    private String txCode; //交易代码	A	50	M	accountOpen
    private String instCode; //机构代码	A	8	M
    private String bankCode; //银行代码	A	8	M
    private String txDate; //交易日期	A	8	M	YYYYMMDD
    private String txTime; //交易时间	A	6	M	hhmmss
    private String seqNo; //交易流水号	N	6	M	定长6位
    private String channel; //交易渠道	A	6	M	000001手机APP000002网页000003微信000004柜面

    private String acqRes; //请求方保留	A	200	C

    /**
     * 返回参数
     */
    private String retCode; //响应代码	A	8	M
    private String retMsg; //响应描述	A	60	M

    private String sign;

    protected String reqData;

    protected String respData;

    protected Map<String, Object> mapReqData;
    protected Map<String, Object> mapRespData;

    /**
     * 测试：https://test.credit2go.cn
     * UAT：https://access.credit2go.cn
     * 生产：https://finance.credit2go.cn
     * <p>
     * 联机：https://xxxx.credit2go.cn/escrow/p2p/online
     */

    private String baseUrl = ConfigUtils.getValue(ConfigConstant.JXBANK_BASE_URL);

    /**
     * 提交地址
     */
    private String summitUrl;

    private static final String TAG_SIGNINFO = "sign";

    /**
     * 直连接口请求参数
     */
    private String[] directRequestParamNames = new String[]{"version", "txCode", "instCode", "bankCode", "txDate", "txTime", "seqNo", "channel", "acqRes"};
    /**
     * 直连接口返回参数
     */
    private String[] directResponseParamNames = new String[]{"version", "txCode", "instCode", "bankCode", "txDate", "txTime", "seqNo", "channel", "retCode", "retMsg", "acqRes"};


    //版本号
    private static final String VERSION = "10";
    //银行代码
    protected static final String BANKCODE = ConfigUtils.getValue(ConfigConstant.JXBANK_BANK_CODE);
    //机构代码   （说明：平台需要根据即信端给定实际参数进行调整）
    protected static final String INSTCODE = ConfigUtils.getValue(ConfigConstant.JXBANK_INST_CODE);
    //交易渠道
    private static final String COINSTCHANNEL = "000002";

    protected static String keys = SpringContextHolder.class.getClassLoader().getResource("/").getPath() + "jxbank/" + ConfigUtils.getValue(ConfigConstant.JXBANK_PRIVATE_PASS);//私钥文件
    protected static String pass = ConfigUtils.getValue(ConfigConstant.JXBANK_PASS);; //私钥密码
    protected static String crt = SpringContextHolder.class.getClassLoader().getResource("/").getPath() + "jxbank/" + ConfigUtils.getValue(ConfigConstant.JXBANK_JIXIN_PUBLIC_PASS);; //即信公钥


    public JxBaseModel() {
        super();
        Date now = DateUtils.getNow();
        version = VERSION;
        instCode = INSTCODE;
        bankCode = BANKCODE;
        txDate = DateUtils.dateStr7(now);
        txTime = DateUtils.dateStr9(now);
        seqNo = OrderNoUtils.getRandomStr(6);
        channel = COINSTCHANNEL;
        setSummitUrl(getBaseUrl() + "/p2p/online");
    }

    /**
     * 商户请求时的签名
     */
    public void sign() {
        try {
        	LOGGER.info("txDate,txTime,seqNo整合的报文头流水号:{}", this.getOrderNo());
            mapReqData = mapReqData();
            LOGGER.info("签名参数mapReqData：{}\r\n", JSON.toJSON(mapReqData).toString().replace(",", ",\r\n"));
            reqData = mergeMap(mapReqData);
            LOGGER.info("签名参数reqData:{},orderNo:{}", reqData, getOrderNo());

            RSAHelper signer = null;

            LOGGER.info("(P2P-->即信端)获取签名私钥:{},{}", keys, pass);
            RSAKeyUtil rsaKey;
            try {
                rsaKey = new RSAKeyUtil(new File(keys), pass);
                signer = new RSAHelper(rsaKey.getPrivateKey());
            } catch (IOException e) {
                LOGGER.error("IOException！{}", e.getMessage());
            }

            sign = signer.sign(reqData);
            LOGGER.info("(P2P-->即信端)签名:{}", sign);
            mapReqData.put("sign", sign);
            setSign(sign);
        } catch (GeneralSecurityException e) {
            LOGGER.error("签名失败！", e);
        }
    }

    /**
     * 验签
     */
    public boolean verify() {
        boolean rst = false;
        try {
            LOGGER.info("验签参数respData:{}", respData);
            LOGGER.info("签名sign:{}", sign);
            sign = sign.replaceAll("[\\t\\n\\r]", "");

            RSAKeyUtil ru = new RSAKeyUtil(new File(crt));
            RSAHelper signHelper = new RSAHelper(ru.getPublicKey());
            rst = signHelper.verify(respData, sign);

        } catch (Exception e) {
            LOGGER.error("验签失败！", e);
        }
        return rst;
    }

    /**
     * 获取reqData参数Map
     *
     * @return
     */
    public Map<String, Object> mapReqData() {
        final Map<String, Object> param = new HashMap<>();
        final String[] paramNames = getParamNames(directRequestParamNames, getDirectRequestParamNames());
        if (paramNames.length > 0) {
            for (String paramName : paramNames) {
                if (TAG_SIGNINFO.equals(paramName)) {
                    continue;
                }
                final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, paramName);
                if (StringUtils.isNotBlank(result)) {
                    param.put(paramName, result);
                }
            }
        }
        return param;
    }


    public Map<String, Object> mapRespData(String[] strs) {
        final Map<String, Object> param = new HashMap<>();
        final String[] paramNames = getParamNames(directResponseParamNames, strs);
        if (paramNames.length > 0) {
            for (String paramName : paramNames) {
                if (TAG_SIGNINFO.equals(paramName)) {
                    continue;
                }
                final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, paramName);
                if (StringUtils.isNotBlank(result)) {
                    param.put(paramName, result);
                }
            }
        }
        return param;
    }

    public Map<String, Object> mapRespData() {
        return mapRespData(getDirectResponseParamNames());
    }

    /**
     * 获取Map的待签名字符串
     *
     * @param map
     * @return
     */
    public static String mergeMap(Map<String, Object> map) {
        //字典序排序后生成待签名字符串
        Map<String, Object> reqMap = new TreeMap<String, Object>(map);

        StringBuffer buff = new StringBuffer();

        Iterator<Map.Entry<String, Object>> iter = reqMap.entrySet().iterator();
        Map.Entry<String, Object> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (!TAG_SIGNINFO.equals(entry.getKey())) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                    buff.append("");
                } else {
                    buff.append(String.valueOf(entry.getValue()));
                }
            }
        }

        String requestMerged = buff.toString();
        return requestMerged;
    }

    public boolean responseVerify() {
        respData = mergeMap(mapRespData());
        return this.verify();
    }

    public boolean responseVerify(String[] strs) {
        respData = mergeMap(mapRespData(strs));
        return this.verify();
    }

    /**
     * 直连请求
     */
    public boolean directRequest() {
        mapRespData = SpringHttpClient.post(getSummitUrl(), mapReqData);
        respData = mergeMap(mapRespData);
        sign = (String) mapRespData.get("sign");
        boolean verifyResult = this.verify();

        if (!verifyResult) {
            LOGGER.error("(即信端------->P2P)验证签名失败...");
            throw new JxbankException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_JSON);
        } else {
            LOGGER.info("(即信端------->P2P)验证签名成功");
        }
        retCode = (String) mapRespData.get("retCode");
        retMsg = (String) mapRespData.get("retMsg");
        LOGGER.info("retCode:{},retMsg:{}", retCode, retMsg);
        validResult(retCode, retMsg);
        return true;
    }

    /**
     * 批量的接口 不需要验签
     */
    public void directRequestBatch() {
        mapRespData = SpringHttpClient.post(getSummitUrl(), mapReqData);
        LOGGER.info("批量请求返回参数：{}", JSON.toJSONString(mapRespData));
    }

    /**
     * 验证返回是否成功，默认报错，不需要报错的则子类重写
     *
     * @param retCode
     * @param retMsg
     * @author jxx
     * @date 2017年8月4日
     */
    public void validResult(String retCode, String retMsg) {
        if (!JxConfig.SUCCESS.equals(retCode)) {
            LOGGER.error("(即信端------->P2P)返回失败...{}", retMsg);
            if (StringUtils.isBlank(retMsg)) {
                throw new JxbankException("存管接口返回错误，请联系管理员", BussinessException.TYPE_JSON);
            }
            throw new JxbankException(retMsg, BussinessException.TYPE_JSON);
        }
    }

    public String[] getParamNames(String[] str1, String[] str2) {
        int length1 = str1.length;
        int length2 = str2.length;
        String[] tmp = new String[length1 + length2];
        System.arraycopy(str1, 0, tmp, 0, length1);
        System.arraycopy(str2, 0, tmp, length1, length2);
        return tmp;
    }

    public String[] getDirectRequestParamNames() {
        return directRequestParamNames;
    }

    public void setDirectRequestParamNames(String[] directRequestParamNames) {
        this.directRequestParamNames = directRequestParamNames;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        this.txCode = txCode;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getTxDate() {
        return txDate;
    }

    public void setTxDate(String txDate) {
        this.txDate = txDate;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String[] getDirectResponseParamNames() {
        return directResponseParamNames;
    }

    public void setDirectResponseParamNames(String[] directResponseParamNames) {
        this.directResponseParamNames = directResponseParamNames;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getAcqRes() {
        return acqRes;
    }

    public void setAcqRes(String acqRes) {
        this.acqRes = acqRes;
    }

    public Map<String, Object> getMapRespData() {
        return mapRespData;
    }

    public void setMapRespData(Map<String, Object> mapRespData) {
        this.mapRespData = mapRespData;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getRespData() {
        return respData;
    }

    public void setRespData(String respData) {
        this.respData = respData;
    }

    public Map<String, Object> getMapReqData() {
        return mapReqData;
    }

    public void setMapReqData(Map<String, Object> mapReqData) {
        this.mapReqData = mapReqData;
    }

    public String getOrderNo() {
        String orderNo = new StringBuffer(StringUtils.isNull(getTxDate()))
                .append(StringUtils.isNull(getTxTime())).append(StringUtils.isNull(getSeqNo())).toString();
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSummitUrl() {
        return summitUrl;
    }

    public void setSummitUrl(String summitUrl) {
        this.summitUrl = summitUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
