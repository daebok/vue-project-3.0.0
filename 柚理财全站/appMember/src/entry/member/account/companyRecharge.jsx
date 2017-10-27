import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import CountDown from '../../../component/countDown/CountDownComponent';
import  { ValidateFn} from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import './recharge.less';
import { Button, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function showerror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请开通第三方资金托管账户',
    okText:'开通托管账户',
    iconType:'question-circle',
    onOk() {
        window.location.href = "/member/security/realNameIdentify.html";
    },
    onCancel() {},
  });
}

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: v,
    content: '',
    okText:"确定",
    onOk() {
      window.location.href = "/member/recharge/index.html";
    }   
  });
}

if($("#j-realNameStatus").val()!=1){
   //showerror()
}


let CompanyRecharge = React.createClass({
  getInitialState() {
    return {
    };
  },
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      
      if (!!errors) {
        console.log('Errors in form!!!');
        //e.preventDefault();
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      //this.setState({ visible: true });
      $.ajax({
        url: '/member/recharge/doRecharge.html',
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
      });
    });

  },
  componentDidMount() {
  },
  render(){
            if(this.state.data == ''){
                      return false;
            }
            let tppUserCustId =$("#tppUserCustId").val();
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const tppUserCustIdProps = getFieldProps('tppUserCustId', {
              initialValue: tppUserCustId
            });
            return (
                <div className="account-main">
                <div className="main-title">充值</div>
                  <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                      <div className="row" style={{width:400,marginBottom:"20px",marginTop:"50px"}}>
                        <div className="col-6 lab form-laber textR">账户余额</div>
                        <div className="col-18 pl20 form-text lab">
                          <em>{this.props.money}</em> 元
                        </div>
                      </div>
                      <div className="row" style={{width:400,marginBottom:"20px"}}>
                        <div className="col-6 lab form-laber textR">线下账号</div>
                        <div className="col-18 pl20">
                          <FormItem hasFeedback><Input {...tppUserCustIdProps} id="tppUserCustId" placeholder="请输线下充值账号" readOnly={true} maxLength="11" /></FormItem>
                        </div>
                      </div>
                      <div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20">
                          <FormItem><Button className="subbutton" type="primary" htmlType="submit" >同步数据</Button></FormItem>
                        </div>
                      </div>
                      {/*<div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20 remind">
                          <span className="star">*</span>充值操作将在资金托管账户网站进行
                        </div>
                      </div>*/}
                  </Form>
                  <div className="help">
                    <div className="help-title"><em className="iconfont">&#xe639;</em>温馨提示</div>
                    <ol>
                     {/* <li>充值/提现必须为开通网上银行的借记卡，不支持存折、信用卡充值。</li>
                      <li>充值期间，请勿关闭浏览器，待充值成功并返回后，可在网站中查看充值金额。</li>*/}
                      <li>严禁信用卡套现、虚假交易等行为。</li>
                    </ol>
                  </div>

                  <Modal  className="orderPayModal" ref="modal"
                    visible={this.state.visible}
                    title="登录资金账户完成充值" onOk={this.handleOk} onCancel={this.handleCancel}
                    width={480}
                    footer={[
                      <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                        充值成功
                      </Button>,
                      <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>充值失败</Button>,
                    ]}
                  >
                  <div className="modal-content clearfix"><em className="iconfont fl">&#xe62e;</em><p className="fl">请在新打开的资金托管账户完成充值</p></div>
                  </Modal>
                </div>
              )
          }
});
CompanyRecharge = createForm()(CompanyRecharge);

ReactDOM.render(<CompanyRecharge money={$("#money").val()} />,  document.getElementById("j-rechargemain"));

ReactDOM.render(<Accountmenu current = {"12"}/>,  document.getElementById("j-sider-menu"));
