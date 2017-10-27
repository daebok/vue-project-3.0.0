import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import CountDown from '../../../component/countDown/CountDownComponent';
import './autoCreditInvestAuth.less';
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
    onOk() {
      window.location.reload();
    },
  });
}

function showerror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请开通第三方资金托管账户',
    okText:'开通托管账户',
    iconType:'question-circle',
    align:{top:'200px'},
    onOk() {
        window.location.href = "/member/security/realNameIdentify.html";
    },
    onCancel() {},
  });
}

if($("#j-realNameStatus").val()!=1){
     showerror()
}

let AutoCreditInvestAuth = React.createClass({
  getInitialState() {
    return {
      visible:false,
      timeflag:false
    };
  },
  handleSubmit(e) {    
    this.props.form.validateFields((errors, values) => {
      //e.preventDefault();
      if (!!errors) {        
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      }
      console.log('Submit!!!');
      console.log(values);
     this.setState({ visible: true });     
      /*$.ajax({
        url: '/member/security/doAutoCreditInvestAuth.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
            success(data.msg);     
        }
        else
        {
          error(data.msg);
          //$(".codeimg")[0].click();
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });*/
    });
  },
  getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target;
    $.ajax({
      url: '/member/security/smsCodeApply.html',
      type: 'POST',
      dataType: 'json',
      data: {"mobile": this.props.mobile,"reqType":"1","srvTxCode":"autoCreditInvestAuthPlus"},
    })
    .done(function(data){
        if(data.result == false)
        {
          error(data.msg);
          self.callback();

        }
        else
        {
          self.setState({
            timeflag : true
          });
          el.disabled = "disabled";
          ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown_phone"));         
        }      
    })
    .fail(function() {
                self.callback();

      error('网络异常，请重试！');
    });    
  },
  callback(){
    this.setState({
      timeflag : false
    }); 
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  handleOk() {
    this.setState({ visible: false });
    window.location.href = "/member/baseInfo/index.html";
  },  
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },
  render(){
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const codeProps = getFieldProps('smsCode', {
              rules: [
                { required: true, whitespace: true, message: '请输入验证码' }
                ]
            });
            return (
                <div className="account-main">
                    <div className="main-title">债权转让授权</div>
                    <Form horizontal className="form-themes" target="_blank"
                      action="/member/security/doAutoCreditInvestAuth.html" method="post" form={this.props.form} onSubmit={this.handleSubmit}>
                        <div className="row" style={{width:"400px",marginBottom:"20px",marginTop:"20px"}}>
                          <div className="col-6 lab form-laber textR">已绑定手机</div>
                          <div className="col-18 pl20">
                          <p className="txt">{this.props.mobile}</p>
                          </div>
                        </div>
                        <div className="row" style={{width:400}}>
                          <div className="col-6 lab form-laber textR">验证码</div>
                          <div className="col-9 pl20">
                            <FormItem hasFeedback><Input {...codeProps} name="smsCode" type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
                            </FormItem>
                          </div>
                          <div className="col-9 pl20">
                          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_phone">点击获取验证码</button>
                          </div>
                        </div>                
                        <div className="row" style={{width:400}}>
                          <div className="col-6">&nbsp;</div>
                          <div className="col-18 pl20">
                            <FormItem><Button className="submit-button" type="primary" htmlType="submit" >提交</Button></FormItem>
                          </div>
                        </div>                               
                    </Form>
                    <Modal ref="modal"
                      visible={this.state.visible}
                      title="债权转让授权"
                      width={480}
                      onCancel={this.handleCancel}
                      footer={[                      
                        <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                          授权成功
                        </Button>,
                        <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>授权失败</Button>,
                      ]}
                    >
                    <div className="modal-content payaccount"><em className="iconfont">&#xe62e;</em>请在新打开页面中进行开户操作</div>
                    </Modal>                   
                </div>
              )
          } 
});
AutoCreditInvestAuth = createForm()(AutoCreditInvestAuth);

ReactDOM.render(<AutoCreditInvestAuth mobile={$("#mobile").val()} />,  document.getElementById("j-paydetailmain"));  

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
} 