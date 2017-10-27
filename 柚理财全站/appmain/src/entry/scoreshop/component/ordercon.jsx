import React from 'react';
import ReactDOM from 'react-dom';
import  { ValidateFn } from '../../../common/validateFn';
import { Modal, Button ,Input, Form, Radio } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const RadioGroup = Radio.Group;

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

export default class Ordercon extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this._handleSubmit = this.handleSubmit.bind(this);
    this.state = {
      data: null,
    };
  }
  componentDidMount(){
    this._getData();
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/index/articleList.html',
      type: 'POST',
      dataType: 'json',
      data: {"name": "scrollPic"}
    })
    .done(function(data) {
      self.setState({data: data});
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
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
      const self = this;
      $.ajax({
        url: "/hhh.html",
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.props.close();
          Modal.success({
            title: '提交成功',
            onOk:function () {
              
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
  render() {
    const { getFieldProps, getFieldError, isFieldValidating } = this.props.form;
    const remarkProps = getFieldProps('', {
      validate: [
        {
          rules: [
            { required: true, message: '请输入兑换留言' }
          ],trigger: ['onBlur']
        }
      ]
    });
    let data = this.state.data;
    if(!data){return false;}
    return (
          <div>
          <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
            <div className="ShippingAddress">
              <div className="title">选择收货地址</div>
                <ul className="clearfix">
                    {
                    data.list.map(function(k,v){
                      if(v==0){
                        return <li key={v} className="active"><Radio value={v}></Radio><span className="Daddress">钱小姐（收）135****1234    浙江省杭州市拱墅区祥符街道丰潭路508号天行国际7幢13楼</span><em>默认</em><span>修改&gt;</span></li>
                      }
                      else{
                        return <li key={v}><Radio value={v}></Radio><span className="Daddress">钱小姐（收）135****1234    浙江省杭州市拱墅区祥符街道丰潭路508号天行国际7幢13楼</span><em>设为默认&gt;</em><span>修改&gt;</span></li>
                      }
                    })
                  }
                  <li><span className="add">添加新地址</span></li>
                </ul>
            </div>
            <div className="confirm">
              <div className="title">确认兑换商品</div>
              <div className="pdetail clearfix">
                <dl className="pname">
                  <dt>商品名称</dt>
                  <dd>Apple iPhone 7 128G手机</dd>
                </dl>
                <dl className="pnum">
                  <dt>数量</dt>
                  <dd>1</dd>
                </dl>
                <dl className="pscore">
                  <dt>消耗积分</dt>
                  <dd>100000</dd>
                </dl>
                <dl className="pmark">
                  <dt>备注</dt>
                  <dd>3-7个工作日内进行审核发货</dd>
                </dl>
              </div>
              <div className="clearfix confirmCon">
                <div className="left">
                  <FormItem hasFeedback >
                    <Input {...remarkProps} placeholder="兑换留言" style={{width:"732px",height:"100px"}} type="textarea" maxLength="200"/>
                  </FormItem>
                </div>
                <div className="right">
                  <div className="top clearfix">
                    共消耗积分<em>10600</em>
                  </div>
                  <div className="down">
                    <FormItem>
                      <Button  type="primary" className="subBtn" htmlType="submit">提交</Button>
                    </FormItem>
                  </div>
                </div>
              </div>
            </div>
          </Form>
          </div>
    );
  }
}
