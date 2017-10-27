import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs } from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import './index.less';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';


const columns = [{
            title: '被邀请人',
            dataIndex: 'inviteeUserMobile',
          },{
            title: '邀请时间',
            dataIndex: 'inviteeUserTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },  {
            title: '获得奖励',
            dataIndex: 'awardRedTotal',
            render(text,record) {
              let string = "";
              if( text > 0 ){
                string = "红包奖励共" + text.toFixed(2) + "元";
              }
              if( record.awardRateTotal > 0 ){
                string = "加息券奖励共" + record.awardRateTotal + "个";
              } 
              if( text > 0 && record.awardRateTotal > 0 ){
                string = "红包奖励共" + text.toFixed(2) + "元,加息券奖励共" + record.awardRateTotal + "个";
              }             
              return string;
            },
          }];
          
const config = {
  single:false,
  datetime:false,
  select:false
}

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="一级人脉" key="1">
                <Tablelist config={config} url='/member/invite/getLogList.html?inviteLevel=1' tableid="tablelist-first" columns={columns}/>
            </TabPane>
            <TabPane tab="二级人脉" key="2">
                <Tablelist config={config} url='/member/invite/getLogList.html?inviteLevel=2' tableid="tablelist-second" columns={columns}/>
            </TabPane>
          </Tabs> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-invite-list"));

ReactDOM.render(<Accountmenu current = {"11"}/>,  document.getElementById("j-sider-menu"));