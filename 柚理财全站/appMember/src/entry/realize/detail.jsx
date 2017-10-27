import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './detail.less';
import { ValidateFn } from '../../common/validateFn';
import { Reg } from '../../common/regularFun';
import { Publiclib } from '../../common/common';
//所持资产信息
import ProductInfo from './productInfo/productInfo';
//产品简介、特点
//import CommonDetail from './commonDetail/commonDetail';
//投资记录
import InvestRecord from './investRecord/investRecord';
//投资框
import InvestFrame from './investFrame/InvestFrameState';
import { Button, Form, Input ,Checkbox, Modal, Tabs } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const TabPane = Tabs.TabPane;

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

class Detail extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this._getInfoData = this.getInfoData.bind(this);
    this._getMsgData = this.getMsgData.bind(this);//获取风险评估数据
    this.state = {
      data: "",
      infoData: "",
      msgdata: ""
    }
  } 
  getData(){
    let self = this;
    $.ajax({      
      url: '/realize/detailInfo.html',
      type: 'POST',
      dataType: 'json',
      data: {"id":$("#id").val()}    
    })
    .done(function(data) {    
      if(data.result){        
        self.setState({data: data});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      error("网络异常，请重试！");
    })
  }
  //获取问卷调查信息
    getMsgData(){
      let self = this;
      $.ajax({
        url: '/invest/userRiskLevelTip.html',
        type: 'POST',
        dataType: 'json',
        data: {"projectId":$("#projectId").val(),"isSelectedTip":$("#isSelectedTip").val()}    
      })
      .done(function(msgdata) {      
        if(msgdata.result)
        {       
          self.setState({msgdata: msgdata});
        }
        else
        {
          $("#isSelectedTip").val(1);
        }      
      })
      .fail(function() {
        //error("网络异常，请重试！");
      })
    }   
  componentDidMount(){
    this._getData();
    this._getInfoData();
    this._getMsgData();
  }
  //获取信息数据
  getInfoData(){
    let self = this;
    $.ajax({
      url: '/user/info.html',
      type: 'POST',
      dataType: 'json',
    }).done(function(infoData){
      if(infoData.result){
        self.setState({infoData: infoData});
      } else {
        error(data.msg);
      }
    }).fail(function(){
      error("网络异常，请重试！");
    })
  }  
  render() {
    let data = this.state.data;
    let infoData = this.state.infoData;
    let msgdata = this.state.msgdata;
    if(!data || !infoData || !msgdata) return false;  
    let project = data.realize;
    //计息方式
    let interestStyle = (project.interestStyle == "1" ? "成立计息" : "T+N计息");
    //还款方式
    let repayStyle = ["月等额本息", "一次性还款", "每月还息到期还本", "等额本金"][(parseInt(project.repayStyle)-1)];   
    //剩余时间
    let deadTimeHtml = '';
    if(project.stopTime){
      let deadTime = Publiclib.formatDatetype4(project.stopTime-data.systemTime);
      if(project.stopTime - data.systemTime <= 0){
        deadTime = "已过期";
      }
      deadTimeHtml = <li><span className="title">剩余时间</span>{deadTime}</li>;
    }
    //判断是否登录    
    let hasLogin = infoData.isLogin;    
		let html = <div className="noLoginText">
			<p>请<a href="/user/login.html">登录</a>或<a href="/user/register.html">注册</a>后查看</p>
		</div>;
    //产品详情
    let productInfoHtml = html;
    //投资记录
    let investRecordHtml = html;
    //查看该原始项目的链接
    var checkLink = '/invest/detail.html?id=' + $("#originalProjectId").val();
    if(hasLogin == "1"){
      productInfoHtml = <div className="realize-main-cont">
        <dl className="module">
            <dt>借款方所持资产信息<em>（该项目回款将作为借款方还款来源）</em><a href={checkLink} className="a-link"><span className="iconfont icon">&#xe66b;</span>查看该项目</a></dt>
            <dd><ProductInfo /></dd>
        </dl>  
        <dl className="module">
            <dt>产品简介</dt>
            <dd className="realize-text">“变现通”是平台推出的投融资服务之一。平台向出借方和借款方提供变现服务，帮助双方快捷方便地完成投资和借贷。通过该服务，符合条件的产品持有人，可以以其持有的特定资产的本金及预期收益作为还款来源，通过平台向出借方发起融资请求，获得快速便捷的短期融资机会。借贷双方通过平台签署借贷协议，明确双方的债务与债权关系。</dd>
        </dl>                  
        <dl className="module">
            <dt>产品特点</dt>
            <dd className="realize-text">
              <p>1、以特定资产的本金及预期收益作为直接还款来源
              通过“变现通”达成的借款，由借款方投资并持有的特定资产的本金及预期收益作为还款来源。当借款方借款到期，系统自动以其持有的特定资产的回款本息来清偿其应还款项。</p>
              <p>2、放款后次日计息，到期一次性还本付息出借方通过“变现通”借出资金，产品投满后统一放款给借款方，放款后次日即可计息。借款到期后，该款项可在到期日当日一次性还本付息到出借方的平台账户中。</p>
              <p>3、预期年化利率为固定利率通过“变现通”服务达成的交易的预期年化利率为固定利率，不会随着中国人民银行同期贷款预期利率的波动而调整。</p>
            </dd>
        </dl>            
      </div>;
      investRecordHtml = <InvestRecord />;
    }
    let investCondition='“'+data.investCondition+'” 及以上'
    return (
      <div>
        <div className="wrap site-row realize-detail-header">
          <div className="site-col-17 realize-header-info">
            <div className="info-title">
              <span className="title-name"><em className="iconfont">&#xe622;</em>{project.projectName}</span><span className="protocol">协议预览</span>
            </div>
            <div className="info-main">
                <ul>
                    <li className="profit">
                        <span className="text">{project.apr}</span>%<s>预期年化收益率</s>
                    </li>
                    <li className="time">
                        <span className="text">{project.timeLimit}</span>天<s>产品期限</s>
                    </li>
                    <li className="min">
                        <span className="text">{project.lowestAccount}</span>元<s>起投金额</s>
                    </li>
                </ul>
            </div>
            <div className="info-basic">
                <ul>
                    <li>
                        <span className="title">产品金额</span>{project.account}元
                    </li>
                    <li>
                        <span className="title">计息方式</span>{interestStyle}
                    </li>
                     <li>
                        <span className="title">收益方式</span>{repayStyle}
                    </li>
                    {deadTimeHtml}
                    <li>
                        <span className="title">安全等级</span>{data.risk}
                    </li>
                    <li>
                        <span className="title">投资条件</span>{investCondition}
                    </li>                                                                                                   
                </ul>
            </div>
          </div>
          <InvestFrame form = {this.props.form} data={data} infoData={this.state.infoData} msgdata={this.state.msgdata} />     
        </div>
        <div className="wrap realize-main">
          <div className="realize-main-nav">
            <Tabs defaultActiveKey="1">
              <TabPane tab="产品详情" key="1">
                {productInfoHtml}
              </TabPane>
              <TabPane tab="投资记录" key="2">
                {investRecordHtml}
              </TabPane>              
            </Tabs>            
          </div>          
        </div>
      </div>
    )
  }
};
Detail = createForm()(Detail);
ReactDOM.render(<Detail />,  document.getElementById("j-realizeDetail"));