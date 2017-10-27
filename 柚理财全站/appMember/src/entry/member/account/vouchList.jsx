import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import './funds.less';
import { Tooltip} from 'antd';
const columns = [{
            title: '时间',
            dataIndex: 'createTime',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          }, {
            title: '交易金额(元)',
            dataIndex: 'money',
          }, {
            title: '状态',
            dataIndex: 'accountTypeStr',
          }, {
            title: '备注',
            dataIndex: 'remark',
            render(text,record) {
              let TooltipHtml = '';
              text= text ? text.replace(/<.*?>/ig,"") : "";
              let part = text ? text.substr(0,20) : "-";
              let html=<span>{part}</span>
              if(record.htmlAUrl!=''){
                  html=<a href={record.htmlAUrl} className="remark-link"><span>{part}</span></a>
                }
              if(text && text.length >= 20){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
              }
              return <p>
                <span>{part}</span>
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
    {"itemName":"垫付成功","itemValue":"repaid_capital,repaid_interest,repaid_late_interest,repaid_merchant_late_interest"}
  ]
};

const select_name = 'accountTypeSet';

ReactDOM.render(
  <Tablelist url='/member/vouch/getLogList.html' tableid="tablelist" columns={columns} select={select} selectname={select_name}/>
, document.getElementById("j-fundstab"));


ReactDOM.render(<AccountVouchMenu current = {"2"}/>,  document.getElementById("j-sider-menu"));