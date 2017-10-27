import React from 'react';
import ReactDOM from 'react-dom';
import InvestindexList from './investindexList';
import { Modal, Pagination } from "antd";

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

export default class InvestListComponent extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this._pageHander = this.pageHander.bind(this);
		this._reSetPage = this.reSetPage.bind(this);
		this.state = {
			data: null,
			current: null,
		};
	}
	componentDidMount(){            
		this._getData({"page.page": 1, "page.pageSize": 10});        
	}
	getData(dataObj){		
		let self = this;               
		$.ajax({
			url: '/realize/projectList.html',
			type: 'POST',
			dataType: 'json',
			data: dataObj,
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
	//分页
	pageHander(page){
		//筛选条件
		let obj = this.props.getSearchConditionOpts($("#searchOperations"));		
		obj["page.page"] = page;		
		//获取列表数据
		this._getData(obj);
		//设置当前分页
		this._reSetPage(this.state.current);
	}
	//设置当前分页
	reSetPage(page){				
		this.setState({current: page});
	}
	render() {
		let data = this.state.data;        
		if(!data){return false;}		
		//系统时间
		let systemTime = data.systemTime;
		//当前分页
		let current = !this.state.current ? data.List.page : this.state.current;
		let pageHtml = "";
		if(data.List.rows){
			pageHtml = <Pagination current={data.List.page} pageSize={data.List.pageSize} total={data.List.count} onChange={this._pageHander} />;
		}		
		return (
			<div className="invest-index-cont clearfix">
				<div className="sort clearfix">
					<ul className="clearfix">
					<li className="w1"><span>产品名称</span></li>
						<li className="w2"><span>预期年化收益率</span></li>
						<li className="w3"><span>产品期限</span></li>
						<li className="w3"><span>起投金额</span></li>
						<li><span>金额</span></li>                            
					</ul>
				</div>
				<div className="result clearfix">
					<InvestindexList data = {data.List.rows} systemTime={systemTime} />
				</div>
				<div className="page page-center mt30">{pageHtml}</div>
			</div>
		);
	}
}