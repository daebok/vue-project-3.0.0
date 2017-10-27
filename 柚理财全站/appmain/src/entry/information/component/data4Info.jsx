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

export default class Data4Info extends React.Component{
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
				<div className="data4Info">
					<div className="title">业务平均数</div>
					<ul className="clearfix">
						<li><em className="iconfont">&#xe60c;</em>1651948000<p>人均累计借款（元）</p></li>
						<li><em className="iconfont">&#xe674;</em>1651948000<p>人均借款额度（元）</p></li>
						<li><em className="iconfont">&#xe65d;</em>1651948000<p>人均累计投资（元）</p></li>
						<li><em className="iconfont">&#xe666;</em>1651948000<p>笔均投资额度（元）</p></li>
						<li><em className="iconfont">&#xe60d;</em>1651948000<p>平均满标时间</p></li>
					</ul>
				</div>
			</div>
		);
	}
}

