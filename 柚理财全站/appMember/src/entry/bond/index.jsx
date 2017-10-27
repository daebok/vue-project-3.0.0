import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
import BondindexList from './commonComponent/bondindexList';
import { Modal, Pagination } from "antd";
//筛选搜索
import SearchOperations from './commonComponent/searchOperations';

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

class Bondindex extends React.Component{
  constructor(props){
    super(props);
    this._showfilter = this.showfilter.bind(this);    
    this._getData = this.getData.bind(this);
    this._pageHander = this.pageHander.bind(this);
    this._getSearchData = this.getSearchData.bind(this);
    this._changeSearchConditionHander = this.changeSearchConditionHander.bind(this);
    this._getSearchConditionOpts = this.getSearchConditionOpts.bind(this);
    this._reSetPage = this.reSetPage.bind(this);
    this.state = {
      data: null,
      condition:null,
      searchData: null,
      current: null,
    }
  }   
  showfilter(btn){    
    let filterBtn = btn;
    let bondList = $(".bond-list");
    if(filterBtn.className === "bond-index-filter on"){
      filterBtn.className = "bond-index-filter";
      $(".filter-btn").find("span").html("更多筛选");      
      bondList.animate({"margin-top": 0}, 500);
    } else {
      filterBtn.className = "bond-index-filter on";
      $(".filter-btn").find("span").html("关闭筛选");     
      bondList.animate({"margin-top": 228}, 500);
    }    
  }
  //获取筛选数据
  getSearchData(id){    
    let _this = this;
    $.ajax({
        url: '/bond/queryBondCondition.html',
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
    let self = this;  
    $.ajax({
      url: '/bond/bondListData.html',
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
    this._getData({"page.page": 1, "page.pageSize": 15});
    //筛选条件
    this._getSearchData();
  }
  //分页
  pageHander(page){
    //筛选条件
		let obj = this._getSearchConditionOpts($("#searchOperations"));		
		obj["page.page"] = page;
		//获取列表数据
		this._getData(obj);
    //设置当前分页
		this._reSetPage(page);   
  }
  //设置当前分页
	reSetPage(page){				
		this.setState({current: page});
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
    return (
      <div className="bond-index-cont clearfix">
          <div className="sort clearfix">
            <ul className="clearfix">
            <li className="w1"><span>债权名称</span></li>
              <li className="w2"><span>预期年化收益率</span></li>
              <li className="w3"><span>剩余期限</span></li>
              <li className="w3"><span>起投金额</span></li>
              <li className="w4"><span>债权金额</span></li>
              <li className="w5"><SearchOperations data={searchData} changeConditionHander = {this._changeSearchConditionHander} showfilter = {this._showfilter} /></li>                   
            </ul>
          </div>
          <div className="result bond-list clearfix">
            <BondindexList data = {data.page.rows} />
          </div>
          <div className="page page-center mt30">{pageHtml}</div>
      </div>
    )
  }
};
ReactDOM.render(<Bondindex />, document.getElementById("j-bondIndex"));