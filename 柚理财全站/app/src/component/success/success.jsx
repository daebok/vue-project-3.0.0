import React from 'react';
import CountDown from '../countDown/CountDownComponent';
import './success.less';
export default class Success extends React.Component{

  render() {
    const textSmall = this.props.textSmall ? <span className="title-small">{this.props.textSmall}</span> : "";
    return (
      <div className="success-page">
        <em className="iconfont">&#xe611;</em>
        <span className="title">{this.props.text}</span>
        {textSmall}
        <span className="text"><s><CountDown time = {this.props.time} callback = {this.props.callback}/></s>秒钟之后，将跳转至 <a href={this.props.link} >{this.props.linkname}</a></span>
      </div>
    );
  }
}
