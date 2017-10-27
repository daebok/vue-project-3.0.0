import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";
import { Publiclib } from '../../../common/common';
import './borrowersInfo.less';

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
//企业用户组件
class CompanyUserInfoComponent extends React.Component{
	render() {
		let data = this.props.data,
		baseInfo = data.baseInfo;//基本信息
		return (
			<div className="personageUser-info companyUser-info">
				<h3><span className="iconfont">&#xe615;</span>企业用户</h3>
				<ul className="list-info companyUser-info">
					<li><span>企业名称</span><strong>{baseInfo.companyName ? baseInfo.companyName : "-"}</strong></li>
					<li><span>企业简称</span><strong>{baseInfo.simpleName ? baseInfo.simpleName : "-"}</strong></li>
					<li><span>注册资本</span><strong>{baseInfo.registeredCapital ? baseInfo.registeredCapital : "0"}元</strong></li>
					<li><span>注册地区</span><strong>{baseInfo.address ? baseInfo.address : "-"}</strong></li>
					<li><span>成立时间</span><strong>{baseInfo.establishDate ? Publiclib.formatDate(baseInfo.establishDate,4) : "-"}</strong></li>
					<li><span>法人代表</span><strong>{baseInfo.legalDelegate ? baseInfo.legalDelegate : "-"}</strong></li>
					<li className="newline"><span>办公地点</span><strong>{baseInfo.officeAddress ? baseInfo.officeAddress : "-"}</strong></li>
					<li className="newline">
						<div className="shareholder">
							<div className="shareholder-name"><span>股东信息</span></div>
							<div className="shareholder-cont natural-person"><strong>1.自然人 {baseInfo.naturalPerson ? baseInfo.naturalPerson : "-"}</strong></div>
							<div className="shareholder-cont"><strong>2.企业法人 {baseInfo.legalPerson ? baseInfo.legalPerson : "-"}</strong></div>
						</div>
					</li>					
				</ul>
				<Authentication baseInfo = {baseInfo} />
			</div>
		)
	}
}

class Authentication extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this.state = {
			data: null,
		};
	}
	componentWillMount(){
		this._getData();
	}
	getData(){
		let self = this;	
		$.ajax({
			url: '/member/usercertification/getCertificateInfo.html',
			type: 'POST',
			dataType: 'json',
			data: {"userId":self.props.baseInfo.userId}			
		})
		.done(function(data){			
			if(data.result){
				self.setState({data:data});
			}	
		})
		.fail(function() {
			//error("网络异常，请重试！");
		})		
	}
	render() {

		let data = this.state.data;

		if(!data)
		{
			return false
		}
		let html = "",flag = false;

		$.each(data.qualificationList,function(k, v) {
			if(v.status == 1){
				flag = true;
			}
		});

		html =<ul className="list-info">
            {
             data.qualificationList.map(function(k,v){
               return k.status == 1 ? <li key={v}><s>{k.name}</s><em className="iconfont">&#xe612;</em></li> : "";
             })
            }			
					</ul>;

		return (
				<div className = {flag ? "" : "hide"}>
					<h3><span className="iconfont">&#xe613;</span>平台认证</h3>
					{html}
				</div>
		)
	}
}

//个人用户组件
class PersonageUserInfoComponent extends React.Component{
	render() {
		let data = this.props.data;
		let baseInfo = data.baseInfo;//基本信息
		let birthday = Publiclib.getAge(baseInfo.birthday,this.props.topdata.systemTime);							//年龄
		let education = Publiclib.getEducation(baseInfo.education);						//学历
		let maritalStatus = Publiclib.getMaritalStatus(baseInfo.maritalStatus);			//婚姻状况
		let workExperience = Publiclib.getWorkExperience(baseInfo.workExperience);		//工作年限
		let monthIncomeRange = Publiclib.getMonthIncomeRange(baseInfo.monthIncomeRange);//月收入范围
		let carStatus = Publiclib.getCarStatus(baseInfo.carStatus);						//有无车产
		let houseStatus = Publiclib.getHouseStatus(baseInfo.houseStatus);				//有无房产
		return (
			<div className="personageUser-info">
				<h3><span className="iconfont">&#xe615;</span>个人信息</h3>
				<ul className="list-info">
					<li><span>借款人姓名</span><strong>{data.userName}</strong></li>
					<li><span>借款人证件号码</span><strong>{data.idNo}</strong></li>					
					<li><span>婚姻状况</span><strong>{maritalStatus}</strong></li>
					<li><span>年收入范围</span><strong>{monthIncomeRange}</strong></li>
					<li><span>年龄</span><strong>{birthday}</strong></li>
					<li><span>所在地区</span><strong>{data.province}{data.city}</strong></li>
					<li><span>最高学历</span><strong>{education}</strong></li>
					<li><span>工作年限</span><strong>{workExperience}</strong></li>
					<li><span>有无车产</span><strong>{carStatus}</strong></li>
					<li><span>有无房产</span><strong>{houseStatus}</strong></li>
				</ul>
				<Authentication baseInfo = {baseInfo} />
			</div>
		)
	}
}

export default class BorrowersInfo extends React.Component{
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
			url: '/project/borrower.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": $("#projectId").val()}			
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
		let html = null;	
		if(data.userNature === "3"){//担保机构
			
		} else if(data.userNature === "2") {//企业用户
			html = <CompanyUserInfoComponent data = {data} topdata = {this.props.data} />
		} else {//个人用户
			html = <PersonageUserInfoComponent data = {data} topdata = {this.props.data}/>
		}	
		return (
			<div>
				{html}	
			</div>
		);
	}
}