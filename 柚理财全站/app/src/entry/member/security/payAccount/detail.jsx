import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './detail.less';
import { Button, Form, Input,Modal} from 'antd';
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

let Payaccount = React.createClass({
  getInitialState() {
    return {
      visible:false
    };
  },
  handleSubmit(e) {    
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      this.setState({ visible: true });     
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
  handleOk() {
    this.setState({ visible: false });
    window.location.reload();
  },  
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },
  regexIdCard(rule, value, callback) {
    if(!value){
      callback();
    }else{
      if(!Reg.regexIdCard(value)){        
        callback([new Error('身份证格式不正确,仅限数字和大写X')]);        
      }else{
      var that = this;
      if(this.state.flag == value){
        callback();
        return;
      }
      $.ajax({
        url: '/user/checkCardId.html',
        type: 'POST',
        dataType: 'json',
        data:{cardId:value}
      })
      .done(function(data) {
        if(data.result){
          that.state.flag = value;
          callback();   
        }
        else
        {
          callback([new Error(data.msg)]); 
        }  
      })
      .fail(function() {
          callback([new Error('网络异常，请重试')]);
      });
      }
    }
  },
  render(){
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const nameProps = getFieldProps('realName', {
              rules: [
                { required: true, min: 2, message: '用户名至少为 2 个字符' },
                //max: 6, message: '用户名最多为 10个字符' },
                { validator: ValidateFn.regexChnName },
              ],
            });
            const idCardProps = getFieldProps('idNo', {
              validate: [{
                rules: [
                  { required: true, message: '请填写您的身份证号码' },
                ],
                trigger: 'onBlur',
              }, {
                rules: [
                  { validator: this.regexIdCard },
                ],
                trigger: ['onBlur'],
              }],
            });
            const returnUrlProps = getFieldProps("returnUrl",{
                //initialValue:"/manage/security/realNameIdentify.html?type=success"
                initialValue:"/member/security/realNameIdentify.html"
            }); 
            return (
                <div className="account-main">
                    <div className="main-title">开通托管账户</div>
                    <Form horizontal className="form-themes" action="/member/security/tppRegister.html" target="_blank" method="post" form={this.props.form} onSubmit={this.handleSubmit}>
                        <div className="row" style={{width:400}}>
                          <div className="col-6 lab form-laber textR">真实姓名</div>
                          <div className="col-18 pl20">
                            <FormItem                         
                              hasFeedback
                              help={isFieldValidating('realName') ? '校验中...' : (getFieldError('realName') || []).join(', ')}
                            >
                              <Input {...nameProps} name="realName" maxLength="10" placeholder="请填写您的真实姓名" />
                            </FormItem> 
                          </div>
                        </div>
                        <div className="row" style={{width:400}}>
                          <div className="col-6 lab form-laber textR">身份证号码</div>
                          <div className="col-18 pl20">
                          <FormItem hasFeedback ><Input {...idCardProps} name="idNo" maxLength='18' placeholder="请填写您的身份证号码" /></FormItem>
                          </div>
                        </div>
                        <Input {...returnUrlProps} name="returnUrl" type="hidden" />                      
                        <div className="row" style={{width:400}}>
                          <div className="col-6">&nbsp;</div>
                          <div className="col-18 pl20">
                            <FormItem><Button className="submit-button" type="primary" htmlType="submit" >立即开通</Button></FormItem>
                          </div>
                        </div>                               
                    </Form>
                    <Modal ref="modal"
                      visible={this.state.visible}
                      title="开通托管账户"
                      width={480}
                      onCancel={this.handleCancel}
                      footer={[                      
                        <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                          开户申请成功
                        </Button>,
                        <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>开户申请失败</Button>,
                      ]}
                    >
                    <div className="modal-content payaccount"><em className="iconfont">&#xe62e;</em>请在新打开页面中进行开户操作</div>
                    </Modal>                    
                </div>
              )
          }  
});
Payaccount = createForm()(Payaccount);

let Regok = React.createClass({
  render(){
    const data = this.props.data;
    const content = <dl><dt><span className="success">已经绑定汇付天下</span><s>汇付天下账户：</s>{this.props.data.tppUserCustId}</dt><dd><a href={this.props.data.tpp_login_url} target="_blank">进入汇付天下</a></dd></dl>;  

    return (
      <div className="account-main">
        <div className="main-title">托管账户</div>
        <div className="about">
          {content}
          <div className="help">
            <ol>
              <li>汇付天下托管账户系统，是汇付天下为P2P平台提供的资金托管账户体系，保障用户账户中的资金独立存放，P2P平台无权动用。</li>
              <li>采用汇付天下作为第三方支付及资金托管平台，充值、提现、投资支付等操作均在汇付天下平台上进行。</li>
              <li>因无法触碰用户资金账户，为了您的资产安全，请牢记汇付天下账户的登录密码及支付密码，并定期修改。</li>
              <li>请勿使用他人身份信息进行汇付天下账户开户、充值、提现及投资等操作。</li>
            </ol>
          </div>
        </div>
      </div>              
      )
  }
});

function getinfo(){
    $.ajax({
      url: '/member/security/getIdentifyResult.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data){
      if(data.result == false){
        error(data.msg);
      }
      else
      {
        
        if(data.realNameStatus != "0"){
          ReactDOM.render(<Regok data={data}/>,  document.getElementById("j-paydetailmain"));
        }
        else
        {
          ReactDOM.render(<Payaccount />,  document.getElementById("j-paydetailmain"));     
        } 

      }  
    })
    .fail(function() {
      error('网络异常，请重试！');
    });
}

getinfo();

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
} 