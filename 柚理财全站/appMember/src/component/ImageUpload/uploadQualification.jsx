import React from 'react';
import ReactDOM from 'react-dom';
import { Icon, Modal } from 'antd';
import '../../../themes/plugins/uploadify/uploadify.css';
import '../../../themes/js/fancybox/jquery.fancybox.css';
import '../../style/upload.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

const Imgupload = React.createClass({
  image_server_url : $("#header").data("url"),
  componentDidMount(){
          let self = this;          
          let maxUploadLimit = this.props.len;
          this.uploadLimit = maxUploadLimit - $(".filelist").eq(this.props.eq).find("li").size();
         $("#file_upload" + this.props.eq).uploadify({
                        fileObjName : 'upload',
                        buttonClass:'uploadBtn',
                        buttonText:'',
                        fileTypeDesc:'Image Files',
                        fileTypeExts:'*.gif; *.jpg; *.png',
                        fileSizeLimit:'1024',
                        uploadLimit:self.uploadLimit,
                        width:72,
                        height:72,
                        swf:'/themes/plugins/uploadify/uploadify.swf',
                        uploader:self.image_server_url + "/upload/uploadifySave.html?nid=userinfo",
                        onSelectError: function(file,errorCode,errorMsg){
                          var msg;
                                  if(errorCode==-110){
                          msg = '上传的单张图片不能超过1M，请重新上传';
                                  }
                                  else if(errorCode==-100){
                          msg = '上传的图片最多不能超过'+ maxUploadLimit +'张';
                                  }
                                  else if(errorCode==-130){
                          msg = '选择的文件类型跟设置的不匹配';
                                  }
                          error(msg);                              
                          return false;
                        },
                        'overrideEvents' : [ 'onDialogClose', 'onUploadSuccess', 'onUploadError', 'onSelectError','onSelectOnce'],
                        onUploadSuccess:function(file,data, response) {
                          if(data!=null){
                              if(data=="error"){
                                  error("图片上传失败,支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M");
                                }else{
                                  $('.filelist').eq(self.props.eq).append("<li data-imgsrc='"+data+"'><a href='"+self.image_server_url+data+"' class='fileshow'><img src='"+self.image_server_url+data+"' width='110px' height='110px' /></a><span class='operation'><i data-imggroup data-fancybox-group='group"+ self.props.eq +"' href='"+self.image_server_url+data+"' class='iconfont showimg' >&#xe67f;</i><i class='iconfont delImg' title='点击删除图片'>&#xe67e;</i><span></li>");
                                    self.updateArray(data);
                                    self.uploadControl();                                    
                                }
                           }
                        }                        
            });

            $(".imgupload").eq(this.props.eq).on("click",".filelist li .delImg",function(event) {
              self.deletePic($(this).parents("li").data("imgsrc"),$(this).parents("[data-imgsrc]"));
            });

            $('.filelist').eq(self.props.eq).find("[data-imggroup]").fancybox();
            self.uploadControl();
            if(this.props.data.status == "0"){
              this.uploadHide();
            }            
  },
  deletePic(info,el){
    const self = this;
    $.ajax({
      url: '/member/usercertification/deletePic.html',
      type: 'POST',
      dataType: 'json',
      data: {"picPath":info},
    })
    .done(function(){
      el.remove();
      $("#file_upload"+ self.props.eq).uploadify('settings','uploadLimit', ++self.uploadLimit);
      self.props.formArray[self.props.eq].picpath.remove(info);
      setTimeout(function(){self.uploadControl()},300);
    })
    .fail(function() {
      error('网络异常，请重试！');
    });   
  },
  updateArray(url){
    this.props.formArray[this.props.eq].picpath.push(url);
  },
  uploadControl(){
    const tr_el = $(".filelist").eq(this.props.eq),
          upBtn = $("#file_upload"+ this.props.eq);
    if(tr_el.find("li").length >= this.props.len ){
        upBtn.css("visibility","hidden");
    }
    else 
    {
        upBtn.css("visibility","visible");
    } 
  },
  uploadHide(){
    const upBtn = $("#file_upload"+ this.props.eq);         
          upBtn.css("visibility","hidden");
  },       
  render() {
    let self = this,
    data = this.props.data ? this.props.data : [];
    this.props.setArray(data.qualificationType,data.name,this.props.eq,[],data.status);//初始化本组数据
    let imgListInfo = data.fileList ? data.fileList : [];
    return (
      <li className="list-li">
        <div className="title">
          {this.props.data.name}
        </div>
        <div className="upload">
          <span className="upload-explain">格式要求：支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M。</span>
          <div className="upload-list">
            <div className="imgupload">
              <ul className="filelist">
              {   
                  imgListInfo.map(function(k,v){                                                   
                    return <li key={v} ><img src={self.image_server_url+k.filePath} /><span className='operation'><i data-imggroup data-fancybox-group={"group"+self.props.eq} href={self.image_server_url+k.filePath} className='iconfont showimg' >&#xe67f;</i></span></li>
                  })
              }
              </ul>
              <div id={"file_upload" + this.props.eq}></div>
            </div>
          </div>
        </div>
        <div className="imgdemo">
          <span className="upload-explain">示例</span>
          <div className="demoimg"><img src="http://www.atool.org/include/placeholder_cache/93741aa0ae53dfe5e40e7922342cf3a3.png" /></div>
        </div>
      </li>        
      );
  }
});

export default Imgupload;