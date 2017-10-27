import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import './cashVouchDetail.less';
import { Button, Form, Input,Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(a) {
  Modal.error({
    title: a,
    okText:"关闭",
    wrapClassName:'tiperror',    
  });
}

function showerror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请开通第三方资金托管账户',
    okText:'开通托管账户',
    iconType:'question-circle',
    onOk() {
      return new Promise((resolve) => {
        window.location.href = "/member/security/realNameIdentify.html";
      });
    },
    onCancel() {},
  });
}

if($("#j-realNameStatus").val()!=1){
  showerror()
}

let Recharge = React.createClass({
  getInitialState() {
    return {
      data: '',
      bankCode:'',
      bankNo:'',
      surplus:'',
      cashtime:this.props.cashtime,
      disabled:false,
      warning:'',
      singlemax:true,
      visible:false,
      banklist:""
    };
    this.surplusflag = true;
  },
    handleOk() {
        window.location.href = "/member/recharge/index.html";
    },
    handleCancel() {
        this.setState({ visible: false });
        window.location.reload();
    },
    handleSubmit(e) {

    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      }
      if(this.state.bankCode == "" || this.state.bankNo == ""){
          error('请选择银行卡');
          e.preventDefault();
          return;
      }
      console.log('Submit!!!');
      console.log(values);
        this.setState({ visible: true });
      // $.ajax({
      //   url: '/user/registerFirst.html',
      //   type: 'POST',
      //   dataType: 'json',
      //   data: values
      // })
      // .done(function(data) {
      //   if(data.result){
                 
      //   }
      //   else
      //   {
      //     error(data.msg);
      //     $(".codeimg")[0].click();
      //   }  
      // })
      // .fail(function() {
      //   error('网络异常，请重试！');
      // });
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
        let banklist = '',bankNo='',bankCode='';
        if(data.bankList != '' || data.bankList.length > 0){
            let that = this,
            candel = false;
            banklist = data.bankList.map(function(k,v){
              let select = "";
              if(k.canDisable === false && !candel)
              {
                candel = true;
                select = "on";
                bankNo = k.bankNo;
                bankCode = k.bankCode;
              }
              return <li key={v} data-bankno={k.bankNo} data-bankcode={k.bankCode} className={select}><s><em className="iconfont">&#xe612;</em></s><span className="bankname"><img src={k.picPath} />{k.bankName}</span><span className="cardnumber">尾号:{k.hideBankNo}</span></li>
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
          bankCode:bankCode
        });
      that.selectcard(that);
    })
    .fail(function(){
      console.log("error");
    });
  },
  componentDidMount(){
    this.getcardlist();
    if(!this.state.cashtime){
        this.setState({
          disabled:true,
          warning:'您今日提现次数已达上限'
        });
    }
    if(this.props.balance==0){
        this.setState({
          disabled:true,
          warning:'您的账户余额已不足'
        });
    }
    if(this.props.withdraw == 0){
        this.setState({
          disabled:true,
          warning:'今日可提现额度为0'
        });
    }
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
  takeall(){
    const {setFieldsValue} = this.props.form;
    this.props.form.setFieldsValue({
      cashnumber:$("#j-canuse").html()
    });
    this.surplusflag = true;
  },
  changeBalance(e){
    let that = e.target,money = that.value*1;
    Publiclib.setdec(that);
    let number = (this.props.balance - money).toFixed(2),
    flag = this.props.withdraw - money,
    singlemax = this.props.singlemax - money,
    singlemin = money-this.props.minamount;
    this.setState({
      surplus : number
    });
    if( singlemax >= 0 ){
      this.setState({
        singlemax:true
      });
    }
    else
    {
      this.setState({
        singlemax:false
      });
    }
    if( money != "" ){
      $("#j-surplus").show();      
    }
    else
    {
      $("#j-surplus").hide();      
    } 
    if( flag >= 0 ){  
        this.surplusflag = true;    
    }
    else
    {      
      this.surplusflag=false;
    }
    if( singlemin >= 0 ){  
        this.singlemin = true;    
    }
    else
    {      
        this.singlemin=false;
    }   
  },
  isNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isFigure(value)){
        callback([new Error('请输正确的数字')]);
    }else if(!this.surplusflag){
        callback([new Error('请输入小于等于可提现金额')]);
    }else if(!this.state.singlemax){
        callback([new Error('超过单笔提现额度限制')]);
    }else if(!this.singlemin){
        callback([new Error('提现金额单笔不能小于'+this.props.minamount+'元')]);
    }else{
      callback();
    }
  },
  render(){
            if(this.state.data == ''){
              return false;
            }
            const addbank = this.state.data.bankList.length >= this.state.data.bankNum ? "" : <li className="addcard"><a href="/member/bankCard/bind.html" target="_blank"><em className="iconfont">&#xe62e;</em>添加银行卡</a></li>;
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const cashnumberProps = getFieldProps('cashnumber', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入提现金额' }
                    ],trigger: ['onBlur']
                  },
                  {
                    rules:[
                      {validator:this.isNumber},
                    ],trigger: ['onBlur']
                  }
                  ] 
                });
            const bankNoProps = getFieldProps("bankNo",{
                initialValue:this.state.bankNo
            });
            const bankCodeProps = getFieldProps("bankCode",{
                initialValue:this.state.bankCode
            }); 
            let balanceval = Publiclib.moneyFormat(this.props.balance);      
            return (
                    <div>
                      <div className="selectcard">
                        <div className="selectcard-title">选择提现银行卡</div>
                        <ul className="cardlist">
                          {this.state.banklist}
                        </ul>
                      </div>
                      <Form horizontal className="form-themes" target="_blank" method="post" action="/member/cash/doCash.html" form={this.props.form} onSubmit={this.handleSubmit}>
                          <div className="row" style={{width:400}}>
                            <div className="col-6 lab form-laber textR">账户余额</div>
                            <div className="col-18 pl20 form-text lab">
                            <span className="form-text"><span className="form-textbig">{balanceval}</span>元</span>
                            </div>
                          </div>
                          <div className="row" style={{width:800}}>
                            <div className="col-3 lab form-laber textR">可提金额</div>
                            <div className="col-9 lab pl20 hasunit">
                              <span className="form-text"><span className="form-textsm" id="j-canuse">{this.props.withdraw}</span>元<span className="link" onClick={this.takeall}>全部提现</span></span>
                            </div>                          
                          </div>                                                                                       
                          <div className="row" style={{width:800}}>
                            <div className="col-3 lab form-laber textR">提现金额</div>
                            <div className="col-9 pl20 hasunit">
                            <FormItem hasFeedback extra={this.state.warning}>
                              <Input {...cashnumberProps} disabled={this.state.disabled} onInput={this.changeBalance} name="amount" placeholder="请输入提现金额" maxLength="10" /></FormItem>
                              <span className="unit">元</span>
                              <div className="noshow surplus" id="j-surplus">提现后余额{this.state.surplus}元</div>
                              </div>                                                    
                          </div>
                          <Input {...bankNoProps} name="bankNo"  type="hidden" />                      
                          <Input {...bankCodeProps} name="bankCode"  type="hidden" />                      
                          <div className="row" style={{width:400}}>
                            <div className="col-6">&nbsp;</div>
                            <div className="col-18 pl20">
                              <FormItem><Button className="subBtn" disabled={this.state.disabled} type="primary" htmlType="submit" >确认提现</Button></FormItem>
                            </div>
                          </div>                               
                      </Form>
                        <Modal className="orderPayModal" ref="modal"
                               visible={this.state.visible}
                               title="登录资金账户完成提现" onOk={this.handleOk} onCancel={this.handleCancel}
                               width={480}
                               footer={[
                                   <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                                       提现成功
                                   </Button>,
                                   <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>提现失败</Button>,
                               ]}
                        >
                            <div className="modal-content"><i className="iconfont">&#xe62e;</i><p>请在新打开的资金托管账户完成提现</p></div>
                        </Modal>
                    </div>
              )
          }  
});
Recharge = createForm()(Recharge);

ReactDOM.render(<Recharge balance={Number($("#j-balance").val())} minamount={Number($("#j-cashMinAmount").val())} withdraw={Number($("#j-withdraw").val())} cashtime={Number($("#j-todaytimes").val()) < Number($("#j-daytimes").val())} singlemax={Number($("#j-single").val())}/>,  document.getElementById("j-cashmain"));
ReactDOM.render(<AccountVouchMenu current = {"3"}/>,  document.getElementById("j-sider-menu"));