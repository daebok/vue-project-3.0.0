import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs,Modal,Tooltip } from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './overdue.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
//弹窗垫付
import Dopay from './dopay';
//确认回款
import Payment from './payment';

const isRepayentStatus={
  "1":"未垫付",
  "2":"已垫付"
}
const columns_first = [{
            title: '项目名称',
            dataIndex: 'projectName',
            render(text,record) {
              let projectName = record.projectName;
              let projectNameval = Publiclib.subNameFun(record.projectName,0,10);
              return <Tooltip title={projectName}>
                <span>{projectNameval}</span>
              </Tooltip>
            },
          },
          {
            title: '借款方',
            dataIndex: 'realName',
            render(text,record) {
              let realName = record.realName;
              let realNameval = Publiclib.subNameFun(record.realName,0,10);
              return <Tooltip title={realName}>
                <span>{realNameval}</span>
              </Tooltip>
            },
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
          },
          {
            title: '应还利息(元)',
            dataIndex: 'interest',
          },
          {
            title: '罚息(元)',
            dataIndex: 'lateInterest',
          },
          {
            title: '应还时间',
            dataIndex: 'repayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },   
          },
          {
            title: '逾期天数(天)',
            dataIndex: 'lateDays',           
          },                     
          {
            title: '操作',
            render: (text, record) => { 
                  const  url = "/member/vouch/doOverdue.html",

                  tipName = "垫付",
                  title = <div className="title"><font>垫付</font>每笔资金受特别保护，安全可靠，请放心操作</div>;
                  return <Dopay capital={record.capital} tipName={tipName} interest={record.interest} lateInterest={record.lateInterest} uuid={record.id} url={url} title={title}/>;

              },
          }];

const columns_second = [{
            title: '项目名称',
            dataIndex: 'projectName',
            render(text,record) {
              let projectName = record.projectName;
              let projectNameval = Publiclib.subNameFun(record.projectName,0,10);
              return <Tooltip title={projectName}>
                <span>{projectNameval}</span>
              </Tooltip>
            },
          },
          {
            title: '借款方',
            dataIndex: 'realName',
            render(text,record) {
              let realName = record.realName;
              let realNameval = Publiclib.subNameFun(record.realName,0,10);
              return <Tooltip title={realName}>
                <span>{realNameval}</span>
              </Tooltip>
            },
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
          },
          {
            title: '应还利息(元)',
            dataIndex: 'interest',
          },
          {
            title: '逾期罚息(元)',
            dataIndex: 'lateInterest',
          },
          {
            title: '实际还款日',
            dataIndex: 'realRepayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },   
          },{
            title: '操作',
            render: (text, record) => {          
                  return  <span className="tablelink">
                            <Payment id={record.id} />
                          </span>                   
              },
          }];

const config_first = {single:false,datetime:false,select:false};
const config_second = {single:false,datetime:false,select:false};

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="未垫付" key="1">
                <Tablelist config={config_first} url='/member/vouch/overdueList.html?overdueStatus=0' tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="已垫付" key="2">
                <Tablelist config={config_second} url='/member/vouch/overdueList.html?overdueStatus=1' tableid="tablelist-second" columns={columns_second}/>
            </TabPane>                       
          </Tabs>
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"6"}/>,  document.getElementById("j-sider-menu"));