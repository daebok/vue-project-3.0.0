import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import Nobankcard from '../../../component/nobankcard/nobankcard';
import CountDown from '../../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Modal,Form,Button,Input,} from 'antd';
import './creditAdd.less';
const createForm = Form.create;
const FormItem = Form.Item;

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

function success(v,url) {
  Modal.success({
    title: '提醒',
    content: v,
    onOk() {
      if(url){
        //console.log(url);
        window.location.href = url;
      }
    }
  });
}


function showerror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请开通第三方资金托管账户',
    okText:'开通托管账户',
    iconType:'question-circle',
    align:{top:'200px'},
    onOk() {
        window.location.href = "/member/security/realNameIdentify.html";
    },
    onCancel() {},
  });
}

if($("#j-realNameStatus").val()!=1){
    showerror()
}

let ApplyCredit =React.createClass({
  getInitialState() {
      return {
      };
  },
  handleSubmit(e) {
        this.props.form.validateFields((errors, values) => {
          e.preventDefault();
          if (!!errors) {
            console.log('Errors in form!!!');
            e.preventDefault();        
            return;
          }
          console.log('Submit!!!');
          console.log(values);
          $.ajax({
            url: '/member/security/applyCredit.html',
            type: 'POST',
            dataType: 'json',
            data: values
          })
          .done(function(data) {
            if(data.result){
                success('申请额度成功','/member/credit/index.html')
            }
            else
            {
              error(data.msg);
              //$(".codeimg")[0].click();
            }
          })
          .fail(function() {
            error('网络异常，请重试！');
          });
          /*this.setState({
            visible:true
          })*/
        });

    },
  render(){
    const {getFieldProps, getFieldError, isFieldValidating} = this.props.form;
    const accountProps = getFieldProps('account', {
        validate: [{
            rules: [
                {required: true, message: '请输入申请额度'},
                {validator: ValidateFn.isNumber},
            ], trigger: 'onBlur'
        }]
    });
    const contentProps = getFieldProps('content', {
        validate: [{
            rules: [
                {required: true, message: '请输入申请说明'},
            ], trigger: 'onBlur'
        }]
    });
    return (
      <div className="account-main">
        <div className="main-title">申请额度</div>
        <div style={{padding:"20px 0 0 20px"}}>
        <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>           
            <div className="row" style={{width:800 , clear:"both"}}>
              <div className="col-3 lab form-laber textR">申请额度</div>
              <div className="col-9 pl20 hasunit">
                <FormItem hasFeedback>
                <Input {...accountProps}  name="account" placeholder="请输入申请额度" maxLength="20"/>
                </FormItem>
              </div>
            </div>
            <div className="row" style={{width:800 , clear:"both",height:124}}>
              <div className="col-3 lab form-laber textR">申请说明</div>
              <div className="col-9 pl20 hasunit">
                <FormItem hasFeedback>
                  <Input {...contentProps} style={{height:100}} type="textarea" name="content" placeholder="请输入申请说明"/>
                </FormItem>
              </div>
            </div>
            <div className="row" style={{width: 400}}>
                <FormItem><Button style={{marginLeft:120}} className="subBtn" type="primary" htmlType="submit">提交申请</Button></FormItem>
            </div>
        </Form>
        </div>
        </div>
      )
  }
})
ApplyCredit = createForm()(ApplyCredit);

ReactDOM.render(<ApplyCredit/>, document.getElementById("j-listtab"));
ReactDOM.render(<Accountmenu current = {"16"}/>,  document.getElementById("j-sider-menu"));
