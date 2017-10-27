import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";
import { Publiclib } from '../../../common/common';
import './productinfo.less';

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

export default class ProductInfo extends React.Component{
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
			url: "/realize/productInfo.html",
			type: 'POST',
			dataType: 'json',
			data: {"id": $("#investId").val()}
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
	render() {
		let data = this.state.data;
		if(!data){return false;}		
		let repayStyle = Publiclib.repayStyle(data.project.repayStyle);	
		return (
			<table className="productInfo-list">
				<tbody>
				<tr>
					<td><div className="box"><strong>产品名称</strong><p>{data.project.projectName}</p></div></td>
					<td><div className="box"><strong>投资金额</strong><p>{data.investAmount}元</p></div></td>
				</tr>
				<tr>
					<td><div className="box"><strong>预期年化收益率</strong><p>{data.project.apr}%</p></div></td>
					<td><div className="box"><strong>投资期限</strong><p>{data.project.timeLimit}{data.project.timeType==0?'个月':'天'}</p></div></td>
				</tr>
				<tr>
					<td><div className="box"><strong>收益方式</strong><p>{repayStyle}</p></div></td>
					<td><div className="box"><strong>收款日</strong><p>{data.repayTime}</p></div></td>
				</tr>
				</tbody>				
			</table>
		);
	}
}