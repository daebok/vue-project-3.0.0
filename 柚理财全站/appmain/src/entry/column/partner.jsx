import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Columnmenu from '../../component/columnmenu/columnmenu';
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

function tabchange(){
  $("body").on("click", ".tablist li", function(){
    var index = $(this).index();
    $(this).addClass("tabhover").siblings("li").removeClass("tabhover");
    $(".tabcon ul li").addClass("hide");
    $(".tabcon ul li").eq(index).removeClass("hide");
  });

 /* $("body").on("mouseover", ".tablist li img", function(){
    $(this).removeClass("gray")
  });

  $("body").on("mouseout", ".tablist li img", function(){
    $(this).addClass("gray")
  });*/
}

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
    tabchange();
  }
  getData(){
    let self = this;    
    $.ajax({
      url: '/column/getArticleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"sectionCode": "partner"}        
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
          <div className="title">合作伙伴</div>
          <div className="small_title">贷款就找我 专业的产业链金融平台</div>
          <div className="partnerbox">
            <ul className="tablist clearfix">
              {data.articleList.rows.map(function(item, k){
                if(k==0){
                  return <li key={k} className="tabhover"><img src={item.picPath} /></li>
                }
                else{
                  return <li key={k}><img src={item.picPath}/></li>
                }
              })}
            </ul> 
            <div className="tabcon">
                <ul>
                  {data.articleList.rows.map(function(item, k){
                    if(k==0){
                      return <li className="clearfix" key={k}>
                        <div className="right">
                            <div className="titleCon">{item.title}</div>
                            <div dangerouslySetInnerHTML={{__html: item.content}}></div>
                        </div>
                      </li>
                    }
                    else{
                      return <li className="clearfix hide" key={k}>
                        <div className="right">
                            <div className="titleCon">{item.title}</div>
                            <div dangerouslySetInnerHTML={{__html: item.content}}></div>
                        </div>
                      </li>
                    }
                  })}
                </ul>
            </div>  
        </div>
        </div>
        )   
  }
}
ReactDOM.render(<Columnlist />, document.getElementById("pantercon"));
ReactDOM.render(<Columnmenu partner={1} />, document.getElementById("columnmenu"));