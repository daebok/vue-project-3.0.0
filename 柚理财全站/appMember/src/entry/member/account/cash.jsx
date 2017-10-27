import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import  { ValidateFn } from '../../../common/validateFn';
import  { Reg } from '../../../common/regularFun';
import {Publiclib} from '../../../common/common';
import './cash.less';
import { Button, Form, Input,Modal,Select} from 'antd';
const Option = Select.Option;
const createForm = Form.create;
const FormItem = Form.Item;

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
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

function pwderror() {
  Modal.error({
    title: '温馨提示',
    content: '为了您的资金安全，请先设置交易密码',
    okText:'设置交易密码',
    iconType:'question-circle',
    align:{top:'200px'},
    onOk() {
        window.location.href = "/member/security/setPwd.html";
    },
    onCancel() {},
  });
}

if($("#payPwdStatus").val()!=1){
    pwderror();
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
      banklist:"",
      banklistLen:"",
      selectVal:""
    };
    this.surplusflag = true;
  },
  handleSubmit(e) {

    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        e.preventDefault();
        return;
      }
      var tppName = $(".tppName").val();
      if(tppName!="cbhb"){
        if(this.state.bankCode == "" || this.state.bankNo == ""){
            error('请绑定并选择银行卡');
            e.preventDefault();
            return;
        }
      }else{
        values.bankCode="";
        values.bankNo="";
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
      if( data.result ){
        window.setTimeout(function(){
          $("#cashnumber").on("input",function(e){
            that.changeBalance(e);
          });
        },1000);
        let banklist = '',bankNo='',bankCode='',banklistLen=data.bankList.length;
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
          warning:'您无可提现金额'
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
    let minNumber = [this.props.withdraw,this.props.balance,this.props.singlemax].min().toString();
    this.props.form.setFieldsValue({
      cashnumber:minNumber
    });
    this.surplusflag = true;
    this.checkFun(Number(minNumber));
  },
  changeBalance(e){
    let that = e.target,money = that.value*1;
    Publiclib.setdec(that);
    this.checkFun(money)
  },
  checkFun(m){
    let money = m*1,
    number = (this.props.balance - money).toFixed(2),
    flag = this.props.withdraw - money,
    singlemax = this.props.singlemax - money,
    singlemin = money - this.props.minamount;
    //this.setState({
      this.state.surplus = number;
    //});
    if( singlemax >= 0 ){
      //this.setState({
        this.state.singlemax=true;
      //});
    }
    else
    {
      //this.setState({
        this.state.singlemax=false;
      //});
    }
    if( isNaN(money)==false ){
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
    }else if(Number(this.props.balance) < value){
        callback([new Error('提现金额不能大于您的可用余额')]);
    }else if(!this.surplusflag){
        callback([new Error('您今天还可提现'+this.props.withdraw+'元')]);
    }else if(!this.state.singlemax){
        callback([new Error('单笔提现不能超过'+this.props.singlemax+'元')]);
    }else if(!this.singlemin){
        callback([new Error('最小提现金额为'+this.props.minamount+'元')]);
    }else if(this.props.todaytimes>=this.props.daytimes){
        callback([new Error('当日提现不能超过'+this.props.todaytimes+'次')]);
    /*}else if(value>50000 && this.state.selectVal=="2"){
        callback([new Error('人行通道提现不能超过5万元')]);*/
    }else{
      callback();
    }
  },
  handleOk() {
    window.location.href = "/member/recharge/index.html?type=cash";
  },
  handleCancel() {
    this.setState({ visible: false });
    window.location.reload();
  },
  handleChange(value) {
    this.setState({
      selectVal : value
    });
  },
  render(){
            if(this.state.data == ''){
              return false;
            }
            let addbank = this.state.banklistLen?"": <li className="addcard"><a href="/member/bankCard/list.html"><em className="iconfont">&#xe645;</em>添加银行卡</a></li>;
            if(this.state.data.userCaCheInfo.userNature=='2'){
                  addbank='';
            }
            let displaystyle = $(".tppName").val()!="cbhb"?"block":"none";
            const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
            const cashnumberProps = getFieldProps('cashnumber', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入提现金额' }
                    ],
                     trigger: ['onBlur'],
                  },
                  {
                    rules:[
                      {validator:this.isNumber},
                    ],
                     trigger: ['onBlur']
                  }
                  ]
                });
            const bankNoProps = getFieldProps("bankNo",{
                initialValue:this.state.bankNo
            });
            const routeCodeProps = getFieldProps("routeCode",{
                initialValue:this.state.selectVal
            });
            const bankCodeProps = getFieldProps("bankCode",{
                initialValue:this.state.bankCode
            });
            let balanceval = Publiclib.moneyFormat(this.props.balance);
            let inputHtml ="";
            if(this.state.selectVal=="2"){
              const cardBankCnapsProps = getFieldProps('cardBankCnaps', {
                  validate: [
                  {
                    rules:[
                      { required: true, message: '请输入联行号' }
                    ],
                     trigger: ['onBlur'],
                  },
                  {
                    rules:[
                      {validator:ValidateFn.isNumber},
                    ],
                     trigger: ['onBlur']
                  }
                  ]
                });
              inputHtml = <div className="row" style={{width:800 , clear:"both"}}>
                            <div className="col-3 lab form-laber textR">联行号</div>
                            <div className="col-9 pl20 hasunit">
                            <FormItem hasFeedback>
                              <Input {...cardBankCnapsProps}  name="cardBankCnaps" placeholder="请输入联行号" maxLength="20"/></FormItem>
                              </div>
                          </div>;
            }
            
            return (
                    <div style={{padding:"0 0 0 20px"}}>
                      <div className="selectcard" style={{"display":displaystyle}}>
                        <div className="selectcard-title">选择银行卡</div>
                        <ul className="cardlist">
                          {this.state.banklist}
                          {addbank}
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
                            <div className="col-3 lab form-laber textR">可选通道</div>
                            <div className="col-9 lab pl20 hasunit">
                              <Select defaultValue="" onChange={this.handleChange}>
                                <Option value="">超网通道</Option>
                                <Option value="2">人行通道</Option>
                              </Select>
                            </div>
                          </div>
                          {inputHtml}
                          <div className="row clearfix" style={{width:800 , clear:"both"}}>
                            <div className="col-3 lab form-laber textR">提现金额</div>
                            <div className="col-9 pl20 hasunit">
                            <FormItem hasFeedback extra={this.state.warning}>
                              <Input {...cashnumberProps}  name="amount" placeholder="请输入提现金额" maxLength="10"/></FormItem>
                              <span className="unit">元</span>
                              <div className="noshow surplus hide" id="j-surplus">提现后余额{this.state.surplus}元</div>
                              </div>
                          </div>
                          <Input {...bankNoProps} name="bankNo"  type="hidden" />
                          <Input {...routeCodeProps} name="routeCode"  type="hidden" />
                          <Input {...bankCodeProps} name="bankCode"  type="hidden" />
                          <div className="row" style={{width:400}}>
                            <div className="col-6">&nbsp;</div>
                            <div className="col-18 pl20">
                              <FormItem><Button className="subBtn"  type="primary" htmlType="submit" >确认提现</Button></FormItem>
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

let RechargeCompany = React.createClass({
  getInitialState() {
    return {}
  },
   render(){
            return (
                    <div style={{padding:"0 0 0 20px"}}>
                      <p className="remind" style={{"marginTop":"100px","fontSize":"18px"}}>请联系客户人员进行提现</p>
                    </div>
              )
          }

})

if($(".userNatrue").val()==2){
   ReactDOM.render(<RechargeCompany />,document.getElementById("j-cashmain"));
}else{
  ReactDOM.render(<Recharge balance={Number($("#j-balance").val())} daytimes={Number($("#j-daytimes").val())} todaytimes={Number($("#j-todaytimes").val())} minamount={Number($("#j-minamount").val())} withdraw={Number($("#j-withdraw").val())} cashtime={Number($("#j-todaytimes").val()) < Number($("#j-daytimes").val())} singlemax={Number($("#j-single").val())}/>,  document.getElementById("j-cashmain"));
}
ReactDOM.render(<Accountmenu current = {"12"}/>,  document.getElementById("j-sider-menu"));
