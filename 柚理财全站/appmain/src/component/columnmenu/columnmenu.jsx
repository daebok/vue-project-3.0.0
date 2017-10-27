import React from 'react';
import ReactDOM from 'react-dom';
import {Icon, Input} from 'antd';

export default class ColumnMenu extends React.Component{
  constructor(props) {
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
      url: '/column/getChildSection.html',
      type: 'POST',
      dataType: 'json',
      data: {"sectionCode": $("#sectionCodeval").val()}        
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
    var _this=this
    if(!data){return false;}
    return (
        <div>
          <div className="columnBg"></div>
          <ul className="clearfix">
            {/*<li className={this.props.partner==2?'menu-link active':'menu-link'}><a href="/column/aboutUs.html">平台简介</a></li>*/}
            {data.sectionList.map(function(item, k){
              const link = "/column/sectionDetail.html?sectionCode="+item.sectionCode;                
              //let hrefval = window.location.href;
              //let hrefval2 = hrefval.split("=");
              let sectionCodeval = $("#sectionCodeval").val();
              if(_this.props.partner==1){
                sectionCodeval=_this.props.partner
              }
              if(item.sectionCode==sectionCodeval){
                return <li key={k} className="menu-link active"><Input value={item.sectionCode}  type="hidden" className="sectionCode"/><a href={link}>{item.sectionName}</a></li>
              }
              else{
                return <li key={k} className="menu-link"><Input value={item.sectionCode}  type="hidden" className="sectionCode"/><a href={link}>{item.sectionName}</a></li>
              }
            })}
            <li className={this.props.partner==1?'menu-link active':'menu-link'}><Input value="partner"  type="hidden" className="sectionCode"/><a href="/column/partner.html">合作伙伴</a></li>
            <li className={this.props.partner==3?'menu-link active':'menu-link'}><a href="/column/dataReport.html">数据报告</a></li>
          </ul>
        </div>
    );
  }
}