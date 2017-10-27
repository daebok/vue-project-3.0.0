import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import './scoreIn.less';
import Tabs from '../../../component/tab/tablelist';
import {Publiclib} from '../../../common/common';
import { Tooltip } from 'antd';

const columns_first = [
          {
            title: '时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },
          {
            title: '行为',
            dataIndex: 'typeName'
          },
          {
            title: '积分变化',
            dataIndex: 'optValue',
            render(text,record) {
              if(record.optType==1){
                return <font>+{text}</font>;
              }
              else{
                return <font>-{text}</font>;
              }
            }          
          },
          {
            title: '行为描述',
            dataIndex: 'remark'
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

const select_name = 'status';

let ScoreIn = React.createClass({
  render(){
      return (
          <Tabs config={config} url='/member/myScore/getScoreLogs.html' tableid="tablelist-first" columns={columns_first} select={select} selectname={select_name}/>
        )
    }
});

ReactDOM.render(<ScoreIn />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"13"}/>,  document.getElementById("j-sider-menu"));
