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

//文字隐现

function scrollfn(){
	let box = $(".index-notice ul");
	let timer;
	function slideTop(box){
        box.find("li:first").fadeIn("slow").show().siblings().fadeOut().hide();
        box.css("marginTop",0).find("li:first").appendTo(box);
    };
	timer=setInterval(function(){
	    slideTop(box);
	}, 3000 )
	$(".index-notice").mouseenter(function(){
       clearInterval(timer);
    })
    $(".index-notice").mouseleave(function(){
       timer=setInterval(function(){
	    slideTop(box);
		}, 3000 )
    })
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
			data: {"code": "notice",'pageSize':8}
		})
		.done(function(data) {
			self.setState({data: data});
			scrollfn();			
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
				<div className="index-notice clearfix">
					<div className="con">
						<div className="title"><em>网站</em>公告</div>
						<ul>
							{data.list.map(function(item, k){
								let	link = "/column/articleDetail.html?uuid="+item.id+"&sectionCode="+item.sectionCode;
								return <li key={k}><a href={link}>{item.title}</a></li>
							})}
						</ul>
						<a href="/column/sectionDetail.html?sectionCode=notice" className="more clearfix">查看更多<span className="iconfont">&#xe636;</span></a>
					</div>
				</div>
			</div>
		);
	}
}