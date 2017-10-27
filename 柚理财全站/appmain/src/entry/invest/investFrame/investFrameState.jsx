import React from 'react';
import { Modal} from 'antd';
import DefaultInvestFrame from './defaultInvestFrame';//默认投资框
import InstantManageMoneyFrame from './instantManageMoneyFrame';//即息理财
import DirectionObjectFrame from './directionObjectFrame';//定向项目
import ObjectForeshowFrame from './objectForeshowFrame';//项目预告
import SellFinishFrame from './sellFinishFrame';//已售罄
import RepaymentingFrame from './repaymentingFrame';//还款中
import RepaymentedFrame from './repaymentedFrame';//已还款
import NoLogin from './noLogin';//为登录
import NoNewClienteleFrame from './noNewClienteleFrame';//非新客查看新客项目
import NoCertificationFrame from './noCertificationFrame';//未开通托管账户
import NoPayPassword from './noPayPassword';//未设置交易密码
import AssureUserFrame from './assureUserFrame';//担保账户查看投资项目
import SelfFrame from './selfFrame';//自己的借款项目
import { Publiclib } from '../../../common/common';


function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

export default class InvestFrameState extends React.Component{
	constructor(props){
	    super(props);
	    this._msgclick = this.msgclick.bind(this);
	    this.state = {
			remainDate:''
	    }
	  }
		componentWillMount(){
			var _this=this;
			$.ajax({
				url: '/project/remainAccount.html',
				type: 'POST',
				dataType: 'json',
				data: {"projectId":_this.props.id}
			})
				.done(function(msgdata) {
					_this.setState({remainDate:msgdata});
					if(msgdata.remainAccount == 0){
						$("#j-deadTimeHtml").hide();
					}
				})
				.fail(function() {
					//error("网络异常，请重试！");
				})
		}
	  msgclick(e){
	  	//alert(1)
	  }
	render(){
		if(!this.state.remainDate){
			return false;
		}
		let data = this.props.data;
		let infoData = this.props.infoData;
		let msgdata = this.props.msgdata;
		let status = data.project.status;
		//alert(infoData.result)				
		let frame = "暂无数据！";		
		//标识
		let flag = data.project.saleTime >= data.systemTime;
		let endTime = "";
		if(flag){
			//售罄用时 = 开始时间-系统时间
			endTime = Publiclib.formatDatetype2(data.systemTime,data.project.saleTime);
		}								
		//投资框
		if(this.props.preview=='1'){
			frame = <div className="nothing"><em className="iconfont">&#xe66d;</em><span className="title-text" onClick={this.msgclick}>此页面为预览页面，不能进行投资</span></div>
		}else if(infoData.isLogin == "0"){//未登录
			frame = <NoLogin form={this.props.form} data={data} />
		} else if(infoData.tppStatus == "0"){//第三方开户平台
			frame = <NoCertificationFrame form={this.props.form} data={data} />
		}else if(infoData.payPwdStatus == "0"){//设置交易密码
			frame = <NoPayPassword form={this.props.form} data={data} />
		}else if( infoData.userId == data.project.userId ){//判断是否为自己的借款项目
			frame = <SelfFrame data={data} />;
		} else if(infoData.userNature == "3"){//担保用户查看投资目
			frame = <AssureUserFrame form={this.props.form} data={data} />
		} else if(data.project.novice == "1" && infoData.novice == "0"){//非新客查看新客项目
			frame = <NoNewClienteleFrame form={this.props.form} data={data} />
		} else if(status == "4"){
			if(flag){//项目预告
				frame = <ObjectForeshowFrame form={this.props.form} data={data} />
			} else {
				if(data.project.saleStyle == "1"){
					if(this.state.remainDate.remainAccount==0){
						frame = <SellFinishFrame />
					}else{
						frame = <DefaultInvestFrame form={this.props.form} id={this.props.id} data={data} msgdata={msgdata}/>
					}
				}
				else{
					if(this.state.remainDate.remainAccount==0){
						frame = <SellFinishFrame />
					}else {
						frame = <InstantManageMoneyFrame form={this.props.form} id={this.props.id} data={data} msgdata={msgdata}/>
					}
				}
			}				
		} else if(status == "5"){//已售罄
			frame = <SellFinishFrame form={this.props.form} data={data} />
		} else if(status == "9" || status == "91" || status == "90"){//已还款
			frame = <RepaymentedFrame form={this.props.form} data={data} />
		} else if([6, 8, 87, 88].indexOf(status) > -1){//还款中
			frame = <RepaymentingFrame form={this.props.form} data={data} />
		}else if(status == "7"){
			frame =<div className="header-from site-col-7 site-col-last"><ul className="investFrame-list"><li><span className="iconfont">&#xe647;</span></li><li><p className="txt1">产品已撤销</p></li><li><a href="/invest/index.html" className="btn">返回首页</a></li></ul></div>
		} else{
			// frame = <div className="nothing"><em className="iconfont">&#xe66c;</em><span className="title-text">该产品已撤销</span><a href="/invest/index.html">看看其他项目</a></div>
			frame = <div className="nothing"><em className="iconfont">&#xe66d;</em><span className="title-text" onClick={this.msgclick}>此页面为预览页面，不能进行投资</span></div>
			//frame = <DefaultInvestFrame form={this.props.form} data={data} msgdata={msgdata}/>
		}
		/* else if(status === 21){//即息理财
			frame = <InstantManageMoneyFrame form={this.props.form} data={data} />
		} else if(status === 22){//定向项目
			frame = <DirectionObjectFrame form={this.props.form} data={data} />
		} else if(status === 29){//未开通托管账户
			frame = <NoCertificationFrame form={this.props.form} data={data} />
		}*/
		return (
			<div>				
				{frame}
			</div>
		)
	}
}