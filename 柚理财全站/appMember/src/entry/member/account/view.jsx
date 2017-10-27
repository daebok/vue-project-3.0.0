import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import {Publiclib} from '../../../common/common';
import Userheaderimg from '../../../component/headerimg/headerimg';
import { Switch,Icon ,Modal,Tooltip,Tabs} from 'antd';
const TabPane = Tabs.TabPane;
import '../myBorrow/loan.less';
import Tablelist from '../../../component/tablelist/tablelist';
const confirm = Modal.confirm;
import './view.less';

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
Math.formatFloat = function(f, digit) {
    if(f==undefined){
        return '0.00'
    }else{
        return  f.toFixed(digit)
    }
};
//S 我的借款
const timetype = ["个月","天"];
const columns_loan = [{
            title: '借款项目',
            dataIndex: 'projectName',
            render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let url='/invest/detail.html?id='+record.id
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }
          },
          {
            title: '借款金额(元)',
            dataIndex: 'account'
          },
          {
            title: '借款年利率',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },
          },
          {
            title: '借款期限',
            dataIndex:"timeLimit",
            render: (text, record) => (
              <span>{text}{timetype[record.timeType]}</span>
              ),
          },
          {
            title: '借款时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },
          {
            title: '借款进度',
            dataIndex: 'scales',
            render(text) {
              if(!text || text == 'undefined'){text=0}
              return text+"%";
            },
          },
          {
            title: '状态',
            dataIndex: 'statusStr'
          }];
const config_loan = {single:false,datetime:false,select:false,pageSize:5};
const select_name = 'status';
// E 我的借款

//S 待还借款
const columns_repayment = [{
            title: '借款项目',
            dataIndex: 'projectName',
            render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let url='/invest/detail.html?id='+record.projectId;
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }
          },
          {
            title: '期数',
            dataIndex: 'period',
            render(text,record) {
              return text+"/"+record.periods;
            },
          },
          {
            title: '应还本金(元)',
            dataIndex: 'capital',
            render(text) {
              return Math.formatFloat(text,2);
            },
          },
          {
            title: '应还利息(元)',
            dataIndex: 'interest',
             render(text) {
              return Math.formatFloat(text,2);
            },
          },
          {
            title: '逾期罚息(元)',
            dataIndex: 'lateInterestSum',
            render(text,record) {
              return  record.lateDays == 0 ? "-" : Math.formatFloat(text,2);
            },
          },
          {
            title: '本期还款日',
            dataIndex: 'repayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },
          },
          {
            title: '状态',
            dataIndex: 'statusTypeStr'
          }];
const config_repayment = {single:false,datetime:false,select:false,pageSize:5};
const select_name1 = 'statusType';
// E 待还借款


let Viewmain = React.createClass({
  getInitialState() {
    return {
      data : this.props.data,
      isauto : this.props.data.autoTender == "1" ? true : false,
      giftshow : false,
      giftName :'',
      rateCoupon :[],
      redenvelope :[]
    };
  },
  close(){
    let self = this;
    $.ajax({
      url: '/member/closeAutoInvest.html',
      type: 'POST',
      dataType: 'json',
    })
    .done(function(data) {
        if(data.result){
           self.setState({
            isauto:false
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
  closetips(){
    let self = this;
    confirm({
      title: '您确定要关闭自动投资吗？',
      content: '',
      onOk() {
        self.close();
      },
      onCancel() {}
    });
  },
  switch(e){
    this.setState({
      isauto : !e
    });
    if(e){
      this.setState({
        isauto : true
      });
      setTimeout(function(){window.location.href = "/member/auto/setting.html";},300);
    }
    else
    {
      this.closetips();
    }
  },
    showgift(){
        let self = this;
        $.ajax({
            url: '/member/account/doVipGift.html',
            type: 'POST',
            dataType: 'json',
        })
            .done(function(data) {
                if(data.result){
                    self.setState({
                        giftshow:true,
                        giftName:data.giftName,
                        rateCoupon:data.rateCoupon,
                        redenvelope:data.redenvelope
                    })
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
    hidegift(){
       window.location.reload()
    },
  componentDidMount(){
    if($(".tppName").val() == "cbhb" && $(".userNatrue").val()=='2'){
      $(".warn-row").hide();
    }else{
      if(!this.state.data.riskLevel){
        $(".warn-row").show();
        $(".warn-row .close").click(function(event) {
          $(".warn-row").animate({
            height: 0},
            600, function() {
            $(this).hide();
          });
        });
      }
    }

  },
  render(){
            const datas = this.state.data;
            let date = "暂无";
            let vipicon="";
            let tppName = $(".tppName").val();
            let userNatrue = $(".userNatrue").val();
            if(datas.nextRepayDate){
              date = Publiclib.formatDate(datas.nextRepayDate);
            }
            const VIPLevel = [<em className="iconfont">&#xe669;</em>,<em className="iconfont">&#xe63b;</em>,<em className="iconfont">&#xe641;</em>,<em className="iconfont">&#xe642;</em>,<em className="iconfont">&#xe640;</em>,<em className="iconfont">&#xe63c;</em>,<em className="iconfont">&#xe63d;</em>,<em className="iconfont">&#xe63e;</em>,<em className="iconfont">&#xe63f;</em>,<em className="iconfont">&#xe643;</em>];
            if(tppName =="cbhb" && userNatrue=='2'){
              vipicon="";
            }else{
              vipicon = VIPLevel[datas.VIPLevel];
            }


            let mobileTips = <span>您已完成手机绑定 {datas.session_user.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")} <a href="/member/security/modifyPhone.html">修改</a></span>;

            let emailTips = datas.session_user_identify.emailStatus == "1" ? <span>您已完成邮箱绑定 {datas.session_user.hideEmail} <a href="/member/security/modifyEmail.html">修改</a></span> : <span>绑定邮箱，使您的账户更加安全 <a href="/member/security/bindEmail.html">立即绑定</a></span>;

            let realnameTips = datas.session_user_identify.realNameStatus == "1" ? <span>您已开通托管账户 <a href="/member/security/realNameIdentify.html">点此登录</a></span> : <span>保障账户安全，只有开通托管账户才能投资 <a href="/member/security/realNameIdentify.html">立即开通</a></span>;

            let mobile = <Tooltip placement="bottom" title={mobileTips}><em className={datas.session_user_identify.mobilePhoneStatus == "1" ? "iconfont statusok" : "iconfont"}>&#xe61e;</em></Tooltip>,
             email=<Tooltip placement="bottom" title={emailTips}><em className={datas.session_user_identify.emailStatus == "1" ? "iconfont statusok email" : "iconfont email"}>&#xe61d;</em></Tooltip>,
             realname = <Tooltip placement="bottom" title={realnameTips}><em className={datas.session_user_identify.realNameStatus == "1" ? "iconfont statusok header" : "iconfont header"}>&#xe644;</em></Tooltip>,

            //  riskLevel =  <span className="type">{datas.riskLevel}</span>;
            riskLevel="";

             // if(datas.session_user_identify.mobilePhoneStatus != "1")
             // {
             //    mobile = <a href="#">{mobile}</a>;
             // }
             if(datas.session_user_identify.emailStatus != "1")
             {
                email = <a href="/member/security/bindEmail.html">{email}</a>;
             }
             var nameText='';
             if(datas.session_user_identify.realNameStatus != "1")
             {
                realname = <a href="/member/security/realNameIdentify.html">{realname}</a>;
                nameText = datas.session_user.userName
             }else{
                nameText = datas.session_user.realName
             }
             let userheaderimg = $('#header').attr('data-url') + datas.avatar_photo;
             if(!datas.avatar_photo){
                userheaderimg = $('#header').attr('data-url') + "/data/img/avatar/default_portrait.jpg";
             }
             if(tppName =="cbhb" && userNatrue=='2'){
               riskLevel="";
             }else{
               if(!datas.riskLevel){
                  riskLevel=<a href="/member/risk/userRiskPapers.html">风险评测</a>
               }else{
                 riskLevel =  <span className="type">{datas.riskLevel}</span>;
               }
             }

             if(datas.session_user_identify.realNameStatus != "1") {

             }


              let html='';
              let display = tppName =='cbhb'&& userNatrue=='2' ?'block':'none';
              // let tppNameDom = tppName!="cbhb" ? <span className="fr auto"><span className="auto-name">自动投资：</span>{this.state.isauto ? "开" : "关"} <a href="/member/auto/setting.html">设置</a></span> : "";

              if(tppName =="cbhb" && userNatrue=='2'){ //如果是企业用户+渤海银行
                html=<div>
                  <div className="profit viewlist">
                    <div className="title">我的借款</div>
                    <dl className="loan">
                      <dt>借款<a href="/member/myLoan/list.html">详情></a></dt>
                      <dd className="title-sm">借款总额</dd>
                      <dd className="number">{datas.totalBorrowAmount ? datas.totalBorrowAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="repayment">
                      <dt>还款</dt>
                      <dd className="title-sm">待还总额</dd>
                      <dd className="number">{datas.totalRepayAmount ? datas.totalRepayAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="nextrepayment">
                      <dt className="clearfix"><a href="/member/myRepayment/list.html">详情></a></dt>
                      <dd className="title-sm">下一笔待还<span className="date">{date}</span></dd>
                      <dd className="number"><s>{datas.nextRepayAmount ? datas.nextRepayAmount.toFixed(2) : "0.00"}</s><em>元</em><a href="/member/myRepayment/list.html" className="pay">还款</a></dd>
                    </dl>
                  </div>
                </div>
              }else if(tppName =="cbhb" && userNatrue=='1'){
                html=<div>
                  <div className="fund viewlist clearfix">
                      <div className="title">资金概览</div>
                      <dl>
                      <dt>资产总计<Tooltip placement="top" title={"可用余额+冻结金额"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                      <dd><s>{datas.account.total ? datas.account.total.toFixed(2) : "0.00"}</s><em>元</em></dd>
                      </dl>
                      <dl>
                          <dt>可用余额<Tooltip placement="top" title={"账户内可自由支配的资金"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                          <dd>{datas.account.useMoney ? datas.account.useMoney.toFixed(2) : "0.00"}<em>元</em></dd>
                      </dl>
                      <dl>
                          <dt>冻结金额<Tooltip placement="top" title={"投资中冻结金额+赎回中冻结金额+提现中冻结金额+变现中冻结金额"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                          <dd>{datas.account.noUseMoney ? datas.account.noUseMoney.toFixed(2) : "0.00"}<em>元</em></dd>
                      </dl>
                      <div className="btn">
                        <a href="/member/recharge/detail.html">充值</a><a href="/member/cash/detail.html" className="cash">提现</a>
                      </div>
                  </div>
                  <div className="payment viewlist">
                    <div className="row">
                      <div className="col-7">
                        <div className="title">收益概览</div>
                        <div className="payment-cont">
                          <dl>
                            <dt>昨日净收益<Tooltip placement="top" title={"昨日到账的收益"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                            <dd><s>{datas.yesterdayEarnAmount ? datas.yesterdayEarnAmount.toFixed(2) : "0.00"}</s><em>元</em></dd>
                          </dl>
                          <dl>
                            <dt>累计净收益<Tooltip placement="top" title={"注册以来截止到昨天的收益总和"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                            <dd>{datas.earnAmount ? datas.earnAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                          </dl>
                        </div>
                      </div>
                      <div className="col-17">
                        <div className="title">回款概览</div>
                        <div className="payment-cont" id="j-chart">

                        </div>
                      </div>
                    </div>
                  </div>
                  <div className={datas.totalBorrowAmount == 0 ? "hide" : "profit viewlist"}>
                    <div className="title">我的借款</div>
                    <dl className="loan">
                      <dt>借款<a href="/member/myLoan/list.html">详情></a></dt>
                      <dd className="title-sm">借款总额</dd>
                      <dd className="number">{datas.totalBorrowAmount ? datas.totalBorrowAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="repayment">
                      <dt>还款</dt>
                      <dd className="title-sm">待还总额</dd>
                      <dd className="number">{datas.totalRepayAmount ? datas.totalRepayAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="nextrepayment">
                      <dt className="clearfix"><a href="/member/myRepayment/list.html">详情></a></dt>
                      <dd className="title-sm">下一笔待还<span className="date">{date}</span></dd>
                      <dd className="number"><s>{datas.nextRepayAmount ? datas.nextRepayAmount.toFixed(2) : "0.00"}</s><em>元</em><a href="/member/myRepayment/list.html" className="pay">还款</a></dd>
                    </dl>
                  </div>
                </div>
              }else{
                html=<div>
                  <div className="fund viewlist clearfix">
                      <div className="title">资金概览</div>
                      <dl>
                      <dt>资产总计<Tooltip placement="top" title={"可用余额+冻结金额"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                      <dd><s>{datas.account.total ? datas.account.total.toFixed(2) : "0.00"}</s><em>元</em></dd>
                      </dl>
                      <dl>
                          <dt>可用余额<Tooltip placement="top" title={"账户内可自由支配的资金"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                          <dd>{datas.account.useMoney ? datas.account.useMoney.toFixed(2) : "0.00"}<em>元</em></dd>
                      </dl>
                      <dl>
                          <dt>冻结金额<Tooltip placement="top" title={"投资中冻结金额+赎回中冻结金额+提现中冻结金额+变现中冻结金额"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                          <dd>{datas.account.noUseMoney ? datas.account.noUseMoney.toFixed(2) : "0.00"}<em>元</em></dd>
                      </dl>
                      <div className="btn">
                        <a href="/member/recharge/detail.html">充值</a><a href="/member/cash/detail.html" className="cash">提现</a>
                      </div>
                  </div>
                  <div className="payment viewlist">
                    <div className="row">
                      <div className="col-7">
                        <div className="title">收益概览</div>
                        <div className="payment-cont">
                          <dl>
                            <dt>昨日净收益<Tooltip placement="top" title={"昨日到账的收益"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                            <dd><s>{datas.yesterdayEarnAmount ? datas.yesterdayEarnAmount.toFixed(2) : "0.00"}</s><em>元</em></dd>
                          </dl>
                          <dl>
                            <dt>累计净收益<Tooltip placement="top" title={"注册以来截止到昨天的收益总和"}><em className="iconfont">&#xe620;</em></Tooltip></dt>
                            <dd>{datas.earnAmount ? datas.earnAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                          </dl>
                        </div>
                      </div>
                      <div className="col-17">
                        <div className="title">回款概览</div>
                        <div className="payment-cont" id="j-chart">

                        </div>
                      </div>
                    </div>
                  </div>
                  <div className={datas.totalBorrowAmount == 0 ? "hide" : "profit viewlist"}>
                    <div className="title">我的借款</div>
                    <dl className="loan">
                      <dt>借款<a href="/member/myLoan/list.html">详情></a></dt>
                      <dd className="title-sm">借款总额</dd>
                      <dd className="number">{datas.totalBorrowAmount ? datas.totalBorrowAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="repayment">
                      <dt>还款</dt>
                      <dd className="title-sm">待还总额</dd>
                      <dd className="number">{datas.totalRepayAmount ? datas.totalRepayAmount.toFixed(2) : "0.00"}<em>元</em></dd>
                    </dl>
                    <dl className="nextrepayment">
                      <dt className="clearfix"><a href="/member/myRepayment/list.html">详情></a></dt>
                      <dd className="title-sm">下一笔待还<span className="date">{date}</span></dd>
                      <dd className="number"><s>{datas.nextRepayAmount ? datas.nextRepayAmount.toFixed(2) : "0.00"}</s><em>元</em><a href="/member/myRepayment/list.html" className="pay">还款</a></dd>
                    </dl>
                  </div>
                </div>

              }
            return (<div className="account-main">
                      <div className="userinfo">
                          <dl className="clearfix">
                              <dt>
                                <Userheaderimg headerimg={userheaderimg} />
                              </dt>
                              <dd className="welcome">
                              欢迎您，{nameText}<a href="/member/account/vip.html">{vipicon}</a>{riskLevel}
                              </dd>
                              <dd className="userinfo-status">
                              {mobile}{email}{realname}<span className="safelevel">安全等级：<span className="unit">{datas.securityLevel}</span></span><a href="/member/baseInfo/index.html
" className="promote">[提升]</a><span className="line">|</span>未读信息：<a href="/member/letter/letter.html" className="unread">{datas.unreadCount}</a><span className="unit">条</span>
                              </dd>
                              <span className={ userNatrue=='1'?'auto clearfix':'auto clearfix hide'}><span className="auto-name">自动投资：</span>{this.state.isauto ? "开" : "关"} <a href="/member/auto/setting.html">设置</a></span>
                              <a href="javascript:;" className={datas.vipGift == 0 ? 'Vip-gift hide': 'Vip-gift show'} onClick={this.showgift}><i className="iconfont">&#xe654;</i>领取<strong>VIP</strong>礼包</a>
                              <div className={ this.state.giftshow?'shadow show':'shadow hide'}>
                              </div>
                                  <div className={ this.state.giftshow?'gift-box show':'gift-box hide'}>
                                      <div className="top">
                                          <h3>{this.state.giftName}</h3>
                                          <i className="btn-cancel" onClick={this.hidegift}></i>
                                      </div>
                                      <div className="list">
                                          <ul className="clearfix">
                                              {this.state.redenvelope.map(function (name) {
                                                  return <li><h4>红包</h4><span><em>￥</em>{name}</span></li>
                                              })}
                                              {this.state.rateCoupon.map(function (name) {
                                                  return  <li className="jx"><h4>加息券</h4><span>{name}<em>%</em></span></li>
                                              })}
                                          </ul>
                                      </div>
                                      <div className="bottom">
                                          <a href="/invest/index.html" className="btn">赶快去投资吧</a>
                                      </div>
                                  </div>

                          </dl>
                      </div>
                      {html}
                      <div className="listTab" style={{display}}>
                             <Tabs defaultActiveKey="1">
                               <TabPane tab="我的借款" key="1">
                                   <a href="/member/myLoan/list.html" className="more">详情></a>
                                   <Tablelist config={config_loan} url='/member/account/getProjectList.html' tableid="tablelist-first" columns={columns_loan} selectname={select_name}/>
                               </TabPane>
                               <TabPane tab="待还借款" key="2">
                                   <a href="/member/myRepayment/list.html" className="more">详情></a>
                                   <Tablelist config={config_repayment} url='/member/account/getProjectRepayList.html' tableid="tablelist-second" columns={columns_repayment} selectname={select_name1} />
                               </TabPane>
                             </Tabs>
                      </div>

                  </div>)
          }
});

let getinfo = function(){
    $.ajax({
      url: '/member/account/getAccountInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
        if( !data.result ){
            error(data.msg,data.url);
            return false;
        }
        ReactDOM.render(<Viewmain data={data} />,  document.getElementById("j-viewmain"));

        let option = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    //name: "月份",
                    data : data.collectMonth,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
            
                {
                    name:'待收总额',
                    type:'bar',
                    stack:'one',
                    barWidth: '60%',
                    data:data.collectData,
                    itemStyle:{
                      normal:{
                        color:'#66bef5'
                      }
                    }
                },{
                    name:'已收金额',
                    type:'bar',
                    stack:'one',
                    barWidth: '60%',
                    data:data.repaidData,
                    itemStyle:{
                      normal:{
                        color:'#ff814a'
                      }
                    }
                }
            ]
        };


      var myChart = echarts.init(document.getElementById('j-chart'));
      myChart.setOption(option);
    })
    .fail(function() {
      //error('网络异常，请重试！');
    });
}

getinfo();


ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));
