package com.rd.ifaes.core.core.sms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.sys.service.ConfigService;


/** 
* 阿里大于
* @ClassName: AldySmsPortalImpl 
* @Description 发送 短信
* @author zhangxj
* @date 2017-09-22
*  
*/ 
public class AldySmsPortalImpl implements SmsPortal {
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AldySmsPortalImpl.class);	
	
	public static final String SUCCESS_CODE = "OK";
	
	//产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	
    private static String accessKeyId;
    private static String accessKeySecret;
    //产品域名,开发者无需替换
	private static String sendurl;
	private static String signName;//短信签名

	
	/**
	 *  创建一个新的实例 SMSSender,助通科技提供的用户名与地址
	 *  accessKeyId	阿里大于ak（必填）
	 * 	accessKeySecret	阿里大于ak（必填）
	 * 	mobile	手机号
	 * 	content	选填，json格式，在模板中替换变量
	 * */
	public AldySmsPortalImpl() {
		final ConfigService configService = SpringContextHolder.getBean("configService");
	    sendurl = configService.findByCode("sms_notice").getConfigValue(); //阿里大于通提供的发送地址 
	    accessKeyId =  configService.findByCode("sms_accessKeyId").getConfigValue();
	    accessKeySecret = configService.findByCode("sms_accessKeySecret").getConfigValue();
	    signName = configService.findByCode("sms_sign_name").getConfigValue();//短信签名
	}

	
	/**
	 * 获得短信通道名称
	 */
	@Override
	public String getSPName() {
		return "阿里大于";
	}

	/**
	 * 发送短信
	 */
	@Override
	public String send(final String phone, final String content) {
		return null;
	}

	@Override
	public String send(final String phone, final String content, final String templateCode) {
		return sendSms(phone, content, templateCode);
	}

	
	/**
	 * 发送短信
	 * @param phone
	 * @param content
	 * @param templateCode
	 * @return
	 */
	private String sendSms(final String phone, final String content, final String templateCode) {
		//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, sendurl);
		} catch (ClientException e1) {
			LOGGER.error("短信初始化acsClient报错：{}", e1.getLocalizedMessage());
		}
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(templateCode);
         if(StringUtils.isNotBlank(content)){
        	request.setTemplateParam(content);
        }
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"customer\":\"蒋小星同学\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        SendSmsResponse sendSmsResponse = null;
        String code = null;
        try {
        	sendSmsResponse = acsClient.getAcsResponse(request);
		} catch (Exception e) {
			LOGGER.error("短信发送报错：{}", e.getLocalizedMessage());
		}
        if(sendSmsResponse != null){
        	code = sendSmsResponse.getCode();
        	if(!SUCCESS_CODE.equals(code)){
        		//短信接口发送失败，打印日志
        		LOGGER.error("短信发送失败：Code：{}，Message：{}，RequestId：{}，BizId：{}", code, sendSmsResponse.getMessage(), 
        				sendSmsResponse.getRequestId(), sendSmsResponse.getBizId());
        	}
        }
		return code;
	}
	

	@Override
	public Map<String, Integer> getUseInfo() {
		return null;
	}


	@Override
	public String getReturnMessage(final String code) {
		String result = "";
		if(StringUtils.isBlank(code)){
			return "返回码异常，不能为空！";
		}
		switch (code) {
		case SUCCESS_CODE:
			result = "发送短信成功";
			break;
		case "isp.RAM_PERMISSION_DENY":
			result = "RAM权限DENY";//当提示RAM权限不足时，就需要给当前使用的AK对应子账号进行授权：AliyunDysmsFullAccess（权限名称）。具体权限授权详见：https://help.aliyun.com/document_detail/55764.html?spm=5176.product44282.6.548.bKZJL2
			break;
		case "isv.OUT_OF_SERVICE":
			result = "业务停机";//请先查看账户余额，若余额大于零，则请通过创建工单联系工程师处理
			break;
		case "isv.PRODUCT_UN_SUBSCRIPT":
			result = "未开通云通信产品的阿里云客户";//未开通云通信产品的阿里云客户（该AK所属的账号尚未开通云通信的服务，包括短信、语音、流量等服务）注：阿里云短信服务包含：1、消息服务 2、云通信短信服务 3、云市场短信接口，账号和短信接口不可混用。当出现此类提示报错需要检查当前AK是否已经开通阿里云云通信短信服务，如已开通消息服务，则参照消息服务文档调用接口。
			break;
		case "isv.PRODUCT_UNSUBSCRIBE":
			result = "产品未开通";//产品未订购（该AK所属的账号尚未开通当前接口的产品，如仅开通了短信服务的用户调用语音接口。），检查AK对应账号是否已开通调用接口对应的服务。短信服务开通链接：https://www.aliyun.com/product/sms
			break;
		case "isv.ACCOUNT_NOT_EXISTS":
			result = "账户不存在";//请确认使用的账号是否与申请的账号一致
			break;
		case "isv.ACCOUNT_ABNORMAL":
			result = "账户异常";//请确认使用的账号是否与申请的账号一致
			break;
		case "isv.SMS_TEMPLATE_ILLEGAL":
			result = "短信模板不合法";//TemplateCode参数请传入审核通过的模板ID，模板见：见：https://dysms.console.aliyun.com/dysms.htm#/template
			break;
		case "isv.SMS_SIGNATURE_ILLEGAL":
			result = "短信签名不合法";//SignName请传入审核通过的签名内容，签名见：https://dysms.console.aliyun.com/dysms.htm#/sign
			break;
		case "isv.INVALID_PARAMETERS":
			result = "参数异常";//对照文档，检查参数格式。例：短信查询接口SendDate日期格式yyyyMMdd，错误：2017-01-01正确：20170101
			break;
		case "isp.SYSTEM_ERROR":
			result = "请重试接口调用，如仍存在此情况请创建工单反馈工程师查看!";
			break;
		case "isv.MOBILE_NUMBER_ILLEGAL":
			result = "非法手机号";//PhoneNumbers参数请传入11位国内号段的手机号码
			break;
		case "isv.MOBILE_COUNT_OVER_LIMIT":
			result = "手机号码数量超过限制";//短信接收号码,支持以英文逗号分隔的形式进行批量调用，批量上限为1000个手机号码，PhoneNumbers参数单次调用不传入过多接收号码
			break;
		case "isv.TEMPLATE_MISSING_PARAMETERS":
			result = "模板缺少变量";//TemplateParam中需要以json格式字符串给使用的模板中出现的所有变量进行赋值。例：模板为：您好${name}，验证码${code} TemplateParam={“name”:”Tom”,”code”:”123”}
			break;
		case "isv.BUSINESS_LIMIT_CONTROL":
			result = "业务限流";//将短信发送频率限制在正常的业务流控范围内，默认流控：短信验证码 ：使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时 ，累计10条/天。
			break;
		case "isv.INVALID_JSON_PARAM":
			result = "JSON参数不合法，只接受字符串值";//TemplateParam入参以Json格式字符串形式传入。例：正确{“code”:”123”}错误{“code”:123}
			break;
		case "isv.BLACK_KEY_CONTROL_LIMIT":
			result = "黑名单管控";//黑名单管控是指变量内容含有限制发送的内容，变量不支持透传url，同时检查通过变量是否透传了一些敏感信息触发关键字
			break;
		case "isv.PARAM_LENGTH_LIMIT":
			result = "参数超出长度限制";//仅对个人用户的限制，单个变量长度限制在15字符内。企业用户无限制
			break;
		case "isv.PARAM_NOT_SUPPORT_URL":
			result = "不支持URL";//变量不支持透传url，同时检查通过变量是否透传了一些敏感信息触发关键字
			break;
		case "isv.AMOUNT_NOT_ENOUGH":
			result = "账户余额不足";//转入金额不足以发送当前信息，确保余额足够发送当前短信
			break;
		case "isv.TEMPLATE_PARAMS_ILLEGAL":
			result = "模板变量里包含非法关键字";//变量不支持透传url，同时检查通过变量是否透传了一些敏感信息触发关键字
			break;
		case "SignatureDoesNotMatch":
			result = "Specified signature is not matched with our calculation.";//Signature加密错误，如为SDK调用，则需要注意accessKeyId和accessKeySecret字符串赋值正确无误；如自行加密的Signature，则需要检查加密逻辑，对照文档：https://help.aliyun.com/document_detail/56189.html
			break;
		case "InvalidTimeStamp.Expired":
			result = "Specified time stamp or date value is expired.";//时间戳错误，发出请求的时间和服务器接收到请求的时间不在15分钟内。经常出现该错误的原因是时区原因造成的,目前网关使用的时间是GMT时间
			break;
		case "SignatureNonceUsed":
			result = "Specified signature nonce was used already.";//唯一随机数重复，SignatureNonce为唯一随机数，用于防止网络重放攻击。不同请求间要使用不同的随机数值。
			break;
		case "InvalidVersion":
			result = "Specified parameter Version is not valid.";//版本号错误，需要确认接口的版本号，如云通信短信、语音、流量服务的Version=2017-05-25
			break;
		case "InvalidAction.NotFound":
			result = "Specified api is not found, please check your url and method";//接口名错误，需要确认接口地址和接口名，如云通信短信服务短信发送：dysmsapi.aliyuncs.com，接口名Action=SendSms
			break;
		default:
			result = "没有找到code码代表的错误信息!";
			break;
		}		
		return result;
	}

}
