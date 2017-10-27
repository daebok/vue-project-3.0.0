package com.rd.ifaes.core.tpp.model.jx.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.common.util.MD5Utils;
import com.rd.ifaes.common.util.SpringHttpClient;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.model.jx.JxBaseModel;
import com.rd.ifaes.core.tpp.util.JxConfig;
import com.rd.ifaes.core.tpp.util.RSAHelper;
import com.rd.ifaes.core.tpp.util.RSAKeyUtil;

public class JxFileBaseModel extends JxBaseModel {
	protected static final Logger LOGGER = LoggerFactory.getLogger(JxFileBaseModel.class);
	
    //银行代码
    protected static final String BANKNUM = ConfigUtils.getValue(ConfigConstant.JXBANK_BANK_NUM);
    //机构代码   （说明：平台需要根据即信端给定实际参数进行调整）
    protected static final String PRODUCT = ConfigUtils.getValue(ConfigConstant.JXBANK_PRODUCT);

	private String instCode;// 机构代码
	private String bankCode;// 银行号
	private String txDate;// 日期
	private String fileName;// 文件名
	private String SIGN;// 签名

	private String returnCode;// 应答码
	private String returnMSG;// 应答描述
	private String MD5;// 文件内容的MD5值
	private String FILE;// 文件内容（GBK编码）不参与SIGN签名计算

	
	public JxFileBaseModel() {
		super();
        instCode = INSTCODE;
        bankCode = BANKCODE;
        setSummitUrl(getBaseUrl() + "/file/download");
        mapReqData = new HashMap<>();
        mapReqData.put("instCode", this.instCode);
        mapReqData.put("bankCode", this.bankCode);
        mapReqData.put("txDate", this.txDate);
	}
	
	/**
     * 商户请求时的签名
     */
    public void sign() {
        try {
            LOGGER.info("签名参数mapReqData：{}\r\n", JSON.toJSON(mapReqData).toString().replace(",", ",\r\n"));
            reqData = mergeMap(mapReqData);
            LOGGER.info("签名参数reqData:{}", reqData);

            RSAHelper signer = null;

            LOGGER.info("(P2P-->即信端)获取签名私钥:{},{}", keys, pass);
            RSAKeyUtil rsaKey;
            try {
                rsaKey = new RSAKeyUtil(new File(keys), pass);
                signer = new RSAHelper(rsaKey.getPrivateKey());
            } catch (IOException e) {
                LOGGER.error("IOException！{}", e.getMessage());
            }

            SIGN = signer.sign(reqData);
            LOGGER.info("(P2P-->即信端)签名:{}", SIGN);
            mapReqData.put("SIGN", SIGN);
            setSIGN(SIGN);
        } catch (GeneralSecurityException e) {
            LOGGER.error("签名失败！", e);
        }
    }
    
    /**
     * 直连请求
     */
    public boolean directRequest() {
        mapRespData = SpringHttpClient.download(getSummitUrl(), mapReqData);
        SIGN = (String) mapRespData.get("SIGN");
        mapRespData.remove("SIGN");
        respData = mergeMap(mapRespData);
        boolean verifyResult = this.verify();

        if (!verifyResult) {
            LOGGER.error("(即信端------->P2P)验证签名失败...");
//            throw new JxbankException(ResourceUtils.get(ResourceConstant.TPP_NOTIFY_SIGN_FAIL), BussinessException.TYPE_JSON);
            return false;
        } else {
            LOGGER.info("(即信端------->P2P)验证签名成功");
        }
        returnCode = (String) mapRespData.get("returnCode");
        returnMSG = (String) mapRespData.get("returnMSG");
        LOGGER.info("下载返回---------------returnCode:{},returnMSG:{}", returnCode, returnMSG);
        if (!JxConfig.DOWNLOAD_SUCCESS.equals(returnCode)) {
            LOGGER.error("(即信端------->P2P)返回失败...{}", returnMSG);
//            throw new JxbankException(retMsg, BussinessException.TYPE_JSON);
            return false;
        } else {
        	download();
        }
        return true;
    }
    
    /**
     * 验签
     */
    public boolean verify() {
        boolean rst = false;
        try {
            LOGGER.info("验签参数respData:{}", respData);
            LOGGER.info("签名sign:{}", SIGN);
            SIGN = SIGN.replaceAll("[\\t\\n\\r]", "");

            RSAKeyUtil ru = new RSAKeyUtil(new File(crt));
            RSAHelper signHelper = new RSAHelper(ru.getPublicKey());
            rst = signHelper.verify(respData, SIGN);

        } catch (Exception e) {
            LOGGER.error("验签失败！", e);
        }
        return rst;
    }
    
    public boolean download() {
        long start = System.currentTimeMillis();
    	 // 下载成功，保存下载的文件到本地
        String fileContent = (String) mapRespData.get("FILE");
        String md5 = (String) mapRespData.get("MD5");
        if (MD5Utils.encode(fileContent).equalsIgnoreCase(md5)) {
            byte[] _file;
			try {
				_file = fileContent.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("下载文件转码错误, {}", e.getMessage());
				return false;
			}
            File newFile = new File("D:/fileForP2P/in");  //文件在本地的存放地址
            if (!newFile.exists() && !newFile.isDirectory())// 判断文件夹是否存在，不存在就创建
            {
                newFile.mkdirs();
            }
            FileOutputStream out;
			try {
				out = new FileOutputStream(newFile + File.separator
				        + this.getFileName());
	            out.write(_file);
	            out.close();
			} catch (FileNotFoundException e) {
				LOGGER.error("下载文件FileNotFoundException错误, {}", e.getMessage());
				return false;
			} catch (IOException e) {
				LOGGER.error("下载文件IOException错误, {}", e.getMessage());
				return false;
			}
            long cost = System.currentTimeMillis() - start;
            LOGGER.info("文件下载成功 \n耗时：{}ms", format(cost));
        } else {
        	LOGGER.error("文件MD5验证失败, MD5Utils.encode(fileContent):{}, md5:{} , fileContent:{}", MD5Utils.encode(fileContent), md5, fileContent);
			return false;
        }
    	return true;
    }
    
    public static String format(long ms) {// 将毫秒数换算成x天x时x分x秒x毫秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        // String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strHour + ":" + strMinute + ":" + strSecond + " "
                + strMilliSecond;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSIGN() {
		return SIGN;
	}

	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMSG() {
		return returnMSG;
	}

	public void setReturnMSG(String returnMSG) {
		this.returnMSG = returnMSG;
	}

	public String getMD5() {
		return MD5;
	}

	public void setMD5(String mD5) {
		MD5 = mD5;
	}

	public String getFILE() {
		return FILE;
	}

	public void setFILE(String fILE) {
		FILE = fILE;
	}

	/**
	 * 右侧补空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String checkLength(String str, int length) {
		StringBuffer sb = new StringBuffer(StringUtils.isNull(str));
		if (sb.length() < length) {
			int n = length - sb.length();
			for (int i = 0; i < n; i++) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * 右侧补空格,对于中文的特殊处理
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String checkCharLength(String str, int length) {
		StringBuffer sb = new StringBuffer(StringUtils.isNull(str));
		try {
			byte[] hash = str.getBytes("GBK");
			if (hash.length < length) {
				int n = length - hash.length;
				for (int i = 0; i < n; i++) {
					sb.append(" ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 * 左侧补0
	 * 
	 * @param str
	 * 
	 * @param length
	 * 
	 * @return
	 */
	public static String checkLengthByLeft(String str, int length) {
		StringBuffer sb = new StringBuffer(StringUtils.isNull(str));
		if (sb.length() < length) {
			int n = length - sb.length();
			for (int i = 0; i < n; i++) {
				sb.insert(0, "0");
			}
		}
		return sb.toString();
	}

	/**
	 * 按字节长度截取指定长度字符串
	 * 
	 * @param str 待截取字符串
	 * @param index 开始位置
	 * @param length 截取长度
	 * @return
	 */
	public static String subFileContent(String str, int index, int length) {
		String resultStr = null;
		try {
			byte[] byteArr = str.getBytes("GBK");
			resultStr = new String(byteArr, index, length, "GBK");
		} catch (UnsupportedEncodingException e) {
			LOGGER.info("截取字符串异常 ：{}", e.getMessage());
		}
		return StringUtils.isNull(resultStr);
	}

	
}
