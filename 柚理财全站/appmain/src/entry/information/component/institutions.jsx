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

export default class Institutions extends React.Component{
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
				<div className="institutions">
					<div className="title">第三方合作机构</div>
					<div className="content1">
						<span>顺势创行（上海）投资控股有限公司</span>
						<div className="mainBody">
							<em>中国（上海）自由贸易试验区加太路29号1幢楼东部夹层101-08室</em>
							<p>投资管理、实业投资、资产管理、创业投资，投资咨询、企业管理及咨询、财务咨询、商务咨询，房地产开发经营，建筑工程设计，企业策划，会务服务、礼仪服务，展览展示服务，市场信息咨询与调查（不得从事社会调查、社会调研、民意调查、民意测验），汽车及配件、金银制品的销售，电子商务（不得从事增值电信、金融业务），从事货物与技术的进出口业务，区内企业间的贸易及贸易代理、转口贸易。 【依法须经批准的项目，经相关部门批准后方可开展经营活动】。</p>
						</div>
					</div>
					<div className="content2">
						<div className="stitle">资金存管银行</div>
						<ul className="clearfix">
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
							<li><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></li>
						</ul>
					</div>
				</div>
			</div>
		);
	}
}

