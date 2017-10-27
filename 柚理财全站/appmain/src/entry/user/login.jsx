import '../../common/lib';

//import {envurlinfo} from '../../../themes/js/env.js';
import ReactDOM from 'react-dom';
import React from 'react';
import './login.less';
import  { ValidateFn } from '../../common/validateFn';
import  { Reg } from '../../common/regularFun';
import { Button, Form, Input,Modal,Tooltip} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function noop() {
  return false;
}

function error(a) {
  Modal.error({
    title: a,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

let Loginform = React.createClass({
  handleSubmit(e) {
    var isRsaVal= $("#isRsa").val();
    if(isRsaVal==1){
      var encrypt = new JSEncrypt();
     var pubKey = $("#pubKey").val();
      encrypt.setPublicKey(pubKey); 
      var encrypted = encrypt.encrypt($("#loginPwd").val());
    }

    var that = this;
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      console.log('Submit!!!');
      if(isRsaVal==1){
        values.loginPwd = encrypted;
      }
      console.log(values);
      $.ajax({
        url: '/user/doLogin.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          if(data.pwdResetFlag){
            window.location.href = '/user/pwdReset.html';
          }else
            {
            if(that.props.url){
              window.location.href = that.props.url;
            }
            else
            {
              window.location.href = '/member/account/main.html';
            }
          }    
        }
        else
        {
          that.props.form.resetFields(["validCode"]);
          $(".codeimg")[0].click();
          if(data.showCaptcha){
            that.setState({
              ischeck:true
            });
            that.refs.imgcoderow.className = 'row show';
            that.changeimg();
          }
          error(data.msg);
        } 
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
      
    });
  },
  getInitialState() {
    return {
      imgsrc: '/validimg.html',
      ischeck:false,
      imgcodeerrorbg:false
    };
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random() });
  },
  isloginName(rule, value, callback){//登录名检测
    if(!value){
      callback();
    }else if(Reg.islogname(value)){        
        callback([new Error('抱歉，请输入正确的用户名/手机号码/电子邮箱')]);        
    }else{
        callback();                        
    }    
  },
  checkPass(rule, value, callback) {
    const { validateFields } = this.props.form;
    if (/\s/.test(value)) {
        callback([new Error('密码不能包含空格')]);
    }
      callback();    
  },
  check(e){
      let keyCode  =  e.keyCode||e.which;
      let isShift  =  e.shiftKey ||(keyCode  ==   16 ) || false ;
      if(((keyCode >=  65 && keyCode <= 90 ) && !isShift) || ((keyCode >=   97   &&  keyCode  <=   122 )  &&  isShift))
      {
        $("#j-capsLock").show();
      }
     else
      {
        $("#j-capsLock").hide();
      }
  },
  close(){
    $("#j-capsLock").hide();
  },
  getInfo(){
      const that = this;
      $.ajax({
        url: '/user/showCaptcha.html',
        type: 'POST',
        dataType: 'json'
      })
      .done(function(data) {        
        if(data.result){
            that.setState({
              ischeck:true
            });
            that.refs.imgcoderow.className = 'row show';
        }      
      })
      .fail(function() {
          //callback([new Error('网络异常，请重试')]);
      });    
  },
  componentWillMount(){
    this.getInfo();
  },
  render() {
    const isRsa = $("#isRsa").val()==1?'1':'0';
    const encrypt = $("#encrypt").val()==1?'1':'0';
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const nameProps = getFieldProps('loginName', {
      validate: [
        {
          rules: [
            { required: true, message: '请填写用户名/手机号码/电子邮箱' },
            { validator: this.isloginName }
          ],trigger: ['onBlur']
        }
      ]
    }); 
    const passwdProps = getFieldProps('loginPwd', {
      validate: [
      {
      rules: [
        { required: true, whitespace: true, message: '请填写密码' }
        ],trigger: ['onBlur']
      },
      {
      rules: [
        {
          validator:this.checkPass
        }
        ],trigger: ['onBlur']
      }
      ]
    });
    const imgcodeProps = getFieldProps('validCode', {
      validate: [
        {
        rules: [
          { required: this.state.ischeck,whitespace: true, message: '请填写验证码' }
          ],trigger: ['onBlur']
        }
      ]  
    });
    const formItemLayout = {
      wrapperCol: { span: 20 ,offset: 2}
    };
    const formItemLayoutSm = {
      wrapperCol: { span: 10 ,offset: 2}
    };
    const redirectURLProps = getFieldProps('redirectURL',{
        initialValue:""
    });
   const isRsaProps = getFieldProps('isRsa',{
        initialValue:isRsa
    });
    const isEncrypt = getFieldProps('encrypt',{
        initialValue:encrypt
    });
    return (      
      <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="login-title">登录</div>
        <FormItem
          {...formItemLayout}         
          hasFeedback
        >
          <Input {...nameProps} maxLength="50" placeholder="用户名/手机号码/电子邮箱" />
        </FormItem>
  
        <FormItem
          {...formItemLayout}          
          hasFeedback
        >
            <Input {...passwdProps} placeholder="登录密码" maxLength="16" type="password" autoComplete="off"
              onContextMenu={noop} onPaste={noop} onCopy={noop} onCut={noop}  onKeyPress = {this.check} onBlur ={this.close} size="large"  />
        </FormItem>

        <div id="j-capsLock" className="ant-tooltip  ant-tooltip-placement-top  ant-tooltip-hidden" ><div className="ant-tooltip-content"><div className="ant-tooltip-arrow" ></div><div className="ant-tooltip-inner">大写已打开</div></div></div>

        <div className="row hide" ref="imgcoderow">
          <div className="col-10 col-offset-2"><FormItem hasFeedback><Input {...imgcodeProps} placeholder="验证码" maxLength="4" size="large" type="text" autoComplete="off" /></FormItem></div>
          <div className="col-8 col-offset-4"><img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" /></div>
        </div>          
        <Input {...redirectURLProps}  type="hidden" />
        <Input {...isRsaProps}  type="hidden" />
        <Input {...isEncrypt}  type="hidden" />
        <div className="ant-form-item forget-reg-link"><div className="col-20 col-offset-2 forget-reg-link-cont"><a href="/user/retrievepwd/index.html">忘记密码?</a><a href="/user/register.html" className="fr">免费注册</a></div></div>
        <FormItem wrapperCol={{ span: 20, offset: 2}}>
          <Button  type="primary" htmlType="submit">立即登录</Button>
        </FormItem>
      </Form>
    );
  },
});

Loginform = createForm()(Loginform);

ReactDOM.render(<Loginform url={$(".login-body").data("url")} />, document.getElementById("loginbox"));