import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import {Select, Radio, Button, Form, Input, Checkbox, Modal, Cascader, DatePicker} from 'antd';
const RadioGroup = Radio.Group;
const createForm = Form.create;
const FormItem = Form.Item;
const Option = Select.Option;

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
        window.location.href = url;
      }
    }
  });
}

let Companyageinfoedit = React.createClass({
	getInitialState(){
		return {
			data: "",
			dateInitialValue: "",
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
	      const self = this;
        if(!$(".ant-calendar-picker-input").val()){
          error("请填写企业成立时间");
          return false;
        }
        values["establishDateStr"]=	$(".ant-calendar-picker-input").val();      
	      $.ajax({
	        url: '/member/baseInfo/modifyCompanyInfo.html',
	        type: 'POST',
	        dataType: 'json',
	        data: values
	      })
	      .done(function(data) {
	        if(data.result){
	        	self.success(data.msg,self.props.changeCompanyageinHander);
	        	self.props.changeCompanyageinHander();
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
			url: '/member/baseInfo/getCompanyInfo.html',
			type: 'POST',
			dataType: 'json',			
		})
		.done(function(data) {
        if(data.result){
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
	setDate(date){		
		this.setState({dateInitialValue: Publiclib.formatDate(date)});
	},
	setdefault(){
		let data = this.state.data,datetime = "";
    this.props.form.setFieldsValue({
    	userName:data.userName,
    	simpleName:data.userCompanyInfo.simpleName,
    	zone:[data.userCache.province, data.userCache.city, data.userCache.area],
		address: data.userCache.address,
		officeAddress: data.userCompanyInfo.officeAddress,
    	contacts:data.userCompanyInfo.contacts,
    	bussinessCode:data.userCompanyInfo.bussinessCode,
      //legalDelegate:data.userCompanyInfo.legalDelegate,
			taxRegNo:data.userCompanyInfo.taxRegNo,
    	telephone:data.userCompanyInfo.telephone,
    	linkmanName:data.userCompanyInfo.contacts,    	
    	naturalPerson:data.userCompanyInfo.naturalPerson,
    	legalPerson:data.userCompanyInfo.legalPerson,
    });

    if(data.userCompanyInfo.registeredCapital){
      this.props.form.setFieldsValue({
        registeredCapital:data.userCompanyInfo.registeredCapital ? data.userCompanyInfo.registeredCapital.toString() : ""
      });
    }

    if(data.userCompanyInfo.officeProvince){
		this.props.form.setFieldsValue({
			officeZone:[data.userCompanyInfo.officeProvince, data.userCompanyInfo.officeCity, data.userCompanyInfo.officeArea]
		})
	}

  },
  	render() {			
	    let data = this.state.data;
	   	if(!data){
	   		return false;
	   	} 
			let self = this;			  	
	    const zone = areajson;
	    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
	    const userNameProps = getFieldProps('userName', {
	    	validate: [{
				rules:[
				  { required: true, message: '请输入6-20位字符，由字母或字母加数字组成' },
				  {validator:ValidateFn.regexUserNameAjax.bind(this)}
				],trigger: ['onBlur'],				
			}]	    	
	    });
		const simpleNameProps = getFieldProps('simpleName', {
	    	validate: [{
				rules:[
				  { required: true, message: '请填写企业简介' },				  
				],		
			}]	    	
	    });
		const registeredCapitalProps = getFieldProps('registeredCapital', {
	    	validate: [{
				rules:[
				  { required: true, message: '请填写注册资本' },				  
				],		
			}]	    	
	    });
	   //  const establishDateStrProps = getFieldProps('establishDateStr', {
    //     initialValue: self.state.dateInitialValue ? self.state.dateInitialValue : data.userCompanyInfo.establishDate ? Publiclib.formatDate(data.userCompanyInfo.establishDate) : ,
				// format: "yyyy-MM-dd",				
	   //  	validate: [{
				// rules:[
				// 		{ required: true, message: '请填写企业成立时间' },				  
				// 	],		
				// }]	    	
	   //  });
	    const taxRegNoProps = getFieldProps('taxRegNo', {
            rules: [
                { message: '请填写税务登记号' },
                {validator:ValidateFn.isFigure.bind(this)}
            ],trigger: ['onBlur'],
        });
			const telephoneProps = getFieldProps('telephone', {
            rules: [
                { message: '请选择您的企业固定电话' },
                {validator:ValidateFn.isFixNumber.bind(this)}
            ],trigger: ['onBlur'],
      });		
      	const zoneProps = getFieldProps('zone', {
	    	rules: [{ required: true, type: 'array', message: '请选择您的所在地区' }],
	    });
		const officeZone = getFieldProps('officeZone', {
			rules: [{ required: true, type: 'array', message: '请选择您的所在地区' }],
		});
			const addressProps = getFieldProps('address', {
	    	rules: [{ required: true, message: '请输入您的具体地址' }],
	    });
		const officeAddress = getFieldProps('officeAddress', {
			rules: [{ required: true, message: '请输入您的具体地址' }],
		});
	    const contactsProps = getFieldProps('contacts', {
            rules: [
                {min:2,max:30, message: '请正确填写您的联系人姓名' },
                {validator:ValidateFn.regexChnName.bind(this)}
            ],trigger: ['onBlur'],
        });
      // const legalDelegateProps = getFieldProps('legalDelegate', {
      //       rules: [
      //           { required: true,min:2,max:10, message: '请填写正确的姓名' },
      //           {validator:ValidateFn.regexChnName.bind(this)}
      //       ],trigger: ['onBlur'],
      //   });      
			const naturalPersonProps = getFieldProps('naturalPerson', {
	    	rules: [{ required: true,min:2,max:300, message: '请填写自然人' }],
	    });
	   	const legalPersonProps = getFieldProps('legalPerson', {
	    	rules: [{ required: true,min:2,max:300, message: '请填写企业法人' }],
	    });

	    //判断是否已修改过一次
	   	let userNameHtml = <Input {...userNameProps} type="text" className="input long-input" maxLength="20" autoComplete="off" placeholder="6-20位字符，由字母或字母加数字组成" />;
	   	if(data.renameFlag === "1"){
	   	  	userNameHtml = <p className="txt">{data.userName}</p>;
	   	}			
	    return (
	    	<Form horizontal form={this.props.form} onSubmit={this.handleSubmit}>               
	    		<ul className="form-list form-list-edit">
					<li>
						<label className="label">用户名<em className="star">*</em></label>
						<div className="form-item">
							<FormItem hasFeedback>
								{userNameHtml}
							</FormItem>
						</div>
					</li>
					<li>
						<label className="label">企业名称</label>
						<div className="form-item">
							<FormItem hasFeedback>
								<p className="txt">{data.userCompanyInfo.companyName}</p>
	              			</FormItem>
						</div>
					</li>
					<li>
						<label className="label">企业简称<em className="star">*</em></label>
						<div className="form-item">
							<FormItem hasFeedback>
								<Input {...simpleNameProps} type="text" className="input" maxLength="20" autoComplete="off" placeholder="不超过20个字符"  />
	              			</FormItem>
						</div>						
					</li>
					<li>
						<label className="label">注册资本<em className="star">*</em></label>
						<div className="form-item">
							<FormItem hasFeedback>
								<Input {...registeredCapitalProps} type="text" className="input" maxLength="15" autoComplete="off" placeholder="不超过15个字符"  />
	              			</FormItem>
						</div>						
					</li>
					<li>
                        <label className="label">注册地区<em className="star">*</em></label>
						<div className="form-item">
	                		<FormItem>
					          <Cascader {...zoneProps} options={zone} style={{width:'400px'}} />
					        </FormItem>
					        <div className="line-inputBox">
					        	<FormItem hasFeedback>
									<Input {...addressProps} type="text" className="input long-input" maxLength="50" autoComplete="off" placeholder="具体地址，不超过50个字符"  />					
		              			</FormItem>
					        </div>                      	
                        </div>                        
            		</li>
					<li>
						 <label className="label">企业成立时间<em className="star">*</em></label>
						  <div className="form-item">
							  <FormItem>
										  <DatePicker defaultValue={this.state.dateInitialValue ? this.state.dateInitialValue : data.userCompanyInfo.establishDate ? Publiclib.formatDate(data.userCompanyInfo.establishDate) : null} onChange={this.setDate}/>
										</FormItem>
						  </div>
					</li>
						<li>
						<label className="label">税务登记号</label>
						<div className="form-item">
							<FormItem hasFeedback>
								<Input {...taxRegNoProps} type="text" className="input long-input" maxLength="15" autoComplete="off" placeholder="15位数字组成"  />
	              			</FormItem>
						</div>
					</li>
					<li>
						<label className="label">企业固定电话</label>
						<div className="form-item">
							<FormItem hasFeedback>
								<Input {...telephoneProps} type="text" className="input long-input" maxLength="12" autoComplete="off" placeholder="以0开头11-12位,区号+号码,如01088886666"  />
	              			</FormItem>
						</div>
					</li>
					<li>
						<label className="label">联系人姓名</label>
						<div className="form-item">
							<FormItem hasFeedback>
								<Input {...contactsProps} type="text" className="input long-input" maxLength="30" autoComplete="off" placeholder="2-30位字符"  />
	              			</FormItem>
						</div>
					</li>
					<li>
						<label className="label">办公地点<em className="star">*</em></label>
						<div className="form-item">
							<FormItem>
								<Cascader {...officeZone} options={zone} style={{width:'400px'}} />
							</FormItem>
							<div className="line-inputBox">
								<FormItem hasFeedback>
									<Input {...officeAddress} type="text" className="input long-input" maxLength="50" autoComplete="off" placeholder="具体地址，不超过50个字符"  />
								</FormItem>
							</div>
						</div>
					</li>
          <li>
            <label className="label">法定代表</label>
            <div className="form-item">
              <FormItem hasFeedback>
                {data.userCompanyInfo.legalDelegate ? data.userCompanyInfo.legalDelegate : ""}
              </FormItem>
            </div>
          </li>		
          <li style={{'marginBottom':'0s'}}>
              <label className="label">股东信息<em className="star">*</em></label>
			  </li>
					<li>
              <div className="form-item pt10">
              	<div className="mb10 clearfix">
									<label className="label">自然人</label>
					<div className="form-item">
                	<FormItem hasFeedback className="fl">
										<Input {...naturalPersonProps} type="textarea" className="input long-input" maxLength="40" autoComplete="off" placeholder="填写股东类型为自然人的真实姓名，姓名之间用（，）隔开"  />
          				</FormItem>
          				<span className="tips2">如果有多个自然人，用逗号分隔。</span>
						</div>
              	</div>
								<div className="clearfix">
									<label className="label">企业法人</label>
									<div className="form-item">
									<FormItem hasFeedback className="fl">		              				
										<Input {...legalPersonProps} type="textarea" className="input long-input" maxLength="40" autoComplete="off" placeholder="填写股东类型为企业法人的公司姓名，姓名之间用（，）隔开"  />
          				</FormItem>
          				<span className="tips2">如果有多个企业法人，用逗号分隔。</span>
								</div>
									</div>
							</div>                                          
          </li>				
          <li>                        
            <div className="form-item">
              	<FormItem>
								<Button type="primary" htmlType="submit" className="btn submitBtn" >保存</Button>
								&nbsp;&nbsp;&nbsp;
								<Button type="ghost" className="btn cancelBtn ml20" onClick={this.props.changeCompanyageinHander}>取消</Button>
					</FormItem>
             </div>                        
          </li>
				</ul>
			</Form>
	    );
	}
});
export default Companyageinfoedit = createForm()(Companyageinfoedit);