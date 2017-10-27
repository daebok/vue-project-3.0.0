import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Carousel } from "antd";

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

export default class Scroll extends React.Component{
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
			data: {"code": "scrollPic"}			
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
				<div className="banner-index clearfix">
					<Carousel autoplay effect="fade">
				    	{data.list.map(function(item, k){
				    		let link = item.url;
				    		let src = item.picPath;										
							return <div key={k} className="bannerImg" style={{backgroundImage:"url("+src+")"}}><a href={link}></a></div>
						})}
				    </Carousel>
				</div>		
		);
	}
}