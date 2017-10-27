import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './pwdReset.less';
import { ValidateFn } from '../../common/validateFn';
import { Button,  Form, Input, Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '提交成功',
    content: v,
  });
}

let ModifyPwd = React.createClass({
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
        url: '/member/security/doModifyPwd.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          // $("#firstStep").remove();        
          // ReactDOM.render(<Success callback={self.settimefun} link={"/member/account/main.html"} linkname={"账户中心"} time={3} text={"初始化密码修改成功"} />, document.getElementById("secondtStep"));
          // $("#secondtStep").show();
          self.props.set(data.realNameStatus); 
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
  settimefun(){
    window.location.href = "/member/account/main.html";
  },
  checkPass(rule, value, callback) {
    const form = this.props.form;
    if (form.getFieldValue('newLoginPwd')) {
      form.validateFields(['confirmPwd'], { force: true });
    }
    callback();
  },
  checkPass4(rule, value, callback) {
    const form = this.props.form;
    if (form.getFieldValue('loginPwd')) {
      form.validateFields(['newLoginPwd'], { force: true });
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
  checkPass3(rule, value, callback) {
    const form = this.props.form; 
    console.log(value);
    if (value && value == form.getFieldValue('loginPwd')) {
      callback('新密码与初始密码不能相同！');
    } else {
      callback();
    }
  },      
  render(){
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const oldPwdProps = getFieldProps('loginPwd',{
       validate: [{
          rules:[
            { required: true, message: '请输入原始密码' },
            { validator: this.checkPass4 }                  
          ],trigger: ['onBlur'],    
        }]
    });
    const pwdProps = getFieldProps('newLoginPwd', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入密码' },
            { validator:ValidateFn.regexPW },
            { validator: this.checkPass3 },
            { validator: this.checkPass }
          ],trigger: ['onBlur'],    
        }]
      
    });
    const rePwdProps = getFieldProps('confirmPwd', {
      rules: [
        { required: true, whitespace: true, message: '请再次输入密码' },
        { validator: this.checkPass2 }
      ],trigger: ['onBlur'],
    });    
    return (
      <div className="account-main" style={{padding:"60px 0 0 0px",height:"560px"}}>                  
        <div className="modifyEmail">
            <div id="firstStep" style={{padding:"0 0 0 365px"}}>
            <div style={{width:400,lineHeight:"52px",color:"#999",padding:"0 0 0 45px"}}><span className="redflag">*</span>为了保障账户安全，请立即修改登录密码。</div>            
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>                               
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">原始密码</div>
                    <div className="col-18 pl20">
                      <FormItem                         
                        hasFeedback
                        help={isFieldValidating('loginPwd') ? '校验中...' : (getFieldError('loginPwd') || []).join(', ')}
                      >
                        <Input {...oldPwdProps} type="password" maxLength="16" placeholder="请输入原始密码" />
                      </FormItem> 
                    </div>
                  </div>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">新密码</div>
                    <div className="col-18 pl20">
                       <FormItem hasFeedback>
                        <Input {...pwdProps} type="password" maxLength="16" placeholder="请输入新密码" />
                      </FormItem>                               
                    </div>
                  </div>
                  <div className="row" style={{width:400}}>
                    <div className="col-6 lab form-laber textR">确认密码</div>
                    <div className="col-18 pl20">
                       <FormItem hasFeedback>
                        <Input {...rePwdProps} type="password" maxLength="16" placeholder="请输入确认密码" />
                      </FormItem>                              
                    </div>
                  </div>                                                                       
                <div className="row" style={{width:400}}>
                  <div className="col-6">&nbsp;</div>
                  <div className="col-18 pl20">
                    <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
                  </div>
                </div>                               
            </Form>
            </div>
        </div>
      </div>
    )
  }  
});

ModifyPwd = createForm()(ModifyPwd);

let ModifyPwdOk = React.createClass({
  render(){

    let flag = false;

    if(this.props.flag == "1"){
      flag = true;
    }
    return (
      <div className="success-content" style={{height:"480px"}}>
        <div className="content-main">
          <div className="iconfont icon">&#xe611;</div>
          <div className="text">恭喜您，登录密码修改成功</div>
          <div className={flag ? "hide" : "btn"}><a href="/member/security/realNameIdentify.html">立即开通</a></div>
          <div className={flag ? "hide" : "remind"}><span className="star">*</span>为了顺利完成资金操作，请开通资金托管账户！</div>
          <div className={flag ? "go" : "hide" }><a href="/member/account/main.html">进入账户中心></a></div>
        </div>
        <div className={flag ? "hide" : "delay"}>暂不开通<a href="/member/account/main.html">进入账户中心></a></div>
      </div>
      )
  }
});

let ModifyPwdMain = React.createClass({
  getInitialState(){
    return {
      step : 1 ,
      isok:""
    }
  },
  changeStep(flag){
    this.setState({
      step : 2,
      isok : flag
    });
  },
  render(){
    let html = "";
    html = this.state.step == 1 ? <ModifyPwd set = {this.changeStep} /> : <ModifyPwdOk flag = {this.state.isok}/>
    return (
      <div>{html}</div>
      )
  }
});

ReactDOM.render(<ModifyPwdMain />,  document.getElementById("j-pwd"));