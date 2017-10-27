import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '提醒',
    content: v,
  });
}

export default class BasicNews extends React.Component{
	 constructor(props){
	 	super(props);
	 	this._getData = this.getData.bind(this);
	 	this.state = {
	 		data: null,
	 	};
	 }
	 componentDidMount(){
	 	this._getData();
	 }
	 getData(){
	 	let self = this;		
	 	$.ajax({
	 		url: '/index/getHomeInvest.html',
	 		type: 'POST',
	 		dataType: 'json',			
	 	})
		.done(function(data) {
			if(data.result){
	 			self.setState({data: data});
	 		} else {
	 			error(data.msg);
	 		}		
	 	})
	 	.fail(function() {
	 		//error("网络异常，请重试！");
	 	})		
	 }
	render() {
		let data = this.state.data;
		if(!data){return false;}
		return (
			<div>
				<div className="basicNews">
					<div className="title">机构基本信息</div>
					<ul className="content1 clearfix">
						<li><label>法人代表：</label>苗军</li>
						<li><label>注册时间：</label>2016-09-09</li>
						<li><label>注册资本：</label>3000万元人民币</li>
						<li><label>经营状态：</label>续存</li>
						<li><label>经营时效：</label>长期</li>
						<li><label>注册城市：</label>杭州</li>
					</ul>
					<ul className="content2 clearfix">
						<li><label className="left">认缴出资：</label>10000万人民币</li>
						<li><label>股东类型：</label>认缴出资</li>
						<li><label className="left">组织机构代码：</label>9133010232175415826</li>
						<li><label>企业法人：</label>苗军</li>
						<li><label className="left">企业类型：</label>私营有限责任公司</li>
						<li><label>自然人：</label>苗军</li>
						<li><label className="left">企业名称：</label>微贷（杭州）金融信息服务有限公司</li>
						<li><label>工商注册号：</label>500903000023674</li>
						<li><label className="left">注册城市：</label>杭州</li>
						<li><label>网站电信业务备案：</label>渝ICP备15000206号</li>
						<li><label className="left">统一信用代码：</label>91330102321924757Q</li>
						<li><label>网站电信业务经营许可证号：</label>浙B2-20160682</li>
						<li><label className="left">注册地址：</label>重庆市北部新区星光大道92号土星商务中心B1栋19楼</li>
						<li className="area clearfix"><label className="left">经营范围：</label><span>一般经营项目：服务：金融信息服务（法律法规规定需前置审批的项目除外），投资管理及咨询服务、实业投资（未经金融等
监管部门批准，不得从事向公众融资存款、融资担保、代客理财等金融服务），计算机软件的技术开发、技术咨询、技术服务。
计算机软件的技术开发、技术咨询、技术服务</span></li>
					</ul>
				</div>
				<div className="otherNews clearfix">
					<div className="left">
						<div className="stitle">股权信息</div>
						<div className="pie" id="j-pie"></div>
					</div>
					<div className="right">
						<div className="stitle">APP信息</div>
						<p>移动APP名称<em>顺势宝</em></p>
						<div className="ewm clearfix">
							<div className="eleft">
								<img src="1.jpg" /><a>下载手机客户端</a>
							</div>
							<div className="eright">
								<img src="1.jpg" /><p>关注微贷网公众号</p>
							</div>
						</div>
						<div className="clearfix">
							<span className="lx"><em className="iconfont">&#xe61e;</em>400-028-8888</span>
							<span className="lx"><em className="iconfont">&#xe683;</em>0571-********</span>
							<span className="lx"><em className="iconfont">&#xe61d;</em>********@163.com</span>
						</div>
					</div>
				</div>
				<div className="certificate">
					<div className="ptitle">平台证件</div>
					<ul className="clearfix">
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
					</ul>
				</div>
				<div className="office">
					<div className="ptitle">办公环境</div>
					<ul className="clearfix">
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
						<li><a className="fancybox" rel="group" href="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg"><img src="http://www.erongdu.com/uploads/allimg/170527/1-1F52G02943.jpg" /></a></li>
					</ul>
				</div>
			</div>
		);
	}
}

