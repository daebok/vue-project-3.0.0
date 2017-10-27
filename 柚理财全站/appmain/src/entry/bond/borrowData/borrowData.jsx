import React from 'react';
import ReactDOM from 'react-dom';
import './borrowData.less';
import { Modal } from "antd";

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

//图片滚动
function scrollfn(obj){
	let btnLeft = obj.find(".btn-left");
	let btnRight = obj.find(".btn-right");
	let scrollUl = obj.find(".scroll-content ul");
	let liW = scrollUl.find("li").width();
	let liLen = scrollUl.find("li").length;
	scrollUl.css({width: liW*liLen});
	let now = 0;
	if(liLen>4){
		btnLeft.on("click", function(){
			now--;
            btnRight.attr('title','').removeClass('disabled');
			if(now < 4-liLen){
				$(this).attr('title','最后一张').addClass('disabled');
                now=4-liLen
			}
			scroll(now);
		});
		btnRight.on("click", function(){
			now++;
            btnLeft.attr('title','').removeClass('disabled');
			if(now > 0) {
                $(this).attr('title','最后一张').addClass('disabled');
                now = 0;
            }
			scroll(now);
		});
	}

	function scroll(now){
		scrollUl.stop(true).animate({left: liW * now}, 500);
	}
}

export default class BorrowData extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this._tabFn = this.tabFn.bind(this);
		this.state = {
			data: null,
		};
	}
	componentDidMount(){
		let self = this;
		this._getData(function(){
			self._tabFn();
		});
				
	}
	getData(callback){
		let self = this;	
		$.ajax({
			url: '/project/borrowPic.html',
			type: 'POST',
			dataType: 'json',
			data: {"projectId": $("#projectId").val()}		
		})
		.done(function(data) {
			if(data.result){
				self.setState({data: data});
				if(callback){
					callback();
				}
			} else {
				error(data.msg);
			}			
		})
		.fail(function() {
			//error("网络异常，请重试！");
		})		
	}
	tabFn(){		
		let tabBox = $("#tabsBox");
		let itemList = tabBox.find(".tab-item");
		tabBox.find(".tab a").on("click", function(){
			if($(this).hasClass("active")){return false;}
			$(this).addClass("active").siblings().removeClass("active");
			itemList.eq($(this).index()).addClass("active").siblings().removeClass("active");
			let index=$('#tabsBox .tab a').index($(this))
			console.log($(".tab-item").eq(index).find(".scroll-content li").length)
            if($(".tab-item").eq(index).find(".scroll-content li").length==0){
                $(".tab-item").eq(index).find(".btns").hide()
            }
		});
		//滚动效果（所以资料）
		scrollfn($("#scrollBox1"));
		//滚动效果（基本信息）
		scrollfn($("#scrollBox2"));
		//滚动效果（抵押资料）
		scrollfn($("#scrollBox3"));
	}
	render() {
		let data = this.state.data;
		if(!data){return false;}		
		let fileTypeL1 = 0;
		let fileTypeL2 = 0;
		let len = data.list.length;		
		for(let i = 0; i < len; i++){			
			if(data.list[i].fileType == 1){
				fileTypeL1++;
			} else if(data.list[i].fileType == 2){
				fileTypeL2++
			}
		}
		return (
			<div className="borrowData-side">
				<div className="tabs-box" id="tabsBox">
					<div className="tab"><a href="javascript:;" className="first active">所有资料（{len}）</a><a href="javascript:;">基本信息（{fileTypeL1}）</a><a href="javascript:;" className="last">抵押资料（{fileTypeL2}）</a></div>
					<div className="tab-items pb20">
						<div className="tab-item active">
							<div className="scroll-box" id="scrollBox1">
								<div className="btns">
									<span className="iconfont btn-left">&#xe64f;</span>
									<span className="iconfont btn-right disabled" title="最后一张">&#xe650;</span>
								</div>
								<div className="scroll-content">
									<ul>
										{data.list.map(function(item, k){								
											return <li key={k}><span className="pic"><img src={item.filePath}/></span></li>
										})}
									</ul>
								</div>								
							</div>
						</div>
						<div className="tab-item">
							<div className="scroll-box" id="scrollBox2">
								<div className="btns">
									<span className="iconfont btn-left">&#xe64f;</span>
									<span className="iconfont btn-right disabled" title="最后一张">&#xe650;</span>
								</div>
								<div className="scroll-content">
									<ul>
										{data.list.map(function(item, k){
											if(item.fileType == 1){
												return <li key={k}><span className="pic"><img src={item.filePath}/></span></li>
											}
										})}
									</ul>
								</div>								
							</div>
						</div>
						<div className="tab-item">
							<div className="scroll-box" id="scrollBox3">
								<div className="btns">
									<span className="iconfont btn-left">&#xe64f;</span>
									<span className="iconfont btn-right disabled" title="最后一张">&#xe650;</span>
								</div>
								<div className="scroll-content">
									<ul>
										{data.list.map(function(item, k){
											if(item.fileType == 2){
												return <li key={k}><span className="pic"><img src={item.filePath}/></span></li>
											}
										})}
									</ul>
								</div>								
							</div>
						</div>
					</div>
				</div>
			</div>
		);
	}
}