import React from 'react';
import ReactDOM from 'react-dom';
import { Icon, Modal ,Tooltip} from 'antd';
import '../../../themes/plugins/uploadify/uploadify.css';
import '../../../themes/js/fancybox/jquery.fancybox.css';
import '../../style/upload.less';
import {Publiclib} from '../../common/common';


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

const Imgupload = React.createClass({
  getInitialState() {
    return {
      name: "",       //材料名称
      status: this.props.item.statusStr,       //状态
      verifyRemark: "",   //审核备注
      verifyTime: "",     //审核时间
      image_server_url: "", //图片服务器地址
      qualificationList: "",  //上传文件列表
      checkbtn:"",
      isNew:false,
      checkOk:false
    };
  },
  image_server_url : $("#header").data("url"),
  componentWillMount(){
    this.props.setArray(this.props.eq);

    let imgListInfo = this.props.item.fileList ? this.props.item.fileList : []; 
    let self = this;
    imgListInfo.map(function(k,v){
      self.props.imgArray[self.props.eq].pic.push(k.filePath);                                  
      return "";
    })
  },
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
              let dom = $(this).parents("li");
              self.deletePic(dom.data("url"),dom.data("id"),$(this).parents("li"));
            });

            $('.filelist').eq(self.props.eq).find("[data-imggroup]").fancybox();

            self.uploadControl();

            if( this.props.item.status == "0" ){
              $("#file_upload"+ this.props.eq).css("visibility","hidden");
            }
  },
  deletePic(picpath,id,el){
    const self = this; 
    let data = {"picPath":picpath,"uploadId":id ? id : picpath};
    $.ajax({
      url: '/member/usercertification/deletePic.html',
      type: 'POST',
      dataType: 'json',
      data:data
    })
    .done(function(){
      el.remove();
      $("#file_upload"+ self.props.eq).uploadify('settings','uploadLimit', ++self.uploadLimit);
      self.props.imgArray[self.props.eq].pic.remove(picpath);
      setTimeout(function(){self.uploadControl()},300);
    })
    .fail(function() {
      error('网络异常，请重试！');
    });
  },
  readStatus(val){    
    if(val == "99"){
      return status = "未上传";
    } else {
      let k = parseInt(val);      
      return ["审核中","审核成功","审核失败"][k];
    }
  },
  addFile(e){
    if(!this.props.imgArray[this.props.eq].pic.length){
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
      data: {"qualificationType":$(e.target).attr("data-qualificationType"),"picPath":self.props.imgArray[self.props.eq].pic}      
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
          $(".filelist").eq(self.props.eq).find(".delImg").remove();
        } else {
          error("上传失败，请重试！");
        }
      
    })
    .fail(function() {
      error('网络异常，请重试！');
    }); 
  },
  setIsNew(){
    this.setState({
      isNew : true
    });
  },     
  updateArray(url){
    this.setIsNew();
    this.props.imgArray[this.props.eq].pic.push(url);
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
    $("#file_upload"+ this.props.eq).css("visibility","hidden");
    $(".imgtr").eq(this.props.eq).find(".checkbtn").hide();
  },       
  render() {
    let item = this.props.item;
    if(!item) return false;

    let image_server_url = $("#header").data("url");
    let time = item.verifyTime ? Publiclib.formatDate(item.verifyTime, 2) : "--";
    let imgListInfo = item.fileList ? item.fileList : []; 
    let self = this;
    let delhtml = "";
    if(item.status == "2"){
      delhtml = <i className='iconfont delImg' title='点击删除图片'>&#xe67e;</i>;
    }
    //let opts = [];
    //let len = imgListInfo.length;
    //for(let i = 0; i < len; i++){
      // let opt = {
      //       //uid: imgListInfo[i].uuid,
      //       uid:-(i+1),
      //       id:imgListInfo[i].id,
      //       // name: imgListInfo[i].name,
      //       // status: imgListInfo[i].status,
      //       delUrl:imgListInfo[i].filePath,
      //       url: image_server_url + imgListInfo[i].filePath,
      //       thumbUrl:image_server_url + imgListInfo[i].filePath,
      //       qualificationType:item.qualificationType 
      // };
      //this.imgArray[i] = imgListInfo[i].filePath;
      //opts.push(opt);
    //}

    const checkbtn = <td style={{width:"60px"}}><span className="checkbtn" data-qualificationtype={item.qualificationType} onClick={self.addFile}>提交</span></td>;

    if( item.status != "0" ){//status == 0审核中不可上传图片
      this.checkbtn = checkbtn;
    }
    else
    {
      this.checkbtn = <td></td>;
    }

    return (
      <tr className="imgtr">
        <td style={{width:"116px",fontSize:"16px"}}><h3 style={{fontSize:"16px"}}>{item.name}</h3></td>
        <td style={{width:"110px",fontSize:"14px"}}><p>{this.state.status}{this.state.status == "审核失败" ? item.verifyRemark ? <Tooltip placement="top" title={item.verifyRemark}><em className="iconfont user-tip">&#xe620;</em></Tooltip> : "" : ""}</p></td>
        <td style={{width:"125px",padding:"0 20px"}}><p className="time">{time}</p></td>
        <td className="td-listUpfile">
          <div className="imgupload">
            <ul className="filelist">
            {   
                imgListInfo.map(function(k,v){                                  
                  return <li key={v} data-url={k.filePath} data-id={k.id ? k.id : k.filePath}><img src={self.image_server_url+k.filePath} /><span className='operation'><i data-imggroup data-fancybox-group={"group"+self.props.eq} href={self.image_server_url+k.filePath} className='iconfont showimg' >&#xe67f;</i>{delhtml}</span></li>
                })
            }
            </ul>
            <div id={"file_upload" + this.props.eq}></div>
          </div>
        </td>
        {this.checkbtn}
      </tr>        
      );
  }
});

export default Imgupload;