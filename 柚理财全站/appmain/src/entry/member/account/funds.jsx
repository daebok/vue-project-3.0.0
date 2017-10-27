import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import './funds.less';
import { Tooltip } from 'antd';

const columns = [{
            title: '时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          }, {
            title: '交易金额(元)',
            dataIndex: 'money',
            render(text,record) {
              let sign = "";
              if(record.paymentsType != "0"){
                sign = record.paymentsType == "1" ? "+" : "-";
              }
              return sign+text;
            }
          }, {
            title: '状态',
            dataIndex: 'accountTypeStr',
          }, {
            title: '交易对方',
            dataIndex: 'toUser',
          }, {
            title: '备注',
            dataIndex: 'remark',
            render(text) {                        
              let TooltipHtml = '',
              newText = text ? text.substr(0,20) : "-";
              if(text && text.length > 20){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              return <p>
                <span>{newText}</span>
                {TooltipHtml}
              </p>
            },
          }];
const select = {
  url:"",
  list:[
    {"itemName":"全部状态","itemValue":""},
    {"itemName":"充值成功","itemValue":"recharge_success_log"},
    {"itemName":"提现成功","itemValue":"cash_success"},
    {"itemName":"投资成功","itemValue":"invest_success"},
    {"itemName":"本金收回","itemValue":"collect_capital"},
    {"itemName":"利息收回","itemValue":"collect_interest"},
    {"itemName":"借款入账","itemValue":"borrow_success"},
    {"itemName":"还款本金","itemValue":"repaid_capital"},
    {"itemName":"还款利息","itemValue":"repaid_interest"}
  ]
};

const select_name = 'accountType';

ReactDOM.render(
  <Tablelist url='/member/fund/getLogList.html' tableid="tablelist" columns={columns} select={select} selectname={select_name}/>
, document.getElementById("j-fundstab"));


ReactDOM.render(<Accountmenu current = {"2"}/>,  document.getElementById("j-sider-menu"));