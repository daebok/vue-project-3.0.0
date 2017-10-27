import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import {Publiclib} from '../../../common/common';
import { Icon ,Select, Modal, Tooltip } from 'antd';
const Option = Select.Option;
import './vouchview.less';

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

let Viewmain = React.createClass({
  getInitialState() {
    return {
      data: this.props.data,
    };
  },
  render(){
            const datas = this.state.data;
            const isauto = datas.autoTender === 1 ? true : false; //1是自动投资
            let date = "暂无";
            if(datas.nextRepayDate){
              date = Publiclib.formatDate(datas.nextRepayDate);
            }
            const VIPLevel = [<em></em>,<em className="iconfont">&#xe63b;</em>,<em className="iconfont">&#xe641;</em>,<em className="iconfont">&#xe642;</em>,<em className="iconfont">&#xe640;</em>,<em className="iconfont">&#xe63c;</em>,<em className="iconfont">&#xe63d;</em>,<em className="iconfont">&#xe63e;</em>,<em className="iconfont">&#xe63f;</em>,<em className="iconfont">&#xe643;</em>];
          let mobileTips = <span>您已完成手机绑定 {datas.session_user.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")} <a href="/member/security/modifyPhone.html">修改</a></span>;

          let emailTips = datas.session_user_identify.emailStatus == "1" ? <span>您已完成邮箱绑定 {datas.session_user.email.replace(/(\w{2})\w*(\w{2})/,"$1****$2")} <a href="/member/security/modifyEmail.html">修改</a></span> : <span>绑定邮箱，使您的账户更加安全 <a href="/member/security/bindEmail.html">立即绑定</a></span>;

          let realnameTips = datas.session_user_identify.realNameStatus == "1" ? <span>您已开通托管账户 <a href="/member/security/realNameIdentify.html">点此登录</a></span> : <span>保障账户安全，只有开通托管账户才能投资 <a href="/member/security/realNameIdentify.html">立即开通</a></span>;

          let mobile = <Tooltip placement="bottom" title={mobileTips}><em className={datas.session_user_identify.mobilePhoneStatus == "1" ? "iconfont mobile-statusok" : "iconfont"}>&#xe61e;</em></Tooltip>,
              email=<Tooltip placement="bottom" title={emailTips}><em className={datas.session_user_identify.emailStatus == "1" ? "iconfont email-statusok email" : "iconfont email"}>&#xe61d;</em></Tooltip>,
              realname = <Tooltip placement="bottom" title={realnameTips}><em className={datas.session_user_identify.realNameStatus == "1" ? "iconfont realname-statusok header" : "iconfont header"}>&#xe644;</em></Tooltip>


          if(datas.session_user_identify.mobilePhoneStatus != "1")
             {
                mobile = <a className="mobile" href="#">{mobile}</a>;
             } 
             if(datas.session_user_identify.emailStatus != "1")
             {
                email = <a className="email" href="/member/security/bindEmail.html">{email}</a>;
             }
              var nameText='';
              if(datas.session_user_identify.realNameStatus != "1")
              {
                  realname = <a href="/member/security/realNameIdentify.html">{realname}</a>;
                  nameText = datas.session_user.userName
              }else{
                  nameText = datas.session_user.realName
              }
             let userheaderimg = datas.image_server_url + datas.avatar_photo;
             if(!datas.avatar_photo){
                userheaderimg = datas.image_server_url + "/data/img/avatar/default_portrait.jpg";
             }

            return (<div className="account-main">
                      <div className="userinfo">
                          <dl className="clearfix">
                              <dt>
                                <span className="companyheaderimg"><a href="#"><img src={datas.logo ? datas.logo : "http://www.atool.org/include/placeholder_cache/35b1fc02d13cc0dd340aab4aec5ada5a.png"} /></a></span>
                              </dt>
                              <dd className="welcome">
                              {nameText}
                              </dd>
                              <dd className="userinfo-status">
                              {mobile}{email}{realname}<span className="safelevel">安全等级：<span className="unit">{datas.securityLevel}</span></span><a href="/member/baseInfo/index.html" className="up" >[提升]</a></dd>
                          </dl>
                      </div>
                      <div className="fund viewlist clearfix">
                      <div className="title">资金概览</div>
                          <dl>
                          <dt>
                            资产总计
                            <Tooltip title="可用余额+担保中金额" placement="bottom">
                              <em className="iconfont">&#xe620;</em>
                            </Tooltip>
                          </dt>
                          <dd><s>{(Number(datas.account.useMoney) + Number(datas.vouchAccount)).toFixed(2)}</s><em>元</em></dd>
                          </dl>
                          <ul className="text">
                            <li><strong>可用余额</strong>{datas.account.useMoney ? (datas.account.useMoney).toFixed(2) : "0.00"}<em>元</em></li>
                            <li><strong>担保中金额</strong>{datas.vouchAccount ? (Number(datas.vouchAccount)).toFixed(2) : "0.00"}<em>元</em></li>
                          </ul>
                          <div className="btn">
                            <a href="/member/recharge/vouchDetail.html">充值</a><a href="/member/cash/vouchDetail.html" className="cash">提现</a>
                          </div>                
                      </div>
                      <div className="profit viewlist">
                        <div className="title">担保概览</div>
                        <dl className="loan loan-bg1">
                          <dt>待审核项目<a href="/member/vouch/verify.html">详情></a></dt>
                          <dd className="number">{datas.toAuditCount}<em>个</em></dd>
                        </dl>
                        <dl className="loan loan-bg2">
                          <dt>担保中项目<a href="/member/vouch/vouchProject.html">详情></a></dt>
                          <dd className="number">{datas.auditingCount}<em>个</em></dd>
                        </dl>
                        <dl className="loan last loan-bg3">
                          <dt>待垫付项目<a href="/member/vouch/overdue.html">详情></a></dt>
                          <dd className="number">{datas.toAdvanceCount}<em>个</em></dd>
                        </dl>                                                                        
                      </div>
                  </div>)
          }  
});

let getinfo = function(){
    $.ajax({
      url: '/member/vouch/getAccountInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
        if( data.result != undefined && !data.result){
          if( data.url != undefined && data.url!='' ){
            error(data.msg,data.url);
          }
          else
          {
            error(data.msg);
          } 
          return false;
        }
        ReactDOM.render(<Viewmain data={data} />,  document.getElementById("j-vouchmain"));
    })
    .fail(function() {
      console.log("error");
    });
}

getinfo();

ReactDOM.render(<AccountVouchMenu current = {"1"}/>,  document.getElementById("j-sider-menu"));