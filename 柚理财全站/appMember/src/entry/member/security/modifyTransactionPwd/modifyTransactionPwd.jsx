import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './modifyTransactionPwd.less';
import { Button, Select, Form, Modal, Input} from 'antd';
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

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}

//通过手机验证
let TransactionPwd = React.createClass({
    getInitialState() {
      return {
        timeflag:false,
        visible:false
      };
    },
   handleSubmit(e) {
   
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {   
       e.preventDefault();     
        console.log('Errors in form!!!');        
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      let name=$("body").attr("data-tokenname");
      values[name]=$("body").attr("data-token");
      const self = this;
      self.setState({
        visible : true
      });
      /*$.ajax({
        url: '/member/security/resetPwd.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
            success()
        }
        else
        {
          error(data.msg);
          //$("#countdown_phone")[0].click();
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
      data: {reqType:this.props.reqType,srvTxCode:this.props.srvTxCode},
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
    window.location.href = "/member/baseInfo/index.html";
  },
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;  
    const codeProps = getFieldProps('smsCode', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });       
    return (
      <div className="account-main">
        <div className="main-title">修改交易密码</div>
        <Form horizontal className="form-themes" target="_blank" method="post" action="/member/security/resetPwd.html" form={this.props.form} onSubmit={this.handleSubmit}>
          <div className="row" style={{width:400}}>
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
                <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >提交</Button></FormItem>
              </div>
            </div>                               
        </Form>
        <Modal className="orderPayModal" ref="modal"
            visible={this.state.visible}
            title="修改交易密码" onOk={this.handleOk} onCancel={this.handleCancel}
            width={480}
            footer={[
              <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                修改成功
              </Button>,
              <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>修改失败</Button>,
            ]}
          >
          <div className="modal-content"><i className="iconfont">&#xe62e;</i><p>请在新打开的资金托管账户完成修改交易密码</p></div>
          </Modal>
      </div>
    );
  }
});

TransactionPwd = createForm()(TransactionPwd);
ReactDOM.render(<TransactionPwd mobile={$("#mobile").val()} reqType={$("#reqType").val()} srvTxCode={$("#srvTxCode").val()} />,  document.getElementById("j-bindPhonemain"));
ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));