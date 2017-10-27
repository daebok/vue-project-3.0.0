import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './mallHelp.less';
//滚动图
import MallHelpList from './component/mallHelpList';

let MallHelp = React.createClass({
	render:function(){
		return (
			<div>
        <div className="mallHelpcon clearfix">
          <div className="left">
            <div className="title">客服服务</div>
            <ul>
              <li className="clearfix"><em className="iconfont">&#xe683;</em><div className="con">400-400-9999<p>服务热线</p></div></li>
              <li className="serviceTime clearfix"><em className="iconfont">&#xe60d;</em><div className="con">早9:00-晚20:00<p>服务时间</p></div></li>
            </ul>
          </div>
          <div className="right">
            <div className="title">商城帮助</div>
            <MallHelpList />
          </div>
        </div>
			</div>
			)
	}
})

ReactDOM.render(<MallHelp />, document.getElementById("mallHelp"));
