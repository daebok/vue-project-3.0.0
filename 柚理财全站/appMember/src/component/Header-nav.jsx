import React from 'react';
import ReactDOM from 'react-dom';
import Header_top from './Header-top';
const Header_nav = React.createClass({
  render() {
    return (
    <div>  
    <Header_top />
    <div className="header_con_wrapper">
    <div className="header_con clearfix">
      <div className="logo">
        <a href="/"></a>
      </div>
      <ul id="nav" className="clearfix navSet">
        <li className="active"><a href="/src/page/">首页</a></li>
        <li className="">
          <a href="/src/page/form.html">我要赚钱</a>
        </li>
        <li ><a href="/borrowBespeak/bespeak.html">我要借款</a></li>
        <li ><a href="/safe.html">安全保障</a></li>
        <li ><a href="/member/score/mall/main.html">增值商城</a></li>
        <li >
          <a href="/member/main.html ">账户中心</a>
        </li>
      </ul>
    </div>
  </div>
  </div>
    );
  },
});

export default Header_nav;