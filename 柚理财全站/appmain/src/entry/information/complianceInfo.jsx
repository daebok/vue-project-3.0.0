import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
import { Modal, Pagination } from "antd";
import { Publiclib } from '../../common/common';

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

class Columnlist extends React.Component{
  constructor(props) {
    super(props);
    this._getData = this.getData.bind(this);
    this._getsjData = this.getsjData.bind(this);
    this._getflData = this.getflData.bind(this);
    this._tabHander = this.tabHander.bind(this);
    this._getSearchData = this.getSearchData.bind(this);
    this.state = {
      data: null,
      sjdata:null,
      fldata:null
    };
  }
  componentDidMount(){
    this._getData();
    this._getsjData();
    this._getflData();
    this._tabHander();
  }
  //类型切换
  tabHander(){
    let _this = this;
    $("body").on("click", ".tab1 li", function(){
      if($(this).hasClass("active")){return;}
      $(this).addClass("active").siblings().removeClass("active");
      let timeval = $(this).attr("data-val");
      _this._getSearchData(timeval);
    });
  }
  //获取筛选数据
  getSearchData(timeval){
    let _this = this;
    $.ajax({
        url: '/information/list.html',
        type: 'POST',
        dataType: 'json',
        data: {"time": timeval}
    }).done(function(data) {
      if(data.result){
        _this.setState({data: data});
      } else {
        error(data.msg);
      }
    }).fail(function() {
        //error('网络异常，请重试！');
    });
  }
  getData(){
    let self = this;
    let time1 = self.state.time1;
    $.ajax({
      url: '/information/list.html',
      type: 'POST',
      dataType: 'json',
      data: {"time": time1}     
    })
    .done(function(data) {
      self.setState({data: data});  
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  getsjData(){
    let self = this;
    $.ajax({
      url: '/information/list1.html',
      type: 'POST',
      dataType: 'json'   
    })
    .done(function(data) {
      self.setState({sjdata: data});  
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  getflData(){
    let self = this;
    $.ajax({
      url: '/information/list1.html',
      type: 'POST',
      dataType: 'json'   
    })
    .done(function(data) {
      self.setState({fldata: data});  
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  render(){
    let data = this.state.data;
    if(!data){return false;}
    let sjdata = this.state.sjdata;
    if(!sjdata){return false;}
    let fldata = this.state.fldata;
    if(!sjdata){return false;}
    let avatarPhoto = $(".serverUrl").val();
      return (
        <div className="complianceInfobox">
          <div className="banner"></div>
          <div className="navigation">
            <ul>
              <li><a href="">从业结构信息</a></li>
              <li><a href="">平台运营数据</a></li>
              <li><a href="" className="active">合规信息</a></li>
            </ul>
          </div>

          <div className="con1">
            <div className="title">年度报告</div>
            <ul className="tab1 clearfix">
              <li className="active" data-val="2016">2016年</li>
              <li data-val="2015">2015年</li>
              <li data-val="2014">2014年</li>
            </ul>
            <div className="listCon1 clearfix">
              <ul className="clearfix">
                {data.list.map(function(item, k){
                  let avatarPhotoUrl = avatarPhoto+item.filePath;
                  return <li key={k}><img src={avatarPhotoUrl} className="gray"/><a href="">{item.title}</a></li>
                })}
              </ul>
            </div>
          </div>
          <div className="con2">
            <div className="listCon2">
            <div className="title">审计报告</div>
              <ul>
                {sjdata.list.map(function(item, k){
                  return <li key={k} className="clearfix"><span>{item.title}</span><div className="line"></div><a href={item.url}>详情</a>
                </li>
                })}
              </ul>
            </div>
          </div>
          <div className="con1">
            <div className="title">合规评估报告</div>
            <ul className="tab1 clearfix">
              <li className="active" data-val="2016">2016年</li>
              <li data-val="2015">2015年</li>
              <li data-val="2014">2014年</li>
            </ul>
            <div className="listCon1 clearfix">
              <ul className="clearfix">
                {data.list.map(function(item, k){
                  let avatarPhotoUrl = avatarPhoto+item.filePath;
                  return <li key={k}><img src={avatarPhotoUrl} className="gray"/><a href="">{item.title}</a></li>
                })}
              </ul>
              <div className="tips">
                根据互联网金融平台的委托，指派专业律师团队对互联网金融平台进行法律尽职调查并根据法律法规及政策的相关规定，为互联网金融平台
提出整改意见或规范方案，并为互联网金融平台出具相关法律意见书。
              </div>
            </div>
          </div>
          <div className="con3">
            <div className="title">网贷法律法规</div>
            <ul>
               {sjdata.list.map(function(item, k){
                  return <li key={k}><a href={item.url}>{item.title}</a></li>
                })}
            </ul>
          </div>
        </div>
        )   
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("complianceInfo"));
