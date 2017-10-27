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
		let numStr = String(data.investTotal);
		let num = numStr.split("");
		let str = "";
		for(var i=0;i<num.length;i++){
			str+="<span>"+num[i]+"</span>"
		}
		return (
			<div>
				<div className="dataStatistics clearfix">
					<div className="con clearfix">
						<div className="dataStatistics-fl site-col-12 clearfix">
							<span>平台累计投资金额</span><font><div dangerouslySetInnerHTML={{__html: str}}></div></font><span>元</span>
						</div>
						<div className="dataStatistics-fr site-col-11">
							共有 <em>{registerTotal}</em> 位聪明的理财人已加入，已为他们赚取收益 <em>{totalEarnAmount}</em>元
						</div>
					</div>
				</div>
			</div>
		);
	}
}