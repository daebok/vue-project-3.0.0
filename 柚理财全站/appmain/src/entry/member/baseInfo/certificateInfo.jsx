import React from 'react';
import { Modal,Tooltip } from "antd";
import ImageUploadList from '../../../component/ImageUpload/index';
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

class InfoList extends React.Component{
  constructor(props){
    super(props);
    this._deletePic = this.deletePic.bind(this);
    this._addFile = this.addFile.bind(this);
    this._uploadControl = this.uploadControl.bind(this);
    this._updateArray = this.updateArray.bind(this);
    this._setIsNew = this.setIsNew.bind(this);
    this.state = {
      name: "",       //材料名称
      status: "",       //状态
      verifyRemark: "",   //审核备注
      verifyTime: "",     //审核时间
      image_server_url: "", //图片服务器地址
      qualificationList: "",  //上传文件列表
      checkbtn:"",
      isNew:false,
      checkOk:false
    }
    this.imgArray = [];
  }
  readStatus(val){    
    if(val == "99"){
      return status = "未上传";
    } else {
      let k = parseInt(val);      
      return ["审核中","审核成功","审核失败"][k];
    }
  }
  deletePic(info){
    const self = this,
    picpath = info.response ? info.response : info.delUrl,
    id = info.response ? info.response : info.id;
    this._setIsNew();
    $.ajax({
      url: '/member/usercertification/deletePic.html',
      type: 'POST',
      dataType: 'json',
      data: {"picPath":picpath,"uploadId":id},
    })
    .done(function(){     
      setTimeout(function(){self._uploadControl()},350);
      self.imgArray.remove(picpath);
    })
    .fail(function() {
      error('网络异常，请重试！');
    });   
  }
  addFile(e){
    console.log(this.imgArray);
    if(!this.imgArray.length){
      error("请上传资料图片");
      return false;
    }

    if(this.state.checkOk){
      error("您的资料正在审核中，请勿重复提交");
      return false;
    }

    if(!this.state.isNew){
      error("您未上传新的图片，不能提交审核");
      return false;
    }

    const self = this;
    $.ajax({
      url: '/member/usercertification/addFile.html',
      type: 'POST',
      dataType: 'json',
      data: {"qualificationType":$(e.target).attr("data-qualificationType"),"picPath":self.imgArray}      
    })
    .done(function(data){

        if(data.result){
          success("您的资料已提交，请等待后台审核。");
          $(".imgtr").eq(self.props.eq).find(".time").html("--");
          self.uploadHide();
          self.setState({
            status : self.readStatus(0),
            checkOk: true     
          });
        } else {
          error("上传失败，请重试！");
        }
      
    })
    .fail(function() {
      error('网络异常，请重试！');
    }); 
  }
  uploadHide(){
    const tr_el = $(".imgtr"),
          imgitem = ".ant-upload-list-item",
          imgselect = ".ant-upload-select";         
          tr_el.eq(this.props.eq).find(imgselect).hide();
          tr_el.eq(this.props.eq).find(".anticon").hide();
          tr_el.eq(this.props.eq).find(".checkbtn").hide();//提交成功 按钮关闭          
          tr_el.eq(this.props.eq).find(".ant-upload-list").addClass('none');
  }
  uploadControl(){
    const tr_el = $(".imgtr"),
          imgitem = ".ant-upload-list-item",
          imgselect = ".ant-upload-select";         
    if(tr_el.eq(this.props.eq).find(imgitem).length > 4){
        tr_el.eq(this.props.eq).find(imgselect).hide();
    }
    else 
    {
      if(this.props.item.status != 0){
        tr_el.eq(this.props.eq).find(imgselect).show();
      }
    } 
  }
  setIsNew(){
    this.setState({
      isNew : true
    });
  }
  updateArray(url){
    this._setIsNew();
  }
  componentDidMount(){
    this._uploadControl();
    this.setState({
      status : this.readStatus(this.props.item.status)      
    });
    if(this.props.item.status == "0"){
      this.uploadHide();
    }
  }
  render() {    
    let item = this.props.item;
    if(!item) return false;
    let image_server_url = $("body").data("url");
    let time = item.verifyTime ? Publiclib.formatDate(item.verifyTime, 2) : "--";
    let imgListInfo = item.fileList ? item.fileList : []; 
    let self = this;
    let opts = [];
    let len = imgListInfo.length;
    for(let i = 0; i < len; i++){
      let opt = {
            //uid: imgListInfo[i].uuid,
            uid:-(i+1),
            id:imgListInfo[i].id,
            // name: imgListInfo[i].name,
            // status: imgListInfo[i].status,
            delUrl:imgListInfo[i].filePath,
            url: image_server_url + imgListInfo[i].filePath,
            thumbUrl:image_server_url + imgListInfo[i].filePath,
            qualificationType:item.qualificationType 
      };
      this.imgArray[i] = imgListInfo[i].filePath;
      opts.push(opt);
    }

    const checkbtn = <td style={{width:"60px"}}><span className="checkbtn" data-qualificationtype={item.qualificationType} onClick={self._addFile}>提交</span></td>;

    let showflag = true;

    if( item.status != "0" ){//status == 0审核中不可上传图片
      this.checkbtn = checkbtn;
    }
    else
    {
      showflag = false;
      this.checkbtn = <td></td>;
    }

    let piclist = <td className="td-listUpfile"><ImageUploadList uploadControl = {this._uploadControl} updateArray = {self._updateArray} opts={opts} deletePic={self._deletePic} showflag={showflag} eq={this.props.eq} image_server_url = {image_server_url}     imgArray={this.imgArray} /></td>;
    return (
      <tr className="imgtr">
        <td style={{width:"116px",fontSize:"16px"}}><h3 style={{fontSize:"16px"}}>{item.name}</h3></td>
        <td style={{width:"110px",fontSize:"14px"}}><p>{this.state.status}{this.state.status == "审核失败" ? item.verifyRemark ? <Tooltip placement="top" title={item.verifyRemark}><em className="iconfont user-tip">&#xe620;</em></Tooltip> : "" : ""}</p></td>
        <td style={{width:"125px",padding:"0 20px"}}><p className="time">{time}</p></td>
        {piclist}
        {this.checkbtn}
      </tr>
    );
  }
};
export default class CertificateInfo extends React.Component{
  constructor(props){
      super(props);
      this._getData = this.getData.bind(this);      
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
      else
      {

      } 
      
    })
    .fail(function() {
      console.log("error");
    })    
  }
  componentDidMount(){
    this._getData();
  } 
  render() {
    let data = this.state.data; 
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
          /*let opts = [];
          let len = item.fileList.length;
          
          for(let i = 0; i <= len; i++){
            opts.push(item.fileList[i]);
          }
          console.log(opts);*/
          return <InfoList key={i} eq={i} item = {item}/>
        })}
        </tbody>
      </table>      
    );
  }
}