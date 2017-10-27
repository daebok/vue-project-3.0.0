import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import '../order.less';
import CountDown from '../../../component/orderCountDown/countDown';
import Favorable from '../commonComponent/favorable';
import  { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import { Publiclib } from '../../../common/common';
import { Button, Form, Input, Modal,InputNumber } from 'antd';
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

export default class OrderCopies extends React.Component{
  constructor(props){
    super(props);
    this._handleSubmit = this.handleSubmit.bind(this);
    this._handleOk = this.handleOk.bind(this);
    this._handleCancel = this.handleCancel.bind(this);
    this._setdefault = this.setdefault.bind(this);//默认投资金额
    this._setRealPayAmount = this.setRealPayAmount.bind(this);//实际支付金额
    this.countIncomeHander = this.countIncomeHander.bind(this);//份数收益计算
    this._RxgAccountYesSecond = this.RxgAccountYesSecond.bind(this);
    this._closeOrderPayModal = this.closeOrderPayModal.bind(this);
    this.getRedRateFlag = this.getRedRateFlag.bind(this);
    this.ErrorShow = this.ErrorShow.bind(this);
    this.amount = $("#amountInput").val();
    this.projectId = $("#projectId").val();
    this.useMoney = $("#useMoney").val();//账户余额 
    this.copyAccount = $("#copyAccount").val();//单份金额
    this.objVal = {};
    this.projectRealizeFlag = $("#projectRealizeFlag").val();//变现标识     
    this.state = {
      data: null,
      visible:false,
      visible1:false,
      interest: 0,
      realPayAmount: this.amount,
      showRedRateTip:false,
      stateClass:'ant-form-item-control',
      hasError:false,
      ErrorText:''
    };
  }
  handleSubmit(e) {
     this.RxgAccountYesSecond(this.refs.amount.value,this.ErrorShow)
     if(this.state.hasError==true){
          e.preventDefault();
          return false
    }
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log(errors);
        e.preventDefault();
        return;
      }
      
      console.log('Submit!!!');      
      this.setState({ visible: true });
    });
  }
  handleOk() {
    window.location.href = "/member/myInvest/list.html?tab=2";
  } 
  handleCancel() {
    this.setState({ visible: false });
    window.location.href = "/member/myInvest/list.html?tab=2";
  }
  setdefault(){
     this.refs.amount.value = (this.amount/this.copyAccount).toString()
  }
  getRedRateFlag(){

    let self = this;

    let Rate = $.ajax({
            url: '/member/coupon/availableRateList.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId": self.projectId, "tenderMoney": $("#amount").val() ? $("#amount").val() : $("#amountInput").val(), "page.pageSize": 6,"page.page":1}
        });

    let Red = $.ajax({
            url: '/member/coupon/availableRedList.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId": self.projectId, "tenderMoney": $("#amount").val() ? $("#amount").val() : $("#amountInput").val(), "page.pageSize": 6,"page.page":1}
        });

    $.when(Rate,Red).done(function(a1,a2){
      if( a1[0].data.rows && a2[0].data.rows ){
        self.setState({
          showRedRateTip : true
        });
      }
    });

  }  
  componentDidMount(){ 
    //初始化投资金额的数值
    this.getRedRateFlag();   
    this._setdefault();
    this.setState({'realPayAmount':this.amount})
    this.refs.trueInput.value=this.amount
    this.getData({"projectId": this.projectId, "amount": this.amount});
  }
  //计算收益
  countIncomeHander(e){
    let el = e.target;
    Publiclib.setint(el);
    this.refs.amount.value=e.replace(/[^\d]/g,'')
    this.refs.amount.value=parseInt(e)
    this.RxgAccountYesSecond(e.target.value,this.ErrorShow);
    if(e.target.value==''){
        this.setState({realPayAmount: 0,interest: 0});
    }
    if(el.value !== '' && !Reg.isNumber(el.value)){
      if(this.refs.fav){
        this.refs.fav.reset();  
      }
      this.getData({"projectId": this.projectId, "amount": el.value*this.copyAccount});
      this.refs.trueInput.value=el.value*this.copyAccount;
      this.getRedRateFlag();//是否可以用优惠券
      //计算支付金额
      let money = el.value;
      if($("#favorableText").attr("data-red")){
        money -= $("#favorableText").attr("data-red");
      }
      this._setRealPayAmount(money*this.copyAccount);
    }
  }
	getData(PostData){		
		let self = this;
    $.ajax({      
      url: '/invest/interest.html',
      type: 'POST',
      dataType: 'json',
      data: PostData,
    })
    .done(function(data) {
      if(data.result){        
        self.setState({interest: data.interest, userInvestMoney:data.userInvestAmount});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
	}
  //支付金额
  setRealPayAmount(val,interests){
    this.RxgAccountYes(val,this.ErrorShow)
    if(interests >= 0){
      this.getData({"projectId": this.projectId, "amount": $("#amount").val(),"upApr":interests});
    }
    if(val<0){
      return false;
    }
    this.setState({realPayAmount: val});
  }
  closeOrderPayModal(){
    this.setState({ visible: false,visible1: true });
  }
  ErrorShow(error){
        if(error==undefined){
            this.setState({
                hasError:false,
                ErrorText:'',
                stateClass:'ant-form-item-control has-success'
            })
        }else{
            this.setState({
                hasError:true,
                ErrorText:error,
                stateClass:'ant-form-item-control has-error'
            })
        }
  }
  RxgAccountYesSecond(value, callback){
    value = parseFloat(value);
      value=value*this.copyAccount;
      let copyAccount=this.copyAccount;
      let remAccount=parseInt(this.objVal.butSendMoney);
      let min=Math.min(this.objVal.mostAccount-this.state.userInvestMoney,remAccount);
      let userMoney=this.useMoney;
      if(!value || value == "0"){
          callback("请输入份数");
      }else if(min==0){
          callback('您已达到投资上限，无法投资');
      }else if(value != 0 && Reg.isNumber(value)){
          callback('抱歉，投资请输入整数份数');
      }else if(remAccount<=this.objVal.lowestAccount&&remAccount<=userMoney){
          this.refs.amount.value = remAccount/copyAccount
          this.refs.trueInput.value = remAccount
          callback();
      }else if(min<this.objVal.lowestAccount){
          if(value<userMoney){
              if(min<this.objVal.lowestAccount){
                  callback('您当前可投份数为'+min/copyAccount+'份小于起投份数，无法投资');
              }else{
                  callback('您当前可投份数为'+min/copyAccount+'份');
              }
          }else {
              if(min<userMoney){
                  callback('您当前可投份数为'+min/copyAccount+'份');
              }else{
                  callback('您当前可投份数为'+min/copyAccount+'份,请立即充值');
              }
          }
      }else if(value < this.objVal.lowestAccount ){
          callback('您当前可投份数为“'+this.objVal.lowestAccount/copyAccount+'份'+'~'+min/copyAccount+'份”');
      }else if(value < min){
          if($("#favorableText").attr("data-red")){
            value -= $("#favorableText").attr("data-red");
          }
          if(value<=userMoney){
              callback();
          }else{
              callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
          }
      }else if(value ==min&&value<=userMoney){
          callback();
      }else if(value <userMoney){
          callback('您当前最高可投份数为'+min/copyAccount+'份');
      }else if(min<userMoney){
          callback('您当前可投份数为'+min/copyAccount+'份');
      }else{
          callback('您当前可投份数为“'+this.objVal.lowestAccount/copyAccount+'份'+'~'+min/copyAccount+'份”，请<a href="/member/recharge/detail.html">立即充值</a>');
      }
  }
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const investTimeout = $("#investTimeout").val();//倒计时
    const projectSaleStyle = $('#projectSaleStyle').val();//借款类型
    let formAction = "/invest/doInvest.html"; 
    if(this.projectRealizeFlag === "1"){
      formAction = "/realize/doRealizeInvest.html";
    }
    //剩下可投金额
		if(!this.objVal.butSendMoney){
			this.objVal.butSendMoney = $("#account").val() - $("#accountYes").val();
		}
		//单笔最低投资金额
		if(!this.objVal.lowestAccount){
			this.objVal.lowestAccount = $("#lowestAccount").val() || 100;
		}
		//单笔最高投资金额
		if(!this.objVal.mostAccount){
			this.objVal.mostAccount = $("#mostAccount").val() || $("#account").val();
		}
		var fav=[]
        if($("#redEnvelopeUseful").val() != '1' && $("#additionalRateUseful").val() !='1' ){
          //fav = <Favorable id={this.projectId} setRealPayAmount={this._setRealPayAmount} />
          fav = <span style={{'fontSize':'14px'}}>该产品不可使用优惠券</span>
        }else{
          fav = <div><span className={this.state.showRedRateTip ? "" : "hide"}><Favorable id={this.projectId} ref={"fav"} setRealPayAmount={this._setRealPayAmount} /></span><span className={!this.state.showRedRateTip ? "f14" : "hide"}>无可用优惠券</span></div>
        }
    return (
      <div className="order-main">
        <Form horizontal target="_blank" method="post" action={formAction} form={this.props.form} onSubmit={this._handleSubmit}>
            <dl className="order-item investment" >
              <dt>投资份数</dt>
              <dd>
                  <div className="form-input">
                      <div className={this.state.stateClass}>
                          <input type="text" name="amount1" autoComplete="off" id="amount1" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder='请输入金额' onKeyUp={this.countIncomeHander}/>
                          <div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}></div>
                          <span className="units">份</span>
                      </div>
                  </div>
                  <div className="text">账户余额<span className="num mr10">{this.useMoney}</span>预计可获收益<span className="num">{this.state.interest}</span>元</div>
              </dd>       
            </dl>
            <dl className="order-item select">
              <dt>选择优惠</dt>
                <dd>
                  {fav}
              </dd>
            </dl>
            <dl className="order-item payment">
              <dt>支付金额</dt>
              <dd>
                  <span className="num">{this.state.realPayAmount}</span>元
                  <input type='hidden' name="amount" ref="trueInput"  id="amount" />
                  <input type="hidden" name="projectId" id="projectId" value={this.projectId} />      
                  <input type="hidden" name="realAmount" value={this.state.realPayAmount} />
                  <input type="hidden" name="investToken" value={this.props.token} />
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
            <div className="modal-content"><i className="iconfont">&#xe64d;</i><p>请在<strong><CountDown time={investTimeout} state={this.state.visible} closeOrderPayModal={this._closeOrderPayModal} /></strong>内完成支付，若超出时间限制未完成，<br/> 将取消订单。</p></div>
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
