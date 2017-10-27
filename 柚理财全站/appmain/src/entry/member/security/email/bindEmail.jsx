import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './bindEmail.less';
import { Button, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(a) {
  Modal.error({
    title: a,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v,url) {
  Modal.success({
    title: '成功提示',
    content: v,
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}

let BindEmail = React.createClass({

  handleSubmit(e) {
    e.preventDefault();  
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');        
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      let self = this;      
      $.ajax({
        url: '/member/security/doBindEmail.html',
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
          self.callback();
          self.props.form.resetFields();          
        }  
      })
      .fail(function() {
        self.callback();
        error('网络异常，请重试！');
      });
    });

  },
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    const { isFieldValidating, getFieldError, getFieldValue} = this.props.form;
    if(this.codeflag || !this.refs.email.props.value || getFieldError("email")){
      return;
    }
    const self = this;
    this.codeflag = true;
    e.target.disabled = true;
    $.ajax({
      url: '/member/security/bindEmailCode.html',
      type: 'POST',
      dataType: 'json',
      data:{email:self.refs.email.props.value}     
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));    
      } else {
        
        self.refs.countdownbtn.disabled = false;
        self.codeflag = false;
        error(data.msg);

      }      
    })
    .fail(function() {
      self.callback();
      error('网络异常，请重试！');
    })      
  },  
  render(){
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const emailProps = getFieldProps('email',{
                validate: [
                {
                  rules:[
                    { required: true, message: '请输入电子邮箱' }
                  ],
                   trigger: ['onBlur'],
                },
                {
                  rules:[
                    {validator:ValidateFn.isEmail},
                  ], trigger: ['onBlur'],
                }
                ]
            });
            const emailCodeProps = getFieldProps('code', {
              rules: [
                { required: true, whitespace: true, message: '请输入验证码' }
                ]
            });
            return (
                <div className="account-main">                	
                	<div className="main-title">
                		绑定邮箱
                		<a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
                	</div>
                	<div className="bindEmail">
	                    <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
	                        <div className="row" style={{width:400}}>
	                          <div className="col-6 lab form-laber textR">电子邮箱</div>
	                          <div className="col-18 pl20">
	                            <FormItem                         
	                              hasFeedback
	                              help={isFieldValidating('email') ? '校验中...' : (getFieldError('email') || []).join(', ')}
	                            >
	                              <Input {...emailProps} ref="email" maxLength="50" placeholder="请输入电子邮箱" />
	                            </FormItem> 
	                          </div>
	                        </div>	                        
	                        <div className="row" style={{width:400}}>
	                          <div className="col-6 lab form-laber textR">验证码</div>
	                          <div className="col-9 pl20">
	                            <FormItem hasFeedback><Input {...emailCodeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
	                            </FormItem>
	                          </div>
	                          <div className="col-9 pl20">
	                          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown">点击获取验证码</button>
	                          </div>
	                        </div>                      
	                        <div className="row" style={{width:400}}>
	                          <div className="col-6">&nbsp;</div>
	                          <div className="col-18 pl20">
	                            <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >立即开通</Button></FormItem>
	                          </div>
	                        </div>                               
	                    </Form>
                	</div>
                </div>
              )
          }  
});
BindEmail = createForm()(BindEmail);
ReactDOM.render(<BindEmail />,  document.getElementById("j-bindEmailmain"));
if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
}  