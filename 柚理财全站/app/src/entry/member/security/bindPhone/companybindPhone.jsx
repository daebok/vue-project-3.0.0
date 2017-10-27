import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import CountDown from '../../../../component/countDown/CountDownComponent';
import Success from '../../../../component/success/success';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import './bindPhone.less';
import { Button, Select, Form, Modal, Input} from 'antd';
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

//通过手机验证
let Phone = React.createClass({
    getInitialState() {
      return {
        timeflag:false
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
        url: '/member/security/doModifyPhone.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
           $("#j-select").remove();
           ReactDOM.render(<Step_second checkType = {self.props.checkType} modifyPhoneSign={data.modifyPhoneSign}/>,  document.getElementById("j-step-second"));
           $(".showbox").hide();
           $("#j-step-second").show();      
        }
        else
        {
          error(data.msg);
          $("#countdown_phone")[0].click();
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });
  }, 
  getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target;
    $.ajax({
      url: '/member/security/modifyPhoneCode.html',
      type: 'POST',
      dataType: 'json',
      data: {checkType: '01'},
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
    this.refs.countdownbtn.innerHTML="点击获取验证码";
  },
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;  
    const codeProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    const checkTypeProps = getFieldProps("checkType",{
        initialValue:this.props.checkType
    });    
    return (
      <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">已绑定手机</div>
          <div className="col-18 pl20">
          <p className="txt">{this.props.mobile}</p>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_phone">点击获取验证码</button>
          </div>
        </div>
        <Input {...checkTypeProps} type="hidden" />
          <div className="row" style={{width:400}}>
            <div className="col-6">&nbsp;</div>
            <div className="col-18 pl20">
              <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
            </div>
          </div>                               
      </Form>
    );
  }
});


//通过密保问题+身份证验证
let PwdQuestion = React.createClass({
    getInitialState() {
      return {
        questionList:"",
        questionListNow:"",
        questionFlag:"",
        questionFlagNow:"",
        questionListSize:"",
        questionListNowNum:"",
        timeflag:false
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
        url: '/member/security/doModifyPhone.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
           $("#j-select").remove();
           ReactDOM.render(<Step_second checkType = {self.props.checkType} modifyPhoneSign={data.modifyPhoneSign}/>,  document.getElementById("j-step-second"));
           $(".showbox").hide();
           $("#j-step-second").show();                   
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
  getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target;
    $.ajax({
      url: '/member/security/modifyPhoneCode.html',
      type: 'POST',
      dataType: 'json',
      data: {checkType: '03'},
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
          ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown_email"));         
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
    this.refs.countdownbtn.innerHTML="点击获取验证码";
  },    
  render() {
    if(this.state.questionList == ""){
      return false;
    }
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;       
    const answerProps = getFieldProps('answer', {
      rules: [
        { required: true, whitespace: true, message: '请输入密保答案' }
        ]
    });
    const checkTypeProps = getFieldProps("checkType",{
        initialValue:this.props.checkType
    });
    const questionFlagProps = getFieldProps("questionFlag",{
        initialValue:this.state.questionFlagNow
    });    
    const codeProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });        
    return (
        <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
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
              <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_email">点击获取验证码</button>
              </div>
            </div>           
            <div className="row" style={{width:400}}>
              <div className="col-6 lab form-laber textR">密保问题</div>
              <div className="col-18 pl20 lab">                
                  <p className="fl">{this.state.questionListNow}</p><a href="javascript:;"  className="fl ml10" onClick={this.changeQuestionList}>换一个</a>
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
            <Input {...questionFlagProps} type="hidden" />             
            <Input {...checkTypeProps} type="hidden" />             
            <div className="row" style={{width:400}}>
              <div className="col-6">&nbsp;</div>
              <div className="col-18 pl20">
                <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
              </div>
            </div>                               
        </Form>       
    );
  }
});

//修改第二步
let Step_second = React.createClass({
    getInitialState() {
      return {
        timeflag:false
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
      let self = this;
      $.ajax({
        url: '/member/security/doBindPhone.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){ 
          $("#j-step-second").remove();        
          ReactDOM.render(<Success callback={self.settimefun} link={"/member/baseInfo/index.html"} linkname={"安全设置"} time={3} text={"手机修改成功"} />, document.getElementById("j-step-third"));            
        }
        else
        {
          error(data.msg);
          $("#countdown_newphone").click();
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    });
  },
  settimefun(){
    window.location.href = "/member/baseInfo/index.html";
  }, 
  getcode(e){
    if(this.state.timeflag){
      return false;
    }
    let self = this,el = e.target,mobile = this.refs.mobiles.props.value;
    if( !mobile ){
      error("请填写手机号");
    }
    else
    {
      $.ajax({
        url: '/member/security/bindPhoneCode.html',
        type: 'POST',
        dataType: 'json',
        data: {mobile:mobile},
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
            ReactDOM.render(<CountDown time = '60' text='秒后重新获取' callback = {self.callback}/>, document.getElementById("countdown_newphone"));         
          }      
      })
      .fail(function() {
        self.callback();
        error('网络异常，请重试！');
      });
    }    
  },
  callback(){
    this.setState({
      timeflag : false
    }); 
    this.refs.countdownbtn.disabled = false;
    this.refs.countdownbtn.innerHTML="点击获取验证码";
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
    const codeProps = getFieldProps('code', {
      rules: [
        { required: true, whitespace: true, message: '请输入验证码' }
        ]
    });
    const modifyPhoneSignProps = getFieldProps("modifyPhoneSign",{
        initialValue:this.props.modifyPhoneSign
    });
    const checkTypeProps = getFieldProps("checkType",{
        initialValue:this.props.checkType
    });        
    return (
      <Form horizontal className="form-themes"  form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">绑定新手机</div>
          <div className="col-18 pl20">
              <FormItem 
              hasFeedback
              help={isFieldValidating('mobile') ? '校验中...' : (getFieldError('mobile') || []).join(', ')}
              ><Input {...mobileProps} ref="mobiles" placeholder="请输入您的手机号码" maxLength="11" /></FormItem>
          </div>
        </div>
        <div className="row" style={{width:400}}>
          <div className="col-6 lab form-laber textR">验证码</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="6" autoComplete="off" placeholder="输入验证码"  />
            </FormItem>
          </div>
          <div className="col-9 pl20">
          <button type="button" className="countdown-btn" onClick={this.getcode}  ref="countdownbtn" id="countdown_newphone">点击获取验证码</button>
          </div>
        </div>
        <Input {...checkTypeProps} type="hidden" />        
        <Input {...modifyPhoneSignProps} type="hidden" />
          <div className="row" style={{width:400}}>
            <div className="col-6">&nbsp;</div>
            <div className="col-18 pl20">
              <FormItem><Button className="submit-btn" type="primary" htmlType="submit" >下一步</Button></FormItem>
            </div>
          </div>                               
      </Form>
    );
  }
});

Step_second = createForm()(Step_second);

let BindPhone = React.createClass({
  getInitialState() {
    return {
      data:"",
      selecTvalue:"",
      typelist:""
    };
  },
  typeChangeHander(value){
    this.setState({
      selecTvalue:value,
    });
    $(".showbox").hide();
    $("#form_"+value).show();
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
      self.selectHtml(data,self);
      $(".showbox").eq(0).show();
    })
    .fail(function() {
      console.log("error");
    });   
  },
  componentDidMount(){
    this.getInfo();
  },
  selectHtml(data){
    //console.log(data,self,this);
    let list = [<Option key="1" value="01">通过手机验证</Option>,<Option key="2" value="03">邮箱+密保问题</Option>],typelist = [],listvalue=[];
    if(data.mobile){
      typelist.push(list[0]);
      listvalue.push("01");
    }
    if(data.questionList.length && data.email){
      typelist.push(list[1]);
      listvalue.push("03");      
    }
    this.setState({
      selecTvalue:listvalue[0],
      typelist:typelist
    });
  },  
  render(){
    let data = this.state.data;
    if(!data){
      return false;
    }
    if(this.state.typelist == "" || this.state.selecTvalue == ""){
      return false;
    }    
    Phone = createForm()(Phone);
    PwdQuestion = createForm()(PwdQuestion);
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const componentArr = [<Phone checkType={this.state.selecTvalue} mobile = {data.mobile} />,<PwdQuestion checkType={this.state.selecTvalue} questionList={data.questionList} email = {data.email} />];
    const html_1 = componentArr[0],html_2 = componentArr[1];
  
    return (
      <div className="account-main">                  
        <div className="main-title">
          修改手机
          <a href="/member/baseInfo/index.html" className="backBtn">&lt; 返回</a>
        </div>
        <div className="bindPhone form-themes">
            <div className="row" style={{width:400}} id="j-select">
              <div className="col-6 lab form-laber textR">验证方式</div>
              <div className="col-18 pl20">        
                  <FormItem>
                  <Select onChange={this.typeChangeHander} defaultValue={this.state.selecTvalue} style={{ width: '100%' }}>
                         {
                            this.state.typelist.map(function(k,v){
                              return k;
                            })
                         }                        
                  </Select>
                  </FormItem>
              </div>
            </div>
            <div className="showbox" id="form_01" >{html_1}</div>
            <div className="showbox" id="form_03" >{html_2}</div>
            <div id="j-step-second" style={{display:"none"}}></div>
        </div>
        <div id="j-step-third"></div>
      </div>
    )
  }  
});
BindPhone = createForm()(BindPhone);
ReactDOM.render(<BindPhone />,  document.getElementById("j-bindPhonemain"));
ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));