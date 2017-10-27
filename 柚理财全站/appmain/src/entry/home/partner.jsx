import React from 'react';
import ReactDOM from 'react-dom';
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

function Imgchange(){
  $("body").on("mouseover", ".con li img", function(){
    $(this).removeClass("gray")
  });

  $("body").on("mouseout", ".con li img", function(){
    $(this).addClass("gray")
  });
}

//图片滚动
function DY_scroll(wraper, prev, next, img, speed, or) {
    var wraper = $(wraper);
    var prev = $(prev);
    var next = $(next);
    var img = $(img).find('ul');
    var w = img.find('li').outerWidth(true);
    var s = speed;
    next.click(function () {
        img.animate({ 'margin-left': -w}, function () {
            img.find('li').eq(0).appendTo(img);
            img.css({ 'margin-left': 0 });
        });
    });
    prev.click(function () {
        img.find('li:last').prependTo(img);
        img.css({ 'margin-left': -w});
        img.animate({ 'margin-left': 0 });
    });
    if (or == true) {
        ad = setInterval(function () { next.click(); }, s * 1000);
        wraper.hover(function () { clearInterval(ad); }, function () { ad = setInterval(function () { next.click(); }, s * 1000); });

    }
}

export default class Partner extends React.Component{
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
			url: '/index/articleList.html',
			type: 'POST',
			dataType: 'json',
			data: {"code": "partner",'pageSize':7}
		})
		.done(function(data) {
			self.setState({data: data});
			DY_scroll('.scroll-box', '.scroll-box .btn-left', '.scroll-box .btn-right', '.scroll-content', 4, false); // true为自动播放，不加此参数或false就默认不自动
			//Imgchange();			
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
				<div className="partner_box">
					<div className="partner">
						<div className="title clarfix">合作伙伴<i>互帮互助 实现共同发展新目标</i><a href="/column/partner.html" className="clearfix">查看更多<em className="iconfont">&#xe636;</em></a></div>
						<div className="scroll-box" id="scrollBox1">
							<div className="btns">
								<span className="iconfont btn-left">&#xe64f;</span>
								<span className="iconfont btn-right">&#xe650;</span>
							</div>
							<div className="scroll-content">
								<ul>
									{data.list.map(function(item, k){						
										return <li key={k}><a href={item.url} target="_blank"><img src={item.picPath}/></a></li>
									})}
								</ul>
							</div>								
						</div>
					</div>
				</div>
			</div>
		);
	}
}