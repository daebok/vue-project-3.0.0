import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";

//按钮判断
import Btnstyle from './btnstyle';
//预约判断
import Appointmentstyle from './appointmentstyle';

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
export default class Optimization extends React.Component{
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
	 		url: '/index/getChoiceProject.html',
	 		type: 'POST',
	 		dataType: 'json',			
	 	})
	 	.done(function(data) {
			self.setState({data: data});
			
				$(".presale").mouseover(function(){
					if($(this).find(".img").length){
			 			$(this).find(".word").addClass("hide");
			 			$(this).find(".img").removeClass("hide");
		 			}
		 		})
		 		$(".presale").mouseout(function(){
		 			if($(this).find(".img").length){
			 			$(this).find(".word").removeClass("hide");
			 			$(this).find(".img").addClass("hide");
		 			}
		 		})
	 					
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
				<div className="Optimizationinvest clarfix">
					<div className="title-w"></div>
					<div className="con clerafix">
						<ul className="titlename clearfix">
							<li className="name">产品名称</li>
							<li className="rate">预期年化收益率</li>
							<li className="term">产品期限</li>
							<li className="amount">起投金额</li>
							<li><a href="/invest/index.html" className="clearfix">查看更多<span className="iconfont">&#xe636;</span></a></li>
						</ul>
						<div className="conList">
							{data.choiceProjectList.map(function(item, k){
								let link = "/invest/detail.html?id="+item.id;
								//判断标类型
						    let noviceHtml = item.novice == "1" ? <span className="tags yellowIcon">新客</span> : "";		
						    let transferHtml = item.bondUseful == "1" ? <span className="tags transfer">可转让</span> : "";
						    let realizeFlag = item.realizeUseful == "1" ?	<span className="tags realize">可变现</span> : "";
						    									
								return <ul className="clearfix" key={k}>
								<li className="name"><s><a href={link}>{item.projectTypeName+'-'+item.projectNo}</a></s>{noviceHtml}{transferHtml}{realizeFlag}</li>
									<li className="rate"><font>{item.apr}</font>%</li>
									<li className="term"><font>{item.timeLimit}</font>{item.timeType==0?'个月':'天'}</li>
									<li className="amount"><font>{item.lowestAccount}</font>元</li>
									<li><Btnstyle id={item.id} saleTime={item.saleTime} nowTime={item.nowTime} accountYes={item.accountYes} account={item.account} bespeakStatus={item.bespeakStatus}/></li>
									<li className="appointment"><Appointmentstyle saleTime={item.saleTime} nowTime={item.nowTime} bespeakStatus={item.bespeakStatus}/></li>
								</ul>
							})}
						</div>
					</div>
				</div>
			</div>
		);
	}
}