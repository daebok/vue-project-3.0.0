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

export default class Information extends React.Component{
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
			url: '/index/articleList.html',
			type: 'POST',
			dataType: 'json',
			data: {"code": "news",'pageSize':2}

		})
		.done(function(data) {
			self.setState({data: data});		
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
				<div className="title clearfix">
					<em className="news_title">动态资讯<i>第一时间了解行业详情动态</i></em>
					<a href="/column/sectionDetail.html?sectionCode=news" className="clearfix">查看更多<em className="iconfont">&#xe636;</em></a>
				</div>
				<ul>
					{data.list.map(function(item, k){
						let createTime = Publiclib.formatDate(item.createTime);
						let remark = Publiclib.subNameFun(item.remark,0,70);
						let link = "/column/articleDetail.html?uuid="+item.id+'&sectionCode='+item.sectionCode;
				    	return <li key={k} className="clearfix">
									<div className="picture"><img src={item.picPath}/></div>
									<div className="picturecon">
										<div className="title-p"><a href={link}>{item.title}</a></div>
										<p>{remark}</p>
										<span>{createTime}</span>
									</div>
								</li>
					})}
				</ul>
			</div>
		);
	}
}