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
    if(!data){return false;}
    return (
        <div>
          <div className="title"><Icon type="copy" />关于我们</div>
          <ul>
            {data.sectionList.map(function(item, k){
              const link = "/column/sectionDetail.html?sectionCode="+item.sectionCode;                
              //let hrefval = window.location.href;
              //let hrefval2 = hrefval.split("=");
              let sectionCodeval = $("#sectionCodeval").val();
              if(item.sectionCode==sectionCodeval){
                return <li key={k} className="menu-link active"><Input value={item.sectionCode}  type="hidden" className="sectionCode"/><a href={link}>{item.sectionName}</a></li>
              }
              else{
                return <li key={k} className="menu-link"><Input value={item.sectionCode}  type="hidden" className="sectionCode"/><a href={link}>{item.sectionName}</a></li>
              }
            })}
            <li className="menu-link"><Input value="partner"  type="hidden" className="sectionCode"/><a href="/column/partner.html">合作伙伴</a></li>
          </ul>
        </div>
    );
  }
}