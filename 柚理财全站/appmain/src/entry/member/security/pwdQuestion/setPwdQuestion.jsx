import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import Success from '../../../../component/success/success';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './setPwdQuestion.less';
import { Form, Button, Select, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const Option = Select.Option;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '登录密码',
    content: v,
  });
}

let SetPwdQuestion = React.createClass({
  getInitialState(){
    return {
      data: "",
    }
  },
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      let self = this;
      $.ajax({
        url: '/member/security/doSetPwdQuestion.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          $(".setPwdQuestion").hide();        
          ReactDOM.render(<Success callback={self.settimefun} link={"/member/baseInfo/index.html"} linkname={"安全设置"} time={3} text={"密保设置成功"} />, document.getElementById("j-step-third")); 
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

  },
  settimefun(){
    window.location.href = "/member/baseInfo/index.html";
  }, 
  callback(){
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  componentDidMount() {
    let self = this;
    $.ajax({
      url: '/member/security/getPwdQuestion.html',
      type: 'POST',
      dataType: 'json',
    })
    .done(function(data) {
      if(data.result){
        self.setState({data, data});
      } else {
        console.log("error");
      }
    })
    .fail(function() {
      console.log("error");
    })
  },
  render(){
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const question1Props = getFieldProps('question1', {
      rules: [
        { required: true, message: '请选择问题一' },
      ],
    });
    const question2Props = getFieldProps('question2', {
      rules: [
        { required: true, message: '请选择问题二' },
      ],
    });
    const question3Props = getFieldProps('question3', {
      rules: [
        { required: true, message: '请选择问题三' },
      ],
    });

    const answer1Props = getFieldProps('answer1', {
      rules: [
          { required: true, message: '请输入答案1' },
      ],
    });
    const answer2Props = getFieldProps('answer2', {
      rules: [
          { required: true, message: '请输入答案2' },
      ],
    });
    const answer3Props = getFieldProps('answer3', {
      rules: [
          { required: true, message: '请输入答案3' },
      ],
    });
    let data = this.state.data;
    if(!data){
      return false;
    }
    let questionList = this.state.data.questionList;

    /*let lang = ["php","java","javascript"];
    let arr2 = lang.splice(1,1);
    console.log(lang);*/


    return (
      <div className="account-main">
      	<div className="main-title">
      		设置密保问题
      		<a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
      	</div>
      	<div className="setPwdQuestion">
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row">
                  <div className="col-24"><p className="txt"><i className="iconfont" style={{'color':'#66bef5','fontSize':'16px','marginRight':'10px'}}>&#xe639;</i>密保问题是您帐号安全的基础保护工具。为了避免遗忘，建议您设置问题答案时填写真实信息。 </p></div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题一</div>
                  <div className="col-18 pl20">
                    <FormItem>
                      <Select {...question1Props} placeholder="请选择问题一" style={{ width: '100%' }}>
                        {questionList.map(function(item, i){
                          let val = (i+1) + "";
                          return <Option key={i} value={item.itemValue}>{item.itemName}</Option>
                        })}
                      </Select>
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                  <div className="col-18 pl20">
                	  <FormItem hasFeedback>
                      <Input {...answer1Props} type="text" maxLength="60" autoComplete="off" />
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题二</div>
                  <div className="col-18 pl20">
                    <FormItem>
                      <Select {...question2Props} placeholder="请选择" style={{ width: '100%' }}>
                        {questionList.map(function(item, i){
                          let val = (i+1) + "";
                          return <Option key={i} value={item.itemValue}>{item.itemName}</Option>
                        })}
                      </Select>
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                  <div className="col-18 pl20">
                    <FormItem hasFeedback>
                      <Input {...answer2Props} type="text" maxLength="60" autoComplete="off" />
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题三</div>
                  <div className="col-18 pl20">
                    <FormItem>
                      <Select {...question3Props} placeholder="请选择" style={{ width: '100%' }}>
                        {questionList.map(function(item, i){
                          let val = (i+1) + "";
                          return <Option key={i} value={item.itemValue}>{item.itemName}</Option>
                        })}
                      </Select>
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                  <div className="col-18 pl20">
                    <FormItem hasFeedback>
                      <Input {...answer3Props} type="text" maxLength="60" autoComplete="off" />
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6">&nbsp;</div>
                  <div className="col-18 pl20">
                    <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
                  </div>
                </div>
            </Form>
      	</div>
        <div id="j-step-third" style={{"padding":"110px 0 0 0"}}></div>
      </div>
    )
  }
});
SetPwdQuestion = createForm()(SetPwdQuestion);
ReactDOM.render(<SetPwdQuestion />, document.getElementById("j-setPwdQuestionmain"));
ReactDOM.render(<Accountmenu current = {"1"}/>, document.getElementById("j-sider-menu"));
