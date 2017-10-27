import React from 'react';
import BondindexItem from './BondindexItem';

export default class BondindexList extends React.Component{
	render(){
		let html = <li style={{"width": "1120px","padding": "60px 0","border": "none", "textAlign": "center"}} className="nolist"><em className="iconfont">&#xe638;</em><span>暂无数据</span></li>;
		if(this.props.data){
			html = this.props.data.map(function(item, k) {
	          return <BondindexItem key = {k} item = {item} />
	        });
		}
		return (
	      <ul>
	        {html}              
	      </ul>
	    );
	}
};