import React from 'react';
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
import AssureUserFrame from './assureUserFrame';//担保账户查看投资项目
import SelfFrame from './selfFrame';//自己的变现产品 

export default class InvestFrameState extends React.Component{
	render(){
		let data = this.props.data;
		let infoData = this.props.infoData;
		let status = data.realize.status;
		let frame = "暂无数据！";
		//标识
		//投资框
		if(infoData.isLogin == "0"){//未登录
			frame = <NoLogin form={this.props.form} data={data} />
		} else if(infoData.tppStatus == "0"){//第三方开户平台
			frame = <NoCertificationFrame form={this.props.form} data={data} />
		}else if( infoData.userId == data.realize.userId ){//判断是否为自己的借款项目
			frame = <SelfFrame />;
		} else if(infoData.userNature == "3"){//担保用户查看投资目
			frame = <AssureUserFrame form={this.props.form} data={data} />
		} else if(data.realize.novice == "1" && infoData.novice == "0"){//非新客查看新客项目
			frame = <NoNewClienteleFrame form={this.props.form} data={data} />
		} else if(status == "4"){
				frame = <DefaultInvestFrame form={this.props.form} data={data} msgdata = {this.props.msgdata} />
		} else if(status == "5"){//已售罄
			frame = <SellFinishFrame form={this.props.form} data={data} />
		} else if(status == "9" || status == "91" || status == "90"){//已还款
			frame = <RepaymentedFrame form={this.props.form} data={data} />
		} else if([6, 8, 87, 88].indexOf(status) > -1){//还款中
			frame = <RepaymentingFrame form={this.props.form} data={data} />
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
