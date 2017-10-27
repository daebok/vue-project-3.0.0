import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs , Modal ,Tooltip} from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import './repayment.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import Dopay from '../vouch/dopay';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

let MsgBtn = React.createClass({
  tip(){
    error("请先完成前面期数的还款");
  },
  render(){
      return (
        <span onClick={this.tip} style={{"cursor":"pointer"}}>还款</span>
        )
    }  
});

Math.formatFloat = function(f, digit) {
    if(f==undefined){
        return '0.00'
    }else{
        return  f.toFixed(digit)
    }
};
const columns_first = [{
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
            title: '借款管理费(元)',
            dataIndex: 'borrowManageFee',
            render(text,record) {
              return Math.formatFloat(text,2);
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
          },                     
          {
            title: '操作',
            render: (text, record) => {
                if( record.status == "0" ){
                  const  url = "/member/myRepayment/doRepay.html",tipName = "还款",title = <div className="title"><font>{tipName}</font>每笔资金受特别保护，安全可靠，请放心操作</div>;
                  return <Dopay title={title} url={url} tipName={tipName} capital={record.capital} interest={record.interest} borrowManageFee={record.borrowManageFee} lateInterest={record.lateInterestSum} uuid={record.id} />
                }
                else if( record.status == "-1" )
                {
                  return <MsgBtn />;
                }
                else if( record.status == "1" )
                {
                  if(record.repayType == "2"){
                    const  url = "/member/myRepayment/doRepayBail.html",tipName = "还垫付款",title = <div className="title"><font>{tipName}</font>每笔资金受特别保护，安全可靠，请放心操作</div>;
                    return <Dopay title={title} url={url} tipName={tipName} capital={record.capital} interest={record.interest} borrowManageFee={record.borrowManageFee} lateInterest={record.lateInterestSum} uuid={record.id} />
                  }else{
                    return "-";
                  }
                }                                 
              },
          }];

const columns_second = [{
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
            title: '实际还款日',
            dataIndex: 'realRepayTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },   
          }];

const config_first = {single:true,datetime:true,select:true};
const config_second = {single:true,datetime:true,select:false};

const select = {url:"",list:[{"itemName":"全部状态","itemValue":"00"},{"itemName":"待还","itemValue":"01"},{"itemName":"逾期待还","itemValue":"02"},{"itemName":"担保垫付","itemValue":"03"}]}

const select_name = 'statusType';

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="待还" key="1">
                <Tablelist select={select} config={config_first} url='/member/myRepayment/getLogList.html?status=0' tableid="tablelist-first" columns={columns_first} selectname={select_name} />
            </TabPane>
            <TabPane tab="已还" key="2">
                <Tablelist config={config_second} url='/member/myRepayment/getLogList.html?status=1' tableid="tablelist-second" columns={columns_second} selectname={select_name} />
            </TabPane>                       
          </Tabs> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"8"}/>,  document.getElementById("j-sider-menu"));