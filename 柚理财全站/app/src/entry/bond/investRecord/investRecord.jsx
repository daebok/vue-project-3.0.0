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
	    //判断新手标
	    let noviceHtml = item.novice=="1" ? <span className="new"></span> : "";
	    //金额=项目总额-已投金额
	    let accountNo = item.account - item.accountYes;
	    //投资时间
	    let createTime = Publiclib.formatDate(item.createTime);	    
		return (
			<li>
		        <dl>
		          <dt>{item.userName}{noviceHtml}</dt>
		          <dd className="dd1">{item.amount}</dd>
		          <dd className="dd2">{createTime}</dd>		          
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
			url: '/bond/getBondInvestPage.html',
			type: 'POST',
			dataType: 'json',
			data: {"id": $("#id").val(),"page": page, "pageSize": 15}	
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
					<li className="w1"><span>受让人</span></li>
						<li className="w2"><span>受让金额（元）</span></li>
						<li className="w3"><span>受让成功时间</span></li>	                                                  
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