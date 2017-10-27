import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form,Input, Modal, InputNumber } from 'antd';
import RemainingAmount from './remainingAmount';
const createForm = Form.create;
const FormItem = Form.Item;
import Dopay from './dopay';
function error(v) {
    Modal.error({
        title: v,
        okText:"关闭",
        wrapClassName:'tiperror'
    });
}
class Income extends React.Component{
	render(){
		let html = this.props.inputChangeStatus ? <div><p className="preInterest"><span className="tit">预期收益</span>{this.props.data.interest}元</p></div> : "输入份数查看收益";
		return (
			<div className="show-income mt15" id="income">
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

//即息理财框state=1
class NoPassFrame extends React.Component{
	constructor(props){
	    super(props);
	    this._computeChange = this.computeChange.bind(this);	   
	    this._handleSubmit = this.handleSubmit.bind(this);	   
	    this.RxgAccountYes = this.RxgAccountYes.bind(this);
		this.countIncomeHander = this.countIncomeHander.bind(this);
		this._getUserMoney = this.getUserMoney.bind(this);
        this.ErrorShow = this.ErrorShow.bind(this);
        this.changeStep=this.changeStep.bind(this);
	    this.objVal = {};
		this.state = {
			inputChangeStatus: false,
			incomeData: '',
			isshow:true,
            stateClass:'ant-form-item-control has-feedback ',
            hasError:true,
            userInvestMoney:'',
            ErrorText:''
		};
	}
	handleSubmit(e) {
        this.RxgAccountYes(this.refs.amount.value,this.ErrorShow);
        if(this.state.hasError==true){
            e.preventDefault();
            return false
        }
	    this.props.form.validateFields((errors, values) => {
	      if (!!errors) {        
	        console.log(errors,'Errors in form!!!');
	        e.preventDefault();
	        return;
	      }
	    });
	}
	componentWillMount(){
		this._getUserMoney();
    //this.getData({"projectId":$("#projectId").val(), "amount": 0});
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
    ErrorShow(error){
        if(error==undefined){
            this.setState({
                hasError:false,
                ErrorText:'',
                stateClass:'ant-form-item-control has-feedback has-success'
            })
        }else{
            this.setState({
                hasError:true,
                ErrorText:error,
                stateClass:'ant-form-item-control has-feedback has-error'
            })
        }
    }
	RxgAccountYes(value, callback){
		let data=this.props.data;
		this.refs.realInput.value=value*data.project.copyAccount;
	    value=value*data.project.copyAccount;
		let copyAccount=data.project.copyAccount;
        let remAccount=this.refs.remAmount.getRemainAccount();
        let min=Math.min(this.props.data.project.mostAccount-this.state.userInvestMoney,remAccount);
        let userMoney=this.state.useMoney;
        if(!value || value == "0"){
            callback("请输入份数");
        }else if(min==0){
            callback('您已达到投资上限，无法投资');
        }else if(value != 0 && Reg.isNumber(value)){
            callback('抱歉，投资请输入整数份数');
        }else if(remAccount<=this.objVal.lowestAccount&&remAccount<=userMoney){
            this.refs.amount.value = remAccount/copyAccount;
            this.refs.realInput.value = remAccount;
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
    changeStep(e){
		let type=$(e.target).attr('data');
		if(type=='down'){
			if(this.refs.amount.value>1){
                this.refs.amount.value=this.refs.amount.value-1
			}
		}else{
			if(this.refs.amount.value==''){
                this.refs.amount.value=1
			}else{
                this.refs.amount.value=parseInt(this.refs.amount.value)+1
            }
		}
        this.RxgAccountYes(parseInt(this.refs.amount.value),this.ErrorShow)
        this.setState({inputChangeStatus: true});
        this.getData({"projectId": this.objVal.id, "amount": this.refs.amount.value*this.props.data.project.copyAccount});
	}
	countIncomeHander(e){
        this.RxgAccountYes(e.target.value,this.ErrorShow);
		e=e.target.value;
        this.refs.amount.value=e.replace(/[^\d]/g,'')
		if(e!= '' && !Reg.isNumber(e) && e!=0){
			if($('#amount').val()!=0){
				this.setState({inputChangeStatus: true});
				this.getData({"projectId": this.objVal.id, "amount": e*this.props.data.project.copyAccount});
			}else{
				this.setState({inputChangeStatus: false, inputVal: 0});
			}
		} else {
			this.setState({inputChangeStatus: false, inputVal: 0});
		}
	}
	computeChange(value){
		this.props.form.setFieldsValue({
		  amount:value
		});
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
					self.setState({incomeData: data,userInvestMoney:data.userInvestAmount});
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
		//剩下可投进度条    
    	let progressVal = data.project.mostAccount/data.project.copyAccount;
		if(!this.objVal.id){
			this.objVal.id = $("#projectId").val();
		}
        if(!this.objVal.lowestAccount){
            this.objVal.lowestAccount = data.project.lowestCopies*data.project.copyAccount;
        }
		let lowerAccount=data.project.lowestCopies==0?'1':data.project.lowestCopies;
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">最高可投<span className="max-number">{progressVal}份</span></div>
	            <RemainingAmount id={this.props.id} ref='remAmount' account={data.project.account} copyAccount={data.project.copyAccount} type={'份'}/>
	            <Form horizontal action="/invest/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row instantManageMoney ant-form-item">
					  <div className={this.state.stateClass}>
						<div className="col-24 hasunit instantManageMoney-hasunit">
							<input type="text" ref="amount" id="amount" maxLength="10" className="ant-input ant-input-lg" placeholder={'起投份数'+lowerAccount+'份'} autoComplete="off" onKeyUp={this.countIncomeHander}/>
							<a href="javascript:;" className="btn btn-down" data="down" onClick={this.changeStep}>-</a>
							<a href="javascript:;" className="btn btn-up" data="up" onClick={this.changeStep}>+</a>
							<span className="unit">份</span>
						</div>
					    <div className={this.state.ErrorText==''?'ant-form-explain hide':'ant-form-explain'} dangerouslySetInnerHTML={{__html:this.state.ErrorText}}></div>
					  </div>
	              </div>
				  <input type="hidden" name="amount" ref="realInput"/>
	              <input type="hidden" value={data.project.projectTypeId} id="projectId"/>
					<Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />
					<input type="hidden" name="projectId" value={this.objVal.id} />
	              <div className="btn-instant">
	                <FormItem style={{ marginTop: 24 }}>
						<div className="submitdom"><Dopay msgdata = {this.props.msgdata}/></div>
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