import React from 'react';
import ReactDOM from 'react-dom';
import './returnMoney.less';
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

class ReturnMoneyItem extends React.Component{
	render(){
		let item = this.props.item;
		let paytime = Publiclib.formatDate(item.repayTime)
		return (
			<li>
		        <dl>
		          <dt className="dd1">{item.period}/{item.periods}</dt>
		          <dd className="dd2">{paytime}</dd>
		          <dd className="dd3">{item.payment}</dd>
		          <dd className="dd4">{item.repaymentTypeStr}</dd>
							<dd className="dd5">{item.realRepayTimeStr}</dd>
							<dd className="dd6">{item.payedAmountStr}</dd>
							<dd className="dd7">{item.lateStr}</dd>
		        </dl>
		    </li>
		)
	}
};

class ReturnMoneyList extends React.Component{
	render(){
		let data = this.props.data;	
		return (
	      <ul>
	        {this.props.data.map(function(item, k) {
						return <ReturnMoneyItem key = {k} item = {item} />
					})}        
	      </ul>
	    );
	}
};

export default class ReturnMoney extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this._pageHander = this.pageHander.bind(this);
		this.state = {
			data: null,
		};
	}
	componentDidMount(){
		this._getData();
	}
	getData(page){		
		let self = this;	
		$.ajax({
			url: '/project/repaymentList.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": $("#projectId").val(),"page.page": page, "page.pageSize": 10}	
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
		let returnMoneyHtml = <div className="returnMoney-detail-list clearfix">
			<div className="noText">
					<span className="pic"></span>
					<p>该项目尚未满额复审，暂未产生还款数据</p>
			</div>
		</div>
		if(data.page.rows){
			returnMoneyHtml = <div className="returnMoney-detail-list clearfix">
					<div className="sort clearfix">
						<ul className="clearfix">
						<li className="w1"><span>期数</span></li>
							<li className="w2"><span>预期回款日期</span></li>
							<li className="w3"><span>预期回款金额（元）</span></li>
							<li className="w4"><span>类型</span></li>
							<li className="w5"><span>实际回款日期</span></li>
							<li className="w6"><span>实际回款金额（元）</span></li>
							<li className="w7"><span>是否逾期</span></li>                       
						</ul>
					</div>
					<div className="result clearfix">
						<ReturnMoneyList data={data.page.rows} />
					</div>
					<div className="page page-center mt30"><Pagination defaultCurrent={data.page.page} pageSize={data.page.pageSize} total={data.page.count} onChange={this._pageHander} /></div>
			</div>;
		}		
		return (		
			<div>{returnMoneyHtml}</div>
		);
	}
}