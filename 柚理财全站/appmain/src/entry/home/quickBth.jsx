import React from 'react';
import ReactDOM from 'react-dom';
import '../../style/shortcut.less';
import Calculator from '../../component/calculator/calculatorBottom';
 $("body").on("click", ".backTop", function(){
	var _this = $(this);
	$('html,body').animate({ scrollTop: 0 }, 500 ,function(){
		_this.hide();
	});
});
$(window).scroll(function(){
	var htmlTop = $(document).scrollTop();
	if( htmlTop > 500){
		$(".backTop").show();
	}else{
		$(".backTop").hide();
	}
});

$("body").on("mouseover", ".overli", function(){
	$(this).find("em").hide();
	$(this).find("a").show();
})
$("body").on("mouseout", ".overli", function(){
	$(this).find("em").show();
	$(this).find("a").hide();
})

export default class Quickbtn extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this.state = {
			visible:false,
			data:''
		};
		}
		componentDidMount(){
			this._getData();
		}
		getData(){
			let self = this;
			$.ajax({
				url: '/index/countInfo.html',
				type: 'POST',
				dataType: 'json',
			})
				.done(function(data) {
					self.setState({data: data});
				})
				.fail(function() {
					//error("网络异常，请重试！");
				})
		}
	render() {if(!this.state.data){
		return false
	}
		return (
			<div>
				<div className="Quickbtn clearfix">
					<ul>
						<li className="overli qqBtn"><em className="iconfont ">&#xe60b;</em><a href="javascript:;" className="qq"><span>在线</span><font>客服</font></a>
							<div className="qqOnline">
								<i className="sj"></i>
								<h3>在线客服</h3>
								<ul>{
									this.state.data.onlineServer.map(function (data,k) {
										let url="http://wpa.qq.com/msgrd?v=3&uin="+data.qq+"&site=qq&menu=yes"
										return <li key={k}><a href={url}><i className="iconfont">&#xe670;</i>QQ客服</a></li>
									})
								}
								</ul>
							</div>
						</li>
						<li className="overli"><em className="iconfont ">&#xe633;</em><a href="/member/myAssistant/opinionAdd.html" className="opinion"><span>意见</span><font>反馈</font></a></li>
						<li className="backTop" style={{display:'none'}}><em className="iconfont ">&#xe631;</em></li>
					</ul>
				</div>
				<div className="calculator-fix">
					<Calculator />
				</div>

			</div>
		);
	}
}