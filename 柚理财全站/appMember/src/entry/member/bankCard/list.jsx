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
import './list.less';
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

let Add =React.createClass({
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
            url: '/member/bankCard/bindCard.html',
            type: 'POST',
            dataType: 'json',
            data: values
          })
          .done(function(data) {
            if(data.result){
                success('银行卡添加成功','/member/bankCard/list.html')
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
          /*this.setState({
            visible:true
          })*/
        });

    },
   getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target;
    $.ajax({
      url: '/member/security/smsCodeApply.html',
      type: 'POST',
      dataType: 'json',
      data: {reqType:this.props.reqType,srvTxCode:this.props.srvTxCode},
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
  render(){
    const {getFieldProps, getFieldError, isFieldValidating} = this.props.form;
    const cardIdProps = getFieldProps('cardId', {
        validate: [{
            rules: [
                {required: true, message: '请输入银行卡号'},
                {validator: ValidateFn.isNumber},
            ], trigger: 'onBlur'
        }]
    });
    const codeProps = getFieldProps('smsCode', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    return (
        <li className="addcard">
        <Form horizontal className="form-themes" target="_blank" action="/member/bankCard/enterpriseBindBankcard.html" method="post" form={this.props.form} onSubmit={this.handleSubmit}>           
            <div className="row" style={{width: 260}}>
                <FormItem hasFeedback >
                    <Input {...cardIdProps} name="cardId" maxLength="19"
                           placeholder="请填写银行卡号"/>
                </FormItem>
            </div>
            <div className="row" style={{width:260}}>
              <div className="col-12 pr20">
                <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
                </FormItem>
              </div>
              <div className="col-9">
              <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_phone">点击获取验证码</button>
              </div>
            </div>
            <div className="row" style={{width: 280}}>
                <FormItem><Button style={{marginLeft:82}} className="submit-button" type="primary" htmlType="submit">添加银行卡</Button></FormItem>
            </div>
        </Form>
          {/*<a href="/member/bankCard/bind.html" target="_blank"><em className="iconfont">&#xe645;</em>添加银行卡</a>*/}
        </li>
      )
  }
})
Add = createForm()(Add);

let Cardlist = React.createClass({
  getInitialState() {
    return {
      data: this.props.data,
      visible:false,
     // url:'',
      bankNo:''
    };
  },
  showModel(e){
    this.setState({
      visible:true,
     // url:$(e.target).attr('data-href'),
      bankNo:$(e.target).attr('data-no')
    })
  },
  handleCancel(){
    this.setState({
      visible:false,
      bankNo:''
    })
  },
  reload(){
    window.location.reload()
  },
  handleOk(){
    let cardId = this.state.bankNo;
    $.ajax({
      url:"/member/bankCard/unbindCard.html",
      type: 'POST',
      dataType: 'json',
      data:{cardId:cardId}
    })
    .done(function(data) {
      if(data.result){
        success('解绑成功','/member/bankCard/list.html');
      }else{
        error(data.msg,'/member/bankCard/list.html');
      }
    })
    .fail(function() {
     // error('网络异常，请重试！');
    });
  },
  render(){
    let addbank = <Add srvTxCode={$("#srvTxCode").val()} reqType={$("#reqType").val()} />;

    if(this.state.data.userCaCheInfo.userNature=='2'){
      addbank='';
    }
    if(this.state.data.bankList.length){
      addbank='';
    }
    let banklist = '';
    var _this=this;
      if(this.state.data.bankList != '' || this.state.data.bankList.length > 0){
          banklist = this.state.data.bankList.map(function(k,v){
              //let link = "/member/bankCard/unbindCard.html?cardId="+ k.bankNo;
              let unbindlink = k.canDisable === true ? <span className="del"><a href='javascript:;' data-no={k.bankNo} onClick={_this.showModel} >解除绑定</a></span> : <span className="del"><a>默认提现银行卡，不允许解绑</a></span>;
              let quick = k.fastPayFlag == "Y" ? <span className="quick">快捷</span> : "";
              return <li key={v}>{quick}<span className="bankname">银行卡号</span><span className="cardnumber">{k.bankNo}</span>{unbindlink}</li>
            })
      }
      else
      {
          banklist = "";
      }

      return (
          <div className="account-main">
              <div className="main-title">银行卡</div>
              <ul className="card-list clearfix">
              {banklist}
              {addbank}
              </ul>
              <div className="help">
                  <div className="help-title"><em className="iconfont">&#xe639;</em>温馨提示</div>
                  <ol>
                    <li>普通卡只能用于提现，快捷卡可以快捷充值和提现。</li>
                    <li>在已绑定普通卡的前提下，绑定快捷卡会覆盖原来绑定的普通卡，成为唯一取现卡。</li>
                    <li>绑定快捷卡后，如需解绑，需要您登录第三方托管账户进行操作。</li>
                  </ol>
              </div>
            <Modal title="提示" visible={this.state.visible}
                   onOk={this.handleOk} onCancel={this.handleCancel}
                   width={400}
                   footer={
                       <div><Button size="large" type="ghost" className='mr10' onClick={this.handleCancel}>取消</Button>
                         <Button onClick={this.handleOk} className="ant-btn ant-btn-primary ant-btn-lg">确认</Button>
                      </div>

                   }>
              <div className="modelCar clearfix"><i className="iconfont">&#xe629;</i><p>是否确定对这张银行卡解除绑定？</p></div>

            </Modal>
          </div>
        )
    }
});

let getinfo = function(){
  $.ajax({
    url: '/member/bankCard/getBankCardList.html',
    type: 'POST',
    dataType: 'json'
  })
  .done(function(data) {

    if( data.result ){

       if(data.userCaCheInfo.userNature == "1")
        {
          ReactDOM.render(<Cardlist data={data}/>, document.getElementById("j-listtab"));
        }
        else
        {
          if( data.bankList.length){
            ReactDOM.render(<Cardlist data={data}/>, document.getElementById("j-listtab"));
          }
          else
          {
            ReactDOM.render(<Nobankcard />, document.getElementById("j-listtab"));
          }
        }

    }
    else
    {
      error(data.msg,data.url);
    }


  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}

getinfo();

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"8"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"10"}/>,  document.getElementById("j-sider-menu"));
}
