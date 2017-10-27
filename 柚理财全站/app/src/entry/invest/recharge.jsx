import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn} from '../../common/validateFn';
import  { Reg } from '../../common/regularFun';
import {Publiclib} from '../../common/common';
import './recharge.less';
import { Button, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

let Recharge = React.createClass({
  getInitialState() {
    return {
      count:this.props.money,
      visible:false,
    };
  },
  handleSubmit(e) {    
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      }     
      this.setState({ visible: true });      
    });
  },
  add(e){
    var that = e.target;
    Publiclib.setint(that);
    let number = (this.props.money*1 + e.target.value*1).toFixed(2);
    this.setState({
      count : number
    });
  },
  handleOk() {
    window.location.href = "/member/recharge/index.html";
  },  
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },  
  render(){
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const banknumberProps = getFieldProps('banknumber', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入充值金额' }
                    ],
                     trigger: ['onBlur'],
                  },
                  {
                    rules:[
                      {validator:ValidateFn.isNumber},
                    ], trigger: ['onBlur'],
                  }
                  ]  
                });
            let count = "充值后余额"+ this.state.count +"元";
            return (
                <div className="investRecharge-wrap">
                <div className="title">充值</div>
                  <div className="investRecharge-form">
                    <Form horizontal className="form-themes" style={{marginLeft:100}} target="_blank" action="/member/recharge/doRecharge.html" method="post" form={this.props.form} onSubmit={this.handleSubmit}>
                      <div className="row" style={{width:400,marginBottom:"20px"}}>
                        <div className="col-6 lab form-laber textR">账户余额</div>
                        <div className="col-18 pl20 form-text lab">
                          {this.props.money}
                        </div>
                      </div>                                                                
                      <div className="row" style={{width:400}}>
                        <div className="col-6 lab form-laber textR">需充值</div>
                        <div className="col-18 pl20 hasunit">
                          <FormItem hasFeedback extra={count}><Input {...banknumberProps} onInput={this.add} style={{fontSize:"18px",color:"#333",lineHeight:"normal"}} name="amount" placeholder="请输入充值金额" maxLength="10"  /></FormItem>
                          <span className="unit">元</span>
                        </div>
                      </div>                    
                      <div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20">
                          <FormItem><Button className="subbutton" type="primary" htmlType="submit" >确认充值</Button></FormItem>
                        </div>
                      </div>
                      <div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20 remind">
                          <em>*</em>充值操作将在资金托管账户网站进行
                        </div>
                      </div>                                
                    </Form>
                  </div>
                  <div className="help">
                    <div className="help-title"><em className="iconfont">&#xe639;</em>温馨提示</div>
                    <p>充值/提现必须为开通网上银行的借记卡，不支持存折、信用卡充值。</p>
                    <p>充值期间，请勿关闭浏览器，待充值成功并返回后，可在网站中查看充值金额。</p>
                    <p>严禁信用卡套现、虚假交易等行为。</p>
                  </div>

                  <Modal ref="modal"
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
                  <div className="modal-content"><em className="iconfont">&#xe62e;</em>请在新打开的资金托管账户充值</div>
                  </Modal>
                </div>
              )
          }  
});
Recharge = createForm()(Recharge);
ReactDOM.render(<Recharge money={$("#money").val()} />,  document.getElementById("j-recharge"));