import React from 'react';
import ReactDOM from 'react-dom';
import Error from '../icon/error';
import Right from '../icon/right';
import Safeinfotext from '../safeinfo/safeinfotext';
import {Modal} from 'antd';

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}

function success(v,url) {
  Modal.success({
    title: '撤销成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}

class AuthSignCancl extends React.Component{
	constructor(props){
    super(props);
    this._getCanclData = this.getCanclData.bind(this);
    /*this.state = {
      data : null,
      personageinfoStyle : 'save',
    }*/
  }
  getCanclData() {
  	console.log(222);
    let self = this;
    $.ajax({
      url: '/member/security/doAutoCreditInvestAuthCancel.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      if( data.result ){
        success("","/member/baseInfo/index.html")
      }
      else
      {
        error(data.msg);
      }
    })
    // .fail(function() {
    //   error('网络异常，请重试！');
    // })

  }
  componentDidMount() {
    //this._getCanclData();
  }
  render(){
  	return (
	  		<li>
	        <span className="name"><Right />{this.props.infoname}</span><Safeinfotext str1={this.props.str1} str2={this.props.str2} str3={this.props.str3} /><a href={this.props.link} onClick={this._getCanclData} className="link">{this.props.linkname}</a>
	      </li>
  		)
  	
  }
}

export default class SafeinfoAuto extends React.Component{
  render() {
    const icon = this.props.status == 1 ? <Right /> : <Error />;
    const linkname = ["绑定", "修改", "开通", "重测", "设置","授权","撤销","查看","评测","重新申请开通"][this.props.linkname];
    let linknameVal = this.props.linkname;
    if(linknameVal == 5){
    	return (
	        <li>
	          <span className="name">{icon}{this.props.infoname}</span><Safeinfotext str1={this.props.str1} str2={this.props.str2} str3={this.props.str3} /><a href={this.props.link} target={ this.props.target} className="link">{linkname}</a>
	        </li>
	    );
    }else{
    	return (
	        <AuthSignCancl infoname={this.props.infoname} str1={this.props.str1} str2={this.props.str2} str3={this.props.str3} link={this.props.link} linkname={linkname}/>
	    );
    }
  }
}