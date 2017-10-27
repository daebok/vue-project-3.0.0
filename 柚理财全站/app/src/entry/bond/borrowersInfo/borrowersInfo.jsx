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
		return (
			<div className="companyUser-info">
				<h3>企业用户</h3>
			</div>
		)
	}
}
//个人用户组件
class PersonageUserInfoComponent extends React.Component{
	render() {
		let data = this.props.data;
		let baseInfo = data.baseInfo;//基本信息
		let birthday = Publiclib.formatDate(baseInfo.birthday);							//出生日期
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
					<li><span>婚姻状况</span><strong>{maritalStatus}</strong></li>
					<li><span>月收入范围</span><strong>{monthIncomeRange}</strong></li>
					<li><span>出生日期</span><strong>{birthday}</strong></li>
					<li><span>户籍城市</span><strong>{baseInfo.province} {baseInfo.city}</strong></li>
					<li><span>最高学历</span><strong>{education}</strong></li>
					<li><span>工作年限</span><strong>{workExperience}</strong></li>
					<li><span>有无车产</span><strong>{carStatus}</strong></li>
					<li><span>有无房车</span><strong>{houseStatus}</strong></li>
				</ul>
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
			html = <CompanyUserInfoComponent />
		} else {//个人用户
			html = <PersonageUserInfoComponent data = {data}/>
		}	
		return (
			<div>
				{html}	
			</div>
		);
	}
}