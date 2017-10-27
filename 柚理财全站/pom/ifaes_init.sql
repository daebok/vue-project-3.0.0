/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50627
Source Host           : localhost:3306
Source Database       : ifaes

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2017-01-18 18:17:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `account_code` varchar(20) NOT NULL COMMENT '账户编号',
  `account_type` varchar(2) DEFAULT NULL COMMENT '账户类别',
  `parent_code` varchar(20) DEFAULT NULL COMMENT '上级账户编号',
  `total` decimal(20,4) DEFAULT '0.0000' COMMENT '账户总额',
  `use_money` decimal(20,4) DEFAULT '0.0000' COMMENT '可用余额',
  `no_use_money` decimal(20,4) DEFAULT '0.0000' COMMENT '冻结金额',
  `status` char(1) DEFAULT '1' COMMENT '账户状态 0 未启用；1 启用',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_account_user_id_account_code` (`user_id`,`account_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('8185665ea4d311e6b8d6408d5c2885aa', 'd315ad95a4d211e6b8d6408d5c2885aa', '01', '01', null, '0.0000', '0.0000', '0.0000', '1', '2016-11-07 18:19:55');

-- ----------------------------
-- Table structure for `account_check`
-- ----------------------------
DROP TABLE IF EXISTS `account_check`;
CREATE TABLE `account_check` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `tpp_user_cust_id` varchar(50) DEFAULT NULL COMMENT '用户第三方id',
  `tpp_use_money` decimal(20,4) DEFAULT NULL COMMENT '第三方可用',
  `tpp_no_use_money` decimal(20,4) DEFAULT NULL COMMENT '第三方冻结',
  `use_money` decimal(20,4) DEFAULT NULL COMMENT '可用',
  `no_use_money` decimal(20,4) DEFAULT NULL COMMENT '冻结',
  `use_money_diff` decimal(20,4) DEFAULT NULL COMMENT '可用差额',
  `no_use_money_diff` decimal(20,4) DEFAULT NULL COMMENT '冻结差额',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户资金对账';

-- ----------------------------
-- Records of account_check
-- ----------------------------

-- ----------------------------
-- Table structure for `account_log`
-- ----------------------------
DROP TABLE IF EXISTS `account_log`;
CREATE TABLE `account_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `account_code` varchar(20) NOT NULL COMMENT '账户编号',
  `to_user` varchar(32) DEFAULT NULL COMMENT '交易对方的用户ID',
  `payments_type` varchar(2) DEFAULT NULL COMMENT '收支类型:0不变  1收入  2支出',
  `money` decimal(20,4) DEFAULT '0.0000' COMMENT '交易金额',
  `account_type` varchar(32) DEFAULT NULL COMMENT '资金操作类型（操作类型：充值recharge、投资tender）',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单号',
  `use_money` decimal(20,4) DEFAULT '0.0000' COMMENT '可用余额',
  `no_use_money` decimal(20,4) DEFAULT '0.0000' COMMENT '冻结余额',
  `create_timestamp` bigint(14) DEFAULT NULL COMMENT '新增时间戳（毫秒）',
  `earn` decimal(20,4) DEFAULT '0.0000' COMMENT '收益',
  PRIMARY KEY (`uuid`),
  KEY `idx_account_log_user_id_account_code` (`user_id`,`account_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户日志表';

-- ----------------------------
-- Records of account_log
-- ----------------------------

-- ----------------------------
-- Table structure for `activity_plan`
-- ----------------------------
DROP TABLE IF EXISTS `activity_plan`;
CREATE TABLE `activity_plan` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `activity_name` varchar(20) DEFAULT NULL COMMENT '活动方案名称',
  `activity_code` varchar(30) DEFAULT NULL COMMENT '活动方案唯一标识',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  `status` char(1) DEFAULT NULL COMMENT '状态 ：0禁用  1启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动方案表';

-- ----------------------------
-- Records of activity_plan
-- ----------------------------
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c982f1', '一级好友邀请投资奖励', 'first_invite_tender_suc', '7', '1', '2016-08-15 14:29:48');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c982f2', '二级好友邀请投资奖励', 'second_invite_tender_suc', '8', '1', '2016-08-15 14:30:23');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f1', '注册送好礼', 'register', '1', '1', '2016-07-22 17:17:47');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f2', '首次投资奖励', 'first_tender', '2', '1', '2016-07-22 17:18:27');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f3', '投资奖励', 'tender_suc', '3', '1', '2016-07-22 17:19:03');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f4', '好友注册奖励', 'invite_register', '4', '1', '2016-07-22 17:19:59');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f5', '一级好友邀请首投奖励', 'first_invite_first_tender', '5', '1', '2016-07-22 17:21:10');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f6', '二级好友邀请首投奖励', 'second_invite_first_tender', '6', '1', '2016-07-22 17:21:57');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f7', '自定义', 'custom', '11', '1', '2016-07-22 17:22:33');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f8', 'vip等级特权', 'vip_level', '9', '1', '2016-07-22 17:23:29');
INSERT INTO `activity_plan` VALUES ('1764f3e6d315454ab070568f55c992f9', '手动定向发放', 'select_users', '10', '1', '2016-08-28 11:14:54');

-- ----------------------------
-- Table structure for `article`
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `section_code` varchar(32) NOT NULL COMMENT '栏目标识',
  `title` varchar(50) NOT NULL COMMENT '标题',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `is_recommend` char(1) DEFAULT '0' COMMENT '是否推荐，0：否，1：是',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶，0：否，1：是',
  `content` text COMMENT '内容',
  `remark` varchar(512) DEFAULT NULL COMMENT '简介',
  `clicks` int(11) DEFAULT '0' COMMENT '点击量',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `pic_path` text COMMENT '图片路径',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `delete_flag` char(1) DEFAULT '0' COMMENT '状态，0：显示，1：删除',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';

-- ----------------------------
-- Records of article
-- ----------------------------

-- ----------------------------
-- Table structure for `auto_invest_rule_log`
-- ----------------------------
DROP TABLE IF EXISTS `auto_invest_rule_log`;
CREATE TABLE `auto_invest_rule_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `amount_day_max` decimal(20,4) DEFAULT NULL COMMENT '每日最大投资金额',
  `repay_styles` varchar(20) DEFAULT NULL COMMENT '还款方式',
  `month_type` char(1) DEFAULT NULL COMMENT '是否可投月期限',
  `month_limit_min` int(8) DEFAULT NULL COMMENT '月期限最小值',
  `month_limit_max` int(8) DEFAULT NULL COMMENT '月截至期限',
  `day_type` char(1) DEFAULT NULL COMMENT '是否可投天期限',
  `day_limit_min` int(8) DEFAULT NULL COMMENT '天期限最小值',
  `day_limit_max` int(8) DEFAULT NULL COMMENT '天期限最大值',
  `apr_min` decimal(4,2) DEFAULT NULL COMMENT '投资收益最小值',
  `realize_useful` char(1) DEFAULT '0' COMMENT ' 是否可投变现产品 0 不可投1可投，默认0',
  `bond_useful` char(1) DEFAULT '0' COMMENT ' 是否可投债权转让0 不可投1可投，默认0',
  `status` char(1) DEFAULT NULL COMMENT '状态 0.关闭 1开启',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自动投资设置记录表';

-- ----------------------------
-- Records of auto_invest_rule_log
-- ----------------------------

-- ----------------------------
-- Table structure for `bond`
-- ----------------------------
DROP TABLE IF EXISTS `bond`;
CREATE TABLE `bond` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `bond_name` varchar(30) DEFAULT NULL COMMENT '债权名称',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '债权人',
  `bond_apr` decimal(10,4) DEFAULT NULL COMMENT '折溢价率',
  `sold_interest` decimal(20,4) DEFAULT NULL COMMENT '已售债权利息',
  `bond_fee` decimal(10,4) DEFAULT NULL COMMENT '已支付手续费',
  `bond_money` decimal(20,4) DEFAULT NULL COMMENT '债权金额',
  `sold_capital` decimal(20,4) DEFAULT NULL COMMENT '已售债权本金',
  `status` char(1) DEFAULT NULL COMMENT '债权状态:发布0， 转让完成3，自动撤回4，后台撤回5 ',
  `limit_hours` int(6) DEFAULT NULL COMMENT '债权有效时长（小时）',
  `bond_end_time` datetime DEFAULT NULL COMMENT '债权截止日期',
  `bond_no` varchar(20) DEFAULT NULL COMMENT '债权转让编号',
  `start_period` int(6) DEFAULT NULL COMMENT '开始期数',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `rule_id` varchar(32) DEFAULT NULL COMMENT '债权转让规则ID',
  `bond_lowest_money` decimal(20,4) DEFAULT '0.0000' COMMENT '债权起投金额',
  `bond_most_money` decimal(20,4) DEFAULT '0.0000' COMMENT '债权最高投资金额',
  `remain_days` int(11) DEFAULT '0' COMMENT '剩余期限（天）',
  `success_time` datetime DEFAULT NULL COMMENT '转让成功时间',
  `sell_style` varchar(1) DEFAULT '' COMMENT '实际转让的方式 0 部分转让 1 全部转让',
  `stage` int(1) DEFAULT NULL COMMENT '债权投资阶段：1 在投;2、满标(或无剩余可投)',
  `protocol_id` varchar(32) DEFAULT NULL COMMENT '债权协议ID',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_bond_bond_no` (`bond_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权转让表';

-- ----------------------------
-- Records of bond
-- ----------------------------

-- ----------------------------
-- Table structure for `bond_collection`
-- ----------------------------
DROP TABLE IF EXISTS `bond_collection`;
CREATE TABLE `bond_collection` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '投资人',
  `bond_id` varchar(32) DEFAULT NULL COMMENT '债权ID',
  `bond_invest_id` varchar(32) DEFAULT NULL COMMENT '债权投资ID',
  `repayment_id` varchar(32) DEFAULT NULL COMMENT '还款ID',
  `bond_capital` decimal(20,4) DEFAULT NULL COMMENT '已成功转出本金',
  `bond_interest` decimal(20,4) DEFAULT NULL COMMENT '已成功转出利息',
  `capital` decimal(20,4) DEFAULT NULL COMMENT '待收本金',
  `collection_amount` decimal(20,4) DEFAULT NULL COMMENT '预计收款金额',
  `collection_time` datetime DEFAULT NULL COMMENT '预计收款时间',
  `received_amount` decimal(20,4) DEFAULT NULL COMMENT '实际收款金额',
  `received_time` datetime DEFAULT NULL COMMENT '实际收款时间',
  `interest` decimal(20,4) DEFAULT NULL COMMENT '待收利息',
  `late_days` int(11) DEFAULT NULL COMMENT '预计天数',
  `late_interest` decimal(20,4) DEFAULT NULL COMMENT '逾期利息',
  `period` tinyint(4) DEFAULT NULL COMMENT '待收期数',
  `status` char(1) DEFAULT NULL COMMENT '状态：0未还，1已还',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`),
  KEY `idx_bond_collection_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权待收表';

-- ----------------------------
-- Records of bond_collection
-- ----------------------------

-- ----------------------------
-- Table structure for `bond_invest`
-- ----------------------------
DROP TABLE IF EXISTS `bond_invest`;
CREATE TABLE `bond_invest` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `bond_id` varchar(32) DEFAULT NULL COMMENT '债权ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `invest_no` varchar(30) DEFAULT NULL COMMENT '投资流水号',
  `invest_date` varchar(8) DEFAULT NULL COMMENT '投资日期',
  `user_id` varchar(32) DEFAULT NULL COMMENT '受让人',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '投资金额',
  `bond_fee` decimal(10,2) DEFAULT NULL COMMENT '债权人支付手续费',
  `payed_interest` decimal(10,2) DEFAULT NULL COMMENT '提前付息',
  `received_account` decimal(10,2) DEFAULT NULL COMMENT '已收金额',
  `status` char(1) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '添加IP',
  `invest_order_no` varchar(32) DEFAULT NULL COMMENT '债权投资订单号',
  `project_invest_id` varchar(32) DEFAULT NULL COMMENT '债权投资对应原始投资记录ID',
  `invest_channel` char(1) DEFAULT NULL COMMENT '投资渠道 1 PC  2 APP 3 微信 ',
  PRIMARY KEY (`uuid`),
  KEY `idx_bond_invest_bond_id` (`bond_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权投资表';

-- ----------------------------
-- Records of bond_invest
-- ----------------------------

-- ----------------------------
-- Table structure for `bond_rule`
-- ----------------------------
DROP TABLE IF EXISTS `bond_rule`;
CREATE TABLE `bond_rule` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `rule_status` char(1) DEFAULT '1' COMMENT '1 开启 0未开启  默认1,未开启不允许发布债权',
  `hold_days` int(4) DEFAULT '0' COMMENT '起息持有天数  默认 0 不开启此规则',
  `remain_days` int(4) DEFAULT '0' COMMENT '还款到期剩余天数,默认0 不开启此规则',
  `period_remain_days` int(4) DEFAULT NULL COMMENT '本期还款剩余天数 0 不开启此规则',
  `deadline` int(8) DEFAULT NULL COMMENT '转让时效（单位：小时，到时间自动撤回）',
  `sell_multiple` int(4) DEFAULT NULL COMMENT '转让起投金额倍数',
  `sell_style` char(1) DEFAULT '0' COMMENT '转让金额方式:0 部分转让;1全额转让,默认0',
  `buy_style` char(1) DEFAULT '0' COMMENT '受让金额方式:0 部分受让;1全额受让,默认0',
  `buy_amount_min` decimal(20,4) DEFAULT NULL COMMENT '单笔受让最小金额',
  `buy_amount_max` decimal(20,4) DEFAULT NULL COMMENT '单笔受让最大金额',
  `discount_rate_min` decimal(6,4) DEFAULT NULL COMMENT '折溢价率下限值',
  `discount_rate_max` decimal(6,4) DEFAULT NULL COMMENT '折溢价率上限值',
  `fee_style` char(1) DEFAULT '1' COMMENT '收费方式 0不收费 1固定金额 2 固定比例; 默认1',
  `fee_initiate_amount` decimal(20,4) DEFAULT NULL COMMENT '收费起步金额',
  `fee_fixed` decimal(20,4) DEFAULT NULL COMMENT '固定金额手续费',
  `fee_rate` decimal(6,4) DEFAULT '0.0000' COMMENT '转让金额百分比收取手续费（默认为0，单位是%）',
  `fee_single_max` decimal(20,4) DEFAULT NULL COMMENT '单笔手续费上限值（单位:元）',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='债权转让规则配置';

-- ----------------------------
-- Records of bond_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `borrow`
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow` (
  `project_id` varchar(32) NOT NULL COMMENT '借款基本信息ID',
  `content` text COMMENT '借款详情',
  `borrow_use` varchar(50) DEFAULT NULL COMMENT '借款用途',
  `period` int(2) DEFAULT NULL COMMENT '期数',
  `repayment_account` decimal(20,4) DEFAULT '0.0000' COMMENT '应还本金',
  `repayment_yes_account` decimal(20,4) DEFAULT '0.0000' COMMENT '实还本金',
  `repayment_yes_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '实还利息',
  `bond_useful` char(1) DEFAULT NULL COMMENT '是否可以债权转让 1 可以，0不可以',
  `bond_max_turn` int(4) DEFAULT '1' COMMENT '债权转让最大次数,默认为1,单次转让，0为无限次转让',
  `borrow_nature` char(1) DEFAULT '1' COMMENT '借款性质: 1  个人借款 2 企业借款',
  `has_pawn`  char(1) DEFAULT '0' COMMENT '是否有抵押物；1有抵押物，0无，默认0',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款项目';

-- ----------------------------
-- Records of borrow
-- ----------------------------

-- ----------------------------
-- Table structure for `borrow_bespeak`
-- ----------------------------
DROP TABLE IF EXISTS `borrow_bespeak`;
CREATE TABLE `borrow_bespeak` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(12) DEFAULT NULL COMMENT '联系电话',
  `sex` char(1) DEFAULT NULL COMMENT '性别:M 男性，F女性',
  `province` varchar(20) DEFAULT NULL COMMENT '省',
  `city` varchar(20) DEFAULT NULL COMMENT '市',
  `money` double(20,4) DEFAULT '0.0000' COMMENT '借款金额',
  `limit_time` int(5) DEFAULT '0' COMMENT '借款期限(天)',
  `status` char(1) DEFAULT NULL COMMENT '状态 0未处理 1 已回访 2不回访',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '添加IP',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `area` varchar(20) DEFAULT NULL COMMENT '区',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款预约表';

-- ----------------------------
-- Records of borrow_bespeak
-- ----------------------------

-- ----------------------------
-- Table structure for `borrow_follow`
-- ----------------------------
DROP TABLE IF EXISTS `borrow_follow`;
CREATE TABLE `borrow_follow` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目id',
  `funds_condition` varchar(512) DEFAULT NULL COMMENT '资金使用情况',
  `operate_condition` varchar(512) DEFAULT NULL COMMENT '借款人经营情况',
  `finance_condition` varchar(512) DEFAULT NULL COMMENT '借款人财务情况',
  `repayment_ability` varchar(512) DEFAULT NULL COMMENT '借款还款能力',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='贷后跟踪';

-- ----------------------------
-- Records of borrow_follow
-- ----------------------------

-- ----------------------------
-- Table structure for `borrow_upload`
-- ----------------------------
DROP TABLE IF EXISTS `borrow_upload`;
CREATE TABLE `borrow_upload` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `file_type` char(2) DEFAULT NULL COMMENT '上传文件类型: 1借款资料 2抵押物资料 3其他',
  `file_path` varchar(512) DEFAULT NULL COMMENT '上传文件路径',
  `add_time` datetime DEFAULT NULL COMMENT '上传时间',
  `sort` int(4) DEFAULT NULL COMMENT '显示顺序',
  PRIMARY KEY (`uuid`),
  KEY `idx_borrow_upload_project_id` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款资料关联表';

-- ----------------------------
-- Records of borrow_upload
-- ----------------------------

-- ----------------------------
-- Table structure for `buyback`
-- ----------------------------
DROP TABLE IF EXISTS `buyback`;
CREATE TABLE `buyback` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `user_id` varchar(32) DEFAULT NULL COMMENT '申请人ID',
  `money` decimal(20,4) DEFAULT NULL COMMENT '赎回金额',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `status` char(2) DEFAULT NULL COMMENT '状态：0 新建  1 申请已提交   2 审批通过 3 审批不通过  4 成功 5 失败',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='赎回记录表';

-- ----------------------------
-- Records of buyback
-- ----------------------------

-- ----------------------------
-- Table structure for `cash`
-- ----------------------------
DROP TABLE IF EXISTS `cash`;
CREATE TABLE `cash` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号（平台本地生成订单号）',
  `cash_no` varchar(64) DEFAULT NULL COMMENT '取现订单号（UFX流水号）',
  `amount` decimal(20,4) DEFAULT '0.0000' COMMENT '取现金额',
  `real_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '实际到账金额',
  `cash_fee` decimal(20,4) DEFAULT '0.0000' COMMENT '取现手续费',
  `serv_fee` decimal(20,4) DEFAULT '0.0000' COMMENT '手续费',
  `status` char(1) DEFAULT '0' COMMENT '0：提现申请  1：提现处理中 2：提现待审核  3：提现成功  4：提现失败',
  `card_id` varchar(50) DEFAULT NULL COMMENT '取现银行卡号',
  `bank_code` varchar(12) DEFAULT NULL COMMENT '所属银行',
  `need_audit` char(1) DEFAULT '0' COMMENT '是否需要审核（1：需要 0：不需要）',
  `is_advance` char(1) DEFAULT '0' COMMENT '是否垫付手续费（1：需要 0：不需要）',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '添加IP',
  `verify_time` datetime DEFAULT NULL COMMENT '审核时间',
  `verify_user_name` varchar(30) DEFAULT NULL COMMENT '审核人',
  `verify_remark` varchar(30) DEFAULT NULL COMMENT '审核备注',
  `money` decimal(20,4) DEFAULT '0.0000' COMMENT '实际操作金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `man_handle` char(1) DEFAULT '0' COMMENT '人工核查状态 0 不需处理 1待处理  2已处理，默认0',
  `handle_reason` varchar(255) DEFAULT NULL COMMENT '核查原因',
  `handle_time` datetime DEFAULT NULL COMMENT '核查时间',
  `handle_user` varchar(32) DEFAULT NULL COMMENT '核查人',
  `channel`  char(1) DEFAULT '1' COMMENT '渠道(1-PC,2-APP,3-微信,默认PC)',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='取现记录表';

-- ----------------------------
-- Records of cash
-- ----------------------------
 
-- ----------------------------
-- Table structure for `freeze_trade_log`
-- ----------------------------
DROP TABLE IF EXISTS `freeze_trade_log`;
CREATE TABLE `freeze_trade_log` (
  `uuid` varchar(32) NOT NULL,
  `freeze_type` char(2) DEFAULT '00' COMMENT '冻结类型（00 冻结, 01 取现冻结;02 还款冻结 ,默认00  ）',
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号（平台本地生成订单号）',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户号',
  `amount` decimal(20,4) DEFAULT '0.0000' COMMENT '交易金额',
  `project_no` varchar(32) DEFAULT NULL COMMENT '项目编号(还款冻结)',
  `freeze_no` varchar(30) DEFAULT NULL COMMENT '冻结流水号',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `status` char(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金冻结记录';

-- ----------------------------
-- Records of freeze_trade_log
-- ----------------------------

-- ----------------------------
-- Table structure for `message_resource`
-- ----------------------------
DROP TABLE IF EXISTS `message_resource`;
CREATE TABLE `message_resource` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `bundle` varchar(20) DEFAULT NULL COMMENT '资源包标识',
  `lang` varchar(20) DEFAULT NULL COMMENT '语言标识',
  `message_code` varchar(50) NOT NULL COMMENT '资源信息标识',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源文件信息';

-- ----------------------------
-- Records of message_resource
-- ----------------------------

-- ----------------------------
-- Table structure for `product`
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `project_id` varchar(32) NOT NULL COMMENT '项目ID',
  `content` text COMMENT '借款项目信息内容',
  `lock_time_limit` int(8) DEFAULT NULL COMMENT '锁定期',
  `sale_time_limit` int(8) DEFAULT NULL COMMENT '开放期',
  `realize_useful` char(1) DEFAULT '0' COMMENT '是否可变现: 1 可变现 0 不可变现，默认 0',
  `buyback_verify` char(1) DEFAULT NULL COMMENT '赎回是否需要审核( 1 需要， 0 不需要,默认1 )',
  `realize_lowest_money` decimal(20,4) DEFAULT NULL COMMENT '最低变现金额',
  `overdue_rate` decimal(10,4) DEFAULT NULL COMMENT '变现逾期罚息率',
  `auto_repay` char(1) DEFAULT NULL COMMENT '是否可自动还款: 1 自动还款 0 自动还款，默认 0',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='理财产品表';

-- ----------------------------
-- Records of product
-- ----------------------------

-- ----------------------------
-- Table structure for `project`
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `project_no` varchar(18) DEFAULT NULL COMMENT '项目编号(YYYYMMDD+5位自增长数字)',
  `project_name` varchar(100) DEFAULT NULL COMMENT '项目名称',
  `raise_account` decimal(20,4) DEFAULT '0.0000' COMMENT '筹集目标金额，大于截标后金额',
  `account` decimal(20,4) DEFAULT '0.0000' COMMENT '项目总额（成立后为实际募集金额）',
  `account_yes` decimal(20,4) DEFAULT '0.0000' COMMENT '已投金额',
  `apr` decimal(8,4) DEFAULT '0.0000' COMMENT '年利率',
  `add_apr` decimal(8,4) DEFAULT NULL COMMENT '加息',
  `interest_style` char(1) DEFAULT '1' COMMENT '计息方式: 1、成立计息   2、 T+N计息 ',
  `interest_start_days` int(3) DEFAULT NULL COMMENT '起始计息天数(T+N计息方式 )',
  `repay_style` char(1) DEFAULT '2' COMMENT '还款方式: 1月等额本息  2一次性还款 3 每月还息到期还本 4 等额本金',
  `fixed_repay_day` int(2) DEFAULT NULL COMMENT '固定还款日(1-31)',
  `time_type` char(1) DEFAULT NULL COMMENT '借款期限类型： 0月标 1天标',
  `time_limit` int(8) DEFAULT NULL COMMENT '借款期限',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `publish_verify_time` datetime DEFAULT NULL COMMENT '发布审核时间(或借款审核时间)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `show_time` datetime DEFAULT NULL COMMENT '上架时间',
  `stop_time` datetime DEFAULT NULL COMMENT '下架时间',
  `sale_time` datetime DEFAULT NULL COMMENT '开售时间',
  `raise_time_limit` int(8) DEFAULT '0' COMMENT '募集期:募集期时长，单位：天（不填，默认为0，不限制）',
  `raise_end_time` datetime DEFAULT NULL COMMENT '募集结束时间',
  `review_time` datetime DEFAULT NULL COMMENT '成立审核时间',
  `borrow_manage_rate` decimal(8,4) DEFAULT NULL COMMENT '借款手续费率',
  `interest_manage_rate` decimal(8,4) DEFAULT NULL COMMENT '利息管理费率',
  `overdue_fee_rate` decimal(8,4) DEFAULT NULL COMMENT '逾期罚息利率',
  `guarantee_rate` decimal(8,4) DEFAULT NULL COMMENT '担保费率',
  `novice` char(1) DEFAULT '0' COMMENT '新手专享标识： 1新手专享 0 普通 ，默认：0',
  `choice` char(1) DEFAULT '0' COMMENT '是否精选 0：不是精选，1：精选;默认0',
  `sale_style` char(1) DEFAULT '1' COMMENT '销售规则 ：1 按金额  2 按份数，默认1',
  `lowest_account` decimal(20,4) DEFAULT '0.0000' COMMENT '最低起投金额',
  `step_account` decimal(20,4) DEFAULT '0.0000' COMMENT '递增金额',
  `most_account` decimal(20,4) DEFAULT '0.0000' COMMENT '最高投资总额',
  `total_copies` int(10) DEFAULT NULL COMMENT '总份数',
  `copy_account` decimal(20,4) DEFAULT NULL COMMENT '每份金额',
  `lowest_copies` int(10) DEFAULT NULL COMMENT '最低起投份数',
  `most_copies` int(10) DEFAULT NULL COMMENT '最高投资份数',
  `risk_level` char(2) DEFAULT '1' COMMENT '产品风险等级（默认为1，极低风险）',
  `red_envelope_rate` decimal(20,4) DEFAULT '0.0000' COMMENT ' 红包可用最大比例',
  `red_envelope_useful` char(1) DEFAULT '0' COMMENT '红包可用标识: 1 红包可用  0 红包不可用',
  `additional_rate_useful` char(1) DEFAULT '1' COMMENT '加息劵可用标识:1可用 0不可用',
  `project_type_id` varchar(32) DEFAULT NULL COMMENT '理财分类:对应前台的理财产品分类，上架时候选择',
  `protocol_id` varchar(32) DEFAULT NULL COMMENT '产品协议ID',
  `status` char(2) DEFAULT NULL COMMENT '状态: 0 新增 1发布待审 2发布审核成功(待售) 3发布审核失败 4 募集中 5 满额待审 6 复审成功 7 复审失败 8 待还   9还款成功（已完成）',
  `is_vouch` char(1) DEFAULT NULL COMMENT '是否担保( 1 担保，0 不担保;默认0）',
  `vouch_id` varchar(32) DEFAULT NULL COMMENT '担保公司ID',
  `borrow_flag` char(1) DEFAULT NULL COMMENT '借款项目标识(1是，借款项目， 0否，即是理财产品）  ',
  `realize_flag` char(1) DEFAULT '0' COMMENT '变现借款项目识( 1是产品变现后发布借款，0 普通借款，默认0)',
  `sale_channel` varchar(10) DEFAULT '1,2,3' COMMENT '上架渠道( 1 PC  2 APP 3 微信 ；多选使用拼接,默认全选：1,2,3)',
  `specific_sale` varchar(2) DEFAULT '0' COMMENT '定向销售方式(0 不定向 1 定向密码 2 定向等级 3 定向邮箱域名，默认0)',
  `specific_sale_rule` varchar(64) DEFAULT NULL COMMENT '定向销售规则（对应规则内容,如：具体定向密码、定向邮箱域名）',
  `repay_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '预还本息',
  `repay_yes_account` decimal(20,4) DEFAULT '0.0000' COMMENT '实还本金',
  `repay_yes_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '实还利息',
  `vouch_status` char(1) DEFAULT '0' COMMENT '担保审核状态(0 待审核 1 审核成功 2 审核失败)',
  `interest_time` varchar(8) DEFAULT NULL COMMENT '交易登记日时间分界点(T+N计息，最长格式HH:MM:SS，暂到小时HH)',
  `vouch_verify_time` datetime DEFAULT NULL COMMENT '担保审核时间',
  `total_period` int(11) DEFAULT '0' COMMENT '项目总期数',
  `repayed_period` int(11) DEFAULT '0' COMMENT '项目已还期数',
  `next_repay_time` datetime DEFAULT NULL COMMENT '本期回款时间',
  `last_repay_time` datetime DEFAULT NULL COMMENT '最后一期回款时间',
  `real_last_repay_time` datetime DEFAULT NULL COMMENT '最后一期实际回款时间',
  `redenvelope_rule_id` varchar(32) DEFAULT NULL COMMENT '红包规则ID',
  `rate_coupon_rule_id` varchar(32) DEFAULT NULL COMMENT '加息券规则ID',
  `stage` int(1) DEFAULT NULL COMMENT '项目投资阶段：1 在投；2 待售；3 满标(或无剩余可投) ;4 还款中或已还款',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_project_project_no` (`project_no`) USING BTREE,
  KEY `idx_project_status` (`status`) USING BTREE,
  KEY `idx_project_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目(理财产品、借款项目表公用信息)';

-- ----------------------------
-- Records of project
-- ----------------------------

-- ----------------------------
-- Table structure for `project_collection`
-- ----------------------------
DROP TABLE IF EXISTS `project_collection`;
CREATE TABLE `project_collection` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `repay_time` datetime DEFAULT NULL COMMENT '预计还款时间',
  `last_repay_time` datetime DEFAULT NULL COMMENT '已经还款时间',
  `status` char(1) DEFAULT '0' COMMENT '还款状态  0未还款 1已还款',
  `payment` decimal(20,4) DEFAULT '0.0000' COMMENT '预还金额',
  `repayed_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '已还金额',
  `capital` decimal(20,4) DEFAULT '0.0000' COMMENT '本金',
  `interest` decimal(20,4) DEFAULT '0.0000' COMMENT '利息',
  `raise_interest` decimal(20,4) DEFAULT NULL COMMENT '加息利息',
  `late_days` int(11) DEFAULT '0' COMMENT '逾期天数',
  `late_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '逾期利息',
  `merchant_late_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '给平台总逾期利息',
  `manage_fee` decimal(20,4) DEFAULT '0.0000' COMMENT '利息管理费',
  `collection_type` char(1) DEFAULT '0' COMMENT '待收类型  0普通待收;1转让人待收 2 受让人待收 3 已作废 ',
  `period` int(8) DEFAULT NULL COMMENT '期数',
  `bond_capital` decimal(20,4) DEFAULT NULL COMMENT '已成功转出本金',
  `bond_interest` decimal(20,4) DEFAULT NULL COMMENT '已成功转出利息',
  `add_ip` varchar(32) DEFAULT '' COMMENT '添加ip',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '待收记录父ID',
  `freeze_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '冻结变现利息',
  `freeze_capital` decimal(20,4) DEFAULT '0.0000' COMMENT '冻结变现本金',
  `periods` int(8) DEFAULT NULL COMMENT '总期数',
  PRIMARY KEY (`uuid`),
  KEY `idx_project_collection_project_id` (`project_id`) USING BTREE,
  KEY `idx_project_collection_user_id` (`user_id`) USING BTREE,
  KEY `idx_project_collection_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款待收表';

-- ----------------------------
-- Records of project_collection
-- ----------------------------

-- ----------------------------
-- Table structure for `project_invest`
-- ----------------------------
DROP TABLE IF EXISTS `project_invest`;
CREATE TABLE `project_invest` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `status` char(1) DEFAULT '0' COMMENT '投资状态 0投资待处理，1成功，2失败',
  `money` decimal(20,4) DEFAULT '0.0000' COMMENT '投资金额',
  `amount` decimal(20,4) DEFAULT '0.0000' COMMENT '有效投资金额',
  `real_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '用户真实资金',
  `payment` decimal(20,4) DEFAULT '0.0000' COMMENT '预收本息',
  `interest` decimal(20,4) DEFAULT '0.0000' COMMENT '预期利息',
  `repayed_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '已还金额',
  `repayed_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '已还利息',
  `wait_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '待还总额',
  `wait_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '待还利息',
  `invest_channel` char(1) DEFAULT NULL COMMENT '投资渠道 1 PC  2 APP 3 微信 ',
  `invest_type` char(1) DEFAULT '0' COMMENT '投资方式  0手动投资;1自动投资',
  `invest_order_no` varchar(32) DEFAULT NULL COMMENT '投资时订单号',
  `freeze_no` varchar(32) DEFAULT NULL COMMENT '投资冻结流水号',
  `user_first_invest` char(1) DEFAULT '0' COMMENT '用户首次投资，0：非首投，1：首投',
  `realize_flag` char(1) DEFAULT '0' COMMENT '变现标识',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT 'ip地址',
  `invest_no` varchar(32) DEFAULT NULL COMMENT '投资流水号',
  `invest_date` char(8) DEFAULT NULL COMMENT '投资日期(UFX)',
  `origin_invest_id` varchar(32) DEFAULT NULL COMMENT '原始投资ID',
  `bond_flag` char(1) DEFAULT '0' COMMENT '债权发布标识0否1是，默认0',
  `invest_style` char(1) DEFAULT '0' COMMENT '投资方式 0通投资,1转让投资,2受让投资，默认0',
  `realize_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '已变现本息',
  `wait_raise_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '待还加息',
  `realize_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '已变现利息',
  `freeze_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '已冻结变现利息',
  `freeze_capital` decimal(20,4) DEFAULT '0.0000' COMMENT '已冻结变现本金',
  `interest_date` date DEFAULT NULL COMMENT '起息日',
  `end_date` date DEFAULT NULL COMMENT '结束日',
  `raise_interest` decimal(20,4) DEFAULT NULL COMMENT '加息利息（元）',
  `realize_id` varchar(32) DEFAULT NULL COMMENT '首次变现ID',
  PRIMARY KEY (`uuid`),
  KEY `idx_project_invest_project_id` (`project_id`) USING BTREE,
  KEY `idx_project_invest_user_id` (`user_id`) USING BTREE,
  KEY `idx_project_invest_invest_order_no` (`invest_order_no`) USING BTREE,
  KEY `idx_project_invest_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资记录';

-- ----------------------------
-- Records of project_invest
-- ----------------------------

-- ----------------------------
-- Table structure for `project_invest_bespeak`
-- ----------------------------
DROP TABLE IF EXISTS `project_invest_bespeak`;
CREATE TABLE `project_invest_bespeak` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '投资预约人id',
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `sale_time` datetime NOT NULL COMMENT '项目开售时间',
  `remind_status` char(1) DEFAULT '0' COMMENT '提醒状态 0未提醒 1已经提醒',
  `remind_time` datetime DEFAULT NULL COMMENT '提醒时间',
  `create_time` datetime DEFAULT NULL COMMENT '预约时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_project_invest_bespeak` (`user_id`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资预约表';

-- ----------------------------
-- Records of project_invest_bespeak
-- ----------------------------

-- ----------------------------
-- Table structure for `project_repayment`
-- ----------------------------
DROP TABLE IF EXISTS `project_repayment`;
CREATE TABLE `project_repayment` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `repay_time` datetime DEFAULT NULL COMMENT '预计还款时间',
  `real_repay_time` datetime DEFAULT NULL COMMENT '实际还款时间',
  `payment` decimal(20,4) DEFAULT '0.0000' COMMENT '预还金额',
  `payed_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '已还金额',
  `capital` decimal(20,4) DEFAULT '0.0000' COMMENT '本金',
  `interest` decimal(20,4) DEFAULT '0.0000' COMMENT '利息',
  `status` char(1) DEFAULT '0' COMMENT '还款状态  0未还；1已还 ',
  `repay_type` char(1) DEFAULT NULL COMMENT '还款类型（1正常还款 2 垫付  3 已还垫付4 逾期还款）',
  `period` int(8) DEFAULT NULL COMMENT '期数',
  `late_days` int(6) DEFAULT '0' COMMENT '逾期天数',
  `late_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '逾期利息',
  `merchant_late_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '给平台逾期利息',
  `repay_user_id` varchar(32) DEFAULT NULL COMMENT '实际还款者',
  `repay_trade_no` varchar(32) DEFAULT NULL COMMENT '第三方还款流水号',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `periods` int(8) DEFAULT '0' COMMENT '总期数',
  PRIMARY KEY (`uuid`),
  KEY `idx_project_repayment_project_id` (`project_id`) USING BTREE,
  KEY `idx_project_repayment_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款还款记录表';

-- ----------------------------
-- Records of project_repayment
-- ----------------------------

-- ----------------------------
-- Table structure for `project_type`
-- ----------------------------
DROP TABLE IF EXISTS `project_type`;
CREATE TABLE `project_type` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  nid varchar(32)  default null comment '类型标识',
  `type_name` varchar(40) DEFAULT NULL COMMENT '分类名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父类ID',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `protocol_id` varchar(32) DEFAULT NULL COMMENT '协议模板标识',
  `features` varchar(15) DEFAULT NULL COMMENT '特点',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `type_level` int(1) DEFAULT NULL COMMENT '层级(根目录为0层，最大2）',
  `liquid` char(1) DEFAULT '0' COMMENT '是否活期，0否1是，默认0',
  `has_child` char(1) DEFAULT '0' COMMENT '有无子分类(1有，0无，默认0）',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品分类';

-- ----------------------------
-- Records of project_type
-- ----------------------------
INSERT INTO `project_type` VALUES ('7ca31c421ce34e3fb8d57208e42f409f', '001','理财频道', null, '1', '1707304646c6453b874aabbb581c99ea', '理财频道', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `project_verify_log`
-- ----------------------------
DROP TABLE IF EXISTS `project_verify_log`;
CREATE TABLE `project_verify_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) NOT NULL COMMENT '项目ID',
  `verify_name` varchar(32) NOT NULL COMMENT '处理人',
  `process_node` varchar(32) DEFAULT NULL COMMENT '处理环节',
  `process_result` varchar(64) DEFAULT NULL COMMENT '处理结果',
  `create_time` datetime DEFAULT NULL COMMENT '处理时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`),
  KEY `idx_project_verify_log_project_id` (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目审核记录表';

-- ----------------------------
-- Records of project_verify_log
-- ----------------------------

-- ----------------------------
-- Table structure for `protocol`
-- ----------------------------
DROP TABLE IF EXISTS `protocol`;
CREATE TABLE `protocol` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `protocol_type` varchar(128) DEFAULT NULL COMMENT '模板类型（注册用户协议、借款协议、转让协议等）',
  `name` varchar(128) DEFAULT '' COMMENT '名称',
  `content` text COMMENT '协议内容',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `status` char(1) DEFAULT '1' COMMENT '1有效,0失效',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='协议模板';

-- ----------------------------
-- Records of protocol
-- ----------------------------
INSERT INTO `protocol` VALUES ('13c0fd39aa4d4de99936a1ceea6fdb38', 'bond_invest_protocol', '债权转让协议', '<html>\r\n<head>\r\n<meta http-equiv=Content-Type content=\"text/html; charset=utf-8\" />\r\n<title></title>\r\n</head>\r\n<body >\r\n<p style=\"text-align: center; border: none; padding: 0px 0px 1px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\"><strong><span style=\"font-stretch: normal; line-height: normal;\"></span>债权转让协议</strong><br/></span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">本《债权转让协议》（下称“本协议”）由以下双方签订：<span>&nbsp;</span></span>\r\n</p>\r\n<p style=\"text-align: left; margin-top: 10px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">转让人/原债权人：&nbsp;<a href=\"http:\"><span style=\"font-family:宋体;font-size:18px;\">___<#if bondUserName?exists>${bondUserName!\"\"}</#if>________________</span></a></span>\r\n</p>\r\n<p style=\"text-align: left; margin-top: 10px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">身份证号码（组织机构代码）：&nbsp;<a href=\"http:\"><span style=\"font-family:宋体;font-size:18px;\">________<#if bondUserCardNo?exists>${bondUserCardNo!\"\"}</#if>___________</span></a></span>\r\n</p>\r\n<p style=\"text-align: left; margin-top: 10px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">受让人/新债权人：<span style=\"text-decoration:underline;\"><span style=\"color: rgb(51, 122, 183);\"><#if bondInvestUserName?exists>${bondInvestUserName!\"\"}</#if></span></span></span>\r\n</p>\r\n<p style=\"text-align: left; margin-top: 10px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">身份证号码（组织机构代码）：&nbsp;<a href=\"http:\"><span style=\"font-family:宋体;font-size:18px;\">_____<#if bondInvestUserCardNo?exists>${bondInvestUserCardNo!\"\"}</#if>______________</span></a></span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">就转让人通过<span><#if web_company?exists>${web_company!\"\"}</#if></span>公司运营管理的互联网金融资产交易平台（域名为<span><#if web_url?exists>${web_url!\"\"}</#if></span>）（以下简称“互金平台”）向受让人转让债权事宜，双方依照平等、自愿的原则，达成如下协议：</span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">1.&nbsp;债权转让</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">转让人同意将其以下债权（“【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”）通过互金平台转让给受让人，受让人同意受让该债。债权基本信息如下：</span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"text-align: left; margin-left: 67px;\">\r\n    <tbody>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border: 1px inset black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">借款人姓名</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width: 318px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-style: inset inset inset none;border-top-color: black;border-right-color: black;border-bottom-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if borrowUserName?exists>${borrowUserName!\"\"}</#if>&nbsp;</span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">借款人身份证号码（组织机构代码）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if borrowUserCardNo?exists>${borrowUserCardNo!\"\"}</#if></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">初始借款金额（元）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if borrowInitMoney?exists>${borrowInitMoney!\"\"}</#if></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">本次转让债权金额（元）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if bondInvestMoney?exists>${bondInvestMoney!\"\"}</#if></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">计息起始日</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">\r\n                        <#if interestStartDate?exists>${interestStartDate!\"\"}</#if>\r\n                        ~\r\n                        <#if interestEndDate?exists>${interestEndDate!\"\"}</#if>\r\n                    </span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">借款期限（天）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if remainDay?exists>${remainDay!\"\"}</#if></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">折溢价率</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">\r\n                        <#if bondApr?exists>\r\n                            ${bondApr!\"\"}\r\n                        </#if>%\r\n                    </span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">债权年利率</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if borrowApr?exists>${borrowApr!\"\"}</#if>%</span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <tr>\r\n            <td width=\"180\" style=\"width: 180px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-right-color: black;border-bottom-color: black;border-left-color: black;padding: 1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\">还款方式</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"318\" style=\"width:318px;border-top:none;border-left:none;border-bottom:inset black 1px;border-right:inset black 1px;padding:1px 1px 1px 10px\">\r\n                <p>\r\n                    <span style=\"font-family:宋体;font-size:18px\"><#if repayStyle?exists>${repayStyle!\"\"}</#if></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n    </tbody>\r\n</table>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">2.债权转让流程</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">2.1&nbsp;受让人在互金平台上，按照互金平台设置的规则，对转让人的债权（“【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”）转让需求点击支付按钮后，本协议即刻生效。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">2.2&nbsp;受让人对“【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”转让需求点击支付按钮后即不可撤销地授权<span><#if web_company?exists>${web_company!\"\"}</#if></span>公司委托其合作的第三方托管账户在其资金账户中，将等同于本协议的第一条所列的本次转让债权金额划至转让人名下的第三方托管账户中。上述划转完成即视为受让人完成支付义务，“【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”已转让成功。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">2.3&nbsp;起息日为受让人支付义务完成日当天。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">3、效力</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">自”【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”转让成功之日起，受让人即成为”【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”的新债权人，承继转让人与”【<span><#if bondName?exists>${bondName!\"\"}</#if></span>】”借款人签订的相应《借款协议》项下出借人的权利并承担出借人的义务。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">4、陈述与保证</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">4.1&nbsp;转让人作出如下陈述和保证：</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>1</span>）转让人拥有完全的民事权利能力和民事行为能力签署并履行本协议；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>2</span>）据转让人合理所知，至转让生效日原始个人借款所载的一切条款及条件均得到适当的遵守及履行；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>3</span>）本协议所设定的转让将会依本协议的规定构成对其合法、有效且有约束力的义务，并可依据本协议的规定予以执行；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>4</span>）未订立或允许订立任何协议（本协议除外），以出售、转让或其他方式处置全部或部分借款债权；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>5</span>）除根据本协议设定的转让外，借款债权上不存在为任何第三人的利益设定的任何优先权益；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>6</span>）在转让生效日，不存在可能会导致转让人不能履行其在本协议项下的义务的诉讼、仲裁或行政程序；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>7</span>）本协议的各条款均是其真实意思的表示，对其具有法律约束力。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">4.2受让人作出如下陈述和保证：</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>1</span>）受让人拥有完全的民事权利能力和民事行为能力签署并履行本协议；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>2</span>）受让人通过互金平台向出让人支付的转让价款的资金来源均合法；</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">（<span>3</span>）本协议的各条款均是其真实意思的表示，对其具有法律约束力。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">5、其他</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">5.1&nbsp;本协议的任何修改、补充均须以互金平台电子文本形式作出。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">5.2&nbsp;双方均确认本协议的签订、生效和履行以不违反中华人民共和国的法律法规为前提。如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无效，但该无效条款并不影响本协议其他条款的效力。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">5.3&nbsp;如果双方在本协议履行过程中发生任何争议，应友好协商解决；如协商不成，则需提交转让人所在地人民法院进行诉讼。</span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">6、还款计划</span>\r\n</p>\r\n<p>\r\n    <br/>\r\n</p>\r\n<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"text-align: left; margin-left: 28px;\">\r\n    <tbody>\r\n        <tr>\r\n            <td width=\"74\" valign=\"top\" style=\"width: 74px;border: 1px inset;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">期数</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"94\" valign=\"top\" style=\"width: 94px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-style: inset inset inset none;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">还款日</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"123\" valign=\"top\" style=\"width: 123px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-style: inset inset inset none;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">应还本息（元）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"75\" valign=\"top\" style=\"width: 75px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-style: inset inset inset none;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">应还本金（元）</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"136\" valign=\"top\" style=\"width: 136px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-style: inset inset inset none;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">应还利息（元）</span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        <#if repayList?exists && repayList?size != 0 >\r\n        <#list repayList as item>\r\n        <tr>\r\n            <td width=\"74\" valign=\"top\" style=\"width: 74px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">${item.period!\"\"}</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"94\" valign=\"top\" style=\"width:94px;border-top:none;border-left:none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">${item.startTime!\"\"}</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"123\" valign=\"top\" style=\"width:123px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">${item.payment!\"\"}</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"75\" valign=\"top\" style=\"width:75px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">${item.capital!\"\"}</span>\r\n                </p>\r\n            </td>\r\n            <td width=\"136\" valign=\"top\" style=\"width:136px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\">&nbsp;${item.interest!\"\"} </span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        </#list>\r\n        <#else>\r\n            <tr>\r\n            <td width=\"74\" valign=\"top\" style=\"width: 74px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-style: none inset inset;border-color: initial;padding: 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n                </p>\r\n            </td>\r\n            <td width=\"94\" valign=\"top\" style=\"width:94px;border-top:none;border-left:none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n                </p>\r\n            </td>\r\n            <td width=\"123\" valign=\"top\" style=\"width:123px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n                </p>\r\n            </td>\r\n            <td width=\"75\" valign=\"top\" style=\"width:75px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n                </p>\r\n            </td>\r\n            <td width=\"136\" valign=\"top\" style=\"width:136px;border-top:none;border-left:   none;border-bottom:inset 1px;border-right:inset 1px;padding:0 7px 0 7px\">\r\n                <p style=\"line-height: 20px\">\r\n                    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n                </p>\r\n            </td>\r\n        </tr>\r\n        </#if>\r\n    </tbody>\r\n</table>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\"></span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; text-indent: 7px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">本协议解释权归<span><#if web_company?exists>${web_company!\"\"}</#if></span>公司所有。</span>\r\n</p>\r\n<p style=\"text-align: left; margin-bottom: 12px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; margin-right: 32px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;转让人签订日&nbsp;<#if date?exists>${date!\"\"}</#if></span>\r\n</p>\r\n<p style=\"text-align: left; margin-top: 10px; text-indent: 7px; line-height: 20px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">受让人签订日<span> &nbsp;<#if date?exists>${date!\"\"}</#if></span></span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; margin: 20px 0px; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left; line-height: 19px;\">\r\n    <span style=\"font-family:宋体;font-size:18px\"><a name=\"_msocom_1\" href=\"http://\"><span style=\"font-family:宋体;font-size:18px;font-family:宋体;font-size:18px;font-family:宋体;font-size:18px;font-family:宋体\"></span></a>&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: left;\">\r\n    <span style=\"font-family:宋体;font-size:18px\">&nbsp;</span>\r\n</p>\r\n<p style=\"text-align: center;\">\r\n    <br/>\r\n</p>\r\n</body>\r\n</html>', '2016-11-03 09:42:23', '1');
INSERT INTO `protocol` VALUES ('1707304646c6453b874aabbb581c99ea', 'invest_protocol', '产品投资协议', '<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title></title><p style=\"margin-top:auto;margin-bottom: auto;text-align:center\"><span style=\"font-family: 宋体;\"><strong><span style=\"font-size:24px\">${protocolTypeName!}</span> </strong></span></p><p style=\"margin-bottom:16px;text-align:center\"><span style=\";font-family:宋体\">?</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">出借人：</span><span style=\";line-height:150%;font-family:\'&\">?<a href=\"http:\" style=\"color: rgb(35, 82, 124); outline: 0px; line-height: 24px; text-indent: 32px; white-space: normal; background-color: rgb(255, 255, 255);\"><span style=\"text-decoration: underline;\"><span style=\"font-family: 宋体;\">? ? ?${investUserRealName!} ? ? ?</span></span></a></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">身份证号码（组织机构代码）：</span><span style=\";line-height:150%;font-family:\'&\">?<a href=\"http://v3.1manage.rdjry.com/main.html\" style=\"color: rgb(35, 82, 124); font-family: \'&\'; line-height: 24px; text-indent: 32px; white-space: normal; outline: 0px; background-color: rgb(255, 255, 255);\"><span style=\"text-decoration: underline;\"><span style=\"font-family: 宋体;\">? ? </span></span></a><a href=\"http:\" style=\"font-family: \'&\'; line-height: 24px; text-indent: 32px; white-space: normal; background-color: rgb(255, 255, 255);\">${investUserCardInfo!}</a> ? ? ?</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">借款人：</span><a href=\"http://\"><span style=\"text-decoration:underline;\"><span style=\";line-height:150%;font-family:宋体\"><span>? ? ?${projectUserRealName!} ? ? ? ? ? ? ?</span></span></span></a><span style=\"text-decoration:underline;\"></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">身份证号码（组织机构代码）：</span><span style=\";line-height:150%;font-family:\'&\">?<a href=\"http://\">_____${projectUserCardInfo!}______________</a></span><span><span></span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">?</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">重要提示：</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">经${webCompany!}公司运营管理的互联网金融资产交易平台（域名为${webName!}</span><span style=\";line-height:150%;font-family:宋体\">）（以下简称“互金平台”）网上撮合，出借人同意向借款人提供借款。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">互金平台为出借人及借款人提供信息发布、合同订立与登记等居间服务。第三方托管平台为出借人及借款人提供在互金平台网站绑定账户间的交易资金划转服务，出借人、借款人在网站绑定账户的安全性须自行管理及承担。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">借款人在互金平台上所发布的借款信息，通过互金平台的合作担保机构审核并进行担保，由该担保机构为借款人的还款作保证。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4.</span><span style=\";line-height:150%;font-family:宋体\">出借人及借款人应确保在第三方托管平台及互金平台登记的联系方式是正确且通畅的，互金平台向各方登记的联系方式发送通知及信息的，视为通知到达。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">5.</span><span style=\";line-height:150%;font-family:宋体\">协议各方应按照法律规定自行处理税收问题。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">一、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">定义</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">除缔约方另有约定之外，本协议相关术语定义如下</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">:</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（一）出借人：本协议所称出借人为符合中华人民共和国法律规定的具有完全民事权利能力和民事行为能力，独立行使和承担本协议项下权利义务的自然人或企业法人。出借人为互金平台网站注册会员。</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（二）借款人：本协议所称借款人为符合中华人民共和国法律规定的具有完全民事权利能力和民事行为能力，独立行使和承担本协议项下权利义务的自然人或企业法人。</span><span style=\";line-height:150%;font-family:宋体\"> </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（三）账户：本协议所指的出借人、借款人账户专指本协议各方与互金平台网站注册会员名绑定的第三方托管平台账户，该第三方托管平台账户的开户姓名、身份证件号码与其绑定的互金平台网站会员名注册时使用的姓名及身份证件号码完全一致。</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（四）募集期：是指借款人在互金平台网站发布借款申请时所设定的筹资期限。出借人只能在该期限内承诺出借，同时将资金划至借款人的账户。该期限以互金平台网站发布的信息为准。</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（五）本协议订立与登记：本协议经出借人和借款人通过互金平台网站以网络在线点击确认的方式订立。出借人或借款人使用互金平台账户登录后，根据互金平台网站的相关规则以其互金平台账户</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">ID</span><span style=\";line-height:150%;font-family:宋体\">在 互金平台网站通过点击确认或类似方式签署的电子协议即视为本人真实意愿并以本人名义签署的协议，具有法律效力。互金平台向出借人和借款人提供电子 协议的备案、查看、核对服务，如对电子协议真伪或电子协议的内容有任何疑问，可以在互金平台网站核对。如对此有任何争议，应以互金平台记录的协议为准。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">二、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">借款基本信息</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（一）借款基本信息</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">出借人在本协议项下的借款本金为${projectMoney!}</span><span><span>?</span></span><span style=\";line-height:150%;font-family:宋体\">元人民币</span><span style=\";line-height:150%;font-family:宋体\"> </span></p><p></p><p><br/></p><table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-left: 28px;border: none\"><tbody><tr><td width=\"102\" valign=\"top\" style=\"width: 102px;border-width: 1px;border-color: windowtext;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\"font-family:宋体\">投资人用户名</span>\n ? ? ? ? ? ? ? ?</p></td><td width=\"123\" valign=\"top\" style=\"width: 123px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-top-color: windowtext;border-right-color: windowtext;border-bottom-color: windowtext;border-left-style: none;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\";line-height:150%;font-family:宋体\">投资人真实姓名</span>\n ? ? ? ? ? ? ? ?</p></td><td width=\"88\" valign=\"top\" style=\"width: 88px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-top-color: windowtext;border-right-color: windowtext;border-bottom-color: windowtext;border-left-style: none;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\";line-height:150%;font-family:宋体\">投资金额</span>\n ? ? ? ? ? ? ? ?</p></td><td width=\"129\" valign=\"top\" style=\"width: 129px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-top-color: windowtext;border-right-color: windowtext;border-bottom-color: windowtext;border-left-style: none;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\";line-height:150%;font-family:宋体\">投资时间</span>\n ? ? ? ? ? ? ? ?</p></td><td width=\"98\" valign=\"top\" style=\"width: 98px;border-top-width: 1px;border-right-width: 1px;border-bottom-width: 1px;border-top-color: windowtext;border-right-color: windowtext;border-bottom-color: windowtext;border-left-style: none;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\";line-height:150%;font-family:宋体\">待收金额</span>\n ? ? ? ? ? ? ? ?</p></td></tr><tr><td width=\"102\" valign=\"top\" style=\"width: 102px;border-right-width: 1px;border-bottom-width: 1px;border-left-width: 1px;border-right-color: windowtext;border-bottom-color: windowtext;border-left-color: windowtext;border-top-style: none;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\">${investUserUserName!}</p></td><td width=\"123\" valign=\"top\" style=\"width: 123px;border-top-style: none;border-left-style: none;border-bottom-width: 1px;border-bottom-color: windowtext;border-right-width: 1px;border-right-color: windowtext;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\">${investUserRealName!}<br/>\n ? ? ? ? ? ? ? ?</p></td><td width=\"88\" valign=\"top\" style=\"width: 88px;border-top-style: none;border-left-style: none;border-bottom-width: 1px;border-bottom-color: windowtext;border-right-width: 1px;border-right-color: windowtext;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\"font-family:宋体\">${investMoney!}</span>\n ? ? ? ? ? ? ? ?</p></td><td width=\"129\" valign=\"top\" style=\"width: 129px;border-top-style: none;border-left-style: none;border-bottom-width: 1px;border-bottom-color: windowtext;border-right-width: 1px;border-right-color: windowtext;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\">${investTime!}<br/>\n ? ? ? ? ? ? ? ?</p></td><td width=\"98\" valign=\"top\" style=\"width: 98px;border-top-style: none;border-left-style: none;border-bottom-width: 1px;border-bottom-color: windowtext;border-right-width: 1px;border-right-color: windowtext;padding: 0 7px\"><p class=\"MsoListParagraph\" style=\"text-indent:0;line-height:150%\"><span style=\"font-family:宋体\">${collectionMoney}</span>\n ? ? ? ? ? ? ? ?</p></td></tr></tbody></table><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">注：</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">起息日是指出借人放款成功之日起第</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">2</span><span style=\";line-height:150%;font-family:宋体\">日。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">出借人通过互金平台以网络在线点击确认的方式接受本协议后，即不可撤销地授权互金平台将金额等同于本金金额的资金由出借人互金平台用户名项下账户划转至借款人互金平台用户名项下账户中，划转完毕即视为借款发放成功。借款发放成功之日即为借款放款日。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">三、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">各方权利义务</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（一）出借人的权利义务</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">保证其为互金平台网站注册会员并在本协议有效期内保持会员身份，并承诺提供的信息真实有效，出借资金来源合法。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">在募集期中，自愿登录互金平台网站进行投资并完成本协议订立与登记，将借款资金划入借款人的账户，同意此资金未划入借款人账户前不计利息。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">借款总金额</span><a href=\"http://\"><span style=\";line-height:150%;font-family:宋体\"><span style=\"font-family: 宋体; line-height: 24px; text-indent: 32px; white-space: normal;\">${projectMoney!}</span></span></a><span style=\";line-height:150%;font-family:宋体\">元</span><span style=\";line-height:150%;font-family:宋体\">，</span><span style=\";line-height:150%;font-family:宋体\">同意委托合作的第三方托管平台按约将出借人账户的出借资金划至借款人账户。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4.</span><span style=\";line-height:150%;font-family:宋体\">同意委托合作的第三方托管平台直接从借款人的还款资金中划扣服务费，具体金额的计算方式以《互金平台网站资费说明》为准。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">5.</span><span style=\";line-height:150%;font-family:宋体\">在借款人出现坏账时，由互金平台合作的担保机构垫付借款本息后，出借人在本协议项下的所有权利视为已经得到满足和实现，出借人不得再对借款人提出任何请求或主张，出借人在本协议下所享的全部权利和主张，包括但不限于对借款本息等所享有权利和主张，均无条件转给互金平台合作的担保机构享有。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">6.</span><span style=\";line-height:150%;font-family:宋体\">应保证其账户状态正常，确保资金划入、划出交易的完成。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">（二）</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">借款人的权利义务</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">借款人应根据互金平台的要求如实向出借人、互金平台提供个人情况（包括但不限于姓名、身份证号、学历、联系方式、联系地址、职业信息、联系人信息等）或企业情况（包括但不限于公司名称、注册地区、办公地点、法人代表、联系方式等）以及借款用途等相关信息。借款人承诺提供的信息真实有效，不得隐瞒任何影响或可能影响其资信能力的事项。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">借款人承诺：如发生任何影响或可能影响借款人经济状况、信用状况、还款能力的事由，包括但不限于借款人的工作单位、职位、工作地点、薪酬等事项的变化，借款人应于前述变更发生之日起</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">2</span><span style=\";line-height:150%;font-family:宋体\">个工作日内通知互金平台。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">借款人承诺根据本协议列明的借款用途使用借款资金，并保证不挪用借款资金或将借款资金用于以下目的和用途：<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(1)?</span><span style=\";line-height:150%;font-family:宋体\">以任何形式进入证券市场，或用于股本权益性投资；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(2)?</span><span style=\";line-height:150%;font-family:宋体\">用于国家明令禁止或限制的经营活动。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4.</span><span style=\";line-height:150%;font-family:宋体\">按约及时登录互金平台网站进行还款，将足额的本息与管理费存入借款人账户，并同意委托互金平台指定的第三方托管平台按约将对应本息划至各出借人账户。 </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">5.</span><span style=\";line-height:150%;font-family:宋体\">应保证其账户状态正常，确保资金划入、划出交易的完成。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">四、违约责任</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">借款人未按协议约定日期及金额归还借款的，且逾期</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">30</span><span style=\";line-height:150%;font-family:宋体\">天，互金平台有权将借款人逾期还款的事宜在网站上公布。 </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">本协议项下的还款逾期超过</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">30</span><span style=\";line-height:150%;font-family:宋体\">天，或在逾期后出现逃避、拒绝沟通或拒不承认欠款等恶意行为的，互金平台有权将借款人违约失信的相关信息向媒体、用人单位、公安机关、检察机关等披露。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">发生下列任何一项或几项情形的，视为借款人违约：<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(1)?</span><span style=\";line-height:150%;font-family:宋体\">借款人违反其在本协议所做的任何承诺和保证的；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(2)</span><span style=\";line-height:150%;font-family:宋体\">借款人的任何财产遭受没收、征用、查封、扣押、冻结等可能影响其履约能力的不利事件，且不能及时提供有效补救措施的；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(3)?</span><span style=\";line-height:150%;font-family:宋体\">借款人的财务状况出现影响其履约能力的不利变化，且不能及时提供有效补救措施的。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4.</span><span style=\";line-height:150%;font-family:宋体\">若借款人违约或根据互金平台、出借人合理判断借款人可能发生违约事件的，出借人（委托互金平台）有权采取下列任何一项或几项救济措施：<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(1)?</span><span style=\";line-height:150%;font-family:宋体\">宣布已发放借款全部提前到期，借款人应立即偿还所有应付款项；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(2)?</span><span style=\";line-height:150%;font-family:宋体\">解除本协议；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(3)?</span><span style=\";line-height:150%;font-family:宋体\">采取法律、法规以及本协议约定的其他救济措施。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">五、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">合同生效条款</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">本协议为附条件生效协议，生效条件为：本协议在募集期内达到借款人本次借款募集金额，且出借人、借款人均登录互金平台网站订立与登记《个人借款协议》，并保存在互金平台后，本协议生效。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">不符合前款条件的，视为出借人投资失败，本《个人借款协议》不生效，出借人的投资款项退回出借人账户。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">六、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">保密条款</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">本协议签署后</span><span style=\";line-height: 150%;font-family:\'Times New Roman\',\'serif\'\">,?</span><span style=\";line-height:150%;font-family:宋体\">除非事先得到另两方的书面同意</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">,?</span><span style=\";line-height:150%;font-family:宋体\">本协议各方均应承担以下保密义务：<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(1)?</span><span style=\";line-height:150%;font-family:宋体\">任何一方不得向非本协议方（互金平台除外）披露本协议以及本协议项下的事宜以及与此等事宜有关的任何文件、资料或信息（“保密信息”）；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(2)?</span><span style=\";line-height:150%;font-family:宋体\">任何一方只能将保密信息和其内容用于本协议项下的目的</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">,?</span><span style=\";line-height:150%;font-family:宋体\">不得用于任何其他目的。 </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方因下列原因披露保密信息，不受前款的限制：<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(1)</span><span style=\";line-height:150%;font-family:宋体\">向本协议各方的董事、监事、高级管理人员和雇员及其聘请的会计师、律师、咨询公司披露；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(2)?</span><span style=\";line-height:150%;font-family:宋体\">因遵循可适用的法律、法规的强制性规定而披露；<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">(3)?</span><span style=\";line-height:150%;font-family:宋体\">依据其他应遵守的法规向审批机构或权力机构进行的披露。 </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">借款人和出借人提供给互金平台的信息及借款人和出借人享受互金平台服务产生的信息（包括本协议签署之前提供和产生的），可由互金平台共享，法律禁止的除外。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4?.</span><span style=\";line-height:150%;font-family:宋体\">本协议任何一方应采取所有其他必要、适当和可以采取的措施，以确保保密信息的保密性。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">5?.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方应促使其向之披露保密信息的人严格遵守本条约定。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">6.</span><span style=\";line-height:150%;font-family:宋体\">各方在本条项下的权利和义务应在本协议终止或到期后继续有效。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">七、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">各方约定的其他事项</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">各方授予互金平台向合作第三方托管机构及其他机构查询并获取其真实身份及合同项下交易信息的权利。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">借款人对含本协议在内的借款总额项下的所有协议均予以承认并履行。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">3.</span><span style=\";line-height:150%;font-family:宋体\">借款人须确保一次性足额偿还含本协议在内的借款总额项下所有协议项下的借款本息，否则不视为还款，由此引起的法律后果及违约责任由借款人连带承担。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">4.</span><span style=\";line-height:150%;font-family:宋体\">借款人对于含本协议在内的借款总额项下本息偿还的还款操作（包括但不限于正常还款、逾期还款等）须登录互金平台网站</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">,</span><span style=\";line-height:150%;font-family:宋体\">通过各自在互金平台网站绑定账户向出借人账户划转款项来完成</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">,</span><span style=\";line-height:150%;font-family:宋体\">否则不视为还款</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">,?</span><span style=\";line-height:150%;font-family:宋体\">由此引起的法律后果及违约责任由借款人连带承担。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">5.</span><span style=\";line-height:150%;font-family:宋体\">本协议项下的收款、还款等数据信息均以互金平台网站生成并公布的内容为准。出借人、借款人可以随时登录互金平台网站查询还款</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">/</span><span style=\";line-height:150%;font-family:宋体\">收款信息。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">6.</span><span style=\";line-height:150%;font-family:宋体\">协议各方应对其他方提供的信息及本协议内容保密，未经其他方同意，任何一方不得向协议主体之外的任何人披露，但法律、行政法规另有强制性规定，或监管、审计等有权机关另有强制性要求的除外。 </span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">7.</span><span style=\";line-height:150%;font-family:宋体\">协议各方知悉并遵守《互金平台公司网站服务协议》、《互金平台网站资费说明》的规定，并接受互金平台网站对前述文本的修订。<span>? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">8.</span> <span style=\";line-height:150%;font-family:宋体\">协议各方根据本协议定义的步骤完成协议订立与登记后，即具有与手写签名同等的法律效力；各方同意，因履行本协议有争议的，以互金平台保留的合同等文件版本及网站解释为准。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">9.</span> <span style=\";line-height:150%;font-family:宋体\">出借人及借款人如因其账户发生销户、换卡、挂失和司法冻结等情况时，为保证业务正常开展，应自行重新绑定其它正常账户。如未及时绑定账户造成资金无法出入账的，须及时通知并申请互金平台协助完成后续处理，并承担由此产生的不利后果及相应责任。<span>? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">10.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方承诺，各方不会利用互金平台网站进行信用卡套现、洗钱、非法集资或其他不正当交易行为，否则应依法独立承担法律责任。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">11.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方确认，借款人和出借人授权和委托互金平台根据本协议所采取的全部行动和措施的法律后果均归属于借款人和出借人本人；在任何情形下，互金平台不是本协议项下任何借款或债务的债务人或需要以其自有资金偿还本协议项下的任何借款或债务。<span>? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">12.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方确认并同意，委托互金平台对本协议项下的任何金额进行计算；在无明显错误的情况下，互金平台对本协议项下任何金额的任何证明或确定，应作为该金额有关事项的终局证明。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">八、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">通知</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">1.</span><span style=\";line-height:150%;font-family:宋体\">本协议任何一方根据本协议约定做出的通知和</span><span style=\";line-height:150%;font-family:\'Times New Roman\',\'serif\'\">/</span><span style=\";line-height:150%;font-family:宋体\">或文件均应以书面形式（包括但不限于电子邮件等方式）做出，或者委托互金平台通过指定渠道进行公布。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:\'&\">2.</span><span style=\";line-height:150%;font-family:宋体\">本协议各方之间的书面通知或文件往来，必须发送至本协议列出的各方的联系人。如以专人送递，在交付后即被视为送达；如以快递或挂号信方式发送的，在快递或挂号信寄出后三日即被视为送达；如以电子邮件发送的，在电子邮件发送之日即被视为送达。若本协议任何一方更改其联系人或联系地址或电子邮件邮箱地址，应尽快按本条约定在相关信息变更之日起三日内书面通知其他各方。<span>?? </span></span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">九、</span><span style=\";line-height:150%;font-family:\'&\">?</span><span style=\";line-height:150%;font-family:宋体\">法律的适用和争议的解决</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">本协议受中华人民共和国法律管辖并按中华人民共和国法律解释。协议履行中发生争议，可由各方协商解决，协商不成，向借款人所在地人民法院起诉。与互金平台有关的争议，向互金平台所在地人民法院起诉。</span><span style=\";line-height:150%;font-family:\'&\">??</span></p><p style=\"margin-top: 10px;text-indent: 32px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">?</span></p><p style=\"margin-right: 32px;line-height: 150%\"><span style=\";line-height: 150%;font-family:\'&\">? </span><span style=\";line-height:150%;font-family:宋体\">出借人签订日</span>? ${date!}</p><p style=\"margin-top: 10px;text-indent: 21px;line-height: 150%\"><span style=\";line-height:150%;font-family:宋体\">借款人签订日 ?${date!}</span></p><p style=\"margin-top: 10px;margin-left: 72px;line-height: 150%\"><span style=\";line-height: 150%;font-family:\'&\">? ?</span></p>', '2016-11-05 14:16:36', '1');
INSERT INTO `protocol` VALUES ('25694e70172b4b2a802e96c4b549e271', 'register_protocol', '用户平台注册协议', '<h3>1.	本协议的签署和修订</h3>\r\n<p>1.1.	本协议签署双方为${web_name}与您（即在${web_name}注册的个人和企业用户），您在${web_name}包括APP和微信内进行之一切活动均须遵守本协议，且应保证该交易活动不违背诚实信用原则，不侵害公共秩序和善良风俗。本服务协议具有合同效力。本协议内容包括协议正文及所有已经发布的或将来可能发布的各类规则。所有规则为协议不可分割的一部分，与协议正文具有同等法律效力。</p> \r\n<p>1.2.	${web_name}用户应当为年满18周岁以上且具有完全民事行为能力和民事权利能力的自然人或系中国境内注册成立的具有独立法人资格的组织。如您违反前述注册限制注册了${web_name}账户（包括您的资金托管账户，如有，下同）并使用了${web_name}服务的，${web_name}有权随时中止或终止您的用户资格，并要求您的监护人承担相应的责任。</p>\r\n<p>1.3.	在您注册成为用户时，您已经阅读、理解并接受本协议正文的全部条款及平台各项规则，并承诺阅读、理解并接受在您注册成为${web_name}用户后${web_name}不时发布的平台规则；您同意遵守中国的各类法律、法规及规范性文件的规定，如有违反而导致任何法律后果的发生，您应以自己的名义独立承担所有相应的法律责任。</p>\r\n<p>1.4.	平台方有权根据需要不时地修改本协议正文各条款或制定、修改平台规则并在${web_name}相关系统板块发布，无需另行单独通知。您应不时地注意本协议正文各条款及平台规则的变更，若您在本协议正文相关条款和/或平台规则内容公告变更后继续使用${web_name}服务的，表示您已充分阅读、理解并接受修改后的内容，也将遵循修改后的本协议正文条款和/或平台规则使用平台的服务。若您不同意修改后的协议内容，您应停止使用${web_name}的服务。</p>\r\n<h3>2.	定义</h3>\r\n<p>2.1.	用户及用户注册：用户必须是具备完全民事行为能力的自然人，或者是具有合法经营资格的实体组织。无民事行为能力人、限制民事行为能力人以及无经营或特定经营资格的组织不应当注册为${web_name}用户或超过其民事权利或行为能力范围从事信息发布、交易的，其与${web_name}之间的服务协议自始无效，${web_name}一经发现，有权立即注销该用户，并追究其使用${web_name}\"服务\"的一切法律责任。用户注册是指用户登录${web_name}，并按要求填写相关信息并确认同意履行相关用户协议的过程。用户因进行信息发布、交易、获取有偿服务或接触${web_name}服务器而发生的所有应纳税赋，以及一切硬件、软件、服务及其它方面的费用均由用户负责支付。</p>\r\n<p>2.2.	会员：在${web_name}上注册的用户成为${web_name}会员。${web_name}作为有借款需求客户（企业、个人）和资金提供方（借出者）的信息撮合平台以及获取各类与投融资相关的服务信息的地点。${web_name}会对客户和机构的身份信息的真实性进行审核但不能完全控制双方交易所涉及的服务的质量、安全或合法性，信息的真实性或准确性，以及交易方履行其在贸易协议项下的各项义务的能力。${web_name}并不作为接受服务方或是提供服务方的身份参与交易行为的本身。${web_name}提醒用户应该通过自己的谨慎判断确定服务及相关信息的真实性、合法性和有效性。</p>\r\n<h3>3.	收费</h3>\r\n<p>3.1.	注册会员使用${web_name}的服务，${web_name}有权根据相关协议、规则、公示、公告等文件收取服务费用。注册会员承诺按照相关约定和规则向${web_name}支付服务费用，并同意${web_name}有权自其相关账户直接划扣服务费用。服务费用的收取时间、类型、比例、金额等内容见具体协议和相关规则。注册会员同意，${web_name}有权单方面调整前述服务费用的类型、金额和收取时间等内容，并根据相关协议和规则予以公示。</p>\r\n<p>3.2.	会员在使用${web_name}服务过程中可能需要向第三方（如银行或第三方支付和结算机构等）支付一定的费用，具体收费标准详见第三方网站相关页面，或${web_name}的提示。</p>\r\n<p>3.3.	会员欠缴${web_name}相关费用的，${web_name}可视情况暂停受理或者办理其相关业务。</p>\r\n<h3>4.	注册义务</h3>\r\n您必须按照申请注册会员的表格，真实、准确、完整的填写您的资料；维持并及时更新资料，使其保持真实、准确、完整和反应当前情况。倘若您提供的资料不真实、准确和完整或我公司有合理理由怀疑该资料不真实、准确、完整的，我公司有权暂停或终止您的注册身份，并拒绝您在目前和将来对服务以任何形式使用。</p>\r\n<h3>5.	您的权利和义务</h3>\r\n<p>5.1.	您有权利拥有在${web_name}的用户名及登录密码，并有权利使用自己的用户名及密码随时登录${web_name}。您不得以任何形式擅自转让或授权他人使用自己的${web_name}用户名及密码；</p>\r\n<p>5.2.	您有权根据本服务协议的规定以及${web_name}上发布的相关规则利用${web_name}上信息发布平台查询服务信息、提交融资信息、参加${web_name}的有关活动以及有权享受${web_name}提供的其它的有关服务；</p>\r\n<p>5.3.	您在${web_name}上交易过程中如与其他融资机构因交易产生纠纷，可以请求${web_name}从中予以协调。您如发现其他机构有违法或违反本服务协议的行为，可以向${web_name}进行反映和要求处理。如您因网上交易与其他用户产生诉讼的，您有权通过司法部门要求${web_name}提供相关资料；</p>\r\n<p>5.4.	您同意遵守${web_name}的交易规则，接收来自${web_name}或${web_name}合作伙伴发出的邮件或信息；</p>\r\n<p>5.5.	您应当保证在使用${web_name}进行信息发布过程中遵守诚实信用的原则，不得在发布的信息中包含有反动、暴力、淫秽、毒品、赌博、教唆及有损社会公共道德的内容；不得发布各类违法或违规信息；</p>\r\n<p>5.6.	您不应在${web_name}上恶意评价其他用户，或采取不正当手段提高自身的信用度或降低其他用户的信用度；</p>\r\n<p>5.7.	您在${web_name}上不得发布包含有国家禁止或限制发布包含有侵犯他人知识产权或其它合法权益的机密类信息，也不得发布违背社会公共利益或公共道德的、或是${web_name}认为不适合在${web_name}上发布的信息。</p>\r\n<p>5.8.	您承诺自己在使用${web_name}时实施的所有行为均遵守国家法律、法规和${web_name}的相关规定以及各种社会公共利益或公共道德。如有违反导致任何法律后果的发生，您独立承担所有相应的法律责任；</p>\r\n<p>5.9.	您同意不对${web_name}上任何数据作商业性利用，包括但不限于在未经${web_name}事先书面批准的情况下，以复制、传播等方式使用在${web_name}上展示的任何资料。</p>\r\n<h3>6.	${web_name}的权利义务</h3>\r\n<p>6.1.	${web_name}有义务在现有技术上维护整个网上平台的正常运行，并努力提升和改进技术。对与您在注册使用${web_name}中所遇到的与信息发布或注册有关的问题及反应的情况，${web_name}应及时作出回复；</p>\r\n<p>6.2.	对于您在${web_name}上的不当行为或其它任何${web_name}认为应当终止服务的情况，${web_name}有权随时作出删除相关信息、终止服务提供等处理，而无须征得您的同意；</p>\r\n<p>6.3.	因网上平台的特殊性，${web_name}没有义务对所有会员的注册资料、行为及其他事项进行事先审查；</p>\r\n<p>6.4.	您在${web_name}进行交易过程中如与其它投资者产生纠纷，请求${web_name}从中予以调处，经${web_name}审核后，${web_name}有权通过电子邮件联系向纠纷双方了解情况，并将所了解的情况通过电子邮件互相通知对方；</p>\r\n<p>6.5.	您因在${web_name}上进行借款而与其他借出者产生诉讼的，您通过司法部门或行政部门依照法定程序要求${web_name}提供相关数据，${web_name}应积极配合并提供有关资料；</p>\r\n<p>6.6.	${web_name}有权对您的注册数据及信息发布行为进行查阅，发现注册数据或信息发布 行为中存在任何问题或怀疑，均有权向您发出询问及要求改正的通知或者直接作出删除等处理；</p>\r\n<p>6.7.	经国家生效法律文书或行政处罚决定确认您存在违法行为，或者${web_name}有足够事实依据可以认定您存在违法或违反服务协议行为的，${web_name}有权在${web_name}及所在网站上以网络发布形式公布您的违法行为；</p>\r\n6.8.	在您使用${web_name}服务时，${web_name}有权接收并记录您的个人信息，包括但不限于IP地址、网站Cookie中的资料及您要求取用的网页记录等。</p>\r\n<h3>7.	许可使用权</h3>\r\n<p>您授予本公司永久的、免费的、完整的许可使用权利（并且有权对该权利进行再授权），使我公司有权使用、复制、修改、发布、翻译、分发您的资料或制作其派生作品，以已知或日后开发的任何形式、媒体或技术，将您的资料纳入其他作品里。</p>\r\n<h3>8.	隐私权</h3>\r\n<p>我公司将对您的个人信息和资料（包括您主动提交的和公司搜集、记录的所有数据、信息、资料和相应的交易行为记录、交易文件，下同）承担保密义务，不将您的信息向第三方进行出租或出售；但您在此同意并授权，公司可以直接将您的个人信息和资料用于如下用途而无需额外取得您的同意和/或授权。</p>\r\n<p>8.1.	进行用户身份识别、资料核查和验证，以确保在${web_name}进行的交易的安全性和防范违法犯罪活动。</p>\r\n<p>8.2.	进行内部归类、模型建设和分析等内部使用，以改进公司对您及其他用户提供的服务。</p>\r\n<p>8.3.	将您必要的个人信息和资料（包括但不限于真实姓名、身份证号码、联系方式、信用状况等）披露给通过${web_name}与您交易的其他主体；当您违反本协议、${web_name}规则和/或您通过${web_name}签署的法律文件之约定或任何法律法规规定时，${web_name}有权根据自己的判断和/或有关法律文件的约定，披露您的个人信息和资料以及您的违法、违约行为和对应的交易文件而无需承担任何责任，披露的方式包括但不限于：①在${web_name}及其他网站公布；②向与您交易的主体以及有权的仲裁机构和司法、行政等权力机关提供；③向受公司或与您交易之其他${web_name}用户委托的专业法律顾问、催收机构等服务提供；④其他披露方式。</p>\r\n<p>8.4.	根据有关法律法规要求向有权的仲裁机构或司法、行政、立法等权力机关提供您的个人资料和信息</p>\r\n<p>8.5.	提供给依法设立的征信机构和个人信用数据库，以供有关单位和个人依法查询和使用。</p>\r\n<p>8.6.	提供给公司的关联方、合作方以用于完成${web_name}对您提供的服务（包括但不限于对您的个人资料和信息进行核实、对您的信用情况进行评估等）。</p>\r\n<p>8.7.	向您推荐公司提供的商品、服务或推广活动，以及与公司的关联方、合作方共享，以向您提供或推荐公司的关联方、合作伙伴的商品或服务或推广活动。您不可撤销地同意公司、公司的关联方及合作方通过站内信、电子邮件、电话、短信等方式向您提供、发送服务状态的通知、营销活动及其他商业性信息。</p>\r\n<p>8.8.	当您使用或申请使用公司的关联方、合作方提供的服务或与其或拟与其进行交易时，将您的个人信息和资料共享与该等公司关联方、合作方，使其可以对您的个人信息和资料进行核实、分析；</p>\r\n<p>8.9.	您授权的其他情形。</p>\r\n<h3>9.	免责声明</h3>\r\n<p>您将对您发布的信息及其他在${web_name}上发生的任何行为承担责任，我公司对此不负任何责任。</p>\r\n<h3>10.	不可抗力</h3>\r\n<p>对于因${web_name}合理控制范围以外的原因，包括但不限于自然灾害、罢工或骚乱、物质短缺或定量配给、暴动、战争行为、政府行为、通讯或其他设施故障或严重伤亡事故等，致使${web_name}延迟或未能履约的，${web_name}不对您承担任何责任。</p>\r\n<h3>11.	管辖</h3>\r\n<p>因本协议或本公司服务所引起的或与其有关的任何争议，应向当地人民法院提起诉讼并适用中华人民共和国法律。</p>\r\n\r\n', '2016-08-15 15:04:34', '1');

-- ----------------------------
-- Table structure for `protocol_log`
-- ----------------------------
DROP TABLE IF EXISTS `protocol_log`;
CREATE TABLE `protocol_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `protocol_id` varchar(32) NOT NULL COMMENT '协议模板id',
  `protocol_type` varchar(128) NOT NULL COMMENT '模板类型（注册用户协议、借款协议、转让协议等）',
  `protocol_name` varchar(128) NOT NULL COMMENT '协议名称',
  `business_id` varchar(32) DEFAULT NULL COMMENT '业务id，如投资，债权投资等id',
  `file_path` varchar(128) DEFAULT NULL COMMENT '生成的合同地址',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='协议记录表';

-- ----------------------------
-- Records of protocol_log
-- ----------------------------

-- ----------------------------
-- Table structure for `rate_coupon_rule`
-- ----------------------------
DROP TABLE IF EXISTS `rate_coupon_rule`;
CREATE TABLE `rate_coupon_rule` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `activity_code` varchar(30) DEFAULT NULL COMMENT '活动方案唯一标识',
  `rule_name` varchar(20) DEFAULT NULL COMMENT '加息券规则名称',
  `status` char(1) DEFAULT NULL COMMENT '状态： 0禁用  1启用',
  `total_num` int(11) DEFAULT NULL COMMENT '发放加息券的总数，当发放方式为固定值时,值为规则明细之和的倍数',
  `lssue_num` int(11) DEFAULT NULL COMMENT '已经发放数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改日期',
  `delete_flag` char(1) DEFAULT NULL COMMENT '逻辑删除标识 0正常  1删除',
  `grant_type` char(1) DEFAULT NULL COMMENT '加息券发放方式：1 固定金额、2金额满返',
  `grant_url` varchar(512) DEFAULT NULL COMMENT '发放的url',
  `grant_start_time` datetime DEFAULT NULL COMMENT '发放开始时间',
  `grant_end_time` datetime DEFAULT NULL COMMENT '发放结束时间',
  `use_project_type` text COMMENT '项目分类id值，多个项目分类以英文,分割 ',
  `use_valid_day` varchar(11) DEFAULT NULL COMMENT '有效天数',
  `use_expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `grant_project_type` text COMMENT '发放项目类别id，多个用逗号分隔',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='加息券规则设置表';

-- ----------------------------
-- Records of rate_coupon_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `rate_coupon_rule_detail`
-- ----------------------------
DROP TABLE IF EXISTS `rate_coupon_rule_detail`;
CREATE TABLE `rate_coupon_rule_detail` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `rule_id` varchar(32) NOT NULL COMMENT '加息券规则id值',
  `up_apr` decimal(20,4) DEFAULT NULL COMMENT '发放加息券的固定值',
  `use_tender_money` decimal(20,4) DEFAULT NULL COMMENT '最低投资金额限制',
  `tender_min` decimal(20,4) DEFAULT NULL COMMENT '投资金额开始区间值',
  `tender_max` decimal(20,4) DEFAULT NULL COMMENT '投资金额结束区间值',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='加息券规则明细表';

-- ----------------------------
-- Records of rate_coupon_rule_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `rd_token_store`
-- ----------------------------
DROP TABLE IF EXISTS `rd_token_store`;
CREATE TABLE `rd_token_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `add_time` datetime DEFAULT NULL,
  `binding_id` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `expires_in` varchar(255) DEFAULT NULL,
  `oauth_token` varchar(255) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端令牌';

-- ----------------------------
-- Records of rd_token_store
-- ----------------------------

-- ----------------------------
-- Table structure for `realize`
-- ----------------------------
DROP TABLE IF EXISTS `realize`;
CREATE TABLE `realize` (
  `project_id` varchar(32) NOT NULL COMMENT '项目ID',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `old_project_id` varchar(32) DEFAULT NULL COMMENT '变现项目ID',
  `original_project_id` varchar(32) DEFAULT NULL COMMENT '变现项目ID',
  `deadline` int(4) DEFAULT NULL COMMENT '变现期限(单位，小时)',
  `rule_id` varchar(32) DEFAULT NULL COMMENT '变现规则ID',
  `realize_amount` decimal(20,4) DEFAULT '0.0000' COMMENT '对应原产品本息',
  `realize_interest` decimal(20,4) DEFAULT '0.0000' COMMENT '对应原产品利息',
  PRIMARY KEY (`project_id`),
  KEY `idx_realize_project_id` (`project_id`) USING BTREE,
  KEY `idx_realize_project_id_invest_id` (`project_id`,`invest_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变现记录表';

-- ----------------------------
-- Records of realize
-- ----------------------------

-- ----------------------------
-- Table structure for `realize_freeze`
-- ----------------------------
DROP TABLE IF EXISTS `realize_freeze`;
CREATE TABLE `realize_freeze` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `realize_id` varchar(32) NOT NULL COMMENT '变现产品ID',
  `collection_id` varchar(32) NOT NULL COMMENT '被变现待收ID',
  `money` decimal(20,4) NOT NULL DEFAULT '0.0000' COMMENT '冻结金额',
  `freeze_type` varchar(32) NOT NULL COMMENT '冻结类型',
  `status` char(2) DEFAULT '0' COMMENT '状态 0未冻结1已冻结2已解冻3已撤销',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号',
  `freeze_no` varchar(32) DEFAULT NULL COMMENT '冻结流水号',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`),
  KEY `idx_realize_freeze_collection_id_status` (`collection_id`,`status`) USING BTREE,
  KEY `idx_realize_freeze_realize_id_status` (`realize_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变现冻结信息表';

-- ----------------------------
-- Records of realize_freeze
-- ----------------------------

-- ----------------------------
-- Table structure for `realize_rule`
-- ----------------------------
DROP TABLE IF EXISTS `realize_rule`;
CREATE TABLE `realize_rule` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `rule_name` varchar(32) DEFAULT NULL COMMENT '规则名称',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `rule_status` char(1) DEFAULT NULL COMMENT '1 开启 0未开启  默认1,未开启不允许发布变现',
  `hold_days` int(4) DEFAULT '0' COMMENT '起息持有天数  默认 0 不开启此规则',
  `remain_days` int(4) DEFAULT '0' COMMENT '还款到期剩余天数,默认0 不开启此规则',
  `period_remain_days` int(4) DEFAULT NULL COMMENT '本期还款剩余天数 0 不开启此规则',
  `sell_style` char(1) DEFAULT '0' COMMENT '变现金额方式:0 部分变现;1全额变现,默认0',
  `sell_amount_min` decimal(20,4) DEFAULT NULL COMMENT '变现最小金额',
  `buy_style` char(1) DEFAULT '0' COMMENT '投资金额方式:0 部分投资;1全额投资,默认0',
  `buy_amount_min` decimal(20,4) DEFAULT NULL COMMENT '单笔投资最小金额',
  `buy_amount_max` decimal(20,4) DEFAULT NULL COMMENT '单笔投资最大金额',
  `realize_rate_min` decimal(6,4) DEFAULT NULL COMMENT '变现利率下限值',
  `realize_rate_max` decimal(6,4) DEFAULT NULL COMMENT '变现利率上限值',
  `overdue_fee_rate` decimal(6,4) DEFAULT NULL COMMENT '逾期罚息率',
  `fee_rate` decimal(6,4) DEFAULT '0.0000' COMMENT '转让金额百分比收取手续费（默认为0，单位是%）',
  `fee_single_max` decimal(20,4) DEFAULT NULL COMMENT '单笔手续费上限值（单位:元）',
  `issue_time` varchar(8) DEFAULT NULL COMMENT '发布时间',
  `raise_end_time` varchar(8) DEFAULT NULL COMMENT '募集结束时间',
  `interest_manage_rate` decimal(8,4) DEFAULT NULL COMMENT '利息管理费',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变现规则配置';

-- ----------------------------
-- Records of realize_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `recharge`
-- ----------------------------
DROP TABLE IF EXISTS `recharge`;
CREATE TABLE `recharge` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号',
  `trade_no` varchar(32) DEFAULT NULL COMMENT 'UFX处理流水号',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `status` char(1) DEFAULT '0' COMMENT '状态 0 充值申请中 1充值成功 2充值失败',
  `card_id` varchar(32) DEFAULT NULL COMMENT '充值银行卡',
  `amount` decimal(20,4) DEFAULT '0.0000' COMMENT '金额',
  `pay_way` char(1) DEFAULT NULL COMMENT '支付方式(0：网银充值 1：代扣充值 2：快捷支付 3：汇款充值 4：企业网银充值 )',
  `fee` decimal(20,4) DEFAULT '0.0000' COMMENT '手续费',
  `fee_type` char(2) NOT NULL COMMENT '手续费类型(01 用户，02商户)',
  `recharge_fee` decimal(20,4) DEFAULT '0.0000' COMMENT '用户承担手续费',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '添加IP',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `channel`  char(1) DEFAULT '1' COMMENT '渠道(1-PC,2-APP,3-微信,默认PC)',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='充值记录表';

-- ----------------------------
-- Records of recharge
-- ----------------------------

-- ----------------------------
-- Table structure for `redenvelope_rule`
-- ----------------------------
DROP TABLE IF EXISTS `redenvelope_rule`;
CREATE TABLE `redenvelope_rule` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `activity_code` varchar(30) DEFAULT NULL COMMENT '活动方案唯一标识',
  `rule_name` varchar(20) DEFAULT NULL COMMENT '红包规则名称',
  `status` char(1) DEFAULT NULL COMMENT '状态： 0禁用  1启用',
  `total_num` int(11) DEFAULT NULL COMMENT '发放红包的总数，当发放方式为固定金额时,值为规则明细之和的倍数',
  `lssue_num` int(11) DEFAULT NULL COMMENT '已经发放数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改日期',
  `delete_flag` char(1) DEFAULT NULL COMMENT '逻辑删除标识 0正常  1删除',
  `grant_type` char(1) DEFAULT NULL COMMENT '红包发放方式：1 固定金额、2固定比例、3金额满返、4比例满返',
  `grant_url` varchar(512) DEFAULT NULL COMMENT '发放的url',
  `grant_start_time` datetime DEFAULT NULL COMMENT '发放开始时间',
  `grant_end_time` datetime DEFAULT NULL COMMENT '发放结束时间',
  `grant_max` decimal(20,4) DEFAULT NULL COMMENT '发放最大金额（发放类型为固定比例、比例满返的时候，设置发放红包最大值）',
  `grant_min` decimal(20,4) DEFAULT NULL COMMENT '发放最小金额（发放类型为固定比例、比例满返的时候，设置发放红包最小值）',
  `grant_rate` decimal(20,4) DEFAULT NULL COMMENT '固定比例（固定比例，红包发放方式为固定比率时设定）',
  `use_project_type` text COMMENT '项目分类id值，多个项目分类以英文,分割 ',
  `use_valid_day` varchar(11) DEFAULT NULL COMMENT '有效天数',
  `use_expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `grant_project_type` text COMMENT '发放项目类别id，多个用逗号分隔',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包规则配置表';

-- ----------------------------
-- Records of redenvelope_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `redenvelope_rule_detail`
-- ----------------------------
DROP TABLE IF EXISTS `redenvelope_rule_detail`;
CREATE TABLE `redenvelope_rule_detail` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `rule_id` varchar(32) NOT NULL COMMENT '红包规则id值',
  `amount` decimal(20,4) DEFAULT NULL COMMENT '发放固定红包金额值',
  `use_tender_money` decimal(20,4) DEFAULT NULL COMMENT '固定金额方式，最低投资金额限制',
  `tender_min` decimal(20,4) DEFAULT NULL COMMENT '投资金额开始区间值',
  `tender_max` decimal(20,4) DEFAULT NULL COMMENT '投资金额结束区间值',
  `grant_rate` decimal(20,4) DEFAULT NULL COMMENT '比例发放的值（固定比例，比例满返）',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='红包规则配置明细表';

-- ----------------------------
-- Records of redenvelope_rule_detail
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_answer`
-- ----------------------------
DROP TABLE IF EXISTS `risk_answer`;
CREATE TABLE `risk_answer` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `question_id` varchar(32) DEFAULT NULL COMMENT '关联问题表ID',
  `answer_no` varchar(20) DEFAULT NULL COMMENT '答案编号',
  `content` varchar(1024) DEFAULT NULL COMMENT '答案内容',
  `score` int(6) DEFAULT '0' COMMENT '答案分值',
  `sort` varchar(10) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='风险评估问卷答案';

-- ----------------------------
-- Records of risk_answer
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_level_config`
-- ----------------------------
DROP TABLE IF EXISTS `risk_level_config`;
CREATE TABLE `risk_level_config` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `risk_level_no` varchar(20) DEFAULT NULL COMMENT '风险等级编号',
  `risk_level_name` varchar(32) DEFAULT NULL COMMENT '产品风险等级名称（默认分为：高风险、中高风险、中等风险、较低风险、低风险）',
  `risk_level_val` int(1) DEFAULT '1' COMMENT '产品风险等级数值（高风险：5；中高风险：4；中等风险：3；较低风险：2；低风险：1；）',
  `risk_level_desc` varchar(512) DEFAULT NULL COMMENT '产品风险等级含义及介绍',
  `user_risk_level_name` varchar(32) DEFAULT NULL COMMENT '用户风险承受能力评估名称',
  `user_risk_level_val` int(1) DEFAULT '0' COMMENT '用户风险承受能力值(0,1,2,3,4)',
  `user_risk_level_desc` varchar(512) DEFAULT NULL COMMENT '用户风险承受能力含义',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `delete_flag` int(1) DEFAULT '0' COMMENT '逻辑删除标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品风险等级和用户风险承受能力对应关系';

-- ----------------------------
-- Records of risk_level_config
-- ----------------------------
INSERT INTO `risk_level_config` VALUES ('0b62b080bdeb4b44b54859d9bb2f77a4', '2016071200005', '高风险', '5', '本金、收益可能有大幅度的波动', '进取型', '4', '可以承受高风险、中高风险、中等风险、较低风险、极低风险的产品', '0', '0', '2016-07-12 14:32:44');
INSERT INTO `risk_level_config` VALUES ('138d7c21fd2c4e72894385dbe791fbaa', '2016071200001', '极低风险', '1', '本金、收益稳定', '保守型', '0', '只能承受极低风险的产品', '0', '0', '2016-07-12 14:26:55');
INSERT INTO `risk_level_config` VALUES ('592490a2e8484b8287ecadb9ac35e643', '2016071200003', '中等风险', '3', '本金、收益可能有一定幅度的波动', '平衡型', '2', '可以承受中等风险、较低风险、极低风险的产品', '0', '0', '2016-07-12 14:30:09');
INSERT INTO `risk_level_config` VALUES ('8a0f572ea5d6477e860866d4ade168d8', '2016071200004', '中高风险', '4', '本金、收益可能有较大幅度的波动', '成长型', '3', '可以承受中高风险、中等风险、较低风险、极低风险的产品', '0', '0', '2016-07-12 14:30:42');
INSERT INTO `risk_level_config` VALUES ('96ac8c459250463ca64178113471d2b1', '2016071200002', '较低风险', '2', '本金相对稳定、收益可能有轻微波动', '稳健型', '1', '可以承受较低风险、极低风险的产品', '0', '0', '2016-07-12 14:28:44');

-- ----------------------------
-- Table structure for `risk_papers`
-- ----------------------------
DROP TABLE IF EXISTS `risk_papers`;
CREATE TABLE `risk_papers` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `papers_no` varchar(20) DEFAULT NULL COMMENT '问卷编号',
  `papers_name` varchar(255) DEFAULT NULL COMMENT '问卷名称',
  `papers_type` int(1) DEFAULT '0' COMMENT '试卷类型（2，答题，1：其他）',
  `question_count` int(10) DEFAULT '0' COMMENT '问题数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT NULL COMMENT '问卷状态值（1:发布中；2：未发布）',
  `delete_flag` int(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='风险评估问卷';

-- ----------------------------
-- Records of risk_papers
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_papers_score`
-- ----------------------------
DROP TABLE IF EXISTS `risk_papers_score`;
CREATE TABLE `risk_papers_score` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `papers_id` varchar(32) DEFAULT NULL COMMENT '关联试卷UUID',
  `risk_id` varchar(32) DEFAULT NULL COMMENT '关联风险等级UUID',
  `start_score` int(6) DEFAULT '0' COMMENT '起始分数0值',
  `end_score` int(6) DEFAULT '0' COMMENT '截止分数值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问卷分数风险等级设置';

-- ----------------------------
-- Records of risk_papers_score
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_question`
-- ----------------------------
DROP TABLE IF EXISTS `risk_question`;
CREATE TABLE `risk_question` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `papers_id` varchar(32) DEFAULT NULL COMMENT '关联问卷表uuid',
  `question_no` varchar(20) DEFAULT NULL COMMENT '问卷问题编号',
  `question_name` varchar(1024) DEFAULT NULL COMMENT '问卷问题内容',
  `sort` int(4) DEFAULT '0' COMMENT '问题序号',
  `score` int(6) DEFAULT '0' COMMENT '问题分值',
  `is_single` int(1) DEFAULT '1' COMMENT '是否单选（1，单选；2，多选，默认单选）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`uuid`),
  KEY `fk_papers_attach_papers_id` (`papers_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='风险评估问卷问题';

-- ----------------------------
-- Records of risk_question
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_user_log`
-- ----------------------------
DROP TABLE IF EXISTS `risk_user_log`;
CREATE TABLE `risk_user_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '关联用户UUID（风险评估人）',
  `papers_id` varchar(32) DEFAULT NULL COMMENT '关联问卷UUID（问卷号）',
  `score` int(6) DEFAULT '0' COMMENT '评估所得分数',
  `risk_level` int(1) DEFAULT '0' COMMENT '评估分数对应风险等级',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(1) DEFAULT '0' COMMENT '评估状态值（0:初始状态值；1：有效状态值）',
  `risk_answers` varchar(1024) DEFAULT NULL COMMENT '用户答题的内容（数组题号和用户选择答案）',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户风险评测记录';

-- ----------------------------
-- Records of risk_user_log
-- ----------------------------

-- ----------------------------
-- Table structure for `score_type`
-- ----------------------------
DROP TABLE IF EXISTS `score_type`;
CREATE TABLE `score_type` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `type_name` varchar(30) DEFAULT NULL COMMENT '积分类型名称',
  `type_code` varchar(32) DEFAULT NULL,
  `grant_type` char(1) DEFAULT NULL COMMENT '发放类型（1：固定值，2：固定比例，3：金额满返 4：比例满返，5：固定递增）',
  `grant_value` int(10) DEFAULT NULL COMMENT '发放积分（发放类型为1的时候填写）',
  `grant_rate` decimal(20,2) DEFAULT NULL COMMENT '发放的比例（发放类型为2,4有效）',
  `grant_min` int(10) DEFAULT NULL COMMENT '发放的最小值',
  `grant_max` int(10) DEFAULT NULL COMMENT '发放的最大值',
  `grant_up` int(10) DEFAULT NULL COMMENT '发放类型为5 固定递增值',
  `remark` varchar(50) DEFAULT NULL COMMENT '规则备注',
  `status` char(1) DEFAULT NULL COMMENT '状态： 0禁用  1启用',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分发放规则表';

-- ----------------------------
-- Records of score_type
-- ----------------------------
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f1', '用户登录', 'score_user_login', '5', null, null, '5', '30', '5', '用户登录', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f2', '邮箱认证', 'score_email', '1', '50', null, null, null, null, '邮箱认证', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f3', '手机认证', 'score_phone', '1', '50', null, null, null, null, '手机认证', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f4', '实名认证', 'score_realname', '1', '100', null, null, null, null, '实名认证', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f5', '投资成功', 'score_invest_success', '2', null, '0.01', null, null, null, '投资成功', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f6', '用户注册', 'score_user_register', '1', '100', null, null, null, null, '用户注册', '1');
INSERT INTO `score_type` VALUES ('1764f3e6d315454ab070568f55c992f7', '债权投资成功', 'score_bond_invest_success', '2', null, '0.01', null, null, null, '债权投资成功', '1');

-- ----------------------------
-- Table structure for `section`
-- ----------------------------
DROP TABLE IF EXISTS `section`;
CREATE TABLE `section` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `section_name` varchar(50) DEFAULT NULL COMMENT '栏目名称',
  `section_code` varchar(20) DEFAULT NULL COMMENT '栏目标识',
  `section_type` char(1) DEFAULT NULL COMMENT '栏目类型，1：列表，2：单页',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上一级栏目ID',
  `delete_flag` char(1) DEFAULT NULL COMMENT '状态 0 未删除，1 已删除',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL,
  `pic_number` int(4) DEFAULT NULL COMMENT '图片个数',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目';

-- ----------------------------
-- Records of section
-- ----------------------------
INSERT INTO `section` VALUES ('1764f3e6d315454ab070568f55c992f2', '合作伙伴', 'partner', '3', 'fc294a1064c647ea820164aa4523d047', '0', '30001', '合作伙伴', '2016-08-04 14:51:36', '8');
INSERT INTO `section` VALUES ('1764f3e6d315454ab070568f55c992f3', '动态资讯', 'news', null, 'fc294a1064c647ea820164aa4523d047', '0', '30009', '动态资讯', '2016-08-04 14:50:35', '3');
INSERT INTO `section` VALUES ('1764f3e6d315454ab070568f55c992f4', '网站公告', 'notice', null, 'fc294a1064c647ea820164aa4523d047', '0', '30002', '网站公告', '2016-07-27 15:29:17', '3');
INSERT INTO `section` VALUES ('1764f3e6d315454ab070568f55c992f5', '滚动图', 'scrollPic', '3', null, '0', '100', '滚动图', '2016-08-04 14:46:57', '3');
INSERT INTO `section` VALUES ('1770737057ec4074abdfecc1405513aa', '帮助中心', 'help', '3', null, '0', '200', '帮助中心', '2016-08-09 18:35:23', null);
INSERT INTO `section` VALUES ('3e6870e33a4e4740a619274520dffa47', '安全保障', 'abc', '1', 'fc294a1064c647ea820164aa4523d047', '0', '30008', '', '2016-11-01 10:43:14', null);
INSERT INTO `section` VALUES ('544b172bf95a4e0b8e1c9ba20aab6952', '充值提现', 'account', '1', '1770737057ec4074abdfecc1405513aa', '0', '20002', '充值提现', '2016-08-09 18:36:36', null);
INSERT INTO `section` VALUES ('5aa5b814214446cbb20814e30cf7f8e7', '注册认证', 'register', '1', '1770737057ec4074abdfecc1405513aa', '0', '20001', '注册认证', '2016-08-09 18:35:59', null);
INSERT INTO `section` VALUES ('73243ceb6ddf4822a7686bae838aa744', '平台简介', 'platformInfo', '1', 'fc294a1064c647ea820164aa4523d047', '0', '30005', '平台简介', '2016-08-10 17:27:14', null);
INSERT INTO `section` VALUES ('b2bd4f7d8be24ff19d668a2ffa3a417a', '资费说明', 'chargeFee', '1', '1770737057ec4074abdfecc1405513aa', '0', '20004', '资费说明', '2016-08-09 18:38:28', null);
INSERT INTO `section` VALUES ('c7a06b999547447c955cb548a1cc385a', '投资收益', 'invest', '1', '1770737057ec4074abdfecc1405513aa', '0', '20003', '投资收益', '2016-08-09 18:37:10', null);
INSERT INTO `section` VALUES ('df693ada499c42b29cc736089f2a3202', '加入我们', 'jionUs', '1', 'fc294a1064c647ea820164aa4523d047', '0', '30007', '加入我们', '2016-08-10 17:28:55', null);
INSERT INTO `section` VALUES ('ecf57b34c1c94f18a4eab002db0d54e4', '联系我们', 'contactUs', '1', 'fc294a1064c647ea820164aa4523d047', '0', '30006', '联系我们', '2016-08-10 17:28:30', null);
INSERT INTO `section` VALUES ('f00a0e1b3b4542329503c4e8cbf7ba98', '法律说明', 'lawInfo', '1', '1770737057ec4074abdfecc1405513aa', '0', '20005', '法律说明', '2016-08-09 18:39:24', null);
INSERT INTO `section` VALUES ('f888af0afbee421db8eeb938591db8fc', '媒体报道', 'media', '1', 'fc294a1064c647ea820164aa4523d047', '0', '30004', '媒体报道', '2016-08-09 10:55:52', null);
INSERT INTO `section` VALUES ('fc294a1064c647ea820164aa4523d047', '关于我们', 'aboutUs', '1', null, '0', '300', '关于我们', '2016-08-09 10:47:15', null);

-- ----------------------------
-- Table structure for `sys_area`
-- ----------------------------
DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `area_code` varchar(6) NOT NULL COMMENT '区域编码',
  `area_name` varchar(20) NOT NULL COMMENT '区域名称',
  `area_level` int(1) DEFAULT NULL COMMENT '层级（根节点 中国 0，省 1 市 2 区、县3）',
  `sort` int(6) DEFAULT NULL COMMENT '排序',
  `parent_code` varchar(6) DEFAULT NULL COMMENT '上一级',
  PRIMARY KEY (`uuid`),
  KEY `ak_uk_sys_area` (`area_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省市区域';

-- ----------------------------
-- Records of sys_area
-- ----------------------------
INSERT INTO `sys_area` VALUES ('7eb2b9db53da11e68586000c293c07d6', '0', '中国', '0', null, null);
INSERT INTO `sys_area` VALUES ('7eb41ed253da11e68586000c293c07d6', '110000', '北京市', null, '1', '0');
INSERT INTO `sys_area` VALUES ('7eb5d46d53da11e68586000c293c07d6', '110100', '东城区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7eb86bd953da11e68586000c293c07d6', '110200', '西城区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7eba6b8753da11e68586000c293c07d6', '110500', '朝阳区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ebc275153da11e68586000c293c07d6', '110600', '丰台区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ebe4e0653da11e68586000c293c07d6', '110700', '石景山区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec0133053da11e68586000c293c07d6', '110800', '海淀区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec1f8e953da11e68586000c293c07d6', '110900', '门头沟区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec3a2dd53da11e68586000c293c07d6', '111100', '房山区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec57dcc53da11e68586000c293c07d6', '111200', '通州区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec7805053da11e68586000c293c07d6', '111300', '顺义区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ec953cd53da11e68586000c293c07d6', '111400', '昌平区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ecb389953da11e68586000c293c07d6', '111500', '大兴区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ecd277753da11e68586000c293c07d6', '111600', '怀柔区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ecf162e53da11e68586000c293c07d6', '111700', '平谷区', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ed0be0d53da11e68586000c293c07d6', '112800', '密云县', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ed2a2a053da11e68586000c293c07d6', '112900', '延庆县', null, null, '110000');
INSERT INTO `sys_area` VALUES ('7ed4839353da11e68586000c293c07d6', '120000', '天津市', null, '2', '0');
INSERT INTO `sys_area` VALUES ('7ed6435353da11e68586000c293c07d6', '120100', '和平区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ed8100153da11e68586000c293c07d6', '120200', '河东区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7eda2a1553da11e68586000c293c07d6', '120300', '河西区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7edc9ef453da11e68586000c293c07d6', '120400', '南开区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ede841c53da11e68586000c293c07d6', '120500', '河北区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee0535453da11e68586000c293c07d6', '120600', '红桥区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee2472953da11e68586000c293c07d6', '120900', '滨海新区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee4021853da11e68586000c293c07d6', '121000', '东丽区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee5c18c53da11e68586000c293c07d6', '121100', '西青区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee7a95553da11e68586000c293c07d6', '121200', '津南区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ee9c54f53da11e68586000c293c07d6', '121300', '北辰区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7eeb991353da11e68586000c293c07d6', '121400', '武清区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7eed6ab753da11e68586000c293c07d6', '121500', '宝坻区', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7eef863253da11e68586000c293c07d6', '122100', '宁河县', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ef142a853da11e68586000c293c07d6', '122300', '静海县', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ef322b853da11e68586000c293c07d6', '122500', '蓟县', null, null, '120000');
INSERT INTO `sys_area` VALUES ('7ef53bff53da11e68586000c293c07d6', '130000', '河北省', null, '3', '0');
INSERT INTO `sys_area` VALUES ('7ef6f49953da11e68586000c293c07d6', '130100', '石家庄市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7ef8bda953da11e68586000c293c07d6', '130101', '市辖区', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7efad6da53da11e68586000c293c07d6', '130102', '长安区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7efcaf2d53da11e68586000c293c07d6', '130103', '桥东区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7efe86d353da11e68586000c293c07d6', '130104', '桥西区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7f006f1b53da11e68586000c293c07d6', '130105', '新华区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7f02695853da11e68586000c293c07d6', '130107', '井陉矿区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7f044f4353da11e68586000c293c07d6', '130108', '裕华区', null, null, '130101');
INSERT INTO `sys_area` VALUES ('7f06153a53da11e68586000c293c07d6', '130121', '井陉县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f07d60653da11e68586000c293c07d6', '130123', '正定县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f09bf3353da11e68586000c293c07d6', '130124', '栾城县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f0b9cb453da11e68586000c293c07d6', '130125', '行唐县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f0d792a53da11e68586000c293c07d6', '130126', '灵寿县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f0f411f53da11e68586000c293c07d6', '130127', '高邑县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f11347753da11e68586000c293c07d6', '130128', '深泽县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f13043353da11e68586000c293c07d6', '130129', '赞皇县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f14c91353da11e68586000c293c07d6', '130130', '无极县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f16c18553da11e68586000c293c07d6', '130131', '平山县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f190a5e53da11e68586000c293c07d6', '130132', '元氏县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f1b259653da11e68586000c293c07d6', '130133', '赵县', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f1cedd653da11e68586000c293c07d6', '130181', '辛集市', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f1ebbe453da11e68586000c293c07d6', '130182', '藁城市', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f20a27453da11e68586000c293c07d6', '130183', '晋州市', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f2261e353da11e68586000c293c07d6', '130184', '新乐市', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f24400d53da11e68586000c293c07d6', '130185', '鹿泉市', null, null, '130100');
INSERT INTO `sys_area` VALUES ('7f26478753da11e68586000c293c07d6', '130200', '唐山市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7f282b3353da11e68586000c293c07d6', '130201', '市辖区', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f29ee0753da11e68586000c293c07d6', '130202', '路南区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f2bccbb53da11e68586000c293c07d6', '130203', '路北区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f2db28053da11e68586000c293c07d6', '130204', '古冶区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f2f89c953da11e68586000c293c07d6', '130205', '开平区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f31303853da11e68586000c293c07d6', '130207', '丰南区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f33081553da11e68586000c293c07d6', '130208', '丰润区', null, null, '130201');
INSERT INTO `sys_area` VALUES ('7f34d4b353da11e68586000c293c07d6', '130223', '滦县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f36e73053da11e68586000c293c07d6', '130224', '滦南县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f38c0ec53da11e68586000c293c07d6', '130225', '乐亭县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f3a924f53da11e68586000c293c07d6', '130227', '迁西县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f3c42b653da11e68586000c293c07d6', '130229', '玉田县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f3e477f53da11e68586000c293c07d6', '130230', '唐海县', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f40489153da11e68586000c293c07d6', '130281', '遵化市', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f422e9b53da11e68586000c293c07d6', '130283', '迁安市', null, null, '130200');
INSERT INTO `sys_area` VALUES ('7f44028453da11e68586000c293c07d6', '130300', '秦皇岛市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7f45c4d953da11e68586000c293c07d6', '130301', '海港区', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f47ba2653da11e68586000c293c07d6', '130303', '山海关区', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f49899a53da11e68586000c293c07d6', '130304', '北戴河区', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f4b60ef53da11e68586000c293c07d6', '130321', '青龙满族自治县', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f4d1f2053da11e68586000c293c07d6', '130322', '昌黎县', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f4efa9053da11e68586000c293c07d6', '130323', '抚宁县', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f51118353da11e68586000c293c07d6', '130324', '卢龙县', null, null, '130300');
INSERT INTO `sys_area` VALUES ('7f52dbd453da11e68586000c293c07d6', '130400', '邯郸市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7f54b3ad53da11e68586000c293c07d6', '130401', '市辖区', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f56a04153da11e68586000c293c07d6', '130402', '邯山区', null, null, '130401');
INSERT INTO `sys_area` VALUES ('7f5891c053da11e68586000c293c07d6', '130403', '丛台区', null, null, '130401');
INSERT INTO `sys_area` VALUES ('7f5a5f1b53da11e68586000c293c07d6', '130404', '复兴区', null, null, '130401');
INSERT INTO `sys_area` VALUES ('7f5c5cc053da11e68586000c293c07d6', '130406', '峰峰矿区', null, null, '130401');
INSERT INTO `sys_area` VALUES ('7f5e14c953da11e68586000c293c07d6', '130421', '邯郸县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f5feecf53da11e68586000c293c07d6', '130423', '临漳县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f61ac2c53da11e68586000c293c07d6', '130424', '成安县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f637dd553da11e68586000c293c07d6', '130425', '大名县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f65512653da11e68586000c293c07d6', '130426', '涉县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f670c6953da11e68586000c293c07d6', '130427', '磁县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f68e9c353da11e68586000c293c07d6', '130428', '肥乡县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f6ac74253da11e68586000c293c07d6', '130429', '永年县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f6c8f5f53da11e68586000c293c07d6', '130430', '邱县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f6e373653da11e68586000c293c07d6', '130431', '鸡泽县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f702f0153da11e68586000c293c07d6', '130432', '广平县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f720cc453da11e68586000c293c07d6', '130433', '馆陶县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f73beca53da11e68586000c293c07d6', '130434', '魏县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f75d5d353da11e68586000c293c07d6', '130435', '曲周县', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f77c1af53da11e68586000c293c07d6', '130481', '武安市', null, null, '130400');
INSERT INTO `sys_area` VALUES ('7f79a3cd53da11e68586000c293c07d6', '130500', '邢台市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7f7b60ec53da11e68586000c293c07d6', '130501', '市辖区', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f7d3fce53da11e68586000c293c07d6', '130502', '桥东区', null, null, '130501');
INSERT INTO `sys_area` VALUES ('7f7f0db953da11e68586000c293c07d6', '130503', '桥西区', null, null, '130501');
INSERT INTO `sys_area` VALUES ('7f81255753da11e68586000c293c07d6', '130521', '邢台县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f82d7d253da11e68586000c293c07d6', '130522', '临城县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f84cd6553da11e68586000c293c07d6', '130523', '内丘县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f86ab9f53da11e68586000c293c07d6', '130524', '柏乡县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f88909553da11e68586000c293c07d6', '130525', '隆尧县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f8a850f53da11e68586000c293c07d6', '130526', '任县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f8c5e6253da11e68586000c293c07d6', '130527', '南和县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f8e227953da11e68586000c293c07d6', '130528', '宁晋县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f90119353da11e68586000c293c07d6', '130529', '巨鹿县', null, null, '130529');
INSERT INTO `sys_area` VALUES ('7f91cb5f53da11e68586000c293c07d6', '130530', '新河县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f93b39753da11e68586000c293c07d6', '130531', '广宗县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f9584f453da11e68586000c293c07d6', '130532', '平乡县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f9745c453da11e68586000c293c07d6', '130533', '威县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f99253f53da11e68586000c293c07d6', '130534', '清河县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f9ae7eb53da11e68586000c293c07d6', '130535', '临西县', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f9c9fd653da11e68586000c293c07d6', '130581', '南宫市', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7f9e6ce153da11e68586000c293c07d6', '130582', '沙河市', null, null, '130500');
INSERT INTO `sys_area` VALUES ('7fa07db953da11e68586000c293c07d6', '130600', '保定市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7fa257b553da11e68586000c293c07d6', '130601', '新市区', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fa41d1d53da11e68586000c293c07d6', '130603', '北市区', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fa5f0be53da11e68586000c293c07d6', '130604', '南市区', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fa8193253da11e68586000c293c07d6', '130621', '满城县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fa9dd5b53da11e68586000c293c07d6', '130622', '清苑县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fabab2553da11e68586000c293c07d6', '130623', '涞水县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fad9cad53da11e68586000c293c07d6', '130624', '阜平县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7faf74c253da11e68586000c293c07d6', '130625', '徐水县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fb1494253da11e68586000c293c07d6', '130626', '定兴县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fb2ff1f53da11e68586000c293c07d6', '130627', '唐县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fb522bb53da11e68586000c293c07d6', '130628', '高阳县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fb70be753da11e68586000c293c07d6', '130629', '容城县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fb8bdb853da11e68586000c293c07d6', '130630', '涞源县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fba849f53da11e68586000c293c07d6', '130631', '望都县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fbc7f2553da11e68586000c293c07d6', '130632', '安新县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fbe570d53da11e68586000c293c07d6', '130633', '易县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fc00af053da11e68586000c293c07d6', '130634', '曲阳县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fc1e5e553da11e68586000c293c07d6', '130635', '蠡县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fc4c24b53da11e68586000c293c07d6', '130636', '顺平县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fc6a46253da11e68586000c293c07d6', '130637', '博野县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fc87c9d53da11e68586000c293c07d6', '130638', '雄县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fca6a1753da11e68586000c293c07d6', '130681', '涿州市', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fcc2e8853da11e68586000c293c07d6', '130682', '定州市', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fce09e453da11e68586000c293c07d6', '130683', '安国市', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fcfc86653da11e68586000c293c07d6', '130684', '高碑店市', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fd1888453da11e68586000c293c07d6', '130685', '白沟新城县', null, null, '130600');
INSERT INTO `sys_area` VALUES ('7fd3688453da11e68586000c293c07d6', '130700', '张家口市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7fd53aa753da11e68586000c293c07d6', '130701', '市辖区', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fd70b3553da11e68586000c293c07d6', '130702', '桥东区', null, null, '130701');
INSERT INTO `sys_area` VALUES ('7fd9018453da11e68586000c293c07d6', '130703', '桥西区', null, null, '130701');
INSERT INTO `sys_area` VALUES ('7fdac78a53da11e68586000c293c07d6', '130705', '宣化区', null, null, '130701');
INSERT INTO `sys_area` VALUES ('7fdcabce53da11e68586000c293c07d6', '130706', '下花园区', null, null, '130701');
INSERT INTO `sys_area` VALUES ('7fde502553da11e68586000c293c07d6', '130721', '宣化县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe0187c53da11e68586000c293c07d6', '130722', '张北县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe207c853da11e68586000c293c07d6', '130723', '康保县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe4238553da11e68586000c293c07d6', '130724', '沽源县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe5e65453da11e68586000c293c07d6', '130725', '尚义县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe7bae453da11e68586000c293c07d6', '130726', '蔚县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fe98a8153da11e68586000c293c07d6', '130727', '阳原县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7feb5d3053da11e68586000c293c07d6', '130728', '怀安县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fed2e0653da11e68586000c293c07d6', '130729', '万全县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7fef017c53da11e68586000c293c07d6', '130730', '怀来县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7ff0def453da11e68586000c293c07d6', '130731', '涿鹿县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7ff2abb053da11e68586000c293c07d6', '130732', '赤城县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7ff47d3a53da11e68586000c293c07d6', '130733', '崇礼县', null, null, '130700');
INSERT INTO `sys_area` VALUES ('7ff667cc53da11e68586000c293c07d6', '130800', '承德市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('7ff8108c53da11e68586000c293c07d6', '130801', '市辖区', null, null, '130800');
INSERT INTO `sys_area` VALUES ('7ff9edf253da11e68586000c293c07d6', '130802', '双桥区', null, null, '130801');
INSERT INTO `sys_area` VALUES ('7ffbc3ac53da11e68586000c293c07d6', '130803', '双滦区', null, null, '130801');
INSERT INTO `sys_area` VALUES ('7ffd78bc53da11e68586000c293c07d6', '130804', '鹰手营子矿区', null, null, '130801');
INSERT INTO `sys_area` VALUES ('7fff29a253da11e68586000c293c07d6', '130821', '承德县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('8000fd9753da11e68586000c293c07d6', '130822', '兴隆县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('8002df1b53da11e68586000c293c07d6', '130823', '平泉县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('8004df0253da11e68586000c293c07d6', '130824', '滦平县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('80069fa753da11e68586000c293c07d6', '130825', '隆化县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('8008748c53da11e68586000c293c07d6', '130826', '丰宁满族自治县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('800a493f53da11e68586000c293c07d6', '130827', '宽城满族自治县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('800c573053da11e68586000c293c07d6', '130828', '围场满族蒙古族自治县', null, null, '130800');
INSERT INTO `sys_area` VALUES ('800e174953da11e68586000c293c07d6', '130900', '沧州市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('80100ac653da11e68586000c293c07d6', '130901', '市辖区', null, null, '130900');
INSERT INTO `sys_area` VALUES ('80120d7953da11e68586000c293c07d6', '130902', '新华区', null, null, '130901');
INSERT INTO `sys_area` VALUES ('8013fadf53da11e68586000c293c07d6', '130903', '运河区', null, null, '130901');
INSERT INTO `sys_area` VALUES ('8015e35853da11e68586000c293c07d6', '130921', '沧县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8017c02953da11e68586000c293c07d6', '130922', '青县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('80199deb53da11e68586000c293c07d6', '130923', '东光县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('801b684e53da11e68586000c293c07d6', '130924', '海兴县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('801d335c53da11e68586000c293c07d6', '130925', '盐山县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('801f0cae53da11e68586000c293c07d6', '130926', '肃宁县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8020f87453da11e68586000c293c07d6', '130927', '南皮县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8022d82253da11e68586000c293c07d6', '130928', '吴桥县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8024eec553da11e68586000c293c07d6', '130929', '献县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8026e18353da11e68586000c293c07d6', '130930', '孟村回族自治县', null, null, '130900');
INSERT INTO `sys_area` VALUES ('8028ced453da11e68586000c293c07d6', '130981', '泊头市', null, null, '130900');
INSERT INTO `sys_area` VALUES ('802a7f7f53da11e68586000c293c07d6', '130982', '任丘市', null, null, '130900');
INSERT INTO `sys_area` VALUES ('802c5c4c53da11e68586000c293c07d6', '130983', '黄骅市', null, null, '130900');
INSERT INTO `sys_area` VALUES ('802e3bac53da11e68586000c293c07d6', '130984', '河间市', null, null, '130900');
INSERT INTO `sys_area` VALUES ('803000ff53da11e68586000c293c07d6', '131000', '廊坊市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('8031cead53da11e68586000c293c07d6', '131001', '市辖区', null, null, '131000');
INSERT INTO `sys_area` VALUES ('8033b49353da11e68586000c293c07d6', '131002', '安次区', null, null, '131001');
INSERT INTO `sys_area` VALUES ('80357e4a53da11e68586000c293c07d6', '131003', '广阳区', null, null, '131001');
INSERT INTO `sys_area` VALUES ('80374c7b53da11e68586000c293c07d6', '131022', '固安县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('8039d79753da11e68586000c293c07d6', '131023', '永清县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('803be6a253da11e68586000c293c07d6', '131024', '香河县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('803dbb9a53da11e68586000c293c07d6', '131025', '大城县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('803f590053da11e68586000c293c07d6', '131026', '文安县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('80413be353da11e68586000c293c07d6', '131028', '大厂回族自治县', null, null, '131000');
INSERT INTO `sys_area` VALUES ('8043461a53da11e68586000c293c07d6', '131081', '霸州市', null, null, '131000');
INSERT INTO `sys_area` VALUES ('804532e153da11e68586000c293c07d6', '131082', '三河市', null, null, '131000');
INSERT INTO `sys_area` VALUES ('8046f0b853da11e68586000c293c07d6', '131100', '衡水市', null, null, '130000');
INSERT INTO `sys_area` VALUES ('8048ebe953da11e68586000c293c07d6', '131101', '市辖区', null, null, '131100');
INSERT INTO `sys_area` VALUES ('804ae57a53da11e68586000c293c07d6', '131102', '桃城区', null, null, '131101');
INSERT INTO `sys_area` VALUES ('804cf9ae53da11e68586000c293c07d6', '131121', '枣强县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('804f094553da11e68586000c293c07d6', '131122', '武邑县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8050cbf153da11e68586000c293c07d6', '131123', '武强县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8052abed53da11e68586000c293c07d6', '131124', '饶阳县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8054885d53da11e68586000c293c07d6', '131125', '安平县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8056405653da11e68586000c293c07d6', '131126', '故城县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8058338c53da11e68586000c293c07d6', '131127', '景县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('805a361e53da11e68586000c293c07d6', '131128', '阜城县', null, null, '131100');
INSERT INTO `sys_area` VALUES ('805c629353da11e68586000c293c07d6', '131181', '冀州市', null, null, '131100');
INSERT INTO `sys_area` VALUES ('805e7ef453da11e68586000c293c07d6', '131182', '深州市', null, null, '131100');
INSERT INTO `sys_area` VALUES ('8060fc7753da11e68586000c293c07d6', '140000', '山西省', null, '4', '0');
INSERT INTO `sys_area` VALUES ('8063a41953da11e68586000c293c07d6', '140100', '太原市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('8065ba8d53da11e68586000c293c07d6', '140101', '市辖区', null, null, '140100');
INSERT INTO `sys_area` VALUES ('8067fb6153da11e68586000c293c07d6', '140105', '小店区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('806a456c53da11e68586000c293c07d6', '140106', '迎泽区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('806c989753da11e68586000c293c07d6', '140107', '杏花岭区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('806ec49353da11e68586000c293c07d6', '140108', '尖草坪区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('8070e1ce53da11e68586000c293c07d6', '140109', '万柏林区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('8073c28c53da11e68586000c293c07d6', '140110', '晋源区', null, null, '140101');
INSERT INTO `sys_area` VALUES ('807609f653da11e68586000c293c07d6', '140121', '清徐县', null, null, '140100');
INSERT INTO `sys_area` VALUES ('80780a3853da11e68586000c293c07d6', '140122', '阳曲县', null, null, '140100');
INSERT INTO `sys_area` VALUES ('807a877653da11e68586000c293c07d6', '140123', '娄烦县', null, null, '140100');
INSERT INTO `sys_area` VALUES ('807c742653da11e68586000c293c07d6', '140181', '古交市', null, null, '140100');
INSERT INTO `sys_area` VALUES ('807e3cec53da11e68586000c293c07d6', '140200', '大同市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80804ffa53da11e68586000c293c07d6', '140201', '市辖区', null, null, '140200');
INSERT INTO `sys_area` VALUES ('80827a8553da11e68586000c293c07d6', '140202', '城区', null, null, '140201');
INSERT INTO `sys_area` VALUES ('80849e5253da11e68586000c293c07d6', '140203', '矿区', null, null, '140201');
INSERT INTO `sys_area` VALUES ('8086972853da11e68586000c293c07d6', '140211', '南郊区', null, null, '140201');
INSERT INTO `sys_area` VALUES ('80884bbf53da11e68586000c293c07d6', '140212', '新荣区', null, null, '140201');
INSERT INTO `sys_area` VALUES ('808a13bc53da11e68586000c293c07d6', '140221', '阳高县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('808c4caf53da11e68586000c293c07d6', '140222', '天镇县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('808ed15a53da11e68586000c293c07d6', '140223', '广灵县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('80907f9753da11e68586000c293c07d6', '140224', '灵丘县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('8092566453da11e68586000c293c07d6', '140225', '浑源县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('809429be53da11e68586000c293c07d6', '140226', '左云县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('8095df3953da11e68586000c293c07d6', '140227', '大同县', null, null, '140200');
INSERT INTO `sys_area` VALUES ('8097956b53da11e68586000c293c07d6', '140300', '阳泉市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('8099ea5653da11e68586000c293c07d6', '140301', '市辖区', null, null, '140300');
INSERT INTO `sys_area` VALUES ('809c3b7953da11e68586000c293c07d6', '140302', '城区', null, null, '140301');
INSERT INTO `sys_area` VALUES ('809e1c2853da11e68586000c293c07d6', '140303', '矿区', null, null, '140301');
INSERT INTO `sys_area` VALUES ('809fc90053da11e68586000c293c07d6', '140311', '郊区', null, null, '140301');
INSERT INTO `sys_area` VALUES ('80a1ea9053da11e68586000c293c07d6', '140321', '平定县', null, null, '140300');
INSERT INTO `sys_area` VALUES ('80a3c1f153da11e68586000c293c07d6', '140322', '盂县', null, null, '140300');
INSERT INTO `sys_area` VALUES ('80a580da53da11e68586000c293c07d6', '140400', '长治市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80a7500953da11e68586000c293c07d6', '140401', '市辖区', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80a9349253da11e68586000c293c07d6', '140402', '城区', null, null, '140401');
INSERT INTO `sys_area` VALUES ('80ab292c53da11e68586000c293c07d6', '140411', '郊区', null, null, '140401');
INSERT INTO `sys_area` VALUES ('80acec9653da11e68586000c293c07d6', '140421', '长治县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80aead3453da11e68586000c293c07d6', '140423', '襄垣县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80b0947c53da11e68586000c293c07d6', '140424', '屯留县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80b2814053da11e68586000c293c07d6', '140425', '平顺县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80b43c8953da11e68586000c293c07d6', '140426', '黎城县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80b64da753da11e68586000c293c07d6', '140427', '壶关县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80b868ee53da11e68586000c293c07d6', '140428', '长子县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80ba8a6f53da11e68586000c293c07d6', '140429', '武乡县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80bc82a553da11e68586000c293c07d6', '140430', '沁县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80be88e953da11e68586000c293c07d6', '140431', '沁源县', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80c0571b53da11e68586000c293c07d6', '140481', '潞城市', null, null, '140400');
INSERT INTO `sys_area` VALUES ('80c2478f53da11e68586000c293c07d6', '140500', '晋城市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80c4320053da11e68586000c293c07d6', '140501', '市辖区', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80c5efe353da11e68586000c293c07d6', '140502', '城区', null, null, '140501');
INSERT INTO `sys_area` VALUES ('80c7e83b53da11e68586000c293c07d6', '140521', '沁水县', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80c9d2f253da11e68586000c293c07d6', '140522', '阳城县', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80cbd79953da11e68586000c293c07d6', '140524', '陵川县', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80cda49c53da11e68586000c293c07d6', '140525', '泽州县', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80cf68f653da11e68586000c293c07d6', '140581', '高平市', null, null, '140500');
INSERT INTO `sys_area` VALUES ('80d1787c53da11e68586000c293c07d6', '140600', '朔州市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80d3922753da11e68586000c293c07d6', '140601', '市辖区', null, null, '140600');
INSERT INTO `sys_area` VALUES ('80d551b653da11e68586000c293c07d6', '140602', '朔城区', null, null, '140601');
INSERT INTO `sys_area` VALUES ('80d71d7b53da11e68586000c293c07d6', '140603', '平鲁区', null, null, '140601');
INSERT INTO `sys_area` VALUES ('80d8eed553da11e68586000c293c07d6', '140621', '山阴县', null, null, '140600');
INSERT INTO `sys_area` VALUES ('80dad28053da11e68586000c293c07d6', '140622', '应县', null, null, '140600');
INSERT INTO `sys_area` VALUES ('80dc8a2053da11e68586000c293c07d6', '140623', '右玉县', null, null, '140600');
INSERT INTO `sys_area` VALUES ('80de6b9d53da11e68586000c293c07d6', '140624', '怀仁县', null, null, '140600');
INSERT INTO `sys_area` VALUES ('80e0500053da11e68586000c293c07d6', '140700', '晋中市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80e200b453da11e68586000c293c07d6', '140701', '市辖区', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80e3f11053da11e68586000c293c07d6', '140702', '榆次区', null, null, '140701');
INSERT INTO `sys_area` VALUES ('80e5b69953da11e68586000c293c07d6', '140721', '榆社县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80e78d8153da11e68586000c293c07d6', '140722', '左权县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80e9454053da11e68586000c293c07d6', '140723', '和顺县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80eb1ceb53da11e68586000c293c07d6', '140724', '昔阳县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80ecf2c853da11e68586000c293c07d6', '140725', '寿阳县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80eee71953da11e68586000c293c07d6', '140726', '太谷县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80f0b0ad53da11e68586000c293c07d6', '140727', '祁县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80f28db053da11e68586000c293c07d6', '140728', '平遥县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80f49f8f53da11e68586000c293c07d6', '140729', '灵石县', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80f77dc353da11e68586000c293c07d6', '140781', '介休市', null, null, '140700');
INSERT INTO `sys_area` VALUES ('80f96e6453da11e68586000c293c07d6', '140800', '运城市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('80fb39f053da11e68586000c293c07d6', '140801', '市辖区', null, null, '140800');
INSERT INTO `sys_area` VALUES ('80fd157853da11e68586000c293c07d6', '140802', '盐湖区', null, null, '140801');
INSERT INTO `sys_area` VALUES ('80fedf0a53da11e68586000c293c07d6', '140821', '临猗县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('8100b6e653da11e68586000c293c07d6', '140822', '万荣县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('81027b4253da11e68586000c293c07d6', '140823', '闻喜县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('810452c453da11e68586000c293c07d6', '140824', '稷山县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('810643be53da11e68586000c293c07d6', '140825', '新绛县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('8107cecb53da11e68586000c293c07d6', '140826', '绛县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('8109a08853da11e68586000c293c07d6', '140827', '垣曲县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('810b74ac53da11e68586000c293c07d6', '140828', '夏县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('810d838753da11e68586000c293c07d6', '140829', '平陆县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('810f3dc153da11e68586000c293c07d6', '140830', '芮城县', null, null, '140800');
INSERT INTO `sys_area` VALUES ('8111821453da11e68586000c293c07d6', '140881', '永济市', null, null, '140800');
INSERT INTO `sys_area` VALUES ('81135ccd53da11e68586000c293c07d6', '140882', '河津市', null, null, '140800');
INSERT INTO `sys_area` VALUES ('81154a7153da11e68586000c293c07d6', '140900', '忻州市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('81170efc53da11e68586000c293c07d6', '140901', '忻府区', null, null, '140900');
INSERT INTO `sys_area` VALUES ('8118e00f53da11e68586000c293c07d6', '140921', '定襄县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('811adf0053da11e68586000c293c07d6', '140922', '五台县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('811c852953da11e68586000c293c07d6', '140923', '代县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('811e720753da11e68586000c293c07d6', '140924', '繁峙县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812050a653da11e68586000c293c07d6', '140925', '宁武县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812261b053da11e68586000c293c07d6', '140926', '静乐县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812487c853da11e68586000c293c07d6', '140927', '神池县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('81266c2653da11e68586000c293c07d6', '140928', '五寨县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812863cc53da11e68586000c293c07d6', '140929', '岢岚县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812a402b53da11e68586000c293c07d6', '140930', '河曲县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812c13fb53da11e68586000c293c07d6', '140931', '保德县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812dbb0653da11e68586000c293c07d6', '140932', '偏关县', null, null, '140900');
INSERT INTO `sys_area` VALUES ('812f951e53da11e68586000c293c07d6', '140981', '原平市', null, null, '140900');
INSERT INTO `sys_area` VALUES ('8131c3c953da11e68586000c293c07d6', '141000', '临汾市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('8133991653da11e68586000c293c07d6', '141001', '市辖区', null, null, '141000');
INSERT INTO `sys_area` VALUES ('8135612553da11e68586000c293c07d6', '141002', '尧都区', null, null, '141000');
INSERT INTO `sys_area` VALUES ('81374f0f53da11e68586000c293c07d6', '141021', '曲沃县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('81391de653da11e68586000c293c07d6', '141022', '翼城县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('813ae33253da11e68586000c293c07d6', '141023', '襄汾县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('813cb96353da11e68586000c293c07d6', '141024', '洪洞县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('813ea07253da11e68586000c293c07d6', '141025', '古县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('8140cbb753da11e68586000c293c07d6', '141026', '安泽县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('8142af2453da11e68586000c293c07d6', '141027', '浮山县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('81449abe53da11e68586000c293c07d6', '141028', '吉县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('8146a3c953da11e68586000c293c07d6', '141029', '乡宁县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('81486b4153da11e68586000c293c07d6', '141030', '大宁县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('814a3d1b53da11e68586000c293c07d6', '141031', '隰县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('814c0b4353da11e68586000c293c07d6', '141032', '永和县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('814dff7f53da11e68586000c293c07d6', '141033', '蒲县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('814fbfb753da11e68586000c293c07d6', '141034', '汾西县', null, null, '141000');
INSERT INTO `sys_area` VALUES ('81517d3d53da11e68586000c293c07d6', '141081', '侯马市', null, null, '141000');
INSERT INTO `sys_area` VALUES ('8153693853da11e68586000c293c07d6', '141082', '霍州市', null, null, '141000');
INSERT INTO `sys_area` VALUES ('815547f353da11e68586000c293c07d6', '141100', '吕梁市', null, null, '140000');
INSERT INTO `sys_area` VALUES ('81571d6053da11e68586000c293c07d6', '141101', '市辖区', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8158d09b53da11e68586000c293c07d6', '141102', '离石区', null, null, '141101');
INSERT INTO `sys_area` VALUES ('815d1a3653da11e68586000c293c07d6', '141121', '文水县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('815edaac53da11e68586000c293c07d6', '141122', '交城县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8160c81653da11e68586000c293c07d6', '141123', '兴县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8162a21853da11e68586000c293c07d6', '141124', '临县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8164a85c53da11e68586000c293c07d6', '141125', '柳林县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8166675153da11e68586000c293c07d6', '141126', '石楼县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8168166f53da11e68586000c293c07d6', '141127', '岚县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('8169f7b753da11e68586000c293c07d6', '141128', '方山县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('816bc7df53da11e68586000c293c07d6', '141129', '中阳县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('816d784f53da11e68586000c293c07d6', '141130', '交口县', null, null, '141100');
INSERT INTO `sys_area` VALUES ('816f8f7453da11e68586000c293c07d6', '141181', '孝义市', null, null, '141100');
INSERT INTO `sys_area` VALUES ('817181dd53da11e68586000c293c07d6', '141182', '汾阳市', null, null, '141100');
INSERT INTO `sys_area` VALUES ('817326d353da11e68586000c293c07d6', '150000', '内蒙古自治区', null, '5', '0');
INSERT INTO `sys_area` VALUES ('81750a6453da11e68586000c293c07d6', '150100', '呼和浩特市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('8176dbbe53da11e68586000c293c07d6', '150101', '市辖区', null, null, '150100');
INSERT INTO `sys_area` VALUES ('817a20d353da11e68586000c293c07d6', '150102', '新城区', null, null, '150101');
INSERT INTO `sys_area` VALUES ('817bfabe53da11e68586000c293c07d6', '150103', '回民区', null, null, '150101');
INSERT INTO `sys_area` VALUES ('817df61753da11e68586000c293c07d6', '150104', '玉泉区', null, null, '150101');
INSERT INTO `sys_area` VALUES ('817fb7f153da11e68586000c293c07d6', '150105', '赛罕区', null, null, '150101');
INSERT INTO `sys_area` VALUES ('818218c353da11e68586000c293c07d6', '150121', '土默特左旗', null, null, '150100');
INSERT INTO `sys_area` VALUES ('818416c153da11e68586000c293c07d6', '150122', '托克托县', null, null, '150100');
INSERT INTO `sys_area` VALUES ('8185d95853da11e68586000c293c07d6', '150123', '和林格尔县', null, null, '150100');
INSERT INTO `sys_area` VALUES ('8187bdfa53da11e68586000c293c07d6', '150124', '清水河县', null, null, '150100');
INSERT INTO `sys_area` VALUES ('8189a14f53da11e68586000c293c07d6', '150125', '武川县', null, null, '150100');
INSERT INTO `sys_area` VALUES ('818b78ec53da11e68586000c293c07d6', '150200', '包头市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('818d3dea53da11e68586000c293c07d6', '150201', '市辖区', null, null, '150200');
INSERT INTO `sys_area` VALUES ('818f1f7853da11e68586000c293c07d6', '150202', '东河区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('8191362053da11e68586000c293c07d6', '150203', '昆都仑区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('81931f1353da11e68586000c293c07d6', '150204', '青山区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('8194fd5c53da11e68586000c293c07d6', '150205', '石拐区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('8196c58053da11e68586000c293c07d6', '150206', '白云矿区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('819899da53da11e68586000c293c07d6', '150207', '九原区', null, null, '150201');
INSERT INTO `sys_area` VALUES ('819a7a6053da11e68586000c293c07d6', '150221', '土默特右旗', null, null, '150200');
INSERT INTO `sys_area` VALUES ('819d518b53da11e68586000c293c07d6', '150222', '固阳县', null, null, '150200');
INSERT INTO `sys_area` VALUES ('819f5f5b53da11e68586000c293c07d6', '150223', '达尔罕茂明安联合旗', null, null, '150200');
INSERT INTO `sys_area` VALUES ('81a1495253da11e68586000c293c07d6', '150300', '乌海市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('81a2f67853da11e68586000c293c07d6', '150301', '市辖区', null, null, '150300');
INSERT INTO `sys_area` VALUES ('81a4fb5153da11e68586000c293c07d6', '150302', '海勃湾区', null, null, '150301');
INSERT INTO `sys_area` VALUES ('81a6bbbb53da11e68586000c293c07d6', '150303', '海南区', null, null, '150301');
INSERT INTO `sys_area` VALUES ('81a8eb3b53da11e68586000c293c07d6', '150304', '乌达区', null, null, '150301');
INSERT INTO `sys_area` VALUES ('81aac0ec53da11e68586000c293c07d6', '150400', '赤峰市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('81ac948b53da11e68586000c293c07d6', '150401', '市辖区', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81aeb41053da11e68586000c293c07d6', '150402', '红山区', null, null, '150401');
INSERT INTO `sys_area` VALUES ('81b09d5c53da11e68586000c293c07d6', '150403', '元宝山区', null, null, '150401');
INSERT INTO `sys_area` VALUES ('81b26cad53da11e68586000c293c07d6', '150404', '松山区', null, null, '150401');
INSERT INTO `sys_area` VALUES ('81b44b2853da11e68586000c293c07d6', '150421', '阿鲁科尔沁旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81b626af53da11e68586000c293c07d6', '150422', '巴林左旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81b7d57a53da11e68586000c293c07d6', '150423', '巴林右旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81b98f4c53da11e68586000c293c07d6', '150424', '林西县', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81bb72ad53da11e68586000c293c07d6', '150425', '克什克腾旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81bd67ac53da11e68586000c293c07d6', '150426', '翁牛特旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81bf2d9a53da11e68586000c293c07d6', '150428', '喀喇沁旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81c1684553da11e68586000c293c07d6', '150429', '宁城县', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81c342bc53da11e68586000c293c07d6', '150430', '敖汉旗', null, null, '150400');
INSERT INTO `sys_area` VALUES ('81c4fcf853da11e68586000c293c07d6', '150500', '通辽市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('81c6dfc953da11e68586000c293c07d6', '150501', '市辖区', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81c8b14f53da11e68586000c293c07d6', '150502', '科尔沁区', null, null, '150501');
INSERT INTO `sys_area` VALUES ('81ca874c53da11e68586000c293c07d6', '150521', '科尔沁左翼中旗', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81cc77b753da11e68586000c293c07d6', '150522', '科尔沁左翼后旗', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81ce4ce353da11e68586000c293c07d6', '150523', '开鲁县', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81d02e3553da11e68586000c293c07d6', '150524', '库伦旗', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81d232e653da11e68586000c293c07d6', '150525', '奈曼旗', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81d4169e53da11e68586000c293c07d6', '150526', '扎鲁特旗', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81d6281d53da11e68586000c293c07d6', '150581', '霍林郭勒市', null, null, '150500');
INSERT INTO `sys_area` VALUES ('81d82e7353da11e68586000c293c07d6', '150600', '鄂尔多斯市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('81da090f53da11e68586000c293c07d6', '150602', '东胜区', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81dc93fa53da11e68586000c293c07d6', '150621', '达拉特旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81de645553da11e68586000c293c07d6', '150622', '准格尔旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81dff99d53da11e68586000c293c07d6', '150623', '鄂托克前旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81e1b8e353da11e68586000c293c07d6', '150624', '鄂托克旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81e38c8253da11e68586000c293c07d6', '150625', '杭锦旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81e5447e53da11e68586000c293c07d6', '150626', '乌审旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81e71fe153da11e68586000c293c07d6', '150627', '伊金霍洛旗', null, null, '150600');
INSERT INTO `sys_area` VALUES ('81e8fa3453da11e68586000c293c07d6', '150700', '呼伦贝尔市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('81eaebfe53da11e68586000c293c07d6', '150701', '市辖区', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81ecac5d53da11e68586000c293c07d6', '150702', '海拉尔区', null, null, '150701');
INSERT INTO `sys_area` VALUES ('81ee69f153da11e68586000c293c07d6', '150721', '阿荣旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f0478a53da11e68586000c293c07d6', '150722', '莫力达瓦达斡尔族自治旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f20c7453da11e68586000c293c07d6', '150723', '鄂伦春自治旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f3c8dc53da11e68586000c293c07d6', '150724', '鄂温克族自治旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f61ccf53da11e68586000c293c07d6', '150725', '陈巴尔虎旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f7f10453da11e68586000c293c07d6', '150726', '新巴尔虎左旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81f9b96f53da11e68586000c293c07d6', '150727', '新巴尔虎右旗', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81fbadb053da11e68586000c293c07d6', '150781', '满洲里市', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81fda4e353da11e68586000c293c07d6', '150782', '牙克石市', null, null, '150700');
INSERT INTO `sys_area` VALUES ('81ff7e6053da11e68586000c293c07d6', '150783', '扎兰屯市', null, null, '150700');
INSERT INTO `sys_area` VALUES ('82016fc453da11e68586000c293c07d6', '150784', '额尔古纳市', null, null, '150700');
INSERT INTO `sys_area` VALUES ('82032dec53da11e68586000c293c07d6', '150785', '根河市', null, null, '150700');
INSERT INTO `sys_area` VALUES ('8204fd3a53da11e68586000c293c07d6', '150800', '巴彦淖尔市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('8206cca653da11e68586000c293c07d6', '150801', '市辖区', null, null, '150800');
INSERT INTO `sys_area` VALUES ('82089bbb53da11e68586000c293c07d6', '150802', '临河区', null, null, '150800');
INSERT INTO `sys_area` VALUES ('820af08053da11e68586000c293c07d6', '150821', '五原县', null, null, '150800');
INSERT INTO `sys_area` VALUES ('820cc87f53da11e68586000c293c07d6', '150822', '磴口县', null, null, '150800');
INSERT INTO `sys_area` VALUES ('820ea0cb53da11e68586000c293c07d6', '150823', '乌拉特前旗', null, null, '150800');
INSERT INTO `sys_area` VALUES ('82105a5c53da11e68586000c293c07d6', '150824', '乌拉特中旗', null, null, '150800');
INSERT INTO `sys_area` VALUES ('82120f9e53da11e68586000c293c07d6', '150825', '乌拉特后旗', null, null, '150800');
INSERT INTO `sys_area` VALUES ('8213efbf53da11e68586000c293c07d6', '150826', '杭锦后旗', null, null, '150800');
INSERT INTO `sys_area` VALUES ('8215959453da11e68586000c293c07d6', '150900', '乌兰察布市', null, null, '150000');
INSERT INTO `sys_area` VALUES ('821760c153da11e68586000c293c07d6', '150901', '市辖区', null, null, '150900');
INSERT INTO `sys_area` VALUES ('82194c9d53da11e68586000c293c07d6', '150902', '集宁区', null, null, '150901');
INSERT INTO `sys_area` VALUES ('821b370f53da11e68586000c293c07d6', '150921', '卓资县', null, null, '150900');
INSERT INTO `sys_area` VALUES ('821cefd853da11e68586000c293c07d6', '150922', '化德县', null, null, '150900');
INSERT INTO `sys_area` VALUES ('82221f0953da11e68586000c293c07d6', '150923', '商都县', null, null, '150900');
INSERT INTO `sys_area` VALUES ('8224109253da11e68586000c293c07d6', '150924', '兴和县', null, null, '150900');
INSERT INTO `sys_area` VALUES ('82260bd453da11e68586000c293c07d6', '150925', '凉城县', null, null, '150900');
INSERT INTO `sys_area` VALUES ('82285ac853da11e68586000c293c07d6', '150926', '察哈尔右翼前旗', null, null, '150900');
INSERT INTO `sys_area` VALUES ('822b595b53da11e68586000c293c07d6', '150927', '察哈尔右翼中旗', null, null, '150900');
INSERT INTO `sys_area` VALUES ('822d7ddb53da11e68586000c293c07d6', '150928', '察哈尔右翼后旗', null, null, '150900');
INSERT INTO `sys_area` VALUES ('822fcf3a53da11e68586000c293c07d6', '150929', '四子王旗', null, null, '150900');
INSERT INTO `sys_area` VALUES ('8231f99253da11e68586000c293c07d6', '150981', '丰镇市', null, null, '150900');
INSERT INTO `sys_area` VALUES ('8234c6fe53da11e68586000c293c07d6', '152200', '兴安盟', null, null, '150000');
INSERT INTO `sys_area` VALUES ('8236fa4353da11e68586000c293c07d6', '152201', '乌兰浩特市', null, null, '152200');
INSERT INTO `sys_area` VALUES ('8239d11e53da11e68586000c293c07d6', '152202', '阿尔山市', null, null, '152200');
INSERT INTO `sys_area` VALUES ('823bf90953da11e68586000c293c07d6', '152221', '科尔沁右翼前旗', null, null, '152200');
INSERT INTO `sys_area` VALUES ('823e3b9a53da11e68586000c293c07d6', '152222', '科尔沁右翼中旗', null, null, '152200');
INSERT INTO `sys_area` VALUES ('824081b053da11e68586000c293c07d6', '152223', '扎赉特旗', null, null, '152200');
INSERT INTO `sys_area` VALUES ('8242b48653da11e68586000c293c07d6', '152224', '突泉县', null, null, '152200');
INSERT INTO `sys_area` VALUES ('8245329853da11e68586000c293c07d6', '152500', '锡林郭勒盟', null, null, '150000');
INSERT INTO `sys_area` VALUES ('8247563953da11e68586000c293c07d6', '152501', '二连浩特市', null, null, '152500');
INSERT INTO `sys_area` VALUES ('8249496c53da11e68586000c293c07d6', '152502', '锡林浩特市', null, null, '152500');
INSERT INTO `sys_area` VALUES ('824b881553da11e68586000c293c07d6', '152522', '阿巴嘎旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('824d567353da11e68586000c293c07d6', '152523', '苏尼特左旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('824fc58953da11e68586000c293c07d6', '152524', '苏尼特右旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('82517ba953da11e68586000c293c07d6', '152525', '东乌珠穆沁旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('8253584c53da11e68586000c293c07d6', '152526', '西乌珠穆沁旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('8255144053da11e68586000c293c07d6', '152527', '太仆寺旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('8256d86753da11e68586000c293c07d6', '152528', '镶黄旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('8258ae3553da11e68586000c293c07d6', '152529', '正镶白旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('825b232353da11e68586000c293c07d6', '152530', '正蓝旗', null, null, '152500');
INSERT INTO `sys_area` VALUES ('825ce21553da11e68586000c293c07d6', '152531', '多伦县', null, null, '152500');
INSERT INTO `sys_area` VALUES ('825f03c053da11e68586000c293c07d6', '152900', '阿拉善盟', null, null, '150000');
INSERT INTO `sys_area` VALUES ('8260d6d153da11e68586000c293c07d6', '152921', '阿拉善左旗', null, null, '152900');
INSERT INTO `sys_area` VALUES ('8262b3d553da11e68586000c293c07d6', '152922', '阿拉善右旗', null, null, '152900');
INSERT INTO `sys_area` VALUES ('8264723053da11e68586000c293c07d6', '152923', '额济纳旗', null, null, '152900');
INSERT INTO `sys_area` VALUES ('8266380553da11e68586000c293c07d6', '210000', '辽宁省', null, '6', '0');
INSERT INTO `sys_area` VALUES ('826a0f7b53da11e68586000c293c07d6', '210100', '沈阳市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('826c005653da11e68586000c293c07d6', '210101', '市辖区', null, null, '210100');
INSERT INTO `sys_area` VALUES ('826e0e3953da11e68586000c293c07d6', '210102', '和平区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('826fcecd53da11e68586000c293c07d6', '210103', '沈河区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('8271ac9253da11e68586000c293c07d6', '210104', '大东区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('82739f2e53da11e68586000c293c07d6', '210105', '皇姑区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('82757c8653da11e68586000c293c07d6', '210106', '铁西区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('8277887c53da11e68586000c293c07d6', '210111', '苏家屯区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('8279523253da11e68586000c293c07d6', '210112', '东陵区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('827b265b53da11e68586000c293c07d6', '210113', '新城子区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('827d2cf153da11e68586000c293c07d6', '210114', '于洪区', null, null, '210101');
INSERT INTO `sys_area` VALUES ('827f46ab53da11e68586000c293c07d6', '210122', '辽中县', null, null, '210100');
INSERT INTO `sys_area` VALUES ('8281634853da11e68586000c293c07d6', '210123', '康平县', null, null, '210100');
INSERT INTO `sys_area` VALUES ('8283303053da11e68586000c293c07d6', '210124', '法库县', null, null, '210100');
INSERT INTO `sys_area` VALUES ('8285226a53da11e68586000c293c07d6', '210181', '新民市', null, null, '210100');
INSERT INTO `sys_area` VALUES ('8287175353da11e68586000c293c07d6', '210182', '沈北新区', null, null, '210100');
INSERT INTO `sys_area` VALUES ('8288f19b53da11e68586000c293c07d6', '210200', '大连市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('828ad01453da11e68586000c293c07d6', '210201', '市辖区', null, null, '210200');
INSERT INTO `sys_area` VALUES ('828cd90753da11e68586000c293c07d6', '210202', '中山区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('828f13d253da11e68586000c293c07d6', '210203', '西岗区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('8290f81353da11e68586000c293c07d6', '210204', '沙河口区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('8292c8d253da11e68586000c293c07d6', '210211', '甘井子区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('8294b30b53da11e68586000c293c07d6', '210212', '旅顺口区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('829690f553da11e68586000c293c07d6', '210213', '金州区', null, null, '210201');
INSERT INTO `sys_area` VALUES ('82987cff53da11e68586000c293c07d6', '210224', '长海县', null, null, '210200');
INSERT INTO `sys_area` VALUES ('829a71d853da11e68586000c293c07d6', '210281', '瓦房店市', null, null, '210200');
INSERT INTO `sys_area` VALUES ('829c5c0853da11e68586000c293c07d6', '210282', '普兰店市', null, null, '210200');
INSERT INTO `sys_area` VALUES ('829e3a1f53da11e68586000c293c07d6', '210283', '庄河市', null, null, '210200');
INSERT INTO `sys_area` VALUES ('82a0177353da11e68586000c293c07d6', '210300', '鞍山市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82a1997153da11e68586000c293c07d6', '210301', '市辖区', null, null, '210300');
INSERT INTO `sys_area` VALUES ('82a3ac8153da11e68586000c293c07d6', '210302', '铁东区', null, null, '210301');
INSERT INTO `sys_area` VALUES ('82a56a2553da11e68586000c293c07d6', '210303', '铁西区', null, null, '210301');
INSERT INTO `sys_area` VALUES ('82a7738453da11e68586000c293c07d6', '210304', '立山区', null, null, '210301');
INSERT INTO `sys_area` VALUES ('82a921a853da11e68586000c293c07d6', '210311', '千山区', null, null, '210301');
INSERT INTO `sys_area` VALUES ('82ab018253da11e68586000c293c07d6', '210321', '台安县', null, null, '210300');
INSERT INTO `sys_area` VALUES ('82accc1e53da11e68586000c293c07d6', '210323', '岫岩满族自治县', null, null, '210300');
INSERT INTO `sys_area` VALUES ('82aef67f53da11e68586000c293c07d6', '210381', '海城市', null, null, '210300');
INSERT INTO `sys_area` VALUES ('82b0e80153da11e68586000c293c07d6', '210400', '抚顺市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82b2c93a53da11e68586000c293c07d6', '210401', '市辖区', null, null, '210400');
INSERT INTO `sys_area` VALUES ('82b48eb253da11e68586000c293c07d6', '210402', '新抚区', null, null, '210401');
INSERT INTO `sys_area` VALUES ('82b6bfa553da11e68586000c293c07d6', '210403', '东洲区', null, null, '210401');
INSERT INTO `sys_area` VALUES ('82b8e9f253da11e68586000c293c07d6', '210404', '望花区', null, null, '210401');
INSERT INTO `sys_area` VALUES ('82bac4b553da11e68586000c293c07d6', '210411', '顺城区', null, null, '210401');
INSERT INTO `sys_area` VALUES ('82bcb9d853da11e68586000c293c07d6', '210421', '抚顺县', null, null, '210400');
INSERT INTO `sys_area` VALUES ('82be91e353da11e68586000c293c07d6', '210422', '新宾满族自治县', null, null, '210400');
INSERT INTO `sys_area` VALUES ('82c05e3153da11e68586000c293c07d6', '210423', '清原满族自治县', null, null, '210400');
INSERT INTO `sys_area` VALUES ('82c25aad53da11e68586000c293c07d6', '210500', '本溪市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82c40e9b53da11e68586000c293c07d6', '210501', '市辖区', null, null, '210500');
INSERT INTO `sys_area` VALUES ('82c5ddb253da11e68586000c293c07d6', '210502', '平山区', null, null, '210501');
INSERT INTO `sys_area` VALUES ('82c830ed53da11e68586000c293c07d6', '210503', '溪湖??', null, null, '210501');
INSERT INTO `sys_area` VALUES ('82ca0cad53da11e68586000c293c07d6', '210504', '明山区', null, null, '210501');
INSERT INTO `sys_area` VALUES ('82cc13cc53da11e68586000c293c07d6', '210505', '南芬区', null, null, '210501');
INSERT INTO `sys_area` VALUES ('82cded8153da11e68586000c293c07d6', '210521', '本溪满族自治县', null, null, '210500');
INSERT INTO `sys_area` VALUES ('82cfd79e53da11e68586000c293c07d6', '210522', '桓仁满族自治县', null, null, '210500');
INSERT INTO `sys_area` VALUES ('82d1b46053da11e68586000c293c07d6', '210600', '丹东市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82d38c2653da11e68586000c293c07d6', '210601', '市辖区', null, null, '210600');
INSERT INTO `sys_area` VALUES ('82d55f8053da11e68586000c293c07d6', '210602', '元宝区', null, null, '210601');
INSERT INTO `sys_area` VALUES ('82d82a8653da11e68586000c293c07d6', '210603', '振兴区', null, null, '210601');
INSERT INTO `sys_area` VALUES ('82d9fd3d53da11e68586000c293c07d6', '210604', '振安区', null, null, '210601');
INSERT INTO `sys_area` VALUES ('82dba4de53da11e68586000c293c07d6', '210624', '宽甸满族自治县', null, null, '210600');
INSERT INTO `sys_area` VALUES ('82de3abd53da11e68586000c293c07d6', '210681', '东港市', null, null, '210600');
INSERT INTO `sys_area` VALUES ('82e0061853da11e68586000c293c07d6', '210682', '凤城市', null, null, '210600');
INSERT INTO `sys_area` VALUES ('82e21eec53da11e68586000c293c07d6', '210700', '锦州市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82e3fe3053da11e68586000c293c07d6', '210701', '市辖区', null, null, '210700');
INSERT INTO `sys_area` VALUES ('82e5e7f653da11e68586000c293c07d6', '210702', '古塔区', null, null, '210701');
INSERT INTO `sys_area` VALUES ('82e7b65653da11e68586000c293c07d6', '210703', '凌河区', null, null, '210701');
INSERT INTO `sys_area` VALUES ('82e991d853da11e68586000c293c07d6', '210711', '太和区', null, null, '210701');
INSERT INTO `sys_area` VALUES ('82eb62fe53da11e68586000c293c07d6', '210726', '黑山县', null, null, '210700');
INSERT INTO `sys_area` VALUES ('82ed317853da11e68586000c293c07d6', '210727', '义县', null, null, '210700');
INSERT INTO `sys_area` VALUES ('82ef074653da11e68586000c293c07d6', '210781', '凌海市', null, null, '210700');
INSERT INTO `sys_area` VALUES ('82f0cb0f53da11e68586000c293c07d6', '210782', '北宁市', null, null, '210700');
INSERT INTO `sys_area` VALUES ('82f2c4e153da11e68586000c293c07d6', '210800', '营口市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('82f49f9953da11e68586000c293c07d6', '210801', '市辖区', null, null, '210800');
INSERT INTO `sys_area` VALUES ('82f6ad1c53da11e68586000c293c07d6', '210802', '站前区', null, null, '210801');
INSERT INTO `sys_area` VALUES ('82f8742e53da11e68586000c293c07d6', '210803', '西市区', null, null, '210801');
INSERT INTO `sys_area` VALUES ('82fa2ac353da11e68586000c293c07d6', '210804', '鲅鱼圈区', null, null, '210801');
INSERT INTO `sys_area` VALUES ('82fc26a653da11e68586000c293c07d6', '210811', '老边区', null, null, '210801');
INSERT INTO `sys_area` VALUES ('82fde54a53da11e68586000c293c07d6', '210881', '盖州市', null, null, '210800');
INSERT INTO `sys_area` VALUES ('82ffa95153da11e68586000c293c07d6', '210882', '大石桥市', null, null, '210800');
INSERT INTO `sys_area` VALUES ('830171a353da11e68586000c293c07d6', '210900', '阜新市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('8303473e53da11e68586000c293c07d6', '210901', '市辖区', null, null, '210900');
INSERT INTO `sys_area` VALUES ('8305f65653da11e68586000c293c07d6', '210902', '海州区', null, null, '210901');
INSERT INTO `sys_area` VALUES ('8307e0e253da11e68586000c293c07d6', '210903', '新邱区', null, null, '210901');
INSERT INTO `sys_area` VALUES ('8309b74753da11e68586000c293c07d6', '210904', '太平区', null, null, '210901');
INSERT INTO `sys_area` VALUES ('830b7f7453da11e68586000c293c07d6', '210905', '清河门区', null, null, '210901');
INSERT INTO `sys_area` VALUES ('830d810453da11e68586000c293c07d6', '210911', '细河区', null, null, '210901');
INSERT INTO `sys_area` VALUES ('830f55f953da11e68586000c293c07d6', '210921', '阜新蒙古族自治县', null, null, '210900');
INSERT INTO `sys_area` VALUES ('831116d553da11e68586000c293c07d6', '210922', '彰武县', null, null, '210900');
INSERT INTO `sys_area` VALUES ('831402da53da11e68586000c293c07d6', '211000', '辽阳市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('8315f82753da11e68586000c293c07d6', '211001', '市辖区', null, null, '211000');
INSERT INTO `sys_area` VALUES ('8317d75253da11e68586000c293c07d6', '211002', '白塔区', null, null, '211001');
INSERT INTO `sys_area` VALUES ('8319ca7853da11e68586000c293c07d6', '211003', '文圣区', null, null, '211001');
INSERT INTO `sys_area` VALUES ('831bae0c53da11e68586000c293c07d6', '211004', '宏伟区', null, null, '211001');
INSERT INTO `sys_area` VALUES ('831d88ad53da11e68586000c293c07d6', '211005', '弓长岭区', null, null, '211001');
INSERT INTO `sys_area` VALUES ('831f98fb53da11e68586000c293c07d6', '211011', '太子河区', null, null, '211001');
INSERT INTO `sys_area` VALUES ('8321634953da11e68586000c293c07d6', '211021', '辽阳县', null, null, '211000');
INSERT INTO `sys_area` VALUES ('832349e953da11e68586000c293c07d6', '211081', '灯塔市', null, null, '211000');
INSERT INTO `sys_area` VALUES ('8325beeb53da11e68586000c293c07d6', '211100', '盘锦市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('8327f45253da11e68586000c293c07d6', '211101', '市辖区', null, null, '211100');
INSERT INTO `sys_area` VALUES ('8329fd3553da11e68586000c293c07d6', '211102', '双台子区', null, null, '211101');
INSERT INTO `sys_area` VALUES ('832bc4c653da11e68586000c293c07d6', '211103', '兴隆台区', null, null, '211101');
INSERT INTO `sys_area` VALUES ('832da02953da11e68586000c293c07d6', '211121', '大洼县', null, null, '211100');
INSERT INTO `sys_area` VALUES ('832f762353da11e68586000c293c07d6', '211122', '盘山县', null, null, '211100');
INSERT INTO `sys_area` VALUES ('833177bb53da11e68586000c293c07d6', '211200', '铁岭市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('83336c6553da11e68586000c293c07d6', '211201', '市辖区', null, null, '211200');
INSERT INTO `sys_area` VALUES ('8335602053da11e68586000c293c07d6', '211202', '银州区', null, null, '211201');
INSERT INTO `sys_area` VALUES ('83373b1c53da11e68586000c293c07d6', '211204', '清河区', null, null, '211201');
INSERT INTO `sys_area` VALUES ('8339c3ab53da11e68586000c293c07d6', '211221', '铁岭县', null, null, '211200');
INSERT INTO `sys_area` VALUES ('833b98c653da11e68586000c293c07d6', '211223', '西丰县', null, null, '211200');
INSERT INTO `sys_area` VALUES ('833d9ae453da11e68586000c293c07d6', '211224', '昌图县', null, null, '211200');
INSERT INTO `sys_area` VALUES ('833f4de753da11e68586000c293c07d6', '211281', '调兵山市', null, null, '211200');
INSERT INTO `sys_area` VALUES ('8340ff7153da11e68586000c293c07d6', '211282', '开原市', null, null, '211200');
INSERT INTO `sys_area` VALUES ('8342da0553da11e68586000c293c07d6', '211300', '朝阳市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('8344b33a53da11e68586000c293c07d6', '211301', '市辖区', null, null, '211300');
INSERT INTO `sys_area` VALUES ('8346845753da11e68586000c293c07d6', '211302', '双塔区', null, null, '211301');
INSERT INTO `sys_area` VALUES ('834866d253da11e68586000c293c07d6', '211303', '龙城区', null, null, '211301');
INSERT INTO `sys_area` VALUES ('834a438753da11e68586000c293c07d6', '211321', '朝阳县', null, null, '211300');
INSERT INTO `sys_area` VALUES ('834bf90653da11e68586000c293c07d6', '211322', '建平县', null, null, '211300');
INSERT INTO `sys_area` VALUES ('834db5e853da11e68586000c293c07d6', '211324', '喀喇沁左翼蒙古族自治县', null, null, '211300');
INSERT INTO `sys_area` VALUES ('834f74e653da11e68586000c293c07d6', '211381', '北票市', null, null, '211300');
INSERT INTO `sys_area` VALUES ('835138bc53da11e68586000c293c07d6', '211382', '凌源市', null, null, '211300');
INSERT INTO `sys_area` VALUES ('835342da53da11e68586000c293c07d6', '211400', '葫芦岛市', null, null, '210000');
INSERT INTO `sys_area` VALUES ('835515df53da11e68586000c293c07d6', '211401', '市辖区', null, null, '211400');
INSERT INTO `sys_area` VALUES ('8356cf0853da11e68586000c293c07d6', '211402', '连山区', null, null, '211401');
INSERT INTO `sys_area` VALUES ('835895ff53da11e68586000c293c07d6', '211403', '龙港区', null, null, '211401');
INSERT INTO `sys_area` VALUES ('835a675e53da11e68586000c293c07d6', '211404', '南票区', null, null, '211401');
INSERT INTO `sys_area` VALUES ('835cf9c853da11e68586000c293c07d6', '211421', '绥中县', null, null, '211400');
INSERT INTO `sys_area` VALUES ('835eacb253da11e68586000c293c07d6', '211422', '建昌县', null, null, '211400');
INSERT INTO `sys_area` VALUES ('83607e6c53da11e68586000c293c07d6', '211481', '兴城市', null, null, '211400');
INSERT INTO `sys_area` VALUES ('8362d48b53da11e68586000c293c07d6', '220000', '吉林省', null, '7', '0');
INSERT INTO `sys_area` VALUES ('8364c35353da11e68586000c293c07d6', '220100', '长春市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('8366a14c53da11e68586000c293c07d6', '220101', '市辖区', null, null, '220100');
INSERT INTO `sys_area` VALUES ('8368c5df53da11e68586000c293c07d6', '220102', '南关区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('836ab57953da11e68586000c293c07d6', '220103', '宽城区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('836c6d7553da11e68586000c293c07d6', '220104', '朝阳区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('836e5ca153da11e68586000c293c07d6', '220105', '二道区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('83704a1553da11e68586000c293c07d6', '220106', '绿园区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('837217e553da11e68586000c293c07d6', '220112', '双阳区', null, null, '220101');
INSERT INTO `sys_area` VALUES ('8373e0ad53da11e68586000c293c07d6', '220122', '农安县', null, null, '220100');
INSERT INTO `sys_area` VALUES ('8375c85553da11e68586000c293c07d6', '220181', '九台市', null, null, '220100');
INSERT INTO `sys_area` VALUES ('8377a69e53da11e68586000c293c07d6', '220182', '榆树市', null, null, '220100');
INSERT INTO `sys_area` VALUES ('8379e0d353da11e68586000c293c07d6', '220183', '德惠市', null, null, '220100');
INSERT INTO `sys_area` VALUES ('837be84e53da11e68586000c293c07d6', '220200', '吉林市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('837defb953da11e68586000c293c07d6', '220201', '市辖区', null, null, '220200');
INSERT INTO `sys_area` VALUES ('837fbc0e53da11e68586000c293c07d6', '220202', '昌邑区', null, null, '220201');
INSERT INTO `sys_area` VALUES ('8381a2ad53da11e68586000c293c07d6', '220203', '龙潭区', null, null, '220201');
INSERT INTO `sys_area` VALUES ('8383a34f53da11e68586000c293c07d6', '220204', '船营区', null, null, '220201');
INSERT INTO `sys_area` VALUES ('8385687953da11e68586000c293c07d6', '220211', '丰满区', null, null, '220201');
INSERT INTO `sys_area` VALUES ('83874acb53da11e68586000c293c07d6', '220221', '永吉县', null, null, '220200');
INSERT INTO `sys_area` VALUES ('83893d2253da11e68586000c293c07d6', '220281', '蛟河市', null, null, '220200');
INSERT INTO `sys_area` VALUES ('838aed2453da11e68586000c293c07d6', '220282', '桦甸市', null, null, '220200');
INSERT INTO `sys_area` VALUES ('838ca7c653da11e68586000c293c07d6', '220283', '舒兰市', null, null, '220200');
INSERT INTO `sys_area` VALUES ('838e919953da11e68586000c293c07d6', '220284', '磐石市', null, null, '220200');
INSERT INTO `sys_area` VALUES ('8390788953da11e68586000c293c07d6', '220300', '四平市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83925c2453da11e68586000c293c07d6', '220301', '市辖区', null, null, '220300');
INSERT INTO `sys_area` VALUES ('8394707753da11e68586000c293c07d6', '220302', '铁西区', null, null, '220301');
INSERT INTO `sys_area` VALUES ('839643ad53da11e68586000c293c07d6', '220303', '铁东区', null, null, '220301');
INSERT INTO `sys_area` VALUES ('839817de53da11e68586000c293c07d6', '220322', '梨树县', null, null, '220300');
INSERT INTO `sys_area` VALUES ('839a214e53da11e68586000c293c07d6', '220323', '伊通满族自治县', null, null, '220300');
INSERT INTO `sys_area` VALUES ('839bff1e53da11e68586000c293c07d6', '220381', '公主岭市', null, null, '220300');
INSERT INTO `sys_area` VALUES ('83a330af53da11e68586000c293c07d6', '220382', '双辽市', null, null, '220300');
INSERT INTO `sys_area` VALUES ('83a4f5b853da11e68586000c293c07d6', '220400', '辽源市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83a6dde253da11e68586000c293c07d6', '220401', '市辖区', null, null, '220400');
INSERT INTO `sys_area` VALUES ('83a8c87153da11e68586000c293c07d6', '220402', '龙山区', null, null, '220401');
INSERT INTO `sys_area` VALUES ('83aad96953da11e68586000c293c07d6', '220403', '西安区', null, null, '220401');
INSERT INTO `sys_area` VALUES ('83acbe8d53da11e68586000c293c07d6', '220421', '东丰县', null, null, '220400');
INSERT INTO `sys_area` VALUES ('83aea29253da11e68586000c293c07d6', '220422', '东辽县', null, null, '220400');
INSERT INTO `sys_area` VALUES ('83b0771253da11e68586000c293c07d6', '220500', '通化市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83b28ef253da11e68586000c293c07d6', '220501', '市辖区', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83b4421853da11e68586000c293c07d6', '220502', '东昌区', null, null, '220501');
INSERT INTO `sys_area` VALUES ('83b6326053da11e68586000c293c07d6', '220503', '二道江区', null, null, '220501');
INSERT INTO `sys_area` VALUES ('83b8141c53da11e68586000c293c07d6', '220521', '通化县', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83b9f59153da11e68586000c293c07d6', '220523', '辉南县', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83bbcf4653da11e68586000c293c07d6', '220524', '柳河县', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83bdbaf753da11e68586000c293c07d6', '220581', '梅河口市', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83bf8e4b53da11e68586000c293c07d6', '220582', '集安市', null, null, '220500');
INSERT INTO `sys_area` VALUES ('83c1646f53da11e68586000c293c07d6', '220600', '白山市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83c3102b53da11e68586000c293c07d6', '220601', '市辖区', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83c538b253da11e68586000c293c07d6', '220602', '八道江区', null, null, '220601');
INSERT INTO `sys_area` VALUES ('83c6f64c53da11e68586000c293c07d6', '220621', '抚松县', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83c8cc9a53da11e68586000c293c07d6', '220622', '靖宇县', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83ca8e8a53da11e68586000c293c07d6', '220623', '长白朝鲜族自治县', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83cc9a7553da11e68586000c293c07d6', '220625', '江源区', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83ce7c9753da11e68586000c293c07d6', '220681', '临江市', null, null, '220600');
INSERT INTO `sys_area` VALUES ('83d0307f53da11e68586000c293c07d6', '220700', '松原市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83d2108b53da11e68586000c293c07d6', '220701', '市辖区', null, null, '220700');
INSERT INTO `sys_area` VALUES ('83d3e45b53da11e68586000c293c07d6', '220702', '宁江区', null, null, '220701');
INSERT INTO `sys_area` VALUES ('83d5d21253da11e68586000c293c07d6', '220721', '前郭尔罗斯蒙古族自治县', null, null, '220700');
INSERT INTO `sys_area` VALUES ('83d79b5a53da11e68586000c293c07d6', '220722', '长岭县', null, null, '220700');
INSERT INTO `sys_area` VALUES ('83d96d2153da11e68586000c293c07d6', '220723', '乾安县', null, null, '220700');
INSERT INTO `sys_area` VALUES ('83db630753da11e68586000c293c07d6', '220724', '扶余市', null, null, '220700');
INSERT INTO `sys_area` VALUES ('83de39f253da11e68586000c293c07d6', '220800', '白城市', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83e0312353da11e68586000c293c07d6', '220801', '市辖区', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83e1f09953da11e68586000c293c07d6', '220802', '洮北区', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83e3d39653da11e68586000c293c07d6', '220821', '镇赉县', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83e5cfae53da11e68586000c293c07d6', '220822', '通榆县', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83e79d4d53da11e68586000c293c07d6', '220881', '洮南市', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83e964c353da11e68586000c293c07d6', '220882', '大安市', null, null, '220800');
INSERT INTO `sys_area` VALUES ('83eb547f53da11e68586000c293c07d6', '222400', '延边朝鲜族自治州', null, null, '220000');
INSERT INTO `sys_area` VALUES ('83eded2a53da11e68586000c293c07d6', '222401', '延吉市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83efe5ab53da11e68586000c293c07d6', '222402', '图们市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83f1f64953da11e68586000c293c07d6', '222403', '敦化市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83f43d4753da11e68586000c293c07d6', '222404', '珲春市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83f651c353da11e68586000c293c07d6', '222405', '龙井市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83f9314e53da11e68586000c293c07d6', '222406', '和龙市', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83fbf22b53da11e68586000c293c07d6', '222424', '汪清县', null, null, '222400');
INSERT INTO `sys_area` VALUES ('83fe582753da11e68586000c293c07d6', '222426', '安图县', null, null, '222400');
INSERT INTO `sys_area` VALUES ('8400815753da11e68586000c293c07d6', '230000', '黑龙江省', null, '8', '0');
INSERT INTO `sys_area` VALUES ('8402e54d53da11e68586000c293c07d6', '230100', '哈尔滨市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('8405625753da11e68586000c293c07d6', '230101', '市辖区', null, null, '230100');
INSERT INTO `sys_area` VALUES ('8407b31253da11e68586000c293c07d6', '230102', '道里区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('8409f3a653da11e68586000c293c07d6', '230103', '南岗区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('840c3ebe53da11e68586000c293c07d6', '230104', '道外区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('840e6bc253da11e68586000c293c07d6', '230106', '香坊区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('8413598453da11e68586000c293c07d6', '230107', '动力区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('8415166653da11e68586000c293c07d6', '230108', '平房区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('841709ff53da11e68586000c293c07d6', '230109', '松北区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('84190cb153da11e68586000c293c07d6', '230111', '呼兰区', null, null, '230101');
INSERT INTO `sys_area` VALUES ('841b6e1d53da11e68586000c293c07d6', '230123', '依兰县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('841d758a53da11e68586000c293c07d6', '230124', '方正县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('841f55cc53da11e68586000c293c07d6', '230125', '宾县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842116a753da11e68586000c293c07d6', '230126', '巴彦县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842308f853da11e68586000c293c07d6', '230127', '木兰县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('8424d00e53da11e68586000c293c07d6', '230128', '通河县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('8426abf753da11e68586000c293c07d6', '230129', '延寿县', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842868b653da11e68586000c293c07d6', '230181', '阿城市', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842a967253da11e68586000c293c07d6', '230182', '双城市', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842ca1fb53da11e68586000c293c07d6', '230183', '尚志市', null, null, '230100');
INSERT INTO `sys_area` VALUES ('842e619f53da11e68586000c293c07d6', '230184', '五常市', null, null, '230100');
INSERT INTO `sys_area` VALUES ('84304fa453da11e68586000c293c07d6', '230200', '齐齐哈尔市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('843274c453da11e68586000c293c07d6', '230201', '市辖区', null, null, '230200');
INSERT INTO `sys_area` VALUES ('84366c1053da11e68586000c293c07d6', '230202', '龙沙区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('84385cfe53da11e68586000c293c07d6', '230203', '建华区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('843a2bc353da11e68586000c293c07d6', '230204', '铁锋区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('843c346e53da11e68586000c293c07d6', '230205', '昂昂溪区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('843e345f53da11e68586000c293c07d6', '230206', '富拉尔基区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('84403d4d53da11e68586000c293c07d6', '230207', '碾子山区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('844200ed53da11e68586000c293c07d6', '230208', '梅里斯达斡尔族区', null, null, '230201');
INSERT INTO `sys_area` VALUES ('8443fb7053da11e68586000c293c07d6', '230221', '龙江县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('8445c90853da11e68586000c293c07d6', '230223', '依安县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('8447874153da11e68586000c293c07d6', '230224', '泰来县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('844949f253da11e68586000c293c07d6', '230225', '甘南县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('844b888b53da11e68586000c293c07d6', '230227', '富裕县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('844d6d3953da11e68586000c293c07d6', '230229', '克山县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('844f285d53da11e68586000c293c07d6', '230230', '克东县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('845137bd53da11e68586000c293c07d6', '230231', '拜泉县', null, null, '230200');
INSERT INTO `sys_area` VALUES ('8453492453da11e68586000c293c07d6', '230281', '讷河市', null, null, '230200');
INSERT INTO `sys_area` VALUES ('84551f9453da11e68586000c293c07d6', '230300', '鸡西市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84572f3c53da11e68586000c293c07d6', '230301', '市辖区', null, null, '230300');
INSERT INTO `sys_area` VALUES ('8459083253da11e68586000c293c07d6', '230302', '鸡冠区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('845b311a53da11e68586000c293c07d6', '230303', '恒山区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('845d4d2a53da11e68586000c293c07d6', '230304', '滴道区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('845f317753da11e68586000c293c07d6', '230305', '梨树区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('84615c9a53da11e68586000c293c07d6', '230306', '城子河区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('8462f3fc53da11e68586000c293c07d6', '230307', '麻山区', null, null, '230301');
INSERT INTO `sys_area` VALUES ('8464dd9053da11e68586000c293c07d6', '230321', '鸡东县', null, null, '230300');
INSERT INTO `sys_area` VALUES ('8466995153da11e68586000c293c07d6', '230381', '虎林市', null, null, '230300');
INSERT INTO `sys_area` VALUES ('8468656853da11e68586000c293c07d6', '230382', '密山市', null, null, '230300');
INSERT INTO `sys_area` VALUES ('846a3fb153da11e68586000c293c07d6', '230400', '鹤岗市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('846ca03b53da11e68586000c293c07d6', '230401', '市辖区', null, null, '230400');
INSERT INTO `sys_area` VALUES ('846e755753da11e68586000c293c07d6', '230402', '向阳区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('847031ff53da11e68586000c293c07d6', '230403', '工农区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('84722a5c53da11e68586000c293c07d6', '230404', '南山区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('8474574d53da11e68586000c293c07d6', '230405', '兴安区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('8476998553da11e68586000c293c07d6', '230406', '东山区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('8478799453da11e68586000c293c07d6', '230407', '兴山区', null, null, '230401');
INSERT INTO `sys_area` VALUES ('847a244b53da11e68586000c293c07d6', '230421', '萝北县', null, null, '230400');
INSERT INTO `sys_area` VALUES ('847c0e5653da11e68586000c293c07d6', '230422', '绥滨县', null, null, '230400');
INSERT INTO `sys_area` VALUES ('847e286753da11e68586000c293c07d6', '230500', '双鸭山市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84800b4953da11e68586000c293c07d6', '230501', '市辖区', null, null, '230500');
INSERT INTO `sys_area` VALUES ('8481ae7a53da11e68586000c293c07d6', '230502', '尖山区', null, null, '230501');
INSERT INTO `sys_area` VALUES ('8483bd5a53da11e68586000c293c07d6', '230503', '岭东区', null, null, '230501');
INSERT INTO `sys_area` VALUES ('8485d9ce53da11e68586000c293c07d6', '230505', '四方台区', null, null, '230501');
INSERT INTO `sys_area` VALUES ('8487acb153da11e68586000c293c07d6', '230506', '宝山区', null, null, '230501');
INSERT INTO `sys_area` VALUES ('84895b7653da11e68586000c293c07d6', '230521', '集贤县', null, null, '230500');
INSERT INTO `sys_area` VALUES ('848b4fd453da11e68586000c293c07d6', '230522', '友谊县', null, null, '230500');
INSERT INTO `sys_area` VALUES ('848d6f0b53da11e68586000c293c07d6', '230523', '宝清县', null, null, '230500');
INSERT INTO `sys_area` VALUES ('848f67a053da11e68586000c293c07d6', '230524', '饶河县', null, null, '230500');
INSERT INTO `sys_area` VALUES ('8491214a53da11e68586000c293c07d6', '230600', '大庆市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('8492fb3a53da11e68586000c293c07d6', '230601', '市辖区', null, null, '230600');
INSERT INTO `sys_area` VALUES ('8494d49253da11e68586000c293c07d6', '230602', '萨尔图区', null, null, '230601');
INSERT INTO `sys_area` VALUES ('8496908453da11e68586000c293c07d6', '230603', '龙凤区', null, null, '230601');
INSERT INTO `sys_area` VALUES ('8498522553da11e68586000c293c07d6', '230604', '让胡路区', null, null, '230601');
INSERT INTO `sys_area` VALUES ('849a6a5853da11e68586000c293c07d6', '230605', '红岗区', null, null, '230601');
INSERT INTO `sys_area` VALUES ('849c35bb53da11e68586000c293c07d6', '230606', '大同区', null, null, '230601');
INSERT INTO `sys_area` VALUES ('849e27b453da11e68586000c293c07d6', '230621', '肇州县', null, null, '230600');
INSERT INTO `sys_area` VALUES ('849fe33653da11e68586000c293c07d6', '230622', '肇源县', null, null, '230600');
INSERT INTO `sys_area` VALUES ('84a1b27053da11e68586000c293c07d6', '230623', '林甸县', null, null, '230600');
INSERT INTO `sys_area` VALUES ('84a38d0353da11e68586000c293c07d6', '230624', '杜尔伯特蒙古族自治县', null, null, '230600');
INSERT INTO `sys_area` VALUES ('84a5388653da11e68586000c293c07d6', '230700', '伊春市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84a72af253da11e68586000c293c07d6', '230701', '市辖区', null, null, '230700');
INSERT INTO `sys_area` VALUES ('84a9201753da11e68586000c293c07d6', '230702', '伊春区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84ab243053da11e68586000c293c07d6', '230703', '南岔区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84ace2ae53da11e68586000c293c07d6', '230704', '友好区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84aec56353da11e68586000c293c07d6', '230705', '西林区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84b1147753da11e68586000c293c07d6', '230706', '翠峦区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84b2ec7553da11e68586000c293c07d6', '230707', '新青区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84b4d5b253da11e68586000c293c07d6', '230708', '美溪区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84b69fb753da11e68586000c293c07d6', '230709', '金山屯区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84b8950553da11e68586000c293c07d6', '230710', '五营区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84ba96b453da11e68586000c293c07d6', '230711', '乌马河区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84bc5c7a53da11e68586000c293c07d6', '230712', '汤旺河区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84bf01ab53da11e68586000c293c07d6', '230713', '带岭区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84c0d3fc53da11e68586000c293c07d6', '230714', '乌伊岭区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84c3d8ee53da11e68586000c293c07d6', '230715', '红星区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84c5b9bc53da11e68586000c293c07d6', '230716', '上甘岭区', null, null, '230701');
INSERT INTO `sys_area` VALUES ('84c7764f53da11e68586000c293c07d6', '230722', '嘉荫县', null, null, '230700');
INSERT INTO `sys_area` VALUES ('84c9693f53da11e68586000c293c07d6', '230781', '铁力市', null, null, '230700');
INSERT INTO `sys_area` VALUES ('84cb294c53da11e68586000c293c07d6', '230800', '佳木斯市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84ccf5ef53da11e68586000c293c07d6', '230801', '市辖区', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84cec7a253da11e68586000c293c07d6', '230803', '向阳区', null, null, '230801');
INSERT INTO `sys_area` VALUES ('84d089f453da11e68586000c293c07d6', '230804', '前进区', null, null, '230801');
INSERT INTO `sys_area` VALUES ('84d2646853da11e68586000c293c07d6', '230805', '东风区', null, null, '230801');
INSERT INTO `sys_area` VALUES ('84d4442853da11e68586000c293c07d6', '230811', '郊区', null, null, '230801');
INSERT INTO `sys_area` VALUES ('84d6593e53da11e68586000c293c07d6', '230822', '桦南县', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84d839ef53da11e68586000c293c07d6', '230826', '桦川县', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84da514f53da11e68586000c293c07d6', '230828', '汤原县', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84dc20bc53da11e68586000c293c07d6', '230833', '抚远县', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84ddf00453da11e68586000c293c07d6', '230881', '同江市', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84dfb82d53da11e68586000c293c07d6', '230882', '富锦市', null, null, '230800');
INSERT INTO `sys_area` VALUES ('84e18d3753da11e68586000c293c07d6', '230900', '七台河市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84e348df53da11e68586000c293c07d6', '230901', '市辖区', null, null, '230900');
INSERT INTO `sys_area` VALUES ('84e517dd53da11e68586000c293c07d6', '230902', '新兴区', null, null, '230901');
INSERT INTO `sys_area` VALUES ('84e6fafa53da11e68586000c293c07d6', '230903', '桃山区', null, null, '230901');
INSERT INTO `sys_area` VALUES ('84e9399c53da11e68586000c293c07d6', '230904', '茄子河区', null, null, '230901');
INSERT INTO `sys_area` VALUES ('84eb22f353da11e68586000c293c07d6', '230921', '勃利县', null, null, '230900');
INSERT INTO `sys_area` VALUES ('84ece5f053da11e68586000c293c07d6', '231000', '牡丹江市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('84eee09f53da11e68586000c293c07d6', '231001', '市辖区', null, null, '231000');
INSERT INTO `sys_area` VALUES ('84f09f6b53da11e68586000c293c07d6', '231002', '东安区', null, null, '231001');
INSERT INTO `sys_area` VALUES ('84f264d053da11e68586000c293c07d6', '231003', '阳明区', null, null, '231001');
INSERT INTO `sys_area` VALUES ('84f40cc153da11e68586000c293c07d6', '231004', '爱民区', null, null, '231001');
INSERT INTO `sys_area` VALUES ('84f5de9353da11e68586000c293c07d6', '231005', '西安区', null, null, '231001');
INSERT INTO `sys_area` VALUES ('84f87ed553da11e68586000c293c07d6', '231024', '东宁县', null, null, '231000');
INSERT INTO `sys_area` VALUES ('84fa429f53da11e68586000c293c07d6', '231025', '林口县', null, null, '231000');
INSERT INTO `sys_area` VALUES ('84fc04d053da11e68586000c293c07d6', '231081', '绥芬河市', null, null, '231000');
INSERT INTO `sys_area` VALUES ('84fe4a7a53da11e68586000c293c07d6', '231083', '海林市', null, null, '231000');
INSERT INTO `sys_area` VALUES ('8500258653da11e68586000c293c07d6', '231084', '宁安市', null, null, '231000');
INSERT INTO `sys_area` VALUES ('85020eb953da11e68586000c293c07d6', '231085', '穆棱市', null, null, '231000');
INSERT INTO `sys_area` VALUES ('8503e59c53da11e68586000c293c07d6', '231100', '黑河市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('8505bab453da11e68586000c293c07d6', '231101', '市辖区', null, null, '231100');
INSERT INTO `sys_area` VALUES ('8507edf153da11e68586000c293c07d6', '231102', '爱辉区', null, null, '231101');
INSERT INTO `sys_area` VALUES ('850a199453da11e68586000c293c07d6', '231121', '嫩江县', null, null, '231100');
INSERT INTO `sys_area` VALUES ('850bfc6453da11e68586000c293c07d6', '231123', '逊克县', null, null, '231100');
INSERT INTO `sys_area` VALUES ('850de5c353da11e68586000c293c07d6', '231124', '孙吴县', null, null, '231100');
INSERT INTO `sys_area` VALUES ('850fc71e53da11e68586000c293c07d6', '231181', '北安市', null, null, '231100');
INSERT INTO `sys_area` VALUES ('8511a06b53da11e68586000c293c07d6', '231182', '五大连池市', null, null, '231100');
INSERT INTO `sys_area` VALUES ('8513a96653da11e68586000c293c07d6', '231200', '绥化市', null, null, '230000');
INSERT INTO `sys_area` VALUES ('851568d753da11e68586000c293c07d6', '231201', '北林区', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8517567153da11e68586000c293c07d6', '231221', '望奎县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8519b08e53da11e68586000c293c07d6', '231222', '兰西县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('851bb0fc53da11e68586000c293c07d6', '231223', '青冈县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('851d570a53da11e68586000c293c07d6', '231224', '庆安县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('851f070553da11e68586000c293c07d6', '231225', '明水县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8520fb1553da11e68586000c293c07d6', '231226', '绥棱县', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8522e3b153da11e68586000c293c07d6', '231281', '安达市', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8524bf7253da11e68586000c293c07d6', '231282', '肇东市', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8526c06153da11e68586000c293c07d6', '231283', '海伦市', null, null, '231200');
INSERT INTO `sys_area` VALUES ('8528b76253da11e68586000c293c07d6', '232700', '大兴安岭地区', null, null, '230000');
INSERT INTO `sys_area` VALUES ('852a8bfa53da11e68586000c293c07d6', '232701', '加格达奇区', null, null, '232700');
INSERT INTO `sys_area` VALUES ('852c62ec53da11e68586000c293c07d6', '232702', '松岭区', null, null, '232700');
INSERT INTO `sys_area` VALUES ('852e493153da11e68586000c293c07d6', '232703', '新林区', null, null, '232700');
INSERT INTO `sys_area` VALUES ('8530392e53da11e68586000c293c07d6', '232704', '呼中区', null, null, '232700');
INSERT INTO `sys_area` VALUES ('8531ec2953da11e68586000c293c07d6', '232721', '呼玛县', null, null, '232700');
INSERT INTO `sys_area` VALUES ('8533d5f953da11e68586000c293c07d6', '232722', '塔河县', null, null, '232700');
INSERT INTO `sys_area` VALUES ('85363d2353da11e68586000c293c07d6', '232723', '漠河县', null, null, '232700');
INSERT INTO `sys_area` VALUES ('85382d4853da11e68586000c293c07d6', '310000', '上海市', null, '9', '0');
INSERT INTO `sys_area` VALUES ('853a188d53da11e68586000c293c07d6', '310100', '黄浦区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('853bd15353da11e68586000c293c07d6', '310300', '卢湾区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('853da54353da11e68586000c293c07d6', '310400', '徐汇区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('853fa8f853da11e68586000c293c07d6', '310500', '长宁区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('854195fe53da11e68586000c293c07d6', '310600', '静安区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('8543633c53da11e68586000c293c07d6', '310700', '普陀区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('85454e3e53da11e68586000c293c07d6', '310800', '闸北区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('854732d553da11e68586000c293c07d6', '310900', '虹口区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('8549332753da11e68586000c293c07d6', '311000', '杨浦区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('854b568353da11e68586000c293c07d6', '311200', '闵行区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('854d388653da11e68586000c293c07d6', '311300', '宝山区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('85511dc653da11e68586000c293c07d6', '311400', '嘉定区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('8552ea4153da11e68586000c293c07d6', '311500', '浦东新区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('85553b5753da11e68586000c293c07d6', '311600', '金山区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('85570a3653da11e68586000c293c07d6', '311700', '松江区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('8558d7e153da11e68586000c293c07d6', '311800', '青浦区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('855aa77753da11e68586000c293c07d6', '311900', '南汇区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('855c735153da11e68586000c293c07d6', '312000', '奉贤区', null, null, '310000');
INSERT INTO `sys_area` VALUES ('855e61d753da11e68586000c293c07d6', '313000', '崇明县', null, null, '310000');
INSERT INTO `sys_area` VALUES ('8560287c53da11e68586000c293c07d6', '320000', '江苏省', null, '10', '0');
INSERT INTO `sys_area` VALUES ('856225f953da11e68586000c293c07d6', '320100', '南京市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8564d38d53da11e68586000c293c07d6', '320101', '市辖区', null, null, '320100');
INSERT INTO `sys_area` VALUES ('8566bb2153da11e68586000c293c07d6', '320102', '玄武区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('856888a453da11e68586000c293c07d6', '320103', '白下区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('856a652153da11e68586000c293c07d6', '320104', '秦淮区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('856c73dc53da11e68586000c293c07d6', '320105', '建邺区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('856e168c53da11e68586000c293c07d6', '320106', '鼓楼区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('856ff4d053da11e68586000c293c07d6', '320107', '下关区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('8571cfb253da11e68586000c293c07d6', '320111', '浦口区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('8573e37453da11e68586000c293c07d6', '320113', '栖霞区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('8575a2c453da11e68586000c293c07d6', '320114', '雨花台区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('8577b48253da11e68586000c293c07d6', '320115', '江宁区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('8579788e53da11e68586000c293c07d6', '320116', '六合区', null, null, '320101');
INSERT INTO `sys_area` VALUES ('857b5e1753da11e68586000c293c07d6', '320124', '溧水区', null, null, '320100');
INSERT INTO `sys_area` VALUES ('857d417353da11e68586000c293c07d6', '320125', '高淳区', null, null, '320100');
INSERT INTO `sys_area` VALUES ('857f084553da11e68586000c293c07d6', '320200', '无锡市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8581071653da11e68586000c293c07d6', '320201', '市辖区', null, null, '320200');
INSERT INTO `sys_area` VALUES ('85838e3953da11e68586000c293c07d6', '320202', '崇安区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('85854bd253da11e68586000c293c07d6', '320203', '南长区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('8587207a53da11e68586000c293c07d6', '320204', '北塘区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('8588f1af53da11e68586000c293c07d6', '320205', '锡山区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('858abedc53da11e68586000c293c07d6', '320206', '惠山区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('858cb21153da11e68586000c293c07d6', '320211', '滨湖区', null, null, '320201');
INSERT INTO `sys_area` VALUES ('858eac2a53da11e68586000c293c07d6', '320281', '江阴市', null, null, '320200');
INSERT INTO `sys_area` VALUES ('85908cf653da11e68586000c293c07d6', '320282', '宜兴市', null, null, '320200');
INSERT INTO `sys_area` VALUES ('859298d553da11e68586000c293c07d6', '320300', '徐州市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('85943f5d53da11e68586000c293c07d6', '320301', '泉山区', null, null, '320300');
INSERT INTO `sys_area` VALUES ('8596563853da11e68586000c293c07d6', '320302', '鼓楼区', null, null, '320300');
INSERT INTO `sys_area` VALUES ('859833f253da11e68586000c293c07d6', '320303', '云龙区', null, null, '320300');
INSERT INTO `sys_area` VALUES ('859a353d53da11e68586000c293c07d6', '320304', '九里区', null, null, '320301');
INSERT INTO `sys_area` VALUES ('859c7df953da11e68586000c293c07d6', '320305', '贾汪区', null, null, '320300');
INSERT INTO `sys_area` VALUES ('859e6dc553da11e68586000c293c07d6', '320321', '丰县', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85a07ea353da11e68586000c293c07d6', '320322', '沛县', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85a24c7a53da11e68586000c293c07d6', '320323', '铜山县', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85a4633753da11e68586000c293c07d6', '320324', '睢宁县', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85a6193053da11e68586000c293c07d6', '320381', '新沂市', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85a7e6f153da11e68586000c293c07d6', '320382', '邳州市', null, null, '320300');
INSERT INTO `sys_area` VALUES ('85aa270b53da11e68586000c293c07d6', '320400', '常州市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('85ac213353da11e68586000c293c07d6', '320401', '市辖区', null, null, '320400');
INSERT INTO `sys_area` VALUES ('85addcb953da11e68586000c293c07d6', '320402', '天宁区', null, null, '320401');
INSERT INTO `sys_area` VALUES ('85afc0e853da11e68586000c293c07d6', '320404', '钟楼区', null, null, '320401');
INSERT INTO `sys_area` VALUES ('85b1be9953da11e68586000c293c07d6', '320405', '戚墅堰区', null, null, '320401');
INSERT INTO `sys_area` VALUES ('85b38e7753da11e68586000c293c07d6', '320411', '新北区', null, null, '320401');
INSERT INTO `sys_area` VALUES ('85b551e153da11e68586000c293c07d6', '320412', '武进区', null, null, '320401');
INSERT INTO `sys_area` VALUES ('85b72e6653da11e68586000c293c07d6', '320481', '溧阳市', null, null, '320400');
INSERT INTO `sys_area` VALUES ('85b8f02753da11e68586000c293c07d6', '320482', '金坛市', null, null, '320400');
INSERT INTO `sys_area` VALUES ('85baece653da11e68586000c293c07d6', '320500', '苏州市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('85bd4bdf53da11e68586000c293c07d6', '320501', '市辖区', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85bfc15b53da11e68586000c293c07d6', '320502', '沧浪区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85c2a75c53da11e68586000c293c07d6', '320503', '平江区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85c51ed353da11e68586000c293c07d6', '320504', '金阊区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85c75db253da11e68586000c293c07d6', '320505', '虎丘区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85c9c2cd53da11e68586000c293c07d6', '320506', '吴中区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85cc388f53da11e68586000c293c07d6', '320507', '相城区', null, null, '320501');
INSERT INTO `sys_area` VALUES ('85cea31053da11e68586000c293c07d6', '320581', '常熟市', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85d1563653da11e68586000c293c07d6', '320582', '张家港市', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85d3980553da11e68586000c293c07d6', '320583', '昆山市', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85d5a2e953da11e68586000c293c07d6', '320584', '吴江市', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85d865a553da11e68586000c293c07d6', '320585', '太仓市', null, null, '320500');
INSERT INTO `sys_area` VALUES ('85da974953da11e68586000c293c07d6', '320600', '南通市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('85dc7ae053da11e68586000c293c07d6', '320601', '市辖区', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85de3a4153da11e68586000c293c07d6', '320602', '崇川区', null, null, '320601');
INSERT INTO `sys_area` VALUES ('85e141c053da11e68586000c293c07d6', '320611', '港闸区', null, null, '320601');
INSERT INTO `sys_area` VALUES ('85e313f353da11e68586000c293c07d6', '320621', '海安县', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85e4eed053da11e68586000c293c07d6', '320623', '如东县', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85e6fde853da11e68586000c293c07d6', '320681', '启东市', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85e932c453da11e68586000c293c07d6', '320682', '如皋市', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85eb300153da11e68586000c293c07d6', '320683', '通州市', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85ed211253da11e68586000c293c07d6', '320684', '海门市', null, null, '320600');
INSERT INTO `sys_area` VALUES ('85eefd3d53da11e68586000c293c07d6', '320700', '连云港市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('85f1064553da11e68586000c293c07d6', '320701', '市辖区', null, null, '320700');
INSERT INTO `sys_area` VALUES ('85f2cb0f53da11e68586000c293c07d6', '320703', '连云区', null, null, '320701');
INSERT INTO `sys_area` VALUES ('85f4cd6053da11e68586000c293c07d6', '320705', '新浦区', null, null, '320701');
INSERT INTO `sys_area` VALUES ('85f6a80c53da11e68586000c293c07d6', '320706', '海州区', null, null, '320701');
INSERT INTO `sys_area` VALUES ('85f876df53da11e68586000c293c07d6', '320721', '赣榆县', null, null, '320700');
INSERT INTO `sys_area` VALUES ('85fa14a153da11e68586000c293c07d6', '320722', '东海县', null, null, '320700');
INSERT INTO `sys_area` VALUES ('85fbf61953da11e68586000c293c07d6', '320723', '灌云县', null, null, '320700');
INSERT INTO `sys_area` VALUES ('85fdc46853da11e68586000c293c07d6', '320724', '灌南县', null, null, '320700');
INSERT INTO `sys_area` VALUES ('85ff9c0753da11e68586000c293c07d6', '320800', '淮安市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8601765253da11e68586000c293c07d6', '320801', '市辖区', null, null, '320800');
INSERT INTO `sys_area` VALUES ('86035aa953da11e68586000c293c07d6', '320802', '清河区', null, null, '320801');
INSERT INTO `sys_area` VALUES ('860554fb53da11e68586000c293c07d6', '320803', '楚州区', null, null, '320801');
INSERT INTO `sys_area` VALUES ('86072c9f53da11e68586000c293c07d6', '320804', '淮阴区', null, null, '320801');
INSERT INTO `sys_area` VALUES ('8608ff3b53da11e68586000c293c07d6', '320811', '清浦区', null, null, '320801');
INSERT INTO `sys_area` VALUES ('860afcf653da11e68586000c293c07d6', '320826', '涟水县', null, null, '320800');
INSERT INTO `sys_area` VALUES ('860d007f53da11e68586000c293c07d6', '320829', '洪泽县', null, null, '320800');
INSERT INTO `sys_area` VALUES ('860ed5d253da11e68586000c293c07d6', '320830', '盱眙县', null, null, '320800');
INSERT INTO `sys_area` VALUES ('8611008d53da11e68586000c293c07d6', '320831', '金湖县', null, null, '320800');
INSERT INTO `sys_area` VALUES ('8612ef6053da11e68586000c293c07d6', '320900', '盐城市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8614c6fd53da11e68586000c293c07d6', '320901', '市辖区', null, null, '320900');
INSERT INTO `sys_area` VALUES ('8616bdf653da11e68586000c293c07d6', '320902', '亭湖区', null, null, '320901');
INSERT INTO `sys_area` VALUES ('8618899c53da11e68586000c293c07d6', '320903', '盐都区', null, null, '320901');
INSERT INTO `sys_area` VALUES ('861a823b53da11e68586000c293c07d6', '320921', '响水县', null, null, '320900');
INSERT INTO `sys_area` VALUES ('861c5faa53da11e68586000c293c07d6', '320922', '滨海县', null, null, '320900');
INSERT INTO `sys_area` VALUES ('861e41c253da11e68586000c293c07d6', '320923', '阜宁县', null, null, '320900');
INSERT INTO `sys_area` VALUES ('86203df953da11e68586000c293c07d6', '320924', '射阳县', null, null, '320900');
INSERT INTO `sys_area` VALUES ('8622264053da11e68586000c293c07d6', '320925', '建湖县', null, null, '320900');
INSERT INTO `sys_area` VALUES ('8624188c53da11e68586000c293c07d6', '320981', '东台市', null, null, '320900');
INSERT INTO `sys_area` VALUES ('86260e9453da11e68586000c293c07d6', '320982', '大丰市', null, null, '320900');
INSERT INTO `sys_area` VALUES ('8627d0e353da11e68586000c293c07d6', '321000', '扬州市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8629a08153da11e68586000c293c07d6', '321001', '市辖区', null, null, '321000');
INSERT INTO `sys_area` VALUES ('862b838453da11e68586000c293c07d6', '321002', '广陵区', null, null, '321001');
INSERT INTO `sys_area` VALUES ('862d29fb53da11e68586000c293c07d6', '321003', '邗江区', null, null, '321001');
INSERT INTO `sys_area` VALUES ('862eef3f53da11e68586000c293c07d6', '321011', '维扬区', null, null, '321001');
INSERT INTO `sys_area` VALUES ('8630dd7153da11e68586000c293c07d6', '321023', '宝应县', null, null, '321000');
INSERT INTO `sys_area` VALUES ('8632e25f53da11e68586000c293c07d6', '321081', '仪征市', null, null, '321000');
INSERT INTO `sys_area` VALUES ('8634beab53da11e68586000c293c07d6', '321084', '高邮市', null, null, '321000');
INSERT INTO `sys_area` VALUES ('8636fc2f53da11e68586000c293c07d6', '321088', '江都市', null, null, '321000');
INSERT INTO `sys_area` VALUES ('8638d44a53da11e68586000c293c07d6', '321100', '镇江市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('863a90f553da11e68586000c293c07d6', '321101', '市辖区', null, null, '321100');
INSERT INTO `sys_area` VALUES ('863c421753da11e68586000c293c07d6', '321102', '京口区', null, null, '321101');
INSERT INTO `sys_area` VALUES ('863e37a953da11e68586000c293c07d6', '321111', '润州区', null, null, '321101');
INSERT INTO `sys_area` VALUES ('86406f4653da11e68586000c293c07d6', '321112', '丹徒区', null, null, '321101');
INSERT INTO `sys_area` VALUES ('8642950853da11e68586000c293c07d6', '321181', '丹阳市', null, null, '321100');
INSERT INTO `sys_area` VALUES ('86443d6a53da11e68586000c293c07d6', '321182', '扬中市', null, null, '321100');
INSERT INTO `sys_area` VALUES ('8645faf353da11e68586000c293c07d6', '321183', '句容市', null, null, '321100');
INSERT INTO `sys_area` VALUES ('8648170253da11e68586000c293c07d6', '321200', '泰州市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('864a032b53da11e68586000c293c07d6', '321201', '市辖区', null, null, '321200');
INSERT INTO `sys_area` VALUES ('864baeb453da11e68586000c293c07d6', '321202', '海陵区', null, null, '321201');
INSERT INTO `sys_area` VALUES ('864d791d53da11e68586000c293c07d6', '321203', '高港区', null, null, '321201');
INSERT INTO `sys_area` VALUES ('864facb853da11e68586000c293c07d6', '321281', '兴化市', null, null, '321200');
INSERT INTO `sys_area` VALUES ('86523e7e53da11e68586000c293c07d6', '321282', '靖江市', null, null, '321200');
INSERT INTO `sys_area` VALUES ('8654218c53da11e68586000c293c07d6', '321283', '泰兴市', null, null, '321200');
INSERT INTO `sys_area` VALUES ('8656139453da11e68586000c293c07d6', '321284', '姜堰市', null, null, '321200');
INSERT INTO `sys_area` VALUES ('8657ddd053da11e68586000c293c07d6', '321300', '宿迁市', null, null, '320000');
INSERT INTO `sys_area` VALUES ('8659942353da11e68586000c293c07d6', '321301', '市辖区', null, null, '321300');
INSERT INTO `sys_area` VALUES ('865b6bb553da11e68586000c293c07d6', '321302', '宿城区', null, null, '321301');
INSERT INTO `sys_area` VALUES ('865d164253da11e68586000c293c07d6', '321311', '宿豫区', null, null, '321301');
INSERT INTO `sys_area` VALUES ('865eef0253da11e68586000c293c07d6', '321322', '沭阳县', null, null, '321300');
INSERT INTO `sys_area` VALUES ('86610a9353da11e68586000c293c07d6', '321323', '泗阳县', null, null, '321300');
INSERT INTO `sys_area` VALUES ('8662cfce53da11e68586000c293c07d6', '321324', '泗洪县', null, null, '321300');
INSERT INTO `sys_area` VALUES ('8664a3d453da11e68586000c293c07d6', '330000', '浙江省', null, '11', '0');
INSERT INTO `sys_area` VALUES ('8666e5e853da11e68586000c293c07d6', '330100', '杭州市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('8668a97053da11e68586000c293c07d6', '330101', '市辖区', null, null, '330100');
INSERT INTO `sys_area` VALUES ('866a685253da11e68586000c293c07d6', '330102', '上城区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('866c240c53da11e68586000c293c07d6', '330103', '下城区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('866e0abc53da11e68586000c293c07d6', '330104', '江干区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('866ffb8853da11e68586000c293c07d6', '330105', '拱墅区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('8671d36f53da11e68586000c293c07d6', '330106', '西湖区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('8673a60a53da11e68586000c293c07d6', '330108', '滨江区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('8675d81953da11e68586000c293c07d6', '330109', '萧山区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('8677bd7653da11e68586000c293c07d6', '330110', '余杭区', null, null, '330101');
INSERT INTO `sys_area` VALUES ('8679a7ce53da11e68586000c293c07d6', '330122', '桐庐县', null, null, '330100');
INSERT INTO `sys_area` VALUES ('867bbf4953da11e68586000c293c07d6', '330127', '淳安县', null, null, '330100');
INSERT INTO `sys_area` VALUES ('867d8d8053da11e68586000c293c07d6', '330182', '建德市', null, null, '330100');
INSERT INTO `sys_area` VALUES ('867f81ca53da11e68586000c293c07d6', '330183', '富阳市', null, null, '330100');
INSERT INTO `sys_area` VALUES ('8681475d53da11e68586000c293c07d6', '330185', '临安市', null, null, '330100');
INSERT INTO `sys_area` VALUES ('86834f4453da11e68586000c293c07d6', '330200', '宁波市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86851d8853da11e68586000c293c07d6', '330201', '市辖区', null, null, '330200');
INSERT INTO `sys_area` VALUES ('868735ba53da11e68586000c293c07d6', '330203', '海曙区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('8688e93653da11e68586000c293c07d6', '330204', '江东区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('868ad61953da11e68586000c293c07d6', '330205', '江北区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('868cf5d253da11e68586000c293c07d6', '330206', '北仑区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('868f509b53da11e68586000c293c07d6', '330211', '镇海区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('8692209c53da11e68586000c293c07d6', '330212', '鄞州区', null, null, '330201');
INSERT INTO `sys_area` VALUES ('86940c3653da11e68586000c293c07d6', '330225', '象山县', null, null, '330200');
INSERT INTO `sys_area` VALUES ('869635eb53da11e68586000c293c07d6', '330226', '宁海县', null, null, '330200');
INSERT INTO `sys_area` VALUES ('86981b8453da11e68586000c293c07d6', '330281', '余姚市', null, null, '330200');
INSERT INTO `sys_area` VALUES ('8699d2e153da11e68586000c293c07d6', '330282', '慈溪市', null, null, '330200');
INSERT INTO `sys_area` VALUES ('869c006c53da11e68586000c293c07d6', '330283', '奉化市', null, null, '330200');
INSERT INTO `sys_area` VALUES ('869e035c53da11e68586000c293c07d6', '330300', '温州市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('869fef9953da11e68586000c293c07d6', '330301', '市辖区', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86a1bdb153da11e68586000c293c07d6', '330302', '鹿城区', null, null, '330301');
INSERT INTO `sys_area` VALUES ('86a390e453da11e68586000c293c07d6', '330303', '龙湾区', null, null, '330301');
INSERT INTO `sys_area` VALUES ('86a5629753da11e68586000c293c07d6', '330304', '瓯海区', null, null, '330301');
INSERT INTO `sys_area` VALUES ('86a71b9753da11e68586000c293c07d6', '330322', '洞头县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86a9092453da11e68586000c293c07d6', '330324', '永嘉县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86aae95a53da11e68586000c293c07d6', '330326', '平阳县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86acc65c53da11e68586000c293c07d6', '330327', '苍南县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86ae8c8153da11e68586000c293c07d6', '330328', '文成县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86b088c853da11e68586000c293c07d6', '330329', '泰顺县', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86b250d053da11e68586000c293c07d6', '330381', '瑞安市', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86b423a253da11e68586000c293c07d6', '330382', '乐清市', null, null, '330300');
INSERT INTO `sys_area` VALUES ('86b5ea6653da11e68586000c293c07d6', '330400', '嘉兴市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86b7c79f53da11e68586000c293c07d6', '330401', '市辖区', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86b993aa53da11e68586000c293c07d6', '330402', '南湖区', null, null, '330401');
INSERT INTO `sys_area` VALUES ('86bb703453da11e68586000c293c07d6', '330411', '秀洲区', null, null, '330401');
INSERT INTO `sys_area` VALUES ('86be12dc53da11e68586000c293c07d6', '330421', '嘉善县', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86bfe72853da11e68586000c293c07d6', '330424', '海盐县', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86c1b3c553da11e68586000c293c07d6', '330481', '海宁市', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86c3a4dd53da11e68586000c293c07d6', '330482', '平湖市', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86c5859653da11e68586000c293c07d6', '330483', '桐乡市', null, null, '330400');
INSERT INTO `sys_area` VALUES ('86c7457f53da11e68586000c293c07d6', '330500', '湖州市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86c966cd53da11e68586000c293c07d6', '330501', '市辖区', null, null, '330500');
INSERT INTO `sys_area` VALUES ('86cb20a653da11e68586000c293c07d6', '330502', '吴兴区', null, null, '330501');
INSERT INTO `sys_area` VALUES ('86cd117153da11e68586000c293c07d6', '330503', '南浔区', null, null, '330501');
INSERT INTO `sys_area` VALUES ('86cf388a53da11e68586000c293c07d6', '330521', '德清县', null, null, '330500');
INSERT INTO `sys_area` VALUES ('86d1067253da11e68586000c293c07d6', '330522', '长兴县', null, null, '330500');
INSERT INTO `sys_area` VALUES ('86d2b5b353da11e68586000c293c07d6', '330523', '安吉县', null, null, '330500');
INSERT INTO `sys_area` VALUES ('86d46ead53da11e68586000c293c07d6', '330600', '绍兴市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86d64f9153da11e68586000c293c07d6', '330601', '市辖区', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86d823ab53da11e68586000c293c07d6', '330602', '越城区', null, null, '330601');
INSERT INTO `sys_area` VALUES ('86d9f62153da11e68586000c293c07d6', '330621', '绍兴县', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86dbf5e153da11e68586000c293c07d6', '330624', '新昌县', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86dddda153da11e68586000c293c07d6', '330681', '诸暨市', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86dfb8b353da11e68586000c293c07d6', '330682', '上虞市', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86e1ad9553da11e68586000c293c07d6', '330683', '嵊州市', null, null, '330600');
INSERT INTO `sys_area` VALUES ('86e382c353da11e68586000c293c07d6', '330700', '金华市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86e5785153da11e68586000c293c07d6', '330701', '市辖区', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86e769b653da11e68586000c293c07d6', '330702', '婺城区', null, null, '330701');
INSERT INTO `sys_area` VALUES ('86e93e9d53da11e68586000c293c07d6', '330703', '金东区', null, null, '330701');
INSERT INTO `sys_area` VALUES ('86eb278953da11e68586000c293c07d6', '330723', '武义县', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86ece8f953da11e68586000c293c07d6', '330726', '浦江县', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86ee93b753da11e68586000c293c07d6', '330727', '磐安县', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86f0738253da11e68586000c293c07d6', '330781', '兰溪市', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86f23ef053da11e68586000c293c07d6', '330782', '义乌市', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86f5117053da11e68586000c293c07d6', '330783', '东阳市', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86f6dd4053da11e68586000c293c07d6', '330784', '永康市', null, null, '330700');
INSERT INTO `sys_area` VALUES ('86f8b6e253da11e68586000c293c07d6', '330800', '衢州市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('86fa827a53da11e68586000c293c07d6', '330801', '市辖区', null, null, '330800');
INSERT INTO `sys_area` VALUES ('86fceb7d53da11e68586000c293c07d6', '330802', '柯城区', null, null, '330801');
INSERT INTO `sys_area` VALUES ('86feb79553da11e68586000c293c07d6', '330803', '衢江区', null, null, '330801');
INSERT INTO `sys_area` VALUES ('8700a82853da11e68586000c293c07d6', '330822', '常山县', null, null, '330800');
INSERT INTO `sys_area` VALUES ('8702615653da11e68586000c293c07d6', '330824', '开化县', null, null, '330800');
INSERT INTO `sys_area` VALUES ('870434d553da11e68586000c293c07d6', '330825', '龙游县', null, null, '330800');
INSERT INTO `sys_area` VALUES ('8706091953da11e68586000c293c07d6', '330881', '江山市', null, null, '330800');
INSERT INTO `sys_area` VALUES ('8707d11153da11e68586000c293c07d6', '330900', '舟山市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('8709ab3953da11e68586000c293c07d6', '330901', '市辖区', null, null, '330900');
INSERT INTO `sys_area` VALUES ('870b966253da11e68586000c293c07d6', '330902', '定海区', null, null, '330901');
INSERT INTO `sys_area` VALUES ('870d9b7053da11e68586000c293c07d6', '330903', '普陀区', null, null, '330901');
INSERT INTO `sys_area` VALUES ('870f59fb53da11e68586000c293c07d6', '330921', '岱山县', null, null, '330900');
INSERT INTO `sys_area` VALUES ('8711593a53da11e68586000c293c07d6', '330922', '嵊泗县', null, null, '330900');
INSERT INTO `sys_area` VALUES ('87132a2153da11e68586000c293c07d6', '331000', '台州市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('871505c753da11e68586000c293c07d6', '331001', '市辖区', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8716ea8153da11e68586000c293c07d6', '331002', '椒江区', null, null, '331001');
INSERT INTO `sys_area` VALUES ('8718cbd153da11e68586000c293c07d6', '331003', '黄岩区', null, null, '331001');
INSERT INTO `sys_area` VALUES ('871ab5db53da11e68586000c293c07d6', '331004', '路桥区', null, null, '331001');
INSERT INTO `sys_area` VALUES ('871c91a953da11e68586000c293c07d6', '331021', '玉环县', null, null, '331000');
INSERT INTO `sys_area` VALUES ('871e620353da11e68586000c293c07d6', '331022', '三门县', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8720304d53da11e68586000c293c07d6', '331023', '天台县', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8722279c53da11e68586000c293c07d6', '331024', '仙居县', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8723df1e53da11e68586000c293c07d6', '331081', '温岭市', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8725ebd053da11e68586000c293c07d6', '331082', '临海市', null, null, '331000');
INSERT INTO `sys_area` VALUES ('8727bde053da11e68586000c293c07d6', '331100', '丽水市', null, null, '330000');
INSERT INTO `sys_area` VALUES ('8729ba1253da11e68586000c293c07d6', '331101', '市辖区', null, null, '331100');
INSERT INTO `sys_area` VALUES ('872c3abc53da11e68586000c293c07d6', '331102', '莲都区', null, null, '331101');
INSERT INTO `sys_area` VALUES ('872e55b153da11e68586000c293c07d6', '331121', '青田县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('8730085953da11e68586000c293c07d6', '331122', '缙云县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('8731fd5853da11e68586000c293c07d6', '331123', '遂昌县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('8733d5a353da11e68586000c293c07d6', '331124', '松阳县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('87382e5853da11e68586000c293c07d6', '331125', '云和县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('8739f06353da11e68586000c293c07d6', '331126', '庆元县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('873bd07d53da11e68586000c293c07d6', '331127', '景宁畲族自治县', null, null, '331100');
INSERT INTO `sys_area` VALUES ('873dae3d53da11e68586000c293c07d6', '331181', '龙泉市', null, null, '331100');
INSERT INTO `sys_area` VALUES ('873fa1f053da11e68586000c293c07d6', '340000', '安徽省', null, '12', '0');
INSERT INTO `sys_area` VALUES ('8741816b53da11e68586000c293c07d6', '340100', '合肥市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87434a3b53da11e68586000c293c07d6', '340101', '市辖区', null, null, '340100');
INSERT INTO `sys_area` VALUES ('8745370753da11e68586000c293c07d6', '340102', '瑶海区', null, null, '340101');
INSERT INTO `sys_area` VALUES ('8746d95f53da11e68586000c293c07d6', '340103', '庐阳区', null, null, '340101');
INSERT INTO `sys_area` VALUES ('8748c5f353da11e68586000c293c07d6', '340104', '蜀山区', null, null, '340101');
INSERT INTO `sys_area` VALUES ('874a97d553da11e68586000c293c07d6', '340111', '包河区', null, null, '340101');
INSERT INTO `sys_area` VALUES ('874c935a53da11e68586000c293c07d6', '340121', '长丰县', null, null, '340100');
INSERT INTO `sys_area` VALUES ('874e651053da11e68586000c293c07d6', '340122', '肥东县', null, null, '340100');
INSERT INTO `sys_area` VALUES ('87505bce53da11e68586000c293c07d6', '340123', '肥西县', null, null, '340100');
INSERT INTO `sys_area` VALUES ('8752436b53da11e68586000c293c07d6', '340124', '庐江县', null, null, '340100');
INSERT INTO `sys_area` VALUES ('8753f83b53da11e68586000c293c07d6', '340181', '巢湖市', null, null, '340100');
INSERT INTO `sys_area` VALUES ('8755d73853da11e68586000c293c07d6', '340200', '芜湖市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('8757bc7653da11e68586000c293c07d6', '340201', '市辖区', null, null, '340200');
INSERT INTO `sys_area` VALUES ('875aeafe53da11e68586000c293c07d6', '340202', '镜湖区', null, null, '340201');
INSERT INTO `sys_area` VALUES ('875cc98953da11e68586000c293c07d6', '340203', '马塘区', null, null, '340201');
INSERT INTO `sys_area` VALUES ('875e83a253da11e68586000c293c07d6', '340207', '鸠江区', null, null, '340201');
INSERT INTO `sys_area` VALUES ('87602c9953da11e68586000c293c07d6', '340221', '芜湖县', null, null, '340200');
INSERT INTO `sys_area` VALUES ('87620fc853da11e68586000c293c07d6', '340222', '繁昌县', null, null, '340200');
INSERT INTO `sys_area` VALUES ('8763e2b553da11e68586000c293c07d6', '340223', '南陵县', null, null, '340200');
INSERT INTO `sys_area` VALUES ('87659af453da11e68586000c293c07d6', '340225', '无为县', null, null, '340200');
INSERT INTO `sys_area` VALUES ('8767b53753da11e68586000c293c07d6', '340300', '蚌埠市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('8769c03453da11e68586000c293c07d6', '340301', '市辖区', null, null, '340300');
INSERT INTO `sys_area` VALUES ('876bbe2253da11e68586000c293c07d6', '340302', '龙子湖区', null, null, '340301');
INSERT INTO `sys_area` VALUES ('876d954053da11e68586000c293c07d6', '340303', '蚌山区', null, null, '340301');
INSERT INTO `sys_area` VALUES ('876f616953da11e68586000c293c07d6', '340304', '禹会区', null, null, '340301');
INSERT INTO `sys_area` VALUES ('87717e1853da11e68586000c293c07d6', '340311', '淮上区', null, null, '340301');
INSERT INTO `sys_area` VALUES ('877345ca53da11e68586000c293c07d6', '340321', '怀远县', null, null, '340300');
INSERT INTO `sys_area` VALUES ('8775289053da11e68586000c293c07d6', '340322', '五河县', null, null, '340300');
INSERT INTO `sys_area` VALUES ('8776ea1453da11e68586000c293c07d6', '340323', '固镇县', null, null, '340300');
INSERT INTO `sys_area` VALUES ('8778cb2453da11e68586000c293c07d6', '340400', '淮南市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('877ae2ce53da11e68586000c293c07d6', '340401', '市辖区', null, null, '340400');
INSERT INTO `sys_area` VALUES ('877cce8053da11e68586000c293c07d6', '340402', '大通区', null, null, '340401');
INSERT INTO `sys_area` VALUES ('877e90a853da11e68586000c293c07d6', '340403', '田家庵区', null, null, '340401');
INSERT INTO `sys_area` VALUES ('87806f8253da11e68586000c293c07d6', '340404', '谢家集区', null, null, '340401');
INSERT INTO `sys_area` VALUES ('8782400153da11e68586000c293c07d6', '340405', '八公山区', null, null, '340401');
INSERT INTO `sys_area` VALUES ('8784362b53da11e68586000c293c07d6', '340406', '潘集区', null, null, '340401');
INSERT INTO `sys_area` VALUES ('8786832953da11e68586000c293c07d6', '340421', '凤台县', null, null, '340400');
INSERT INTO `sys_area` VALUES ('8788be8253da11e68586000c293c07d6', '340500', '马鞍山市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('878b4f3c53da11e68586000c293c07d6', '340501', '市辖区', null, null, '340500');
INSERT INTO `sys_area` VALUES ('878da63d53da11e68586000c293c07d6', '340502', '金家庄区', null, null, '340501');
INSERT INTO `sys_area` VALUES ('878fbe9a53da11e68586000c293c07d6', '340503', '花山区', null, null, '340501');
INSERT INTO `sys_area` VALUES ('87922d3653da11e68586000c293c07d6', '340504', '雨山区', null, null, '340501');
INSERT INTO `sys_area` VALUES ('8794565553da11e68586000c293c07d6', '340521', '当涂县', null, null, '340500');
INSERT INTO `sys_area` VALUES ('8796e9f553da11e68586000c293c07d6', '340522', '含山县', null, null, '340500');
INSERT INTO `sys_area` VALUES ('87993d9253da11e68586000c293c07d6', '340523', '和县', null, null, '340500');
INSERT INTO `sys_area` VALUES ('879b986c53da11e68586000c293c07d6', '340600', '淮北市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('879df19b53da11e68586000c293c07d6', '340601', '市辖区', null, null, '340600');
INSERT INTO `sys_area` VALUES ('879fef0753da11e68586000c293c07d6', '340602', '杜集区', null, null, '340601');
INSERT INTO `sys_area` VALUES ('87a1ae7153da11e68586000c293c07d6', '340603', '相山区', null, null, '340601');
INSERT INTO `sys_area` VALUES ('87a42a7453da11e68586000c293c07d6', '340604', '烈山区', null, null, '340601');
INSERT INTO `sys_area` VALUES ('87a62fe653da11e68586000c293c07d6', '340621', '濉溪县', null, null, '340600');
INSERT INTO `sys_area` VALUES ('87a866f653da11e68586000c293c07d6', '340700', '铜陵市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87aacd5a53da11e68586000c293c07d6', '340701', '市辖区', null, null, '340700');
INSERT INTO `sys_area` VALUES ('87acb33553da11e68586000c293c07d6', '340702', '铜官山区', null, null, '340701');
INSERT INTO `sys_area` VALUES ('87ae5a8f53da11e68586000c293c07d6', '340703', '狮子山区', null, null, '340701');
INSERT INTO `sys_area` VALUES ('87b04f2a53da11e68586000c293c07d6', '340711', '郊区', null, null, '340701');
INSERT INTO `sys_area` VALUES ('87b25ba753da11e68586000c293c07d6', '340721', '铜陵县', null, null, '340700');
INSERT INTO `sys_area` VALUES ('87b497d353da11e68586000c293c07d6', '340800', '安庆市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87b65eaf53da11e68586000c293c07d6', '340801', '市辖区', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87b7e65a53da11e68586000c293c07d6', '340802', '迎江区', null, null, '340801');
INSERT INTO `sys_area` VALUES ('87b9e8a753da11e68586000c293c07d6', '340803', '大观区', null, null, '340801');
INSERT INTO `sys_area` VALUES ('87bbc56653da11e68586000c293c07d6', '340811', '宜秀区', null, null, '340801');
INSERT INTO `sys_area` VALUES ('87bd960753da11e68586000c293c07d6', '340822', '怀宁县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87bf678753da11e68586000c293c07d6', '340823', '枞阳县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87c132a053da11e68586000c293c07d6', '340824', '潜山县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87c31f4253da11e68586000c293c07d6', '340825', '太湖县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87c4f06f53da11e68586000c293c07d6', '340826', '宿松县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87c6c6e753da11e68586000c293c07d6', '340827', '望江县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87c8aa8053da11e68586000c293c07d6', '340828', '岳西县', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87cac4d053da11e68586000c293c07d6', '340881', '桐城市', null, null, '340800');
INSERT INTO `sys_area` VALUES ('87cc8fe153da11e68586000c293c07d6', '341000', '黄山市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87cec64453da11e68586000c293c07d6', '341001', '黄山区', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87d0da2253da11e68586000c293c07d6', '341002', '屯溪区', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87d308ac53da11e68586000c293c07d6', '341004', '徽州区', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87d4e3fb53da11e68586000c293c07d6', '341021', '歙县', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87d6de7353da11e68586000c293c07d6', '341022', '休宁县', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87d977d953da11e68586000c293c07d6', '341023', '黟县', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87db602353da11e68586000c293c07d6', '341024', '祁门县', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87dd13d153da11e68586000c293c07d6', '341091', '汤口镇', null, null, '341000');
INSERT INTO `sys_area` VALUES ('87def90053da11e68586000c293c07d6', '341100', '滁州市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87e0f90b53da11e68586000c293c07d6', '341101', '市辖区', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87e3091f53da11e68586000c293c07d6', '341102', '琅琊区', null, null, '341101');
INSERT INTO `sys_area` VALUES ('87e4d6fd53da11e68586000c293c07d6', '341103', '南谯区', null, null, '341101');
INSERT INTO `sys_area` VALUES ('87e6bdc553da11e68586000c293c07d6', '341122', '来安县', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87e8ebcc53da11e68586000c293c07d6', '341124', '全椒县', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87eac08c53da11e68586000c293c07d6', '341125', '定远县', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87ec8f1e53da11e68586000c293c07d6', '341126', '凤阳县', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87ee707d53da11e68586000c293c07d6', '341181', '天长市', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87f05f5453da11e68586000c293c07d6', '341182', '明光市', null, null, '341100');
INSERT INTO `sys_area` VALUES ('87f2217e53da11e68586000c293c07d6', '341200', '阜阳市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('87f3d5a853da11e68586000c293c07d6', '341201', '颍泉区', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87f5ab6253da11e68586000c293c07d6', '341202', '颍州区', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87f79c6453da11e68586000c293c07d6', '341203', '颍东区', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87f9a3bc53da11e68586000c293c07d6', '341221', '临泉县', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87fb89fc53da11e68586000c293c07d6', '341222', '太和县', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87fd774253da11e68586000c293c07d6', '341225', '阜南县', null, null, '341200');
INSERT INTO `sys_area` VALUES ('87ff5dd653da11e68586000c293c07d6', '341226', '颍上县', null, null, '341200');
INSERT INTO `sys_area` VALUES ('8801118453da11e68586000c293c07d6', '341282', '界首市', null, null, '341200');
INSERT INTO `sys_area` VALUES ('8802dfd553da11e68586000c293c07d6', '341300', '宿州市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('8804b83353da11e68586000c293c07d6', '341301', '市辖区', null, null, '341300');
INSERT INTO `sys_area` VALUES ('8806f2ca53da11e68586000c293c07d6', '341302', '埇桥区', null, null, '341301');
INSERT INTO `sys_area` VALUES ('880941e453da11e68586000c293c07d6', '341321', '砀山县', null, null, '341300');
INSERT INTO `sys_area` VALUES ('880b0f4453da11e68586000c293c07d6', '341322', '萧县', null, null, '341300');
INSERT INTO `sys_area` VALUES ('880ce51c53da11e68586000c293c07d6', '341323', '灵璧县', null, null, '341300');
INSERT INTO `sys_area` VALUES ('880eb45253da11e68586000c293c07d6', '341324', '泗县', null, null, '341300');
INSERT INTO `sys_area` VALUES ('8810825753da11e68586000c293c07d6', '341402', '居巢区', null, null, null);
INSERT INTO `sys_area` VALUES ('8812587453da11e68586000c293c07d6', '341500', '六安市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('881499c353da11e68586000c293c07d6', '341501', '市辖区', null, null, '341500');
INSERT INTO `sys_area` VALUES ('8816700f53da11e68586000c293c07d6', '341502', '金安区', null, null, '341501');
INSERT INTO `sys_area` VALUES ('88187d4b53da11e68586000c293c07d6', '341503', '裕安区', null, null, '341501');
INSERT INTO `sys_area` VALUES ('881a2f0e53da11e68586000c293c07d6', '341521', '寿县', null, null, '341500');
INSERT INTO `sys_area` VALUES ('881bf8a253da11e68586000c293c07d6', '341522', '霍邱县', null, null, '341500');
INSERT INTO `sys_area` VALUES ('881ddd1653da11e68586000c293c07d6', '341523', '舒城县', null, null, '341500');
INSERT INTO `sys_area` VALUES ('881f9a1853da11e68586000c293c07d6', '341524', '金寨县', null, null, '341500');
INSERT INTO `sys_area` VALUES ('88215a3853da11e68586000c293c07d6', '341525', '霍山县', null, null, '341500');
INSERT INTO `sys_area` VALUES ('882323c653da11e68586000c293c07d6', '341600', '亳州市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('8824f2cb53da11e68586000c293c07d6', '341601', '谯城区', null, null, '341600');
INSERT INTO `sys_area` VALUES ('8828515d53da11e68586000c293c07d6', '341621', '涡阳县', null, null, '341600');
INSERT INTO `sys_area` VALUES ('882a31c153da11e68586000c293c07d6', '341622', '蒙城县', null, null, '341600');
INSERT INTO `sys_area` VALUES ('882be5e853da11e68586000c293c07d6', '341623', '利辛县', null, null, '341600');
INSERT INTO `sys_area` VALUES ('882de42f53da11e68586000c293c07d6', '341700', '池州市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('882fbd8953da11e68586000c293c07d6', '341701', '市辖区', null, null, '341700');
INSERT INTO `sys_area` VALUES ('8831731d53da11e68586000c293c07d6', '341702', '贵池区', null, null, '341701');
INSERT INTO `sys_area` VALUES ('88335d9353da11e68586000c293c07d6', '341721', '东至县', null, null, '341700');
INSERT INTO `sys_area` VALUES ('8835786353da11e68586000c293c07d6', '341722', '石台县', null, null, '341700');
INSERT INTO `sys_area` VALUES ('88374c0753da11e68586000c293c07d6', '341723', '青阳县', null, null, '341700');
INSERT INTO `sys_area` VALUES ('8839fcb053da11e68586000c293c07d6', '341800', '宣城市', null, null, '340000');
INSERT INTO `sys_area` VALUES ('883bde7853da11e68586000c293c07d6', '341801', '市辖区', null, null, '341800');
INSERT INTO `sys_area` VALUES ('883daead53da11e68586000c293c07d6', '341802', '宣州区', null, null, '341801');
INSERT INTO `sys_area` VALUES ('883f85a653da11e68586000c293c07d6', '341821', '郎溪县', null, null, '341800');
INSERT INTO `sys_area` VALUES ('8841526d53da11e68586000c293c07d6', '341822', '广德县', null, null, '341800');
INSERT INTO `sys_area` VALUES ('88433b6653da11e68586000c293c07d6', '341823', '泾县', null, null, '341800');
INSERT INTO `sys_area` VALUES ('884520d253da11e68586000c293c07d6', '341824', '绩溪县', null, null, '341800');
INSERT INTO `sys_area` VALUES ('8847889a53da11e68586000c293c07d6', '341825', '旌德县', null, null, '341800');
INSERT INTO `sys_area` VALUES ('8849abe453da11e68586000c293c07d6', '341881', '宁国市', null, null, '341800');
INSERT INTO `sys_area` VALUES ('884b980f53da11e68586000c293c07d6', '350000', '福建省', null, '13', '0');
INSERT INTO `sys_area` VALUES ('884d3cfb53da11e68586000c293c07d6', '350100', '福州市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('884f11cc53da11e68586000c293c07d6', '350101', '市辖区', null, null, '350100');
INSERT INTO `sys_area` VALUES ('88510f3253da11e68586000c293c07d6', '350102', '鼓楼区', null, null, '350101');
INSERT INTO `sys_area` VALUES ('885331c953da11e68586000c293c07d6', '350103', '台江区', null, null, '350101');
INSERT INTO `sys_area` VALUES ('8856c4f953da11e68586000c293c07d6', '350104', '仓山区', null, null, '350101');
INSERT INTO `sys_area` VALUES ('885894e653da11e68586000c293c07d6', '350105', '马尾区', null, null, '350101');
INSERT INTO `sys_area` VALUES ('885a805553da11e68586000c293c07d6', '350111', '晋安区', null, null, '350101');
INSERT INTO `sys_area` VALUES ('885c65c253da11e68586000c293c07d6', '350121', '闽侯县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('885e20d953da11e68586000c293c07d6', '350122', '连江县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('886007ff53da11e68586000c293c07d6', '350123', '罗源县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('8861b77253da11e68586000c293c07d6', '350124', '闽清县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('8863b82653da11e68586000c293c07d6', '350125', '永泰县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('886600f253da11e68586000c293c07d6', '350128', '平潭县', null, null, '350100');
INSERT INTO `sys_area` VALUES ('88682c7553da11e68586000c293c07d6', '350181', '福清市', null, null, '350100');
INSERT INTO `sys_area` VALUES ('8869f6d853da11e68586000c293c07d6', '350182', '长乐市', null, null, '350100');
INSERT INTO `sys_area` VALUES ('886becee53da11e68586000c293c07d6', '350200', '厦门市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('886dda4e53da11e68586000c293c07d6', '350201', '市辖区', null, null, '350200');
INSERT INTO `sys_area` VALUES ('886fa54853da11e68586000c293c07d6', '350203', '思明区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('8871ae8153da11e68586000c293c07d6', '350205', '海沧区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('8873ae2c53da11e68586000c293c07d6', '350206', '湖里区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('88758bcf53da11e68586000c293c07d6', '350211', '集美区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('8877756853da11e68586000c293c07d6', '350212', '同安区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('8879711253da11e68586000c293c07d6', '350213', '翔安区', null, null, '350201');
INSERT INTO `sys_area` VALUES ('887b4b0353da11e68586000c293c07d6', '350300', '莆田市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('887cfe8053da11e68586000c293c07d6', '350301', '市辖区', null, null, '350300');
INSERT INTO `sys_area` VALUES ('887eef9453da11e68586000c293c07d6', '350302', '城厢区', null, null, '350301');
INSERT INTO `sys_area` VALUES ('88812be053da11e68586000c293c07d6', '350303', '涵江区', null, null, '350301');
INSERT INTO `sys_area` VALUES ('8882f5d553da11e68586000c293c07d6', '350304', '荔城区', null, null, '350301');
INSERT INTO `sys_area` VALUES ('8885253c53da11e68586000c293c07d6', '350305', '秀屿区', null, null, '350301');
INSERT INTO `sys_area` VALUES ('8887101753da11e68586000c293c07d6', '350322', '仙游县', null, null, '350300');
INSERT INTO `sys_area` VALUES ('888966a053da11e68586000c293c07d6', '350400', '三明市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('888b5dd053da11e68586000c293c07d6', '350401', '市辖区', null, null, '350400');
INSERT INTO `sys_area` VALUES ('888d343053da11e68586000c293c07d6', '350402', '梅列区', null, null, '350401');
INSERT INTO `sys_area` VALUES ('888f176453da11e68586000c293c07d6', '350403', '三元区', null, null, '350401');
INSERT INTO `sys_area` VALUES ('8891072353da11e68586000c293c07d6', '350421', '明溪县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('8892faa453da11e68586000c293c07d6', '350423', '清流县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('8894eba153da11e68586000c293c07d6', '350424', '宁化县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('8896a3cf53da11e68586000c293c07d6', '350425', '大田县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('8898965f53da11e68586000c293c07d6', '350426', '尤溪县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('889a69c153da11e68586000c293c07d6', '350427', '沙县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('889c16e153da11e68586000c293c07d6', '350428', '将乐县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('889e3e7953da11e68586000c293c07d6', '350429', '泰宁县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('88a009b553da11e68586000c293c07d6', '350430', '建宁县', null, null, '350400');
INSERT INTO `sys_area` VALUES ('88a231e653da11e68586000c293c07d6', '350481', '永安市', null, null, '350400');
INSERT INTO `sys_area` VALUES ('88a4302753da11e68586000c293c07d6', '350500', '泉州市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('88a60ba253da11e68586000c293c07d6', '350501', '市辖区', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88a7d8d253da11e68586000c293c07d6', '350502', '鲤城区', null, null, '350501');
INSERT INTO `sys_area` VALUES ('88a9bd0353da11e68586000c293c07d6', '350503', '丰泽区', null, null, '350501');
INSERT INTO `sys_area` VALUES ('88ab67c653da11e68586000c293c07d6', '350504', '洛江区', null, null, '350501');
INSERT INTO `sys_area` VALUES ('88ad8f3253da11e68586000c293c07d6', '350505', '泉港区', null, null, '350501');
INSERT INTO `sys_area` VALUES ('88af997f53da11e68586000c293c07d6', '350521', '惠安县', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88b1889153da11e68586000c293c07d6', '350524', '安溪县', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88b3779c53da11e68586000c293c07d6', '350525', '永春县', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88b566f153da11e68586000c293c07d6', '350526', '德化县', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88b736c453da11e68586000c293c07d6', '350527', '金门县', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88b9047653da11e68586000c293c07d6', '350581', '石狮市', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88bb019753da11e68586000c293c07d6', '350582', '晋江市', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88bc97ff53da11e68586000c293c07d6', '350583', '南安市', null, null, '350500');
INSERT INTO `sys_area` VALUES ('88bea16b53da11e68586000c293c07d6', '350600', '漳州市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('88c085db53da11e68586000c293c07d6', '350601', '市辖区', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88c259d553da11e68586000c293c07d6', '350602', '芗城区', null, null, '350601');
INSERT INTO `sys_area` VALUES ('88c4409553da11e68586000c293c07d6', '350603', '龙文区', null, null, '350601');
INSERT INTO `sys_area` VALUES ('88c6218c53da11e68586000c293c07d6', '350622', '云霄县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88c82c2453da11e68586000c293c07d6', '350623', '漳浦县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88ca120e53da11e68586000c293c07d6', '350624', '诏安县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88cbc7a453da11e68586000c293c07d6', '350625', '长泰县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88cdbfdf53da11e68586000c293c07d6', '350626', '东山县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88cf9b3653da11e68586000c293c07d6', '350627', '南靖县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88d1a39753da11e68586000c293c07d6', '350628', '平和县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88d368bb53da11e68586000c293c07d6', '350629', '华安县', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88d59dca53da11e68586000c293c07d6', '350681', '龙海市', null, null, '350600');
INSERT INTO `sys_area` VALUES ('88e49f7053da11e68586000c293c07d6', '350700', '南平市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('88e6b72953da11e68586000c293c07d6', '350701', '市辖区', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88e8636053da11e68586000c293c07d6', '350702', '延平区', null, null, '350701');
INSERT INTO `sys_area` VALUES ('88ea438853da11e68586000c293c07d6', '350721', '顺昌县', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88ec3bef53da11e68586000c293c07d6', '350722', '浦城县', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88ee5ef353da11e68586000c293c07d6', '350723', '光泽县', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f062c753da11e68586000c293c07d6', '350724', '松溪县', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f231a653da11e68586000c293c07d6', '350725', '政和县', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f4253753da11e68586000c293c07d6', '350781', '邵武市', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f61f0153da11e68586000c293c07d6', '350782', '武夷山市', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f8042753da11e68586000c293c07d6', '350783', '建瓯市', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88f9a98353da11e68586000c293c07d6', '350784', '建阳市', null, null, '350700');
INSERT INTO `sys_area` VALUES ('88fbb93553da11e68586000c293c07d6', '350800', '龙岩市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('88fd7b0153da11e68586000c293c07d6', '350801', '市辖区', null, null, '350800');
INSERT INTO `sys_area` VALUES ('88ffbee253da11e68586000c293c07d6', '350802', '新罗区', null, null, '350801');
INSERT INTO `sys_area` VALUES ('8901b9f753da11e68586000c293c07d6', '350821', '长汀县', null, null, '350800');
INSERT INTO `sys_area` VALUES ('89038b3c53da11e68586000c293c07d6', '350822', '永定县', null, null, '350800');
INSERT INTO `sys_area` VALUES ('890553d653da11e68586000c293c07d6', '350823', '上杭县', null, null, '350800');
INSERT INTO `sys_area` VALUES ('890713dc53da11e68586000c293c07d6', '350824', '武平县', null, null, '350800');
INSERT INTO `sys_area` VALUES ('8908d83353da11e68586000c293c07d6', '350825', '连城县', null, null, '350800');
INSERT INTO `sys_area` VALUES ('890ad01f53da11e68586000c293c07d6', '350881', '漳平市', null, null, '350800');
INSERT INTO `sys_area` VALUES ('890cb76d53da11e68586000c293c07d6', '350900', '宁德市', null, null, '350000');
INSERT INTO `sys_area` VALUES ('890e932353da11e68586000c293c07d6', '350901', '市辖区', null, null, '350900');
INSERT INTO `sys_area` VALUES ('89106ee453da11e68586000c293c07d6', '350902', '蕉城区', null, null, '350901');
INSERT INTO `sys_area` VALUES ('89125e7653da11e68586000c293c07d6', '350921', '霞浦县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('89144b0a53da11e68586000c293c07d6', '350922', '古田县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('8916137d53da11e68586000c293c07d6', '350923', '屏南县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('8917e54e53da11e68586000c293c07d6', '350924', '寿宁县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('891a05c753da11e68586000c293c07d6', '350925', '周宁县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('891bea6553da11e68586000c293c07d6', '350926', '柘荣县', null, null, '350900');
INSERT INTO `sys_area` VALUES ('891e623053da11e68586000c293c07d6', '350981', '福安市', null, null, '350900');
INSERT INTO `sys_area` VALUES ('892069de53da11e68586000c293c07d6', '350982', '福鼎市', null, null, '350900');
INSERT INTO `sys_area` VALUES ('8922430a53da11e68586000c293c07d6', '360000', '江西省', null, '14', '0');
INSERT INTO `sys_area` VALUES ('8924141b53da11e68586000c293c07d6', '360100', '南昌市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('8925e26753da11e68586000c293c07d6', '360101', '市辖区', null, null, '360100');
INSERT INTO `sys_area` VALUES ('8927ece153da11e68586000c293c07d6', '360102', '东湖区', null, null, '360101');
INSERT INTO `sys_area` VALUES ('8929b70e53da11e68586000c293c07d6', '360103', '西湖区', null, null, '360101');
INSERT INTO `sys_area` VALUES ('892b72ac53da11e68586000c293c07d6', '360104', '青云谱区', null, null, '360101');
INSERT INTO `sys_area` VALUES ('892d2fd453da11e68586000c293c07d6', '360105', '湾里区', null, null, '360101');
INSERT INTO `sys_area` VALUES ('892ee23753da11e68586000c293c07d6', '360111', '青山湖区', null, null, '360101');
INSERT INTO `sys_area` VALUES ('8930d90d53da11e68586000c293c07d6', '360121', '南昌县', null, null, '360100');
INSERT INTO `sys_area` VALUES ('8932f1da53da11e68586000c293c07d6', '360122', '新建县', null, null, '360100');
INSERT INTO `sys_area` VALUES ('8934b2fd53da11e68586000c293c07d6', '360123', '安义县', null, null, '360100');
INSERT INTO `sys_area` VALUES ('89369c1e53da11e68586000c293c07d6', '360124', '进贤县', null, null, '360100');
INSERT INTO `sys_area` VALUES ('893c735953da11e68586000c293c07d6', '360200', '景德镇市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('893e66d853da11e68586000c293c07d6', '360201', '市辖区', null, null, '360200');
INSERT INTO `sys_area` VALUES ('89402f0753da11e68586000c293c07d6', '360202', '昌江区', null, null, '360201');
INSERT INTO `sys_area` VALUES ('894238f453da11e68586000c293c07d6', '360203', '珠山区', null, null, '360201');
INSERT INTO `sys_area` VALUES ('8944307e53da11e68586000c293c07d6', '360222', '浮梁县', null, null, '360200');
INSERT INTO `sys_area` VALUES ('8945c8e053da11e68586000c293c07d6', '360281', '乐平市', null, null, '360200');
INSERT INTO `sys_area` VALUES ('8947cb7353da11e68586000c293c07d6', '360300', '萍乡市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('894999ec53da11e68586000c293c07d6', '360301', '市辖区', null, null, '360300');
INSERT INTO `sys_area` VALUES ('894b7f0253da11e68586000c293c07d6', '360302', '安源区', null, null, '360301');
INSERT INTO `sys_area` VALUES ('894d4ef253da11e68586000c293c07d6', '360313', '湘东区', null, null, '360301');
INSERT INTO `sys_area` VALUES ('894ffce453da11e68586000c293c07d6', '360321', '莲花县', null, null, '360300');
INSERT INTO `sys_area` VALUES ('895217e353da11e68586000c293c07d6', '360322', '上栗县', null, null, '360300');
INSERT INTO `sys_area` VALUES ('89549e4c53da11e68586000c293c07d6', '360323', '芦溪县', null, null, '360300');
INSERT INTO `sys_area` VALUES ('8956c37453da11e68586000c293c07d6', '360400', '九江市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('8959b74853da11e68586000c293c07d6', '360401', '市辖区', null, null, '360400');
INSERT INTO `sys_area` VALUES ('895bf84053da11e68586000c293c07d6', '360402', '庐山区', null, null, '360401');
INSERT INTO `sys_area` VALUES ('895e25cd53da11e68586000c293c07d6', '360403', '浔阳区', null, null, '360401');
INSERT INTO `sys_area` VALUES ('8960a7da53da11e68586000c293c07d6', '360421', '九江县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8962ab6d53da11e68586000c293c07d6', '360423', '武宁县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8964d88353da11e68586000c293c07d6', '360424', '修水县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8967398253da11e68586000c293c07d6', '360425', '永修县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('89697ac953da11e68586000c293c07d6', '360426', '德安县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('896b398653da11e68586000c293c07d6', '360427', '星子县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('896d87e353da11e68586000c293c07d6', '360428', '都昌县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('896f525353da11e68586000c293c07d6', '360429', '湖口县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8971785753da11e68586000c293c07d6', '360430', '彭泽县', null, null, '360400');
INSERT INTO `sys_area` VALUES ('897349d753da11e68586000c293c07d6', '360481', '瑞昌市', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8975016d53da11e68586000c293c07d6', '360482', '共青城市', null, null, '360400');
INSERT INTO `sys_area` VALUES ('8977931053da11e68586000c293c07d6', '360500', '新余市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('89793bed53da11e68586000c293c07d6', '360501', '市辖区', null, null, '360500');
INSERT INTO `sys_area` VALUES ('897bb81453da11e68586000c293c07d6', '360502', '渝水区', null, null, '360501');
INSERT INTO `sys_area` VALUES ('897d81b053da11e68586000c293c07d6', '360521', '分宜县', null, null, '360500');
INSERT INTO `sys_area` VALUES ('897f5b4253da11e68586000c293c07d6', '360600', '鹰潭市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('8980f52b53da11e68586000c293c07d6', '360601', '市辖区', null, null, '360600');
INSERT INTO `sys_area` VALUES ('8982d0a153da11e68586000c293c07d6', '360602', '月湖区', null, null, '360601');
INSERT INTO `sys_area` VALUES ('8984aaa153da11e68586000c293c07d6', '360622', '余江县', null, null, '360600');
INSERT INTO `sys_area` VALUES ('8986c23853da11e68586000c293c07d6', '360681', '贵溪市', null, null, '360600');
INSERT INTO `sys_area` VALUES ('8988b96a53da11e68586000c293c07d6', '360700', '赣州市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('898a6e4f53da11e68586000c293c07d6', '360701', '市辖区', null, null, '360700');
INSERT INTO `sys_area` VALUES ('898c854e53da11e68586000c293c07d6', '360702', '章贡区', null, null, '360701');
INSERT INTO `sys_area` VALUES ('898e4f8353da11e68586000c293c07d6', '360721', '赣县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('8990463753da11e68586000c293c07d6', '360722', '信丰县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('899231d853da11e68586000c293c07d6', '360723', '大余县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('8993fedc53da11e68586000c293c07d6', '360724', '上犹县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('899623ca53da11e68586000c293c07d6', '360725', '崇义县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('8997d60553da11e68586000c293c07d6', '360726', '安远县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('8999c21953da11e68586000c293c07d6', '360727', '龙南县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('899b80ae53da11e68586000c293c07d6', '360728', '定南县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('899d691b53da11e68586000c293c07d6', '360729', '全南县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('899f2a9353da11e68586000c293c07d6', '360730', '宁都县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89a12ea953da11e68586000c293c07d6', '360731', '于都县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89a3349c53da11e68586000c293c07d6', '360732', '兴国县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89a58e1c53da11e68586000c293c07d6', '360733', '会昌县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89a7b08553da11e68586000c293c07d6', '360734', '寻乌县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89a95c7553da11e68586000c293c07d6', '360735', '石城县', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89aba11053da11e68586000c293c07d6', '360781', '瑞金市', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89ad6e3753da11e68586000c293c07d6', '360782', '南康市', null, null, '360700');
INSERT INTO `sys_area` VALUES ('89af87d053da11e68586000c293c07d6', '360800', '吉安市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('89b13fd053da11e68586000c293c07d6', '360801', '市辖区', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89b3652f53da11e68586000c293c07d6', '360802', '吉州区', null, null, '360801');
INSERT INTO `sys_area` VALUES ('89b58a2d53da11e68586000c293c07d6', '360803', '青原区', null, null, '360801');
INSERT INTO `sys_area` VALUES ('89b7a01053da11e68586000c293c07d6', '360821', '吉安县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89b980e153da11e68586000c293c07d6', '360822', '吉水县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89bb3eb653da11e68586000c293c07d6', '360823', '峡江县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89bd0dae53da11e68586000c293c07d6', '360824', '新干县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89beda4d53da11e68586000c293c07d6', '360825', '永丰县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c0b82a53da11e68586000c293c07d6', '360826', '泰和县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c2832153da11e68586000c293c07d6', '360827', '遂川县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c4549553da11e68586000c293c07d6', '360828', '万安县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c62bea53da11e68586000c293c07d6', '360829', '安福县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c7cb5253da11e68586000c293c07d6', '360830', '永新县', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89c9ba4553da11e68586000c293c07d6', '360881', '井冈山市', null, null, '360800');
INSERT INTO `sys_area` VALUES ('89cb966d53da11e68586000c293c07d6', '360900', '宜春市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('89cd4e9653da11e68586000c293c07d6', '360901', '市辖区', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89cf159d53da11e68586000c293c07d6', '360902', '袁州区', null, null, '360901');
INSERT INTO `sys_area` VALUES ('89d0daa253da11e68586000c293c07d6', '360921', '奉新县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89d2ab5d53da11e68586000c293c07d6', '360922', '万载县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89d475af53da11e68586000c293c07d6', '360923', '上高县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89d6373453da11e68586000c293c07d6', '360924', '宜丰县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89d882fe53da11e68586000c293c07d6', '360925', '靖安县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89da525a53da11e68586000c293c07d6', '360926', '铜鼓县', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89dc1f5853da11e68586000c293c07d6', '360981', '丰城市', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89ddfaf053da11e68586000c293c07d6', '360982', '樟树市', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89e0179e53da11e68586000c293c07d6', '360983', '高安市', null, null, '360900');
INSERT INTO `sys_area` VALUES ('89e1ecba53da11e68586000c293c07d6', '361000', '抚州市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('89e3a56553da11e68586000c293c07d6', '361001', '市辖区', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89e570a053da11e68586000c293c07d6', '361002', '临川区', null, null, '361001');
INSERT INTO `sys_area` VALUES ('89e766e653da11e68586000c293c07d6', '361021', '南城县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89e94bab53da11e68586000c293c07d6', '361022', '黎川县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89eb075053da11e68586000c293c07d6', '361023', '南丰县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89ece09953da11e68586000c293c07d6', '361024', '崇仁县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89ef01d553da11e68586000c293c07d6', '361025', '乐安县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f0c4e853da11e68586000c293c07d6', '361026', '宜黄县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f29f1253da11e68586000c293c07d6', '361027', '金溪县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f46bda53da11e68586000c293c07d6', '361028', '资溪县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f64a9b53da11e68586000c293c07d6', '361029', '东乡县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f7f10953da11e68586000c293c07d6', '361030', '广昌县', null, null, '361000');
INSERT INTO `sys_area` VALUES ('89f9d62a53da11e68586000c293c07d6', '361100', '上饶市', null, null, '360000');
INSERT INTO `sys_area` VALUES ('89fba52553da11e68586000c293c07d6', '361101', '市辖区', null, null, '361100');
INSERT INTO `sys_area` VALUES ('89fd65d453da11e68586000c293c07d6', '361102', '信州区', null, null, '361101');
INSERT INTO `sys_area` VALUES ('89ff46a753da11e68586000c293c07d6', '361121', '上饶县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a012ac053da11e68586000c293c07d6', '361122', '广丰县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a03151b53da11e68586000c293c07d6', '361123', '玉山县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a04bead53da11e68586000c293c07d6', '361124', '铅山县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a0691d653da11e68586000c293c07d6', '361125', '横峰县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a08803a53da11e68586000c293c07d6', '361126', '弋阳县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a0a468a53da11e68586000c293c07d6', '361127', '余干县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a0c35e253da11e68586000c293c07d6', '361128', '鄱阳县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a0e0da353da11e68586000c293c07d6', '361129', '万年县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a0ffb9753da11e68586000c293c07d6', '361130', '婺源县', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a11b04153da11e68586000c293c07d6', '361181', '德兴市', null, null, '361100');
INSERT INTO `sys_area` VALUES ('8a1392f953da11e68586000c293c07d6', '370000', '山东省', null, '15', '0');
INSERT INTO `sys_area` VALUES ('8a1574e153da11e68586000c293c07d6', '370100', '济南市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a1752a353da11e68586000c293c07d6', '370101', '市辖区', null, null, '370100');
INSERT INTO `sys_area` VALUES ('8a19163353da11e68586000c293c07d6', '370102', '历下区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a1ae6c453da11e68586000c293c07d6', '370103', '市中区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a1cf77853da11e68586000c293c07d6', '370104', '槐荫区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a1ed74353da11e68586000c293c07d6', '370105', '天桥区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a209dd853da11e68586000c293c07d6', '370112', '历城区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a22612753da11e68586000c293c07d6', '370113', '长清区', null, null, '370101');
INSERT INTO `sys_area` VALUES ('8a2439c253da11e68586000c293c07d6', '370124', '平阴县', null, null, '370100');
INSERT INTO `sys_area` VALUES ('8a2629bd53da11e68586000c293c07d6', '370125', '济阳县', null, null, '370100');
INSERT INTO `sys_area` VALUES ('8a28085153da11e68586000c293c07d6', '370126', '商河县', null, null, '370100');
INSERT INTO `sys_area` VALUES ('8a29e04153da11e68586000c293c07d6', '370181', '章丘市', null, null, '370100');
INSERT INTO `sys_area` VALUES ('8a2ba47a53da11e68586000c293c07d6', '370200', '青岛市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a2d547a53da11e68586000c293c07d6', '370201', '市辖区', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a30120d53da11e68586000c293c07d6', '370202', '市南区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a3223ca53da11e68586000c293c07d6', '370203', '市北区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a33fd4f53da11e68586000c293c07d6', '370205', '四方区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a35b73253da11e68586000c293c07d6', '370211', '黄岛区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a37946653da11e68586000c293c07d6', '370212', '崂山区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a3968fb53da11e68586000c293c07d6', '370213', '李沧区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a3b3dec53da11e68586000c293c07d6', '370214', '城阳区', null, null, '370201');
INSERT INTO `sys_area` VALUES ('8a3d2e6b53da11e68586000c293c07d6', '370281', '胶州市', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a3ef85a53da11e68586000c293c07d6', '370282', '即墨市', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a40d9d753da11e68586000c293c07d6', '370283', '平度市', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a42c57d53da11e68586000c293c07d6', '370284', '胶南市', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a44aab553da11e68586000c293c07d6', '370285', '莱西市', null, null, '370200');
INSERT INTO `sys_area` VALUES ('8a467d6753da11e68586000c293c07d6', '370300', '淄博市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a4845ff53da11e68586000c293c07d6', '370301', '市辖区', null, null, '370300');
INSERT INTO `sys_area` VALUES ('8a4a0c9853da11e68586000c293c07d6', '370302', '淄川区', null, null, '370301');
INSERT INTO `sys_area` VALUES ('8a4bddd153da11e68586000c293c07d6', '370303', '张店区', null, null, '370301');
INSERT INTO `sys_area` VALUES ('8a4df3c953da11e68586000c293c07d6', '370304', '博山区', null, null, '370301');
INSERT INTO `sys_area` VALUES ('8a4fbab653da11e68586000c293c07d6', '370305', '临淄区', null, null, '370301');
INSERT INTO `sys_area` VALUES ('8a51870653da11e68586000c293c07d6', '370306', '周村区', null, null, '370301');
INSERT INTO `sys_area` VALUES ('8a5358b653da11e68586000c293c07d6', '370321', '桓台县', null, null, '370300');
INSERT INTO `sys_area` VALUES ('8a5528b153da11e68586000c293c07d6', '370322', '高青县', null, null, '370300');
INSERT INTO `sys_area` VALUES ('8a57134653da11e68586000c293c07d6', '370323', '沂源县', null, null, '370300');
INSERT INTO `sys_area` VALUES ('8a58d19053da11e68586000c293c07d6', '370400', '枣庄市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a5ad56153da11e68586000c293c07d6', '370401', '市辖区', null, null, '370400');
INSERT INTO `sys_area` VALUES ('8a5cab9c53da11e68586000c293c07d6', '370402', '市中区', null, null, '370401');
INSERT INTO `sys_area` VALUES ('8a5ea6de53da11e68586000c293c07d6', '370403', '薛城区', null, null, '370401');
INSERT INTO `sys_area` VALUES ('8a6094ee53da11e68586000c293c07d6', '370404', '峄城区', null, null, '370401');
INSERT INTO `sys_area` VALUES ('8a62736953da11e68586000c293c07d6', '370405', '台儿庄区', null, null, '370401');
INSERT INTO `sys_area` VALUES ('8a6440b453da11e68586000c293c07d6', '370406', '山亭区', null, null, '370401');
INSERT INTO `sys_area` VALUES ('8a661d0953da11e68586000c293c07d6', '370481', '滕州市', null, null, '370400');
INSERT INTO `sys_area` VALUES ('8a67daa353da11e68586000c293c07d6', '370500', '东营市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a69c33d53da11e68586000c293c07d6', '370501', '市辖区', null, null, '370500');
INSERT INTO `sys_area` VALUES ('8a6b9e2953da11e68586000c293c07d6', '370502', '东营区', null, null, '370501');
INSERT INTO `sys_area` VALUES ('8a6d782553da11e68586000c293c07d6', '370503', '河口区', null, null, '370501');
INSERT INTO `sys_area` VALUES ('8a6f4c5353da11e68586000c293c07d6', '370521', '垦利县', null, null, '370500');
INSERT INTO `sys_area` VALUES ('8a71aca353da11e68586000c293c07d6', '370522', '利津县', null, null, '370500');
INSERT INTO `sys_area` VALUES ('8a73784253da11e68586000c293c07d6', '370523', '广饶县', null, null, '370500');
INSERT INTO `sys_area` VALUES ('8a75433953da11e68586000c293c07d6', '370600', '烟台市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a77094e53da11e68586000c293c07d6', '370601', '市辖区', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a78eee753da11e68586000c293c07d6', '370602', '芝罘区', null, null, '370601');
INSERT INTO `sys_area` VALUES ('8a7ad2b553da11e68586000c293c07d6', '370611', '福山区', null, null, '370601');
INSERT INTO `sys_area` VALUES ('8a7c723d53da11e68586000c293c07d6', '370612', '牟平区', null, null, '370601');
INSERT INTO `sys_area` VALUES ('8a7e8e3d53da11e68586000c293c07d6', '370613', '莱山区', null, null, '370601');
INSERT INTO `sys_area` VALUES ('8a806de453da11e68586000c293c07d6', '370634', '长岛县', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a8230b453da11e68586000c293c07d6', '370681', '龙口市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a83e48153da11e68586000c293c07d6', '370682', '莱阳市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a85ce9853da11e68586000c293c07d6', '370683', '莱州市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a87aa0453da11e68586000c293c07d6', '370684', '蓬莱市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a894ec253da11e68586000c293c07d6', '370685', '招远市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a8b1a4853da11e68586000c293c07d6', '370686', '栖霞市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a8d538f53da11e68586000c293c07d6', '370687', '海阳市', null, null, '370600');
INSERT INTO `sys_area` VALUES ('8a8f343253da11e68586000c293c07d6', '370700', '潍坊市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8a910a2453da11e68586000c293c07d6', '370701', '市辖区', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8a92e90153da11e68586000c293c07d6', '370702', '潍城区', null, null, '370701');
INSERT INTO `sys_area` VALUES ('8a94f89c53da11e68586000c293c07d6', '370703', '寒亭区', null, null, '370701');
INSERT INTO `sys_area` VALUES ('8a96ced153da11e68586000c293c07d6', '370704', '坊子区', null, null, '370701');
INSERT INTO `sys_area` VALUES ('8a989c9053da11e68586000c293c07d6', '370705', '奎文区', null, null, '370701');
INSERT INTO `sys_area` VALUES ('8a9a672b53da11e68586000c293c07d6', '370724', '临朐县', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8a9c5e5653da11e68586000c293c07d6', '370725', '昌乐县', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8a9e28a853da11e68586000c293c07d6', '370781', '青州市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8a9ffbec53da11e68586000c293c07d6', '370782', '诸城市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8aa1dc3e53da11e68586000c293c07d6', '370783', '寿光市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8aa3d1f453da11e68586000c293c07d6', '370784', '安丘市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8aa5a82f53da11e68586000c293c07d6', '370785', '高密市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8aa7591053da11e68586000c293c07d6', '370786', '昌邑市', null, null, '370700');
INSERT INTO `sys_area` VALUES ('8aa939e853da11e68586000c293c07d6', '370800', '济宁市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8aab031853da11e68586000c293c07d6', '370801', '市辖区', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8aacbb4f53da11e68586000c293c07d6', '370802', '市中区', null, null, '370801');
INSERT INTO `sys_area` VALUES ('8aae764d53da11e68586000c293c07d6', '370811', '任城区', null, null, '370801');
INSERT INTO `sys_area` VALUES ('8ab0467c53da11e68586000c293c07d6', '370826', '微山县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ab223e653da11e68586000c293c07d6', '370827', '鱼台县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ab4105f53da11e68586000c293c07d6', '370828', '金乡县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ab5d7c153da11e68586000c293c07d6', '370829', '嘉祥县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ab7a95353da11e68586000c293c07d6', '370830', '汶上县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ab966ec53da11e68586000c293c07d6', '370831', '泗水县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8abb9cc353da11e68586000c293c07d6', '370832', '梁山县', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8abd522053da11e68586000c293c07d6', '370881', '曲阜市', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8abf1c4653da11e68586000c293c07d6', '370882', '兖州市', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ac132d253da11e68586000c293c07d6', '370883', '邹城市', null, null, '370800');
INSERT INTO `sys_area` VALUES ('8ac5a83153da11e68586000c293c07d6', '370900', '泰安市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8ac7826f53da11e68586000c293c07d6', '370901', '岱岳区', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8ac96fb153da11e68586000c293c07d6', '370902', '泰山区', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8acb210d53da11e68586000c293c07d6', '370921', '宁阳县', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8acd1b0253da11e68586000c293c07d6', '370923', '东平县', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8acee02d53da11e68586000c293c07d6', '370982', '新泰市', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8ad0b20053da11e68586000c293c07d6', '370983', '肥城市', null, null, '370900');
INSERT INTO `sys_area` VALUES ('8ad28c2453da11e68586000c293c07d6', '371000', '威海市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8ad4a60853da11e68586000c293c07d6', '371001', '市辖区', null, null, '371000');
INSERT INTO `sys_area` VALUES ('8ad66fdf53da11e68586000c293c07d6', '371002', '环翠区', null, null, '371001');
INSERT INTO `sys_area` VALUES ('8ad84f6a53da11e68586000c293c07d6', '371081', '文登市', null, null, '371000');
INSERT INTO `sys_area` VALUES ('8ada296c53da11e68586000c293c07d6', '371082', '荣成市', null, null, '371000');
INSERT INTO `sys_area` VALUES ('8adbfec253da11e68586000c293c07d6', '371083', '乳山市', null, null, '371000');
INSERT INTO `sys_area` VALUES ('8ade091f53da11e68586000c293c07d6', '371100', '日照市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8adfcd3453da11e68586000c293c07d6', '371101', '市辖区', null, null, '371100');
INSERT INTO `sys_area` VALUES ('8ae1a6d853da11e68586000c293c07d6', '371102', '东港区', null, null, '371101');
INSERT INTO `sys_area` VALUES ('8ae399f353da11e68586000c293c07d6', '371103', '岚山区', null, null, '371101');
INSERT INTO `sys_area` VALUES ('8ae5553853da11e68586000c293c07d6', '371121', '五莲县', null, null, '371100');
INSERT INTO `sys_area` VALUES ('8ae72ad253da11e68586000c293c07d6', '371122', '莒县', null, null, '371100');
INSERT INTO `sys_area` VALUES ('8ae8f17253da11e68586000c293c07d6', '371200', '莱芜市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8aeabf1553da11e68586000c293c07d6', '371201', '市辖区', null, null, '371200');
INSERT INTO `sys_area` VALUES ('8aec7d9e53da11e68586000c293c07d6', '371202', '莱城区', null, null, '371201');
INSERT INTO `sys_area` VALUES ('8aee55e153da11e68586000c293c07d6', '371203', '钢城区', null, null, '371201');
INSERT INTO `sys_area` VALUES ('8af045f353da11e68586000c293c07d6', '371300', '临沂市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8af20f9553da11e68586000c293c07d6', '371301', '市辖区', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8af408c653da11e68586000c293c07d6', '371302', '兰山区', null, null, '371301');
INSERT INTO `sys_area` VALUES ('8af5bc3153da11e68586000c293c07d6', '371311', '罗庄区', null, null, '371301');
INSERT INTO `sys_area` VALUES ('8af79e0f53da11e68586000c293c07d6', '371312', '河东区', null, null, '371301');
INSERT INTO `sys_area` VALUES ('8af96cde53da11e68586000c293c07d6', '371321', '沂南县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8afb6a6853da11e68586000c293c07d6', '371322', '郯城县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8afd274453da11e68586000c293c07d6', '371323', '沂水县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8aff0f2f53da11e68586000c293c07d6', '371324', '苍山县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b015ba853da11e68586000c293c07d6', '371325', '费县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b03122753da11e68586000c293c07d6', '371326', '平邑县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b04f5ea53da11e68586000c293c07d6', '371327', '莒南县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b06c43053da11e68586000c293c07d6', '371328', '蒙阴县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b08729f53da11e68586000c293c07d6', '371329', '临沭县', null, null, '371300');
INSERT INTO `sys_area` VALUES ('8b0a3e2453da11e68586000c293c07d6', '371400', '德州市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8b0c173e53da11e68586000c293c07d6', '371401', '市辖区', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b0e318253da11e68586000c293c07d6', '371402', '德城区', null, null, '371401');
INSERT INTO `sys_area` VALUES ('8b0ff47053da11e68586000c293c07d6', '371421', '陵县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b12325b53da11e68586000c293c07d6', '371422', '宁津县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b13f22153da11e68586000c293c07d6', '371423', '庆云县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b15e90e53da11e68586000c293c07d6', '371424', '临邑县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b179f6253da11e68586000c293c07d6', '371425', '齐河县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b199f1953da11e68586000c293c07d6', '371426', '平原县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b1bbe1153da11e68586000c293c07d6', '371427', '夏津县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b1dc8ba53da11e68586000c293c07d6', '371428', '武城县', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b1fda5b53da11e68586000c293c07d6', '371481', '乐陵市', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b245b1c53da11e68586000c293c07d6', '371482', '禹城市', null, null, '371400');
INSERT INTO `sys_area` VALUES ('8b26e12153da11e68586000c293c07d6', '371500', '聊城市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8b28e5fa53da11e68586000c293c07d6', '371501', '市辖区', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b2b490e53da11e68586000c293c07d6', '371502', '东昌府区', null, null, '371501');
INSERT INTO `sys_area` VALUES ('8b2d6ad253da11e68586000c293c07d6', '371521', '阳谷县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b30277e53da11e68586000c293c07d6', '371522', '莘县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b327b4b53da11e68586000c293c07d6', '371523', '茌平县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b349c9153da11e68586000c293c07d6', '371524', '东阿县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b365bbf53da11e68586000c293c07d6', '371525', '冠县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b38e6d553da11e68586000c293c07d6', '371526', '高唐县', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b3ac74153da11e68586000c293c07d6', '371581', '临清市', null, null, '371500');
INSERT INTO `sys_area` VALUES ('8b3cebfb53da11e68586000c293c07d6', '371600', '滨州市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8b3ec9d453da11e68586000c293c07d6', '371601', '市辖区', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b41983d53da11e68586000c293c07d6', '371602', '滨城区', null, null, '371601');
INSERT INTO `sys_area` VALUES ('8b438d1f53da11e68586000c293c07d6', '371621', '惠民县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b45d36f53da11e68586000c293c07d6', '371622', '阳信县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b48098c53da11e68586000c293c07d6', '371623', '无棣县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b49b73c53da11e68586000c293c07d6', '371624', '沾化县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b4bb81b53da11e68586000c293c07d6', '371625', '博兴县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b4fa78853da11e68586000c293c07d6', '371626', '邹平县', null, null, '371600');
INSERT INTO `sys_area` VALUES ('8b5174c853da11e68586000c293c07d6', '371700', '菏泽市', null, null, '370000');
INSERT INTO `sys_area` VALUES ('8b53743a53da11e68586000c293c07d6', '371701', '市辖区', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b5551a253da11e68586000c293c07d6', '371702', '牡丹区', null, null, '371701');
INSERT INTO `sys_area` VALUES ('8b57234153da11e68586000c293c07d6', '371721', '曹县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b599c2353da11e68586000c293c07d6', '371722', '单县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b5bb4fb53da11e68586000c293c07d6', '371723', '成武县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b5d8a5c53da11e68586000c293c07d6', '371724', '巨野县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b5f5c7653da11e68586000c293c07d6', '371725', '郓城县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b613e1c53da11e68586000c293c07d6', '371726', '鄄城县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b631ed853da11e68586000c293c07d6', '371727', '定陶县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b651e2653da11e68586000c293c07d6', '371728', '东明县', null, null, '371700');
INSERT INTO `sys_area` VALUES ('8b670f2d53da11e68586000c293c07d6', '410000', '河南省', null, '16', '0');
INSERT INTO `sys_area` VALUES ('8b68df0853da11e68586000c293c07d6', '410100', '郑州市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8b6b302e53da11e68586000c293c07d6', '410101', '金水区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b6ce68453da11e68586000c293c07d6', '410102', '中原区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b6efa5753da11e68586000c293c07d6', '410103', '二七区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b70d2a153da11e68586000c293c07d6', '410104', '管城回族区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b72b37d53da11e68586000c293c07d6', '410106', '上街区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b748df453da11e68586000c293c07d6', '410108', '惠济区', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b7670e753da11e68586000c293c07d6', '410122', '中牟县', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b78466753da11e68586000c293c07d6', '410181', '巩义市', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b7a4aca53da11e68586000c293c07d6', '410182', '荥阳市', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b7c128953da11e68586000c293c07d6', '410183', '新密市', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b7e2ecc53da11e68586000c293c07d6', '410184', '新郑市', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b813f0b53da11e68586000c293c07d6', '410185', '登封市', null, null, '410100');
INSERT INTO `sys_area` VALUES ('8b83406453da11e68586000c293c07d6', '410200', '开封市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8b85154753da11e68586000c293c07d6', '410201', '市辖区', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b87000d53da11e68586000c293c07d6', '410202', '龙亭区', null, null, '410201');
INSERT INTO `sys_area` VALUES ('8b88e63c53da11e68586000c293c07d6', '410203', '顺河回族区', null, null, '410201');
INSERT INTO `sys_area` VALUES ('8b8aec1153da11e68586000c293c07d6', '410204', '鼓楼区', null, null, '410201');
INSERT INTO `sys_area` VALUES ('8b8c9a1b53da11e68586000c293c07d6', '410205', '禹王台区', null, null, '410201');
INSERT INTO `sys_area` VALUES ('8b8e82db53da11e68586000c293c07d6', '410211', '金明区', null, null, '410201');
INSERT INTO `sys_area` VALUES ('8b90a8cc53da11e68586000c293c07d6', '410221', '杞县', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b92ad4d53da11e68586000c293c07d6', '410222', '通许县', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b94678853da11e68586000c293c07d6', '410223', '尉氏县', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b962b3253da11e68586000c293c07d6', '410224', '开封县', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b98109c53da11e68586000c293c07d6', '410225', '兰考县', null, null, '410200');
INSERT INTO `sys_area` VALUES ('8b99de8753da11e68586000c293c07d6', '410300', '洛阳市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8b9e637253da11e68586000c293c07d6', '410301', '市辖区', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8ba052a053da11e68586000c293c07d6', '410302', '老城区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8ba2210e53da11e68586000c293c07d6', '410303', '西工区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8ba3da6553da11e68586000c293c07d6', '410304', '廛河回族区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8ba5948253da11e68586000c293c07d6', '410305', '涧西区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8ba7ade953da11e68586000c293c07d6', '410306', '吉利区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8ba9856353da11e68586000c293c07d6', '410307', '洛龙区', null, null, '410301');
INSERT INTO `sys_area` VALUES ('8bab788b53da11e68586000c293c07d6', '410322', '孟津县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bad2d1c53da11e68586000c293c07d6', '410323', '新安县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bafbb3953da11e68586000c293c07d6', '410324', '栾川县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bb1829a53da11e68586000c293c07d6', '410325', '嵩县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bb3de2253da11e68586000c293c07d6', '410326', '汝阳县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bb5f9ce53da11e68586000c293c07d6', '410327', '宜阳县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bb7bc5353da11e68586000c293c07d6', '410328', '洛宁县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bb9715953da11e68586000c293c07d6', '410329', '伊川县', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bbb471253da11e68586000c293c07d6', '410381', '偃师市', null, null, '410300');
INSERT INTO `sys_area` VALUES ('8bbcf8af53da11e68586000c293c07d6', '410400', '平顶山市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8bbef77553da11e68586000c293c07d6', '410401', '市辖区', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bc2021453da11e68586000c293c07d6', '410402', '新华区', null, null, '410401');
INSERT INTO `sys_area` VALUES ('8bc3dd7553da11e68586000c293c07d6', '410403', '卫东区', null, null, '410401');
INSERT INTO `sys_area` VALUES ('8bc5913553da11e68586000c293c07d6', '410404', '石龙区', null, null, '410401');
INSERT INTO `sys_area` VALUES ('8bc786cf53da11e68586000c293c07d6', '410411', '湛河区', null, null, '410401');
INSERT INTO `sys_area` VALUES ('8bc94c9153da11e68586000c293c07d6', '410421', '宝丰县', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bcb20f653da11e68586000c293c07d6', '410422', '叶县', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bccf40053da11e68586000c293c07d6', '410423', '鲁山县', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bceb58953da11e68586000c293c07d6', '410425', '郏县', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bd0e2b453da11e68586000c293c07d6', '410481', '舞钢市', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bd2cb3f53da11e68586000c293c07d6', '410482', '汝州市', null, null, '410400');
INSERT INTO `sys_area` VALUES ('8bd4ace453da11e68586000c293c07d6', '410500', '安阳市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8bd682db53da11e68586000c293c07d6', '410501', '市辖区', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8bd83c2453da11e68586000c293c07d6', '410502', '文峰区', null, null, '410501');
INSERT INTO `sys_area` VALUES ('8bda21b753da11e68586000c293c07d6', '410503', '北关区', null, null, '410501');
INSERT INTO `sys_area` VALUES ('8bdbd67853da11e68586000c293c07d6', '410505', '殷都区', null, null, '410501');
INSERT INTO `sys_area` VALUES ('8bddbd1753da11e68586000c293c07d6', '410506', '龙安区', null, null, '410501');
INSERT INTO `sys_area` VALUES ('8bdff15553da11e68586000c293c07d6', '410522', '安阳县', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8be21c7353da11e68586000c293c07d6', '410523', '汤阴县', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8be3e80053da11e68586000c293c07d6', '410526', '滑县', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8be6bef853da11e68586000c293c07d6', '410527', '内黄县', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8be883d053da11e68586000c293c07d6', '410581', '林州市', null, null, '410500');
INSERT INTO `sys_area` VALUES ('8bea481253da11e68586000c293c07d6', '410600', '鹤壁市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8bec434653da11e68586000c293c07d6', '410601', '市辖区', null, null, '410600');
INSERT INTO `sys_area` VALUES ('8bee353753da11e68586000c293c07d6', '410602', '鹤山区', null, null, '410601');
INSERT INTO `sys_area` VALUES ('8beff26953da11e68586000c293c07d6', '410603', '山城区', null, null, '410601');
INSERT INTO `sys_area` VALUES ('8bf2272d53da11e68586000c293c07d6', '410611', '淇滨区', null, null, '410601');
INSERT INTO `sys_area` VALUES ('8bf4108a53da11e68586000c293c07d6', '410621', '浚县', null, null, '410600');
INSERT INTO `sys_area` VALUES ('8bf61ef853da11e68586000c293c07d6', '410622', '淇县', null, null, '410600');
INSERT INTO `sys_area` VALUES ('8bf8060153da11e68586000c293c07d6', '410700', '新乡市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8bf9c03553da11e68586000c293c07d6', '410701', '市辖区', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8bfc051453da11e68586000c293c07d6', '410702', '红旗区', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8bfde4b153da11e68586000c293c07d6', '410703', '卫滨区', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c00082453da11e68586000c293c07d6', '410704', '凤泉区', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c01db4c53da11e68586000c293c07d6', '410711', '牧野区', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c03a46a53da11e68586000c293c07d6', '410721', '新乡县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c05780353da11e68586000c293c07d6', '410724', '获嘉县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c07590e53da11e68586000c293c07d6', '410725', '原阳县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c09040453da11e68586000c293c07d6', '410726', '延津县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c0af88353da11e68586000c293c07d6', '410727', '封丘县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c0ccdb453da11e68586000c293c07d6', '410728', '长垣县', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c0e98d953da11e68586000c293c07d6', '410781', '卫辉市', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c105b5853da11e68586000c293c07d6', '410782', '辉县市', null, null, '410700');
INSERT INTO `sys_area` VALUES ('8c122de953da11e68586000c293c07d6', '410800', '焦作市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c13eb3d53da11e68586000c293c07d6', '410801', '市辖区', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c15acdc53da11e68586000c293c07d6', '410802', '解放区', null, null, '410801');
INSERT INTO `sys_area` VALUES ('8c179b4153da11e68586000c293c07d6', '410803', '中站区', null, null, '410801');
INSERT INTO `sys_area` VALUES ('8c19797653da11e68586000c293c07d6', '410804', '马村区', null, null, '410801');
INSERT INTO `sys_area` VALUES ('8c1b279c53da11e68586000c293c07d6', '410811', '山阳区', null, null, '410801');
INSERT INTO `sys_area` VALUES ('8c1cfa2753da11e68586000c293c07d6', '410821', '修武县', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c1fef2e53da11e68586000c293c07d6', '410822', '博爱县', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c21b74d53da11e68586000c293c07d6', '410823', '武陟县', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c238f1453da11e68586000c293c07d6', '410825', '温县', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c254a5053da11e68586000c293c07d6', '410882', '沁阳市', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c2721bd53da11e68586000c293c07d6', '410883', '孟州市', null, null, '410800');
INSERT INTO `sys_area` VALUES ('8c2a47a153da11e68586000c293c07d6', '410900', '濮阳市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c2c1d2053da11e68586000c293c07d6', '410901', '市辖区', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c2e1b4a53da11e68586000c293c07d6', '410902', '华龙区', null, null, '410901');
INSERT INTO `sys_area` VALUES ('8c3000c353da11e68586000c293c07d6', '410922', '清丰县', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c31daa953da11e68586000c293c07d6', '410923', '南乐县', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c33d22953da11e68586000c293c07d6', '410926', '范县', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c35a5bd53da11e68586000c293c07d6', '410927', '台前县', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c37771053da11e68586000c293c07d6', '410928', '濮阳县', null, null, '410900');
INSERT INTO `sys_area` VALUES ('8c395bc653da11e68586000c293c07d6', '411000', '许昌市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c3b246053da11e68586000c293c07d6', '411001', '市辖区', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c3d06b353da11e68586000c293c07d6', '411002', '魏都区', null, null, '411001');
INSERT INTO `sys_area` VALUES ('8c3ec9c653da11e68586000c293c07d6', '411023', '许昌县', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c4090f653da11e68586000c293c07d6', '411024', '鄢陵县', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c42b42053da11e68586000c293c07d6', '411025', '襄城县', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c44882353da11e68586000c293c07d6', '411081', '禹州市', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c465c9553da11e68586000c293c07d6', '411082', '长葛市', null, null, '411000');
INSERT INTO `sys_area` VALUES ('8c48205a53da11e68586000c293c07d6', '411100', '漯河市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c4a01b253da11e68586000c293c07d6', '411101', '召陵区', null, null, '411100');
INSERT INTO `sys_area` VALUES ('8c4bd7df53da11e68586000c293c07d6', '411102', '源汇区', null, null, '411100');
INSERT INTO `sys_area` VALUES ('8c4eb67f53da11e68586000c293c07d6', '411103', '郾城区', null, null, '411100');
INSERT INTO `sys_area` VALUES ('8c50742a53da11e68586000c293c07d6', '411121', '舞阳县', null, null, '411100');
INSERT INTO `sys_area` VALUES ('8c52619e53da11e68586000c293c07d6', '411122', '临颍县', null, null, '411100');
INSERT INTO `sys_area` VALUES ('8c54429f53da11e68586000c293c07d6', '411200', '三门峡市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c5630f953da11e68586000c293c07d6', '411201', '市辖区', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c58053953da11e68586000c293c07d6', '411202', '湖滨区', null, null, '411201');
INSERT INTO `sys_area` VALUES ('8c59e4c153da11e68586000c293c07d6', '411221', '渑池县', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c5bc4be53da11e68586000c293c07d6', '411222', '陕县', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c5dcc1e53da11e68586000c293c07d6', '411224', '卢氏县', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c5f7c1953da11e68586000c293c07d6', '411281', '义马市', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c61550853da11e68586000c293c07d6', '411282', '灵宝市', null, null, '411200');
INSERT INTO `sys_area` VALUES ('8c6367ab53da11e68586000c293c07d6', '411300', '南阳市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c6544bd53da11e68586000c293c07d6', '411301', '市辖区', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c6718b253da11e68586000c293c07d6', '411302', '宛城区', null, null, '411301');
INSERT INTO `sys_area` VALUES ('8c68e2d353da11e68586000c293c07d6', '411303', '卧龙区', null, null, '411301');
INSERT INTO `sys_area` VALUES ('8c6ab75153da11e68586000c293c07d6', '411321', '南召县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c6ca84153da11e68586000c293c07d6', '411322', '方城县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c6e8bb053da11e68586000c293c07d6', '411323', '西峡县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c705de853da11e68586000c293c07d6', '411324', '镇平县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c724c8753da11e68586000c293c07d6', '411325', '内乡县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c742ac053da11e68586000c293c07d6', '411326', '淅川县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c75fef253da11e68586000c293c07d6', '411327', '社旗县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c77eb4553da11e68586000c293c07d6', '411328', '唐河县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c79bb9553da11e68586000c293c07d6', '411329', '新野县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c7b957153da11e68586000c293c07d6', '411330', '桐柏县', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c7d669553da11e68586000c293c07d6', '411381', '邓州市', null, null, '411300');
INSERT INTO `sys_area` VALUES ('8c7f91e053da11e68586000c293c07d6', '411400', '商丘市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c8167e353da11e68586000c293c07d6', '411401', '市辖区', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c83140653da11e68586000c293c07d6', '411402', '梁园区', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c8502df53da11e68586000c293c07d6', '411403', '睢阳区', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c8708fe53da11e68586000c293c07d6', '411421', '民权县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c89d64153da11e68586000c293c07d6', '411422', '睢县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c8c052253da11e68586000c293c07d6', '411423', '宁陵县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c8e00c853da11e68586000c293c07d6', '411424', '柘城县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c8f9ec653da11e68586000c293c07d6', '411425', '虞城县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c91bee553da11e68586000c293c07d6', '411426', '夏邑县', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c93c13d53da11e68586000c293c07d6', '411481', '永城市', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c95b5c353da11e68586000c293c07d6', '411482', '新区', null, null, '411400');
INSERT INTO `sys_area` VALUES ('8c97715053da11e68586000c293c07d6', '411500', '信阳市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8c99284853da11e68586000c293c07d6', '411501', '市辖区', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8c9b14e153da11e68586000c293c07d6', '411502', '浉河区', null, null, '411501');
INSERT INTO `sys_area` VALUES ('8c9d024a53da11e68586000c293c07d6', '411503', '平桥区', null, null, '411501');
INSERT INTO `sys_area` VALUES ('8c9eb6ce53da11e68586000c293c07d6', '411521', '罗山县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8ca0c04a53da11e68586000c293c07d6', '411522', '光山县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8ca2901653da11e68586000c293c07d6', '411523', '新县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8ca4505753da11e68586000c293c07d6', '411524', '商城县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8ca6491953da11e68586000c293c07d6', '411525', '固始县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8ca80c1f53da11e68586000c293c07d6', '411526', '潢川县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8caa158353da11e68586000c293c07d6', '411527', '淮滨县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8cac3cd153da11e68586000c293c07d6', '411528', '息县', null, null, '411500');
INSERT INTO `sys_area` VALUES ('8cadf50b53da11e68586000c293c07d6', '411600', '周口市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8cafe43d53da11e68586000c293c07d6', '411601', '市辖区', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cb1be4053da11e68586000c293c07d6', '411602', '川汇区', null, null, '411601');
INSERT INTO `sys_area` VALUES ('8cb49fd053da11e68586000c293c07d6', '411621', '扶沟县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cb67f1b53da11e68586000c293c07d6', '411622', '西华县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cb841f753da11e68586000c293c07d6', '411623', '商水县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cba803253da11e68586000c293c07d6', '411624', '沈丘县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cbc393853da11e68586000c293c07d6', '411625', '郸城县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cbdefc253da11e68586000c293c07d6', '411626', '淮阳县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cbfbcc553da11e68586000c293c07d6', '411627', '太康县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cc17a8753da11e68586000c293c07d6', '411628', '鹿邑县', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cc3269253da11e68586000c293c07d6', '411681', '项城市', null, null, '411600');
INSERT INTO `sys_area` VALUES ('8cc4f42f53da11e68586000c293c07d6', '411700', '驻马店市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8cc6d3dd53da11e68586000c293c07d6', '411701', '市辖区', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cc8b2c553da11e68586000c293c07d6', '411702', '驿城区', null, null, '411701');
INSERT INTO `sys_area` VALUES ('8cca74ae53da11e68586000c293c07d6', '411721', '西平县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8ccc646353da11e68586000c293c07d6', '411722', '上蔡县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cce36af53da11e68586000c293c07d6', '411723', '平舆县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cd02bf453da11e68586000c293c07d6', '411724', '正阳县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cd3112e53da11e68586000c293c07d6', '411725', '确山县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cd4bb7753da11e68586000c293c07d6', '411726', '泌阳县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cd69f5253da11e68586000c293c07d6', '411727', '汝南县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cdb94d053da11e68586000c293c07d6', '411728', '遂平县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cdd7f4453da11e68586000c293c07d6', '411729', '新蔡县', null, null, '411700');
INSERT INTO `sys_area` VALUES ('8cdf698853da11e68586000c293c07d6', '411800', '济源市', null, null, '410000');
INSERT INTO `sys_area` VALUES ('8ce10de153da11e68586000c293c07d6', '411801', '市辖区', null, null, '411800');
INSERT INTO `sys_area` VALUES ('8ce2e8c553da11e68586000c293c07d6', '420000', '湖北省', null, '17', '0');
INSERT INTO `sys_area` VALUES ('8ce4c2df53da11e68586000c293c07d6', '420100', '武汉市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8ce713a253da11e68586000c293c07d6', '420101', '市辖区', null, null, '420100');
INSERT INTO `sys_area` VALUES ('8cea02db53da11e68586000c293c07d6', '420102', '江岸区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cec8b2d53da11e68586000c293c07d6', '420103', '江汉区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cee7eb153da11e68586000c293c07d6', '420104', '硚口区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cf08b4b53da11e68586000c293c07d6', '420105', '汉阳区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cf33d8653da11e68586000c293c07d6', '420106', '武昌区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cf5aa7553da11e68586000c293c07d6', '420107', '青山区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cf7e02f53da11e68586000c293c07d6', '420111', '洪山区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cf9e2b553da11e68586000c293c07d6', '420112', '东西湖区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cfbd99653da11e68586000c293c07d6', '420113', '汉南区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8cfe0c3353da11e68586000c293c07d6', '420114', '蔡甸区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8d0010f653da11e68586000c293c07d6', '420115', '江夏区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8d020e8853da11e68586000c293c07d6', '420116', '黄陂区', null, null, '420101');
INSERT INTO `sys_area` VALUES ('8d049b5953da11e68586000c293c07d6', '420117', '新洲区', null, null, '420100');
INSERT INTO `sys_area` VALUES ('8d065ec353da11e68586000c293c07d6', '420200', '黄石市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d0869ab53da11e68586000c293c07d6', '420201', '市辖区', null, null, '420200');
INSERT INTO `sys_area` VALUES ('8d0a2c2253da11e68586000c293c07d6', '420202', '黄石港区', null, null, '420201');
INSERT INTO `sys_area` VALUES ('8d0c893453da11e68586000c293c07d6', '420203', '西塞山区', null, null, '420201');
INSERT INTO `sys_area` VALUES ('8d0ea39753da11e68586000c293c07d6', '420204', '下陆区', null, null, '420201');
INSERT INTO `sys_area` VALUES ('8d10f7fe53da11e68586000c293c07d6', '420205', '铁山区', null, null, '420201');
INSERT INTO `sys_area` VALUES ('8d12c18853da11e68586000c293c07d6', '420222', '阳新县', null, null, '420200');
INSERT INTO `sys_area` VALUES ('8d14533753da11e68586000c293c07d6', '420281', '大冶市', null, null, '420200');
INSERT INTO `sys_area` VALUES ('8d160be453da11e68586000c293c07d6', '420300', '十堰市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d1826c653da11e68586000c293c07d6', '420301', '市辖区', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d1a2feb53da11e68586000c293c07d6', '420302', '茅箭区', null, null, '420301');
INSERT INTO `sys_area` VALUES ('8d1bffd053da11e68586000c293c07d6', '420303', '张湾区', null, null, '420301');
INSERT INTO `sys_area` VALUES ('8d1db9d853da11e68586000c293c07d6', '420321', '郧县', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d1fa50553da11e68586000c293c07d6', '420322', '郧西县', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d219cbc53da11e68586000c293c07d6', '420323', '竹山县', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d233d5e53da11e68586000c293c07d6', '420324', '竹溪县', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d25137853da11e68586000c293c07d6', '420325', '房县', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d27ba1153da11e68586000c293c07d6', '420381', '丹江口市', null, null, '420300');
INSERT INTO `sys_area` VALUES ('8d298d9253da11e68586000c293c07d6', '420500', '宜昌市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d2bda3353da11e68586000c293c07d6', '420501', '市辖区', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d2da59653da11e68586000c293c07d6', '420502', '西陵区', null, null, '420501');
INSERT INTO `sys_area` VALUES ('8d2f8a9253da11e68586000c293c07d6', '420503', '伍家岗区', null, null, '420501');
INSERT INTO `sys_area` VALUES ('8d317abd53da11e68586000c293c07d6', '420504', '点军区', null, null, '420501');
INSERT INTO `sys_area` VALUES ('8d334b3253da11e68586000c293c07d6', '420505', '猇亭区', null, null, '420501');
INSERT INTO `sys_area` VALUES ('8d34fc8d53da11e68586000c293c07d6', '420506', '夷陵区', null, null, '420501');
INSERT INTO `sys_area` VALUES ('8d37226453da11e68586000c293c07d6', '420525', '远安县', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d3908b153da11e68586000c293c07d6', '420526', '兴山县', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d3af0d553da11e68586000c293c07d6', '420527', '秭归县', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d3c984353da11e68586000c293c07d6', '420528', '长阳土家族自治县', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d3ecef553da11e68586000c293c07d6', '420529', '五峰土家族自治县', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d40be8153da11e68586000c293c07d6', '420581', '宜都市', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d42ad2453da11e68586000c293c07d6', '420582', '当阳市', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d446db653da11e68586000c293c07d6', '420583', '枝江市', null, null, '420500');
INSERT INTO `sys_area` VALUES ('8d462d3453da11e68586000c293c07d6', '420600', '襄阳市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d4847ab53da11e68586000c293c07d6', '420601', '市辖区', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d4a187353da11e68586000c293c07d6', '420602', '襄城区', null, null, '420601');
INSERT INTO `sys_area` VALUES ('8d4be8d053da11e68586000c293c07d6', '420606', '樊城区', null, null, '420601');
INSERT INTO `sys_area` VALUES ('8d4db6b753da11e68586000c293c07d6', '420607', '襄州区', null, null, '420601');
INSERT INTO `sys_area` VALUES ('8d4f9ba253da11e68586000c293c07d6', '420624', '南漳县', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d51514f53da11e68586000c293c07d6', '420625', '谷城县', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d533f7753da11e68586000c293c07d6', '420626', '保康县', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d55152953da11e68586000c293c07d6', '420682', '老河口市', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d56bb9c53da11e68586000c293c07d6', '420683', '枣阳市', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d588eea53da11e68586000c293c07d6', '420684', '宜城市', null, null, '420600');
INSERT INTO `sys_area` VALUES ('8d5a64e453da11e68586000c293c07d6', '420700', '鄂州市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d5c714553da11e68586000c293c07d6', '420701', '市辖区', null, null, '420700');
INSERT INTO `sys_area` VALUES ('8d5e8dc253da11e68586000c293c07d6', '420702', '梁子湖区', null, null, '420701');
INSERT INTO `sys_area` VALUES ('8d6040ce53da11e68586000c293c07d6', '420703', '华容区', null, null, '420701');
INSERT INTO `sys_area` VALUES ('8d61f51853da11e68586000c293c07d6', '420704', '鄂城区', null, null, '420701');
INSERT INTO `sys_area` VALUES ('8d63abd353da11e68586000c293c07d6', '420800', '荆门市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d65705653da11e68586000c293c07d6', '420801', '市辖区', null, null, '420800');
INSERT INTO `sys_area` VALUES ('8d6745ce53da11e68586000c293c07d6', '420802', '东宝区', null, null, '420801');
INSERT INTO `sys_area` VALUES ('8d69123953da11e68586000c293c07d6', '420804', '掇刀区', null, null, '420801');
INSERT INTO `sys_area` VALUES ('8d6afb5753da11e68586000c293c07d6', '420821', '京山县', null, null, '420800');
INSERT INTO `sys_area` VALUES ('8d6cb84053da11e68586000c293c07d6', '420822', '沙洋县', null, null, '420800');
INSERT INTO `sys_area` VALUES ('8d6e9a1f53da11e68586000c293c07d6', '420881', '钟祥市', null, null, '420800');
INSERT INTO `sys_area` VALUES ('8d708cb553da11e68586000c293c07d6', '420900', '孝感市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d727b3e53da11e68586000c293c07d6', '420901', '市辖区', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d74498053da11e68586000c293c07d6', '420902', '孝南区', null, null, '420901');
INSERT INTO `sys_area` VALUES ('8d762cd953da11e68586000c293c07d6', '420921', '孝昌县', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d784dff53da11e68586000c293c07d6', '420922', '大悟县', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d79fe6d53da11e68586000c293c07d6', '420923', '云梦县', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d7bcb3853da11e68586000c293c07d6', '420981', '应城市', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d7d8eeb53da11e68586000c293c07d6', '420982', '安陆市', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d7f474053da11e68586000c293c07d6', '420984', '汉川市', null, null, '420900');
INSERT INTO `sys_area` VALUES ('8d810a3553da11e68586000c293c07d6', '421000', '荆州市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d82c34a53da11e68586000c293c07d6', '421001', '市辖区', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d84d3aa53da11e68586000c293c07d6', '421002', '沙市区', null, null, '421001');
INSERT INTO `sys_area` VALUES ('8d86b65253da11e68586000c293c07d6', '421003', '荆州区', null, null, '421001');
INSERT INTO `sys_area` VALUES ('8d888c8853da11e68586000c293c07d6', '421022', '公安县', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d8a78c753da11e68586000c293c07d6', '421023', '监利县', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d8c2f0153da11e68586000c293c07d6', '421024', '江陵县', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d8e181c53da11e68586000c293c07d6', '421081', '石首市', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d8fd7df53da11e68586000c293c07d6', '421083', '洪湖市', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d91c0c553da11e68586000c293c07d6', '421087', '松滋市', null, null, '421000');
INSERT INTO `sys_area` VALUES ('8d9383dc53da11e68586000c293c07d6', '421100', '黄冈市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8d954f6253da11e68586000c293c07d6', '421101', '市辖区', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8d97906753da11e68586000c293c07d6', '421102', '黄州区', null, null, '421101');
INSERT INTO `sys_area` VALUES ('8d99780e53da11e68586000c293c07d6', '421121', '团风县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8d9b850753da11e68586000c293c07d6', '421122', '红安县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8d9d615053da11e68586000c293c07d6', '421123', '罗田县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8d9f327453da11e68586000c293c07d6', '421124', '英山县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8da11c4353da11e68586000c293c07d6', '421125', '浠水县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8da2ec0d53da11e68586000c293c07d6', '421126', '蕲春县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8da4cc2753da11e68586000c293c07d6', '421127', '黄梅县', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8da69bda53da11e68586000c293c07d6', '421181', '麻城市', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8da868cd53da11e68586000c293c07d6', '421182', '武穴市', null, null, '421100');
INSERT INTO `sys_area` VALUES ('8daa1a4153da11e68586000c293c07d6', '421200', '咸宁市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8dac0c7353da11e68586000c293c07d6', '421201', '市辖区', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8daddfed53da11e68586000c293c07d6', '421202', '咸安区', null, null, '421201');
INSERT INTO `sys_area` VALUES ('8daffd1a53da11e68586000c293c07d6', '421221', '嘉鱼县', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8db1b10253da11e68586000c293c07d6', '421222', '通城县', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8db3959c53da11e68586000c293c07d6', '421223', '崇阳县', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8db57a6453da11e68586000c293c07d6', '421224', '通山县', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8db75d1453da11e68586000c293c07d6', '421281', '赤壁市', null, null, '421200');
INSERT INTO `sys_area` VALUES ('8db9100953da11e68586000c293c07d6', '421300', '随州市', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8dbaecf453da11e68586000c293c07d6', '421301', '市辖区', null, null, '421300');
INSERT INTO `sys_area` VALUES ('8dbcc39153da11e68586000c293c07d6', '421302', '曾都区', null, null, '421301');
INSERT INTO `sys_area` VALUES ('8dbe757453da11e68586000c293c07d6', '421381', '广水市', null, null, '421300');
INSERT INTO `sys_area` VALUES ('8dc0610253da11e68586000c293c07d6', '422800', '恩施土家族苗族自治州', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8dc222c353da11e68586000c293c07d6', '422801', '恩施市', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dc4ead153da11e68586000c293c07d6', '422802', '利川市', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dc7206f53da11e68586000c293c07d6', '422822', '建始县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dc9134453da11e68586000c293c07d6', '422823', '巴东县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dcac9e653da11e68586000c293c07d6', '422825', '宣恩县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dcc9bad53da11e68586000c293c07d6', '422826', '咸丰县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dce406c53da11e68586000c293c07d6', '422827', '来凤县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dd0063453da11e68586000c293c07d6', '422828', '鹤峰县', null, null, '422800');
INSERT INTO `sys_area` VALUES ('8dd22a3a53da11e68586000c293c07d6', '429000', '省直辖行政单位', null, null, '420000');
INSERT INTO `sys_area` VALUES ('8dd3d46b53da11e68586000c293c07d6', '429004', '仙桃市', null, null, '429000');
INSERT INTO `sys_area` VALUES ('8dd6355553da11e68586000c293c07d6', '429005', '潜江市', null, null, '429000');
INSERT INTO `sys_area` VALUES ('8dd7f6f253da11e68586000c293c07d6', '429006', '天门市', null, null, '429000');
INSERT INTO `sys_area` VALUES ('8dda193053da11e68586000c293c07d6', '429021', '神农架林区', null, null, '429000');
INSERT INTO `sys_area` VALUES ('8ddbc8f653da11e68586000c293c07d6', '430000', '湖南省', null, '18', '0');
INSERT INTO `sys_area` VALUES ('8ddd907953da11e68586000c293c07d6', '430100', '长沙市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8ddf5f3453da11e68586000c293c07d6', '430101', '市辖区', null, null, '430100');
INSERT INTO `sys_area` VALUES ('8de0fecb53da11e68586000c293c07d6', '430102', '芙蓉区', null, null, '430101');
INSERT INTO `sys_area` VALUES ('8de2d37a53da11e68586000c293c07d6', '430103', '天心区', null, null, '430101');
INSERT INTO `sys_area` VALUES ('8de4881353da11e68586000c293c07d6', '430104', '岳麓区', null, null, '430101');
INSERT INTO `sys_area` VALUES ('8de6836553da11e68586000c293c07d6', '430105', '开福区', null, null, '430101');
INSERT INTO `sys_area` VALUES ('8de884a953da11e68586000c293c07d6', '430111', '雨花区', null, null, '430101');
INSERT INTO `sys_area` VALUES ('8dea441253da11e68586000c293c07d6', '430121', '长沙县', null, null, '430100');
INSERT INTO `sys_area` VALUES ('8dec282353da11e68586000c293c07d6', '430122', '望城县', null, null, '430100');
INSERT INTO `sys_area` VALUES ('8dee0f3e53da11e68586000c293c07d6', '430124', '宁乡县', null, null, '430100');
INSERT INTO `sys_area` VALUES ('8defe90c53da11e68586000c293c07d6', '430181', '浏阳市', null, null, '430100');
INSERT INTO `sys_area` VALUES ('8df1a27753da11e68586000c293c07d6', '430200', '株洲市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8df377f653da11e68586000c293c07d6', '430201', '市辖区', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8df5541f53da11e68586000c293c07d6', '430202', '荷塘区', null, null, '430201');
INSERT INTO `sys_area` VALUES ('8df7176653da11e68586000c293c07d6', '430203', '芦淞区', null, null, '430201');
INSERT INTO `sys_area` VALUES ('8df8e98653da11e68586000c293c07d6', '430204', '石峰区', null, null, '430201');
INSERT INTO `sys_area` VALUES ('8dfada3553da11e68586000c293c07d6', '430211', '天元区', null, null, '430201');
INSERT INTO `sys_area` VALUES ('8dfca31e53da11e68586000c293c07d6', '430221', '株洲县', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8dfe602253da11e68586000c293c07d6', '430223', '攸县', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8e002a9853da11e68586000c293c07d6', '430224', '茶陵县', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8e02137e53da11e68586000c293c07d6', '430225', '炎陵县', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8e03f0a953da11e68586000c293c07d6', '430281', '醴陵市', null, null, '430200');
INSERT INTO `sys_area` VALUES ('8e06219c53da11e68586000c293c07d6', '430300', '湘潭市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e07e43953da11e68586000c293c07d6', '430301', '市辖区', null, null, '430300');
INSERT INTO `sys_area` VALUES ('8e09c48953da11e68586000c293c07d6', '430302', '雨湖区', null, null, '430301');
INSERT INTO `sys_area` VALUES ('8e0ba5b053da11e68586000c293c07d6', '430304', '岳塘区', null, null, '430301');
INSERT INTO `sys_area` VALUES ('8e0d82e153da11e68586000c293c07d6', '430321', '湘潭县', null, null, '430300');
INSERT INTO `sys_area` VALUES ('8e0f6d2353da11e68586000c293c07d6', '430381', '湘乡市', null, null, '430300');
INSERT INTO `sys_area` VALUES ('8e11756953da11e68586000c293c07d6', '430382', '韶山市', null, null, '430300');
INSERT INTO `sys_area` VALUES ('8e134cc553da11e68586000c293c07d6', '430400', '衡阳市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e15275153da11e68586000c293c07d6', '430401', '市辖区', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e16fbfd53da11e68586000c293c07d6', '430405', '珠晖区', null, null, '430401');
INSERT INTO `sys_area` VALUES ('8e18df3053da11e68586000c293c07d6', '430406', '雁峰区', null, null, '430401');
INSERT INTO `sys_area` VALUES ('8e1aac5753da11e68586000c293c07d6', '430407', '石鼓区', null, null, '430401');
INSERT INTO `sys_area` VALUES ('8e1c736953da11e68586000c293c07d6', '430408', '蒸湘区', null, null, '430401');
INSERT INTO `sys_area` VALUES ('8e1e4f4453da11e68586000c293c07d6', '430412', '南岳区', null, null, '430401');
INSERT INTO `sys_area` VALUES ('8e20412f53da11e68586000c293c07d6', '430421', '衡阳县', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e22284d53da11e68586000c293c07d6', '430422', '衡南县', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e23f3e153da11e68586000c293c07d6', '430423', '衡山县', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e26046253da11e68586000c293c07d6', '430424', '衡东县', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e28484d53da11e68586000c293c07d6', '430426', '祁东县', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e2a3f8153da11e68586000c293c07d6', '430481', '耒阳市', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e2bf95753da11e68586000c293c07d6', '430482', '常宁市', null, null, '430400');
INSERT INTO `sys_area` VALUES ('8e2de12d53da11e68586000c293c07d6', '430500', '邵阳市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e2fb69a53da11e68586000c293c07d6', '430501', '市辖区', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e3198cb53da11e68586000c293c07d6', '430502', '双清区', null, null, '430501');
INSERT INTO `sys_area` VALUES ('8e35266153da11e68586000c293c07d6', '430503', '大祥区', null, null, '430501');
INSERT INTO `sys_area` VALUES ('8e3700b753da11e68586000c293c07d6', '430511', '北塔区', null, null, '430501');
INSERT INTO `sys_area` VALUES ('8e38d11053da11e68586000c293c07d6', '430521', '邵东县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e3ab6c753da11e68586000c293c07d6', '430522', '新邵县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e3c8dcb53da11e68586000c293c07d6', '430523', '邵阳县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e3e832753da11e68586000c293c07d6', '430524', '隆回县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e4037b853da11e68586000c293c07d6', '430525', '洞口县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e41fcad53da11e68586000c293c07d6', '430527', '绥宁县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e43ec9253da11e68586000c293c07d6', '430528', '新宁县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e45b6d853da11e68586000c293c07d6', '430529', '城步苗族自治县', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e4797f053da11e68586000c293c07d6', '430581', '武冈市', null, null, '430500');
INSERT INTO `sys_area` VALUES ('8e496c9e53da11e68586000c293c07d6', '430600', '岳阳市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e4b4b9553da11e68586000c293c07d6', '430601', '市辖区', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e4d159953da11e68586000c293c07d6', '430602', '岳阳楼区', null, null, '430601');
INSERT INTO `sys_area` VALUES ('8e4edbcc53da11e68586000c293c07d6', '430603', '云溪区', null, null, '430601');
INSERT INTO `sys_area` VALUES ('8e50ac0453da11e68586000c293c07d6', '430611', '君山区', null, null, '430601');
INSERT INTO `sys_area` VALUES ('8e53327853da11e68586000c293c07d6', '430621', '岳阳县', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e55056e53da11e68586000c293c07d6', '430623', '华容县', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e56b25653da11e68586000c293c07d6', '430624', '湘阴县', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e588da753da11e68586000c293c07d6', '430626', '平江县', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e5a556653da11e68586000c293c07d6', '430681', '汨罗市', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e5c40ec53da11e68586000c293c07d6', '430682', '临湘市', null, null, '430600');
INSERT INTO `sys_area` VALUES ('8e5e097053da11e68586000c293c07d6', '430700', '常德市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e60375053da11e68586000c293c07d6', '430701', '市辖区', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e62598a53da11e68586000c293c07d6', '430702', '武陵区', null, null, '430701');
INSERT INTO `sys_area` VALUES ('8e6440ac53da11e68586000c293c07d6', '430703', '鼎城区', null, null, '430701');
INSERT INTO `sys_area` VALUES ('8e661b3253da11e68586000c293c07d6', '430721', '安乡县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e680b1f53da11e68586000c293c07d6', '430722', '汉寿县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e69e46053da11e68586000c293c07d6', '430723', '澧县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e6bb5f153da11e68586000c293c07d6', '430724', '临澧县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e6d9d0353da11e68586000c293c07d6', '430725', '桃源县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e6facbe53da11e68586000c293c07d6', '430726', '石门县', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e71b12453da11e68586000c293c07d6', '430781', '津市市', null, null, '430700');
INSERT INTO `sys_area` VALUES ('8e73895753da11e68586000c293c07d6', '430800', '张家界市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e754b7953da11e68586000c293c07d6', '430801', '市辖区', null, null, '430800');
INSERT INTO `sys_area` VALUES ('8e794a3f53da11e68586000c293c07d6', '430802', '永定区', null, null, '430801');
INSERT INTO `sys_area` VALUES ('8e7b4ac153da11e68586000c293c07d6', '430811', '武陵源区', null, null, '430801');
INSERT INTO `sys_area` VALUES ('8e7cf0a653da11e68586000c293c07d6', '430821', '慈利县', null, null, '430800');
INSERT INTO `sys_area` VALUES ('8e7ecba653da11e68586000c293c07d6', '430822', '桑植县', null, null, '430800');
INSERT INTO `sys_area` VALUES ('8e80c1d453da11e68586000c293c07d6', '430900', '益阳市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e827c2a53da11e68586000c293c07d6', '430901', '市辖区', null, null, '430900');
INSERT INTO `sys_area` VALUES ('8e844fa053da11e68586000c293c07d6', '430902', '资阳区', null, null, '430901');
INSERT INTO `sys_area` VALUES ('8e86284053da11e68586000c293c07d6', '430903', '赫山区', null, null, '430901');
INSERT INTO `sys_area` VALUES ('8e87facd53da11e68586000c293c07d6', '430921', '南县', null, null, '430900');
INSERT INTO `sys_area` VALUES ('8e89e66553da11e68586000c293c07d6', '430922', '桃江县', null, null, '430900');
INSERT INTO `sys_area` VALUES ('8e8bc98853da11e68586000c293c07d6', '430923', '安化县', null, null, '430900');
INSERT INTO `sys_area` VALUES ('8e8dd06153da11e68586000c293c07d6', '430981', '沅江市', null, null, '430900');
INSERT INTO `sys_area` VALUES ('8e8fbf8553da11e68586000c293c07d6', '431000', '郴州市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8e9191e253da11e68586000c293c07d6', '431001', '市辖区', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8e935f2e53da11e68586000c293c07d6', '431002', '北湖区', null, null, '431001');
INSERT INTO `sys_area` VALUES ('8e95c84a53da11e68586000c293c07d6', '431003', '苏仙区', null, null, '431001');
INSERT INTO `sys_area` VALUES ('8e979de053da11e68586000c293c07d6', '431021', '桂阳县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8e99a0af53da11e68586000c293c07d6', '431022', '宜章县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8e9e31f153da11e68586000c293c07d6', '431023', '永兴县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea011e753da11e68586000c293c07d6', '431024', '嘉禾县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea23c0f53da11e68586000c293c07d6', '431025', '临武县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea4268853da11e68586000c293c07d6', '431026', '汝城县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea5f2a053da11e68586000c293c07d6', '431027', '桂东县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea7f92753da11e68586000c293c07d6', '431028', '安仁县', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8ea9cde553da11e68586000c293c07d6', '431081', '资兴市', null, null, '431000');
INSERT INTO `sys_area` VALUES ('8eac1af953da11e68586000c293c07d6', '431100', '永州市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8eade3ce53da11e68586000c293c07d6', '431101', '市辖区', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8eb0169153da11e68586000c293c07d6', '431102', '零陵区', null, null, '431101');
INSERT INTO `sys_area` VALUES ('8eb27ca953da11e68586000c293c07d6', '431103', '冷水滩区', null, null, '431101');
INSERT INTO `sys_area` VALUES ('8eb4712e53da11e68586000c293c07d6', '431121', '祁阳县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8eb6553253da11e68586000c293c07d6', '431122', '东安县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8eb895a153da11e68586000c293c07d6', '431123', '双牌县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8eba779653da11e68586000c293c07d6', '431124', '道县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ebc4eb053da11e68586000c293c07d6', '431125', '江永县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ebe508753da11e68586000c293c07d6', '431126', '宁远县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ec0c89353da11e68586000c293c07d6', '431127', '蓝山县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ec2cb9a53da11e68586000c293c07d6', '431128', '新田县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ec4eb2453da11e68586000c293c07d6', '431129', '江华瑶族自治县', null, null, '431100');
INSERT INTO `sys_area` VALUES ('8ec7720d53da11e68586000c293c07d6', '431200', '怀化市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8ec98d2e53da11e68586000c293c07d6', '431201', '市辖区', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ecb9d0a53da11e68586000c293c07d6', '431202', '鹤城区', null, null, '431201');
INSERT INTO `sys_area` VALUES ('8ece20dd53da11e68586000c293c07d6', '431221', '中方县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ed0694553da11e68586000c293c07d6', '431222', '沅陵县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ed257de53da11e68586000c293c07d6', '431223', '辰溪县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ed41cce53da11e68586000c293c07d6', '431224', '溆浦县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ed629f453da11e68586000c293c07d6', '431225', '会同县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ed8504f53da11e68586000c293c07d6', '431226', '麻阳苗族自治县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8eda7aac53da11e68586000c293c07d6', '431227', '新晃侗族自治县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8edc491753da11e68586000c293c07d6', '431228', '芷江侗族自治县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ede327c53da11e68586000c293c07d6', '431229', '靖州苗族侗族自治县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ee0087753da11e68586000c293c07d6', '431230', '通道侗族自治县', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ee2ae3753da11e68586000c293c07d6', '431281', '洪江市', null, null, '431200');
INSERT INTO `sys_area` VALUES ('8ee47cca53da11e68586000c293c07d6', '431300', '娄底市', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8ee6912253da11e68586000c293c07d6', '431301', '市辖区', null, null, '431300');
INSERT INTO `sys_area` VALUES ('8ee8937753da11e68586000c293c07d6', '431302', '娄星区', null, null, '431301');
INSERT INTO `sys_area` VALUES ('8eea5a5853da11e68586000c293c07d6', '431321', '双峰县', null, null, '431300');
INSERT INTO `sys_area` VALUES ('8eec4f8553da11e68586000c293c07d6', '431322', '新化县', null, null, '431300');
INSERT INTO `sys_area` VALUES ('8eee281553da11e68586000c293c07d6', '431381', '冷水江市', null, null, '431300');
INSERT INTO `sys_area` VALUES ('8eeffc8753da11e68586000c293c07d6', '431382', '涟源市', null, null, '431300');
INSERT INTO `sys_area` VALUES ('8ef1be6453da11e68586000c293c07d6', '433100', '湘西土家族苗族自治州', null, null, '430000');
INSERT INTO `sys_area` VALUES ('8ef38d5f53da11e68586000c293c07d6', '433101', '吉首市', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8ef589cd53da11e68586000c293c07d6', '433122', '泸溪县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8ef7439953da11e68586000c293c07d6', '433123', '凤凰县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8ef9430253da11e68586000c293c07d6', '433124', '花垣县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8efb14fd53da11e68586000c293c07d6', '433125', '保靖县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8efd5b1e53da11e68586000c293c07d6', '433126', '古丈县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8eff158e53da11e68586000c293c07d6', '433127', '永顺县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8f010e2853da11e68586000c293c07d6', '433130', '龙山县', null, null, '433100');
INSERT INTO `sys_area` VALUES ('8f031b1f53da11e68586000c293c07d6', '440000', '广东省', null, '19', '0');
INSERT INTO `sys_area` VALUES ('8f0500f553da11e68586000c293c07d6', '440100', '广州市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f07564153da11e68586000c293c07d6', '440101', '市辖区', null, null, '440100');
INSERT INTO `sys_area` VALUES ('8f0945f153da11e68586000c293c07d6', '440103', '荔湾区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f0b17ff53da11e68586000c293c07d6', '440104', '越秀区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f0cf41053da11e68586000c293c07d6', '440105', '海珠区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f0ed33653da11e68586000c293c07d6', '440106', '天河区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f10953b53da11e68586000c293c07d6', '440111', '白云区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f1284f953da11e68586000c293c07d6', '440112', '黄埔区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f146b8753da11e68586000c293c07d6', '440113', '番禺区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f164fcf53da11e68586000c293c07d6', '440114', '花都区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f18410c53da11e68586000c293c07d6', '440115', '南沙区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f1a503453da11e68586000c293c07d6', '440116', '萝岗区', null, null, '440101');
INSERT INTO `sys_area` VALUES ('8f1c35dd53da11e68586000c293c07d6', '440183', '增城市', null, null, '440100');
INSERT INTO `sys_area` VALUES ('8f1e144353da11e68586000c293c07d6', '440184', '从化市', null, null, '440100');
INSERT INTO `sys_area` VALUES ('8f20384253da11e68586000c293c07d6', '440200', '韶关市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f21fc1b53da11e68586000c293c07d6', '440201', '市辖区', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f23fdd953da11e68586000c293c07d6', '440203', '武江区', null, null, '440201');
INSERT INTO `sys_area` VALUES ('8f25d05f53da11e68586000c293c07d6', '440204', '浈江区', null, null, '440201');
INSERT INTO `sys_area` VALUES ('8f27a6c553da11e68586000c293c07d6', '440205', '曲江区', null, null, '440201');
INSERT INTO `sys_area` VALUES ('8f29885253da11e68586000c293c07d6', '440222', '始兴县', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f2b67d753da11e68586000c293c07d6', '440224', '仁化县', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f2d50d353da11e68586000c293c07d6', '440229', '翁源县', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f2f383153da11e68586000c293c07d6', '440232', '乳源瑶族自治县', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f311a1253da11e68586000c293c07d6', '440233', '新丰县', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f32f9a353da11e68586000c293c07d6', '440281', '乐昌市', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f34d10753da11e68586000c293c07d6', '440282', '南雄市', null, null, '440200');
INSERT INTO `sys_area` VALUES ('8f369a5053da11e68586000c293c07d6', '440300', '深圳市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f38cdbb53da11e68586000c293c07d6', '440301', '市辖区', null, null, '440300');
INSERT INTO `sys_area` VALUES ('8f3aa62a53da11e68586000c293c07d6', '440303', '罗湖区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f3c7cf453da11e68586000c293c07d6', '440304', '福田区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f3ec4b753da11e68586000c293c07d6', '440305', '南山区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f40d1a253da11e68586000c293c07d6', '440306', '宝安区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f42a2dd53da11e68586000c293c07d6', '440307', '龙岗区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f44795953da11e68586000c293c07d6', '440308', '盐田区', null, null, '440301');
INSERT INTO `sys_area` VALUES ('8f46446253da11e68586000c293c07d6', '440400', '珠海市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f4862fd53da11e68586000c293c07d6', '440401', '市辖区', null, null, '440400');
INSERT INTO `sys_area` VALUES ('8f4a612753da11e68586000c293c07d6', '440402', '香洲区', null, null, '440401');
INSERT INTO `sys_area` VALUES ('8f4c516b53da11e68586000c293c07d6', '440403', '斗门区', null, null, '440401');
INSERT INTO `sys_area` VALUES ('8f4eb32153da11e68586000c293c07d6', '440404', '金湾区', null, null, '440401');
INSERT INTO `sys_area` VALUES ('8f50617153da11e68586000c293c07d6', '440500', '汕头市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f52323553da11e68586000c293c07d6', '440501', '市辖区', null, null, '440500');
INSERT INTO `sys_area` VALUES ('8f5452c953da11e68586000c293c07d6', '440507', '龙湖区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f56469053da11e68586000c293c07d6', '440511', '金平区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f57fc4053da11e68586000c293c07d6', '440512', '濠江区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f5b1ec253da11e68586000c293c07d6', '440513', '潮阳区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f5e230153da11e68586000c293c07d6', '440514', '潮南区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f5ffd6353da11e68586000c293c07d6', '440515', '澄海区', null, null, '440501');
INSERT INTO `sys_area` VALUES ('8f61dbfc53da11e68586000c293c07d6', '440523', '南澳县', null, null, '440500');
INSERT INTO `sys_area` VALUES ('8f63b37053da11e68586000c293c07d6', '440600', '佛山市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f65f00053da11e68586000c293c07d6', '440601', '市辖区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f67bf7f53da11e68586000c293c07d6', '440604', '禅城区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f69a22a53da11e68586000c293c07d6', '440605', '南海区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f6bad9353da11e68586000c293c07d6', '440606', '顺德区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f6d940453da11e68586000c293c07d6', '440607', '三水区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f6f8bd753da11e68586000c293c07d6', '440608', '高明区', null, null, '440600');
INSERT INTO `sys_area` VALUES ('8f713d0253da11e68586000c293c07d6', '440700', '江门市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f7323a353da11e68586000c293c07d6', '440701', '市辖区', null, null, '440700');
INSERT INTO `sys_area` VALUES ('8f75344853da11e68586000c293c07d6', '440703', '蓬江区', null, null, '440701');
INSERT INTO `sys_area` VALUES ('8f7987b353da11e68586000c293c07d6', '440704', '江海区', null, null, '440701');
INSERT INTO `sys_area` VALUES ('8f7b7e4553da11e68586000c293c07d6', '440705', '新会区', null, null, '440701');
INSERT INTO `sys_area` VALUES ('8f7da31f53da11e68586000c293c07d6', '440781', '台山市', null, null, '440700');
INSERT INTO `sys_area` VALUES ('8f7f582b53da11e68586000c293c07d6', '440783', '开平市', null, null, '440700');
INSERT INTO `sys_area` VALUES ('8f813a3353da11e68586000c293c07d6', '440784', '鹤山市', null, null, '440700');
INSERT INTO `sys_area` VALUES ('8f8337f653da11e68586000c293c07d6', '440785', '恩平市', null, null, '440700');
INSERT INTO `sys_area` VALUES ('8f85033d53da11e68586000c293c07d6', '440800', '湛江市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f870e4c53da11e68586000c293c07d6', '440801', '市辖区', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f88d20953da11e68586000c293c07d6', '440802', '赤坎区', null, null, '440801');
INSERT INTO `sys_area` VALUES ('8f8b062d53da11e68586000c293c07d6', '440803', '霞山区', null, null, '440801');
INSERT INTO `sys_area` VALUES ('8f8dcf2a53da11e68586000c293c07d6', '440804', '坡头区', null, null, '440801');
INSERT INTO `sys_area` VALUES ('8f8f8da753da11e68586000c293c07d6', '440811', '麻章区', null, null, '440801');
INSERT INTO `sys_area` VALUES ('8f918c7053da11e68586000c293c07d6', '440823', '遂溪县', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f9389ef53da11e68586000c293c07d6', '440825', '徐闻县', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f955a9153da11e68586000c293c07d6', '440881', '廉江市', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f97b5b053da11e68586000c293c07d6', '440882', '雷州市', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f99775053da11e68586000c293c07d6', '440883', '吴川市', null, null, '440800');
INSERT INTO `sys_area` VALUES ('8f9be1ac53da11e68586000c293c07d6', '440900', '茂名市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8f9daf1553da11e68586000c293c07d6', '440901', '市辖区', null, null, '440900');
INSERT INTO `sys_area` VALUES ('8fa28dac53da11e68586000c293c07d6', '440902', '茂南区', null, null, '440901');
INSERT INTO `sys_area` VALUES ('8fa45d2c53da11e68586000c293c07d6', '440903', '茂港区', null, null, '440901');
INSERT INTO `sys_area` VALUES ('8fa6367453da11e68586000c293c07d6', '440923', '电白县', null, null, '440900');
INSERT INTO `sys_area` VALUES ('8fa80d6953da11e68586000c293c07d6', '440981', '高州市', null, null, '440900');
INSERT INTO `sys_area` VALUES ('8faa05b853da11e68586000c293c07d6', '440982', '化州市', null, null, '440900');
INSERT INTO `sys_area` VALUES ('8fabec1b53da11e68586000c293c07d6', '440983', '信宜市', null, null, '440900');
INSERT INTO `sys_area` VALUES ('8faddcc753da11e68586000c293c07d6', '441200', '肇庆市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8fafaccd53da11e68586000c293c07d6', '441201', '市辖区', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fb1d05453da11e68586000c293c07d6', '441202', '端州区', null, null, '441201');
INSERT INTO `sys_area` VALUES ('8fb37edb53da11e68586000c293c07d6', '441203', '鼎湖区', null, null, '441201');
INSERT INTO `sys_area` VALUES ('8fb56bfb53da11e68586000c293c07d6', '441223', '广宁县', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fb7446053da11e68586000c293c07d6', '441224', '怀集县', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fb948a353da11e68586000c293c07d6', '441225', '封开县', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fbb0ca753da11e68586000c293c07d6', '441226', '德庆县', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fbce3a253da11e68586000c293c07d6', '441283', '高要市', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fbef9b353da11e68586000c293c07d6', '441284', '四会市', null, null, '441200');
INSERT INTO `sys_area` VALUES ('8fc0fd6153da11e68586000c293c07d6', '441300', '惠州市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8fc2bc8053da11e68586000c293c07d6', '441301', '市辖区', null, null, '441300');
INSERT INTO `sys_area` VALUES ('8fc49bce53da11e68586000c293c07d6', '441302', '惠城区', null, null, '441301');
INSERT INTO `sys_area` VALUES ('8fc6537953da11e68586000c293c07d6', '441303', '惠阳区', null, null, '441301');
INSERT INTO `sys_area` VALUES ('8fc848a753da11e68586000c293c07d6', '441322', '博罗县', null, null, '441300');
INSERT INTO `sys_area` VALUES ('8fca4bb853da11e68586000c293c07d6', '441323', '惠东县', null, null, '441300');
INSERT INTO `sys_area` VALUES ('8fcc46fd53da11e68586000c293c07d6', '441324', '龙门县', null, null, '441300');
INSERT INTO `sys_area` VALUES ('8fcdfb0953da11e68586000c293c07d6', '441400', '梅州市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8fcfb5d853da11e68586000c293c07d6', '441401', '市辖区', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fd150c753da11e68586000c293c07d6', '441402', '梅江区', null, null, '441401');
INSERT INTO `sys_area` VALUES ('8fd334e653da11e68586000c293c07d6', '441421', '梅县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fd5166953da11e68586000c293c07d6', '441422', '大埔县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fd6cebb53da11e68586000c293c07d6', '441423', '丰顺县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fd8ecdc53da11e68586000c293c07d6', '441424', '五华县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fdabb3453da11e68586000c293c07d6', '441426', '平远县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fdc725c53da11e68586000c293c07d6', '441427', '蕉岭县', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fde369d53da11e68586000c293c07d6', '441481', '兴宁市', null, null, '441400');
INSERT INTO `sys_area` VALUES ('8fe03d1953da11e68586000c293c07d6', '441500', '汕尾市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8fe20c1553da11e68586000c293c07d6', '441501', '市辖区', null, null, '441500');
INSERT INTO `sys_area` VALUES ('8fe4538953da11e68586000c293c07d6', '441502', '城区', null, null, '441501');
INSERT INTO `sys_area` VALUES ('8fe625e053da11e68586000c293c07d6', '441521', '海丰县', null, null, '441500');
INSERT INTO `sys_area` VALUES ('8fe8a69153da11e68586000c293c07d6', '441523', '陆河县', null, null, '441500');
INSERT INTO `sys_area` VALUES ('8fea7e7353da11e68586000c293c07d6', '441581', '陆丰市', null, null, '441500');
INSERT INTO `sys_area` VALUES ('8fec5f9a53da11e68586000c293c07d6', '441600', '河源市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8fee79ba53da11e68586000c293c07d6', '441601', '市辖区', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ff02f3453da11e68586000c293c07d6', '441602', '源城区', null, null, '441601');
INSERT INTO `sys_area` VALUES ('8ff2ed7153da11e68586000c293c07d6', '441621', '紫金县', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ff51bd653da11e68586000c293c07d6', '441622', '龙川县', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ff6e5d653da11e68586000c293c07d6', '441623', '连平县', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ff9024853da11e68586000c293c07d6', '441624', '和平县', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ffb29ce53da11e68586000c293c07d6', '441625', '东源县', null, null, '441600');
INSERT INTO `sys_area` VALUES ('8ffcf63653da11e68586000c293c07d6', '441700', '阳江市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('8ffedc0d53da11e68586000c293c07d6', '441701', '市辖区', null, null, '441700');
INSERT INTO `sys_area` VALUES ('9000bbed53da11e68586000c293c07d6', '441702', '江城区', null, null, '441701');
INSERT INTO `sys_area` VALUES ('9002cf2353da11e68586000c293c07d6', '441721', '阳西县', null, null, '441700');
INSERT INTO `sys_area` VALUES ('9004a03c53da11e68586000c293c07d6', '441723', '阳东县', null, null, '441700');
INSERT INTO `sys_area` VALUES ('9006857753da11e68586000c293c07d6', '441781', '阳春市', null, null, '441700');
INSERT INTO `sys_area` VALUES ('9008a77853da11e68586000c293c07d6', '441800', '清远市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('900bf21e53da11e68586000c293c07d6', '441801', '市辖区', null, null, '441800');
INSERT INTO `sys_area` VALUES ('900dcac053da11e68586000c293c07d6', '441802', '清城区', null, null, '441801');
INSERT INTO `sys_area` VALUES ('900fe33e53da11e68586000c293c07d6', '441821', '佛冈县', null, null, '441800');
INSERT INTO `sys_area` VALUES ('9011c18f53da11e68586000c293c07d6', '441823', '阳山县', null, null, '441800');
INSERT INTO `sys_area` VALUES ('9013863d53da11e68586000c293c07d6', '441825', '连山壮族瑶族自治县', null, null, '441800');
INSERT INTO `sys_area` VALUES ('90157aa453da11e68586000c293c07d6', '441826', '连南瑶族自治县', null, null, '441800');
INSERT INTO `sys_area` VALUES ('9017601153da11e68586000c293c07d6', '441827', '清新县', null, null, '441800');
INSERT INTO `sys_area` VALUES ('9019d2e353da11e68586000c293c07d6', '441881', '英德市', null, null, '441800');
INSERT INTO `sys_area` VALUES ('901bb5a653da11e68586000c293c07d6', '441882', '连州市', null, null, '441800');
INSERT INTO `sys_area` VALUES ('901d802053da11e68586000c293c07d6', '441900', '东莞市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('901f759c53da11e68586000c293c07d6', '442000', '中山市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('90212e9853da11e68586000c293c07d6', '445100', '潮州市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('9022ee8753da11e68586000c293c07d6', '445101', '市辖区', null, null, '445100');
INSERT INTO `sys_area` VALUES ('9024d3bd53da11e68586000c293c07d6', '445102', '湘桥区', null, null, '445101');
INSERT INTO `sys_area` VALUES ('9026f8f953da11e68586000c293c07d6', '445121', '潮安区', null, null, '445100');
INSERT INTO `sys_area` VALUES ('9028b87953da11e68586000c293c07d6', '445122', '饶平县', null, null, '445100');
INSERT INTO `sys_area` VALUES ('902a8d7253da11e68586000c293c07d6', '445200', '揭阳市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('902c7c5a53da11e68586000c293c07d6', '445201', '市辖区', null, null, '445200');
INSERT INTO `sys_area` VALUES ('902f57c553da11e68586000c293c07d6', '445202', '榕城区', null, null, '445201');
INSERT INTO `sys_area` VALUES ('903181be53da11e68586000c293c07d6', '445221', '揭东县', null, null, '445200');
INSERT INTO `sys_area` VALUES ('90336fde53da11e68586000c293c07d6', '445222', '揭西县', null, null, '445200');
INSERT INTO `sys_area` VALUES ('9035405353da11e68586000c293c07d6', '445224', '惠来县', null, null, '445200');
INSERT INTO `sys_area` VALUES ('9037200d53da11e68586000c293c07d6', '445281', '普宁市', null, null, '445200');
INSERT INTO `sys_area` VALUES ('903905ea53da11e68586000c293c07d6', '445300', '云浮市', null, null, '440000');
INSERT INTO `sys_area` VALUES ('903af80753da11e68586000c293c07d6', '445301', '市辖区', null, null, '445300');
INSERT INTO `sys_area` VALUES ('903cabff53da11e68586000c293c07d6', '445302', '云城区', null, null, '445301');
INSERT INTO `sys_area` VALUES ('903e872f53da11e68586000c293c07d6', '445321', '新兴县', null, null, '445300');
INSERT INTO `sys_area` VALUES ('9040598253da11e68586000c293c07d6', '445322', '郁南县', null, null, '445300');
INSERT INTO `sys_area` VALUES ('9042710d53da11e68586000c293c07d6', '445323', '云安县', null, null, '445300');
INSERT INTO `sys_area` VALUES ('90441f6353da11e68586000c293c07d6', '445381', '罗定市', null, null, '445300');
INSERT INTO `sys_area` VALUES ('9045fc8953da11e68586000c293c07d6', '450000', '广西壮族自治区', null, '20', '0');
INSERT INTO `sys_area` VALUES ('9047c5d853da11e68586000c293c07d6', '450100', '南宁市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90497fd053da11e68586000c293c07d6', '450101', '市辖区', null, null, '450100');
INSERT INTO `sys_area` VALUES ('904b6a3453da11e68586000c293c07d6', '450102', '兴宁区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('904d706c53da11e68586000c293c07d6', '450103', '青秀区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('90509c0d53da11e68586000c293c07d6', '450105', '江南区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('9052953253da11e68586000c293c07d6', '450107', '西乡塘区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('9055008453da11e68586000c293c07d6', '450108', '良庆区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('90570a3553da11e68586000c293c07d6', '450109', '邕宁区', null, null, '450101');
INSERT INTO `sys_area` VALUES ('9058e6dd53da11e68586000c293c07d6', '450122', '武鸣县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('905ab8f753da11e68586000c293c07d6', '450123', '隆安县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('905c760853da11e68586000c293c07d6', '450124', '马山县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('905e620f53da11e68586000c293c07d6', '450125', '上林县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('906012a053da11e68586000c293c07d6', '450126', '宾阳县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('9061f11853da11e68586000c293c07d6', '450127', '横县', null, null, '450100');
INSERT INTO `sys_area` VALUES ('9063d89253da11e68586000c293c07d6', '450200', '柳州市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90658c6353da11e68586000c293c07d6', '450201', '市辖区', null, null, '450200');
INSERT INTO `sys_area` VALUES ('90677f1153da11e68586000c293c07d6', '450202', '城中区', null, null, '450201');
INSERT INTO `sys_area` VALUES ('90694ad953da11e68586000c293c07d6', '450203', '鱼峰区', null, null, '450201');
INSERT INTO `sys_area` VALUES ('906b299a53da11e68586000c293c07d6', '450204', '柳南区', null, null, '450201');
INSERT INTO `sys_area` VALUES ('906cd48653da11e68586000c293c07d6', '450205', '柳北区', null, null, '450201');
INSERT INTO `sys_area` VALUES ('906e9b7b53da11e68586000c293c07d6', '450221', '柳江县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('90705dfe53da11e68586000c293c07d6', '450222', '柳城县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('9072635553da11e68586000c293c07d6', '450223', '鹿寨县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('90741ee553da11e68586000c293c07d6', '450224', '融安县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('9075fa1553da11e68586000c293c07d6', '450225', '融水苗族自治县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('9077ce7c53da11e68586000c293c07d6', '450226', '三江侗族自治县', null, null, '450200');
INSERT INTO `sys_area` VALUES ('907a74f453da11e68586000c293c07d6', '450300', '桂林市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('907c228453da11e68586000c293c07d6', '450301', '市辖区', null, null, '450300');
INSERT INTO `sys_area` VALUES ('907e170253da11e68586000c293c07d6', '450302', '秀峰区', null, null, '450301');
INSERT INTO `sys_area` VALUES ('9080399853da11e68586000c293c07d6', '450303', '叠彩区', null, null, '450301');
INSERT INTO `sys_area` VALUES ('9082b0b453da11e68586000c293c07d6', '450304', '象山区', null, null, '450301');
INSERT INTO `sys_area` VALUES ('9084a94f53da11e68586000c293c07d6', '450305', '七星区', null, null, '450301');
INSERT INTO `sys_area` VALUES ('9086919753da11e68586000c293c07d6', '450311', '雁山区', null, null, '450301');
INSERT INTO `sys_area` VALUES ('9088d52c53da11e68586000c293c07d6', '450321', '阳朔县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('908a824953da11e68586000c293c07d6', '450322', '临桂区', null, null, '450300');
INSERT INTO `sys_area` VALUES ('908cb20c53da11e68586000c293c07d6', '450323', '灵川县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('908ed80853da11e68586000c293c07d6', '450324', '全州县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('909136d553da11e68586000c293c07d6', '450325', '兴安县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('9093b2a053da11e68586000c293c07d6', '450326', '永福县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('9095990d53da11e68586000c293c07d6', '450327', '灌阳县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('9097b6e353da11e68586000c293c07d6', '450328', '龙胜各族自治县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('9099c17c53da11e68586000c293c07d6', '450329', '资源县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('909bdc4a53da11e68586000c293c07d6', '450330', '平乐县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('909dd5d553da11e68586000c293c07d6', '450331', '荔浦县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('909fbfe153da11e68586000c293c07d6', '450332', '恭城瑶族自治县', null, null, '450300');
INSERT INTO `sys_area` VALUES ('90a2496553da11e68586000c293c07d6', '450400', '梧州市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90a4602153da11e68586000c293c07d6', '450401', '市辖区', null, null, '450400');
INSERT INTO `sys_area` VALUES ('90a67e3053da11e68586000c293c07d6', '450403', '万秀区', null, null, '450401');
INSERT INTO `sys_area` VALUES ('90a84efd53da11e68586000c293c07d6', '450404', '蝶山区', null, null, '450401');
INSERT INTO `sys_area` VALUES ('90aa020a53da11e68586000c293c07d6', '450405', '长洲区', null, null, '450401');
INSERT INTO `sys_area` VALUES ('90ac38a553da11e68586000c293c07d6', '450421', '苍梧县', null, null, '450400');
INSERT INTO `sys_area` VALUES ('90ae187e53da11e68586000c293c07d6', '450422', '藤县', null, null, '450400');
INSERT INTO `sys_area` VALUES ('90b018be53da11e68586000c293c07d6', '450423', '蒙山县', null, null, '450400');
INSERT INTO `sys_area` VALUES ('90b233e953da11e68586000c293c07d6', '450481', '岑溪市', null, null, '450400');
INSERT INTO `sys_area` VALUES ('90b3dca053da11e68586000c293c07d6', '450500', '北海市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90b5a98053da11e68586000c293c07d6', '450501', '市辖区', null, null, '450500');
INSERT INTO `sys_area` VALUES ('90b7946853da11e68586000c293c07d6', '450502', '海城区', null, null, '450501');
INSERT INTO `sys_area` VALUES ('90b93a3453da11e68586000c293c07d6', '450503', '银海区', null, null, '450501');
INSERT INTO `sys_area` VALUES ('90be386153da11e68586000c293c07d6', '450512', '铁山港区', null, null, '450501');
INSERT INTO `sys_area` VALUES ('90bff93653da11e68586000c293c07d6', '450521', '合浦县', null, null, '450500');
INSERT INTO `sys_area` VALUES ('90c1fbff53da11e68586000c293c07d6', '450600', '防城港市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90c3d01153da11e68586000c293c07d6', '450601', '市辖区', null, null, '450600');
INSERT INTO `sys_area` VALUES ('90c5895553da11e68586000c293c07d6', '450602', '港口区', null, null, '450601');
INSERT INTO `sys_area` VALUES ('90c7aee553da11e68586000c293c07d6', '450603', '防城区', null, null, '450601');
INSERT INTO `sys_area` VALUES ('90c991f053da11e68586000c293c07d6', '450621', '上思县', null, null, '450600');
INSERT INTO `sys_area` VALUES ('90cbea5653da11e68586000c293c07d6', '450681', '东兴市', null, null, '450600');
INSERT INTO `sys_area` VALUES ('90cdb4d353da11e68586000c293c07d6', '450700', '钦州市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90cfadc853da11e68586000c293c07d6', '450701', '市辖区', null, null, '450700');
INSERT INTO `sys_area` VALUES ('90d1b2a253da11e68586000c293c07d6', '450702', '钦南区', null, null, '450700');
INSERT INTO `sys_area` VALUES ('90d3a0f953da11e68586000c293c07d6', '450703', '钦北区', null, null, '450700');
INSERT INTO `sys_area` VALUES ('90d5695a53da11e68586000c293c07d6', '450721', '灵山县', null, null, '450700');
INSERT INTO `sys_area` VALUES ('90d7382d53da11e68586000c293c07d6', '450722', '浦北县', null, null, '450700');
INSERT INTO `sys_area` VALUES ('90d900b553da11e68586000c293c07d6', '450800', '贵港市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90dab45953da11e68586000c293c07d6', '450801', '市辖区', null, null, '450800');
INSERT INTO `sys_area` VALUES ('90e15fc053da11e68586000c293c07d6', '450802', '港北区', null, null, '450801');
INSERT INTO `sys_area` VALUES ('90e33ddd53da11e68586000c293c07d6', '450803', '港南区', null, null, '450801');
INSERT INTO `sys_area` VALUES ('90e53a5253da11e68586000c293c07d6', '450804', '覃塘区', null, null, '450801');
INSERT INTO `sys_area` VALUES ('90e6f8e653da11e68586000c293c07d6', '450821', '平南县', null, null, '450800');
INSERT INTO `sys_area` VALUES ('90e8dc4f53da11e68586000c293c07d6', '450881', '桂平市', null, null, '450800');
INSERT INTO `sys_area` VALUES ('90eab33953da11e68586000c293c07d6', '450900', '玉林市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90ec770e53da11e68586000c293c07d6', '450901', '市辖区', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90ee3cb653da11e68586000c293c07d6', '450902', '玉州区', null, null, '450901');
INSERT INTO `sys_area` VALUES ('90f005ea53da11e68586000c293c07d6', '450921', '容县', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90f238b953da11e68586000c293c07d6', '450922', '陆川县', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90f3fa1453da11e68586000c293c07d6', '450923', '博白县', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90f5b7d253da11e68586000c293c07d6', '450924', '兴业县', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90f7d3af53da11e68586000c293c07d6', '450981', '北流市', null, null, '450900');
INSERT INTO `sys_area` VALUES ('90f9ad9053da11e68586000c293c07d6', '451000', '百色市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('90fb7b5b53da11e68586000c293c07d6', '451001', '市辖区', null, null, '451000');
INSERT INTO `sys_area` VALUES ('90fd482953da11e68586000c293c07d6', '451002', '右江区', null, null, '451001');
INSERT INTO `sys_area` VALUES ('90ff12df53da11e68586000c293c07d6', '451021', '田阳县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('91017c4453da11e68586000c293c07d6', '451022', '田东县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('9103468653da11e68586000c293c07d6', '451023', '平果县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('9109411953da11e68586000c293c07d6', '451024', '德保县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('910af26b53da11e68586000c293c07d6', '451025', '靖西县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('910cbc0553da11e68586000c293c07d6', '451026', '那坡县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('910ea93353da11e68586000c293c07d6', '451027', '凌云县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('9110852153da11e68586000c293c07d6', '451028', '乐业县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('911287d553da11e68586000c293c07d6', '451029', '田林县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('91143dbc53da11e68586000c293c07d6', '451030', '西林县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('9116394053da11e68586000c293c07d6', '451031', '隆林各族自治县', null, null, '451000');
INSERT INTO `sys_area` VALUES ('9118357653da11e68586000c293c07d6', '451100', '贺州市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('911a088e53da11e68586000c293c07d6', '451101', '市辖区', null, null, '451100');
INSERT INTO `sys_area` VALUES ('911bd52553da11e68586000c293c07d6', '451102', '八步区', null, null, '451101');
INSERT INTO `sys_area` VALUES ('911ddb9553da11e68586000c293c07d6', '451121', '昭平县', null, null, '451100');
INSERT INTO `sys_area` VALUES ('911fbf0453da11e68586000c293c07d6', '451122', '钟山县', null, null, '451100');
INSERT INTO `sys_area` VALUES ('91219d0053da11e68586000c293c07d6', '451123', '富川瑶族自治县', null, null, '451100');
INSERT INTO `sys_area` VALUES ('91236eaa53da11e68586000c293c07d6', '451200', '河池市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('91255bd553da11e68586000c293c07d6', '451201', '市辖区', null, null, '451200');
INSERT INTO `sys_area` VALUES ('9127490453da11e68586000c293c07d6', '451202', '金城江区', null, null, '451201');
INSERT INTO `sys_area` VALUES ('9129041c53da11e68586000c293c07d6', '451221', '南丹县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('912ae98c53da11e68586000c293c07d6', '451222', '天峨县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('912cc71e53da11e68586000c293c07d6', '451223', '凤山县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('912e8c2d53da11e68586000c293c07d6', '451224', '东兰县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('91308ea953da11e68586000c293c07d6', '451225', '罗城仫佬族自治县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('91325b1153da11e68586000c293c07d6', '451226', '环江毛南族自治县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('913443bc53da11e68586000c293c07d6', '451227', '巴马瑶族自治县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('9135e97653da11e68586000c293c07d6', '451228', '都安瑶族自治县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('9137b33b53da11e68586000c293c07d6', '451229', '大化瑶族自治县', null, null, '451200');
INSERT INTO `sys_area` VALUES ('913992e953da11e68586000c293c07d6', '451281', '宜州市', null, null, '451200');
INSERT INTO `sys_area` VALUES ('913b409653da11e68586000c293c07d6', '451300', '来宾市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('913d0cc953da11e68586000c293c07d6', '451301', '市辖区', null, null, '451300');
INSERT INTO `sys_area` VALUES ('913f190653da11e68586000c293c07d6', '451302', '兴宾区', null, null, '451301');
INSERT INTO `sys_area` VALUES ('9140e9a053da11e68586000c293c07d6', '451321', '忻城县', null, null, '451300');
INSERT INTO `sys_area` VALUES ('9142b9f553da11e68586000c293c07d6', '451322', '象州县', null, null, '451300');
INSERT INTO `sys_area` VALUES ('914492aa53da11e68586000c293c07d6', '451323', '武宣县', null, null, '451300');
INSERT INTO `sys_area` VALUES ('9146784953da11e68586000c293c07d6', '451324', '金秀瑶族自治县', null, null, '451300');
INSERT INTO `sys_area` VALUES ('9148527853da11e68586000c293c07d6', '451381', '合山市', null, null, '451300');
INSERT INTO `sys_area` VALUES ('914a19cd53da11e68586000c293c07d6', '451400', '崇左市', null, null, '450000');
INSERT INTO `sys_area` VALUES ('914be99e53da11e68586000c293c07d6', '451401', '市辖区', null, null, '451400');
INSERT INTO `sys_area` VALUES ('914f76a953da11e68586000c293c07d6', '451402', '江洲区', null, null, '451401');
INSERT INTO `sys_area` VALUES ('91516d8653da11e68586000c293c07d6', '451421', '扶绥县', null, null, '451400');
INSERT INTO `sys_area` VALUES ('9153a4d253da11e68586000c293c07d6', '451422', '宁明县', null, null, '451400');
INSERT INTO `sys_area` VALUES ('915565ab53da11e68586000c293c07d6', '451423', '龙州县', null, null, '451400');
INSERT INTO `sys_area` VALUES ('91575d8853da11e68586000c293c07d6', '451424', '大新县', null, null, '451400');
INSERT INTO `sys_area` VALUES ('9159244b53da11e68586000c293c07d6', '451425', '天等县', null, null, '451400');
INSERT INTO `sys_area` VALUES ('915b283753da11e68586000c293c07d6', '451481', '凭祥市', null, null, '451400');
INSERT INTO `sys_area` VALUES ('915d164e53da11e68586000c293c07d6', '460000', '海南省', null, '21', '0');
INSERT INTO `sys_area` VALUES ('915ed5ab53da11e68586000c293c07d6', '460100', '海口市', null, null, '460000');
INSERT INTO `sys_area` VALUES ('9160b25753da11e68586000c293c07d6', '460101', '市辖区', null, null, '460100');
INSERT INTO `sys_area` VALUES ('91628c7553da11e68586000c293c07d6', '460105', '秀英区', null, null, '460101');
INSERT INTO `sys_area` VALUES ('91645cb853da11e68586000c293c07d6', '460106', '龙华区', null, null, '460101');
INSERT INTO `sys_area` VALUES ('91660ee753da11e68586000c293c07d6', '460107', '琼山区', null, null, '460101');
INSERT INTO `sys_area` VALUES ('9168324553da11e68586000c293c07d6', '460108', '美兰区', null, null, '460101');
INSERT INTO `sys_area` VALUES ('916a03e453da11e68586000c293c07d6', '460200', '三亚市', null, null, '460000');
INSERT INTO `sys_area` VALUES ('916bd34b53da11e68586000c293c07d6', '460201', '市辖区', null, null, '460200');
INSERT INTO `sys_area` VALUES ('916d9d5f53da11e68586000c293c07d6', '469000', '省直辖县级行政单位', null, null, '460000');
INSERT INTO `sys_area` VALUES ('916f73d253da11e68586000c293c07d6', '469001', '五指山市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9174e41853da11e68586000c293c07d6', '469002', '琼海市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9176d7eb53da11e68586000c293c07d6', '469003', '儋州市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9178adc353da11e68586000c293c07d6', '469005', '文昌市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('917a627b53da11e68586000c293c07d6', '469006', '万宁市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('917c47e653da11e68586000c293c07d6', '469007', '东方市', null, null, '469000');
INSERT INTO `sys_area` VALUES ('917e1a6353da11e68586000c293c07d6', '469025', '定安县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('917fd60453da11e68586000c293c07d6', '469026', '屯昌县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9181c6da53da11e68586000c293c07d6', '469027', '澄迈县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('918395dc53da11e68586000c293c07d6', '469028', '临高县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('91857e1053da11e68586000c293c07d6', '469030', '白沙黎族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9187524953da11e68586000c293c07d6', '469031', '昌江黎族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('918928de53da11e68586000c293c07d6', '469033', '乐东黎族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('918aedda53da11e68586000c293c07d6', '469034', '陵水黎族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('918ce9a753da11e68586000c293c07d6', '469035', '保亭黎族苗族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('918ed43653da11e68586000c293c07d6', '469036', '琼中黎族苗族自治县', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9190e29d53da11e68586000c293c07d6', '469037', '西沙群岛', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9192bb3853da11e68586000c293c07d6', '469038', '南沙群岛', null, null, '469000');
INSERT INTO `sys_area` VALUES ('919485cc53da11e68586000c293c07d6', '469039', '中沙群岛的岛礁及其海域', null, null, '469000');
INSERT INTO `sys_area` VALUES ('9196376d53da11e68586000c293c07d6', '500000', '重庆市', null, '22', '0');
INSERT INTO `sys_area` VALUES ('919801c353da11e68586000c293c07d6', '500100', '万州区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('919a234b53da11e68586000c293c07d6', '500101', '万州区', null, null, '500100');
INSERT INTO `sys_area` VALUES ('919bd3eb53da11e68586000c293c07d6', '500200', '涪陵区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('919d9f5753da11e68586000c293c07d6', '500201', '涪陵区', null, null, '500200');
INSERT INTO `sys_area` VALUES ('919f953753da11e68586000c293c07d6', '500300', '渝中区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91a18b4c53da11e68586000c293c07d6', '500301', '渝中区', null, null, '500300');
INSERT INTO `sys_area` VALUES ('91a361d253da11e68586000c293c07d6', '500400', '大渡口区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91a5217453da11e68586000c293c07d6', '500401', '大渡口区', null, null, '500400');
INSERT INTO `sys_area` VALUES ('91a6f47553da11e68586000c293c07d6', '500500', '江北区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91a8d2de53da11e68586000c293c07d6', '500501', '江北区', null, null, '500500');
INSERT INTO `sys_area` VALUES ('91aa888a53da11e68586000c293c07d6', '500600', '沙坪坝区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91ac589d53da11e68586000c293c07d6', '500601', '沙坪坝区', null, null, '500600');
INSERT INTO `sys_area` VALUES ('91ae574853da11e68586000c293c07d6', '500700', '九龙坡区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91b0436553da11e68586000c293c07d6', '500701', '九龙坡区', null, null, '500700');
INSERT INTO `sys_area` VALUES ('91b2570a53da11e68586000c293c07d6', '500800', '南岸区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91b4368a53da11e68586000c293c07d6', '500801', '南岸区', null, null, '500800');
INSERT INTO `sys_area` VALUES ('91b6419953da11e68586000c293c07d6', '500900', '北碚区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91b8a75c53da11e68586000c293c07d6', '500901', '北碚区', null, null, '500900');
INSERT INTO `sys_area` VALUES ('91ba648453da11e68586000c293c07d6', '501000', '万盛区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91bc1f9353da11e68586000c293c07d6', '501001', '万盛区', null, null, '501000');
INSERT INTO `sys_area` VALUES ('91bdeac153da11e68586000c293c07d6', '501100', '双桥区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91c032a953da11e68586000c293c07d6', '501101', '双桥区', null, null, '501100');
INSERT INTO `sys_area` VALUES ('91c3328b53da11e68586000c293c07d6', '501200', '渝北区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91c5171653da11e68586000c293c07d6', '501201', '渝北区', null, null, '501200');
INSERT INTO `sys_area` VALUES ('91c6d43653da11e68586000c293c07d6', '501300', '巴南区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91c8b3b353da11e68586000c293c07d6', '501301', '巴南区', null, null, '501300');
INSERT INTO `sys_area` VALUES ('91ca935e53da11e68586000c293c07d6', '501400', '黔江区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91cc55aa53da11e68586000c293c07d6', '501401', '黔江区', null, null, '501400');
INSERT INTO `sys_area` VALUES ('91ce0f1353da11e68586000c293c07d6', '501500', '长寿区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91cfd36453da11e68586000c293c07d6', '501501', '长寿区', null, null, '501500');
INSERT INTO `sys_area` VALUES ('91d204fe53da11e68586000c293c07d6', '502200', '綦江区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91d3b5bb53da11e68586000c293c07d6', '502201', '綦江区', null, null, '502200');
INSERT INTO `sys_area` VALUES ('91d5901753da11e68586000c293c07d6', '502300', '潼南县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91d7555d53da11e68586000c293c07d6', '502301', '潼南县', null, null, '502300');
INSERT INTO `sys_area` VALUES ('91d9393a53da11e68586000c293c07d6', '502400', '铜梁县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91daf25953da11e68586000c293c07d6', '502401', '铜梁县', null, null, '502400');
INSERT INTO `sys_area` VALUES ('91dcdb5753da11e68586000c293c07d6', '502500', '大足区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91ded58e53da11e68586000c293c07d6', '502501', '大足区', null, null, '502500');
INSERT INTO `sys_area` VALUES ('91e0900e53da11e68586000c293c07d6', '502600', '荣昌县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91e2366d53da11e68586000c293c07d6', '502601', '荣昌县', null, null, '502600');
INSERT INTO `sys_area` VALUES ('91e4056453da11e68586000c293c07d6', '502700', '璧山县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91e5cb8453da11e68586000c293c07d6', '502701', '璧山县', null, null, '502700');
INSERT INTO `sys_area` VALUES ('91e7a4ae53da11e68586000c293c07d6', '502800', '梁平县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91e971e753da11e68586000c293c07d6', '502801', '梁平县', null, null, '502800');
INSERT INTO `sys_area` VALUES ('91eb60d253da11e68586000c293c07d6', '502900', '城口县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91ed114753da11e68586000c293c07d6', '502901', '城口县', null, null, '502900');
INSERT INTO `sys_area` VALUES ('91ef283953da11e68586000c293c07d6', '503000', '丰都县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91f0fad653da11e68586000c293c07d6', '503001', '丰都县', null, null, '503000');
INSERT INTO `sys_area` VALUES ('91f2fcca53da11e68586000c293c07d6', '503100', '垫江县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91f4decb53da11e68586000c293c07d6', '503101', '垫江县', null, null, '503100');
INSERT INTO `sys_area` VALUES ('91f6a97853da11e68586000c293c07d6', '503200', '武隆县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91f87dd053da11e68586000c293c07d6', '503201', '武隆县', null, null, '503200');
INSERT INTO `sys_area` VALUES ('91fa56c553da11e68586000c293c07d6', '503300', '忠县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('91fc499853da11e68586000c293c07d6', '503301', '忠县', null, null, '503300');
INSERT INTO `sys_area` VALUES ('91fe1fe253da11e68586000c293c07d6', '503400', '开县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('920014fe53da11e68586000c293c07d6', '503401', '开县', null, null, '503400');
INSERT INTO `sys_area` VALUES ('920207a553da11e68586000c293c07d6', '503500', '云阳县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9203ed8653da11e68586000c293c07d6', '503501', '云阳县', null, null, '503500');
INSERT INTO `sys_area` VALUES ('9205dab953da11e68586000c293c07d6', '503600', '奉节县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9207a00053da11e68586000c293c07d6', '503601', '奉节县', null, null, '503600');
INSERT INTO `sys_area` VALUES ('9209b12f53da11e68586000c293c07d6', '503700', '巫山县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('920b651f53da11e68586000c293c07d6', '503701', '巫山县', null, null, '503700');
INSERT INTO `sys_area` VALUES ('920d721253da11e68586000c293c07d6', '503800', '巫溪县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('920f6b2d53da11e68586000c293c07d6', '503801', '巫溪县', null, null, '503800');
INSERT INTO `sys_area` VALUES ('9211766653da11e68586000c293c07d6', '504000', '石柱县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9213235c53da11e68586000c293c07d6', '504001', '石柱县', null, null, '504000');
INSERT INTO `sys_area` VALUES ('92150db853da11e68586000c293c07d6', '504100', '秀山县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9216f3b853da11e68586000c293c07d6', '504101', '秀山县', null, null, '504100');
INSERT INTO `sys_area` VALUES ('9218a61653da11e68586000c293c07d6', '504200', '酉阳县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('921a5c7f53da11e68586000c293c07d6', '504201', '酉阳县', null, null, '504200');
INSERT INTO `sys_area` VALUES ('921c407953da11e68586000c293c07d6', '504300', '彭水县', null, null, '500000');
INSERT INTO `sys_area` VALUES ('921e7bf653da11e68586000c293c07d6', '504301', '彭水县', null, null, '504300');
INSERT INTO `sys_area` VALUES ('92207fb453da11e68586000c293c07d6', '508100', '江津区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('92224d0053da11e68586000c293c07d6', '508101', '江津区', null, null, '508100');
INSERT INTO `sys_area` VALUES ('922460e253da11e68586000c293c07d6', '508200', '合川区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9226354253da11e68586000c293c07d6', '508201', '合川区', null, null, '508200');
INSERT INTO `sys_area` VALUES ('9228296c53da11e68586000c293c07d6', '508300', '永川区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('9229f5e553da11e68586000c293c07d6', '508301', '永川区', null, null, '508300');
INSERT INTO `sys_area` VALUES ('922bca6753da11e68586000c293c07d6', '508400', '南川区', null, null, '500000');
INSERT INTO `sys_area` VALUES ('922da86853da11e68586000c293c07d6', '508401', '南川区', null, null, '508400');
INSERT INTO `sys_area` VALUES ('9230d60f53da11e68586000c293c07d6', '510000', '四川省', null, '23', '0');
INSERT INTO `sys_area` VALUES ('9232cde653da11e68586000c293c07d6', '510100', '成都市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92347f7253da11e68586000c293c07d6', '510101', '市辖区', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9236227853da11e68586000c293c07d6', '510104', '锦江区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('923829b553da11e68586000c293c07d6', '510105', '青羊区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('923a0bbe53da11e68586000c293c07d6', '510106', '金牛区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('923bbb2b53da11e68586000c293c07d6', '510107', '武侯区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('923d86cf53da11e68586000c293c07d6', '510108', '成华区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('923f985f53da11e68586000c293c07d6', '510112', '龙泉驿区', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9241b1d353da11e68586000c293c07d6', '510113', '青白江区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('9244159d53da11e68586000c293c07d6', '510114', '新都区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('9245f33e53da11e68586000c293c07d6', '510115', '温江区', null, null, '510101');
INSERT INTO `sys_area` VALUES ('9247a34a53da11e68586000c293c07d6', '510121', '金堂县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('92496cc553da11e68586000c293c07d6', '510122', '双流县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('924bdb8353da11e68586000c293c07d6', '510124', '郫县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('924e752b53da11e68586000c293c07d6', '510129', '大邑县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9250824b53da11e68586000c293c07d6', '510131', '蒲江县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9252c2ab53da11e68586000c293c07d6', '510132', '新津县', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9254c8df53da11e68586000c293c07d6', '510181', '都江堰市', null, null, '510100');
INSERT INTO `sys_area` VALUES ('925697a253da11e68586000c293c07d6', '510182', '彭州市', null, null, '510100');
INSERT INTO `sys_area` VALUES ('9258464853da11e68586000c293c07d6', '510183', '邛崃市', null, null, '510100');
INSERT INTO `sys_area` VALUES ('925a642b53da11e68586000c293c07d6', '510184', '崇州市', null, null, '510100');
INSERT INTO `sys_area` VALUES ('925c4da153da11e68586000c293c07d6', '510300', '自贡市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('925eace953da11e68586000c293c07d6', '510301', '市辖区', null, null, '510300');
INSERT INTO `sys_area` VALUES ('9260e19053da11e68586000c293c07d6', '510302', '自流井区', null, null, '510301');
INSERT INTO `sys_area` VALUES ('926352be53da11e68586000c293c07d6', '510303', '贡井区', null, null, '510301');
INSERT INTO `sys_area` VALUES ('92655c0a53da11e68586000c293c07d6', '510304', '大安区', null, null, '510301');
INSERT INTO `sys_area` VALUES ('926721a753da11e68586000c293c07d6', '510311', '沿滩区', null, null, '510301');
INSERT INTO `sys_area` VALUES ('92697f3453da11e68586000c293c07d6', '510321', '荣县', null, null, '510300');
INSERT INTO `sys_area` VALUES ('926b57d353da11e68586000c293c07d6', '510322', '富顺县', null, null, '510300');
INSERT INTO `sys_area` VALUES ('926d195853da11e68586000c293c07d6', '510400', '攀枝花市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('926f2db353da11e68586000c293c07d6', '510401', '市辖区', null, null, '510400');
INSERT INTO `sys_area` VALUES ('9270cfc853da11e68586000c293c07d6', '510402', '东区', null, null, '510401');
INSERT INTO `sys_area` VALUES ('9272f93553da11e68586000c293c07d6', '510403', '西区', null, null, '510401');
INSERT INTO `sys_area` VALUES ('9274ca3553da11e68586000c293c07d6', '510411', '仁和区', null, null, '510401');
INSERT INTO `sys_area` VALUES ('92769b8253da11e68586000c293c07d6', '510421', '米易县', null, null, '510400');
INSERT INTO `sys_area` VALUES ('92787b5253da11e68586000c293c07d6', '510422', '盐边县', null, null, '510400');
INSERT INTO `sys_area` VALUES ('927a546953da11e68586000c293c07d6', '510500', '泸州市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('927c5e5253da11e68586000c293c07d6', '510501', '市辖区', null, null, '510500');
INSERT INTO `sys_area` VALUES ('927f474153da11e68586000c293c07d6', '510502', '江阳区', null, null, '510501');
INSERT INTO `sys_area` VALUES ('9281abf153da11e68586000c293c07d6', '510503', '纳溪区', null, null, '510501');
INSERT INTO `sys_area` VALUES ('92836f3a53da11e68586000c293c07d6', '510504', '龙马潭区', null, null, '510501');
INSERT INTO `sys_area` VALUES ('9285263953da11e68586000c293c07d6', '510521', '泸县', null, null, '510500');
INSERT INTO `sys_area` VALUES ('928706f553da11e68586000c293c07d6', '510522', '合江县', null, null, '510500');
INSERT INTO `sys_area` VALUES ('9288e3e353da11e68586000c293c07d6', '510524', '叙永县', null, null, '510500');
INSERT INTO `sys_area` VALUES ('928a8f6d53da11e68586000c293c07d6', '510525', '古蔺县', null, null, '510500');
INSERT INTO `sys_area` VALUES ('928c70c953da11e68586000c293c07d6', '510600', '德阳市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('928e680553da11e68586000c293c07d6', '510601', '市辖区', null, null, '510600');
INSERT INTO `sys_area` VALUES ('9290f76c53da11e68586000c293c07d6', '510603', '旌阳区', null, null, '510601');
INSERT INTO `sys_area` VALUES ('9292eb4653da11e68586000c293c07d6', '510623', '中江县', null, null, '510600');
INSERT INTO `sys_area` VALUES ('9294dce353da11e68586000c293c07d6', '510626', '罗江县', null, null, '510600');
INSERT INTO `sys_area` VALUES ('929735a053da11e68586000c293c07d6', '510681', '广汉市', null, null, '510600');
INSERT INTO `sys_area` VALUES ('9299162353da11e68586000c293c07d6', '510682', '什邡市', null, null, '510600');
INSERT INTO `sys_area` VALUES ('929af12c53da11e68586000c293c07d6', '510683', '绵竹市', null, null, '510600');
INSERT INTO `sys_area` VALUES ('929e06c053da11e68586000c293c07d6', '510700', '绵阳市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92a018bb53da11e68586000c293c07d6', '510701', '市辖区', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92a2065b53da11e68586000c293c07d6', '510703', '涪城区', null, null, '510701');
INSERT INTO `sys_area` VALUES ('92a3b28553da11e68586000c293c07d6', '510704', '游仙区', null, null, '510701');
INSERT INTO `sys_area` VALUES ('92a58a2b53da11e68586000c293c07d6', '510722', '三台县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92a757f053da11e68586000c293c07d6', '510723', '盐亭县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92a9283453da11e68586000c293c07d6', '510724', '安县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92ab06e053da11e68586000c293c07d6', '510725', '梓潼县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92acf4e153da11e68586000c293c07d6', '510726', '北川羌族自治县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92aeee4053da11e68586000c293c07d6', '510727', '平武县', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92b0ad3753da11e68586000c293c07d6', '510781', '江油市', null, null, '510700');
INSERT INTO `sys_area` VALUES ('92b2998053da11e68586000c293c07d6', '510800', '广元市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92b470eb53da11e68586000c293c07d6', '510801', '市辖区', null, null, '510800');
INSERT INTO `sys_area` VALUES ('92b6508d53da11e68586000c293c07d6', '510802', '市中区', null, null, '510801');
INSERT INTO `sys_area` VALUES ('92b7f18b53da11e68586000c293c07d6', '510811', '元坝区', null, null, '510801');
INSERT INTO `sys_area` VALUES ('92b9e5a953da11e68586000c293c07d6', '510812', '朝天区', null, null, '510801');
INSERT INTO `sys_area` VALUES ('92bba77a53da11e68586000c293c07d6', '510821', '旺苍县', null, null, '510800');
INSERT INTO `sys_area` VALUES ('92bd509d53da11e68586000c293c07d6', '510822', '青川县', null, null, '510800');
INSERT INTO `sys_area` VALUES ('92bf1fce53da11e68586000c293c07d6', '510823', '剑阁县', null, null, '510800');
INSERT INTO `sys_area` VALUES ('92c1787453da11e68586000c293c07d6', '510824', '苍溪县', null, null, '510800');
INSERT INTO `sys_area` VALUES ('92c3603e53da11e68586000c293c07d6', '510900', '遂宁市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92c5436e53da11e68586000c293c07d6', '510901', '市辖区', null, null, '510900');
INSERT INTO `sys_area` VALUES ('92c708c853da11e68586000c293c07d6', '510903', '船山区', null, null, '510901');
INSERT INTO `sys_area` VALUES ('92c8d4be53da11e68586000c293c07d6', '510904', '安居区', null, null, '510901');
INSERT INTO `sys_area` VALUES ('92cab14453da11e68586000c293c07d6', '510921', '蓬溪县', null, null, '510900');
INSERT INTO `sys_area` VALUES ('92cc582f53da11e68586000c293c07d6', '510922', '射洪县', null, null, '510900');
INSERT INTO `sys_area` VALUES ('92ce227853da11e68586000c293c07d6', '510923', '大英县', null, null, '510900');
INSERT INTO `sys_area` VALUES ('92d0066b53da11e68586000c293c07d6', '511000', '内江市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92d1ce9e53da11e68586000c293c07d6', '511001', '市辖区', null, null, '511000');
INSERT INTO `sys_area` VALUES ('92d3cc9153da11e68586000c293c07d6', '511002', '市中区', null, null, '511001');
INSERT INTO `sys_area` VALUES ('92d79e7e53da11e68586000c293c07d6', '511011', '东兴区', null, null, '511001');
INSERT INTO `sys_area` VALUES ('92d9585553da11e68586000c293c07d6', '511024', '威远县', null, null, '511000');
INSERT INTO `sys_area` VALUES ('92db336f53da11e68586000c293c07d6', '511025', '资中县', null, null, '511000');
INSERT INTO `sys_area` VALUES ('92dd23cc53da11e68586000c293c07d6', '511028', '隆昌县', null, null, '511000');
INSERT INTO `sys_area` VALUES ('92deda5953da11e68586000c293c07d6', '511100', '乐山市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92e0b3f153da11e68586000c293c07d6', '511101', '市辖区', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92e28fc353da11e68586000c293c07d6', '511102', '市中区', null, null, '511101');
INSERT INTO `sys_area` VALUES ('92e4920553da11e68586000c293c07d6', '511111', '沙湾区', null, null, '511101');
INSERT INTO `sys_area` VALUES ('92e6439d53da11e68586000c293c07d6', '511112', '五通桥区', null, null, '511101');
INSERT INTO `sys_area` VALUES ('92e80bb053da11e68586000c293c07d6', '511113', '金口河区', null, null, '511101');
INSERT INTO `sys_area` VALUES ('92ea452753da11e68586000c293c07d6', '511123', '犍为县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92ec1dc453da11e68586000c293c07d6', '511124', '井研县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92ede94c53da11e68586000c293c07d6', '511126', '夹江县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92effbf853da11e68586000c293c07d6', '511129', '沐川县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92f1be3053da11e68586000c293c07d6', '511132', '峨边彝族自治县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92f49b6353da11e68586000c293c07d6', '511133', '马边彝族自治县', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92f66d6053da11e68586000c293c07d6', '511181', '峨眉山市', null, null, '511100');
INSERT INTO `sys_area` VALUES ('92f88a5653da11e68586000c293c07d6', '511300', '南充市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('92fa538a53da11e68586000c293c07d6', '511301', '市辖区', null, null, '511300');
INSERT INTO `sys_area` VALUES ('92fc70a053da11e68586000c293c07d6', '511302', '顺庆区', null, null, '511301');
INSERT INTO `sys_area` VALUES ('92fe4d7553da11e68586000c293c07d6', '511303', '高坪区', null, null, '511301');
INSERT INTO `sys_area` VALUES ('93001eb053da11e68586000c293c07d6', '511304', '嘉陵区', null, null, '511301');
INSERT INTO `sys_area` VALUES ('9301c89753da11e68586000c293c07d6', '511321', '南部县', null, null, '511300');
INSERT INTO `sys_area` VALUES ('9303adc653da11e68586000c293c07d6', '511322', '营山县', null, null, '511300');
INSERT INTO `sys_area` VALUES ('9305894253da11e68586000c293c07d6', '511323', '蓬安县', null, null, '511300');
INSERT INTO `sys_area` VALUES ('93074d7e53da11e68586000c293c07d6', '511324', '仪陇县', null, null, '511300');
INSERT INTO `sys_area` VALUES ('93092ee953da11e68586000c293c07d6', '511325', '西充县', null, null, '511300');
INSERT INTO `sys_area` VALUES ('930af46553da11e68586000c293c07d6', '511381', '阆中市', null, null, '511300');
INSERT INTO `sys_area` VALUES ('930d717153da11e68586000c293c07d6', '511400', '眉山市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('930f4a4c53da11e68586000c293c07d6', '511401', '市辖区', null, null, '511400');
INSERT INTO `sys_area` VALUES ('931130fb53da11e68586000c293c07d6', '511402', '东坡区', null, null, '511401');
INSERT INTO `sys_area` VALUES ('931303b953da11e68586000c293c07d6', '511421', '仁寿县', null, null, '511400');
INSERT INTO `sys_area` VALUES ('9314d4f253da11e68586000c293c07d6', '511422', '彭山县', null, null, '511400');
INSERT INTO `sys_area` VALUES ('9316875753da11e68586000c293c07d6', '511423', '洪雅县', null, null, '511400');
INSERT INTO `sys_area` VALUES ('9318743253da11e68586000c293c07d6', '511424', '丹棱县', null, null, '511400');
INSERT INTO `sys_area` VALUES ('931a787e53da11e68586000c293c07d6', '511425', '青神县', null, null, '511400');
INSERT INTO `sys_area` VALUES ('931c95c553da11e68586000c293c07d6', '511500', '宜宾市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('931e59c153da11e68586000c293c07d6', '511501', '市辖区', null, null, '511500');
INSERT INTO `sys_area` VALUES ('932009b353da11e68586000c293c07d6', '511502', '翠屏区', null, null, '511500');
INSERT INTO `sys_area` VALUES ('9321dd2c53da11e68586000c293c07d6', '511521', '宜宾县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('9323bdd853da11e68586000c293c07d6', '511522', '南溪县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('9325678a53da11e68586000c293c07d6', '511523', '江安县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('93273cec53da11e68586000c293c07d6', '511524', '长宁县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('932931a753da11e68586000c293c07d6', '511525', '高县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('932b1d4653da11e68586000c293c07d6', '511526', '珙县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('932ccb4853da11e68586000c293c07d6', '511527', '筠连县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('932ebd0153da11e68586000c293c07d6', '511528', '兴文县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('9330d20853da11e68586000c293c07d6', '511529', '屏山县', null, null, '511500');
INSERT INTO `sys_area` VALUES ('9332a75253da11e68586000c293c07d6', '511600', '广安市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('9334b1d553da11e68586000c293c07d6', '511601', '市辖区', null, null, '511600');
INSERT INTO `sys_area` VALUES ('9336f51e53da11e68586000c293c07d6', '511602', '广安区', null, null, '511601');
INSERT INTO `sys_area` VALUES ('9338d35b53da11e68586000c293c07d6', '511621', '岳池县', null, null, '511600');
INSERT INTO `sys_area` VALUES ('933ab8a853da11e68586000c293c07d6', '511622', '武胜县', null, null, '511600');
INSERT INTO `sys_area` VALUES ('933ca66b53da11e68586000c293c07d6', '511623', '邻水县', null, null, '511600');
INSERT INTO `sys_area` VALUES ('933e862453da11e68586000c293c07d6', '511681', '华蓥市', null, null, '511600');
INSERT INTO `sys_area` VALUES ('9340719e53da11e68586000c293c07d6', '511682', '广安区', null, null, '511600');
INSERT INTO `sys_area` VALUES ('93424c8f53da11e68586000c293c07d6', '511700', '达州市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('9344032353da11e68586000c293c07d6', '511701', '市辖区', null, null, '511700');
INSERT INTO `sys_area` VALUES ('9345ec8053da11e68586000c293c07d6', '511702', '通川区', null, null, '511701');
INSERT INTO `sys_area` VALUES ('9347a49753da11e68586000c293c07d6', '511721', '达川区', null, null, '511700');
INSERT INTO `sys_area` VALUES ('934976bf53da11e68586000c293c07d6', '511722', '宣汉县', null, null, '511700');
INSERT INTO `sys_area` VALUES ('934b815d53da11e68586000c293c07d6', '511723', '开江县', null, null, '511700');
INSERT INTO `sys_area` VALUES ('934d655253da11e68586000c293c07d6', '511724', '大竹县', null, null, '511700');
INSERT INTO `sys_area` VALUES ('934f511f53da11e68586000c293c07d6', '511725', '渠县', null, null, '511700');
INSERT INTO `sys_area` VALUES ('9351aa7153da11e68586000c293c07d6', '511781', '万源市', null, null, '511700');
INSERT INTO `sys_area` VALUES ('9353751a53da11e68586000c293c07d6', '511800', '雅安市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('935551ab53da11e68586000c293c07d6', '511801', '雨城区', null, null, '511800');
INSERT INTO `sys_area` VALUES ('9357261c53da11e68586000c293c07d6', '511802', '雨城区', null, null, '511801');
INSERT INTO `sys_area` VALUES ('9358ea0653da11e68586000c293c07d6', '511821', '名山区', null, null, '511800');
INSERT INTO `sys_area` VALUES ('935ac09e53da11e68586000c293c07d6', '511822', '荥经县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('935c9ba053da11e68586000c293c07d6', '511823', '汉源县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('935facf153da11e68586000c293c07d6', '511824', '石棉县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('9361843d53da11e68586000c293c07d6', '511825', '天全县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('936370df53da11e68586000c293c07d6', '511826', '芦山县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('93658fde53da11e68586000c293c07d6', '511827', '宝兴县', null, null, '511800');
INSERT INTO `sys_area` VALUES ('9367569053da11e68586000c293c07d6', '511900', '巴中市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('93692f3e53da11e68586000c293c07d6', '511901', '市辖区', null, null, '511900');
INSERT INTO `sys_area` VALUES ('936bd5ac53da11e68586000c293c07d6', '511902', '巴州区', null, null, '511901');
INSERT INTO `sys_area` VALUES ('936db91f53da11e68586000c293c07d6', '511921', '通江县', null, null, '511900');
INSERT INTO `sys_area` VALUES ('936fb3f053da11e68586000c293c07d6', '511922', '南江县', null, null, '511900');
INSERT INTO `sys_area` VALUES ('9371707253da11e68586000c293c07d6', '511923', '平昌县', null, null, '511900');
INSERT INTO `sys_area` VALUES ('9373635753da11e68586000c293c07d6', '512000', '资阳市', null, null, '510000');
INSERT INTO `sys_area` VALUES ('93754a0853da11e68586000c293c07d6', '512001', '市辖区', null, null, '512000');
INSERT INTO `sys_area` VALUES ('93774b4b53da11e68586000c293c07d6', '512002', '雁江区', null, null, '512001');
INSERT INTO `sys_area` VALUES ('93794a7e53da11e68586000c293c07d6', '512021', '安岳县', null, null, '512000');
INSERT INTO `sys_area` VALUES ('937b76c753da11e68586000c293c07d6', '512022', '乐至县', null, null, '512000');
INSERT INTO `sys_area` VALUES ('937d59fe53da11e68586000c293c07d6', '512081', '简阳市', null, null, '512000');
INSERT INTO `sys_area` VALUES ('937f326a53da11e68586000c293c07d6', '513200', '阿坝藏族羌族自治州', null, null, '510000');
INSERT INTO `sys_area` VALUES ('9380f0ef53da11e68586000c293c07d6', '513221', '汶川县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9382ed1d53da11e68586000c293c07d6', '513222', '理县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9384e89853da11e68586000c293c07d6', '513223', '茂县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9386bf8a53da11e68586000c293c07d6', '513224', '松潘县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('93888bf053da11e68586000c293c07d6', '513225', '九寨沟县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('938a6e0853da11e68586000c293c07d6', '513226', '金川县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('938c551e53da11e68586000c293c07d6', '513227', '小金县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('938e2efe53da11e68586000c293c07d6', '513228', '黑水县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('939005e853da11e68586000c293c07d6', '513229', '马尔康县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9391d4e453da11e68586000c293c07d6', '513230', '壤塘县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9393ae9d53da11e68586000c293c07d6', '513231', '阿坝县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('9395708f53da11e68586000c293c07d6', '513232', '若尔盖县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('939758dd53da11e68586000c293c07d6', '513233', '红原县', null, null, '513200');
INSERT INTO `sys_area` VALUES ('939934d753da11e68586000c293c07d6', '513300', '甘孜藏族自治州', null, null, '510000');
INSERT INTO `sys_area` VALUES ('939b4a4053da11e68586000c293c07d6', '513321', '康定县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('939d1c4f53da11e68586000c293c07d6', '513322', '泸定县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('939ef9a953da11e68586000c293c07d6', '513323', '丹巴县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93a0e0da53da11e68586000c293c07d6', '513324', '九龙县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93a2933353da11e68586000c293c07d6', '513325', '雅江县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93a457ce53da11e68586000c293c07d6', '513326', '道孚县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93a63dc553da11e68586000c293c07d6', '513327', '炉霍县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93a81db753da11e68586000c293c07d6', '513328', '甘孜县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93aa279153da11e68586000c293c07d6', '513329', '新龙县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93abf49253da11e68586000c293c07d6', '513330', '德格县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93ae145753da11e68586000c293c07d6', '513331', '白玉县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93afec8a53da11e68586000c293c07d6', '513332', '石渠县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93b19f9f53da11e68586000c293c07d6', '513333', '色达县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93b37b8453da11e68586000c293c07d6', '513334', '理塘县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93b58d9953da11e68586000c293c07d6', '513335', '巴塘县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93b788fe53da11e68586000c293c07d6', '513336', '乡城县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93b9657853da11e68586000c293c07d6', '513337', '稻城县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93bb241753da11e68586000c293c07d6', '513338', '得荣县', null, null, '513300');
INSERT INTO `sys_area` VALUES ('93bd3f4553da11e68586000c293c07d6', '513400', '凉山彝族自治州', null, null, '510000');
INSERT INTO `sys_area` VALUES ('93bf409a53da11e68586000c293c07d6', '513401', '西昌市', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93c11ddf53da11e68586000c293c07d6', '513422', '木里藏族自治县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93c2f05a53da11e68586000c293c07d6', '513423', '盐源县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93c4a5aa53da11e68586000c293c07d6', '513424', '德昌县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93c6658a53da11e68586000c293c07d6', '513425', '会理县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93c8a72853da11e68586000c293c07d6', '513426', '会东县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93ca8fac53da11e68586000c293c07d6', '513427', '宁南县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93cc55c353da11e68586000c293c07d6', '513428', '普格县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93ce4a3253da11e68586000c293c07d6', '513429', '布拖县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93cfef8f53da11e68586000c293c07d6', '513430', '金阳县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93d1ef2653da11e68586000c293c07d6', '513431', '昭觉县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93d3c2a453da11e68586000c293c07d6', '513432', '喜德县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93d57ebe53da11e68586000c293c07d6', '513433', '冕宁县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93d75d7153da11e68586000c293c07d6', '513434', '越西县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93d9277453da11e68586000c293c07d6', '513435', '甘洛县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93db177153da11e68586000c293c07d6', '513436', '美姑县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93dcd65953da11e68586000c293c07d6', '513437', '雷波县', null, null, '513400');
INSERT INTO `sys_area` VALUES ('93deaed053da11e68586000c293c07d6', '520000', '贵州省', null, '24', '0');
INSERT INTO `sys_area` VALUES ('93e1349353da11e68586000c293c07d6', '520100', '贵阳市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('93e30f4653da11e68586000c293c07d6', '520101', '市辖区', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93e4c97353da11e68586000c293c07d6', '520102', '南明区', null, null, '520101');
INSERT INTO `sys_area` VALUES ('93e68e4153da11e68586000c293c07d6', '520103', '云岩区', null, null, '520101');
INSERT INTO `sys_area` VALUES ('93e87fa653da11e68586000c293c07d6', '520111', '花溪区', null, null, '520101');
INSERT INTO `sys_area` VALUES ('93ea5c1053da11e68586000c293c07d6', '520112', '乌当区', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93ec26c553da11e68586000c293c07d6', '520113', '白云区', null, null, '520101');
INSERT INTO `sys_area` VALUES ('93edfe2853da11e68586000c293c07d6', '520114', '小河区', null, null, '520101');
INSERT INTO `sys_area` VALUES ('93f0184a53da11e68586000c293c07d6', '520121', '开阳县', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93f1e5dd53da11e68586000c293c07d6', '520122', '息烽县', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93f3997053da11e68586000c293c07d6', '520123', '修文县', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93f5788853da11e68586000c293c07d6', '520181', '清镇市', null, null, '520100');
INSERT INTO `sys_area` VALUES ('93f74e7453da11e68586000c293c07d6', '520200', '六盘水市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('93f9060e53da11e68586000c293c07d6', '520201', '钟山区', null, null, '520200');
INSERT INTO `sys_area` VALUES ('93fafc3753da11e68586000c293c07d6', '520203', '六枝特区', null, null, '520200');
INSERT INTO `sys_area` VALUES ('93fcc29853da11e68586000c293c07d6', '520221', '水城县', null, null, '520200');
INSERT INTO `sys_area` VALUES ('93fe9caa53da11e68586000c293c07d6', '520222', '盘县', null, null, '520200');
INSERT INTO `sys_area` VALUES ('94005e9253da11e68586000c293c07d6', '520300', '遵义市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('94023cae53da11e68586000c293c07d6', '520301', '市辖区', null, null, '520300');
INSERT INTO `sys_area` VALUES ('940410f353da11e68586000c293c07d6', '520302', '红花岗区', null, null, '520301');
INSERT INTO `sys_area` VALUES ('9405d28b53da11e68586000c293c07d6', '520303', '汇川区', null, null, '520301');
INSERT INTO `sys_area` VALUES ('94079c7653da11e68586000c293c07d6', '520321', '遵义县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('9409944653da11e68586000c293c07d6', '520322', '桐梓县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('940b76f153da11e68586000c293c07d6', '520323', '绥阳县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('940d538e53da11e68586000c293c07d6', '520324', '正安县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('940f431153da11e68586000c293c07d6', '520325', '道真仡佬族苗族自治县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('941157bd53da11e68586000c293c07d6', '520326', '务川仡佬族苗族自治县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('94132ede53da11e68586000c293c07d6', '520327', '凤冈县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('9415aa3a53da11e68586000c293c07d6', '520328', '湄潭县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('94178fe253da11e68586000c293c07d6', '520329', '余庆县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('941964a453da11e68586000c293c07d6', '520330', '习水县', null, null, '520300');
INSERT INTO `sys_area` VALUES ('941b348d53da11e68586000c293c07d6', '520381', '赤水市', null, null, '520300');
INSERT INTO `sys_area` VALUES ('941db08253da11e68586000c293c07d6', '520382', '仁怀市', null, null, '520300');
INSERT INTO `sys_area` VALUES ('941fb32353da11e68586000c293c07d6', '520400', '安顺市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('9421836653da11e68586000c293c07d6', '520401', '市辖区', null, null, '520400');
INSERT INTO `sys_area` VALUES ('9423546053da11e68586000c293c07d6', '520402', '西秀区', null, null, '520401');
INSERT INTO `sys_area` VALUES ('94267aea53da11e68586000c293c07d6', '520421', '平坝县', null, null, '520400');
INSERT INTO `sys_area` VALUES ('94288c1453da11e68586000c293c07d6', '520422', '普定县', null, null, '520400');
INSERT INTO `sys_area` VALUES ('942ab1c953da11e68586000c293c07d6', '520423', '镇宁布依族苗族自治县', null, null, '520400');
INSERT INTO `sys_area` VALUES ('942c8dc253da11e68586000c293c07d6', '520424', '关岭布依族苗族自治县', null, null, '520400');
INSERT INTO `sys_area` VALUES ('942e533b53da11e68586000c293c07d6', '520425', '紫云苗族布依族自治县', null, null, '520400');
INSERT INTO `sys_area` VALUES ('9430110d53da11e68586000c293c07d6', '522200', '铜仁市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('943213e953da11e68586000c293c07d6', '522201', '碧江区', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943439e453da11e68586000c293c07d6', '522222', '江口县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943654db53da11e68586000c293c07d6', '522223', '玉屏侗族自治县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('94380f0c53da11e68586000c293c07d6', '522224', '石阡县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943a0c9353da11e68586000c293c07d6', '522225', '思南县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943c14cc53da11e68586000c293c07d6', '522226', '印江土家族苗族自治县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943e428f53da11e68586000c293c07d6', '522227', '德江县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('943fe88753da11e68586000c293c07d6', '522228', '沿河土家族自治县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('9441f15d53da11e68586000c293c07d6', '522229', '松桃苗族自治县', null, null, '522200');
INSERT INTO `sys_area` VALUES ('944403b653da11e68586000c293c07d6', '522230', '万山区', null, null, '522200');
INSERT INTO `sys_area` VALUES ('9445d70f53da11e68586000c293c07d6', '522300', '黔西南布依族苗族自治州', null, null, '520000');
INSERT INTO `sys_area` VALUES ('9447814e53da11e68586000c293c07d6', '522301', '兴义市', null, null, '522300');
INSERT INTO `sys_area` VALUES ('94494b5b53da11e68586000c293c07d6', '522322', '兴仁县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('944b3be953da11e68586000c293c07d6', '522323', '普安县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('944cf89353da11e68586000c293c07d6', '522324', '晴隆县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('944ecbbe53da11e68586000c293c07d6', '522325', '贞丰县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('94527f7253da11e68586000c293c07d6', '522326', '望谟县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('94544b8253da11e68586000c293c07d6', '522327', '册亨县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('945625eb53da11e68586000c293c07d6', '522328', '安龙县', null, null, '522300');
INSERT INTO `sys_area` VALUES ('94582b7f53da11e68586000c293c07d6', '522400', '毕节市', null, null, '520000');
INSERT INTO `sys_area` VALUES ('9459fd2e53da11e68586000c293c07d6', '522401', '七星关区', null, null, '522400');
INSERT INTO `sys_area` VALUES ('945bc36f53da11e68586000c293c07d6', '522422', '大方县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('945d85be53da11e68586000c293c07d6', '522423', '黔西县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('945f5e9353da11e68586000c293c07d6', '522424', '金沙县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('946137ea53da11e68586000c293c07d6', '522425', '织金县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('9463204453da11e68586000c293c07d6', '522426', '纳雍县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('94653b6753da11e68586000c293c07d6', '522427', '威宁彝族回族苗族自治县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('946755f053da11e68586000c293c07d6', '522428', '赫章县', null, null, '522400');
INSERT INTO `sys_area` VALUES ('94693d3853da11e68586000c293c07d6', '522600', '黔东南苗族侗族自治州', null, null, '520000');
INSERT INTO `sys_area` VALUES ('946b538853da11e68586000c293c07d6', '522601', '凯里市', null, null, '522600');
INSERT INTO `sys_area` VALUES ('946d090553da11e68586000c293c07d6', '522622', '黄平县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('946ebcae53da11e68586000c293c07d6', '522623', '施秉县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9470629c53da11e68586000c293c07d6', '522624', '三穗县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9472940f53da11e68586000c293c07d6', '522625', '镇远县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9474ac2153da11e68586000c293c07d6', '522626', '岑巩县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9476a71b53da11e68586000c293c07d6', '522627', '天柱县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9478936253da11e68586000c293c07d6', '522628', '锦屏县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('947a790353da11e68586000c293c07d6', '522629', '剑河县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('947c453053da11e68586000c293c07d6', '522630', '台江县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('947e3d4253da11e68586000c293c07d6', '522631', '黎平县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9480089e53da11e68586000c293c07d6', '522632', '榕江县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9481d32853da11e68586000c293c07d6', '522633', '从江县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9483b66053da11e68586000c293c07d6', '522634', '雷山县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('9485be9253da11e68586000c293c07d6', '522635', '麻江县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('94876ad753da11e68586000c293c07d6', '522636', '丹寨县', null, null, '522600');
INSERT INTO `sys_area` VALUES ('94895e6153da11e68586000c293c07d6', '522700', '黔南布依族苗族自治州', null, null, '520000');
INSERT INTO `sys_area` VALUES ('948b483753da11e68586000c293c07d6', '522701', '都匀市', null, null, '522700');
INSERT INTO `sys_area` VALUES ('948d268253da11e68586000c293c07d6', '522702', '福泉市', null, null, '522700');
INSERT INTO `sys_area` VALUES ('948f010953da11e68586000c293c07d6', '522722', '荔波县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('9490b93f53da11e68586000c293c07d6', '522723', '贵定县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('9492d1bb53da11e68586000c293c07d6', '522725', '瓮安县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('9494ad1553da11e68586000c293c07d6', '522726', '独山县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('94969cec53da11e68586000c293c07d6', '522727', '平塘县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('949863f253da11e68586000c293c07d6', '522728', '罗甸县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('949a655253da11e68586000c293c07d6', '522729', '长顺县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('949c457e53da11e68586000c293c07d6', '522730', '龙里县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('949e330653da11e68586000c293c07d6', '522731', '惠水县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('949ff5a653da11e68586000c293c07d6', '522732', '三都水族自治县', null, null, '522700');
INSERT INTO `sys_area` VALUES ('94a1c12553da11e68586000c293c07d6', '530000', '云南省', null, '25', '0');
INSERT INTO `sys_area` VALUES ('94a387c453da11e68586000c293c07d6', '530100', '昆明市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('94a5719953da11e68586000c293c07d6', '530101', '市辖区', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94a768d153da11e68586000c293c07d6', '530102', '五华区', null, null, '530101');
INSERT INTO `sys_area` VALUES ('94a9360553da11e68586000c293c07d6', '530103', '盘龙区', null, null, '530101');
INSERT INTO `sys_area` VALUES ('94ab046853da11e68586000c293c07d6', '530111', '官渡区', null, null, '530101');
INSERT INTO `sys_area` VALUES ('94acd88153da11e68586000c293c07d6', '530112', '西山区', null, null, '530101');
INSERT INTO `sys_area` VALUES ('94ae9fec53da11e68586000c293c07d6', '530113', '东川区', null, null, '530101');
INSERT INTO `sys_area` VALUES ('94b0a07253da11e68586000c293c07d6', '530121', '呈贡县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94b2552f53da11e68586000c293c07d6', '530122', '晋宁县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94b46ff453da11e68586000c293c07d6', '530124', '富民县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94b64aa753da11e68586000c293c07d6', '530125', '宜良县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94b8080f53da11e68586000c293c07d6', '530126', '石林彝族自治县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94ba2ec253da11e68586000c293c07d6', '530127', '嵩明县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94bbfe1c53da11e68586000c293c07d6', '530128', '禄劝彝族苗族自治县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94bdd8fb53da11e68586000c293c07d6', '530129', '寻甸回族彝族自治县', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94bf9c6753da11e68586000c293c07d6', '530181', '安宁市', null, null, '530100');
INSERT INTO `sys_area` VALUES ('94c14c1153da11e68586000c293c07d6', '530300', '曲靖市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('94c3315153da11e68586000c293c07d6', '530301', '市辖区', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94c544d653da11e68586000c293c07d6', '530302', '麒麟区', null, null, '530301');
INSERT INTO `sys_area` VALUES ('94c6f6e953da11e68586000c293c07d6', '530321', '马龙县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94c8debd53da11e68586000c293c07d6', '530322', '陆良县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94ca908c53da11e68586000c293c07d6', '530323', '师宗县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94cc842f53da11e68586000c293c07d6', '530324', '罗平县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94ce5cdd53da11e68586000c293c07d6', '530325', '富源县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94d0434b53da11e68586000c293c07d6', '530326', '会泽县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94d2398f53da11e68586000c293c07d6', '530328', '沾益县', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94d4039c53da11e68586000c293c07d6', '530381', '宣威市', null, null, '530300');
INSERT INTO `sys_area` VALUES ('94d5be9d53da11e68586000c293c07d6', '530400', '玉溪市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('94d7901d53da11e68586000c293c07d6', '530401', '市辖区', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94d994b353da11e68586000c293c07d6', '530402', '红塔区', null, null, '530401');
INSERT INTO `sys_area` VALUES ('94db52d153da11e68586000c293c07d6', '530421', '江川县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94dd0c6f53da11e68586000c293c07d6', '530422', '澄江县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94defa0953da11e68586000c293c07d6', '530423', '通海县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e0e1d953da11e68586000c293c07d6', '530424', '华宁县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e29e0853da11e68586000c293c07d6', '530425', '易门县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e46e9553da11e68586000c293c07d6', '530426', '峨山彝族自治县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e64cc853da11e68586000c293c07d6', '530427', '新平彝族傣族自治县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e8159853da11e68586000c293c07d6', '530428', '元江哈尼族彝族傣族自治县', null, null, '530400');
INSERT INTO `sys_area` VALUES ('94e9e72053da11e68586000c293c07d6', '530500', '保山市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('94ebbca653da11e68586000c293c07d6', '530501', '市辖区', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94eda96f53da11e68586000c293c07d6', '530502', '隆阳区', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94ef68fa53da11e68586000c293c07d6', '530521', '施甸县', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94f1540153da11e68586000c293c07d6', '530522', '腾冲县', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94f3380253da11e68586000c293c07d6', '530523', '龙陵县', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94f5009a53da11e68586000c293c07d6', '530524', '昌宁县', null, null, '530500');
INSERT INTO `sys_area` VALUES ('94f6ee5153da11e68586000c293c07d6', '530600', '昭通市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('94f8cf9b53da11e68586000c293c07d6', '530601', '市辖区', null, null, '530600');
INSERT INTO `sys_area` VALUES ('94face1f53da11e68586000c293c07d6', '530602', '昭阳区', null, null, '530601');
INSERT INTO `sys_area` VALUES ('94fcec7953da11e68586000c293c07d6', '530621', '鲁甸县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('94feddca53da11e68586000c293c07d6', '530622', '巧家县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('95008ec953da11e68586000c293c07d6', '530623', '盐津县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('950296b553da11e68586000c293c07d6', '530624', '大关县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('9504954353da11e68586000c293c07d6', '530625', '永善县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('95064fb253da11e68586000c293c07d6', '530626', '绥江县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('95083b7f53da11e68586000c293c07d6', '530627', '镇雄县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('950a2d5d53da11e68586000c293c07d6', '530628', '彝良县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('950c2c0353da11e68586000c293c07d6', '530629', '威信县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('950e054b53da11e68586000c293c07d6', '530630', '水富县', null, null, '530600');
INSERT INTO `sys_area` VALUES ('95100c0d53da11e68586000c293c07d6', '530700', '丽江市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('9511e5d153da11e68586000c293c07d6', '530701', '市辖区', null, null, '530700');
INSERT INTO `sys_area` VALUES ('9513e85e53da11e68586000c293c07d6', '530702', '古城区', null, null, '530701');
INSERT INTO `sys_area` VALUES ('9515ac7f53da11e68586000c293c07d6', '530721', '玉龙纳西族自治县', null, null, '530700');
INSERT INTO `sys_area` VALUES ('9517b3ae53da11e68586000c293c07d6', '530722', '永胜县', null, null, '530700');
INSERT INTO `sys_area` VALUES ('9519a13753da11e68586000c293c07d6', '530723', '华坪县', null, null, '530700');
INSERT INTO `sys_area` VALUES ('951bb72853da11e68586000c293c07d6', '530724', '宁蒗彝族自治县', null, null, '530700');
INSERT INTO `sys_area` VALUES ('951d61b053da11e68586000c293c07d6', '530800', '普洱市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('951f1e6b53da11e68586000c293c07d6', '530801', '市辖区', null, null, '530800');
INSERT INTO `sys_area` VALUES ('9521412253da11e68586000c293c07d6', '530802', '翠云区', null, null, '530801');
INSERT INTO `sys_area` VALUES ('95232ab753da11e68586000c293c07d6', '530821', '宁洱哈尼族彝族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('9524e77653da11e68586000c293c07d6', '530822', '墨江哈尼族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('9526df0c53da11e68586000c293c07d6', '530823', '景东彝族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('9528b3cc53da11e68586000c293c07d6', '530824', '景谷傣族彝族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('952a813153da11e68586000c293c07d6', '530825', '镇沅彝族哈尼族拉祜族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('952c3cd653da11e68586000c293c07d6', '530826', '江城哈尼族彝族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('952df82253da11e68586000c293c07d6', '530827', '孟连傣族拉祜族佤族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('952fd03453da11e68586000c293c07d6', '530828', '澜沧拉祜族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('953190bf53da11e68586000c293c07d6', '530829', '西盟佤族自治县', null, null, '530800');
INSERT INTO `sys_area` VALUES ('953381d653da11e68586000c293c07d6', '530900', '临沧市', null, null, '530000');
INSERT INTO `sys_area` VALUES ('953569c053da11e68586000c293c07d6', '530901', '市辖区', null, null, '530900');
INSERT INTO `sys_area` VALUES ('9537491053da11e68586000c293c07d6', '530902', '临翔区', null, null, '530901');
INSERT INTO `sys_area` VALUES ('95392ee753da11e68586000c293c07d6', '530921', '凤庆县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('953b30b253da11e68586000c293c07d6', '530922', '云县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('953d4a2f53da11e68586000c293c07d6', '530923', '永德县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('953f12ee53da11e68586000c293c07d6', '530924', '镇康县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('9540d9d453da11e68586000c293c07d6', '530925', '双江拉祜族佤族布朗族傣族自治县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('9542bca553da11e68586000c293c07d6', '530926', '耿马傣族佤族自治县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('95449c9753da11e68586000c293c07d6', '530927', '沧源佤族自治县', null, null, '530900');
INSERT INTO `sys_area` VALUES ('95465f1453da11e68586000c293c07d6', '532300', '楚雄彝族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('95481b5a53da11e68586000c293c07d6', '532301', '楚雄市', null, null, '532300');
INSERT INTO `sys_area` VALUES ('954a2b9753da11e68586000c293c07d6', '532322', '双柏县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('954c212953da11e68586000c293c07d6', '532323', '牟定县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('954e015253da11e68586000c293c07d6', '532324', '南华县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('954fa96353da11e68586000c293c07d6', '532325', '姚安县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('9551889253da11e68586000c293c07d6', '532326', '大姚县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('95538c8353da11e68586000c293c07d6', '532327', '永仁县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('9555729053da11e68586000c293c07d6', '532328', '元谋县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('9557554d53da11e68586000c293c07d6', '532329', '武定县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('95599a5c53da11e68586000c293c07d6', '532331', '禄丰县', null, null, '532300');
INSERT INTO `sys_area` VALUES ('955b916353da11e68586000c293c07d6', '532500', '红河哈尼族彝族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('955d638a53da11e68586000c293c07d6', '532501', '个旧市', null, null, '532500');
INSERT INTO `sys_area` VALUES ('955f2b6053da11e68586000c293c07d6', '532502', '开远市', null, null, '532500');
INSERT INTO `sys_area` VALUES ('95610ab853da11e68586000c293c07d6', '532522', '蒙自市', null, null, '532500');
INSERT INTO `sys_area` VALUES ('9562cc3953da11e68586000c293c07d6', '532523', '屏边苗族自治县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('95647dfe53da11e68586000c293c07d6', '532524', '建水县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('9566524a53da11e68586000c293c07d6', '532525', '石屏县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('9568222c53da11e68586000c293c07d6', '532526', '弥勒市', null, null, '532500');
INSERT INTO `sys_area` VALUES ('956a09b253da11e68586000c293c07d6', '532527', '泸西县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('956bc67153da11e68586000c293c07d6', '532528', '元阳县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('956dc2df53da11e68586000c293c07d6', '532529', '红河县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('956f98e353da11e68586000c293c07d6', '532530', '金平苗族瑶族傣族自治县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('9571457a53da11e68586000c293c07d6', '532531', '绿春县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('957311a353da11e68586000c293c07d6', '532532', '河口瑶族自治县', null, null, '532500');
INSERT INTO `sys_area` VALUES ('9574eeb453da11e68586000c293c07d6', '532600', '文山壮族苗族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('95770afc53da11e68586000c293c07d6', '532621', '文山市', null, null, '532600');
INSERT INTO `sys_area` VALUES ('9578c3de53da11e68586000c293c07d6', '532622', '砚山县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('957e150a53da11e68586000c293c07d6', '532623', '西畴县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('957ff96053da11e68586000c293c07d6', '532624', '麻栗坡县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('9581b79853da11e68586000c293c07d6', '532625', '马关县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('9583909353da11e68586000c293c07d6', '532626', '丘北县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('958551cb53da11e68586000c293c07d6', '532627', '广南县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('9587258753da11e68586000c293c07d6', '532628', '富宁县', null, null, '532600');
INSERT INTO `sys_area` VALUES ('9589052753da11e68586000c293c07d6', '532800', '西双版纳傣族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('958abf9a53da11e68586000c293c07d6', '532801', '景洪市', null, null, '532800');
INSERT INTO `sys_area` VALUES ('958caac853da11e68586000c293c07d6', '532822', '勐海县', null, null, '532800');
INSERT INTO `sys_area` VALUES ('958e9ce853da11e68586000c293c07d6', '532823', '勐腊县', null, null, '532800');
INSERT INTO `sys_area` VALUES ('95911db853da11e68586000c293c07d6', '532900', '大理白族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('9592eb2753da11e68586000c293c07d6', '532901', '大理市', null, null, '532900');
INSERT INTO `sys_area` VALUES ('9594b9bb53da11e68586000c293c07d6', '532922', '漾濞彝族自治县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95967dbe53da11e68586000c293c07d6', '532923', '祥云县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95984b3453da11e68586000c293c07d6', '532924', '宾川县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('959a048253da11e68586000c293c07d6', '532925', '弥渡县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('959bbc2f53da11e68586000c293c07d6', '532926', '南涧彝族自治县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('959dd18653da11e68586000c293c07d6', '532927', '巍山彝族回族自治县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95a4f67353da11e68586000c293c07d6', '532928', '永平县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95a6fee353da11e68586000c293c07d6', '532929', '云龙县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95a8b55f53da11e68586000c293c07d6', '532930', '洱源县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95aa82c953da11e68586000c293c07d6', '532931', '剑川县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95ac5d5753da11e68586000c293c07d6', '532932', '鹤庆县', null, null, '532900');
INSERT INTO `sys_area` VALUES ('95ae35c553da11e68586000c293c07d6', '533100', '德宏傣族景颇族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('95b036cb53da11e68586000c293c07d6', '533102', '瑞丽市', null, null, '533100');
INSERT INTO `sys_area` VALUES ('95b20e3b53da11e68586000c293c07d6', '533103', '潞西市', null, null, '533100');
INSERT INTO `sys_area` VALUES ('95b40a5353da11e68586000c293c07d6', '533122', '梁河县', null, null, '533100');
INSERT INTO `sys_area` VALUES ('95b5cddb53da11e68586000c293c07d6', '533123', '盈江县', null, null, '533100');
INSERT INTO `sys_area` VALUES ('95b80a3c53da11e68586000c293c07d6', '533124', '陇川县', null, null, '533100');
INSERT INTO `sys_area` VALUES ('95b9e0a153da11e68586000c293c07d6', '533300', '怒江傈僳族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('95bba55753da11e68586000c293c07d6', '533321', '泸水县', null, null, '533300');
INSERT INTO `sys_area` VALUES ('95bd6a6a53da11e68586000c293c07d6', '533323', '福贡县', null, null, '533300');
INSERT INTO `sys_area` VALUES ('95bfa58c53da11e68586000c293c07d6', '533324', '贡山独龙族怒族自治县', null, null, '533300');
INSERT INTO `sys_area` VALUES ('95c161d153da11e68586000c293c07d6', '533325', '兰坪白族普米族自治县', null, null, '533300');
INSERT INTO `sys_area` VALUES ('95c64cca53da11e68586000c293c07d6', '533400', '迪庆藏族自治州', null, null, '530000');
INSERT INTO `sys_area` VALUES ('95c8910b53da11e68586000c293c07d6', '533421', '香格里拉县', null, null, '533400');
INSERT INTO `sys_area` VALUES ('95ca53a753da11e68586000c293c07d6', '533422', '德钦县', null, null, '533400');
INSERT INTO `sys_area` VALUES ('95cc0ad853da11e68586000c293c07d6', '533423', '维西傈僳族自治县', null, null, '533400');
INSERT INTO `sys_area` VALUES ('95cdf3f753da11e68586000c293c07d6', '540000', '西藏自治区', null, '26', '0');
INSERT INTO `sys_area` VALUES ('95cfcb3953da11e68586000c293c07d6', '540100', '拉萨市', null, null, '540000');
INSERT INTO `sys_area` VALUES ('95d1857653da11e68586000c293c07d6', '540101', '市辖区', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95d37b6953da11e68586000c293c07d6', '540102', '城关区', null, null, '540101');
INSERT INTO `sys_area` VALUES ('95d580da53da11e68586000c293c07d6', '540121', '林周县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95d7b75553da11e68586000c293c07d6', '540122', '当雄县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95d9bbd353da11e68586000c293c07d6', '540123', '尼木县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95dbb85f53da11e68586000c293c07d6', '540124', '曲水县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95de46ee53da11e68586000c293c07d6', '540125', '堆龙德庆县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95e01e4a53da11e68586000c293c07d6', '540126', '达孜县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95e1fc8653da11e68586000c293c07d6', '540127', '墨竹工卡县', null, null, '540100');
INSERT INTO `sys_area` VALUES ('95e41bf953da11e68586000c293c07d6', '542100', '昌都地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('95e6791f53da11e68586000c293c07d6', '542121', '昌都县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95e87faa53da11e68586000c293c07d6', '542122', '江达县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95eace6553da11e68586000c293c07d6', '542123', '贡觉县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95ec90eb53da11e68586000c293c07d6', '542124', '类乌齐县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95ef221553da11e68586000c293c07d6', '542125', '丁青县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95f191db53da11e68586000c293c07d6', '542126', '察雅县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95f35fd853da11e68586000c293c07d6', '542127', '八宿县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95f5a02753da11e68586000c293c07d6', '542128', '左贡县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95f7897853da11e68586000c293c07d6', '542129', '芒康县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95f9b3c353da11e68586000c293c07d6', '542132', '洛隆县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95fc8a6a53da11e68586000c293c07d6', '542133', '边坝县', null, null, '542100');
INSERT INTO `sys_area` VALUES ('95feb88953da11e68586000c293c07d6', '542200', '山南地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('960084b053da11e68586000c293c07d6', '542221', '乃东县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9602431853da11e68586000c293c07d6', '542222', '扎囊县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('96042a4a53da11e68586000c293c07d6', '542223', '贡嘎县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9606410a53da11e68586000c293c07d6', '542224', '桑日县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9608555453da11e68586000c293c07d6', '542225', '琼结县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9609f49453da11e68586000c293c07d6', '542226', '曲松县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('960bd8df53da11e68586000c293c07d6', '542227', '措美县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('960f492d53da11e68586000c293c07d6', '542228', '洛扎县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('961113bb53da11e68586000c293c07d6', '542229', '加查县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9612dbbd53da11e68586000c293c07d6', '542231', '隆子县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('9614bfe053da11e68586000c293c07d6', '542232', '错那县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('961680fb53da11e68586000c293c07d6', '542233', '浪卡子县', null, null, '542200');
INSERT INTO `sys_area` VALUES ('96185ad353da11e68586000c293c07d6', '542300', '日喀则地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('961a513e53da11e68586000c293c07d6', '542301', '日喀则市', null, null, '542300');
INSERT INTO `sys_area` VALUES ('961c266d53da11e68586000c293c07d6', '542322', '南木林县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('961e74ee53da11e68586000c293c07d6', '542323', '江孜县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('962029c953da11e68586000c293c07d6', '542324', '定日县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('96221b9753da11e68586000c293c07d6', '542325', '萨迦县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('96240de053da11e68586000c293c07d6', '542326', '拉孜县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('9625c56053da11e68586000c293c07d6', '542327', '昂仁县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('9627b98c53da11e68586000c293c07d6', '542328', '谢通门县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('9629a4d153da11e68586000c293c07d6', '542329', '白朗县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('962ba1af53da11e68586000c293c07d6', '542330', '仁布县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('962d78ad53da11e68586000c293c07d6', '542331', '康马县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('962f497753da11e68586000c293c07d6', '542332', '定结县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('96311e7353da11e68586000c293c07d6', '542333', '仲巴县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('9633139053da11e68586000c293c07d6', '542334', '亚东县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('9634bef353da11e68586000c293c07d6', '542335', '吉隆县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('96372a1c53da11e68586000c293c07d6', '542336', '聂拉木县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('96391c9a53da11e68586000c293c07d6', '542337', '萨嘎县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('963ae84253da11e68586000c293c07d6', '542338', '岗巴县', null, null, '542300');
INSERT INTO `sys_area` VALUES ('963d277c53da11e68586000c293c07d6', '542400', '那曲地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('963f069453da11e68586000c293c07d6', '542421', '那曲县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('9640c89f53da11e68586000c293c07d6', '542422', '嘉黎县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('964293a353da11e68586000c293c07d6', '542423', '比如县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('9644490353da11e68586000c293c07d6', '542424', '聂荣县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('964647d953da11e68586000c293c07d6', '542425', '安多县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('96484dbd53da11e68586000c293c07d6', '542426', '申扎县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('964a2bcf53da11e68586000c293c07d6', '542427', '索县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('964c01a053da11e68586000c293c07d6', '542428', '班戈县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('964e266953da11e68586000c293c07d6', '542429', '巴青县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('9651ce6253da11e68586000c293c07d6', '542430', '尼玛县', null, null, '542400');
INSERT INTO `sys_area` VALUES ('96539caf53da11e68586000c293c07d6', '542500', '阿里地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('9655540453da11e68586000c293c07d6', '542521', '普兰县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('96573e8053da11e68586000c293c07d6', '542522', '札达县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('9659186453da11e68586000c293c07d6', '542523', '噶尔县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('965acc6553da11e68586000c293c07d6', '542524', '日土县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('965d654653da11e68586000c293c07d6', '542525', '革吉县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('965f52fc53da11e68586000c293c07d6', '542526', '改则县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('96612b9b53da11e68586000c293c07d6', '542527', '措勤县', null, null, '542500');
INSERT INTO `sys_area` VALUES ('96632a4053da11e68586000c293c07d6', '542600', '林芝地区', null, null, '540000');
INSERT INTO `sys_area` VALUES ('966529f353da11e68586000c293c07d6', '542621', '林芝县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('9667306a53da11e68586000c293c07d6', '542622', '工布江达县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('96690d3553da11e68586000c293c07d6', '542623', '米林县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('966b315053da11e68586000c293c07d6', '542624', '墨脱县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('966d7dd353da11e68586000c293c07d6', '542625', '波密县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('966f3b6b53da11e68586000c293c07d6', '542626', '察隅县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('96712f2753da11e68586000c293c07d6', '542627', '朗县', null, null, '542600');
INSERT INTO `sys_area` VALUES ('9673145c53da11e68586000c293c07d6', '610000', '陕西省', null, '27', '0');
INSERT INTO `sys_area` VALUES ('9676551753da11e68586000c293c07d6', '610100', '西安市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96785b9553da11e68586000c293c07d6', '610101', '长安区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('967a695153da11e68586000c293c07d6', '610102', '新城区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('967c886953da11e68586000c293c07d6', '610103', '碑林区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('967e800f53da11e68586000c293c07d6', '610104', '莲湖区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('9680637d53da11e68586000c293c07d6', '610111', '灞桥区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('968258d853da11e68586000c293c07d6', '610112', '未央区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('9684758753da11e68586000c293c07d6', '610113', '雁塔区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('96862ba353da11e68586000c293c07d6', '610114', '阎良区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('9687ea3453da11e68586000c293c07d6', '610115', '临潼区', null, null, '610100');
INSERT INTO `sys_area` VALUES ('968a3b4653da11e68586000c293c07d6', '610122', '蓝田县', null, null, '610100');
INSERT INTO `sys_area` VALUES ('968cde4653da11e68586000c293c07d6', '610124', '周至县', null, null, '610100');
INSERT INTO `sys_area` VALUES ('968ec1b253da11e68586000c293c07d6', '610125', '户县', null, null, '610100');
INSERT INTO `sys_area` VALUES ('96908ccf53da11e68586000c293c07d6', '610126', '高陵县', null, null, '610100');
INSERT INTO `sys_area` VALUES ('96925cf353da11e68586000c293c07d6', '610200', '铜川市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96944f8f53da11e68586000c293c07d6', '610201', '市辖区', null, null, '610200');
INSERT INTO `sys_area` VALUES ('9696301b53da11e68586000c293c07d6', '610202', '王益区', null, null, '610201');
INSERT INTO `sys_area` VALUES ('9697f42953da11e68586000c293c07d6', '610203', '印台区', null, null, '610201');
INSERT INTO `sys_area` VALUES ('9699d6bf53da11e68586000c293c07d6', '610204', '耀州区', null, null, '610201');
INSERT INTO `sys_area` VALUES ('969cea5053da11e68586000c293c07d6', '610222', '宜君县', null, null, '610200');
INSERT INTO `sys_area` VALUES ('969eeb5953da11e68586000c293c07d6', '610300', '宝鸡市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96a0cd6b53da11e68586000c293c07d6', '610301', '市辖区', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96a2830d53da11e68586000c293c07d6', '610302', '渭滨区', null, null, '610301');
INSERT INTO `sys_area` VALUES ('96a44bbe53da11e68586000c293c07d6', '610303', '金台区', null, null, '610301');
INSERT INTO `sys_area` VALUES ('96a6253f53da11e68586000c293c07d6', '610304', '陈仓区', null, null, '610301');
INSERT INTO `sys_area` VALUES ('96a824bd53da11e68586000c293c07d6', '610322', '凤翔县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96aa096153da11e68586000c293c07d6', '610323', '岐山县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96abc89753da11e68586000c293c07d6', '610324', '扶风县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96adb41753da11e68586000c293c07d6', '610326', '眉县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96af7e5753da11e68586000c293c07d6', '610327', '陇县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96b140e253da11e68586000c293c07d6', '610328', '千阳县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96b33b1f53da11e68586000c293c07d6', '610329', '麟游县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96b511d953da11e68586000c293c07d6', '610330', '凤县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96b6dbde53da11e68586000c293c07d6', '610331', '太白县', null, null, '610300');
INSERT INTO `sys_area` VALUES ('96b8a2bf53da11e68586000c293c07d6', '610400', '咸阳市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96ba85da53da11e68586000c293c07d6', '610401', '市辖区', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96bc804453da11e68586000c293c07d6', '610402', '秦都区', null, null, '610401');
INSERT INTO `sys_area` VALUES ('96be42bf53da11e68586000c293c07d6', '610404', '渭城区', null, null, '610401');
INSERT INTO `sys_area` VALUES ('96c0286b53da11e68586000c293c07d6', '610422', '三原县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96c21f3053da11e68586000c293c07d6', '610423', '泾阳县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96c4267753da11e68586000c293c07d6', '610424', '乾县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96c5e92653da11e68586000c293c07d6', '610425', '礼泉县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96c7b64153da11e68586000c293c07d6', '610426', '永寿县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96c988a353da11e68586000c293c07d6', '610427', '彬县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96cb7bfb53da11e68586000c293c07d6', '610428', '长武县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96cd609053da11e68586000c293c07d6', '610429', '旬邑县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96cf5fcd53da11e68586000c293c07d6', '610430', '淳化县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96d147ac53da11e68586000c293c07d6', '610431', '武功县', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96d31a9e53da11e68586000c293c07d6', '610481', '兴平市', null, null, '610400');
INSERT INTO `sys_area` VALUES ('96d4f0c053da11e68586000c293c07d6', '610500', '渭南市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96d749f353da11e68586000c293c07d6', '610501', '市辖区', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96d91a1553da11e68586000c293c07d6', '610502', '临渭区', null, null, '610501');
INSERT INTO `sys_area` VALUES ('96db96a753da11e68586000c293c07d6', '610521', '华县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96dd5b7953da11e68586000c293c07d6', '610522', '潼关县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96df17d753da11e68586000c293c07d6', '610523', '大荔县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e0ed6353da11e68586000c293c07d6', '610524', '合阳县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e2cf4e53da11e68586000c293c07d6', '610525', '澄城县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e4b81353da11e68586000c293c07d6', '610526', '蒲城县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e65ae053da11e68586000c293c07d6', '610527', '白水县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e828bd53da11e68586000c293c07d6', '610528', '富平县', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96e9f53853da11e68586000c293c07d6', '610581', '韩城市', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96ebf01153da11e68586000c293c07d6', '610582', '华阴市', null, null, '610500');
INSERT INTO `sys_area` VALUES ('96edc55d53da11e68586000c293c07d6', '610600', '延安市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('96ef90c753da11e68586000c293c07d6', '610601', '市辖区', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96f1608353da11e68586000c293c07d6', '610602', '宝塔区', null, null, '610601');
INSERT INTO `sys_area` VALUES ('96f37a2753da11e68586000c293c07d6', '610621', '延长县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96f54c0a53da11e68586000c293c07d6', '610622', '延川县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96f72ea753da11e68586000c293c07d6', '610623', '子长县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96f8d51a53da11e68586000c293c07d6', '610624', '安塞县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96faa9d853da11e68586000c293c07d6', '610625', '志丹县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96fd2ecf53da11e68586000c293c07d6', '610626', '吴起县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('96ff2cec53da11e68586000c293c07d6', '610627', '甘泉县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('97010cac53da11e68586000c293c07d6', '610628', '富县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('970318e053da11e68586000c293c07d6', '610629', '洛川县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('9704d97753da11e68586000c293c07d6', '610630', '宜川县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('9706a34f53da11e68586000c293c07d6', '610631', '黄龙县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('97087ce053da11e68586000c293c07d6', '610632', '黄陵县', null, null, '610600');
INSERT INTO `sys_area` VALUES ('970a3fc153da11e68586000c293c07d6', '610700', '汉中市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('970c2b5553da11e68586000c293c07d6', '610701', '市辖区', null, null, '610700');
INSERT INTO `sys_area` VALUES ('970e073d53da11e68586000c293c07d6', '610702', '汉台区', null, null, '610701');
INSERT INTO `sys_area` VALUES ('97101e3e53da11e68586000c293c07d6', '610721', '南郑县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('9711ed5253da11e68586000c293c07d6', '610722', '城固县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('9713bc9653da11e68586000c293c07d6', '610723', '洋县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('9715b5f553da11e68586000c293c07d6', '610724', '西乡县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('97179d1953da11e68586000c293c07d6', '610725', '勉县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('97195a3353da11e68586000c293c07d6', '610726', '宁强县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('971ba5ff53da11e68586000c293c07d6', '610727', '略阳县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('971d766353da11e68586000c293c07d6', '610728', '镇巴县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('971f4d2553da11e68586000c293c07d6', '610729', '留坝县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('972133fd53da11e68586000c293c07d6', '610730', '佛坪县', null, null, '610700');
INSERT INTO `sys_area` VALUES ('9723c0fc53da11e68586000c293c07d6', '610800', '榆林市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('972591a453da11e68586000c293c07d6', '610801', '市辖区', null, null, '610800');
INSERT INTO `sys_area` VALUES ('97277e2253da11e68586000c293c07d6', '610802', '榆阳区', null, null, '610800');
INSERT INTO `sys_area` VALUES ('97299b7c53da11e68586000c293c07d6', '610821', '神木县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('972b5ee653da11e68586000c293c07d6', '610822', '府谷县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('972d7d0553da11e68586000c293c07d6', '610823', '横山县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('972f775553da11e68586000c293c07d6', '610824', '靖边县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('9731771253da11e68586000c293c07d6', '610825', '定边县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('9733289753da11e68586000c293c07d6', '610826', '绥德县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('9734f93453da11e68586000c293c07d6', '610827', '米脂县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('973707a953da11e68586000c293c07d6', '610828', '佳县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('973908e053da11e68586000c293c07d6', '610829', '吴堡县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('973acb9053da11e68586000c293c07d6', '610830', '清涧县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('973cbdd053da11e68586000c293c07d6', '610831', '子洲县', null, null, '610800');
INSERT INTO `sys_area` VALUES ('973e867653da11e68586000c293c07d6', '610900', '安康市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('97406e7553da11e68586000c293c07d6', '610901', '市辖区', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9742465d53da11e68586000c293c07d6', '610902', '汉滨区', null, null, '610901');
INSERT INTO `sys_area` VALUES ('97440ff353da11e68586000c293c07d6', '610921', '汉阴县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9745f84d53da11e68586000c293c07d6', '610922', '石泉县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9747cb8553da11e68586000c293c07d6', '610923', '宁陕县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9749ba1853da11e68586000c293c07d6', '610924', '紫阳县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('974ba05653da11e68586000c293c07d6', '610925', '岚皋县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('974d7ba353da11e68586000c293c07d6', '610926', '平利县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('974f341953da11e68586000c293c07d6', '610927', '镇坪县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('975117a953da11e68586000c293c07d6', '610928', '旬阳县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9753295053da11e68586000c293c07d6', '610929', '白河县', null, null, '610900');
INSERT INTO `sys_area` VALUES ('9755157553da11e68586000c293c07d6', '611000', '商洛市', null, null, '610000');
INSERT INTO `sys_area` VALUES ('9756d6d553da11e68586000c293c07d6', '611001', '市辖区', null, null, '611000');
INSERT INTO `sys_area` VALUES ('9758e18653da11e68586000c293c07d6', '611002', '商州区', null, null, '611001');
INSERT INTO `sys_area` VALUES ('975aab2453da11e68586000c293c07d6', '611021', '洛南县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('975c80ee53da11e68586000c293c07d6', '611022', '丹凤县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('975e48e053da11e68586000c293c07d6', '611023', '商南县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('9760375b53da11e68586000c293c07d6', '611024', '山阳县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('976274c553da11e68586000c293c07d6', '611025', '镇安县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('9764586153da11e68586000c293c07d6', '611026', '柞水县', null, null, '611000');
INSERT INTO `sys_area` VALUES ('97662e8553da11e68586000c293c07d6', '611100', '杨凌示范区', null, null, '610000');
INSERT INTO `sys_area` VALUES ('9767ea8d53da11e68586000c293c07d6', '611103', '杨凌区', null, null, '611100');
INSERT INTO `sys_area` VALUES ('9769f87f53da11e68586000c293c07d6', '620000', '甘肃省', null, '28', '0');
INSERT INTO `sys_area` VALUES ('976bc2d253da11e68586000c293c07d6', '620100', '兰州市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('976d887653da11e68586000c293c07d6', '620101', '市辖区', null, null, '620100');
INSERT INTO `sys_area` VALUES ('976fbbcc53da11e68586000c293c07d6', '620102', '城关区', null, null, '620101');
INSERT INTO `sys_area` VALUES ('9771bb2d53da11e68586000c293c07d6', '620103', '七里河区', null, null, '620101');
INSERT INTO `sys_area` VALUES ('9773975d53da11e68586000c293c07d6', '620104', '西固区', null, null, '620101');
INSERT INTO `sys_area` VALUES ('9775577353da11e68586000c293c07d6', '620105', '安宁区', null, null, '620101');
INSERT INTO `sys_area` VALUES ('97773aee53da11e68586000c293c07d6', '620111', '红古区', null, null, '620101');
INSERT INTO `sys_area` VALUES ('97794e5453da11e68586000c293c07d6', '620121', '永登县', null, null, '620100');
INSERT INTO `sys_area` VALUES ('977b2e1453da11e68586000c293c07d6', '620122', '皋兰县', null, null, '620100');
INSERT INTO `sys_area` VALUES ('977d5fd653da11e68586000c293c07d6', '620123', '榆中县', null, null, '620100');
INSERT INTO `sys_area` VALUES ('977f264253da11e68586000c293c07d6', '620200', '嘉峪关市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('9780e94c53da11e68586000c293c07d6', '620201', '市辖区', null, null, '620200');
INSERT INTO `sys_area` VALUES ('9782e4d453da11e68586000c293c07d6', '620300', '金昌市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('9784cfe253da11e68586000c293c07d6', '620301', '金川区', null, null, '620300');
INSERT INTO `sys_area` VALUES ('9786a4bd53da11e68586000c293c07d6', '620321', '永昌县', null, null, '620300');
INSERT INTO `sys_area` VALUES ('978874ca53da11e68586000c293c07d6', '620400', '白银市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('978a55bc53da11e68586000c293c07d6', '620401', '市辖区', null, null, '620400');
INSERT INTO `sys_area` VALUES ('978c352b53da11e68586000c293c07d6', '620402', '白银区', null, null, '620401');
INSERT INTO `sys_area` VALUES ('978e2b0053da11e68586000c293c07d6', '620403', '平川区', null, null, '620401');
INSERT INTO `sys_area` VALUES ('978ff27b53da11e68586000c293c07d6', '620421', '靖远县', null, null, '620400');
INSERT INTO `sys_area` VALUES ('9791b63853da11e68586000c293c07d6', '620422', '会宁县', null, null, '620400');
INSERT INTO `sys_area` VALUES ('9793837d53da11e68586000c293c07d6', '620423', '景泰县', null, null, '620400');
INSERT INTO `sys_area` VALUES ('979577b653da11e68586000c293c07d6', '620500', '天水市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97977c7c53da11e68586000c293c07d6', '620501', '麦积区', null, null, '620500');
INSERT INTO `sys_area` VALUES ('9799453653da11e68586000c293c07d6', '620502', '秦州区', null, null, '620500');
INSERT INTO `sys_area` VALUES ('979b0f7753da11e68586000c293c07d6', '620521', '清水县', null, null, '620500');
INSERT INTO `sys_area` VALUES ('979cea0d53da11e68586000c293c07d6', '620522', '秦安县', null, null, '620500');
INSERT INTO `sys_area` VALUES ('979ebb4553da11e68586000c293c07d6', '620523', '甘谷县', null, null, '620500');
INSERT INTO `sys_area` VALUES ('97a0696453da11e68586000c293c07d6', '620524', '武山县', null, null, '620500');
INSERT INTO `sys_area` VALUES ('97a2740953da11e68586000c293c07d6', '620525', '张家川回族自治县', null, null, '620500');
INSERT INTO `sys_area` VALUES ('97a459e253da11e68586000c293c07d6', '620600', '武威市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97a68e1953da11e68586000c293c07d6', '620601', '市辖区', null, null, '620600');
INSERT INTO `sys_area` VALUES ('97a88b1253da11e68586000c293c07d6', '620602', '凉州区', null, null, '620601');
INSERT INTO `sys_area` VALUES ('97aaab4f53da11e68586000c293c07d6', '620621', '民勤县', null, null, '620600');
INSERT INTO `sys_area` VALUES ('97ac9d2553da11e68586000c293c07d6', '620622', '古浪县', null, null, '620600');
INSERT INTO `sys_area` VALUES ('97b2bf3e53da11e68586000c293c07d6', '620623', '天祝藏族自治县', null, null, '620600');
INSERT INTO `sys_area` VALUES ('97b4a71053da11e68586000c293c07d6', '620700', '张掖市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97b6c70153da11e68586000c293c07d6', '620701', '市辖区', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97b8d56753da11e68586000c293c07d6', '620702', '甘州区', null, null, '620701');
INSERT INTO `sys_area` VALUES ('97bb2ad353da11e68586000c293c07d6', '620721', '肃南裕固族自治县', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97bd277f53da11e68586000c293c07d6', '620722', '民乐县', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97bfd2f453da11e68586000c293c07d6', '620723', '临泽县', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97c1e7a453da11e68586000c293c07d6', '620724', '高台县', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97c38f9a53da11e68586000c293c07d6', '620725', '山丹县', null, null, '620700');
INSERT INTO `sys_area` VALUES ('97c56c3453da11e68586000c293c07d6', '620800', '平凉市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97c75cbb53da11e68586000c293c07d6', '620801', '市辖区', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97c9c5d953da11e68586000c293c07d6', '620802', '崆峒区', null, null, '620801');
INSERT INTO `sys_area` VALUES ('97cb85ae53da11e68586000c293c07d6', '620821', '泾川县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97cda06953da11e68586000c293c07d6', '620822', '灵台县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97cf7d0d53da11e68586000c293c07d6', '620823', '崇信县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97d17e8453da11e68586000c293c07d6', '620824', '华亭县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97d33cf653da11e68586000c293c07d6', '620825', '庄浪县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97d4ffd353da11e68586000c293c07d6', '620826', '静宁县', null, null, '620800');
INSERT INTO `sys_area` VALUES ('97d6f0c153da11e68586000c293c07d6', '620900', '酒泉市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97d8edb653da11e68586000c293c07d6', '620901', '市辖区', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97daa87153da11e68586000c293c07d6', '620902', '肃州区', null, null, '620901');
INSERT INTO `sys_area` VALUES ('97dc96a953da11e68586000c293c07d6', '620921', '金塔县', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97de732253da11e68586000c293c07d6', '620922', '瓜洲县', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97e04d8753da11e68586000c293c07d6', '620923', '肃北蒙古族自治县', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97e2291153da11e68586000c293c07d6', '620924', '阿克塞哈萨克族自治县', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97e3c66653da11e68586000c293c07d6', '620981', '玉门市', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97e5913053da11e68586000c293c07d6', '620982', '敦煌市', null, null, '620900');
INSERT INTO `sys_area` VALUES ('97e775a453da11e68586000c293c07d6', '621000', '庆阳市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97e99b1d53da11e68586000c293c07d6', '621001', '市辖区', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97eb7b2b53da11e68586000c293c07d6', '621002', '西峰区', null, null, '621001');
INSERT INTO `sys_area` VALUES ('97ed2b8853da11e68586000c293c07d6', '621021', '庆城县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97eec77a53da11e68586000c293c07d6', '621022', '环县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97f0b47b53da11e68586000c293c07d6', '621023', '华池县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97f2988453da11e68586000c293c07d6', '621024', '合水县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97f4ba4453da11e68586000c293c07d6', '621025', '正宁县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97f6a3ab53da11e68586000c293c07d6', '621026', '宁县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97f8c8f653da11e68586000c293c07d6', '621027', '镇原县', null, null, '621000');
INSERT INTO `sys_area` VALUES ('97fab3e153da11e68586000c293c07d6', '621100', '定西市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('97fcc06753da11e68586000c293c07d6', '621101', '市辖区', null, null, '621100');
INSERT INTO `sys_area` VALUES ('97fe9f6753da11e68586000c293c07d6', '621102', '安定区', null, null, '621101');
INSERT INTO `sys_area` VALUES ('98008fac53da11e68586000c293c07d6', '621121', '通渭县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('980292ea53da11e68586000c293c07d6', '621122', '陇西县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('98044f6553da11e68586000c293c07d6', '621123', '渭源县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('9806099f53da11e68586000c293c07d6', '621124', '临洮县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('98080d6f53da11e68586000c293c07d6', '621125', '漳县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('980a1cd153da11e68586000c293c07d6', '621126', '岷县', null, null, '621100');
INSERT INTO `sys_area` VALUES ('980c369353da11e68586000c293c07d6', '621200', '陇南市', null, null, '620000');
INSERT INTO `sys_area` VALUES ('980df3e853da11e68586000c293c07d6', '621201', '武都区', null, null, '621200');
INSERT INTO `sys_area` VALUES ('980fc0ec53da11e68586000c293c07d6', '621221', '成县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('981205a953da11e68586000c293c07d6', '621222', '文县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('98154bd653da11e68586000c293c07d6', '621223', '宕昌县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('98196be553da11e68586000c293c07d6', '621224', '康县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('981fc29453da11e68586000c293c07d6', '621225', '西和县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('9821f70053da11e68586000c293c07d6', '621226', '礼县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('9823f4d253da11e68586000c293c07d6', '621227', '徽县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('98259cee53da11e68586000c293c07d6', '621228', '两当县', null, null, '621200');
INSERT INTO `sys_area` VALUES ('98274e2b53da11e68586000c293c07d6', '622900', '临夏回族自治州', null, null, '620000');
INSERT INTO `sys_area` VALUES ('982922a853da11e68586000c293c07d6', '622901', '临夏市', null, null, '622900');
INSERT INTO `sys_area` VALUES ('982af9e053da11e68586000c293c07d6', '622921', '临夏县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('982cd75953da11e68586000c293c07d6', '622922', '康乐县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('982e76e053da11e68586000c293c07d6', '622923', '永靖县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('9830441953da11e68586000c293c07d6', '622924', '广河县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('98322b9a53da11e68586000c293c07d6', '622925', '和政县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('9833d66c53da11e68586000c293c07d6', '622926', '东乡族自治县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('9835a67453da11e68586000c293c07d6', '622927', '积石山保安族东乡族撒拉族自治县', null, null, '622900');
INSERT INTO `sys_area` VALUES ('98379f7b53da11e68586000c293c07d6', '623000', '甘南藏族自治州', null, null, '620000');
INSERT INTO `sys_area` VALUES ('98396f8e53da11e68586000c293c07d6', '623001', '合作市', null, null, '623000');
INSERT INTO `sys_area` VALUES ('983b24bc53da11e68586000c293c07d6', '623021', '临潭县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('983cf51753da11e68586000c293c07d6', '623022', '卓尼县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('983ea71053da11e68586000c293c07d6', '623023', '舟曲县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('98404f8a53da11e68586000c293c07d6', '623024', '迭部县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('98427fb953da11e68586000c293c07d6', '623025', '玛曲县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('984454ae53da11e68586000c293c07d6', '623026', '碌曲县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('9846188753da11e68586000c293c07d6', '623027', '夏河县', null, null, '623000');
INSERT INTO `sys_area` VALUES ('9847dd0a53da11e68586000c293c07d6', '630000', '青海省', null, '29', '0');
INSERT INTO `sys_area` VALUES ('9849c4a553da11e68586000c293c07d6', '630100', '西宁市', null, null, '630000');
INSERT INTO `sys_area` VALUES ('984bacf053da11e68586000c293c07d6', '630101', '市辖区', null, null, '630100');
INSERT INTO `sys_area` VALUES ('984d7da353da11e68586000c293c07d6', '630102', '城东区', null, null, '630101');
INSERT INTO `sys_area` VALUES ('984f445153da11e68586000c293c07d6', '630103', '城中区', null, null, '630101');
INSERT INTO `sys_area` VALUES ('985124c853da11e68586000c293c07d6', '630104', '城西区', null, null, '630101');
INSERT INTO `sys_area` VALUES ('98533d8c53da11e68586000c293c07d6', '630105', '城北区', null, null, '630101');
INSERT INTO `sys_area` VALUES ('9854efc453da11e68586000c293c07d6', '630121', '大通回族土族自治县', null, null, '630100');
INSERT INTO `sys_area` VALUES ('9856e80753da11e68586000c293c07d6', '630122', '湟中县', null, null, '630100');
INSERT INTO `sys_area` VALUES ('9858fb9453da11e68586000c293c07d6', '630123', '湟源县', null, null, '630100');
INSERT INTO `sys_area` VALUES ('985b0fb253da11e68586000c293c07d6', '632100', '海东市', null, null, '630000');
INSERT INTO `sys_area` VALUES ('985cadb153da11e68586000c293c07d6', '632121', '平安县', null, null, '632100');
INSERT INTO `sys_area` VALUES ('985f097453da11e68586000c293c07d6', '632122', '民和回族土族自治县', null, null, '632100');
INSERT INTO `sys_area` VALUES ('9867554e53da11e68586000c293c07d6', '632123', '乐都区', null, null, '632100');
INSERT INTO `sys_area` VALUES ('987866a953da11e68586000c293c07d6', '632126', '互助土族自治县', null, null, '632100');
INSERT INTO `sys_area` VALUES ('9882b22853da11e68586000c293c07d6', '632127', '化隆回族自治县', null, null, '632100');
INSERT INTO `sys_area` VALUES ('9885df2953da11e68586000c293c07d6', '632128', '循化撒拉族自治县', null, null, '632100');
INSERT INTO `sys_area` VALUES ('988a3a8d53da11e68586000c293c07d6', '632200', '海北藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('988d1b9553da11e68586000c293c07d6', '632221', '门源回族自治县', null, null, '632200');
INSERT INTO `sys_area` VALUES ('988f44c953da11e68586000c293c07d6', '632222', '祁连县', null, null, '632200');
INSERT INTO `sys_area` VALUES ('98918b8a53da11e68586000c293c07d6', '632223', '海晏县', null, null, '632200');
INSERT INTO `sys_area` VALUES ('98955c1353da11e68586000c293c07d6', '632224', '刚察县', null, null, '632200');
INSERT INTO `sys_area` VALUES ('989752b853da11e68586000c293c07d6', '632300', '黄南藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('989a573253da11e68586000c293c07d6', '632321', '同仁县', null, null, '632300');
INSERT INTO `sys_area` VALUES ('989da72053da11e68586000c293c07d6', '632322', '尖扎县', null, null, '632300');
INSERT INTO `sys_area` VALUES ('98a08d6553da11e68586000c293c07d6', '632323', '泽库县', null, null, '632300');
INSERT INTO `sys_area` VALUES ('98aefecf53da11e68586000c293c07d6', '632324', '河南蒙古族自治县', null, null, '632300');
INSERT INTO `sys_area` VALUES ('98b2b50e53da11e68586000c293c07d6', '632500', '海南藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('98b7460d53da11e68586000c293c07d6', '632521', '共和县', null, null, '632500');
INSERT INTO `sys_area` VALUES ('98b9caf653da11e68586000c293c07d6', '632522', '同德县', null, null, '632500');
INSERT INTO `sys_area` VALUES ('98bb9f5153da11e68586000c293c07d6', '632523', '贵德县', null, null, '632500');
INSERT INTO `sys_area` VALUES ('98bd40f853da11e68586000c293c07d6', '632524', '兴海县', null, null, '632500');
INSERT INTO `sys_area` VALUES ('98bf205053da11e68586000c293c07d6', '632525', '贵南县', null, null, '632500');
INSERT INTO `sys_area` VALUES ('98c1387353da11e68586000c293c07d6', '632600', '果洛藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('98c2f5e253da11e68586000c293c07d6', '632621', '玛沁县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98c4e78f53da11e68586000c293c07d6', '632622', '班玛县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98c6a34353da11e68586000c293c07d6', '632623', '甘德县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98c8b05053da11e68586000c293c07d6', '632624', '达日县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98ca836953da11e68586000c293c07d6', '632625', '久治县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98cc40a453da11e68586000c293c07d6', '632626', '玛多县', null, null, '632600');
INSERT INTO `sys_area` VALUES ('98ce103c53da11e68586000c293c07d6', '632700', '玉树藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('98cff10b53da11e68586000c293c07d6', '632721', '玉树县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98d1e0ec53da11e68586000c293c07d6', '632722', '杂多县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98d3821753da11e68586000c293c07d6', '632723', '称多县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98d5506c53da11e68586000c293c07d6', '632724', '治多县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98d724e053da11e68586000c293c07d6', '632725', '囊谦县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98d91b2753da11e68586000c293c07d6', '632726', '曲麻莱县', null, null, '632700');
INSERT INTO `sys_area` VALUES ('98dad60153da11e68586000c293c07d6', '632800', '海西蒙古族藏族自治州', null, null, '630000');
INSERT INTO `sys_area` VALUES ('98dced1f53da11e68586000c293c07d6', '632801', '格尔木市', null, null, '632800');
INSERT INTO `sys_area` VALUES ('98de92d553da11e68586000c293c07d6', '632802', '德令哈市', null, null, '632800');
INSERT INTO `sys_area` VALUES ('98e041bb53da11e68586000c293c07d6', '632821', '乌兰县', null, null, '632800');
INSERT INTO `sys_area` VALUES ('98e1fef353da11e68586000c293c07d6', '632822', '都兰县', null, null, '632800');
INSERT INTO `sys_area` VALUES ('98e3d1fb53da11e68586000c293c07d6', '632823', '天峻县', null, null, '632800');
INSERT INTO `sys_area` VALUES ('98e5b72653da11e68586000c293c07d6', '640000', '宁夏回族自治区', null, '30', '0');
INSERT INTO `sys_area` VALUES ('98e81a4853da11e68586000c293c07d6', '640100', '银川市', null, null, '640000');
INSERT INTO `sys_area` VALUES ('98ea0a4353da11e68586000c293c07d6', '640104', '兴庆区', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98ebe68153da11e68586000c293c07d6', '640105', '西夏区', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98eda2c553da11e68586000c293c07d6', '640106', '金凤区', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98efa94953da11e68586000c293c07d6', '640121', '永宁县', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98f19fda53da11e68586000c293c07d6', '640122', '贺兰县', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98f3f8ff53da11e68586000c293c07d6', '640181', '灵武市', null, null, '640100');
INSERT INTO `sys_area` VALUES ('98f5f1dd53da11e68586000c293c07d6', '640200', '石嘴山市', null, null, '640000');
INSERT INTO `sys_area` VALUES ('98f7b0d753da11e68586000c293c07d6', '640202', '大武口区', null, null, '640200');
INSERT INTO `sys_area` VALUES ('98f96ce153da11e68586000c293c07d6', '640205', '惠农县', null, null, '640200');
INSERT INTO `sys_area` VALUES ('98fb38bd53da11e68586000c293c07d6', '640221', '平罗县', null, null, '640200');
INSERT INTO `sys_area` VALUES ('98fd0b6253da11e68586000c293c07d6', '640300', '吴忠市', null, null, '640000');
INSERT INTO `sys_area` VALUES ('98feca6b53da11e68586000c293c07d6', '640301', '红寺堡区', null, null, '640300');
INSERT INTO `sys_area` VALUES ('9900be6b53da11e68586000c293c07d6', '640302', '利通区', null, null, '640300');
INSERT INTO `sys_area` VALUES ('990283b753da11e68586000c293c07d6', '640323', '盐池县', null, null, '640300');
INSERT INTO `sys_area` VALUES ('9904638153da11e68586000c293c07d6', '640324', '同心县', null, null, '640300');
INSERT INTO `sys_area` VALUES ('99063ee253da11e68586000c293c07d6', '640381', '青铜峡市', null, null, '640300');
INSERT INTO `sys_area` VALUES ('9908191253da11e68586000c293c07d6', '640400', '固原市', null, null, '640000');
INSERT INTO `sys_area` VALUES ('9909f64b53da11e68586000c293c07d6', '640401', '市辖区', null, null, '640400');
INSERT INTO `sys_area` VALUES ('990ba84f53da11e68586000c293c07d6', '640402', '原州区', null, null, '640400');
INSERT INTO `sys_area` VALUES ('990d80d153da11e68586000c293c07d6', '640422', '西吉县', null, null, '640400');
INSERT INTO `sys_area` VALUES ('990f3e9553da11e68586000c293c07d6', '640423', '隆德县', null, null, '640400');
INSERT INTO `sys_area` VALUES ('9910fef353da11e68586000c293c07d6', '640424', '泾源县', null, null, '640400');
INSERT INTO `sys_area` VALUES ('9912e27e53da11e68586000c293c07d6', '640425', '彭阳县', null, null, '640400');
INSERT INTO `sys_area` VALUES ('9914bce653da11e68586000c293c07d6', '640500', '中卫市', null, null, '640000');
INSERT INTO `sys_area` VALUES ('99171b9c53da11e68586000c293c07d6', '640501', '市辖区', null, null, '640500');
INSERT INTO `sys_area` VALUES ('9918dd7b53da11e68586000c293c07d6', '640502', '沙坡头区', null, null, '640500');
INSERT INTO `sys_area` VALUES ('991ae30153da11e68586000c293c07d6', '640521', '中宁县', null, null, '640500');
INSERT INTO `sys_area` VALUES ('991cc07c53da11e68586000c293c07d6', '640522', '海原县', null, null, '640500');
INSERT INTO `sys_area` VALUES ('991e8f9053da11e68586000c293c07d6', '650000', '新疆维吾尔自治区', null, '31', '0');
INSERT INTO `sys_area` VALUES ('9920575d53da11e68586000c293c07d6', '650100', '乌鲁木齐市', null, null, '650000');
INSERT INTO `sys_area` VALUES ('9922356c53da11e68586000c293c07d6', '650101', '市辖区', null, null, '650100');
INSERT INTO `sys_area` VALUES ('9924360753da11e68586000c293c07d6', '650102', '天山区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('99261bc353da11e68586000c293c07d6', '650103', '沙依巴克区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('9927d95553da11e68586000c293c07d6', '650104', '新市区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('9929b04353da11e68586000c293c07d6', '650105', '水磨沟区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('992b8bcb53da11e68586000c293c07d6', '650106', '头屯河区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('992d38a153da11e68586000c293c07d6', '650107', '达坂城区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('992f23fe53da11e68586000c293c07d6', '650108', '东山区', null, null, '650101');
INSERT INTO `sys_area` VALUES ('9931599653da11e68586000c293c07d6', '650121', '乌鲁木齐县', null, null, '650100');
INSERT INTO `sys_area` VALUES ('9933174353da11e68586000c293c07d6', '650200', '克拉玛依市', null, null, '650000');
INSERT INTO `sys_area` VALUES ('9935426553da11e68586000c293c07d6', '650201', '市辖区', null, null, '650200');
INSERT INTO `sys_area` VALUES ('9937c88d53da11e68586000c293c07d6', '650202', '独山子区', null, null, '650201');
INSERT INTO `sys_area` VALUES ('9939693253da11e68586000c293c07d6', '650203', '克拉玛依区', null, null, '650201');
INSERT INTO `sys_area` VALUES ('993b454653da11e68586000c293c07d6', '650204', '白碱滩区', null, null, '650201');
INSERT INTO `sys_area` VALUES ('993d9fdf53da11e68586000c293c07d6', '650205', '乌尔禾区', null, null, '650201');
INSERT INTO `sys_area` VALUES ('993f634253da11e68586000c293c07d6', '652100', '吐鲁番地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('9941022e53da11e68586000c293c07d6', '652101', '吐鲁番市', null, null, '652100');
INSERT INTO `sys_area` VALUES ('99430c1853da11e68586000c293c07d6', '652122', '鄯善县', null, null, '652100');
INSERT INTO `sys_area` VALUES ('9946c15453da11e68586000c293c07d6', '652123', '托克逊县', null, null, '652100');
INSERT INTO `sys_area` VALUES ('994896f153da11e68586000c293c07d6', '652200', '哈密地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('994a87a753da11e68586000c293c07d6', '652201', '哈密市', null, null, '652200');
INSERT INTO `sys_area` VALUES ('994c4dcc53da11e68586000c293c07d6', '652222', '巴里坤哈萨克自治县', null, null, '652200');
INSERT INTO `sys_area` VALUES ('994e14ad53da11e68586000c293c07d6', '652223', '伊吾县', null, null, '652200');
INSERT INTO `sys_area` VALUES ('994fe2e153da11e68586000c293c07d6', '652300', '昌吉回族自治州', null, null, '650000');
INSERT INTO `sys_area` VALUES ('9951a32153da11e68586000c293c07d6', '652301', '昌吉市', null, null, '652300');
INSERT INTO `sys_area` VALUES ('9953825c53da11e68586000c293c07d6', '652302', '阜康市', null, null, '652300');
INSERT INTO `sys_area` VALUES ('99557a1153da11e68586000c293c07d6', '652303', '米泉市', null, null, '652300');
INSERT INTO `sys_area` VALUES ('9957684e53da11e68586000c293c07d6', '652323', '呼图壁县', null, null, '652300');
INSERT INTO `sys_area` VALUES ('9959393c53da11e68586000c293c07d6', '652324', '玛纳斯县', null, null, '652300');
INSERT INTO `sys_area` VALUES ('995adf1953da11e68586000c293c07d6', '652325', '奇台县', null, null, '652300');
INSERT INTO `sys_area` VALUES ('995ccf2653da11e68586000c293c07d6', '652327', '吉木萨尔县', null, null, '652300');
INSERT INTO `sys_area` VALUES ('995eb5ba53da11e68586000c293c07d6', '652328', '木垒哈萨克自治县', null, null, '652300');
INSERT INTO `sys_area` VALUES ('99607cc553da11e68586000c293c07d6', '652700', '博尔塔拉蒙古自治州', null, null, '650000');
INSERT INTO `sys_area` VALUES ('996229a753da11e68586000c293c07d6', '652701', '博乐市', null, null, '652700');
INSERT INTO `sys_area` VALUES ('9963f31253da11e68586000c293c07d6', '652722', '精河县', null, null, '652700');
INSERT INTO `sys_area` VALUES ('9965ff6953da11e68586000c293c07d6', '652723', '温泉县', null, null, '652700');
INSERT INTO `sys_area` VALUES ('9967c3bf53da11e68586000c293c07d6', '652800', '巴音郭楞蒙古自治州', null, null, '650000');
INSERT INTO `sys_area` VALUES ('9969c6c153da11e68586000c293c07d6', '652801', '库尔勒市', null, null, '652800');
INSERT INTO `sys_area` VALUES ('996b867553da11e68586000c293c07d6', '652822', '轮台县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('996e2c8353da11e68586000c293c07d6', '652823', '尉犁县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('9970664f53da11e68586000c293c07d6', '652824', '若羌县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('99727a5053da11e68586000c293c07d6', '652825', '且末县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('9975150b53da11e68586000c293c07d6', '652826', '焉耆回族自治县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('997734b053da11e68586000c293c07d6', '652827', '和静县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('997978fa53da11e68586000c293c07d6', '652828', '和硕县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('997b996353da11e68586000c293c07d6', '652829', '博湖县', null, null, '652800');
INSERT INTO `sys_area` VALUES ('997dc1e053da11e68586000c293c07d6', '652900', '阿克苏地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('997f73a953da11e68586000c293c07d6', '652901', '阿克苏市', null, null, '652900');
INSERT INTO `sys_area` VALUES ('9981869253da11e68586000c293c07d6', '652922', '温宿县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('9983a0df53da11e68586000c293c07d6', '652923', '库车县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('9985962e53da11e68586000c293c07d6', '652924', '沙雅县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('998795a353da11e68586000c293c07d6', '652925', '新和县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('9989a6ec53da11e68586000c293c07d6', '652926', '拜城县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('998b6d5953da11e68586000c293c07d6', '652927', '乌什县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('998db3da53da11e68586000c293c07d6', '652928', '阿瓦提县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('998f945b53da11e68586000c293c07d6', '652929', '柯坪县', null, null, '652900');
INSERT INTO `sys_area` VALUES ('9991c42653da11e68586000c293c07d6', '653000', '克孜勒苏柯尔克孜自治州', null, null, '650000');
INSERT INTO `sys_area` VALUES ('999411e353da11e68586000c293c07d6', '653001', '阿图什市', null, null, '653000');
INSERT INTO `sys_area` VALUES ('9995e92b53da11e68586000c293c07d6', '653022', '阿克陶县', null, null, '653000');
INSERT INTO `sys_area` VALUES ('9997b47153da11e68586000c293c07d6', '653023', '阿合奇县', null, null, '653000');
INSERT INTO `sys_area` VALUES ('9999702e53da11e68586000c293c07d6', '653024', '乌恰县', null, null, '653000');
INSERT INTO `sys_area` VALUES ('999b363353da11e68586000c293c07d6', '653100', '喀什地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('999ceddb53da11e68586000c293c07d6', '653101', '喀什市', null, null, '653100');
INSERT INTO `sys_area` VALUES ('999ebbf153da11e68586000c293c07d6', '653121', '疏附县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a09fab53da11e68586000c293c07d6', '653122', '疏勒县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a2847953da11e68586000c293c07d6', '653123', '英吉沙县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a43b7653da11e68586000c293c07d6', '653124', '泽普县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a62b9d53da11e68586000c293c07d6', '653125', '莎车县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a80c2853da11e68586000c293c07d6', '653126', '叶城县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99a9b27c53da11e68586000c293c07d6', '653127', '麦盖提县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99ab7bf853da11e68586000c293c07d6', '653128', '岳普湖县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99ad461b53da11e68586000c293c07d6', '653129', '伽师县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99af16b153da11e68586000c293c07d6', '653130', '巴楚县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99b0d05d53da11e68586000c293c07d6', '653131', '塔什库尔干塔吉克自治县', null, null, '653100');
INSERT INTO `sys_area` VALUES ('99b31e6953da11e68586000c293c07d6', '653200', '和田地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('99b6627553da11e68586000c293c07d6', '653201', '和田市', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99b839b153da11e68586000c293c07d6', '653221', '和田县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99b9ea2053da11e68586000c293c07d6', '653222', '墨玉县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99bba20353da11e68586000c293c07d6', '653223', '皮山县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99bd4ad453da11e68586000c293c07d6', '653224', '洛浦县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99bfb62953da11e68586000c293c07d6', '653225', '策勒县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99c1869353da11e68586000c293c07d6', '653226', '于田县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99c3ba8e53da11e68586000c293c07d6', '653227', '民丰县', null, null, '653200');
INSERT INTO `sys_area` VALUES ('99c589a053da11e68586000c293c07d6', '654000', '伊犁哈萨克自治州', null, null, '650000');
INSERT INTO `sys_area` VALUES ('99c750f053da11e68586000c293c07d6', '654002', '伊宁市', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99c919c553da11e68586000c293c07d6', '654003', '奎屯市', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99cacb8753da11e68586000c293c07d6', '654021', '伊宁县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99ccbc0d53da11e68586000c293c07d6', '654022', '察布查尔锡伯自治县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99ce812753da11e68586000c293c07d6', '654023', '霍城县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99d064ad53da11e68586000c293c07d6', '654024', '巩留县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99d2346953da11e68586000c293c07d6', '654025', '新源县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99d4452a53da11e68586000c293c07d6', '654026', '昭苏县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99d68e7353da11e68586000c293c07d6', '654027', '特克斯县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99d86c0e53da11e68586000c293c07d6', '654028', '尼勒克县', null, null, '654000');
INSERT INTO `sys_area` VALUES ('99da64ee53da11e68586000c293c07d6', '654200', '塔城地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('99dc039853da11e68586000c293c07d6', '654201', '塔城市', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99de04f153da11e68586000c293c07d6', '654202', '乌苏市', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99dfb8c753da11e68586000c293c07d6', '654221', '额敏县', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99e1a00153da11e68586000c293c07d6', '654223', '沙湾县', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99e36e7253da11e68586000c293c07d6', '654224', '托里县', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99e5405053da11e68586000c293c07d6', '654225', '裕民县', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99e7126653da11e68586000c293c07d6', '654226', '和布克赛尔蒙古自治县', null, null, '654200');
INSERT INTO `sys_area` VALUES ('99e8d2d953da11e68586000c293c07d6', '654300', '阿勒泰地区', null, null, '650000');
INSERT INTO `sys_area` VALUES ('99eaaa5053da11e68586000c293c07d6', '654301', '阿勒泰市', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99ec6f8353da11e68586000c293c07d6', '654321', '布尔津县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99ee566453da11e68586000c293c07d6', '654322', '富蕴县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99f0442c53da11e68586000c293c07d6', '654323', '福海县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99f2910153da11e68586000c293c07d6', '654324', '哈巴河县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99f4582253da11e68586000c293c07d6', '654325', '青河县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99f615e553da11e68586000c293c07d6', '654326', '吉木乃县', null, null, '654300');
INSERT INTO `sys_area` VALUES ('99f7de1653da11e68586000c293c07d6', '659000', '省直辖行政单位', null, null, '650000');
INSERT INTO `sys_area` VALUES ('99f9993153da11e68586000c293c07d6', '659001', '石河子市', null, null, '659000');
INSERT INTO `sys_area` VALUES ('99fb5b6553da11e68586000c293c07d6', '659002', '阿拉尔市', null, null, '659000');
INSERT INTO `sys_area` VALUES ('99fd335253da11e68586000c293c07d6', '659003', '图木舒克市', null, null, '659000');
INSERT INTO `sys_area` VALUES ('99feeed353da11e68586000c293c07d6', '659004', '五家渠市', null, null, '659000');
INSERT INTO `sys_area` VALUES ('9a00f32253da11e68586000c293c07d6', '990000', '新疆建设兵团', null, '32', '0');
INSERT INTO `sys_area` VALUES ('9a02b9df53da11e68586000c293c07d6', '990100', '第一师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a04935f53da11e68586000c293c07d6', '990200', '第二师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a065d1d53da11e68586000c293c07d6', '990300', '第三师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a08144b53da11e68586000c293c07d6', '990400', '第四师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a0a097153da11e68586000c293c07d6', '990500', '第五师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a0bd20653da11e68586000c293c07d6', '990600', '第六师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a0e181853da11e68586000c293c07d6', '990700', '第七师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a0fccde53da11e68586000c293c07d6', '990800', '第八师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a1190ed53da11e68586000c293c07d6', '990900', '第九师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a1406a053da11e68586000c293c07d6', '991000', '第十师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a15e9e153da11e68586000c293c07d6', '991100', '建工师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a17f82153da11e68586000c293c07d6', '991200', '第十二师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a19b6c153da11e68586000c293c07d6', '991300', '第十三师', null, null, '990000');
INSERT INTO `sys_area` VALUES ('9a1be8cf53da11e68586000c293c07d6', '991400', '第十四师', null, null, '990000');

-- ----------------------------
-- Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(100) NOT NULL COMMENT '配置项标识',
  `config_name` varchar(100) NOT NULL COMMENT '配置项名称',
  `config_value` varchar(10240) DEFAULT NULL COMMENT '值',
  `config_type` char(2) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `status` char(1) DEFAULT '1' COMMENT '启用状态：0 未启用 1 启用',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `edit_enable` char(1) DEFAULT NULL COMMENT '是否可编辑',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `ak_uk_sys_config_code` (`code`) USING BTREE,
  UNIQUE KEY `idx_sys_config_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES ('0a9f135dc1d6492f94000d72dfa52242', 'invest_unpay_max', '当日用户最大未支付投资订单数', '20', '2', '2016-08-14 14:27:53', '1', '当日用户最大未支付投资订单数，包括支付超时、待支付订单', '1');
INSERT INTO `sys_config` VALUES ('0c56bcda11494b64a7361d2773a9c002', 'borrow_most_add_apr', '最高加息利率', '3', '2', '2016-08-10 16:59:01', '1', '最高加息利率：2%', '1');
INSERT INTO `sys_config` VALUES ('1', 'manage_url', '管理平台地址', 'http://172.16.90.30:8081', '2', '2016-06-22 11:45:53', '1', '网站后台页面访问地址，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('10', 'web_index_sina_microblog', '新浪微博', 'http://weibo.com/erongdu', '1', '2016-06-22 11:45:53', '1', '平台新浪微博（关注我们）', '1');
INSERT INTO `sys_config` VALUES ('100', 'account_code', '账户编号', '001', '2', '2016-07-13 11:45:53', '1', '子账户类型： 001-互金；002-众筹', '0');
INSERT INTO `sys_config` VALUES ('101', 'auth_service_type', '授权业务类型', '2', '2', '2016-07-22 13:38:33', '1', '汇付2 双乾2,3,5 联动1 兴业2,3', '0');
INSERT INTO `sys_config` VALUES ('102', 'mobile_url', '移动端平台地址', 'http://172.16.90.30:8088', '1', '2016-12-20 18:28:28', '1', '移动端平台地址', '1');
INSERT INTO `sys_config` VALUES ('12', 'address', '地址', '上海市嘉定区陈翔路88号6幢2楼B区2171', '1', '2016-06-22 11:45:53', '1', '用于网站显示公司地址', '1');
INSERT INTO `sys_config` VALUES ('13', 'web_index_copyright', '网站CpoyRight', 'Copyright@2016 杭州融都科技股份有限公司 版权所有', '1', '2016-06-22 11:45:53', '1', '用于网站显示网站版权', '1');
INSERT INTO `sys_config` VALUES ('14', 'web_record_number', '网站备案号', '沪ICP备15011786号', '1', '2016-06-22 11:45:53', '1', '用于网站底部显示网站ICP备案号', '1');
INSERT INTO `sys_config` VALUES ('15', 'web_meta_keywords', '网站关键词', '互金3.0', '1', '2016-06-22 11:45:53', '1', '网站关键词用于SEO，使网站更易被搜索引擎收录', '1');
INSERT INTO `sys_config` VALUES ('155', 'vip_growth_rate', 'vip成长系数', '1', '2', '2016-08-28 17:39:30', '1', 'vip成长系数(投资金额*该值)', '1');
INSERT INTO `sys_config` VALUES ('16', 'web_meta_description', '网站描述', '互金3.0', '1', '2016-06-22 11:45:53', '1', '网站描述用于SEO，使网站更易被搜索引擎收录', '1');
INSERT INTO `sys_config` VALUES ('17', 'ftl_dir', 'freemarker的模板地址', '/WEB-INF/html_default/', '1', '2016-06-22 11:45:53', '1', '用于代码读取freemarker的模板目录', '0');
INSERT INTO `sys_config` VALUES ('18', 'theme_dir', '模板目录', '/themes/theme_default', '1', '2016-06-22 11:45:53', '1', '用于代码读取js的模板目录', '0');
INSERT INTO `sys_config` VALUES ('19', 'admin_account', '系统管理员账户Id', '1', '2', '2016-06-22 11:45:53', '1', '资金记录中，用于记录管理员账户Id', '0');
INSERT INTO `sys_config` VALUES ('1ac934dffb8b4846ab66958bc67d6f09', 'borrow_most_apr', '最高借款利率', '24', '2', '2016-06-29 11:01:15', '1', '最高借款利率：24%', '1');
INSERT INTO `sys_config` VALUES ('2', 'web_url', '网站网址', 'http://172.16.90.30:8080', '1', '2016-06-22 11:45:53', '1', 'http://172.16.88.13，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('200547df6a154a6984dbe0ffc4aa6831', 'ufx_public_key', '商户公钥', '308189028181009a7c39a1eeea708c2c4c763dde611ba94c135c7bdc5dda8a5b0855c92ca092ec8799c4679951e1258bfba3a5961d7fdaf90df0cdc0ea814f67d84de97d81bb5f30b75891fed8e38fa305a369ee51b819ca443ad08e2a8be946fb9c787a4aa40132dd6db11592662808f24c1e97302c3f8957e03e8f20d031bc5da8a893072a0d0203010001', '2', '2016-07-11 10:21:47', '1', 'UFX商户公钥', '0');
INSERT INTO `sys_config` VALUES ('200547df6a154a6984dbe0ffc4aa6832', 'ufx_private_key', '商户私钥', '30820275020100300d06092a864886f70d01010105000482025f3082025b020100028181008e8eebd800a1e49b9b97572d04036e49c98cbf1df4daae387a4bfb71b7583f6668dcc4ea425cd56bdceeb3b81376f30eb48ac279063395a1101e4d26c73ead57c544e085fa9216c2336db1ceefc8c27b9bef3db1ba5af4dc282a4616ade6a4559281d1b246bb4c3aeffefd8b4aedef40df5b4bd91246836159c70e4c17df0a6702030100010281803f3c78b39d3c331b2f7e87860964b83474fa4100b9cf933eac492e25784de5f9b8844db9186cf3a2e022e687bde2a2d8a00f794a940ca7d005455bb5dae61b9fccbeddb0050ef3270b61e0a23c6141f4ba00065ef0568a93684efbad996bf3cb4e64ee4c40c3537c480f2fcbbc18cfb8e085d46e035c86096e6cbb5bcede69d9024100c9b8b0cf8b2f51707c43981494b71e3dc2eb8d134d103be6f95de627f9c57a21b618c487566671565ccdd30dd3e81a70165478a1daac51356829da2d1d2af91b024100b4eadb2d006e1eb10dba77634a59c950aaa4e18720f4ff1fe6e046c6fcd1857086273aa6347c29b815062418fd3687bd39943dd0641cb0614961ded8f5e934a502405eadb90f309cbcf4b6de62fb05f20cd7a02d3ec1d861ce3bd2bf27cf8b840c96d60939f4ebb09971978d638a11f3d6b4d5464078775279ab3f80a54e976a1c11024051102e96d26b851b0630b7cfe87fa4a19d64759d2b9a3b3a5b23c0f2e653273c4d1004ca7617f045883e112c9540babf81ae37a849434590c1e5b589775e3ff502404ebc03e083a502520c4947c3d6c6cdd112e3279e9132bce7176b117d0d02c26c762e90c0dfa328b148a2076170e0025be693566902d4bd430bf4160e7fb54262', '2', '2016-07-11 10:21:47', '1', 'UFX商户私钥', '0');
INSERT INTO `sys_config` VALUES ('200547df6a154a6984dbe0ffc4aa6833', 'ufx_customerId', 'UFX商户号', '01160708462', '2', '2016-06-14 10:21:47', '1', 'UFX商户号', '0');
INSERT INTO `sys_config` VALUES ('200547df6a154a6984dbe0ffc4aa6834', 'ufx_url', 'UFX生产环境路径', 'http://ufx.web.rdtest.cn/ufx/main.html', '2', '2016-07-11 10:21:47', '1', 'UFX生产环境路径，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('200547df6a154a6984dbe0ffc4aa6835', 'ufx_test_url', 'UFX测试环境路径', 'http://ufx.web.rdtest.cn/ufx/main.html', '2', '2016-07-11 10:21:47', '1', 'UFX测试环境路径，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('22e7d6a4badc11e6b0fefa163e2d23ab', 'tpp_service_name', '第三方支付（银行存管）服务类型', 'ufx', '1', '2016-12-05 19:15:32', '1', null, '0');
INSERT INTO `sys_config` VALUES ('230e42c900d249fcad4177a941cfb41c', 'project_time_limit_max_year', '项目最长年限', '4', '1', '2016-11-02 16:59:22', '1', '项目最长年限', '0');
INSERT INTO `sys_config` VALUES ('2340af41644b4134a47ebc3b7bda1465', 'sign_id', '签名商户的id', '1111563517', '2', '2016-07-19 11:40:16', '1', '协议有关的签名，1111563517e签宝id，38814918D6336888E05311016B0A0B19安心签id', '1');
INSERT INTO `sys_config` VALUES ('2340af41644b4134a47ebc3b7bda1577', 'sign_secret', '签名商户的秘钥', '95439b0863c241c63a861b87d1e647b7', '2', '2016-07-19 11:43:07', '1', '协议有关的签名，e签宝需要，安心签不需要 \n95439b0863c241c63a861b87d1e647b7--e签报', '1');
INSERT INTO `sys_config` VALUES ('26', 'cash_single_day_time_limit', '提现-单日提现次数限制', '3', '2', '2016-06-22 11:45:53', '1', '提现-单日提现次数限制：5次', '1');
INSERT INTO `sys_config` VALUES ('27', 'cash_single_max_amount_limit', '提现-单笔提现额度限制', '5000000', '2', '2016-06-22 11:45:53', '1', '单笔提现最大额度：5000000元', '1');
INSERT INTO `sys_config` VALUES ('28', 'cash_single_day_max_amount_limit', '提现-单日提现额度限制', '5000000', '2', '2016-06-22 11:45:53', '1', '单日提现额度：5000000元', '1');
INSERT INTO `sys_config` VALUES ('2a0b7b78775d422d85633766ef5f33ca', 'cash_fee_platform_advance', '提现手续费是否平台垫付', '0', '2', '2016-10-20 09:59:29', '1', '1：平台垫付\n0：个人支付', '1');
INSERT INTO `sys_config` VALUES ('2c1f361a3587405bae46fa23a16e1fb8', 'borrow_lowest_account', '最低借款金额', '300', '2', '2016-06-29 10:49:46', '1', '最低借款金额：300元', '1');
INSERT INTO `sys_config` VALUES ('2c388d35be3e41c081b96e830a4ef621', 'invest_timeout', '投资订单有效时间', '10', '2', '2016-08-17 11:26:40', '1', '单位：分钟', '1');
INSERT INTO `sys_config` VALUES ('32', 'overdue_fee', '逾期罚息利率', '0.0004', '2', '2016-06-22 11:45:53', '1', '用于计算逾期需要支付的手续费， 此处值为真实值,不加% 号', '1');
INSERT INTO `sys_config` VALUES ('34f4267f8cb642daa1a9dbf481a87b38', 'bond_no_init_value', '债权项目编号初始值', '2000', '2', '2016-11-07 16:21:30', '1', '债权项目编号初始值', '1');
INSERT INTO `sys_config` VALUES ('37', 'email_port', '邮箱地址端口号', '25', '3', '2016-06-22 11:45:53', '1', '邮箱地址端口号', '1');
INSERT INTO `sys_config` VALUES ('38', 'email_host', 'SMTP服务器', 'smtp.erongdu.com', '3', '2016-06-22 11:45:53', '1', 'SMTP服务器', '1');
INSERT INTO `sys_config` VALUES ('3819577e6d0211e6b37f000c293c07d6', 'tppName', '第三方名称', 'chinapnr', '1', '2016-08-28 17:31:37', '1', '平台使用的第三方名称，chinapnr表示汇付天下；请勿随意更改（PS:若此值为汇付天下，则将影响业务授权、债权转让只能全额转让和全额受让）', '1');
INSERT INTO `sys_config` VALUES ('4', 'image_server_url', '图片服务器地址', 'http://172.16.90.30:8083', '1', '2016-06-22 11:45:53', '1', '图片服务器，请不要在地址后面加/', '1');
INSERT INTO `sys_config` VALUES ('40', 'email_email', '邮箱地址', 'hj3@erongdu.com', '3', '2016-06-22 11:45:53', '1', '发送系统邮件的邮箱帐号', '1');
INSERT INTO `sys_config` VALUES ('41', 'email_pwd', '邮箱密码', 'Erongdu@1234', '3', '2016-06-22 11:45:53', '1', '发送系统邮件的邮箱密码', '1');
INSERT INTO `sys_config` VALUES ('42', 'email_from', '发件人Email', 'test@erongdu.com', '3', '2016-06-22 11:45:53', '1', '发送系统邮件的邮箱帐号', '1');
INSERT INTO `sys_config` VALUES ('43', 'email_from_name', '发件人昵称或姓名', '互金3.0--您信赖的投资平台', '3', '2016-06-22 11:45:53', '1', '发送系统邮件的昵称或姓名', '1');
INSERT INTO `sys_config` VALUES ('431ebc8412e84a6ba4a9c6755f469947', 'recharge_max_amount', '单笔充值最大金额', '100000000', '2', '2016-09-22 20:41:53', '1', '单笔充值最大金额', '1');
INSERT INTO `sys_config` VALUES ('43447301d95d4ef5bcd16c05199babdb', 'cash_min_amount', '最小提现金额', '10', '2', '2016-08-10 21:29:34', '1', '最小提现金额：10元', '1');
INSERT INTO `sys_config` VALUES ('44', 'sms_username', '短信接口用户名', 'Erongdu3', '4', '2016-06-22 11:45:53', '1', '短信运营商提供用户名用来发送短信', '1');
INSERT INTO `sys_config` VALUES ('45', 'sms_password', '短信接口密码', 'Erongdu888', '4', '2016-06-22 11:45:53', '1', '短信运营商提供密码用来发送短信', '1');
INSERT INTO `sys_config` VALUES ('46', 'sms_notice', '短信调用地址', 'http://www.ztsms.cn/sendNSms.do', '4', '2016-06-22 11:45:53', '1', '短信调用地址用来发送短信', '1');
INSERT INTO `sys_config` VALUES ('47', 'sms_use_url', '查询短信可用条数接口', 'http://sendsms.irdsms.com/sent.html', '1', '2016-06-22 11:45:53', '1', '查询短信可用条数接口，请不要在地址后面加/', '1');
INSERT INTO `sys_config` VALUES ('477260768c7a4e478bf588a4e0a081ff', 'days_of_year', '每年?天', '365', '2', '2016-07-21 09:55:02', '1', '表示一年360天，每月按照30天计算，用于还款时计息的计算', '1');
INSERT INTO `sys_config` VALUES ('48', 'sms_used_url', '查询短信已用条数接口', 'http://sendsms.irdsms.com/sent.html', '3', '2016-06-22 11:45:53', '1', '查询短信已用条数接口，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('49', 'verify_code_time', '短信、语音验证码有效期', '5', '5', '2016-06-22 11:45:53', '1', '验证码有效期，单位为分', '1');
INSERT INTO `sys_config` VALUES ('4a7676fc1d9e4ab29a3aea48c25ecc6e', 'web_company', '公司名称', '杭州融都科技股份有限公司', '1', '2016-09-29 10:46:03', '1', '公司名称', '1');
INSERT INTO `sys_config` VALUES ('4baec0ca9afc4f44870e9798c76ed0a8', 'cash_serv_fee', '提现服务费', '0.0005', '2', '2016-08-11 17:36:47', '1', '按照比例收费', '1');
INSERT INTO `sys_config` VALUES ('4bb851a4c4824436aa41f02d55f6dce5', 'support_interest_financing', '是否支持即息理财', '1', '2', '2016-11-30 10:17:37', '1', '是否支持即息理财', '1');
INSERT INTO `sys_config` VALUES ('4d6cfb543a884f36907f172863eec829', 'merchant_account', '平台账户', '01160708462', '2', '2016-08-18 21:41:12', '1', '平台支付活动奖励给投资用户的结算账户', '0');
INSERT INTO `sys_config` VALUES ('5', 'static_resource_url', '静态资源服务器', 'http://172.16.90.30:8083', '2', '2016-06-22 11:45:53', '1', 'http://10.0.5.103:54611，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('50', 'bank_num', '绑定银行卡数量', '5', '2', '2016-06-22 11:45:53', '1', '用户绑定银行卡数量最大个数', '1');
INSERT INTO `sys_config` VALUES ('54', 'vip_open', '是否开启vip功能', '1', '2', '2016-06-22 11:45:53', '1', '0：不开启，1：开启', '0');
INSERT INTO `sys_config` VALUES ('54c84337b640461e922d2743c6e721b6', 'default_user_pwd', '默认用户密码', 'abc1234567', '2', '2016-07-30 15:02:19', '1', '后台新建个人用户，默认登录密码', '1');
INSERT INTO `sys_config` VALUES ('56', 'user_invest_redEnvelope_rate', '每笔投资使用红包最大百分比', '99', '2', '2016-06-22 11:45:53', '1', '每笔投资使用红包总额占投资总额最大百分比,小于100', '1');
INSERT INTO `sys_config` VALUES ('56787f7501a24f2ab7f88bf2afe67716', 'auto_invest_vip_flag', 'VIP等级优先自动投资', '1', '2', '2016-10-31 10:13:16', '1', 'VIP等级优先自动投资0.未启用1启用', '1');
INSERT INTO `sys_config` VALUES ('56ae97b61849465ab8bbdeda62ae3c97', 'isOnlineConfig', '是否是线上环境', '1', '2', '2016-11-02 15:41:27', '1', '是否是线上环境1 ：是  ，0 ： 否', '1');
INSERT INTO `sys_config` VALUES ('6', 'web_name', '网站名称', '互联网金融资产交易系统', '1', '2016-06-22 11:45:53', '1', '网站名称', '1');
INSERT INTO `sys_config` VALUES ('69', 'late_interest_rate', '逾期利息划给用户比例', '0.6', '2', '2016-06-22 11:45:53', '1', '借款人逾期还款，逾期利息划给投资人比例，1代表全部给投资人，0代表全部给平台', '1');
INSERT INTO `sys_config` VALUES ('6a6b8498fbac4fb8bfbe03e54cf7414e', 'ufx_bond_fee_limit', '转让手续费限制', '10', '2', '2016-11-17 10:50:51', '1', '转让手续费限制（单位：%） - -PS :由平台和第三方签订的协议可做配置变更', '1');
INSERT INTO `sys_config` VALUES ('7', 'customer_hotline', '客户服务热线', '400-183-3666', '1', '2016-06-22 11:45:53', '1', '客户服务热线', '1');
INSERT INTO `sys_config` VALUES ('7565546a5523419880eb1eebbf2c7faa', 'borrow_lowest_apr', '最低借款利率', '1', '2', '2016-06-29 11:00:39', '1', '最低借款利率：1%', '1');
INSERT INTO `sys_config` VALUES ('762494463c6c4407bf8ef6aa73a8b4a3', 'free_overdue_day', '逾期？天内不收逾期罚息', '0', '2', '2016-10-22 11:14:21', '1', '配置逾期不收逾期罚息最大天数', '1');
INSERT INTO `sys_config` VALUES ('76e44686c5b511e6817dfa163e88e3b3', 'tpp_account_id', 'TPP商户账户号', '01160708462', '2', '2016-12-19 14:36:25', '1', 'TPP商户账户号', '0');
INSERT INTO `sys_config` VALUES ('7e0ef9e96ca946e1b0fef036fe23a712', 'account_lock_time', '账户锁定时间', '86400', '6', '2016-11-23 11:39:29', '1', '单位为秒，账户锁定时间是指用户登录失败次数超过限制而被锁定的时长，超过该设置时间后即系统自动解锁。', '1');
INSERT INTO `sys_config` VALUES ('8', 'fax', '传真', '0571-85132616', '1', '2016-06-22 11:45:53', '1', '用于网站显示公司传真', '1');
INSERT INTO `sys_config` VALUES ('85', 'auto_invest', '自动投资开关', '1', '2', '2016-06-22 11:45:53', '1', '自动投资功能是否开启：1 开启；0 关闭', '1');
INSERT INTO `sys_config` VALUES ('86', 'notify_url', '回调地址', 'http://172.16.90.30:8080', '2', '2016-06-22 11:45:53', '1', '测试使用，请不要在地址后面加/', '0');
INSERT INTO `sys_config` VALUES ('8672c9bc1d4c4f51bb584a8f32560a43', 'borrow_lowest_invest_account', '最低起投金额', '1', '2', '2016-08-30 16:04:22', '1', '最低起投金额：1元', '1');
INSERT INTO `sys_config` VALUES ('87', 'config_online', '是否是在线生产环境', '0', '2', '2016-06-22 11:45:53', '1', '是否开通线上环境配置', '0');
INSERT INTO `sys_config` VALUES ('8f08063beb7343d0b818147378f2887b', 'account_warn_contacts', '资金预警联系人', '13516711996,15825541499,13856911394,18042465087', '1', '2016-09-29 20:48:19', '1', '资金预警联系人手机号，多个手机号为用半角,分开，如13899999999,13688888888(最多4个)', '1');
INSERT INTO `sys_config` VALUES ('9', 'web_index_qq', 'QQ群', '277288852', '1', '2016-06-22 11:45:53', '1', '平台交流QQ群', '1');
INSERT INTO `sys_config` VALUES ('918adf4d8e9311e699af000c296f8d8c', 'protocolStyle', '', '1', '1', '2016-10-10 10:45:13', '1', '1', null);
INSERT INTO `sys_config` VALUES ('941019ea8068425ab52d3b70745b0a94', 'bond_prefix', '债权转让标名称前缀', '转让', '2', '2016-11-05 15:35:44', '1', '债权转让标名称前缀', '1');
INSERT INTO `sys_config` VALUES ('99', 'cash_need_audit', '提现是否需要取现复核', '1', '2', '2016-06-22 11:45:53', '1', '汇付、双乾需要提现复核', '1');
INSERT INTO `sys_config` VALUES ('99be7591ac2546b8aa086465420ebf00', 'interest_manage_rate', '利息管理费', '0.2', '2', '2016-06-29 19:24:32', '1', '利息管理费：0.2%', '1');
INSERT INTO `sys_config` VALUES ('9c7dc1ae0f2e48eeb10eb953c60cbc8b', 'days_of_month', '每月?天', '30', '2', '2016-07-21 09:55:52', '1', '表示每月月均天数为30天，用于还款时计息的计算', '1');
INSERT INTO `sys_config` VALUES ('a3adfcaf4d174768b77b04fe6d58b918', 'account_type', '账户类型', '01', '2', '2016-08-09 11:50:01', '1', '账户类型', '0');
INSERT INTO `sys_config` VALUES ('b03687c9e84f48978781819ce1502f2c', 'borrow_most_account', '最高借款金额', '50000000', '2', '2016-06-29 10:50:25', '1', '最高借款金额', '1');
INSERT INTO `sys_config` VALUES ('b855aadbc69447c9a723a1bfb5ebd6b6', 'project_no_init_value', '项目编号初始值', '3000', '2', '2016-09-09 14:55:28', '1', '项目编号初始值', '0');
INSERT INTO `sys_config` VALUES ('bd6f6c4f8ba14bc3bc81595ef5d3da08', 'open_first_invest', '投资/成立审核之后发放首投奖励', '0', '2', '2017-01-18 11:47:03', '1', '1 成立审核之后发放首投奖励，0 投资之后发放首投奖励，默认0', '1');
INSERT INTO `sys_config` VALUES ('bd874da06ea04924a606cad55ee6396d', 'open_rabbitmq', '开启RabbitMQ异步服务', '1', '2', '2016-06-23 10:16:23', '1', '开启RabbitMQ异步服务', '1');
INSERT INTO `sys_config` VALUES ('c06d3dfaf3354a739a8c70599eb2d513', 'login_error_flash_time', '登录失败次数刷新时间', '3600', '6', '2016-08-09 21:44:11', '1', '单位为秒，统计登录失败次数的清零时间，即超过该时长后重新登录则重新开始统计登录失败次数。', '1');
INSERT INTO `sys_config` VALUES ('d65ae86cda44432585fa2ad4645441fc', 'recharge_min_amount', '单笔充值最小金额', '1', '2', '2016-08-10 16:24:37', '1', '最小充值金额：1元', '1');
INSERT INTO `sys_config` VALUES ('e50705d539ba412fb5c817d551c486e3', 'auto_invest_min_usemoney', '用户开启自动投资最低可用金额', '100', '2', '2016-07-26 16:07:18', '1', '用户开启自动投资最低可用金额', '1');
INSERT INTO `sys_config` VALUES ('eb2fde7b706c48069d61357a45fb8484', 'auto_invest_max_apr', '产品最大自动投资比率', '0.3', '2', '2016-07-26 16:05:59', '1', '产品最大自动投资比率', '1');
INSERT INTO `sys_config` VALUES ('f53c5be0008345f8adc03d68cd8a3017', 'company_max_tender', '企业用户最多可投数', '100', '2', '2016-06-04 16:32:05', '1', '企业用户最多可投数', '1');
INSERT INTO `sys_config` VALUES ('f53c5be0008345f8adc03d68cd8a4444', 'sign_type', '签名类型', 'e_sign', '2', '2016-07-16 13:31:15', '1', '协议有关的签名，e_sign  是e签宝配置，cfca_sign是安心签配置，不是这两种则就是没有第三方签署', '1');
INSERT INTO `sys_config` VALUES ('fa2fe59b3a244a4baedc188777745211', 'send_code_flash_time', '短信验证码刷新时间', '60', '5', '2016-08-08 18:04:47', '1', '时间单位为秒', '1');
INSERT INTO `sys_config` VALUES ('fa2fe59b3a244a4baedc188777745231', 'borrow_manage_rate', '借款管理费', '0.7', '2', '2016-06-29 19:23:25', '1', '借款管理费：0.7%', '1');
INSERT INTO `sys_config` VALUES ('fa2fe59b3a244a4baedc188777745233', 'login_lock_number', '登录错误锁定次数', '3', '6', null, '1', '登录失败次数超过设置的值即锁定该账号。', '1');
INSERT INTO `sys_config` VALUES ('fb9273270af946d5bd8a80cc3d279d22', 'redenvelope_max_avail_period', '红包最大有效期（固定期）', '365', '2', '2016-09-27 20:11:46', '1', '固定期红包可设置的最大有效期天数', '1');
INSERT INTO `sys_config` VALUES ('fb9273270af946d5bd8a80cc3d279d54', 'send_code_space_time', '发送验证码间隔时间', '60', '5', '2016-08-16 20:17:45', '1', '发送验证码间隔时间', '1');
INSERT INTO `sys_config` VALUES ('c2cd38fd45d8412c9cd0a2071500b2f8', 'message_resource_default_language', '资源默认语言', 'zh_CN', '2', '2017-03-15 16:07:13', '1', '资源默认语言', '1');
-- 新增 渤海银行商户号 字段
insert into sys_config() values(replace(UUID(),"-",""),'cbhb_customerId','渤海银行商户号','800057100010001','1','2017-03-01 11:13:11','1','渤海银行商户号','1');
-- 新增渤海银行提交地址
insert into sys_config() values(replace(UUID(),"-",""),'cbhb_submit_url','渤海银行提交地址','http://221.239.93.145:8280/hipos/payTransaction','1','2017-03-01 11:13:11','1','渤海银行提交地址(现在暂为测试地址)','1');
-- 新增渤海银行证书私钥密码
insert into sys_config() values(replace(UUID(),"-",""),'cbhb_private_key','渤海银行证书私钥密码','123456','1','2017-03-01 11:13:11','1','渤海银行证书私钥密码','1');


-- ----------------------------
-- Table structure for `sys_dict_data`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `dict_type` varchar(64) NOT NULL COMMENT '字典类型',
  `item_name` varchar(64) NOT NULL COMMENT '字典项名称',
  `item_value` varchar(128) DEFAULT NULL COMMENT '字典项值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `status` char(1) DEFAULT '1' COMMENT '启用状态(1 启用，0停用)',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `expression` varchar(128) DEFAULT NULL COMMENT '表达式',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_dict_data_type_value` (`item_value`,`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('00d0e09e32eb495bbee024219910ec12', 'sysLogContent', '角色权限', 'role_perms', null, '77', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('00fd0febff334b6fa2dd71b5407bdc5b', 'interestStyle', '成立计息', '1', null, '1', '1', '成立计息', null);
INSERT INTO `sys_dict_data` VALUES ('01418f8d1f12469aaad399a41860ef75', 'YesNo', '否', '0', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0141c7f236904c859752a728fd831eca', 'sysLogMethod', '查询', 'query', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0162d92ed9a94d14b6216c65c9bbf637', 'bondStatus', '转让完成', '3', null, '1', '1', '转让完成', null);
INSERT INTO `sys_dict_data` VALUES ('016a83e0c9af4328836eef5a6e66f277', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62b;&lt;/em&gt;', '&amp;#xe62b;', null, '19', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('02a65a5426a74cbd9deece7d591290eb', 'sysLogContent', '参数配置', 'config', null, '63', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('03284e056c6d4694a44d7d161ec3a3f1', 'sysLogContent', '邀请奖励', 'user_invite_award', null, '84', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('03a003fed8474a09bab1cad1d195c20c', 'sysLogContent', '账户日志', 'accountlog_list', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('03e5d313b15543d7807b6aa198d4efb5', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe618;&lt;/em&gt;', '&amp;#xe618;', null, '25', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('0441420041714d34890351ae441be246', 'userType', '担保机构', '3', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('046eb4f27f504f8992ebdb1b15f18c92', 'sysLogContent', '借款下架', 'borrow_stop', null, '26', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0506faa91167482493ad4789741fa8cf', 'bondInvestStatus', '受让失败', '2', null, '3', '1', '受让失败', '');
INSERT INTO `sys_dict_data` VALUES ('061dba46c3e442148a5ea48f48cb9cfd', 'sex', '男', '1', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('07001c0c198c4049be37f8935ffc4737', 'workExperience', '25年以上', '7', null, '7', '0', '', null);
INSERT INTO `sys_dict_data` VALUES ('07fd2dbc60c54b26af11a887324fc2ef', 'productStatus', '成立待审', '5', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0818fae9c0ef4a74811d2886081514ae', 'productTimeLimit', '6个月以下', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0a5e650d9bac482b99a5f89883fefbbe', 'remainDaysType', '30天以下', '1', null, '1', '1', '30天以下', 'n < 30');
INSERT INTO `sys_dict_data` VALUES ('0aa5136d91cb4a438011a10634e87dbf', 'userNature', '担保机构', '3', null, '3', '1', '担保机构', null);
INSERT INTO `sys_dict_data` VALUES ('0b90e3336c7b4909bed88622d9f77022', 'investStatus', '投资成功', '1', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0c031f5038a04466b7993b75332d5832', 'sysLogContent', '红包', 'user_red', null, '51', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0d52cbf52db948f3a1d1b1c41455e58c', 'sysLogContent', '组织机构', 'org', null, '74', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0dc0f6004f97431a860af6880e7289c7', 'sysLogContent', '数据字典', 'dict', null, '64', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0e7170b34c66491187d8efa47e7aa99b', 'sysLogContent', '投资列表', 'invest_record', null, '32', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0e920fd35f234369ab1fcea54232cf19', 'investType', '手动投资', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('0f07a7eb6c0641518c1e1d49c55bbc7d', 'userFreeze', '借款', 'loan', null, '6', '1', '借款', null);
INSERT INTO `sys_dict_data` VALUES ('0fa577a6f6884230ba3be46c12b607e5', 'platAccountType', '账户互转', '3', null, '3', '1', '账户互转', null);
INSERT INTO `sys_dict_data` VALUES ('0fd3d9e17bee41838f166bdc86e1ea7f', 'specificSale', '定向密码', '1', null, '1', '1', '定向密码', null);
INSERT INTO `sys_dict_data` VALUES ('10c8b1b0e4444a6dbc233fce72656b35', 'salaryRange', '15-20万', '6', null, '6', '1', '13000-15000元', null);
INSERT INTO `sys_dict_data` VALUES ('10e72143f30b4f719977357fe204a448', 'platAccountStatus', '成功', '1', null, '2', '1', '成功', null);
INSERT INTO `sys_dict_data` VALUES ('114eba1180aa48d291827ed5dfdca99f', 'sysLogContent', '后台用户', 'operator', null, '72', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1179b9648b1545e19e57e4c66f7d4b6d', 'bondMoneyType', '小于10000元', '1', null, '1', '1', '小于10000元', 'n < 10000');
INSERT INTO `sys_dict_data` VALUES ('12393fd7cc2244698fb3e5095e2ea8f8', 'user_security_question', '您哥哥的姓名？', 'question_brother_name', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294464', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62d;&lt;/em&gt;', '&amp;#xe62d;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294465', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62e;&lt;/em&gt;', '&amp;#xe62e;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294466', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe602;&lt;/em&gt;', '&amp;#xe602;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294467', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe603;&lt;/em&gt;', '&amp;#xe603;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294468', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60a;&lt;/em&gt;', '&amp;#xe60a;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294469', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe608;&lt;/em&gt;', '&amp;#xe608;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294470', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61f;&lt;/em&gt;', '&amp;#xe61f;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294471', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62c;&lt;/em&gt;', '&amp;#xe62c;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294472', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62f;&lt;/em&gt;', '&amp;#xe62f;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294473', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe630;&lt;/em&gt;', '&amp;#xe630;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294474', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe631;&lt;/em&gt;', '&amp;#xe631;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294475', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe632;&lt;/em&gt;', '&amp;#xe632;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249754916495294476', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe633;&lt;/em&gt;', '&amp;#xe633;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249760773924716544', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe636;&lt;/em&gt;', '&amp;#xe636;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249760773924716545', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe635;&lt;/em&gt;', '&amp;#xe635;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249760773924716546', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe637;&lt;/em&gt;', '&amp;#xe637;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1249760773924716547', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61b;&lt;/em&gt;', '&amp;#xe61b;', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('13909cb92fc842ec9004ba25c55f6ff9', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe612;&lt;/em&gt;', '&amp;#xe612;', null, '13', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('13faefe851de4dc2aa11ac4d5e018800', 'accountType', '利息管理费', 'interest_manage_fee', null, '35', '1', '利息管理费', null);
INSERT INTO `sys_dict_data` VALUES ('148295ffbcae4e7182ea746e55d301d7', 'sysLogContent', '协议', 'protocol', null, '55', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('14d532773bc44b9db2c500b65c29a392', 'sysLogMethod', '冻结/解冻', 'freeze', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('15600c3359a9483b8b4fd3dd5aa90c6c', 'bondStatus', '转让中', '0', null, '2', '1', '转让中', null);
INSERT INTO `sys_dict_data` VALUES ('15ed787af2f8486aa4878a3fa1efd2a3', 'sysLogContent', '资质认证', 'user_qualification', null, '87', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('16b01f693c2b41a49551144939cd2f70', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe616;&lt;/em&gt;', '&amp;#xe616;', null, '29', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('1716a765052e4a08a1cc628357da64ea', 'test', '类型2', '2', null, '2', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('171d341d285645ed9dab1f68b82a35ca', 'productStatus', '待上架', '40', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('173ba885f87d46a788c80e4c22b2c680', 'accountBank', '建设银行', 'CCB', null, '5', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('178fe6173f624cd09a37a7a3942afa42', 'repayStyle', '等额本金', '4', null, '4', '1', '等额本金', null);
INSERT INTO `sys_dict_data` VALUES ('17c73df783e04fd69be765b7b4e18b26', 'amountCondition', '1万元以下', '1', null, '1', '1', '', 'n<=10000');
INSERT INTO `sys_dict_data` VALUES ('17d2f83302974232bb13e1e096f29a25', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe624;&lt;/em&gt;', '&amp;#xe624;', null, '38', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('17f5a753ce5340288ffa6f6d506d79f4', 'productStatus', '募集中', '4', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('18dcc9f5c85140eabbbc0c57323d8c9b', 'userStatus', '冻结', '-1', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('18df2887203c42ac84fee39742ea0ac5', 'amountCondition', '5~10万元', '3', null, '3', '1', '', '50000<n && n<=100000');
INSERT INTO `sys_dict_data` VALUES ('18fb69fc1ca449da8556fa3d85a2d349', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe626;&lt;/em&gt;', '&amp;#xe626;', null, '37', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('19', 'accountType', '借款手续费', 'borrow_fee', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('190f6a7849a444789801d1542b0a75eb', 'sysLogContent', '提现审核', '提现列表', null, '11', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc29c', 'tppType', '放款', 'loan', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1a089eb4351446e0b4263c70d886dcef', 'timeType', '月', '0', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1b0280c7f09a49669fdbcd60f099fd4a', 'projectStatus', '还款中', '8', null, '14', '1', '还款中', null);
INSERT INTO `sys_dict_data` VALUES ('1b9a173b571047fcbe231e149545cff5', 'borrowUse', '短期周转', '短期周转', null, '1', '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('1bb7234cb22d4a179b6dc7e7e28c13ae', 'configType', '网站管理', '1', '2016-09-26 13:41:37', '1', '1', '网站管理', null);
INSERT INTO `sys_dict_data` VALUES ('1c3633c3f5d249e2a8d169ed2a71b761', 'userStatus', '正常', '0', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1d03c072c52644a5a0995aabf5e81674', 'repayType', '逾期还款', '4', null, '4', '1', '逾期还款', null);
INSERT INTO `sys_dict_data` VALUES ('1da47388c0434cba857c95d1ba2c67e2', 'configType', '短信', '4', '2016-09-26 13:41:37', '4', '1', '短信', null);
INSERT INTO `sys_dict_data` VALUES ('1dd5714514b44ec6b0314fa32e2b7633', 'bondMoneyType', '50000元~100000元', '3', null, '3', '1', '50000元~100000元', 'n >= 50000 && n <10000');
INSERT INTO `sys_dict_data` VALUES ('1dde9bfc84d1488a98d0103a564a05cf', 'sysLogContent', '逾期记录', 'repayment_overdue', null, '41', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1e7fd3afa8a7449dabeb96051a3edc68', 'bondInvestStatus', '超时取消', '3', null, '4', '1', '超时取消', '');
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c0088881c', 'accountType', '提现待审核', 'cash_audit', null, '4', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c008e2e1c', 'accountType', '提现处理中', 'cash_proccess', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('1f41e4aae095457ca64fa6a4a46a2d62', 'tppType', '解冻', 'unFreeze', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('20ab1879e1b2410b96e32f1111069528', 'sysLogContent', '充值列表', 'recharge_list', null, '13', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('20c690d7e9d349718f0b6f07f262e7a9', 'educationLevel', '中专', '4', null, '3', '1', '中专', null);
INSERT INTO `sys_dict_data` VALUES ('219834eaf935432eb487a6906d597ca8', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60d;&lt;/em&gt;', '&amp;#xe60d;', null, '16', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('21ef7ba159234a058a00bfa7ab96d554', 'repayStyle', '等额本息', '1', null, '1', '1', '月等额本息', null);
INSERT INTO `sys_dict_data` VALUES ('22', 'accountType', '投资冻结', 'invest_freeze', null, '5', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('221fbea1107a46dbb1dccfdda967206f', 'qualificationType', '社保', 'socialSecurity', null, '2', '1', '社会保障', null);
INSERT INTO `sys_dict_data` VALUES ('22520c53588b432b930ee4e5faf30afd', 'educationLevel', '初中', '2', null, '2', '1', '初中', null);
INSERT INTO `sys_dict_data` VALUES ('23', 'accountType', '借款入账', 'borrow_success', null, '6', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('233ba78c727540c8b2a0059cddd850cc', 'borrowUse', '购物消费', '购物消费', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('23a6e01b2b6741d69a435c52a6e3b600', 'interLanguage', 'United States 美国(英文)', 'en_US', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('243', 'messageType', '待还到期提醒', 'borrower_repay_notice', '2016-06-22 13:50:27', '2', '1', '待还到期提醒', null);
INSERT INTO `sys_dict_data` VALUES ('243a2a0e2abc4732aae877c1d4dbd857', 'salaryRange', '30-50万', '8', null, '8', '1', '20000元以上', null);
INSERT INTO `sys_dict_data` VALUES ('244', 'messageType', '修改银行帐号验证码', 'bank_verify_code', '2016-06-22 13:50:27', '3', '1', '修改银行帐号验证码', null);
INSERT INTO `sys_dict_data` VALUES ('245', 'messageType', '后台初审不通过', 'borrow_verify_fail', '2016-06-22 13:50:27', '4', '1', '后台初审不通过', null);
INSERT INTO `sys_dict_data` VALUES ('246', 'messageType', '后台初审通过', 'borrow_verify_succ', '2016-06-22 13:50:27', '5', '1', '后台初审通过', null);
INSERT INTO `sys_dict_data` VALUES ('247', 'messageType', '自动投资成功通知', 'auto_tender', '2016-06-22 13:50:27', '6', '1', '自动投资成功通知', null);
INSERT INTO `sys_dict_data` VALUES ('248', 'messageType', '借款人还款', 'receive_repay', '2016-06-22 13:50:27', '7', '1', '借款人还款', null);
INSERT INTO `sys_dict_data` VALUES ('249', 'messageType', '线上充值成功', 'recharge_succ', '2016-06-22 13:50:27', '8', '1', '线上充值成功', null);
INSERT INTO `sys_dict_data` VALUES ('24b501bd2bb44851821b2d89b4511091', 'interLanguage', 'China 中国(中文简体)', 'zh_CN', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('24ece2d25130447f89803205e81813a5', 'realizeDayCondition', '180天以上', '4', null, '4', '1', '180天以上', 'n &gt;= 180');
INSERT INTO `sys_dict_data` VALUES ('250', 'messageType', '提现审核失败', 'cash_verify_fail', '2016-06-22 13:50:27', '9', '1', '提现审核失败', null);
INSERT INTO `sys_dict_data` VALUES ('251', 'messageType', '线下充值审核失败', 'down_recharge_verify_fail', '2016-06-22 13:50:27', '10', '1', '线下充值审核失败', null);
INSERT INTO `sys_dict_data` VALUES ('252', 'messageType', '登录密码修改通知', 'password_update', '2016-06-22 13:50:27', '11', '1', '登录密码修改通知', null);
INSERT INTO `sys_dict_data` VALUES ('253', 'messageType', '投资成功通知', 'invest_succ', '2016-06-22 13:50:27', '12', '1', '投资成功通知', null);
INSERT INTO `sys_dict_data` VALUES ('254', 'messageType', '满标审核失败通知', 'borrow_full_fail', '2016-06-22 13:50:27', '13', '1', '满标审核失败通知', null);
INSERT INTO `sys_dict_data` VALUES ('255', 'messageType', '借款项目取消通知', 'borrow_cancel', '2016-06-22 13:50:27', '14', '1', '借款项目取消通知', null);
INSERT INTO `sys_dict_data` VALUES ('256', 'messageType', '流转标还款成功通知', 'flow_repay_succ', '2016-06-22 13:50:27', '15', '1', '流转标还款成功通知', null);
INSERT INTO `sys_dict_data` VALUES ('256e4670de6d4ffbafa030a3a85be243', 'projectProcessNode', '下架', 'stop', null, '5', '1', '下架', null);
INSERT INTO `sys_dict_data` VALUES ('257', 'messageType', '后台批量充值审核成功', 'batch_recharge_verify_succ', '2016-06-22 13:50:27', '16', '1', '后台批量充值审核成功', null);
INSERT INTO `sys_dict_data` VALUES ('258', 'messageType', '流转标重新流转', 'flow_restart', '2016-06-22 13:50:27', '17', '1', '流转标重新流转', null);
INSERT INTO `sys_dict_data` VALUES ('259', 'messageType', '认证审核失败', 'certify_fail', '2016-06-22 13:50:27', '18', '1', '认证审核失败', null);
INSERT INTO `sys_dict_data` VALUES ('25a96820fa7c4c699d925ec74d967358', 'sendType', '不限', '', '2016-11-04 11:30:14', '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('25b8c9f8bb1e4fd3812aea62d43f61eb', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe62a;&lt;/em&gt;', '&amp;#xe62a;', null, '20', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('260', 'messageType', '重新获取交易密码', 'get_paypwd', '2016-06-22 13:50:27', '19', '1', '重新获取交易密码', null);
INSERT INTO `sys_dict_data` VALUES ('261', 'messageType', '获得投资奖励', 'receive_tender_award', '2016-06-22 13:50:27', '20', '1', '获得投资奖励', null);
INSERT INTO `sys_dict_data` VALUES ('262', 'messageType', '后台扣费成功', 'houtai_deduct_succ', '2016-06-22 13:50:27', '21', '1', '后台扣费成功', null);
INSERT INTO `sys_dict_data` VALUES ('263', 'messageType', '获得回款奖励', 'receive_huikuan_award', '2016-06-22 13:50:27', '22', '1', '获得回款奖励', null);
INSERT INTO `sys_dict_data` VALUES ('264', 'messageType', '待收到期提醒', 'loaner_repay_notice', '2016-06-22 13:50:27', '23', '1', '待收到期提醒', null);
INSERT INTO `sys_dict_data` VALUES ('265', 'messageType', '提现验证码', 'cash_verify_code', '2016-06-22 13:50:27', '24', '1', '提现验证码', null);
INSERT INTO `sys_dict_data` VALUES ('266', 'messageType', '认证审核撤销', 'certify_cancel', '2016-06-22 13:50:27', '25', '1', '认证审核撤销', null);
INSERT INTO `sys_dict_data` VALUES ('267', 'messageType', '新借款项目发布通知', 'new_borrow', '2016-06-22 13:50:27', '26', '1', '新借款项目发布通知', null);
INSERT INTO `sys_dict_data` VALUES ('268', 'messageType', '投资失败', 'invest_fail', '2016-06-22 13:50:27', '27', '1', '投资失败', null);
INSERT INTO `sys_dict_data` VALUES ('269', 'messageType', '提现审核成功', 'cash_verify_succ', '2016-06-22 13:50:27', '28', '1', '提现审核成功', null);
INSERT INTO `sys_dict_data` VALUES ('270', 'messageType', '线下充值审核成功', 'down_recharge_verify_succ', '2016-06-22 13:50:27', '29', '1', '线下充值审核成功', null);
INSERT INTO `sys_dict_data` VALUES ('2705c5cdb37942d48062dc29c3043011', 'sysLogContent', '借款资料', 'borrow_pic', null, '23', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('271', 'messageType', '短信验证码', 'phone_code', '2016-06-22 13:50:27', '30', '1', '短信验证码', null);
INSERT INTO `sys_dict_data` VALUES ('272', 'messageType', '交易密码修改通知', 'paypwd_update', '2016-06-22 13:50:27', '31', '1', '交易密码修改通知', null);
INSERT INTO `sys_dict_data` VALUES ('273', 'messageType', '满标审核通过通知', 'borrow_full_succ', '2016-06-22 13:50:27', '32', '1', '满标审核通过通知', null);
INSERT INTO `sys_dict_data` VALUES ('273d5f5200f94511bc2f38bd07a3e545', 'sendStatus', '发送失败', '2', '2016-06-22 16:32:34', '3', '1', '发送失败', null);
INSERT INTO `sys_dict_data` VALUES ('274', 'messageType', '还款成功通知', 'repay_succ', '2016-06-22 13:50:27', '33', '1', '还款成功通知', null);
INSERT INTO `sys_dict_data` VALUES ('2741e88b19c948e8a746fc064298e88a', 'tppStatus', '未处理', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('275', 'messageType', '流转标认购成功通知', 'flow_buy_succ', '2016-06-22 13:50:27', '34', '1', '流转标认购成功通知', null);
INSERT INTO `sys_dict_data` VALUES ('276', 'messageType', 'VIP到期通知', 'vip_expired', '2016-06-22 13:50:27', '35', '1', 'VIP到期通知', null);
INSERT INTO `sys_dict_data` VALUES ('276a68273db543e0934bef2dbc30e6be', 'borrowUse', '生意周转', '生意周转', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('277', 'messageType', 'VIP生日', 'vip_birth', '2016-06-22 13:50:27', '36', '1', 'VIP生日', null);
INSERT INTO `sys_dict_data` VALUES ('278', 'messageType', '认证审核成功', 'certify_succ', '2016-06-22 13:50:27', '37', '1', '认证审核成功', null);
INSERT INTO `sys_dict_data` VALUES ('279', 'messageType', '重新激活邮件', 'get_email', '2016-06-22 13:50:27', '38', '1', '重新激活邮件', null);
INSERT INTO `sys_dict_data` VALUES ('27d16e5ab1404d768a3e44d90e575a53', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe614;&lt;/em&gt;', '&amp;#xe614;', null, '7', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('280', 'messageType', '重新获取登录密码', 'get_pwd', '2016-06-22 13:50:27', '39', '1', '重新获取登录密码', null);
INSERT INTO `sys_dict_data` VALUES ('281', 'messageType', '支付投资奖励', 'deduct_borrower_award', '2016-06-22 13:50:27', '40', '1', '支付投资奖励', null);
INSERT INTO `sys_dict_data` VALUES ('281307ca83634caeb44d131337882d09', 'sysLogContent', '产品审核', 'product_audit', null, '35', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('282', 'sendType', '短信', '1', '2016-06-22 14:05:26', '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('283', 'sendType', '邮件', '2', '2016-06-22 14:05:54', '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('284', 'sendType', '站内信', '3', '2016-06-22 14:06:19', '3', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('28e020d7f3674d94a1d2f7eb2bdc7e0a', 'papersStatus', '启用', '1', null, '1', '1', '启用', null);
INSERT INTO `sys_dict_data` VALUES ('2a97dcb3a18c4773a3d212d8e4a8e42f', 'repayStyle', '每月还息到期还本', '3', null, '3', '1', '每月还息到期还本', null);
INSERT INTO `sys_dict_data` VALUES ('2b0b109e3dd641ffb6b95f5125675b0f', 'sysLogContent', '贷后跟踪', 'borrow_follow', null, '29', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2b23eb14e4834e4cabb3f3b7dce0c380', 'accountType', '冻结逾期罚息', 'freeze_realize_lateinterest', null, '75', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2b3f89746a4842589985ceb3a807622f', 'aprType', '14%以上', '4', null, '4', '1', '14%以上', 'n >= 14');
INSERT INTO `sys_dict_data` VALUES ('2b52855d52f1498aa43532408703064f', 'realizeStatus', '待还款', '8', null, '2', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('2b7fd89a7ea6421d8d27ce1a46526a9f', 'sysLogContent', '风险评测记录', 'risk_user_log', null, '62', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2bb477e39df74039b995ff6054863763', 'sysLogContent', '用户列表', 'user_list', null, '86', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2be89341af674ded834320c530a2b013', 'realizeStatus', '撤回', '7', null, '4', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('2c22c8d5ec5e45a9bee21e9003cf9502', 'sysLogMethod', '删除', 'del', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2c2efbb5ff314d0597cdf37079bde09c', 'sysLogContent', '操作日志', 'log', null, '65', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2c54cf71724f45408a68976af0ff3535', 'repayType', '已还垫付', '3', null, '3', '1', '已还垫付', null);
INSERT INTO `sys_dict_data` VALUES ('2c856e916a8a4370a2fdff45582924d1', 'sysLogContent', '文章', 'article', null, '16', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2ddd9e2e590b419cb1959a823823025d', 'borrowStatus', '待上架', '40', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2de3cced8c7a46af914d4bf218f511f8', 'qualificationType', '行驶证', 'carLicense', null, '3', '1', '行驶证', null);
INSERT INTO `sys_dict_data` VALUES ('2dffb41875e847a298c2cb02c05e26ca', 'isSingle', '多选', '2', null, '2', '1', '多选', null);
INSERT INTO `sys_dict_data` VALUES ('2e117a80a743414b98425c6496e84b4b', 'bondMoneyType', '10000元~50000元', '2', null, '2', '1', '10000元~50000元', 'n >= 10000 && n <50000');
INSERT INTO `sys_dict_data` VALUES ('2e7159525d464f228efbaad1e8a12e1f', 'accountBank', '招商银行', 'CMB', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('2fee84c1f8444b10ae1090b56d13a5e6', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61e;&lt;/em&gt;', '&amp;#xe61e;', null, '33', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('2ff0fa4531c446d6ab200a521fc21003', 'sysLogContent', '借款记录', 'borrow_record', null, '28', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('300', 'accountType', '逾期利息收回', 'collect_late_interest', null, '61', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('300916be14c14e6cb6de98115beff343', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe621;&lt;/em&gt;', '&amp;#xe621;', null, '27', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('30912d3620d641ad8b21260e9622ae99', 'realizeStatus', '已还款', '9', null, '3', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('3194c4217cf3416b99c8bdc0934cb97c', 'projectStatus', '成立待审', '5', null, '10', '1', '成立待审', null);
INSERT INTO `sys_dict_data` VALUES ('3346582cfe9346fb9ba74a8aa3036f7c', 'activityLogStatus', '未使用', '0', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('336f00e458994d6bbd9a53e6adf36dfe', 'companyQualificationType', '其它', 'other', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('33b99df496ea424385c7de4d6a83b021', 'repayStatus', '已还款', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('34', 'accountType', '管理费', 'manage_fee', null, '34', '1', '包括利息管理费、变现服务费、转让手续费、借款管理费、提现手续费，状态均显示为管理费', null);
INSERT INTO `sys_dict_data` VALUES ('3400726c5a0f408f84cdaac3906388e6', 'rechargeFeeType', '个人支付', '01', null, '1', '1', '个人支付', null);
INSERT INTO `sys_dict_data` VALUES ('3480e4710fd14903983836498dee4df8', 'vipLevel', 'VIP1及以上', '1', null, '1', '1', 'VIP1以上', null);
INSERT INTO `sys_dict_data` VALUES ('35', 'accountType', '冻结资金', 'freeze', null, '35', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('351', 'accountType', '还款失败', 'repay_unfreeze', null, '72', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('352', 'accountType', '逾期利息', 'repaid_late_interest', null, '0', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('358', 'accountType', '加息利息收回', 'collect_add_interest', null, '100', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('35c93b513c294b2da8f117829a50151c', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe601;&lt;/em&gt;', '&amp;#xe601;', null, '5', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('36', 'accountType', '解冻资金', 'unfreeze', null, '36', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('36f2edf493d24dc59d0e2ed4fd73ed81', 'sysLogContent', '债权列表', 'bond_list', null, '15', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('37', 'accountType', '待收利息', 'wait_interest', null, '40', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('371', 'accountType', '转让本金收回', 'bond_sell_capital', null, '12', '1', '转让本金收回', null);
INSERT INTO `sys_dict_data` VALUES ('372', 'accountType', '转让利息收回', 'bond_sell_interest', null, '12', '0', '转让利息收回', null);
INSERT INTO `sys_dict_data` VALUES ('37a2e466ff364fa881d6f150fa74fbb1', 'sysLogContent', '平台提现', 'merchant_cash', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('38', 'accountType', '提现成功', 'cash_success', null, '41', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('3835f2e89cd340f4932ec0fe3146e3aa', 'userFreeze', '充值', 'recharge', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('38363f34cf97473bb074ef8478114247', 'realizeAmountCondition', '10万元以上', '4', null, '4', '1', '', 'n &gt; 100000');
INSERT INTO `sys_dict_data` VALUES ('39', 'accountType', '提现失败', 'cash_fail', null, '42', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('3915a45dc120494aa9616f43331667dc', 'bespeakStatus', '已回访', '1', null, '2', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('392760fd152440618778d62728b162e3', 'sysLogContent', '平台充值', 'merchant_recharge', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('3a22e104a91441ce82e5d432a4368f8d', 'account_type', '代收本金', 'wait_capital', null, '1', '1', '12', null);
INSERT INTO `sys_dict_data` VALUES ('3aa08a9a773d45a7bbc4fb61adbb1048', 'papers_type', '答题', '2', null, '1', '1', '答题', null);
INSERT INTO `sys_dict_data` VALUES ('3c4bdeff39b944df8842446a55114faa', 'projectStatus', '成立审核成功', '6', null, '11', '1', '成立审核成功', null);
INSERT INTO `sys_dict_data` VALUES ('3c9f1f57ebab40ae91e40a8519ef04f5', 'borrowStatus', '已作废', '30', null, '13', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('3d972579a5ee4a6a9dca38d4f1a015fa', 'sysLogContent', '平台给用户转账', 'merchant_transfer', null, '7', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('3e8f113ed6214d3bb09210ba089f1a8b', 'sysLogContent', '垫付记录', 'repayment_advance', null, '42', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('3ef2cc3406ef4f7cb56233d8290825a4', 'accountType', '冻结已变现本金', 'freeze_realize_capital', null, '90', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('40', 'accountType', '扣款', 'account_back', null, '43', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('408', 'accountType', '充值成功', 'recharge_success_log', null, '0', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('41', 'accountType', '本金收回', 'collect_capital', null, '44', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('4197e25c24b34cc7b9c479019079b2cf', 'sysLogContent', '风险问卷', 'risk_paper', null, '60', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('41d9ef1e085c405188d8c0991d21144d', 'qualificationApplyStatus', '审核不通过', '2', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('42', 'accountType', '利息收回', 'collect_interest', null, '45', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('4331e125a9b9448fa46b577792adab2d', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61a;&lt;/em&gt;', '&amp;#xe61a;', null, '26', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('43a953525ba04863a8a2b4d20bf3f289', 'realizeDayCondition', '30~90天', '2', null, '2', '1', '30天~90天', 'n &gt;= 30 &amp;&amp; n &lt; 90');
INSERT INTO `sys_dict_data` VALUES ('43c9ae25b6a84d6695c1967160e48312', 'papersStatus', '禁用', '2', null, '2', '1', '禁用', null);
INSERT INTO `sys_dict_data` VALUES ('43e4ecebeac94cd0b4c6030e630587ca', 'remainDaysType', '180天以上', '4', null, '4', '1', '180天以上', 'n >= 180');
INSERT INTO `sys_dict_data` VALUES ('44616bc4c6d44cc4b1d270ea0a224271', 'projectStatus', '撤销处理中', '71', null, '13', '0', '撤销处理中', null);
INSERT INTO `sys_dict_data` VALUES ('448d63792c1947dab3b2cdb6bca0cf6e', 'cardType', '身份证', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('449e9fc7b1734975bc5b391a5e3a7254', 'sysLogContent', '转让记录', 'bond_invest_list', null, '14', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('44cb3289ba9d4858af25ca6991807236', 'companyQualificationType', '企业证照', 'licence', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('44eaa4a43c6c4a70bc6f006d8b995238', 'salaryRange', '80-100万', '10', null, '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('451f4a6824c54d0597f71c5c21ff8778', 'platAccountType', '转让手续费', '9', null, '9', '1', '转让手续费', null);
INSERT INTO `sys_dict_data` VALUES ('4545f6662ddd4a28a8aaa85536fb69e8', 'bondInvestStatus', '受让成功', '1', null, '2', '1', '受让成功', null);
INSERT INTO `sys_dict_data` VALUES ('455b9b5d805c411ab92af87c3d603b12', 'sysLogContent', '用户vip', 'user_vip', null, '54', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('46676cedc35a4e4593cab2d39da50fa9', 'sysLogContent', '用户', 'user', null, '85', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('46d3c1f00373496489c545951f6efe9f', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe622;&lt;/em&gt;', '&amp;#xe622;', null, '31', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('48b3653fa2dc4adf823ec4e375c9c4aa', 'sysLogContent', '借款上架', 'borrow_sale', null, '25', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('49a9f6785c0b4c388efe22a7923c7fe9', 'timeType', '天', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('4a5386a9c20c45fd86184ddd4bb3c163', 'sysLogMethod', '审核', 'audit', null, '11', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('4a99388fc4ef4ed98fc4bda8e396704f', 'sysLogMethod', '新增', 'add', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('4baa5be31aa44d0fa103fe3495591f99', 'RealizeSellStyle', '全额变现', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('4bb2c8b785764ee4927f36b24b9efa71', 'salaryRange', '8-10万', '4', null, '4', '1', '8000-10000', null);
INSERT INTO `sys_dict_data` VALUES ('4cd78d98989540919f0a9917064ff040', 'sysLogContent', '风险问题', 'risk_question', null, '61', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('4f2373dbb55f4186a34c558b1d7b9ee0', 'userType', '个人用户', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('50272493de7d41b1bc4e936c0754cde3', 'aprType', '6-10%', '2', null, '2', '1', '6-10%', 'n>= 6 && n<10');
INSERT INTO `sys_dict_data` VALUES ('50e1ba7121e248ab9db55624adf1f2b5', 'specificSale', '定向等级', '2', null, '2', '1', '定向等级：定向VIP等级销售，大于指定VIP等级才可以购买', null);
INSERT INTO `sys_dict_data` VALUES ('51ae37496e2248aa84fe75748ac980d4', 'timeCondition', '6~12个月', '4', null, '4', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('51e1c4d55843439089d64bb6096350f1', 'carStatus', '无', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('52331de3fecf460faa2b039d97e4d26d', 'platAccountType', '垫付红包', '8', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('526ed01a84c148fb81b716e438c76ebb', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60e;&lt;/em&gt;', '&amp;#xe60e;', null, '17', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('52e84178205d4148b511dabecfe13c04', 'logTemplateType', '资金日志', '1', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('5302d2fa8e884012aba115a574c9521d', 'user_security_question', '您父亲的姓名？', 'question_father_name', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('533a7f6a9a2540f7b4005ae44e614c8c', 'tppType', '投资撤销', 'investFail', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('537c6b3d73eb4f7da6d1a3762d995f60', 'sysLogContent', '用户积分', 'user_score', null, '48', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('53898c7b7d2f417f97735407b43001be', 'sendStatus', '新建', '0', '2016-06-22 16:32:37', '1', '0', '新建状态', null);
INSERT INTO `sys_dict_data` VALUES ('53b5a40971bc4accaa62d32955dd1929', 'sysLogMethod', '下载', 'download', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('53dbe1fdfa6d46ce8277d83674352b33', 'houseStatus', '无', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('53eb1e8cfc504117a5285d2cd551b5c7', 'bespeakLimitTime', '1-3个月', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5458a3885bbc49fca2962c2bd280c76b', 'bespeakStatus', '不回访', '2', null, '3', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('54a3f9a0dfc142afa3f1f15b9f905e36', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe613;&lt;/em&gt;', '&amp;#xe613;', null, '2', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('553c95b3a74a492190c27c5e4263eda7', 'projectStatus', '募集中', '4', null, '8', '1', '募集中', null);
INSERT INTO `sys_dict_data` VALUES ('5555e5555c9c4c249aea32489e931bbe', 'timeCondition', '1-3个月', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('562424d5cd33480c9320bdddc1788c5e', 'investStatus', '待支付', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5716b2b402f7421abbb85def516c47e9', 'amountCondition', '1~5万元', '2', null, '2', '1', '', '10000<n && n<=50000');
INSERT INTO `sys_dict_data` VALUES ('5735958fa5e4467f84b8eb94820b9cca', 'accountType', '投资失败', 'invest_unfreeze', null, '6', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('579e8b34257b4d84b62e081b1caff489', 'accountType', '逾期利息给平台', 'repaid_merchant_late_interest', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('588d1d46216c4b668faebea0e69ccc7a', 'maritalStatus', '丧偶', '3', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('58cf95d8427a4fb08cf73d7c67f28c55', 'YesNo', '是', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('58e5609e76ac42138733a8e594f43e26', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe625;&lt;/em&gt;', '&amp;#xe625;', null, '35', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('590ad24e368141409f8f234e41e7d343', 'accountBank', '广发银行', 'GDB', null, '12', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('5917677abb3544ca898fbf97bab3a6e2', 'projectStatus', '新增', '0', null, '1', '1', '新增', null);
INSERT INTO `sys_dict_data` VALUES ('59bfe1bf6de4426299129399e334c70f', 'repayType', '正常还款', '1', null, '1', '1', '正常还款', null);
INSERT INTO `sys_dict_data` VALUES ('5a3927e4ef2f4f289ad4dfd0cb13b9ce', 'vouchFreeze', '提现', 'cash', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5ad5ef14510d4c1f98c84eadba76f7d2', 'borrowStatus', '成立待审', '5', null, '8', '1', '已下架的借款、募集金额已满的借款、募集时间结束的借款', null);
INSERT INTO `sys_dict_data` VALUES ('5bef7235dc2c4df18a6bf543a3a9bf47', 'borrowNature', '个人借款', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5c14740c805e45d99d7258492fe3da9c', 'productStatus', '未成立', '7', null, '7', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5c451c939f3f44d9b0f10b828643be93', 'saleStyle', '按份额', '2', null, '2', '1', '按份额', null);
INSERT INTO `sys_dict_data` VALUES ('5c77c23c30164df3a0047117c86b1854', 'userFreeze', '提现', 'cash', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5c9e5c669ad74473a05adc3d571263ca', 'accountType', '变现金额入账', 'realize_income', null, '76', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5da5fbb98191424ab0ab2fabdb5993d5', 'borrowStatus', '待售', '41', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5dc1d924875f414fa7f161b3c3701f18', 'borrowUse', '测试用途', '测试用途', null, '6', '1', '6', '');
INSERT INTO `sys_dict_data` VALUES ('5e2afdd223844a47a882a40c5198089a', 'rechargeStatus', '充值失败', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('5f251b868c8f4e10bb5eaa7c50164716', 'sex', '女', '2', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('5fd43fcbea06496aa1962cdadd3dd10a', 'accountBank', '工商银行', 'ICBC', null, '2', '1', '招商银行', null);
INSERT INTO `sys_dict_data` VALUES ('6030da48f82e43468c4ebf96f95fc744', 'investStatus', '投资失败', '2', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6244a23f76ce4a8fb3c1a2771fd1669c', 'qualificationType', '还款能力证明', 'repaymentAbility', null, '6', '1', '还款能力证明', null);
INSERT INTO `sys_dict_data` VALUES ('627e7993b8714f9f9c5391401d443ec2', 'vipLevel', 'VIP4及以上', '4', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('62cbc3ff9890448aa3f2cdfb78fe41ab', 'aprType', '6%以下', '1', null, '1', '1', '6%以下', 'n<6');
INSERT INTO `sys_dict_data` VALUES ('639f2d68feaa4f84b3a2e6f07c2dd951', 'sex', '男', 'M', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('6426a7311c6543978bca5a03a8b6693f', 'sysLogContent', '用户积分记录', 'user_score_log', null, '49', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6537e980098f4f84a165e1ec17d3c93f', 'rechargePayWay', '网银充值', '0', null, '1', '1', '网银充值', null);
INSERT INTO `sys_dict_data` VALUES ('657d5fc60a024ec8bd2481bb7f992f67', 'user_security_question', '您母亲的姓名？', 'question_mother_name', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('65c6b686e91d472f967b1c95e426dba8', 'timeCondition', '1个月以下', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('66c254849bc54631a1122c0947c58361', 'orgLevel', '二级', '1', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('6797e4ff774b40ad9c6acdc944ed35ec', 'productStatus', '发布待审', '1', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('67a69d8d7bfd4f86a27bd502e73f1b2c', 'accountBank', '兴业银行', 'CIB', null, '9', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('67ef4715946f44458a0048f89e1506e1', 'sysLogContent', '提现列表', 'cash_list', null, '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('68feaaa4f9c447d28ab442a0ee41b90b', 'realNameStatus', '认证通过', '1', null, '2', '1', '认证通过', null);
INSERT INTO `sys_dict_data` VALUES ('69306d27cfbe4f39877ff6da56270822', 'investStatus', '超时取消', '5', null, '6', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('6959791438214b0284b75379834d55fb', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe606;&lt;/em&gt;', '&amp;#xe606;', null, '8', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('69d89405b65740b5ae2081b0ab38d2c0', 'qualificationApplyStatus', '待审核', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6a0819fb1a474f7db94c7f8a2e18f79f', 'remainDaysType', '90天~180天', '3', null, '3', '1', '90天~180天', 'n >= 90 &&  n < 180');
INSERT INTO `sys_dict_data` VALUES ('6a84955cf9a14211b7abfe376d814f64', 'activityLogStatus', '已使用', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6aa67a973b4e409ca441ea58ff54e2d4', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe605;&lt;/em&gt;', '&amp;#xe605;', null, '10', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('6aac2465e4fb48c989d6614b5e8be393', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe611;&lt;/em&gt;', '&amp;#xe611;', null, '3', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('6b131942e43342aeb16cb26480bbb831', 'buyStyle', '部分投资', '0', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6ba8c29fdb3d4ca29193f0f63f381acf', 'rechargeStatus', '处理中', '0', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6bf5eca9de284c31a7d39e90eb49f7d4', 'specificSale', '不定向', '0', null, '0', '1', '定向销售默认值，不做定向限制', null);
INSERT INTO `sys_dict_data` VALUES ('6c5c9e13310a42168146655798bed704', 'investStatus', '已过期', '4', null, '5', '0', '已过期', '');
INSERT INTO `sys_dict_data` VALUES ('6c70091d21974d9798a9eb015810d80a', 'sysLogMethod', '登陆', 'login', null, '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6cb987a196d5435b97e868dfd0a70276', 'sysLogContent', '产品分类', 'project_type', null, '39', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6d424754cc5b497c92d9609069ee1be2', 'borrowStatus', '待担保', '11', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6d5717b6350a4e6e923401af056947f6', 'sysLogContent', '风险评估答案', 'risk_answer', null, '58', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6d9dea033ba24399b051d5bfe5e1df05', 'amountCondition', '10万元以上', '4', null, '4', '1', '', 'n>100000');
INSERT INTO `sys_dict_data` VALUES ('6e40e3b326974546a315aa014d914535', 'sysLogContent', '调度任务', 'tpp_trade', null, '79', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('6fa45b81a5c44cdeadb7cac3b4fbbd01', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe607;&lt;/em&gt;', '&amp;#xe607;', null, '11', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('6fdb3dbdd0554a50b56b9f9454cc73f1', 'educationLevel', '博士后', '10', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('704e12ae92fe46c18015b2734f52efef', 'accountBank', '中国银行', 'BOC', null, '1', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('7061202f976b43e1b980307fd03edcf7', 'cashFeeType', '个人支付', '0', null, '1', '1', '个人支付', null);
INSERT INTO `sys_dict_data` VALUES ('70ed3174a8b84bfea7664a8f0a7427a6', 'workExperience', '0-3年', '1', null, '1', '1', '工作年限', null);
INSERT INTO `sys_dict_data` VALUES ('7113e658469c4863a88e97acb2b8a41d', 'salaryRange', '50-80万', '9', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('720ce19f57874fa1b8294ac185e4372b', 'sysLogContent', '变现', 'realize', null, '56', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7239001aba764c9d9748923297ef658c', 'productStatus', '已完成', '9', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7255cc7f56a74f9fa245af854eade243', 'sysLogContent', '债权规则', 'bond_rule', null, '17', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('725fdfea402148669028161c9c67dc95', 'educationLevel', '其他', '9', null, '10', '1', '其它', null);
INSERT INTO `sys_dict_data` VALUES ('7263b846c7304763b34ccd7ea9aa701b', 'protocolType', '注册用户协议', 'register_protocol', null, '1', '1', '注册用户协议', null);
INSERT INTO `sys_dict_data` VALUES ('7278c603daf5413791b83dcaef150260', 'accountBank', '光大银行', 'CEB', null, '8', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('73ffa518ce92427ea650de5a85fbba4f', 'enable', '禁用', '0', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('741c3b9ffbb64408bc901b606423396e', 'cashStatus', '提现处理中', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('754948ababf24dfd893f436cce931bd6', 'sysLogMethod', '编辑', 'edit', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('765214ace71845e993edc575e433b734', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60f;&lt;/em&gt;', '&amp;#xe60f;', null, '18', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('769940f1f0bd400b8133d931965d00a5', 'sysLogContent', '用户礼包', 'user_gift', null, '53', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('76bfc24179654f2fa34236f03335815a', 'accountBank', '深发银行', 'SDB', null, '16', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('76f64d2dccae4c0f892de1eebeeb8cc4', 'interestStyle', 'T+N计息', '2', null, '2', '1', 'T表示投资交易成功时间，N个自然日后，开始计息', null);
INSERT INTO `sys_dict_data` VALUES ('76fb4ca220294b978f3fb63ba0342e77', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe617;&lt;/em&gt;', '&amp;#xe617;', null, '30', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('77063198557d46af936f726fc1c35179', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe615;&lt;/em&gt;', '&amp;#xe615;', null, '1', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('7816006920334e0c9d3937bb1e8a9b79', 'projectStatus', '待上架', '40', null, '6', '1', '发布审核成功，上架管理已处理未到上架时间', null);
INSERT INTO `sys_dict_data` VALUES ('78eded0fafcd43318914ae529b6d0354', 'sysLogContent', '菜单', 'menu', null, '69', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7967ed2cb87442799c36556c68fce246', 'bespeakLimitTime', '3-6个月', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7a691ab51a1a4320a511346559792cca', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe604;&lt;/em&gt;', '&amp;#xe604;', null, '9', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('7af563f9245d4730a98994ef5b2daaa4', 'sysLogContent', '借款成立审核', 'borrow_establish', null, '27', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7c2c58c0e152499f9bcc4fc87df49e47', 'workExperience', '5-8年', '3', null, '3', '1', '工作年限', null);
INSERT INTO `sys_dict_data` VALUES ('7db368677d3b4dd6b843703279b600b6', 'sysLogContent', '借款', 'borrow', null, '21', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7defd915ff6b4ffb98bec4f3c7e2ebbd', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe609;&lt;/em&gt;', '&amp;#xe609;', null, '12', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('7e0ee6472733448482469f07cadeb07e', 'borrowNature', '公司借款', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7e1ed372c8584b4ead9d9e2fcfd8b6ac', 'salaryRange', '4-8万', '3', null, '3', '1', '5000-8000元', null);
INSERT INTO `sys_dict_data` VALUES ('7e9ed36c6bfb4c838ebb88d2ed6bbda2', 'borrowStatus', '募集中', '4', null, '7', '1', '4', null);
INSERT INTO `sys_dict_data` VALUES ('7ea2233a60324f36a46ebe1f9b2a3683', 'borrowStatus', '担保被拒', '12', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('7f6f6451a7de48159387ee9ef9f5f97f', 'accountType', '垫付成功', 'pay_for_success', null, '4', '0', '', null);
INSERT INTO `sys_dict_data` VALUES ('80507bc3b0a44b2d8422e1773a40ac04', 'houseStatus', '有', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('80720cbabe2540b4999b5f2323a14b0b', 'cardType', '军官证', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('80758afe901149eb8e01879ba78ca3d2', 'realNameStatus', '未认证', '0', null, '1', '1', '未认证', null);
INSERT INTO `sys_dict_data` VALUES ('8206439655eb4794a004ce9b75982b90', 'sysLogContent', '业务统计', 'sale_statistics', null, '81', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('823662eaf718455d8a6dbd6b978ad81f', 'projectProcessNode', '审核', 'verify', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('826226a077244d0b95a4003816a3dafd', 'saleChannel', 'PC', '1', null, '1', '1', 'PC端上架', null);
INSERT INTO `sys_dict_data` VALUES ('8342acfb9bc94c2d973e415d30341111', 'accountStatus', '处理中', '0', '2016-07-12 11:35:42', '0', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('8342acfb9bc94c2d973e415d30342222', 'accountStatus', '成功', '1', '2016-07-12 11:36:22', '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('8342acfb9bc94c2d973e415d30343333', 'accountStatus', '失败', '2', '2016-07-12 11:37:05', '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('8342acfb9bc94c2d973e415d303448ba', 'sendStatus', '发送成功', '1', '2016-06-22 16:32:40', '2', '1', '发送成功', null);
INSERT INTO `sys_dict_data` VALUES ('8351f5538bf242119c7a7130bf2c57df', 'user_security_question', '您姐姐的姓名？', 'question_sister_name', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('83893edc1a63467d9006605816c8036a', 'tppType', '冻结', 'freeze', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8392a4bf0cca49d4ae5d84322dfd95d9', 'tppStatus', '失败', '2', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('839e1c06ef0e4994aa4fe9cd87e191c1', 'platAccountType', '垫付加息利息', '7', null, '7', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('83e41f46e320409aa620837a2b82d03f', 'salaryRange', '2-4万', '2', null, '2', '1', '3000-5000元', null);
INSERT INTO `sys_dict_data` VALUES ('842f976f0ad04d4e920ef62384026dd2', 'accountType', '解冻已变现金额', 'unfreeze_realize_money', null, '73', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('84e0ac8abf164982bdd53706e66738e7', 'qualificationType', '驾驶证', 'driverLicense', null, '4', '1', '驾驶证', null);
INSERT INTO `sys_dict_data` VALUES ('8568b255ca374f32bb21ed395ef9fbbd', 'bondStatus', 'test', '1', null, '5', '0', '', '');
INSERT INTO `sys_dict_data` VALUES ('85f7d7e06ca643e7811cfced74cef370', 'saleChannel', '微信', '3', null, '3', '1', '微信端上架', null);
INSERT INTO `sys_dict_data` VALUES ('8625917f432f49fd8cef30232c00f6b4', 'sysLogMethod', '成立审核', 'establish', null, '14', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('867a1c269fdc409f9bc404d80756ba3a', 'productStatus', '待售', '41', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('869017e88cc642bb9a7607c9ba7b445e', 'protocolType', '变现投资协议', 'realize_invest_protocol', null, '4', '1', '变现投资协议', null);
INSERT INTO `sys_dict_data` VALUES ('86c6b02a38774d7f98995acc9fc3158d', 'realizeDayCondition', '30天以下', '1', null, '1', '1', '30天以下', 'n &lt; 30');
INSERT INTO `sys_dict_data` VALUES ('86d1d3cad4274a5991efb0eb7db3d719', 'sysLogContent', '账户列表', 'account_list', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('86d3927a774f455d95912204abf45b49', 'borrowUse', '生活周转', '生活周转', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('87bc6b462d59404eb29181a54a3e42f6', 'projectStatus', '未成立', '7', null, '12', '1', '未成立', null);
INSERT INTO `sys_dict_data` VALUES ('8836ec8ff40241c0a6736173e3654bb6', 'sysLogContent', '企业用户', 'user_company', null, '82', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8880c1346fc449f09155ad2cd6b13cc9', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61d;&lt;/em&gt;', '&amp;#xe61d;', null, '22', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('88fd7264f70d432086d4406fc229b733', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60b;&lt;/em&gt;', '&amp;#xe60b;', null, '14', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('8914d73c3ab741dd90eaba8eed1b13d5', 'repayStatus', '未还款', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('895bb52b3be04b989f28aacd25d52bef', 'accountType', '冻结已变现利息', 'freeze_realize_interest', null, '91', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('89f8c8db84c64ec999464c0d0247871e', 'logTemplateType', '积分日志', '2', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('8ad79df1d72f4480875f1ebdde89472f', 'educationLevel', '硕士', '7', null, '7', '1', '硕士', null);
INSERT INTO `sys_dict_data` VALUES ('8ba760d98dc34394ab2ab6012ea246b1', 'userFreeze', '变现', 'realize', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8be06dfc314c45158d6b82b4e9e8a1dd', 'accountType', '转让溢价收益', 'bond_sell_earn', null, '1', '1', '转让溢价收益', null);
INSERT INTO `sys_dict_data` VALUES ('8c1927162018404996f489201fa62c75', 'sysLogContent', '产品列表', 'product_list', null, '34', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8c31e87e61c64c1a8b9be33416e431a9', 'sysLogContent', '加息规则', 'rate_rule', null, '46', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8c9dbab9f8454e1da07a2d22345ba826', 'borrowStatus', '坏账', '88', null, '12', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8d1edcc7e9294cf48c40de6886599bb2', 'sysLogContent', '经纪人客户', 'operator_customer', null, '80', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8d589b7560244bcfa42a3b02e9ed7a37', 'account_type', '待收奖励', 'wait_award', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8e7c357cf56c4be28710610b9e539737', 'sysLogContent', '审核记录', 'verify_log', null, '30', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('8f8e4e1f0f61484980795a474fd11cd6', 'saleStyle', '按金额', '1', null, '1', '1', '按金额', null);
INSERT INTO `sys_dict_data` VALUES ('900ee0fd74414675bfd3dbcb1331be34', 'awardType', '加息券', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('904dbe01ee224253a79f381fa7fbdf0a', 'investStatus', '订单失效', '6', null, '7', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('92104836d23048649e7a6a1c6e4040e0', 'productStatus', '发布审核被拒', '3', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('931b430e71c4416ab38d7e76ee9d6fb5', 'bondStatus', '前台手动撤回', '5', null, '4', '1', '前台手动撤回', null);
INSERT INTO `sys_dict_data` VALUES ('935a8890015845feba7753658db6a4bd', 'sysLogContent', 'vip等级', 'vip_level', null, '52', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('947311aa8a124e57be0049f52e55eb84', 'investType', '自动投资', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('94923051cac54396a928b9d10af9e6b9', 'sysLogContent', '预约跟进', 'borrow_bespeak', null, '19', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('949cbe42ea034271b3449b1f8ca413db', 'sysLogMethod', '下架', 'stop', null, '13', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('95900aa46b1a4228aebe038b10911c73', 'sysLogContent', '产品上架', 'product_sale', null, '36', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('95c8a28e043d463a8f56562c632a6b28', 'realizeAprCondition', '10~14%', '3', null, '3', '1', '', 'n &gt;= 10 &amp;&amp; n &lt;14');
INSERT INTO `sys_dict_data` VALUES ('95d49009752a4b35a0852b04df68ee0c', 'projectStatus', '发布审核通过', '2', null, '5', '1', '审核通过', null);
INSERT INTO `sys_dict_data` VALUES ('95ec1ff0b19d4e009a0d01eb8e1c4913', 'projectProcessNode', '上架', 'sale', null, '4', '1', '上架', null);
INSERT INTO `sys_dict_data` VALUES ('96050540c307437892bbe4e98d025c07', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe627;&lt;/em&gt;', '&amp;#xe627;', null, '36', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('9626b0d38f8d43f7b4fa3c719827f421', 'rechargeStatus', '充值成功', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('968965c94c9c4981a1ce0c66707f9400', 'projectStatus', '发布审核被拒', '3', null, '6', '1', '审核被拒', null);
INSERT INTO `sys_dict_data` VALUES ('971fae287de4472d9d80a751f02966d4', 'userType', '企业用户', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('9746d5a81b13483aab08ab9b6f4877fd', 'qualificationType', '信用报告', 'creditReport', null, '5', '1', '信用报告', null);
INSERT INTO `sys_dict_data` VALUES ('97497905f69049dc9f8d33fdfacf68dc', 'sysLogContent', '角色', 'role', null, '76', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('97abaacf8207411490f213abbdc390c9', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe620;&lt;/em&gt;', '&amp;#xe620;', null, '34', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('988fbe949dd546f887c779c6b3846346', 'borrowStatus', '已完成', '9', null, '11', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('99aedd7422f0490c8fad93809d8d2063', 'repayType', '垫付中', '2', null, '2', '1', '垫付中', null);
INSERT INTO `sys_dict_data` VALUES ('9a3d984f781946be80cca9baf1f204cb', 'activityLogStatus', '已作废', '3', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('9c94449ee65b43f7a19e1b81f4d2c9ff', 'projectStatus', '担保被拒', '12', null, '3', '1', '担保被拒', null);
INSERT INTO `sys_dict_data` VALUES ('9dd054547a4e42668a93b3a68d426b3b', 'sysLogContent', '产品下架', 'product_stop', null, '37', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('9ea5f6a218874580b2381aa40fb733de', 'zannacount', 'zanna1', '12', null, '2', '0', '撒旦法吃', '士大夫');
INSERT INTO `sys_dict_data` VALUES ('9f2b1a9dee904402b62db0d8ec556a8d', 'productStatus', '还款中', '8', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('9f32a2abd6314fb1be180cb7ab45b062', 'investStatus', '退款处理中', '3', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('9feb92a560614ca4a4f3b918f20b69cd', 'accountBank', '平安银行', 'PAB', null, '15', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('a1873b3580b14cc59e6e92c0965a52c3', 'salaryRange', '20-30万', '7', null, '7', '1', '15000-20000元', null);
INSERT INTO `sys_dict_data` VALUES ('a1a99753dade486b99a344faeeae4b2c', 'protocolType', '债权投资协议', 'bond_invest_protocol', null, '3', '1', '债权投资协议', null);
INSERT INTO `sys_dict_data` VALUES ('a1bf3d02c25445009af253e2bfcf4ad6', 'cashStatus', '提现待审核', '2', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a2c8cf0949ac4f5c8cd2f5f675c2ada7', 'orgLevel', '一级', '0', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('a2d81239989343b79c2ef050ae701e02', 'bondInvestStatus', '投资作废', '4', null, '5', '1', '投资作废', null);
INSERT INTO `sys_dict_data` VALUES ('a38ed744128d491fa51539aadb4eb825', 'carStatus', '有', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a52a61a6c37b441eab760332988f10e0', 'projectProcessNode', '修改', 'edit', null, '2', '1', '修改', null);
INSERT INTO `sys_dict_data` VALUES ('a5533dc35e3d40368066874e8794c50b', 'accountType', '还款利息', 'repaid_interest', null, '21', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a6ca1526d86a4a55841e6116e7874f3c', 'configType', '业务规则', '2', '2016-09-26 13:41:37', '2', '1', '业务规则', null);
INSERT INTO `sys_dict_data` VALUES ('a72d6bb5b052435f874b1e1152e50e83', 'platAccountType', '账户充值', '1', null, '1', '1', '账户充值', null);
INSERT INTO `sys_dict_data` VALUES ('a7dd5c05c1c011e6817dfa163e88e3b3', 'myLoanStatus', '待审核', '40', '2016-12-14 13:46:27', '5', '1', '待上架', null);
INSERT INTO `sys_dict_data` VALUES ('a7dd6a97c1c011e6817dfa163e88e3b3', 'myLoanStatus', '已作废', '30', '2016-12-14 13:46:27', '13', '0', '', null);
INSERT INTO `sys_dict_data` VALUES ('a7e01955c1c011e6817dfa163e88e3b3', 'myLoanStatus', '成立待审', '5', '2016-12-14 13:46:27', '8', '1', '已下架的借款、募集金额已满的借款、募集时间结束的借款', null);
INSERT INTO `sys_dict_data` VALUES ('a7e01b7dc1c011e6817dfa163e88e3b3', 'myLoanStatus', '募集中', '41', '2016-12-14 13:46:27', '6', '1', '待售', null);
INSERT INTO `sys_dict_data` VALUES ('a7e01d3ac1c011e6817dfa163e88e3b3', 'myLoanStatus', '待审核', '11', '2016-12-14 13:46:27', '1', '1', '发布待审', null);
INSERT INTO `sys_dict_data` VALUES ('a7e02164c1c011e6817dfa163e88e3b3', 'myLoanStatus', '募集中', '4', '2016-12-14 13:46:27', '7', '1', '募集中', null);
INSERT INTO `sys_dict_data` VALUES ('a7e02488c1c011e6817dfa163e88e3b3', 'myLoanStatus', '审核未通过', '12', '2016-12-14 13:46:27', '2', '1', '担保被拒', null);
INSERT INTO `sys_dict_data` VALUES ('a7e0265ec1c011e6817dfa163e88e3b3', 'myLoanStatus', '还款中', '88', '2016-12-14 13:46:27', '12', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a7e02783c1c011e6817dfa163e88e3b3', 'myLoanStatus', '已还款', '9', '2016-12-14 13:46:27', '11', '1', '已完成', null);
INSERT INTO `sys_dict_data` VALUES ('a7e02872c1c011e6817dfa163e88e3b3', 'myLoanStatus', '待审核', '0', '2016-12-14 13:46:27', '0', '1', '新增', null);
INSERT INTO `sys_dict_data` VALUES ('a7e02964c1c011e6817dfa163e88e3b3', 'myLoanStatus', '还款中', '8', '2016-12-14 13:46:27', '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a7e0325cc1c011e6817dfa163e88e3b3', 'myLoanStatus', '待审核', '1', '2016-12-14 13:46:27', '3', '1', '发布审核被拒', null);
INSERT INTO `sys_dict_data` VALUES ('a7e03398c1c011e6817dfa163e88e3b3', 'myLoanStatus', '审核未通过', '3', '2016-12-14 13:46:27', '4', '1', '产品的发布审核不通过；借款审核不通过', null);
INSERT INTO `sys_dict_data` VALUES ('a7e03489c1c011e6817dfa163e88e3b3', 'myLoanStatus', '未成立', '7', '2016-12-14 13:46:27', '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a8091ed9183f428a93c343280509c6a8', 'realizeAprCondition', '14%以上', '4', null, '4', '1', '', 'n &gt;= 14');
INSERT INTO `sys_dict_data` VALUES ('a87b83ba4f264507b4d804708f6c2abe', 'sysLogContent', '用户登陆', 'user_login', null, '68', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('a91daaab707c42fbb4c968aaf5eeeb19', 'papers_type', '其他', '1', null, '2', '1', '其他', null);
INSERT INTO `sys_dict_data` VALUES ('ab0d43cb46074ba0981981e94b83f095', 'bondMoneyType', '大于100000元', '4', null, '4', '1', '大于10000元', 'n>=100000');
INSERT INTO `sys_dict_data` VALUES ('ac4b5d0f7022416583fd06bbe2b8e536', 'sysLogMethod', '执行', 'do', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('acb91624afd141b5aa5a81d3529a62f1', 'RealizeSellStyle', '部分变现', '0', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('adabb91a3f864eeeb518555853c07c21', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe61c;&lt;/em&gt;', '&amp;#xe61c;', null, '28', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('ae4d7f20ff6242ca847b4c314bc77a2f', 'maritalStatus', '未婚', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ae6a61052c27445d85d142c1a42a2eee', 'borrowStatus', '新增待维护', '0', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('af243f6c6aaa45e392322718abe4406d', 'workExperience', '3-5年', '2', null, '2', '1', '工作年限', null);
INSERT INTO `sys_dict_data` VALUES ('af3175cc44084957b5abf99fa9b4f0cc', 'accountBank', '交通银行', 'BCM', null, '6', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('afcac0acbce341f5b7b23ce12374dc09', 'accountBank', '华夏银行', 'HXB', null, '10', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('b0f90785d14247889c876ff5b970d36c', 'accountType', '还款本金', 'repaid_capital', null, '22', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b13b5e7202984da8b4beaf478b143648', 'productStatus', '已作废', '30', null, '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b15e87a0a0e84b589d72bf945578a986', 'educationLevel', '博士', '8', null, '8', '1', '博士', null);
INSERT INTO `sys_dict_data` VALUES ('b19771966810443797ecd689e2d00854', 'platAccountType', '利息管理费', '5', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b19a2bdbbcea44dc85e3543202fd3cd4', 'userFreeze', '投资', 'invest', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b1e92adfddb54a9d8179eac173f0d1f2', 'sysLogContent', '变现规则', 'realize_rule', null, '57', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b222306f7e6b4ff2af5d908f89572b97', 'accountType', '转让手续费', 'bond_buy_fee', null, '12', '1', '转让手续费', null);
INSERT INTO `sys_dict_data` VALUES ('b2544b145a6447b5bc06dfb56c4f191b', 'borrowStatus', '还款中', '8', null, '10', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b2c626d7874345a2a51f7abe263f5af3', 'tppStatus', '成功', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b31c0996c82144dcb49d1d29200ae87c', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe60c;&lt;/em&gt;', '&amp;#xe60c;', null, '15', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('b3752f31085146ab848898508314124c', 'vipLevel', 'VIP5及以上', '5', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b3ad5a68d83f4d7fafd5cfa7694a7ce7', 'cashStatus', '提现申请', '0', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b3e0f89696a7435aa621ba797bee0e60', 'platAccountType', '账户取现', '2', null, '2', '1', '账户取现', null);
INSERT INTO `sys_dict_data` VALUES ('b3ffa6f160b340de83aef9dad18fe12c', 'sysLogMethod', '导出', 'export', null, '7', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b43906e11eea4d44ae1aad4621d1bc07', 'realizeAmountCondition', '5~10万元', '3', null, '3', '1', '', 'n&gt; 50000 &amp;&amp; n&lt;= 100000');
INSERT INTO `sys_dict_data` VALUES ('b4ddb4ee10514414937385064f76852b', 'enable', '启用', '1', null, '1', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('b50550ff4740443db96c23fe9f9f5476', 'accountBank', '邮储银行', 'PSBC', null, '14', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('b51bf5345b4d4cfb97ee94959c6b7561', 'cashStatus', '提现成功', '3', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b58307348bcc4f57ba5ea39c1dc0c274', 'realizeStatus', '成立待审', '5', null, '7', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('b5896fe0615e495497f7f39900e00632', 'borrowUse', '其它借款', '其它借款', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('b5bd2807644f4b17b2aadc86e934840a', 'educationLevel', '大专', '5', null, '5', '1', '大专', null);
INSERT INTO `sys_dict_data` VALUES ('b5df4890f36a433fa0c2089bc572111d', 'qualificationType', '住房公积金', 'housingFund', null, '1', '1', '住房公积金', null);
INSERT INTO `sys_dict_data` VALUES ('b9b897faeb514ddc9e601cfd31738dd3', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe623;&lt;/em&gt;', '&amp;#xe623;', null, '32', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('b9de9b8a19d74d4c842c17e38e249ce9', 'workExperience', '10年以下', '6', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ba187265a38d43f4bab8cbc1583a5b6f', 'projectStatus', '已还款', '9', null, '18', '1', '还款成功', null);
INSERT INTO `sys_dict_data` VALUES ('ba5e27380011442fb0a102e8e236a423', 'realizeStatus', '借款中', '4', null, '1', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('bb2938fd28204427abab814c80014da6', 'productTimeLimit', '12个月以上', '3', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('bb64dd0aecd34af1a15e91e21578866c', 'educationLevel', '本科', '6', null, '6', '1', '本科', null);
INSERT INTO `sys_dict_data` VALUES ('bbb7599180b64d9b98c7ccaca42043f3', 'educationLevel', '高中', '3', null, '4', '1', '高中', null);
INSERT INTO `sys_dict_data` VALUES ('bbfaf6825f40452785fb494ffef18835', 'sysLogContent', '平台账户日志', 'merchant_log', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('bc32443d8a1944769d3235602d9905ad', 'accountBank', '农业银行', 'ABC', null, '4', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('bc72fcefc46842d0b6a5a5554de09965', 'remainDaysType', '30天~90天', '2', null, '2', '1', '30天~90天', 'n >= 30 && n < 90');
INSERT INTO `sys_dict_data` VALUES ('bc803db9552545c1a61f0587e9f9e3a5', 'sysLogContent', '担保机构', 'vouch_list', null, '89', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('bd713d36b60445869ea00f4703999394', 'accountBank', '浦发银行', 'SPDB', null, '11', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('be4b23c612f447318b15597acc804e94', 'salaryRange', '100万以上', '11', null, '11', '1', '11', null);
INSERT INTO `sys_dict_data` VALUES ('bf35062e1c234d5cbb969770e12bff99', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe610;&lt;/em&gt;', '&amp;#xe610;', null, '6', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('c07a4e44dbab487b9fe53a15c9064c9f', 'protocolType', '投资协议', 'invest_protocol', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c07de1bbcaee46ffbef57f102145ad54', 'qualificationApplyStatus', '审核通过', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c2769761bf6244ff92524a59fcdc22c1', 'realizeAprCondition', '6~10%', '2', null, '2', '1', '', 'n&gt;= 6 &amp;&amp; n&lt;10');
INSERT INTO `sys_dict_data` VALUES ('c5030964462b4e96ab185dde4791982e', 'maritalStatus', '已婚', '1', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c555725ecf40468395f8f7ccbcd79f97', 'isSingle', '单选', '1', null, '1', '1', '单选', null);
INSERT INTO `sys_dict_data` VALUES ('c56f9b39524542249d2cf0ae2f3645df', 'investStatus', '投资待处理', '11', null, '2', '1', '是指投资成功还未成立审核的产品', null);
INSERT INTO `sys_dict_data` VALUES ('c6d1f56a5656430db2d44987e31e9e12', 'sysLogContent', '资源', 'resource', null, '75', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c71a91c21ea847009cdcfa1f1d79e91b', 'accountType', '转让折价损失', 'bond_sell_duct', null, '1', '1', '转让折价损失', null);
INSERT INTO `sys_dict_data` VALUES ('c71fabb3d8244858b513f454920ce209', 'sysLogContent', '还款记录', 'repayment_record', null, '40', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c7a9ebd2fc01462fbe52ce02acfb9ae9', 'aprType', '10-14%', '3', null, '3', '1', '10-14%', 'n >= 10 && n <14');
INSERT INTO `sys_dict_data` VALUES ('c88ba01e570c461d9656b0dc34c56393', 'sysLogContent', '催收', 'repay_urge', null, '43', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c91cbe86c24d4a3cb7625e3192e229a1', 'sysLogContent', '意见反馈', 'opinion', null, '78', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('c962ab839375426f9291a37482544254', 'projectProcessNode', '成立审核', 'establishVerify', null, '6', '1', '成立审核', null);
INSERT INTO `sys_dict_data` VALUES ('c9ebd897ed164cf391aca09951f2d365', 'sysLogContent', '好友邀请', 'user_invite', null, '83', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('cb8c776c1ba442d18440fb47c4dec387', 'sysLogContent', '日志模板', 'log_template', null, '66', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('cbfb20bfed184b478f2158a726f63993', 'borrowStatus', '发布待审', '1', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('cc41d7aaa11848fc8493f01084bb1fbe', 'buyStyle', '全额投资', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('cc9386513a2e4a859a4fe6f27e63356d', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe600;&lt;/em&gt;', '&amp;#xe600;', null, '4', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('cd1bb27a752c4f769903b0bd021c59bb', 'borrowUse', '长期周转', '长期周转', null, '7', '1', '阿三', null);
INSERT INTO `sys_dict_data` VALUES ('ce1f82b090f145b9b85ca35d8c2470a7', 'salaryRange', '2万以下', '1', null, '1', '1', '3000元以下', null);
INSERT INTO `sys_dict_data` VALUES ('ce6952e2765a40399d8ff2bdcbc2243b', 'sysLogContent', '用户资金', 'user_account', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ce6f104e65974dd195dee0f7c8f8282d', 'realizeAmountCondition', '1~5万元', '2', null, '2', '1', '', '10000&lt;n &amp;&amp; n &lt;=50000');
INSERT INTO `sys_dict_data` VALUES ('cf3d3bb9656d4bc394b0b5f73cfa1008', 'sysLogContent', '消息记录', 'message', null, '70', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('cf469cc31f794005bad736d30e60db70', 'realizeDayCondition', '90~180天', '3', null, '3', '1', '90天~180天', 'n &gt;= 90 &amp;&amp; n &lt;180');
INSERT INTO `sys_dict_data` VALUES ('cfb9c1694b15436e92429a7f8b9e68e1', 'bondStatus', '自动撤回', '4', null, '3', '1', '自动撤回', null);
INSERT INTO `sys_dict_data` VALUES ('d074ec4fd66b47368788a289b0411c9d', 'userFreeze', '债权转让', 'bond', null, '5', '1', '债权转让', null);
INSERT INTO `sys_dict_data` VALUES ('d07764f18e594b26b6aa2698c8be5dfb', 'bespeakStatus', '未处理', '0', null, '1', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('d099151e67b342d2ab261cc0d6256e11', 'realizeAmountCondition', '1万元以下', '1', null, '1', '1', '', 'n&lt;= 10000');
INSERT INTO `sys_dict_data` VALUES ('d0c1b7d9dac942bc95410cc15d10be8a', 'timeCondition', '3-6个月', '3', null, '3', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('d1ed83a1a60a4502b502aa3e79b762b8', 'sysLogContent', '资金对账', 'account_check', null, '9', '0', '', null);
INSERT INTO `sys_dict_data` VALUES ('d28f1803c9d14f2791ea2222d6825432', 'realizeStatus', '逾期中', '87', null, '5', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('d291d500a98e497aa4e4cf815d3182ea', 'sysLogContent', '借款列表', 'borrow_list', null, '20', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d38199b1cab442649d8038b10777ee18', 'sysLogMethod', '查看', 'view', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d38f231cdaaf44988d115a0a346ac6db', 'cashFeeType', '平台垫付', '1', null, '2', '1', '平台垫付', null);
INSERT INTO `sys_dict_data` VALUES ('d44919ced8ce4182848bb5170a0c5347', 'realizeStatus', '逾期已还款', '91', null, '6', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('d57960a450b14f87bc5bd376d75f8bfc', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe619;&lt;/em&gt;', '&amp;#xe619;', null, '24', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('d5f2d5c92ad741d995f4050cada04f9c', 'activityLogStatus', '已过期', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d7b2a86facf740b793451a194ba9a0cb', 'saleChannel', 'APP', '2', null, '2', '1', 'APP端上架', null);
INSERT INTO `sys_dict_data` VALUES ('d7c486ba3fb742acad0bd633a6e0ea4b', 'userNature', '企业用户', '2', null, '2', '1', '企业账户', null);
INSERT INTO `sys_dict_data` VALUES ('d7c71ab6960e41259b6374f577d16ee6', 'sysLogContent', '风险等级', 'risk_level', null, '59', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d7f301f6a83a41ccb666088a9378c731', 'sysLogContent', '消息模板', 'message_type', null, '71', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d8b377e538b2413c895219f55ed82085', 'sysLogContent', '产品成立审核', 'product_establish', null, '38', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d913859444db432badcc5da76a3dcbf3', 'test', '类型1', '1', null, '1', '1', '类型1', '类型1');
INSERT INTO `sys_dict_data` VALUES ('d93998e9370c4a3c935c75624c0814c3', 'orgLevel', '三级', '2', null, '3', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('d977c93480cd42818f76c99207101d13', 'workExperience', '10年以上', '5', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('d9bb42c9f897479084edbcee3b924f2a', 'bespeakLimitTime', '12个月以上', '4', null, '4', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('dadf452dd3ac4e1e80e45e1a74a303a0', 'configType', '邮箱', '3', '2016-09-26 13:41:37', '3', '1', '邮箱', null);
INSERT INTO `sys_dict_data` VALUES ('db7fe3276df840dba31c66d0fde98dbd', 'rechargePayWay', '线下充值', '3', null, '3', '1', '线下充值', null);
INSERT INTO `sys_dict_data` VALUES ('dce0977d61374a5a884dbab82d508d6f', 'sysLogContent', '借款审核', 'borrow_audit', null, '24', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('dd0bb6abc11846f8a943e3f1ff2b7fb5', 'awardType', '红包', '1', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('de3565a2caf74195aa42f8c0c3801255', 'sex', '女', 'F', null, '2', '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('de3abfaac72b45f1a7da2046a32ea670', 'sysLogContent', '坏账', 'repay_bad_debt', null, '44', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('de97e76e8beb4a14b200c6ffad00c10e', 'sysLogContent', '栏目', 'section', null, '18', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('dfd584e80ba0455faae31e76ef242d01', 'projectStatus', '坏账', '88', null, '16', '1', '坏账', null);
INSERT INTO `sys_dict_data` VALUES ('e0b6dd753a0f492b9dea1ae53484fd51', 'repayStyle', '每季还息到期还本', '5', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e0e8076b28544ff59fb200e271df3e64', 'platAccountStatus', '待处理', '0', null, '1', '1', '待处理', null);
INSERT INTO `sys_dict_data` VALUES ('e11131922af0485aadc5ef3355e1f7a2', 'sysLogContent', '平台账户间转账', 'sub_merchant_transfer', null, '8', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e17932bf1e074753afae0d8697f49f7e', 'sysLogContent', '产品', 'product', null, '33', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e17b4241d51f46a596a7d6e6da028b29', 'maritalStatus', '离异', '2', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e3e958b398a047a6ba761312b8a3bfde', 'sysLogContent', '待收列表', 'collection_record', null, '31', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e44fc74f3ed147589e2ad9480c185bd5', 'bondInvestStatus', '待支付', '0', null, '1', '1', '待支付', null);
INSERT INTO `sys_dict_data` VALUES ('e45cb29d45f2417c8e6e8dc179b98798', 'userNature', '个人用户', '1', null, '1', '1', '个人账户', null);
INSERT INTO `sys_dict_data` VALUES ('e45e21cd83ab11e699af000c296f8d81', 'configType', '登录校验', '6', '2016-09-26 13:41:37', '6', '1', '登录校验', null);
INSERT INTO `sys_dict_data` VALUES ('e45e21cd83ab11e699af000c296f8d8c', 'configType', '验证码', '5', '2016-09-26 13:41:37', '5', '1', '验证码', null);
INSERT INTO `sys_dict_data` VALUES ('e4de5a02ac464e0caef7a8f167d78bea', 'realizeAprCondition', '6%以下', '1', null, '1', '1', '', 'n&lt;6');
INSERT INTO `sys_dict_data` VALUES ('e580a23cefbd4d1e963304c80da0dec8', 'vipLevel', 'VIP2及以上', '2', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e68204661a2c42089cda62051a276d18', 'rechargeFeeType', '平台垫付', '02', null, '2', '1', '平台垫付', null);
INSERT INTO `sys_dict_data` VALUES ('e77da6295c504261afaf0c2424546dc6', 'bespeakLimitTime', '6-12个月', '3', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e7e64c045f7146ab94f53199e84bfae5', 'sysLogMethod', '发放', 'grant', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e7eb723da54e41d4b7f9b4d4b4e0b429', 'projectStatus', '发布待审', '1', null, '4', '1', '发布待审', null);
INSERT INTO `sys_dict_data` VALUES ('e97462f35ba64d33a3b10dd8c3ec74b8', 'vipLevel', 'VIP3及以上', '3', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e97d1d1c895b420a80332ae29a29fe3f', 'vouchFreeze', '担保', 'vouch', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('e9e19b9b4fbd45c587894b3a3256e6be', 'sysLogContent', '提现人工核查', 'cash_check', null, '12', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ea06317743894fa2bcd21e32bf6b1505', 'projectStatus', '已作废', '30', null, '7', '1', '已作废', null);
INSERT INTO `sys_dict_data` VALUES ('ea2ebdbe35134133887ff21f7288df8d', 'platAccountType', '借款管理费', '6', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ea881c9b92fe4ddbaa892245cdae8021', 'educationLevel', '小学', '1', null, '1', '1', '小学', null);
INSERT INTO `sys_dict_data` VALUES ('eac1bab8baa6431781a6fd489a59aa16', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe634;&lt;/em&gt;', '&amp;#xe634;', null, '21', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('ead3a993d92643c59e2081de6e7b6f73', 'borrowStatus', '发布审核被拒', '3', null, '4', '1', '产品的发布审核不通过；借款审核不通过', null);
INSERT INTO `sys_dict_data` VALUES ('eb1ee626d51b4bdcb2b0e444798db128', 'educationLevel', '幼稚园', '11', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('eb32c278395146aaaec61cb1d09c8cf9', 'sysLogContent', '担保用户', 'vouch', null, '88', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('ed6f7f5877974d418492d374568ed69e', 'accountType', '受让投资', 'bond_buy', null, '1', '1', '受让投资', null);
INSERT INTO `sys_dict_data` VALUES ('edf40d0ba0ca489c883d5079143f6fdf', 'cashStatus', '提现失败', '4', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('eee5f83c16654d28ae2b68cc180ebdbd', 'platAccountStatus', '失败', '2', null, '3', '1', '失败', null);
INSERT INTO `sys_dict_data` VALUES ('ef23ed9cb6e74393987e5710f310f4c3', 'accountBank', '民生银行', 'CMBC', null, '7', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('ef8458c77c014747bde0129cab4ca5d1', 'specificSale', '定向邮箱域名', '3', null, '3', '1', '只有指定邮箱域名的用户可以投资', null);
INSERT INTO `sys_dict_data` VALUES ('efa56c53cd444cb4acdf14846bd6e317', 'productTimeLimit', '6-12个月', '2', null, '3', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('efbcf9e89eb84984b368c33650ab663e', 'timeCondition', '12个月以上', '5', null, '5', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('efdb50233c97480f813d00ecf3bbd5a0', 'salaryRange', '10-15万', '5', null, '5', '1', '10000-13000元', null);
INSERT INTO `sys_dict_data` VALUES ('efddc6b176e649d88c72f322225e1ec7', 'vouchFreeze', '充值', 'recharge', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f0078190017d4b57bec8f08e9bf9829a', 'workExperience', '8年', '4', null, '4', '1', '工作年限', null);
INSERT INTO `sys_dict_data` VALUES ('f007cba5342c487d94db82ebb6821624', 'projectStatus', '待售', '41', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f07dcedc3e324aa0afe973f6e3ad1bda', 'sysLogContent', '首页信息', 'index', null, '67', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f201657e22754749b89eb7e4df4cff2c', 'accountType', '变现管理费', 'realize_managefee', null, '77', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f2d19b6059b740c38570d987851c4c2c', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe628;&lt;/em&gt;', '&amp;#xe628;', null, '39', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('f447abc8e5934443b5f5082fb87e7905', 'projectStatus', '担保待审', '11', null, '2', '1', '担保待审', null);
INSERT INTO `sys_dict_data` VALUES ('f47eb2f71fa546c2bd1b0d5f861e758a', 'platAccountType', '逾期罚息给平台', '10', null, '10', '1', '逾期罚息给平台', null);
INSERT INTO `sys_dict_data` VALUES ('f48e2ba2a7594fef913a7dd8280bb8c1', 'tppType', '还款', 'repay', null, '5', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f4c7eefeeaeb402eb17ca5717c84fba3', 'sysLogContent', '活动方案', 'activity', null, '45', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f508fcfc3bef4f368e8fbffc280bafed', 'sysLogContent', '角色关联', 'operator_role', null, '73', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f527ea4e8caf47bd920e50d7233db921', 'sendStatus', '全部', '99', null, '0', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f5ca539f8e8943cc8fa5aa904f0539fc', 'menuIcon', '&lt;em class=&quot;iconfont&quot;&gt;&amp;#xe629;&lt;/em&gt;', '&amp;#xe629;', null, '40', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('f5fa088e7ca44566a84d93d843150725', 'companyQualificationType', '还款能力证明', 'repaymentAbility', null, '2', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f662c60e770149528dfd2a8c567c95d6', 'accountType', '成立放款', 'loan', null, '20', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f8d3ca5f621a4303b17099d5a916dff0', 'accountType', '投资成功', 'invest_success', null, '1', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('f9155dd559194080bb0a3200602e114f', 'realNameStatus', '认证未通过', '-1', null, '3', '1', '认证未通过', null);
INSERT INTO `sys_dict_data` VALUES ('f96a725e4cbf4f3086c1da16136c1413', 'sysLogContent', '红包规则', 'red_rule', null, '47', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fa2f74f21a0544958065c6920fe8de80', 'sysLogContent', '借款人', 'borrower', null, '22', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fb8c602198c948b0b569dd9359effb6e', 'sysLogContent', '加息券', 'user_rate', null, '50', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fb92e8d8868b4ace9a824ece35f851f9', 'repayStyle', '一次性还款', '2', null, '2', '1', '一次性还款', null);
INSERT INTO `sys_dict_data` VALUES ('fbb74f24cd984e8489298d72a67f138d', 'borrowStatus', '未成立', '7', null, '9', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fbe3043c03694c94b23dca0ec74d0772', 'vipLevel', 'VIP6', '6', null, '6', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fdc7af6c2ab344518ce1dec010897fe0', 'sysLogMethod', '上架', 'sale', null, '12', '1', '', null);
INSERT INTO `sys_dict_data` VALUES ('fe55b62a53fe472491af914789c3d045', 'rechargePayWay', '快捷充值', '2', null, '2', '1', '快捷充值', null);
INSERT INTO `sys_dict_data` VALUES ('feaeebcfd7e24432911830babd5bde92', 'projectProcessNode', '新增', 'add', null, '1', '1', '新增', null);
INSERT INTO `sys_dict_data` VALUES ('fed2f724f77a465d9cdea90c7a56707e', 'accountType', '冻结已变现金额', 'freeze_realize_money', null, '74', '1', '冻结已变现金额', null);
INSERT INTO `sys_dict_data` VALUES ('fee085959bd642179b38de5f4a43d4f8', 'accountBank', '中信银行', 'CNCB', null, '13', '1', '', '');
INSERT INTO `sys_dict_data` VALUES ('ff88cf28bd1a43fd9f1ac920240d0911', 'optType', '消耗', '0', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('ff88cf28bd1a43fd9f1ac920240d0912', 'optType', '获取', '1', null, null, '1', null, null);
INSERT INTO `sys_dict_data` VALUES ('ff88cf28bd1a43fd9f1ac920240d0920', 'platAccountType', '账户转账', '4', null, '4', '1', '账户转账', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','平安银行', 		 'PAB', null, '12', '1', '平安银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','建设银行', 		 'CCB', null, '4', '1', '建设银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','农行', 			 'ABC', null, '2', '1', '农行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '民生银行',		 'CMBC',null, '7', '1', '民生银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '渤海银行', 		 'CBHB',null, '9', '1', '渤海银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '交通银行', 		 'BCOM',null, '6', '1', '交通银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '工商银行', 		 'ICBC',null, '1', '1', '工商银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','广发银行', 		 'GDB', null, '10', '1', '广发银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','招行', 			 'CMB', null, '3', '1', '招行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','兴业银行', 		 'CIB', null, '8', '1', '兴业银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','中国银行', 		 'BOC', null, '5', '1', '中国银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '上海浦东发展银行', 'SPDB',null, '14', '1', '上海浦东发展银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','华夏银行', 		 'HXB', null, '11', '1', '华夏银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','光大银行', 		 'CEB', null, '15', '1', '光大银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '邮储银行', 		 'PSBC',null, '13', '1', '邮储银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'S3', '放款解冻', null, '9', '1', '放款解冻', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'F2', '撤标解冻失败', null, '7', '1', '撤标解冻失败', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'C1', '初始化', null, '2', '1', '初始化', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'O1', '其他', null, '10', '1', '其他', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'W4', '银行受理成功', null, '4', '1', '银行受理成功', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'S1', '交易成功,已清分', null, '1', '1', '交易成功,已清分', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'B1', '部分成功,部分冻结', null, '8', '1', '部分成功,部分冻结', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'F1', '交易失败,未清分', null, '5', '1', '交易失败,未清分', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'W3', '请求处理中', null, '3', '1', '请求处理中', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', 'S2', '撤标解冻成功', null, '6', '1', '撤标解冻成功', null);


-- ----------------------------
-- Table structure for `sys_dict_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `dict_type` varchar(64) DEFAULT NULL COMMENT '字典类型',
  `type_name` varchar(40) DEFAULT NULL COMMENT '类型名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典分类表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('00923cfc6d71423db5863f4d4a874f83', 'accountType', '用户资金类别', '2016-06-04 18:09:57', '');
INSERT INTO `sys_dict_type` VALUES ('00923cfc6d71423db5863f4d4a876456', 'accountStatus', '金额状态', '2016-07-12 11:34:45', '充值提现使用');
INSERT INTO `sys_dict_type` VALUES ('033673e493ea45b4a14471cb671886b4', 'vouchFreeze', '担保机构可冻结功能', '2016-10-09 19:29:17', '');
INSERT INTO `sys_dict_type` VALUES ('06eadd0e2d364a4dbcda9a59afd38e4a', '1111', '1111', '2016-10-31 20:42:43', '');
INSERT INTO `sys_dict_type` VALUES ('09b656bc9a4d43878cbabe4f446968c0', 'zannacount', 'zanna添加的', '2016-09-20 16:13:18', '随便添加一个看看');
INSERT INTO `sys_dict_type` VALUES ('0e8ad58f0b1f43e6b52b7fc4338e0544', 'rechargePayWay', '充值类型', '2016-08-04 13:39:01', '充值类型');
INSERT INTO `sys_dict_type` VALUES ('11121', 'messageType', '消息类型', '2016-06-22 13:46:55', '消息类型');
INSERT INTO `sys_dict_type` VALUES ('11122', 'sendType', '通知类型', '2016-06-22 14:03:46', '通知类型');
INSERT INTO `sys_dict_type` VALUES ('11123', 'borrowNature', '借款性质', '2016-06-27 13:30:26', '借款性质根据用户性质来');
INSERT INTO `sys_dict_type` VALUES ('11d44438a6e048eeb3efa1d236f2f311', 'optType', '积分行为', null, null);
INSERT INTO `sys_dict_type` VALUES ('11d44438a6e048eeb3efa1d236f2f351', 'platAccountStatus', '平台资金状态', '2016-07-29 15:47:10', '');
INSERT INTO `sys_dict_type` VALUES ('13276fae643641149b42419e53b0ed9c', 'buyStyle', '投资金额方式', '2016-07-25 14:07:41', '');
INSERT INTO `sys_dict_type` VALUES ('1365056938784bcfae3bf992566b4533', 'investStatus', '投资状态', '2016-07-29 13:16:39', '投资状态');
INSERT INTO `sys_dict_type` VALUES ('1b92cd0141124117ba8f691fb867fd36', 'interLanguage', '国际化语言', '2016-07-28 14:01:16', '国际化语言');
INSERT INTO `sys_dict_type` VALUES ('1e9130605f084156b10e6840f9262c35', 'productTimeLimit', '产品期限', '2016-06-29 17:43:14', '产品期限,产品管理条件查询枚举值');
INSERT INTO `sys_dict_type` VALUES ('2600e0429add4107878945c1bbce4a62', 'realizeStatus', '变现状态', '2016-09-12 16:52:20', '变现项目的状态');
INSERT INTO `sys_dict_type` VALUES ('2727226a2e554458962260ff509ecf9d', 'configType', '参数类型', '2016-08-23 18:02:40', null);
INSERT INTO `sys_dict_type` VALUES ('2a2779fe53124cd3bd71496d098e0db5', 'userNature', '用户性质', '2016-06-29 14:58:14', '用户性质');
INSERT INTO `sys_dict_type` VALUES ('2b192879ef2d4152a53ca0328764f872', 'salaryRange', '年收入', '2016-07-27 13:46:41', '年收入');
INSERT INTO `sys_dict_type` VALUES ('2c813424004a44a6a489ed7ef79c0fee', 'myLoanStatus', '我的借款状态', '2016-12-14 13:40:19', '我的借款-项目状态');
INSERT INTO `sys_dict_type` VALUES ('2ca8cea8b30d4ee0a76a6db612135ab2', 'vipLevel', 'vip等级', '2016-07-04 17:46:04', '');
INSERT INTO `sys_dict_type` VALUES ('318e15995d4547798ba53060ec856fb5', 'repayStatus', '还款状态', '2016-08-03 10:41:50', '还款状态');
INSERT INTO `sys_dict_type` VALUES ('31f3749ecc114584b7b87c5cfafaad6e', 'sysLogMethod', '操作方式', '2016-10-24 11:19:46', '系统日志操作方式');
INSERT INTO `sys_dict_type` VALUES ('3c8dd341b1d74a47a83c9a66955b0875', 'user_security_question', '用户密保问题', '2016-07-20 18:03:55', '用户密保问题');
INSERT INTO `sys_dict_type` VALUES ('3f7549af7e5c4a38a6de00fc16ef3531', 'protocolType', '协议类型', '2016-07-21 15:50:18', '协议模板类型标识（注册用户协议、借款协议、债权转让协议等）');
INSERT INTO `sys_dict_type` VALUES ('3f83b814545842fd9ae74316d9993a58', 'companyQualificationType', '企业资质认证资料类型', '2016-08-03 15:10:23', '企业上传资质认证资料类型');
INSERT INTO `sys_dict_type` VALUES ('41969ae9689346d5959ff43f863acefc', 'bondMoneyType', '债权专区查询条件剩余债权', '2016-08-18 18:11:47', '债权专区查询条件剩余债权');
INSERT INTO `sys_dict_type` VALUES ('44be6c29da4b4737bb9c27303742014c', 'borrowStatus', '借款状态', '2016-06-27 14:17:32', '借贷项目状态');
INSERT INTO `sys_dict_type` VALUES ('460547f61b4c4712927928e16e8fc942', 'projectStatus', '借贷状态', '2016-08-02 11:29:33', '我的借款-前台列表使用');
INSERT INTO `sys_dict_type` VALUES ('465d342f57d647f582ba4a5ff02f9057', 'sex', '性别', '2016-08-23 18:02:40', null);
INSERT INTO `sys_dict_type` VALUES ('471674cc094b49e6bdf1d456c335d420', 'papersStatus', '问卷状态', '2016-07-13 11:23:40', '问卷状态');
INSERT INTO `sys_dict_type` VALUES ('4c8eeac66af34a59bff610666c2c0a90', 'tppStatus', '第三方交易状态', '2016-09-27 15:43:47', '第三方交易状态：0-未处理，1-成功，2-失败');
INSERT INTO `sys_dict_type` VALUES ('4e840a359a6d489aaf660327aca35712', 'bespeakStatus', '预约借款状态', '2016-08-19 13:33:45', '');
INSERT INTO `sys_dict_type` VALUES ('520bbeee63024c328419bd64444932e4', 'specificSale', '定向销售', '2016-07-06 14:58:02', '产品定向销售类型：不限制；定向VIP等级；定向密码；定向邮箱；');
INSERT INTO `sys_dict_type` VALUES ('54129d1bfe984d6bb81ff78463104dc0', 'borrowUse', '借款用途', '2016-06-27 16:01:29', '');
INSERT INTO `sys_dict_type` VALUES ('567d54185c754e66ab92b007f682ee53', 'carStatus', '车产', '2016-08-03 21:52:02', '');
INSERT INTO `sys_dict_type` VALUES ('56e904cf1c004786af5a4cfcdb2308cc', 'bondStatus', '债权状态', '2016-08-05 10:59:28', '债权状态');
INSERT INTO `sys_dict_type` VALUES ('5e69c28e81054666a76fa13a5f406cb3', 'educationLevel', '学历', '2016-07-27 13:25:05', '学历');
INSERT INTO `sys_dict_type` VALUES ('5fe55dc2b1374e9ab8019017455923b6', 'realizeAmountCondition', '变现金额查询条件', '2016-08-19 13:41:46', '变现金额查询条件');
INSERT INTO `sys_dict_type` VALUES ('610c627792b04ccaaaa04ce70ec9e2a6', 'interestStyle', '计息方式', '2016-06-22 15:28:55', '计息方式');
INSERT INTO `sys_dict_type` VALUES ('6a510934c8394dd3b4df6928b3f3e9ad', 'workExperience', '工作年限', '2016-07-27 13:43:50', '工作年限');
INSERT INTO `sys_dict_type` VALUES ('6bdcfe862b5e4a24aacfe8ed1859bc1d', 'cardType', '证件类别', '2016-06-06 17:34:53', '');
INSERT INTO `sys_dict_type` VALUES ('736837ab2b3a4e15bc23444ebf097093', 'userFreeze', '用户可冻结功能', '2016-07-27 10:36:06', '用户可冻结功能');
INSERT INTO `sys_dict_type` VALUES ('738d5244a30b4c5f8b910dbf3072d269', 'accountBank', '银行列表', '2016-06-04 18:08:58', '');
INSERT INTO `sys_dict_type` VALUES ('754914792f8f4d69a0ab63d817fffc90', 'realNameStatus', '实名认证状态', '2016-07-30 10:56:46', '实名认证状态');
INSERT INTO `sys_dict_type` VALUES ('7565cfe44dd747289022a565e370b3ac', 'realizeDayCondition', '变现日期查询条件', '2016-08-19 13:43:34', '');
INSERT INTO `sys_dict_type` VALUES ('7cef2e452fb44e0c9f89bf75bc0e75f4', 'saleStyle', '销售方式', '2016-06-22 17:27:41', '销售方式');
INSERT INTO `sys_dict_type` VALUES ('81040a436946450b8d38778396aee877', 'rechargeFeeType', '充值垫付类型', '2016-08-04 13:37:28', '充值垫付类型');
INSERT INTO `sys_dict_type` VALUES ('8977532c90984f6cbd3aa0d2cd345c68', 'investType', '投资方式', '2016-07-29 14:09:56', '');
INSERT INTO `sys_dict_type` VALUES ('89cda4799171445c98f2b00b40242f58', 'rechargeStatus', '充值状态', '2016-07-23 10:16:23', '');
INSERT INTO `sys_dict_type` VALUES ('8f7e2ba76275463bbdb6f956b9376e68', 'projectProcessNode', '项目环节', '2016-09-26 17:45:10', '项目的流程环节，用于记录项目审核记录');
INSERT INTO `sys_dict_type` VALUES ('9058ffb6cb524e0ca0a0d4d596b91371', 'repayType', '还款类型', '2017-03-26 11:33:21', '还款类型（1正常还款 2 垫付  3 已还垫付4 逾期还款）');
INSERT INTO `sys_dict_type` VALUES ('9637d8e82b4942c7b4b29c4a944d04a9', 'aprType', '债权专区查询条件利率', '2016-08-18 20:20:10', '债权专区查询条件利率');
INSERT INTO `sys_dict_type` VALUES ('9645429e382845dab528f50bd5c22f49', 'maritalStatus', '婚姻状况', '2016-08-03 21:43:08', '');
INSERT INTO `sys_dict_type` VALUES ('985268face2e42df8c8a4768edd880c0', 'cashFeeType', '提现垫付类型', '2016-08-04 14:28:26', '提现垫付类型');
INSERT INTO `sys_dict_type` VALUES ('98e6548ddbd04cf38228fdcc8a148d26', 'timeType', '借款期限类型', '2016-06-27 15:32:43', '');
INSERT INTO `sys_dict_type` VALUES ('9d1483a353374d89a75081f6423eca9b', 'orgLevel', '机构层级', '2016-08-23 18:02:40', null);
INSERT INTO `sys_dict_type` VALUES ('a7413c1b19bf46fe924d10fa5bddbfde', 'papers_type', '问卷类型', '2016-07-13 11:25:43', '问卷类型');
INSERT INTO `sys_dict_type` VALUES ('a784b78371c34d49ae9aea24ff7d63a9', 'houseStatus', '房产', '2016-08-03 21:53:40', '');
INSERT INTO `sys_dict_type` VALUES ('ac60eb9209ec4801af7459aa0da1fadd', '答复', '是打发点', '2016-10-31 20:42:54', '大师傅大');
INSERT INTO `sys_dict_type` VALUES ('b494deca668045bf8df4d4deba0859df', 'timeCondition', '产品期限查询条件', '2016-08-11 17:02:16', '');
INSERT INTO `sys_dict_type` VALUES ('b88287c0b2044ac6a8ad155c71058910', 'saleChannel', '上架渠道', '2016-07-06 19:48:27', '上架渠道:PC,APP,微信');
INSERT INTO `sys_dict_type` VALUES ('ba2b025b359346289f6daa7dbd51e8ac', 'logTemplateType', '日志模板类型', '2016-06-04 09:36:32', null);
INSERT INTO `sys_dict_type` VALUES ('bb452a0602af4f95bc18321d6b6543da', 'userType', '用户类型', '2016-06-06 17:35:33', '');
INSERT INTO `sys_dict_type` VALUES ('bed93f86c78d414db32ee5a41023ba86', 'productStatus', '产品状态', '2016-10-12 19:48:37', '产品状态');
INSERT INTO `sys_dict_type` VALUES ('bf80442d72314a53a67bafebe4193a68', 'cashStatus', '提现状态', '2016-07-23 10:19:58', '');
INSERT INTO `sys_dict_type` VALUES ('bf8cfe2337d1442da1d61d58fc3c4691', 'test', '测试类型', '2016-08-23 16:03:00', '测试类型案例');
INSERT INTO `sys_dict_type` VALUES ('c62d18ad10e8407bb47f75e53fe65e3e', 'qualificationApplyStatus', '资质证明上传状态', '2016-08-03 19:03:39', '');
INSERT INTO `sys_dict_type` VALUES ('c63bf86c33cf4c09baf79e0640057e81', 'cashStatusWeb', '提现前台状态', '2016-10-19 21:06:01', '用户提现状态(前台展示)');
INSERT INTO `sys_dict_type` VALUES ('c7eb23df009546a092f38330598833fb', 'amountCondition', '产品金额查询条件', '2016-08-11 17:00:24', '产品金额查询条件');
INSERT INTO `sys_dict_type` VALUES ('c9060803e2ce47fd99a4a42749e48554', 'qualificationType', '个人资质认证资料类型', '2016-07-26 16:07:41', '个人上传资质认证资料类型');
INSERT INTO `sys_dict_type` VALUES ('ca1277ff48f94d728dfa45c9a0bcb7d4', 'realizeSellStyle', '变现金额方式', '2016-07-25 14:01:49', '变现金额方式');
INSERT INTO `sys_dict_type` VALUES ('d1c1ce8c19054a649e6a4bd7e93b3d2f', 'realizeAprCondition', '变现利率查询条件', '2016-08-19 13:46:54', '变现利率查询条件');
INSERT INTO `sys_dict_type` VALUES ('d326671f3cdd451986057a1694b0a3bd', 'awardType', '奖励类型', '2016-10-28 18:07:04', '');
INSERT INTO `sys_dict_type` VALUES ('d8726e1c8b754350ad71bb6ab3f4fb9c', 'yesNo', '是否', '2016-08-10 14:14:12', '是否标识');
INSERT INTO `sys_dict_type` VALUES ('dc4397c823db4788b6ab64487908d301', 'platAccountType', '商户资金类别', '2016-07-29 15:43:57', '商户资金类别');
INSERT INTO `sys_dict_type` VALUES ('df728772cd2145ffaee1b1621f217b4f', 'menuIcon', '菜单图标', '2016-08-26 16:53:02', '');
INSERT INTO `sys_dict_type` VALUES ('e020515e087d414c81171477b88eeae1', 'activityLogStatus', '邀请奖励状态', '2016-10-28 18:10:59', '');
INSERT INTO `sys_dict_type` VALUES ('e15686c5bfe14b2cbbb1b69da1763d46', 'userStatus', '用户状态', '2016-08-12 15:44:35', null);
INSERT INTO `sys_dict_type` VALUES ('e415925c59f346e6875d40ffec70dbfd', 'sysLogContent', '操作内容', '2016-10-24 11:18:15', '系统日志操作内容');
INSERT INTO `sys_dict_type` VALUES ('e5c879a01151471da814a7966c2505e0', 'bespeakLimitTime', '预约借款期限', '2016-08-10 20:16:11', '');
INSERT INTO `sys_dict_type` VALUES ('ed0d983af998494d96a8703140a50278', 'sendStatus', '发送状态', '2016-06-22 16:26:26', '消息发送状态');
INSERT INTO `sys_dict_type` VALUES ('ed69d1f6e8cd422599919264d6b93b96', 'bondInvestStatus', '债权投资状态', '2016-08-05 11:30:48', '债权投资状态');
INSERT INTO `sys_dict_type` VALUES ('ed7266c764f84529b338d4f37f7958df', 'tppType', '第三方服务类型', '2016-09-27 15:48:30', '第三方服务类型');
INSERT INTO `sys_dict_type` VALUES ('ee0e7c8db046468ebacb78c5994c4eec', 'No updated records', '逾期垫付', '2016-09-28 17:59:53', '回款成功');
INSERT INTO `sys_dict_type` VALUES ('f0c8f4e3a27c49298638e2bbfcb5d69b', 'authentication_type', '操作记录类型', '2016-06-06 15:25:51', '操作记录类型');
INSERT INTO `sys_dict_type` VALUES ('f22a41de2f244cfd83b84339acd9a38c', 'remainDaysType', '债权专区查询条件剩余期限', '2016-08-18 18:18:09', '债权专区查询条件剩余期限');
INSERT INTO `sys_dict_type` VALUES ('f614ecf27d9a458d8a1b2588945b2fdf', 'enable', '是否启用', '2016-08-23 18:02:40', null);
INSERT INTO `sys_dict_type` VALUES ('fcd16b7389c14d66a38289a436cf490e', 'isSingle', '是否单选', '2016-07-13 11:48:01', '是否单选');
INSERT INTO `sys_dict_type` VALUES ('ff6cd5a3abe240e7853cb0d76ff5ca4a', 'repayStyle', '还款方式', '2016-06-06 17:34:12', '');
INSERT INTO `sys_dict_type` VALUES (replace(UUID(),"-",""), 'cbhb_bank_card', '渤海银行--银行卡列表', '2017-03-01 13:48:02', '渤海银行--银行卡列表');
INSERT INTO `sys_dict_type` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', '渤海银行--交易状态枚举', '2017-03-02 20:00:09', '渤海银行--交易状态枚举');

-- ----------------------------
-- Table structure for `sys_letter`
-- ----------------------------
DROP TABLE IF EXISTS `sys_letter`;
CREATE TABLE `sys_letter` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `send_user` varchar(32) DEFAULT NULL COMMENT '发送用户ID',
  `receive_user` varchar(32) DEFAULT NULL COMMENT '接收用户ID',
  `title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `content` text COMMENT '内容',
  `status` char(1) DEFAULT NULL COMMENT '状态（0 新建，1发送成功 2发送失败）',
  `read_flag` char(1) DEFAULT NULL COMMENT '阅读标识(1已阅读，0未阅读，默认0)',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(512) DEFAULT '' COMMENT '备注: 发送结果信息',
  PRIMARY KEY (`uuid`),
  KEY `idx_sys_letter_receive_user_read_delete` (`receive_user`,`read_flag`,`delete_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站内信表（是message表的子集，提高前台用户查询效率）';

-- ----------------------------
-- Records of sys_letter
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `log_type` char(1) DEFAULT NULL COMMENT '日志类型',
  `ip` varchar(128) DEFAULT NULL COMMENT '操作IP地址',
  `request_uri` varchar(512) DEFAULT NULL COMMENT '请求URI',
  `request_method` varchar(128) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `exception` text COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `USER_ID` varchar(32) DEFAULT NULL COMMENT '登录用户id',
  `TAKE_TIME` decimal(10,4) DEFAULT NULL COMMENT '耗时(s)',
  `operation_content` varchar(32) DEFAULT NULL COMMENT '操作内容',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_log_template`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_template`;
CREATE TABLE `sys_log_template` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `log_type` int(4) DEFAULT NULL COMMENT '日志类型:1资金日志，2积分日志',
  `code` varchar(50) NOT NULL COMMENT '模板标识',
  `template_name` varchar(50) DEFAULT NULL COMMENT '模板名称',
  `template_content` varchar(1024) DEFAULT NULL COMMENT '模板信息',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`,`code`),
  KEY `idx_sys_log_template_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志模板';

-- ----------------------------
-- Records of sys_log_template
-- ----------------------------
INSERT INTO `sys_log_template` VALUES ('004679fc5cac410ca91b1e41abc8f56d', '1', 'realize_managefee', '扣除变现管理费', '扣除变现管理费${amount}元', '0', '2016-08-30 16:59:24', '扣除变现管理费');
INSERT INTO `sys_log_template` VALUES ('0483f641a15e4deeaa30de80bf60ced7', '1', 'freeze_realize_lateinterest', '变现冻结逾期罚息', '冻结逾期罚息${amount!}元', '0', '2016-08-30 17:47:14', '变现冻结逾期罚息');
INSERT INTO `sys_log_template` VALUES ('07113f8c4adf4bbca83fe598f36763be', '1', 'collect_interest', '收到利息', '${info!}还款，收到利息${amount}元', '0', '2016-08-29 14:19:50', '收到利息');
INSERT INTO `sys_log_template` VALUES ('15dabf7ce88c4cd5a5209d1f36eb5bb5', '1', 'bond_sell_capital', '转让人转让本金入账', '【${bondInfo!}】转让成功，本金入账${capital!}元', '0', '2016-08-10 17:44:14', '转让人转让本金入账');
INSERT INTO `sys_log_template` VALUES ('15ec4997946549348a9d005041164a6c', '1', 'collect_add_interest', '收到加息', '${info!}还款，收到加息${amount}元', '0', '2016-08-29 14:23:19', '收到加息');
INSERT INTO `sys_log_template` VALUES ('1dff73dd846b40dc92dbf89bb6c03d28', '1', 'borrow_fee', '借款管理费', '支付借款管理费${fee!}元', '0', '2016-10-08 18:11:03', '借款管理费');
INSERT INTO `sys_log_template` VALUES ('2748e39c2530439f89a57b399b288dcd', '1', 'first_tender_award', '第一次投资奖励', '收到第一次投资奖励${award}元。', '0', '2016-08-12 15:44:35', '第一次投资奖励金额日志。');
INSERT INTO `sys_log_template` VALUES ('2ba421e612ad4e21b6e1e486732bc67c', '1', 'freeze_realize_interest', '冻结已变现利息', '变现原产品${projectName!}还款，冻结利息${amount!}元用于变现还款。', '0', '2016-12-12 18:07:21', '冻结已变现利息');
INSERT INTO `sys_log_template` VALUES ('3dee7433ea19430b831d92ee7099a36d', '1', 'repay_late_interest_to_admin', '还款逾期利息给平台', '还款逾期利息，归还平台${amount!}元！', '0', '2016-10-26 15:38:21', '还款逾期利息，归还平台xxx元！');
INSERT INTO `sys_log_template` VALUES ('4ea9e81b914611e699af000c296f8d8c', '1', 'bond_sell_duct', '转让人转让成功折让扣除', '【${bondInfo!}】转让成功,扣除折让金额${earnMoney!}元。', '0', '2016-10-13 21:09:43', '转让人转让成功折让扣除');
INSERT INTO `sys_log_template` VALUES ('4f411f2e7daa4b60b2a0676e0ec31a25', '1', 'first_tender_award', '第一次投资奖励', '收到第一次投资奖励${award}元。', '0', '2016-08-23 18:02:40', '第一次投资奖励金额日志。');
INSERT INTO `sys_log_template` VALUES ('51d4db463bcc4b2ea5ebdd257e5bf494', '1', 'collect_late_interest', '收到逾期利息', '${info!}还款，收到逾期利息${amount!}元', '0', '2016-08-29 14:20:25', '收到逾期利息');
INSERT INTO `sys_log_template` VALUES ('56a7a52f7e104bafbd48e4413b3ff832', '1', 'invest_unfreeze', '投资失败', '投资失败，投资金额${amount!}，解冻实际支付金额${realAmount!}元。', '0', '2016-08-26 10:09:47', '投资失败解冻资金');
INSERT INTO `sys_log_template` VALUES ('58420e1718a845a9ba47af9e96aff04a', '1', 'repaid_merchant_late_interest', '逾期给平台利息', '${info!}还款逾期,给平台利息${amount!}元', '0', '2016-11-05 18:57:01', '逾期给平台利息');
INSERT INTO `sys_log_template` VALUES ('59d3495721d545ff9d2ae336d00c4c1a', '1', 'realize_income', '变现进账', '变现成功,收到变现金额${amount}元', '0', '2016-08-30 16:56:57', '变现进账');
INSERT INTO `sys_log_template` VALUES ('5d4d5809914511e699af000c296f8d8c', '1', 'bond_sell_earn', '转让人转让成功收益收取', '【${bondInfo!}】转让成功,赚取收益${earnMoney!}元。', '0', '2016-10-13 21:02:58', '转让人转让成功,收益处理');
INSERT INTO `sys_log_template` VALUES ('6030c83a60bc44e8a788e2b01eb1e349', '2', 'first_tender_award', '第一次投资奖励', '收到第一次投资奖励${award}元。', '0', '2016-11-28 14:56:16', '第一次投资奖励金额日志。');
INSERT INTO `sys_log_template` VALUES ('70eef852ae4b4fd48c96cacdc8abf525', '1', 'freeze_realize_capital', '冻结已变现本金', '变现原产品${projectName!}还款，冻结本金${amount!}元用于变现还款。', '0', '2016-08-29 14:26:24', '冻结已变现金额');
INSERT INTO `sys_log_template` VALUES ('802ee17049c342ca912ff2385ec4e28d', '1', 'interest_manage_fee', '利息管理费用', '${info!}还款利息，扣除利息管理费 ${amount}元。', '0', '2016-06-04 10:53:55', '利息管理费日志。');
INSERT INTO `sys_log_template` VALUES ('8c009f63fde0436c86b128f9aea2f83e', '1', 'bond_buy_fee', '转让手续费收取', '【${bondInfo!}】转让成功，收取手续费${fee}元。', '0', '2016-08-17 17:52:40', '转让手续费收取');
INSERT INTO `sys_log_template` VALUES ('9571007174c9446581e4fa09c5387133', '1', 'tender_award', '累计投资奖励', '用户名为${username}的用户累计投资奖励金额${award}元，已成功转入个人账户。', '0', '2016-06-04 09:49:05', '用户累计投资奖励金额日志。');
INSERT INTO `sys_log_template` VALUES ('9bbeedd116f649e295e5c63169276d59', '1', 'borrow_success', '借款入账', '借款【${projectInfo!}】成立，收到借款${amount!} 元', '0', '2016-10-08 18:06:28', '借款入账');
INSERT INTO `sys_log_template` VALUES ('9c019238db2b4c74b7bbe96c79df0d56', '1', 'collect_capital', '收到本金', '${info!}还款，收到本金${amount}元', '0', '2016-08-29 14:17:12', '收到本金');
INSERT INTO `sys_log_template` VALUES ('a0c4789529b548959312b3172bc62876', '1', 'invest_success', '投资成功', '投资项目【${projectInfo!}】成功，投资金额${amount}元，实际支付${realAmount}元', '0', '2016-08-30 16:50:54', '投资成功');
INSERT INTO `sys_log_template` VALUES ('b5101b3a4e3543079fd78f94cbbb5ba6', '1', 'first_tender_award', '第一次投资奖励', '收到第一次投资奖励${award}元。', '0', '2016-06-04 09:49:49', '第一次投资奖励金额日志。');
INSERT INTO `sys_log_template` VALUES ('b8938ea45477417080919b67cd5a9076', '1', 'bond_buy', '受让人投资成功', '购买债权【${bondInfo!}】成功，实际支付${realPay!}元，得到债权${tenderMoney!}元，待收本息${bondCollectionMoney!}元', '0', '2016-08-10 17:39:02', '债权转让受让人投资');
INSERT INTO `sys_log_template` VALUES ('bb58597e4c1d4690b9d5cc446dcba843', '2', 'score_email', '邮箱认证通过', '邮箱认证通过，获得${score}个积分。', '0', '2016-06-04 12:07:05', '积分日志：邮箱认证通过积分日志。');
INSERT INTO `sys_log_template` VALUES ('bf0c47f7c7b94bcc8525c0e46b28544b', '1', 'repaid_late_interest', '还款逾期利息', '${info!}还款逾期利息${amount!}元', '0', '2016-08-29 14:04:26', '还款逾期利息');
INSERT INTO `sys_log_template` VALUES ('d0f477addc9a4e679b5baa2418a4bbfa', '1', 'recharge_success_log', '充值成功', '用户账户充值成功，金额${money}元。', '0', '2016-06-03 14:29:01', '充值手续费平台垫付');
INSERT INTO `sys_log_template` VALUES ('d0f477addc9a4e679b5baa2418a4bbfc', '2', 'back_manage_fee_level', '根据会员积分等级，退还利息管理费', '[<a href=\'${web_url}/invest/${borrow.uuid!}/detail.html\' target=_blank>${borrow.name}</a>]，根据会员积分等级，退还利息管理费${money}元。', '0', '2016-06-03 14:29:01', '根据会员积分等级，退还利息管理费日志。');
INSERT INTO `sys_log_template` VALUES ('e7ca28b241834692ba337a2b4298d326', '1', 'repaid_capital', '还款本金', '${info!}还款本金${amount!}元', '0', '2016-08-29 13:58:55', '还款本金');
INSERT INTO `sys_log_template` VALUES ('f11e905d9d7449c58ee1c1fa58d07f81', '1', 'invest_freeze', '投资冻结', '用户投资项目【${info!}】金额${amount!}元，冻结金额${realAmount!}元<#if redEnvelope?? && redEnvelope gt 0>，使用红包${redEnvelope!}元</#if><#if upApr?? && upApr gt 0>，使用${upApr!}%加息券</#if>。', '0', '2016-07-16 16:51:05', '投资冻结资金');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d7401111111', '1', 'cash_proccess', '提现处理中', '提现处理中，冻结提现金额${money}元。', '0', '2016-07-06 10:24:51', '申请提现日志。');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d7401111112', '1', 'cash_audit', '提现待审核', '提现待审核，冻结提现金额${money}元。', '0', '2016-07-06 10:24:51', '申请提现日志。');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d740217b912', '1', 'cash_bank_fail', '提现对账', '提现银行对账失败，退回提现金额${money}元。', '0', '2016-07-06 10:24:51', '提现异步对账日志。');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d740217b9e4', '1', 'cash_success', '提现成功', '提现成功，提现${amount}元，到帐${realAmount}元，手续费${cashFee}元。', '0', '2016-07-06 10:24:51', '提现成功日志。');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d740217b9e5', '2', 'fee', '扣除充值手续费', '${payment}扣除充值手续费:${deduct}元。', '0', '2016-06-04 10:24:51', '扣除充值手续费日志。');
INSERT INTO `sys_log_template` VALUES ('f97a8c7c45a242bfbcb72d740217b9e6', '1', 'cash_fail', '提现失败', '提现失败，退回冻结资金${money}元。', '0', '2016-07-06 10:24:51', '提现审核不通过日志。');
INSERT INTO `sys_log_template` VALUES ('fa5353225439429590340cfe0eff39c5', '1', 'unfreeze_realize_money', '解冻已变现金额 ', '解冻已变现金额 ${amount}元', '0', '2016-11-23 19:06:31', '解冻已变现金额 ');
INSERT INTO `sys_log_template` VALUES ('fc2fb482871e4a8c97c52da0044f0c81', '1', 'repaid_interest', '还款利息', '${info!}还款利息${amount!}元', '0', '2016-08-29 14:02:11', '还款利息');

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(64) NOT NULL COMMENT '菜单编号',
  `menu_name` varchar(40) NOT NULL COMMENT '菜单名称',
  `url` varchar(512) NOT NULL COMMENT 'url',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `parent_ids` varchar(512) DEFAULT NULL COMMENT '链接地址',
  `icon_css` varchar(256) DEFAULT NULL COMMENT '图标',
  `sort` int(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `is_leaf` char(1) DEFAULT '0' COMMENT '是否叶子节点，1是0否，默认0',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_menu_code` (`code`) USING BTREE,
  UNIQUE KEY `idx_sys_menu_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES(replace(UUID(),'-',''),'check','对账','',NULL,NULL,NULL,9,NULL,0,NULL,0);
INSERT INTO `sys_menu` VALUES ('034b7d6b6c104f89ab76c68b4a5e6343', 'set:operator', '个人设置', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe636;', '16', '2016-10-10 09:56:09', '0', '', '0');
INSERT INTO `sys_menu` VALUES ('07145b5a77aa4ffdbb41e96800ac0fbf', 'project:borrow:verify', '借款审核', '/loan/borrow/borrowVerifyManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '4', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('0738842d7db04e49b16f2735e0c24cd0', 'stat:account', '资金分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '5', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('086d419bcf8b4e12a29dc40ea7de0844', 'account:cashList', '提现记录', '/account/cash/cashList.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&amp;#xe603;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('099e7b2858994d3ca59fedc491634f68', 'project:borrow:bespeak', '预约跟进', '/loan/borrowBespeak/borrowBespeakManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', '&amp;#xe611;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('0a1b98124c854381a403f767def3d124', 'project:borrow:stop', '下架管理', '/loan/borrow/borrowStopManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '6', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('0a6829ac52064b16960c958e11daac8e', 'set:accountTpl', '资金记录模板', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', null, '7', null, '1', '待定', '1');
INSERT INTO `sys_menu` VALUES ('0ad428871db242999d4f7de1b700c01b', 'stat:borrow:borrowState', '借款情况分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '5', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('0b3d8734926b4514b3e5a171de5107e3', 'project:borrow:sale', '上架管理', '/loan/borrow/borrowSaleManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '5', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('0c6eede4546f4dadadd31dae718a7998', 'stat:invest:investArea', '投资地区分析', '#', '2efc0cf3524f449b964723d58397abab', '9b6b5489b9d947cdb73d209dde0ecf63:2efc0cf3524f449b964723d58397abab', null, '4', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('0e9cbe92293944ddb6a2b83310eb31fd', 'vip:qualification', '资质认证', '/user/userQualification/manage.html', '7193b8e131824023abd1fe88ceb285dd', '7193b8e131824023abd1fe88ceb285dd', '&amp;#xe626;', '4', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('0f934a2ed82b46cabd0e288161929eb0', 'consumer:consumerList', '客户列表', '/user/customer/customerManage.html', '70b917bfb77c49229f1b1c12e721690d', '70b917bfb77c49229f1b1c12e721690d', '&amp;#xe628;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('10037550244b4052a1a70956ef889e5f', 'set:contactStaff', '客服管理', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', null, '4', null, '1', '待开发', '1');
INSERT INTO `sys_menu` VALUES ('1345de60f67945179c0b7812d77e2346', 'stat:borrow:borrowUser', '借款人数分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '1', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('14d98400f07643b8b07071ae8951c15c', 'stat:invest:investState', '投资情况分析', '#', '2efc0cf3524f449b964723d58397abab', '9b6b5489b9d947cdb73d209dde0ecf63:2efc0cf3524f449b964723d58397abab', null, '5', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('168c490a9eba4a5ebf4c1550652492b9', 'stat:account:recharge', '充值统计分析', '#', '0738842d7db04e49b16f2735e0c24cd0', '9b6b5489b9d947cdb73d209dde0ecf63:0738842d7db04e49b16f2735e0c24cd0', null, '1', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('17199438e26e4ce5ab7dcd324868f0a0', 'stat:account:userBalance', '用户余额分析', '#', '0738842d7db04e49b16f2735e0c24cd0', '9b6b5489b9d947cdb73d209dde0ecf63:0738842d7db04e49b16f2735e0c24cd0', null, '3', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('172c5c180bef49c98d9836108b34cee8', 'set:riskTpl:riskLevel', '风险等级管理', '/risk/riskConfigManage.html', 'd8d3e092a09e4c36abde2260f1111a49', 'a0f88f24408a42759589270385618808:d8d3e092a09e4c36abde2260f1111a49', '&amp;#xe62b;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('191536e89e1c4fed89a57f41920dcb18', 'oper:score:scoreList', '积分记录', '/operate/score/userScoreLogManage.html', 'ce3536e101814111175dbc755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e101814111175dbc755a411145', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('1bab1129adec42e9ae38cd5fa10e16b9', 'stat:borrow:borrowArea', '借款地区分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '4', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('1e102fbed8a448098bb64ce12ed36c30', 'project:product:ransom', '赎回审核', '#', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '8', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('1f719034546143b783da0a82bbf5c4a1', 'project', '产品', '', null, null, null, '1', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('22b308bd36744d3e8c94894a899c0394', 'set:resource', '资源管理', '/sys/resource/resourceManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe635;', '5', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('2812eb5139054b00be156d3096cbd944', 'set:website:section', '栏目管理', '/column/section/sectionManage.html', '84f9a75ee24940ada9ea967d7825c8bc', 'a0f88f24408a42759589270385618808:84f9a75ee24940ada9ea967d7825c8bc', '&amp;#xe615;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('2efc0cf3524f449b964723d58397abab', 'stat:invest', '投资分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '3', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('2f700a22cf4349c891316352f719824d', 'product:bond', '转让管理', '#', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '&#xe606;', '4', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('31f91e34272e4eb4a7d1986de4ea150f', 'set:sys:role', '角色管理', '/sys/role/roleManage.html', '434db50f546d4b87b9f340eb3905b3ba', 'a0f88f24408a42759589270385618808:434db50f546d4b87b9f340eb3905b3ba', null, '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('32b5c1c2e04342e09f696d257552f896', 'stat:oper:addApr', '加息券统计分析', '#', 'dab0444b0a974a3281abadf26a8aade9', '9b6b5489b9d947cdb73d209dde0ecf63:dab0444b0a974a3281abadf26a8aade9', null, '2', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('356ab94e1cb94c9999ea11e3d9b15777', 'project:product:publish', '发布审核', '/product/manage/productPublishManage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('358b374116284401aa4d510b0231fcdb', 'stat:borrow:borrowMoney', '借款金额分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '2', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('3655012406c94acaacbe979d52e14900', 'set:riskTpl:question', '问卷管理', '/risk/riskPapersSingleManage.html', 'd8d3e092a09e4c36abde2260f1111a49', 'a0f88f24408a42759589270385618808:d8d3e092a09e4c36abde2260f1111a49', '&amp;#xe62a;', '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('376c627aac1748a1aa7faffb50c18187', 'oper:addApr:addAprList', '加息券列表', '/operate/rateCoupon/userRateCouponManage.html', 'ce3536e101814b7b975dbc755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e101814b7b975dbc755a411145', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('3975def04c7942aebe871a4dcfa7124c', 'set:msg', '消息管理', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe62d;', '5', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('39af852544484d0b9dce4cb7c99564ec', 'project:product:ev', '成立审核', '/product/manage/productEstablishVerifyManage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '7', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('3e4323cd2e924860865ede6d4f35e994', 'oper:actPlan', '活动方案', '/operate/activity/activityManage.html', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&amp;#xe61a;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('434db50f546d4b87b9f340eb3905b3ba', 'set:sys', '后台管理', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&#xe634;', '12', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('453b4c7da24e42e098644a8839cb2bfe', 'set:msg:msgType', '消息模板', '/sys/messageType/messageTypeManage.html', '3975def04c7942aebe871a4dcfa7124c', 'a0f88f24408a42759589270385618808:3975def04c7942aebe871a4dcfa7124c', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('4e951fb9c90d4679b92998b6bb6fa483', 'project:product:stop', '下架管理', '/product/manage/productStopManage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '5', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('54166c4a88e740fba422386ae21f3c05', 'stat:invest:investMoney', '投资金额分析', '#', '2efc0cf3524f449b964723d58397abab', '9b6b5489b9d947cdb73d209dde0ecf63:2efc0cf3524f449b964723d58397abab', null, '2', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('542782979aad4f02ad7111f6c9e765ee', 'set:sys:user', '后台用户管理', '/sys/operator/operatorManage.html', '434db50f546d4b87b9f340eb3905b3ba', 'a0f88f24408a42759589270385618808:434db50f546d4b87b9f340eb3905b3ba', null, '2', null, '0', null, '1');
INSERT INTO `sys_menu` VALUES ('54ccb1eda92f42728f4bfbcc5bd37d0c', 'stat:bond', '债权转让分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '4', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('604873a8ae66493fb1ac6c429c8803d6', 'project:borrow:advance', '垫付记录', '/loan/repayment/repaymentAdvance.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '14', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('610092f012f3442e96b1779f1fd30a67', 'project:borrow:borrowTest', '测试0001', 'www.baidu.com', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', '', '100', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('6282fd375d3948b9b511a577aa3715dc', 'account:userAccount', '用户账户列表', '/account/account/accountList.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&amp;#xe616;', '4', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('66ebb7d8daac40bfa6e2ad4e8104f1c1', 'project:product:sale', '上架管理', '/product/manage/productSaleManage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '4', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('6940d2925e36443f9630f229679fd945', 'project:borrow', '借贷管理', '#', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '&#xe601;', '3', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('697b4509ac2f4d14b8a8f5398dd488fd', 'project:product:aprMan', '利率维护', '#', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '6', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('6fc5976458c3415c894922d454be7293', 'set:msg:msgList', '消息记录', '/sys/message/messageManage.html', '3975def04c7942aebe871a4dcfa7124c', 'a0f88f24408a42759589270385618808:3975def04c7942aebe871a4dcfa7124c', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('70b917bfb77c49229f1b1c12e721690d', 'consumer', '客户', '', null, null, null, '6', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('70c49177b7664df9a3200074d18263bb', 'project:borrow:borrowMan', '借款维护', '/loan/borrow/borrowManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', '&amp;#xe613;', '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('7193b8e131824023abd1fe88ceb285dd', 'vip', '会员', '', null, null, null, '5', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('7431371acc1d40d98e45bbbcac350dd3', 'stat:borrow', '借款分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '2', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('783a91f0e7474325b7076352f7881765', 'project:product:add', '新增产品', '/product/manage/productAddPage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('7ab02fac290345d8993623b3c9fdb677', 'project:borrow:investList', '投资记录', '/loan/invest/investRecord.html?borrowFlag=1', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '9', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('7f83cb85060b4499a665735723e9362d', 'set:opinion', '意见反馈记录', '/sys/opinion/opinionManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe632;', '8', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('82da1ec38a83453f9bf835da0e6302f2', 'oper:vip:vipMan', 'VIP等级配置', '/operate/vip/vipLevelManage.html', 'ce3536e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e10181411111212c755a411145', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('82da1ec38a83453f9bf835da0e6302f3', 'oper:vip:vipGrowth', 'VIP成长日志', '/operate/vip/vipGrowthLogManage.html', 'ce3536e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e10181411111212c755a411145', null, '4', null, '1', null, '0');
INSERT INTO `sys_menu` VALUES ('8460c24149d246a8a5e9200e3f12fce5', 'stat:account:investIncome', '投资收益分析', '#', '0738842d7db04e49b16f2735e0c24cd0', '9b6b5489b9d947cdb73d209dde0ecf63:0738842d7db04e49b16f2735e0c24cd0', null, '4', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('84f9a75ee24940ada9ea967d7825c8bc', 'set:website', '网站管理', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe62f;', '1', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('867d3707f5f940079ed01f3ab459cd70', 'project:product:ransomList', '赎回列表', '#', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '10', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('86c4b1ce0688456384f223f35014dfd1', 'set:control', '调度任务', '/tpp/ufx/tppManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe633;', '10', null, '0', '待定', '1');
INSERT INTO `sys_menu` VALUES ('879c53ceed324fd8a63f06f3876acd2b', 'oper:userInvite:inviteStat', '邀请统计', '/user/invite/statisticsManage.html', 'ce3511e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3511e10181411111212c755a411145', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('898de066d9c74fcabc7c5347d0d24e20', 'project:realize:realizeList', '变现记录', '/realize/manage/realizeManage.html', 'ca0551e24d394eae8ea30085c9dcbc4d', '1f719034546143b783da0a82bbf5c4a1:ca0551e24d394eae8ea30085c9dcbc4d', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('89c357d88df342b88cc3c29f49e60a73', 'set:operator:pwd', '修改密码', '/sys/operator/operatorChangePwdPage.html', '034b7d6b6c104f89ab76c68b4a5e6343', 'a0f88f24408a42759589270385618808:034b7d6b6c104f89ab76c68b4a5e6343', '&amp;#xe613;', '1', '2016-10-10 09:53:21', '0', '', '1');
INSERT INTO `sys_menu` VALUES ('8aea4bc2842f4f5ebe31342cf0c9ea4b', 'project:product:productList', '产品列表', '/product/manage/productQuery.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '12', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('8b0802e0208d493c853c6561d9e1f754', 'project:borrow:repayList', '还款记录', '/loan/repayment/repaymentRecord.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '11', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('8d0d3a41322148c2b27f27e0fb569afb', 'set:queue', '队列监控', '#', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', null, '11', null, '1', '待定', '1');
INSERT INTO `sys_menu` VALUES ('8ec02740ef6d4620b101589ff5942227', 'project:typeMan', '类别管理', '/project/type/typeManage.html', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '&amp;#xe604;', '5', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('8f64473b15a24a19a40df3c0a0ff074e', 'stat:invest:investCount', '投资笔数分析', '#', '2efc0cf3524f449b964723d58397abab', '9b6b5489b9d947cdb73d209dde0ecf63:2efc0cf3524f449b964723d58397abab', null, '3', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('8febc2d0a04d4a42a3f8904970778b8c', 'set:riskTpl:answer', '评测记录', '/risk/riskUserLogManage.html', 'd8d3e092a09e4c36abde2260f1111a49', 'a0f88f24408a42759589270385618808:d8d3e092a09e4c36abde2260f1111a49', '&amp;#xe62c;', '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('90c22e2f12b44f218d25c42b5bbd16a7', 'oper', '运营', '', null, null, null, '3', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('914d4ad12e724cbfbb59456997242959', 'project:borrow:borrowList', '借款记录', '/loan/borrow/borrowRecord.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '8', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('91c57b3475a8433e82ff99a4b820c382', 'oper:redpacket:redpacketList', '红包列表', '/operate/redEnvelope/userRedenvelopeManage.html', 'ce3536e101814b7b975dbc755a411a45', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e101814b7b975dbc755a411a45', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('91c8e1ce152a4f24a315698ef6ea0b49', 'project:bond:bondList', '转让记录', '/bond/bond/bondManage.html', '2f700a22cf4349c891316352f719824d', '1f719034546143b783da0a82bbf5c4a1:2f700a22cf4349c891316352f719824d', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('948ccc45bfc04e56b7c6831ba7486bc7', 'account:accountList', '资金记录', '/account/account/accountLogList.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&amp;#xe607;', '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('98d3ec42322044f788b5ea0c2a87ef80', 'set:operLog', '操作日志', '/sys/log/logManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe615;', '9', null, '0', '待定', '1');
INSERT INTO `sys_menu` VALUES ('99413546529348cda88f62d4d809c911', 'account:platAccount', '平台账户', '/account/merchant/tppMerchantLogList.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&amp;#xe617;', '5', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('99830e3a22514f89af96d4c804649890', 'project:borrow:collList', '待收记录', '/loan/collection/collectionRecord.html?borrowFlag=1', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '10', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('9992d409c17b4c7996e9805f894ffcc7', 'set:sys:org', '组织机构管理', '/sys/org/orgManage.html', '434db50f546d4b87b9f340eb3905b3ba', 'a0f88f24408a42759589270385618808:434db50f546d4b87b9f340eb3905b3ba', null, '1', null, '0', null, '1');
INSERT INTO `sys_menu` VALUES ('9ab14bce07864d12b3ce2843315abc5c', 'project:borrow:add', '新增借款', '/loan/borrow/borrowAddPage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('9ae631ce200d48a8a6e0b9b63b6ffaa0', 'stat:borrow:borrowCount', '借款笔数分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '3', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('9b6b5489b9d947cdb73d209dde0ecf63', 'stat', '统计', '', null, null, null, '4', null, '1', null, '0');
INSERT INTO `sys_menu` VALUES ('9f0f275c5911427a9d59564b055a950c', 'set:sys:menu', '菜单管理', '/sys/menu/menuManage.html', '434db50f546d4b87b9f340eb3905b3ba', 'a0f88f24408a42759589270385618808:434db50f546d4b87b9f340eb3905b3ba', null, '4', null, '0', null, '1');
INSERT INTO `sys_menu` VALUES ('a0f88f24408a42759589270385618808', 'set', '设置', '', null, null, null, '8', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('a26352064b324c839891e40e9046d5ea', 'oper:addApr:addAprRule', '加息券规则', '/operate/rateCoupon/rateRuleManage.html', 'ce3536e101814b7b975dbc755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e101814b7b975dbc755a411145', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('a39d0f69ccac4543a361bcf6e3392938', 'oper:score:userScore', '用户积分', '/operate/score/userScoreManage.html', 'ce3536e101814111175dbc755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e101814111175dbc755a411145', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('a467e89e50a146ada2f4cbae197d1e9d', 'project:realize:realizeRule', '变现规则', '/realize/rule/realizeRuleManage.html', 'ca0551e24d394eae8ea30085c9dcbc4d', '1f719034546143b783da0a82bbf5c4a1:ca0551e24d394eae8ea30085c9dcbc4d', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('abb04a408212494489f529aad408dda3', 'project:product:investList', '投资列表', '/loan/invest/investRecord.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '9', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('ac9942de9e4f4259a41952edcddc0a5f', 'stat:borrow:repay', '还款分析', '#', '7431371acc1d40d98e45bbbcac350dd3', '9b6b5489b9d947cdb73d209dde0ecf63:7431371acc1d40d98e45bbbcac350dd3', null, '6', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('acb271cef985467696078186bfb3737a', 'stat:oper:redpacket', '红包统计分析', '#', 'dab0444b0a974a3281abadf26a8aade9', '9b6b5489b9d947cdb73d209dde0ecf63:dab0444b0a974a3281abadf26a8aade9', null, '1', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('af599d39657e4c7dae33ca9ff8a82dcd', 'account:rechargeList', '充值记录', '/account/recharge/rechargeList.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&amp;#xe602;', '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('b0e54a0b29eb4218b137a5de708dece2', 'consumer:performance', '业绩统计', '/user/customer/saleStatistics.html', '70b917bfb77c49229f1b1c12e721690d', '70b917bfb77c49229f1b1c12e721690d', '&amp;#xe629;', '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('b22a29475cfc4b8db37838fe40857667', 'stat:oper:actAccount', '活动投资金额分析', '#', 'dab0444b0a974a3281abadf26a8aade9', '9b6b5489b9d947cdb73d209dde0ecf63:dab0444b0a974a3281abadf26a8aade9', null, '3', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('b449d0ffc8334dcfa0cbd5057e0c3245', 'project:product:productMan', '产品维护', '/product/manage/newProductManage.html', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('b7415f6c0eee47f18ebab70cd5655ffe', 'project:borrow:overdue', '逾期管理', '/loan/repayment/repaymentOverdue.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '13', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('b9cc5f669454439f8a28b81fc4a7cf8b', 'oper:userInvite:inviteReward', '邀请奖励', '/user/invite/userInviteAward.html', 'ce3511e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3511e10181411111212c755a411145', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('ba1540b3cb32441cad6e14da6b4fa15b', 'project:borrow:follow', '贷后跟进', '/loan/borrow/borrowFollow.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '12', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('be89d0803efe402a9697a40689ebda31', 'oper:vip:vipPack', 'VIP礼包管理', '/operate/vip/userGiftManage.html', 'ce3536e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7', null, '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('c1483b2033214964b2d763c305971180', 'set:config', '参数设置', '/sys/config/configManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe630;', '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('c41abf5b7cac4a5989f9bf46e9d69afb', 'project:bond:bondRule', '转让规则', '/bond/rule/bondRuleAddEditQuery.html', '2f700a22cf4349c891316352f719824d', '1f719034546143b783da0a82bbf5c4a1:2f700a22cf4349c891316352f719824d', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('c5eab41d9d8d4de892ad4d7122f44112', 'set:operator:login', '个人信息', '/sys/operator/operatorLoginEditPage.html', '034b7d6b6c104f89ab76c68b4a5e6343', 'a0f88f24408a42759589270385618808:034b7d6b6c104f89ab76c68b4a5e6343', '', '2', '2016-10-31 19:19:39', '0', '', '1');
INSERT INTO `sys_menu` VALUES ('ca0551e24d394eae8ea30085c9dcbc4d', 'project:realize', '变现管理', '#', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '&#xe607;', '2', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ca10cbdd993e44f79385ee0fa1b8b62e', 'vip:company', '企业用户', '/user/userCompany/manage.html', '7193b8e131824023abd1fe88ceb285dd', '7193b8e131824023abd1fe88ceb285dd', '&amp;#xe624;', '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('cb2cdcdf1f8f4b868a6f486322bd86fd', 'stat:user:portrait', '用户画像分析', '#', 'dda6aa8faede4bed9bb07cd28aef8925', '9b6b5489b9d947cdb73d209dde0ecf63:dda6aa8faede4bed9bb07cd28aef8925', null, '3', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', 'vip:member', '个人用户', '/user/userPerson/manage.html', '7193b8e131824023abd1fe88ceb285dd', '7193b8e131824023abd1fe88ceb285dd', '&amp;#xe617;', '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('cc81470463a44a35bacfcf9e9b10bd4a', 'vip:vouch', '担保机构', '/user/userVouch/manage.html', '7193b8e131824023abd1fe88ceb285dd', '7193b8e131824023abd1fe88ceb285dd', '&amp;#xe627;', '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('cd4d2fb17ca149e7ab85cfcee2e8c1e5', '123122131', '1234', '123123123', '034b7d6b6c104f89ab76c68b4a5e6343', 'a0f88f24408a42759589270385618808', '', '100', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3511e10181411111212c755a411145', 'oper:userInvite', '好友邀请管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe619;', '5', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3536e10181411111212c755a411145', 'oper:vip', 'vip管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe618;', '4', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3536e101814111175dbc755a411145', 'oper:score', '积分管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe61d;', '3', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3536e101814b7b975dbc755a411145', 'oper:addApr', '加息券管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe61c;', '2', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3536e101814b7b975dbc755a411a45', 'oper:redpacket', '红包管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe61b;', '1', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('ce3536e101814b7b975dbc755a434a45', 'project:product', '产品管理', '#', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '&#xe600;', '1', null, '0', '', '0');
INSERT INTO `sys_menu` VALUES ('d0a1d4764e89473c989319f5a2fc33e3', 'set:website:article', '文章管理', '/column/article/articleManage.html', '84f9a75ee24940ada9ea967d7825c8bc', 'a0f88f24408a42759589270385618808:84f9a75ee24940ada9ea967d7825c8bc', null, '2', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('d0ef25414a2f4f1fb8be2336bf630f2d', 'stat:invest:investUser', '投资人数分析', '#', '2efc0cf3524f449b964723d58397abab', '9b6b5489b9d947cdb73d209dde0ecf63:2efc0cf3524f449b964723d58397abab', null, '1', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('d20738c990c4417ba2b49af085ac8a8c', 'oper:vip:vipUser', 'VIP用户', '/operate/vip/userVipManage.html', 'ce3536e10181411111212c755a411145', '90c22e2f12b44f218d25c42b5bbd16a7:ce3536e10181411111212c755a411145', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('d8d3e092a09e4c36abde2260f1111a49', 'risk', '风控', '', null, null, null, '7', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('d8d3e092a09e4c36abde2260f1660a49', 'account', '资金', '', null, null, null, '2', null, '0', null, '0');
INSERT INTO `sys_menu` VALUES ('d9aefea965d94f13a136956f9bfafe0a', 'oper:redpacket:redpacketRule', '红包规则', '/operate/redEnvelope/redRuleManage.html', 'ce3536e101814b7b975dbc755a411a45', '90c22e2f12b44f218d25c42b5bbd16a7,ce3536e101814b7b975dbc755a411a45', null, '1', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('da10b946d2eb490bab155435f9d5a4c5', 'stat:account:cash', '提现统计分析', '#', '0738842d7db04e49b16f2735e0c24cd0', '9b6b5489b9d947cdb73d209dde0ecf63:0738842d7db04e49b16f2735e0c24cd0', null, '2', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('dab0444b0a974a3281abadf26a8aade9', 'stat:oper', '活动运营分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '6', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('db08669c140e4e28a2238646ee10f111', 'project:protocol:tempalte', '协议管理', '/protocol/protocol/protocolList.html', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1,db08669c140e4e28a2238646ee10fa3e', '&amp;#xe605;', '6', null, '0', null, '1');
INSERT INTO `sys_menu` VALUES ('dda6aa8faede4bed9bb07cd28aef8925', 'stat:user', '用户分析', '#', '9b6b5489b9d947cdb73d209dde0ecf63', '9b6b5489b9d947cdb73d209dde0ecf63', null, '1', null, '1', '', '0');
INSERT INTO `sys_menu` VALUES ('e3db465f991e421ea12342dd3b9b13b2', 'set:logtemplate', '日志模板', '/sys/logTemplate/logTemplateManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe637;', '4', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('e3db465f991e421eadcd02dd3b9b13b2', 'set:dict', '数据字典', '/sys/dict/dictManage.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&amp;#xe631;', '3', null, '0', '', '1');
INSERT INTO `sys_menu` VALUES ('ef5b87ebc45e4537ac5e6d5328387218', 'stat:user:register', '注册用户分析', '#', 'dda6aa8faede4bed9bb07cd28aef8925', '9b6b5489b9d947cdb73d209dde0ecf63:dda6aa8faede4bed9bb07cd28aef8925', null, '1', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('f03f8ab5839d4cdeaa783593168da4aa', 'stat:account:platBalance', '平台盈亏分析', '#', '0738842d7db04e49b16f2735e0c24cd0', '9b6b5489b9d947cdb73d209dde0ecf63:0738842d7db04e49b16f2735e0c24cd0', null, '5', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('f495963d647b44e39dd426a3fdd8aa36', 'stat:user:active', '活跃用户分析', '#', 'dda6aa8faede4bed9bb07cd28aef8925', '9b6b5489b9d947cdb73d209dde0ecf63:dda6aa8faede4bed9bb07cd28aef8925', null, '2', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('f67fc535ef7d4bb38180d22b0e908451', 'project:product:aprList', '派息列表', '#', 'ce3536e101814b7b975dbc755a434a45', '1f719034546143b783da0a82bbf5c4a1:ce3536e101814b7b975dbc755a434a45', null, '11', null, '1', '', '1');
INSERT INTO `sys_menu` VALUES ('fbb532930f11419eae657051c959dd2e', 'project:borrow:ev', '成立审核', '/loan/borrow/borrowEstablishVerifyManage.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', null, '7', null, '0', '', '1');

-- ----------------------------
-- Table structure for `sys_message`
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `message_type` varchar(30) DEFAULT NULL COMMENT '消息模板标识',
  `send_user` varchar(32) DEFAULT NULL COMMENT '发送用户ID',
  `receive_user` varchar(32) DEFAULT NULL COMMENT '接收用户ID',
  `receive_addr` varchar(50) DEFAULT '' COMMENT '接收地址(1短信 ：填写接收人手机号；2邮件： 接收人邮箱 ；3站内信，填写用户名)',
  `send_type` char(1) DEFAULT NULL COMMENT '消息类型（1短信 2邮件 3站内信）',
  `title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `content` text COMMENT '内容',
  `status` char(1) DEFAULT NULL COMMENT '状态（0 新建，1发送成功 2发送失败）',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(512) DEFAULT '' COMMENT '备注: 发送结果信息',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息记录表';

-- ----------------------------
-- Records of sys_message
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_message_type`
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_type`;
CREATE TABLE `sys_message_type` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `message_type` varchar(30) DEFAULT NULL COMMENT '消息类型标识',
  `send_type` char(1) DEFAULT NULL COMMENT '消息类型（1短信 2邮件 3站内信）',
  `message_name` varchar(30) DEFAULT NULL COMMENT '消息类型名称',
  `message_title` varchar(100) DEFAULT NULL COMMENT '消息标题模板(freemark模板格式)',
  `message_content` text COMMENT '消息内容模板(freemark模板格式)',
  `send` char(1) NOT NULL DEFAULT '0' COMMENT '是否发送: 0 不发送，1 发送',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息类型表';

-- ----------------------------
-- Records of sys_message_type
-- ----------------------------
INSERT INTO `sys_message_type` VALUES ('1001', 'user_repay_phoneCode', '1', '还款短信验证码', '还款短信验证码', '尊敬的用户[${user.userName}]，您于${noticeTime}所获取还款验证码为${code}，请您在${vtime}分钟内填写，完成验证。', '1', '2016-08-22 10:32:51', '用户还款时，确认身份使用');
INSERT INTO `sys_message_type` VALUES ('102', 'get_email', '3', '重新激活邮件', '用户重新激活邮件', '尊敬的用户[${user.userName}]，您使用了激活邮件功能，请查收邮件并按邮件所述进行操作，如非本人操作，请联系客服。', '1', '2016-06-22 09:23:38', '重新激活邮件');
INSERT INTO `sys_message_type` VALUES ('109', 'project_full_succ', '3', '成立审核通过', '成立审核通过', '尊敬的用户[${user.userName}]，您发布的项目[${projectName!}]已经通过成立审核，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '成立审核通过');
INSERT INTO `sys_message_type` VALUES ('110', 'project_full_fail', '3', '成立审核失败', '成立审核失败', '尊敬的用户[${user.userName}]，您发布的项目[${projectName!}]成立审核未通过，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '成立审核失败通知');
INSERT INTO `sys_message_type` VALUES ('111', 'invest', '3', '认购成功', '认购成功', '尊敬的用户[${user.userName}]，您于${investTime}投资的项目[${projectName!}]投资成功，冻结金额${investMoney}元，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '投资成功通知');
INSERT INTO `sys_message_type` VALUES ('112', 'invest_fail', '3', '投资失败', '用户投资失败', '尊敬的用户[${user.userName}]，您于${investTime!}投资的项目[${projectName}]满标复审没有通过，投资金额${amount!}，解冻实际支付金额${realAmount!}元。，请登录网站查看详情。', '1', '2016-06-22 09:23:38', '撤标，复审失败后将资金退回投资人帐户。');
INSERT INTO `sys_message_type` VALUES ('115', 'receive_repay', '3', '借款人还款', '借款人还款', '尊敬的用户[${user.userName}]，您投资的项目[${projectName!}]第${period!1}期，于${repayTime!}回款，${repayStr}请查看资金明细详情。', '1', '2016-06-22 09:23:38', '投资人收到还款时的短信通知');
INSERT INTO `sys_message_type` VALUES ('118', 'recharge_succ', '3', '线上充值成功', '用户线上充值成功', '尊敬的用户[${user.userName}]，您于${rechargeTime!}充值${amount!}元成功。', '1', '2016-06-22 09:23:38', '线上充值成功提醒。');
INSERT INTO `sys_message_type` VALUES ('12', 'receive_repay', '1', '借款人还款', '借款人还款', '尊敬的用户[${user.userName}]，您投资的项目[${projectName!}]第${period!1}期，于${repayTime!}回款，${repayStr}请登录网站查看详情。', '1', '2016-06-22 09:23:38', '借款人还款后向投资人发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('123', 'phone_code', '3', '短信验证码', '短信验证码', '尊敬的用户[${user.userName}]，您于${dateformat(parseLong(noticeTime)?c)}所获取手机认证验证码为${code}，请您在${vtime}分钟内填写，如非本人操作，请联系客服。', '1', '2016-06-22 09:23:38', '短信验证码');
INSERT INTO `sys_message_type` VALUES ('1231ad24f4f4y5yy5y435yy', 'cash_fail', '1', '提现失败', '提现失败', '尊敬的用户[${user.userName}]，您于${cashTime!}的提现审核失败，解冻金额${amount!}元，请登录网站查看详情！', '0', '2016-11-07 18:33:05', '提现失败');
INSERT INTO `sys_message_type` VALUES ('134', 'vip_birth', '3', 'VIP生日', '祝您生日快乐', '尊敬的用户[${user.userName}]，今天是您的生日，感谢您一年来的支持，祝您生日快乐，万事如意，并请您继续支持我们。', '1', '2016-06-22 09:23:38', 'VIP生日祝福');
INSERT INTO `sys_message_type` VALUES ('135', 'certify_succ', '3', '认证通过', '用户${type}审核通过', '尊敬的用户[${user.userName}]，您的${type}审核通过。', '1', '2016-06-22 09:23:38', '认证通过提醒');
INSERT INTO `sys_message_type` VALUES ('136', 'certify_fail', '3', '认证未通过', '用户${type}审核未通过', '尊敬的用户[${user.userName}]，您的${type}审核不通过，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '认证不通过提醒');
INSERT INTO `sys_message_type` VALUES ('138', 'auto_invest', '3', '自动投资成功', '自动投资成功', '尊敬的用户[${user.userName}]，系统于${(noticeTime?string(\'yyyy-MM-dd HH:mm\'))!}为您进行自动投资[${project.projectName}],投资金额${parseDouble(money)?string(`#.##`)}元。', '1', '2016-06-22 09:23:38', '自动投资成功通知');
INSERT INTO `sys_message_type` VALUES ('141', 'bond_sell_succ', '3', '债权转让成功', '债权转让成功', '尊敬的用户[${user.userName}]，您的债权[${bondName}]转让成功。', '1', '2016-06-22 09:23:38', '债权转让成功');
INSERT INTO `sys_message_type` VALUES ('142', 'bond_buy_succ', '3', '债权转让投资成功', '债权转让投资成功', '尊敬的用户[${user.userName}]，您投资[${bondName}]债权成功，投资金额${investMoney}元，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '债权转让投资成功');
INSERT INTO `sys_message_type` VALUES ('148', 'user_register_phoneCode', '1', '注册验证码', '注册验证码', '手机号码为[${phone}]的用户,您在[${webname}]注册的验证码是${code}。', '1', '2016-06-22 09:23:38', '注册验证码');
INSERT INTO `sys_message_type` VALUES ('149', 'invest_succ', '1', '投资成功', '投资成功', '尊敬的用户[${user.userName}]，您投资的项目[${projectName!}]已经成立，投资金额${amount!0}元，扣除冻结资金${realAmount!0}元，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '成立审核');
INSERT INTO `sys_message_type` VALUES ('15', 'recharge_succ', '1', '线上充值成功', '线上充值成功', '尊敬的用户[${user.userName}]，您于${rechargeTime!}充值${amount!}元成功，请登录网站查看详情！', '0', '2016-06-22 09:23:38', '线上充值成功提醒。');
INSERT INTO `sys_message_type` VALUES ('150', 'invest_succ', '2', '投资成功', '投资成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您投资的项目[${projectName!}]已经成立，投资金额${amount!0}元，扣除冻结资金${realAmount!0}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '成立审核');
INSERT INTO `sys_message_type` VALUES ('151', 'invest_succ', '3', '投资成功', '投资成功', '尊敬的用户[${user.userName}]，您投资的项目[${projectName!}]已经成立，投资金额${amount!0}元，扣除冻结资金${realAmount!0}元，如有疑问，请联系客服。', '1', '2016-06-22 09:23:38', '成立审核');
INSERT INTO `sys_message_type` VALUES ('152', 'notice_operator_password_reset', '1', '操作员密码重置', '操作员密码重置', '操作员${operator.name}重置密码成功！重置后密码是：${password}。为保证账户安全请尽快修改密码！', '1', '2016-06-22 09:23:38', '操作员密码重置');
INSERT INTO `sys_message_type` VALUES ('160', 'modify_phone_emailCode', '2', '修改绑定手机', '修改绑定手机邮箱验证码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					本次[修改绑定手机]的验证码为：${code}，请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', '修改绑定手机邮箱验证码');
INSERT INTO `sys_message_type` VALUES ('161', 'modify_email_emailCode', '2', '修改绑定邮箱', '修改绑定邮箱-邮箱验证码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${noticeTime}所获取的邮箱认证验证码为<em style=\'color: #f95a28; font-size: 18px; font-family: Arial; font-style: normal\'>${code}</em>，请您在${vtime}分钟内填写，完成验证。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', '修改绑定邮箱-邮箱验证码');
INSERT INTO `sys_message_type` VALUES ('162', 'reset_question_emailCode', '2', '重置密保问题', '重置密保问题邮箱验证码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					本次[重置密保问题]的验证码为：${code}，请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '重置密保问题邮箱验证码');
INSERT INTO `sys_message_type` VALUES ('163', 'reset_question_phoneCode', '1', '重置密保问题', '重置密保问题短信验证码', '尊敬的用户[${user.userName}]，本次[重置密保问题]的验证码为：${code}，请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。', '1', '2016-06-22 09:23:38', '重置密保问题短信验证码');
INSERT INTO `sys_message_type` VALUES ('20', 'phone_code', '1', '短信验证码', '短信验证码', '尊敬的用户[${user.userName}]，您于${dateformat(parseLong(noticeTime)?c)}所获取手机认证验证码为${code}，请您在${vtime}分钟内填写。如非本人操作，请致电${customer_hotline}。', '1', '2016-06-22 09:23:38', '短信验证码');
INSERT INTO `sys_message_type` VALUES ('201', 'account_warn', '1', '自动预警', '订单资金异常预警', '${orderType}订单[${orderNo}]于${(createTime?string(\'yyyy年MM月dd日 HH:mm\'))!}出现资金不平衡，请尽快登录网站后台进行处理。', '1', '2016-06-22 09:23:38', '订单出现资金异常时向联系人发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('32', 'vip_birth', '1', 'VIP生日', 'VIP生日', '尊敬的用户[${user.userName}]，今天是您的生日，感谢您一年来的支持，祝您生日快乐，万事如意，并请您继续支持我们。', '0', '2016-06-22 09:23:38', 'VIP生日祝福');
INSERT INTO `sys_message_type` VALUES ('34', 'certify_succ', '1', '认证通过', '${type}审核通过', '尊敬的用户[${user.userName}]，您的${type}审核通过，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '认证通过提醒');
INSERT INTO `sys_message_type` VALUES ('35', 'certify_fail', '1', '认证未通过', '${type}审核未通过', '尊敬的用户[${user.userName}]，您的${type}审核未通过，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '认证不通过提醒');
INSERT INTO `sys_message_type` VALUES ('37', 'get_email', '1', '激活邮件', '激活邮件', '尊敬的用户[${user.userName}]，您使用了激活邮件功能，请查收邮件并按邮件所述进行操作。', '0', '2016-06-22 09:23:38', '重新激活邮件');
INSERT INTO `sys_message_type` VALUES ('382fb58d8f8311e699af000c296f8d8c', 'account_compare_warn', '1', '资金对比异常短信提醒', '资金异常', '尊敬的运维人员[${operUserName!\'\'}],您维护的平台${web_name!\'\'}上的用户${errorUserName!\'\'}资金有异常,请登录网站核查资金。', '1', '2016-10-11 15:20:42', '资金对比异常短信提醒');
INSERT INTO `sys_message_type` VALUES ('39', 'get_pwd_phone', '1', '找回登录密码', '找回登录密码', '尊敬的用户[${user.userName}]，本次[找回登录密码]的验证码为：${code}。请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。', '1', '2016-06-22 09:23:38', '通过手机验证码找回登录密码');
INSERT INTO `sys_message_type` VALUES ('3e581a293fa747afa47029bb06710c64', 'project_invest_bespeak_remind', '3', '预约投资提醒', '预约投资提醒', '尊敬的【${tender!}】，您预约的产品【${projectName!}】将于【${saleTime!}】开售，请登录网站进行投资。', '1', null, null);
INSERT INTO `sys_message_type` VALUES ('40', 'auto_invest', '1', '自动投资成功', '自动投资成功', '尊敬的用户[${user.userName}]，系统于${(noticeTime?string(\'yyyy年MM月dd日 HH:mm\'))!}为您进行自动投资[${(project.projectName+`..........`)?substring(0,10)}],投资金额${parseDouble(money)?string(`#.##`)}元，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '自动投资成功通知');
INSERT INTO `sys_message_type` VALUES ('41', 'bind_phone', '1', '绑定手机验证码', '手机验证码', '尊敬的用户[${user.userName}]，您于${noticeTime}所获取手机认证验证码为${code}，请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。', '1', '2016-06-22 09:23:38', '绑定手机验证码');
INSERT INTO `sys_message_type` VALUES ('42', 'modify_phone_phoneCode', '1', '修改绑定手机', '修改绑定手机短信验证码', '尊敬的用户[${user.userName}]，本次[修改绑定手机]的验证码为：${code}，请在${vtime}分钟内输入。请勿将验证码告知他人，如非本人操作，请致电${customer_hotline}。', '1', '2016-06-22 09:23:38', '修改绑定手机短信验证码');
INSERT INTO `sys_message_type` VALUES ('45', 'bond_sell_succ', '1', '债权转让成功', '债权转让成功', '尊敬的用户[${user.userName}]，您已成功的转让[${bondName}]债权，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '债权转让成功');
INSERT INTO `sys_message_type` VALUES ('46', 'bond_buy_succ', '1', '债权转让投资成功', '债权转让投资成功', '尊敬的用户[${user.userName}]，您投资[${bondName}]债权成功，投资金额${investMoney}元，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '债权转让投资成功');
INSERT INTO `sys_message_type` VALUES ('56', 'project_full_succ', '2', '成立审核通过', '成立审核通过', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您发布的项目[${projectName!}]已经通过成立审核，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '成立审核通过时向借款人发出的通知邮件。');
INSERT INTO `sys_message_type` VALUES ('57', 'project_full_fail', '2', '成立审核失败', '成立审核失败', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您发布的项目[${projectName!}]成立审核未通过，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '成立审核失败时向借款人发出的通知邮件');
INSERT INTO `sys_message_type` VALUES ('58', 'invest', '2', '认购成功', '认购成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${investTime!}成功投资项目[${projectName!}]，投资金额${investMoney!0}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '投资成功时向投资人发出的通知邮件');
INSERT INTO `sys_message_type` VALUES ('59', 'invest_fail', '2', '投资失败', '投资失败', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${investTime!}投资的项目[${projectName}]满标复审没有通过，投资金额${amount!}，解冻实际支付金额${realAmount!}元。，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '撤标、复审不通过后将自己退回投资人账户时发出的通知邮件');
INSERT INTO `sys_message_type` VALUES ('6', 'project_full_succ', '1', '成立审核通过', '成立审核通过', '尊敬的用户[${user.userName}]，您发布的项目[${projectName!}]已经通过成立审核，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '成立审核通过时向借款人发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('62', 'receive_repay', '2', '借款人还款', '借款人还款', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您投资的项目[${projectName!}]第${period!0}期，于${repayTime!}回款，${repayStr}请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '借款人还款后向投资人发出的通知邮件');
INSERT INTO `sys_message_type` VALUES ('65', 'recharge_succ', '2', '线上充值成功', '线上充值成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${rechargeTime!}充值${amount!}元成功，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '线上充值成功提醒。');
INSERT INTO `sys_message_type` VALUES ('7', 'project_full_fail', '1', '成立审核失败', '成立审核失败', '尊敬的用户[${user.userName}]，您发布的项目[${projectName!}]成立审核未通过，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '成立审核失败时向借款人发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('70', 'phone_code', '2', '短信验证码', '短信验证码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${dateformat(parseLong(noticeTime)?c)}所获取手机认证验证码为${code}，请您在${vtime}分钟内填写。如非本人操作，请致电${customer_hotline}。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', '短信验证码');
INSERT INTO `sys_message_type` VALUES ('8', 'invest', '1', '认购成功', '认购成功', '尊敬的用户[${user.userName}]，您于${investTime}成功投资项目[${projectName!}]，投资金额${investMoney!0}元，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '投资成功时向投资人发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('82', 'vip_birth', '2', 'VIP生日', '祝您生日快乐', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					今天是您的生日，感谢您一年来的支持，祝您生日快乐，万事如意，并请您继续支持我们。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', 'VIP生日祝福');
INSERT INTO `sys_message_type` VALUES ('84', 'certify_succ', '2', '认证通过', '${type}审核通过', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您的${type}审核通过，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '认证通过提醒');
INSERT INTO `sys_message_type` VALUES ('85', 'certify_fail', '2', '认证未通过', '${type}审核未通过', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您的${type}审核未通过，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '认证不通过提醒');
INSERT INTO `sys_message_type` VALUES ('87', 'auto_invest', '2', '自动投资成功', '自动投资成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					系统于${(noticeTime?string(\'yyyy年MM月dd日 HH:mm\'))!}为您进行自动投资[${(project.projectName+`..........`)?substring(0,10)}],投资金额${parseDouble(money)?string(`#.##`)}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '自动投资成功通知');
INSERT INTO `sys_message_type` VALUES ('88', 'get_email', '2', '激活邮件', '用户账户激活邮件', '<p>亲爱的<span style=\'color: rgb(196, 0, 0);\'>${user.userName}</span>用户，您好！</p><p style=\'text-indent:2em;\'>感谢您注册${web_name}，请点击下方链接，即可完成注册：</p><a title=\'点击激活邮件\' href=\'${web_url}${activeUrl}\' target=\'_blank\' swaped=\'true\'>${web_url}${activeUrl}</a><p style=\'color:#666;\'>（您也可以将以上链接复制到浏览器地址栏访问）</p><p style=\'text-indent:2em;\'>为保障您的帐号安全，请在24小时内点击该链接，超时需要重新获取邮件。</p><p style=\'margin:60px 0 0 0;\'>${web_name!}——专业，专注，品牌，服务，我们一直在努力！</p><hr style=\'border:1px solid #315a8b;\'><p style=\'color:#666;\'>此邮件由系统自动发送，请勿回复。</p><p style=\'color:#666;\'>如果您有任何疑问，请查看<a href=\'${web_url}/help/guide.html\' target=\'_blank\'>帮助中心</a>或致电客服热线：${customer_hotline!}</p><p style=\'color:#666;\'>${copyright!}|${web_record_number!}</p>', '1', '2016-06-22 09:23:38', '激活邮件');
INSERT INTO `sys_message_type` VALUES ('9', 'invest_fail', '1', '投资失败', '投资失败', '尊敬的用户[${user.userName}]，您于${investTime!}投资的项目[${projectName}]满标复审没有通过，投资金额${amount!}，解冻实际支付金额${realAmount!}元。，请登录网站查看详情。', '0', '2016-06-22 09:23:38', '撤标、复审不通过后将自己退回投资人账户时发出的通知短信');
INSERT INTO `sys_message_type` VALUES ('90', 'get_pwd_email', '2', '通过邮箱找回登录密码', '登录密码找回', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${noticeTime}进行找回登录密码操作，本次操作的验证码为：${code}。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', '找回登录密码');
INSERT INTO `sys_message_type` VALUES ('90bfe77552dc41ee944d7eae212c96f1', 'retrieve_password_phoneCode', '1', '找回密码', '找回密码', '尊敬的用户[${receiveAddr}]，您于${noticeTime}所获取手机认证验证码为${code}，请您在${vtime}分钟内填写。如非本人操作，请致电${customer_hotline}。', '1', null, null);
INSERT INTO `sys_message_type` VALUES ('90bfe77552dc41ee944d7eae212c96f3', 'retrieve_password_emailCode', '2', '找回密码', '找回密码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${noticeTime}所获取手机认证验证码为${code}，请您在${vtime}分钟内填写。如非本人操作，请致电${customer_hotline}。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', null, null);
INSERT INTO `sys_message_type` VALUES ('91', 'bind_email', '2', '绑定邮箱验证码', '邮箱验证码', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${noticeTime}所获取的邮箱认证验证码为<em style=\'color: #f95a28; font-size: 18px; font-family: Arial; font-style: normal\'>${code}</em>，请您在${vtime}分钟内填写，完成验证。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', '2016-06-22 09:23:38', '绑定邮箱验证码');
INSERT INTO `sys_message_type` VALUES ('92', 'modify_email_phoneCode', '1', '修改绑定邮箱', '修改绑定邮箱-手机验证码', '尊敬的用户[${user.userName}]，您于${noticeTime}所获取邮箱认证验证码为${code}，请您在${vtime}分钟内填写，完成验证。', '1', '2016-06-22 09:23:38', '修改绑定邮箱-手机验证码');
INSERT INTO `sys_message_type` VALUES ('95', 'bond_sell_succ', '2', '债权转让成功', '债权转让成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您已成功的转让[${bondName}]债权，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '债权转让成功');
INSERT INTO `sys_message_type` VALUES ('96', 'bond_buy_succ', '2', '债权转让投资成功', '债权转让投资成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您投资[bondName}]债权成功，投资金额${investMoney}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-06-22 09:23:38', '债权转让投资成功');
INSERT INTO `sys_message_type` VALUES ('9e48de15a28b11e6817dfa163e88e3b3', 'cash_succ', '1', '提现成功', '提现成功', '尊敬的用户[${user.userName}]，您于${cashTime!}提现操作成功，到账${amount!}元，手续费${fee!}元，请登录网站查看详情！', '0', '2016-11-04 20:38:04', '提现成功通知');
INSERT INTO `sys_message_type` VALUES ('bebca9e0a28b11e6817dfa163e88e3b3', 'cash_succ', '2', '提现成功', '提现成功', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${cashTime!}提现操作成功，到账${amount!}元，手续费${fee!}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-11-04 20:40:05', '提现成功通知');
INSERT INTO `sys_message_type` VALUES ('bf9479dca28b11e6817dfa163e88e3b3', 'cash_succ', '3', '提现成功', '提现成功', '尊敬的用户[${user.userName}]，您于${cashTime!}提现操作成功，到账${amount!}元，手续费${fee!}元。', '1', '2016-11-04 20:40:07', '提现成功通知');
INSERT INTO `sys_message_type` VALUES ('d1f3d4t2f34f4fg4h34g6h', 'cash_fail', '2', '提现失败', '提现失败', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您于${cashTime!}的提现审核失败，解冻金额${amount!}元，请登录网站查看详情。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '0', '2016-11-07 18:33:07', '提现失败');
INSERT INTO `sys_message_type` VALUES ('d81c34ee3302457594268d9d5876f977', 'project_invest_bespeak_remind', '2', '预约投资提醒', '预约投资提醒', '<div id=\'contentDiv0\' style=\'background: #f2f4f7; padding-top: 30px; padding-bottom: 30px; zoom: 1; position: relative; z-index: 1;\'>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\' style=\'margin-bottom: 10px\'>\r\n		<tbody>\r\n			<tr>\r\n				<td><img src=\'${image_server_url}/data/img/logo/logo.png\' /></td>\r\n				<td style=\'text-align: right\'><a href=\'#\' style=\'font-size: 14px; color: #aaa; font-family: Microsoft YaHei; text-decoration: none\'>访问首页</a>\r\n				</td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tbody>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 16px; color: #666; font-family: Microsoft YaHei; font-weight: bold; line-height: 85px; padding-left: 35px;\'>\r\n					${user.userName}，您好：\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; padding: 0 35px; line-height: 40px; text-indent: 32px;\'>\r\n					您预约的产品【${projectName!}】将于【${saleTime!}】开售，请登录网站进行投资。\r\n				</td>\r\n			</tr>\r\n			<tr style=\'background: #fff\'>\r\n				<td style=\'font-size: 14px; color: #999; font-family: Microsoft YaHei; text-align: right; padding-right: 35px; line-height: 60px;\'>\r\n					<em style=\'font-weight: bold; font-style: normal\'>${web_name}</em> · 运营团队\r\n				</td>\r\n			</tr>\r\n			<tr>\r\n				<td style=\'height: 7px; padding: 0; margin: 0\'><img src=\'${image_server_url}/data/img/logo/img-bottom.png\' style=\'display: block\'></td>\r\n			</tr>\r\n		</tbody>\r\n	</table>\r\n	<table align=\'center\' border=\'0\' cellpadding=\'0\' cellspacing=\'0\' width=\'680\'>\r\n		<tr>\r\n			<td style=\'font-size: 12px; color: #aaa; font-family: Microsoft YaHei\'>\r\n				<p>此为系统邮件，自动发送，请勿回复！</p>\r\n				<p>为了您能正常收到邮件，请将 <em style=\'color: #f95a28; font-style: normal\'>${sendEmail!}</em>添加进您的通讯录。</p>\r\n				<p>如有任何疑问，您可以拨打我们的客服电话 ${customer_hotline}</p>\r\n			</td>\r\n		</tr>\r\n	</table>\r\n</div>', '1', null, null);
INSERT INTO `sys_message_type` VALUES ('d8347g4t24hg4t3g2413f', 'cash_fail', '3', '提现失败', '提现失败', '尊敬的用户[${user.userName}]，您于${cashTime!}的提现审核失败，解冻金额${amount!}元。', '1', '2016-11-07 18:33:09', '提现失败');
INSERT INTO `sys_message_type` VALUES ('dc8e735e54454f4997ffb6b11adbc0a4', 'project_invest_bespeak_remind', '1', '预约投资提醒', '预约投资提醒', '尊敬的【${tender!}】，您预约的产品【${projectName!}】将于【${saleTime!}】开售，请登录网站进行投资。', '1', null, null);
INSERT INTO `sys_message_type` VALUES ('f8bf77d3902b11e699af000c296f8d8c', 'tpp_warn', '1', 'tppTask失败短信提醒', 'tppTask失败短信提醒', '尊敬的运维人员[${operUserName!\'\'}],您维护的平台${web_name!\'\'}上的用户${errorUserNameList!\'\'}于${createTime!}${operatorNameList!\'\'}失败,订单号为：${orderNoList!\'\'},请登录网站后台核查第三方调度任务。', '1', '2016-10-12 11:28:41', 'tppTask失败短信提醒');

-- ----------------------------
-- Table structure for `sys_operation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation`;
CREATE TABLE `sys_operation` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(64) NOT NULL COMMENT '菜单英文标识,如:systemmange:user:page',
  `operation_name` varchar(50) NOT NULL,
  `operation_type` char(1) NOT NULL DEFAULT '1' COMMENT '操作类型：1操作  2 页面元素 （按钮、图表等）3文件 ',
  `sort` int(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `delete_flag` char(1) NOT NULL DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能操作表';

-- ----------------------------
-- Records of sys_operation
-- ----------------------------
INSERT INTO `sys_operation` VALUES ('650237d88b5b4a2ea4fbb1a8c187aa22', 'query', '查询', '1', '1', '2016-06-04 15:10:50', '0', null);
INSERT INTO `sys_operation` VALUES ('90b04389913b41008a69cead0cbf02b7', 'edit', '编辑', '1', '3', '2016-06-04 15:11:16', '0', null);
INSERT INTO `sys_operation` VALUES ('94149ff5a0fa11e69638000c296f8d8c', 'resetPwd', '重置密码', '1', '31', '2016-11-02 20:48:49', '0', null);
INSERT INTO `sys_operation` VALUES ('cc53ee6bba3947a392447e49b8f5e4f1', 'del', '删除', '1', '4', '2016-06-04 15:11:40', '0', null);
INSERT INTO `sys_operation` VALUES ('ec401012c17444a29bb1247175452456', 'contentadd', '内容新增', '1', '20', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247171234765', 'sale', '上架', '1', '17', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175111ccc', 'follow', '跟进', '1', '25', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175234ccc', 'answerdel', '答案删除', '1', '24', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175400001', 'check', '人工核查', '1', '26', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb124717545009', 'assign', '子账户划拨', '1', '15', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb124717545098', 'transfer', '用户转账', '1', '14', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175450987', 'stop', '下架', '1', '18', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175451212', 'download', '下载', '1', '16', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175451234', 'freeze', '冻结', '1', '9', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452093', 'answeradd', '答案新增', '1', '22', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452123', 'auditing', '审核', '1', '11', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452124', 'export', '导出', '1', '8', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452222', 'cash', '提现', '1', '13', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452321', 'grant', '发放红包', '1', '35', '2016-11-05 15:45:31', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452456', 'recharge', '充值', '1', '12', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452992', 'answeredit', '答案编辑', '1', '23', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452abc', 'cancel', '作废', '1', '10', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452b11', 'mgr', '角色-用户管理', '1', '6', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452bf0', 'add', '新增', '1', '2', '2016-06-04 15:10:15', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452bf9', 'auth', '授权', '1', '5', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452c99', 'contentedit', '内容编辑', '1', '21', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452ccc', 'user:del', '角色-用户删除', '1', '7', '2017-01-18 18:12:26', '0', null);
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452ddd', 'view', '经纪人-展示', '1', '30', '2016-10-26 17:53:27', '0', '');

-- ----------------------------
-- Table structure for `sys_operator`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operator`;
CREATE TABLE `sys_operator` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `login_name` varchar(40) NOT NULL COMMENT '用户登陆名',
  `pwd` varchar(64) NOT NULL COMMENT '登陆密码',
  `real_name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `emp_no` varchar(40) DEFAULT NULL COMMENT '工号',
  `org_id` varchar(32) DEFAULT NULL COMMENT '所属部门',
  `telephone` varchar(40) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(40) DEFAULT NULL COMMENT '手机',
  `email` varchar(40) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号码',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `status` varchar(2) DEFAULT NULL COMMENT '用户状态:(-1 锁定,0 正常)',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sys_user_login_name` (`login_name`) USING BTREE,
  KEY `fk_sys_user_org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统用户';

-- ----------------------------
-- Records of sys_operator
-- ----------------------------
INSERT INTO `sys_operator` VALUES ('54c294528b5041ff838380676a2ed643', 'admin', '7abbc4d092252eb23a61f40f0656e2211fe3b958e65b1c0f', null, null, null, null, null, null, null, null, null, '0', null);

-- ----------------------------
-- Table structure for `sys_operator_customer`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operator_customer`;
CREATE TABLE `sys_operator_customer` (
  `uuid` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '客户id',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '经纪人id',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_type` char(1) DEFAULT NULL COMMENT '添加类型 1 注册添加、2 后台添加',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='经理人客户表';

-- ----------------------------
-- Records of sys_operator_customer
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_operator_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_operator_role`;
CREATE TABLE `sys_operator_role` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `operator_id` varchar(32) NOT NULL COMMENT '用户主键',
  `role_id` varchar(32) NOT NULL COMMENT '角色主键',
  PRIMARY KEY (`uuid`),
  KEY `fk_sys_user_role_role_id` (`role_id`) USING BTREE,
  KEY `fk_sys_user_role_user_id` (`operator_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

-- ----------------------------
-- Records of sys_operator_role
-- ----------------------------
INSERT INTO `sys_operator_role` VALUES ('aa701a23b86a11e6b0fefa163e2d23ab', '54c294528b5041ff838380676a2ed643', '22c0eef4a2c14515855d84f96eb991ae');

-- ----------------------------
-- Table structure for `sys_org`
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `org_no` varchar(32) DEFAULT NULL COMMENT '机构(部门)编号',
  `org_name` varchar(40) NOT NULL COMMENT '机构(部门)名称',
  `org_type` char(1) DEFAULT NULL COMMENT '类型(机构、部门)',
  `org_level` int(2) DEFAULT NULL COMMENT '层级，如： 根节点 0 一级部门 1  二级部门 2',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级ID',
  `parent_ids` varchar(512) DEFAULT NULL COMMENT '父级ID集',
  `sort` int(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`),
  KEY `idx_sys_org_name` (`org_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构部门表';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES ('dfcc6546a4d511e6b8d6408d5c2885aa', '1', '融都科技', null, '0', null, null, '1', null, '0', null);

-- ----------------------------
-- Table structure for `sys_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `uuid` varchar(32) NOT NULL,
  `code` varchar(64) NOT NULL COMMENT '权限标识,如: systemmange:user:page',
  `permission_name` varchar(40) DEFAULT NULL COMMENT '权限名称',
  `menu_id` varchar(32) DEFAULT NULL COMMENT '菜单ID',
  `operation_id` varchar(32) DEFAULT NULL COMMENT '功能操作ID',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `ak_uk_sys_permission_code` (`code`) USING BTREE,
  UNIQUE KEY `idx_sys_permission_code` (`code`) USING BTREE,
  KEY `fk_sys_permission_menu_id` (`menu_id`) USING BTREE,
  KEY `fk_sys_permission_operation_id` (`operation_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES(replace(UUID(),'-',''),'set:query:exp','实时红包对账','f846f42e7030413496b18139efc130cf','650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES(replace(UUID(),'-',''),'set:query:wdc','提现对账','7d3a8be5b46a43a7b1db032a4e5a8fa1','650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES(replace(UUID(),'-',''),'set:query:pdd','充值对账','33d8ce8fff0b4d03b9ddee5537cd323a','650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES(replace(UUID(),'-',''),'set:query:invest','投标对账','a04ab66398b94c4ca1619f9a6cb0c3cc','650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1', 'set:sys:org:add', '组织-新增', '9992d409c17b4c7996e9805f894ffcc7', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('121231a12311232131123123989889', 'set:operator:login:view', '经纪人-邀请', 'c5eab41d9d8d4de892ad4d7122f44112', 'ec40101ca17444a29bb1247175452ddd');
INSERT INTO `sys_permission` VALUES ('121231a123114546786asda', 'set:operator:login:edit', '信息-编辑', 'c5eab41d9d8d4de892ad4d7122f44112', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('121231awe752qwe67558', 'set:operator:pwd:edit', '密码-修改', '89c357d88df342b88cc3c29f49e60a73', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('123asd51236h187eug2e', 'set:operLog:query', '操作日志', '98d3ec42322044f788b5ea0c2a87ef80', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249611175294867495', 'set:logtemplate:add', '日志模板-新增', 'e3db465f991e421ea12342dd3b9b13b2', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249611175294877517', 'set:suggest:query', '意见反馈-查询', '7f83cb85060b4499a665735723e9362d', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249611440076095822', 'oper:score:userScore:export', '用户积分-导出', 'a39d0f69ccac4543a361bcf6e3392938', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249611440076095823', 'oper:score:scoreList:export', '积分记录-导出', '191536e89e1c4fed89a57f41920dcb18', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617225294867496', 'set:logtemplate:edit', '日志模板-编辑', 'e3db465f991e421ea12342dd3b9b13b2', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617633394867497', 'set:logtemplate:query', '日志模板-查询', 'e3db465f991e421ea12342dd3b9b13b2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617671234867629', 'project:product:sale:edit', '产品上架-编辑', '66ebb7d8daac40bfa6e2ad4e8104f1c1', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294800001', 'account:cashList:check', '提现-人工核查', '086d419bcf8b4e12a29dc40ea7de0844', 'ec40101ca17444a29bb1247175400001');
INSERT INTO `sys_permission` VALUES ('1249617675294860491', 'set:resource:add', '资源管理-新增', '22b308bd36744d3e8c94894a899c0394', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294860492', 'set:resource:edit', '资源管理-编辑', '22b308bd36744d3e8c94894a899c0394', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294860493', 'set:resource:query', '资源管理-查询', '22b308bd36744d3e8c94894a899c0394', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294863428', 'oper:redpacket:redpacketList:grant', '红包记录-发放', '91c57b3475a8433e82ff99a4b820c382', 'ec40101ca17444a29bb1247175452321');
INSERT INTO `sys_permission` VALUES ('1249617675294867468', 'set:sys:org:query', '组织-查询', '9992d409c17b4c7996e9805f894ffcc7', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867469', 'set:sys:user:add', '后台用户-新增', '542782979aad4f02ad7111f6c9e765ee', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867470', 'set:sys:user:del', '后台用户-删除', '542782979aad4f02ad7111f6c9e765ee', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867471', 'set:sys:user:edit', '后台用户-编辑', '542782979aad4f02ad7111f6c9e765ee', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867472', 'set:sys:user:query', '后台用户-查询', '542782979aad4f02ad7111f6c9e765ee', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867473', 'set:sys:org:del', '组织-删除', '9992d409c17b4c7996e9805f894ffcc7', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867474', 'set:sys:role:del', '角色-删除', '31f91e34272e4eb4a7d1986de4ea150f', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867475', 'set:sys:role:edit', '角色-编辑', '31f91e34272e4eb4a7d1986de4ea150f', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867476', 'set:sys:role:query', '角色-查询', '31f91e34272e4eb4a7d1986de4ea150f', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867477', 'set:sys:role:auth', '角色-授权', '31f91e34272e4eb4a7d1986de4ea150f', 'ec40101ca17444a29bb1247175452bf9');
INSERT INTO `sys_permission` VALUES ('1249617675294867478', 'set:sys:role:user:mgr', '角色-用户-管理', '31f91e34272e4eb4a7d1986de4ea150f', 'ec40101ca17444a29bb1247175452b11');
INSERT INTO `sys_permission` VALUES ('1249617675294867479', 'set:sys:role:user:del', '角色-用户-删除', '31f91e34272e4eb4a7d1986de4ea150f', 'ec40101ca17444a29bb1247175452ccc');
INSERT INTO `sys_permission` VALUES ('1249617675294867480', 'set:sys:role:add', '角色-新增', '31f91e34272e4eb4a7d1986de4ea150f', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867487', 'set:sys:menu:add', '菜单-新增', '9f0f275c5911427a9d59564b055a950c', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867488', 'set:sys:menu:del', '菜单-删除', '9f0f275c5911427a9d59564b055a950c', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867489', 'set:sys:menu:edit', '菜单-编辑', '9f0f275c5911427a9d59564b055a950c', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867490', 'set:sys:menu:query', '菜单-查询', '9f0f275c5911427a9d59564b055a950c', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867491', 'set:config:add', '参数设置-新增', 'c1483b2033214964b2d763c305971180', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867493', 'set:config:edit', '参数设置-编辑', 'c1483b2033214964b2d763c305971180', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867494', 'set:config:query', '参数设置-查询', 'c1483b2033214964b2d763c305971180', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867495', 'set:dict:add', '字典类型-新增', 'e3db465f991e421eadcd02dd3b9b13b2', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867496', 'set:dict:edit', '字典类型-编辑', 'e3db465f991e421eadcd02dd3b9b13b2', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867497', 'set:dict:query', '字典类型-查询', 'e3db465f991e421eadcd02dd3b9b13b2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867498', 'set:dict:contentadd', '字典内容-新增', 'e3db465f991e421eadcd02dd3b9b13b2', 'ec401012c17444a29bb1247175452456');
INSERT INTO `sys_permission` VALUES ('1249617675294867499', 'set:dict:contentedit', '字典内容-编辑', 'e3db465f991e421eadcd02dd3b9b13b2', 'ec40101ca17444a29bb1247175452c99');
INSERT INTO `sys_permission` VALUES ('1249617675294867502', 'set:msg:msgType:edit', '消息模板-编辑', '453b4c7da24e42e098644a8839cb2bfe', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867503', 'set:msg:msgType:query', '消息模板-查询', '453b4c7da24e42e098644a8839cb2bfe', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867507', 'vip:member:add', '个人用户-新增', 'cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867508', 'vip:member:edit', '个人用户-编辑', 'cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867509', 'vip:member:query', '个人用户-查询', 'cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867510', 'vip:member:freeze', '个人用户-冻结', 'cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', 'ec40101ca17444a29bb1247175451234');
INSERT INTO `sys_permission` VALUES ('1249617675294867511', 'vip:member:export', '个人用户-导出', 'cb4a9eb3d4d847b6ba3fc5f8ebfa54ec', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617675294867512', 'vip:company:add', '企业用户-新增', 'ca10cbdd993e44f79385ee0fa1b8b62e', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867513', 'vip:company:edit', '企业用户-编辑', 'ca10cbdd993e44f79385ee0fa1b8b62e', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867514', 'vip:company:query', '企业用户-查询', 'ca10cbdd993e44f79385ee0fa1b8b62e', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867515', 'vip:company:freeze', '企业用户-冻结', 'ca10cbdd993e44f79385ee0fa1b8b62e', 'ec40101ca17444a29bb1247175451234');
INSERT INTO `sys_permission` VALUES ('1249617675294867516', 'vip:company:export', '企业用户-导出', 'ca10cbdd993e44f79385ee0fa1b8b62e', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617675294867517', 'vip:vouch:add', '担保机构-新增', 'cc81470463a44a35bacfcf9e9b10bd4a', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867518', 'vip:vouch:query', '担保机构-查询', 'cc81470463a44a35bacfcf9e9b10bd4a', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867519', 'vip:qualification:query', '资质认证-查询', '0e9cbe92293944ddb6a2b83310eb31fd', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867520', 'project:typeMan:query', '类别管理-查询', '8ec02740ef6d4620b101589ff5942227', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867521', 'project:typeMan:add', '类别管理-新增', '8ec02740ef6d4620b101589ff5942227', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867522', 'project:product:add', '产品新增-提交', '783a91f0e7474325b7076352f7881765', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867523', 'project:product:productMan:query', '产品维护-查询', 'b449d0ffc8334dcfa0cbd5057e0c3245', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867524', 'project:product:productMan:edit', '产品维护-编辑', 'b449d0ffc8334dcfa0cbd5057e0c3245', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867525', 'project:product:productMan:cancel', '产品维护-作废', 'b449d0ffc8334dcfa0cbd5057e0c3245', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867526', 'project:product:publish:query', '产品-发布审核-查询', '356ab94e1cb94c9999ea11e3d9b15777', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867527', 'project:product:publish:auditing', '产品-发布审核-审核', '356ab94e1cb94c9999ea11e3d9b15777', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867528', 'project:borrow:sale:sale', '借款上架-上架', '0b3d8734926b4514b3e5a171de5107e3', 'ec40101ca17444a29bb1247171234765');
INSERT INTO `sys_permission` values ('1249617675294867028','project:borrow:sale:edit','借款上架-编辑','0b3d8734926b4514b3e5a171de5107e3','90b04389913b41008a69cead0cbf02b7');

INSERT INTO `sys_permission` VALUES ('1249617675294867529', 'project:borrow:sale:query', '借款上架-查询', '0b3d8734926b4514b3e5a171de5107e3', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867530', 'project:borrow:stop:query', '借款下架-查询', '0a1b98124c854381a403f767def3d124', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867531', 'project:borrow:stop:stop', '借款下架-下架', '0a1b98124c854381a403f767def3d124', 'ec40101ca17444a29bb1247175450987');
INSERT INTO `sys_permission` VALUES ('1249617675294867532', 'project:product:ev:query', '产品-成立审核-查询', '39af852544484d0b9dce4cb7c99564ec', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867533', 'project:product:ev:auditing', '产品-成立审核', '39af852544484d0b9dce4cb7c99564ec', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867534', 'project:product:investList:query', '产品-投资列表-查询', 'abb04a408212494489f529aad408dda3', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867535', 'project:product:productList:query', '产品列表-查询', '8aea4bc2842f4f5ebe31342cf0c9ea4b', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867536', 'project:borrow:add:add', '借款新增-提交', '9ab14bce07864d12b3ce2843315abc5c', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867538', 'project:borrow:borrowMan:cancel', '借款维护-作废', '70c49177b7664df9a3200074d18263bb', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867539', 'project:borrow:borrowMan:edit', '借款维护-编辑', '70c49177b7664df9a3200074d18263bb', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867541', 'project:borrow:borrowMan:query', '借款维护-查询', '70c49177b7664df9a3200074d18263bb', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867542', 'project:borrow:verify:query', '借款审核-查询', '07145b5a77aa4ffdbb41e96800ac0fbf', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867543', 'project:borrow:verify:auditing', '借款审核-审核', '07145b5a77aa4ffdbb41e96800ac0fbf', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867548', 'project:borrow:ev:query', '借款成立-查询', 'fbb532930f11419eae657051c959dd2e', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867549', 'project:borrow:ev:auditing', '借款成立-审核', 'fbb532930f11419eae657051c959dd2e', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867550', 'project:borrow:borrowList:query', '借款记录-查询', '914d4ad12e724cbfbb59456997242959', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867551', 'project:borrow:investList:query', '借款-投资记录-查询', '7ab02fac290345d8993623b3c9fdb677', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867552', 'project:borrow:collList:query', '待收记录-查询', '99830e3a22514f89af96d4c804649890', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867553', 'project:borrow:repayList:query', '还款记录-查询', '8b0802e0208d493c853c6561d9e1f754', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867554', 'project:borrow:follow:query', '贷后跟进-查询', 'ba1540b3cb32441cad6e14da6b4fa15b', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867555', 'project:borrow:overdue:query', '逾期管理-查询', 'b7415f6c0eee47f18ebab70cd5655ffe', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867556', 'project:borrow:advance:query', '垫付记录-查询', '604873a8ae66493fb1ac6c429c8803d6', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867558', 'project:realize:realizeRule:add', '变现规则-新增', 'a467e89e50a146ada2f4cbae197d1e9d', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867559', 'project:bond:bondRule:add', '转让规则-新增', 'c41abf5b7cac4a5989f9bf46e9d69afb', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867560', 'project:bond:bondList:query', '转让记录-查询', '91c8e1ce152a4f24a315698ef6ea0b49', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867563', 'project:bond:bondRule:query', '转让规则-查询', 'c41abf5b7cac4a5989f9bf46e9d69afb', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867564', 'set:riskTpl:riskLevel:add', '风险等级-新增', '172c5c180bef49c98d9836108b34cee8', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867565', 'set:riskTpl:riskLevel:query', '风险等级-查询', '172c5c180bef49c98d9836108b34cee8', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867566', 'set:riskTpl:riskLevel:del', '风险等级-删除', '172c5c180bef49c98d9836108b34cee8', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867567', 'set:riskTpl:riskLevel:edit', '风险等级-编辑', '172c5c180bef49c98d9836108b34cee8', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867568', 'set:riskTpl:question:add', '问卷管理-新增', '3655012406c94acaacbe979d52e14900', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867569', 'set:riskTpl:question:query', '问卷管理-查询', '3655012406c94acaacbe979d52e14900', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867570', 'set:riskTpl:question:del', '问卷管理-删除', '3655012406c94acaacbe979d52e14900', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867571', 'set:riskTpl:question:edit', '问卷管理-编辑', '3655012406c94acaacbe979d52e14900', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867572', 'set:riskTpl:question:answeradd', '问卷答案-新增', '3655012406c94acaacbe979d52e14900', 'ec40101ca17444a29bb1247175452093');
INSERT INTO `sys_permission` VALUES ('1249617675294867573', 'set:riskTpl:question:answerdel', '问卷答案-删除', '3655012406c94acaacbe979d52e14900', 'ec40101ca17444a29bb1247175234ccc');
INSERT INTO `sys_permission` VALUES ('1249617675294867574', 'set:riskTpl:question:answeredit', '问卷答案-编辑', '3655012406c94acaacbe979d52e14900', 'ec40101ca17444a29bb1247175452992');
INSERT INTO `sys_permission` VALUES ('1249617675294867575', 'set:riskTpl:answer:query', '评测记录-查询', '8febc2d0a04d4a42a3f8904970778b8c', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867576', 'account:cashList:export', '提现-导出', '086d419bcf8b4e12a29dc40ea7de0844', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617675294867577', 'account:cashList:auditing', '提现-审核', '086d419bcf8b4e12a29dc40ea7de0844', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867578', 'account:cashList:query', '提现-查询', '086d419bcf8b4e12a29dc40ea7de0844', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867579', 'account:rechargeList:query', '充值-查询', 'af599d39657e4c7dae33ca9ff8a82dcd', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867580', 'account:rechargeList:export', '充值-导出', 'af599d39657e4c7dae33ca9ff8a82dcd', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617675294867581', 'account:accountList:query', '资金记录-查询', '948ccc45bfc04e56b7c6831ba7486bc7', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867582', 'account:accountList:export', '资金记录-导出', '948ccc45bfc04e56b7c6831ba7486bc7', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('1249617675294867583', 'account:userAccount:query', '用户账户资金-查询', '6282fd375d3948b9b511a577aa3715dc', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867584', 'account:platAccount:query', '平台账户（流水/余额）-查询', '99413546529348cda88f62d4d809c911', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867585', 'account:platAccount:assign', '平台账户-子账户划拨', '99413546529348cda88f62d4d809c911', 'ec40101ca17444a29bb124717545009');
INSERT INTO `sys_permission` VALUES ('1249617675294867586', 'account:platAccount:transfer', '平台账户-用户转账', '99413546529348cda88f62d4d809c911', 'ec40101ca17444a29bb124717545098');
INSERT INTO `sys_permission` VALUES ('1249617675294867587', 'account:platAccount:recharge', '平台账户-充值', '99413546529348cda88f62d4d809c911', 'ec40101ca17444a29bb1247175452456');
INSERT INTO `sys_permission` VALUES ('1249617675294867588', 'account:platAccount:cash', '平台账户-提现', '99413546529348cda88f62d4d809c911', 'ec40101ca17444a29bb1247175452222');
INSERT INTO `sys_permission` VALUES ('1249617675294867589', 'set:website:section:add', '栏目管理-新增', '2812eb5139054b00be156d3096cbd944', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867590', 'set:website:section:query', '栏目管理-查询', '2812eb5139054b00be156d3096cbd944', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867591', 'set:website:article:add', '文章管理-新增', 'd0a1d4764e89473c989319f5a2fc33e3', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867592', 'set:website:article:query', '文章管理-查询', 'd0a1d4764e89473c989319f5a2fc33e3', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867593', 'oper:actPlan:query', '活动方案-查询', '3e4323cd2e924860865ede6d4f35e994', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867594', 'oper:actPlan:cancel', '活动方案-禁用', '3e4323cd2e924860865ede6d4f35e994', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867595', 'oper:redpacket:redpacketRule:add', '红包规则-新增', 'd9aefea965d94f13a136956f9bfafe0a', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867596', 'oper:redpacket:redpacketRule:query', '红包规则-查询', 'd9aefea965d94f13a136956f9bfafe0a', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867597', 'oper:redpacket:redpacketRule:del', '红包规则-删除', 'd9aefea965d94f13a136956f9bfafe0a', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867598', 'oper:redpacket:redpacketRule:edit', '红包规则-编辑', 'd9aefea965d94f13a136956f9bfafe0a', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867599', 'oper:redpacket:redpacketList:cancel', '红包记录-作废', '91c57b3475a8433e82ff99a4b820c382', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867600', 'oper:redpacket:redpacketList:query', '红包记录-查询', '91c57b3475a8433e82ff99a4b820c382', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867601', 'oper:addApr:addAprRule:add', '加息券规则-新增', 'a26352064b324c839891e40e9046d5ea', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867602', 'oper:addApr:addAprRule:query', '加息券规则-查询', 'a26352064b324c839891e40e9046d5ea', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867603', 'oper:addApr:addAprRule:del', '加息券规则设置-删除', 'a26352064b324c839891e40e9046d5ea', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867604', 'oper:addApr:addAprRule:edit', '加息券规则设置-编辑', 'a26352064b324c839891e40e9046d5ea', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867605', 'oper:addApr:addAprList:query', '加息券记录-查询', '376c627aac1748a1aa7faffb50c18187', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867606', 'oper:addApr:addAprList:cancel', '加息券记录-作废', '376c627aac1748a1aa7faffb50c18187', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867607', 'oper:vip:vipPack:add', 'VIP礼包-新增', 'be89d0803efe402a9697a40689ebda31', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867608', 'oper:vip:vipPack:query', 'VIP礼包-查询', 'be89d0803efe402a9697a40689ebda31', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867609', 'oper:vip:vipPack:del', 'VIP礼包-删除', 'be89d0803efe402a9697a40689ebda31', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867610', 'oper:vip:vipPack:edit', 'VIP礼包-编辑', 'be89d0803efe402a9697a40689ebda31', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867611', 'oper:vip:vipMan:add', 'VIP等级配置-新增', '82da1ec38a83453f9bf835da0e6302f2', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867612', 'oper:vip:vipMan:query', 'VIP等级配置-查询', '82da1ec38a83453f9bf835da0e6302f2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867613', 'oper:vip:vipMan:del', 'VIP等级配置-删除', '82da1ec38a83453f9bf835da0e6302f2', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867614', 'oper:vip:vipMan:edit', 'VIP等级配置-编辑', '82da1ec38a83453f9bf835da0e6302f2', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867615', 'oper:vip:vipUser:query', 'VIP用户-查询', 'd20738c990c4417ba2b49af085ac8a8c', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867616', 'oper:vip:vipGrowth:query', 'VIP成长日志-查询', '82da1ec38a83453f9bf835da0e6302f3', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867617', 'oper:score:userScore:query', '用户积分-查询', 'a39d0f69ccac4543a361bcf6e3392938', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867618', 'oper:score:scoreList:query', '用户积分日志-查询', '191536e89e1c4fed89a57f41920dcb18', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867619', 'project:protocol:tempalte:add', '协议模板-新增', 'db08669c140e4e28a2238646ee10f111', 'ec40101ca17444a29bb1247175452bf0');
INSERT INTO `sys_permission` VALUES ('1249617675294867620', 'project:protocol:tempalte:query', '协议模板-查询', 'db08669c140e4e28a2238646ee10f111', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867621', 'project:protocol:tempalte:edit', '协议模板-编辑', 'db08669c140e4e28a2238646ee10f111', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867622', 'project:protocol:tempalte:cancel', '协议模板-禁用', 'db08669c140e4e28a2238646ee10f111', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867625', 'vip:qualification:auditing', '资质认证-审核', '0e9cbe92293944ddb6a2b83310eb31fd', 'ec40101ca17444a29bb1247175452123');
INSERT INTO `sys_permission` VALUES ('1249617675294867626', 'project:typeMan:edit', '类别管理-编辑', '8ec02740ef6d4620b101589ff5942227', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867627', 'project:typeMan:del', '类别管理-删除', '8ec02740ef6d4620b101589ff5942227', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294867628', 'project:product:sale:sale', '产品上架-上架', '66ebb7d8daac40bfa6e2ad4e8104f1c1', 'ec40101ca17444a29bb1247171234765');
INSERT INTO `sys_permission` VALUES ('1249617675294867629', 'project:product:sale:query', '产品上架-查询', '66ebb7d8daac40bfa6e2ad4e8104f1c1', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867630', 'project:product:stop:stop', '产品下架-下架', '4e951fb9c90d4679b92998b6bb6fa483', 'ec40101ca17444a29bb1247175450987');
INSERT INTO `sys_permission` VALUES ('1249617675294867631', 'project:product:stop:query', '产品下架-查询', '4e951fb9c90d4679b92998b6bb6fa483', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867633', 'project:borrow:follow:edit', '贷后跟进-跟进', 'ba1540b3cb32441cad6e14da6b4fa15b', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867634', 'project:borrow:overdue:edit', '逾期管理-记录', 'b7415f6c0eee47f18ebab70cd5655ffe', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867635', 'consumer:consumerList:query', '客户列表-查询', '0f934a2ed82b46cabd0e288161929eb0', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867636', 'consumer:performance:query', '业绩统计-查询', 'b0e54a0b29eb4218b137a5de708dece2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867637', 'oper:userInvite:inviteStat:query', '邀请统计-查询', '879c53ceed324fd8a63f06f3876acd2b', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867638', 'oper:userInvite:inviteReward:query', '邀请奖励-查询', 'b9cc5f669454439f8a28b81fc4a7cf8b', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867640', 'project:borrow:bespeak:follow', '预约跟进-编辑', '099e7b2858994d3ca59fedc491634f68', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294867641', 'project:borrow:bespeak:query', '预约跟进-查询', '099e7b2858994d3ca59fedc491634f68', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('1249617675294867698', 'oper:redpacket:redpacketRule:cancle', '红包规则-禁用', 'd9aefea965d94f13a136956f9bfafe0a', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294867709', 'oper:addApr:addAprRule:cancel', '加息券规则-禁用', 'a26352064b324c839891e40e9046d5ea', 'ec40101ca17444a29bb1247175452abc');
INSERT INTO `sys_permission` VALUES ('1249617675294868592', 'set:website:article:edit', '文章管理-修改', 'd0a1d4764e89473c989319f5a2fc33e3', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294868617', 'vip:vouch:edit', '担保机构-修改', 'cc81470463a44a35bacfcf9e9b10bd4a', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('1249617675294868691', 'set:website:article:del', '文章管理-删除', 'd0a1d4764e89473c989319f5a2fc33e3', 'cc53ee6bba3947a392447e49b8f5e4f1');
INSERT INTO `sys_permission` VALUES ('1249617675294877517', 'vip:vouch:freeze', '担保机构-冻结', 'cc81470463a44a35bacfcf9e9b10bd4a', 'ec40101ca17444a29bb1247175451234');
INSERT INTO `sys_permission` VALUES ('1249617675294887566', 'project:borrow:overdue:export', '逾期管理-导出', 'b7415f6c0eee47f18ebab70cd5655ffe', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('12496176752qwe67558', 'project:realize:realizeList:query', '变现记录-查询', '898de066d9c74fcabc7c5347d0d24e20', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c07d6', 'vip:vouch:export', '担保机构:导出', 'cc81470463a44a35bacfcf9e9b10bd4a', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('3', 'set:sys:org:edit', '组织-编辑', '9992d409c17b4c7996e9805f894ffcc7', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('asd123f34hfdghtgh34534', 'set:msg:msg:query', '消息记录', '6fc5976458c3415c894922d454be7293', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('ba8c6633a0fa11e69638000c296f8d8c', 'set:sys:user:resetPwd', '后台用户管理-重置密码', '542782979aad4f02ad7111f6c9e765ee', '94149ff5a0fa11e69638000c296f8d8c');
INSERT INTO `sys_permission` VALUES ('bd6aaf15848e11e699af000c296f8d8c', 'set:control:edit', '调度任务-重新触发', '86c4b1ce0688456384f223f35014dfd1', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d8c', 'set:control:query', '调度任务-查询', '86c4b1ce0688456384f223f35014dfd1', '650237d88b5b4a2ea4fbb1a8c187aa22');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:product:investList:export','产品-投资列表-导出','abb04a408212494489f529aad408dda3','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:product:productList:export','产品-产品列表-导出','8aea4bc2842f4f5ebe31342cf0c9ea4b','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:borrow:investList:export','借款-投资记录-导出','7ab02fac290345d8993623b3c9fdb677','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:borrow:borrowList:export','借款-借款记录-导出','914d4ad12e724cbfbb59456997242959','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:borrow:collList:export','借款-待收记录-导出','99830e3a22514f89af96d4c804649890','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:borrow:repayList:export','借款-还款记录-导出','8b0802e0208d493c853c6561d9e1f754','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'project:borrow:advance:export','借款-垫付记录-导出','604873a8ae66493fb1ac6c429c8803d6','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'account:userAccount:export','用户账户列表-导出','6282fd375d3948b9b511a577aa3715dc','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'account:platAccount:export','平台账户-导出','99413546529348cda88f62d4d809c911','ec40101ca17444a29bb1247175452124');
insert into sys_permission values( REPLACE(uuid(),'-',''),'consumer:consumerList:add','客户列表-新增','0f934a2ed82b46cabd0e288161929eb0','ec40101ca17444a29bb1247175452bf0');
insert into sys_permission values( REPLACE(uuid(),'-',''),'consumer:consumerList:edit','客户列表-编辑','0f934a2ed82b46cabd0e288161929eb0','90b04389913b41008a69cead0cbf02b7');
insert into sys_permission values( REPLACE(uuid(),'-',''),'set:website:section:edit','栏目管理-编辑','2812eb5139054b00be156d3096cbd944','90b04389913b41008a69cead0cbf02b7');
insert into sys_permission values( REPLACE(uuid(),'-',''),'set:website:section:del','栏目管理-删除','2812eb5139054b00be156d3096cbd944','cc53ee6bba3947a392447e49b8f5e4f1');
insert into sys_permission values( REPLACE(uuid(),'-',''),'oper:addApr:addAprList:add','加息券记录-发放','376c627aac1748a1aa7faffb50c18187','ec40101ca17444a29bb1247175452bf0');

-- ----------------------------
-- Table structure for `sys_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `res_name` varchar(100) DEFAULT NULL COMMENT '资源标识',
  `res_text` varchar(200) DEFAULT NULL COMMENT '资源信息内容',
  `res_language` varchar(10) DEFAULT NULL COMMENT '语言',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_resource` (`res_name`,`res_language`) USING BTREE,
  UNIQUE KEY `uk_sys_resource_res_name` (`res_name`) USING BTREE,
  KEY `ak_uk_sys_resource_name` (`res_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统资源';

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
 INSERT INTO `sys_resource` VALUES ('64e5ea9e8037413f84a7a65ba1c0ff4b', 'rateCoupon.max.period.error', '固定期加息券发放最大有效期提示', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('b8109f2ed468430f94ec09d3c429d8b6', 'customer.user.is.not.personal', '添加客户不能是担保用户或者企业用户', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('0ec84c54a991444982c55d4f5bd6181b', 'cbhb.resource.error.request.borrowId.empty', '渤海银行请求标的ID不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('11b736ca9378475492f2fe3bc21243a8', 'cbhb.resource.error.request.charset.empty', '渤海银行请求字符编码不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('136729940db64cfa8302013f75525978', 'cbhb.resource.error.request.borrowTyp.empty', '渤海银行请求标的属性不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('1c708f97c80d485f97278c7f3279c70d', 'cbhb.resource.error.request.mobileNo.empty', '渤海银行请求手机号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('235cd40e2f64428bb9b60766257beab8', 'cbhb.resource.error.request.usrName.empty', '渤海银行请求姓名不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('2c7f9b5526de452e8b8f1faca8c9ba05', 'cbhb.resource.error.request.pageReturnUrl.empty', '渤海银行请求页面返回url不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('2d96fe15e2db4b8ca99c18cb6edad591', 'cbhb.resource.error.request.mobileOrPlaCustId.empty', '手机号和账户存管平台客户号不可同时为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('3a96ba32060d4004b0cf0697d8069b85', 'cbhb.resource.error.request.borrowStartDate.empty', '渤海银行请求募集开始日不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('3d0439613a8045008cf52f36e0ad4e1d', 'cbhb.resource.error.request.projectNo.empty', '渤海银行请求标的项目编号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('576d4613cafc43e1b7981832af62dbde', 'cbhb.resource.error.submit', 'http请求错误', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('6d21ff9242304d7fb715b61130bf0617', 'cbhb.resource.error.request.placustId.empty', '渤海银行请求存管账户号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('70ff074ee66a40e58e2c8d00a2015e6e', 'cbhb.resource.error.request.oldTransId.empty', '渤海银行请求原账户存管平台流水不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('72438555ae39426cb4f832572f4e7105', 'cbhb.resource.error.request.openType.empty', '渤海银行请求开户类型不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('767a5b3eaceb4e4cb3719ab24724bff6', 'cbhb.resource.error.request.identiType.empty', '渤海银行请求证件类型不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('7d2fa3e0cbad453783c32dcc78253707', 'cbhb.resource.error.request.identNo.empty', '渤海银行请求证件号码不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('80b64a9ba1d24c6baf3696c3920d73b4', 'cbhb.resource.error.request.params.sign', '请求参数签名异常:{0}', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('81ed82341d0b4b30a961c69d5b5b3b89', 'cbhb.resource.error.urldecode', 'UrlDecode解码失败', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('82830a9d9fde47059a06e2c1922fcd82', 'cbhb.resource.error.request.submitUrl.empty', '渤海银行请求提交地址不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('838b18762c0f42e29114e8756a6b21cf', 'cbhb.resource.error.request.partnerId.empty', '渤海银行请求商户号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('86b5b6062d5d4b4ca462ecea788cab23', 'cbhb.resource.error.request.releaseType.empty', '渤海银行请求放款方式不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('8de1d984ef454457a2448aa5a6edd986', 'cbhb.resource.error.request.merBillNo.empty', '渤海银行请求商户流水号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('9812323bea3945cd9c28323d54460a68', 'cbhb.resource.error.request.batchNo.empty', '渤海银行请求批次号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('a1b2f97e928f4924aa37a02dd5f2e163', 'cbhb.resource.error.request.merFeeAmt.empty', '渤海银行请求商户手续费收入不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('a357691d04b14bd989d4043766b3626a', 'cbhb.resource.error.request.borrCustId.empty', '渤海银行请求借款人tppCustId不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('b4a4f9a2741f40249e3c2ad352f8ede1', 'cbhb.resource.error.request.versionNo.empty', '渤海银行请求版本号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('b5f7cf0d826746dfb0474e9b46d58f14', 'cbhb.resource.error.request.borrowerInterestAmt.empty', '渤海银行请求标的年化率不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('bae86f60ea97435db6fde6410e5006bd', 'cbhb.resource.error.bindCard', '修改银行卡返回码为{0},原因：{1}', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('bd4ff513ccf648e68f2f11e75d8aeef0', 'cbhb.resource.error.request.freezeId.empty', '渤海银行请求冻结编号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('be2ca7b852f545688d92bf5d9ae1398e', 'cbhb.resource.error.request.feeType.empty', '渤海银行请求手续费模式不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('c0903ec0854b4e2ea9a21ff06791537a', 'cbhb.resource.error.request.guaranteeNo.empty', '渤海银行请求担保人账号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('c53b87d2db274107949592ada7211345', 'cbhb.resource.error.request.borrowRepayDate.empty', '渤海银行请求标到期日不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('c7c5c814495442c2b081107e8ed370f3', 'cbhb.resource.error.request.notifyUrl.empty', '渤海银行请求后台通知url不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('d3da581aad844af798f8fdfe09911a59', 'cbhb.resource.error.request.smsCode.empty', '渤海银行请求短信验证码不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('d48170bccabc4dcb8bbd43a5ed49063e', 'cbhb.resource.error.privateKey.init', '私钥初始化错误', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('e1bba78c12064cfea6be85327d5af09d', 'cbhb.resource.error.request.openBankId.empty', '渤海银行请求开户银行代号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('e572ffe300a34a03bfc3ad6f102ba768', 'cbhb.resource.error.bindMobile', '修改手机号返回码为：{0},原因:{1}', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('e73a9fb9abbb411b84dca16ddb2a6df3', 'cbhb.resource.error.request.signType.empty', '渤海银行请求签名类型不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('e9bc6e2984374bd7a6e9c641ecc1f8f9', 'cbhb.resource.error.request.borrowEndDate.empty', '渤海银行请求募集到期日不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('ea984845700043c79ae4e7e739de6313', 'cbhb.resource.error.request.openAcctId.empty', '渤海银行请求开户银行账号不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('fa8e62408d334721a0d80ee0ad4d117f', 'cbhb.resource.error.response.params.sign', '响应回调参数验签异常:{0}', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('fead0460d484464da8b07f9e0ba0b6da', 'cbhb.resource.error.request.oldTenderMoney.empty', '渤海银行请求原投标金额不能为空', 'zh_CN');
 INSERT INTO `sys_resource` VALUES ('ff29fc2952254377b1cc78ed9e6c33dc', 'cbhb.resource.error.request.transAmt.empty', '渤海银行请求交易金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e8000001', 'order.type.cash', '提现', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e8000002', 'org.code.is.used', '该组织机构代码已被使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e8000003', 'credit.code.is.used', '该社会信用代码已被使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e8000004', 'bussiness.code.is.used', '该营业执照注册号已被使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e896689c', 'invest.fail', '投资失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00973c6e8a9848aa95f9f8e6e896689g', 'account.is.locked', '账号已被锁定，请您联系客服', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('00b9958aab60481497644822d5543920', 'user.authSign.status', '为方便托管方在本平台进行相关资金操作，请您进行业务授权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('015548feeeef4bab84593ebf37fc231c', 'user.riskLevel.isEmpty', '未进行风险评估', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('01a8df3672cf4a99839bbf2054952487', 'user.vip.growthUp.investSuccess', '投资成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('01d695cbac90416c8710a8009ae1d2a8', 'login.fail.for.userName.error', '用户名不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('01e868cea9b9433ba223fed0719a1610', 'idcard.is.exist', '身份证号已存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('01e868cea9b9433ba223fed0719aaaaa', 'cash.over.tpp.money', '抱歉，您的提现已超额！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('022745ded4814a8fbb4434c0bc241c1', 'user.tpp.register.success', '开户成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('022745ded4814a8fbb4434c0bc241c11', 'core.invest.investNoviceAgain', '您已经不是新手了，不能进行投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('02f9749bf4a9479fbf8be8c736571d88', 'user.id.is.not.null', '用户ID不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('03563580f9bb4bd7829784a5adfcb1d1', 'invest.orderNo.notEmpty', '投资订单号不可为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0360e16f318b4abb9b7042414cfe3307', 'project.saleMail.formatError', '定向销售邮箱域名格式错误（如：*.com,*.com)', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('03d9e6a2cb3346488aefd4892cafe81c', 'common.email.format.error', '请输入正确的电子邮箱', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('054e6e82e60f45899ec4bb39c4d8f32c', 'autoInvestRuleLog.need.realName', '您需开通第三方资金托管账户，才能开启自动投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('055fd066fc6046ecb5ac25e7a049dc0f', 'project.specificSaleRule.notEmpty', '产品销售规则不可为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('05a6cde31ab44996a2b59440eaec3285', 'growthvalue.must.lt.downlevel', '所需成长值需小于下级所需成长值:{0}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('05f2cef6951a492785ddefcec983bf01', 'come.back.to.user.center', '返回个人中心！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('05f2cef6951a492785ddefcec983bf02', 'user.tpp.register.fail', '开户失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('05f2cef6951a492785ddefcec983bf03', 'tpp.notify.sign.fail', '回调验签失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('05f2cef6951a492785ddefcec983bf07', 'user.pwd.error', '密码为8-16位字符，至少包含1位字母和1位数字', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('060079db48ab4765bb39ceca3fb0f00f', 'global.authSign.statusError', '请先通过授权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0602c1bd1e76479a8405f0482a88141c', 'redenvelope.rednum.undivisible.totalnum', '发放数量需为{0}倍数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('061eb33b3ea54457a48bd233a52422ce', 'bond.message.bondNotTransferableError', '原始项目发布时设置了不允许转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0641d42b8165486cbdaa11d0730a7a60', 'bond.message.bondInvest.unpay.tooMuch', '未支付的债权订单过多', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('06544ee3bf1f4659be4ec4beb2665770', 'realize.capital.must.gt.one', '变现借款本金必须大于1元', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('066b6dfa604c4e9b98915da65dc05372', 'project.showTime.gtSaleTime', '上架时间不能晚于开售时间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('07bfb04f813e11e699af000c296f8d8c', 'redEnvelopeRule.message.startTime.endTime.error', '发放结束时间不能小于发放开始时间!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('086501f2609944e1abb99ac5d23d59d3', 'project.verify.remark.lengthError', '审核备注长度错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('087c3e4727fc4679abd6bd1644b20c64', 'invest.status.notright', '投资状态不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('09dc868bf9124a8bb423e9e4d5e2b4c7', 'calculator.fixedRepayDay.outOfRange', '固定还款日不能大于{0}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0acdd7de96f444118231b8b7e15eb68b', 'web.invest.projectIdIsNull', '未指定投资项目', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0c014e36b112424a9d50343d670be4d1', 'project.addapr.rangeError', '加息利率不能超过{0}%', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0c6dade2a6dd422f96bac9f443ea1039', 'bond.message.holddays.zero', '勾选起息日限制持有天数必须大于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0f7403317ef011e699af000c296f8d8c', 'risk.messgae.papers.update.fail', '修改失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('0f9be5f59b1a4b7eae923b7ecf7d02a3', 'borrow.userId.notEmpty', '借款方不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1041fafb69464210b3a3dff9ed201d82', 'bond.message.bondNotExists', '债权项目不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('105087c5a07f40d099108efb0303590c', 'borrow.stop.remark.over.length', '下架原因长度不能超过128', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1060701b2dc54cf3867286022f91f2c0', 'credit.code.error', '请输入长度为18位的社会统一信用代码', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('10702e60690841ef956ef67f490110b9', 'realize.fixedRepaymentTime.not', '有固定还款日的产品不支持变现！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('10f1cee87ee511e699af000c296f8d8c', 'risk.messgae.level.already.set.error', '该试卷设定等级已经完成,请勿再次设定', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('111222', 'use.validday.notempty', '有效期为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90001', 'answer.first.is.error', '问题一的密保答案错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90002', 'answer.two.is.error', '问题二的密保答案错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90003', 'answer.three.is.error', '问题三的密保答案错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90004', 'picPath.is.empty', '图片路径不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90005', 'qualification.can.not.be.empty', '证明资料类型不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90006', 'qualification.picPaths.at.least.for.one', '请至少上传一份认证资料', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90007', 'qualification.picPaths.reach.max', '认证图片最多上传[{0}]张', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90008', 'qualification.is.auditing', '证明资料正在待审核，无法上传新的证明资料', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90009', 'need.to.select.one.record', '请先选择一条记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90011', 'answer.first.is.empty', '问题一的答案不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90012', 'answer.two.is.empty', '问题二的答案不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90013', 'answer.three.is.empty', '问题三的答案不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90014', 'question.first.is.empty', '问题一不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90015', 'question.two.is.empty', '问题二不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90016', 'question.three.is.empty', '问题三不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1140dd1893454aa098474e1049f90017', 'question.is.repeat', '密保问题重复，请重新设置！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('11736f95c24e41349856b3fa18f91ac6', 'operator.empty', '后台用户不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('11df3cf4cf764ebc8de1923361bb4827', 'projectInvestBespeak.exist', '您已经预约投资成功，不能重复预约', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('123c17d202934cf4bc3e17830021af86', 'project.projectType.noExists', '选择类别不存在或已删除！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1240ae14fb64422c820b21bc6366571c', 'bond.message.bondPublishFailError', '债权发布失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1242e708621f43ada2f0944935736757', 'to.user.is.null', '规则发放对象为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1255357b38e64741a076632762f99746', 'core.project.investTime.emptyMsg', '投资时间不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('128affe01fa64135a8e11634a3ae83a0', 'project.timeLimit.outOfDays', '期限不能超过{0}天', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('12a33cced7ab4b718dec79977e20af3e', 'core.invest.mountNotMatchStepError', '投资金额不匹配递增金额！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('12a39205b0c14afd9e7951dfd1017b2c', 'borrowBespeak.contactName.emptyMsg', '请填写您的姓名', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('12f15e24468b4b5f9703eedfe2e155c0', 'email.cannotbe.same', '修改的邮箱不能与原邮箱相同', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1322683e740b44c2ade3206a6b0d10bc', 'realize.repaystyle.isnot.right', '还款方式不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('133aafb34e404b94bfbbe454a5d753e0', 'borrow.projectName.notEmpty', '借款名称不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('134c309515504399a3a424ba213c9591', 'invest.order.hasHandled', '投资回调重复处理', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('141286c31f4d47a99fd10cee13aa58df', 'borrow.content.notEmpty', '借款详情不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('14673b6092e640f2aaba06c45019d120', 'project.projectType.mustLiquidError', '活期产品请选择活期类别！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('15acfc5d6d0511e6b37f000c293c07d6', 'bond.message.rule.tppBuyLimit', 'UFX对接汇付第三方接口不允许债权部分受让,请选择一次性全额受让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('165f67230402416c9c32f7479c3a5d85', 'manage.projectType.typeName.lengthError', '类别名称长度需不大于5', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('16f038760b484c738137e3df099d9a2b', 'core.invest.bothCompanyError', '您不可以投资企业用户的借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('17d54b55649847a5b816a90eb1f50ea7', 'realize.notLimitTime.not', '期限未设置的产品不支持变现！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('180583ceb6684f72bafbed42ba61d6b7', '发的萨芬发的', '发的萨芬', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('189ae2c37ef311e699af000c296f8d8c', 'risk.messgae.qestion.is.wait.add', '请添加试题!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('18fee94dc68f476d9b5fa9e11cb8190a', 'use.money.no.use.money.cant.increase.together', '更新失败，可用金额和不可用金额 不可同时都增加', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('19ba445f6dbe11e6b37f000c293c07d6', 'core.project.investAfterSaleEnd', '项目募集时间已经结束！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a171ee90ca14147a4ff88cba219e1f8', 'article.content.outofRange', '文章详情不能超过{0}个字符', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a1b23e20ef949f99ab47cb99f87baee', 'projectType.parentId.notEmpty', '请选择上级节点', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a473699d38348899f41f1849a0c1c24', 'realize.must.fullrezlize', '变现成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a74c934c76f4c69b0dd0749d2b63d18', 'money.not.less.than.zero', '金额不能小于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a974e089f324c9387ae888b49cae307', 'article.message.objectNotExists', '栏目不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1ad95ca081c646fda91308612fc8a864', 'project.interestStyle.notEmpty', '计息方式不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1c37d81053f64e18bd79f52abc20d89a', 'user.userName.repeat', '用户名重复，请更换用户名', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1c4f28b7290f43b9898892640c1e6983', 'realize.repay.isnot.right', '变现还款信息异常', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1d420a9a99ab4728962f6ced471f841a', 'global.mq.connectError', '消息队列连接错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e1c43aaa1ca4f90849f392b23f29b78', 'borrow.vouch.error', '担保机构不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e40dd1793454aa098454e1049f929e4', 'user.mobile.error', '手机号码格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e40dd1793454aa098454e1049f929e5', 'check.mobile.is.empty', '手机号码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e40dd1793454aa098454e1049f929e6', 'check.userName.is.empty', '用户名不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e40dd1793454aa098454e1049f929ea', 'update.mobile.status.fail', '更新手机绑定状态失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1e40dd1793454aa098454e1049f929eb', 'update.email.status.fail', '更新邮箱绑定状态失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1ecebfa3a9b743bea2bb1bb40ac26eee', 'project.timeLimit.quarterError', '每季还息到期还本，借款期限数值必须为3的整数倍', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1fa64e3f6cc011e6b37f000c293c07d6', 'bond.message.bondInvest.notExists', '受让记录不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('200be1cf83f0410daff5163aad5079ef', 'use.money.no.use.money.cant.decrease.together', '更新失败，可用金额和不可用金额 不可同时都减少', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('200c0fab03134fa09fd9ee5c5029b460', 'project.projectType.selectLiquidError', '非活期产品请选择非活期类别！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('205fdcbbeeb743d08ae81b53618c302e', 'autoInvestRuleLog.open.realName', '去开通', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2098dd7d31a44e2297dfaaae67233efc', 'core.redEnvelope.useTooMuchError', '单笔投资使用红包过多', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2200f191690011e68586000c293c07d6', 'bond.message.borrowNotExists', '借款项目不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('220c9f0c7ee611e699af000c296f8d8c', 'risk.messgae.papers.level.is.exists', '试卷已有等级,请点击编辑!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('223da079c4dc4632bd449a364a3f3111', 'invest.bespeak.success', '您已预约成功，系统会在开售前提醒您前来投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('224abee3854a11e699af000c296f8d8c', 'bond.message.invest.is.pay.status', '非待支付状态,不能支付', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('23357b7e7f1740f281c4607a2cca3506', 'global.del.noRecord', '请勾选需要删除的记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('23a00c649117433bae6bd93d38f04e09', '11111', '11111', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('23da44061a6c471d807efe7993ae264e', 'realize.nowRepaymentStyle.not', '当前还款方式不支持变现！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2433c904060e425bbd63a34264d5856d', 'autoInvestRuleLog.timeType.notEmpty', '请至少填写一个投资期限区间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('26873296d917459e8bd10ce64ddb2988', 'protocol.message.status_valid.only.one', '同一类型的债权/变现有效协议只能有一个,故不能启用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('26fe471761df47c5884dee2bdd1b626c', 'core.rateCoupon.cannotUseError', '项目不可使用加息券', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2705de563ccb426d8687b5d072b7ec6a', 'borrow.account.rangeError', '借款金额范围须在{0}到{1}之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('27d78c5d17c940e99ef0cbba90515641', 'realize.realizeRateMax.emptyMsg', '请填写变现利率上限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('281e72da6d07459189b6ddf65437f513', 'bond.message.bondIsSaleOkError', '债权已经售完，不可撤回', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('28481d7085ac4d569eb7aa1bc1b205af', 'user.pwd.manage.error', '默认密码格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('28ff1034562e45eea6f68e93097442c7', 'bond.message.bondRuleNotStartError', '债权规则尚未开启，不允许发布债权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('298431b46e9a4fe8965090cf5aaf9644', 'customer.remark.over.length', '备注长度不能超过256', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('29ec04ff92f849e0a4505f6ce7efdc16', 'product.borrower.noExists', '借款人不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2a326e0aeec8487c99c6df0f0a883489', 'realize.not.less.sellAmountMin', '变现金额不能小于最小变现金额{0}元', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2a55e0aadc354d2d9d79fc3b131b00b7', 'bond.message.borrowUserNotInvestError', '原始项目借款人不能投资自己借款下的债权！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2a8223d5f7054c04ba94b81fb5d8e33a', 'borrowBespeak.money.formatError', '请输入正整数的借款金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2aa205c9a157496aac70f6c8b071e3c3', 'tpp.investFail.failError', '撤销投资金额失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2c24072e6cbf11e6b37f000c293c07d6', 'bond.message.bondInvestId.notExists', '回款计划受让记录标识不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2c2d8054a222445e8e8dd8b8f9782dfd', 'autoInvestRuleLog.go.recharge', '立即充值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2c2dfd773a5a4939b8d21ecfa6fdf4bb', 'article.title.valid', '文章标题不能为空，并且不能超过{0}个字符！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2c96074845a8448c88f8ab337c4a6a80', 'operator.first.newPwd.again.error', '新密码与初始密码不能相同', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2d0746d78b81461f90c0ec2e1a708d12', 'fixed.repay.day.error', '固定还款日的区间应该在1-31之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2d1c7d2b8299427190738d6321a8fa60', 'user.taxRegNo.lenghtError', '税务登记证号格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2e4a5a5c84d64d2baa8f710e255d85c7', 'realize.is.not.raiseend', '变现项目不在成立待审状态', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2e6533eb6d0511e6b37f000c293c07d6', 'user.freeze.investFreezed', '投资功能已被锁定，请联系客服人员。', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2e80e543ccb84b88a04d3aad0fd59785', 'realize.feeSingleMax.emptyMsg', '请填写单笔手续费上限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2eb9953ff9a044a8b69a14cd8e4cae67', 'borrow.projectName.lengthError', '借款名称长度范围须在{0}到{1}之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2fb13ba568f811e68586000c293c07d6', 'bond.message.project.notExists', '原始借款项目不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2fd7897ec5474a55b6e2fbc61c3a05d6', 'bond.message.bondInvestRemainMoenyLimitError', '投资完毕之后剩余债权金额不能小于债权起投金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('2ff0bfc1c41e43259e74f6d2a65e397b', 'article.remark.valid', '文章简介不能为空，并且不能超过{0}个字符！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3068d489fd8a45ba8da949c86c82200c', 'user.orgCode.lenghtError', '织机构代码长度错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('312ba06f6e8711e6b37f000c293c07d6', 'bond.message.notRepayStart', '借款项目不是还款中状态,不能发布转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('312ee1fb71924336ac219a6c916d5e75', 'product.account.rangeError', '产品金额范围须在{0}到{1}之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3155058abde5428e9a634db71cf074f5', 'autoInvestRuleLog.investMin.rangeError', '账户单日最高投资金额不能小于最低自动投资限制额度{0}元', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('31d3526b28364181af9376c587ed6621', 'realize.project.not.realize', '项目不是变现项目', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('32d8e21021f0430da51c91e8efd25f7c', 'risk.messgae.common.status.delete.error', '已启用试题不允许删除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('351f6c7d253440b6adc9fadffa6920bb', 'product.timeLimit.oneTimeRepaymentOnly', '借款期限为1的还款方式只能为一次性还款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('35cdfa097ff011e699af000c296f8d8c', 'risk.message.answer.is.null', '答案不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3625855064f749c89119397204fbeaa3', 'realize.feeRate.emptyMsg', '请填写转让金额手续费', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('363ead6f0a6d4c0d98d263030d6f6271', 'borrow.raiseTimeLimit.rangeError', '募集期须在{0}到{1}之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('365f7a62296042a38469a64b1dd2bedd', 'buy.amountmin.is.empty', '部分投资最小投资金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('368b7895d0744a15a07db6671d3674e9', 'autoInvestRuleLog.amountDayMax.notEmpty', '单日最高投资金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3767f306db6b42b6bb3ac724cfd517fa', 'project.account.notEmpty', '产品（借款）金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('37750bccc96843398435cce0fc47949b', 'project.saleVipLevel.msg', '仅VIP{0}以上会员可投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('393f24c9ca594d8d85727f32111fa515', 'bond.message.bondInvestLowestLimitError', '投资金额小于债权起投金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('394834406daf11e6b37f000c293c07d6', 'core.repayment.useMoneyNotEnough', '还款失败，可用金额不足！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('39a6a30d7c184700851fea66f3a0914a', 'realize.is.cancelling', '变现项目撤销处理中', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3b13ede472b3439d9cd4a73850856978', 'borrowBespeak.remark.lengthError', '备注信息过长', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3b9026056d0711e6b37f000c293c07d6', 'bond.message.rule.feeInitiateAmount.notExists', '收费起步金额 不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3cbdb960e7e84d6a8a683439758a2747', 'buy_amountmax_is_empty', '部分变现最大投资金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3cc9efc338fc41d494c1efe06986fa73', 'global.realName.statusError', '请先通过实名认证', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3cd676319aee4017befa93c15e87446d', 'project.apr.notEmpty', '年利率不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3d385a18d21f4036a0b701688f2f3da3', 'expiretime.must.gt.starttime', '有效期必须大于发放开始时间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3d53a51c1f024eed946a374b014bb0e7', 'activity.plan.ban.error', '存在生效的红包/加息券规则，请勿禁用！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3de8d6cc31774a2596151249ba54478f', 'autoInvestRuleLog.open.authSign', '去授权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3fd82b6dda204a24ba9c678bd29925de', 'project.stepAccount.gtLowestAccount', '递增金额需小于等于最低起投金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('3fe67c63171842cf97615ee4fca51807', 'user.realName.status', '为了您的资金安全，请开通第三方资金托管账户', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4068d82f2a594cd4b4dd62d143248d3a', 'product.stepAccount.mustlt.account', '最低起投金额和递增金额的总和不能超过产品金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('40c8fee0407049c48a479cef6716f084', 'account.useMoney.notEnoughError', '可用余额不足！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('40e9093f7ef211e699af000c296f8d8c', 'risk.messgae.papers.publish.after.cancle', '当前已有启用的问卷!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('41676ca1c5fa450fa5bf7e1215a68557', 'sys.config.code.lengthError', '标识长度须位于5到50之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4243c20cacff4696b74ef14276ad5c5e', 'core.invest.hasNotFreezeNo', '投资记录[{0}]缺少冻结流水号', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('42c4de2991da11e699af000c296f8d8c', 'bond.message.intput.not.money', '请输入小数点为2位的有效数字', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('435993bc7ff011e699af000c296f8d8c', 'risk.message.question.is.null', '试题不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43a386479216491bbab97daa62e9aa9c', 'core.repayment.hasUnpayRecords', '该项目本期前还有尚未还款的借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43af04b6ca724f30be1d46062cb2d540', 'user.mobile.isEmpty', '手机号码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43c354d3ac374ff893d978b76f273f78', 'bond.message.bondRule.idNotExists', '债权规则标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43c4ab23a70a4436a4082e20718857ce', 'core.redEnvelope.useByOtherError', '不可使用别人的红包', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43d8e93c643243e7827fced640decd33', 'product.content.outOfRange', '产品详情不能超过{0}个字符', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('43f17783e5144bb5b2212eb57f1d8906', 'buy.amountmin.must.gt.one', '部分投资最小投资金额必须大于等于1', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('44793fdf5cf741ff99ee4b62baa462d5', 'web.project.notExists', '指定项目不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('44a4fc871cbd4f04b6831cdb797b752b', 'tradeNo.not.null', '交易流水号不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('44ccb3237efb11e699af000c296f8d8c', 'risk.messgae.config.min.up.max', '最低分不能大于等于最高分', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('452d6368c62b458a8e28cfdb4ac57a3a', 'project.type.sort.cannot.repeat', '一级类别的排序不能重复', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('455c0de9adab4a1b9027672705001e81', 'borrow.raiseTimeLimit.notEmpty', '募集期不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('457a95ffd7da4aa0aa152e09064e1122', 'autoInvest.success', '自动投资成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('45b123065822491385a3cf98f5a19bb7', 'autoInvestRuleLog.aprMin.rangeError', '投资收益范围值错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('462855a092f244cea2254dbc2bdba013', 'risk.question.answerList.valid', '问题选项列必须大于等于{0}个！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('46ca62cec0804c5aa3f2d239dd510076', 'project.saleEdit.showTimeBeforeNow', '再上架操作,上架时间不得晚于当前时间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('472615085ed74cefa0906c5bca25cd5b', 'borrow.timeLimit.notEmpty', '借款期限不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('478a4da27a5f11e699af000c296f8d8c', 'risk.message.papers.id.is.null', '试卷标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('47e03ff0975b11e6b953000c296f8d8c', 'risk.message.config.in.project.is.yes', '已有对应风险等级的产品，不允许删除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4822dcc5597a411f8125e89f309614ea', 'user.pwd.hava.space', '密码不能有空格', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('493d4d50ccfc4ceda941b757af47c083', 'rule.over.validity.time', '规则不在有效期内', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4957818285484844b963a068f295b061', 'web.invest.notFound', '投资记录未找到', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('49e9f915a73047f2bf423e7116602102', 'account.and.log.not.null', '资金账户或资金日志参数不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4a1f532ff62045fd8d5caf5f832d9c0a', 'common.message.validCode.notEmpty', '验证码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4a8013ad96b44ae4af51f59335d84710', 'user_gift_award_not_null', '请至少选择一项礼包福利！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4ad1cbe3170d4e79964394f3bf934733', 'borrow.borrowUse.error', '借款用途错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4ccba1249ed44033bdf617e346654634', 'project.saleTime.notEmpty', '开售时间不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4cf2dee304b54645be256f4abbc42c3f', 'realize.deadline.emptyMsg', '请填写变现时效', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4d452235ecab4329a8d55f294eb1a13f', 'bond.message.perioddays.zero', '勾选到期日限制距离本期还款日必须大于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4d73f4b369d211e68586000c293c07d6', 'bond.message.huifulimit', 'ufx对接汇付接口限制,只能一次性全部转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4e113a00f980462abea3343276872954', 'user.authSign.status.error', '您需完成业务授权，才能申请借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4eb4187a54524dbc92d3f65fba84b7e8', 'type.name.over.length', '类型名称长度不能超过40', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4f0173e12eca478c9d76eb22d9e5742a', 'canot.invest.self.realize', '自己不能投自己发布的借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4f6bf8fc03c64817a85a0f0f5e30c1e0', 'bond.message.bondDay.low.zero', '剩余期限天数小于等于0天,禁止发布转让!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('4ff1dddb95b011e6bdf8000c296f8d8c', 'projectType.not.exists', '类别不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('513534c7d1f041d4a56aadea47cb8bd1', 'user.userName.error', '用户名由6-20位字符组成，由英文字母、英文字母加数字组成', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('513534c7d1f041d4a56aadea47cb8bd2', 'user.not.exist', '用户不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('513534c7d1f041d4a56aadea47cb8bd3', 'company.register.merPriv.not.exist', '企业开户，私有域不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('51f28c027ef011e699af000c296f8d8c', 'risk.messgae.papers.score.error', '修改的分数参数异常', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('527a8f868dff4417a0a31cd5015140e8', 'rule.over.grant.time', '规则不在发放期内', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('52d47324944d11e699af000c296f8d8c', 'bond.message.disrate.validnum.limit', '折溢价率的小数部分不能大于等于2位', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('52d77129944d11e699af000c296f8d8c', 'bond.message.disrate.num.limit', '折溢价率的整数部分不能大于2位', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5303cca9b7d9420690436202e15935e9', 'project.stepAccount.mustInt', '递增金额必须为整数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('53558a9c3e564d1fb81d2508fac9b950', 'bond.message.disrate.max.limit', '折溢价率的最大值不能大于等于100', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('537e44fd54794aa981b3c58c247eca7e', 'bond.message.bondMoneyLessLowsetMoneyError', '转让金额小于最低转让金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('54759724d2fc47c6921571f48b193b74', 'borrow.timeLimitType.onlyDayOrMonth', '借款期限类型只能是天或者月', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('55ab2cee9b2f44d1b3a574015d4be6f2', 'invest.timeoutHandle.statusError', '投资订单超时处理，订单状态错误，还款ID={}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5661f3e90889424e81bd0b59376c5949', 'realize.orgproject.not.repayed', '原产品未还款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('566e106b9cfc4241a9c1c1d67f912064', 'sys.org.orgName.lengthError', '机构名称长度须在2到50之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5693bd4007524a5db95d4c7a5b9783b6', 'product.copyAccount.gtAccount', '每份金额不能大于产品总额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('569ff8060a094bc8badc9e0cbdb24454', 'core.repayment.hasBond', '存在转让中的借款，不能进行还款操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('570019bb355f47f4a22c84d06af6a1cb', 'customer.mobile.noExists', '该手机号码尚未注册，请提醒用户注册', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('57078393672a4310bb38681f03646e52', 'project.lowestAccount.ltOneError', '最低起投金额不能小于{0}元', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5713f35a5631414fa7740d20fbb429d5', 'autoInvestRuleLog.amountDayMax.formatError', '单日最高投资金额只能为正整数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5795b12d6daf11e6b37f000c293c07d6', 'core.repayment.tpp.UseMoneyNotEnough', '还款失败，第三方可用金额不足！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('58db2b7dd53749f7998a7765422cbce2', 'sys.message.email.addressError', '邮箱地址错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('591e9e4a839111e699af000c296f8d8c', 'user.mobile.is.used', '手机已经被使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5a1032c1fe9144d4a581bc99c4aa9ecf', 'resource.resName.lengthError', '资源名称的长度须在5到100之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5b865e403e9146269e37cb5810482530', 'project.apr.rangeError', '年化利率范围须在{0}%到{1}%之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5bceb44f76e54ff2abc15a04b53fae92', 'rule.num.over.limit', '发放数量最大值为1000000', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5c7ce7c516954e0a8c6f4ade2811a453', 'realize.apr.remainTwoDecimal', '变现利率最多保留两位小数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5d2f3de466ff435da58e78cc4fc1e8d5', 'realize.orgproject.not.fount', '变现原项目未找到', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5dc5d678813711e699af000c296f8d8c', 'customer.message.mobile.style.error', '手机号格式或者长度异常', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5ef8a5057e3848d7a34da6840c6991c5', 'user.mobile.notEmpty', '手机号码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5f2310fd59294b8b9f3e6a6b2ac81415', 'bond.message.bondInvestAccountNotEnough', '债权剩余可投金额不足', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('5f8f026962a24aa68658bf07c1435df6', 'global.msg.fail', '操作失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('604003021b3144f6a1cebcf1c7db9130', 'sys.user.realName.lengthError', '姓名长度必须位于2到10之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('608bc86c364e4025895951d12e9307c4', 'autoInvestRuleLog.aprMin.error', '投资收益最多保留两位小数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('615becf8bd4940d6a7e77ca01b58e501', 'autoInvestRuleLog.repayStyles.notEmpty', '请至少选择一种收益方式', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('617602d6574341d9b7431372e4ccfa10', 'realize.realizeRateMin.mustlt.realizeRateMax', '变现利率下限值不能高于变现利率上限值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('61a2286ae1ad46079adcaeacc95dc1bb', 'risk.message.question.on.not.delete', '已启用试题不允许删除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('61a4a9fd50c44134acb0ed1b8bcdea0e', 'autoInvestRuleLog.go.risk', '立即评估', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('61ac844fe407479cb037a8762570f019', 'product.timeTypeDay.oneTimeRepaymentOnly', '期限类型为天的借款的还款方式只能为一次性还款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('624a4884fe7f46628c152e7bbc9be253', 'project.password.notEmpty', '定向密码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6270132b2387421d9eb8aa7c1d05d950', 'user.telephone.lenghtError', '企业固定电话长度错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('62dcfb3da9f1403f81b4719e1af10f62', 'award.only.once', '该奖励只可获取一次，谢谢您的参与！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('63039de7d39b41f4bf85c91c7fb84466', 'core.redEnvelope.useError', '红包使用错误：过期、已使用或限制项目类别', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('63900fd1a07a41e59e1df1cc264a9a4f', 'bond.message.bondInvest.doubleLowestLimitError', '剩余债权小于2倍的最小投资金额，则需要全部受让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('63b5e27f839748e2abffb77ed11476b7', 'common.message.validCodeError', '请输入正确验证码', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('64fd34b26d0511e6b37f000c293c07d6', 'user.mobile.notAuth', '请先通过手机认证', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('662b7db3e2eb4b05a9d5a6b5350ed545', 'borrowBespeak.notChange', '请选择预约借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('673827bf02dd4b1e83ec754efd2921e0', 'account.and.log.not.match', '资金账户主体和资金日志主体不匹配！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('68199eb58799455ebcf0adda496f4851', 'project.password.formatError', '定向密码格式错误（6位数字）', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('682c1b0598a24009be3bd0d4510fb71d', 'realize.invest.not.found', '变现原投资未找到', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('682ccd74e0624bf4ae112ef459f46a9a', 'bond.message.projectInvest.idNotExists', '可转让投资记录标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('683f997c17594952af2dc1d7d263fa65', 'core.invest.amountGreaterMost', '投资金额大于最高投资金额!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('68f9271c780e11e699af000c296f8d8c', 'bond.message.bondStatusIsNotPublishError', '债权表状态不是发布中状态', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6a2b42772a004131b5295f249b279641', 'project.repayStyle.notEmpty', '还款方式不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6ab8c4f3fe6b4257b099cf9b3e797b4a', 'global.email.statusError', '邮箱已绑定,请勿重复操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6b0dd43aa7694816b12324e97a658134', 'resource.resText.lengthError', '资源值的长度须在2到100之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6b80b0357eef11e699af000c296f8d8c', 'risk.messgae.papers.config.add.fail', '添加失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9781', 'user.idNo.error', '请输入15或18位的身份证号，仅限数字和大写X', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9782', 'company.name.error', '请输入2-30位的公司名称，由中文、（）组成', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9783', 'bussiness.code.error', '请输入正确的营业执照注册号', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9784', 'org.code.error', '请输入9位组织机构代码', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9785', 'company.tpp.reg.auditing', '您的企业开户申请正在审核中，请不要重复提交开户请求', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b9786', 'company.tpp.reg.success', '您的企业开户申请已通过', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c727dfc8a534c3a809c2a3c500b978f', 'user.realName.error', '请输入2-10个字符的真实姓名，仅为中文', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c776d701f874357a5c1beaba7466e98', 'company.zone.empty', '注册地区不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c85d2fc975911e6b953000c296f8d8c', 'risk.message.config.riskVal.is.exists', '已有对应级别的风险等级', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6c8b6b19021243179aa4450980470899', 'realize.apr.rangeError', '变现利率范围错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6d06a3f1f19547d6b73eafc66ea3dfe4', 'bond.message.bondIdNotExists', '债权的借款标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6d1af3dd9e964b338de27224cb338e1a', 'bond.message.collection.exists', '债权的借款待收不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6dadfa103a5d4a61b7086b33dfbff36e', 'bond.message.bondRuleNotFondError', '债权规则尚未配置', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6e94787cd01e4543a358f930b397be73', 'realize.less.must.fullrealize', '变现金额小于最小变现金额{0}元时,必须全额变现', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6eaaf738e8424584aede435f5e94cbb0', 'porject.type.is.associated', '{0}类别已关联生效的红包规则,请重新选择！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6f8d3aaaec914f5a83d00fb16ec1776a', 'realize.realizeRateMin.emptyMsg', '请填写变现利率下限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6fd234db0e9e479194eafbf58435bd2d', 'realize.borrower.not.right', '变现借款人不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('6fe08e112f8c443cb3f1cb1ca8f6155e', 'project.timeType.error', '期限类型不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('701b2ab646294331a3245ae8fe6984ff', 'product.content.notEmpty', '产品详情不可为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7146510bd69c48f69f767f456c481b8f', 'project.repayment.create.fail', '项目还款计划生成失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('719579386d0711e6b37f000c293c07d6', 'bond.message.rule.feeBuyMax.notExists', '单笔手续费上限值不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('72d845f27a8c4403b3a3de71d63daf4e', 'manage.projectType.features.lengthError', '类别特点长度不大于15', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('72e28bae21a24f3080fea3ed7dbf9076', 'project.stepAccount.mustlt.mostAccount', '最低起投金额和递增金额的总和不能超过最高投资总额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('731a7b6531944e5fae954d549bcbc9a8', 'realize.not.above.mostmoney', '变现金额不得大于最大可变现额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('74606f82553a438087cd4ca1baba3074', 'login.fail.with.two.chance', '用户名或密码错误，您还可输入2次', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('74a63833780f11e699af000c296f8d8c', 'bond.message.projectInvestIdIsNull', '原始项目投资记录标识不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7517b88d4f5e46009712085a64fd39ee', 'project.showTime.notEmpty', '上架时间不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('755b432c7f1e4682bfd28ebba1246cc8', 'common.form.repeatSubmitError', '表单已经处理，请勿重复提交！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7707b66ce30e40d8b89b2270aae24548', 'core.invest.notEqRemainAmount', '剩余可投较少，需要全部购买', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('77c9454337ab4e0fab85866c29d3d257', 'borrowBespeak.zone.error', '所在城市不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('78069230b9794a58a6f3629ad19b9605', 'qualification.picPaths.empty', '请上传资质材料', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('78b6ce33102a42b39268be1042431198', 'project.status.neq.raising', '项目状态已不在募集中', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('78f0fd0e070e4c9fa0f3b6b0999cb99b', 'order.handle.system.exception', '系统异常，业务处理失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('794b99dd68f111e68586000c293c07d6', 'bond.message.projectInvest.isNotExists', '可转让投资记录对象不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('795068fa7b73413db45f6bdc94a6a07a', 'manage.projectType.typeLevel.bigError', '类别层级大于等于3', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7a7f45946d0611e6b37f000c293c07d6', 'bond.message.rule.bondAprMinOverMax', '折溢价率下限不能超过折溢价率上限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7aadc9d3281e4a26bbbc85e90b5fdf32', 'remaindays.must.gt.zero', '距离到期日必须大于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7b60e6167fe411e699af000c296f8d8c', 'risk.message.papers.is.null', '试卷不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7bd980532e4e4309bb4a385e982f7052', 'bond.message.maxApr.low.rule', '您的债权溢价率超出了限制范围，不允许转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7c12b732235341cd8b3883daebe6f810', 'securityanswer.save.error', '密保设置失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7c43b2c8ad97490797b1f7456cff62cd', 'project.lowestAccount.notEmpty', '最低起投金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7e04a15739f74466bf673c140163229e', 'sys.org.parentId.emptyMsg', '请选择上级节点', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7f0040141b584ffc8ea8137261a455c1', 'user.companyName.lenghtError', '企业名称长度要大于{0}小于等于{1}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('7f524450e883417ebf185edc4163be38', 'project.redEnvelopeRate.rangeError', '红包可用最大比例不能超过100', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('802e91c3a0ad49498c7013e28dbac966', 'project.saleChannel.notEmpty', '上架渠道不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('80a3f82963bb4602bc0df5413c1f1847', 'raiseendtime.must.gt.issuetime', '募集结束时间必须大于发布时间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8100a62198de4bdea79fc15585d2b622', 'operator.oldPwd.error', ' 原登录密码不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('81298bd09415429b903b277fcabbad70', 'bond.message.holdDayNotEnoughError', '持有待收天数不足，不能发布债权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('817e290f954d40fd817c2c89b78fc55d', 'project.borrower.loan.freeze', '您的账户借款功能已被冻结，不能申请借款，请联系平台客服', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('821887f4aeec4df188c476f938f11718', 'bond.message.projectIdIsNull', '债权对应原始项目标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('822eb4719a73425792ab540beeaa1b8d', 'project.verify.remark.not.empty', '审核备注不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8242518e412843f690d50240f2d36bcf', 'user.realName.go.open', '开通托管账户', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('82b0797f691e11e68586000c293c07d6', 'bond.message.bondInterest.amountIsNull', '收益计算时输入金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('82b1641fd47a4c699048b57291c55254', 'project.lowestAccount.gtAccountError', '最低起投金额不能超过总金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('82dce18a26ea491dba5ed770a5c36b8f', 'invest.unpay.tooMuch', '您今日未支付订单（支付超时、待支付）超过{0}次上限，若有未支付订单请先完成支付', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('840ebe1596b2481480b463d2efe1e2a8', 'calculator.dayRepay.notSupport', '该还款方式不支持按天还款!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('840f13107a5843d9915590f0476b6f1f', 'growthlimitvalue.must.gt.growthvalue', '成长封顶值需大于所需成长值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8430e666d5c04313bf6bff2dea1ad05d', 'borrow.borrowData.notEmpty', '借款资料不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('84cba2a37a5f11e699af000c296f8d8c', 'risk.message.papers.id.is.empty', '试卷标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('868e226d854d11e699af000c296f8d8c', 'bond.message.bondrule.disrate.is.must.num', '折溢价率必须是数字', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('86ddd9d0783c4ed2a9d50c01428293ba', 'borrowBespeak.sex.error', '性别类型错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('871edb8af39c485fbe9790f46b1942f1', 'borrow.content.outOfRange', '借款详情不能超过{0}个字符', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('87b5b957094b4c8f82d2b72338e5947d', 'global.code.exists', '编码 [{0}] 已存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('88f39e3cccf442eca4a936c98bc5bbb9', 'tpp.userCustId.is.repeat', '第三方商户号重复', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('88f39e3cccf442eca4a936c98bc5ccc9', 'create.account.sign.fail', '创建账户签章失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('88f39e3cccf442eca4a936c98bc5dde9', 'user.tppUserCust.lenghtError', '第三方商户号过长', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8a1373ea7ee611e699af000c296f8d8c', 'risk.messgae.papers.not.publish', '试卷未发布', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8a5a20fb3d84455aaca74788e2422f2d', 'project.apr.remainTwoDecimal', '年利率最多保留两位小数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8b0257a64d584202a2963231da587e34', 'has.overdue.project', '存在逾期借贷，请还款后再进行变现操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8c134a17bcca414fad5ef00a65f256cb', 'core.invest.investSelfBorrow', '自己不能投自己发布的借款!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8c1b9813cd9e4d50a7223f506c2b4dd4', 'core.invest.SpecificSale.mail', '定向邮箱错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8c1fe447d1f6460f9a7fc5d837dcaeef', 'product.company.creditCode.businessCode.notEmpty', '企业缺少统一社会信用代码或者营业执照编号', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8c2250e2501448c0894f1f5295cbd80c', 'realize.is.freeze', '变现功能已被锁定，请联系客服人员。', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8d45be9e45734c238588ba99393428c3', 'sell.amountmin.is.empty', '部分变现最低变现金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8d85f976b85649b1b22e950ad9573894', 'project.projectTypeId.notEmpty', '所属类别不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('8edac712790d4b8e97007ff8730014b2', 'user.accountcode.is.not.null', '账户编号不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9034d1cf6d0511e6b37f000c293c07d6', 'user.tppStatus.notOpen', '为了您的资金安全，请您开通第三方资金托管账户', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('903fde35780f11e699af000c296f8d8c', 'bond.message.projectInvestIsNull', '原始项目的投资记录不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('913f153f5a904601bfed97272397c10b', 'autoInvestRuleLog.need.authSign', '您需完成业务授权，才能开启自动投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9168e981632e4d87be7377adc5fce427', 'mobile.is.not.right', '手机号码不正确,请返回重新输入', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('917ccf588bc54af7a5d1b17aaea3f28e', 'ratecoupon.cancel.status.error', '只有未使用的加息券才能作废', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('917d4325c35746829c2e1c91fb512986', 'risk.message.papers.on.not.delete', '已启用问卷不允许删除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('918ba9847ec445a0a4506c294f8f907e', 'user.login.password.error', '用户或密码错误, 请重试', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('927fa57f57174c4a87d36605595f0509', 'gift.name.error', '礼包名称的长度在2-15个字符之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('92b2a0f95ce34b418bfee8bd7ceff854', 'realize.is.loaning', '项目正在放款中', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('92c4bd290217401ab386141c25ada2bb', 'sys.message.email.sendSuccess', '邮件发送成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('94053dcc0f3c43799cedbdb7b76639ed', 'project.verify.notVouchUserError', '当前用户不是该项目担保人', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9413218168ff11e68586000c293c07d6', 'bond.message.bondRuleNotExists', '债权规则不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('94355538f18f42f6bf1270a9a2e61b5e', '12345', '啊啊', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('94a5d159af0246a0be6c2c91d4c8e50e', 'user.authSign.open', '马上授权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('94ecec4d6d0711e6b37f000c293c07d6', 'bond.message.rule.feeFixd.notExists', '固定金额手续费金额不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('951a055c1c7048b093e8969dd61dc202', 'test', '测试{0}，时间：{1}，地点：{2}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('952484b9f8174224b856acde9dfe9e90', 'project.saleMail.notEmpty', '定向销售邮箱域名不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('95d4c96c7ee511e699af000c296f8d8c', 'risk.messgae.common.selected.delete.error', '请勾选需要删除的记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('966c01f7e306454395cdb72a5d4996c4', 'bond.message.periodRemainDayNotEnoughError', '距离本期还款日到期天数不足，不能发布债权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('969dc43c2972439b90c73daefb629778', 'autoInvestRuleLog.month.rangeError', '月范围值错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('97f92852de484c4e9bb53fe47c5ff425', 'autoInvest.investMoney.overLimit', '您已达到单日最高自动投资金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('97fae365780e11e699af000c296f8d8c', 'bond.message.bondInvestUserIsMineError', '债权投资不能是发布债权的人', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('980586f7da314d999b6e3b32c0b921d0', 'autoInvestRuleLog.day.rangeError', '天范围值错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('98e0623ca4b54b1e854fc9a933959979', 'user.update.faild', '更新数据失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('99637f5a839111e699af000c296f8d8c', 'user.info.is.right', '请确认注册信息是否正确！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9a4354bc2eb045b49e99caafd641df01', 'bond.message.bondAlreaduReleasedError', '此笔债权已经发布,请勿重新发布', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9a92605d267f4242bf5e657f95f8a754', 'bond.message.bondInvestRemainMoenyTwoMulLimitError', '剩余债权金额小于等于最低受让金额的2倍则需一次性全部受让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9b15b6af1f894d938f87701896b64b0d', 'cash.success', '提现成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9b1de543bd284c15a4aa76d0c0019c25', 'borrow.web.success', '您的借款申请已提交，请耐心等待审核！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9b7b9e77de9c417b95e420d447946f29', 'user.realName.open', '马上开通', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9c6e32ec78b711e699af000c296f8d8c', 'bond.message.bondAprUpMax', '折溢价率不能大于最大折溢价率', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9d75d6e4d2d34fd5bb0b536e442370a4', 'bond.is.freeze', '债权转让功能已被锁定，请联系客服人员。', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9d7893626d0611e6b37f000c293c07d6', 'bond.message.rule.bondAprMinMaxNotExists', '折溢价率上限、下限均不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9dac537ad8ba4051bc1129b67b39b602', 'borrow.stepAccount.mustlt.account', '最低起投金额和递增金额的总和不能超过借款金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9e0206a327d34db79eb295b0a6ea1827', 'bond.message.remainDayNotEnoughError', '距离到期还款日天数不足，不能发布债权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9e98719d112f4d9996cff565cf0d47cf', 'user.pwd.hava.chinese', '密码不能含有中文', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9f85ad04009946d28694f5e1a553f07e', 'site.nid.emptyMsg', '请填写栏目标识', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('9f874fd7435c4492ad83a6fffb2375c9', 'user.add.faild', '账户新增失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a03f71d9751943609b2c2cf6f6f13de1', 'operator.pwd.again.error', '两次密码输入不一致', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a1099dd6693911e68586000c293c0711', 'period.not.empty', '投资期限为必填项!', null);
INSERT INTO `sys_resource` VALUES ('a1099dd6693911e68586000c293c07d4', 'principal.not.empty', '投资金额为必填项！', null);
INSERT INTO `sys_resource` VALUES ('a1099dd6693911e68586000c293c07d6', 'bond.message.bondInvestOrderNoNotEmpty', '债权投资订单号不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a157ff96488e4e1ea3e99d88a4c3a354', 'realize.is.not.repaying', '变现项目不在还款中状态', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a1697c52cbea48e29973f0a1633353fc', 'invest.timeout.timeError', '投资订单超时时间设置错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a1edf14ab1a94689adfc7dd3be0c121e', 'common.go.back', '返回', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a2589474fb9f4b6eabd6691e47a3b33f', 'sys.message.email.sendFail', '邮件发送失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a299456d4ab341f0bfbc941ff9000423', 'project.borrower.noAuth', '该用户未实名认证', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a3a4c49fce6640208caba8cecf60c3d8', 'borrowBespeak.success', '您的借款预约申请已经提交成功，我们的工作人员会尽快审核您的需求，并尽快与您取得联系', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a51e14a3ae45453b9ade6374178fa5af', 'borrowBespeak.sex.notEmpty', '性别不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a51e9aab627b44dd9b33f6ce3c8a73e9', 'manage.projectType.deleteParentNodeError', '类别【{0}】下存在子类别不允许删除!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a6e3d240d82649599dde5a6b7a8aacf9', 'product:must:set:locktime', '请设置锁定期', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a739054c95ef4e83a926d6c8fe51a03e', 'borrowBespeak.status.error', '预约跟进已处理', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a774316214234bb5aba25ba82e0ab18c', 'global.record.noExists', '选择记录不存在或无效！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a7dd8790625140028e748ed1434cb9df', 'realize.repayer.not.right', '变现还款人不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a80940f60d6d414fb341a7641284e061', '676576', 'ytyty', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a87f8f7e6d0511e6b37f000c293c07d6', 'bond.message.rule.lowestSellUpperMity', '若选择部分转让,必须填写最低转让金额大于等于单笔受让最低金额的倍率!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a8d4f8adf8c848a792f48b978bf42439', 'realize.must.fullrealize', '必须全额变现', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('a92902f7e0f7484787cf3b8dd82f6f40', 'rule.is.baned', '规则被禁用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('aa20c87e5a5e4608a49edfd33eab1ecc', 'realize.is.repayed', '变现项目已经还款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('aa532d3378b711e699af000c296f8d8c', 'bond.message.bondAprLoMin', '折溢价率不能小于最小折溢价率', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('aadf9bc11ec14cb5b30589495e664f98', 'sort.value.not.null', '排序值不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ac55f011acb7466784d48e1e9b3c1c00', 'risk.messgae.config.next.min.up.max', '最低分需高于上一级最高分', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ac585240da1a4278bbe34dbf9eba6c8b', 'borrowBespeak.money.notEmpty', '请填写借款金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('acd5211570514cb799c4c49c938c0d55', 'core.repayment.noexists', '该期借款不存在！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('acea75ba91dc427cb5e7e4ad5ded7a26', 'product.copyAccount.leZero', '每份金额不能小于等于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ad0bff6493a54916941abcd6b3768249', 'remaining.rule.num.insufficient', '剩余发放数量不足', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ad8a17b0cc844929a3d7a422ff5f3908', 'user.pwd.isEmpty', '默认密码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30001', 'user.not.create.by.manage', '该用户非后台添加，不可修改', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30002', 'bank.cardId.can.not.be.empty', '银行卡号不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30003', 'bank.code.can.not.be.empty', '银行标识不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30004', 'global.msg.illegal', '非法操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30005', 'protocol.should.be.agree', '请阅读并同意服务协议！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30006', 'user.name.can.not.be.empty', '用户名不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc30007', 'user.password.can.not.be.empty', '密码不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc51', 'cash.audit.fail', '提现审核失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc52', 'cash.is.freeze', '您的提现功能被锁定，请联系客服解锁！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc53', 'cash.need.bank.card', '请先选择提现银行卡', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc54', 'cash.money.positive', '提现金额不能为空，且必须大于零', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc55', 'cash.money.error', '提现金额格式错误，最多输入两位小数！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc56', 'cash.single.min.money', '单笔提现金额不能小于{0}元！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc57', 'cash.max.use.money', '提现金额不能大于您的可用余额！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc58', 'cash.day.time.limit', '提现失败，每天最多只能提现{0}次！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc59', 'cash.day.max.money', '提现失败，您今天提现已达限额{0}元！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc5c', 'cash.fail', '提现失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc60', 'cash.day.remain.money', '提现失败，您今天还可提现{0}元！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc61', 'cash.is.frequency', '您的提现操作过于频繁，请稍后再试', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc62', 'cash.update.fail', '更新提现记录失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc63', 'cash.audit.is.doing', '该提现记录正在审核处理中，请不要重复操作！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc64', 'account.tpp.fail', '资金托管方处理失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc65', 'cash.audit.tpp.error', '取现复核发生错误异常，审核不通过但第三方处理失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fc3dc66', 'cash.account.error', '账户可用余额不足', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ae888b2ea3f0428ca53df5c50fcaacaa', 'cash.has.audited', '该提现记录已经被审核处理过，请刷新页面重试', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('aee59fe68bd64b7192445a97b2223d9a', 'grant.total.num.error', '发放数量最大值为1000000', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b02bd17c548c11e68586000c293c07d6', 'core.project.hasNotSuccessInvest', '项目[{0}]没有成功的投资记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b03c1dd55deb420bac2fb62b7623dc50', 'core.project.isNotRaising', '产品募集时间结束!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b092d43abbd84fbdaff06dd682c0a590', 'article.code.isNotBlank', '栏目标识不可为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b2502c052a834368a14f555e08ae3ce5', 'core.rateCoupon.noExistsError', '加息券不存在！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b30ebbd6fa754235b35af5ea1f6e8d3d', 'user.nature.error', '担保用户不能申请借款', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b36e6fceaeef40b9aa57c841e10e5e2d', 'rule_ban_error_for_gift', '用户礼包：{0}正在使用，规则禁用失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b4477b62efd14b61841c41d6027c6495', 'risk.question.sort.valid', '问题序号必须大于等于零，并且小于{0}！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b4c48f4caec440a2924588203b4a9bdd', 'autoInvestRuleLog.success', '自动投资配置成功，将在5分钟内生效', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b51ce2a436134997944ce7745a2969c5', 'projectInvestBespeak.time.error', '已经开售，预约投资失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b57126b77b104707b65d5a1342a90aa3', 'remark.over.length', '描述信息长度不能超过512', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b6298a86ff8a4293a4098d2f491c24de', '融范德萨发的', '12', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b6b038997eef11e699af000c296f8d8c', 'risk.messgae.config.max.min.score.empty', '请录入最低分值和最高分值,不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b6ec11c88a1d4f61bd0795d6fa12f971', 'borrowBespeak.province.emptyMsg', '请填写您的常住城市', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b7aecbf0f1e04b958bd9e3eb73c5d6c8', 'user.companyName.irregular', '公司名称不合规则： 长度为2—30个字符 ，由中文、（）组成', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b7c2de92a677415eb3d9c4a7b37179d3', 'autoInvestRuleLog.dayLimit.rangeError', '天范围最大值不能小于最小值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b7e60197ea22450586e5a960a2137cf3', 'bond.message.bondCountOverError', '转让次数已满，不可再继续转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('b88ca39c6d0711e6b37f000c293c07d6', 'bond.message.rule.feeRate.notExists', '比例手续费比例值不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('baa9b50c93514212bba2e28c3e8399e8', 'core.project.amountFullError', '产品已筹满 ！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bbf8399a71194d3c8479ac13054a7491', 'project.timeLimit.outOfMonths', '期限不能超过{0}个月', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bc03ae031d244319b45f210533e92309', 'common.message.validCodeIsNull', '验证码为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bc37a4e16d684812b6e46f916bc00ec1', 'order.not.exist', '订单不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bd2851825bd84a0293e7267792ef6f88', 'autoInvestRuleLog.aprMin.notEmpty', '请填写投资收益最低值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bd5accf24c5b48b1a7b52c69d04ba632', 'invest.realAmount.eqZeroError', '投资实际支付不可为零', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bdbca2aaeed044a2ad607049a422bebd', 'project.mostAccount.gtAccountError', '最高投资总额不能超过总金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bdd72fe6f1b7414aa483aaa51070a09d', 'growthvalue.must.gt.uplevel', '所需成长值需大于上级所需成长值:{0}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('be17dceb78724df98cdbf5d17d9ce3d9', 'article.url.prefix.error', '文章的url链接必须以http://或者https://开头', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf101212f3de4b92a0e910c223860ab7', '77ytyt', '77gf', 'en_US');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a01', 'user.mobile.need.new', '新手机号码不能与原有手机号码相同', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a02', 'common.email.is.used', '邮箱已经被使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a03', 'common.email.can.not.be.empty', '邮箱不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a04', 'common.email.over.max.length', '邮箱超出最大长度限制', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a05', 'idcard.is.empty', '身份证号不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a06', 'idcard.is.error', '身份证号错误，请重新输入', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a07', 'answer.can.not.be.empty', '答案不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a08', 'answer.is.error', '答案错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a09', 'pwd.question.is.not.exist', '用户未设置过该密保问题', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a10', 'identify.method.need.selected', '请先选择认证方式', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a11', 'old.pwd.can.not.be.empty', '原密码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a12', 'new.pwd.can.not.be.empty', '新密码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a13', 'confirm.pwd.can.not.be.empty', '确认密码不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf6382c2839111e68fb9000c293c0a14', 'new.pwd.format.error', '新密码为8-16位字符，至少包含1位字母和数字', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bf68e7a26c354e99a63edd0f07846298', 'project.saleVipLevel.formatError', '定向销售VIP格式错误（数字）', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('bfa22514aab24f1b9fc663e26ef7eed0', 'project.verify.notVouchError', '审核项目不是担保项目', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c058416e020a4507baf387079f22cb47', 'user.telephone.formatError', '企业固定电话格式不正确，正确的格式如：0571-85132616', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c08fda140d5e4219a9ad3a7c0cf72c5c', 'user.vip.count.gt.zero', '当前有VIP等级大于0的用户 无法修改或删除VIP等级', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c0c54408134d4843871a84727adcd883', 'user.bussinessCode.lenghtError', '营业执照注册号格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c0fd7da35f4d4658b2d526971b951810', 'user.contacts.lenghtError', '联系人姓名长度要大于{0}小于等于{1}', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c14b84926dc711e6b37f000c293c07d6', 'core.project.saleVipError', '项目定向销售VIP等级错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c1bccecb96d94f82b835318bc7f0eb96', 'sys.config.configName.remark', '备注长度不能超过512', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c1c86a61150c4105928364d34e2d61bf', 'invest.freezeno.isempty', '投资记录冻结流水号为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c1f2c2bc272c40ff9b634f6a57a0554d', 'protocol.message.status_valid.remain.one', '有效的协议剩余一个,不能禁用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c2b2f05161da4ab196889d8d2d0037b2', '融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决', '融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决融都科技是一家专业提供互联网金融整体解决', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c2d55e40c51a411e819882821072ac54', 'bond.message.invest.status_init.exists', '该笔债权存在待支付的订单，不允许撤回！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c2e91e39da93426cb1ae3d81a68a229a', 'borrow.web.confirm', '您的借款申请已保存，请确认信息', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c36ea8d1d6904de49a9e08e4605ec788', 'invest.riskLevel.tooLow', '风险承受能力不足', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c40850450f2b43f7808c1981b3f92357', 'project.timeType.quarterError', '每季还息到期还本，期限类型必须为月', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c66c3bd27ee611e699af000c296f8d8c', 'risk.messgae.papers.answer.empty', '答案内容不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c73a025293a34bb888e862d7f7c97272', 'risk.question.name.valid', '问题内容不能为空，并且不能超过{0}个字符！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c74fc8aa62184f8d984e62696f3ff4b7', 'rule.name.error', '规则名称的长度在2-15个字符之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c7c0523f6923400798d02b47fb31bcb7', 'site.siteName.emptyMsg', '请填写栏目名称', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c7fb15b5184c40b1b9ab4c028a1ed5b4', 'account.and.log.not.equal', '资金账户个数和资金日志个数不匹配！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e710', 'recharge.is.freeze', '您的充值功能被锁定，请联系客服解锁！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e711', 'recharge.money.positive', '充值金额不能为空，且必须大于零', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e712', 'recharge.money.error', '充值金额格式错误，最多输入两位小数！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e713', 'recharge.money.min', '充值金额不能小于{0}元！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e714', 'recharge.money.max', '充值金额不能大于{0}元！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e715', 'recharge.update.fail', '更改充值记录失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e7e8', 'recharge.success', '充值成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c894da5eb6f5483b84ea58c2c3e0e7e9', 'recharge.fail', '充值失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c8b43be1770c48c18358841739f04a59', 'product.timeLimit.notEmpty', '产品期限不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c8d53fecf91d4382aa334a70543cfadf', 'user.mobile.repeat', '该手机已被注册，请更换手机', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c976f910addf443ebaa134dbb89668ef', 'securityanswer_is_repeat', '密保问题不能重复', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c99f8722f3a145359b95d6fe28e16052', 'user.not.login', '当前用户未登录!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('c9d8b7833131463295d5983a82475725', 'dict.type.over.length', '类型标识长度不能超过64', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ca0b5cee55814f0d8b57297a53a457e4', 'ACTIVITY_GRANT_BAN', '手动定向发放活动已被禁用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ca1bbff8d8c243ddb5579b030e3714c7', 'project.borrower.userNature.notClear', '借款人用户类型不明确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ca45627b84b511e699af000c296f8d8c', 'bond.message.invest.has.ok', '债权已经投资满额,不能撤回!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('caaab30f8fee4dd99f64ab481bbd6440', 'borrow.borrowUse.notEmpty', '借款用途不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('caf34779da3a403ba48786cb00a95807', 'autoInvestRuleLog.amountDayMax.rangeError', '单日最高投资金额不能超过账户可用余额{0}元', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('cb667f8fdbe8420293d02d6c7bce9d93', 'invest.account.rangeError', '投资金额范围须在{0}到{1}之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('cc4ac42e6dc711e6b37f000c293c07d6', 'core.project.saleEmailError', '项目定向销售邮箱错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('cc65d213ab844eb091d1d1711d516076', 'realize.overdueFeeRate.emptyMsg', '请填写逾期罚息', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ccdebe66780f11e699af000c296f8d8c', 'bond.message.investUserIsNull', '投资用户不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('cd4ded4e07514399bd0d43116c361094', 'core.project.salePasswordError', '定向密码错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('cdc2871e08a1451ca6bd8de7bcfc52ce', 'project.show.editError', '已上架项目记录不可修改!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ce54b897576d46ba93df0036d962521d', 'borrowBespeak.limitTime.error', '借款期限错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ce58d62137ed4e748e958d613bb32981', 'delete.role.must.has.no.operates', '该角色下还有操作员，请删除操作员后再删除角色', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d0d4d95905f74379a1c444c6eef9bc38', 'project.timeType.notEmpty', '期限类型不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d0f53753dc784f3191c8dda78652a805', 'project.interestStyle.tndays.notEmpty', 'T+N计息时起始计息天数不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d120c5fe6d0511e6b37f000c293c07d6', 'bond.message.rule.lowestBuyAccountNotExists', '若选择部分受让,必须填写单笔受让最低限制金额!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d37c758d693911e68586000c293c07d6', 'bond.message.bondInvestOrderHasHandle', '债权投资订单已经处理了', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d44462c19e9841909416e7df5606e815', 'autoInvest.useMoney.notEnough', '可用不足，自动投资失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d4646a35f5e241e7ba256b5c3de2cb0a', 'user.email.error', '联系邮箱格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d46d1e19ee4041d1b3ec48e73e34360f', 'manage.projectType.deleteUsingError', '类别【{0}】下已有产品不允许删除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d48cdbd955bc40288bafc63be44a8f5d', 'project.interestFinancing.noSupport', '不支持即息理财', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d5060978319946ebb3b0fbc1c6f16ad7', 'only.ban.rule.can.delete', '只有禁用的规则才可删除，请仔细检查', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d56ad1987ef111e699af000c296f8d8c', 'risk.messgae.papers.publish.id.null', '请勾选需要发布的记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d5bd3309f13a4882949742789e7b10a7', 'user.address.lenghtError', '企业地址长度错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d61746efd14d42788f504281901f2dc1', 'project.interestStartDays.mustLt.timeLimit', '起始计息天数不能超过产品期限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d61a3c14839111e699af000c296f8d8c', 'user.validcode.error', '验证码错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d765fdd70293436a99f0ca7383bba8f4', '232sqwe', 'we232', 'en_US');
INSERT INTO `sys_resource` VALUES ('d80245259eee44d3bbe23e08aa1c94bf', 'item.value.over.length', '字典值长度不能超过128', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d8abcb1dcd504043a33eabed33a50c79', 'core.redEnvelope.noExistsError', '红包不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d9195b5472a1491cb773861d3e1a0a64', 'role.delete.error', '系统默认角色:超级管理员、在线客服、经理人不可删除，请确认删除的角色中不包含以上类别', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d9198e3e7b224a58a291467dff73ebb4', 'sys.config.configValue.emptyMsg', '值不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d9adda4cfab3407bb1e957db4df577a7', 'securityanswer.length.error', '密保答案设置过长', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('d9faf7889133428dae91cdb8cd212330', 'borrowBespeak.zone.notEmpty', '请选择所在城市', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dae42f28de1a48feac3c7fac4f72db87', 'realize.project.not.fount', '变现项目未找到', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('daed620884894395a1049530a1036c97', 'export.num.error', '导出记录不能超过10000条，请分批次导出', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('db517a6f3d354df58fdfaa826fa98c64', 'product.userId.notEmpty', '资金账户不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('db52efece59e468ebe8b13bd84651c9a', 'bond.message.bondInvestMostLimitError', '投资金额大于债权最高投资金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dceef453e7c647f0bd83275f8535cd59', 'redenvelope.num.must.gt.zero', '规则数量不需大于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dcf3b7870b1440b79c7cbc76ba8b295b', 'bond.message.remainDays.zero', '勾选还款日限制距离还款日必须大于0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dd6cdf97f35447d99dc9d49bc4c468d9', 'operator.first.oldPwd.error', '您的初始密码输入错误，如遗忘请联系平台', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dd9cd903dc184b0c907e5ea8defc3429', 'borrowBespeak.limitTime.emptyMsg', '请填写借款期限', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ddb0db0c7ee511e699af000c296f8d8c', 'risk.messgae.answer.sort.is.exists', '题号已存在,请重新选择!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ddf2b4c96fd844c3aaf04527f3d824be', 'bond.message.bondInvestAccountError', '债权投资金额有误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('de2f0eb080a211e699af000c296f8d8c', 'customer.message.remark.too.long', '备注过长,请不要输入超过128个字符!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('dfadfc22f0354bc380866d6e40a67f3b', 'autoInvestRuleLog.useMoney.error', '账户可用余额达{0}元才能开启自动投资，请充值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e00975a4d85f41d78a34cb4890b3a2cf', 'bond.message.bondInsertHasHandle', '债权发布已经处理了,请勿多次提交处理!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e111aa3de7fc41e0b6d4195077c82dc1', 'project.saleVipLevel.notEmpty', '定向销售VIP不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e13c3e8b108b40bbaf5e0bf09910cb0c', 'borrow.projectName.outOfRange', '借款名称不能超过{0}个字符', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e32674198ac64682936600ae6cd23c40', 'autoInvestRuleLog.monthLimit.rangeError', '月范围最大值不能小于最小值', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e348cca5ea144019930d7f0bdf845946', 'product.copyAccount.undivisibleByAccount', '每份金额必须能被产品总额整除', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e35c979ac03e4dfc878cfae2af318180', 'borrow.repayStyle.error', '请选择还款方式', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e42cb9e760bb4f9090675aaa4dd106d6', 'autoInvest.useMoney.ltLowestAccount', '您的可用小于此产品最小限额，自动投资失败', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e4ce966f04d447ad891f94db6c44d5fb', 'product.lowestCopies.gtTotalCopies', '起投份数不能高于项目总份数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e50bd8a661164ad19baa4fe7a7601ec3', 'operator.newPwd.again.error', '新密码与原密码不能相同', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e5f7a68695af11e6bdf8000c296f8d8c', 'protocol_status_not_valid', '协议无效', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e6289785706f41d9ab7458922b8906b1', 'borrowBespeak.contactName.lengthError', '姓名不能超过10个汉字', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e70b6039918049afb144236fcca638e9', 'global.msg.success', '操作成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e71af84819b14e9f9fa20d5e741831ed', 'project.addapr.additionalRateUseful.superposition', '加息率和加息券不能叠加使用', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e739c448723a4f5796398623364fb103', 'bond.message.bondIsCancleError', '债权已经撤回，请勿重复撤回。', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e750338c839011e699af000c296f8d8c', 'user.register.mobile.style.error', '手机号码格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e804586b6bc749b282a68f30bb98013a', 'sys.user.loginName.lengthError', '用户登录名长度必须位于6到20之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e819f5209f4c4277b6b8e2f736142109', 'redEnvelopeRule.message.uprate.error', '加息利率不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e81a25efa30f44ed99357e3c5d0d6279', 'project.saleMail.msg', '仅定向用户可投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e8253c1ac07643c096dacdde8f76cf09', 'borrowBespeak.city.emptyMsg', '请填写您的常住城市', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e8d76ec2022943c2a3b5fc1da1805d83', 'borrowBespeak.money.emptyMsg', '请填写借款金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e8f695d4e1a04326b7b86e77b0f3edd1', 'bond.message.rule.feeRate.overLimit', '汇付限制,比例手续费比例值不能超过%s', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e90011c1313f453799b6c4d0e0fa2ba6', 'login.fail.with.last.chance', '还有最后一次重试机会', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e947ed73c5ef4411aa79331c740054fa', 'web.invest.amountIsNotVaild', '投资金额不正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('e9b3fa76431348249b30ab9d58bc7301', 'user.orgCode.irregular', '组织机构代码不合规则: 9位数字（或大写拉丁字母）', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ea0fac88861811e699af000c296f8d8c', 'bond.message.bond.protocol.is.not.exists', '债权协议不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ea3ffcba2f7346849d4191c9817d6243', 'item.name.over.length', '字典名称长度不能超过64', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ea70b7d9cc1042a19ad1331fbf67f0c1', 'operator.pwd.error', '密码格式错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ead81bd006434c449cbc4e9f3534e746', 'project.interestStartDays.rangeError', '起始计息天数不能小于0且不大于7', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('eb1ae233813d11e699af000c296f8d8c', 'redEnvelopeRule.message.startTime.error', '发放开始时间不能小于当前时间!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('eb9cec9a92f34cf78aef9fed509787aa', 'core.invest.amountLessLowest', '投资金额小于起投金额!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ebadb1ed256e4fd3b7e4d68fd602f10b', 'projectInvestBespeak.status.error', '项目状态错误，预约投资失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ebf36550d9a44b2fbfe067854b9ec79c', 'project.riskLevel.notEmpty', '风险等级不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ec4701efbbdb41e195eff8c9062fde6e', 'article.url.valid', '文章链接不能为空，并且不能超过{0}个字符！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ecd5d59262fc47d9bb5624e40b539095', 'project.interest.mustlt.capital', '利率过大或者期限过长导致利息超过本金', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('edd2270cf2ef43cda35b313547d7e075', 'redenvelope.cancel.status.error', '只有未使用的红包才能作废', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ee35a6e4dd04426d9816eb1a5cb3e218', 'core.rateCoupon.useError', '加息券使用错误：已使用或过期、限定项目类别！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ee52841d1eef4ae38051ace671d73986', 'bond.message.insert.repeat', '请勿重复点击提交发布债权', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('eed51499d85d4ce0abba563ebeacc439', 'customer.operator.exists', '该用户已有经纪人，请您确认手机号码是否正确', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ef050c9c8242423da4518cda094089a0', 'core.redEnvelope.cannotUseError', '投资项目不可使用红包', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('efa2cc93093c4bec9c2cb2abeb96e133', 'core.rateCoupon.useByOtherError', '冒用他人的加息券！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f0810e8e52fa40e396ebad968391062c', 'project.lowestAccount.gtMostAccountError', '最低起投金额不能超过最高投资总额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f0a291209ae64ffa8868b3dc365e1159', 'bond.message.disrate.min.limit', '折溢价率的最小值不能小于等于-100', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f17acd297335433e82f79a17000f4a77', 'core.repayment.hasRepaid', '该期借款已经还款,请不要重复操作！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f2a853bd167945f5bd61d1c913bcabef', 'sys.config.configName.lengthError', '名称长度须位于2到50之间', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f2b65772a86a43c0bd47229a20c73ad8', 'realize.is.not.raising', '变现项目不在募集状态', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f3df517beb554e9794ea135ce641f7c2', 'autoInvest.remainAccount.ltDoubleLowestAccount', '自动投资所剩额度已不足两倍最底投资额度', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f5f2cfd16d0411e6b37f000c293c07d6', 'bond.message.rule.tppSellLimit', 'UFX对接汇付第三方接口不允许债权部分转让,请选择一次性全额转让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f63e588fc62d4134aef9ade9af6b9b22', 'core.project.investBeforeSaleTime', '未到开售时间！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f6783da7014b4dbd82d61017e94a0328', 'tpp.account.queryFail', '查询托管账户失败，请联系管理员', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f6c7012e95f911e6bdf8000c296f8d8c', 'protocol.message.isnot.exists', '协议不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f6d49ae94eac48f487b1f7f95836393b', 'global.noRecord', '请选择操作记录', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f78e92492f0d45b3bd1028063c24488f', 'project.account.mustInt', '产品(借款)金额必须为整数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f79d18abf7994e0aa459a9b8027b74a1', 'bond.message.bondInvest.ruleBuyAllLimitError', '一次性全额受让必须全部购买', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f897aa78152443bba49c1a5cd2a15233', 'global.statusError', '该状态下不可操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f8b990f9d1b744b2a0ac635f843b951d', 'core.invest.amountLessDoubleLowest', '剩余可投小于起投金额的两倍，需要购买全部剩余可投！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f92eb02bf3024137b5f187c42c47bb47', 'borrowBespeak.money.rangeError', '请输入大于{0}元，小于{1}元的借款金额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f93accaa87d040fbb21190533a40b57f', 'project.noRecord', '很抱歉，您查看的产品找不到了！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f9a8b46e690111e68586000c293c07d6', 'bond.message.projectTypeNotExists', '项目类型不存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f9db620043064edd96c7b0bd250a565a', 'project.repayment.money.zero', '项目还款金额为0不能进行还款操作', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('f9de0f14813d11e699af000c296f8d8c', 'redEnvelopeRule.message.endTime.error', ' 发放结束时间不能小于当前时间!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa130103780f11e68fb9000c293c07d1', 'login.success', '登录成功', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa130103780f11e68fb9000c293c07d2', 'login.fail.for.user.is.lock', '账号已被锁定，请您联系客服', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa130103780f11e68fb9000c293c07d3', 'login.fail.for.code.is.error', '登录失败，图形验证码错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa130103780f11e68fb9000c293c07d4', 'login.fail.for.name.or.password.is.error', '登录失败，用户名或密码错误', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa27f2b0f69f486c818dec3ed45cb939', 'user.is.exist', '该账户已经存在', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa6650447ef111e699af000c296f8d8c', 'risk.messgae.papers.publish.is.only', '发布只能同时发布一个', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fa9bdabc88d543fabce8608cce71a278', 'realize.protocol.not.check', '请先阅读协议并勾选', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('faa5a94ba20747b6a38deece5eaae0b9', 'bond.message.invest.must.buy.all', '等额本金或者等额本息还款方式下必须全额受让', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fabcf95173aa4653a4752202c8addff9', 'project.raising.editError', '已开售项目不可以修改！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fafae7cd27504082b7da0f1525a2c013', 'autoInvestRuleLog.need.risk', '您需完成风险承受能力评估，才能开启自动投资', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fb6454b3b34e4941b8e20a2e5c3f518a', 'product.mostCopies.gtTotalCopies', '最高可投份数不能高于项目总份数', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fcba0110de3e4742b85e480a464c5284', 'company.office.zone.empty', '办公地点不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fd20b505ff914f72a9a830a646a72420', 'core.project.account.notEnoughError', '项目可投金额不足!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fd386e65e7dc4867b597169835ff6d4c', 'user.companyName.isEmpty', '企业名称不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fdcaed1c5597460fbbdd12d1de2669d2', 'article.content.not.null', '文章详情不能为空', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fe32dbea55a1443fb4557f8ea4dc700a', 'realize.is.full', '变现已满额', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('fe9dacc59d2a488285315e3817badfd2', 'rule_ban_error_for_vip_level', 'VIP等级奖励：{0}正在使用，规则禁用失败！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ff24ca63b5994e53a3b7f554a0e5cbf6', 'project.fixedRepayDay.gtDaysOfMonth', '固定还款日不能大于{0}天', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ff99bcb15f3340deb986cb577b0f0e96', 'change.money.not.null', '更新失败，变动金额都为0', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4099', 'section.code.has.exist', '栏目标识已存在！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4100', 'calculator.repay.style.error', '还款方式与投资期限不匹配！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4101', 'user.mobile.not.exist', '手机号码未注册！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4111', 'redenvolopeRule.grantmin.notEmpty', '红包最小金额为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4112', 'user.email.not.exist', '邮箱未绑定账号！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4119', 'password.retrieve.type.error', '密码找回方式有误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4122', 'retrieve.password.not.change', '新密码与原密码不能相同！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4123', 'redenvelope.max.period.error', '固定期红包最大有效期限不能超过{0}天', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4133', 'config.value.max.lenth', '参数值为必填项且输入长度不允许超过{0}！', null);
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4234', 'dynamic.validcode.error', '短信或邮箱验证码错误！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e01', 'role.name.error', '角色名称为必填项且长度不能超过{0}!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e11', 'redenvolopeRule.grantrate.notEmpty', '发放比例为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e23', 'redenvolopeRule.grant.amount.error', '最小金额不能大于最大金额！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e31', 'record.is.verified', '该记录已审核', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e35', 'redenvolopeRule.grantmax.notEmpty', '红包最大金额为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e75', 'order.is.timeout', '订单超时', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e89', 'redenvolopeRule.grant.begintime.notempty', '发放开始时间为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e90', 'redenvolopeRule.grant.endtime.notempty', '发放结束时间为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e91', 'redenvolopeRule.grant.time.error', '发放结束时间不能小于开始时间!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e92', 'tender.amount.integer.error', '投资金额应为整数！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e93', 'grant.quantity.integer.error', '发放数量应为有效整数！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e94', 'use.project.type.notempty', '使用限制条件不能为空！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e95', 'vouch.is.freeze', '担保功能已被锁定，请联系客服人员!', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e96', 'redenvolopeRule.tendermin.notEmpty', '最小投资金额为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e97', 'redenvolopeRule.tendermax.notEmpty', '最大投资金额为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e98', 'redenvolopeRule.tender.amount.error', '最小投资金额不能大于最大投资金额！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('ffe39a5b53a6444f926269b0120c4e99', 'redenvolopeRule.grantamount.notEmpty', '红包金额为必填项！', 'zh_CN');
INSERT INTO `sys_resource` VALUES ('1a84321151b04b92978aa0535402d0f7', 'userRed.grant.tooOften', '用户红包发放操作过于频繁', 'zh_CN');
insert into sys_resource values('200c0fab03134fa9fd9ee5c5029b4601','project.type.nid.has.existed','分类标识已存在','zh_CN');
-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `code` varchar(64) DEFAULT NULL COMMENT '角色标识，如admin/manager等',
  `role_name` varchar(16) NOT NULL COMMENT '角色名',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `delete_flag` char(1) DEFAULT '0' COMMENT '0 未删除，1 已删除',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_role_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('22c0eef4a2c14515855d84f96eb991ae', 'admin', '超级管理员', '2016-09-29 16:34:34', '0', '');
INSERT INTO `sys_role` VALUES ('839ab500da37478c84e9415c2283ee13', 'agent', '经纪人', '2016-10-26 10:44:18', '0', '');
INSERT INTO `sys_role` VALUES ('e6c841039f7f459c9a68e540ef8462b0', 'onlineServer', '在线客服', '2016-10-13 15:48:19', '0', '');

-- ----------------------------
-- Table structure for `sys_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `role_id` varchar(32) NOT NULL COMMENT '角色主键',
  `permission_id` varchar(32) NOT NULL COMMENT '权限主键',
  PRIMARY KEY (`uuid`),
  KEY `fk_sys_role_permission_permission_id` (`permission_id`) USING BTREE,
  KEY `fk_sys_role_permission_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('003b379d6c854e2e9de247ac607f5448', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867610');
INSERT INTO `sys_role_permission` VALUES ('014fca72ced4407eabcafaee58b38380', '22c0eef4a2c14515855d84f96eb991ae', '123asd51236h187eug2e');
INSERT INTO `sys_role_permission` VALUES ('024dc5f4c0d345368bedc7b769bcb6e1', '22c0eef4a2c14515855d84f96eb991ae', '226a55c07ff811e68fb9000c293c07d6');
INSERT INTO `sys_role_permission` VALUES ('03f1023443424bc4b269861a922d02db', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867641');
INSERT INTO `sys_role_permission` VALUES ('04005797626f43d18b65c3491fb88c55', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867698');
INSERT INTO `sys_role_permission` VALUES ('0480d453c1654f56b46b7c404c977871', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867579');
INSERT INTO `sys_role_permission` VALUES ('04bcddf45d7c421785941856d8a51c04', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867502');
INSERT INTO `sys_role_permission` VALUES ('05a6c3d4ce704da0bff9cfd82854b14e', '22c0eef4a2c14515855d84f96eb991ae', '1249617671234867629');
INSERT INTO `sys_role_permission` VALUES ('09712d179f644651bedc7b542b7b009e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867565');
INSERT INTO `sys_role_permission` VALUES ('09be6a070e774df48261756ff9ad428c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867525');
INSERT INTO `sys_role_permission` VALUES ('0be6c32d1f894483b8c54cfaf31b44b0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867514');
INSERT INTO `sys_role_permission` VALUES ('0f85584e41a94f73b5ba2b64603ff9c4', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867625');
INSERT INTO `sys_role_permission` VALUES ('107db4eab51e420aaa09ac8d90051cd6', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867601');
INSERT INTO `sys_role_permission` VALUES ('1130ba16259b44db974d786fd1f41dfd', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867541');
INSERT INTO `sys_role_permission` VALUES ('1537a9f8b57b436bab021d2007ebd74c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867536');
INSERT INTO `sys_role_permission` VALUES ('156419cddb1444b9a1ab17696b215bbb', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867519');
INSERT INTO `sys_role_permission` VALUES ('18650e303c3d4fd7ae61d9ff2af9473a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867615');
INSERT INTO `sys_role_permission` VALUES ('1b2322307b5349499c047e2dae97afd5', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867572');
INSERT INTO `sys_role_permission` VALUES ('1cee213259f54777b23b8b63e835e573', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867493');
INSERT INTO `sys_role_permission` VALUES ('1da77d57052441f7b3e5b708fff32472', '22c0eef4a2c14515855d84f96eb991ae', '1249611175294867495');
INSERT INTO `sys_role_permission` VALUES ('21240819ebcd4582b329444c57e1df2a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867619');
INSERT INTO `sys_role_permission` VALUES ('228b18c16f2b4a84ac4afbcb3cbf12ab', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867580');
INSERT INTO `sys_role_permission` VALUES ('254878670843414fa21ff2fe4f857683', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867548');
INSERT INTO `sys_role_permission` VALUES ('276f35a4939740dc8a21b133768d6c09', '22c0eef4a2c14515855d84f96eb991ae', 'e54c5ca6848e11e699af000c296f8d8c');
INSERT INTO `sys_role_permission` VALUES ('295d592b27cb459e843bea05717eeb98', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867526');
INSERT INTO `sys_role_permission` VALUES ('297c716d0c794bfb9307e7354420352f', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867633');
INSERT INTO `sys_role_permission` VALUES ('2cf6e4e7d6eb42af88cf8cac06ed15eb', '22c0eef4a2c14515855d84f96eb991ae', '1249611175294877517');
INSERT INTO `sys_role_permission` VALUES ('2d245c1de6ed4c31917720b0f846f2e9', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867588');
INSERT INTO `sys_role_permission` VALUES ('303b19f1c3dc4a22a174b149879734a0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867488');
INSERT INTO `sys_role_permission` VALUES ('31522f9ff08944379c19054388191f84', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867583');
INSERT INTO `sys_role_permission` VALUES ('3178465a00ac4f9bb9c64601188c23f8', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867509');
INSERT INTO `sys_role_permission` VALUES ('32bedca4590b49babbb70e373f0b6e3a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867598');
INSERT INTO `sys_role_permission` VALUES ('38e02616e16d40b781ccdb94defae404', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867597');
INSERT INTO `sys_role_permission` VALUES ('3d36cd0ed7444816932547be707b88c4', '22c0eef4a2c14515855d84f96eb991ae', '3');
INSERT INTO `sys_role_permission` VALUES ('3d9f7ab666e24251a1a54b42938763fc', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867510');
INSERT INTO `sys_role_permission` VALUES ('3e0259747a8342b9bc6babb53cbe2906', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867489');
INSERT INTO `sys_role_permission` VALUES ('3e3f241b087d463f914171335bce9534', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867558');
INSERT INTO `sys_role_permission` VALUES ('40059b18372b499b87737d427f90f0f5', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867628');
INSERT INTO `sys_role_permission` VALUES ('4217e7f71fe7484cb65261c9ea17f67d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867604');
INSERT INTO `sys_role_permission` VALUES ('42651967910f40afb28708eb71ffb156', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867491');
INSERT INTO `sys_role_permission` VALUES ('4346fb31c57e4cdfba0c60fb7bba9e07', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867494');
INSERT INTO `sys_role_permission` VALUES ('43e554a2f1c547228719ddc0be5bc680', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867477');
INSERT INTO `sys_role_permission` VALUES ('45fb42e0fa3f4e5092dbd9874d381325', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867629');
INSERT INTO `sys_role_permission` VALUES ('47388a7dde2c40dbbe56587a5900fe0e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867567');
INSERT INTO `sys_role_permission` VALUES ('48b6007274eb4a46a3ce7e5018bde762', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867591');
INSERT INTO `sys_role_permission` VALUES ('4c0889f2314048e4b8bbc1b534493d7c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867555');
INSERT INTO `sys_role_permission` VALUES ('50c7de3d151a4653a74b848b4b0684cc', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867515');
INSERT INTO `sys_role_permission` VALUES ('5179280bb2014f5c813bbf67cf5ece66', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867578');
INSERT INTO `sys_role_permission` VALUES ('53314cd175d7463497f923a8622a8aa3', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867584');
INSERT INTO `sys_role_permission` VALUES ('53d8543d65a94a64b8467f0d6d6d5595', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867468');
INSERT INTO `sys_role_permission` VALUES ('5507d4ad8832455bb4f6d2e02abbde36', '22c0eef4a2c14515855d84f96eb991ae', '1249611440076095822');
INSERT INTO `sys_role_permission` VALUES ('57487d9f48a242bbb87097437d25a6ff', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867532');
INSERT INTO `sys_role_permission` VALUES ('57e7d8c8765541de9d3b85ea98c38918', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867590');
INSERT INTO `sys_role_permission` VALUES ('59cf6ed06f3c4ff9a91ce849a3c65b39', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867511');
INSERT INTO `sys_role_permission` VALUES ('5aad610bc30a4cdeb100649131567a30', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867585');
INSERT INTO `sys_role_permission` VALUES ('5b34c80658e848e68e0143859f27237b', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867474');
INSERT INTO `sys_role_permission` VALUES ('5bc8eb670e814702a77c1eb908419ac7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294868691');
INSERT INTO `sys_role_permission` VALUES ('5c6c5930419e4dfb8c9b6b67386a59a1', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867595');
INSERT INTO `sys_role_permission` VALUES ('5f8f4e7bd6b74808a3a0b7c833f04257', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294887566');
INSERT INTO `sys_role_permission` VALUES ('5fc9106777864b7d82aa2632096f4879', '22c0eef4a2c14515855d84f96eb991ae', '1249617633394867497');
INSERT INTO `sys_role_permission` VALUES ('60331b5aa1e14ac2a06390b8c7c930dd', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867550');
INSERT INTO `sys_role_permission` VALUES ('6051cecc1d164f7da8ae9e4be599fe7d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867563');
INSERT INTO `sys_role_permission` VALUES ('6142c8ab37eb4bfc824c4b11a1113ebe', '22c0eef4a2c14515855d84f96eb991ae', '1249617225294867496');
INSERT INTO `sys_role_permission` VALUES ('6314a027d83a4b95b1f5ece820825aa8', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867518');
INSERT INTO `sys_role_permission` VALUES ('6375583a34de4adfaa5b820897dffd42', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867534');
INSERT INTO `sys_role_permission` VALUES ('639507ec39374c95aa706eefabe9d5e6', '22c0eef4a2c14515855d84f96eb991ae', 'ba8c6633a0fa11e69638000c296f8d8c');
INSERT INTO `sys_role_permission` VALUES ('65b4f687db4b4f2bbb1739c9b57fc3c9', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867568');
INSERT INTO `sys_role_permission` VALUES ('66bde0abe9924118ac60331ff301b6da', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867553');
INSERT INTO `sys_role_permission` VALUES ('67d000d29bb8428bb94151ed10b6274b', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867478');
INSERT INTO `sys_role_permission` VALUES ('6945b48ff4ac4b0da28d336fab33a8d7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867627');
INSERT INTO `sys_role_permission` VALUES ('6a151e7d9c704df6a311cd558dddd668', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867522');
INSERT INTO `sys_role_permission` VALUES ('6c3351d75aa044ce9f08a6f983fe9ac9', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867476');
INSERT INTO `sys_role_permission` VALUES ('6ef57a32b86841a1855a3554f7e640f7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867470');
INSERT INTO `sys_role_permission` VALUES ('701b3f4db49e406788e47f328f1a955f', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867560');
INSERT INTO `sys_role_permission` VALUES ('70aaf230b7254f46afe759c5b15fa50e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867552');
INSERT INTO `sys_role_permission` VALUES ('71008909f15f473f91674b050c95c837', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867564');
INSERT INTO `sys_role_permission` VALUES ('71946dcad3264cb1bc6743619219d2bc', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867569');
INSERT INTO `sys_role_permission` VALUES ('73a3c058d91944e68b72f7a2c68e6a0c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867490');
INSERT INTO `sys_role_permission` VALUES ('75768863b64e4b43ba3044d656bab623', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867495');
INSERT INTO `sys_role_permission` VALUES ('77c0adcae1df4ff8822e1ef1c95f45be', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867607');
INSERT INTO `sys_role_permission` VALUES ('7a2cd21e3a8248e3a1ee1f2c6de93808', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867554');
INSERT INTO `sys_role_permission` VALUES ('7a4603978e014cedaf2d89a94028877c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294868617');
INSERT INTO `sys_role_permission` VALUES ('7f5131b82e904fadba49314a7b5345ef', '22c0eef4a2c14515855d84f96eb991ae', '12496176752qwe67558');
INSERT INTO `sys_role_permission` VALUES ('80e0a5bf16c748fea3c0f942e804ec3e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867551');
INSERT INTO `sys_role_permission` VALUES ('8158e8e0217e413f909d8810309fdd20', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867472');
INSERT INTO `sys_role_permission` VALUES ('82bbc102b11e4d79bcf22cc8057bb8cc', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867487');
INSERT INTO `sys_role_permission` VALUES ('82e1a9f0ee3f472c87179d2bdbcc1ff3', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867542');
INSERT INTO `sys_role_permission` VALUES ('840bb9cfa7f142a3a48ab7c0a61d6bc1', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867527');
INSERT INTO `sys_role_permission` VALUES ('84c660a783414351ae1abfdf580df691', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867538');
INSERT INTO `sys_role_permission` VALUES ('84d0598ea1d14afcbb8404f55471b9a0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867520');
INSERT INTO `sys_role_permission` VALUES ('84ffd3aa92434120945101c51a84c588', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867512');
INSERT INTO `sys_role_permission` VALUES ('88bb9d0878ee427dbe2ba391302979b4', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867620');
INSERT INTO `sys_role_permission` VALUES ('8c22c855b7954de2b3c8a1e25f294930', '22c0eef4a2c14515855d84f96eb991ae', '121231a123114546786asda');
INSERT INTO `sys_role_permission` VALUES ('8d839fe922224059916a3d0b01444899', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867592');
INSERT INTO `sys_role_permission` VALUES ('92473be8ebc64c54a22899cd708b1262', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294868592');
INSERT INTO `sys_role_permission` VALUES ('9377ac8b33194b5fafa790ca9196e9f4', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867618');
INSERT INTO `sys_role_permission` VALUES ('9478120928234ab9a653b2d156303568', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867549');
INSERT INTO `sys_role_permission` VALUES ('9494f497b5df4a24966b5e2625e064db', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867475');
INSERT INTO `sys_role_permission` VALUES ('95778c42c75040438bb0e3abc889898c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867638');
INSERT INTO `sys_role_permission` VALUES ('957e4cacfb984ac2b419896ca23de44f', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867587');
INSERT INTO `sys_role_permission` VALUES ('969b897621b64d33adaf76d0a122df4d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867603');
INSERT INTO `sys_role_permission` VALUES ('981204b991cb4ec0889116e56e26f7a2', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867517');
INSERT INTO `sys_role_permission` VALUES ('98fc720f25a74c5e93ef95304d3b38ec', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867612');
INSERT INTO `sys_role_permission` VALUES ('9922f779fea449b1909e376147008c07', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867528');
INSERT INTO `sys_role_permission` VALUES ('9a605495848844a7b92655af5a572c47', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294800001');
INSERT INTO `sys_role_permission` VALUES ('9ae4355ff5d04ee289b0cac467a08b0a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867523');
INSERT INTO `sys_role_permission` VALUES ('9c5085dbdd6f4c6ab8061b13de42be57', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867471');
INSERT INTO `sys_role_permission` VALUES ('9cc49d21de17417ba4dd4dfe39b71aa3', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867566');
INSERT INTO `sys_role_permission` VALUES ('9cdbc0de35ba44ec998c99317df5f609', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294860491');
INSERT INTO `sys_role_permission` VALUES ('9f3b9be47db84f1db169e75ffd68f46b', '22c0eef4a2c14515855d84f96eb991ae', 'asd123f34hfdghtgh34534');
INSERT INTO `sys_role_permission` VALUES ('a07734974fda4777b2ab89861f0897ed', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867499');
INSERT INTO `sys_role_permission` VALUES ('a0c6615f633c48788c37c603273c34b0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867469');
INSERT INTO `sys_role_permission` VALUES ('a1a22b3d6fd3474cb65ca32a2946e2c0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867617');
INSERT INTO `sys_role_permission` VALUES ('a1b25345dbbe4c7bb797ab29fb279e4d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867513');
INSERT INTO `sys_role_permission` VALUES ('a2fbd11dc6fd465f9dc05d9fda9d84f4', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867634');
INSERT INTO `sys_role_permission` VALUES ('a583b14bef6148f987dbfeeaf377d4d9', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867621');
INSERT INTO `sys_role_permission` VALUES ('a65b396cc86e4815aaea286fd2bf8fe4', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867559');
INSERT INTO `sys_role_permission` VALUES ('a74abb42203945bc8c6eee146ea62f40', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867530');
INSERT INTO `sys_role_permission` VALUES ('aac82d71c9c440a3927f29df4b8b892f', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867589');
INSERT INTO `sys_role_permission` VALUES ('ab5a891cb1d549c4b13e4dc3adb7ff59', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867575');
INSERT INTO `sys_role_permission` VALUES ('aba6b0c24adf424fad4cf9406055931c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867571');
INSERT INTO `sys_role_permission` VALUES ('ac8f2f3dcd114f3f98487bdcea87fa97', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867497');
INSERT INTO `sys_role_permission` VALUES ('af316df312274e329700d1e51854eadb', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294877517');
INSERT INTO `sys_role_permission` VALUES ('afd0f427b4c44402ad7ca39c6398b9b1', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867524');
INSERT INTO `sys_role_permission` VALUES ('b01cf54117fc49748967aad4ec7269c6', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867533');
INSERT INTO `sys_role_permission` VALUES ('b028e54cce6b4d3d8c28e8e03897c435', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867503');
INSERT INTO `sys_role_permission` VALUES ('b03115fbf22c471b822cbf8533a43fb2', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867614');
INSERT INTO `sys_role_permission` VALUES ('b0b040200a7a465a88242b95d578b6e5', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867473');
INSERT INTO `sys_role_permission` VALUES ('b31b5132e3a14ecbbf610673944110de', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867608');
INSERT INTO `sys_role_permission` VALUES ('b347ed8e34b64226a7993809338238e8', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867574');
INSERT INTO `sys_role_permission` VALUES ('b4c2963f20c24543b50e479d77c07cf9', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867577');
INSERT INTO `sys_role_permission` VALUES ('b4d5a5efedfc4fb2841e74b66880e07f', '22c0eef4a2c14515855d84f96eb991ae', '121231a12311232131123123989889');
INSERT INTO `sys_role_permission` VALUES ('b6b59d7ab6c84eadb860f5cbbfce7365', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867599');
INSERT INTO `sys_role_permission` VALUES ('b867ed09c89d4144b771d15a8cfe414b', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867529');
INSERT INTO `sys_role_permission` VALUES ('b8bfa41aff4b4db7a4f691cbb9391baf', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867636');
INSERT INTO `sys_role_permission` VALUES ('b93c1b06467f43878dc777621702e6a3', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867539');
INSERT INTO `sys_role_permission` VALUES ('bb36c974b4ce4728bf8cd8383ba92dd7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867516');
INSERT INTO `sys_role_permission` VALUES ('bca39949f1124a4096747ca109e40ad8', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867480');
INSERT INTO `sys_role_permission` VALUES ('bde38601b03b4a1cb32dff6d4181d28a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867594');
INSERT INTO `sys_role_permission` VALUES ('bf17789cf4d04d6d98d6733018031660', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867531');
INSERT INTO `sys_role_permission` VALUES ('bf3dadb6901a4e22bdce6744c9b39b3a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867602');
INSERT INTO `sys_role_permission` VALUES ('c023205e98b84462ac85f44cb80f353d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867631');
INSERT INTO `sys_role_permission` VALUES ('c8bc5a88bd024fd9bf9aa5335aed3f02', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867556');
INSERT INTO `sys_role_permission` VALUES ('caaa6943e1e146fc91678445d9794d0e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867479');
INSERT INTO `sys_role_permission` VALUES ('cb586cd2f25647f2962eb2481cc8e498', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867613');
INSERT INTO `sys_role_permission` VALUES ('cc368afd71a3411995c4fd39a2ab2000', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294863428');
INSERT INTO `sys_role_permission` VALUES ('cdff787ceb0c4a879725b4be49c35dd6', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867501');
INSERT INTO `sys_role_permission` VALUES ('cee1e0adbaa3406281b13638a13889eb', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867626');
INSERT INTO `sys_role_permission` VALUES ('d11a2b794983483297155e64d7277e2c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867521');
INSERT INTO `sys_role_permission` VALUES ('d22f4b144f6b44b78b4a1e64b743f8ca', '22c0eef4a2c14515855d84f96eb991ae', 'bd6aaf15848e11e699af000c296f8d8c');
INSERT INTO `sys_role_permission` VALUES ('d4c64b635d714bfb91d278d59bb17bfa', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867498');
INSERT INTO `sys_role_permission` VALUES ('d5a898845e81437ebe8cec0df6c34736', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867605');
INSERT INTO `sys_role_permission` VALUES ('d62b54bedc4147a0b30952641d301a4e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294860493');
INSERT INTO `sys_role_permission` VALUES ('d8a66dd71bbe4d218b94768de192bcef', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867582');
INSERT INTO `sys_role_permission` VALUES ('d9a145edcce34297b8f770cca37a488d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867635');
INSERT INTO `sys_role_permission` VALUES ('d9d7673e43f347ab8d52c7529d8bc38d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867609');
INSERT INTO `sys_role_permission` VALUES ('da2975c1ab39436fa4ab755b0d41e8dc', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867581');
INSERT INTO `sys_role_permission` VALUES ('db58f3ffa7134c50bd5d1028e5b4575f', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867637');
INSERT INTO `sys_role_permission` VALUES ('dd5e26295f744798be88dc3ffcee36b6', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867600');
INSERT INTO `sys_role_permission` VALUES ('dd67c82c65f3475e990b87103fbb7d3b', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867576');
INSERT INTO `sys_role_permission` VALUES ('deb621ec1f0543b2b60d3e99614b02b7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867543');
INSERT INTO `sys_role_permission` VALUES ('dfe169a443b241d7b027fb377b3b00db', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867508');
INSERT INTO `sys_role_permission` VALUES ('dfeab263da424ddfa72dfb7f358d3979', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867586');
INSERT INTO `sys_role_permission` VALUES ('e0df3ed24b0e4741afdbf98e06113d5d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867496');
INSERT INTO `sys_role_permission` VALUES ('e14283543e8e4a809f5295b10f2fd98c', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867535');
INSERT INTO `sys_role_permission` VALUES ('e30fc686ad05492e8c1f23a5bec95358', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867709');
INSERT INTO `sys_role_permission` VALUES ('e34c5bfbbdda47bab47a6ae493051ee1', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867616');
INSERT INTO `sys_role_permission` VALUES ('e4821520904943158cd638508d47b911', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867596');
INSERT INTO `sys_role_permission` VALUES ('e50755d6975042a584e94b318dda05e7', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867570');
INSERT INTO `sys_role_permission` VALUES ('e8f07a71ffae49acb7f9e0ff1467144e', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867507');
INSERT INTO `sys_role_permission` VALUES ('ebbb5a6985544afb8984836406c6dc9d', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867640');
INSERT INTO `sys_role_permission` VALUES ('edf4cdf5f65b478394f664056f91a7e5', '22c0eef4a2c14515855d84f96eb991ae', '1249611440076095823');
INSERT INTO `sys_role_permission` VALUES ('ee94012f2a644bf3b49ace9094aa1924', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867630');
INSERT INTO `sys_role_permission` VALUES ('efa5e51968e44f83863abbcc72ea9dd5', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867611');
INSERT INTO `sys_role_permission` VALUES ('efb70c4c36944d3abd4d07a52e8e5794', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867573');
INSERT INTO `sys_role_permission` VALUES ('f0ad72c471a0415ebe1ba489d9b3e76e', '22c0eef4a2c14515855d84f96eb991ae', '1');
INSERT INTO `sys_role_permission` VALUES ('f15c9f038c7f4636af6acf4db0760c9a', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294860492');
INSERT INTO `sys_role_permission` VALUES ('f3729e5c1265478198be6e1ffd3a1350', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867593');
INSERT INTO `sys_role_permission` VALUES ('f4525fd9eca64c70ac848bc223ba2f5b', '22c0eef4a2c14515855d84f96eb991ae', '121231awe752qwe67558');
INSERT INTO `sys_role_permission` VALUES ('fbbdd27ce56f41d193c1c479e97933a0', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867606');
INSERT INTO `sys_role_permission` VALUES ('fc0c70989c5e4f41bfa24715d6580398', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867622');

-- ----------------------------
-- Table structure for `sys_user_message_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_message_config`;
CREATE TABLE `sys_user_message_config` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户',
  `message_type` varchar(32) DEFAULT NULL COMMENT '消息类型',
  `sms` char(1) DEFAULT '1' COMMENT '短信接收配置( 1 接收，0不接收)',
  `email` char(1) DEFAULT '1' COMMENT '邮件接收配置( 1 接收，0不接收)',
  `letter` char(1) DEFAULT '1' COMMENT '站内信接收配置( 1 接收，0不接收)',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户接收消息配置表';

-- ----------------------------
-- Records of sys_user_message_config
-- ----------------------------

-- ----------------------------
-- Table structure for `token_store`
-- ----------------------------
DROP TABLE IF EXISTS `token_store`;
CREATE TABLE `token_store` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `binding_id` varchar(256) DEFAULT NULL COMMENT '对应用户',
  `client_id` varchar(256) DEFAULT NULL COMMENT 'token来源',
  `expires_in` varchar(256) DEFAULT NULL COMMENT '有效时间',
  `oauth_token` varchar(256) DEFAULT NULL COMMENT 'token值',
  `refresh_token` varchar(256) DEFAULT NULL COMMENT '刷新token值',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='移动端token表';

-- ----------------------------
-- Records of token_store
-- ----------------------------

-- ----------------------------
-- Table structure for `tpp_merchant_log`
-- ----------------------------
DROP TABLE IF EXISTS `tpp_merchant_log`;
CREATE TABLE `tpp_merchant_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '操作员',
  `account` varchar(32) DEFAULT NULL COMMENT '付款账户',
  `to_account` varchar(32) DEFAULT NULL COMMENT '收款账户',
  `money` decimal(20,2) DEFAULT '0.00' COMMENT '操作金额',
  `fee` decimal(20,2) DEFAULT '0.00' COMMENT '操作手续费',
  `status` char(1) DEFAULT '0' COMMENT '操作状态(0处理中1成功2失败，默认0)',
  `operate_type` char(2) DEFAULT NULL COMMENT '操作类型(1充值2提现3转账)',
  `order_no` varchar(32) DEFAULT NULL COMMENT '订单号',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户账户操作日志（充值、提现、转账）';

-- ----------------------------
-- Records of tpp_merchant_log
-- ----------------------------

-- ----------------------------
-- Table structure for `tpp_trade`
-- ----------------------------
DROP TABLE IF EXISTS `tpp_trade`;
CREATE TABLE `tpp_trade` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `status` char(2) DEFAULT NULL COMMENT '状态：0-未处理，1-成功，2-失败',
  `service_type` varchar(50) DEFAULT NULL COMMENT '平台操作类型',
  `tpp_type` varchar(50) DEFAULT NULL COMMENT '第三方服务类型',
  `user_id` varchar(32) DEFAULT NULL COMMENT '付款用户',
  `tpp_user_cust_id` varchar(50) DEFAULT NULL COMMENT '付款用户第三方帐户',
  `to_user_id` varchar(32) DEFAULT NULL COMMENT '收款用户',
  `to_tpp_user_cust_id` varchar(50) DEFAULT NULL COMMENT '收款用户第三方账户',
  `money` decimal(20,4) DEFAULT NULL COMMENT '交易金额',
  `serv_fee` decimal(20,4) DEFAULT NULL COMMENT '服务费',
  `order_no` varchar(50) DEFAULT NULL COMMENT '平台订单号',
  `order_date` datetime DEFAULT NULL COMMENT '平台订单日期',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '三方流水号',
  `trade_date` datetime DEFAULT NULL COMMENT '三方交易日期',
  `project_id` varchar(32) DEFAULT NULL COMMENT '本地 projectNo，对应第三方的projectId',
  `invest_id` varchar(32) DEFAULT NULL COMMENT '投资id',
  `params` varchar(512) DEFAULT NULL COMMENT '扩展参数',
  `return_url` varchar(255) DEFAULT NULL COMMENT '前台回调url',
  `notice_url` varchar(255) DEFAULT NULL COMMENT '后台回调url',
  `resp_desc` varchar(1024) DEFAULT NULL COMMENT '回调信息',
  `invest_project_id` varchar(32) DEFAULT NULL COMMENT '本地项目id',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `redo_flag` char(1) DEFAULT '0' COMMENT '是否已重新发送',
  `pre_trade_id` varchar(32) DEFAULT NULL COMMENT '重发原记录ID',
  `notify_time` datetime DEFAULT NULL COMMENT '回调时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`uuid`),
  KEY `idx_tpp_trade_to_user_id` (`to_user_id`) USING BTREE,
  KEY `idx_tpp_trade_tpp_type` (`tpp_type`) USING BTREE,
  KEY `idx_tpp_trade_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口交易记录';

-- ----------------------------
-- Records of tpp_trade
-- ----------------------------

-- ----------------------------
-- Table structure for `urge_repay_log`
-- ----------------------------
DROP TABLE IF EXISTS `urge_repay_log`;
CREATE TABLE `urge_repay_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `repayment_id` varchar(32) DEFAULT NULL COMMENT '代还id',
  `operator_id` varchar(32) DEFAULT NULL COMMENT '操作员id',
  `create_time` datetime DEFAULT NULL COMMENT '催收时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='催收记录日志';

-- ----------------------------
-- Records of urge_repay_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_no` varchar(25) DEFAULT NULL COMMENT '用户唯一标识',
  `user_name` varchar(20) NOT NULL COMMENT '登录用户名',
  `pwd` varchar(64) NOT NULL COMMENT '登陆密码',
  `real_name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(2) DEFAULT '0' COMMENT '用户状态:(-1 锁定,0 正常,默认0)',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `pay_pwd` varchar(64) DEFAULT NULL COMMENT '交易密码',
  `tpp_status` char(1) DEFAULT '0' COMMENT '第三方账号是否激活 1 激活， 0未激活',
  `tpp_user_acc_id` varchar(50) DEFAULT NULL COMMENT '用户资金存管平台账户号(汇付天下账号如：xxx_2016051150021 )',
  `tpp_user_cust_id` varchar(50) DEFAULT NULL COMMENT '用户资金存管平台账户(uid)',
  `user_nature` char(1) DEFAULT NULL COMMENT '用户类型:  1个人用户、2 企业用户、3 担保机构，冗余user_cache.user_nature字段',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_user_user_name` (`user_name`) USING BTREE,
  UNIQUE KEY `uk_user_mobile` (`mobile`) USING BTREE,
  KEY `idx_user_create_time` (`create_time`) USING BTREE,
  KEY `idx_user_user_nature` (`user_nature`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('d315ad95a4d211e6b8d6408d5c2885aa', '1', 'admin', 'admin', null, null, null, '0', null, null, '0', null, null, '2');

-- ----------------------------
-- Table structure for `user_activity_award_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_activity_award_log`;
CREATE TABLE `user_activity_award_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `award` varchar(20) DEFAULT NULL COMMENT '发放的奖励(红包：金额，加息券：加息比例)',
  `red_rule_id` varchar(32) DEFAULT NULL COMMENT '红包规则id',
  `rate_rule_id` varchar(32) DEFAULT NULL COMMENT '加息券规则id',
  `user_red_id` varchar(32) DEFAULT NULL COMMENT '发放的红包id',
  `user_rate_id` varchar(32) DEFAULT NULL COMMENT '发放的加息券id',
  `tender_id` varchar(32) DEFAULT NULL COMMENT '投资记录id',
  `invite_user_id` varchar(32) DEFAULT NULL COMMENT '邀请人id',
  `award_type` char(1) DEFAULT NULL COMMENT '奖励类型 1 红包、2加息券',
  `create_time` datetime DEFAULT NULL COMMENT '发放时间',
  `activity_name` varchar(30) DEFAULT NULL COMMENT '活动名称',
  `rule_name` varchar(30) DEFAULT NULL COMMENT '规则名称',
  `activity_code` varchar(30) DEFAULT NULL COMMENT '活动标志',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动奖励记录';

-- ----------------------------
-- Records of user_activity_award_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_auth_sign_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_auth_sign_log`;
CREATE TABLE `user_auth_sign_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户',
  `auth_option` char(1) DEFAULT NULL COMMENT '签约类型 0：授权解约 1：授权签约 ',
  `service_types` varchar(25) DEFAULT NULL COMMENT '业务类型',
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `resp_desc` char(1) DEFAULT NULL COMMENT '0.未处理 1.成功 2.失败',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '操作ip',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户业务授权(解约)记录';

-- ----------------------------
-- Records of user_auth_sign_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_auto_invest`
-- ----------------------------
DROP TABLE IF EXISTS `user_auto_invest`;
CREATE TABLE `user_auto_invest` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `rule_id` varchar(32) DEFAULT NULL COMMENT '设置规则ID',
  `sort_time` bigint(20) DEFAULT NULL COMMENT '排序时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_auto_invest_user_id` (`user_id`) USING BTREE,
  KEY `idx_user_auto_invest_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户自动投资设置表';

-- ----------------------------
-- Records of user_auto_invest
-- ----------------------------

-- ----------------------------
-- Table structure for `user_base_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_base_info`;
CREATE TABLE `user_base_info` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `birthday` date DEFAULT NULL COMMENT '出生年月日',
  `education` char(2) DEFAULT NULL COMMENT '学历 1：小学；2：初中；3：高中；4：中专；5：大专；6：本科；7：硕士；8：博士；9其他',
  `marital_status` char(1) DEFAULT NULL COMMENT '婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶',
  `work_experience` char(2) DEFAULT NULL COMMENT '工作年限 1：0-3年；2：3-5年；3:5-8年；4:8年以上；',
  `month_income_range` varchar(20) DEFAULT NULL COMMENT '月收入范围',
  `car_status` char(1) DEFAULT '0' COMMENT '车产有无',
  `house_status` char(1) DEFAULT '0' COMMENT '房产有无',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_base_info_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户基本信息表';

-- ----------------------------
-- Records of user_base_info
-- ----------------------------

-- ----------------------------
-- Table structure for `user_cache`
-- ----------------------------
DROP TABLE IF EXISTS `user_cache`;
CREATE TABLE `user_cache` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `user_nature` char(1) DEFAULT NULL COMMENT '用户类型:  1个人用户、2 企业用户、3 担保机构',
  `cert_type` char(2) DEFAULT NULL COMMENT '证件类型，01 身份证18位，02 身份证15位；默认1',
  `id_no` varchar(32) DEFAULT NULL COMMENT '证件号码',
  `sex` char(1) DEFAULT NULL COMMENT '性别:M 男性，F女性',
  `post_code` varchar(6) DEFAULT NULL COMMENT '邮编',
  `province` varchar(50) DEFAULT NULL COMMENT '省',
  `city` varchar(50) DEFAULT NULL COMMENT '市',
  `area` varchar(50) DEFAULT NULL COMMENT '区',
  `address` varchar(128) DEFAULT NULL COMMENT '地址',
  `risk_level` char(2) DEFAULT '0' COMMENT '风险评估等级(数字等级)',
  `avatar_photo` varchar(256) DEFAULT NULL COMMENT '头像照片路径',
  `login_fail_times` int(1) DEFAULT NULL COMMENT '登录失败次数',
  `lock_type` char(1) DEFAULT NULL COMMENT '锁定类型  0-登录失败锁定 1-后台手动锁定',
  `lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `lock_remark` varchar(255) DEFAULT NULL COMMENT '锁定备注',
  `rename_flag` char(1) DEFAULT '0' COMMENT '用户名修改标识（默认0，修改过默认生成用户名置为1）',
  `sign_account` varchar(40) DEFAULT NULL COMMENT '签名认证账号',
  `sign_seal_data` text COMMENT '签名电子印章数据',
  `reg_mode` char(1) DEFAULT '0' COMMENT '用户注册方式 0 用户注册 1 后台录入',
  `invest_num` int(10) DEFAULT '0' COMMENT '用户投资记录数（在投、成功）',
  `reset_pwd_flag` char(1) DEFAULT '0' COMMENT '是否需要修改密码0.不需要1.需要',
  `first_award_flag` char(1) DEFAULT '0' COMMENT '首投奖励（0未发放，1已发放,默认0）',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_cache_user_id` (`user_id`) USING BTREE,
  KEY `idx_user_cache_id_no` (`id_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户附属信息表';

-- ----------------------------
-- Records of user_cache
-- ----------------------------

-- ----------------------------
-- Table structure for `user_company_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_company_info`;
CREATE TABLE `user_company_info` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `simple_name` varchar(10) DEFAULT NULL COMMENT '企业简称',
  `establish_date` date DEFAULT NULL COMMENT '成立时间',
  `registered_capital` varchar(16) DEFAULT NULL COMMENT '注册本金',
  `address` varchar(50) DEFAULT NULL COMMENT '企业地址',
  `bussiness_code` varchar(50) DEFAULT NULL COMMENT '执照号',
  `org_code` varchar(50) DEFAULT NULL COMMENT '组织机构代码',
  `license_address` varchar(50) DEFAULT NULL COMMENT '营业执照所在地',
  `logo` varchar(100) DEFAULT NULL COMMENT '企业LOGO',
  `license_expire_date` date DEFAULT NULL COMMENT '执照过期日（营业期限），格式为"YYYYMMDD"',
  `business_scope` varchar(256) DEFAULT NULL COMMENT '营业范围',
  `telephone` varchar(13) DEFAULT NULL COMMENT '联系电话',
  `contacts` varchar(10) DEFAULT NULL COMMENT '联系人',
  `email` varchar(50) DEFAULT NULL COMMENT '联系Email',
  `summary` varchar(512) DEFAULT NULL COMMENT '企业简介',
  `natural_person` varchar(50) DEFAULT NULL COMMENT '自然人',
  `legal_person` varchar(50) DEFAULT NULL COMMENT '企业法人',
  `cert_no` varchar(30) DEFAULT NULL COMMENT '法人证件号码',
  `cert_type` varchar(18) DEFAULT NULL COMMENT '证件类型，默认身份证',
  `legal_person_phone` varchar(11) DEFAULT NULL COMMENT '法人手机号码',
  `bank_code` varchar(10) DEFAULT NULL COMMENT '银行编号',
  `bank_account_no` varchar(30) DEFAULT NULL COMMENT '银行卡号',
  `bank_province` varchar(10) DEFAULT NULL COMMENT '开户行省份',
  `bank_city` varchar(10) DEFAULT NULL COMMENT '开户行城市',
  `bank_branch` varchar(30) DEFAULT NULL COMMENT '支行名称',
  `extend_param` varchar(200) DEFAULT NULL COMMENT '扩展信息',
  `audit_order_no` varchar(50) DEFAULT NULL COMMENT '请求审核订单号',
  `audit_status` char(2) DEFAULT '0' COMMENT '审核状态  0：待审核，1：审核成功，2：审核失败',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_message` varchar(200) DEFAULT NULL COMMENT '审核信息',
  `legal_delegate` varchar(50) DEFAULT NULL COMMENT '法人代表',
  `tax_reg_no` varchar(20) DEFAULT NULL COMMENT '税务登记号',
  `credit_code` varchar(32) DEFAULT NULL COMMENT '统一社会信用代码',
  `office_province` varchar(16) DEFAULT NULL COMMENT '工作地点省份',
  `office_city` varchar(16) DEFAULT NULL COMMENT '工作地点成市',
  `office_area` varchar(16) DEFAULT NULL COMMENT '工作地点地区',
  `office_address` varchar(50) DEFAULT NULL COMMENT '工作地点',
  `three_certificate` char(1) DEFAULT '0' COMMENT '是否三证合一用户（1是0否,默认0）',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_company_info_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='企业用户信息表';

-- ----------------------------
-- Records of user_company_info
-- ----------------------------

-- ----------------------------
-- Table structure for `user_earn_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_earn_log`;
CREATE TABLE `user_earn_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `earn_date` datetime DEFAULT NULL COMMENT '收益日期',
  `earn` decimal(20,4) DEFAULT '0.0000' COMMENT '收益金额',
  `total` decimal(20,4) DEFAULT '0.0000' COMMENT '累计收益',
  PRIMARY KEY (`uuid`),
  KEY `idx_user_earn_log_user_id` (`user_id`) USING BTREE,
  KEY `idx_user_earn_log_user_id_earn_date` (`user_id`,`earn_date`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收益记录';

-- ----------------------------
-- Records of user_earn_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_freeze`
-- ----------------------------
DROP TABLE IF EXISTS `user_freeze`;
CREATE TABLE `user_freeze` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `operation` varchar(20) DEFAULT NULL COMMENT '用户可冻结功能标识：充值recharge提现cash投资invest变现realize债权转让bond借款loan',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`),
  KEY `idx_user_freeze_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户冻结功能信息表';

-- ----------------------------
-- Records of user_freeze
-- ----------------------------

-- ----------------------------
-- Table structure for `user_gift`
-- ----------------------------
DROP TABLE IF EXISTS `user_gift`;
CREATE TABLE `user_gift` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `gift_name` varchar(30) DEFAULT NULL COMMENT '礼包名称',
  `gift_type` char(1) DEFAULT NULL COMMENT '礼包类型 （0生日礼包 1专享礼包）',
  `redenvelope_rule_id` varchar(32) DEFAULT NULL COMMENT '红包发放规则id',
  `rate_coupon_rule_id` varchar(32) DEFAULT NULL COMMENT '加息券发放规则id',
  `remark` varchar(50) DEFAULT NULL COMMENT '礼包详情',
  `create_time` datetime DEFAULT NULL COMMENT '添加日期',
  `update_time` datetime DEFAULT NULL COMMENT '最后修改日期',
  `delete_flag` char(1) DEFAULT NULL COMMENT '逻辑删除标识 0正常  1删除',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户礼包表';

-- ----------------------------
-- Records of user_gift
-- ----------------------------

-- ----------------------------
-- Table structure for `user_identify`
-- ----------------------------
DROP TABLE IF EXISTS `user_identify`;
CREATE TABLE `user_identify` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `real_name_status` char(1) DEFAULT '0' COMMENT '实名认证 0:未认证 1：实名认证通过 -1：实名认证未通过',
  `real_name_verify_time` datetime DEFAULT NULL COMMENT '实名认证时间',
  `email_status` char(1) DEFAULT '0' COMMENT '邮箱认证 0:未认证 1：邮箱认证通过',
  `email_verify_time` datetime DEFAULT NULL COMMENT '邮箱认证时间',
  `mobile_phone_status` char(1) DEFAULT '0' COMMENT '手机认证 -1:未通过,0:未认证,1:已认证',
  `mobile_phone_verify_time` datetime DEFAULT NULL COMMENT '手机认证时间',
  `auth_sign_status` char(1) DEFAULT '0' COMMENT '业务授权状态（0.未授权 1.已授权，默认0）',
  `auth_sign_verify_time` datetime DEFAULT NULL COMMENT '业务授权时间',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_identify_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户认证信息';

-- ----------------------------
-- Records of user_identify
-- ----------------------------

-- ----------------------------
-- Table structure for `user_invest_auto_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_invest_auto_log`;
CREATE TABLE `user_invest_auto_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目id',
  `invest_money` decimal(20,4) DEFAULT NULL COMMENT '投资金额',
  `status` char(1) DEFAULT NULL COMMENT '状态 0处理中. 1 成功 2 失败 ',
  `invest_time` datetime DEFAULT NULL COMMENT '投资时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '结果备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自动投资记录表';

-- ----------------------------
-- Records of user_invest_auto_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_invite`
-- ----------------------------
DROP TABLE IF EXISTS `user_invite`;
CREATE TABLE `user_invite` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `invite_time` datetime DEFAULT NULL COMMENT '邀请时间',
  `invite_user_id` varchar(32) DEFAULT NULL COMMENT '邀请人',
  `invitee_user_id` varchar(32) DEFAULT NULL COMMENT '被邀请人',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='邀请好友关联表';

-- ----------------------------
-- Records of user_invite
-- ----------------------------

-- ----------------------------
-- Table structure for `user_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `login_time` datetime DEFAULT NULL,
  `login_ip` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `idx_user_login_log_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录记录表';

-- ----------------------------
-- Records of user_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_opinion`
-- ----------------------------
DROP TABLE IF EXISTS `user_opinion`;
CREATE TABLE `user_opinion` (
  `UUID` varchar(32) NOT NULL COMMENT '主键',
  `title` varchar(64) DEFAULT NULL COMMENT '标题',
  `attachment_path` varchar(512) DEFAULT NULL COMMENT '附件路径',
  `user_id` varchar(32) DEFAULT NULL COMMENT '反馈人',
  `remark` varchar(512) DEFAULT NULL COMMENT '反馈内容',
  `create_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`UUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户意见反馈表';

-- ----------------------------
-- Records of user_opinion
-- ----------------------------

-- ----------------------------
-- Table structure for `user_qualification_apply`
-- ----------------------------
DROP TABLE IF EXISTS `user_qualification_apply`;
CREATE TABLE `user_qualification_apply` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `qualification_type` varchar(32) DEFAULT NULL COMMENT '资质类型',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `verify_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `verify_user` varchar(32) DEFAULT NULL COMMENT '审核人',
  `status` char(1) DEFAULT NULL COMMENT '申请状态(待审核 0 ，审核成功 1 ，审核失败2 ，默认 0 )',
  `verify_time` datetime DEFAULT NULL COMMENT '审核时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '申请IP',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款资质审核申请';

-- ----------------------------
-- Records of user_qualification_apply
-- ----------------------------

-- ----------------------------
-- Table structure for `user_qualification_upload`
-- ----------------------------
DROP TABLE IF EXISTS `user_qualification_upload`;
CREATE TABLE `user_qualification_upload` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `qualification_apply_id` varchar(32) NOT NULL COMMENT '审核申请ID',
  `file_path` varchar(50) DEFAULT NULL COMMENT '文件路径',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `delete_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款资质文件';

-- ----------------------------
-- Records of user_qualification_upload
-- ----------------------------

-- ----------------------------
-- Table structure for `user_rate_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `user_rate_coupon`;
CREATE TABLE `user_rate_coupon` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户id值',
  `rule_id` varchar(32) DEFAULT NULL COMMENT '加息券规则uuid',
  `rule_name` varchar(20) NOT NULL COMMENT '加息券规则名称',
  `activity_name` varchar(20) DEFAULT NULL COMMENT '活动名称',
  `up_apr` decimal(20,4) NOT NULL COMMENT '加息利率',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `create_time` datetime DEFAULT NULL COMMENT '发放时间',
  `tender_id` varchar(32) DEFAULT NULL COMMENT '投资记录id值',
  `status` char(1) DEFAULT NULL COMMENT '加息券状态：0未使用,1已使用，2已过期 3已作废',
  `use_valid_day` varchar(11) DEFAULT NULL COMMENT '有效天数',
  `use_tender_money` decimal(20,4) DEFAULT NULL COMMENT '最低投资可用金额',
  `use_project_type` text COMMENT '可使用项目类型',
  `use_expire_time` datetime DEFAULT NULL COMMENT '使用到期时间',
  `grant_type` char(1) NOT NULL COMMENT '加息券发放方式：1 固定金额、2金额满返',
  PRIMARY KEY (`uuid`),
  KEY `idx_user_rate_coupon_user_id_status` (`user_id`,`status`) USING BTREE,
  KEY `idx_user_rate_coupon_use_expire_time_status` (`use_expire_time`,`status`) USING BTREE,
  KEY `idx_user_rate_coupon_tender_id` (`tender_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户加息券记录表';

-- ----------------------------
-- Records of user_rate_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for `user_redenvelope`
-- ----------------------------
DROP TABLE IF EXISTS `user_redenvelope`;
CREATE TABLE `user_redenvelope` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户id值',
  `rule_id` varchar(32) DEFAULT NULL COMMENT '红包规则uuid',
  `rule_name` varchar(20) NOT NULL COMMENT '红包规则名称',
  `activity_name` varchar(20) DEFAULT NULL COMMENT '活动名称',
  `amount` decimal(20,4) NOT NULL COMMENT '红包金额',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `create_time` datetime DEFAULT NULL COMMENT '发放时间',
  `tender_id` varchar(32) DEFAULT NULL COMMENT '投资记录ID',
  `status` char(1) DEFAULT NULL COMMENT '红包状态：0未使用,1已使用，2已过期 3已作废',
  `use_valid_day` varchar(11) DEFAULT NULL COMMENT '有效天数',
  `use_tender_money` decimal(20,4) DEFAULT NULL COMMENT '最低投资可用金额',
  `use_project_type` text COMMENT '可使用项目类型',
  `use_expire_time` datetime DEFAULT NULL COMMENT '使用到期时间',
  `grant_type` char(1) NOT NULL COMMENT '红包发放方式：1 固定金额、2固定比例、3金额满返、4比例满返',
  PRIMARY KEY (`uuid`),
  KEY `idx_user_redenvelope_tender_id` (`tender_id`) USING BTREE,
  KEY `idx_user_redenvelope_user_id_status` (`user_id`,`status`) USING BTREE,
  KEY `idx_user_redenvelope_use_expire_time_status` (`use_expire_time`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户获取红包实体表';

-- ----------------------------
-- Records of user_redenvelope
-- ----------------------------

-- ----------------------------
-- Table structure for `user_score`
-- ----------------------------
DROP TABLE IF EXISTS `user_score`;
CREATE TABLE `user_score` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `total_score` int(10) DEFAULT NULL COMMENT '总积分',
  `use_score` int(10) DEFAULT NULL COMMENT '可用积分',
  `no_use_score` int(10) DEFAULT NULL COMMENT '冻结积分',
  `expense_score` int(10) DEFAULT NULL COMMENT '消费积分',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_user_socre_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户积分表';

-- ----------------------------
-- Records of user_score
-- ----------------------------

-- ----------------------------
-- Table structure for `user_score_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_score_log`;
CREATE TABLE `user_score_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `total_score` int(10) DEFAULT NULL COMMENT '总积分',
  `use_score` int(10) DEFAULT NULL COMMENT '可用积分',
  `opt_value` int(10) DEFAULT NULL COMMENT '操作积分',
  `no_use_score` int(10) DEFAULT NULL COMMENT '冻结积分',
  `opt_type` char(1) DEFAULT NULL COMMENT '操作类型（0减少，1增加）',
  `type_name` varchar(30) DEFAULT NULL COMMENT '积分类型名称',
  `type_code` varchar(32) DEFAULT NULL,
  `remark` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`uuid`),
  KEY `idx_user_score_log_user_id_type_code` (`user_id`,`type_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户积分记录';

-- ----------------------------
-- Records of user_score_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_security_answer`
-- ----------------------------
DROP TABLE IF EXISTS `user_security_answer`;
CREATE TABLE `user_security_answer` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `question_flag` varchar(30) DEFAULT NULL COMMENT '问题标识（关联字典数据user_security_question）',
  `answer` varchar(64) DEFAULT NULL COMMENT '答案',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `add_ip` varchar(32) DEFAULT NULL COMMENT '添加IP',
  `delete_flag` char(1) DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户密保答案';

-- ----------------------------
-- Records of user_security_answer
-- ----------------------------

-- ----------------------------
-- Table structure for `user_vip`
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户的uuid',
  `growth_value` decimal(20,2) DEFAULT NULL COMMENT '成长值',
  `vip_level` char(2) DEFAULT NULL COMMENT 'Vip等级 1,2,3等',
  `up_apr` decimal(10,2) DEFAULT NULL COMMENT '额外收益率',
  `birthday_gift_status` char(1) DEFAULT NULL COMMENT '生日礼包状态（0未领取，1已经领取，3已经作废）',
  `exclusive_gift_status` char(1) DEFAULT NULL COMMENT '专享礼包状态（0未领取，1已经领取，3已经作废）',
  `create_time` datetime DEFAULT NULL COMMENT '获取vip等级时间',
  `update_time` datetime DEFAULT NULL COMMENT 'vip状态更新时间',
  `vip_level_rule` text COMMENT 'vip生成/更新时的vip等级json字符串',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uq_user_vip_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户VIP等级';

-- ----------------------------
-- Records of user_vip
-- ----------------------------

-- ----------------------------
-- Table structure for `user_vip_growth_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_vip_growth_log`;
CREATE TABLE `user_vip_growth_log` (
  `uuid` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `growth_value` decimal(20,2) DEFAULT NULL COMMENT '原来成长值',
  `opt_type` char(1) DEFAULT NULL COMMENT '操作类型（0减少，1增加）',
  `growth_value_new` decimal(20,2) DEFAULT NULL COMMENT '操作后的成长值',
  `opt_value` decimal(20,2) DEFAULT NULL COMMENT '本次操作成长值',
  `remark` varchar(512) DEFAULT NULL COMMENT '操作备注',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='vip成长记录表';

-- ----------------------------
-- Records of user_vip_growth_log
-- ----------------------------

-- ----------------------------
-- Table structure for `user_vip_level`
-- ----------------------------
DROP TABLE IF EXISTS `user_vip_level`;
CREATE TABLE `user_vip_level` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `growth_value` decimal(20,2) DEFAULT NULL COMMENT '所需成长值',
  `growth_limit_value` decimal(20,2) DEFAULT NULL COMMENT '成长封顶值',
  `vip_level` char(2) DEFAULT '' COMMENT 'vip等级 每次新增+1 ',
  `year_deduction` decimal(20,2) DEFAULT NULL COMMENT '年扣除成长值',
  `up_apr` decimal(10,2) DEFAULT NULL COMMENT '额外收益率',
  `redenvelope_rule_id` varchar(32) DEFAULT NULL COMMENT '红包发放规则id',
  `rate_coupon_rule_id` varchar(32) DEFAULT NULL COMMENT '加息前发放规则id',
  `birthday_gift_id` varchar(32) DEFAULT NULL COMMENT '生日礼包id',
  `exclusive_gift_id` varchar(32) DEFAULT NULL COMMENT '专享礼包id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` char(1) DEFAULT NULL COMMENT '逻辑删除标识 0正常  1删除',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='vip等级配置表';

-- ----------------------------
-- Records of user_vip_level
-- ----------------------------


-- ----------------------------
-- 添加FTP配置参数
-- ----------------------------
insert into sys_config(uuid,code,config_name,config_value,config_type,create_time,status,remark,edit_enable) values
(replace(uuid(),'-',''),'ftp_host','ftp主机ip','221.239.93.145','2',now(),'1','ftp主机ip','0'),
(replace(uuid(),'-',''),'ftp_port','ftp port','21','2',now(),'1','ftp port','0'),
(replace(uuid(),'-',''),'ftp_username','ftp用户名','rongdu','2',now(),'1','ftp用户名','0'),
(replace(uuid(),'-',''),'ftp_password','ftp密码','rongdu','2',now(),'1','ftp密码','0');


DROP TABLE IF EXISTS `tpp_cbhb_request`;
CREATE TABLE `tpp_cbhb_request` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `request_type` varchar(50) NOT NULL COMMENT '请求类型',
  `request_merbillno` varchar(50) NOT NULL COMMENT '请求流水号',
  `request_map` text NOT NULL COMMENT '请求数据包（包含明文、密文、证书信息）',
  `request_file_url` varchar(512) NOT NULL COMMENT '请求文件的路径',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渤海银行请求记录表';

DROP TABLE IF EXISTS `tpp_cbhb_response`;
CREATE TABLE `tpp_cbhb_response` (
  `uuid` varchar(32) NOT NULL,
  `resp_type` varchar(50) NOT NULL COMMENT '响应类型',
  `request_merbillno` varchar(50) NOT NULL COMMENT '请求流水号',
  `resp_transid` varchar(50) NOT NULL COMMENT '回调响应流水号',
  `resp_map` text NOT NULL COMMENT '回调响应数据包（包含明文、密文、证书）',
  `resp_file_url` varchar(512) NOT NULL COMMENT '回调响应文件地址',
  `resp_code` varchar(50) NOT NULL COMMENT '实时回调码',
  `resp_desc` varchar(128) NOT NULL COMMENT '实时回调码解析',
  `resp_time` datetime NOT NULL COMMENT '实时响应时间',
  `return_resp_code` varchar(50) NOT NULL COMMENT '同步回调码',
  `return_resp_desc` varchar(128) NOT NULL COMMENT '同步回调码对应解析',
  `return_resp_time` datetime NOT NULL COMMENT '同步响应时间',
  `notify_resp_code` varchar(50) NOT NULL COMMENT '异步回调码',
  `notify_resp_desc` varchar(128) NOT NULL COMMENT '异步回调码解析',
  `notify_resp_time` datetime NOT NULL COMMENT '异步响应时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渤海银行响应记录表';

DROP TABLE IF EXISTS account_bank_card;
CREATE TABLE `account_bank_card` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID' ,
`bank_code`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行代码' ,
`bank_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称' ,
`bank_account`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行账号' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`create_ip`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加时候的IP' ,
`update_ip`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时候的ip' ,
`status`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '银行卡状态，0:未启用，1：启用' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行卡表';

CREATE TABLE `receiving_info` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID' ,
`address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址' ,
`province`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省' ,
`city`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市' ,
`area`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区' ,
`is_defult`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否默认收货信息' ,
`mobile`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人手机' ,
`name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收件人姓名' ,
`postal_code`  varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮政编码' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '添加时间' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收货地址记录表';


CREATE TABLE `score_goods` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`goods_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称' ,
`goods_category_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类ID' ,
`exchange_limit`  int(11) NULL DEFAULT NULL COMMENT '限购数量' ,
`status`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品状态  0 待审核 1 审核成功   2 审核失败  3 已上架   4 已下架' ,
`sale_time`  datetime NULL DEFAULT NULL COMMENT '上架时间' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '添加时间' ,
`create_operator_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加者' ,
`update_time`  datetime NULL DEFAULT NULL COMMENT '修改时间' ,
`update_operator_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '添加者' ,
`cost_price`  decimal(20,4) NULL DEFAULT NULL COMMENT '采购价格' ,
`market_price`  decimal(20,4) NULL DEFAULT NULL COMMENT '市场价' ,
`score`  int(11) NULL DEFAULT NULL COMMENT '所需积分' ,
`content`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品详情' ,
`goods_attribute`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品属性信息' ,
`is_virtual`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否虚拟商品 1 是 0 否 默认0' ,
`remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`verify_operator_Id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人ID' ,
`verify_remark`  varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核备注' ,
`verify_time`  datetime NULL DEFAULT NULL COMMENT '审核时间' ,
`recommend`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否推荐' ,
`rule_id`  char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '虚拟物品所对应的规则Id' ,
`virtual_type`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '虚拟类型：0 红包 1加息卷' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分商品表';

CREATE TABLE `score_goods_category` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`goods_category_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品分类名称' ,
`intro_pic`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类图片路径' ,
`sort`  tinyint(4) NULL DEFAULT NULL COMMENT '排序' ,
`remark`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '添加时间' ,
`operator`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员' ,
`delete_flag`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否删除 1 是 0否，默认0' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分商品分类表';

CREATE TABLE `score_goods_order` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id' ,
`user_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名' ,
`goods_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品ID' ,
`goods_name`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称' ,
`num`  int(11) NOT NULL DEFAULT 1 COMMENT '兑换数量' ,
`score`  int(11) NULL DEFAULT NULL COMMENT '使用积分值' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '兑换时间' ,
`deliver_time`  datetime NULL DEFAULT NULL COMMENT '发货时间' ,
`deliver_user`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送人' ,
`express_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流名称' ,
`express_no`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号' ,
`money`  decimal(20,4) NULL DEFAULT NULL COMMENT '费用' ,
`receive_address`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址' ,
`receive_phone`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话' ,
`receive_user_name`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名' ,
`receive_remark`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '兑换留言' ,
`status`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0 新建 1审批通过  2 审核失败 3 已发货 4 已收货 5结束' ,
`verify_time`  datetime NULL DEFAULT NULL COMMENT '审批时间' ,
`verify_user_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批人ID' ,
`verify_user`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批人用户名' ,
`verify_remark`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批备注' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分商品订单表';

CREATE TABLE `score_goods_pic` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`goods_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品ID' ,
`pic_type`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片类型 1 缩略图 2  实体照片' ,
`pic_path`  varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片路径' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '添加时间' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分商品图片表';

CREATE TABLE `score_goods_store` (
`uuid`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键' ,
`goods_id`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品ID' ,
`store`  int(11) NULL DEFAULT 0 COMMENT '商品库存' ,
`freeze_store`  int(11) NULL DEFAULT 0 COMMENT '冻结库存' ,
`sold_amount`  int(11) NULL DEFAULT 0 COMMENT '销售数量' ,
PRIMARY KEY (`uuid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分商品库存表';

-- ----------------------------
-- 统计分析
-- ----------------------------

-- ----------------------------
-- Table structure for `statistic_borrow`
-- ----------------------------
DROP TABLE IF EXISTS `statistic_borrow`;
CREATE TABLE `statistic_borrow` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `project_id` varchar(32) DEFAULT NULL COMMENT '项目ID',
  `borrow_date` date NOT NULL COMMENT '借款日期',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `is_mortgage` char(1) DEFAULT '0' COMMENT '是否抵押',
  `is_vouch` char(1) DEFAULT '0' COMMENT '是否担保',
  `account` decimal(20,4) DEFAULT '0.0000' COMMENT '借款金额',
  `time_limit` int(8) DEFAULT '0' COMMENT '借款期限',
  `borrow_nature` char(1) DEFAULT '1' COMMENT '借款性质',
  `area` varchar(32) DEFAULT '' COMMENT '地区',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='借款统计';


-- ----------------------------
-- Table structure for `statistic_overdue`
-- ----------------------------
DROP TABLE IF EXISTS `statistic_overdue`;
CREATE TABLE `statistic_overdue` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `record_date` date DEFAULT NULL COMMENT '记录日期',
  `stat_type` char(1) DEFAULT NULL COMMENT '统计类型：1-人数，2-金额，3-笔数',
  `total_count` decimal(20,4) DEFAULT '0.0000' COMMENT '总数',
  `first_series` decimal(20,4) DEFAULT '0.0000' COMMENT '第一级 90天以下',
  `second_series` decimal(20,4) DEFAULT '0.0000' COMMENT '第二级 90-180',
  `third_series` decimal(20,4) DEFAULT '0.0000' COMMENT '第三级 180天以上',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='投资分析';

-- ----------------------------
-- Table structure for `statistic_repay`
-- ----------------------------
DROP TABLE IF EXISTS `statistic_repay`;
CREATE TABLE `statistic_repay` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `record_date` date NOT NULL COMMENT '记录日期',
  `repay_status` char(1) DEFAULT '0' COMMENT '还款状态 0 未还 1 已还',
  `borrow_type` varchar(8) DEFAULT '' COMMENT '借款类型',
  `borrow_nature` char(1) DEFAULT '0' COMMENT '借款性质',
  `payment` decimal(20,4) DEFAULT '0.0000' COMMENT '总额',
  `capital` decimal(20,4) DEFAULT '0.0000' COMMENT '本金',
  `interest` decimal(20,4) DEFAULT '0.0000' COMMENT '利息',
  `repay_count` bigint(8) DEFAULT '0' COMMENT '笔数',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款统计';


-- ----------------------------
-- Table structure for `statistic_repay_info`
-- ----------------------------
DROP TABLE IF EXISTS `statistic_repay_info`;
CREATE TABLE `statistic_repay_info` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `record_date` date NOT NULL COMMENT '记录日期',
  `wait_total` decimal(20,4) DEFAULT '0.0000' COMMENT '待还总额',
  `biggest_one` decimal(20,4) DEFAULT '0.0000' COMMENT '最大单户借款余额',
  `biggest_one_ratio` decimal(20,4) DEFAULT '0.0000' COMMENT '最大单户余额占比',
  `biggest_ten` decimal(20,4) DEFAULT '0.0000' COMMENT '最大十户借款余额',
  `biggest_ten_ratio` decimal(20,4) DEFAULT '0.0000' COMMENT '最大十户余额占比',
  `overdue_num` decimal(20,0) DEFAULT '0' COMMENT '逾期项目数',
  `overdue_ratio` decimal(20,4) DEFAULT '0.0000' COMMENT '逾期率',
  `overdue_account` decimal(20,4) DEFAULT '0.0000' COMMENT '逾期金额',
  `overdue_account_ratio` decimal(20,4) DEFAULT '0.0000' COMMENT '逾期金额比率',
  `old_overdue_account` decimal(20,4) DEFAULT '0.0000' COMMENT '历史项目逾期金额',
  `old_overdue_ratio` decimal(20,4) DEFAULT '0.0000' COMMENT '历史项目逾期金额比率',
  `commutation_num` decimal(20,0) DEFAULT '0' COMMENT '代偿笔数',
  `commutation_account` decimal(20,4) DEFAULT '0.0000' COMMENT '代偿金额',
  `overdue_borrower` decimal(20,0) DEFAULT '0' COMMENT '逾期人数',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='还款详情统计';

-- ----------------------------
-- Table structure for `statistic_user`
-- ----------------------------
DROP TABLE IF EXISTS `statistic_user`;
CREATE TABLE `statistic_user` (
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `regist_date` date NOT NULL COMMENT '注册日期',
  `real_name_date` date DEFAULT NULL COMMENT '认证日期',
  `user_nature` char(1) DEFAULT NULL COMMENT '用户类型',
  `regist_channel` char(1) DEFAULT NULL COMMENT '注册渠道',
  `sex` char(1) DEFAULT NULL COMMENT '性别',
  `year` int(4) DEFAULT '1990' COMMENT '生日年份',
  `real_name_status` char(1) DEFAULT NULL,
  `area` varchar(10) DEFAULT NULL,
  `is_investor` char(1) DEFAULT '0' COMMENT '是否为投资人 0 普通用户 1 投资人 ',
  `is_borrower` char(1) DEFAULT '0' COMMENT '是否为借款人 0 普通用户 1 借款人',
  `education` char(2) DEFAULT NULL COMMENT '学历',
  `marital_status` char(1) DEFAULT NULL COMMENT '婚姻状况 0:未婚；1：已婚；2：离异；3：丧偶',
  `month_income_range` varchar(20) DEFAULT NULL COMMENT '收入范围',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户统计';

CREATE TABLE `statistic_account` (
	`uuid` VARCHAR(32) NOT NULL COMMENT '主键',
	`stat_date` DATE NULL DEFAULT NULL COMMENT '统计日期',
	`use_money` DECIMAL(32,4) NULL DEFAULT '0.0000' COMMENT '可用余额',
	`relative_ratio` VARCHAR(10) NULL DEFAULT NULL COMMENT '环比值',
	PRIMARY KEY (`uuid`)
)
COMMENT='账户统计'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `statistic_bad_debt` (
	`uuid` VARCHAR(32) NOT NULL COMMENT '主键',
	`stat_date` DATE NULL DEFAULT NULL COMMENT '代收日期',
	`project_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '代收订单ID',
	`amount` DECIMAL(20,4) NULL DEFAULT '0.0000' COMMENT '代收金额',
	`is_vouch` CHAR(1) NULL DEFAULT NULL COMMENT '是否担保借款，1-是，0-否',
	`is_mortgage` CHAR(1) NULL DEFAULT NULL COMMENT '是否抵押借款，1-是，0-否',
	`is_realize` CHAR(1) NULL DEFAULT NULL COMMENT '是否变现借款，1-是，0-否',
	PRIMARY KEY (`uuid`)
)
COMMENT='坏账分析'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `statistic_bond` (
	`uuid` VARCHAR(32) NOT NULL COMMENT '主键',
	`user_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '用户ID',
	`bond_date` DATE NULL DEFAULT NULL COMMENT '转让日期',
	`bond_invest_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '转让投资订单ID',
	`bond_amount` DECIMAL(20,4) NULL DEFAULT '0.0000' COMMENT '转让金额',
	`bond_time_limit` INT(8) NULL DEFAULT NULL COMMENT '债权期限，以天为单位，1个月30天，1年365天',
	`is_vouch` CHAR(1) NULL DEFAULT NULL COMMENT '是否担保，1-是，0-否',
	`is_mortgage` CHAR(1) NULL DEFAULT NULL COMMENT '是否抵押，1-是，0-否',
	PRIMARY KEY (`uuid`)
)
COMMENT='债权转让分析'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `statistic_earn` (
	`uuid` VARCHAR(32) NOT NULL COMMENT '主键',
	`user_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '用户ID',
	`stat_date` DATE NULL DEFAULT NULL COMMENT '统计日期',
	`amount` DECIMAL(20,4) NULL DEFAULT '0.0000' COMMENT '金额',
	`account_type` VARCHAR(32) NULL DEFAULT NULL COMMENT '资金类型',
	PRIMARY KEY (`uuid`)
)
COMMENT='投资收益分析'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `statistic_invest` (
	`uuid` VARCHAR(32) NOT NULL COMMENT '主键',
	`invest_date` DATE NULL DEFAULT NULL COMMENT '投资日期',
	`invest_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '投资ID',
	`invest_amount` DECIMAL(20,4) NULL DEFAULT '0.0000' COMMENT '投标金额',
	`invest_area` VARCHAR(15) NULL DEFAULT NULL COMMENT '投资地址',
	`invest_channel` CHAR(1) NULL DEFAULT NULL COMMENT '投资渠道 1 PC  2 APP 3 微信 ',
	`project_type` VARCHAR(32) NULL DEFAULT NULL COMMENT '产品类型标识： BX-变现理财，ZQ-债权转让、其它理财类型以project_type表nid为准',
	`project_apr` DECIMAL(8,4) NULL DEFAULT '0.0000' COMMENT '年利率',
	`project_time_limit` INT(8) NULL DEFAULT NULL COMMENT '借款期限，以天为单位，1个月30天，1年365天',
	`user_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '用户ID',
	`user_nature` CHAR(1) NULL DEFAULT NULL COMMENT '用户类型:  1个人用户、2 企业用户',
	`real_amount` DECIMAL(20,4) NULL DEFAULT '0.0000' COMMENT '实付金额=投资金额-红包金额',
	`is_use_red` CHAR(1) NULL DEFAULT '0' COMMENT '是否使用了红包，0-未使用，1-已使用',
	`is_use_rate` CHAR(1) NULL DEFAULT '0' COMMENT '是否使用了加息券，0-未使用，1-已使用',
	`is_add_apr` CHAR(1) NULL DEFAULT '0' COMMENT '产品是否加息，0-未加息，1-已加息',
	PRIMARY KEY (`uuid`)
)
COMMENT='投资分析'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;


-- ----------------------------
-- 统计-新增菜单
-- ----------------------------
INSERT INTO sys_menu values('d8d3e092a09e4c36abde2260f1660a48', 'statistic', '统计', '', NULL, NULL, NULL, 8, NULL, '0', NULL, '0');
INSERT INTO sys_menu values('3ce49a9c2db546b4b0dcb0fab9920640', 'statistic:account', '资金分析', '#', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 5, NULL, '0', '', '0');
INSERT INTO sys_menu values('ca155fe4600248b4a9eb0b1839c95bc3', 'statistic:account:circulate', '出入金分析', '/statistic/account/accountCirculateManage.html', '3ce49a9c2db546b4b0dcb0fab9920640', 'd8d3e092a09e4c36abde2260f1660a48:3ce49a9c2db546b4b0dcb0fab9920640', '', 1, NULL, '0', '', '1');
INSERT INTO sys_menu values('4f7ce05aa09346e18a7c4dd314f83c29', 'statistic:account:investEarn', '投资收益分析', '/statistic/account/investEarnManage.html', '3ce49a9c2db546b4b0dcb0fab9920640', 'd8d3e092a09e4c36abde2260f1660a48:3ce49a9c2db546b4b0dcb0fab9920640', '', 3, NULL, '0', '', '1');
INSERT INTO sys_menu values('c00a85642d504665ab2c36e11fb2acd8', 'statistic:account:platProfit', '平台盈亏分析', '/statistic/account/platProfitManage.html', '3ce49a9c2db546b4b0dcb0fab9920640', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 4, NULL, '0', '', '1');
INSERT INTO sys_menu values('a1eca58c1eca474cba6fdd56e2053381', 'statistic:account:useMoney', '用户余额分析', '/statistic/account/userUseMoneyManage.html', '3ce49a9c2db546b4b0dcb0fab9920640', 'd8d3e092a09e4c36abde2260f1660a48:3ce49a9c2db546b4b0dcb0fab9920640', '', 2, NULL, '0', '', '1');
INSERT INTO sys_menu values('0d0e71628cce4c83a2c05095914b7005', 'statistic:activity', '活动运营分析', '/statistic/activity/activityOperManage.html', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 6, NULL, '0', '', '1');
INSERT INTO sys_menu values('46bfc4625f1b4d9ba103df86813f914e', 'statistic:bond', '债权转让分析', '/statistic/bond/bondInvestManage.html', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 4, NULL, '0', '', '1');
INSERT INTO sys_menu values('bc219fea63ef4beb81c84b76ea8cbcb6', 'statistic:borrow', '借款分析', '#', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 2, NULL, '0', '', '0');
INSERT INTO sys_menu values('849d37e47f574d49b6fbcd0b171cdea9', 'statistic:borrow:borrowAccount', '借款金额分析', '/statistic/borrow/borrowAccountManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 2, NULL, '0', '', '0');
INSERT INTO sys_menu values('49e81e74b9864b5cab6967b8ace5ded2', 'statistic:borrow:borrowArea', '借款地区分析', '/statistic/borrow/borrowAreaManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 4, NULL, '0', '', '0');
INSERT INTO sys_menu values('077b112b0bbd47f2ba9813b530eda275', 'statistic:borrow:borrowerNum', '借款人数分析', '/statistic/borrow/borrowerNumManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 1, NULL, '0', '', '1');
INSERT INTO sys_menu values('bcb09c3e9d9945219f47ceb98c220542', 'statistic:borrow:borrowInfo', '借款情况分析', '/statistic/borrow/borrowInfoManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 5, NULL, '0', '', '0');
INSERT INTO sys_menu values('a02387d015ff48bba67c1d981cc03d97', 'statistic:borrow:borrowNum', '借款笔数分析', '/statistic/borrow/borrowNumManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 3, NULL, '0', '', '0');
INSERT INTO sys_menu values('d9f63eed161c42dd8cc98b0d81fa8c01', 'statistic:borrow:collection', '待还款分析', '/statistic/borrow/borrowNotRepayManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 6, NULL, '0', '', '0');
INSERT INTO sys_menu values('f97a03ae28a241c5adf40fd404493743', 'statistic:borrow:overdue', '逾期分析', '/statistic/borrow/borrowOverdueManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 8, NULL, '0', '', '0');
INSERT INTO sys_menu values('845dbe5a43ef44c0bdd1476d3962baad', 'statistic:borrow:repay', '正常还款分析', '/statistic/borrow/borrowRepaidManage.html', 'bc219fea63ef4beb81c84b76ea8cbcb6', 'd8d3e092a09e4c36abde2260f1660a48:bc219fea63ef4beb81c84b76ea8cbcb6', '', 7, NULL, '0', '', '0');
INSERT INTO sys_menu values('f0cf1d906c474e4ab5d31eee923a961f', 'statistic:invest', '投资分析', '#', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 3, NULL, '0', '', '0');
INSERT INTO sys_menu values('62bd0ed157a24dccb614525cae2aef73', 'statistic:invest:amount', '投资金额分析', '/statistic/invest/investAmntManage.html', 'f0cf1d906c474e4ab5d31eee923a961f', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 2, NULL, '0', '', '1');
INSERT INTO sys_menu values('319bf128233e4e7baad711d3e02e02d0', 'statistic:invest:area', '投资地区分析', '/statistic/invest/investAreaManage.html', 'f0cf1d906c474e4ab5d31eee923a961f', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 4, NULL, '0', '', '1');
INSERT INTO sys_menu values('56fab97f48fb43ad972f75aad9f87e75', 'statistic:invest:condition', '投资情况分析', '/statistic/invest/investConditionManage.html', 'f0cf1d906c474e4ab5d31eee923a961f', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 5, NULL, '0', '', '1');
INSERT INTO sys_menu values('dbc97e8de83c4985882f0a22268ce0fe', 'statistic:invest:customers', '投资人数分析', '/statistic/invest/investCustomersManage.html', 'f0cf1d906c474e4ab5d31eee923a961f', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 1, NULL, '0', '', '1');
INSERT INTO sys_menu values('56e6ae1af42741e39b97d1b9c9144f27', 'statistic:invest:times', '投资笔数分析', '/statistic/invest/investNumManage.html', 'f0cf1d906c474e4ab5d31eee923a961f', 'd8d3e092a09e4c36abde2260f1660a48:f0cf1d906c474e4ab5d31eee923a961f', '', 3, NULL, '0', '', '1');
INSERT INTO sys_menu values('0e150c63266d4338b5f221bad13f47e5', 'statistic:user', '用户分析', '#', 'd8d3e092a09e4c36abde2260f1660a48', 'd8d3e092a09e4c36abde2260f1660a48', '', 1, NULL, '0', '', '0');

-- ----------------------------
-- 统计-新增权限
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0001', 'statistic:invest:customers:export', '投资人数分析-导出', 'dbc97e8de83c4985882f0a22268ce0fe', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0002', 'statistic:invest:amount:export', '投资金额分析-导出', '62bd0ed157a24dccb614525cae2aef73', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0003', 'statistic:invest:times:export', '投资笔数分析-导出', '56e6ae1af42741e39b97d1b9c9144f27', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0004', 'statistic:borrow:borrowArea:export', '投资地区分析-导出', '319bf128233e4e7baad711d3e02e02d0', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0005', 'statistic:borrow:borrowerNum:export', '借款人数分析-导出', '077b112b0bbd47f2ba9813b530eda275', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0006', 'statistic:borrow:borrowAccount:export', '借款金额分析-导出', '849d37e47f574d49b6fbcd0b171cdea9', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0007', 'statistic:borrow:borrowNum:export', '借款笔数分析-导出', 'a02387d015ff48bba67c1d981cc03d97', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0008', 'statistic:borrow:area:export', '借款地区分析-导出', '49e81e74b9864b5cab6967b8ace5ded2', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('226a55c07ff811e68fb9000c293c0011', 'statistic:account:platProfit:export', '平台收益盈亏分析-导出', 'c00a85642d504665ab2c36e11fb2acd8', 'ec40101ca17444a29bb1247175452124');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c25678d31', 'statistic:invest:amount:query', '投资金额分析', '62bd0ed157a24dccb614525cae2aef73', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c25678d32', 'statistic:invest:area:query', '投资地区分析', '319bf128233e4e7baad711d3e02e02d0', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c25678d33', 'statistic:invest:condition:query', '投资情况分析', '56fab97f48fb43ad972f75aad9f87e75', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c25678d34', 'statistic:invest:times:query', '投资笔数分析', '56e6ae1af42741e39b97d1b9c9144f27', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d11', 'statistic:user:userStatistic:query', '注册认证用户统计', '61225cc0a22e4dfa8e01f5f6f913bde2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d12', 'statistic:user:userActive:query', '活跃用户分析', '87c418076f9a4b758cb799fbdbe3869c', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d13', 'statistic:user:userPortrait:query', '用户画像', '65b7cb9b030e43f4b2fc396f9a55d87d', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d21', 'statistic:borrow:borrowerNum:query', '借款人数分析', '077b112b0bbd47f2ba9813b530eda275', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d22', 'statistic:borrow:borrowAccount:query', '借款金额分析', '849d37e47f574d49b6fbcd0b171cdea9', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d23', 'statistic:borrow:borrowNum:query', '借款笔数分析', 'a02387d015ff48bba67c1d981cc03d97', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d24', 'statistic:borrow:borrowArea:query', '借款地区分析', '49e81e74b9864b5cab6967b8ace5ded2', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d25', 'statistic:borrow:borrowInfo:query', '借款情况分析', 'bcb09c3e9d9945219f47ceb98c220542', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d26', 'statistic:borrow:collection:query', '待还款分析', 'd9f63eed161c42dd8cc98b0d81fa8c01', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d27', 'statistic:borrow:repay:query', '正常还款分析', '845dbe5a43ef44c0bdd1476d3962baad', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d28', 'statistic:borrow:overdue:query', '逾期分析', 'f97a03ae28a241c5adf40fd404493743', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d31', 'statistic:invest:customers:query', '投资人数分析', 'dbc97e8de83c4985882f0a22268ce0fe', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d41', 'statistic:bond:query', '债权分析', '46bfc4625f1b4d9ba103df86813f914e', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af000c296f8d61', 'statistic:activity:query', '活动运营分析', '0d0e71628cce4c83a2c05095914b7005', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af0cba25670001', 'statistic:account:circulate:query', '出入金分析', 'ca155fe4600248b4a9eb0b1839c95bc3', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af0cba25670002', 'statistic:account:useMoney:query', '用户余额分析', 'a1eca58c1eca474cba6fdd56e2053381', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af0cba25670003', 'statistic:account:investEarn:query', '投资收益分析', '4f7ce05aa09346e18a7c4dd314f83c29', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` VALUES ('e54c5ca6848e11e699af0cba25670004', 'statistic:account:platProfit:query', '平台盈亏分析', 'c00a85642d504665ab2c36e11fb2acd8', '650237d88b5b4a2ea4fbb1a8c187aa22');


-- ----------------------------
-- 统计-新增常量类型
-- ----------------------------
INSERT INTO sys_dict_type values('05a495bb4cb74b21a25fb9dabb1ae2c5', 'accountRange', '统计金额区间', '2017-03-16 10:50:24', '统计金额区间');
INSERT INTO sys_dict_data values('654208995dee44009ab3d06ecfa71d02', 'accountRange', '1万以下', '1', NULL, 1, '1', '', ',10000');
INSERT INTO sys_dict_data values('13b2a5042d3c475782b28e6d2a59ba2f', 'accountRange', '1万-5万', '2', NULL, 2, '1', '', '10000,50000');
INSERT INTO sys_dict_data values('c19b411820b14cb4be23efa6b9d0e238', 'accountRange', '5万-10万', '3', NULL, 3, '1', '', '50000,100000');
INSERT INTO sys_dict_data values('c0eeca8ce02b4e65874f9858e8de6aa3', 'accountRange', '10万-20万', '4', NULL, 4, '1', '', '100000,200000');
INSERT INTO sys_dict_data values('b6fa8d5c47ea4c849b58a0e5bcf41f08', 'accountRange', '20万-50万', '5', NULL, 5, '1', '', '200000,500000');
INSERT INTO sys_dict_data values('fd1cf78c45074844bea45243cd576f27', 'accountRange', '50万以上', '6', NULL, 6, '1', '', '500000,');

insert into sys_dict_type values('4a0387a55cba41918cdb68c48566eaad','investAmountRange','投资金额范围','2017-03-16 13:45:49','投资金额范围');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa1','investAmountRange','1000元以下','1','2017-03-16 13:49:28','1','1','','0,1000');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa2','investAmountRange','1001-5000元','2','2017-03-16 13:49:28','2','1','','1001,5000');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa3','investAmountRange','5001-1万元','3','2017-03-16 13:49:28','3','1','','5001,10000');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa4','investAmountRange','10001-5万元','4','2017-03-16 13:49:28','4','1','','10001,50000');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa5','investAmountRange','50001-10万元','5','2017-03-16 13:49:28','5','1','','50001,100000');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89aa6','investAmountRange','10万元以上','6','2017-03-16 13:49:28','6','1','','100001,');

INSERT INTO sys_dict_type values('06eadd0e2d364a4dbcda9a59afd3abca', 'statAprType', '年利率统计类型', '2017-04-11 13:42:43', '年利率统计类型');
INSERT INTO sys_dict_data values('2b3f89746a4842589985ceb3a8072222', 'statAprType', '0-6%', '1', '2017-04-11 13:42:43', 1, '1', '0-6%', '0,6');
INSERT INTO sys_dict_data values('50272493de7d41b1bc4e936c07543333', 'statAprType', '6-12%', '2', '2017-04-11 13:42:43', 2, '1', '6-12%', '6,12');
INSERT INTO sys_dict_data values('62cbc3ff9890448aa3f2cdfb78fe4444', 'statAprType', '12-18%', '3', '2017-04-11 13:42:43', 2, '1', '12-18%', '12,18');
INSERT INTO sys_dict_data values('c7a9ebd2fc01462fbe52ce02acfb5555', 'statAprType', '18-20%', '4', '2017-04-11 13:42:43', 3, '1', '18-20%', '18,20');
INSERT INTO sys_dict_data values('c7a9ebd2fc01462fbe52ce02acfb6666', 'statAprType', '20%以上', '5', '2017-04-11 13:42:43', 4, '1', '20%以上', '20,');

insert into sys_dict_type values('4a0387a55cba41918cdb68c48566ebdd','bondTimeLimitRange','债权期限范围','2017-03-16 13:45:49','债权期限范围');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89001','bondTimeLimitRange','0-30天','1','2017-03-16 13:49:28','1','1','','0,30');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89002','bondTimeLimitRange','31-90天','2','2017-03-16 13:49:28','2','1','','31,90');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89003','bondTimeLimitRange','91-180天','3','2017-03-16 13:49:28','3','1','','91,180');
insert into sys_dict_data values('bfe3b8302c6e48149521c44449b89004','bondTimeLimitRange','180天以上','4','2017-03-16 13:49:28','4','1','','181,');

INSERT INTO sys_dict_type values('4a0387a55cba41918cdb68c48566ebd4', 'timeLimitRange', '统计期限范围', '2017-03-16 13:45:49', '统计期限范围');
INSERT INTO sys_dict_data values('054e486fcd704003ae0cb35c4e972957', 'timeLimitRange', '0-1个月', '1', '2017-03-16 13:49:28', 1, '1', '', '0,30');
INSERT INTO sys_dict_data values('a839b8110ea24256a35e642ff586f78a', 'timeLimitRange', '1-3个月', '2', '2017-03-16 13:49:28', 2, '1', '', '30,90');
INSERT INTO sys_dict_data values('2d7f058b18564b4790fda91fc30873a9', 'timeLimitRange', '3-6个月', '3', '2017-03-16 13:49:28', 3, '1', '', '90,180');
INSERT INTO sys_dict_data values('86f93e5890054dd8ac9f5c34a2e15b2d', 'timeLimitRange', '6-12个月', '4', '2017-03-16 13:49:28', 4, '1', '', '180,360');
INSERT INTO sys_dict_data values('bfe3b8302c6e48149521c44449b8976a', 'timeLimitRange', '12-18个月', '5', '2017-03-16 13:49:28', 5, '1', '', '360,540');
INSERT INTO sys_dict_data values('94d6188662fc4a5aa6bf8bcd4288ec45', 'timeLimitRange', '18-24个月', '6', '2017-03-16 13:49:28', 6, '1', '', '540,720');
INSERT INTO sys_dict_data values('3afb93c0ae7b4edbabe92a1145b63d7c', 'timeLimitRange', '24个月以上', '7', '2017-03-16 13:49:28', 7, '1', '', '720,');

INSERT INTO sys_dict_type values('1949a4b33411481bb55ac2c8c691edb1', 'ageRange', '年龄范围', '2017-03-16 15:54:12', '年龄范围');
INSERT INTO sys_dict_data values('3018406557f34da69c6e71a941db90b9', 'ageRange', '18-25', '1', NULL, 1, '1', '', '18,25');
INSERT INTO sys_dict_data values('ccdae29ec6db47b0bbc71cfb053203cc', 'ageRange', '26-35', '2', NULL, 2, '1', '', '26,35');
INSERT INTO sys_dict_data values('fcabaa33e09240e29692d8226665ce82', 'ageRange', '36-45', '3', NULL, 3, '1', '', '36,45');
INSERT INTO sys_dict_data values('c520c7b7b3d34fa69888187585db1396', 'ageRange', '46-55', '4', NULL, 4, '1', '', '46,55');
INSERT INTO sys_dict_data values('5391dfb76ebd403ba0835d63fdccd037', 'ageRange', '56-65', '5', NULL, 5, '1', '', '56,65');
INSERT INTO sys_dict_data values('428413355cd74bec938ed846da6908ce', 'ageRange', '66以上', '6', NULL, 6, '1', '', '66,');

INSERT INTO sys_dict_data values('f47eb2f71fa546c2bd1b0d5f861e723', 'platAccountType', '变现管理费', '11', NULL, 11, '1', '变现管理费', NULL);
INSERT INTO sys_dict_data values('f47eb2f71fa546c2bd1b0d5f861e321', 'platAccountType', '充值手续费垫付', '12', NULL, 12, '1', '充值手续费垫付', NULL);
INSERT INTO sys_dict_data values('f47eb2f71fa546c2bd1b0d5f861e322', 'platAccountType', '提现手续费垫付', '13', NULL, 13, '1', '提现手续费垫付', NULL);


-- ----------------------------
-- 统计-修改原数据库表
-- ----------------------------
-- alter table project_type add column nid varchar(32) null default null comment '类型标识';

-- ----------------------------
-- 统计-新增异常信息
-- ----------------------------
insert into sys_resource values('00973c6e8a9848aa95f9f8e6e8120001','statistic.query.date.is.empty','查询开始日期和结束日期都不能为空','zh_CN');
insert into sys_resource values('00973c6e8a9848aa95f9f8e6e8120002','statistic.query.date.is.error','查询开始日期不能大于结束日期','zh_CN');
insert into sys_resource values('00973c6e8a9848aa95f9f8e6e8120003','statistic.compare.date.is.empty','对比日期不能为空','zh_CN');

-- ----------------------------
-- 统计-新增操作类型
-- ----------------------------
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe81', 'sysLogContent', '出入金分析', 'statistic_account_circulate', NULL, 105, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe83', 'sysLogContent', '投资收益分析', 'statistic_account_earn', NULL, 107, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe84', 'sysLogContent', '平台盈亏分析', 'statistic_account_plant', NULL, 108, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe82', 'sysLogContent', '用户余额分析', 'statistic_account_useMoney', NULL, 106, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe85', 'sysLogContent', '活动运营分析', 'statistic_actvity', NULL, 109, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe86', 'sysLogContent', '债权转让分析', 'statistic_bond', NULL, 110, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe56', 'sysLogContent', '借款人数分析', 'statistic_borrower_num', NULL, 94, '1', '', NULL);
INSERT INTO sys_dict_data values('17a188f449c84d5d9c6ffe202e556c61', 'sysLogContent', '借款金额分析', 'statistic_borrow_account', NULL, 95, '1', '', NULL);
INSERT INTO sys_dict_data values('70d05c28577e4bdda5b4efd3c6e39540', 'sysLogContent', '借款地区', 'statistic_borrow_area', NULL, 99, '1', '', NULL);
INSERT INTO sys_dict_data values('5cdc942d7a6146939d79b203255f1df7', 'sysLogContent', '借款情况', 'statistic_borrow_info', NULL, 100, '1', '', NULL);
INSERT INTO sys_dict_data values('519697d70ef84dafa25a0aad298be16b', 'sysLogContent', '统计借款列表', 'statistic_borrow_list', NULL, 98, '1', '', NULL);
INSERT INTO sys_dict_data values('c28bc615fcf044f69f4240eaf751392a', 'sysLogContent', '待还分析', 'statistic_borrow_notrepay', NULL, 101, '1', '', NULL);
INSERT INTO sys_dict_data values('f17a8bc7c2e540598ed5ee524ce41829', 'sysLogContent', '借款笔数分析', 'statistic_borrow_num', NULL, 96, '1', '', NULL);
INSERT INTO sys_dict_data values('47889ee91f354c04a6a6574d0b0eb8bf', 'sysLogContent', '逾期分析', 'statistic_borrow_overdue', NULL, 104, '1', '', NULL);
INSERT INTO sys_dict_data values('bacdf59854a34f6f89fe2620ab116e2e', 'sysLogContent', '正常还款分析', 'statistic_borrow_repaid', NULL, 102, '1', '', NULL);
INSERT INTO sys_dict_data values('a4c380325eed4037a84a49c6945a53f6', 'sysLogContent', '借款对比', 'statistic_compare_data', NULL, 97, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe88', 'sysLogContent', '投资金额分析', 'statistic_invest_amount', NULL, 111, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe90', 'sysLogContent', '投资地区分析', 'statistic_invest_area', NULL, 111, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe91', 'sysLogContent', '投资情况分析', 'statistic_invest_condition', NULL, 111, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe87', 'sysLogContent', '投资人数分析', 'statistic_invest_customers', NULL, 111, '1', '', NULL);
INSERT INTO sys_dict_data values('d6a8cd8b4b4b4948877a74aab5bdbe89', 'sysLogContent', '投资笔数分析', 'statistic_invest_times', NULL, 111, '1', '', NULL);
INSERT INTO sys_dict_data values('1b1eaaedba8342a1b28c43306d0fea46', 'sysLogContent', '用户统计', 'statistic_user', NULL, 91, '1', '', NULL);
INSERT INTO sys_dict_data values('88f0589338084037b68f00fd79cd1361', 'sysLogContent', '用户活跃度', 'statistic_user_active', NULL, 92, '1', '', NULL);
INSERT INTO sys_dict_data values('f7b570e50e164d28a63fff2905218006', 'sysLogContent', '用户画像', 'statistic_user_portrait', NULL, 93, '1', '', NULL);

-- ----------------------------
-- 新增收货地址异常提示
-- ----------------------------
INSERT INTO sys_resource  VALUES ('58db2b7dd53749f7998a7765422cbce5', 'receiving.address.is.error', '当前收货地址已发生变动，请重新确认收货地址！', 'zh_CN');

-- ----------------------------
-- 新增提现服务费
-- ----------------------------
INSERT INTO sys_dict_data VALUES ('f47eb2f71fa546c2bd1b0d5f861e323', 'platAccountType', '提现服务费', '14', NULL, 14, '1', '提现服务费', NULL);

-- ----------------------------
-- 用户礼包发放
-- ----------------------------
DROP TABLE IF EXISTS `user_gift_grant`;
CREATE TABLE `user_gift_grant` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `gift_name` varchar(32) DEFAULT NULL COMMENT '礼包名称',
  `gift_type` char(1) DEFAULT NULL COMMENT '礼包类型 （0生日礼包 1专享礼包）',
  `gift_level`  char(3) NULL DEFAULT '1' COMMENT '礼包等级',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `redenvelope_rule_id` varchar(32) DEFAULT NULL COMMENT '红包发放规则id',
  `rate_coupon_rule_id` varchar(32) DEFAULT NULL COMMENT '加息券发放规则id',
  `status` char(1) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户礼包发放';

-- ----------------------------
-- 新增兑换商品不存在提示
-- ----------------------------
insert into sys_resource values('00973c6e8a9848aa95f9f8e6e8111111','score.goods.is.not.exist','该兑换商品不存在','zh_CN');

-- ----------------------------
-- 前台产品展示开关
-- ----------------------------
INSERT INTO sys_config VALUES ('8211eb1ad4154f7eb096eec7f9378c90', 'open_product', '产品展示开关', '1', '1', '2017-05-19 11:39:17', '1', '1.前台展示产品相关0.前台屏蔽产品相关', '0');
