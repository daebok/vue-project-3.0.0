import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//担保用户查看投资项目state=10
export default class AssureUserFrame extends React.Component{		
	render(){		
		let data = this.props.data;
		return (
			<div className="header-from site-col-7 site-col-last">				
            	<ul className="investFrame-list">
	            	<li>
	            		<span className="iconfont">&#xe647;</span>
	            	</li>
	            	<li>		            		
	            		<p className="txt1">担保用户无法进行投资</p>	            		
	            	</li>
	            	<li>
	            		<a href="/" className="btn">返回首页</a>
	            	</li>	            	
	            </ul>
	        </div>
		)
	}
}