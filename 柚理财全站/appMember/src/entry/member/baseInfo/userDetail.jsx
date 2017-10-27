import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import Right from '../../../component/icon/right';
import Error from '../../../component/icon/error';
import Safeinfo from '../../../component/safeinfo/safeinfo';
import Safeinfo02 from '../../../component/safeinfo/safeinfo02';
import SafeinfoAuto from '../../../component/safeinfo/safeinfoAuto';
import Personageinfoshow from './personageinfoshow';
import Personageinfoedit from './personageinfoedit';
import CertificateInfo from './certificateInfo';
import Userheaderimg from '../../../component/headerimg/headerimg';
import {Publiclib} from '../../../common/common';
import './commonDetail.less';
import { Tabs,Slider,Modal} from 'antd';
const TabPane = Tabs.TabPane;

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

function callback(key) {
  //console.log(key);
}
class Baseinfo extends React.Component{
  constructor(props){
    super(props);
    this._getAjaxData = this.getAjaxData.bind(this);
    this._changePersonageinEditHander = this.changePersonageinEditHander.bind(this);
    this._changePersonageinSaveHander = this.changePersonageinSaveHander.bind(this);
    this.state = {
      data : null,
      personageinfoStyle : 'save',
    }
  }

  getAjaxData(e) {
    let self = this;
    $.ajax({
      url: '/member/baseInfo/getIdentifyInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      if( data.result ){
        self.setState({data: data});
        $("body").data("url",$("#header").data("url"));
      }
      else
      {
        error(data.msg,data.url);
      }
    })
    // .fail(function() {
    //   error('网络异常，请重试！');
    // })

  }
  componentDidMount() {
    this._getAjaxData();
  }
  changePersonageinEditHander(){
    this.setState({personageinfoStyle: 'edit'});
  }
  changePersonageinSaveHander(){
    this.setState({personageinfoStyle: 'save'});
  }
  render(){
    let data = this.state.data;
    //第一次加载没有数据，不加载
    if(!data){
      return false;
    }
    let realNameStatus = parseInt(data.userIdentify.realNameStatus);          //托管账号状态
    let emailStatus = parseInt(data.userIdentify.emailStatus);                //邮箱绑定状态
    let mobilePhoneStatus = parseInt(data.userIdentify.mobilePhoneStatus);    //手机绑定状态
    let pwdQuestStatus = parseInt(data.hasPwdQuest);                          //密保问题状态
    let authSignStatus = parseInt(data.userIdentify.authSignStatus);          //业务授权状态
    let payPwdStatus = parseInt(data.payPwd);          //业务授权状态
    let tppNameData = data.tppName;                                           //托管方
    let linkname = [],str1=[],str2=[],str3=[],riskLevel = 3,link='';
    if( realNameStatus != 1 ){
      linkname[0] = 2;
      str1[0] = '开通托管账户后，才能进行投资借款等资金业务。';
    }
    else{
        linkname[0] = 7;
        str1[0] = '您认证的'+tppNameData+'账号为：';
        str2[0] = data.tppUserCustId;
    }

    if( payPwdStatus != 1 ){
      linkname[10] = 0;
      str1[10] = '开通交易密码后，才能进行投资借款等资金业务。';
      link = '/member/security/setPwd.html';
    }
    else{
        linkname[10] = 1;
        str1[10] = '您的交易密码已设置';
        link = '/member/security/resetPwd.html';
    }

    if( emailStatus != 1 ){
      str1[1] = '绑定邮箱后，可及时获取账户资金变动通知和投资讯息';
    }
    else
    {
      str1[1] = '您绑定的邮箱：';
      str2[1] = data.email;
    }
    if(data.riskLevel){
      str2[5] = data.riskLevel; //风险承受能力类型
      str1[5] = "您的风险承受能力：";
    }
    else
    {
      str2[5] = "";
      riskLevel = 8;
      str1[5] = "根据风险评测结果选择您适合的投资项目。";
    }

    //个人信息保存与修改
    let personageinfo = this.state.personageinfoStyle == 'save' ? <Personageinfoshow changePersonageinHander = {this._changePersonageinEditHander}/> : <Personageinfoedit  changePersonageinHander = {this._changePersonageinSaveHander}/>;

    //托管账号跳转

    //邮箱跳转链接
    let emailLink = ["/member/security/bindEmail.html","/member/security/modifyEmail.html"][emailStatus];
    //交易密码跳转链接
    let payPwdlLink = ["/member/security/setPwd.html","/member/security/modifyTransactionPwd.html"][payPwdStatus];
    let targetStyle = ["_blank",""][payPwdStatus];
    //密保跳转链接
    //let pwdQuestLink = ["/member/security/setPwdQuestion.html","/member/security/pwdQuestion.html"][pwdQuestStatus];
    let pwdQuestLinkName = pwdQuestStatus==0 ? (pwdQuestStatus+4) : (pwdQuestStatus);
    //头像
    let headPortrait = $("#header").data("url") + (data.avatar_photo || "/data/img/avatar/default_portrait.jpg");
    //上次登录时间
    let loginTime = data.lastLoginTime;
    //安全等级提示信息
    let securityValueText = "";
    let ValueType=''
    if(data.securityLevel == "高"){
      securityValueText = "太棒了，您可以任性地投资或融资了";
      ValueType='class3'
    } else if(data.securityLevel == "中"){
      securityValueText = "安全等级还能提升，快去进行更多安全设置";
      ValueType='class2'
    } else if(data.securityLevel == "低"){
      securityValueText = "安全等级较低，快去进行更多安全设置";
      ValueType='class1'
    }

    if(!!loginTime){
      loginTime = "上次登录时间："+Publiclib.formatDate(loginTime, 2);
    }

    let tppName = $(".tppName").val();
    let userNatrue = $(".userNatrue").val();
    let riskDom = '';
    let tppNameDom = tppName!="cbhb" ? <SafeinfoAuto status={authSignStatus} infoname='业务授权' link={authSignStatus == "1" ? 'javascript:void(0)' : '/member/security/bindAuthorization.html'} str1='业务授权是方便资金托管方在本平台进行相关资金操作，如投资、借款等' linkname={authSignStatus == "1" ? 6 : 5} /> : <Safeinfo status={true} status={true}infoname='交易密码' link='/member/security/modifyCbhbPass.html' str1='保障用户交易安全' linkname={1}/>;
    if(tppName !="cbhb" && userNatrue!='2'){
      riskDom = <Safeinfo status={data.riskLevel ? true : false} infoname='风险承受能力' link='/member/risk/userRiskPapers.html' str1={str1[5]} str2={str2[5]} linkname={riskLevel} />
    }
    return(
      <div className="baseinfo">
        <div className="baseinfo-header">
          <div className="userpic">
            <Userheaderimg headerimg={headPortrait} />
          </div>
          <dl className="user-grade">
            <dt><h3>您的账户安全等级：{data.securityLevel}</h3></dt>
            <dd className={ValueType}><Slider defaultValue={data.securityValue} disabled/><span className="text">{securityValueText}</span></dd>
            <dd><span className="timer">{loginTime}</span></dd>
          </dl>
        </div>
        <div className="baseinfo-content">
          <Tabs defaultActiveKey="1" onChange={callback}>
            <TabPane tab="安全设置" key="1">
              <ul className="setinfo">
                <Safeinfo status={realNameStatus} infoname='托管账户' link='/member/security/realNameIdentify.html' str1={str1[0]} str2={str2[0]} linkname={linkname[0]}/>
                <Safeinfo02 status={payPwdStatus} infoname='交易密码' link={payPwdlLink} target={targetStyle} str1={str1[10]} linkname={payPwdStatus}/>
                <Safeinfo status={emailStatus} infoname='邮箱绑定' link={emailLink} str1={str1[1]} str2={str2[1]} linkname={emailStatus} />
                <Safeinfo status={mobilePhoneStatus} infoname='手机绑定' link='/member/security/modifyPhone.html' str1='您绑定的手机：' str2={data.mobile} str3='。若已丢失或停用，请立即更换，避免账户被盗' linkname={mobilePhoneStatus} />
                <Safeinfo status={true} infoname='登录密码' link='/member/security/modifyPwd.html' str1='互联网账号存在被盗风险，建议您定期更改密码以保护账户安全' linkname={1} />
                <Safeinfo status={pwdQuestStatus} infoname='密保问题' link='/member/security/pwdQuestion.html' str1='保障个人隐私，修改个人信息等操作时使用' linkname={pwdQuestLinkName} />
                {riskDom}
                {tppNameDom}
              </ul>
            </TabPane>
            <TabPane tab="个人信息" key="2">
              <div className="personageinfo" id="personageinfo">
                {personageinfo}
              </div>
            </TabPane>
            <TabPane tab="借款资质" key="3">
              <CertificateInfo />
            </TabPane>
          </Tabs>
        </div>
      </div>
    );
  }
}
ReactDOM.render(<Baseinfo />,  document.getElementById("j-baseinfomain"));
ReactDOM.render(<Accountmenu current = {"9"}/>,  document.getElementById("j-sider-menu"));
