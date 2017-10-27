import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Columnmenu from '../../component/columnmenu/columnmenu';
import './index.less';
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
    let createTime = Publiclib.formatDate(data.articleInfo.createTime);
    let backurl = "/column/sectionDetail.html?sectionCode="+data.articleInfo.sectionCode;
    let nexta ='';
    let previousa ='';
    if(data.articleInfo.nextUuid){
    let nexturl = "/column/articleDetail.html?uuid="+data.articleInfo.nextUuid+"&sectionCode="+data.articleInfo.sectionCode;
    nexta = <a href={nexturl}>下一篇：{data.articleInfo.nextTitle}</a>
    }
    if(data.articleInfo.previousUuid){
    let previousurl = "/column/articleDetail.html?uuid="+data.articleInfo.previousUuid+"&sectionCode="+data.articleInfo.sectionCode;
    previousa =	<a href={previousurl}>上一篇：{data.articleInfo.previousTitle}</a>
    }
      return (
        <div>
         	<div className="title">{data.articleInfo.sectionName}</div>
         	<div className="detailcon">
         		<div className="title">{data.articleInfo.title}</div>
         		<p>发布时间：{createTime}　　点击: {data.articleInfo.clicks} 次</p>
         		<div className="con"><span dangerouslySetInnerHTML={{__html: data.articleInfo.content}}></span></div>
         		<div className="other clearfix">
         			<div className="left">
         			  {previousa}
                {nexta}
         			</div>
         			<div className="right">
         				<a href={backurl}>返回列表</a>
         			</div>
         		</div>
         	</div>
        </div>
        )
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("columncontent"));
ReactDOM.render(<Columnmenu />, document.getElementById("columnmenu"));
