import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './qualification.less';
import { Upload, Icon, Modal } from 'antd';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}


const ImageUploadList = React.createClass({
  componentDidMount(){
    if(!this.props.showflag){
      $(".ant-upload").eq(this.props.eq).hide();
    }
  },
  render() {
    //默认图片
    let fileList = !this.props.opts ? '' : this.props.opts;
    let self = this;  
    const props = {
      action:this.props.image_server_url + "/upload/uploadifySave.html?nid=userinfo",
      name:"upload",     
      listType: 'picture-card',
      defaultFileList: fileList,
      multiple: false,
      onChange(info){
          if(info.file.status == "done"){
            if(info.file.response != "error"){
              self.props.uploadControl();
              self.props.updateArray(info.file.response);
            }
            else
            {
              error("上传失败，支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M");
              $("#j-qualification ul li").eq(self.props.eq).find(".ant-upload-list-item-done:contains('文件上传中')").last().remove();
            }
          }
          if(info.file.status == "error"){

          }
          if(info.file.status=="removed"){           
            self.props.deletePic(info.file);
          }       
      },
    };
    return (
      <div className="clearfix">
        <Upload {...props}>
          <Icon type="plus" />
          <div className="ant-upload-text">上传图片</div>
        </Upload>        
      </div>
    );
  },
});


let ImgCont = React.createClass({
  imgArray: [],
  getInitialState() {    
    return {
      data: ''
    };
  },  
  deletePic(info){
    const self = this,
    picpath = info.response ? info.response : info.delUrl;
    $.ajax({
      url: '/member/usercertification/deletePic.html',
      type: 'POST',
      dataType: 'json',
      data: {"picPath":picpath},
    })
    .done(function(){          
      self.props.formArray[self.props.eq].picpath.remove(picpath);
      setTimeout(function(){self.uploadControl()},300);
    })
    .fail(function() {
      error('网络异常，请重试！');
    });   
  },
  updateArray(url){
    this.props.formArray[this.props.eq].picpath.push(url);
  }, 
  uploadHide(){
    const tr_el = $(".list-li"),
          imgitem = ".ant-upload-list-item",
          imgselect = ".ant-upload-select";         
          tr_el.eq(this.props.eq).find(imgselect).hide();
          tr_el.eq(this.props.eq).find(".anticon").hide();
          tr_el.eq(this.props.eq).find(".ant-upload-list").addClass('none');
  },
  uploadControl(){
    const tr_el = $(".list-li"),
          imgitem = ".ant-upload-list-item",
          imgselect = ".ant-upload-select";         
    if(tr_el.eq(this.props.eq).find(imgitem).length > 4){
        tr_el.eq(this.props.eq).find(imgselect).hide();
    }
    else 
    {
      if(this.props.status != 0){
        tr_el.eq(this.props.eq).find(imgselect).show();
      }
    } 
  },  
  componentDidMount(){
    this.uploadControl();
    if(this.props.data.status == "0"){
      this.uploadHide();
    }
  },  
  render() {
    let item = this.props.data,
    image_server_url = $("body").attr("data-url"),
    imgListInfo = item.fileList ? item.fileList : [],
    opts = [],
    len = imgListInfo.length,
    showflag = true;

    if( item.status == "0" ){//status == 0审核中不可上传图片
      showflag = false;
    }

    if( item.status == "2" )
    {
      len = [];
    }
    for(let i = 0; i < len; i++){
      let opt = {
            uid:-(i+1),
            delUrl:imgListInfo[i].filePath,
            url: image_server_url + imgListInfo[i].filePath,
            thumbUrl:image_server_url + imgListInfo[i].filePath,
            qualificationType:item.qualificationType 
      };
      //this.imgArray[i] = imgListInfo[i].filePath;
      opts.push(opt);
    }
    //let imgs = this.imgArray.slice(0);
    this.props.setArray(item.qualificationType,item.name,this.props.eq,[],item.status);//初始化本组数据
    return (      
      <li className="list-li">
        <div className="title">
          {this.props.data.name}
        </div>
        <div className="upload">
          <span className="upload-explain">格式要求：支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M。</span>
          <div className="upload-list">
            <ImageUploadList  opts={opts} deletePic={this.deletePic} showflag={showflag} image_server_url = {image_server_url} eq={this.props.eq} uploadControl = {this.uploadControl} updateArray = {this.updateArray}/>
          </div>
        </div>
        <div className="imgdemo">
          <span className="upload-explain">示例</span>
          <div className="demoimg"><img src="http://www.atool.org/include/placeholder_cache/93741aa0ae53dfe5e40e7922342cf3a3.png" /></div>
        </div>
      </li>
    );
  },
});

let List = React.createClass({
  formArray:[],
  acceptName:[],
  getInitialState() {
    return {
      data: '',
    };
  },
  submit(){
    let self = this,flag = true,errorName = "",datas={"qualificationTypes":[]};
    console.log(this.formArray);
    $.each(this.formArray,function(k,v){
      if( !v.picpath.length && v.status == "2" ){
        flag = false;
        errorName = v.name;
        return false;
      }
      else if(v.picpath.length){
        datas[v.field] = v.picpath;
        datas["qualificationTypes"][k] = v.field;
      }
    });

    datas[$("body").data("tokenname")] = $("body").data("token");

    if(!flag){
      error("请上传"+ errorName + "证明");
    }
    else{
      $.ajax({
        url: '/borrow/addFiles.html',
        type: 'POST',
        dataType: 'json',
        data:datas
      })
      .done(function(data) {
        if(data.result){
          window.location.href = "/borrow/addBorrowPage.html";
        }
        else
        {
          error(data.msg);
        }  
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
    }
  
  },
  getInfo(){
    const self = this;
    $.ajax({
      url: '/member/usercertification/getCertificateInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      self.setState({
        data:data.qualificationList
      });
    })
    .fail(function() {
      error('网络异常，请重试！');
    });    
  },
  componentDidMount(){
    this.getInfo();
  },
  setArray(key,name,index,picArray,status){
    this.formArray[index]={"field":key,"name":name,"picpath":picArray,"status":status};
  },
  render() {
    let data = this.state.data;
    if(!data){
      return false;
    }
    const self = this;
    return (
      <div>
        <ul className="list-ul">
          {            
            data.map(function(k,v){              
              return <ImgCont key={v} eq={v} data = {k} setArray = {self.setArray} formArray = {self.formArray}/>
            })
          }
        </ul>
        <div className="btn"><a href="javascript:" onClick={this.submit} className="subbutton">确定提交</a></div>
      </div>
    )
  }
});

ReactDOM.render(<List />, document.getElementById("j-qualification"));