import React from 'react';
import ReactDOM from 'react-dom';
import  { ValidateFn } from '../../../common/validateFn';
import { Modal, Button ,Input, Form, Radio} from 'antd';
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
  render() {
    let data = this.state.data;
    if(!data){return false;}
    return (
          <div>
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
          </div>
    );
  }
}
