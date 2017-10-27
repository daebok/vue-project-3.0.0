import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//已还款state=6
export default class RepaymentedFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		let time = Publiclib.formatDatetype2(data.reviewTime, data.lastRepayTime);
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">还款用时</span>
		            		<p>{time}<em>个月</em></p>
		            	</li>
		            	<li>
		            		<span className="tit">最后还款日</span>
		            		<p>{Publiclib.formatDate(data.lastRepayTime)}</p>	            		
		            	</li>
		            </ul>
		            <div className="btns-box">		            	
		            	<a href="#" className="btn">看看其他项目</a>
		            </div>
	            </div>
	        </div>
		)
	}
}