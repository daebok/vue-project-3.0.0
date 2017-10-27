import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Input ,Checkbox, Modal, Tabs } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
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
        this._getUserMoney = this.getUserMoney.bind(this);
		this._countIncomeHander = this.countIncomeHander.bind(this);
		this._getData = this.getData.bind(this);
        this.ErrorShow = this.ErrorShow.bind(this);
	    this.objVal = {};
        this.hasError=false;
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',
                stateClass:'ant-form-item-control has-feedback ',
                userInvestMoney:'',
                hasError:true,
                ErrorText:'',
                money:'',
                useMoney:''
			};         
	}
	handleSubmit(e) {
        this.RxgAccountYes(this.refs.amount.value,this.ErrorShow);
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
    ErrorShow(error){
        if(error==undefined){
            this.setState({
                hasError:false,
                ErrorText:'',
                stateClass:'ant-form-item-control has-feedback has-success'
            })
            this.hasError=false;
        }else{
            this.setState({
                hasError:true,
                ErrorText:error,
                stateClass:'ant-form-item-control has-feedback has-error'
            })
            this.hasError=true;
        }
    }
    RxgAccountYes(value, callback){
        let remAccount=this.props.remainDate;
        let y=this.objVal.mostAccount-this.state.userInvestMoney;
        let min=Math.min(y,remAccount);
        let min1=Math.min(this.objVal.lowestAccount,remAccount);
        let userMoney=this.state.useMoney;
        if(!value || value == "0"){
            callback("请输入金额");
        }else if(this.props.data.buyStyle==1){
            this.refs.amount.value=this.props.data.realize.account;
            if(remAccount<=userMoney){
                callback()
            }else{
                callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
            }
        }else if(remAccount<this.objVal.lowestAccount*2){
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
                    callback('您当前可投金额'+y+'元小于'+min1+'元，无法投资');
                }
            }else{
                if(value<this.objVal.lowestAccount){
                    if(value<=userMoney){
                        if(this.objVal.lowestAccount==min){
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”');
                        }else{
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”');
                        }
                    }else{
                        if(this.objVal.lowestAccount==min){
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”，请<a href="/member/recharge/detail.html">立即充值</a>');
                        }else{
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”，请<a href="/member/recharge/detail.html">立即充值</a>');
                        }
                        
                    }
                }else{
                    if(value<=min){
                        if(value<=userMoney){
                            callback()
                        }else{
                            callback('账户余额不足，请<a href="/member/recharge/detail.html">立即充值</a>。');
                        }
                    }else{
                         if(this.objVal.lowestAccount==min){
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元”');
                         }else{
                            callback('您当前可投金额为“'+this.objVal.lowestAccount+'元~'+min+'元”');
                         }
                    }
                }
            }
        }
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
                    //error("网络异常，请重试！");
                }
            })
    }
	countIncomeHander(e){
		let el = e.target;
        this.RxgAccountYes(e.target.value,this.ErrorShow)
		if(el.value !== '' && !Reg.isNumber(el.value)){
			this.setState({inputChangeStatus: true});			
			this.getData({"projectId": this.objVal.id, "amount": el.value});
		} else {
			this.setState({inputChangeStatus: false, inputVal: 0});		
		}
	}
	getData(PostData){//计算收益			
		let self = this;
		let money=this.props.remainDate;
		if(money<this.objVal.lowestAccount){
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
        self.setState({incomeData: data,userInvestMoney:data.userInvestAmount});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
	}
	componentDidMount(){
        this.getUserMoney();
        let _this=this;
            if(_this.props.data.buyStyle==1) {
                _this.setState({inputChangeStatus: true,hasError:false});
                _this.getData({"projectId": _this.objVal.id, "amount": _this.props.data.realize.account});
            }else if(this.props.remainDate< _this.objVal.lowestAccount*2){
                _this.refs.amount.value =  this.props.remainDate;
                _this.setState({inputChangeStatus: true});
                _this.getData({"projectId":  _this.objVal.id, "amount": this.props.remainDate});
            }else{
                _this.getData({"projectId": _this.objVal.id, "amount": 1});
            }

	}
	render(){
		const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
		let data = this.props.data;
		let AllMoneyInput = [];
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
		let progressVal = (1-this.props.remainDate/data.realize.account)*100 + "%";
		let projectId = $("#projectId").val();
		//起投金额提示
		const lowestAccountText = "起投金额"+ this.objVal.lowestAccount +"元";
		//债权标ID			
		if(!this.objVal.id){
			this.objVal.id = $("#projectId").val();
		}
        if(data.buyStyle==1){
            AllMoneyInput =  <input type="text" value={this.props.data.realize.account} name="amount" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder={lowestAccountText} onKeyUp={this._countIncomeHander}/>
        }else{
            AllMoneyInput = <input type="text" name="amount" autoComplete="off" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder={lowestAccountText} onKeyUp={this._countIncomeHander}/>
        }
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">投资<span className="max-number">最高可投 {data.realize.mostAccount}元</span></div>
	            <div className="surplus">剩余可投<s>{this.props.remainDate}元</s></div>
	            <div className="progress"><s style={{width: progressVal}}></s></div>
	            <Form horizontal action="/invest/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
					  <div className="ant-form-item hasunit">
						  <div className={this.state.stateClass}>
                              {AllMoneyInput}
                              <span className="unit">元</span>
							  <div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}>
							  </div>
						  </div>
					  </div>
	              </div>
	              <Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />              
	              <div className="btn-instant">
	              	<input type="hidden" name="projectId" value={projectId} />
	              	<input type="hidden" name="isSelectedTip" value="1" />
	                <FormItem style={{ marginTop: 24 }}>
	                	<div className="submitdom"><Dopay msgdata = {this.props.msgdata}/></div>	                 
	                </FormItem>  
	              </div>
	            </Form>
	        </div>
		)
	}
}