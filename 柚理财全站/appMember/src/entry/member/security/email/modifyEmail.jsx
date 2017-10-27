import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import Success from '../../../../component/success/success';
import './modifyEmail.less';
import { Button, Select, Form, Input, Modal} from 'antd';
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
    title: '提交成功',
    content: v,
  });
}

//通过手机验证
let Phone = React.createClass({
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    if(this.codeflag){
      return false;
    }
    const self = this;
    this.codeflag = true;
    e.target.disabled = true;

    $.ajax({
      url: '/member/security/modifyEmailCode.html',
      type: 'POST',
      dataType: 'json',
      data:{"checkType":"01"}
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown msg={"验证码已经发送到您的手机，请注意查收！"} time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));
      } else {

        self.refs.countdownbtn.disabled = false;
        self.codeflag = false;
        error(data.msg);

      }
    })
    .fail(function() {
      self.callback();
      error('网络异常，请重试！');
    })
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const codeProps = getFieldProps('code', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
            ]
      }]
    });
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定手机</div>
          <div className="col-18 pl20">
          <p className="txt">{this.props.phone}</p>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown">点击获取验证码</button>
          </div>
        </div>
      </div>
    );
  }
});

//通过邮箱验证
let Email = React.createClass({
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    if(this.codeflag){
      return false;
    }
    const self = this;
    this.codeflag = true;
    e.target.disabled = true;

    $.ajax({
      url: '/member/security/modifyEmailCode.html',
      type: 'POST',
      dataType: 'json',
      data:{"checkType":"02"}
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown msg={"验证码已经发送到您的邮箱"+ self.props.email +"，请注意查收！"} time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));
      } else {

        self.refs.countdownbtn.disabled = false;
        self.codeflag = false;
        error(data.msg);

      }
    })
    .fail(function() {
      self.callback();
      //error('网络异常，请重试！');
    })
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const codeProps = getFieldProps('code', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
            ]
      }]
    });
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定邮箱</div>
          <div className="col-18 pl20">
          <p className="txt">{this.props.email}</p>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown">点击获取验证码</button>
          </div>
        </div>
      </div>
    );
  }
});

//通过密保问题+身份证验证
let PwdQuestion = React.createClass({
  getInitialState(){
    return {
      questionList:"",
      questionListNow:"",
      questionFlag:"",
      questionFlagNow:"",
      questionListSize:"",
      questionListNowNum:""
    }
  },
  setQuestionList(){
    let qname = [],qvalue = [];
    this.props.questionList.map(function(k,v){
      qname[v] = k.itemName;
      qvalue[v] = k.itemValue;
    });
    this.setState({
      questionList:qname,
      questionFlag:qvalue,
      questionListSize:qname.length,
      questionListNowNum:0,
      questionListNow:qname[0],
      questionFlagNow:qvalue[0]
    });
  },
  changeQuestionList(){
    let index = this.state.questionListNowNum;
    if(this.state.questionListSize - 1 > this.state.questionListNowNum){
      index +=1;
    }
    else
    {
      index = 0;
    }
    this.setState({
      questionListNow:this.state.questionList[index],
      questionFlagNow:this.state.questionFlag[index],
      questionListNowNum:index
    });
    console.log(index);
  },
  componentDidMount(){
    this.setQuestionList();
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const idCardProps = getFieldProps('idNo',{
        validate: [
        {
          rules:[
            { required: true, message: '请输入身份证号码' }
          ],
           trigger: ['onBlur'],
        },
        {
          rules:[
            {validator:ValidateFn.regexIdCard},
          ], trigger: ['onBlur'],
        }
        ]
    });
    const answerProps = getFieldProps('answer', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    const questionFlagProps = getFieldProps("questionFlag",{
        initialValue:this.state.questionFlagNow
    });
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">身份证号码</div>
          <div className="col-18 pl20">
            <FormItem hasFeedback>
              <Input {...idCardProps} type="text" className="input long-input" maxLength="18" autoComplete="off" placeholder="请输入身份证号码"  />
            </FormItem>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">密保问题</div>
          <div className="col-18 pl20">
            <FormItem hasFeedback>
              <p>{this.state.questionListNow}</p><a href="javascript:;" onClick={this.changeQuestionList}>换一个</a>
            </FormItem>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">答案</div>
          <div className="col-18 pl20">
            <FormItem hasFeedback>
              <Input {...answerProps} type="text" className="input long-input" maxLength="50" autoComplete="off" placeholder="请输入答案"  />
            </FormItem>
          </div>
        </div>
      </div>
    );
  }
});


//修改第二步
let Step_second = React.createClass({
  getInitialState() {
    return {
      checked: true,
      imgsrc:'/validimg.html',
      visible:false,
      content:""
    };
  },
  settimefun(){
    window.location.href = "/member/baseInfo/index.html";
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
      let self = this;
      values["checkType"] = this.props.checktype;
      values["modifyEmailSign"] = this.props.modifyEmailSign;
      let name=$("body").attr("data-tokenname");
      values[name]=$("body").attr("data-token");
      values['modifyType']=1;
      $.ajax({
        url: '/member/security/doBindEmail.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
          .done(function(data) {
            if(data.result){
              $("#modifyEmail").remove();
              ReactDOM.render(<Success callback={self.settimefun} link={"/member/baseInfo/index.html"} linkname={"安全设置"} time={3} text={"邮箱修改成功"} />, document.getElementById("modifyEmail1"));

            }
            else
            {
              error(data.msg);
              //$(".codeimg")[0].click();
            }
          })
          .fail(function() {
            //error('网络异常，请重试！');
          });
    });

  },
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getcode(e){
    if(this.codeflag){
      return false;
    }
    let email = this.props.form.getFieldValue('email');

    if( !email ){
      $("#email").focus().blur();
      return false;
    }else if(this.props.form.getFieldError('email')){
        return false;
    }

    const self = this;
    this.codeflag = true;
    e.target.disabled = true;
    $.ajax({
      url: '/member/security/bindEmailCode.html',
      type: 'POST',
      dataType: 'json',
      data:{email:email,checkType:self.props.checktype}
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));
      } else {

        self.refs.countdownbtn.disabled = false;
        self.codeflag = false;
        error(data.msg);

      }
    })
    .fail(function() {
      self.callback();
      //error('网络异常，请重试！');
    })
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;

    const emailProps = getFieldProps('email',{
      validate: [
        {
          rules:[
            { required: true, message: '请输入电子邮箱' }
          ],
          trigger: ['onBlur'],
        },
        {
          rules:[
            {validator:ValidateFn.isEmail},
          ], trigger: ['onBlur'],
        }
      ]
    });
    const codeProps = getFieldProps('code', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
            ]
      }]
    });
    return (
      <div className="account-main">
        <div className="main-title">
          修改邮箱
          <a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
        </div>
        <div className="modifyEmail" id="modifyEmail">
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">邮箱</div>
                  <div className="col-18 pl20">
                    <FormItem hasFeedback>
                      <Input type="text" {...emailProps}  className="input long-input" maxLength="50" autoComplete="off" placeholder="请输入邮箱"  />
                    </FormItem>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">验证码</div>
                  <div className="col-9 pl20">
                    <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
                    </FormItem>
                  </div>
                  <div className="col-9 pl20">
                  <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown">点击获取验证码</button>
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6">&nbsp;</div>
                  <div className="col-18 pl20">
                    <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >修改</Button></FormItem>
                  </div>
                </div>
            </Form>
        </div>
        <div id="modifyEmail1" style={{'paddingTop':'80px'}}></div>
      </div>
    );
  }
});

Step_second = createForm()(Step_second);

let ModifyEmail = React.createClass({
  getInitialState(){
    return {
      type: '01',
      data:""
    }
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
      let self = this;
      values["checkType"] = this.state.type;
      $.ajax({
        url: '/member/security/doModifyEmail.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.props.set(self.state.type,data.modifyEmailSign);
        }
        else
        {
          error(data.msg);
          //$(".codeimg")[0].click();
        }
      })
      .fail(function() {
        //error('网络异常，请重试！');
      });
    });

  },
  callback(){
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  typeChangeHander(value){
    this.setState({type: value});
  },
  backArrValHander(arr, key){
    return arr[key];
  },
  getInfo(){
    const self = this;
    $.ajax({
      url: '/member/security/getCheckUserInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      self.setState({
        data:data
      });
    })
    .fail(function() {
      //console.log("error");
    });
  },
  componentDidMount(){
    this.getInfo();
  },
  render(){
    let data = this.state.data;
    if(!data){
      return false;
    }
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const componentArr = [<Phone form={this.props.form} phone={data.mobile}/>, <Email form={this.props.form} email={data.email}/>, <PwdQuestion form={this.props.form}  questionList={data.questionList} />];
    let html = this.backArrValHander(componentArr, parseInt(this.state.type)-1);
    let list =  "";

    if( data.mobile){
      list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                <Option value="01">通过手机验证</Option>
              </Select>;
    }
    if( data.email){
      list = <Select onChange={this.typeChangeHander} defaultValue="02" style={{ width: '100%' }}>
                <Option value="02">通过邮箱验证</Option>
              </Select>;
    }

    if( data.questionList.length && data.realNameStatus == "1" && data.userNature == "1"){
      list = <Select onChange={this.typeChangeHander} defaultValue="03" style={{ width: '100%' }}>
                <Option value="03">通过身份证+密保问题</Option>
              </Select>;
    }

    if( data.mobile && data.email ){
      list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                <Option value="01">通过手机验证</Option>
                <Option value="02">通过邮箱验证</Option>
              </Select>;
    }

    if(data.userNature == "1"){
      if( data.mobile && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                  <Option value="01">通过手机验证</Option>
                  <Option value="03">通过身份证+密保问题</Option>
                </Select>;
      }
    }
    else
    {
      if( data.mobile && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                  <Option value="01">通过手机验证</Option>
                </Select>;
      }
    }

    if(data.userNature == "1"){
      if( data.email && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="02" style={{ width: '100%' }}>
                  <Option value="02">通过邮箱验证</Option>
                  <Option value="03">通过身份证+密保问题</Option>
                </Select>;
      }
    }
    else
    {
      if( data.email && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="02" style={{ width: '100%' }}>
                  <Option value="02">通过邮箱验证</Option>
                </Select>;
      }
    }

    if(data.userNature == "1"){
      if( data.mobile && data.email && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                  <Option value="01">通过手机验证</Option>
                  <Option value="02">通过邮箱验证</Option>
                  <Option value="03">通过身份证+密保问题</Option>
                </Select>;
      }
    }
    else
    {
      if( data.mobile && data.email && data.questionList.length && data.realNameStatus == "1"){
        list = <Select onChange={this.typeChangeHander} defaultValue="01" style={{ width: '100%' }}>
                  <Option value="01">通过手机验证</Option>
                  <Option value="02">通过邮箱验证</Option>
                </Select>;
      }
    }


    return (
      <div className="account-main">
        <div className="main-title">
          修改邮箱
          <a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
        </div>
        <div className="modifyEmail">
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">验证方式</div>
                  <div className="col-18 pl20">
                    <FormItem>
                      {list}
                    </FormItem>
                  </div>
                </div>
                <div>
                <div id="firstStep">{html}</div>
                <div id="secondtStep" style={{display:"none"}}></div>
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
ModifyEmail = createForm()(ModifyEmail);

let SetEmail = React.createClass({
  getInitialState(){
    return {
      flag:true,
      checkType:'',
      modifyEmailSign:''
    }
  },
  set(e,modifyEmailSign){
    this.setState({
      flag:false,
      checkType:e,
      modifyEmailSign:modifyEmailSign
    })
  },
  render(){
    let html = this.state.flag ? <ModifyEmail set = {this.set}/> : <Step_second checktype={this.state.checkType} modifyEmailSign = {this.state.modifyEmailSign}/>;
    return (
      <span>{html}</span>
    )
  }
});

ReactDOM.render(<SetEmail />,  document.getElementById("j-modifyEmailmain"));


if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
}
