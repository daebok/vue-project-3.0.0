import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs,Tooltip } from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './verify.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';

const isRepayentStatus={
  "1":"未审核",
  "2":"已审核"
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
            title: '年化利率',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },            
          },
          {
            title: '项目金额(元)',
            dataIndex: 'account',
          },
          {
            title: '项目期限(天)',
            dataIndex: 'timeLimit',
          },                     
          {
            title: '操作',
            render: (text, record) => {
                  let link = "/member/vouch/verifyPage.html?id="+record.id;               
                  return  <span className="tablelink">
                            <a href={link}>审核</a>
                          </span>                   
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
            title: '年化利率',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },            
          },
          {
            title: '项目金额(元)',
            dataIndex: 'account',
          },
          {
            title: '项目期限(天)',
            dataIndex: 'timeLimit', 
          },                     
          {
            title: '状态',
            dataIndex: 'vouchStatus',
            render(text) {
              if(text==1){
                return <span>审核通过</span>;
              }
              else{
                return <span>审核拒绝</span>;
              }
            },
          },
            {
                title: '操作',
                dataIndex: 'id',
                render(text) {
                    let url='/member/vouch/projectInfo.html?uuid='+text
                   return <span className="tablelink"><a href={url}>详情</a></span>
                },
            }];

const config_first = {single:false,datetime:false,select:false};
const config_second = {single:false,datetime:false,select:false};

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="未审核" key="1">
                <Tablelist config={config_first} url='/member/vouch/verifyList.html?vouchStatus=0' tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="已审核" key="2">
                <Tablelist config={config_second} url='/member/vouch/verifyList.html?vouchStatus=1' tableid="tablelist-second" columns={columns_second}/>
            </TabPane>                       
          </Tabs> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"4"}/>,  document.getElementById("j-sider-menu"));