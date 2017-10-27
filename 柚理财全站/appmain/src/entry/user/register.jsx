import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './register.less';
import CountDown from '../../component/countDown/CountDownComponent';
import  { ValidateFn } from '../../common/validateFn';
import  { Reg } from '../../common/regularFun';
import { Button, Form, Input ,Checkbox, Modal} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function noop() {
  return false;
}

function error(a) {
  Modal.error({
    title: a,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

let FirstStep = React.createClass({
   getInitialState() {
    return {
      checked: true,
      imgsrc:'/validimg.html',
      visible:false,
      content:"",
      protocolType:'协议',
      protocolName:'标题',
      inviteType:1
    };
  },
  handleReset(e) {
    e.preventDefault();
    this.props.form.resetFields();
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

      if(!this.state.checked){
        console.log('unchecked');
        return;
      }
      let self = this;
      $.ajax({
        url: '/user/registerFirst.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          ReactDOM.render(<SecondStep  mobile={data.mobile} />, document.getElementById("registerForm"));        
        }
        else
        {
          error(data.msg);
          $(".codeimg")[0].click();
          self.props.form.resetFields(["validCode"]); 
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });
  },
  checkPass(rule, value, callback) {
    const { validateFields } = this.props.form;
    if (!value) {
        callback();
    }else if(Reg.regexPassword(value)){        
        callback([new Error('8-16位长且不含空格，至少包含一个字母、数字')]);        
    }// else if (value) {
    //     validateFields(['confirmPwd'], { force: true });
    // }
      callback();    
  },
  checkPass2(rule, value, callback) {
    const { getFieldValue } = this.props.form;
    if (!value) {
        callback();
    }else if(Reg.regexPassword(value))
    {        
        callback([new Error('8-16位长且不含空格，至少包含一个字母、数字')]);        
    }else if (value && value !== getFieldValue('Pwd')) {
        callback('两次输入密码不一致！');
    }
      callback();       
  },
  inviteUserMobile(rule, value, callback) {
    const { getFieldValue } = this.props.form;
    if (!value) {
        callback();
    }else if(Reg.isMobile(value))
    {        
        callback([new Error('邀请人手机号码格式不正确')]);        
    }
      callback();       
  },
  inviteStaffMobile(rule, value, callback) {
    const { getFieldValue } = this.props.form;
    if (!value) {
        callback();
    }else if(Reg.regStaff(value))
    {        
        callback([new Error('选填员工码，格式如 JZ030010')]);        
    }
      callback();       
  },
  onChange(e) { 
    this.setState({
      checked:e.target.checked,
    });
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
    this.props.form.resetFields(["validCode"]);
    this.props.form.validateFields(['validCode']);
  },
  getInfo(id){
    let self = this;
    $.ajax({
      url: '/user/registerProtocol.html',
      type: 'POST',
      dataType: 'json',
      data: {protocolId:id},
    })
    .done(function(data) {
      if(data.result){
       self.setState({
          visible: true,
          content:data.content,
          protocolType:data.protocolType,
          protocolName:data.protocolName
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
  componentDidMount(){
    let self = this;
    self.setdefault();
    if( this.props.phone){
       $(".choiceNo a").removeClass("active");
       $(".choiceNo a").eq(1).addClass("active");
       $(".inviteUserMobilebox").addClass("hide");
      $(".inviteStaffMobilebox").removeClass("hide");
      const input = this.refs.phoneInput.refs.input;
      input.focus();
      this.setState({
        inviteType: 2
      });
    }
    $(".agreementbox a").click(function(){
      self.getInfo($(this).data("id"));
    });
    $(".choiceNo a").click(function(){
      $(this).addClass("active");
      $(this).siblings().removeClass("active");
    })
  },
  handleCancel() {
    this.setState({ visible: false });
  },
  choiceNo(e){
     this.setState({
        inviteType: 1
      });
     $(".inviteUserMobilebox").removeClass("hide");
     $(".inviteStaffMobilebox").addClass("hide");
  },
  choiceNo2(e){
     this.setState({
        inviteType: 2
      });
     $(".inviteUserMobilebox").addClass("hide");
     $(".inviteStaffMobilebox").removeClass("hide");
  },
  setdefault(){
    this.props.form.setFieldsValue({
      inviteUserMobile: this.props.phone,
    });
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
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
          {validator:ValidateFn.checkMobile.bind(this)},
        ], trigger: ['onBlur'],
      }
      ]
    });    
    const passwdProps = getFieldProps('Pwd', {
     validate: [
      { rules: [
        { required: true, whitespace: true, message: '请填写密码' },
        {
          validator:this.checkPass
        }
      ],trigger: ['onBlur'],
    }]
    });
    const rePasswdProps = getFieldProps('confirmPwd', {
      validate: [
      {rules: [{
        required: true,
        whitespace: true,
        message: '请再次输入密码',
      },
      {
        validator: this.checkPass2
      }],trigger: ['onBlur'],
    }]
    });
    const inviteUserMobileProps = getFieldProps('inviteUserMobile', {
      validate: [
      {
        rules:[
          {validator:this.inviteUserMobile},
        ], trigger: ['onBlur'],
      }
      ]
    });
    const inviteStaffMobileProps = getFieldProps('inviteStaffMobile', {
      validate: [
      {
        rules:[
          {validator:this.inviteStaffMobile},
        ], trigger: ['onBlur'],
      }
      ]
    });
    //验证码校验
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
    const uiProps = getFieldProps("ui",{
        initialValue:this.props.ui
    });
    const userNatureProps = getFieldProps("userNature",{
        initialValue:this.props.userNature
    });
    const inviteTypeProps = getFieldProps("inviteType",{
        initialValue: this.state.inviteType
    });
    const agreeProps = getFieldProps("agree",{
        initialValue:"1"
    });
    const registerChannelProps = getFieldProps("registerChannel",{
    });
    const formItemLayout = {
      wrapperCol: { span: 24},
      style:{marginBottom:0}
    };
    const label = !this.state.checked ? '请阅读并同意协议' : '';
    let agreement ="";
    
    agreement = protocolList.map(function(k,v){
     return <a data-id={k.id} key={v}>《{k.name}》</a>;
    });
    return (
      <div>
        <div className="reg_step step01 clearfix">
            <em className="one active">填写账户信息</em>
            <em className="two">手机号码验证</em>
            <em className="three">账户注册完成</em>
        </div>
        <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">手机号码</div>
              <div className="col-18 pl20">
                <FormItem 
                hasFeedback
                help={isFieldValidating('mobile') ? '校验中...' : (getFieldError('mobile') || []).join(', ')}
                ><Input {...mobileProps} placeholder="请输入您的手机号码" maxLength="11" /></FormItem>
              </div>
            </div>

            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">登录密码</div>
              <div className="col-18 pl20">
                <FormItem hasFeedback><Input {...passwdProps} type="password" autoComplete="off" maxLength="16" placeholder="创建登录密码" 
                onContextMenu={noop} onPaste={noop} onCopy={noop} onCut={noop} />
                </FormItem>
              </div>
            </div>

            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">确认密码</div>
              <div className="col-18 pl20">
                <FormItem hasFeedback><Input {...rePasswdProps} type="password" autoComplete="off" maxLength="16" placeholder="再次确认登录密码"
                  onContextMenu={noop} onPaste={noop} onCopy={noop} onCut={noop}
                /></FormItem>
              </div>
            </div>
            <div className="row choiceNo" style={{width:400,height:30}}>
              <div className="col-6 lab form-laber textR"></div>
              <div className="col-18 pl20">
                <a href="javascript:;" onClick={this.choiceNo} className="active">员工码</a>
                <a href="javascript:;" onClick={this.choiceNo2}>邀请码</a>
              </div>
            </div>
            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">选填项</div>
              <div className="col-18 pl20">
                <div className="inviteUserMobilebox">
                  <FormItem hasFeedback><Input {...inviteStaffMobileProps} placeholder="选填，请输入服务人员专属员工码" autoComplete="off" maxLength="11" /></FormItem>
                </div>
                <div className="inviteStaffMobilebox hide">
                  <FormItem hasFeedback><Input {...inviteUserMobileProps} ref="phoneInput" placeholder="选填，请输入邀请人手机号" autoComplete="off" maxLength="11" /></FormItem>
                </div>
              </div>
            </div>
            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">注册渠道</div>
              <div className="col-18 pl20">
                <FormItem hasFeedback><Input {...registerChannelProps} name="registerChannel" type="text" autoComplete="off" placeholder="选填，请输入注册渠道" maxLength="30"/></FormItem>
              </div>
            </div>
            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">验证码</div>
              <div className="col-10 pl20">
              <FormItem
                hasFeedback
                help={isFieldValidating('validCode') ? '校验中...' : (getFieldError('validCode') || []).join(', ')}
              ><Input maxLength="4" {...validCodeProps}  placeholder="验证码" size="large" type="text" autoComplete="off" /></FormItem></div>
              <div className="col-4 col-offset-2"><img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" /></div>
            </div>
            <div className="row" style={{width:400}}>
              <div className="col-6">&nbsp;</div>
              <div className="col-18 pl20">
              <FormItem validateStatus="error" className="agreement" {...agreeProps} help = {label} style={{marginBottom:"0"}}><span className="agreementbox"><Checkbox checked={this.state.checked} onChange={this.onChange} ></Checkbox>我已阅读并同意{agreement}</span></FormItem>
              </div>
            </div>  
            
            <Input {...uiProps}  type="hidden" />
            <Input {...userNatureProps} type="hidden" />
            <Input {...inviteTypeProps} type="hidden" />
            <div className="row" style={{width:400}}>
              <div className="col-6">&nbsp;</div>
              <div className="col-18 pl20">
                <FormItem><Button type="primary" htmlType="submit" >立即注册</Button></FormItem>
              </div>
            </div>
            <Modal
              visible={this.state.visible}
              title={this.state.protocolType} onCancel={this.handleCancel}
              width={680}
              footer={[
                <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>关闭</Button>
              ]}
            >
              <h2 className="textC">{this.state.protocolName}</h2>
              <div style={{maxHeight:"400px",overflow:"auto"}} dangerouslySetInnerHTML={{__html: this.state.content}}></div>
            </Modal> 
        </Form>
      </div>
    );
  },
});

FirstStep = createForm()(FirstStep);

let SecondStep = React.createClass({
   getInitialState() {
    return {
      timeflag:false,
      checked: true
    };
  },
  handleReset(e) {
    e.preventDefault();
    this.props.form.resetFields();
  },
  handleSubmit(e) {
    let self = this;
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }     
      $.ajax({
        url: '/user/doRegister.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.NoErrorShow();
          $(".site-row").eq(1).hide();
          $("#j-register-ok").addClass('show');
          $(".a-changeRegister").hide();
        }
        else
        {          
          self.setErrorText(data.msg);                 
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });
  },
  callback(){
    this.setState({
      timeflag : false
    });     
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  getsms(e){
      if(this.state.timeflag){
        return false;
      }
      let self = this,el = e.target,mobile = this.props.mobile;
      self.setState({
        timeflag : true
      }); 
      $.ajax({
        url: '/user/registerPhoneCode.html',
        type: 'POST',
        dataType: 'json',
        data: {mobilePhone:mobile},
      })
      .done(function(data){
          if(data.result == false)
          { 
            self.setErrorText(data.msg);       
            self.callback();           
          }
          else
          {
            self.NoErrorShow();
            el.disabled = true;
            ReactDOM.render(<CountDown  time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));    
          }      
      })
      .fail(function() {
        self.callback();
        error('网络异常，请重试！');
      });
  },
  NoErrorShow(){
    $("#secondStepErrorMsg").css({"display": "none"});
  },
  setErrorText(msg){
     $("#secondStepErrorMsg").html(msg).css({"display": "block"});  
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const smsProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    return (
      <div>
        <div className="reg_step step02 clearfix">
            <em className="one active">填写账户信息</em>
            <em className="two active">手机号码验证</em>
            <em className="three">账户注册完成</em>
        </div>
        <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
          <div className="row" style={{width:400,margin:"0 0 14px"}}>
            <div className="col-6 lab form-laber textR">手机号码</div>
            <div className="col-18 pl20 form-text lab">
              {this.props.mobile}
            </div>
          </div>
          <div className="row secondStep-btn" style={{width:400}}>
            <div className="col-6 lab form-laber textR">短信验证码</div>
            <div className="col-9 pl20">
              <FormItem hasFeedback><Input {...smsProps} type="text" maxLength="6" autoComplete="off" placeholder="输入短信验证码"  onInput={this.NoErrorShow} />
              </FormItem>
            </div>
            <div className="col-9 pl20">
            <button type="button" className="countdown-btn" onClick={this.getsms}  ref="countdownbtn" id="countdown">点击获取验证码</button>
            </div>            
          </div>
          <div className="row secondStep-btn">
             <div className="col-6">&nbsp;</div>
             <div className="error-msg col-18 pl20" id="secondStepErrorMsg"></div>
          </div>         
          <div className="row" style={{width:400}}>
            <div className="col-6">&nbsp;</div>
            <div className="col-18 pl20">              
              <FormItem><Button type="primary" htmlType="submit" >提交</Button></FormItem>              
            </div>
          </div>          
        </Form> 
      </div>
        
      );
  }

});

SecondStep = createForm()(SecondStep);
ReactDOM.render(<FirstStep userNature={$("#userNature").data("value")} ui={$("#userNature").data("ui")} phone={$("#userNature").data("phone")} />, document.getElementById("registerForm"));