import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../../common/validateFn';
import { Modal, Button ,Input, Form,} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
import './dopay.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror',
    onOk() {
      let el = $(".ant-modal-container").eq(0);
      if(el.css("visibility") == "hidden"){
        el.css({visibility:"visible"});
      }
    }
  });
}

function success(v) {
  Modal.success({
    title: '垫付成功',
    content: v,
  });
}
function success1(v) {
  Modal.success({
    title: '发送成功',
    content: v,
    okText:"确定"
  });
}

let tipFlag = false;

class CountDown extends React.Component{
  static defaultProps = {
    time: 5
  }
  constructor(props) {
    super(props);
    this.state = {count: '60',showTime:'',thisTimer:false};
    this.changeSetate = this.changeSetate.bind(this);
  }
  changeSetate(){
    this.setState({
      thisTimer:true
    })
  }
  componentDidMount() {
    let msg = this.props.msg;
    if(msg){
      success(msg);
    }
      this.timer = setInterval(function () {
        this.setState({
          count: this.state.count - 1
        });
        if(this.state.count==0){
          clearInterval(this.timer)
          this.props.callback()
        }
      }.bind(this), 1000);

  }
  render(){
    const text = this.props.text ? this.props.text : '';
    return (
        <span>{this.state.count}{text}</span>
    );
  }
}

class Modalcontent extends React.Component{
    constructor(props){
      super(props);
      this.getcode = this.getcode.bind(this);
      this.handleSubmit = this.handleSubmit.bind(this);
      this.callback = this.callback.bind(this);
      this.state = {
        timeflag:false,
        btnText:'秒后重新获取',
        defBtn:'立即获取验证码',
        tipFlagMsg:''
      };
    }

  getcode(e){
    if(this.state.timeflag){
      return false;
    }

    $(this.refs.refBtn).data("tipFlag",true);

    this.setState({
      tipFlagMsg : ""
    });

    let self = this,el = e.target;

    $.ajax({
      url: '/member/myRepayment/getRepayCode.html',
      type: 'POST',
      dataType: 'json',
      data:{'repaymentId':this.props.uuid}
    })
    .done(function(data){
        if(data.result == false)
        {
          error(data.msg);
          self.callback();
        }
        else
        {
          el.disabled = true;
          self.setState({
            timeflag : true
          });
        }
    })
    .fail(function() {
      self.callback();
      error('网络异常，请重试！');
    });
  }
  handleSubmit(e) {
    e.preventDefault();
    if(!$(this.refs.refBtn).data("tipFlag"))
    {
      this.setState({
        tipFlagMsg : "请点击获取验证码"
      });
      return;
    }
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {
        console.log('Errors in form!!!');
        return;
      }
      console.log('Submit!!!');
      console.log(values);
      const self = this;
      values[$("body").data("tokenname")] = $("body").data("token");
      $(".ant-modal-container").eq(0).css({visibility:"hidden"});
      $.ajax({
        url: this.props.url,
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.props.close();
          Modal.success({
            title: '还款成功',
            onOk:function () {
              window.location.reload();
            }
          });
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
  callback(e){
    this.setState({
      timeflag : false,
      defBtn:'重新获取'
    });
    $(this.refs.refBtn).attr('disabled',false)

  }

  render(){
        const {getFieldProps} = this.props.form;
        const codeProps = getFieldProps('code', {
          validate: [{
            rules: [
              { required: true, whitespace: true, message: '请输入验证码' }
              ]
          }]
        });
        const uuidProps = getFieldProps("uuid",{
            initialValue:this.props.uuid
        });
        Math.formatFloat = function(f, digit) {
          var m = Math.pow(10, digit);
          return parseInt(f * m, 10) / m;
        }
        const alsoval = Math.formatFloat(this.props.capital+this.props.interest+this.props.lateInterest,2)
          var countDown =this.state.defBtn;
          if (this.state.timeflag)
          {
            countDown = <CountDown ref="countDown" callback={this.callback} text={this.state.btnText}/>
          }
        return (
            <div className="advanceform">
              <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
                <div className="advancecon">
                    <div className="also">应还金额<font>{alsoval}</font>元</div>
                    <p>（应还本金 {this.props.capital} 元，应还利息 {this.props.interest} 元，逾期罚息 {this.props.lateInterest} 元,借款管理费{ this.props.borrowManageFee} 元）</p>
                </div>
                <Input {...uuidProps} type="hidden" />
                <div className="row">
                  <div className="col-6 lab form-laber textR">短信验证码</div>
                  <div className="col-9 pl20">
                    <FormItem hasFeedback><Input {...codeProps} type="text" maxLength="8" autoComplete="off" placeholder="请输入短信验证码"  />
                    </FormItem>
                  </div>
                  <div className="col-9 pl20 has-error">
                    <button id="countdown_phone" type="button" className="countdown-btn" ref="refBtn" onClick={this.getcode}>{countDown}</button>
                    <span className="ant-form-explain" style={{display:"block"}}>{this.state.tipFlagMsg}</span>
                  </div>
                </div>
                <FormItem>
                  <Button className="submit-btn" type="primary" htmlType="submit">确定还款</Button>
                </FormItem>
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
    this.state = {
      data: null,
      visible:false,
    };
  }
  showModal() {
    this.setState({ visible: true });
  }
  handleOk() {
    window.location.href = "";
  }
  handleCancel() {
    this.setState({ visible: false });

    this.refs.clean.resetFields();
  }
  componentDidMount(){
    //初始化投资金额的数值
  }
  render() {
    return (
      <div>
      <span className="tablelink"><a onClick={this.showModal}>{this.props.tipName}</a></span>
        <Modal ref="modal"
          visible={this.state.visible}
          title={this.props.title} onCancel={this.handleCancel}
          footer={[
          ]}
        >
          <div className="modal-content">
              <Modalcontent capital={this.props.capital} ref="clean" close = {this.handleCancel} interest={this.props.interest} borrowManageFee ={this.props.borrowManageFee} lateInterest={this.props.lateInterest} uuid={this.props.uuid} url={this.props.url}/>
          </div>
        </Modal>
      </div>
    );
  }
}
