import React from 'react';
import { ValidateFn } from '../../../common/validateFn';
import { Reg } from '../../../common/regularFun';
import './investFrameState.less';
import { Publiclib } from '../../../common/common';

//已售罄
export default class SellFinishFrame extends React.Component{		
	render(){
		return (
			<div className="header-from site-col-7 site-col-last">
				<div className="investFrame-side">
					<ul className="investFrame-list">
						<li>
							<span className="iconfont">&#xe66f;</span>
						</li>
						<li>
							<p className="txt1">该产品已售罄</p>
						</li>
						<li>
							<a href="/invest/index.html" className="btn">看看其他项目</a>
						</li>
					</ul>
	            </div>
	        </div>
		)
	}
}