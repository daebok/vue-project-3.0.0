import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
import { Modal, Pagination } from "antd";
import BasicNews from './component/basicNews';
import Institutions from './component/institutions';
import MajorNews from './component/majorNews';
import { Publiclib } from '../../common/common';

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

let getinfo = function(){
    $.ajax({
      url: '/member/account/getAccountInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
        if( !data.result ){
            error(data.msg,data.url);
            return false;
        }

        let option = {
    tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)"
    },
    series: [
        {
            name:'访问来源',
            type:'pie',
            selectedMode: 'single',
            radius: [0, '30%'],

            label: {
                normal: {
                    position: 'inner'
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            }
        },
        {
            name:'访问来源',
            type:'pie',
            radius: ['40%', '55%'],

            data:[
                {value:335, name:'直达'},
                {value:310, name:'邮件营销'},
                {value:234, name:'联盟广告'},
                {value:135, name:'视频广告'},
                {value:1048, name:'百度'},
                {value:251, name:'谷歌'},
                {value:147, name:'必应'},
                {value:102, name:'其他'}
            ]
        }
    ]
};
    var myChart = echarts.init(document.getElementById('j-pie'));
    myChart.setOption(option);
  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}

let picinfo = function(){
  $( ".fancybox").fancybox();
}

class Columnlist extends React.Component{
  componentDidMount(){
   getinfo();
   picinfo();
  }
  render(){
      return (
        <div className="organizationInfo">
          <div className="banner"></div>
          <div className="navigation">
            <ul>
              <li><a href="" className="active">从业结构信息</a></li>
              <li><a href="">平台运营数据</a></li>
              <li><a href="">合规信息</a></li>
            </ul>
          </div>
          <div className="con1 clearfix">
            <div className="left"></div>
            <div className="right">
              <div className="title">重庆益多宝信息技术有限公司</div>
              <p>重庆益多宝信息技术有限公司成立于2014年12月，注册资金3000万元。经营范围：计算机软硬件领域内的技术咨询、技术开发、技术转让、技术服务；计算机网络技术咨询服务；利用企业自有资金进行对外投资；资产管理；企业管理咨询。</p>
              <p> 顺势宝依靠强大的专业化团队、成熟的项目评审体系、严格的风控体系，专注于寻找优质项目，控制投资风险，开创性的提出了八重风控体系，将投资者的投资本金、收益的风险降到最低。为投资者提供更有针对性的金融服务，最终为投资者的资产实现保值、增值。</p>
            </div>
          </div>
          <div className="con2"></div>
          <div className="con3"><BasicNews /></div>
          <div className="con4"><Institutions /></div>
          <div className="con5"><MajorNews /></div>
        </div>
        )   
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("organizationInfo"));
