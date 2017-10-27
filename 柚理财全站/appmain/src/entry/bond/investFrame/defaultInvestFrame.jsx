import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Input ,Checkbox, Modal, Tabs, Popover } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;

//弹窗
import Dopay from '../../invest/investFrame/dopay';

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

//收益计算
class Income extends React.Component{	
	render(){
		let content = "受让本金 + 受让本金 * 折溢价率 + 应付利息 - 应付利息 * 利息管理费率";
		let content1 = "受让本金的待收利息 - 应付利息 - 受让后持有天数利息 * 利息管理费率±折溢价金额";
		let html = this.props.inputChangeStatus ? <div>
			<p className="preInterest"><span className="tit">预期收益</span>{this.props.data.preInterest}元
				<Popover overlayClassName="realPayAmount-pop" placement="bottom" overlay={content1} trigger="hover" overlayStyle={{width:270}}>
					<i className="iconfont">&#xe629;</i>
				</Popover></p>
			<p className="realPayAmount">
				<span className="tit">实际支付</span>{this.props.data.realPayAmount}元
				<Popover overlayClassName="realPayAmount-pop" placement="bottom" overlay={content} trigger="hover" overlayStyle={{width:270}}>
					<i className="iconfont">&#xe629;</i>
				</Popover>
			</p>
		</div> : "输入查看收益";		
		return (
			<div className="show-income" id="income">		
				{html}
			</div>
		)
	}
}

//投资框state=0
export default class DefaultInvestFrame extends React.Component{
	constructor(props){
	    super(props);
	    this._handleSubmit = this.handleSubmit.bind(this);	   
	    this._RxgAccountYes = this.RxgAccountYes.bind(this);
  		this._countIncomeHander = this.countIncomeHander.bind(this);
  		this._getData = this.getData.bind(this);
      this.ErrorShow = this.ErrorShow.bind(this);
      this._getUserMoney = this.getUserMoney.bind(this);
	    this.objVal = {};
	    this.hasError=true;
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',
        stateClass:'ant-form-item-control has-feedback ',
        userInvestMoney:'',
        useMoney:'',
        ErrorText:'',
        realPayAmount:0
			};         
	}
	handleSubmit(e) {
        this.RxgAccountYes(this.refs.amount.value,this.ErrorShow)
        if(this.hasError==true){
            e.preventDefault();
            return false
        }
	    this.props.form.validateFields((errors, values) => {
	      if (!!errors) {        
	        console.log('Errors in form!!!');
	        e.preventDefault();
	        return;
	      }
	    });
	}
	getUserMoney(){
        let self = this;
        $.ajax({
            url: '/member/account/userAccount.html',
            type: 'POST',
            dataType: 'json'
        })
            .done(function(data) {
                if(data.result){
                    self.setState({useMoney: data.account.useMoney});
                } else {
                    error(data.msg);
                }
            })
    }
    componentWillMount(){
        this._getUserMoney();
        if(this.props.data.buyStyle==1){
        	this.hasError=false
			      
          this.setState({inputChangeStatus: true});
          this._getData({"id":$("#id").val(), "amount": this.props.data.bondMostMoney});
		    }
    }
    componentDidMount(){
        if(this.objVal.butSendMoney<=this.objVal.lowestAccount*2){
          this.hasError=false
          this.setState({inputChangeStatus: true});
          this._getData({"id":$("#id").val(), "amount": this.props.data.bondMostMoney});
        }
        $("input[name='amount']").on("keypress",function(e){
              if(e.keyCode==13)
              {
                $(".tablelink a")[0].click();              
                e.preventDefault();
                return;
              }
        }); 
    }
    ErrorShow(error){
        if(error==undefined){
        	this.hasError=false;
            this.setState({
                ErrorText:'',
                stateClass:'ant-form-item-control has-feedback has-success'
            })
        }else{
        	this.hasError=true;
            this.setState({
                ErrorText:error,
                stateClass:'ant-form-item-control has-feedback has-error'
            })
        }
    }

 getData(PostData){      
  let self = this;
    $.ajax({      
      url: '/bond/getInvestData.html',
      type: 'POST',
      dataType: 'json',
      data: PostData,
    })
    .done(function(data) {
      if(data.result){        
        self.setState({incomeData: data,realPayAmount:data.realPayAmount});
      } else {        
        error(data.msg);
      }
      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }

	RxgAccountYes( value, callback){
    	let userMoney=this.state.useMoney;
		let z=this.objVal.butSendMoney;
    let y=this.state.realPayAmount;
	    if(!value){
	    	callback();
	    }else if(this.props.data.buyStyle==1){
	    	this.refs.amount.value=this.props.data.bondMostMoney;
            if(y<=userMoney){
                callback()
            }else{
                callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
            }
		}else if(value != 0 && Reg.isNumber(value) && this.props.data.buyStyle=='0'){
	    	callback('抱歉，请输入正确的金额');
	    }else if(z<=this.objVal.lowestAccount*2){
            this.refs.amount.value=z;
            //console.log(this.state.incomeData.realPayAmount,userMoney)
            if(y<=userMoney){
                callback()
            }else{
             // console.log(22);
                callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
            }
		}else if(value<this.objVal.lowestAccount) {
        if(value<=userMoney){
          if(this.objVal.lowestAccount==z){
          callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”');
          }else{
            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+z+'元”');
          } 
      }else{
        if(this.objVal.lowestAccount==z){
          callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”，请<a href="/member/recharge/detail.html">立即充值</a>')
        }else{
           callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+z+'元”，请<a href="/member/recharge/detail.html">立即充值</a>')
        }
      }
    }else if(value<=z){
    	//console.log(y,222)
        if(y<=userMoney){

          callback()
      }else{
      	//console.log(3333)
          callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
      }
    }else{
      if(this.objVal.lowestAccount==z){
        callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”');
      }else{
              callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+z+'元”');
      }
    }
	}		
	countIncomeHander(e){
		let el = e.target;
        this.RxgAccountYes(e.target.value,this.ErrorShow)
		if(el.value !== '' && !Reg.isNumber(el.value)){
			this.setState({inputChangeStatus: true});			
			this._getData({"id": this.objVal.id, "amount": el.value});
		} else {
			this.setState({inputChangeStatus: false, inputVal: 0});		
		}
		if(el.value !== '' && this.props.data.buyStyle=='1'){
			this.setState({inputChangeStatus: true});
			this._getData({"id": this.objVal.id, "amount": el.value});
		}
	}
	
	render(){
		const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
		let data = this.props.data;	
		let msgdata = this.props.msgdata;
		if(!data || !msgdata) return false;
		//剩下可投金额
		if(!this.objVal.butSendMoney){
			this.objVal.butSendMoney = data.remainMoney;
		}
		//单笔最低投资金额
		if(!this.objVal.lowestAccount){
			this.objVal.lowestAccount = data.bondLowestMoney || 100;
		}
		//单笔最高投资金额
		if(!this.objVal.mostAccount){
			this.objVal.mostAccount = data.bondMostMoney || data.BondMoney;
		}
		//剩下可投进度条    
		let progressVal = data.remainMoney/data.BondMoney*100 + "%";
		//起投金额提示
		let lowestAccountText = "起投金额"+ this.objVal.lowestAccount +"元";
		//债权标ID			
		if(!this.objVal.id){
			this.objVal.id = $("#id").val();
		}
		//实际支付金额
		if(!this.objVal.realPayAmount){
			this.objVal.realPayAmount = this.state.realPayAmount;
		}
        let AllMoneyInput = [];
        if(data.buyStyle==1){
            AllMoneyInput = <input type="text" value={data.bondMostMoney} name="amount" autoComplete="off" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} onPropertyChange={this._countIncomeHander} />
        }else if(this.objVal.butSendMoney<=this.objVal.lowestAccount*2){
            AllMoneyInput = <input type="text" value={data.bondMostMoney} name="amount" autoComplete="off" ref="amount" className="ant-input ant-input-lg" maxLength="10" readonly={true} placeholder={lowestAccountText} onInput={this._countIncomeHander} onPropertyChange={this._countIncomeHander} />
        }else{
            AllMoneyInput = <input type="text" name="amount" ref="amount" autoComplete="off" className="ant-input ant-input-lg" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} onPropertyChange={this._countIncomeHander} />
        }
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="max">最高可投<span className="max-number">{data.bondMostMoney}元</span></div>
				<div className="surplus">剩余债权<s>{this.objVal.butSendMoney}元</s></div>{/*
				<div className="progress"><s style={{width: progressVal}}></s></div>*/}
				<Form horizontal action="/bond/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
					<div className="row">
						<div className="ant-form-item hasunit">
							<div className={this.state.stateClass}>
								{AllMoneyInput}<span className="unit">元</span>
								<div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}>
								</div>
							</div>
						</div>
					</div>
					<Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />                    
					<div className="btn-instant">
						<input type="hidden" name="bondId" value={this.objVal.id} />
						<input type="hidden" name="realPayAmount" value={this.objVal.realPayAmount} />
						<input type="hidden" name="isSelectedTip" value="1" />
						<FormItem style={{ marginTop: 24 }}>
							<div className="submitdom"><Dopay msgdata = {msgdata}/></div>
						</FormItem>  
					</div>
				</Form>
			</div>
		)
	}
}