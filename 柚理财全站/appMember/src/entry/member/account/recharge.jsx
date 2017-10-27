import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import CountDown from '../../../component/countDown/CountDownComponent';
import  { ValidateFn} from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import './recharge.less';
import { Button, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function showerror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请开通第三方资金托管账户',
    okText:'开通托管账户',
    iconType:'question-circle',
    onOk() {
        window.location.href = "/member/security/realNameIdentify.html";
    },
    onCancel() {},
  });
}

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success() {
  Modal.success({
    title: '充值成功',
    content: '',
    okText:"确定",
    onOk() {
      window.location.href = "/member/recharge/index.html";
    }   
  });
}

if($("#j-realNameStatus").val()!=1){
   showerror()
}

let Recharge = React.createClass({
  getInitialState() {
    return {
      visible:false,
      data: '',
      bankCode:'',
      bankNo:'',
      banklist:"",
      banklistLen:"",
      timeflag:false
    };
  },
  handleSubmit(e) {
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      
      if (!!errors) {
        console.log('Errors in form!!!');
        //e.preventDefault();
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      //this.setState({ visible: true });
      $.ajax({
        url: '/member/recharge/doRecharge.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success();
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
    });

  },
  getcardlist(){
    const that = this;
    $.ajax({
      url: '/member/bankCard/getBankCardList.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      if( data.result ){
        window.setTimeout(function(){
          $("#cashnumber").on("input",function(e){
            that.changeBalance(e);
          });
        },1000);
        let banklist = '',bankNo='',bankCode='',banklistLen= data.bankList.length;
        if(data.bankList != '' || data.bankList.length > 0){
            let that = this,
            candel = false;
            banklist = data.bankList.map(function(k,v){
              let select = "";
              if((k.canDisable === false && !candel) || v==0)
              {
                candel = true;
                select = "on";
                bankNo = k.bankNo;
                bankCode = k.bankCode;
              }
             return <li key={v} data-bankno={k.bankNo} data-bankcode={k.bankCode} className={select}><s><em className="iconfont">&#xe612;</em></s><span className="bankname">{k.bankNo}</span><span className="cardnumber">银行卡号</span></li>
              })
        }
        else
        {
            //banklist = <li>暂无银行卡</li>;
            banklist = "";
        }
        that.setState({
          data : data,
          banklist : banklist,
          bankNo:bankNo,
          bankCode:bankCode,
          banklistLen:banklistLen
        });
        that.selectcard(that);
      }
      else
      {
        error(data.msg,data.url);
      }

    })
    .fail(function(){
      error('网络异常，请重试！');
    });
  },
  add(e){
    var that = e.target,money = that.value*1;
    Publiclib.rechargeCheck(that);
    let number = (this.props.money*1 + e.target.value*1).toFixed(2);
    $("#j-count").html("充值后可用余额"+number+" 元");
    // if( singlemin >= 0 ){
    //     this.singlemin = true;
    // }
    // else
    // {
    //     this.singlemin=false;
    // }
  },
   isNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(value < this.props.minamount){
        callback([new Error('充值金额单笔不能小于'+this.props.minamount+'元')]);
    }else if(value <= 0){
        callback([new Error('充值金额单笔不能小于等于0元')]);
    }else if(value > this.props.maxamount){
        callback([new Error('充值金额单笔不能大于'+this.props.maxamount+'元')]);
    }else{
      callback();
    }
  },
  mobile(rule, value, callback) {
    const { getFieldValue } = this.props.form;
    if (!value) {
        callback();
    }else if(Reg.isMobile(value))
    {        
        callback([new Error('邀请人手机号码格式不正确')]);        
    }
      callback();       
  },
  handleOk() {
    window.location.href = "/member/recharge/index.html";
  },
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },
  componentDidMount() {

      let self = this;
      self.getcardlist();
      self.setdefault();
      $("#banknumber").on("input",function(e){
        self.add(e);
      });
  },
  selectcard(that){
    $(".cardlist li").click(function() {
      if(!$(this).hasClass('addcard')){
        $(".cardlist li").removeClass('on');
        $(this).addClass('on');
        that.setState({
          bankNo: $(this).data("bankno"),
          bankCode: $(this).data("bankcode"),
        });
      }
    });
  },
  getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target;
    let mobile = self.refs.mobileRef.props.value;
    $.ajax({
      url: '/member/security/smsCodeApply.html',
      type: 'POST',
      dataType: 'json',
      data: {mobile:mobile,srvTxCode:this.props.srvTxCode,reqType:this.props.reqType,cardId:this.state.bankNo},
    })
    .done(function(data){
        if(data.result == false)
        {
          error(data.msg);
          self.callback();

        }
        else
        {
          self.setState({
            timeflag : true
          });
          el.disabled = "disabled";
          ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown_phone"));         
        }      
    })
    .fail(function() {
                self.callback();

      error('网络异常，请重试！');
    });    
  },
  callback(){
    this.setState({
      timeflag : false
    }); 
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  setdefault(){
    this.props.form.setFieldsValue({
      mobile: this.props.mobile,
    });
  },
  render(){
            if(this.state.data == ''){
                      return false;
            }
            let addbank = this.state.banklistLen>0? "" : <li className="addcard"><a href="/member/bankCard/list.html"><em className="iconfont">&#xe645;</em>添加银行卡</a></li>;
            let tppUserCustId =$("#tppUserCustId").val();
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const banknumberProps = getFieldProps('amount', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入充值金额' }
                    ],trigger: ['onBlur']
                  },
                  {
                    rules:[
                      {validator:this.isNumber},
                    ],trigger: ['onBlur']
                  }
                  ]
                });
            const mobileProps = getFieldProps('mobile', {
              validate: [
              {
                rules:[
                  { required: true, message: '请输入手机号码' }
                ],trigger: ['onBlur']
              },
              {
                rules:[
                  {validator:this.mobile},
                ], trigger: ['onBlur'],
              }
              ]
            });
            const codeProps = getFieldProps('smsCode', {
              rules: [
                { required: true, whitespace: true, message: '请输入验证码' }
                ]
            });
            const bankNoProps = getFieldProps("cardId",{
                initialValue:this.state.bankNo
            });
            const bankCodeProps = getFieldProps("bankCode",{
                initialValue:this.state.bankCode
            });
            const reqTypeProps = getFieldProps("reqType",{
                initialValue:this.props.reqType
            });
            const srvTxCodeProps = getFieldProps("srvTxCode",{
                initialValue: this.props.srvTxCode
            });
            let showMobile = this.props.showMobile == 1? true : false;
            return (
                <div className="account-main">
                <div className="main-title">充值</div>
                  <div className="selectcard">
                    <div className="selectcard-title">选择银行卡</div>
                    <ul className="cardlist">
                      {this.state.banklist}
                      {addbank}
                    </ul>
                  </div>
                  <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                      <div className="row" style={{width:400,marginBottom:"20px"}}>
                        <div className="col-6 lab form-laber textR">账户余额</div>
                        <div className="col-18 pl20 form-text lab">
                          <em>{this.props.money}</em> 元
                        </div>
                      </div>
                      <div className="row" style={{width:400,marginBottom:"20px"}}>
                        <div className="col-6 lab form-laber textR">手机号码</div>
                        <div className="col-18 pl20">
                          <FormItem hasFeedback><Input {...mobileProps} id="mobile" ref="mobileRef" placeholder="请输入您的手机号码" readOnly={showMobile} maxLength="11" /></FormItem>
                        </div>
                      </div>
                      <div className="row" style={{width:400,marginBottom:"20px"}}>
                        <div className="col-6 lab form-laber textR">验证码</div>
                        <div className="col-9 pl20">
                          <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
                          </FormItem>
                        </div>
                        <div className="col-9 pl20">
                        <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_phone">点击获取验证码</button>
                        </div>
                      </div>
                      <div className="row" style={{width:400}}>
                        <div className="col-6 lab form-laber textR">充值金额</div>
                        <div className="col-18 pl20 hasunit">
                          <FormItem><Input {...banknumberProps} className="input" style={{fontSize:"18px",color:"#333",lineHeight:"normal"}} name="amount" placeholder="请输入充值金额" maxLength="12"  /></FormItem>
                          <span className="unit">元</span>
                          <span style={{fontSize:"14px",color:"#999",padding:"0 0 5px 0",display:"block"}} id="j-count">{"充值后可用余额"+this.props.money+" 元"}</span>
                        </div>
                      </div>
                      <Input {...bankNoProps} name="cardId"  type="hidden" />
                      <Input {...bankCodeProps} name="bankCode"  type="hidden" />
                      <Input {...reqTypeProps} name="reqType"  type="hidden" />
                      <Input {...srvTxCodeProps} name="srvTxCode"  type="hidden" />
                      <div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20">
                          <FormItem><Button className="subbutton" type="primary" htmlType="submit" >确认充值</Button></FormItem>
                        </div>
                      </div>
                      <div className="row" style={{width:400}}>
                        <div className="col-6">&nbsp;</div>
                        <div className="col-18 pl20 remind">
                          <span className="star">*</span>充值操作将在资金托管账户网站进行
                        </div>
                      </div>
                  </Form>
                  <div className="help">
                    <div className="help-title"><em className="iconfont">&#xe639;</em>温馨提示</div>
                    <ol>
                      <li>线下充值账号为：{tppUserCustId}。</li>
                      <li>充值/提现必须为开通网上银行的借记卡，不支持存折、信用卡充值。</li>
                      <li>充值期间，请勿关闭浏览器，待充值成功并返回后，可在网站中查看充值金额。</li>
                      <li>严禁信用卡套现、虚假交易等行为。</li>
                    </ol>
                  </div>

                  <Modal  className="orderPayModal" ref="modal"
                    visible={this.state.visible}
                    title="登录资金账户完成充值" onOk={this.handleOk} onCancel={this.handleCancel}
                    width={480}
                    footer={[
                      <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                        充值成功
                      </Button>,
                      <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>充值失败</Button>,
                    ]}
                  >
                  <div className="modal-content clearfix"><em className="iconfont fl">&#xe62e;</em><p className="fl">请在新打开的资金托管账户完成充值</p></div>
                  </Modal>
                </div>
              )
          }
});
Recharge = createForm()(Recharge);

ReactDOM.render(<Recharge reqType={$("#reqType").val()} srvTxCode={$("#srvTxCode").val()} mobile={$("#mobile").val()} showMobile={$("#showMobile").val()} money={$("#money").val()} maxamount={Number($("#j-rechargeMaxAmount").val())} minamount={Number($("#j-rechargeMinAmount").val())}/>,  document.getElementById("j-rechargemain"));

ReactDOM.render(<Accountmenu current = {"12"}/>,  document.getElementById("j-sider-menu"));
