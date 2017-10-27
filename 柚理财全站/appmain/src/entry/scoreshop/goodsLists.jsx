import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './goodsLists.less';
import GoodsindexList from './component/goodsindexList';
import { Modal, Pagination } from "antd";
//筛选搜索
import SearchOperations from './component/searchOperations';

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

let IconTip = React.createClass({
    render(){
        var icon=''
        if(this.props.rank==0){
            icon=<i className="iconfont">&#xe672;</i>
        }else if(this.props.rank==1&&this.props.name==this.props.nameData){
            icon=<i className="iconfont" style={{'color':'#f95a28'}}>&#xe671;</i>
        }else if(this.props.rank==2&&this.props.name==this.props.nameData){
            icon=<i className="iconfont" style={{'color':'#f95a28'}}>&#xe673;</i>
        }else{
            icon=<i className="iconfont">&#xe672;</i>
        }
        return (
            <span className="">
        {icon}
      </span>
        )
    }
});

class Goodsindex extends React.Component{
  constructor(props){
    super(props);  
    this._getData = this.getData.bind(this);
    this._pageHander = this.pageHander.bind(this);
    this.changeList = this.changeList.bind(this);
    this._getSearchData = this.getSearchData.bind(this);
    this._changeSearchConditionHander = this.changeSearchConditionHander.bind(this);
    this._getSearchConditionOpts = this.getSearchConditionOpts.bind(this);
    this._reSetPage = this.reSetPage.bind(this);
    this.state = {
      data: null,
      condition:null,
      searchData: null,
      current: null,
        rank:{
            value:0,
            name:''
        }
    }
  }   

  //获取筛选数据
  getSearchData(id){    
    let _this = this;
    $.ajax({
        url: '/scoreshop/queryCondition.html',
        type: 'POST',
        dataType: 'json',
    }).done(function(data) {      
      if(data.result){                    
        _this.setState({searchData: data});
      } else {
        error(data.msg);
      }        
    }).fail(function() {
        error('网络异常，请重试！');
    });
  }
  //筛选搜索
  changeSearchConditionHander(e){
    let target = $(e.target);
    if(!target.attr("data-type")){return false;}    
    let projectDiv = target.parents("div#searchOperations");    
    target.addClass("on").siblings("span").removeClass("on"); 
    let obj = this._getSearchConditionOpts(projectDiv);    
    //获取列表数据
      let orderValue='';
      if(this.state.rank.value==1){
          orderValue='asc'
      }else if(this.state.rank.value==2){
          orderValue='desc'
      }else if(this.state.rank.value==0){
          orderValue=''
      }
      obj['page.sort']=this.state.rank.name;
      obj['page.order']=orderValue;
    this._getData(obj);
  }
  //筛选数据参数
  getSearchConditionOpts(projectDiv){
    let obj = {};
    let onSpans = projectDiv.find("span.on");
    let len = onSpans.length;
    obj = {"page.page": 1, "page.pageSize": 15}
    for(let i=0; i<len; i++){      
      let type = onSpans.eq(i).attr("data-type");
      let val = onSpans.eq(i).attr("data-val");      
      if(type && val){
        obj[type] = onSpans.eq(i).attr("data-val");
      }
    }
    return obj;
   }
  //获取列表数据
  getData(dataObj){
    console.log(dataObj)    
    let self = this;  
    $.ajax({
      url: '/scoreshop/getGoodsList.html',
      type: 'POST',
      dataType: 'json',
      data: dataObj,
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
  //组件加载完执行
  componentDidMount(){    
    this._getData({"page.page": 1, "page.pageSize": 15,"goodsCategoryId":$(".categoryId").val()});
    //筛选条件
    this._getSearchData();
  }
  //分页
  pageHander(page){
    //筛选条件
    let obj = this._getSearchConditionOpts($("#searchOperations"));   
    obj["page.page"] = page;
      obj["page.sort"] = this.state.rank.name;
      let orderValue=''
      if(this.state.rank.value==1){
          orderValue='asc'
      }else if(this.state.rank.value==2){
          orderValue='desc'
      }else if(this.state.rank.value==0){
          orderValue=''
      }
      obj["page.order"] = orderValue;
    //获取列表数据
    this._getData(obj);
    //设置当前分页
    this._reSetPage(page);   
  }
  //设置当前分页
  reSetPage(page){        
    this.setState({current: page});
  }
    changeList(e){
        var value=0;
          if(this.state.rank.name!=$(e.target).parents('.type').attr('data-name')){
              this.setState({
                  rank:{
                      value:1,
                      name:$(e.target).parents('.type').attr('data-name')
                  }
              })
              value=1
          }else{
              if(this.state.rank.value<2){
                  this.setState({
                      rank:{
                          value:this.state.rank.value+1,
                          name:$(e.target).parents('.type').attr('data-name')
                      }
                  })
                  value=this.state.rank.value+1
              }else{
                  this.setState({
                      rank:{
                          value:0,
                          name:$(e.target).parents('.type').attr('data-name')
                      }
                  })
                  value=0
              }
          }
        if(value==1){
            value='asc'
        }else if(value==2){
            value='desc'
        }else if(value==0){
            value=''
        }
        var url={"projectTypeId": $("#typeListTab li.active").find('a').attr('data-id'),"page.page": 1, "page.pageSize": 10};
        let onSpans = $("#searchOperations").find("span.on");
        let len = onSpans.length;
        for(let i=0; i<len; i++){
            let type = onSpans.eq(i).attr("data-type");
            let val = onSpans.eq(i).attr("data-val");
            if(type === "projectTypeList"){//产品类型
                url = {"projectTypeId": onSpans.eq(i).attr("data-id")};
            }
            if(type && val){
                url[type] = onSpans.eq(i).attr("data-val");
            }
        }
        url['page.sort']= value == 0 ?'':$(e.target).parents('.type').attr('data-name');
        url['page.order']=value;
        this._getData(url);
    }
  render() {
    let data = this.state.data;
    if(!data){return false;}    
    let searchData = this.state.searchData;
    if(!searchData){return false;}
    //当前分页      
    let current = !this.state.current ? data.page.page : this.state.current;
    let pageHtml = "";
    if(data.page.rows){
      pageHtml = <Pagination current={current} pageSize={data.page.pageSize} total={data.page.count} onChange={this._pageHander} />;
    }
    let score = $(".useScore").val();
    let serverUrl = $(".serverUrl").val();
    let userCacheAvatarPhoto = $(".userCacheAvatarPhoto").val();
    let userheaderimg = serverUrl + "/data/img/avatar/default_portrait.jpg";
    let avatarPhoto = $(".userCacheAvatarPhoto").val()!="" ? $(".avatarPhoto").val() : userheaderimg;
    return (
      <div className="bond-index-cont clearfix">
          <div className="sort clearfix">
            <div className="left">
              <div className="searchDom clearfix"><SearchOperations data={searchData} changeConditionHander = {this._changeSearchConditionHander} /></div>                   
              <ul className="clearfix">
                <li className="w3"><span onClick={this.changeList} data-name="score" className="type">积分<IconTip rank={this.state.rank.value} name='score'  nameData={this.state.rank.name}/></span></li>
                <li className="w3"><span onClick={this.changeList} data-name="lessNum" className="type">剩余数量<IconTip rank={this.state.rank.value} name='lessNum'  nameData={this.state.rank.name}/></span></li>
              </ul>
            </div>
            <div className="right">
                <div className="clearfix scoreNews">
                    <img src={avatarPhoto} className="img" /> 
                    <div className="score">
                      <em>{score}</em>
                      <p>您当前可用积分</p>
                    </div>
                </div>
                <div className="btns">
                  <a href="/member/myScore/scoreOut.html">我的兑换</a>
                  <a href="/member/myScore/scoreIn.html">积分记录</a>
                </div>
            </div>
          </div>
          <div className="result bond-list clearfix">
            <GoodsindexList data = {data.page.rows} />
          </div>
          <div className="page page-center mt30">{pageHtml}</div>
      </div>
    )
  }
};
ReactDOM.render(<Goodsindex />, document.getElementById("j-goodsList"));