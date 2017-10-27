

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `account_code` varchar(4) NOT NULL COMMENT '账户编号',
  `account_type` varchar(2) NOT NULL COMMENT '账户类别',
  `parent_code` varchar(4) DEFAULT NULL COMMENT '上级账户编号',
  `total` decimal(20,4) DEFAULT NULL COMMENT '账户总额',
  `use_money` decimal(20,4) DEFAULT NULL COMMENT '可用余额',
  `no_use_money` decimal(20,4) DEFAULT NULL COMMENT '冻结金额',
  `status` char(1) DEFAULT '1' COMMENT '账户状态 0 未启用；1 启用',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';

-- ----------------------------
-- Records of account
-- ----------------------------

INSERT INTO `account` VALUES ('ff3d815052b142389054a5e828c02455', '111', '001', '1', null, '0.0000', '465.0000', '0.0000', null);
INSERT INTO `account` VALUES ('ff6579a399ab4d7c9b13814dfb0278fb', '111', '001', '1', null, '0.0000', '996.0000', '0.0000', null);
INSERT INTO `account` VALUES ('ff95276826404eaabf2ba78a45fef2f8', '111', '001', '1', null, '0.0000', '53.0000', '0.0000', null);

-- ----------------------------
-- Table structure for `account_log`
-- ----------------------------
DROP TABLE IF EXISTS `account_log`;
CREATE TABLE `account_log` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户ID',
  `account_code` varchar(4) NOT NULL COMMENT '账户编号',
  `to_user` varchar(32) DEFAULT NULL COMMENT '交易对方的用户ID',
  `account_type` varchar(32) DEFAULT NULL COMMENT '资金类型',
  `account_name` varchar(64) DEFAULT NULL COMMENT '资金类型名称',
  `money` decimal(20,4) DEFAULT NULL COMMENT '交易金额',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `payments_type` varchar(2) DEFAULT NULL COMMENT '收支类型',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户日志表';

-- ----------------------------
-- Records of account_log
-- ----------------------------

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
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uk_sys_dict_data_type_value` (`item_value`,`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES ('18dcc9f5c85140eabbbc0c57323d8c9b', 'userStatus', '锁定', '-1', null, '2', '1', null);
INSERT INTO `sys_dict_data` VALUES ('1bb7234cb22d4a179b6dc7e7e28c13ae', 'configType', '底层参数配置', '1', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('1c3633c3f5d249e2a8d169ed2a71b761', 'userStatus', '正常', '0', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('37dd803758e94a2f8e4339eadcde860d', 'account_bank', '建设银行', 'CCB', null, '3', '1', '');
INSERT INTO `sys_dict_data` VALUES ('3a22e104a91441ce82e5d432a4368f8d', 'account_type', '代收本金', 'wait_capital', null, '1', '1', '12');
INSERT INTO `sys_dict_data` VALUES ('45dbd1456c18480ca1907de68cc77c50', 'account_bank', '中国银行', 'BOC', null, '2', '1', '');
INSERT INTO `sys_dict_data` VALUES ('52e84178205d4148b511dabecfe13c04', 'logTemplateType', '资金日志', '1', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('639f2d68feaa4f84b3a2e6f07c2dd951', 'sex', '男', '1', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('66c254849bc54631a1122c0947c58361', 'orgLevel', '二级', '1', null, '2', '1', null);
INSERT INTO `sys_dict_data` VALUES ('73ffa518ce92427ea650de5a85fbba4f', 'enable', '禁用', '0', null, '2', '1', null);
INSERT INTO `sys_dict_data` VALUES ('89f8c8db84c64ec999464c0d0247871e', 'logTemplateType', '积分日志', '2', null, '2', '1', null);
INSERT INTO `sys_dict_data` VALUES ('8cf29fc0e4c640fbbbc150e2a1c8a0c5', 'account_bank', '工商银行', 'ICBC', null, '1', '1', 'desc');
INSERT INTO `sys_dict_data` VALUES ('8d589b7560244bcfa42a3b02e9ed7a37', 'account_type', '待收奖励', 'wait_award', null, '0', '1', '');
INSERT INTO `sys_dict_data` VALUES ('a2c8cf0949ac4f5c8cd2f5f675c2ada7', 'orgLevel', '一级', '0', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('b4ddb4ee10514414937385064f76852b', 'enable', '启用', '1', null, '1', '1', null);
INSERT INTO `sys_dict_data` VALUES ('d93998e9370c4a3c935c75624c0814c3', 'orgLevel', '三级', '2', null, '3', '1', null);
INSERT INTO `sys_dict_data` VALUES ('de3565a2caf74195aa42f8c0c3801255', 'sex', '女', '2', null, '2', '1', null);

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
  KEY `idx_dict_type_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典分类表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES ('00923cfc6d71423db5863f4d4a874f83', 'account_type', '用户资金类别', '2016-06-04 18:09:57', '');
INSERT INTO `sys_dict_type` VALUES ('2e792b57d6754713a582a71ef2bc22e3', 'sex', '性别', '2016-06-02 17:55:27', '123');
INSERT INTO `sys_dict_type` VALUES ('5a338fba46e54e7795615584c2d0d12a', 'enable', '是否启用', '2016-06-02 17:56:12', null);
INSERT INTO `sys_dict_type` VALUES ('6bdcfe862b5e4a24aacfe8ed1859bc1d', 'card_type', '证件类别', '2016-06-06 17:34:53', '');
INSERT INTO `sys_dict_type` VALUES ('70ceb00433284ca0a22cc6e73577a1d6', 'borrow_use', '借款用途', '2016-06-06 17:34:37', '');
INSERT INTO `sys_dict_type` VALUES ('738d5244a30b4c5f8b910dbf3072d269', 'account_bank', '银行列表', '2016-06-04 18:08:58', '');
INSERT INTO `sys_dict_type` VALUES ('b36e2451d4ac4b58a3435d811c426e4d', 'userStatus', '用户状态', '2016-06-04 09:09:06', null);
INSERT INTO `sys_dict_type` VALUES ('ba2b025b359346289f6daa7dbd51e8ac', 'logTemplateType', '日志模板类型', '2016-06-04 09:36:32', null);
INSERT INTO `sys_dict_type` VALUES ('bb452a0602af4f95bc18321d6b6543da', 'user_type', '用户类型', '2016-06-06 17:35:33', '');
INSERT INTO `sys_dict_type` VALUES ('c041a3cabee14e849d88de437ba5a167', 'configType', '参数类型', '2016-06-02 17:57:42', null);
INSERT INTO `sys_dict_type` VALUES ('e6c562c25ae643868bd273edb5c7df90', 'orgLevel', '机构层级', '2016-06-03 14:17:21', null);
INSERT INTO `sys_dict_type` VALUES ('f0c8f4e3a27c49298638e2bbfcb5d69b', 'authentication_type', '操作记录类型', '2016-06-06 15:25:51', '操作记录类型');
INSERT INTO `sys_dict_type` VALUES ('ff6cd5a3abe240e7853cb0d76ff5ca4a', 'borrow_style', '还款方式', '2016-06-06 17:34:12', '');
