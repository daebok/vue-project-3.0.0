import React from 'react';
import InvestindexItem from './investindexItem';

export default class InvestindexList extends React.Component{
	render(){
		let _this = this;
		let liHtml = <li style={{"width": "1120px","padding": "60px 0","border": "none", "textAlign": "center"}} className="nolist"><em className="iconfont" style={{display:'block',fontSize:34+'px',color:'#aaa'}}>&#xe638;</em><span style={{fontSize:14+'px',color:'#aaa'}}>暂无数据</span></li>;
		if(this.props.data){
			liHtml = this.props.data.map(function(item, k) {
	          return <InvestindexItem key = {k} item = {item} systemTime={_this.props.systemTime}/>
	        });
		}
		return (
	      <ul>
	        {liHtml}              
	      </ul>
	    );
	}
};