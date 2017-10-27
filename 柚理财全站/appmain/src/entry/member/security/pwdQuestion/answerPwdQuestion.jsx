import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './answerPwdQuestion.less';
import { Form, Button, Select, Input} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

let AnswerPwdQuestion = React.createClass({
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
          window.location.href = "/member/security/pwdQuestion.html";
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
  callback(){
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    e.target.disabled = true;
    ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {this.callback}/>, document.getElementById("countdown"));    
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
      //console.log("error");
    })
  },    
  render(){
            let data = this.state.data;
            if(!data){
              return false;
            }
            let questionList = this.state.data.questionList; 
             
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;            
            const answerOneProps = getFieldProps('answerOne', {
              rules: [
                  { required: true, message: '请输入答案' },
              ],
            });            
            const answerTwoProps = getFieldProps('answerTwo', {
              rules: [
                  { required: true, message: '请输入答案' },
              ],
            });            
            const answerThreeProps = getFieldProps('answerThree', {
              rules: [
                  { required: true, message: '请输入答案' },
              ],
            });            
            return (
                <div className="account-main">                  
                  <div className="main-title">
                    修改密保问题
                    <a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
                  </div>
                  <div className="answerPwdQuestion">
                      <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                          <div className="row">
                            <div className="col-24"><p className="txt">填写原密保问题答案<a href="/member/security/resetPwdQuestion.html">忘记密保问题</a></p></div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题一</div>
                            <div className="col-18 pl20">
                              <p className="text">您大学辅导员的名字是？</p>
                            </div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                            <div className="col-18 pl20">
                              <FormItem hasFeedback>
                                <Input {...answerOneProps} type="text" autoComplete="off" maxLength="60" />
                              </FormItem>
                            </div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题二</div>
                            <div className="col-18 pl20">
                              <p className="text">您小学班主任的名字是？</p>
                            </div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                            <div className="col-18 pl20">
                              <FormItem hasFeedback>
                                <Input {...answerTwoProps} type="text" autoComplete="off" maxLength="60" />
                              </FormItem>
                            </div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 问题三</div>
                            <div className="col-18 pl20">
                              <FormItem>
                                <p className="text">您父亲的名字是？</p>
                              </FormItem>
                            </div>
                          </div>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR"><span className="icon-star">*</span> 答案</div>
                            <div className="col-18 pl20">
                              <FormItem hasFeedback>
                                <Input {...answerThreeProps} type="text" autoComplete="off" maxLength="60" />
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
                </div>
              )
          }  
});
AnswerPwdQuestion = createForm()(AnswerPwdQuestion);

ReactDOM.render(<AnswerPwdQuestion />,  document.getElementById("j-answerPwdQuestionmain"));

ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));