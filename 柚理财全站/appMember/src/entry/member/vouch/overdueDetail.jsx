import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Tabs,Modal } from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './overdueDetail.less';
import {Publiclib} from '../../../common/common';

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

class Detail extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this.state = {
      data: null,
    };
  }
  componentDidMount(){
    this._getData();
    $("tr th:even").css("background-color","#fafafa");
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/member/vouch/overdueDetailData.html',
      type: 'POST',
      dataType: 'json',
      data: {"uuid":$("#uuid").val()}
    })
    .done(function(data) {
        self.setState({data: data});
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  render() {
    let data = this.state.data;
    if(!data) return false;
    let repayTime = Publiclib.formatDate(data.repayTime);
    let realRepayTime = Publiclib.formatDate(data.realRepayTime, 3);
    return (
      <div>
        <div className="title">借款信息</div>
        <div className="deail">
            <table>
              <tbody>
                <tr>
                  <th className="titlename">项目编号</th>
                  <th>{data.projectNo}</th>
                  <th className="titlename">项目名称</th>
                  <th>{data.projectName}</th>
                </tr>
                <tr>
                  <th className="titlename">借款方</th>
                  <th>{data.realName}</th>
                  <th className="titlename">逾期期数</th>
                  <th>{data.periodsStr}</th>
                </tr>
                <tr>
                  <th className="titlename">应还款时间</th>
                  <th>{repayTime}</th>
                  <th className="titlename">逾期天数</th>
                  <th>{data.lateDays}天</th>
                </tr>
                <tr>
                  <th className="titlename">应还本金</th>
                  <th>{data.capital} 元</th>
                  <th className="titlename">应付利息</th>
                  <th>{data.interest} 元</th>
                </tr>
                <tr>
                  <th className="titlename">罚息</th>
                  <th>{data.lateInterestSum} 元</th>
                  <th className="titlename"></th>
                  <th></th>
                </tr>
                </tbody>
            </table>
        </div>
        <div className="record">
          <div className="title">垫付</div>
          <p>垫付金额<font>{data.payedAmount}</font>元</p>
          <p>垫付时间<span>{realRepayTime}</span></p>
        </div>
      </div>
    );
  }
}

ReactDOM.render(<Detail />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"6"}/>,  document.getElementById("j-sider-menu"));
