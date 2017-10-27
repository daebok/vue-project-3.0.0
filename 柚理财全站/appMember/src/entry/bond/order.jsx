/*author：Gujibao*/
import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './order.less';
import CountDown from '../../component/orderCountDown/countDown';
import  { ValidateFn } from '../../common/validateFn';
import { Publiclib } from '../../common/common';
import { Button, Form, Input, Modal } from 'antd';
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
    title: '提醒',
    content: v,
  });
}

export default class BondOrder extends React.Component{
  constructor(props){
    super(props);
    this._handleSubmit = this.handleSubmit.bind(this);
    this._handleOk = this.handleOk.bind(this);
    this._handleCancel = this.handleCancel.bind(this);
    this._setdefault = this.setdefault.bind(this);
    this._amountHander = this.amountHander.bind(this);
    this._getData = this.getData.bind(this);
    this._closeOrderPayModal = this.closeOrderPayModal.bind(this);
    this.objVal = {};      
    this.state = {
      data: null,
      visible:false,
      visible1:false,
      amount: $("#amountInput").val(),
      calculateEarning: {"preInterest":0,"realPayAmount":0,"changeMoney":0},
    };
  }
  getData(){
    let self = this;
    $.ajax({      
      url: '/bond/getInvestData.html',
      type: 'POST',
      dataType: 'json',
      data: {"id":$("#bondId").val(),"amount": $("#amountInput").val()}    
    })
    .done(function(data) {      
      if(data.result){       
        self.setState({calculateEarning: {"preInterest": data.preInterest,"receivedAccount": data.realPayAmount,"changeMoney": data.changeMoney}});
      } else {        
        error(data.msg);
      }
      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  handleSubmit(e) {    
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      } 
      this.setState({ visible: true });
    });
  }
  handleOk() {
    window.location.href = "/bond/index.html";
  } 
  handleCancel() {
    this.setState({ visible: false });
    window.location.href = "/bond/index.html";
  }
  setdefault(){    
    this.props.form.setFieldsValue({
      amount: this.state.amount,     
    });
  }
  componentDidMount(){ 
    //初始化投资金额的数值   
    this._setdefault();
    this._getData();
  }
  amountHander(e){
    Publiclib.setint(e.target);
    this.setState({amount: e.target.value});     
    this.calculateEarnings(this.objVal.bondId,e.target.value);
  }
  calculateEarnings(id, value){
    let _this = this;
    $.ajax({
      url: "/bond/getInvestData.html",
      type: "POST",      
      dataType: 'json',
      data:{"id": id,"amount": value}
    }).done(function(data){
      if(data.result){        
        _this.setState({calculateEarning: {"preInterest": data.preInterest,"receivedAccount": data.realPayAmount,"changeMoney": data.changeMoney}});
      } else {
        error(data.msg);
      }
    }).fail(function() {
        error('网络异常，请重试！');
      });
  }
  //关闭支付框
  closeOrderPayModal(){
    this.setState({ visible: false,visible1: true });
  }
  render() {
    let isBuyAll = $("#isBuyAll").val();
    let amountInput = $("#amountInput").val();
    let inputdom="";
    if(isBuyAll=="true"){
      inputdom = <FormItem><Input {...amountProps} name="amount" value={amountInput} type="text" disabled="true" onInput={this._amountHander} placeholder="请输入投资金额" /></FormItem>;
    }
    else{
      inputdom = <FormItem><Input {...amountProps} name="amount" type="text"  onInput={this._amountHander} placeholder="请输入投资金额"/></FormItem>;
    }
    
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;    
    
    let checkfun = "";

    checkfun = isBuyAll=="true" ? ValidateFn.isApr : ValidateFn.isNumber;

    const amountProps = getFieldProps('amount', {      
      rules: [
        { required: true, whitespace: true, message: '请输入投资金额！' },
        { validator: isBuyAll, message: '对不起，只能输入数字！'}        
      ],
    });
    
    //债权的uuid
    if(!this.objVal.bondId){
      this.objVal.bondId = $("#bondId").val();
    }
    //原始标的uuid
    if(!this.objVal.projectId){
      this.objVal.projectId = $("#projectId").val();
    }
    //原始标投资Id
    if(!this.objVal.investId){
      this.objVal.investId = $("#investId").val();
    }
    //设置默认实际支付
    if(!this.state.calculateEarning.receivedAccount){
      this.state.calculateEarning.receivedAccount = $("#receivedAccount").val();
    }
    //表单提交url
    let formAction = "/bond/doBondInvest.html?amount="+this.state.amount+"&receivedAccount=" + this.state.calculateEarning.receivedAccount;  
    //超时时间
    let investTimeOut =  $("#investTimeOut").val();

    let changeMoneyValue = "",
        changeMoneyText = "",
        changeNumber = this.state.calculateEarning.changeMoney;

    switch (true)
    {
     case changeNumber > 0:
     changeMoneyText = "溢价金额";
     changeMoneyValue = changeNumber;
     break;
     case changeNumber < 0:
     changeMoneyText = "折价金额";
     changeMoneyValue = Math.abs(changeNumber);
     break;
     case changeNumber === 0:
     changeMoneyText = "折溢价金额";
     changeMoneyValue = 0;
     break;          
    }
    return (
      <div className="">
        <Form horizontal target="_blank" method="post" action={formAction} form={this.props.form} onSubmit={this._handleSubmit}>
            <dl className="order-item investment" >
              <dt>投资金额</dt>
              <dd>
                  <div className="form-input">
                  <div>{inputdom}</div>
                  <span className="units">元</span>                 
                  </div>
                  <div className="text">{changeMoneyText}<span className="mr10"><em className="num">{changeMoneyValue}</em>元</span>预计可获收益<span className="num">{this.state.calculateEarning.preInterest}</span>元</div>
              </dd>       
            </dl>           
            <dl className="order-item payment">
              <dt>支付金额</dt>
              <dd>
                  <span className="num">{this.state.calculateEarning.receivedAccount}</span>元
                  <input type="hidden" name="bondId" value={this.objVal.bondId} />
                  <input type="hidden" name="projectId" value={this.objVal.projectId} />
                  <input type="hidden" name="investId" value={this.objVal.investId} />
                  <FormItem><Button className="subbutton" type="primary" htmlType="submit" >去支付</Button></FormItem>
              </dd>
            </dl>
          </Form>
          <Modal ref="modal" className="orderPayModal"
            visible={this.state.visible}
            title="登录资金账户完成支付" onOk={this._handleOk} onCancel={this._handleCancel}
            width={480}
            footer={[                      
              <Button key="submit" type="primary" size="large" onClick={this._handleOk}>
                支付成功
              </Button>,
              <Button key="back" type="ghost" size="large" onClick={this._handleCancel}>支付失败</Button>,
            ]}
          >
            <div className="modal-content"><i className="iconfont">&#xe64d;</i><p>请在<strong><CountDown time={investTimeOut} state={this.state.visible} closeOrderPayModal={this._closeOrderPayModal} /></strong>内完成支付，若超出时间限制未完成，<br/> 将取消订单。</p></div>
          </Modal>
        <Modal ref="modal" className="orderPayModal"
               visible={this.state.visible1}
               title="支付超时" onOk={this._handleOk} onCancel={this._handleCancel}
               width={480}
               footer={[
                 <Button key="back" type="ghost" size="large" onClick={this._handleCancel}>返回</Button>
               ]}
        >
          <div className="modal-content"><i className="iconfont">&#xe62e;</i><p style={{'paddingTop':'0'}}>您的支付已超时，系统已自动取消订单。<br/>请关闭此页面重新操作</p></div>
        </Modal>
      </div>
    );
  }
}
BondOrder = createForm()(BondOrder);
ReactDOM.render(<BondOrder />, document.getElementById("j-bondOrder"));