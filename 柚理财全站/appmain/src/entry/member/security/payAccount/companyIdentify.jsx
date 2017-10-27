import '../../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../../component/accountmenu/AccountVouchMenu';
import  { ValidateFn } from '../../../../common/validateFn';
import  { Reg } from '../../../../common/regularFun';
import {Publiclib} from '../../../../common/common';
import './companyIdentify.less';
import { Button, Form, Input,Modal,Checkbox} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

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

let CompanyAllInfo = React.createClass({
    getInitialState() {
        return {
            visible:false
        };
    },
    handleOk() {
        this.setState({ visible: false });
        window.location.reload();
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
            console.log('Submit!!!');
            console.log(values);
            this.setState({ visible: true });
        });

    },
    companyName(rule, value, callback) {
        if(!value){
            callback();
        }else if(Reg.isCompanyName(value)){
            callback([new Error('公司名称为2-30位,仅包含中文、（）' )]);
        }else{
            callback();
        }
    },
    render(){
        const {getFieldProps, getFieldError, isFieldValidating} = this.props.form;

        const companyNameProps = getFieldProps('companyName', {
            validate: [
                {
                    rules: [
                        {required: true, min: 2, message: '公司名称为2-30位,仅包含中文、（）'},
                    ], trigger: 'onBlur'
                },{
                    rules: [
                        {validator: this.companyName}
                    ], trigger: 'onBlur'
                }
            ]
        });

        const bussinessCodeProps = getFieldProps('bussinessCode', {
            validate: [{
                rules: [
                    {required: true, len: 15, message: '请输入15位营业执照注册号'}
                ], trigger: 'onBlur'
            }]
        });
        const orgCodeProps = getFieldProps('orgCode', {
            validate: [{
                rules: [
                    {required: true, len: 9, message: '请填写正确的组织机构代码，格式XXXXXXXXX'}
                ], trigger: 'onBlur'
            }]
        });

        const returnUrlProps = getFieldProps("returnUrl", {
            //initialValue:"/manage/security/realNameIdentify.html?type=success"
            initialValue: "/member/security/realNameIdentify.html"
        });
        const legalPersonProps = getFieldProps('legalPerson', {
            rules: [
                {required: true, min: 2, message: '用户名至少为 2 个字符'},
                {validator: ValidateFn.regexChnName}
            ],
        });
        const idCardProps = getFieldProps('certNo', {
            validate: [{
                rules: [
                    {required: true, message: '请填写您的身份证号码'},
                ],
                trigger: 'onBlur',
            }, {
                rules: [
                    {validator: ValidateFn.regexIdCard},
                ],
                trigger: ['onBlur'],
            }],
        });
        return (
            <div>
                <Form horizontal className="form-themes" target="_blank"
                      action="/member/security/tppCompanyRegister.html" method="post" form={this.props.form}
                      onSubmit={this.handleSubmit}>

                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">企业名称</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                                help={isFieldValidating('companyName') ? '校验中...' : (getFieldError('companyName') || []).join(', ')}
                            >
                                <Input {...companyNameProps} name="companyName" maxLength="30"
                                       placeholder="请填写营业执照上的公司全称"/>
                            </FormItem>
                        </div>
                    </div>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">营业执照注册号</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                                help={isFieldValidating('bussinessCode') ? '校验中...' : (getFieldError('bussinessCode') || []).join(', ')}
                            >
                                <Input {...bussinessCodeProps} name="bussinessCode" maxLength="15"
                                       placeholder="请输入15位营业执照注册号"/>
                            </FormItem>
                        </div>
                    </div>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">组织机构代码</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                                help={isFieldValidating('orgCode') ? '校验中...' : (getFieldError('orgCode') || []).join(', ')}
                            >
                                <Input {...orgCodeProps} name="orgCode" maxLength="9"
                                       placeholder="请填写组织机构代码,格式XXXXXXXXX"/>
                            </FormItem>
                        </div>
                    </div>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">法定代表</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                            >
                                <Input {...legalPersonProps} name="legalDelegate" maxLength="10" placeholder="请填写法定代表"/>
                            </FormItem>
                        </div>
                    </div>

                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">法定代表身份证号</div>
                        <div className="col-17 pl20">
                            <FormItem hasFeedback><Input {...idCardProps} name="certNo" maxLength='18'
                                                         placeholder="请填写法定代表身份证号"/></FormItem>
                        </div>
                    </div>

                    <Input {...returnUrlProps} name="returnUrl" type="hidden"/>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7">&nbsp;</div>
                        <div className="col-17 pl20">
                            <FormItem><Button className="submit-button" type="primary"
                                              htmlType="submit">下一步</Button></FormItem>
                        </div>
                    </div>
                </Form>
                <Modal ref="modal"
                       visible={this.state.visible}
                       title="开通托管账户"
                       width={480}
                       onCancel={this.handleCancel}
                       footer={[
                           <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                               开户申请成功
                           </Button>,
                           <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>开户申请失败</Button>,
                       ]}
                >
                    <div className="modal-content"><em className="iconfont">&#xe62e;</em>请在新打开页面中进行开户操作</div>
                </Modal>
            </div>
        )
    }
    })

CompanyAllInfo = createForm()(CompanyAllInfo);

let CompanyMinInfo = React.createClass({
    getInitialState() {
        return {
            visible:false
        };
    },
    handleOk() {
        this.setState({ visible: false });
        window.location.reload();
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
            console.log('Submit!!!');
            console.log(values);
            this.setState({ visible: true });
        });

    },
    companyName(rule, value, callback) {
        if(!value){
            callback();
        }else if(Reg.isCompanyName(value)){
            callback([new Error('公司名称为2-30位,仅包含中文、（）' )]);
        }else{
            callback();
        }
    },
    render(){
        const {getFieldProps, getFieldError, isFieldValidating} = this.props.form;

        const companyNameProps = getFieldProps('companyName', {
            validate: [
                {
                    rules: [
                        {required: true, min: 2, message: '公司名称为2-30位,仅包含中文、（）'},
                    ], trigger: 'onBlur'
                },{
                    rules: [
                        {validator: this.companyName}
                    ], trigger: 'onBlur'
                }
            ]
        });

        const creditCode = getFieldProps('creditCode', {
            validate: [{
                rules: [
                    {required: true, len: 18, message: '请填写18位统一社会信用代码'}
                ], trigger: 'onBlur'
            }]
        });

        const returnUrlProps = getFieldProps("returnUrl", {
            //initialValue:"/manage/security/realNameIdentify.html?type=success"
            initialValue: "/member/security/realNameIdentify.html"
        });
        const legalPersonProps = getFieldProps('legalPerson', {
            rules: [
                {required: true, min: 2, message: '用户名至少为 2 个字符'},
                {validator: ValidateFn.regexChnName}
            ],
        });
        const idCardProps = getFieldProps('certNo', {
            validate: [{
                rules: [
                    {required: true, message: '请填写您的身份证号码'},
                ],
                trigger: 'onBlur',
            }, {
                rules: [
                    {validator: ValidateFn.regexIdCard},
                ],
                trigger: ['onBlur'],
            }],
        });
        return (
            <div>
                <Form horizontal className="form-themes" target="_blank"
                      action="/member/security/tppCompanyRegister.html" method="post" form={this.props.form}
                      onSubmit={this.handleSubmit}>

                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">企业名称</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                                help={isFieldValidating('companyName') ? '校验中...' : (getFieldError('companyName') || []).join(', ')}
                            >
                                <Input {...companyNameProps} name="companyName" maxLength="30"
                                       placeholder="请填写营业执照上的公司全称"/>
                            </FormItem>
                        </div>
                    </div>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">统一社会信用代码</div>
                        <div className="col-17 pl20">
                            <FormItem hasFeedback>
                                <Input {...creditCode} name="creditCode" maxLength="18"
                                       placeholder="请填写18位统一社会信用代码"/>
                            </FormItem>
                        </div>
                    </div>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">法定代表</div>
                        <div className="col-17 pl20">
                            <FormItem
                                hasFeedback
                            >
                                <Input {...legalPersonProps} name="legalDelegate" maxLength="10" placeholder="请填写法定代表"/>
                            </FormItem>
                        </div>
                    </div>

                    <div className="row" style={{width: 400}}>
                        <div className="col-7 lab form-laber textR">法定代表身份证号</div>
                        <div className="col-17 pl20">
                            <FormItem hasFeedback><Input {...idCardProps} name="certNo" maxLength='18'
                                                         placeholder="请填写法定代表身份证号"/></FormItem>
                        </div>
                    </div>

                    <Input {...returnUrlProps} name="returnUrl" type="hidden"/>
                    <div className="row" style={{width: 400}}>
                        <div className="col-7">&nbsp;</div>
                        <div className="col-17 pl20">
                            <FormItem><Button className="submit-button" type="primary"
                                              htmlType="submit">下一步</Button></FormItem>
                        </div>
                    </div>
                </Form>
                <Modal ref="modal"
                       visible={this.state.visible}
                       title="开通托管账户"
                       width={480}
                       onCancel={this.handleCancel}
                       footer={[
                           <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                               开户申请成功
                           </Button>,
                           <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>开户申请失败</Button>,
                       ]}
                >
                    <div className="modal-content"><em className="iconfont">&#xe62e;</em>请在新打开页面中进行开户操作</div>
                </Modal>
            </div>
        )
    }
})
CompanyMinInfo = createForm()(CompanyMinInfo);

let Payaccount = React.createClass({
  getInitialState() {
    return {
      visible:false,
      company:'false'
    };
  },


  changeCompany(e) {
      if(e.target.checked==false){
          this.setState({ company: true });
      }else{
          this.setState({ company: false });
      }

  },


  render(){

            return (
                <div className="account-main">
                    <div className="main-title">开通托管账户</div>
                    <p className="remind"><em className="iconfont">&#xe639;</em>为了顺利开通托管账户，请填写真实有效的企业信息</p>
                    <div className="row check mb10 mt10" style={{'marginLeft': '286px'}}>
                        <Checkbox defaultChecked={false} onChange={this.changeCompany}/>
                        我是三证合一企业
                    </div>
                    {this.state.company==false?<CompanyMinInfo/>:<CompanyAllInfo/>}
                </div>
              )
          }  
});


let Regok = React.createClass({
  reg(){
    ReactDOM.render(<Payaccount />,  document.getElementById("j-paydetailmain")); 
  },
  render(){
    const data = this.props.data;
    const content = [<dl><dt><span className="success">已经绑定汇付天下</span><s>汇付天下账户：</s>{this.props.data.tppUserCustId}</dt><dd><a href={this.props.data.tpp_login_url} target="_blank">进入汇付天下</a></dd></dl>,<dl><dt><span className="success">企业开户审核不通过</span><s>{data.auditDesc}</s></dt><dd><a href="javascript:" onClick={this.reg}>重新开户</a></dd></dl>,<dl><dt><span className="success">企业开户审核中</span><s>开户申请时间：</s>{Publiclib.formatDate(data.realNameVerifyTime,2)}</dt></dl>][data.realNameStatus-1];    

    return (
      <div className="account-main">
        <div className="main-title">托管账户</div>
        <div className="about">
          {content}
          <div className="help">
            <ol>
              <li>汇付天下托管账户系统，是汇付天下为P2P平台提供的资金托管账户体系，保障用户账户中的资金独立存放，P2P平台无权动用。</li>
              <li>采用汇付天下作为第三方支付及资金托管平台，充值、提现、投资支付等操作均在汇付天下平台上进行。</li>
              <li>因无法触碰用户资金账户，为了您的资产安全，请牢记汇付天下账户的登录密码及支付密码，并定期修改。</li>
              <li>请勿使用他人身份信息进行汇付天下账户开户、充值、提现及投资等操作。</li>
            </ol>
          </div>
        </div>
      </div>              
      )
  }
});

function getinfo(){
    $.ajax({
      url: '/member/security/getIdentifyResult.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data){
      if(data.result == false){
        error(data.msg);
      }
      else
      {
        
        if(data.realNameStatus != "0"){
          ReactDOM.render(<Regok data={data}/>,  document.getElementById("j-paydetailmain"));
        }
        else
        {
          ReactDOM.render(<Payaccount />,  document.getElementById("j-paydetailmain"));     
        } 

      }  
    })
    .fail(function() {
      error('网络异常，请重试！');
    });
}

getinfo();

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"7"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
} 