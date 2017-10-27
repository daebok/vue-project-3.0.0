import React from 'react';
import ReactDOM from 'react-dom';
import { Modal, Input} from "antd";

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

export default class Data5Info extends React.Component{
	constructor(props) {
	    super(props);
	    this._tabHander = this.tabHander.bind(this);
        this._percent = this.percent.bind(this);
	    this._getinfo = this.getinfo.bind(this);
  	}
	componentDidMount(){
	 	this._getinfo();
        this._percent();
	 	this._tabHander();
	}
    //百分比
    percent(){
        $('.knob').each(function(){
            let val = parseInt($(this).val());
            if(val <= 25 && val > 0){
                $(this).attr("data-fgColor","#ffa234");
            }else if(val == 0){
                $(this).attr("data-fgColor","#ffa234");
            }else if(val > 25 && val <= 50){
                $(this).attr("data-fgColor","#ffa234");
            }else if(val > 50 && val < 100){
                $(this).attr("data-fgColor","#ffa234");
            }else{
                let isIE = function(ver){
                    let b = document.createElement('b')
                    b.innerHTML = '<!--[if IE ' + ver + ']><i></i><![endif]-->'
                    return b.getElementsByTagName('i').length === 1
                }
                if(isIE(8)){
                    $(this).next().addClass("investComplete100");
                }
                $(this).attr("data-fgColor","#ffa234");
            }
        });
        $('.knob').knob({
            'width':76,
            'height':76,
            'thickness':.1
        });
    }
	//类型切换
	  tabHander(){
	    let _this = this;
	    $("body").on("click", ".tabs li", function(){
	      if($(this).hasClass("active")){return;}
	      $(this).addClass("active").siblings().removeClass("active");
	      let id = $(this).attr("data-val");
	      _this._getinfo(id);
	    });
	  }

	getinfo (id){
    $.ajax({
      url: '/member/account/getAccountInfo.html',
      type: 'POST',
      dataType: 'json',
      data: {"time": id}
    })
    .done(function(data) {
        if( !data.result ){
            error(data.msg,data.url);
            return false;
        }

        let option = {
            title: {
            },
            tooltip : {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    label: {
                        backgroundColor: '#caebf4'
                    }
                }
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    boundaryGap : false,
                    data : ['周一','周二','周三','周四','周五','周六','周日']
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'搜索引擎',
                    type:'line',
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'top'
                        }
                    },
                    areaStyle: {normal: {}},
                    data:[820, 932, 901, 934, 1290, 1330, 1320]
                }
            ]
        };


      var myChart = echarts.init(document.getElementById('chart2'));
      myChart.setOption(option);
    })
    .fail(function() {
      //error('网络异常，请重试！');
    });
}
	render() {
		return (
			<div>
				<div className="data5Info clearfix">
                    <div className="title">待还数据</div>
                    <div className="clearfix con">
                        <div className="left">
                            <div className="top clearfix">
                                <em className="iconfont">&#xe660;</em><span><font>356万</font><p>待还余额（元）</p></span>
                            </div>
                            <div className="down clearfix">
                                <div className="dleft">
                                    <div className="index-jd"><span className="investProcess"><Input type="text" defaultValue="30" className="knob" /><span className="investComplete"><font>30</font>%</span></span></div>
                                    <div className="word">最大单<p>借款余额占比</p></div>
                                </div>
                                <div className="dright">
                                    <div className="index-jd"><span className="investProcess"><Input type="text" defaultValue="30" className="knob" /><span className="investComplete"><font>30</font>%</span></span></div>
                                    <div className="word">最大10户<p>借款余额占比</p></div>
                                </div>
                            </div>
                        </div>
                        <div className="right">
        					<div className="clearfix">
        						<div className="title">待还余额</div>
        						<ul className="tabs clearfix">
        							<li data-val="1" className="active">每日</li>
        							<li data-val="2">每周</li>
        							<li data-val="3">每月</li>
        			    		</ul>
        					</div>
        					<div className="chart2" id="chart2">
        					</div>
                        </div>
                    </div>
				</div>
			</div>
		);
	}
}

