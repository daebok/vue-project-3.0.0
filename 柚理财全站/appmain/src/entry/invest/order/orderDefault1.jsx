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

export default class InvestOrder extends React.Component{
  constructor(props){
    super(props);
    this._handleSubmit = this.handleSubmit.bind(this);
    this._handleOk = this.handleOk.bind(this);
    this._handleCancel = this.handleCancel.bind(this);
    this._setdefault = this.setdefault.bind(this);//默认投资金额
    this._setRealPayAmount = this.setRealPayAmount.bind(this);//实际支付金额
    this._countIncomeHander = this.countIncomeHander.bind(this);//收益计算
    this.RxgAccountYes = this.RxgAccountYes.bind(this);
    this._closeOrderPayModal = this.closeOrderPayModal.bind(this);
    this.getRedRateFlag = this.getRedRateFlag.bind(this);
    this.ErrorShow = this.ErrorShow.bind(this);
    this.amount = $("#amountInput").val();
    this.projectId = $("#projectId").val();
    this.useMoney = $("#useMoney").val();//账户余额
    this.copyAccount = $("#copyAccount").val();//单份金额
    this.stepAccount = $("#stepAccount").val();//单份金额
    this.objVal = {};
    this.projectRealizeFlag = $("#projectRealizeFlag").val();//变现标识  
    this.hasError=false;   
    this.state = {
      data: null,
      visible:false,
      visible1:false,
      interest: 0,
      realPayAmount: this.amount,
      showRedRateTip:true,
      userInvestMoney:'',
      stateClass:'ant-form-item-control',     
      ErrorText:'',
      money:''
    };
  }
  handleSubmit(e) {
      this.RxgAccountYes(parseInt(this.refs.amount.value),this.ErrorShow)
      if(this.hasError==true){
          e.preventDefault();
          return false
      }
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        e.preventDefault();
        return;
      }     
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
      this.refs.amount.value = this.amount
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
      if( !a1[0].data.rows && !a2[0].data.rows ){
        self.setState({
          showRedRateTip : false
        });
      }
      else
      {
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
    this.getData({"projectId": this.projectId, "amount": this.amount});
      if(this.objVal.butSendMoney<this.objVal.lowestAccount*2){
         this.refs.amount.readOnly=true
      }
       var _this=this;
        $.ajax({
            url: '/project/remainAccount.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId":_this.projectId}
        })
        .done(function(msgdata) {
                _this.setState({money: msgdata.remainAccount});
        })
  }
  //计算收益
  countIncomeHander(e){
    let el = e.target;
    Publiclib.setint(el);
    this.RxgAccountYes(e.target.value,this.ErrorShow)
    this.getRedRateFlag();//是否可以用优惠券

    if(el.value !== '' && !Reg.isNumber(el.value)){
      if(this.refs.fav){
        this.refs.fav.reset();  
      }
      this.getData({"projectId": this.projectId, "amount": el.value});
      //计算支付金额
      let money = el.value;   
      if($("#favorableText").attr("data-red")){
        money -= $("#favorableText").attr("data-red");
      }
      if(this.objVal.lowestAccount>=this.objVal.butSendMoney){
        money=this.objVal.butSendMoney
      }
      this._setRealPayAmount(money);
    }
  }
  getData(PostData){    
      let self = this;
      let money=this.objVal.butSendMoney;
      if(money<=this.objVal.lowestAccount){
        PostData['amount']=money
      }
    $.ajax({      
      url: '/invest/interest.html',
      type: 'POST',
      dataType: 'json',
      data: PostData,
    })
    .done(function(data) {
      if(data.result){        
        self.setState({
          interest: data.interest,
          userInvestMoney:data.userInvestAmount
        });
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
           this.hasError=false
            this.setState({
               
                ErrorText:'',
                stateClass:'ant-form-item-control has-success'
            })
        }else{
           this.hasError=true
            this.setState({
                ErrorText:error,
                stateClass:'ant-form-item-control has-error'
            })
        }
    }
    RxgAccountYes(value, callback){
        let remAccount=this.state.money;
        let min=Math.min(this.objVal.mostAccount-this.state.userInvestMoney,remAccount);
        let min1=Math.min(this.objVal.lowestAccount,remAccount);
        let y=this.objVal.mostAccount-this.state.userInvestMoney;
        value=Number(value);
        let userMoney=Number(this.useMoney);
      if(!value || value == "0"){
        callback("请输入金额");
      }else if(Reg.isNumber(value)){
        callback("请输入整数金额");
      }else if(remAccount<=this.objVal.lowestAccount*2){
        if(remAccount<=y){
            this.refs.amount.value = remAccount;
            if(remAccount<=userMoney){
              callback()
            }else{
                callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
            }
        }else{
          if(y==0){
                    callback('您已达到投资上限，无法投资');
        }else{
                    callback('您当前可投金额'+y+'元小于'+remAccount+'元，无法投资');
        }
      }
    }else{
        if(y<this.objVal.lowestAccount){
          if(y==0){
                    callback('您已达到投资上限，无法投资');
        }else{
                    callback('您当前可投金额'+y+'元小于起投金额，无法投资');
        }
      }else{
        if(value<this.objVal.lowestAccount){
          if(value<=userMoney){
                        callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”');
          }else{
                        callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”，请<a href="/member/recharge/detail.html">立即充值</a>');
          }
        }else{
          if( this.stepAccount>0 && (value-this.objVal.lowestAccount) %  this.stepAccount!=0){
                        callback("金额必须为递增金额的整数倍+起投金额");
          }else{
            if(value<=min){
               if($("#favorableText").attr("data-red")){
                value -= $("#favorableText").attr("data-red");
                }
              if(value<=userMoney){
                callback()
              }else{
                callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
              }
            }else{
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”');
            }
          }
        }
      }
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
              <dt>投资金额</dt>
              <dd>
                <div className="form-input">
                  <div className={this.state.stateClass}>
                    <input type="text" name="amount" autoComplete="off" id="amount" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder='请输入金额' onKeyUp={this._countIncomeHander}/>
                    <div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}></div>
                    <span className="units">元</span>
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
