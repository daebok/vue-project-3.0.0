import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs } from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import './benefitlist.less';

const type=["未使用","已使用","已过期","已作废"];

const columns_first = [{
            title: '面值',
            dataIndex: 'amount',
            width:195,
            render(text) {
              return <span className="red"><s>¥</s>{text}</span>;
            }            
          }, {
            title: '名称',
            dataIndex: 'ruleName',
          }, {
            title: '使用期限',
            dataIndex: 'createTime',
            render(text,record) {
              let useExpireTime = "";
              if(record.useExpireTime){
                useExpireTime = record.useExpireTime ? <span><span className="line">|</span>{Publiclib.formatDate(record.useExpireTime,2)}</span> : "";
                return <span className="tdspan">{Publiclib.formatDate(text,2)}{useExpireTime}</span>;
              }
              else
              {
                return <span>永久有效</span>
              }
            }            
          }, {
            title: '使用条件',
            dataIndex: 'useTenderMoney',
            width:220,
            render(text,record) {
              let textString = record.useProjectType ? record.useProjectTypeName : "所有产品";
              const string = record.useProjectTypeName ? "<br/>(适用于" + textString + ")" : "";
              return <span className="tdspan">投资满{text}元可用<span dangerouslySetInnerHTML={{__html: string}}></span></span>;
            }            
          }, {
            title: '使用情况',
            dataIndex: 'status',
            width:94,
            render(text) {
              return type[text];
            }
          }];

const columns_second = [{
            title: '面值',
            dataIndex: 'upApr',
            render(text) {
              return <span className="couponsbg">+{text}<s>%</s></span>;
            }
          }, {
            title: '名称',
            dataIndex: 'ruleName',
          }, {
            title: '使用期限',
            dataIndex: 'createTime',
            render(text,record) {
              let useExpireTime = "";
              if(record.useExpireTime){
                useExpireTime = record.useExpireTime  ? <span><span className="line">|</span>{Publiclib.formatDate(record.useExpireTime,2)}</span> : "";
                return <span className="tdspan">{Publiclib.formatDate(text,2)}{useExpireTime}</span>;
              }
              else
              {
                return <span>永久有效</span>;
              }              
            }            
          }, {
            title: '使用条件',
            dataIndex: 'useTenderMoney',
            render(text,record) {
              const string = record.useProjectTypeName ? "<br/>(适用于" + record.useProjectTypeName + ")" : "";
              return <span className="tdspan">投资满{text}元可用<span dangerouslySetInnerHTML={{__html: string}}></span></span>;
            }            
          }, {
            title: '使用情况',
            dataIndex: 'status',
            render(text) {
              return type[text];
            }
          }];



const config_first = {single:true,datetime:false,select:false}
const config_second = {single:true,datetime:false,select:false}
const singleArr = ["未使用","已使用","已失效"];

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="红包" key="1">
                <Tablelist config={config_first} url='/member/coupon/userRedenvelopeList.html' singleArr = {singleArr} tableid="tablelist-first" columns={columns_first}/>
            </TabPane>
            <TabPane tab="加息券" key="2">
                <Tablelist config={config_second} url='/member/coupon/userRateCouponList.html' singleArr = {singleArr} tableid="tablelist-second" columns={columns_second}/>
            </TabPane>                       
          </Tabs>
        )
    }  
});

ReactDOM.render(<InviteIndex />, document.getElementById("j-benefit"));

ReactDOM.render(<Accountmenu current = {"6"}/>,  document.getElementById("j-sider-menu"));