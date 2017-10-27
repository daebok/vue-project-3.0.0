import React from 'react';
import './nobankcard.less';
export default class Nobankcard extends React.Component{
  render() {  
    return (
        <div className="account-main nobankcard">
          <div className="main-title">银行卡</div>
          <div className="iconfont icon">&#xe67d;</div>
          <div className="text">银行卡为开通托管账户时绑定</div>
        </div>
    );
  }
}