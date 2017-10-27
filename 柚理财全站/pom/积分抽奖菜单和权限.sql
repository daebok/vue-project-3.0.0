INSERT INTO `sys_menu` (`uuid`, `code`, `menu_name`, `url`, `parent_id`, `parent_ids`, `icon_css`, `sort`, `create_time`, `delete_flag`, `remark`, `is_leaf`) VALUES ('4ad463daca9a494d9608a4d4b4da3a1e', 'oper:scorelottery', '积分抽奖管理', '#', '90c22e2f12b44f218d25c42b5bbd16a7', '90c22e2f12b44f218d25c42b5bbd16a7', '&#xe60a;', '8', NULL, '0', '', '0');
INSERT INTO `sys_menu` (`uuid`, `code`, `menu_name`, `url`, `parent_id`, `parent_ids`, `icon_css`, `sort`, `create_time`, `delete_flag`, `remark`, `is_leaf`) VALUES ('7bb123994f7d4d28b81a53ef1319421c', 'oper:scorelottety:record', '积分抽奖记录', '/operate/score/userScoreLotteryRecordManage.html', '4ad463daca9a494d9608a4d4b4da3a1e', '90c22e2f12b44f218d25c42b5bbd16a7:4ad463daca9a494d9608a4d4b4da3a1e', '', '2', NULL, '0', '', '0');
INSERT INTO `sys_menu` (`uuid`, `code`, `menu_name`, `url`, `parent_id`, `parent_ids`, `icon_css`, `sort`, `create_time`, `delete_flag`, `remark`, `is_leaf`) VALUES ('9e2b9fd6f84a4d13911aeb1fe5cce78e', 'oper:scorelottery:set', '积分抽奖设置', '/operate/score/scoreLotteryManage.html', '4ad463daca9a494d9608a4d4b4da3a1e', '90c22e2f12b44f218d25c42b5bbd16a7:4ad463daca9a494d9608a4d4b4da3a1e', '', '1', NULL, '0', '', '1');
/*权限*/
INSERT INTO `sys_permission` (`uuid`, `code`, `permission_name`, `menu_id`, `operation_id`) VALUES ('e54c5ca6848e11e699af0cba25671030', 'oper:scorelottety:record:query', '积分抽奖记录-查询', '7bb123994f7d4d28b81a53ef1319421c', '650237d88b5b4a2ea4fbb1a8c187aa22');
INSERT INTO `sys_permission` (`uuid`, `code`, `permission_name`, `menu_id`, `operation_id`) VALUES ('e54c5ca6848e11e699af0cba25671028', 'oper:scorelottery:set:edit', '积分抽奖设置-编辑', '9e2b9fd6f84a4d13911aeb1fe5cce78e', '90b04389913b41008a69cead0cbf02b7');
INSERT INTO `sys_permission` (`uuid`, `code`, `permission_name`, `menu_id`, `operation_id`) VALUES ('e54c5ca6848e11e699af0cba25671029', 'oper:scorelottery:set:query', '积分抽奖设置-查询', '9e2b9fd6f84a4d13911aeb1fe5cce78e', '650237d88b5b4a2ea4fbb1a8c187aa22');
/*积分抽奖相关表*/
CREATE TABLE `score_lottery` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `lottery_name` varchar(32) DEFAULT '' COMMENT '中奖名称',
  `lottery_type` char(1) DEFAULT '1' COMMENT '中奖类型 1，红包 2，实物',
  `goods_id` varchar(32) DEFAULT '' COMMENT '实物有的话，实物的id，暂时不用',
  `lottery_area` char(1) DEFAULT '0' COMMENT '中奖区域',
  `lottery_value` varchar(32) DEFAULT '' COMMENT '中奖值',
  `rate` decimal(20,2) DEFAULT '0.00' COMMENT '中奖概率',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `status` char(1) DEFAULT '0' COMMENT '状态，1：启用，0：关闭',
  `remark` varchar(128) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='中奖概率表';
CREATE TABLE `user_score_lottery_record` (
  `uuid` varchar(32) NOT NULL COMMENT '主键',
  `user_id` varchar(32) NOT NULL COMMENT '中奖人id',
  `lottery_type` char(1) DEFAULT '1' COMMENT '中奖类型 1，红包 2，实物',
  `goods_id` varchar(32) DEFAULT '' COMMENT '实物有的话，实物的id',
  `lottery_value` varchar(32) DEFAULT '' COMMENT '中奖奖品',
  `score` int(11) DEFAULT '0' COMMENT '消耗的积分',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_ip` varchar(15) DEFAULT '' COMMENT '中奖人ip',
  `status` char(1) DEFAULT '0' COMMENT '状态，0：已中奖，未处理，1：已发奖',
  `remark` varchar(128) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户中奖记录表';

/*积分抽奖1次需要的积分*/
INSERT INTO `sys_config` (`uuid`, `code`, `config_name`, `config_value`, `config_type`, `create_time`, `status`, `remark`, `edit_enable`) VALUES ('fb9273270af946d5bd8a80cc3d279314', 'score_lottery_num', '积分抽奖', '10', '2', '2017-06-05 19:52:44', '1', '积分抽奖1次需要的积分', '1');
INSERT INTO `sys_resource` (`uuid`, `res_name`, `res_text`, `res_language`) VALUES ('e54c5ca6848e11e699af0cba25634570', 'score.lottery.status.error', '积分抽奖概率状态错误!', 'zh_CN');
INSERT INTO `sys_resource` (`uuid`, `res_name`, `res_text`, `res_language`) VALUES ('e54c5ca6848e11e699af0cba25634571', 'score.lottery.success.remark', '积分抽奖，扣除积分{0}分', 'zh_CN');
/*数据字典*/
INSERT INTO `sys_dict_type` (`uuid`, `dict_type`, `type_name`, `create_time`, `remark`) VALUES ('f3506670048e4eaea6bb947e4ac87fc6', 'lotteryType', '抽奖类型', '2017-06-14 19:53:43', '抽奖类型 1红包2实物');
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('97e79fc69ee441c3bb54d2f1cb4b8c20', 'lotteryType', '实物', '2', NULL, '2', '1', '', NULL);
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('a7d6d998eb414fb6a744689f1477a423', 'lotteryType', '积分', '3', NULL, '3', '1', '', NULL);
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('a7d6d998eb414fb6a744689f1477a46d', 'lotteryType', '红包', '1', NULL, '1', '1', '', NULL);

/*红包设置*/
INSERT INTO `redenvelope_rule` (`uuid`, `activity_code`, `rule_name`, `status`, `total_num`, `lssue_num`, `create_time`, `update_time`, `delete_flag`, `grant_type`, `grant_url`, `grant_start_time`, `grant_end_time`, `grant_max`, `grant_min`, `grant_rate`, `use_project_type`, `use_valid_day`, `use_expire_time`, `grant_project_type`, `red_invest_expire_time`) VALUES ('024aa34be5f44562ac660c03b3b8e60b', 'custom', '积分抽奖红包30', '1', '1000', '0', '2017-09-06 12:08:26', '2017-09-06 12:08:27', '0', '1', 'score30', NULL, NULL, '0.0000', '0.0000', '0.0000', NULL, NULL, NULL, '', NULL);
INSERT INTO `redenvelope_rule` (`uuid`, `activity_code`, `rule_name`, `status`, `total_num`, `lssue_num`, `create_time`, `update_time`, `delete_flag`, `grant_type`, `grant_url`, `grant_start_time`, `grant_end_time`, `grant_max`, `grant_min`, `grant_rate`, `use_project_type`, `use_valid_day`, `use_expire_time`, `grant_project_type`, `red_invest_expire_time`) VALUES ('7ec445c75d5f4d75bd0e4d081039bd76', 'custom', '积分抽奖红包10', '1', '1000', '0', '2017-06-19 17:57:33', '2017-06-19 17:57:33', '0', '1', 'score10', NULL, NULL, '0.0000', '0.0000', '0.0000', NULL, NULL, NULL, '', NULL);

INSERT INTO `redenvelope_rule_detail` (`uuid`, `rule_id`, `amount`, `use_tender_money`, `tender_min`, `tender_max`, `grant_rate`, `create_time`) VALUES ('7162eefeb6f140a8819331c80de12c01', '024aa34be5f44562ac660c03b3b8e60b', '30.0000', '1000.0000', NULL, NULL, NULL, '2017-06-13 16:38:32');
INSERT INTO `redenvelope_rule_detail` (`uuid`, `rule_id`, `amount`, `use_tender_money`, `tender_min`, `tender_max`, `grant_rate`, `create_time`) VALUES ('55db418b229c43289847f7c7e5abb6d1', '7ec445c75d5f4d75bd0e4d081039bd76', '10.0000', '1000.0000', NULL, NULL, NULL, '2017-06-19 17:57:33');

/*利息设置*/
INSERT INTO `rate_coupon_rule` (`uuid`, `activity_code`, `rule_name`, `status`, `total_num`, `lssue_num`, `create_time`, `update_time`, `delete_flag`, `grant_type`, `grant_url`, `grant_start_time`, `grant_end_time`, `use_project_type`, `use_valid_day`, `use_expire_time`, `grant_project_type`) VALUES ('9955963d2da4422e8cf6633a45d4bc11', 'custom', '积分抽奖加息0.5%', '1', '1000', '0', '2017-08-28 17:22:51', '2017-08-28 17:22:52', '0', '1', 'score5', NULL, NULL, NULL, NULL, NULL, '');
INSERT INTO `rate_coupon_rule` (`uuid`, `activity_code`, `rule_name`, `status`, `total_num`, `lssue_num`, `create_time`, `update_time`, `delete_flag`, `grant_type`, `grant_url`, `grant_start_time`, `grant_end_time`, `use_project_type`, `use_valid_day`, `use_expire_time`, `grant_project_type`) VALUES ('9955963d2da4422e8cf6633a45d4bc12', 'custom', '积分抽奖加息1.0%', '1', '1000', '0', '2017-08-28 17:22:51', '2017-08-28 17:22:52', '0', '1', 'score10', NULL, NULL, NULL, NULL, NULL, '');
INSERT INTO `rate_coupon_rule_detail` (`uuid`, `rule_id`, `up_apr`, `use_tender_money`, `tender_min`, `tender_max`, `create_time`) VALUES ('b8641b8bae284d8e997b61967a88bb11', '9955963d2da4422e8cf6633a45d4bc11', '0.5000', '1000.0000', NULL, NULL, '2017-08-15 17:22:52');
INSERT INTO `rate_coupon_rule_detail` (`uuid`, `rule_id`, `up_apr`, `use_tender_money`, `tender_min`, `tender_max`, `create_time`) VALUES ('b8641b8bae284d8e997b61967a88bb12', '9955963d2da4422e8cf6633a45d4bc12', '1.0000', '1000.0000', NULL, NULL, '2017-08-15 17:22:52');

/*初始化scoreLottery*/
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd311', '0.5%加息券', '2', '', '1', '5', '10.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd312', '10积分', '3', '', '2', '10', '30.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd313', '30元投资红包', '1', '', '3', '30', '10.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd314', '20积分', '3', '', '4', '20', '20.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd315', '再来一次', '3', '', '5', '10', '10.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd316', '1.0%加息券', '2', '', '6', '10', '10.00', NULL, '1', '');
INSERT INTO `score_lottery` (`uuid`, `lottery_name`, `lottery_type`, `goods_id`, `lottery_area`, `lottery_value`, `rate`, `create_time`, `status`, `remark`) VALUES ('2aa7ceb39f9e4cc4b3cd34b2fa7cd317', '10元投资红包', '1', '', '0', '10', '10.00', NULL, '1', '');
