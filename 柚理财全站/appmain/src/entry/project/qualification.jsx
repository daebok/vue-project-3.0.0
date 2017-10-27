import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './qualification.less';
import { Icon, Modal } from 'antd';
import Imgupload from '../../component/ImageUpload/uploadQualification';

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

function showerror(msg,url,btnname) {
  if(!btnname){
    btnname = "点击跳转";
  }
  Modal.error({
    title: '温馨提示',
    content: msg,
    okText:btnname,
    iconType:'question-circle',
    align:{top:'200px'},
    onOk() {
        window.location.href = url;
    },
    onCancel() {},
  });
}

let flagObj = $("body").data("flag");
if(!flagObj.result){
  showerror(flagObj.msg,flagObj.url,flagObj.buttonName);
  //showerror(flagObj.msg,flagObj.url);
}

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
      if( (!v.picpath.length && v.status == "2") || (!v.picpath.length && v.status == "99") ){
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
              return <Imgupload key={v} eq={v} data = {k} setArray = {self.setArray} formArray = {self.formArray} len ={5} />
            })
          }
        </ul>
        <div className="btn"><a href="javascript:" onClick={this.submit} className="subbutton">确定提交</a></div>
      </div>
    )
  }
});

ReactDOM.render(<List />, document.getElementById("j-qualification"));