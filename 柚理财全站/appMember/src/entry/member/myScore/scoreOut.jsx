import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import './scoreOut.less';
import Tabs from '../../../component/tab/tablelists';
import {Publiclib} from '../../../common/common';
import { Tooltip } from 'antd';

const statusType =['待审核','审批通过','审核失败','已发货','已收货','结束'];
const columns_first = [
          {
            title: '时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },
          {
            title: '商品',
            dataIndex: 'goodsName'
          },
          {
            title: '数量',
            dataIndex: 'num'
          },
          {
            title: '消耗积分',
            dataIndex: 'score',
            render(text,record) {
              return <font>-{text}</font>;
            }          
          },
          {
            title: '兑换留言',
            dataIndex: 'receiveRemark'
          },
          {
            title: '审核备注',
            dataIndex: 'verifyRemark'
          },
          {
            title: '兑换状态',
            dataIndex: 'status',
            render(text,record) {
              return statusType[text];
            }          
          }];

const config = {single:true,record:true,datetime:true,select:false};
// const select = {url:"/public/getSelectList.html?dictType=projectStatus"};
const select = {
  url:"",
  list:[
    {"itemName":"全部状态","itemValue":""},
    {"itemName":"待审核","itemValue":"1"},
    {"itemName":"审核未通过","itemValue":"2"},
    {"itemName":"募集中","itemValue":"3"},
    {"itemName":"成立待审","itemValue":"4"},
    {"itemName":"未成立","itemValue":"5"},
    {"itemName":"还款中","itemValue":"6"},
    {"itemName":"已还款","itemValue":"7"}
  ]
};

const select_name = 'none';

let ScoreIn = React.createClass({
  render(){
      return (
          <Tabs config={config} url='/member/myScore/getScoreOrders.html' tableid="tablelist-first" columns={columns_first} select={select} selectname={select_name}/>
        )
    }
});

ReactDOM.render(<ScoreIn />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"15"}/>,  document.getElementById("j-sider-menu"));
