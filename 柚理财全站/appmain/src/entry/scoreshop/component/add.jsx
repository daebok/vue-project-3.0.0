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

class Addcontent extends React.Component{
    constructor(props){
      super(props);
      this.handleSubmit = this.handleSubmit.bind(this);
      this.handleCancel = this.handleCancel.bind(this);
      this.onChange = this.onChange.bind(this);
      this.state = {
        data: null,
        checked: true,
        checkedval:1,
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
        url: '/scoreshop/addReceivingInfo.html',
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          success('新增地址成功！')
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
    render(){
        const zone = areajson;
        const {getFieldProps} = this.props.form;
        const userNameProps = getFieldProps('name', {
              rules: [{required: true,message: '请填写收货人姓名' }],
            }); 
        const mobileProps = getFieldProps('mobile', {
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
          rules: [{required: true, type: 'array', message: '请选择您的所在地区' }],
        });
        const addressProps = getFieldProps('address', {
          rules: [{required: true,message: '请填写详细地址' }],
        });
        const postalCodeProps = getFieldProps('postalCode', {
          rules: [{message: '请填写邮政编码' }],
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
                        <Input {...userNameProps} type="text" className="input long-input" maxLength="20" autoComplete="off" placeholder="请输入收货人姓名" />              
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
Addcontent = createForm()(Addcontent);


export default class Add extends React.Component{
  constructor(props){
    super(props);  
    this.showModal = this.showModal.bind(this);
    this.handleCancel = this.handleCancel.bind(this); 
    this.handleOk = this.handleOk.bind(this);   
    this.state = {
      data: null,
      visibleval:false,
      other:0
    };
  }
  showModal() {
    this.setState({ visibleval: true });
    this.setState({ other: 1 });
  }  
  handleOk() {
    window.location.href = "http://"+window.location.host+"/member/vouch/overdue.html?tab=2";
  } 
  handleCancel() {
    this.setState({ visibleval: false });
    this.setState({ other: 1 });
  }
  componentDidMount(){   
  } 
  render() {
    let visibleval = this.state.other==1 ? this.state.visibleval : this.props.visibleval;
    let title = <div className="title">添加新地址</div>;
    let btn=<a onClick={this.showModal}><em className="iconfont">&#xe645;</em>添加新地址</a>;
    return (
      <div>
        <div className="btns">{btn}</div>
        <Modal ref="modal" className="orderPayModal"
          visible={visibleval}
          title={title} onCancel={this.handleCancel}
          width={800}
          footer={[
          ]}
        >
          <div className="modal-content">
              <Addcontent close = {this.handleCancel} />
          </div>
        </Modal>
      </div>
    );
  }
}