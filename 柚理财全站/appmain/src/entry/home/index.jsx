import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
//滚动图
import Scroll from './scroll';
//网站公告
import Notice from './notice';
//数据统计
import DataStatistics from './dataStatistics';
//安心贷
import Peaceinvest from './peaceinvest';
//新客专享
import Anovice from './anovice';
//优选理财
import Optimization from './optimization';
//动态资讯
import Information from './information';
//动态资讯
import Rankinglist from './rankinglist';
//合作伙伴
import Partner from './partner';



let Scrollcontent = React.createClass({
	render:function(){
		return (
			<div>
				<Scroll />
			</div>
			)
	}
})


let Homecontent = React.createClass({
	render:function(){
		let tppName = $("#tppName").val();
    	let tppNameDom = tppName!="cbhb" ? <a href="/member/myRealize/list.html">体验变现</a> : <a href="###">体验变现</a>;
		return (
			<div>
				<Notice />
				<div className="trait">
					<div className="con">
						<ul className="clearfix">
							<li className="site-col-8 site-col-first">
								<span>持续高收益</span>
								<p>定制好理财，收益更高更稳定</p>
							</li>
							<li className="site-col-8 site-col-mid">
								<span>安全有保障</span>
								<p>第三方资金托管，监管机构监管</p>
							</li>
							<li className="site-col-8 site-col-last">
								<span>赚钱更轻松</span>
								<p>随时随地查收益，轻松购买，快速提取</p>
							</li>
						</ul>
					</div>
				</div>
				<DataStatistics />
				<div className="mainBg">
					<div className="main_w1180">
						<div className="product_top clearfix">
							<Anovice />
							<Peaceinvest />
						</div>
						<div className="Optimization_box">
							<Optimization />
						</div>
					</div>
				</div>
				{/*<div className="product">
					<ul className="clearfix">
						<li className="assignment">
							<span className="title"><em className="iconfont">&#xe608;</em>转让</span>
							<p>1步轻松搞定，快速收回资金</p>
							<p>转让成功后，资金自动打款至账户余额</p>
							<a href="/member/myBond/list.html">体验转让</a>
						</li>
						<li className="realization">
							<span className="title"><em className="iconfont">&#xe60c;</em>变现</span>
							<p>2步轻松搞定，自主设定借款费率</p>
							<p>变现成功后，资金自动打款至账户余额</p>
							{tppNameDom}
						</li>
						<li className="financing">
							<span className="title"><em className="iconfont">&#xe60f;</em>融资</span>
							<p>可在线预约借款</p>
							<p>可上传相关资质材料，在线发布借款</p>
							<a href="/borrow/bespeak.html">体验融资</a>
						</li>
					</ul>
				</div>*/}
				<div className="news">
					<div className="clearfix con">
						<div className="information">
							<Information />
						</div>
						<div className="rankinglist site-col-7 site-col-last">
							<Rankinglist />
						</div>
					</div>
				</div>
				<Partner />

			</div>
			)
	}
})

ReactDOM.render(<Scrollcontent />, document.getElementById("scrollContent"));
ReactDOM.render(<Homecontent />, document.getElementById("homeCon"));



