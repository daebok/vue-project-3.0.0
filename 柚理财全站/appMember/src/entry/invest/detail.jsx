import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './detail.less';
import { ValidateFn } from '../../common/validateFn';
import { Reg } from '../../common/regularFun';
import { Publiclib } from '../../common/common';
//投资框
import InvestFrame from './investFrame/InvestFrameState';
//产品详情
import BorrowDetail from './borrowDetail/borrowDetail';
import BorrowersInfo from './borrowersInfo/borrowersInfo';
import BorrowData from './borrowData/borrowData';
//投资记录
import InvestRecord from './investRecord/investRecord';
//还款计划
import ReturnMoney from './returnMoney/returnMoney';

//担保机构
import VoucnOrganization from './voucnOrganization/voucnOrganization';
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
    this._getData = this.getData.bind(this);//获取详情数据
    this._getInfoData = this.getInfoData.bind(this);//获取登录数据
    this._getMsgData = this.getMsgData.bind(this);//获取风险评估数据
    this.state = {
      data: "",
      infoData: "",
      msgdata: "",
    }
  }  
  //获取详情数据
  getData(){
    let self = this;
    $.ajax({      
      url: '/project/detail.html',
      type: 'POST',
      dataType: 'json',
      data: {"projectId":$("#projectId").val()}    
    })
    .done(function(data) {      
      if(data.result){        
        self.setState({data: data});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      //error("网络异常，请重试！");
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
      //error("网络异常，请重试！");
    })
  }
  componentDidMount(){
    this._getData();
    this._getInfoData();
    this._getMsgData();
  }
  render() {
    let data = this.state.data;
    let infoData = this.state.infoData;
    let msgdata = this.state.msgdata;
    if(!data || !infoData || !msgdata) return false; 
    //alert(msgdata.msg)  
    let project = data.project;
    //预期年化收益率加息
    let addApr = "";
    if(project.addApr){
      addApr = "+" + project.addApr +"%";
    }
    //计息方式
    let interestStyle = (project.interestStyle == "1" ? "成立计息" : "T+"+ project.interestStartDays +"计息");
    //还款方式
    let repayStyle = ["等额本息", "一次性还款", "每月还息到期还本", "等额本金", "每季还息到期还本"][(parseInt(project.repayStyle)-1)];
    //剩余时间
    let deadTimeHtml = '';
    if(project.saleEndTime){   
      let deadTime = Publiclib.formatDatetype2(data.systemTime, project.saleEndTime);
      if(project.saleEndTime - data.systemTime <= 0){
        deadTime = "已过期";
      }
      deadTimeHtml = <li id="j-deadTimeHtml"><span className="title">剩余时间</span>{deadTime}</li>;
    }  
    //担保机构
    let investVouch = "";
    if(project.isVouch == "1"){
      investVouch = <dl className="module">
          <dt>担保机构</dt>
          <dd><VoucnOrganization /></dd>
        </dl>
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
    //回款计划
    let returnMoneyPlanHtml = html;
    let projectTypeName ='';
    if(hasLogin == "1"){
        if(project.borrowFlag=='0'){
            productInfoHtml = <div className="invest-main-cont">
                <dl className="module">
                    <dt>产品详情</dt>
                    <dd><BorrowDetail hasLogin={hasLogin} borrowFlag={0} /></dd>
                </dl>
            </div>;
            projectTypeName= project.projectName
        }else{
            productInfoHtml = <div className="invest-main-cont">
                <dl className="module">
                    <dt>借款详情</dt>
                    <dd className="detail-info">
                        <div className="mt20" style={{'fontSize':'14px'}}>
                            <label>借款标题</label>{project.projectName}
                        </div>
                             <BorrowDetail hasLogin={hasLogin} borrowFlag={1} />
                    </dd>
                </dl>
                <dl className="module">
                    <dt>借款方信息</dt>
                    <dd><BorrowersInfo hasLogin={hasLogin} data = {data} /></dd>
                </dl>
                <dl className="module">
                    <dt>借款资料</dt>
                    <dd><BorrowData hasLogin={hasLogin} /></dd>
                </dl>
                {investVouch}
            </div>;
        }

      investRecordHtml = <InvestRecord />;
      returnMoneyPlanHtml = <ReturnMoney />;
    }
    //判断是否有还款计划    
    let investMainHtml = "",totalCopies = "",copyAccount="";   
    if(project.status > 7){//有
      investMainHtml = <Tabs defaultActiveKey="1">
        <TabPane tab="原始产品详情" key="1">
          {productInfoHtml}
        </TabPane>
        <TabPane tab="投资记录" key="2">
          {investRecordHtml}
        </TabPane>              
        <TabPane tab="还款计划" key="3">{returnMoneyPlanHtml}</TabPane>              
      </Tabs>;
    } else {//无
      investMainHtml = <Tabs defaultActiveKey="1">
        <TabPane tab="产品详情" key="1">
          {productInfoHtml}
        </TabPane>
        <TabPane tab="投资记录" key="2">
          {investRecordHtml}
        </TabPane>
      </Tabs>;

    }
    let investCondition='“'+data.investCondition+'”及以上';
    if(data.project.saleStyle == "2"){
      totalCopies = <li><span className="title">产品份数</span>{data.project.totalCopies}份</li>;
      copyAccount = <li><span className="title">每份售价</span>{data.project.copyAccount}元</li>;
    }
    let noviceHtml = project.novice == "1" ? <span className="tags yellowIcon">新客</span> : "";   
    let transferHtml = project.bondUseful == "1" ? <span className="tags transfer">可转让</span> : "";
    let realizeFlag = project.realizeUseful == "1" ? <span className="tags realize">可变现</span> : "";
    return (
      <div>
        <div className="wrap site-row invest-detail-header">
          <div className="site-col-17 invest-header-info">
            <div className="info-title">
              <span className="title-name"><em className="iconfont">&#xe622;</em>{project.projectTypeName}-{project.projectNo}<span className="bottom-name">{projectTypeName}</span></span>{noviceHtml}{transferHtml}{realizeFlag}<span className="protocol">协议预览</span>
            </div>
            <div className="info-main">
                <ul>
                    <li className="profit">
                        <span className="text">{project.apr}</span>%{addApr}<s>预期年化收益率</s>
                    </li>
                    <li className="time">
                        <span className="text">{project.timeLimit}</span>{project.timeType == "1" ? "天" : "个月"}<s>产品期限</s>
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
                      {totalCopies}
                      {copyAccount}
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
          <InvestFrame form = {this.props.form} id={this.props.id} data={data} preview={this.props.preview} infoData={this.state.infoData} msgdata={this.state.msgdata}/>
        </div>
        <div className="wrap invest-main">
          <div className="invest-main-nav">
            {investMainHtml}            
          </div>          
        </div>
      </div>
    )
  }
}
Detail = createForm()(Detail);
ReactDOM.render(<Detail preview={$('#preview').val()} id={$('#projectId').val()} />,  document.getElementById("j-investDetail"));