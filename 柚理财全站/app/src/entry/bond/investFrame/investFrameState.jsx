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
import SelfFrame from './selfFrame';//自己的转让项目 

export default class InvestFrameState extends React.Component{	
	render(){
		let data = this.props.data;
		let infoData = this.props.infoData;		
		let msgdata = this.props.msgdata;
		let status = data.status;
		let frame = "暂无数据！";		
		//投资框
		if(infoData.isLogin == "0"){//未登录
			frame = <NoLogin form={this.props.form} data={data} id={this.props.id} />
		}else if( infoData.userId == data.bondUserId ){//判断是否为自己的转让项目
			frame = <SelfFrame />;
		}else if(data.isRealName==false){
			frame = <div className="header-from site-col-7 site-col-last"><ul className="investFrame-list"><li><span className="iconfont">&#xe648;</span></li><li><p className="txt1">请先开通第三方资金托管账户</p></li><li><a href="/member/security/realNameIdentify.html" className="btn">开通托管账户</a></li></ul></div>
		}else if(data.isBorrower==true){
			frame = <div className="header-from site-col-7 site-col-last"><ul className="investFrame-list"><li><span className="iconfont">&#xe647;</span></li><li><p className="txt1">借款用户无法进行投资</p></li><li><a href="/" className="btn">返回首页</a></li></ul></div>
		}else if(data.isBonder==true){
			frame = <div className="header-from site-col-7 site-col-last"><ul className="investFrame-list"><li><span className="iconfont">&#xe647;</span></li><li><p className="txt1">债权发布用户无法进行投资</p></li><li><a href="/" className="btn">返回首页</a></li></ul></div>
		}else if(status != "9"){//没有全部回款 回款中
			if(status == "3"){//债权转让完成
				frame = <SellFinishFrame form={this.props.form} data={data} />
			} else if(status == "0"){//立即投资
				if(data.remainMoney=="0"){
					frame = <SellFinishFrame form={this.props.form} data={data} />
				}else{
					frame = <DefaultInvestFrame form={this.props.form} data={data} msgdata={msgdata}/>
				}
			} else if(status == "4"){//自动撤回
				frame = "自动撤回";
			} else if(status == "5"){//手动撤回
				frame = "手动撤回";
			}
		} else {//已回款
			frame = <RepaymentedFrame form={this.props.form} data={data} />
		}			
		// //默认投资框		
		// if(status == "4" || status == "49"){
		// 	frame = <DefaultInvestFrame form={this.props.form} data={data} />	
		// } else if(status === 21){//即息理财
		// 	frame = <InstantManageMoneyFrame form={this.props.form} data={data} />
		// } else if(status === 22){//定向项目
		// 	frame = <DirectionObjectFrame form={this.props.form} data={data} />
		// } else if(status === 23){//项目预告
		// 	frame = <ObjectForeshowFrame form={this.props.form} data={data} />
		// } else if(status == "5"){//已售罄
		// 	frame = <SellFinishFrame form={this.props.form} data={data} />
		// } else if([6, 8, 87, 88, 90].indexOf(status) > -1){//还款中
		// 	frame = <RepaymentingFrame form={this.props.form} data={data} />
		// } else if(status == "9" || status == "91"){//已还款
		// 	frame = <RepaymentedFrame form={this.props.form} data={data} />
		// } else if(status === 27){//未登录
		// 	frame = <NoLogin form={this.props.form} data={data} />
		// } else if(status === 28){//非新客查看新客项目
		// 	frame = <NoNewClienteleFrame form={this.props.form} data={data} />
		// } else if(status === 29){//未开通托管账户
		// 	frame = <NoCertificationFrame form={this.props.form} data={data} />
		// } else if(status === 210){//担保账户查看投资项目
		// 	frame = <AssureUserFrame form={this.props.form} data={data} />
		// }
		return (
			<div>				
				{frame}
			</div>
		)
	}
}