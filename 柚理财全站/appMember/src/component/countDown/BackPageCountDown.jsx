import React from 'react';
import './BackPageCountDown.less';
import CountDown from './CountDownComponent';
export default class BackPageCountDown extends React.Component{
	static defaultProps = {		
		url: '/',
		urlText: '网站首页',
	}
	constructor(props) {
		super(props);
		this.state = {		
			skipUrl: this.props.url,
			skipUrlText : this.props.urlText,
		}
		this.callback = this.callbackFn.bind(this);
	}
	callbackFn(e) {
		console.log(1);		
	}
    render(){    	
    	return (    		
    		<div className="countDown">
    			<div>图标</div>    			
    			<h2 ref="h2">{this.props.text}</h2>
    			<CountDown time = '3' callback = {this.callback}/>秒钟之后，将跳转至 <a href={this.state.skipUrl}>{this.state.skipUrlText}</a>
    		</div>
    	);
    }
}