import React from 'react';
import { DatePicker } from 'antd';
import { Progress } from 'antd';
const ProgressLine = Progress.Line;


const App = React.createClass({
  render() {
    return (
      <div className="item_list_box">
    					<h4><a href="/invest/1604061382167036/detail.html" title={this.props.name}><em className="color_red">新手</em>{this.props.name}</a></h4>
    					<p className="apr">年化 <span className="number">{this.props.apr}<em>%</em></span></p>
    					<div className="process">
    						<span className="clearfix"><em className="float_left">进度</em><em className="float_right">{this.props.percent}%</em></span>
    					<ProgressLine percent={this.props.percent} showInfo={false}  />
    					</div>
    					<p className="money mt10">总额 <span>{this.props.money}</span>元</p>
    					<p className="time mt10">期限 <span>1</span>个月</p>

    					<p className="btn"><a href="/invest/1604061382167036/detail.html" title="我要投标" id="index_invest_btn">我要存钱</a></p>
					</div>
    );
  },
});

export default App;
