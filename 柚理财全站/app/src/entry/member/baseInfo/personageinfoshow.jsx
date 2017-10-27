import React from 'react';
import {Publiclib} from '../../../common/common';
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

export default class Personageinfoshow extends React.Component{
	constructor(props){
	    super(props);
	    this._getData = this.getData.bind(this);	    
	    this.state = {
	      data : null,	     
	    }
	}
	getData(){
		let self = this;
		$.ajax({
			url: '/member/baseInfo/getUserInfo.html',
			type: 'POST',
			dataType: 'json',			
		})
		.done(function(data) {			
      if( data.result ){
				self.setState({data: data});
      }
      else
      {
        error(data.msg,data.url);
      } 			
		})
		.fail(function() {
			error('网络异常，请重试！');
		})		
	}
	componentDidMount(){
		this._getData();
	}
	render() {
			let data = this.state.data;
			if(!data){
				return false;
			}

			let educationList = [],workExperienceList=[],monthIncomeRangeList=[];

			this.state.data.educationList.map(function(k,v){
					educationList[k.itemValue] = k.itemName;
			});

			this.state.data.workExperienceList.map(function(k,v){
					workExperienceList[k.itemValue] = k.itemName;
			});

			this.state.data.monthIncomeRangeList.map(function(k,v){
					monthIncomeRangeList[k.itemValue] = k.itemName;
			});

			let sex = data.userCache.sex ? data.userCache.sex == "M" ? "男" : "女" : "-";
			let baseInfo = data.userBaseInfo;
			//学历
			let education = educationList[baseInfo.education];//
			//婚姻状况
			let maritalStatus = Publiclib.getMaritalStatus(baseInfo.maritalStatus);//
			//工作年限
			let workExperience = workExperienceList[baseInfo.workExperience];//
			//收入范围
			let monthIncomeRange = monthIncomeRangeList[baseInfo.monthIncomeRange];//
			//车产
			let carStatus = Publiclib.getCarStatus(baseInfo.carStatus);		
			//房产
			let houseStatus = Publiclib.getHouseStatus(baseInfo.houseStatus);
			//所在地区
			let address = "";
			if(data.userCache.provinceStr){
				address += data.userCache.provinceStr;
			}
			console.log(data.userCache.cityStr);
			if(data.userCache.cityStr){
				address += "-"+data.userCache.cityStr;
			}
			if(data.userCache.areaStr){
				address += "-"+data.userCache.areaStr;
			}			
	    return (
	        <ul className="form-list" style={{marginTop:0,paddingTop:40}}>
				  <li><label className="label">用户名</label><p className="txt">{data.userName}</p><span className="tips1">可用于登录，请牢记哦~</span></li>
				  <li><label className="label">出生日期</label><p className="txt">{baseInfo.birthday ? Publiclib.formatDate(baseInfo.birthday) : "-"}</p></li>
				  <li><label className="label">性别</label><p className="txt">{sex}</p></li>
				  <li><label className="label">学历</label><p className="txt">{education}</p></li>
				  <li><label className="label">婚姻状况</label><p className="txt">{maritalStatus}</p></li>
				  <li className="clearfix"><label className="label fl">所在地区</label><p className="txt fl">{address}<br/>{data.userCache.address}</p></li>
				  <li><label className="label">工作年限</label><p className="txt">{workExperience}</p></li>
				  <li><label className="label">收入范围</label><p className="txt">{monthIncomeRange}</p></li>
				  <li><label className="label">有无房产</label><p className="txt">{carStatus}</p></li>
				  <li><label className="label">有无车产</label><p className="txt">{houseStatus}</p></li>
				  <li><a href="javascript:;" className="btn edit-btn" onClick={this.props.changePersonageinHander}>修改资料</a></li>
			</ul>
	    );
	}
}