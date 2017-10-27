package com.rd.ifaes.core.core.constant;

/**
 * 缓存常量类
 * 
 * @author lh
 * @version 3.0
 * @since 2016-7-1
 *
 */
public class CacheConstant {
	
	private CacheConstant() {
	}

	/**
	 * 缓存 KEY 前缀
	 */
	public static final String KEY_CACHE_PREFIX = "sysCache:";
	
	/**
	 * 默认缓存 redis
	 */
	public static final String DEFAULT_CACHE_TYPE="redis";	
	/**
	 * 缓存类型：ehcache
	 */
	public static final String CACHE_TYPE_EHCACHE = "ehcache";
	/**
	 * 缓存类型：redis
	 */
	public static final String CACHE_TYPE_REDIS   = "redis";
	/**
	 * 缓存类型：redisCluster
	 */
	public static final String CACHE_TYPE_REDIS_CLUSTER   = "redisCluster";
	
	/**
	 * 缓存清除标识前缀
	 */
	public static final String  KEY_PREFIX_CACHE_EVICT="cacheEvict:interval:";
	
	/****************************************** 用户相关参数  ******************************************/
	/**
	 * 用户认证前缀
	 */
	public static final String KEY_PREFIX_USER_IDENTIFY_USER_ID="userIdentify:userId:";
	/**
	 * 用户信息前缀
	 */
	public static final String KEY_PREFIX_USER_USER_ID="user:userId:";
	
	/**
	 * 用户登录失败次数前缀
	 */
	public static final String KEY_PREFIX_USER_LOGIN_FAIL_NUM="user:loginFailNum:";
	
	/**
	 * 用户关联表信息前缀
	 */
	public static final String KEY_PREFIX_USER_CACHE_USER_ID="userCache:userId:";
	
	/**
	 * 用户银行卡
	 */
	public static final String KEY_PREFIX_USER_BANK_CARD = "userBankCard:userId:";
	/****************************项目相关***************************/
	/**
	 * 产品单体缓存前缀
	 */
	public static final String KEY_PROJECT_UUID_PREFIX = "project:uuid:";
	/**
	 * 产品单体缓存
	 */
	public static final String KEY_PROJECT_UUID = "project:uuid:{uuid}";
	/**
	 * 产品列表（分页）
	 */
	public static final String KEY_PROJECT_LIST= "projectList:p{model.projectTypeId}:m{model.amountCondition}:t{model.timeCondition}:r{model.repayStyle}"
			+":o{model.page.order}:s{model.page.sort}:ps{model.page.pageSize}:p{model.page.page}:sc{model.saleChannel}:uat{model.userAccountType}";
	/**
	 * 产品列表前缀
	 */
	public static final String KEY_PREFIX_PROJECT_LIST  =  "projectList:";
	/**
	 * 项目详情
	 */
	public static final String KEY_PREFIX_PROJECT_CONTENT="project:content:";
	/**
	 * 借款资料
	 */
	public static final String KEY_PREFIX_BORROWER_INFO="project:borrowerInfo:";
	/**
	 * 项目自动投资标识
	 */
	public static final String KEY_PREFIX_PROJECT_AUTO_INVEST_STATUS="project:auto:invest:status";
	
	/**债权缓存区 start */
	/**
	 * 债权规则缓存
	 */
	public static final String KEY_BOND_RULE_UUID = "bond:bondRule:{id}";
	/**
	 * 债权实体类 缓存
	 */
	public static final String KEY_PREFIX_BOND_UUID = "bond:{bondId}:entity";
	/**
	 * 债权详情 user登录后
	 */
	public static final String KEY_BOND_DETAIL = "bond:{id}:detail";
	/**
	 * 债权详情
	 */
	public static final String KEY_BOND_DETAIL_USER = "bond:{id}:detail:{user.uuid}";
	/**
	 * 进入转让设置缓存
	 */
	public static final String KEY_BOND_SETTING_UUID = "bondList:bond:setting:{id}";
	/**
	 * 债权专区 筛选条件 
	 */
	public static final String KEY_BOND_CONDITION= "bond:condition";
	/**
	 * 债权前缀
	 */
	public static final String KEY_PREFIX_BOND  =  "bond:";
	/**
	 * 债权专区列表前缀
	 */
	public static final String KEY_PREFIX_BOND_LIST  =  "bondList:";
	/**
	 * 债权专区列表 含筛选条件
	 */
	public static final String KEY_BOND_LIST  =  "bondList:bmt{bond.bondMoneyType}:rdt{bond.remainDaysType}:at{bond.aprType}:rs{bond.repayStyle}"
			+":o{bond.page.order}:s{bond.page.sort}:ps{bond.page.pageSize}:p{bond.page.page}";
	
	/**债权缓存区 end */
	/**
	 * 项目详情页（项目信息）
	 */
	public static final String KEY_PREFIX_PROJECT_DETAIL="project:detail:";
	/**
	 * 项目详情页（项目借款资料）
	 */
	public static final String  KEY_PREFIX_PROJECT_PIC ="project:pic:";
	/**
	 * 项目分类-缓存前缀
	 */
	public static final String KEY_PREFIX_PROJECT_TYPE="projectType:";
	/**
	 * 项目分类的子分类
	 */
	public static final String  KEY_PREFIX_PROJECT_TYPE_CHILD="projectType:childs:{parentId}";
	/**
	 * 项目分类-查询条件
	 */
	public static final String KEY_PREFIX_PROJECT_TYPE_QUERY_CONDITION="projectType:queryCondition:{projectTypeId}";
	
	/****************************************** 变现相关参数 Start ****************************************/
	/**
	 * 变现实体
	 */
	public static final String KEY_PREFIX_REALIZE_UUID= "realize:uuid:{uuid}";
	/**
	 * 变现实体
	 */
	public static final String KEY_REALIZE_UUID= "realize:uuid:";
	/**
	 * 被变现产品详情
	 */
	public static final String KEY_PREFIX_REALIZE_INFO = "realize:realize:info:{realize.uuid}";
	/**
	 * 被变现产品详情
	 */
	public static final String KEY_REALIZE_INFO = "realize:realize:info:";
	/**
	 * 变现原始投资ID
	 */
	public static final String KEY_REALIZE_INVESTID = "realize:investId:";
	/**
	 * 被变现产品详情
	 */
	public static final String KEY_PREFIX_PRODUCT_INFO = "realize:product:info:{investId}";
	/**
	 * 变现原始产品简介
	 */
	public static final String KEY_PREFIX_ORGPROJECT_CONTENT = "realize:orgProject:content:{projectId}";
	/**
	 * 变现原始产品特点
	 */
	public static final String KEY_PREFIX_ORGPROJECT_FEATURES= "realize:orgProject:features:{projectId}";
	/**
	 * 变现列表前缀
	 */
	public static final String KEY_PREFIX_REALIZE_LIST  =  "realizeList:";
	/**
	 * 变现列表(含筛选条件)
	 */
	public static final String KEY_REALIZE_LIST  =  "realizeList:m{realize.realizeAmountCondition}:t{realize.realizeDayCondition}:a{realize.realizeAprCondition}"
			+":o{realize.page.order}:s{realize.page.sort}:ps{realize.page.pageSize}:p{realize.page.page}:sc{realize.saleChannel}";
	/**
	 * 变现规则
	 */
	public static final String KEY_REALIZE_RULE = "realize:rule";
	/**
	 * 变现筛选条件
	 */
	public static final String KEY_REALIZE_CONDITION= "realize:condition";
	
	
	/****************************************** 变现相关参数 end ******************************************/
	/****************************************** 待还/待收相关参数 start ******************************************/
	
	public static final String KEY_PREFIX_COLLECTION_YEARCOLLECTDATA_USERID = "collection:yearCollectData:userId:";
	public static final String KEY_COLLECTION_YEARCOLLECTMONTHS = "collection:yearCollectMonths";
	
	
	/****************************************** 待还/待收相关参数 end ******************************************/
	
	/**
	 * 风险等级
	 */
	public static final String KEY_PREFIX_LEVEL_CONFIG= "levelConfig:{riskLevel}";
	
	/****************************************** 系统管理相关参数  ******************************************/
	/**
	 * 字典项
	 */
	public static final String KEY_PREFIX_DICT_DATA_DICT_TYPE = "sys:dictData:dictType:";

	/**
	 * 参数配置
	 */
	public static final String KEY_PREFIX_CONFIG_CODE="sys:config:code:";
	
	/**
	 * 消息缓存
	 */
	public static final String KEY_PREFIX_MESSAGE_TYPE="sys:messageType:";
	
	/**
	 * 地区表
	 */
	public static final String KEY_AREA_JSON = "sys:area:sjon";
	
	/**
	 * 资源信息
	 */
	public static final String KEY_RESOURCE = "sys:resource";
	
	/**
	 * 资金模板
	 */
    public static final String KEY_LOG_TEMPLATE = "sys:logtemplate";
    
    /**
	 * 栏目配置
	 */
	public static final String KEY_PREFIX_SECTION_CODE="sys:section:code:";
    
    
    /****************************************** 缓存参数名称 ******************************************/
    
	/**
	 * 缓存对象名称
	 */
	public static final String KEY_OBJECT_NAME = "objectName";
	
	/**
	 * 缓存列表名称
	 */
	public static final String KEY_LIST_NAME = "listName";
	
	/**
	 * 缓存对象唯一性属性名，可以是uuid或者code
	 */
	public static final String KEY_OBJECT_UNIQUE_NAME = "uniqueName";
	/**
	 * 分组参数名称，String[]类型
	 */
	public static final String KEY_GROUP_PARAMS_NAME = "groupParamsName";
	
	/**
	 * 其他分组参数名称，String[][]类型
	 */
	public static final String KEY_OTHER_GROUP_PARAMS_NAME = "otherGroupParamsName";
	
	/**
	 * 排序参数名称，String[]类型
	 */
	public static final String KEY_SORT_PARAMS_NAME = "sortParamsName";
	
	/**
	 * 类别：所有类别 all
	 */
	public static final String KEY_GROUP_ALL = "all";
	
	/**
	 * 首页项目缓存key（新手、精选）
	 */
	public static final String KEY_PROJECT_LIST_INDEX= "projectList:c{model.choice}:n{model.novice}:sc{model.saleChannel}";
	
	/****************************************** 注册短信缓存常量 ******************************************/
	/**
	 * 注册短信号码缓存
	 */
    public static final String KEY_PHONECODE_PHONENUM = "phoneCode:phoneNum:";
	/**
	 * 注册短信IP缓存
	 */
    public static final String KEY_PHONECODE_SENDIP = "phoneCode:sendIp:";
    
    /**
	 * 注册短信时间缓存
	 */
    public static final String KEY_PHONECODE_SENDTIME = "phoneCode:sendTime:";
    /****************************************** 自动投资缓存常量 ******************************************/
    /**
	 * 自动投资剩余可投
	 */
    public static final String KEY_AUTOINVEST_REMAINACCOUNT = "autoInvest:remainAccount:";
    /**
	 * 自动投资状态
	 */
    public static final String KEY_AUTOINVEST_AUTOSTATUS = "autoInvest:autoStatus:";
    
    /*********投资预约************/
    /**
	 * 用户投资预约项目ID字符串
	 */
    public static final String KEY_PREFIX_USER_BESPEAK_PROJECT_IDS = "bespeak:projectIds:";
    
    /*********VIP************/
    /**
     * 用户VIP
     */
    public static final String KEY_PREFIX_USER_VIP="userVip:userId:";
    
    /**
     * 用户vip等级列表
     */
    public static final String KEY_VIP_LEVEL_LIST = "vipLevelList:{vipLevel.vipLevel}:{vipLevel.deleteFlag}";
    /**
     * 用户vip等级
     */
    public static final String KEY_VIP_LEVEL = "vipLevel:{vipLevel}";
    
    /**
     * 用户vip等级列表前缀
     */
    public static final String KEY_PREFIX_VIP_LEVEL_LIST = "vipLevelList:";
    
    /**
     * 积分商品剩余数量前缀
     */
    public static final String KEY_PREFIX_SCORE_GOODS_LESS_NUM = "goodsLessNum:";
    /**
     * 用户订单状态统计前缀
     */
    public static final String KEY_PREFIX_USER_ORDER_STATUS_NUM = "userOrderStatus:";
    /**
     * 最近的订单
     */
    public static final String KEY_NEW_ORDER = "newOrder";
    
    /**
     * 商品列表页查询条件
     */
    public static final String KEY_PREFIX_GOODS_QUERY_CONDITION = "goodsQueryCondition";
    
    /**
     * 用户昨日收益
     */
    public static final String KEY_USER_YESTERDAY_EARN_AMOUNT = "yesterdayEarnAmount:{userId}";
    
    /**
     * 红包发放
     */
    public static final String KEY_GRANT_USER_RED = "oper:redpacket:redpacketList:grant";
    
    /**
     * 消息群发
     */
    public static final String KEY_SEND_MESSAGE = "message:mass:mass" ;
    
}
