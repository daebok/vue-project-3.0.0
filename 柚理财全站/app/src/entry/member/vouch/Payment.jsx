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

class Modalcontent extends React.Component{
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
      $.ajax({
        url: '/member/vouch/doLineRepay.html?uuid='+this.props.id,
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.props.ok();
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
        const alsoval = this.props.capital+this.props.interest+this.props.lateInterest;      
        return (
            <div className="advanceform">
              <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                <div style={{"padding":"25px 0","overflow":"hidden","textAlign":"center"}}><i className="iconfont">&#xe62e;</i><p style={{'paddingTop':'0',"fontSize":"18px",'lineHeight':"42px"}}>您确定线下已收到借款人的回款？</p></div>
                <input type="hidden" name="uuid" value={this.props.id} />
                <div className="btn">
                  <FormItem>
                    <Button type="primary" htmlType="submit">已经收到</Button><Button key="back" onClick={this.props.close} type="ghost" size="large" >暂未收到</Button>
                  </FormItem>
                </div>
              </Form>
            </div>
        )
    }
}
Modalcontent = createForm()(Modalcontent);


export default class Dopay extends React.Component{
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
    window.location.reload();
  } 
  handleCancel() {
    this.setState({ visible: false });
  }
  componentDidMount(){ 
    //初始化投资金额的数值   
  } 
  render() {
    let title = <div className="title">确认回款</div>;
    let url ="/member/vouch/overdueDetail.html?uuid="+this.props.id;
    return (
      <div>
        <div className="btns"><a onClick={this.showModal}>确认回款</a> | <a href={url}>详情</a></div>
        <Modal ref="modal" className="orderPayModal"
          visible={this.state.visible}
          title={title} onCancel={this.handleCancel}
          footer={[
          ]}
        >
          <div className="modal-content">
              <Modalcontent id={this.props.id} close = {this.handleCancel} ok = {this.handleOk} />

          </div>
        </Modal>
      </div>
    );
  }
}