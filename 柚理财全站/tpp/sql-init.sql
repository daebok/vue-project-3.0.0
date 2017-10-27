-- 所有的orderNo字段从17位变为32位
	tpp_merchant_log  ： order_no
	project_invest: invest_order_no,order_no
-- 修改后台的tppName  从chinapnr改为cbhb  后台操作
-- 新增 渤海银行商户号 字段
insert into sys_config() values('cbhb_customerId','渤海银行商户号','800057100010001','1','2017-03-01 11:13:11','1','渤海银行商户号','1');
-- 新增渤海银行提交地址
insert into sys_config() values('cbhb_submit_url','渤海银行提交地址','http://221.239.93.145:8280/hipos/payTransaction','1','2017-03-01 11:13:11','1','渤海银行提交地址(现在暂为测试地址)','1');
-- 新增渤海银行证书私钥密码
insert into sys_config() values('cbhb_private_key','渤海银行证书私钥密码','123456','1','2017-03-01 11:13:11','1','渤海银行证书私钥密码','1');

-- 银行卡表
DROP TABLE IF exists `account_bank_card`;
CREATE TABLE `account_bank_card` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `status` char(1) DEFAULT '0' COMMENT '银行卡状态，0:未启用，1：启用',
  `bank_code` varchar(64) DEFAULT NULL COMMENT '银行代码',
  `bank_name` varchar(64) DEFAULT NULL COMMENT '银行名称',
  `bank_account` varchar(128) DEFAULT NULL COMMENT '银行账号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_ip` varchar(255) DEFAULT NULL COMMENT '添加时候的IP',
  `update_ip` varchar(255) DEFAULT NULL COMMENT '修改时候的ip',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行卡记录表';

alter table account_bank_card add column `status` char(1) DEFAULT '0' COMMENT '银行卡状态，0:未启用，1：启用';

INSERT INTO `sys_dict_type` VALUES (replace(UUID(),"-",""), 'cbhb_bank_card', '渤海银行--银行卡列表', '2017-03-01 13:48:02', '渤海银行--银行卡列表');
INSERT INTO `sys_dict_type` VALUES (replace(UUID(),"-",""), 'cbhb_transStat', '渤海银行--交易状态枚举', '2017-03-02 20:00:09', '渤海银行--交易状态枚举');


INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','平安银行', 		 'PAB', null, '12', '1', '平安银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','建设银行', 		 'CCB', null, '4', '1', '建设银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card','农行', 			 'ABC', null, '2', '1', '农行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '民生银行',		 'CMBC',null, '7', '1', '民生银行', null);
INSERT INTO `sys_dict_data` VALUES (replace(UUID(),"-",""),'cbhb_bank_card', '渤海银行', 		 'CBHB',null, '9', '1', '渤海银行', null);
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



-- 第三方发送的请求参数和回调参数记录表

CREATE TABLE `tpp_cbhb_request` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `tpp_cust_id` varchar(50) NOT NULL COMMENT '操作者：存管账户号',
  `request_type` varchar(50) NOT NULL COMMENT '请求类型',
  `request_merbillno` varchar(50) NOT NULL COMMENT '请求流水号',
  `request_map` text NOT NULL COMMENT '请求数据包（包含明文、密文、证书信息）',
  `request_file_url` varchar(512) NOT NULL COMMENT '请求文件的路径',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='渤海银行请求记录表';



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






