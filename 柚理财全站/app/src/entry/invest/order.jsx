import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './order.less';
import CountDown from '../../component/orderCountDown/countDown';
import Favorable from './commonComponent/favorable';
import  { ValidateFn } from '../../common/validateFn';
import { Reg } from '../../common/regularFun';
import { Publiclib } from '../../common/common';
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
    this.countIncomeHander2 = this.countIncomeHander2.bind(this);//份数收益计算
    this._RxgAccountYes = this.RxgAccountYes.bind(this);
    this._RxgAccountYesSecond = this.RxgAccountYesSecond.bind(this);
    this._closeOrderPayModal = this.closeOrderPayModal.bind(this);
    this.amount = $("#amountInput").val();
    this.projectId = $("#projectId").val();
    this.useMoney = $("#useMoney").val();//账户余额 
    this.copyAccount = $("#copyAccount").val();//单份金额
    this.projectSaleStyle=$('#projectSaleStyle').val();//借款类型
    this.objVal = {};
    this.projectRealizeFlag = $("#projectRealizeFlag").val();//变现标识     
    this.state = {
      data: null,
      visible:false,
      visible1:false,
      interest: 0,
      realPayAmount: this.amount,  
    };
  }
  handleSubmit(e) {    
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
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
    if(this.projectSaleStyle==1){
    this.props.form.setFieldsValue({
      amount: this.amount,     
    });}else{
      this.props.form.setFieldsValue({
        amount1: this.amount/this.copyAccount,
      })
    }
  }
  componentDidMount(){ 
    //初始化投资金额的数值   
    this._setdefault();
    if(this.projectSaleStyle==1){
    this.getData({"projectId": this.projectId, "amount": this.amount});
    }else{
    this.setState({'realPayAmount':this.amount})
    this.refs.trueInput.value=this.amount
    this.getData({"projectId": this.projectId, "amount": this.amount});
    }
  }
  //计算收益
  countIncomeHander(e){
    let el = e.target;
    Publiclib.setint(el);
		if(el.value !== '' && !Reg.isNumber(el.value)){      		
			this.getData({"projectId": this.projectId, "amount": el.value});
      //计算支付金额
      let money = el.value;   
      if($("#favorableText").attr("data-red")){
        money -= $("#favorableText").attr("data-red");
      }
      this._setRealPayAmount(money);
		}
	}
  countIncomeHander2(e){
    let el = e.target;
    Publiclib.setint(el);
    if(el.value !== '' && !Reg.isNumber(el.value)){
      this.getData({"projectId": this.projectId, "amount": el.value*this.copyAccount});
      this.refs.trueInput.value=el.value*this.copyAccount;
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
        self.setState({interest: data.interest});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
	}
  //支付金额
  setRealPayAmount(val){   
    if(val<0){
      return false;
    }  
    this.setState({realPayAmount: val});
  }
  closeOrderPayModal(){
    this.setState({ visible: false,visible1: true });
  }
  RxgAccountYes(rule, value, callback){
      value = parseFloat(value);
	    if(!value){
	    	callback();
	    }else if(value != 0 && Reg.isNumber(value) && this.objVal.butSendMoney >= this.objVal.lowestAccount*2){        
	    	callback([new Error('抱歉，请输入正确的金额')]);        
	    }else if(value < this.objVal.lowestAccount && this.objVal.butSendMoney >= this.objVal.lowestAccount){
	    	callback([new Error('单笔最低投资金额为'+this.objVal.lowestAccount+'元')]);
	    } if(value > this.objVal.mostAccount){
	    	callback([new Error('单笔最高投资金额为'+this.objVal.mostAccount+'元')]);
	    }else if(value != this.objVal.butSendMoney && this.objVal.butSendMoney < this.objVal.lowestAccount*2){
				this.props.form.setFieldsValue({
					amount: this.objVal.butSendMoney,  
				});
	    	callback([new Error('请输入剩余可投金额为：'+this.objVal.butSendMoney+'元')]);
	    }else{
	      callback();
	    }
	}
  RxgAccountYesSecond(rule, value, callback){
    value = parseFloat(value);
    if(!value){
      callback();
    }else if(value=='-1'){
      callback();
    }else if(value != 0 && Reg.isNumber(value) ){
      callback([new Error('抱歉，请输入正确的份数')]);
    }else if(this.objVal.lowestAccount>($("#account").val()-$("#accountYes").val())){
      this.props.form.setFieldsValue({
        amount1: ($("#account").val()-$("#accountYes").val())/this.copyAccount
      });
      this.refs.trueInput.value=$("#account").val()-$("#accountYes").val();
      this._setRealPayAmount($("#account").val()-$("#accountYes").val());
      callback([new Error('请输入剩余可投份数为：'+($("#account").val()-$("#accountYes").val())/this.copyAccount+'份')]);
    } else if(value <this.objVal.lowestAccount/this.copyAccount ){
      callback([new Error('最低投资份数为'+this.objVal.lowestAccount/this.copyAccount+'份')]);
    } if(value > this.objVal.mostAccount/this.copyAccount){
      callback([new Error('最高投资份数为'+this.objVal.mostAccount/this.copyAccount+'份')]);
    }else{
      callback();
    }
  }
  render() {
    let Breqiuire=true;
    if(this.projectSaleStyle==1){
      Breqiuire=false
    }
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;    
    const amountProps = getFieldProps('amount', {      
      rules: [
        { required: true, whitespace: true, message: '请输入投资金额！' },
        { validator: this._RxgAccountYes}        
      ],
    });

    const amountPropsSecond = getFieldProps('amount1', {
      rules: [
        { required: Breqiuire, whitespace: true, message: '请输入投资份数！' },
        { validator: this._RxgAccountYesSecond}
      ],
    });

    const investTimeout = $("#investTimeout").val();//倒计时
    const projectSaleStyle = $('#projectSaleStyle').val();//借款类型
    let typeName='';
    let units='';
    var typeInput='';
    var hiddenInput=''
    if(projectSaleStyle==1){
      typeName='投资金额';
      units='元';
      typeInput= <Input {...amountProps} name="amount" type="text" placeholder="请输入投资金额" onInput={this._countIncomeHander} />
    }else{
      typeName='投资份数';
      units='份';
      typeInput = <Input {...amountPropsSecond} name="amount1" type="text" placeholder="请输入投资份数" onInput={this.countIncomeHander2} />
      hiddenInput = <input type='hidden' ref="trueInput" name="amount" id="amount" />
    }
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
          fav = <span style={{'fontSize':'14px'}}>该产品不可使用优惠劵</span>
        }else{
          fav = <Favorable id={this.projectId} setRealPayAmount={this._setRealPayAmount} />
        }
    return (
      <div className="order-main">
        <Form horizontal target="_blank" method="post" action={formAction} form={this.props.form} onSubmit={this._handleSubmit}>
            <dl className="order-item investment" >
              <dt>{typeName}</dt>
              <dd>
                  <div className="form-input">
                  <FormItem style={{marginBottom:"1px"}}>
                    {typeInput}
                  </FormItem>
                    {hiddenInput}
                  <span className="units">{units}</span>
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
InvestOrder = createForm()(InvestOrder);
ReactDOM.render(<InvestOrder />, document.getElementById("j-investOrder"));