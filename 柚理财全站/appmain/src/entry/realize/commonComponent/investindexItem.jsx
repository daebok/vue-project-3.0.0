import React from 'react';
import { Publiclib } from '../../../common/common';

class InvestStatus extends React.Component{
	render(){
		let item = this.props.item;
		let html = "";
		let endTime = null;//售罄用时
		//链接
		let link = "/realize/detail.html?id="+ item.id;
		//标识

			//售罄用时 = 开始时间-系统时间
			endTime = Publiclib.formatDatetype2(item.saleTime,item.raiseEndTime);

		switch (item.status)
		{
			case "4":
				if(this.props.item.remainAccount=='0'){
					html = <div className="item-status" style={{'marginTop':'34px','marginLeft':'38px'}}>
						<em style={{'fontStyle':'normal'}}>已售罄</em>
					</div>;
				}else {
					html = <a href={link} target="_blank" className="investment">立即投资</a>;
				}
				break;
			case "5":
				html = <div className="item-status">
					<strong>售罄用时</strong>
					<p>{endTime}</p>
				</div>;
				break;
			case "6":
			case "8":
			case "87":
			case "88":
			case "90":
				html = <div className="item-status">
					<strong>回款进度</strong>
					<p>共{item.totalPeriod}期 已还{item.repayedPeriod==undefined?'0':item.repayedPeriod}期</p>
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
				html = <p className="item-status"><em>剩余</em>0<em>元</em></p>;
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
	    let noviceHtml = item.novice=="1" ? <span className="new"></span> : "";
		let projectName = item.projectName;//产品名称
		//以下标的状态跳转到详情页面
		let link = "/realize/detail.html?id=" + item.id;
		projectName = <a href={link} target="_blank">{projectName}</a>;
		return (
			<li>
		        <dl>
		          <dt>{projectName}{noviceHtml}</dt>
		          <dd className="profit">{item.apr}<em>%</em></dd>
		          <dd className="timelimit"><span className="f16">{item.timeLimit}</span>天</dd>
		          <dd className="minmoney"><span className="f16">{item.lowestAccount}</span><span>元</span></dd>
		          <MoneyStatus item={item}/>
		          <InvestStatus item={item} systemTime={this.props.systemTime}/>
		        </dl>
		    </li>
		)
	}
};
