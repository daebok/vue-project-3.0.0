import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './order.less';
import CountDown from '../../component/orderCountDown/countDown';
import Favorable from './commonComponent/favorable';
import  { ValidateFn } from '../../common/validateFn';
import { Reg } from '../../common/regularFun';
import { Publiclib } from '../../common/common';
import { Button, Form, Input, Modal,InputNumber } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
import OrderDefault from './order/orderDefault';//默认订单
import OrderCopies from './order/orderCopies';//按份数订单

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

export default class InvestOrder extends React.Component{
  constructor(props){
    super(props);
    this.projectSaleStyle=$('#projectSaleStyle').val();//借款类型
  }

  render() {
    let html=this.projectSaleStyle=='1'?<OrderDefault form={this.props.form} token={this.props.token} />:<OrderCopies token={this.props.token} form={this.props.form}/>
    return (
      <div>
        {html}
      </div>
    );
  }
}
InvestOrder = createForm()(InvestOrder);
ReactDOM.render(<InvestOrder token={$('body').attr('data-token')} />, document.getElementById("j-investOrder"));