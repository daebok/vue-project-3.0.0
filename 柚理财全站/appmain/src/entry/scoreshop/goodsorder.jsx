import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './goodsorder.less';
import { Modal, Button ,Input, Form, Radio } from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
const RadioGroup = Radio.Group;
//修改
import Amend from './component/amend';
//设置默认
import DefaultSetting from './component/defaultSetting';
//添加新地址
import Add from './component/add';

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
    this._getData = this.getData.bind(this);
    this._getDatas = this.getDatas.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.setdefault = this.setdefault.bind(this);
    this.handleClose = this.handleClose.bind(this);
    this.state = {
      data: null,
      visible: false,
      contentval:"",
      addressStatus:0,
      successStatus:0,
      visibleval:false,
      value:""
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
    this._getData();
    this._getDatas();
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/scoreshop/getReceivingInfo.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {
      if(data.list.length<1){
        self.state.addressStatus = 1;
        self.state.successStatus = 0;
        self.setState({
            visible: true,
            contentval:"您还未设置收货地址"
          });
      }
      self.setState({data: data});
      $("ul li").each(function(){
        $(this).click(function(){
          $("ul li").removeClass("active");
          $(this).addClass("active");
        })
      })
      if(data.list.length>0){
        self.setdefault();
      }
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  setdefault(){
      this.setState({
        value: 0,
      });
  }
  handleClose(){
     this.setState({
      visible: false,
      visibleval:true
    });
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
  onChange = (e) => {
    console.log('radio checked', e.target.value);
    this.setState({
      value: e.target.value,
    });
  }
  handleSubmit(e) {
  console.log(this.state.value)
    e.preventDefault(); 
    this.props.form.validateFields((errors, values) => {
      if (!!errors) {        
        console.log('Errors in form!!!');    
        return;
      }
      let addresscon="";
      let receivingInfoIdval="";
      if(this.state.data.list.length>0){
        let i = this.state.value;
        if(this.state.data.list[i].areaStr!=undefined){
          addresscon = ""+this.state.data.list[i].provinceStr+""+this.state.data.list[i].cityStr+""+this.state.data.list[i].areaStr+""+this.state.data.list[i].address+"";
        }else{
          addresscon = ""+this.state.data.list[i].provinceStr+""+this.state.data.list[i].cityStr+""+this.state.data.list[i].address+"";
        }
        receivingInfoIdval=this.state.data.list[i].id;
      }
      console.log('Submit!!!');
      console.log(values);
      values.receiveRemark = $(".marks").val();
      values.receivingInfoId = receivingInfoIdval;
      values.receivingAddress = addresscon;
      let self = this;
      if(self.state.data.list.length<1){
        self.state.addressStatus = 1;
        self.state.successStatus = 0;
        self.setState({
            visible: true,
            contentval:"您还未设置收货地址"
          });
      }else{
        $.ajax({
        url: "/scoreshop/exchangeGoods.html",
        type: 'POST',
        dataType: 'json',
        data: values
      })
      .done(function(data) {
        self.state.addressStatus = 0;
        self.state.successStatus = 1;
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
      }
    });
  }
  render() {
    let data = this.state.data;
    if(!data){return false;}
    let datainformation = this.state.datainformation;
    if(!datainformation){return false;}
    let nums = $(".num").val();
    let score = datainformation.score;
    let goodsName = datainformation.goodsName;
    let isVirtual = datainformation.isVirtual;
    let goodsId = $(".goodsId").val()
    let allscore = nums*score;
    let allscoreCon = allscore.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
    let Mnums = $(".Mnums").val();
    let existNums = data.list.length;
    let residueNums = Mnums-existNums;
    let htype = residueNums <= 0 ? "none" : "block";
    let stype = this.state.successStatus==0 ? "none" : "block";
    let rtype = this.state.addressStatus==0 ? "none" : "block";
    let visibleval = this.state.visibleval;
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
          <Form horizontal className="form-themes" form={this.props.form} onSubmit={this.handleSubmit} data={this.state.data}>
            <div className="ShippingAddress">
              <div className="title">选择收货地址</div>
              <FormItem>
                <ul className="clearfix">
                  <RadioGroup onChange={this.onChange} value={this.state.value}>
                    {
                    data.list.map(function(k,v){
                      if(k.isDefult==1){
                        return <Radio key={v} value={v}><li key={v} className="active"><span className="Daddress"><em>{k.name}（收）{k.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")}</em><em>{k.provinceStr}{k.cityStr}{k.areaStr}{k.address}</em></span><em className="default">默认</em><span><Amend uuid={k.id}/></span></li></Radio>
                      }
                      else{
                        return <Radio key={v} value={v}><li key={v}><span className="Daddress"><em>{k.name}（收）{k.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")}</em><em>{k.provinceStr}{k.cityStr}{k.areaStr}{k.address}</em></span><em><DefaultSetting uuid={k.id}/></em><span><Amend uuid={k.id}/></span></li></Radio>
                      }
                    })
                  }
                  </RadioGroup>
                  <li><span className="add" style={{"display":htype}}><Add visibleval={visibleval} addType = "1" /></span></li>
                </ul>
              </FormItem>
            </div>
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
              <div className="success" style={{"display":stype}}>
                <div className="successCon">
                  <em className="iconfont">&#xe62d;</em>
                  <span>{this.state.contentval}</span>
                </div>
                <div className="successBtn clearfix">
                  <span onClick={this.handleOk} className="goon">继续兑换</span>
                  <a href="/member/myScore/scoreOut.html">查看兑换记录</a>
                </div>
              </div>
              <div className="reportError" style={{"display":rtype}}>
                <div className="successCon">
                  <em className="iconfont">&#xe64b;</em>
                  <span>{this.state.contentval}</span>
                </div>
                <div className="successBtn clearfix">
                  <span onClick={this.handleClose} className="goon">去设置</span>
                  <a onClick={this.handleCancel}>下次吧</a>
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
