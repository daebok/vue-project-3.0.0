import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//还款中
export default class RepaymentingFrame extends React.Component{		
	render(){		
		let data = this.props.data;    	
		let nextRepayDay = data.nextRepayDay ? Publiclib.formatDate(data.nextRepayDay) : '';    	
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">剩余期数</span>
		            		<p>{data.realize.remainPeriod}</p>
		            	</li>
		            	<li>
		            		<span className="tit">下一合约还款日</span>
		            		<p>{nextRepayDay}</p>	            		
		            	</li>
		            </ul>
		            <div className="btns-box">		            	
		            	<a href="/realize/index.html" className="btn">看看其他项目</a>
		            </div>
	            </div>
	        </div>
		)
	}
}