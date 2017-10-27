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

export default class Companyageinfoshow extends React.Component{
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
			url: '/member/baseInfo/getCompanyInfo.html',
			type: 'POST',
			dataType: 'json',			
		})
		.done(function(data) {			
			
		  if(data.result){
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
			let region = <p className="txt fl">{data.userCache.provinceStr}-{data.userCache.cityStr}-{data.userCache.areaStr} {data.userCache.address}</p>
		if(!data.userCache.provinceStr){
			region = <p className="txt fl">--</p>;
		}
		else if(!data.userCache.areaStr){
			region = <p className="txt fl">{data.userCache.provinceStr}-{data.userCache.cityStr}<br/>{data.userCache.address}</p>;
		}
		let officeRegion = <p className="txt fl">{data.userCompanyInfo.officeProvinceStr}-{data.userCompanyInfo.officeCityStr}-{data.userCompanyInfo.officeAreaStr} {data.userCompanyInfo.officeAddress}</p>
		if(!data.userCompanyInfo.officeProvinceStr){
			officeRegion = <p className="txt fl">--</p>;
		}
		else if(!data.userCompanyInfo.officeAreaStr){
			officeRegion = <p className="txt fl">{data.userCompanyInfo.officeProvinceStr}-{data.userCompanyInfo.officeCityStr}<br/>{data.userCompanyInfo.officeAddress}</p>;
		}
			let establishDate = data.userCompanyInfo.establishDate ? Publiclib.formatDate(data.userCompanyInfo.establishDate) : "";
	    return (
	        <ul className="form-list">
				  <li><label className="label">用户名</label><p className="txt">{data.userName}</p><span className="tips1">可用于登录，请牢记哦~</span></li>
				  <li><label className="label">企业名称</label><p className="txt">{data.userCompanyInfo.companyName}</p></li>
				  <li><label className="label">企业简称</label><p className="txt">{data.userCompanyInfo.simpleName}</p></li>
				  <li><label className="label">注册资本</label><p className="txt">{data.userCompanyInfo.registeredCapital}</p></li>
				  <li className="clearfix"><label className="label fl">所在地区</label>{region}</li>				  
				  <li><label className="label">企业成立时间</label><p className="txt">{establishDate}</p></li>				  
				  <li><label className="label">税务登记证号</label><p className="txt">{data.userCompanyInfo.taxRegNo}</p></li>
				  <li><label className="label">企业固定电话</label><p className="txt">{data.userCompanyInfo.telephone}</p></li>
				  <li><label className="label">联系人姓名</label><p className="txt">{data.userCompanyInfo.contacts}</p></li>
					<li className="clearfix"><label className="label fl">办公地点</label>{officeRegion}</li>
				<li><label className="label">法定代表</label><p className="txt">{data.userCompanyInfo.legalDelegate}</p></li>
				  <li>
				  	<label className="label">股东信息</label>
				  	<div className="form-item">
            	<div className="mb10">                        		
								<span style={{display:"inline-block"}}>自然人：{data.userCompanyInfo.naturalPerson}</span>								
            	</div>
							<div>								
								<span style={{display:"inline-block"}}>企业法人：{data.userCompanyInfo.legalPerson}</span>								             			
							</div>	              			
						</div>         
				  </li>
				  <li><a href="javascript:;" className="btn edit-btn" onClick={this.props.changeCompanyageinHander}>修改资料</a></li>
			</ul>
	    );
	}
}