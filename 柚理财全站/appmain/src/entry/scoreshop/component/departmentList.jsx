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

export default class DepartmentList extends React.Component{
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
      url: '/scoreshop/getBestseller.html?goodsCategoryId='+this.props.id,
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      self.setState({data: data});
      $(".department ul li:nth-child(3n)").css({"border":0,"width":299});
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
            <ul className="clearfix">
                {
                data.list.map(function(k,v){
                  let scoreval = (k.score).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
                  var urlval = "/scoreshop/goodsDetails.html?id="+k.id;
                  let avatarPhotoUrl = avatarPhoto+k.picSmall;
                  let classType = k.lessNum ==0 ? "sellOut" : "";
                  return <li key={v} className={classType}><a href={urlval}><img src={avatarPhotoUrl}/></a><div className="describe"><p className="ptitle clearfix"><a href={urlval}>{k.goodsName}</a></p><p className="score"><font>{scoreval}</font>积分</p><p className="num">剩余数量：{k.lessNum}</p></div><div className="sellOutbg"></div></li>
                })
              }
            </ul>
          </div>
    );
  }
}
