import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import {Tabs,Tooltip} from 'antd';
const TabPane = Tabs.TabPane;
import Accountmenu from '../../../component/accountmenu/menu';
import Tablelist from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import './benefitlist.less';

const type=["未使用","已使用","已失效","已作废"];

function getTypeName(id){
  var ids = id.split(","),
  names = "";
  // for(var n=0;n<ids.length;n++){
  //   for(var i=0;i<typeName.length;i++){
  //     if(typeName[i].id == ids[n]){
  //       var sign = ",";
  //       if(n == 0){
  //         sign = "";
  //       }
  //       names+= (sign + typeName[i].typeName);
  //     }
  //   }
  // }
  $.each(ids,function(k, v) {
      $.each(typeName,function(index, el) {
        if(typeName[index].id == ids[k]){
          var sign = ",";
          if(k == 0){
            sign = "";
          }
          names+= (sign + typeName[index].typeName);
        }    
      });
  });
  return names;
}

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
                useExpireTime = record.useExpireTime ? <span><span className="line">至</span>{Publiclib.formatDate(record.useExpireTime,2)}</span> : "";
                return <span className="tdspan">{Publiclib.formatDate(text,2)}{useExpireTime}</span>;
              }
              else
              {
                return <span>长期有效</span>
              }
            }            
          }, {
            title: '使用条件',
            dataIndex: 'useTenderMoney',
            width:220,
            render(text,record) {
                let textString = record.useProjectType ? getTypeName(record.useProjectType) : "所有产品",
                TooltipHtml = "",
                html = textString,
                newText =  ")";

                if(textString && textString.length > 8){
                  TooltipHtml = <span><Tooltip placement="bottom" title={textString}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip> )</span>;
                  newText="...";
                  html = textString.substr(0,8);
                }

                let string =  "(适用于" + html + newText;
                if(text==0||text==''){
                    text=1
                }
                return <span className="tdspan">投资满{text}元可用<span style={{display:"block"}}>{string}{TooltipHtml}</span></span>;
            }            
          }, {
            title: '使用情况',
            dataIndex: 'status',
            width:94,
            render(text,record) {
                var html;
                if(text==0){
                    html=type[text]
                }else if(text==1){
                    html=<div><p>{Publiclib.formatDate(record.useTime,2)}</p><p>{type[text]}</p></div>
                }else if(text==2){
                    html=<div><p>{Publiclib.formatDate(record.useExpireTime,2)}</p><p>{type[text]}</p></div>
                }else{
                    html=type[text]
                }
              return html;
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
                useExpireTime = record.useExpireTime  ? <span><span className="line">至</span>{Publiclib.formatDate(record.useExpireTime,2)}</span> : "";
                return <span className="tdspan">{Publiclib.formatDate(text,2)}{useExpireTime}</span>;
              }
              else
              {
                return <span>长期有效</span>;
              }              
            }            
          }, {
            title: '使用条件',
            dataIndex: 'useTenderMoney',
            render(text,record) {
                let textString = record.useProjectType ? getTypeName(record.useProjectType) : "所有产品",
                TooltipHtml = "",
                html = textString,
                newText =  ")";

                if(textString && textString.length > 8){
                  TooltipHtml = <span><Tooltip placement="bottom" title={textString}><a href="javascript:;" className="iconfont tooltip-icon">&#xe664;</a></Tooltip> )</span>;
                  newText="...";
                  html = textString.substr(0,8);
                }

                let string =  "(适用于" + html + newText;
                if(text==0||text==''){
                    text=1
                }
                return <span className="tdspan">投资满{text}元可用<span style={{display:"block"}}>{string}{TooltipHtml}</span></span>;
            }            
          }, {
            title: '使用情况',
            dataIndex: 'status',
            render(text,record) {
                var html;
                if(text==0){
                    html=type[text]
                }else if(text==1){
                    html=<div><p>{Publiclib.formatDate(record.useTime,2)}</p><p>{type[text]}</p></div>
                }else if(text==2){
                    html=<div><p>{Publiclib.formatDate(record.useExpireTime,2)}</p><p>{type[text]}</p></div>
                }else{
                    html=type[text]
                }
                return html;
            }
          }];



const config_first = {single:true,datetime:false,select:false}
const config_second = {single:true,datetime:false,select:false}
const singleArr = ["未使用","已使用","已失效"];
const singleArrId = ["0","1","4"];
const singNoAll = true;

let InviteIndex = React.createClass({
  render(){
      return (
          <Tabs defaultActiveKey="1">
            <TabPane tab="红包" key="1">
                <Tablelist config={config_first} url='/member/coupon/userRedenvelopeList.html' singleArr = {singleArr} singNoAll = {singNoAll} singleArrId = {singleArrId} tableid="tablelist-first" columns={columns_first} pageSize = {5} />
            </TabPane>
            <TabPane tab="加息券" key="2">
                <Tablelist config={config_second} url='/member/coupon/userRateCouponList.html' singleArr = {singleArr} singNoAll = {singNoAll} singleArrId = {singleArrId} tableid="tablelist-second" columns={columns_second} pageSize = {5} />
            </TabPane>                       
          </Tabs>
        )
    }  
});

ReactDOM.render(<InviteIndex />, document.getElementById("j-benefit"));

ReactDOM.render(<Accountmenu current = {"6"}/>,  document.getElementById("j-sider-menu"));