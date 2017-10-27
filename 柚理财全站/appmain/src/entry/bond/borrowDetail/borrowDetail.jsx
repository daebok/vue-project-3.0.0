import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";

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

export default class BorrowDetail extends React.Component{
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
			url: '/project/content.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": $("#projectId").val()}
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
		let html ="";
		if(this.props.borrowFlag==0){
			html=<div dangerouslySetInnerHTML={{__html: data.content}} className="pull-left" style={{"marginTop": '30px'}}></div>
		}else{
			html=<div>
				<div className="mt20 clearfix" style={{'fontSize':'14px'}}>
					<label>借款用途</label>{this.state.data.borrowUse}
				</div>
				<div className="mt20 clearfix">
					<label>借款描述</label>
					<div dangerouslySetInnerHTML={{__html: data.content}} className="pull-left" style={{"width": '960px'}}></div>
				</div>
			</div>
		}	
		return (
				<div>{html}</div>
		);
	}
}