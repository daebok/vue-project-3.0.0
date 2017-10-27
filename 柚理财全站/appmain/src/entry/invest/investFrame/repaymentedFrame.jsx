import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//已还款
export default class RepaymentedFrame extends React.Component{		
	render(){		
		let data = this.props.data;    	
    let time = Publiclib.formatDatetype2(data.project.reviewTime, data.lastRepayTime);
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">还款用时</span>
		            		<p>{time}</p>
		            	</li>
		            	<li>
		            		<span className="tit">最后还款日</span>
		            		<p>{Publiclib.formatDate(data.lastRepayTime)}</p>	            		
		            	</li>
		            </ul>
		            <div className="btns-box">		            	
		            	<a href="/invest/index.html" className="btn">看看其他项目</a>
		            </div>
	            </div>
	        </div>
		)
	}
}