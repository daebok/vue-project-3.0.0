import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs , Tooltip } from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './vouchProject.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';

const isRepayentStatus={
  "1":"未还款",
  "2":"已还款"
}
const columns_first = [{
            title: '项目名称',
            dataIndex: 'projectName',
            render(text,record) {
                let projectName = text;
                let projectNameval = Publiclib.subNameFun(projectName,0,10);
                let url='/invest/detail.html?id='+record.projectId
              return <Tooltip title={projectName}>
                  <a href={url}><span>{projectNameval}</span></a>
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
            title: '应付利息(元)',
            dataIndex: 'interest',
          },
          {
            title: '预计还款时间',
            dataIndex: 'repayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },   
          }];

const columns_second = [{
            title: '项目名称',
            dataIndex: 'projectName',
            render(text,record) {
                let projectName = text;
                let projectNameval = Publiclib.subNameFun(projectName,0,10);
                let url='/invest/detail.html?id='+record.projectId
              return <Tooltip title={projectName}>
                  <a href={url}><span>{projectNameval}</span></a>
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
            title: '已还本金(元)',
            dataIndex: 'capital',
          },
          {
            title: '已还利息(元)',
            dataIndex: 'interest',
          },
          {
            title: '罚息(元)',
            dataIndex: 'lateInterestSum',
          },
          {
            title: '实际还款时间',
            dataIndex: 'realRepayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },   
          }];

const config_first = {single:false,datetime:false,select:false};
const config_second = {single:false,datetime:false,select:false};
const select_name = 'statusType';
let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="未还款" key="1">
                <Tablelist config={config_first} url='/member/vouch/vouchProjectList.html?status=0' tableid="tablelist-first" columns={columns_first} selectname={select_name}/>
            </TabPane>
            <TabPane tab="已还款" key="2">
                <Tablelist config={config_second} url='/member/vouch/vouchProjectList.html?status=1' tableid="tablelist-second" columns={columns_second} selectname={select_name}/>
            </TabPane>                       
          </Tabs> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"5"}/>,  document.getElementById("j-sider-menu"));