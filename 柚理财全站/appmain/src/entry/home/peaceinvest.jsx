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
	 		url: '/index/getSureProfit.html',
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
			{data.data.map(function(item, k){
				let link = "/invest/index.html?projectTypeId="+item.typeId;
				let html = item.features ? <span className="tips">{item.features}</span> : "";
				return <div key={k} className="peaceinvest clearfix">
					<div className="title-w"></div>
					<div className="con clerafix">
							 <div className="peaceinvestList">
							<p className="title">{item.typeName} {html}</p>
							<div className="rate"><font>{item.apr}<em>%</em></font><p>预期年化收益率</p></div>
							<a href={link}>立即买入</a>
						</div>
					</div>
				</div>
				})}
			</div>
		);
	}
}