import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Input ,Checkbox, Modal, Tabs } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;
//弹窗
import Dopay from './dopay';

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
		let html = this.props.inputChangeStatus ? <div><p className="preInterest"><span className="tit">预期收益</span>{this.props.data.interest}元</p></div> : "输入查看收益";		
		return (
			<div className="show-income" id="income">		
				{html}
			</div>
		)
	}
}


class HasPassFrame extends React.Component{

	constructor(props){
	    super(props);
	    this._passWord = this.passWord.bind(this);    
	    this.objVal = {};
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',
				isshow:true,		
			};         
	}
	passWord(e){
	    this.props.form.validateFields((errors, values) => {
	      e.preventDefault();
	      if (!!errors) {
	        console.log(errors);	        
	        return;
	      }
	      console.log(values);
	      let self = this;
	      $.ajax({
	        url: '/project/checkPwd.html',
	        type: 'POST',
	        dataType: 'json',
	        data: values
	      })
	      .done(function(data) {
	 				
	      	if(data.result){
	      		self.props.close();
	      	}
	      	else
	      	{
	      		error(data.msg);
	      	}	

	      })
	      .fail(function() {
	        error('网络异常，请重试！');
	      });	      
	    });
	}
	render(){
		const { getFieldProps, getFieldError, isFieldValidating,setFieldsValue} = this.props.form;

    const passProps = getFieldProps("pwd",{
    	//initialValue:this.state.pwdValue,
      rules: [	        
        { required: true, message: '请填写密码'}
      ],
    });

    const idProps = getFieldProps("projectId",{
    	initialValue:$("#projectId").val()
    });

		let data = this.props.data;
		let msgdata = this.props.msgdata;
		if(!data || !msgdata) return false;		
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="check-password">
	            	<Form horizontal className="form-themes form-detail" form={this.props.form} onSubmit={this._passWord}>
		              <div className="row">
		              	<div className="col-24 help-text">
		              		<em className="iconfont">&#xe62a;</em>
		              		输入定向密码后方可进行投资
		              	</div>
		                <div className="col-24">
		                  <FormItem hasFeedback>
		                    <Input {...passProps} type="password" autoComplete="off" maxLength="6" placeholder='请输入密码' />	                    
		                  </FormItem>
		                </div>
		              </div>		              
		              <div className="btn-instant" style={{position:'static'}}>
		              	<input type="hidden" {...idProps} />
		                <FormItem style={{ marginTop:12 }}>
		                  <Button className="subBtn" type="primary" htmlType="submit">确认密码</Button>
		                </FormItem>  
		              </div>	              
	            	</Form>
	            </div>
	        </div>
		)
	}
}


class NoPassFrame extends React.Component{

	constructor(props){
	    super(props);		   
	    this._handleSubmit = this.handleSubmit.bind(this);	   
	    this._RxgAccountYes = this.RxgAccountYes.bind(this);
		this._countIncomeHander = this.countIncomeHander.bind(this);
		this._getData = this.getData.bind(this);
		this._getUserMoney = this.getUserMoney.bind(this);
		this.ErrorShow = this.ErrorShow.bind(this);
	    this.objVal = {};
	    this.hasError=false;
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',
				checked: true,
				useMoney:'',
				stateClass:'ant-form-item-control has-feedback ',
				userInvestMoney:'',
				hasError:true,
				ErrorText:'',
				money:''
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
	      console.log(values);
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
	componentDidMount(){
        var _this=this;
        $.ajax({
            url: '/project/remainAccount.html',
            type: 'POST',
            dataType: 'json',
            data: {"projectId":_this.props.id}
        })
        .done(function(msgdata) {
            if(msgdata.remainAccount<= _this.objVal.lowestAccount*2){
                _this.refs.amount.value =  msgdata.remainAccount;
                _this.setState({inputChangeStatus: true,money: msgdata.remainAccount});
                _this.getData({"projectId":  _this.objVal.id, "amount":  msgdata.remainAccount});
            }else{
                _this.setState({money: msgdata.remainAccount});
			}
        })
		this._getUserMoney();

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
		let remAccount=this.state.money;
		let min=Math.min(this.objVal.mostAccount-this.state.userInvestMoney,remAccount);
        let min1=Math.min(this.objVal.lowestAccount,remAccount);
        let y=this.objVal.mostAccount-this.state.userInvestMoney;
		let userMoney=this.state.useMoney;
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
					if(this.props.data.project.stepAccount>0 && (value-this.objVal.lowestAccount) % this.props.data.project.stepAccount!=0){
                        callback("金额必须为递增金额的整数倍+起投金额");
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
	}
	countIncomeHander(e){
		let el = e.target;
		this.RxgAccountYes(e.target.value,this.ErrorShow)
		if(el.value !== '' && !Reg.isNumber(el.value)){
			this.setState({inputChangeStatus: true});
			this.getData({"projectId": this.objVal.id, "amount": el.value});
		} else {
			this.setState({inputChangeStatus: false});
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
        self.setState({
			incomeData: data,
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
	render(){
		const { getFieldProps, getFieldError, isFieldValidating,setFieldsValue} = this.props.form;
		let data = this.props.data;
		let msgdata = this.props.msgdata;
		if(!data || !msgdata) return false;
		
		//剩下可投金额
		if(!this.objVal.butSendMoney){
			this.objVal.butSendMoney = parseFloat(data.project.account - data.project.accountYes);			
		}
		//单笔最低投资金额
		if(!this.objVal.lowestAccount){
			this.objVal.lowestAccount = data.project.lowestAccount || 100;
		}
		//单笔最高投资金额
		if(!this.objVal.mostAccount){
			this.objVal.mostAccount = data.project.mostAccount || data.project.account;
		}
		//剩下可投进度条    
        let progressVal = (1-this.state.money/data.project.account)*100 + "%";
		//起投金额提示
		let lowestAccountText = "起投金额"+ this.objVal.lowestAccount +"元";
		//债权标ID			
		if(!this.objVal.id){
			this.objVal.id = $("#projectId").val();
		}
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">最高可投<span className="max-number">{data.project.mostAccount}元</span></div>
				<div className="surplus">剩余可投<s>{this.state.money}元</s></div>
				{/*<div className="progress"><s style={{width: progressVal}}></s></div>*/}
	            <Form horizontal action="/invest/order.html" method="post" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
	                <div className="col-24 hasunit">
						<div className="ant-form-item">
							<div className={this.state.stateClass}>
							   <input type="text" name="amount" autoComplete="off" ref="amount" className="ant-input ant-input-lg" maxLength="10" placeholder={lowestAccountText} onInput={this._countIncomeHander} onPropertyChange={this._countIncomeHander} />
								<span className="unit">元</span>
								<div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}>
								</div>
								<div className={data.project.stepAccount>0?'ant-form-extra':'ant-form-extra hide'}>
									递增金额{data.project.stepAccount}元
								</div>
							</div>
						</div>
	                </div>
	              </div>
	              <Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />              
	              <div className="btn-instant">
	              	<input type="hidden" name="projectId" value={this.objVal.id} />
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

//投资框state=0
export default class DefaultInvestFrame extends React.Component{
	constructor(props){
	    super(props);
			this._setIframe = this.setIframe.bind(this);	    
	    this.objVal = {};
			this.state = {
				inputChangeStatus: false,	
				incomeData: '',
				showFlag : true		
			};         
	}
	componentWillMount(){
		let data = this.props.data;
		if( data.project.status == "4" && data.project.specificSale == "1" ){
			this.setState({
				showFlag : false
			})
		}
	}
	setIframe(){
		this.setState({
			showFlag : true
		})
	}
	render(){
		let data = this.props.data;
		let frameHtml = this.state.showFlag ? <NoPassFrame form={this.props.form} id={this.props.id} data={data} msgdata={this.props.msgdata} /> : <HasPassFrame form={this.props.form} data={data} close = {this._setIframe} msgdata={this.props.msgdata} />;
		
		if( (data.project.specificSale == "2" || data.project.specificSale == "3") && data.project.specificSaleRule && data.project.specificSaleRule != "" )
		{
			frameHtml = <div className="nothing"><em className="iconfont">&#xe647;</em><span className="title-text">{data.project.specificSaleRule}</span><a href="/invest/index.html">看看其他项目</a></div>;
		}
		return (
			<div>
				{frameHtml}
			</div>
			)
	}
}