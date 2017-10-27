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

export default class DigitalList extends React.Component{
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
      url: '/scoreshop/getBestseller.html',
      type: 'POST',
      dataType: 'json',
      data: {"goodsCategoryId": "digital"}
    })
    .done(function(data) {
      self.setState({data: data});
      $(".digital ul li").eq(2).css({"border":0,"width":299});
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
            <ul className="clearfix">
                {
                data.list.map(function(k,v){
                  var urlval = "/scoreshop/goodsDetails.html?id="+k.id;
                  return <li key={v}><a href={urlval}><img src={k.picPath}/></a><div className="describe"><p className="ptitle clearfix"><a href={urlval}>{k.goodsName}</a></p><p className="score"><font>{k.score}</font>积分</p><p className="num">剩余数量：{k.numbers}</p></div></li>
                })
              }
            </ul>
          </div>
    );
  }
}
