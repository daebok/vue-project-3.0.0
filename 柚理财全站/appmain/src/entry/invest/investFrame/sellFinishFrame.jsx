import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//已售罄
export default class SellFinishFrame extends React.Component{		
	render(){		
		// let data = this.props.data;
		// if(this.props.remainDate!=undefined){
		// 	data.investNum=this.props.remainDate.investNum
		// 	data.project.raiseEndTime=this.props.remainDate.lastSuccessTime
		// }
		// let time = data.project.raiseEndTime ? Publiclib.formatDatetype2(data.project.saleTime, data.project.raiseEndTime) : data.project.raiseTimeLimit+"天";
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
					<ul className="investFrame-list">
						<li>
							<span className="iconfont">&#xe66f;</span>
						</li>
						<li>
							<p className="txt1">该产品已售罄</p>
						</li>
						<li>
							<a href="/invest/index.html" className="btn">看看其他项目</a>
						</li>
					</ul>
	            </div>
	        </div>
		)
	}
}