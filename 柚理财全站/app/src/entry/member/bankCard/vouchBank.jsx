import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import AccountVouchMenu from '../../../component/accountmenu/AccountVouchMenu';
import './list.less';

let Cardlist = React.createClass({
  getInitialState() {
    return {
      data: this.props.data
    };
  },
  render(){   
      let banklist = '';
      if(this.state.data.bankList != '' || this.state.data.bankList.length > 0){                                                          
          banklist = this.state.data.bankList.map(function(k,v){
              let link = "/member/bankCard/unBind.html?cardId="+ k.bankNo +"&bankCode="+k.bankCode;
              return <li key={v}><span className="bankname"><img src={k.picPath} />{k.bankName}</span><span className="cardnumber">{k.bankNo}</span></li>
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
              </ul>
          </div>
        )
    }  
});

let getinfo = function(){
  $.ajax({
    url: '/member/vouch/getBank.html',
    type: 'POST',
    dataType: 'json'
  })
  .done(function(data) {
    ReactDOM.render(<Cardlist data={data}/>, document.getElementById("j-listtab"));
  })
  .fail(function() {
    console.log("error");
  });
}

getinfo();

ReactDOM.render(<AccountVouchMenu current = {"8"}/>,  document.getElementById("j-sider-menu"));