import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Columnmenu from '../../component/columnmenu/columnmenu';
import './dataReport.less';
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
    this._getData(1);
  }
  getData(page){
    let self = this;
    let pages = "1";
    if(page){
      pages = page;
    }    
    $.ajax({
      url: '/column/getArticleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"sectionCode": $("#sectionCodeval").val(),"page.pageSize":15,"page.page":pages}        
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
    let listHtml = <li style={{"width": "1180px","background":"#ffffff","border-radius":"0","padding": "60px 0","border": "none", "textAlign": "center"}} className="nolist"><em className="iconfont">&#xe638;</em><span>暂无数据</span></li>;
    let pageHtml = "";
    if(data.articleList.rows && data.articleList.rows.length>0){
      listHtml = data.articleList.rows.map(function(item, k){
                let url = "/column/dataReportDetail.html?uuid="+item.id+"&sectionCode="+item.sectionCode;
                return <li key={k}>
                    <a href={url}><img src={item.picPath ? item.picPath : $("#header").attr("data-url") + "/data/img/avatar/news_list_bg.png"}/></a>
                </li>
              });
      pageHtml = <Pagination defaultCurrent={data.articleList.page} pageSize={data.articleList.pageSize} total={data.articleList.count} onChange={this._pageHander} />;
    }

    if(data.isLeaf==0){
      return (
        <div>
          <div className="title">数据报告</div>
          <div className="small_title">贷款就找我 专业的产业链金融平台</div>
          <div className="listCon">
            <ul className="columnul clearfix">            
              {listHtml}
            </ul>
            <div className="page page-center mt30">{pageHtml}</div>
          </div>
        </div>
        )
    }
    else{
      return (
        <div>
          <div className="title">数据报告</div>
          <div className="small_title">贷款就找我 专业的产业链金融平台</div>
          <div className="listCon">
            <ul className="columnul clearfix">            
              {listHtml}
            </ul>
            <div className="page page-center mt30">{pageHtml}</div>
          </div>
        </div>
        )
    }
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("columnlist"));

ReactDOM.render(<Columnmenu partner={3} />, document.getElementById("columnmenu"));
