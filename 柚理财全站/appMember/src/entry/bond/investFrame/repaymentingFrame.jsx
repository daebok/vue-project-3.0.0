import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//还款中state=5
export default class RepaymentingFrame extends React.Component{		
	render(){		
		let data = this.props.data;    	
    	
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">剩余期数</span>
		            		<p>36<em>个月</em></p>
		            	</li>
		            	<li>
		            		<span className="tit">下一合约还款日</span>
		            		<p>2016-04-24</p>	            		
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