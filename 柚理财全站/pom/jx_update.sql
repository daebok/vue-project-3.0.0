-- 8.22 投标成功授权码
ALTER TABLE project_invest
  ADD COLUMN auth_code VARCHAR(20) DEFAULT NULL
COMMENT '江西银行投标成功授权码';

-- 8.31 新增tpp词典
INSERT INTO `sys_dict_data` VALUES ('8392a4bf0cca49d4ae5d84322dfd9522', 'tppStatus', '已查询', '3', NULL, '4', '1', '', NULL);

INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc331', 'tppType', '批量放款', 'batch_loan', NULL, '6', '1', '', NULL);

-- 9.5
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc332', 'tppType', '批量还款', 'batch_repay', NULL, '7', '1', '', NULL);
ALTER TABLE tpp_trade
  ADD COLUMN auth_code VARCHAR(20) DEFAULT NULL
COMMENT '江西银行投标成功授权码';

-- 9.6 加息
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc333', 'tppType', '放款加息发放', 'voucher', NULL, '8', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc334', 'tppType', '批次放款加息发放', 'batch_voucher', NULL, '9', '1', '', NULL);

-- 9.8 电子账户资金交易明细查询
INSERT INTO `sys_menu` VALUES ('8c061da85ef64dfabc985123b45c8645', 'account:accountDetails', '交易明细', '/account/account/accountDetailsQuery.html', 'd8d3e092a09e4c36abde2260f1660a49', 'd8d3e092a09e4c36abde2260f1660a49', '&#xe605;', '6', NULL, '0', '', '1');
INSERT INTO `sys_permission` VALUES ('12496176752948675813', 'account:accountDetails:query', '交易明细-查询', '8c061da85ef64dfabc985123b45c8645', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_role_permission` VALUES ('6f5872464a6b4bbd8bd8a310bfa45c3c', '3b87d72397f948108d583bdf429b8e9e', '12496176752948675813');

INSERT INTO `sys_dict_type` VALUES ('00923cfc6d71423db5863f4d4a873f83', 'tranType', '交易类型', '2016-06-04 18:09:57', '');
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c001', 'tranType', 'P2P到期还款', '2781', '2017-09-08 15:09:38', '1', '1', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c002', 'tranType', 'P2P融资扣款', '2780', '2017-09-08 15:09:38', '2', '1', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c003', 'tranType', '银联代收付渠道资金转出', '2616', '2017-09-08 15:09:38', '3', '1', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c004', 'tranType', '银联代收付渠道资金转入', '7616', '2017-09-08 15:09:38', '4', '1', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES ('1eab8f654f2446078204fc7c005', 'tranType', '行内渠道资金转入', '7820', '2017-09-08 15:09:38', '5', '1', NULL, NULL);

-- 9.11 结束债权
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc335', 'tppType', '结束债权', 'credit_end', NULL, '10', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc336', 'tppType', '批次结束债权', 'batch_credit_end', NULL, '11', '1', '', NULL);

-- 9.15 借款人还担保垫付款需要用到
ALTER TABLE tpp_trade
  ADD COLUMN `project_repayment_id` VARCHAR(32) DEFAULT NULL
COMMENT '对应的还款记录ID';

INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc337', 'tppType', '担保垫付', 'bail_repay', NULL, '12', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc338', 'tppType', '批次担保垫付', 'batch_bail_repay', NULL, '13', '1', '', NULL);

ALTER TABLE user_identify
  ADD COLUMN `auto_credit_invest_auth_status` CHAR(1) DEFAULT '0'
COMMENT '投资人自动债权转让签约授权状态（0.未授权 1.已授权，默认0）';
ALTER TABLE user_identify
  ADD COLUMN `auto_credit_invest_auth_time` DATETIME DEFAULT NULL
COMMENT '投资人自动债权转让签约授权时间';

-- 9.18 借款人还垫付款资金日志
INSERT INTO `sys_log_template` VALUES ('fc2fb482871e4a8c97c52da0044f0c83', '1', 'repay_bail', '还垫付款', '${info!}还垫付款${money!}元。其中本金${capital!}元，利息${interest!}元，逾期利息${lateInterest!}元，平台罚息${merchantLateInterest!}元。', '0', '2017-09-18 14:02:11', '还垫付款');
INSERT INTO `sys_log_template` VALUES ('fc2fb482871e4a8c97c52da0044f0c84', '1', 'repay_bail_back', '收回垫付款', '${info!}收回垫付款${money!}元。', '0', '2017-09-18 14:02:11', '收回垫付款');
