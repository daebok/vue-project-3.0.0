import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './dataReportDetail.less';
import { Modal, Pagination } from "antd";
import { Publiclib } from '../../common/common';

class Columnlist extends React.Component{
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
      url: '/column/articleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"uuid": $("#uuid").val(),"sectionCode": $("#sectionCodeval").val()}
    })
    .done(function(data) {
      self.setState({data: data});
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  render(){
    let data = this.state.data;
    if(!data){return false;}
      return (
        <div>
         		<div className="con"><span dangerouslySetInnerHTML={{__html: data.articleInfo.content}}></span></div>
        </div>
        )
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("columncontent"));