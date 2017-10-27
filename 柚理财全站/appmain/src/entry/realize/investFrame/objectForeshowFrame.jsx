import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//项目预告
export default class ObjectForeshowFrame extends React.Component{		
	render(){		
		let data = this.props.data;    	
    	let saleTime = data.realize.saleTime ? Publiclib.formatDate(data.realize.saleTime, 2) : '';
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
	            	<ul className="text-list">
		            	<li>
		            		<span className="tit">预计上线时间</span>
		            		<strong>{saleTime}</strong>
		            	</li>
		            	<li>
		            		<span className="tit">预约人数</span>
		            		<p>{data.bespeakNum}<em>人</em></p>	            		
		            	</li>
		            </ul>
		            <div className="btns-box">
		            	<p className="txt">预约信息仅供参考，请以上线时的描述为准</p>
		            	<a href="#" className="btn">项目上线前通知我</a>
		            </div>
	            </div>
	        </div>
		)
	}
}