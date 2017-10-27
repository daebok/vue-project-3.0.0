import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs , Modal,Tooltip} from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import './mybondlist.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
const confirm = Modal.confirm;
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

function showConfirm(e) {
    let value = $(e.target).attr("data-value");
    let link = $(e.target).attr("data-link");
    confirm({
        title: '友情提示',
        content: '此笔投资您拥有加息收益'+value+'元,若您进行转让,系统将视为您放弃此笔投资的加息收益。',
        onOk() {
           window.location.href=link;
        },
        onCancel() {}
    });
}


function withdraw(e) {
    let id = $(e.target).attr("data-id");
    confirm({
        title: '您是否确认撤回债权',
        onOk() {
            console.log(id)
            $.ajax({
                url: '/member/myBond/cancleBond.html',
                type: 'POST',
                dataType: 'json',
                data:{id:id}
            })
                .done(function(data) {
                    if(data.result){
                        success(data.msg,'/member/myBond/list.html?tab=2');
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
    });


  
}

const columns_first = [{
            title: '产品名称',
            dataIndex: 'projectName',
            render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let url='/invest/detail.html?id='+record.projectId
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }             
          },
          {
            title: '原资产预期年化',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },
          },
          {
            title: '可转让金额(元)',
            dataIndex: 'amount',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }
            },             
          },
          {
            title: '剩余期限(天)',
            dataIndex: 'remainDays',
          },
          {
            title: '本期回款日',
            dataIndex: 'nextRepayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },           
          },
          {
            title: '操作',
            render: (text, record) => {
                let link = "/member/myBond/bondSetList.html?id=" + record.id;
                let html =  <a href={link} >转让</a>
                if(record.raiseInterest>0){
                    html =  <a href='javascript:;' data-link={link} data-value={record.raiseInterest} onClick={showConfirm} >转让</a>
                }
                return   <span className="tablelink">{html}</span>
              },
          }];

const columns_second = [{
            title: '债权名称',
            dataIndex: 'bondName',
            render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let url='/bond/detail.html?id='+record.id
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }            
          },
          {
            title: '债权本金(元)',
            dataIndex: 'bondMoney',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }
            },            
          },
          {
            title: '折溢价率',
            dataIndex: 'bondApr',
            render(text) {
              return text+"%";
            },
          },
          {
            title: '转让价格(元)',
            dataIndex: 'bondPrice',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },             
          },
          {
            title: '已转出债权(元)',
            dataIndex: 'soldCapital',
          },
          {
            title: '转让申请时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },           
          },{
            title: '操作',
            render: (text, record) => {
                return <span className="tablelink"><a href="javascript:" data-id={record.id} onClick={withdraw}>撤回</a></span>
              },            
          }];

const columns_third = [{
            title: '债权名称',
            dataIndex: 'bondName',
            render(text, record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                let url='/bond/detail.html?id='+record.id
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }             
          },
          {
            title: '债权本金',
            dataIndex: 'bondMoney',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },             
          },
          {
            title: '折溢价率',
            dataIndex: 'bondApr',
            render(text) {
              return text+"%";
            },
          },
          {
            title: '转让价格(元)',
            dataIndex: 'bondPrice',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },             
          },
          {
            title: '实收金额(元)',
            dataIndex: 'receivedMoney',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },             
          },
          {
            title: '转让成功时间',
            dataIndex: 'successTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },           
          },
          /*{
            title: '应收收益(元)',
            dataIndex: 'happendInterest',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },
          },*/
          {
            title: '手续费(元)',
            dataIndex: 'manageFee',
            render(text) {
              if(Publiclib.isnumber(text)){
                return text.toFixed(2);
              }            },             
          },{
        title: '操作',
        render: (text, record) => {
            var url="/member/myBond/downloadAllBondProtocol.html?bondId="+record.id
            return <span className="tablelink"><a href={url} >下载协议</a></span>
        },
    }];

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

const columns_fourth = [{
            title: '债权名称',
            dataIndex: 'bondName',
             render(text, record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
                 let url='/bond/detail.html?id='+record.bondId;
                 return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }            
          },
          {
            title: '债权本金',
            dataIndex: 'amount',
            render(text) {
              return text.toFixed(2);
            },             
          },
          {
            title: '折溢价率',
            dataIndex: 'bondApr',
            render(text) {
              return text+"%";
            },
          },
          {
            title: '支付金额(元)',
            dataIndex: 'paidMoney',
            render(text) {
              return text.toFixed(2);
            },             
          },
          {
            title: '待收本息(元)',
            dataIndex: 'waitMoney',
             render(text) {
              return text.toFixed(2);
            },            
          },         
          {
            title: '受让成功时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },           
          },
          {
            title: '操作',
            render: (text, record) => {
                let link = record.status == "1" ? "/member/myBond/bondSetList.html?id=" + record.projectInvestId : "/member/myBond/doBondPay.html?investOrderNo=" + record.investOrderNo,
                link_back = "/member/myInvest/projectCollection.html?type=bond&investId=" + record.projectInvestId,url = "/member/myBond/downloadBondInvestProtocol.html?bondInvestId=" + record.id
                let href='';
                if(record.projectStatus=='9'){
                    href= <span><a href={url}>下载协议</a> <a href={link_back}>回款计划</a></span>
                }else if(record.projectStatus=='8'){
                  href = record.status == "1" ? <span><a href={url}>下载协议</a> <a href={link_back}>回款计划</a> <a href={link} >转让</a></span> : <span><a href={link} >去支付</a><span><ListCountDown time={record.remainTimes}/></span></span>;
                  if(record.showBond == false && record.status == "1"){
                    href=<span><a href={url}>下载协议</a> <a href={link_back}>回款计划</a></span>
                  }
                }else{
                    href = '-'
                }
                return <span className="tablelink">{href}</span>;                 
              },
          }];

const config = {single:true,datetime:true,select:false};
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
    bondConfirm:function () {
        showConfirm()
    },
    render(){
      return (
          <Tabs defaultActiveKey={this.props.urlTab}>
            <TabPane tab="可转让" key="1">
                <Tablelist config={config} url='/member/myBond/ableBondList.html' tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="转让中" key="2">
                <Tablelist config={config} url='/member/myBond/sellingBondList.html' tableid="tablelist-second" columns={columns_second}/>
            </TabPane>
            <TabPane tab="已转让" key="3">
                <Tablelist config={config} url='/member/myBond/soldBondList.html' tableid="tablelist-third" columns={columns_third}/>
            </TabPane>
            <TabPane tab="已受让" key="4">
                <Tablelist config={config} url='/member/myBond/boughtBondList.html' tableid="tablelist-fourth" columns={columns_fourth}/>
            </TabPane>
          </Tabs>
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"4"}/>,  document.getElementById("j-sider-menu"));