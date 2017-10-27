import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import { Modal, Button ,Input, Form,Icon} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: v,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk(){
      location.reload() 
    }
  });
}

class SettingContent extends React.Component{
    constructor(props){
      super(props);
      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleCancel = this.handleCancel.bind(this);
    }
  handleSubmit(e) { 
    e.preventDefault(); 
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');    
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      let self = this;
      $(".ant-modal-container").eq(0).css({
        visibility: 'hidden'
      });
      $.ajax({
        url: '/scoreshop/deleteReceivingInfo.html?id='+this.props.uuid,
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success(data.msg);
          self.props.close();        
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
  }
  handleCancel() {
    this.setState({ visible: false });
  }
    render(){
        const {getFieldProps} = this.props.form; 
        const isDefultProps = getFieldProps("isDefult",{
          initialValue:"1"
        });      
        return (
            <div className="settingform">
              <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                <div style={{"padding":"25px 0","overflow":"hidden","textAlign":"center"}}><i className="iconfont">&#xe62e;</i><p style={{'paddingTop':'0',"fontSize":"18px",'lineHeight':"42px"}}>您确定要把该地址删除？</p></div>
                <input type="hidden" {...isDefultProps}/>
                <div className="btn">
                  <FormItem>
                    <Button type="primary" htmlType="submit">确定</Button><Button key="back" onClick={this.props.close} type="ghost" size="large" >取消</Button>
                  </FormItem>
                </div>
              </Form>
            </div>
        )
    }
}
SettingContent = createForm()(SettingContent);


export default class DeleteSetting extends React.Component{
  constructor(props){
    super(props);  
    this.showModal = this.showModal.bind(this);
    this.handleCancel = this.handleCancel.bind(this); 
    this.handleOk = this.handleOk.bind(this);   
    this.state = {
      data: null,
      visible:false,
    };
  }
  showModal() {
    this.setState({ visible: true });
  }  
  handleOk() {
    window.location.href = "http://"+window.location.host+"/member/myScore/myReceivingPage.html";
  } 
  handleCancel() {
    this.setState({ visible: false });
  }
  componentDidMount(){   
  } 
  render() {
    let title = <div className="title">温馨提示</div>;
    let btn=<a onClick={this.showModal}>删除</a>;
    return (
      <div>
        <div className="defaultSetting">{btn}</div>
        <Modal ref="modal" className="orderPayModal"
          visible={this.state.visible}
          title={title} onCancel={this.handleCancel}
          footer={[
          ]}
        >
          <div className="modal-content">
              <SettingContent uuid={this.props.uuid} close = {this.handleCancel}/>
          </div>
        </Modal>
      </div>
    );
  }
}