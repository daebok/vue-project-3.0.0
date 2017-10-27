import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import './myrealizesetting.less';
import { Button, Form, Input,Modal,Checkbox,InputNumber} from 'antd';
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

let MyrealizeSetting = React.createClass({
  getInitialState() {
    return {
      data: '',
      propmtNumber:"",
      aprnumber:"",
      visible1 : false,
      content :'',
      label:''
    };
  },
  handleSubmit(e) {


    this.props.form.validateFields((errors, values) => {
      e.preventDefault();
      if (!!errors) {        
        console.log('Errors in form!!!');        
        return;
      }

        if(this.refs.check.props.checked!=true){
            this.setState({ label: '请勾选变现协议！' });
            return
        }
        if(!this.state.checked){
            console.log('unchecked');
            return;
        }
      console.log('Submit!!!');
      console.log(values);
      values[this.props.tokenName]=this.props.tokenValue;
      $.ajax({
        url: '/member/myRealize/doSet.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success(data.msg,data.url)       
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
  onChange(e) { 
    this.setState({
      checked:e.target.checked,
       label:''
    });
  },
  compute(){

  },

    handleCancel() {
        this.setState({ visible1: false });
    },
  computeChange(value){
    this.props.form.setFieldsValue({
      apr:value
    });
    this.compute();
  },
  getInfo(){
        let self = this;
        $.ajax({
            url: '/member/myRealize/realizeProtocol.html',
            type: 'POST',
            dataType: 'json'
        })
            .done(function(data) {
                if(data.result){
                    self.setState({
                        visible1: true,
                        content:data.content
                    });
                }
                else
                {
                    error(data.msg);
                }
            })
            .fail(function() {
                error('网络异常，请重试！');
            });
   },
  filter(e){
    Publiclib.setdec(e.target);
  },
  filterInt(e){
    Publiclib.setdec(e.target);
  },
  checkapr(rule, value, callback){
      this.setState({
        propmtNumber : "",
        aprnumber : ""
      });
    if(!value){
      callback();
    } else if(value < this.props.min || value > this.props.max){
      callback([new Error('抱歉，请输入正确的变现利率')]);
    } else {
      let aprnumber = parseInt(this.props.assets/(1+(value*this.props.timeLimit)/(this.props.date*100)));     
      this.setState({
        propmtNumber : "最高可变现金额"+ aprnumber + "元",
        aprnumber : aprnumber
      });
      if(this.props.all == 1){
        this.props.form.setFieldsValue({
          account:aprnumber.toString()
        });
        $("#account").prev().hide()
      }
      callback();
    }
  },
  checkNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(this.state.aprnumber == "" ){
        callback([new Error('抱歉，请先输入变现利率')]);
    }else if(value > Number(this.state.aprnumber)){
        callback([new Error('抱歉，变现金额不能大于最高可变现金额')]);
    }else{
        callback();
    }
  },    
  render(){

            const { getFieldProps, getFieldError, isFieldValidating,setFieldsValue} = this.props.form;
            const aprProps = getFieldProps('apr', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入变现利率' }
                    ],
                     trigger: ['onBlur'],
                  },
                  {
                    rules:[
                      {validator:this.checkapr},
                    ], trigger: ['onBlur'],
                  }
                  ]                  
                });
            const accountProps = getFieldProps('account', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入变现金额' }
                    ],
                     trigger: ['onBlur'],
                  },
                  {
                    rules:[
                      {validator:this.checkNumber},
                    ], trigger: ['onBlur'],
                  }
                  ] 
                });
            const agreeProps = getFieldProps("agree",{
                initialValue:"1"
            });

            const investIdProps = getFieldProps("investId",{
                initialValue:this.props.uuid
            });           

            const interest = '利率范围' + this.props.min + '%~' + this.props.max + '%';


            return (
                    <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                        <div className="row mb24" style={{width:800}}>
                          <div className="col-3 lab form-laber textR">所持资产</div>
                          <div className="col-18 pl20 form-text lab">
                          <span className="form-text"><span className="form-textsm">{this.props.assets}</span>元<span className="form-supplement">(含预期收益的未加息部分利息{this.props.waitInterest}元)</span></span>
                          </div>
                        </div>                                                                                      
                        <div className="row" style={{width:800}}>
                          <div className="col-3 lab form-laber textR">变现利率</div>

                          <div className="col-9 pl20 hasunit hasunit-number">
                            <FormItem hasFeedback  extra={interest}><InputNumber placeholder="请输入变现利率" {...aprProps} size="large"  step={0.01} min={this.props.min} max={this.props.max} maxLength="7" onChange={this.computeChange} onInput={this.filter} /></FormItem>
                            <span className="unit">%</span>
                          </div>

                        </div>
                        <div className="row" style={{width:800}}>
                          <div className="col-3 lab form-laber textR">变现金额</div>
                          <div className="col-9 pl20 hasunit">
                          <FormItem hasFeedback extra={this.state.propmtNumber}>
                            <Input {...accountProps}  placeholder="请输入变现金额" maxLength="10" onInput = {this.filterInt} readOnly={this.props.all == 1 ? true : false} /></FormItem>
                            <span className="unit">元</span>
                            </div>                                                    
                        </div>
                        <div className="row" style={{width:800}}>
                          <div className="col-3 lab form-laber textR">变现期限</div>
                          <div className="col-9 pl20 lab">
                            <span className="deadline">{this.props.timeLimit}</span>天
                          </div>                                                    
                        </div>                        
                        <div className="row" style={{width:800}}>
                          <div className="col-3">&nbsp;</div>
                          <div className="col-18 pl20">
                            <FormItem validateStatus="error" {...agreeProps} help = {this.state.label}><Checkbox ref="check" checked={this.state.checked} onChange={this.onChange} ></Checkbox>我已阅读并同意<a href="javascript:;" onClick={this.getInfo}>《变现协议》</a></FormItem>
                              <Modal
                                  visible={this.state.visible1}
                                  title="变现协议" onCancel={this.handleCancel}
                                  width={780}
                                  footer={[
                                      <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>关闭</Button>
                                  ]}
                              >
                                  <div style={{maxHeight:"400px",overflow:"auto"}} dangerouslySetInnerHTML={{__html: this.state.content}}></div>
                              </Modal>
                          </div>
                        </div>                                                                     
                        <div className="row" style={{width:800}}>
                          <div className="col-3">&nbsp;</div>
                          <div className="col-18 pl20">
                            <FormItem><Button className="subbutton" type="primary" htmlType="submit" >确定</Button></FormItem>                            
                          </div>
                        </div>                               
                    </Form>
              )
          }  
});
MyrealizeSetting = createForm()(MyrealizeSetting);

ReactDOM.render(<MyrealizeSetting
 date =  {Number($("#j-date").val())}
 timeLimit = {Number($("#j-timeLimit").val())}
 uuid = {$("#j-uuid").val() }
 tokenName={"commitRealizeToken"}
 tokenValue={$("input[name='commitRealizeToken']").val()}
 waitInterest = {$("#j-waitInterest").val()}
 max = {Number($("#j-realizeRateMax").val())}
 min = {Number($("#j-realizeRateMin").val())}
 all = {Number($("#j-all").val())}
 assets={Number($("#j-assets").val())} />,  document.getElementById("j-myrealize-setting"));

ReactDOM.render(<Accountmenu current = {"5"}/>,  document.getElementById("j-sider-menu"));