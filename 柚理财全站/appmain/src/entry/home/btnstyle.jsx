import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Icon } from "antd";
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

export default class Btnstyle extends React.Component{
	constructor(props){
    	super(props);
    	this._getBespeak = this.getBespeak.bind(this);    
	}
	getBespeak(e){
		let target = e.target;		
		let projectId = $(target).attr("data-id");
		let self = this;
		$.ajax({
			url: '/index/investBespeak.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": projectId},
		})
		.done(function(data) {		
			if(data.result){
				success(data.msg, function(){
					self.props.getBespeak();
				});
			} else {
				if(data.redirect){
					window.location.href = "/user/login.html?redirectURL=/invest/detail.html?id=" + self.props.id;
				}
				else
				{
					error(data.msg);
				}	
			}			
		})
		.fail(function() {
			//error("网络异常，请重试！");
		})
	}
	render() {
		let id = this.props.id;
		let saleTime = this.props.saleTime;
		let nowTime = this.props.nowTime;
		let accountYes = this.props.accountYes;
		let account = this.props.account;
		let bespeakStatus = this.props.bespeakStatus;
		let link = "/invest/detail.html?id="+id;
		let saleTimeval = Publiclib.formatDateInvest(saleTime);
		let timeHtml="";
		if(bespeakStatus==0){
			timeHtml =<div className="presale clbtn" onClick={this._getBespeak}><span className="word">{saleTimeval}</span><span className="img hide"><em className="iconfont" data-id={id}>&#xe60d;</em></span></div>;	
		}
		else{
			timeHtml =<div className="presale"><span className="word">{saleTimeval}</span></div>;	
		}
		if(nowTime>saleTime){
			if(account>accountYes){
				timeHtml =  <div><a href={link} className="btn">立即投资</a></div>
			}
			else{
				timeHtml =  <div className="btn exhausted">已售罄</div>
			}
		}
		return ( 
			<div>{timeHtml}</div>
		) 
		
	}
}