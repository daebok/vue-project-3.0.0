import React from 'react';
import { Publiclib } from '../../../common/common';
import { Modal } from "antd";

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v, fn) {
  Modal.success({
    title: '提醒',
    content: v,
	onOk: fn,
  });
}

class InvestStatus extends React.Component{
	constructor(props){
		super(props);		
		this._getInvestBespeak = this.getInvestBespeak.bind(this);
	}	
	//预约
	getInvestBespeak(e){		
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
					self.props.getInvestBespeak();
				});
			} else {
				
				if(data.redirect){
					window.location.href = "/user/login.html?redirectURL=/invest/detail.html?id=" + self.props.item.id;
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
	render(){
		let item = this.props.item;
		let html = "";
		let endTime = null;//售罄用时
		//链接
		let link = "/invest/detail.html?id="+ item.id;		
		//标识
		let flag = item.saleTime >= this.props.systemTime;
		if(!flag){
			//售罄用时 = 开始时间-系统时间
			endTime = Publiclib.formatDatetype2(item.saleTime,item.raiseEndTime);
		}		
		switch (item.status)
		{
			case "4":
			case "49":
				if(flag && item.bespeak == "0"){
					html = <div className="item-status2 bespeakBox">
						<p className="itemText">{Publiclib.formatDateInvest(item.saleTime)}开售</p>
						<a href="javascript:;" data-id={item.id} className="iconfont bespeakBtn" onClick={this._getInvestBespeak}>&#xe60d;</a>
					</div>
				} else if(flag && item.bespeak == "1"){
					html = <div className="item-status1">
						<p>{Publiclib.formatDateInvest(item.saleTime)}开售</p>
						<span className="bespeak-tips"></span>
					</div>
				} else {
					if(this.props.item.remainAccount=='0'){
						html = <div className="item-status" style={{'marginTop':'34px','marginLeft':'38px'}}>
							<em style={{'fontStyle':'normal'}}>已售罄</em>
						</div>;
					}else{
					html = <a href={link} className="investment">立即投资</a>
					}
				}													
				break;
			case "5":
				html = <div className="item-status" style={{'marginTop':'34px','marginLeft':'38px'}}>
					<em style={{'fontStyle':'normal'}}>已售罄</em>
				</div>;
				break;
			case "6":
			case "8":
			case "87":
			case "88":
			case "90":
				html = <div className="item-status">
					<strong>回款进度</strong>
					<p>共{item.periods}期 已还{item.repayedPeriods}期</p>
				</div>;
				break;			
			case "9":
			case "91":
				html = <div className="item-status">
					<strong>最后回款日</strong>
					<p>{item.lastRepayTime ? Publiclib.formatDate(item.lastRepayTime) : "-"}</p>
				</div>;
				break;			
		}		
		return (
			<dd className="status">{html}</dd>
		)
	}
}

class MoneyStatus extends React.Component{	
	render(){
		let item = this.props.item;
		let html = "";
		//金额=项目总额-已投金额
	    //let accountNo = item.account - item.accountYes;
	    let accountNo = item.remainAccount;
		switch (item.status)
		{
			case "4":
			case "49":
				html = <span className="f16"><em>剩余</em>{accountNo}<em>元</em></span>;
				break;
			case "5":
				html = <p className="f16"><em>剩余</em>0<em>元</em></p>;
				break;
			case "6":
			case "8":
			case "87":
			case "88":
			case "90":
				html = <p className="item-status">回款中</p>;
				break;			
			case "9":
			case "91":
				html = <p className="item-status">已回款</p>;
				break;			
		}				
		return (
			<dd className="money">{html}</dd>
		)
	}
}

export default class InvestindexItem extends React.Component{
	render(){
		let item = this.props.item;
    //判断新手标
    let noviceHtml = item.novice == "1" ? <span className="tags yellowIcon">新客</span> : "";		
    let transferHtml = item.bondUseful == "1" ? <span className="tags transfer">可转让</span> : "";
    let realizeFlag = item.realizeUseful == "1" ?	<span className="tags realize">可变现</span> : "";
		let lockTimeLimit = "";
		// if(item.lockTimeLimit){
		// 	lockTimeLimit = <s>锁定期{item.lockTimeLimit}天</s>;
		// }		
		//以下标的状态跳转到详情页面				
		let link = "/invest/detail.html?id=" + item.id;		
		//加息
		let addAprHtml = '';		
		if(item.addApr){
			addAprHtml = <em>+{item.addApr}%</em>;
		}
		//let itemName = item.projectName + ;
		// if(item.projectName.length>20){
		// 	itemName=item.projectName.substr(0,17)+'...'
		// }else{
		// 	itemName=item.projectName
		// }
		return (
				<li>
		        <dl>
		          <dt><a href={link} title={item.projectName + item.projectNo}>{item.projectName}-{item.projectNo}</a>{noviceHtml}{transferHtml}{realizeFlag}</dt>
		          <dd className="profit">{item.apr}<em>%</em>{addAprHtml}</dd>
		          <dd className="timelimit"><span className="f16">{item.timeLimit}</span>{item.timeType == "0" ? "个月" : "天"}</dd>
		          <dd className="minmoney"><span className="f16">{item.lowestAccount}</span><span>元</span></dd>		          
				  		<MoneyStatus item={item}/>
		          <InvestStatus item={item} systemTime={this.props.systemTime} getInvestBespeak={this.props.getInvestBespeak}/>				  
		        </dl>
		    </li>
		)
	}
};