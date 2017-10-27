import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import {Select, Radio, Button, Form, Input, Checkbox, Modal, Cascader} from 'antd';
const Option = Select.Option;
const RadioGroup = Radio.Group;
const createForm = Form.create;
const FormItem = Form.Item;

function noop() {
  return false;
}

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}


let Personageinfoedit = React.createClass({
	getInitialState(){
		return {
			data: "",
			RadioValue1: "0",
			RadioValue2: "0",
		}
	},
  success(v,fun){
  Modal.success({
    title: '成功提示',
    content: v,
    okText:"关闭",
    onOk() {
      fun();
    }
  });
  },  
	handleSubmit(e) {
	    e.preventDefault();	    
	    this.props.form.validateFieldsAndScroll((errors, values) => {
	      if (!!errors) {
	        console.log('Errors in form!!!');
	        return;
	      }	
        console.log(values);
        const self = this;     
	      $.ajax({
	        url: '/member/baseInfo/modifyUserInfo.html',
	        type: 'POST',
	        dataType: 'json',
	        data: values
	      })
	      .done(function(data) {
	        if(data.result){
	        	self.success(data.msg,self.props.changePersonageinHander);
            selft.props.changePersonageinHander();
	        }
	        else
	        {
	          error(data.msg,data.url);	         
	        }
	      })
	      .fail(function() {
	        error('网络异常，请重试！');
	      });
	    });
	},
	handleReset(e) {		
		e.preventDefault();
		this.props.form.resetFields();
	},
	componentDidMount(){
		let self = this;	
		$.ajax({
			url: '/member/baseInfo/getUserInfo.html',
			type: 'POST',
			dataType: 'json',			
		})
		.done(function(data) {
      if( data.result ){
        self.setState({data: data});
        self.setdefault();
      }
      else
      {
        error(data.msg,data.url);
      }      
		})
		.fail(function() {
			error('网络异常，请重试！');
		})
	},
	setdefault(){
		this.props.form.setFieldsValue({
		  userName:this.state.data.userName,
		  carStatus:this.state.data.userBaseInfo.carStatus ? this.state.data.userBaseInfo.carStatus : "0",
		  houseStatus:this.state.data.userBaseInfo.houseStatus ? this.state.data.userBaseInfo.houseStatus: "0",
		  zone:[this.state.data.userCache.province, this.state.data.userCache.city, this.state.data.userCache.area],
		  address:this.state.data.userCache.address,
		});
	},
  	render() {
      let data = this.state.data;
      if(!data){
        return false;
      }
      
      const address = areajson;
 	  	
	    const { getFieldProps, getFieldError, isFieldValidating ,setFieldsValue} = this.props.form;
	    const userNameProps = getFieldProps('userName', {
	    	validate: [{
				rules:[
				  { required: true, message: '请输入6-20位字符，由字母或字母加数字组成' },
				  {validator:ValidateFn.regexUserNameAjax.bind(this)}
				],trigger: ['onBlur'],				
			}]	    	
	    });
	    const educationnProps = getFieldProps('education', {
            initialValue:data.userBaseInfo.education
        });
        const maritalStatusProps = getFieldProps('maritalStatus', {
            initialValue:data.userBaseInfo.maritalStatus,
            rules: [
                {message: '请选择您的婚姻状况' },
            ],
        });
		const workExperienceProps = getFieldProps('workExperience', {
            initialValue:data.userBaseInfo.workExperience,
            rules: [
                {message: '请选择您的工作年限' },
            ],
        });
		const monthIncomeRangeProps = getFieldProps('monthIncomeRange', {
            initialValue:data.userBaseInfo.monthIncomeRange,
            rules: [
                { required: true, message: '请选择您的年收入范围' },
            ],
        });
	    const zoneProps = getFieldProps('zone', {
	    	rules: [{ type: 'array', message: '请选择您的所在地区' }],
	    });
	    const addressProps = getFieldProps('address', {
	    	initialValue:data.userBaseInfo.address,
	    	rules: [{message: '请填写具体地址' }],
	    });
	    const carStatusProps = getFieldProps('carStatus', {
			rules: [
				{ required: true, message: '请选择有无车产' },
			],
	    });
	    const houseStatusProps = getFieldProps('houseStatus', {
			rules: [
				{ required: true, message: '请选择有无房产' },
			],
	    });	
	    const professionProps = getFieldProps('profession', {
            initialValue:data.userBaseInfo.professionStr
        });

   	
	   	//用户附属信息
	   	let userCache = data.userCache;
	   	//用户个人基本信息
	   	let userBaseInfo = data.userBaseInfo;
	   	//判断是否已修改过一次
	   	let userNameHtml = <Input {...userNameProps} type="text" style={{width:"310px"}} className="input long-input" maxLength="20" autoComplete="off" placeholder="6-20位字符，由字母或字母加数字组成" />;
	   	if(userCache.renameFlag === "1"){
	   		userNameHtml = <p className="txt">{data.userName}</p>
	   	}
	   	//性别
	   	let sex = userCache.sex == undefined ? "" : Publiclib.getSex(userCache.sex);	   	
	   	//出生日期
	   	let birthday = userBaseInfo.birthday == undefined ? "" : Publiclib.formatDate(userBaseInfo.birthday);
	   	return (	    	
	    	<Form horizontal form={this.props.form} onSubmit={this.handleSubmit}>               
	    		<ul className="form-list">
					<li>
						<label className="label">用户名<em className="star">*</em></label>
						<div className="form-item">
							<FormItem hasFeedback>
								{userNameHtml}							
	            </FormItem>
						</div>
						<span className="tips2">只允许修改一次</span>
					</li>
					<li>
						<label className="label">出生日期</label>
						<div className="form-item">
							<FormItem>
								<p className="txt">{birthday}</p>
	              			</FormItem>
						</div>
					</li>
					<li>
						<label className="label">性别</label>
						<div className="form-item">
							<FormItem>
								<p className="txt">{sex}</p>
	              			</FormItem>
						</div>
					</li>
					<li>
              <label className="label">学历</label>
              <div className="form-item">
              	<FormItem>
                    <Select {...educationnProps} placeholder="请选择学历" style={{ width: '100px' }}>
                      {
                        data.educationList.map(function(k,v){                                    
                          return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
                        })
                       }  
                    </Select>
                </FormItem>
              </div>                        
          </li>
          <li>
              <label className="label">职业</label>
              <div className="form-item">
              	<FormItem>
                    <Input {...professionProps} placeholder="请输入职业" />
                </FormItem>
              </div>                        
          </li>
          <li>
              <label className="label">婚姻状况</label>
              <div className="form-item">
              	<FormItem>
                    <Select {...maritalStatusProps} placeholder="请选择婚姻状况" style={{ width: '100%' }}>
                        <Option value="0">未婚</Option>
                        <Option value="1">已婚</Option>	                               
                        <Option value="2">离异</Option>	                               
                        <Option value="3">丧偶</Option>	                               
                    </Select>
                </FormItem>
              </div>                        
          </li>
           <li>
              <label className="label">所在地区</label>
              <div className="form-item">
                <div>
	                <FormItem>
					          <Cascader {...zoneProps} options={address} style={{width:'400px'}} />
					        </FormItem>
                </div>
				        <div className="line-inputBox">
				        	<FormItem hasFeedback>
								     <Input {...addressProps}  style={{width:"400px"}} type="text" className="input long-input" maxLength="50" autoComplete="off" placeholder="具体地址，不超过50个字符"  />					
	              	</FormItem>
				        </div>                      	
              </div>                        
          </li>
         <li>
              <label className="label">工作年限</label>
              <div className="form-item">
              	<FormItem>
                    <Select {...workExperienceProps} placeholder="请选择工作年限" style={{ width: '150px' }}>
                      {
                        data.workExperienceList.map(function(k,v){                                    
                          return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
                        })
                       }                                
                    </Select>
                </FormItem>
              </div>                        
          </li>
           <li>
              <label className="label">年收入<em className="star">*</em></label>
              <div className="form-item">
              	<FormItem>
                    <Select {...monthIncomeRangeProps} placeholder="请选择年收入范围"  style={{ width: '150px' }}>
                      {
                        data.monthIncomeRangeList.map(function(k,v){                                    
                          return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
                        })
                       } 
                    </Select>
                </FormItem>
              </div>                        
          </li>
					<li>
                        <label className="label">有无车产<em className="star">*</em></label>
                        <div className="form-item">
                        	<FormItem>
								<RadioGroup {...carStatusProps}>								
									<Radio value="0">无</Radio>
									<Radio value="1">有</Radio>
								</RadioGroup>								
							</FormItem>
                        </div>                        
                    </li>
					<li>
                        <label className="label">有无房产<em className="star">*</em></label>
                        <div className="form-item">
                        	<FormItem>
								<RadioGroup {...houseStatusProps}>								
									<Radio value="0">无</Radio>
									<Radio value="1">有</Radio>
								</RadioGroup>								
							</FormItem>
                        </div>                        
                    </li>
                    <li>                        
                        <div className="form-item">
                        	<FormItem>
								<Button type="primary" className="btn submitBtn" onClick={this.handleSubmit}>保存</Button>
								&nbsp;&nbsp;&nbsp;
								<Button type="ghost" className="btn cancelBtn ml20" onClick={this.props.changePersonageinHander}>取消</Button>
							</FormItem>
                        </div>                        
                    </li>					
				</ul>
			</Form>
	    );
	}
});
export default Personageinfoedit = createForm()(Personageinfoedit);