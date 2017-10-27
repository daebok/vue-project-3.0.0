import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//非新客查看新客项目state=9
export default class NoCertificationFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		return (
			<div className="header-from site-col-7 site-col-last">				
            	<ul className="investFrame-list">
	            	<li>
	            		<span className="iconfont">&#xe648;</span>
	            	</li>
	            	<li>		            		
	            		<p className="txt1">请先开通第三方资金托管账户</p>	            		
	            	</li>
	            	<li>
	            		<a href="/member/security/realNameIdentify.html" className="btn">开通托管账户</a>
	            	</li>	            	
	            </ul>
	        </div>
		)
	}
}