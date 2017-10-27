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

export default class Data7Info extends React.Component{
	constructor(props) {
	    super(props);
	    this._getTimeinfo = this.getTimeinfo.bind(this);
  	}
	componentDidMount(){
	 	this._getTimeinfo();
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
    var myChart = echarts.init(document.getElementById('investAccountPie'));
    myChart.setOption(option);
  })
  .fail(function() {
    //error('网络异常，请重试！');
  });
}

	render() {
		return (
			<div>
				<div className="data7Info clearfix">
					<div className="left">
                        <div className="title">投资金额渠道分布</div>
                        <div className="leftPie" id="investAccountPie"></div>
                    </div>
                    <div className="right">
                        <div className="title">移动端数据</div>
                        <ul className="clearfix">
                            <li className="clearfix"><em className="iconfont iconfont1">&#xe60f;</em><div className="word"><font>1498129,684</font><p>交易总金额（万元）</p></div></li>
                            <li className="clearfix"><em className="iconfont iconfont2">&#xe668;</em><div className="word"><font>1498129,684</font><p>交易总笔数（笔）</p></div></li>
                            <li className="clearfix"><em className="iconfont iconfont3">&#xe674;</em><div className="word"><font>1498129,684</font><p>借款人数（个）</p></div></li>
                            <li className="clearfix"><em className="iconfont iconfont4">&#xe615;</em><div className="word"><font>1498129,684</font><p>投资人数（个）</p></div></li>
                        </ul>
                    </div>
				</div>
			</div>
		);
	}
}

