import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";

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

export default class Data3Info extends React.Component{
	constructor(props) {
	    super(props);
	    this._getTimeinfo = this.getTimeinfo.bind(this);
        this._getTypeinfo = this.getTypeinfo.bind(this);
  	}
	componentDidMount(){
	 	this._getTimeinfo();
        this._getTypeinfo();
	}
	getTimeinfo(){
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
    var myChart = echarts.init(document.getElementById('leftPie'));
    myChart.setOption(option);
  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}

getTypeinfo(){
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
                {value:335, name:'直达1'},
                {value:310, name:'邮件营销1'},
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
    var myChart = echarts.init(document.getElementById('rightPie'));
    myChart.setOption(option);
  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}
	render() {
		return (
			<div>
				<div className="data3Info clearfix">
					<div className="left">
                        <div className="title">产品期限分布</div>
                        <div className="leftPie" id="leftPie"></div>
                    </div>
                    <div className="right">
                        <div className="title">产品类型分布</div>
                        <div className="rightPie" id="rightPie"></div>
                    </div>
				</div>
			</div>
		);
	}
}

