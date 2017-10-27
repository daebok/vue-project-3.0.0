import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs } from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import './myrealizelist.less';
import Tablelist from '../../../component/tablelist/tablelist';
import TablelistOther from '../../../component/tablelist/tablelistOther';
import {Publiclib} from '../../../common/common';
import { Tooltip } from 'antd';

const columns_first = [{
            title: '产品名称',
            dataIndex: 'projectName',
                        render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              let url='';
              if(record.realizeFlag=="1"){
                  url='/realize/detail.html?id='+record.projectId
              }else{
                  url='/invest/detail.html?id='+record.projectId
              }

              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }   
          }, {
            title: '投资金额(元)',
            dataIndex: 'amount',
          }, {
            title: '预期收益(元)',
            dataIndex: 'waitInterest',
          }, {
            title: '剩余期限(天)',
            dataIndex: 'remainDays',
          }, {
            title: '本期回款日',
            dataIndex: 'nextRepayTime',
            render(text) {
            
                return text ? Publiclib.formatDate(text) : "-";
              
            },
          }, {
            title: '操作',
            render: (text, record) => {
                  let link = "/member/myRealize/setting.html?projectInvestId="+record.id;
                 return <span className="tablelink">                    
                    <a href={link}>变现</a>
                  </span> 
              },
          }];
const status ={
  "01":"待支付"
}
const columns_second = [{
            title: '产品名称',
            dataIndex: 'projectName',
                        render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
               let url='/realize/detail.html?id='+record.projectId
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }   
          }, {
            title: '变现利率',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },            
          }, {
            title: '变现金额(元)',
            dataIndex: 'account',
          },{
            title: '变现申请时间',
            dataIndex: 'createTime',
            render(text) {
              
              return text ? Publiclib.formatDate(text,2) : "-";
              
            },
          }, {
            title: '变现进度',
            dataIndex: 'scales',
            render(text) {
              return text+"%";
            },  
          }];

const columns_third = [{
            title: '产品名称',
            dataIndex: 'projectName',
                        render(text,record) {
              let TooltipHtml = '',
              newText = text ? text.substr(0,10) : "-";
              if(text && text.length > 10){                
                TooltipHtml = <Tooltip placement="bottom" title={text}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip>;
                newText+="...";
              }
              let url='/realize/detail.html?id='+record.projectId
              return <p>
                <span><a href={url}>{newText}</a></span>
                {TooltipHtml}
              </p>
            }   
          },{
            title: '变现利率',
            dataIndex: 'apr',
            render(text) {
              return text+"%";
            },            
          },{
            title: '变现金额(元)',
            dataIndex: 'account',
          },{
            title: '变现完成时间',
            dataIndex: 'reviewTime',
            render(text) {
              
              return text ? Publiclib.formatDate(text,2) : "-";
              
            },           
          },{
            title:'操作',
            render: (text, record) => {

                      let link = "/member/myLoan/projectRepayment.html?type=myRealize&projectId="+record.projectId;
                       return <span className="tablelink"><a href={link}>还款计划</a></span>;

              },
          }];

const config = {single:true,datetime:true,select:false}
const config1 = {single:true,datetime:true,select:false,namestyle:true}

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="可变现" key="1">
                <TablelistOther config={config1} url='/member/myRealize/getRealizeAbleList.html' tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="变现中" key="2">
                <Tablelist config={config} url='/member/myRealize/getRealizingList.html' tableid="tablelist-second" columns={columns_second}/>
            </TabPane>
            <TabPane tab="已变现" key="3">
                <Tablelist config={config} url='/member/myRealize/getRealizedList.html' tableid="tablelist-third" columns={columns_third}/>
            </TabPane>                        
          </Tabs> 
        )
    }  
});

ReactDOM.render(<InviteIndex />,  document.getElementById("j-myrealize-list"));

ReactDOM.render(<Accountmenu current = {"5"}/>,  document.getElementById("j-sider-menu"));