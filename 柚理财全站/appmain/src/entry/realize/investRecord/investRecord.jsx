import React from 'react';
import ReactDOM from 'react-dom';
import './investRecord.less';
import { Modal, Pagination } from "antd";
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

class InvestRecordItem extends React.Component{
	render(){
		let item = this.props.item;
	    //判断是否来自移动端
	    let mobileHtml = item.investChannel=="1" ? "" : <em className="iconfont font-f95a28">&#xe607;</em>;
	    //金额=项目总额-已投金额
	    let accountNo = item.account - item.accountYes;
	    //投资时间
	    let createTime = Publiclib.formatDate(item.createTime,2);
	    //投资方式
	    let investType = Publiclib.getInvestType(item.investType);
		let useAwardHtml = "未使用";
		if(item.redenvelopeAmount > 0){
			useAwardHtml = "使用红包"+item.redenvelopeAmount+"元";
		}
		if(item.rateCouponVal > 0){
			if(useAwardHtml.length>3){
				useAwardHtml += ",加息券"+item.rateCouponVal+"%";
			}else{
				useAwardHtml  = "使用加息券"+item.rateCouponVal+"%";
			}
		}
		return (
			<li>
		        <dl>
		          <dt>{item.userName} {mobileHtml}</dt>
		          <dd className="dd1">{item.amount}</dd>
		          <dd className="dd2">{createTime}</dd>
		          <dd className="dd3">{investType}</dd>
					<dd className="dd5">{useAwardHtml}</dd>
		        </dl>
		    </li>
		)
	}
};

class InvestRecordList extends React.Component{
	render(){
		return (
	      <ul>
	        {this.props.data.map(function(item, k) {
	          return <InvestRecordItem key = {k} item = {item} />
	        })}              
	      </ul>
	    );
	}
};

export default class InvestRecord extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this._pageHander = this.pageHander.bind(this);
		this.state = {
			data: null,
		};
	}
	componentDidMount(){
		this._getData(1);
	}
	getData(page){		
		let self = this;	
		$.ajax({
			url: '/invest/recordList.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": $("#projectId").val(),"page.page": page, "page.pageSize": 15}	
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
	pageHander(page){		
		this._getData(page);
	}
	render() {
		let data = this.state.data;
		if(!data){return false;}
		let investDetailHtml = <div className="invest-detail-list clearfix">
			<div className="noText">
				<span className="pic"></span>
				<p>暂无投资记录</p>
			</div>
		</div>;	
		if(data.page.rows){
			investDetailHtml = <div className="invest-detail-list clearfix">
				<div className="sort clearfix">
					<ul className="clearfix">
					<li className="w1"><span>投资人</span></li>
						<li className="w2"><span>投资金额（元）</span></li>
						<li className="w3"><span>投资时间</span></li>
						<li className="w4"><span>投资方式</span></li>
						<li><span>使用奖励</span></li>                            
					</ul>
				</div>
				<div className="result clearfix">
					<InvestRecordList data={data.page.rows} />
				</div>
				<div className="page page-center mt30"><Pagination defaultCurrent={data.page.page} pageSize={data.page.pageSize} total={data.page.count} onChange={this._pageHander} /></div>
			</div>;
		}
		return (
			<div>
				{investDetailHtml}
			</div>
			
		);
	}
}