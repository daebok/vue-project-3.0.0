import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';
import { Modal } from "antd";
//项目预告
function success(v, fn) {
	Modal.success({
		title: '提醒',
		content: v,
		onOk: fn,
	});
}
function error(v) {
	Modal.error({
		title: v,
		okText:"关闭",
		wrapClassName:'tiperror'
	});
}
export default class ObjectForeshowFrame extends React.Component{
	constructor(props){
		super(props);
		this._getInvestBespeak = this.getInvestBespeak.bind(this);
	}
	getInvestBespeak(e){
		let target = e.target;
		let projectId = this.props.data.project.id;
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
						window.location.reload()
					});
				} else {
					error(data.msg);
				}
			})
			.fail(function() {
				//error("网络异常，请重试！");
			})
	}
	render(){		
		let data = this.props.data;    	
    	let saleTime = data.project.saleTime ? Publiclib.formatDateInvest(data.project.saleTime, 2) : '';
		let typeA=data.bespeak==1?<a href="#" className="btn">已预约</a>:<a href="javascript:;" className="btn" onClick={this._getInvestBespeak}>项目上线前通知我</a>
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">预计上线时间</span>
		            		<strong>{saleTime}</strong>
		            	</li>
		            	<li>
		            		<span className="tit">预约人数</span>
		            		<p>{data.bespeakNum}<em>人</em></p>	            		
		            	</li>
		            </ul>
		            <div className="btns-box">
		            	<p className="txt">预约信息仅供参考，请以上线时的描述为准</p>
						{typeA}
		            </div>
	            </div>
	        </div>
		)
	}
}