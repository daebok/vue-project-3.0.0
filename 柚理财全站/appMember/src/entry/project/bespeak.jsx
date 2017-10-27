import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './bespeak.less';
import  { ValidateFn } from '../../common/validateFn';
import  { Reg } from '../../common/regularFun';
import { Button, Form, Input,Modal,Select ,Cascader } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}

let Loanform = React.createClass({
   getInitialState() {
    return {
      imgsrc:'/validimg.html'
    };
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
        const self = this;     
        $.ajax({
          url: '/borrow/bespeakAdd.html',
          type: 'POST',
          dataType: 'json',
          data: values
        })
        .done(function(data) {
          if(data.result){
            success(data.msg,window.location.href);
          }
          else
          {
            error(data.msg);
            $(".codeimg")[0].click();    
          }
        })
        .fail(function() {
          error('网络异常，请重试！');
        });   
    });
  },
  isNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isNumber(value)){        
        callback([new Error('抱歉，请输入正整数')]);        
    }else if(value > borrowMaxAccount || value < borrowMinAccount){        
        callback([new Error('必须大于等于'+ borrowMinAccount +'小于等于'+borrowMaxAccount)]);        
    }else{
      callback();
    }    
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
  },
  render() {
      const address = areajson,TimeList = bespeakTimeList;
      const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
      const contactNameProps = getFieldProps('contactName', {
        validate: [{
        rules:[
          { required: true, message: '请输入姓名' },
          {validator:ValidateFn.regexChnName.bind(this)}
        ],trigger: ['onBlur'],        
      }]        
      });
      const sexProps = getFieldProps('sex', {
            rules: [
                { required: true, message: '请选择您的性别' },
            ],
        });
      const mobileProps = getFieldProps('mobile', {
      validate: [
      {
        rules:[
          { required: true, message: '请填写手机号码' }
        ],
         trigger: ['onBlur'],
      },
      {
        rules:[
          {validator:ValidateFn.isMobile},
        ], trigger: ['onBlur'],
      }
      ]
      });
      const zoneProps = getFieldProps('zone', {
        rules: [{ required: true, type: 'array', message: '请选择您的所在地区' }],
      });
      const moneyProps = getFieldProps('money', {
            validate: [
            {
              rules:[
                { required: true, message: '请输入借款金额' }
              ],trigger: ['onBlur']
            },
            {
              rules:[
                {validator:this.isNumber},
              ],trigger: ['onBlur']
            }
            ] 
          });
        const limitTimeProps = getFieldProps('limitTime', {
            rules: [
                { required: true, message: '请选择借款期限' },
            ],
        });
        const validCodeProps = getFieldProps('validCode', {
          validate: [
            {
              rules: [
                { required: true, whitespace: true, message: '请输入验证码' }
              ],
             trigger: ['onBlur'],
            },
            {
              rules:[
                {validator:ValidateFn.checkValidCode.bind(this)},
              ], trigger: ['onBlur'],
            }
          ]
        });      
        //let bespeakTimeList = [{"itemValue":"1","itemName":"期限1类型"},{"itemValue":"2","itemName":"期限2类型"}];                     
    return (      
      <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="bespeak-title hide">借款预约申请</div>
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">您的姓名</div>
          <div className="col-10">
            <FormItem hasFeedback ><Input {...contactNameProps} placeholder="请输入您的姓名" maxLength="10" style={{"padding":"9px 26px 9px 7px"}} /></FormItem>
          </div>
          <div className="col-8 pl20">
            <FormItem hasFeedback >
                <Select {...sexProps} placeholder="请选择性别" style={{ width: '100%' }}>                                                
                    <Option value="M">先生</Option>
                    <Option value="F">女士</Option>                            
                </Select>              
            </FormItem>
          </div>
        </div>     
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">手机号码</div>
          <div className="col-18">
            <FormItem hasFeedback ><Input {...mobileProps} placeholder="请输入手机号码" maxLength="11" /></FormItem>
          </div>                                                    
        </div>
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">所在城市</div>
          <div className="col-18">
            <FormItem ><Cascader {...zoneProps} options={address} style={{ width: '100%' }} /></FormItem>
          </div>                                                    
        </div>
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">借款金额</div>
          <div className="col-18">
            <FormItem hasFeedback ><Input {...moneyProps} placeholder="请输入借款金额" maxLength="10" /></FormItem>
          </div>                                                    
        </div> 
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">借款期限</div>
          <div className="col-18">
            <FormItem hasFeedback >
                <Select {...limitTimeProps} placeholder="请选择借款期限" style={{ width: '100%' }}>
                   { 
                    TimeList.map(function(k,v){                                   
                      return <Option key={v} value={k.itemValue} title={k.itemName}>{k.itemName}</Option>
                    })
                    }                                           
                </Select>              
            </FormItem>
          </div>                                                    
        </div>
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-6 lab form-laber textL">验证码</div>
          <div className="col-10">
            <FormItem hasFeedback ><Input maxLength="4" {...validCodeProps}  placeholder="验证码" size="large" type="text" autoComplete="off" /></FormItem>
          </div>
          <div className="col-8 pl20">
            <img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" />
          </div>                                                    
        </div>                                           
        <div className="row" style={{"width":320,"margin":"0 0 3px"}}>
          <div className="col-18">
            <FormItem><Button className="subbutton" type="primary" htmlType="submit" >立即预约</Button></FormItem>                            
          </div>
        </div>                               
      </Form>
    );
  },
});

Loanform = createForm()(Loanform);

ReactDOM.render(<Loanform />, document.getElementById("j-bespeakForm"));