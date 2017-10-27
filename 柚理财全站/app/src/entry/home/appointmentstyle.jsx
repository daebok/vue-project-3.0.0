import React from 'react';
import ReactDOM from 'react-dom';
import { Publiclib } from '../../common/common';

export default class Appointmentstyle extends React.Component{
	constructor(props){
    super(props);    
	    this.state = {
	      data: null,
	    };
	}
	render() {
		let saleTime = this.props.saleTime;
		let nowTime = this.props.nowTime;
		let bespeakStatus = this.props.bespeakStatus;
		//判断是否预约
		let appointmentHtml = bespeakStatus=="1" ? <span className="img"></span> : "";
		if(nowTime>saleTime){
			appointmentHtml = "";
		}
		return ( 
			<div>{appointmentHtml}</div>
		) 
		
	}
}