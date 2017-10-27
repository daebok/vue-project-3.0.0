import React from 'react';
import {Publiclib} from '../../../common/common';

class GoodsStatus extends React.Component{	
	render(){
		let item = this.props.item;
		//状态判断
		let statusHtml = "";
		if(item.borrowStatus != "9"){//没有全部回款 回款中
			if(item.status == "3"){//债权转让完成
				statusHtml = <div className="status3">
					<strong>回款进度</strong><p><em>共{item.totalPeriod}期</em><em>已还{item.repayedPeriod}期</em></p>
				</div>
			} else if(item.status == "0"){//立即投资
				if(item.remainMoney=="0"){
					statusHtml = <div style={{"marginTop":"34px","fontSize":"14px","marginLeft":"40px"}}>已售罄</div>
				}else{
					statusHtml = <a href={"/bond/detail.html?id="+ item.id} className="bondment">立即投资</a>;
				}

			} else if(item.status == "4"){//自动撤回
				statusHtml = "自动撤回";
			} else if(item.status == "5"){//手动撤回
				statusHtml = "手动撤回";
			}
		} else {//已回款
			statusHtml = <div className="status9">
				<strong>最后回款日</strong><p><em>{item.lastRepayTime ? Publiclib.formatDate(item.lastRepayTime) : "-"}</em></p>
			</div>
		}
		return (
			<dd className="status">{statusHtml}</dd>
		)
	}
}

class MoneyStatus extends React.Component{
	render(){
		let item = this.props.item;
		//债权总额
	  let accountNo = item.remainMoney;
	  if( item.remainMoney > item.bondMoney ){
	  	accountNo = item.bondMoney;
	  }
		if( item.remainMoney < 0 ){
	  	accountNo = 0;
	  }
		let moneyHtml = "";
		if(item.borrowStatus != "9"){
			if(item.status == "3"){//回款中
				moneyHtml = <span className="f16">回款中</span>
			} else {//剩下投资金额
				moneyHtml = <span className="f16">剩余{accountNo}元</span>;
			}			
		} else {//已回款
			moneyHtml = <span className="f16">已回款</span>;
		}	
		return (
			<dd className="money">{moneyHtml}</dd>
		)
	}
}

export default class GoodsindexItem extends React.Component{
	render(){
		let item = this.props.item;		
	    //详情页面链接
		let link = "/bond/detail.html?id=" + item.id;
		//判断新手标
	    let makeHtml = <span className="yellowIcon">转让</span>;		    
		return (
				<li>
		        <dl>
		          <dt><a href={link}>{item.bondName}</a>{makeHtml}</dt>
		          <dd className="profit">{item.apr}<em>%</em></dd>
		          <dd className="timelimit"><span className="f16">{item.remainDays}</span>天</dd>
		          <dd className="minmoney"><span className="f16">{item.bondLowestMoney}</span><span>元</span></dd>
		          <MoneyStatus item={item}/>
		          <GoodsStatus item={item}/>
		        </dl>
		   </li>
		)
	}
};