import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";
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

export default class dataStatistics extends React.Component{
	 constructor(props){
	 	super(props);
	 	this._getData = this.getData.bind(this);
	 	this.state = {
	 		data: null,
	 	};
	 }
	 componentDidMount(){
	 	this._getData();
	 }
	 getData(){
	 	let self = this;		
	 	$.ajax({
	 		url: '/index/countInfo.html',
	 		type: 'POST',
	 		dataType: 'json',			
	 	})
	 	.done(function(data) {
	 			self.setState({data:data});		
	 	})
	 	.fail(function() {
	 		//error("网络异常，请重试！");
	 	})		
	}
	render() {
		let data = this.state.data;
		if(!data){return false;}
		let registerTotal = Publiclib.numberFormat(data.registerTotal);
		let totalEarnAmount = Publiclib.moneyFormat(data.totalEarnAmount);
		//let numStr = String(data.investTotal);
		let numStr = data.investTotal<10000 ? String(data.investTotal):String((data.investTotal/10000).toFixed(2));
		let num = numStr.split("");
		let str = "";
		let unit = data.investTotal<10000 ? "元":"万元";
		for(var i=0;i<num.length;i++){
			str+="<span>"+num[i]+"</span>"
		}
		return (
			<div>
				<div className="dataStatistics clearfix">
					<div className="logoIndex"></div>
					<div className="con clearfix">
							平台累计投资金额<font><div dangerouslySetInnerHTML={{__html: str}}></div></font>{unit}　　共有 <em>{registerTotal}</em> 位聪明的理财人已加入，已为他们赚取收益 <em>{totalEarnAmount}</em>元
					</div>
				</div>
			</div>
		);
	}
}