import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";
import { Publiclib } from '../../../common/common';

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
	let box = $(".exchangeListcon ul");
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
	$(".exchangeListcon").mouseenter(function(){
       clearInterval(timer);
    })
    $(".exchangeListcon").mouseleave(function(){
       timer=setInterval(function(){
	    slideTop(box);
		}, 3000 )
    })
}


export default class ExchangeList extends React.Component{
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
	 		url: '/scoreshop/getNewOrders.html',
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
		let avatarPhoto = $(".serverUrl").val();
		if(!data){return false;}
		return (
			<div className="exchangeLists">
		      <div className="exchangeListcon">
						<ul>
							{data.data.map(function(item, k){
								let times = Publiclib.formatDatetype1(item.createTime,item.nowTime);
								let avatarPhotoUrl = avatarPhoto+item.avatarPhoto;
								return <li key={k} className="clearfix"><div className="left"><img src={avatarPhotoUrl}/></div><div className="right"><p><em>{item.userName.replace(/(\d{3})\d{6}(\d{4})/,"$1****$2")}</em> </p>成功兑换了<font>{item.goodsName}</font></div></li>
							})}
						</ul>
		      </div>
			</div>
		);
	}
}
