import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//已售罄
export default class SellFinishFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		let raiseEndTime = data.realize.raiseEndTime ? data.realize.raiseEndTime : data.realize.raiseTimeLimit;
    	let time = Publiclib.formatDatetype2(data.realize.saleTime, raiseEndTime);
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">投资笔数</span>
		            		<p>{data.investNum}<em>笔</em></p>
		            	</li>
		            	<li>
		            		<span className="tit">售罄用时</span>
		            		<p>{time}</p>	            		
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