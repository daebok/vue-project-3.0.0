import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './getpwd.less';
import { ValidateFn } from '../../common/validateFn';
import Success from '../../component/success/success';
import CountDown from '../../component/countDown/CountDownComponent';
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
  getInitialState(){
    return {
      imgsrc:'/validimg.html'
    }
  },
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn2.disabled = false;
    this.refs.countdownbtn2.innerHTML="重新获取";
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
  },   
  getcode(e){
    if(this.codeflag){
      return false;
    }

    let mobile = this.props.form.getFieldValue('pathWay');
    let imgcode = this.props.form.getFieldValue('imgValidCode');

    if( !mobile ){
      $("#pathWay").focus().blur();
      return false;
    }else if(this.props.form.getFieldError('pathWay')){
        return false;
    }

    if( !imgcode ){
      $("#imgValidCode").focus().blur();
      return false;
    }else if(this.props.form.getFieldError('imgValidCode')){
      return false;
    }

    const self = this;
    this.codeflag = true;
    e.target.disabled = true;
    $.ajax({
      url: '/user/security/sendValidCode.html',
      type: 'POST',
      dataType: 'json',
      data:{"pathWay":mobile,"imgValidCode":imgcode,"type":"1"}
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown msg={"验证码已经发送到您的手机，请注意查收！"} time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown2"));    
      } else {
        
        self.refs.countdownbtn2.disabled = false;
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
    const codeProps = getFieldProps('dynamicValidCode', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    //验证码校验
    const validCodeProps = getFieldProps('imgValidCode', {
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
    const mobileProps = getFieldProps('pathWay', {
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
          ], trigger: ['onBlur']
        }
      ]
    });  
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定手机</div>
          <div className="col-18 pl20">
              <FormItem 
              hasFeedback
              help={isFieldValidating('pathWay') ? '校验中...' : (getFieldError('pathWay') || []).join(', ')}
              ><Input {...mobileProps}   placeholder="请输入您的手机号码" maxLength="11" /></FormItem>     
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...validCodeProps}   type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
            <img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" />
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">短信验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn2" id="countdown2">点击获取验证码</button>
          </div>
        </div>
      </div>
    );
  }
});

//通过邮箱验证
let Email = React.createClass({
  getInitialState(){
    return {
      imgsrc:'/validimg.html'
    }
  },  
  callback(){
    this.codeflag = false;
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="重新获取";
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
  },   
  getcode(e){
    if(this.codeflag){
      return false;
    }

    let email = this.props.form.getFieldValue('pathWay');
    let imgcode = this.props.form.getFieldValue('imgValidCode');

    if( !email ){
      $("#pathWay").focus().blur();
      return false;
    }else if(this.props.form.getFieldError('pathWay')){
        return false;
    }

    if( !imgcode ){
      $("#imgValidCode").focus().blur();
      return false;
    }else if(this.props.form.getFieldError('imgValidCode')){
      return false;
    }

    const self = this;
    this.codeflag = true;
    e.target.disabled = true;
    $.ajax({
      url: '/user/security/sendValidCode.html',
      type: 'POST',
      dataType: 'json',
      data:{"pathWay":email,"imgValidCode":imgcode,"type":"2"}
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<CountDown msg={"验证码已经发送到您的邮箱，请注意查收！"} time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown"));    
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
    const codeProps = getFieldProps('dynamicValidCode', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
     //验证码校验
    const validCodeProps = getFieldProps('imgValidCode', {
      validate: [
        {
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
          ],
         trigger: ['onBlur', 'onChange'],
        },
        {
          rules:[
            {validator:ValidateFn.checkValidCode.bind(this)},
          ], trigger: ['onBlur'],
        }
      ]
    });
    const emailProps = getFieldProps('pathWay',{
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
    return (
      <div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定邮箱</div>
          <div className="col-18 pl20">
              <FormItem hasFeedback>
                <Input type="text" {...emailProps}   className="input long-input" maxLength="50" autoComplete="off" placeholder="请输入邮箱"  />
              </FormItem> 
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...validCodeProps}   type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
            <img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" />
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">邮箱验证码</div>
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
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
  },
  settimefun(){
    window.location.href = "/member/baseInfo/index.html";
  },
  setTab(index){
    $(".getpwd-main ul li").removeClass('on');
    $(".getpwd-main ul li").eq(index).addClass('on');
  },
  componentWillMount(){
    this.setTab(1);
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
      values["type"] = this.props.checktype;
      values["pathWay"] = this.props.data.pathWay;
      values["imgValidCode"] = this.props.data.imgValidCode;
      values["dynamicValidCode"] = this.props.data.dynamicValidCode;
      $.ajax({
        url: '/user/retrievepwd/confirm.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
          .done(function(data) {
            if(data.result){$("#modifyEmail").remove();
              
              self.setTab(2);
              ReactDOM.render(<Success callback={self.settimefun} link={"/member/baseInfo/index.html"} linkname={"登录页面"} time={3} text={"恭喜你，密码重设成功"} />, document.getElementById("modifyEmail1"));

            }
            else
            {
              error(data.msg);
            }
          })
          .fail(function() {
            //error('网络异常，请重试！');
          });
    });

  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const pwdProps = getFieldProps('password', {
      validate: [{
          rules: [
            { required: true, whitespace: true, message: '请填写密码' },
            { validator:ValidateFn.regexPW },
            { validator: this.checkPass }
          ],trigger: ['onBlur'],    
        }]      
    });
    const rePwdProps = getFieldProps('confirmPassword', {
      rules: [
        { required: true, whitespace: true, message: '请再次输入密码' },
        { validator: this.checkPass2 }
      ],trigger: ['onBlur']
    });
    return (
      <div className="account-main">                  
        <div className="modifyEmail" id="modifyEmail" style={{padding:"60px 0 0 355px"}}>
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">设新密码</div>
                  <div className="col-18 pl20">
                     <FormItem hasFeedback>
                      <Input {...pwdProps} type="password" maxLength="16" placeholder="不少于8位，至少包含一个字母和一个数字" />
                    </FormItem>                               
                  </div>
                </div>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">确认新密码</div>
                  <div className="col-18 pl20">
                     <FormItem hasFeedback>
                      <Input {...rePwdProps} type="password" maxLength="16" placeholder="不少于8位，至少包含一个字母和一个数字" />
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
        <div id="modifyEmail1" style={{'paddingTop':'80px'}}></div>
      </div>
    );
  }
});

Step_second = createForm()(Step_second);

let ModifyPwd = React.createClass({
  getInitialState(){
    return {
      type: '1',
      data:"",
      imgsrc:'/validimg.html',
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
      values["type"] = this.state.type;
      $.ajax({
        url: '/user/retrievepwd/validation.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.props.set(self.state.type,data);
        }
        else
        {
          error(data.msg);
          $(".codeimg")[0].click();
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
      console.log("error");
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
    const componentArr = [<Phone form={this.props.form}/>, <Email form={this.props.form}/>];
    let html = this.backArrValHander(componentArr, parseInt(this.state.type)-1);

    return (
      <div className="account-main" style={{padding:"60px 0 0 355px"}}>                  
        <div className="modifyEmail">
            <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="row" style={{width:400}}>
                  <div className="col-6 lab form-laber textR">找回方式</div>
                  <div className="col-18 pl20">
                    <FormItem>                        
                      <Select onChange={this.typeChangeHander} defaultValue="1" style={{ width: '100%' }}>
                        <Option value="1">通过手机号码</Option>
                        <Option value="2">通过邮箱</Option>                           
                      </Select>
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
ModifyPwd = createForm()(ModifyPwd);

let SetPwd = React.createClass({
  getInitialState(){
    return {
      flag:true,
      checkType:''
    }
  },
  set(e,data){
    this.setState({
      flag:false,
      checkType:e,
      data:data
    })
  },
  render(){
    let html = this.state.flag ? <ModifyPwd set = {this.set}/> : <Step_second checktype={this.state.checkType} data = {this.state.data} />;
    return (
      <span>{html}</span>
    )
  }  
});

ReactDOM.render(<SetPwd />,  document.getElementById("j-pwd"));