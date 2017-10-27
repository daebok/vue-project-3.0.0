import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Modal, InputNumber } from 'antd';
import RemainingAmount from './remainingAmount';
const createForm = Form.create;
const FormItem = Form.Item;

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
//即息理财框state=1
export default class InstantManageMoneyFrame extends React.Component{
	constructor(props){
	    super(props);
	    this._computeChange = this.computeChange.bind(this);	   
	    this._handleSubmit = this.handleSubmit.bind(this);	   
	    this._RxgAccountYes = this.RxgAccountYes.bind(this);
		this._countIncomeHander = this.countIncomeHander.bind(this);
	    this.objVal = {};
		this.state = {
			inputChangeStatus: false,
			incomeData: '',
			isshow:true,
		};
	}
	handleSubmit(e) {    
	    this.props.form.validateFields((errors, values) => {
	      if (!!errors) {        
	        console.log(errors,'Errors in form!!!');
	        e.preventDefault();
	        return;
	      }
	    });
	}
	RxgAccountYes(rule, value, callback){
		let data=this.props.data;
		this.refs.realInput.value=value*data.project.copyAccount
	    if(!value){
	      callback();
	    }else if(Reg.isNumber(value)){        
	      callback([new Error('抱歉，请输入正确的份数')]);
	    }else if(value>(data.project.account-data.project.accountYes)/data.project.copyAccount){
	      callback([new Error('抱歉，输入份数不能大于剩余可投份数')]);
	    }else if(value<data.project.lowestCopies&&data.project.lowestAccount/data.project.copyAccount>(data.project.account-data.project.accountYes)/data.project.copyAccount){
			this.props.form.setFieldsValue({
				amount: (data.project.account-data.project.accountYes)/data.project.copyAccount,
			});
			callback([new Error('请输入剩余可投份数')]);
		}else if(value<data.project.lowestCopies){
			callback([new Error('抱歉，输入份数不能小于起投份数')]);
		}else{
	      callback();
	    }
	}
	countIncomeHander(e){
		this.props.form.setFieldsValue({
			amount:e
		});
		if(e!= '' && !Reg.isNumber(e)){
			this.setState({inputChangeStatus: true});
			this.getData({"projectId": this.objVal.id, "amount": e*this.props.data.project.copyAccount});
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
	 //    const amountProps = getFieldProps('amount', {
	 //      rules: [
	 //        { required: true, message: '请输入份数' },
	 //        //{ validator: this._RxgAccountYes}
	 //      ],
	 //    });

  const { getFieldProps, getFieldError, isFieldValidating,setFieldsValue} = this.props.form;
  const amountProps = getFieldProps('amount', {
        validate: [
        {
          rules:[
			  { required: true, message: '请输入份数' },
			  { validator: this._RxgAccountYes}
          ],
           trigger: ['onBlur'],
        }
        ]
      });

		let data = this.props.data;
		//剩下可投进度条    
    	let progressVal = data.project.mostAccount/data.project.copyAccount;
		if(!this.objVal.id){
			this.objVal.id = $("#projectId").val();
		}
		var lowerAccount=data.project.lowestCopies==0?'1':data.project.lowestCopies;

		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">投资<span className="max-number">最高可投 {progressVal}份</span></div>
	            <RemainingAmount id={this.props.id} account={data.project.account} copyAccount={data.project.copyAccount} type={'份'}/>
	            <Form horizontal action="/invest/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
	                <div className="col-24 hasunit instantManageMoney-hasunit">
	                  <FormItem                         
	                    hasFeedback
	                  ><InputNumber {...amountProps} size="large" placeholder="请输入份数" onChange={this._countIncomeHander}  step={1}   min={lowerAccount} max={(data.project.account-data.project.accountYes)/data.project.copyAccount} maxLength="10"/>
	                  </FormItem><span className="unit">份</span>
						<input type="hidden" name="amount" ref="realInput"/>
	                </div>
	              </div>
	              <input type="hidden" value={data.project.projectTypeId} id="projectId"/>
					<Income inputChangeStatus={this.state.inputChangeStatus} data={this.state.incomeData} />
					<input type="hidden" name="projectId" value={this.objVal.id} />
	              <div className="btn-instant">
	                <FormItem style={{ marginTop: 24 }}>
	                  <Button className="subBtn" type="primary" htmlType="submit">立即投资</Button>
	                </FormItem>  
	              </div>
	            </Form>
	        </div>
		)
	}
}