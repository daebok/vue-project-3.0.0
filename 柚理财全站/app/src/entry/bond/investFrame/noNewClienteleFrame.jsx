import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//非新客查看新客项目state=8
export default class NoNewClienteleFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		return (
			<div className="header-from site-col-7 site-col-last">				
            	<ul className="investFrame-list">
	            	<li>
	            		<span className="iconfont">&#xe649;</span>
	            	</li>
	            	<li>		            		
	            		<p className="txt1">该产品仅支持新客投资</p>	            		
	            	</li>
	            	<li>
	            		<a href="/bond/index.html" className="btn">看其他项目</a>
	            	</li>	            	
	            </ul>
	        </div>
		)
	}
}