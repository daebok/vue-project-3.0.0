import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import  { ValidateFn } from '../../common/validateFn';
import {Publiclib} from '../../common/common';
import { Modal, Button ,Input, Form,} from 'antd';
const createForm = Form.create;
const FormItem = Form.Item;
import './goodsDetails.less';

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

export default class DigitalList extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this.jump = this.jump.bind(this);
    this.jumpvirtual = this.jumpvirtual.bind(this);
    this.state = {
      data: null
    };
  }
  componentDidMount(){
    this._getData();
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/scoreshop/getGoodsInfo.html',
      type: 'POST',
      dataType: 'json',
      data: {"id":$(".id").val()}
    })
    .done(function(data) {
      self.setState({data: data});
      var maxnum = self.state.data.exchangeLimit;//限兑个数
      var redeem = self.state.data.score;//所需积分
      var useScore = $(".useScore").val();//可用积分
      var residue = self.state.data.lessNum;//剩余数量
      var exchangeNum = Math.floor(useScore/redeem)//可兑换个数
      var exchangedOrders = $(".exchangedOrders").val()//已兑换个数
      var syval = maxnum-exchangedOrders;
      var nums = 0;
      var cz = useScore-redeem;
      var useScoreval = useScore.toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
      $(".residueScore").find("em").html(useScoreval);
      if(syval>0){
      $(".submit-btn").attr("disabled",false);
      $(".error-tips").find("span").html("");
      $(".error-tips").hide();
      $(".nums").keyup(function(){
        let val = $(this).val();
        let vals= val.replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
        $(this).val(vals);
        if($(this).val()>maxnum){
          $(".submit-btn").attr("disabled",true);
          $(".error-tips").find("span").html("不能超过每人限兑个数")
          $(".error-tips").show();
        }
        else if($(this).val()>syval){
          $(".submit-btn").attr("disabled",true);
          $(".error-tips").find("span").html("兑换数量不能超过个人限兑数量")
          $(".error-tips").show();
        }
        else if($(this).val()>exchangeNum){
          $(".submit-btn").attr("disabled",true);
          $(".error-tips").find("span").html("积分不足")
          $(".error-tips").show();
        }
        else if($(this).val()>residue){
          $(".submit-btn").attr("disabled",true);
          $(".error-tips").find("span").html("不能超过剩余数量")
          $(".error-tips").show();
        }
        else{
          $(".submit-btn").attr("disabled",false)
          $(".error-tips").find("span").html("")
          $(".error-tips").hide();
        }
      })
      if(cz<0){
        $(".submit-btn").attr("disabled",true);
        $(".error-tips").find("span").html("积分不足")
        $(".error-tips").show();
      }
      else{
        $(".submit-btn").attr("disabled",false)
        $(".error-tips").find("span").html("")
        $(".error-tips").hide();
      }
      $(".add").click(function(){
        if(nums<syval){
         if(nums<residue){
           if(nums<maxnum){
            if(nums<exchangeNum){
              nums += 1;
              $(".nums").val(nums);
            }
           } 
         }
        }
      })
      $(".down").click(function(){
         if(nums>0){
            nums -= 1;
            $(".nums").val(nums);
          }
      })
      }
      else{
        $(".submit-btn").attr("disabled",true);
        $(".error-tips").find("span").html("兑换数量不能超过每人限兑数量")
        $(".error-tips").show();
      }
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  jump(){
    if($(".nums").val()!=0){
      window.location.href = "http://"+window.location.host+"/scoreshop/order.html?id="+$(".id").val()+"&num="+$(".nums").val();
    }
  }
  jumpvirtual(){
    if($(".nums").val()!=0){
      window.location.href = "http://"+window.location.host+"/scoreshop/virtualOrder.html?id="+$(".id").val()+"&num="+$(".nums").val();
    }
  }
  check(e){
    var that = e.target;
    Publiclib.setint(that);
  }
  render() {
    let data = this.state.data;
    if(!data){return false;}
    let exchangeLimit = data.exchangeLimit;
    let exchangedOrders = $(".exchangedOrders").val()//已兑换个数
    let syval = exchangeLimit-exchangedOrders;
    let isVirtual = data.isVirtual;
    let serverUrl = $(".serverUrl").val();
    let serverUrlval = serverUrl+data.picSmall;
    let btnDom = isVirtual=="0" ? <a className="submit-btn" onClick={this.jump}>立即兑换</a> : <a className="submit-btn" onClick={this.jumpvirtual}>立即兑换</a>
    let nobtn = <a className="nobtn" >已兑完</a>;
    let domval = data.lessNum > 0 ? btnDom : nobtn ;
    let scoreval = (data.score).toString().replace(/(\d)(?=(?:\d{3})+$)/g, '$1,');
    let syvalDom = syval==exchangeLimit ? <em>（每人限兑{data.exchangeLimit}个）</em> : <em>（每人限兑{data.exchangeLimit}个,您还可以兑换{syval}个）</em>;
    return (
          <div>
              <div className="buyGoods clearfix">
                  <div className="left"><img src={serverUrlval}/></div>
                  <div className="right">
                      <div className="ptitle">
                        <span>{data.goodsName}</span>
                        <p><em className="iconfont icontips">&#xe685;</em><em>{data.goodsCategoryName}</em><em className="iconfont icontips">&#xe684;</em><em>{data.remark}</em></p>
                      </div>
                      <ul className="datadetail clearfix">
                         <li className="score">{scoreval}<p>所需积分</p></li>
                         <li className="price clearfix"><em>￥{data.marketPrice}</em><p>市场价格</p></li>
                         <li className="residue">{data.lessNum}<p>剩余数量</p></li>
                      </ul>
                      <div className="exchangeNum clearfix">
                          <div className="copies clearfix">
                            <span className="down">-</span>
                            <Input type="text" className="nums" maxLength="100" autoComplete="off" defaultValue="0" onKeyUp={this.check}/>
                            <span className="add">+</span>
                            {syvalDom}
                          </div>
                          <div className="residueScore">可用积分：<em>50000</em></div>
                      </div>
                      {domval}<span className="error-tips"><em className="iconfont">&#xe62e;</em>积分不足</span>
                  </div>
              </div>
              <div className="goodsDetails">
                <div className="title">商品详情</div>
                <div className="con">
                    {data.content}
                </div>
              </div>
          </div>
    );
  }
}

ReactDOM.render(<DigitalList />, document.getElementById("goodsdetail"));
