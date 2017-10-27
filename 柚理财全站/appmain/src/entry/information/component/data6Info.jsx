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

export default class Data6Info extends React.Component{
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
                {value:310, name:'邮件营销'}
            ]
        }
    ]
};
    var myChart = echarts.init(document.getElementById('topPie'));
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
                {value:310, name:'邮件营销1'}
            ]
        }
    ]
};
    var myChart = echarts.init(document.getElementById('downPie'));
    myChart.setOption(option);
  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}
	render() {
		return (
			<div>
				<div className="data6Info clearfix">
					<div className="title">违约数据</div>
                    <div className="clearfix">
                        <div className="left">
                            <div className="topPie" id="topPie"></div>
                            <div className="downPie" id="downPie"></div>
                        </div>
                        <div className="right">
                            <ul className="clearfix topUl">
                                <li><em className="iconfont iconfont1">&#xe65f;</em><div className="word"><font>1%</font><p>累计违约率</p></div></li>
                                <li className="swcondLi"><em className="iconfont iconfont2">&#xe60d;</em><div className="word"><font>20000</font><p>借款逾期金额（元）</p></div></li>
                                <li className="threeLi"><em className="iconfont iconfont3">&#xe660;</em><div className="word"><font>10000</font><p>代偿金额（元）</p></div></li>
                            </ul>
                            <ul className="clearfix downUl">
                                <li>
                                    <div className="topLi clearfix">
                                        <span className="stitle">项目逾期率</span>
                                        <font><em>8.2</em>%</font>
                                    </div>
                                    <div className="downLi">
                                        <div className="jdBg"><div className="percent" style={{width:"10%"}}></div></div>
                                    </div>   
                                </li>
                                <li>
                                    <div className="topLi clearfix">
                                        <span className="stitle">项目逾期率</span>
                                        <font><em>8.2</em>%</font>
                                    </div>
                                    <div className="downLi">
                                        <div className="jdBg"><div className="percent" style={{width:"10%"}}></div></div>
                                    </div>   
                                </li>
                                <li>
                                    <div className="topLi clearfix">
                                        <span className="stitle">项目逾期率</span>
                                        <font><em>8.2</em>%</font>
                                    </div>
                                    <div className="downLi">
                                        <div className="jdBg"><div className="percent" style={{width:"10%"}}></div></div>
                                    </div>   
                                </li>
                                <li>
                                    <div className="topLi clearfix">
                                        <span className="stitle">项目逾期率</span>
                                        <font><em>8.2</em>%</font>
                                    </div>
                                    <div className="downLi">
                                        <div className="jdBg"><div className="percent" style={{width:"10%"}}></div></div>
                                    </div>   
                                </li>
                            </ul>
                        </div>
                    </div>
				</div>
			</div>
		);
	}
}

