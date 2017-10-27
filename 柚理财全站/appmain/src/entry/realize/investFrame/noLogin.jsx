import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';

//未登录state=7
export default class NoLogin extends React.Component{		
	render(){		
		let data = this.props.data;
		let url = "/user/login.html?redirectURL=/realize/detail.html?id="+data.realize.id;
		return (
			<div className="header-from site-col-7 site-col-last">				
            	<ul className="investFrame-list">
	            	<li>
	            		<span className="iconfont">&#xe64a;</span>
	            	</li>
	            	<li>		            		
	            		<p className="txt1">尚未登录，请登录后投资</p>	            		
	            	</li>
	            	<li>
	            		<a href={url} className="btn">立即登录</a>
	            	</li>
	            	<li>
	            		<p className="txt2">没有账号？<a href="/user/register.html" className="a-link">免费注册</a></p>
	            	</li>
	            </ul>
	        </div>
		)
	}
}