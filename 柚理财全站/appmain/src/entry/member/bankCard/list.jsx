import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import {Modal} from 'antd';
import './list.less';

function error(a,url) {
  Modal.error({
    title: a,
    okText:"确定",
    wrapClassName:'tiperror',
    onOk() {
      if(url){
        console.log(url);
        window.location.href = url;
      }
    }
  });
}

let Cardlist = React.createClass({
  getInitialState() {
    return {
      data: this.props.data
    };
  },
  render(){

    let addbank = this.state.data.bankList.length >= this.state.data.bankNum ? "" : <li className="addcard"><a href="/member/bankCard/bind.html" target="_blank"><em className="iconfont">&#xe645;</em>添加银行卡</a></li>;

    if(this.state.data.userCaCheInfo.userNature=='2'){
      addbank='';
    }
    let banklist = '';
      if(this.state.data.bankList != '' || this.state.data.bankList.length > 0){                                                          
          banklist = this.state.data.bankList.map(function(k,v){
              let link = "/member/bankCard/unBind.html?cardId="+ k.bankNo +"&bankCode="+k.bankCode;
              let unbindlink = k.canDisable === true ? <span className="del"><a href={link} target="_blank">解除绑定</a></span> : <span className="del"><a>默认提现银行卡，不允许解绑。</a></span>;                   
              return <li key={v}><span className="bankname"><img src={k.picPath} />{k.bankName}</span><span className="cardnumber">{k.bankNoSuffix}</span>{unbindlink}</li>
            })
      }
      else
      {
          banklist = "";
      } 

      return (
          <div className="account-main">
              <div className="main-title">银行卡</div>
              <ul className="card-list">
              {banklist}
              {addbank} 
              </ul>
          </div>
        )
    }  
});

let getinfo = function(){
  $.ajax({
    url: '/member/bankCard/getBankCardList.html',
    type: 'POST',
    dataType: 'json'
  })
  .done(function(data) {
    if( data.result ){
     ReactDOM.render(<Cardlist data={data}/>, document.getElementById("j-listtab"));
    }
    else
    {
      error(data.msg,data.url);
    } 
  })
  .fail(function() {
    error('网络异常，请重试！');
  });
}

getinfo();

if($("body").attr("data-vouch") == "3"){
  ReactDOM.render(<AccountVouchMenu current = {"8"}/>,  document.getElementById("j-sider-menu"));
}
else
{
  ReactDOM.render(<Accountmenu current = {"10"}/>,  document.getElementById("j-sider-menu"));
}