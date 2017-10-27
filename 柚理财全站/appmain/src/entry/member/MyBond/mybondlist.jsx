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
            render(text) {                        
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              return <p>
                <span>{newText}</span>
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
            dataIndex: 'nextRepayDate',
            render(text) {
              return Publiclib.formatDate(text);
            },           
          },
          {
            title: '操作',
            render: (text, record) => {
                let link = "/member/myBond/bondSetList.html?id=" + record.id;
                return   <span className="tablelink"><a href={link} >转让</a></span>                  
              },
          }];

const columns_second = [{
            title: '债权名称',
            dataIndex: 'bondName',
            render(text) {                        
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              return <p>
                <span>{newText}</span>
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
            render(text) {                        
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              return <p>
                <span>{newText}</span>
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
            dataIndex: 'bondSuccessTime',
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

const columns_fourth = [{
            title: '产品名称',
            dataIndex: 'projectName',
             render(text) {                        
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              return <p>
                <span>{newText}</span>
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
                link_back = "/member/myInvest/projectCollection.html?investId=" + record.projectInvestId,url = "/member/myBond/downloadBondInvestProtocol.html?bondInvestId=" + record.id
                let href='';
                if(record.projectStatus=='9'){
                    href= <span><a href={url}>下载协议</a> <a href={link_back}>回款计划</a></span>
                }else if(record.projectStatus=='8'){
                    href = record.status == "1" ? <span><a href={url}>下载协议</a> <a href={link_back}>回款计划</a> <a href={link} >转让</a></span> : <a href={link} >去支付</a>;
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