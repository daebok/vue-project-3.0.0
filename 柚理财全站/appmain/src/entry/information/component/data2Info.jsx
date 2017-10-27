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

export default class Data1Info extends React.Component{
	constructor(props) {
	    super(props);
	    this._tabHander = this.tabHander.bind(this);
	    this._getinfo = this.getinfo.bind(this);
  	}
	componentDidMount(){
	 	this._getinfo();
	 	this._tabHander();
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
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
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
                    //name: "月份",
                    data : data.collectMonth,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
            
                {
                    name:'待收总额',
                    type:'bar',
                    stack:'one',
                    barWidth: '60%',
                    data:data.collectData,
                    itemStyle:{
                      normal:{
                        color:'#3c92ee'
                      }
                    }
                },{
                    name:'已收金额',
                    type:'bar',
                    stack:'one',
                    barWidth: '60%',
                    data:data.repaidData,
                    itemStyle:{
                      normal:{
                        color:'#ee4e4e'
                      }
                    }
                }
            ]
        };


      var myChart = echarts.init(document.getElementById('chart1'));
      myChart.setOption(option);
    })
    .fail(function() {
      //error('网络异常，请重试！');
    });
}
	render() {
		return (
			<div>
				<div className="data2Info">
					<div className="clearfix">
						<div className="title">业务总览</div>
						<ul className="tabs clearfix">
							<li data-val="1" className="active">交易总金额</li>
							<li data-val="2">交易总笔数</li>
							<li data-val="3">借款人数量</li>
							<li data-val="4">投资人数量</li>
						</ul>
					</div>
					<div className="chart1" id="chart1">

					</div>
					<div className="endTime">
						数据截止 2016-7-11
					</div>
				</div>
			</div>
		);
	}
}

