-- 自动投资存管接口使用限额增加
INSERT INTO `sys_config` VALUES ('0a9f135dc1d6492f94000d72dfa5212', 'auth_single_amount_limit', '单笔投标金额的上限', '999999.00', '2', '2016-08-14 14:27:53', '1', '单笔投标金额的上限', '1');
INSERT INTO `sys_config` VALUES ('0a9f135dc1d6492f94000d72dfa52213', 'auth_max_amount_limit', '自动投标总金额上限（不算已还金额）', '999999.00', '2', '2016-08-14 14:27:53', '1', '自动投标总金额上限（不算已还金额）', '1');

-- 自动投资红包加息券增加
ALTER TABLE `auto_invest_rule_log`
ADD COLUMN `red_type`  char(1) NULL DEFAULT '1' COMMENT '红包类型 1最早到期日 2最大金额' AFTER `create_time`,
ADD COLUMN `increase_type`  char(1) NULL DEFAULT '1' COMMENT '加息券类型 1最早到期日 2最大金额' AFTER `red_type`;



-- 自动投资改类型
ALTER TABLE `auto_invest_rule_log`
MODIFY COLUMN `red_type`  tinyint(1) NULL DEFAULT 1 COMMENT '红包类型 1最早到期日 2最大金额' AFTER `create_time`,
MODIFY COLUMN `increase_type`  tinyint(1) NULL DEFAULT 1 COMMENT '加息券类型 1最早到期日 2最大金额' AFTER `red_type`;

-- 红包表加订单号
ALTER TABLE `user_redenvelope`
ADD COLUMN `order_no`  char(20) NULL DEFAULT '' COMMENT '发送红包的订单号' AFTER `redenvelope_uuid`;

-- 加息劵最大有效期限
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed644', 'rateCoupon_max_avail_period', '加息劵最大有效期限', '365', '2', '2016-08-14 14:27:53', '1', '固定期加息劵可设置的最大有效期天数', '1');

-- 权限有关矫正修改

delete from sys_menu where LOCATE('9b6b5489b9d947cdb73d209dde0ecf63',parent_ids) > 0;

-- 查找查询出来的问题
select * from sys_permission t where t.menu_id not in (select uuid from sys_menu);

select * from sys_permission t where t.operation_id not in (select uuid from sys_operation);

select * from sys_role_permission t where t.permission_id not in (select uuid from sys_permission);
-- 对账具体删除
DELETE from sys_permission  where uuid in('0b9086ea736911e7895eac9e1791bd12','0b90b97b736911e7895eac9e1791bd12','0b90f05b736911e7895eac9e1791bd12','0b914114736911e7895eac9e1791bd12');

-- 对账删除
delete from sys_menu where uuid in ('0b3d5301736911e7895eac9e1791bd12');


-- 统计用户分析缺失的三个菜单
INSERT INTO `sys_menu` VALUES ('61225cc0a22e4dfa8e01f5f6f913bde2', 'statistic:user:userStatistic', '注册认证用户统计', '/statistic/user/userStatisticManage.html', '0e150c63266d4338b5f221bad13f47e5', 'd8d3e092a09e4c36abde2260f1660a48,0e150c63266d4338b5f221bad13f47e5', '', '1', null, '0', '', '1');


INSERT INTO `sys_menu` VALUES ('87c418076f9a4b758cb799fbdbe3869c', 'statistic:user:userActive', '活跃用户分析', '/statistic/user/userActiveManage.html', '0e150c63266d4338b5f221bad13f47e5', 'd8d3e092a09e4c36abde2260f1660a48,0e150c63266d4338b5f221bad13f47e5', '', '2', null, '0', '', '1');


INSERT INTO `sys_menu` VALUES ('65b7cb9b030e43f4b2fc396f9a55d87d', 'statistic:user:userPortrait', '用户画像', '/statistic/user/userPortraitManage.html', '0e150c63266d4338b5f221bad13f47e5', 'd8d3e092a09e4c36abde2260f1660a48,0e150c63266d4338b5f221bad13f47e5', '', '3', null, '0', '', '1');

-- 提现失败修改
update sys_log_template set template_content = '提现失败，退回提现金额${amount}元' where uuid = 'f97a8c7c45a242bfbcb72d740217b9e6';

-- 9.13 接口基础参数调整
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed111', 'jxbank_inst_code', '机构代码', '01340001', '1', '2017-09-13 14:27:53', '1', '机构代码', '1');
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed112', 'jxbank_bank_code', '银行代码', '30050000', '1', '2017-09-13 14:27:53', '1', '银行代码', '1');
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed113', 'jxbank_pass', '私钥密码', 'jingyuanyoulicai_sit@2016', '1', '2017-09-13 14:27:53', '1', '私钥密码', '1');
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed114', 'jxbank_base_url', '访问环境的路径', 'https://test.credit2go.cn', '1', '2017-09-13 14:27:53', '1', '访问环境的路径', '1');

-- update sys_config set config_value = '01340001' where uuid = '54c294528b5041ff838380676a2ed111';
-- update sys_config set config_value = '30050000' where uuid = '54c294528b5041ff838380676a2ed112';
-- update sys_config set config_value = 'jingyuanyoulicai_uat@2016' where uuid = '54c294528b5041ff838380676a2ed113';
-- update sys_config set config_value = 'https://access.credit2go.cn' where uuid = '54c294528b5041ff838380676a2ed114';


INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed115', 'jxbank_private_pass', '私钥名称', 'jingyuanyoulicai_sit.p12', '1', '2017-09-13 14:27:53', '1', '私钥名称', '1');
-- update sys_config set config_value = 'jingyuanyoulicai_uat.p12' where uuid = '54c294528b5041ff838380676a2ed115';

INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed116', 'jxbank_redenvelope', '红包账号', '6212462380000150039', '1', '2017-09-13 14:27:53', '1', '红包账号', '1');

-- update sys_config set config_value = '6212462280000000012' where uuid = '54c294528b5041ff838380676a2ed116';


-- 9.19 文件下载增加
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed117', 'jxbank_product', '产品编号', '0139', '1', '2017-09-13 14:27:53', '1', '产品编号', '1');
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed118', 'jxbank_bank_num', '银行编号', '3005', '1', '2017-09-13 14:27:53', '1', '银行编号', '1');


-- update sys_config set config_value = '0119' where uuid = '54c294528b5041ff838380676a2ed117';
-- update sys_config set config_value = '3005' where uuid = '54c294528b5041ff838380676a2ed118';

-- 最后一起还红包日志
INSERT INTO `sys_log_template` VALUES ('15ec4997946549348a9d005041164a77', '1', 'collect_redenvelope', '收回红包', '${info!}还款，收到红包${amount}元', '0', '2017-09-18 14:02:11', '收回垫付款');


-- 9.21 放款未结束，请稍后操作
INSERT INTO `sys_resource` (`uuid`, `res_name`, `res_text`, `res_language`) VALUES ('bd98ffb54c3a40088fa49eeba0d34324', 'bond.message.bondInvest.loan.not.success', '放款未结束，请稍后操作', 'zh_CN');


-- 9.25 即信公钥增加
INSERT INTO `sys_config` VALUES ('54c294528b5041ff838380676a2ed119', 'jxbank_jixin_public_pass', '即信公钥名称', 'fdep.crt', '1', '2017-09-13 14:27:53', '1', '即信公钥名称', '1');
-- update sys_config set config_value = 'cs_jx.crt' where uuid = '54c294528b5041ff838380676a2ed119';

-- 展示借款，不展示产品
update sys_config set config_value = '0' where code = 'open_product';

-- 更新地址
update sys_config set config_value = 'https://test.credit2go.cn/escrow' where uuid = '54c294528b5041ff838380676a2ed114';

-- 10.09数字字典修改
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc339', 'tppType', '还担保垫付', 'repay_bail', NULL, '14', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc340', 'tppType', '批次还担保垫付', 'batch_repay_bail', NULL, '15', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc341', 'accountType', '还担保垫付', 'repay_bail', NULL, '102', '1', '', NULL);
INSERT INTO `sys_dict_data` VALUES ('198aae9daf734ec9b42ead7efa1fc342', 'accountType', '收回担保垫付', 'repay_bail_back', NULL, '103', '1', '', NULL);

-- 10.25借款管理费增加
ALTER TABLE `project_collection`
ADD COLUMN `borrow_manage_fee`  decimal(20,4) NULL DEFAULT 0 COMMENT '每个投资人每期的对应的借款人管理费' AFTER `periods`;
ALTER TABLE `project_repayment`
ADD COLUMN `borrow_manage_fee`  decimal(20,4) NULL DEFAULT 0 COMMENT '每期借款管理费' AFTER `periods`;

