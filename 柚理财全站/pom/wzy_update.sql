/*
User: wangzhongyuan
Date: 2017-08-22 19:03
*/
/*添加一个新的还款状态，为'已垫付'*/
INSERT INTO `sys_dict_data` VALUES ('d636fa043e544d01b13a9022d2382c19', 'repayStatus', '已垫付', '2', null, '3', '1', '由担保机构垫付的情况', null);


/*
*author:wzy
*date:2017/08/25
*/
/*添加一个合同管理菜单*/
INSERT INTO `sys_menu` VALUES ('06a99c712c2e4217b016b1d848692496', 'project:contract', '合同管理', '/protocol/protocol/protocolList.html', '1f719034546143b783da0a82bbf5c4a1', '1f719034546143b783da0a82bbf5c4a1', '', '7', null, '0', '', '1');
/*为合同管理添加查询权限*/
insert into sys_permission values('1249617675294860498', 'project:contract:query', '合同管理-查询', '06a99c712c2e4217b016b1d848692496', '650237d88b5b4a2ea4fbb1a8c187aa22');
/*角色关联查询*/
insert into sys_role_permission values('e40fc686ad05492e8c1f56a5bec62846', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294860498');


/*
*author:wzy
*date:2017/8/28
*/
/*给借款记录添加下载权限*/
insert into sys_permission values('1249617675294860499', 'project:borrow:borrowList:downLoad', '借款-借款记录-协议下载', '914d4ad12e724cbfbb59456997242959', 'ec40101ca17444a29bb1247175451212');

/*在关联表中插入记录*/
insert into sys_role_permission values('e40fc686ad06851e8c1f56a5bec62236', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294860499');

/**
 * author:wzy
 * date:2017/8/30
 */
/*在消息模板中添加一个催款提醒*/
INSERT INTO `sys_message_type` VALUES ('99', 'urge_repayment', '1', '催收提醒', '催收提醒', '尊敬的用户[${user.userName}]，您于${createTime}的还款[${projectName}]，还剩${payment-payedAmount}未还，请尽快还款!', '1', '2017-08-29 17:30:45', '催收还款提醒');

/**
 * author:wzy
 * date:2017/09/01
 */
 /*给红包表加一个编号字段*/
alter table user_redenvelope add column redenvelope_uuid varchar(16) default null;
 /*给加息券表加一个编号字段*/
alter table user_rate_coupon add column rate_coupon_uuid varchar(16) default null;
/**添加加息券发放权限*/
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452324', 'rate_grant', '发放加息券', '1', '31', '2017-09-01 11:31:40', '0', null);
/*给加息券列表添加加息券发放权限*/
INSERT INTO `sys_permission` VALUES ('0f108e0a866a11e78474fcaa144dff92', 'oper:addApr:addAprList:rate_grant', '加息券记录-发放', '376c627aac1748a1aa7faffb50c18187', 'ec40101ca17444a29bb1247175452324');
/*表与表之间的关联*/
INSERT INTO `sys_role_permission` VALUES ('ea548c65a2104f68bff2f761811d1fee', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294123456');

/*修改合同管理的路径*/
UPDATE sys_menu SET url='/contract/download/downContract.html' WHERE uuid='06a99c712c2e4217b016b1d848692496';

/**
*author:wzy
*date:2017/09/04
*/
/*添加一个积分发放权限*/
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452fff', 'score_grant', '积分发放', '1', '37', '2017-09-04 08:53:31', '0', null);
/*给用户积分赋予积分发放权限*/
INSERT INTO `sys_permission` VALUES ('1249617675294867624', 'oper:score:userScore:score_grant', '用户积分-发放', 'a39d0f69ccac4543a361bcf6e3392938', 'ec40101ca17444a29bb1247175452fff');
/*关联表*/
INSERT INTO `sys_role_permission` VALUES ('fff449518da6424ba8a960af3d5614b8', '22c0eef4a2c14515855d84f96eb991ae', '1249617675294867624');
/*给积分表增加一个手动发放积分字段*/
alter table `user_score` add column `hand_score` int(10) default 0;

/*添加群发权限*/
INSERT INTO `sys_operation` VALUES ('ec40101ca17444a29bb1247175452eee', 'mass', '群发', '1', '32', '2017-09-02 10:31:37', '0', null);
/*在运营中添加消息群发菜单*/
INSERT INTO `sys_menu` VALUES ('e0957fe856524d19b4f5739b9379edcc', 'oper:message', '消息群发', '/operate/sendMessage/sendUserMessage.html', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '', '6', null, '0', '消息群发', '1');
/*给消息群发增加群发权限*/
INSERT INTO `sys_permission` VALUES ('asd123f34hfdghtgh12345', 'oper:message:mass', '消息群发', 'e0957fe856524d19b4f5739b9379edcc', 'ec40101ca17444a29bb1247175452eee');
/*关联表*/
INSERT INTO `sys_role_permission` VALUES ('ff5f3a8f94904a7799c6d9b4b09fef24', '22c0eef4a2c14515855d84f96eb991ae', 'asd123f34hfdghtgh12345');

/**
*author:wzy
*date:2017/09/05
*/
/*给借款表加平台借款管理费字段*/
ALTER TABLE `project` ADD COLUMN `cost_manage` decimal(20,4) DEFAULT '0.0000' COMMENT '平台借款管理费';

/**
*author:wzy
*date:2017/09/06
*/
/*给用户回馈意见表增加状态字段*/
alter table `user_opinion` add column `status` char(2) default '0' COMMENT '回馈意见状态';
/*给用户回馈意见表增加回馈内容表*/
alter table `user_opinion` add column `content` varchar(500) default null COMMENT '回馈意见内容';

/**
*author:wzy
*date:2017/09/07
*/
/*在设置中添加前台用户登录菜单*/
INSERT INTO `sys_menu` VALUES ('d2a6caf832fd4d09a1cd3e23869a0cc8', 'set:user_login', '前台用户登录', '/sys/user/user_login.html', 'a0f88f24408a42759589270385618808', 'a0f88f24408a42759589270385618808', '&#xe617;', '11', null, '0', '前台用户登录', '1');
/*给前台用户登录添加查询权限*/
INSERT INTO `sys_permission` VALUES ('1249617675294867666', 'set:user_login:query', '前台用户登录查询', 'd2a6caf832fd4d09a1cd3e23869a0cc8', '650237d88b5b4a2ea4fbb1a8c187aa22');
/*关联表*/
insert into `sys_role_permission` values('fff333518da6424ba8a960af3d5614b8','22c0eef4a2c14515855d84f96eb991ae','1249617675294867666');


/*
 * author:wzy
 * date:2017-09-08
 * 
 */
/**
 * 创建用户申请额度表
 */
DROP TABLE IF EXISTS `user_credit`;
CREATE TABLE `user_credit` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `account` int(10) DEFAULT 0 COMMENT '金额',
  `content` text COMMENT '申请内容',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `status` char(1) DEFAULT '1' COMMENT '0待审核,1审核通过,2审核失败',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='额度申请表';

/**
 * 用户总额度表
 */
 DROP TABLE IF EXISTS `user_credit_line`;
CREATE TABLE `user_credit_line` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT 0 COMMENT '用户id',
  `total_credit` int(10) DEFAULT 0 COMMENT '总额度',
  `use_credit` int(10) DEFAULT 0  COMMENT '可用额度',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户额度表';

/*新建额度管理菜单*/
INSERT INTO `sys_menu` VALUES ('75366baca57d4556a438700c58385727', 'project:borrow:credit', '额度管理', '/loan/credit/creditRecord.html', '6940d2925e36443f9630f229679fd945', '1f719034546143b783da0a82bbf5c4a1:6940d2925e36443f9630f229679fd945', '', '15', null, '0', '额度管理', '1');

/*给额度管理增加权限*/
insert into `sys_permission` values('0f0739b8955a11e78474fcaa144dff92','project:borrow:credit:query', '额度管理-查询', '75366baca57d4556a438700c58385727', '650237d88b5b4a2ea4fbb1a8c187aa22');
insert into `sys_permission` values('0f0739b8944a11e78474fcaa144dff92','project:borrow:credit:edit', '额度管理-编辑', '75366baca57d4556a438700c58385727', '90b04389913b41008a69cead0cbf02b7');
insert into `sys_permission` values('0f0739b8933a11e78474fcaa144dff92','project:borrow:credit:add', '额度管理-增加', '75366baca57d4556a438700c58385727', 'ec40101ca17444a29bb1247175452bf0');
insert into `sys_permission` values('0f0739b8922a11e78474fcaa144dff92','project:borrow:credit:auditing', '额度管理-审核', '75366baca57d4556a438700c58385727', 'ec40101ca17444a29bb1247175452123');

/**
 * 关联表
 */
insert into `sys_role_permission` values('ec40101ca17333a29bb1247175452bf0','22c0eef4a2c14515855d84f96eb991ae','0f0739b8955a11e78474fcaa144dff92');
insert into `sys_role_permission` values('ec40101ca17222a29bb1247175452bf0','22c0eef4a2c14515855d84f96eb991ae','0f0739b8944a11e78474fcaa144dff92');
insert into `sys_role_permission` values('ec40101ca17111a29bb1247175452bf0','22c0eef4a2c14515855d84f96eb991ae','0f0739b8933a11e78474fcaa144dff92');
insert into `sys_role_permission` values('ec40101ca17000a29bb1247175452bf0','22c0eef4a2c14515855d84f96eb991ae','0f0739b8922a11e78474fcaa144dff92');

/**
 * 给用户申请额度表增加字段
 */
alter table `user_credit` add column `remark` text default null comment '审核内容';


/*
 * author:wzy
 * date:2017-09-09
 * 
 */
/**
 * 修改额度管理菜单
 */
INSERT INTO `sys_menu` VALUES ('f1aa56e9b4d5490685b7d684e99ca789', 'oper:credit', '额度管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe61b;', '5', null, '0', '额度管理', '0');
update `sys_menu` set code = 'oper:credit:credit' where uuid='75366baca57d4556a438700c58385727';
update `sys_menu` set parent_ids = 'f1aa56e9b4d5490685b7d684e99ca789:90c22e2f12b44f218d25c42b5bbd16a7' where uuid='75366baca57d4556a438700c58385727';

/**
 * 在额度管理下增加用户额度菜单
 */
INSERT INTO `sys_menu` VALUES ('4999f6fff09042908ea76dcaa2ca6879', 'oper:credit:userCredit', '用户额度', '/loan/credit/creditList.html', 'f1aa56e9b4d5490685b7d684e99ca789', '90c22e2f12b44f218d25c42b5bbd16a7:f1aa56e9b4d5490685b7d684e99ca789', '&#xe61b;', '1', null, '0', '', '1');
/**
 * 给用户额度增加查询权限
 */
insert into `sys_permission` values ('0f0739b8911a11e78474fcaa144dff92','oper:credit:userCredit:query','用户额度-查询', '4999f6fff09042908ea76dcaa2ca6879', '650237d88b5b4a2ea4fbb1a8c187aa22');
/*用户授权*/
insert into `sys_role_permission` VALUES ('008d0ea00f834a5cba9b2631234fa0b7', '22c0eef4a2c14515855d84f96eb991ae', '0f0739b8911a11e78474fcaa144dff92');

INSERT INTO `sys_message_type` VALUES ('250','credit_letter', '3', '额度扣除', '额度扣除', '尊敬的用户[${user.userName}]，由于您[${userCredit.content}]，故扣除您${userCredit.deduct}元的额度','1','2016-08-22 10:32:51','因为各人原因，扣除额度');

INSERT INTO `sys_message_type` VALUES ('251','add_credit_letter', '3', '申请额度', '申请额度', '尊敬的用户[${user.userName}]，您的额度申请已被受理，当前额度为[${totalCredit}]','1','2016-08-22 10:32:51','申请额度');
/*
 * author:wzy
 * date:2017-09-014
 * 
 */
update `sys_menu` set parent_id = 'f1aa56e9b4d5490685b7d684e99ca789' where uuid='75366baca57d4556a438700c58385727';
ALTER TABLE `user_credit` ADD COLUMN `deduct` int(11) DEFAULT '0' COMMENT '扣除金額';
ALTER TABLE `user_credit` ADD COLUMN `remark` text COMMENT '审核内容';

/*
 * author:wzy
 * date:2017-10-26
 */
ALTER TABLE `score_goods` ADD COLUMN `supplier` VARCHAR(100) DEFAULT NULL COMMENT '供货商';
ALTER TABLE `score_goods` ADD COLUMN `notes` VARCHAR(255) DEFAULT NULL COMMENT '文字备注';

