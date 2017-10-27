import React from 'react';
import ReactDOM from 'react-dom';
import Error from '../icon/error';
import Right from '../icon/right';
import Safeinfotext from '../safeinfo/safeinfotext';
export default class Safeinfo extends React.Component{
  render() {
    const icon = this.props.status == 1 ? <Right /> : <Error />;
    const linkname = ["绑定", "修改", "开通", "重测", "设置","授权","已授权","查看","评测","重新申请开通"][this.props.linkname];  
    return (
        <li>
          <span className="name">{icon}{this.props.infoname}</span><Safeinfotext str1={this.props.str1} str2={this.props.str2} str3={this.props.str3} /><a href={this.props.link} target={ this.props.target} className="link">{linkname}</a>
        </li>
    );
  }
}