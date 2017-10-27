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

function scrollfn(){
	let box = $(".rankingListcon ul");
	let oLiHeight = box.find("li:first").height();
	let timer;
	function slideTop(box){
        box.animate({"marginTop":-oLiHeight+"px"},800,function(){
            box.css("marginTop",0).find("li:first").appendTo(box);
        })
    };
	timer=setInterval(function(){
	    slideTop(box);
	}, 3000 )
	$(".rankingListcon").mouseenter(function(){
       clearInterval(timer);
    })
    $(".rankingListcon").mouseleave(function(){
       timer=setInterval(function(){
	    slideTop(box);
		}, 3000 )
    })
}


export default class Rankinglist extends React.Component{
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
				<div className="title">大家都在抢</div>
				<div className="rankingListcon">
				<div className="rankingListbox">
				<ul>
					{data.data.map(function(item, k){
						let amount = Publiclib.moneyFormat(item.amount);
						let times = Publiclib.formatDatetype1(item.createTime,item.nowTime);
						return <li key={k}><p><em>{item.userName.replace(/(\d{3})\d{6}(\d{4})/,"$1****$2")}</em> 投资了<font>¥{amount}</font></p><span>{times}</span></li>
					})}
				</ul>
				</div>
				</div>
			</div>
		);
	}
}