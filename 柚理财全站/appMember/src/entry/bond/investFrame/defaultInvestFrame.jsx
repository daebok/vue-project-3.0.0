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
		let content = "受让本金-受让本金*折溢价率+应付利息-应付利息*利息管理费率";
		let content1 = "受让本金的待收利息-应付利息-受让后持有天数利息*利息管理费率±折溢价金额";
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
	    this.objVal = {};
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',			
			};         
	}
	handleSubmit(e) {    
	    this.props.form.validateFields((errors, values) => {
	      if (!!errors) {        
	        console.log('Errors in form!!!');
	        e.preventDefault();
	        return;
	      }
	    });
	}
	RxgAccountYes(rule, value, callback){
	    if(!value){
	    	callback();
	    }else if(value != 0 && Reg.isNumber(value) && this.props.data.sellStyle=='0'){
	    	callback([new Error('抱歉，请输入正确的金额')]);        
	    }else if(this.props.data.sellStyle=='1' && Reg.isApr(value)){
				callback([new Error('抱歉，请输入正确的数字')]);
			}else if(value != 0 && value!=this.objVal.lowestAccount && this.props.data.sellStyle=='1'){
			callback([new Error('抱歉，请输入金额为'+this.objVal.lowestAccount+'元的投资金额')]);
			}else if(value < this.objVal.lowestAccount && this.objVal.butSendMoney >= this.objVal.lowestAccount){
	    	callback([new Error('单笔最低投资金额为'+this.objVal.lowestAccount+'元')]);
	    } if(value > this.objVal.mostAccount){
	    	callback([new Error('单笔最高投资金额为'+this.objVal.mostAccount+'元')]);
	    }else if(value != this.objVal.butSendMoney && this.objVal.butSendMoney < this.objVal.lowestAccount*2){
	    	callback([new Error('请输入剩余可投金额为：'+this.objVal.butSendMoney+'元')]);
	    }
	    else{
	      callback();
	    }
	}	
	countIncomeHander(e){
		let el = e.target;		
		if(el.value !== '' && !Reg.isNumber(el.value)){
			if(!el.style.fontSize){
				el.style.fontSize = '26px';
				el.style.color = "#333";
			}
			this.setState({inputChangeStatus: true});			
			this.getData({"id": this.objVal.id, "amount": el.value});
		} else {
			if(el.style.fontSize){
				el.style.fontSize = '';
				el.style.color = "#666";
			}
			this.setState({inputChangeStatus: false, inputVal: 0});		
		}
		if(el.value !== '' && this.props.data.sellStyle=='1'){
			if(!el.style.fontSize){
				el.style.fontSize = '26px';
				el.style.color = "#333";
			}
			this.setState({inputChangeStatus: true});
			this.getData({"id": this.objVal.id, "amount": el.value});
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
        self.setState({incomeData: data});
      } else {        
        error(data.msg);
      }
      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
	}
	render(){
		const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
		const amountProps = getFieldProps('amount', {
			rules: [
				{ required: true,message: '请输入金额' },
				{ validator: this._RxgAccountYes}
			],
		});			
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
			this.objVal.realPayAmount = this.state.incomeData.realPayAmount;
		}	
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="max">投资<span className="max-number">最高可投 {data.bondMostMoney}元</span></div>
				<div className="surplus">剩余债权<s>{this.objVal.butSendMoney}元</s></div>
				<div className="progress"><s style={{width: progressVal}}></s></div>
				<Form horizontal action="/bond/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
					<div className="row">
						<div className="col-24 hasunit">
							<FormItem                         
								hasFeedback
								help={isFieldValidating('amount') ? '校验中...' : (getFieldError('amount') || []).join(', ')}
							>
								<Input {...amountProps} name="amount" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} />	                    
							</FormItem><span className="unit">元</span>      
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