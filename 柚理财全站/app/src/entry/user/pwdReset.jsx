import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './pwdReset.less';
import { ValidateFn } from '../../common/validateFn';
import Success from '../../component/success/success';
import { Button,  Form, Input, Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

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

let ModifyPwd = React.createClass({
  getInitialState(){
    return {
      data:""
    }
  },
  handleSubmit(e) {
    e.preventDefault();  
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');  
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      let self = this;
      $.ajax({
        url: '/user/retrievepwd/validation.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          
        }
        else
        {
          error(data.msg);
        }  
      })
      .fail(function() {
        //error('网络异常，请重试！');
      });
    });

  },
  render(){
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    return (
      <div className="account-main" style={{padding:"60px 0 0 355px"}}>                  
        <div className="modifyEmail">
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>                
                <div id="firstStep">

                </div>
                <div id="secondtStep" style={{display:"none"}}></div>
                                                              
                <div className="row" style={{width:400}}>
                  <div className="col-6">&nbsp;</div>
                  <div className="col-18 pl20">
                    <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
                  </div>
                </div>                               
            </Form>
        </div>
      </div>
    )
  }  
});

ModifyPwd = createForm()(ModifyPwd);

ReactDOM.render(<ModifyPwd />,  document.getElementById("j-pwd"));