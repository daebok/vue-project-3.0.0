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
                                  $('.filelist').eq(self.props.eq).append("<li data-url='"+data+"'><a href='"+self.image_server_url+data+"' class='fileshow'><img src='"+self.image_server_url+data+"' width='110px' height='110px' /></a><span class='operation'><i data-imggroup data-fancybox-group='group"+ self.props.eq +"' href='"+self.image_server_url+data+"' class='iconfont showimg' >&#xe67f;</i><i class='iconfont delImg' title='点击删除图片'>&#xe67e;</i><span></li>");
                                    self.updateArray(data);
                                    self.uploadControl();                                    
                                }
                           }
                        }                        
            });

            $(".imgupload").eq(this.props.eq).on("click",".filelist li .delImg",function(event) {
              self.deletePic($(this).parents("li").data("url"),$(this).parents("li"));
            });

            $('.filelist').eq(self.props.eq).find("[data-imggroup]").fancybox();
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
      self.props.imgArray.remove(info);
      setTimeout(function(){self.uploadControl()},300);
    })
    .fail(function() {
      error('网络异常，请重试！');
    });   
  },
  updateArray(url){
    this.props.imgArray.push(url);
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
  render() {
    let self = this,
    data = this.props.data ? this.props.data : [];
    return (
        <div className="imgupload">
          <ul className="filelist">
          {   
              data.map(function(k,v){
                self.props.imgArray[v] = k;                                    
                return <li key={v} ><img src={self.image_server_url+k} /><span className='operation'><i data-imggroup data-fancybox-group={"group"+self.props.eq} href={self.image_server_url+k} className='iconfont showimg' >&#xe67f;</i></span></li>
              })
          }
          </ul>
          <div id={"file_upload" + this.props.eq}></div>
        </div>
      );
  }
});

export default Imgupload;