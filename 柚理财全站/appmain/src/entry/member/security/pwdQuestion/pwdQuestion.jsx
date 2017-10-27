import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './pwdQuestion.less';
import { Button, Select, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const Option = Select.Option;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '登录密码',
    content: v,
  });
}

//通过手机验证
let Phone = React.createClass({
  getcode(e){
    e.target.disabled = true;
    ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {this.callback}/>, document.getElementById("countdown"));    
  },
  render() {
    const { getFieldProps } = this.props.form;    
    const codeProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定手机</div>
          <div className="col-18 pl20">
          <p className="txt">134*****998</p>
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
        </div>;
      </div>
    );
  }
});

//通过邮箱验证
let Email = React.createClass({
  getcode(e){
    e.target.disabled = true;
    ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {this.callback}/>, document.getElementById("countdown"));    
  },
  render() {
    const { getFieldProps } = this.props.form;
    const codeProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定邮箱</div>
          <div className="col-18 pl20">
          <p className="txt">p****1@qq.com</p>
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
        </div>;
      </div>
    );
  }
});

let PwdQuestion = React.createClass({
  getInitialState(){
    return {
      type: '01',
    }
  },
  handleSubmit(e) {
    e.preventDefault();  
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');        
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      
      $.ajax({
        url: '/member/security/doResetPwdQuestion.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
                 
        }
        else
        {
          error(data.msg);
          $(".codeimg")[0].click();
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });

  },
  callback(){
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    e.target.disabled = true;
    ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {this.callback}/>, document.getElementById("countdown"));    
  },
  typeChangeHander(value){
    this.setState({type: value});
  },
  backArrValHander(arr, key){
    return arr[key];
  },
  componentDidMount(){
    
  },
  render(){
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const componentArr = [<Phone form={this.props.form}/>, <Email form={this.props.form}/>, <PwdQuestion form={this.props.form}/>];
    let html = this.backArrValHander(componentArr, parseInt(this.state.type)-1); 
    return (
        <div className="account-main">                	
        	<div className="main-title">
        		重置密保问题
        		<a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
        	</div>
        	<div className="pwdQuestion">
              <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">验证方式</div>
                    <div className="col-18 pl20">
                      <FormItem>                        
                        <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                          <Option value="01">通过手机验证</Option>
                          <Option value="02">通过邮箱验证</Option>                                                         
                        </Select>
                      </FormItem>
                    </div>
                  </div>
                  <div>
                  <div>{html}</div>
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
PwdQuestion = createForm()(PwdQuestion);
ReactDOM.render(<PwdQuestion />,  document.getElementById("j-pwdQuestionmain"));
//ReactDOM.render(<BindEmail />,  document.getElementById("j-bindEmailmain"));
if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
}