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

function Imgchange(){
  $("body").on("mouseover", ".con li img", function(){
    $(this).removeClass("gray")
  });

  $("body").on("mouseout", ".con li img", function(){
    $(this).addClass("gray")
  });
}

export default class Partner extends React.Component{
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
			data: {"code": "partner"}			
		})
		.done(function(data) {
			self.setState({data: data});
			Imgchange();			
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
				<div className="partner">
					<div className="title clarfix">合作伙伴<a href="/column/partner.html" className="clearfix">查看更多<em className="iconfont">&#xe636;</em></a></div>
					<ul className="clearfix con">
						{data.list.map(function(item, k){									
							return <li key={k}><a target="_blank"><img src={item.picPath} className="gray"/></a></li>
						})}
					</ul>
				</div>
			</div>
		);
	}
}