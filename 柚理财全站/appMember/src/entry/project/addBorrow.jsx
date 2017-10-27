import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import {Modal,Form,Input} from 'antd';
import Success from '../../component/success/success';
const FormItem = Form.Item;
import './qualification.less';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v,url) {
  Modal.success({
    title: '提交成功',
    content: v,
    okText:"确定",
    onOk() {
      if(url){
        window.location.href = url;
      }
    }   
  });
}


let BorrowForm = React.createClass({
  getInitialState() {
    return {
      data: '',
      imgsrc:'/validimg.html',
    };
  },
  confirm(){
    const self = this;
    let values = {};
    if(this.refs.codeInput.value==""){
        error('请输入验证码！');
        return false
    }
    values[$("body").data("tokenname")] = $("body").data("token");
    values['validCode'] = this.refs.codeInput.value;
    $.ajax({
      url: '/borrow/doAddBorrow.html',
      type: 'POST',
      dataType: 'json',
      data: values
    })
    .done(function(data) {
      if(data.result){
        ReactDOM.render(<Success callback={self.settimefun} link={"/member/myLoan/list.html"} linkname={"账户借款列表"} time={3} text={"您的借款申请正在审核中"} textSmall={"1个工作日内平台将审核完成，请耐心等候"} />, document.getElementById("j-addBorrow"));
      }
      else
      {
        error(data.msg);
      }  
    })
    .fail(function() {
      error('网络异常，请重试！');
    });    
  },
  settimefun(){
    window.location.href = "/member/myLoan/list.html";
  },  
  getInfo(){
      let self = this;
      $.ajax({
        url: '/borrow/getBorrowData.html',
        type: 'POST',
        dataType: 'json'
      })
      .done(function(data) {
        self.setState({
          data:data
        });
      })
      .fail(function() {
        error('网络异常，请重试！');
      });
  },
  componentDidMount(){
    this.getInfo();
  },
    changeimg(){
        this.setState({ imgsrc: '/validimg.html?t=' + Math.random()});
    },
  render() {
    let data = this.state.data;
    if(!data){return false;}
    let repayStyle = data.borrow.repayStyle;
    let repayvalue = "";
    if(repayStyle==1){
        repayvalue="等额本息";
    }else if(repayStyle==2){
        repayvalue="一次性还款";
    }else if(repayStyle==3){
        repayvalue="每月还息到期还本";
    }else if(repayStyle==4){
        repayvalue="等额本金";
    }else if(repayStyle==5){
        repayvalue="每季还息到期还本";
    }
    if(!data){
      return false;
    }
    const unit = data.borrow.timeType == "1" ? "天" : "个月";

    let picpath = [],image_server_url = $("body").attr("data-url");

    if(data.borrow.picPath){
      picpath = data.borrow.picPath;
    }
    let mtgpath = [];

    if(data.borrow.mtgPath){
      mtgpath = data.borrow.mtgPath;
    }
    return (
        <div className="inventory">
          <ul className="list">
            <li>
              <span className="number">{data.borrow.account}<s>元</s></span>借款金额
            </li>
            <li>
              <span className="number">{data.borrow.apr}<s>%</s></span>年化收益率
            </li>
            <li>
              <span className="number">{data.borrow.timeLimit}<s>{unit}</s></span>借款期限
            </li>                        
          </ul>
          <dl className="interest">
            <dt>预计利息</dt>
            <dd>{parseFloat(data.borrow.repaymentInterest).toFixed(2)}元（投资人向借款人收取作为利息）</dd>
          </dl>
          <dl>
            <dt>借款标题</dt>
            <dd>{data.borrow.projectName}</dd>
          </dl>
          <dl>
            <dt>借款用途</dt>
            <dd>{data.borrow.borrowUse}</dd>
          </dl>
          <dl>
            <dt>还款方式</dt>
            <dd>{repayvalue}</dd>
          </dl>
          <dl>
            <dt>借款描述</dt>
            <dd style={{width:880}}>{data.borrow.content}</dd>
          </dl>
          <dl className="vertical">
            <dt>借款资料</dt>
            <dd>
              <ul>
                  {
                    picpath.map(function(k,v){
                      return <li  key={v}><img src={image_server_url+k} /></li>
                    })
                  }
              </ul>
            </dd>
          </dl>
          <dl className="vertical">
            <dt>抵押物资料</dt>
            <dd>
              <ul>
                  {
                    mtgpath.map(function(k,v){                                    
                      return <li  key={v}><img src={image_server_url+k} /></li>
                    })
                  }
              </ul>
            </dd>
          </dl>
          <dl className={data.userCompanyList.length ? "" : "hide"}>
            <dt>担保公司</dt>
            <dd>
            {
              data.userCompanyList.map(function(k,v){
                if(data.borrow.vouchId == k.userId){
                  return k.companyName;
                }                                    
              })              
            }
            </dd>
          </dl>
            <dl>
                <dt style={{'marginTop':'5px'}}>验证码</dt>
                <div className="col-3"><input maxLength="4" className="ant-input ant-input-lg" ref="codeInput" placeholder="验证码" size="large" type="text" autoComplete="off" />
                </div>
                <div className="col-8 pl20"><img onClick={this.changeimg} src={this.state.imgsrc} className="ant-form-text codeimg" /></div>
            </dl>
        <div className="btn"><a href="javascript:" onClick={this.confirm} className="subbutton">确认借款</a><a href="/borrow/addBorrowPage.html"  className="subbutton_back">返回修改</a></div>
        </div>
    )
  }
});

ReactDOM.render(<BorrowForm />, document.getElementById("j-addBorrow"));