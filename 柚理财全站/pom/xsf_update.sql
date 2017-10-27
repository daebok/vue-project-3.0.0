use youlicai;
/*增加文章的状态列*/
alter table article add COLUMN `status` char(1) DEFAULT '0' COMMENT '待审核:0,审核通过:1,发布:2';
/*增加用户重置密码的状态*/
alter table sys_operator add column pwd_flag char(1) DEFAULT '0' COMMENT '0 未重置密码,1 已重置密码';
/*增加用户登录失败次数和锁定时间*/
alter table sys_operator add column login_fail_times int(1) DEFAULT NULL COMMENT '登录失败次数';
alter table sys_operator add column lock_time datetime DEFAULT NULL COMMENT '锁定时间';/
/*增加文章状态*/
INSERT INTO `sys_dict_type` (`uuid`, `dict_type`, `type_name`, `create_time`, `remark`) VALUES ('1885e8ded4124bf2957708dd6b04cb88', 'articleStatus', '文章状态', '2017-08-31 16:22:11', '文章状态');
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('3ea8b10a77924192b2dbbc94515308fe', 'articleStatus', '待审核', '0', NULL, '0', '1', '文章待审核', NULL);
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('584c1b8a77fc4ce5972412a3e8b19beb', 'articleStatus', '已发布', '2', NULL, '2', '1', '已发布', NULL);
INSERT INTO `sys_dict_data` (`uuid`, `dict_type`, `item_name`, `item_value`, `create_time`, `sort`, `status`, `remark`, `expression`) VALUES ('82805f1fb3bf44f3a71bae379c0a22f2', 'articleStatus', '审核通过', '1', NULL, '1', '1', '审核通过但未发布', NULL);
/*将登录失败次数放到了redis里，不存在数据库中*/
alter table sys_operator drop COLUMN login_fail_times;
/*增加红包适用投资期限字段
 * 9-13号
 * */
alter table redenvelope_rule add COLUMN `red_invest_expire_time` int(11) DEFAULT NULL COMMENT '红包适用投资期限(以月为单位)';

/*10-26号*/

INSERT INTO sys_config (`uuid`, `code`, `config_name`, `config_value`, `config_type`, `create_time`, `status`, `remark`, `edit_enable`) VALUES ('275403b54bfd4f69b7e21929623fd3af', 'user_login_form_encrypt_enable', '是否启用用户登录页面表单密码加密', '1', '6', '2017-10-26 15:52:04', '1', '开启后登录会加密', '1');
INSERT INTO sys_config (`uuid`, `code`, `config_name`, `config_value`, `config_type`, `create_time`, `status`, `remark`, `edit_enable`) VALUES ('fb9273270af946d5bd8a80cc3d279432', 'user_login_form_encrypt_public', '登录页面表单密码加密公钥', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfU3jYhR8QzD+0x7SG/gGuQ9w3JOlJJ2CNbN8eAdiDKCPZ9gektP52tZE31txQ5NQraqW13+un16jJTs/iumQVw2n9L5ZmXCDZueNWrOwaaTA6vHs9KD9SgEVM6+7ml0NJpRBeBEr58Dtjzh51f6YDVn0nJeJ6pr0GdWXi3kNqMQIDAQAB', '2', '2017-10-26 15:52:04', '1', '是否启用：1启用 0关闭', '0');
INSERT INTO sys_config (`uuid`, `code`, `config_name`, `config_value`, `config_type`, `create_time`, `status`, `remark`, `edit_enable`) VALUES ('fb9273270af946d5bd8a80cc3d279433', 'user_login_form_encrypt_private', '登录页面表单密码加密私钥', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9TeNiFHxDMP7THtIb+Aa5D3Dck6UknYI1s3x4B2IMoI9n2B6S0/na1kTfW3FDk1CtqpbXf66fXqMlOz+K6ZBXDaf0vlmZcINm541as7BppMDq8ez0oP1KARUzr7uaXQ0mlEF4ESvnwO2POHnV/pgNWfScl4nqmvQZ1ZeLeQ2oxAgMBAAECgYAXUQjzbu/v7mQ4Wa2Sv+ORFD9LFqzJVujraY5xfsWn1B0DDd1qfk5rIwFAkcImWIawX+gmaMG9C3OZGl6UCMESrr+0E7cQceyoz3vHuY2uRuJZq7yfnQyJKmPZ5kf+tY4T9DgfgYvt/J+xz9X5IGQYvUiLpZd9iXLrQYHqoehJbQJBAOOzxbIfJTlF5EU2dKDzM9uKdKeDiZvt4k6tXHM107fstk1rz/NA78IyorRs/uj9hPH82WIwfC9XNMwBhK6+0BMCQQCzIFj6HL0XuNqZoKcTb2O45eYpudIXl2Q2DPKu9pkrmh5fFnSqNhEa2fO/PaYktJTiL0vdm/hNX3KTEr2xdI0rAkEAkwJV+RIyri95mVX3JpLeQDe76Qr7pTiIi9NRhPCTqIOjj4iz0ZFzOiYG9gYI7dQAKVvd3Y8AHnBnHe89ArUfEQJBAIIDaJGhal5dfc0kHiCtKOR7eaOvjB4zdDkHDN6Rfnt3UbQSyHsC40dqCtE0HfNmXuoNCjO/kWoXbUHyyFyVDCECQD7TBTc9JDQ1fJItyL/QrqmXH4ixOjaHS5ZC0eeWxkHdlsTbzibonqk6YC9R0628DB+MYkCdi4rouvvS7V8f50o=', '2', '2017-10-26 15:52:04', '1', '是否启用：1启用 0关闭', '0');
