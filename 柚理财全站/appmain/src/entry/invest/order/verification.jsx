import ReactDOM from 'react-dom';
import React from 'react';
import {  Button, Form, Input, Modal  } from 'antd';
import CountDown from '../../../component/countDown/CountDownComponent';
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
    title: '提醒',
    content: v,
  });
}

export default class Verification extends React.Component{
	constructor(props) {
		super(props);
		this.state = {
			skipUrl: this.props.url,
		}
		this.handleSubmit = this.handleSubmit.bind(this);
		this.callback = this.callback.bind(this);
		this.getsms = this.getsms.bind(this);
		this.NoErrorShow = this.NoErrorShow.bind(this);
		this.setErrorText = this.setErrorText.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}
	handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }

      $.ajax({
        url: '/invest/cbhbDoInvest.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          window.location.href = "/member/myInvest/list.html?tab=2";
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
  reSetForm(){
    this.props.form.resetFields();
  }
  callback(){
    this.setState({
      timeflag : false
    });
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  }
  getsms(e){
      if(this.state.timeflag){
        return false;
      }
      let self = this,el = e.target;
      self.setState({
        timeflag : true
      });
      $.ajax({
        url: '/invest/getCode.html',
        type: 'POST',
        dataType: 'json'
      })
      .done(function(data){
          if(data.result == false)
          {
            self.setErrorText(data.msg);
            self.callback();
          }
          else
          {
            self.NoErrorShow();
            el.disabled = true;
            ReactDOM.render(<CountDown  time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));
          }
      })
      .fail(function() {
        self.callback();
        error('网络异常，请重试！');
      });
  }
  NoErrorShow(){
    $("#secondStepErrorMsg").css({"display": "none"});
  }
  setErrorText(msg){
     $("#secondStepErrorMsg").html(msg).css({"display": "block"});
  }
    render(){

      const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
      const codeProps = getFieldProps('smsCode', {
        validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
            ]
        }]
      });
      const projectIdProps = getFieldProps("projectId",{
          initialValue:$('#projectId').val()
      });
      const amountProps = getFieldProps("amount",{
          initialValue:$('input[name="amount"]').val()
      });
      const redEnvelopeIdsProps = getFieldProps("redEnvelopeIds",{
          initialValue:$('input[name="redEnvelopeIds"]').val()
      });
      const rateCouponIdProps = getFieldProps("rateCouponId",{
          initialValue:$('input[name="rateCouponId"]').val()
      });
      const realAmountProps = getFieldProps("realAmount",{
          initialValue:$('input[name="realAmount"]').val()
      });
      const investToken = getFieldProps("investToken",{
          initialValue:$('input[name="investToken"]').val()
      });

    	return (
        <Form horizontal className="form-themes form-detail" form={this.props.form} onSubmit={this.handleSubmit}>
          <div className="row" style={{marginTop:20}}>
            <div className="col-6 lab form-laber textR">
							验证码：
            </div>
            <div className="col-9 pl20">
              <FormItem hasFeedback>
                <Input type="text" {...codeProps} autoComplete="off" maxLength="6" placeholder='请输入验证码' />
              </FormItem>
            </div>
						<div className="col-5 pl20">
                <button type="button" className="countdown-btn" onClick={this.getsms}  ref="countdownbtn" id="countdown">点击获取验证码</button>
            </div>
          </div>
					<div className="row secondStep-btn">
						 <div className="error-msg col-18 pl20" style={{marginLeft:135}} id="secondStepErrorMsg"></div>
					</div>

          <Input {...projectIdProps}  type="hidden" />
          <Input {...amountProps} type="hidden" />
          <Input {...redEnvelopeIdsProps} type="hidden" />
          <Input {...rateCouponIdProps} type="hidden" />
          <Input {...realAmountProps} type="hidden" />
          <Input {...investToken} type="hidden" />

					<div className="btn-instant">
						<FormItem style={{ marginLeft:135 }}>
							<Button className="subBtn" type="primary" htmlType="submit" >确认提交</Button>
						</FormItem>
					</div>
        </Form>
    	);
    }
}
