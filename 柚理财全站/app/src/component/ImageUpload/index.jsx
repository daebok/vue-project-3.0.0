import React from 'react';
import ReactDOM from 'react-dom';
import { Upload, Icon, Modal } from 'antd';
import './ImageUpload.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

const ImageUploadList = React.createClass({
  getInitialState() {
    return {
      priviewVisible: false,
      priviewImage: '',
      _addFile: this.props.addFile,
      _deletePic: this.props.deletePic,
    };
  },
  handleCancel() {
    this.setState({
      priviewVisible: false,
    });
  },
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
              self.props.updateArray();
              self.props.imgArray.push(info.file.response);
            }
            else
            {
              error("上传失败，支持jpg、jpeg、bmp、gif、png格式图片，大小不超过1M");
            }

          }
          if(info.file.status=="removed"){           
            self.state._deletePic(info.file);
          }   		
      },
    };
    return (
      <div className="clearfix">
        <Upload {...props}>
          <Icon type="plus" />
          <div className="ant-upload-text">上传文件</div>
        </Upload>        
        <Modal visible={this.state.priviewVisible} footer={null} onCancel={this.handleCancel}>
          <img alt="example" src={this.state.priviewImage} />
        </Modal>
      </div>
    );
  },
});

export default ImageUploadList;
