import React from 'react';

const Header_top = React.createClass({
  render() {
    return (   
  <div id="header_info">
    <div  className="clearfix header_top">
      <div className="float_left">
        <span className="tel">服务热线<em className="pl10 pr5">400-8077-098</em>（9:00-20:00）</span>
      </div>
      <div className="float_right">
            <span className="loginStatus">
          <a href="/help/guide/register.html" className="mr25" title="新手指引">新手指引 </a>
          <a href="/aboutUs/siteIntro/aboutus.html" className="mr25">关于我们</a>
          <a href="">APP下载</a>
          <a href="/user/login.html" className="leftLine pr15 pl15 ml15">登录</a><a href="/user/register.html" className="leftLine rightLine pr15 pl15">注册</a>

          </span>
      </div>
    </div>
  </div>

    );
  },
});

export default Header_top;