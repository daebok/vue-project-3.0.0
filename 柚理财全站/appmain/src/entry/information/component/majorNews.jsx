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

export default class MajorNews extends React.Component{
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
				<div className="majorNews">
					<div className="title">重大事项信息</div>
					<ul>
						<li>
							<div className="timeTitle">
								时间<em>2016-10-24</em>
								事项<em>董、监、高变更情况</em>
							</div>
							<div className="changeCon clearfix">
								<span>内容</span><p>变更前：注册资本: 11829.25万人民币元; 股东: 杭州汉鼎宇佑股权投资合伙企业（有限合伙）, 1829.25万; 德清锦绣管理咨询合伙企业（有限合伙）, 2300万; 姚宏, 7700万;变更前：注册资本: 11829.25万人民币元; 股东: 杭州汉鼎宇佑股权投资合伙企业（有限合伙）, 1829.25万; 德清锦绣管理咨询合伙企业（有限合伙）, 2300万; 姚宏, 7700万;</p>
							</div>
						</li>
					</ul>
				</div>
			</div>
		);
	}
}

