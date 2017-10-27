
import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './detail.less';
import { ValidateFn } from '../../common/validateFn';
import { Reg } from '../../common/regularFun';
import { Publiclib } from '../../common/common';
//倒计时组件
import CountDown from '../../component/countDown/CountDownComponentBig';
//产品详情
import BorrowDetail from './borrowDetail/borrowDetail';
import BorrowersInfo from './borrowersInfo/borrowersInfo';
import BorrowData from './borrowData/borrowData';
//投资记录
import InvestRecord from './investRecord/investRecord';
//投资框
import InvestFrame from './investFrame/InvestFrameState';
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
    this._getData = this.getData.bind(this);
    this._getInfoData = this.getInfoData.bind(this);//获取登录数据
    this._getMsgData = this.getMsgData.bind(this);//获取风险评估数据
    this.handleCancel = this.handleCancel.bind(this);
    this.getInfo = this.getInfo.bind(this);
    this.state = {
      data: "",
      infoData: "",
      msgdata: "",
      visible: false,
      content:'',
      protocolType:'债权协议'
    }
  }
    handleCancel() {
        this.setState({ visible: false });
    }
  getData(){
    let self = this;
    $.ajax({
      url: '/bond/getBondProjectDetail.html',
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
        if(msgdata.result){       
          self.setState({msgdata: msgdata});
        } else {
          $("#isSelectedTip").val(1)        
          //error(data.msg);
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
    getInfo(){
        let self = this;
        $.ajax({
            url: '/member/myBond/getBondProtocolContent.html',
            type: 'POST',
            dataType: 'json',
            data: {protocolId:this.state.data.protocolId,bondId:$("#id").val()},
        })
            .done(function(data) {
                if(data.result){
                    self.setState({
                        visible: true,
                        content:data.content,
                        protocolType:data.protocolType
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
    let project = data;

    //剩余时间
    let deadTime = (project.deadTime-project.currentTime)/1000;
    if(deadTime<0){
      deadTime=0;
    }
    if(project.saleEndTime - project.systemTime <= 0){
      deadTime = "已过期";
    }
    //担保机构
    let investVouch = "";
    if(project.isVouch == "1"){
      investVouch = <dl className="module">
          <dt>担保机构</dt>
          <dd><VoucnOrganization /></dd>
        </dl>
    }
    //投资条件
    let tenderCondition = '"' + project.tenderCondition + '"' + '及以上';
    //判断是否登录
    let hasLogin = infoData.isLogin;
		let html = <div className="noLoginText">
			<p>请<a href="/user/login.html">登录</a>或<a href="/user/register.html">注册</a>后查看</p>
		</div>;
    //产品详情
    let productInfoHtml = html;
    //投资记录
    let investRecordHtml = html;
    if(hasLogin == "1"){
      productInfoHtml = <div className="bond-main-cont">
        <dl className="module">
            <dt>借款详情</dt>
            <dd><BorrowDetail /></dd>
        </dl>
        <dl className="module">
            <dt>借款方信息</dt>
            <dd><BorrowersInfo /></dd>
        </dl>
        <dl className="module">
            <dt>借款资料</dt>
            <dd><BorrowData /></dd>
        </dl>
        {investVouch}
      </div>;
      investRecordHtml = <InvestRecord />;
    }
    let Count =<CountDown time={deadTime}/>
      let apr = "";
      if(project.bondApr<0){
          apr=project.bondApr;
      }else{
          apr='+'+project.bondApr
      }
    return (
      <div>
        <div className="wrap site-row bond-detail-header">
          <div className="site-col-17 bond-header-info">
            <div className="info-title">
              <span className="title-name"><em className="iconfont">&#xe622;</em>{project.bondName}<i className="yellowIcon">转让</i></span><span className="protocol" onClick={this.getInfo}>协议预览</span>
                <Modal
                    visible={this.state.visible}
                    title={this.state.protocolType} onCancel={this.handleCancel}
                    width={780}
                    footer={[
                        <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>关闭</Button>
                    ]}
                >
                    <div style={{maxHeight:"400px",overflow:"auto"}} dangerouslySetInnerHTML={{__html: this.state.content}}></div>
                </Modal>
            </div>
            <div className="info-main">
                <ul>
                    <li className="profit">
                        <span className="text">{project.apr}</span><em>%</em><s>预期年化收益率</s>
                    </li>
                    <li className="time">
                        <span className="text">{project.remainDay}</span>天<s>剩余期限</s>
                    </li>
                    <li className="bondApr">
                        <span className="text">{apr}</span>%<s>折溢价率</s>
                    </li>
                    <li className="min">
                        <span className="text">{project.bondLowestMoney}</span>元<s>起投金额</s>
                    </li>
                </ul>
            </div>
            <div className="info-basic">
                <ul>
                    <li>
                        <span className="title">债权总额</span>{project.bondMoney}元
                    </li>
                    <li>
                        <span className="title">计息方式</span>{project.interestStyle}
                    </li>
                     <li>
                        <span className="title">还款方式</span>{project.repayStyle}
                    </li>
                    <li>
                        <span className="title">剩余时间</span>{Count}
                    </li>
                    <li>
                        <span className="title">安全等级</span>{project.riskLevel}
                    </li>
                    <li>
                        <span className="title">投资条件</span>{tenderCondition}
                    </li>
                </ul>
            </div>
          </div>
          <InvestFrame form = {this.props.form} data={project} infoData={this.state.infoData} id={$("#id").val()} msgdata={this.state.msgdata}/>
        </div>
        <div className="wrap bond-main">
          <div className="bond-main-nav">
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
ReactDOM.render(<Detail />,  document.getElementById("j-bondDetail"));
