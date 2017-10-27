import React from 'react';
export default class Error extends React.Component{
  render() {
    return (
      <span className="userheaderimg"><img src={this.props.headerimg} /><a href="/member/info/face.html">修改头像</a></span>
    );
  }
}

