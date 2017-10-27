import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './modifyPwd.less';
import { Modal, Button, Form, Input} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    onOk(){
      if(url){
        window.location.href = url;
        }
    },
  });
}

let ModifyPwd = React.createClass({
   getInitialState() {
    return {
      passBarShow: false, // 是否显示密码强度提示条
      rePassBarShow: false,
      passStrength: 'L', // 密码强度
      rePassStrength: 'L',
    };
  },
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      console.log(values);
      $.ajax({
        url: '/member/security/doModifyPwd.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success(data.msg,"/member/baseInfo/index.html");
        }
        else
        {
          error(data.msg);
        }
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });

  },
  checkPass(rule, value, callback) {
    const form = this.props.form;
    if (form.getFieldValue('newLoginPwd')) {
      form.validateFields(['confirmPwd'], { force: true });
    }
    callback();
  },

  checkPass2(rule, value, callback) {
    const form = this.props.form;
    console.log(value);
    if (value && value !== form.getFieldValue('newLoginPwd')) {
      callback('两次输入密码不一致！');
    } else {
      callback();
    }
  },
  render(){
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const oldPwdProps = getFieldProps('loginPwd',{
       validate: [{
          rules:[
            { required: true, message: '请输入原始密码' }
          ],trigger: ['onBlur'],
        }]
    });
    const pwdProps = getFieldProps('newLoginPwd', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入密码' },
            { validator:ValidateFn.regexPW },
            { validator: this.checkPass }
          ],trigger: ['onBlur'],
        }]

    });
    const rePwdProps = getFieldProps('confirmPwd', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请再次输入密码' },
            { validator: this.checkPass2 }
          ],trigger: ['onBlur'],
      }]
    });
    return (
        <div className="account-main">
        	<div className="main-title">
        		修改登录密码
        		<a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
        	</div>
        	<div className="modifyPwd">
              <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">原始密码</div>
                    <div className="col-18 pl20">
                      <FormItem
                        hasFeedback
                        help={isFieldValidating('oldPwd') ? '校验中...' : (getFieldError('oldPwd') || []).join(', ')}
                      >
                        <Input {...oldPwdProps} type="password" maxLength="16" placeholder="请输入原始密码" />
                      </FormItem>
                    </div>
                  </div>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">新密码</div>
                    <div className="col-18 pl20">
                  	   <FormItem hasFeedback>
                        <Input {...pwdProps} type="password" maxLength="16" placeholder="不少于8位，至少包含一个字母和一个数字" />
                      </FormItem>
                    </div>
                  </div>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">确认新密码</div>
                    <div className="col-18 pl20">
                       <FormItem hasFeedback>
                        <Input {...rePwdProps} type="password" maxLength="16" placeholder="不少于8位，至少包含一个字母和一个数字" />
                      </FormItem>
                    </div>
                  </div>
                  <div className="row" style={{width:400}}>
                    <div className="col-6">&nbsp;</div>
                    <div className="col-18 pl20">
                      <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >确定</Button></FormItem>
                    </div>
                  </div>
              </Form>
        	</div>
        </div>
      )
  }
});
ModifyPwd = createForm()(ModifyPwd);
ReactDOM.render(<ModifyPwd />,  document.getElementById("j-modifyPwdmain"));
if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
}
