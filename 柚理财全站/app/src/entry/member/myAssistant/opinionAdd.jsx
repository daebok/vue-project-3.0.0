import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Button, Form, Input,Modal,Upload,Icon} from 'antd';
import './opinionAdd.less';
import  { ValidateFn } from '../../../common/validateFn';
const createForm = Form.create;
const FormItem = Form.Item;

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
  render() {
    //默认图片
    let fileList = !this.props.opts ? '' : this.props.opts;
    let self = this;  
    const props = {
      action:this.props.image_server_url + "/upload/uploadifySave.html?nid=userinfo",
      name:"upload",     
      listType: 'picture-card',
      defaultFileList: fileList,
      accept:"image/gif,image/jpg,image/bmp,image/jpeg,image/png",
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
            $("#j-upimg").eq(self.props.eq).find(".ant-upload-list-item-done").last().remove();
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
          <div className="ant-upload-text">选择文件</div>
        </Upload>        
      </div>
    );
  },
});


let ImgCont = React.createClass({
  imgArray: [],
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
      self.props.imgArray.remove(picpath);
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
    const tr_el = $("#j-upimg"),
          imgitem = ".ant-upload-list-item",
          imgselect = ".ant-upload-select";
    if(tr_el.eq(this.props.eq).find(imgitem).length >= this.props.len ){
        tr_el.eq(this.props.eq).find(imgselect).hide();
    }
    else 
    {
        tr_el.eq(this.props.eq).find(imgselect).show();
    } 
  },  
  componentDidMount(){
    this.uploadControl();
  },  
  render() {
    let item = this.props.data,
    image_server_url = $("body").attr("data-url"),
    imgListInfo = item ? item : [],
    opts = [],
    len = imgListInfo.length;

    for(let i = 0; i < len; i++){
      let opt = {
            uid:-(i+1),
            delUrl:imgListInfo[i],
            url: image_server_url + imgListInfo[i],
            thumbUrl:image_server_url + imgListInfo[i]
      };
      this.props.imgArray[i] = imgListInfo[i];
      opts.push(opt);
    }
    return (
            <ImageUploadList  opts={opts} deletePic={this.deletePic} uploadControl = {this.uploadControl} updateArray={this.updateArray} image_server_url = {image_server_url} eq={this.props.eq} />
          );
    },
});

let Msgform = React.createClass({
  picPath:[],
  handleSubmit(e) {
    var that = this;
    e.preventDefault();
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      values["attachmentPaths"] = this.picPath;
      $.ajax({
        url: '/member/myAssistant/opinionAddSave.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success(data.msg,data.url)
        }
        else
        {
          error(data.msg);
        } 
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
      
    });
  },
  getInitialState() {
    return {
      imgsrc:'/validimg.html'
    };
  },
  changeimg(){
    this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
  },  
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const titleProps = getFieldProps('title', {
      validate: [
        {
          rules: [
            { required: true, message: '请输入标题' }
          ],trigger: ['onBlur']
        }
      ]
    }); 
    const remarkProps = getFieldProps('remark', {
      validate: [
        {
          rules: [
            { required: true, message: '请输入描述' }
          ],trigger: ['onBlur']
        }
      ]
    });
    //验证码校验
    const validCodeProps = getFieldProps('imgValidCode', {
      validate: [
        {
          rules: [
            { required: true, whitespace: true, message: '请输入验证码' }
          ],
         trigger: ['onBlur', 'onChange'],
        },
        {
          rules:[
            {validator:ValidateFn.checkValidCode.bind(this)},
          ], trigger: ['onBlur'],
        }
      ]
    });     
    return (      
      <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
        <div className="row" style={{width:800}}>
          <div className="col-3 lab form-laber textR">意见标题</div>
          <div className="col-9 pl20">
            <FormItem hasFeedback >
              <Input {...titleProps}placeholder="标题" maxLength="30"/>
            </FormItem>
          </div>
        </div>  
        <div className="row" style={{width:800}}>
          <div className="col-3 lab form-laber textR">意见描述</div>
          <div className="col-16 pl20">
            <FormItem hasFeedback >
              <Input {...remarkProps} placeholder="描述" style={{width:"515px",height:"100px"}} type="textarea" maxLength="200"/>
            </FormItem>
          </div>
        </div>
        <div className="row" style={{width:800}}>
          <div className="col-3 lab form-laber textR">上传图片</div>
          <div className="col-18 pl20" style={{paddingTop:"13px",height:"150px"}}>
            <p>格式要求：支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M,最多五张。</p>
            <div id="j-upimg" style={{padding:"20px 0 0 0"}}><ImgCont eq={0} data="" imgArray = {this.picPath} len ={5} /></div>
          </div>
        </div>
        <div className="row" style={{width:800}}>
          <div className="col-3 lab form-laber textR">验证码</div>
          <div className="col-4 pl20">
          <FormItem
            hasFeedback
            help={isFieldValidating('validCode') ? '校验中...' : (getFieldError('validCode') || []).join(', ')}
          ><Input maxLength="4" {...validCodeProps}  placeholder="验证码" size="large" type="text" autoComplete="off" /></FormItem></div>
          <div className="col-4 col-offset-2"><img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" /></div>
        </div>
        <div className="row" style={{width:800}}>
          <div className="col-3 lab form-laber textR"> </div>
          <div className="col-9 pl20">
            <FormItem>
              <Button  type="primary" className="subBtn" htmlType="submit">提交</Button>
            </FormItem>
          </div>
        </div>                  

      </Form>
    );
  },
});

Msgform = createForm()(Msgform);

ReactDOM.render(<Msgform />,  document.getElementById("j-opinionAdd"));
