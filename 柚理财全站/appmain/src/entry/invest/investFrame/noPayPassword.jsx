import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

export default class NoCertificationFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		return (
			<div className="header-from site-col-7 site-col-last">				
            	<ul className="investFrame-list">
	            	<li>
	            		<span className="iconfont">&#xe62a;</span>
	            	</li>
	            	<li>		            		
	            		<p className="txt1">请先设置交易密码</p>	            		
	            	</li>
	            	<li>
	            		<a href="/member/security/setPwd.html" className="btn">设置交易密码</a>
	            	</li>	            	
	            </ul>
	        </div>
		)
	}
}