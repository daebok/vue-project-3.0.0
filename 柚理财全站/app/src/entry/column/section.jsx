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
    this._pageHander = this.pageHander.bind(this);
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
      url: '/column/getArticleList.html',
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
  pageHander(page){   
    this._getData(page);
  }
  render(){
    let data = this.state.data;
    if(!data){return false;}
    if(data.isLeaf==0){
      return (
        <div>
          <div className="title">{data.sectionName}</div>
          <ul className="columnul">            
            {data.articleList.rows.map(function(item, k){
              let url = "/column/articleDetail.html?uuid="+item.id+"&sectionCode="+item.sectionCode;
              let createTime = Publiclib.formatDate(item.createTime);
              let content = (item.content).replace(/<[^>].*?>/g,"");
              let str = content.replace(/&nbsp;/ig,'');
              let contents = ""+str.substring(0,90)+"..."; 
            	return <li className="clearfix" key={k}>
            		<div className="columnul-img"><img src={item.picPath}/></div>
            		<div className="columnul-con">
            			<div className="columnul-title"><a href={url}>{item.title}</a></div>
            			<p>发布时间：{createTime}　　点击: {item.clicks} 次</p>
            			<div className="columnul-word">
            				<span>{contents}</span><a href={url}>全文</a>
            			</div>
            		</div>
            	</li>
            })}
          </ul>
          <div className="page page-center mt30"><Pagination defaultCurrent={data.articleList.page} pageSize={data.articleList.pageSize} total={data.articleList.totalPage} onChange={this._pageHander} /></div>
        </div>
        )
    }
    else{
      return (
        <div>
          <div className="title">{data.articleInfo.sectionName}</div>
          <div className="singlePage" dangerouslySetInnerHTML={{__html: data.articleInfo.content}}></div>
        </div>
        )
    }
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("columnlist"));
ReactDOM.render(<Columnmenu />, document.getElementById("columnmenu"));