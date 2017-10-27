import React from 'react';
export default class CountDown extends React.Component{
	static defaultProps = {
		time: 5,
	}
	constructor(props) {
		super(props);
		this.state = {count: props.time,showTime:''};
		this.tick = this.tick.bind(this);
		this.handleChange = this.handleChange.bind(this);
		this.timer=null;
	}
	formatSeconds(value) {
		var theTime = parseInt(value);// 秒
		var theTime1 = 0;// 分
		var theTime2 = 0;// 小时
		if(theTime > 60) {
			theTime1 = parseInt(theTime/60);
			theTime = parseInt(theTime%60);
			if(theTime1 > 60) {
				theTime2 = parseInt(theTime1/60);
				theTime1 = parseInt(theTime1%60);
			}
		}
		var result = ""+parseInt(theTime)+"秒";
		if(theTime1 > 0) {
			result = ""+parseInt(theTime1)+"分"+result;
		}
		if(theTime2 > 0) {
			result = ""+parseInt(theTime2)+"小时"+result;
		}
		if(parseInt(theTime2) >= 24 ){
			result = Math.ceil(parseInt(theTime2)/24) + "天";
		}
		if(value==0){
			clearInterval(this.timer)
			result='已结束'
		}
		return result;
	}
	tick() {
		this.setState({count: this.state.count - 1,showTime:this.formatSeconds(this.state.count)});
	}
	handleChange() {
		if(this.state.count >= 0){
			this.tick();
		} else if(this.props.callback) {
			this.props.callback();
		}
	}
	componentDidMount(){
		this.timer=setInterval(this.handleChange, 1000);
	}
	render(){
		const text = this.props.text ? this.props.text : '';
		return (<em>{this.state.showTime}{text}</em>);
	}
}