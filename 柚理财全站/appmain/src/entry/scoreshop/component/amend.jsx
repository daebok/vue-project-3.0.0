import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import { Select, Radio, Button, Form, Input, Checkbox, Modal, Cascader} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror',
    onOk(){     
    }
  });
}

function success(v) {
  Modal.success({
    title: v,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk(){
      location.reload(); 
    }
  });
}

class Amendcontent extends React.Component{
    constructor(props){
      super(props);
      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleCancel = this.handleCancel.bind(this);
      this.onChange = this.onChange.bind(this);
      this.state = {
        data: null,
        checked: false,
        checkedval:0,
      };
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
      $.ajax({
        url: '/scoreshop/editReceivingInfo.html?id='+this.props.uuid,
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success('修改地址成功！')
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
  onChange(e) { 
    this.setState({
      checked:e.target.checked,
    });
    this.state.checkedval = e.target.checked == true ? "1" : "0";
  }
    componentDidMount(){
      console.log(this.props.uuid)
    let self = this;  
    $.ajax({
      url: '/scoreshop/getReceivingInfoById.html?id='+this.props.uuid,
      type: 'POST',
      dataType: 'json',     
    })
    .done(function(data) {
      if( data ){
        self.setState({data: data});
        self.setdefault();
      }
      else
      {
        error(data.msg,data.url);
      }      
    })
    .fail(function() {
      error('网络异常，请重试！');
    })
  }
  setdefault(){
    let data = this.state.data;
    this.state.checkedval=data.receivingInfo.isDefult;
    this.state.checked=data.receivingInfo.isDefult==1 ? true : false;
    if(data.receivingInfo.province){
      this.props.form.setFieldsValue({
        zone:[data.receivingInfo.province,data.receivingInfo.city, data.receivingInfo.area],
      })
    }
  }
    render(){
        let data = this.state.data;
        if(!data){
          return false;
        }
        const zone = areajson;
        const {getFieldProps} = this.props.form;
        const userNameProps = getFieldProps('name', {
              initialValue:data.receivingInfo.name,
            }); 
        const mobileProps = getFieldProps('mobile', {
          initialValue:data.receivingInfo.mobile,
          validate: [
          {
            rules:[
              { required: true, message: '请填写手机号码' }
            ],
             trigger: ['onBlur'],
          }
          ]
        });
        const zoneProps = getFieldProps('zone', {
          rules: [{ type: 'array', message: '请选择您的所在地区' }],
        });
        const addressProps = getFieldProps('address', {
          initialValue:data.receivingInfo.address,
          rules: [{message: '请填写详细地址' }],
        });
        const postalCodeProps = getFieldProps('postalCode', {
          initialValue:data.receivingInfo.postalCode
        });
        const agreeProps = getFieldProps("isDefult",{
          initialValue:this.state.checkedval
        });       
        return (
            <div className="advanceform">
              <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                <ul className="form-list">
                  <li>
                    <label className="label">收货人<em className="star">*</em></label>
                    <div className="form-item">
                      <FormItem hasFeedback>
                        <Input {...userNameProps} type="text" className="input long-input" maxLength="20" autoComplete="off" placeholder="6-20位字符，由字母或字母加数字组成" />              
                      </FormItem>
                    </div>
                  </li>
                  <li>
                    <label className="label">联系方式<em className="star">*</em></label>
                    <div className="form-item">
                      <FormItem hasFeedback>
                        <Input {...mobileProps} type="text" className="input long-input" maxLength="20" autoComplete="off" placeholder="请输入手机号码" />              
                      </FormItem>
                    </div>
                  </li>
                   <li>
                      <label className="label">所在地区<em className="star">*</em></label>
                      <div className="form-item">
                        <FormItem>
                          <Cascader {...zoneProps} options={zone} style={{width:'310px'}}/>
                        </FormItem>
                      </div>                      
                    </li>
                    <li>
                      <label className="label">详细地址<em className="star">*</em></label>
                      <div className="form-item">
                        <FormItem hasFeedback>
                            <Input {...addressProps} type="text" className="input long-input" maxLength="50" autoComplete="off" placeholder="具体地址，不超过50个字符" style={{width:'480px'}} />         
                        </FormItem>
                      </div>                      
                    </li>
                    <li>
                      <label className="label">邮政编码</label>
                      <div className="form-item">
                        <FormItem>
                            <Input hasFeedback {...postalCodeProps} type="text" className="input long-input" />         
                        </FormItem>
                      </div>                      
                    </li>
                </ul>
                <div className="agreement">
                  <FormItem hasFeedback {...agreeProps} style={{marginBottom:"0"}}><span className="agreementbox"><Checkbox checked={this.state.checked} onChange={this.onChange} ></Checkbox>设置为默认收货地址</span></FormItem>                    
                </div>
                <div className="btn">
                  <FormItem>
                    <Button type="primary" htmlType="submit">保存</Button><Button key="back" onClick={this.props.close} type="ghost" size="large" >取消</Button>
                  </FormItem>
                </div>
              </Form>
            </div>
        )
    }
}
Amendcontent = createForm()(Amendcontent);


export default class Amend extends React.Component{
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
    window.location.href = "http://"+window.location.host+"/member/vouch/overdue.html?tab=2";
  } 
  handleCancel() {
    this.setState({ visible: false });
  }
  componentDidMount(){   
  } 
  render() {
    let title = <div className="title">修改地址</div>;
    let btn=<a onClick={this.showModal}>修改&gt;</a>;
    return (
      <div>
        <div className="btns">{btn}</div>
        <Modal ref="modal" className="orderPayModal"
          visible={this.state.visible}
          title={title} onCancel={this.handleCancel}
          width={800}
          footer={[
          ]}
        >
          <div className="modal-content">
              <Amendcontent uuid={this.props.uuid} close = {this.handleCancel}/>
          </div>
        </Modal>
      </div>
    );
  }
}