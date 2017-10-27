import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Button, Form, Input ,Checkbox, Modal, Tabs } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;

//项目预告state=3
export default class DirectionObjectFrame extends React.Component{
	constructor(props){
	    super(props);
	    this._handleSubmit = this.handleSubmit.bind(this);
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
		
	render(){
		const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
	    const passWordProps = getFieldProps('passWord', {
	      rules: [
	        { required: true,message: '请输入定向密码' },	       
	      ],
	    });
		let data = this.props.data;		
    	
    	let projectId = $("#projectId").val(); 	
		return (
			<div className="header-from site-col-7 site-col-last">
	            <div className="picBox">
	            	<span className="iconfont">&#xe646;</span>
	            	<p className="text">输入定向密码后方可进行投资</p>
	            </div>
	            <Form horizontal action="/invest/order.html" method="post" target="_blank" className="form-themes form-detail from-picBox" form={this.props.form} onSubmit={this._handleSubmit}>
	              <div className="row">
	                <div className="col-24 hasunit">
	                  <FormItem                         
	                    hasFeedback
	                    help={isFieldValidating('passWord') ? '校验中...' : (getFieldError('passWord') || []).join(', ')}
	                  >
	                    <Input type="password" {...passWordProps} name="passWord" maxLength="10" placeholder="请输入定向密码" />	                    
	                  </FormItem>                  
	                </div>
	              </div>	                         
	              <div className="btn-instant">
	              	<input type="hidden" name="projectId" value={projectId} />
	                <FormItem style={{ marginTop: 24 }}>
	                  <Button className="subBtn" type="primary" htmlType="submit">确认密码</Button>
	                </FormItem>  
	              </div>
	            </Form>
	        </div>
		)
	}
}