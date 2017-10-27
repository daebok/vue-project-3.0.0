import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './email.less';
import { Button, Form, Input} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

let Payaccount = React.createClass({
  handleSubmit(e) {
    e.preventDefault();  
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');        
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      
      // $.ajax({
      //   url: '/user/registerFirst.html',
      //   type: 'POST',
      //   dataType: 'json',
      //   data: values
      // })
      // .done(function(data) {
      //   if(data.result){
                 
      //   }
      //   else
      //   {
      //     error(data.msg);
      //     $(".codeimg")[0].click();
      //   }  
      // })
      // .fail(function() {
      //   error('网络异常，请重试！');
      // });
    });

  },
  callback(){
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="点击获取验证码";
  },
  getcode(e){
    e.target.disabled = true;
    ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {this.callback}/>, document.getElementById("countdown"));    
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
            const codeProps = getFieldProps('code', {
              rules: [
                { required: true, whitespace: true, message: '请输入验证码' }
                ]
            });
            return (
                <div className="account-main">
                    <div className="main-title">绑定邮箱</div>
                    <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                        <div className="row" style={{width:400}}>
                          <div className="col-6 lab form-laber textR">电子邮箱</div>
                          <div className="col-18 pl20">
                            <FormItem                         
                              hasFeedback
                              help={isFieldValidating('email') ? '校验中...' : (getFieldError('email') || []).join(', ')}
                            >
                              <Input {...emailProps} maxLength="250" placeholder="请填写电子邮箱" />
                            </FormItem> 
                          </div>
                        </div>
                        <div className="row" style={{width:400}}>
                          <div className="col-6 lab form-laber textR">验证码</div>
                          <div className="col-9 pl20">
                            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
                            </FormItem>
                          </div>
                          <div className="col-9 pl20">
                          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown">点击获取验证码</button>
                          </div>
                        </div>                      
                        <div className="row" style={{width:400}}>
                          <div className="col-6">&nbsp;</div>
                          <div className="col-18 pl20">
                            <FormItem><Button className="submit-button" type="primary" htmlType="submit" >立即开通</Button></FormItem>
                          </div>
                        </div>                               
                    </Form>
                </div>
              )
          }  
});
Payaccount = createForm()(Payaccount);

ReactDOM.render(<Payaccount />,  document.getElementById("j-paydetailmain"));

ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));