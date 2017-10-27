import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
//滚动图
import Scroll from './component/scroll';
//热门商品
import HotgoodsList from './component/goodsList';
//最新兑换
import ExchangeList from './component/exchangeList';
//家居百货
import DepartmentList from './component/departmentList';

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}


class Scorecontent extends React.Component{
  constructor(props){
    super(props);
    this._getData = this.getData.bind(this);
    this.state = {
      data : ""
    }
  }
  getData(){
    let self = this;
    $.ajax({      
      url: '/scoreshop/getScoreGoodsCategoryList.html',
      type: 'POST',
      dataType: 'json'
    })
    .done(function(data) {      
      if(data.result){        
        self.setState({data: data});
      } else {        
        error(data.msg);
      }      
    })
    .fail(function() {
      //error("网络异常，请重试！");
    })
  }
  componentDidMount(){
    this._getData();
  }
	render(){
    let data = this.state.data;
    if(!data) return false; 
    let score = $(".useScore").val();
    let serverUrl = $(".serverUrl").val();
    let userCacheAvatarPhoto = $(".userCacheAvatarPhoto").val();
    let userheaderimg = serverUrl + "/data/img/avatar/default_portrait.jpg";
    let avatarPhoto = $(".userCacheAvatarPhoto").val()!="" ? $(".avatarPhoto").val() : userheaderimg;
    console.log(avatarPhoto);
		return (
			<div>
        <div className="scorecon1">
  				<div className="myscore clearfix">
              <div className="left"><Scroll/></div>
              <div className="right">
                  <div className="tx"><img src={avatarPhoto} /></div>
                  <div className="currentScore"><p>{score}</p>您当前可用积分</div>
                  <div className="scoreBtn clearfix"><a href="/member/myScore/scoreOut.html" className="exchange">我的兑换</a><a href="/member/myScore/scoreIn.html" className="record">积分记录</a></div>
              </div>
          </div>
          <div className="scorecon2 clearfix">
              <div className="hotgoods">
                  <div className="title">热门商品</div>
                  <HotgoodsList />
              </div>
              <div className="exchangeList">
									<div className="title">最新兑换</div>
									<ExchangeList />
							</div>
          </div>
        </div>
				<div className="scoreProducts">
        <div className="title clearfix">精品推荐<a href="/scoreshop/goodsList.html">查看全部&gt;</a></div>
             {
                data.list.map(function(k,v){
                  let serverUrlval = serverUrl+k.introPic;
                  var urlval = "/scoreshop/goodsList.html?id="+k.id;
                  return <div key={v}><div className="department clearfix"><div className="left"><img src={serverUrlval}/><span className="abg"></span><a href={urlval}>更多</a></div><div className="right"><DepartmentList id = {k.id}/></div></div></div>
                })
              }
				</div>
			</div>
			)
	}
}



ReactDOM.render(<Scorecontent />, document.getElementById("scoreCon"));
