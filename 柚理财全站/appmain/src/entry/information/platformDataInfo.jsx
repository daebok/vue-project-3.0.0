import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
import { Modal, Pagination } from "antd";
import { Publiclib } from '../../common/common';

//数据总和
import Data1Info from './component/data1Info';
//业务总览
import Data2Info from './component/data2Info';
//产品分布
import Data3Info from './component/data3Info';
//业务平均数
import Data4Info from './component/data4Info';
//待还数据
import Data5Info from './component/data5Info';
//违约数据
import Data6Info from './component/data6Info';
//投资金额渠道分布/移动端数据
import Data7Info from './component/data7Info';
//投诉信息
import Data8Info from './component/data8Info';
//贷后信息
import Data9Info from './component/data9Info';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '提醒',
    content: v,
  });
}

let picinfo = function(){
  $( ".fancybox").fancybox();
}
              
class Columnlist extends React.Component{
  componentDidMount(){
   picinfo();
  }
  render(){
      return (
        <div className="platformDataInfo">
          <div className="banner"></div>
          <div className="navigation">
            <ul>
              <li><a href="">从业结构信息</a></li>
              <li><a href="" className="active">平台运营数据</a></li>
              <li><a href="">合规信息</a></li>
            </ul>
          </div>
          <Data1Info />
          <Data2Info />
          <Data3Info />
          <Data4Info />
          <Data5Info />
          <Data6Info />
          <Data7Info />
          <div className="dataFoot">
            <Data8Info />
            <Data9Info />
          </div>
        </div>
        )   
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("platformDataInfo"));
