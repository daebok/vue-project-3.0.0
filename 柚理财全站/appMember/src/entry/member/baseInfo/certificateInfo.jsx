import React from 'react';
import { Modal,Tooltip } from "antd";
import Imgupload from '../../../component/ImageUpload/uploadBaseinfo';
import {Publiclib} from '../../../common/common';

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

let baseInfoImgArray = [];

export default class CertificateInfo extends React.Component{
  constructor(props){
      super(props);
      this._getData = this.getData.bind(this);
      this.setArray = this.setArray.bind(this);            
      this.state = {
        data : null,       
      }
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/member/usercertification/getCertificateInfo.html',
      type: 'POST',
      dataType: 'json',     
    })
    .done(function(data) {
      if(data.result){
        self.setState({data: data});
      }      
    })
    .fail(function() {
      console.log("error");
    })    
  }
  setArray(eq){
    baseInfoImgArray[eq]={"pic":[]};
  }
  componentDidMount(){
    this._getData();
  } 
  render() {
    let data = this.state.data;
    let self = this; 
    if(!data){
      return false;
    }        
    return (      
      <table className="table certificateInfo-table">
        <thead>
          <tr><th>材料名称</th><th>状态</th><th>审核时间</th><th className="th-listUpfile">上传文件</th><th>操作</th></tr>
        </thead>
        <tbody>
        {data.qualificationList.map(function(item, i){
          return <Imgupload key={i} eq={i} item = {item} len = {5} imgArray = {baseInfoImgArray} setArray ={self.setArray}/>
        })}
        </tbody>
      </table>      
    );
  }
}