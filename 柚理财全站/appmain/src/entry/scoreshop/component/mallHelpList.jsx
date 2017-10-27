import React from 'react';
import ReactDOM from 'react-dom';
import { Modal,Carousel } from "antd";

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

export default class MallHelpList extends React.Component{
  constructor(props){
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
      url: '/column/getArticleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"sectionCode": "scoreHelp"}
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
            <ul className="mallHelpList">
                  {
                  data.articleList.rows.map(function(k,v){
                    return <li className="clearfix" key={v}>
                      <div className="problem clearfix"><em>Q</em><div className="con">{k.title}</div></div>
                      <div className="answer clearfix"><em>A</em><div className="con"><div dangerouslySetInnerHTML={{__html: k.content}}></div></div></div>
                    </li>
                  })
                }
            </ul>
          </div>
    );
  }
}
