import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs } from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import './vouchIndex.less';


const columns_first = [{
            title: '时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          }, {
            title: '交易金额(元)',
            dataIndex: 'amount',
          },{
            title:"交易流水号",
            dataIndex:"orderNo",
          },{
            title:"状态",
            dataIndex:"statusStr",   
          },{
            title:"备注",
            dataIndex:"remark",
            render(text) {
              return !text ? '-' : text;
            },           
          }
          ];

const columns_second = [{
            title: '时间',
            dataIndex: 'addTime',
            render(text) {
              return Publiclib.formatDate(text);
            },
          }, {
            title: '交易金额(元)',
            dataIndex: 'amount',
          },{
            title:"手续费(元)",
            dataIndex:"cashFee",
            render(text) {
              return !text ? '-' : text;
            },            
          },{
            title:"交易流水号",
            dataIndex:"orderNo",   
          },{
            title:"状态",
            dataIndex:"statusStr",   
          },{
            title:"备注",
            dataIndex:"remark",
            render(text) {
              return !text ? '-' : text;
            },              
          }
          ];

const select_first = {url:"/public/getSelectList.html?dictType=rechargeStatus"};
const select_second = {
    url:"",
    list:[
        {"itemName":"全部状态","itemValue":""},
        {"itemName":"提现处理中","itemValue":"0,1"},
        {"itemName":"提现待审核","itemValue":"2"},
        {"itemName":"提现成功","itemValue":"3"},
        {"itemName":"提现失败","itemValue":"4"}
    ]};

let RechargeIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="充值" key="1">
                <Tablelist url='/member/vouch/rechargeList.html' tableid="tablelist" columns={columns_first} select={select_first} />
            </TabPane>
            <TabPane tab="提现" key="2">
                <Tablelist url='/member/vouch/cashList.html' tableid="tablelist-cash" columns={columns_second} select={select_second} selectname='statusList' />
            </TabPane>
          </Tabs> 
        )
    }  
});

ReactDOM.render(<RechargeIndex />,  document.getElementById("j-rechargeindex"));

ReactDOM.render(<AccountVouchMenu current = {"3"}/>,  document.getElementById("j-sider-menu"));