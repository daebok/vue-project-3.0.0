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

export default class HotgoodsList extends React.Component{
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
      dataType: 'json'
    })
    .done(function(data) {
      self.setState({data: data});
      $("ul.hot li").each(function(){
        $(this).mouseover(function(){
          $(this).find(".describe").css("display","block");
        })
        $(this).mouseout(function(){
          $(this).find(".describe").css("display","none");
        })
      })
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  render() {
    let data = this.state.data;
    if(!data){return false;}
    let avatarPhoto = $(".serverUrl").val();
    return (
          <div>
            <ul className="clearfix hot">
                {
                data.list.map(function(k,v){
                  var urlval = "/scoreshop/goodsDetails.html?id="+k.id;
                  let avatarPhotoUrl = avatarPhoto+k.picSmall;
                  return <li key={v}><a href={urlval}><img src={avatarPhotoUrl}/><div className="describe">{k.goodsName}</div></a></li>
                })
              }
            </ul>
          </div>
    );
  }
}
