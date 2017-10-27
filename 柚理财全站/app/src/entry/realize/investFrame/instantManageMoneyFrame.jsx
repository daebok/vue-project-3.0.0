import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Modal, InputNumber } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

//即息理财框state=1
export default class InstantManageMoneyFrame extends React.Component{
	constructor(props){
	    super(props);
	    this._handleSubmit = this.handleSubmit.bind(this);	   
	    this._RxgAccountYes = this.RxgAccountYes.bind(this);	   
	    this.objVal = {};	    
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
	    }else if(Reg.isNumber(value)){        
	      callback([new Error('抱歉，请输入正确的金额')]);        
	    }else if(value>=this.objVal.accountYes){
	      callback([new Error('抱歉，输入金额不能大于剩余可投金额')]);
	    }else{
	      callback();
	    }
	}	
	render(){
		const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
	    const amountProps = getFieldProps('amount', {
	      rules: [
	        { required: true, message: '请输入份数' },
	        { validator: this._RxgAccountYes}
	      ],
	    });
		let data = this.props.data;
		//剩下金额
	    if(!this.objVal.accountYes){
	      this.objVal.accountYes = data.accountYes;
	    }
		//剩下可投进度条    
    	let progressVal = data.accountYes/data.account*100 + "%";		
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="max">投资<span className="max-number">最高可投 {data.mostAccount}份</span></div>
	            <div className="surplus">剩余可投<s>{data.accountYes}份</s></div>
	            <div className="progress"><s style={{width: progressVal}}></s></div>
	            <Form horizontal action="/src/views/invest/order.html" method="post" target="_blank" className="form-themes form-detail" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
	                <div className="col-24 hasunit instantManageMoney-hasunit">
	                  <FormItem                         
	                    hasFeedback
	                    help={isFieldValidating('amount') ? '校验中...' : (getFieldError('amount') || []).join(', ')}
	                  >
	                  	<InputNumber name="amount" size="large" placeholder="起始份数1份" min={1} max={100000} maxLength="10"/>
	                    <input type="hidden" value={data.projectTypeId} id="projectId"/>
	                  </FormItem><span className="unit">份</span>
	                </div>
	              </div>
	              <div className="show-income">输入份数查看收益</div>               
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