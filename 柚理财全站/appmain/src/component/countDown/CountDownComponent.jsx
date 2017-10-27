import React from 'react';
import ReactDOM from 'react-dom';
import {Modal,Icon} from 'antd';

function success(v) {
  Modal.success({
    title: '发送成功',
    content: v,
    okText:"确定"  
  });
}

export default class CountDown extends React.Component{
	static defaultProps = {
		time: 5,		
	}
	constructor(props) {
		super(props);
		this.state = {count: props.time,showTime:''};
		this.tick = this.tick.bind(this);
		this.handleChange = this.handleChange.bind(this);
	}
	tick() {
		this.setState({count: this.state.count - 1});
		this.set
	}
	handleChange() {
		if(this.state.count > 0){
			this.tick();
		} else if(this.props.callback) {				
			this.props.callback();
		}
	}

	componentDidMount() {

		let msg = this.props.msg;
		if(msg){
			success(msg);
		}
    
  }
    render(){
      const text = this.props.text ? this.props.text : '';
    	setTimeout(this.handleChange, 1000);
    	return (
    		<span>{this.state.count}{text}</span>
    		);
    }
}