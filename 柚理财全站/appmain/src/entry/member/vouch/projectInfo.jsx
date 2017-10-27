import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import {Tabs} from 'antd';
const TabPane = Tabs.TabPane;
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './projectInfo.less';
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
    $("tr th:even").addClass("eventh");
  }
  getData(){
    let self = this;    
    $.ajax({
      url: '/member/vouch/getProjectInfo.html',
      type: 'POST',
      dataType: 'json', 
      data: {"uuid":$("#uuid").val()}     
    })
    .done(function(data) {
      if(data.result){
        self.setState({data: data});
      } else {
        error(data.msg);
      }     
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })    
  }
  render() {
    let data = this.state.data;    
    if(!data){return false;}
    let createTime = Publiclib.formatDate(data.borrow.createTime, 2);
    let verifyTime = data.projectVerifyLog.createTime ? Publiclib.formatDate(data.projectVerifyLog.createTime, 2) : "";
    let borrow_nature = Publiclib.borrowNature(data.borrow.borrowNature);
    let repay_style = Publiclib.repayStyle(data.borrow.repayStyle);
    let vouchStatus = Publiclib.vouchStatus(data.borrow.vouchStatus);
    let imgurl = data.image_server_url;
    return (
      <div>
        <div className="title">借款信息</div>
        <div className="detail">
            <table>
              <tbody>
                <tr>
                  <th>项目编号</th>
                  <th>{data.borrow.projectNo}</th>
                  <th>项目名称</th>
                  <th>{data.borrow.projectName}</th>
                </tr>
                <tr>
                  <th>借款方</th>
                  <th>{data.borrow.realName}</th>
                  <th>预期年化收益率</th>
                  <th>{data.borrow.apr}%</th>
                </tr>
                <tr>
                  <th>项目金额</th>
                  <th>{data.borrow.account}元</th>
                  <th>项目期限</th>
                  <th>{data.borrow.timeLimit}天</th>
                </tr>
                <tr>
                  <th>借款性质</th>
                  <th>{borrow_nature}</th>
                  <th>借款用途</th>
                  <th>{data.borrow.borrowUse}</th>
                </tr>
                <tr>
                  <th>最低起投金额</th>
                  <th>{data.borrow.lowestAccount} 元</th>
                  <th>最高投资总额</th>
                  <th>{data.borrow.mostAccount}元</th>
                </tr>
                <tr>
                  <th>还款方式</th>
                  <th>{repay_style}</th>
                  <th>申请时间</th>
                  <th>{createTime}</th>
                </tr>
                </tbody>
            </table>
        </div>
        <div className="detail">
          <div className="content clearfix">
            <span>借款详情</span>
            <p dangerouslySetInnerHTML={{__html: data.borrow.content}}></p>
          </div>
          <div className="contentImg">
            <span>借款资料</span>
            <ul className="clearfix">
              {data.picList.map(function(item, k){
                let imgUrl= ""+imgurl+""+item.filePath+"";
                  return <li key={k} className="clearfix"><img src={imgUrl}/></li>
              })}
            </ul>
          </div>
          <div className="contentImg">
            <span>抵押物资料</span>
            <ul className="clearfix">
              {data.mtgList.map(function(item, k){
                  let imgUrl= ""+imgurl+""+item.filePath+"";
                  return <li key={k} className="clearfix"><img src={imgUrl}/></li>
              })}
            </ul>
          </div>
        </div>
        <div className="title">审核信息</div>
        <div className="detail">
          <div className="content clearfix">
            <span>审核状态</span>
            <p><font>{vouchStatus}</font></p>
          </div>
          <div className="content clearfix">
            <span>审核时间</span>
            <p>{verifyTime}</p>
          </div>
          <div className="content clearfix">
            <span>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注</span>
            <p>{data.projectVerifyLog.remark}</p>
          </div>
        </div>
      </div>
    );
  }
}


ReactDOM.render(<Detail />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<AccountVouchMenu current = {"4"}/>,  document.getElementById("j-sider-menu"));