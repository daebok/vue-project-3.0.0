import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
//筛选搜索
import SearchOperations from './commonComponent/searchOperations';
//投资列表组件
import InvestListComponent from './commonComponent/investListComponent';
import { Tabs } from 'antd';
const TabPane = Tabs.TabPane;

function error(v) {
  Modal.error({
    title: v,
    okText:"关闭",
    wrapClassName:'tiperror'
  });
}

function success(v) {
  Modal.success({
    title: '提交成功',
    content: v,
  });
}

class ProjectTypeList extends React.Component{
  constructor(props){
    super(props);
    this._showfilter = this.showfilter.bind(this);
    this._getSearchData = this.getSearchData.bind(this); 
    this._changeSearchConditionHander = this.changeSearchConditionHander.bind(this);
    this._getSearchConditionOpts = this.getSearchConditionOpts.bind(this);
    this._getListData = this.getListData.bind(this);
    this.state = {      
      searchData: null,
    }
  }
  componentDidMount(){    
    //筛选条件
    this._getSearchData();
  }  
  //获取筛选数据
  getSearchData(id){
    let _this = this;
    $.ajax({
        url: '/realize/queryCondition.html',
        type: 'POST',
        dataType: 'json',
        data: {"projectTypeId": id}
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
  showfilter(btn){    
    let filterBtn = btn;
    let investList = $(".invest-index-list");
    if(filterBtn.className === "invest-index-filter on"){
      filterBtn.className = "invest-index-filter";
      $(".filter-btn").find("span").html("更多筛选");      
      investList.stop(true,false).animate({"margin-top": 0}, 500);
    } else {
      filterBtn.className = "invest-index-filter on";
      $(".filter-btn").find("span").html("关闭筛选");     
      investList.stop(true,false).animate({"margin-top": 180}, 500);
    }    
  }
  //筛选搜索
  changeSearchConditionHander(e){
    let target = $(e.target);
    if(!target.attr("data-type")){return false;}    
    let projectDiv = target.parents("div#searchOperations");    
    target.addClass("on").siblings("span").removeClass("on"); 
    let obj = this._getSearchConditionOpts(projectDiv);
    let list = this.refs.investListComponent.getListState();
      if(list.value==1){
          list.value='asc'
      }else if(list.value==2){
          list.value='desc'
      }else{
          list.value=''
      }
      obj['page.sort']=list.name;
      obj['page.order']=list.value;
    //获取列表数据
    this._getListData(obj);
    
  }
  //筛选数据参数
  getSearchConditionOpts(projectDiv){
    let obj = {};
    let onSpans = projectDiv.find("span.on");
    let len = onSpans.length;
    obj = {"page.page": 1, "page.pageSize": 10}
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
  getListData(obj){    
    this.refs.investListComponent._getData(obj);
  }
  render(){   
    let navList = this.props.typeList;
    navList.sort(function(a,b){//排序
      return a.sort - b.sort;
    });
    let searchData = this.state.searchData;
    if(!searchData){return false;}     
    return (
      <div className="realize-index-main">
        <div className="realize-index-nav clearfix">
          <ul className="navList" id="typeListTab">
            {navList.map(function(item, k){
                let link = "/invest/index.html?projectTypeId=" + item.id;
                return <li key={item.sort}><a href={link}>{item.typeName}</a></li>
            })}
            <li className="NoClick active"><a href="javascript:;">变现通</a></li>
          </ul>
          <SearchOperations data={searchData} changeConditionHander = {this._changeSearchConditionHander} showfilter = {this._showfilter} />
        </div>
        <div className="invest-index-list">
          <InvestListComponent ref="investListComponent" getSearchConditionOpts={this._getSearchConditionOpts} />
        </div>
      </div>      
    )
  }
}

class RealizeIndex extends React.Component{
  constructor(props){
    super(props);
    this._getProjectTypeList = this.getProjectTypeList.bind(this);
    this.state = {
      condition:null,      
      typeList: null,
    }    
  }
  componentDidMount(){
    this._getProjectTypeList();
  }
  getProjectTypeList(){
    let self = this;	
		$.ajax({
			url: '/invest/projectTypeList.html',
			type: 'POST',
			dataType: 'json',
			data: {"parentId": "7ca31c421ce34e3fb8d57208e42f409f"}	
		})
		.done(function(data) {      
			if(data.result){          				
				self.setState({typeList: data});
			} else {
				error(data.msg);
			}			
		})
		.fail(function() {
			//error("网络异常，请重试！");
		})		
  }
  render() {
    if(!this.state.typeList){return false;}    
    return (   
      <ProjectTypeList typeList={this.state.typeList.list} />
    )
  }
};
ReactDOM.render(<RealizeIndex />, document.getElementById("j-realizeIndex"));