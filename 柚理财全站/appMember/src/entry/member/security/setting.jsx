import '../../../common/lib';
// import {envurlinfo} from '../../js/env.js';
import ReactDOM from 'react-dom';
import React from 'react';
import './setting.less';
import  { ValidateFn } from '../../../common/validateFn';
import { Button, Form, Input, Checkbox, Modal } from 'antd';
import BackPageCountDown from '../../../component/countDown/BackPageCountDown';
const createForm = Form.create;
const FormItem = Form.Item;

function noop() {
  return false;
}
function error() {
  Modal.error({
    title: '这是一条通知信息',
    content: '一些附加信息一些附加信息一些附加信息',
  });
}
//开通资金账户
let AccountForm = React.createClass({ 
  getInitialState() {
    return {
      checked: true,
      disabled: false,
    };
  },
  handleSubmit(e) {
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        e.preventDefault();        
        return;
      }
      console.log('Submit!!!');
      console.log(values);

    });
  },
  onChange(e) {
    this.setState({
      checked: e.target.checked,
    });
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const nameProps = getFieldProps('realName', {
      rules: [
        { required: true, min: 2, message: '用户名至少为 2 个字符' },
        //max: 6, message: '用户名最多为 10个字符' },
        { validator: ValidateFn.regexChnName },
      ],
    });
    const idCardProps = getFieldProps('idNo', {
      validate: [{
        rules: [
          { required: true, message: '请填写您的身份证号码' },
        ],
        trigger: 'onBlur',
      }, {
        rules: [
          { validator: ValidateFn.regexIdCard },
        ],
        trigger: ['onBlur'],
      }],
    });
    const returnUrlProps = getFieldProps("returnUrl",{
        initialValue:"member/security/userGuide.html?type=second"
    });   
    return (
      <Form horizontal className="form-themes" action="/member/security/tppRegister.html" method="post" form={this.props.form} onSubmit={this.handleSubmit}>      
        <div className="row formRow" style={{marginLeft:350}}>
          <div className="col-6 lab textR">真实姓名</div>
          <div className="col-18 pl20">
            <FormItem                         
              hasFeedback
              help={isFieldValidating('realName') ? '校验中...' : (getFieldError('realName') || []).join(', ')}
            >
              <Input {...nameProps} maxLength="10" placeholder="请填写您的真实姓名" />
            </FormItem>           
          </div>
        </div>
        <div className="row formRow" style={{marginLeft:350}}>
          <div className="col-6 lab textR">身份证号码</div>
          <div className="col-18 pl20">
            <FormItem                     
              hasFeedback
            >
              <Input {...idCardProps} maxLength='18' placeholder="请填写您的身份证号码" />
            </FormItem>          
            <Input {...returnUrlProps}  type="hidden" />          
            <FormItem>
              <Button type="primary" htmlType="submit">立即开通</Button>          
            </FormItem>
          </div>
        </div>
      </Form>
    );
  },
});

AccountForm = createForm()(AccountForm);
ReactDOM.render(<AccountForm />, document.getElementById("accountForm"));

let BankForm = React.createClass({ 
  render() {
    return (
        <div>
        <div className="row formRow">
          <div className="col-6 lab textR">真实姓名</div>
          <div className="col-18 form-text-lh">
            <FormItem>
            <p className="ant-form-text" id="static" name="static">张三丰</p>  
          </FormItem>
          </div>
        </div>
        <div className="row formRow">         
          <div className="col-18 pl20">
            <FormItem wrapperCol={{ span: 12, offset: 7 }}>
              <Button type="primary" >确认添加</Button>          
            </FormItem>
          </div>
        </div>
        </div>        
    );
  },  
});

//ReactDOM.render(<BankForm />, document.getElementById('bankForm'));

//倒计时
//ReactDOM.render(<BackPageCountDown />, document.getElementById('seccendTime'));




















