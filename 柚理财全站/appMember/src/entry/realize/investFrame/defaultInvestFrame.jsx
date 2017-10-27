import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Input ,Checkbox, Modal, Tabs } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
//弹窗
import Dopay from '../../invest/investFrame/dopay';

//收益计算
class Income extends React.Component{		
	render(){
		let html = this.props.inputChangeStatus ? <div><p className="preInterest"><span className="tit">预期收益</span>{this.props.data.interest}元</p></div> : "输入查看收益";		
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
	    if(!value || value == "0"){
	    	callback([new Error('抱歉，请输入金额')]);
	    }else if(value != 0 && Reg.isNumber(value) && this.objVal.butSendMoney >= this.objVal.lowestAccount*2){        
	    	callback([new Error('抱歉，请输入正确的金额')]);        
	    }else if(value < this.objVal.lowestAccount && this.objVal.butSendMoney >= this.objVal.lowestAccount){
	    	callback([new Error('单笔最低投资金额为'+this.objVal.lowestAccount+'元')]);
	    } if(value > this.objVal.mostAccount){
	    	callback([new Error('单笔最高投资金额为'+this.objVal.mostAccount+'元')]);
	    }else if(value && value != this.objVal.butSendMoney && this.objVal.butSendMoney < this.objVal.lowestAccount*2){
				this.props.form.setFieldsValue({
					amount: this.objVal.butSendMoney,  
				});
	    	callback([new Error('请输入剩余可投金额为：'+this.objVal.butSendMoney+'元')]);
	    }else{
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
			this.getData({"projectId": this.objVal.id, "amount": el.value});
		} else {
			if(el.style.fontSize){
				el.style.fontSize = '';
				el.style.color = "#666";
			}
			this.setState({inputChangeStatus: false, inputVal: 0});		
		}
	}
	getData(PostData){//计算收益			
		let self = this;
    $.ajax({      
      url: '/invest/interest.html',
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
		let data = this.props.data;
		let Allmoney = data.realize.lowestAccount;
		let AllMoneyInput = [];

		const amountProps = getFieldProps('amount', {
			initialValue : Allmoney,
			rules: [
				{ validator: this._RxgAccountYes}
			]
		});
		if(data.buyStyle==1){
			Allmoney =data.realize.account;
			AllMoneyInput = <Input {...amountProps} style={{fontSize: 26+'px',color:'#'+333}} readOnly name="amount" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} />
		}else{
			AllMoneyInput = <Input {...amountProps} style={{fontSize: 26+'px',color:'#'+333}} name="amount" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} />
		}

		//剩下可投金额
		if(!this.objVal.butSendMoney){
			this.objVal.butSendMoney = data.realize.account - data.realize.accountYes;
		}		
		//单笔最低投资金额
		if(!this.objVal.lowestAccount){
			this.objVal.lowestAccount = data.realize.lowestAccount || 100;
		}
		//单笔最高投资金额
		if(!this.objVal.mostAccount){
			this.objVal.mostAccount = data.realize.mostAccount || data.realize.account;
		}
		//剩下可投进度条    
		let progressVal = (1-data.realize.accountYes/data.realize.account)*100 + "%"; 
		let projectId = $("#projectId").val();
		//起投金额提示
		let lowestAccountText = "起投金额"+ this.objVal.lowestAccount +"元";
		//债权标ID			
		if(!this.objVal.id){
			this.objVal.id = $("#projectId").val();
		}

		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">投资<span className="max-number">最高可投 {data.realize.mostAccount}元</span></div>
	            <div className="surplus">剩余可投<s>{this.objVal.butSendMoney}元</s></div>
	            <div className="progress"><s style={{width: progressVal}}></s></div>
	            <Form horizontal action="/invest/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
	                <div className="col-24 hasunit">
	                  <FormItem                         
	                    hasFeedback
	                    help={isFieldValidating('amount') ? '校验中...' : (getFieldError('amount') || []).join(', ')}
	                  >
						  {AllMoneyInput}
	                  </FormItem><span className="unit">元</span>	                  
	                </div>
	              </div>
	              <Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />              
	              <div className="btn-instant">
	              	<input type="hidden" name="projectId" value={projectId} />
	                <FormItem style={{ marginTop: 24 }}>
	                	<div className="submitdom"><Dopay msgdata = {this.props.msgdata}/></div>	                 
	                </FormItem>  
	              </div>
	            </Form>
	        </div>
		)
	}
}