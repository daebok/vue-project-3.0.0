import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs , Modal, Button,Tooltip} from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import CountDown from '../../../component/orderCountDown/countDownSecond';
import './myinvestlist.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        window.location.href = url;
      }
    }
  });
}


export default class ListCountDown extends React.Component{
    static defaultProps = {
        time: 5,
    }
    constructor(props) {
        super(props);
        this.state = {count: props.time,showTime:''};
        this.tick = this.tick.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.timer=null;
    }
    formatSeconds(value) {
        var theTime = parseInt(value);// 秒
        var theTime1 = 0;// 分
        var theTime2 = 0;// 小时
        if(theTime > 60) {
            theTime1 = parseInt(theTime/60);
            theTime = parseInt(theTime%60);
            if(theTime1 > 60) {
                theTime2 = parseInt(theTime1/60);
                theTime1 = parseInt(theTime1%60);
            }
        }
        var result = ""+parseInt(theTime)+"秒";
        if(theTime1 > 0) {
            result = ""+parseInt(theTime1)+"分"+result;
        }
        if(theTime2 > 0) {
            result = ""+parseInt(theTime2)+"小时"+result;
        }
        if(parseInt(theTime2) >= 24 ){
            result = Math.ceil(parseInt(theTime2)/24) + "天";
        }
        if(value==0){
            window.location.reload()
        }
        return result;
    }
    tick() {
        this.setState({count: this.state.count - 1,showTime:this.formatSeconds(this.state.count)});
    }
    handleChange() {
        if(this.state.count >= 0){
            this.tick();
        } else {
            window.location.reload()
        }
    }
    componentDidMount(){
        this.timer=setInterval(this.handleChange, 1000);
    }
    render(){
        return (
            <span className="ml10" style={{fontSize:'12px','border':'1px solid #eeeeee',borderRadius:'3px', background:'#fafafa',padding:'2px 3px'}}>只剩 <span style={{'color':'#f95a28'}}>{this.state.showTime}</span></span>
        );
    }
}

const columns_first = [{
            title: '产品名称',
            dataIndex: 'projectName',
            render(text,record) {
              let TooltipHtml = '',
              autoProduct = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              let typeUrl='';
               if(record.realizeFlag==0){
                   typeUrl= '/invest/detail.html?id='
               } else{
                   typeUrl= '/realize/detail.html?id='
               }
              let url=typeUrl+record.projectId;
              if(record.investType == "1"){
                autoProduct = <span className="auto-product"></span>;
              }
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}{autoProduct}
              </p>
            }            
          }, {
            title: '投资金额(元)',
            dataIndex: 'amount',
          }, {
            title: '已收本息',
            dataIndex: 'repayedAmount',
          }, {
            title: '待收本息',
            dataIndex: 'waitAmount',
          }, {
            title: '投资时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          }, {
            title: '预计下期回款日',
            dataIndex: 'nextRepayDate',
            render(text) {
              return text ? Publiclib.formatDate(text) : "-";
            },            
          }, {
            title: '操作',
            render: (text, record) => {
                    const href = "/member/myInvest/projectCollection.html?tab=1&investId=" + record.id;
                    const downlink = "/member/myInvest/downloadInvestProtocol.html?investId=" + record.id;
             return record.status == "1" ?   <span className="tablelink">
                  <a href={href}>回款计划</a> <a href={downlink}>下载协议</a>
                </span> : <span className="tablelink"><a href={downlink}>下载协议</a></span>;
              }
          }];
const status ={
  "0":"待支付",
  "1":"支付成功",
  "2":"申请失败",
  "3":"申请失败",
  "5":"支付失败"
}
const columns_second = [{
            title: '产品名称',
            dataIndex: 'projectName',
             render(text,record) {
              let TooltipHtml = '',
              autoProduct = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                 let typeUrl='';
                 if(record.realizeFlag==0){
                     typeUrl= '/invest/detail.html?id='
                 } else{
                     typeUrl= '/realize/detail.html?id='
                 }
                 let url=typeUrl+record.projectId;
                  if(record.investType == "1"){
                    autoProduct = <span className="auto-product"></span>;
                  }
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}{autoProduct}
              </p>
            }           
          }, {
            title: '投资金额(元)',
            dataIndex: 'amount',
          }, {
            title: '全部状态',
            dataIndex: 'status',
            render(text,record) {
                let html=''
                if(text==0){
                    html=<div>{status[text]}<span><ListCountDown time={record.remainTimes}/></span></div>
                }else{
                    html=status[text]
                }
                return html;
            },
          },{
            title: '投资申请时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          }, {
            title: '操作',
            render: (text, record) => {
                    const href = "/member/myInvest/doPay.html?uuid="+ record.id;
                    return record.status == "0" ?  <span className="tablelink"><a data-pay data-uuid={record.id}  data-href={href} target="_blank">去支付</a></span> : "-";
                  }
          }];

const statusThird={
  "01":"赎回申请中"
}
const columns_third = [{
            title: '产品名称',
            dataIndex: 'projectName',
            render(text,record) {
              let TooltipHtml = '',
              autoProduct = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let typeUrl='';
                if(record.realizeFlag==0){
                    typeUrl= '/invest/detail.html?id='
                } else{
                    typeUrl= '/realize/detail.html?id='
                }
                let url=typeUrl+record.projectId;
                if(record.investType == "1"){
                  autoProduct = <span className="auto-product"></span>;
                }                
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}{autoProduct}
              </p>
            }            
          }, {
            title: '赎回金额(元)',
            dataIndex: 'money',
          },{
            title: '赎回申请时间',
            dataIndex: 'applyTime',
            render(text) {
              return Publiclib.formatDate(text);
            },
          }, {
            title: '状态',
            dataIndex: 'status',
            render(text){                
               return statusThird[text];           
              },
          }];

const columns_fourth = [{
            title: '产品名称',
            dataIndex: 'projectName',
             render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                 let typeUrl='';
                 if(record.realizeFlag==0){
                     typeUrl= '/invest/detail.html?id='
                 } else{
                     typeUrl= '/realize/detail.html?id='
                 }
                 let url=typeUrl+record.projectId
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }            
          }, {
            title: '投资金额(元)',
            dataIndex: 'amount',
          },{
            title: '实际收益(元)',
            dataIndex: 'repayedInterest',
          },{
            title: '起息日',
            dataIndex: 'interestDate',
            render(text) {
              return Publiclib.formatDate(text);
            },            
          },{
            title: '结束日',
            dataIndex: 'endDate',
            render(text) {
              return Publiclib.formatDate(text);
            },            
          }, {
            title: '操作',
            render: (text, record) => {
                      const href = "/member/myInvest/projectCollection.html?tab=3&investId="+record.id;                           
             return   <span className="tablelink"><a href={href}>回款详情</a></span>
              },
          }];

const config_first = {single:true,datetime:true,select:false}
const config_second = {single:true,datetime:true,select:true}
const config_third = {single:true,datetime:true,select:false}
const config_fourth = {single:true,datetime:true,select:false}


const select_second = {
  url:"",
  list:[
    {"itemName":"全部状态","itemValue":""},
    {"itemName":"待支付","itemValue":"0"},
    {"itemName":"支付成功","itemValue":"1"},
    {"itemName":"申请失败","itemValue":"2,3"},
    {"itemName":"支付失败","itemValue":"5"}
  ]
};

const select_name = 'statusSet';

function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return '1';
}

let InviteIndex = React.createClass({
  getDefaultProps : function () {
      return {
          urlTab : GetQueryString('tab')
      }
  },
  getInitialState() {
    return {
          visible:false,
          visible1:false,
          time:0
        };
  },  
  handleOk() {
    window.location.href = "/member/myInvest/list.html?tab=2";
  }, 
  handleCancel() {
    this.setState({ visible: false });
    window.location.href = "/member/myInvest/list.html?tab=2";
  },
  closeOrderPayModal(){
    this.setState({ visible: false,visible1: true });
  },
  getInfo(uuid,el){
    let self = this;
    $.ajax({
      url: '/member/myInvest/repay.html',
      type: 'POST',
      dataType: 'json',
      async:false,
      data: {uuid:uuid}
    })
    .done(function(data) {
      if(data.result){
        el.attr("href",el.data("href"));
        self.setState({
          visible: true,
          time:data.remainSeconds
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

    $("#j-myinvest-list").on("click","[data-pay]",function(){
      self.getInfo($(this).data("uuid"),$(this));
    });

  }, 
  render(){
      return (
        <div>
          <Tabs defaultActiveKey={this.props.urlTab}>
            <TabPane tab="持有中" key="1">
                <Tablelist config={config_first} url='/member/myInvest/getBorrowHolding.html' tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="投资申请" key="2">
                <Tablelist config={config_second} url='/member/myInvest/getInvestApply.html' tableid="tablelist-second" select={select_second} columns={columns_second} selectname={select_name}/>
            </TabPane>
            <TabPane tab="已结束" key="3">
                <Tablelist config={config_fourth} url='/member/myInvest/getInvestClosed.html' tableid="tablelist-fourth" columns={columns_fourth}/>
            </TabPane>                         
          </Tabs>
          <Modal ref="modal" className="orderPayModal"
              visible={this.state.visible}
              title="登录资金账户完成支付" onOk={this.handleOk} onCancel={this.handleCancel}
              width={480}
              footer={[                      
                <Button key="submit" type="primary" size="large" onClick={this.handleOk}>
                  支付成功
                </Button>,
                <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>支付失败</Button>,
              ]}
            >
              <div className="modal-content"><i className="iconfont">&#xe64d;</i><p>请在<strong><CountDown time={this.state.time} state={this.state.visible} closeOrderPayModal={this.closeOrderPayModal} /></strong>内完成支付，若超出时间限制未完成，<br/> 将取消订单。</p></div>
            </Modal>
          <Modal ref="modal" className="orderPayModal"
                 visible={this.state.visible1}
                 title="支付超时" onOk={this.handleOk} onCancel={this.handleCancel}
                 width={480}
                 footer={[
                   <Button key="back" type="ghost" size="large" onClick={this.handleCancel}>返回</Button>
                 ]}
          >
            <div className="modal-content"><i className="iconfont">&#xe62e;</i><p style={{'paddingTop':'0'}}>您的支付已超时，系统已自动取消订单。<br/>请关闭此页面重新操作</p></div>
          </Modal>
        </div> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-myinvest-list"));

ReactDOM.render(<Accountmenu current = {"3"}/>,  document.getElementById("j-sider-menu"));