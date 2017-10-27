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

export default class Scroll extends React.Component{
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
      url: '/index/articleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"code": "scrollPic",'pageSize':6}
    })
    .done(function(data) {
      self.setState({data: data});
      $('.flexslider').flexslider({
        directionNav: true,
        pauseOnAction: false,
        slideshowSpeed:5000
      });     
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })    
  }
  render() {
    let data = this.state.data;
    if(!data){return false;}
    return (
          <div id="banner_wrap"> 
            <div className="banner">
              <div className="flexslider sss">
                <ul className="banner_con slides">
                  {                                                           
                  data.list.map(function(k,v){
                    var bgpic = "url("+ k.picPath + ") no-repeat center 0";
                    return <li key={v}  style={{background:bgpic}}><a href={k.url}></a></li>
                  })
                }
                </ul>
              </div>
            </div>
          </div>  
    );
  }
}