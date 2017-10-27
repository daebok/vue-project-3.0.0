import React from 'react';
import ReactDOM from 'react-dom';
export default class Uploadimg extends React.Component{
	render() {
		return (
			<div className="uploadPic">
				<img src={this.props.imgSrc} /><a href="javascript:;">修改头像</a>
			</div>
		);
	}
}