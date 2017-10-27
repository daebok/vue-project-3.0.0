import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './goodsorder.less';
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

class Ordercon extends React.Component{
  constructor(props){
    super(props);
    this._getDatas = this.getDatas.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.state = {
      data: null,
      visible: false,
      contentval:"",
    };
  }
  showModal = () => {
    this.setState({
      visible: true,
    });
  }
  handleOk = () => {
     location.href = "/scoreshop/goodsList.html";  
  }
  handleCancel = () => {
    this.setState({
      visible: false,
    });
  }
  componentDidMount(){
    this._getDatas();
  }
  getDatas(){
    let self = this;
    $.ajax({
      url: '/scoreshop/getGoodsInfo.html',
      type: 'POST',
      dataType: 'json',
      data: {"id": $(".goodsId").val()}
    })
    .done(function(data) {
      self.setState({datainformation: data});
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
      values.receiveRemark = $(".marks").val();
      let self = this;
      $.ajax({
        url: "/scoreshop/exchangeGoods.html",
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        if(data.result){
          self.setState({
            visible: true,
            contentval:data.msg
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
    let datainformation = this.state.datainformation;
    if(!datainformation){return false;}
    let nums = $(".num").val();
    let score = datainformation.score;
    let goodsName = datainformation.goodsName;
    let isVirtual = datainformation.isVirtual;
    let goodsId = $(".goodsId").val()
    let allscore = nums*score;
    let allscoreCon = allscore.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
    let serverUrl = $(".serverUrl").val();
    let serverUrlval = serverUrl+datainformation.picSmall;
    const that = this;
    const { getFieldProps, getFieldError,setFieldsValue } = this.props.form;
    const numProps = getFieldProps('num', {
          initialValue:nums
        });
    const goodsIdProps = getFieldProps('goodsId', {
          initialValue:goodsId
        });
    const scoreProps = getFieldProps('score', {
          initialValue:allscore
        });
    const goodsNameProps = getFieldProps('goodsName', {
          initialValue:goodsName
        });
    return (
          <div>
          <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit}>
            <div className="confirm">
              <div className="title">确认兑换商品</div>
              <div className="pdetail clearfix">
                <dl className="pname">
                  <dt>商品名称</dt>
                  <dd><span className="imgStyle"><img src={serverUrlval} /></span>{datainformation.goodsName}</dd>
                </dl>
                <dl className="pnum">
                  <dt>数量</dt>
                  <dd className="nums">{nums}</dd>
                </dl>
                <dl className="pscore">
                  <dt>消耗积分</dt>
                  <dd>{allscoreCon}</dd>
                </dl>
                <dl className="pmark">
                  <dt>备注</dt>
                  <dd>{datainformation.remark}</dd>
                </dl>
              </div>
              <div className="clearfix confirmCon">
                <div className="left">
                  <FormItem hasFeedback >
                    <Input className="marks" placeholder="兑换留言" style={{width:"732px",height:"100px"}} type="textarea" maxLength="200"/>
                  </FormItem>
                </div>
                <div className="right">
                  <div className="top clearfix">
                    共消耗积分<em>{allscoreCon}</em>
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
          <Modal title="" visible={this.state.visible} onCancel={this.handleCancel} width={500}
            footer={[]}
          >
            <div>
              <div className="success">
                <div className="successCon">
                  <em className="iconfont">&#xe62d;</em>
                  <span>{this.state.contentval}</span>
                </div>
                <div className="successBtn clearfix">
                  <span onClick={this.handleOk} className="goon">继续兑换</span>
                  <a href="/member/myScore/scoreOut.html">查看兑换记录</a>
                </div>
              </div>
            </div>
          </Modal>
          </div>
    );
  }
}

Ordercon = createForm()(Ordercon);

let Ordercontent = React.createClass({
	render:function(){
		return (
			<div>
        		<div className="con"><Ordercon /></div>
			</div>
			)
	}
})



ReactDOM.render(<Ordercontent />, document.getElementById("order"));
