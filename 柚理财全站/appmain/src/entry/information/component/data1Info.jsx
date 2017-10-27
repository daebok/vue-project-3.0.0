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

export default class Data1Info extends React.Component{
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
	 		url: '/index/getHomeInvest.html',
	 		type: 'POST',
	 		dataType: 'json',			
	 	})
		.done(function(data) {
			if(data.result){
	 			self.setState({data: data});
	 		} else {
	 			error(data.msg);
	 		}		
	 	})
	 	.fail(function() {
	 		//error("网络异常，请重试！");
	 	})		
	 }
	render() {
		let data = this.state.data;
		if(!data){return false;}
		return (
			<div>
				<div className="data1Info">
					<ul className="clearfix">
						<li className="clearfix">
							<div className="left"><em className="iconfont iconfont1">&#xe666;</em></div>
							<div className="right">
								525649<p>交易总金额（万元）</p>
							</div>
						</li>
						<li className="clearfix">
							<div className="left"><em className="iconfont iconfont2">&#xe65d;</em></div>
							<div className="right">
								525649<p>交易总笔数（笔）</p>
							</div>
						</li>
						<li className="clearfix">
							<div className="left"><em className="iconfont iconfont3">&#xe674;</em></div>
							<div className="right">
								525649<p>借款人数（人）</p>
							</div>
						</li>
						<li className="clearfix" style={{"margin-right":"0px"}}>
							<div className="left"><em className="iconfont iconfont4">&#xe64a;</em></div>
							<div className="right">
								525649<p>投资人数（人）</p>
							</div>
						</li>
					</ul>
				</div>
			</div>
		);
	}
}

