import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Modal } from "antd";
import Accountmenu from '../../../component/accountmenu/menu';
import './profile.less';
//修改
import Amend from './amend';
//删除
import DeleteSetting from './deleteSetting';
//添加新地址
import Add from './add';
//设置默认
import DefaultSetting from './defaultSetting';

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


class ScoreIn extends React.Component{
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
      url: '/scoreshop/getReceivingInfo.html',
      type: 'POST',
      dataType: 'json'      
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
    let Mnums = $(".Mnums").val();
    let existNums = data.list.length;
    let residueNums = Mnums-existNums;
    let stype = residueNums <=0 ? "none" : "block";
    const that = this;
    return (
      <div>
          <div className="site-row warn-row">
            <div className="account-warn"><em className="iconfont">&#xe639;</em>最多可以添加<span className="maxNum">{Mnums}</span>个收货地址，你还可以添加<span className="residueNum">{residueNums}</span>个收货地址</div>
          </div>
          <ul className="profileList clearfix">
            {data.list.map(function(item, k){  
                 if(item.isDefult==1){
                    return <li key={k} className="clearfix">
                      <div className="left">{item.name}（收）{item.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")}<em className="active">默认</em><p>{item.provinceStr}{item.cityStr}{item.areaStr}{item.address}</p></div>
                      <div className="right"><span className="amend"><Amend uuid={item.id}/></span><span className="line">|</span><span className="remove"><DeleteSetting uuid={item.id}/></span></div>
                    </li>
                  }
                  else{
                    return <li key={k} className="clearfix">
                      <div className="left">{item.name}（收）{item.mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")}<p>{item.provinceStr}{item.cityStr}{item.areaStr}{item.address}</p></div>
                      <div className="right"><span className="amend"><DefaultSetting uuid={item.id}/></span><span className="line">|</span><span className="amend"><Amend uuid={item.id}/></span><span className="line">|</span><span className="remove"><DeleteSetting uuid={item.id}/></span></div>
                    </li>
                  }
            })}
          </ul>
          <div className="add" style={{"display":stype}}>
            <Add />
          </div>
      </div>
    );
  }
}

ReactDOM.render(<ScoreIn />,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"14"}/>,  document.getElementById("j-sider-menu"));
