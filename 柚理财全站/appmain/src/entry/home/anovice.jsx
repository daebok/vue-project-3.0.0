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

export default class Anovice extends React.Component{
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
	 		url: '/index/getNoviceProject.html',
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
		 		});
		 		$(".presale").mouseout(function(){
		 			if($(this).find(".img").length){
			 			$(this).find(".word").removeClass("hide");
			 			$(this).find(".img").addClass("hide");
		 			}
		 		});		
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
				<div className="Anoviceinvest">
					<div className="title-w"></div>
					<div className="con clerafix">
						<div className="conList">
							{data.noviceProjectList.map(function(item, k){
						    let transferHtml = item.bondUseful == "1" ? <em className="tags transfer">可转让</em> : "";
						    let realizeFlag = item.realizeUseful == "1" ?	<em className="tags realize">可变现</em> : "";								
								let link = "/invest/detail.html?id="+item.id;
								return <div className="list" key={k}>
									<div className="name"><s><a href={link}>{item.projectTypeName+'-'+item.projectNo}</a></s><em>新客</em>{transferHtml}{realizeFlag}</div>
									<dl className="clearfix">
										<dt>
											<font>{item.apr}<em>%</em></font><br/>
											预期年化收益率
										</dt>
										<dd>
											<em>起投<font>{item.lowestAccount}元</font></em>
											<em>期限<font>{item.timeLimit}{item.timeType==0?'个月':'天'}</font></em>
										</dd>
									</dl>
									<Btnstyle id={item.id} saleTime={item.saleTime} nowTime={item.nowTime} accountYes={item.accountYes} account={item.account} bespeakStatus={item.bespeakStatus}/>
									<div className="appointment"><Appointmentstyle saleTime={item.saleTime} nowTime={item.nowTime} bespeakStatus={item.bespeakStatus}/></div>
								</div>
							})}
						</div>
					</div>
				</div>
			</div>
		);
	}
}