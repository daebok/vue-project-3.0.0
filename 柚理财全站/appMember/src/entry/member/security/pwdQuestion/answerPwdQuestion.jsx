import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './answerPwdQuestion.less';
import { Form, Button, Select, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

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
      values["question1"] = this.state.data.questionList[0].id;
      values["question2"] = this.state.data.questionList[1].id;
      values["question3"] = this.state.data.questionList[2].id;
      $.ajax({
        url: '/member/security/doAnswerPwdQuestion.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          window.location.href = "/member/security/setPwdQuestion.html";
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
  componentWillMount() {
    let self = this;
    $.ajax({
      url: '/member/security/getUserPwdQuestion.html',
      type: 'POST',
      dataType: 'json',
    })
    .done(function(data) {
      if(data.result){
        self.setState({data:data});
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
            const answerOneProps = getFieldProps('answer1', {
              rules: [
                  { required: true, message: '请输入答案' },
              ],
            });            
            const answerTwoProps = getFieldProps('answer2', {
              rules: [
                  { required: true, message: '请输入答案' },
              ],
            });            
            const answerThreeProps = getFieldProps('answer3', {
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
                              <p className="text">{data.questionList[0].itemName}</p>
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
                              <p className="text">{data.questionList[1].itemName}</p>
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
                                <p className="text">{data.questionList[2].itemName}</p>
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

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
}