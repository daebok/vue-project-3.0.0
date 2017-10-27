import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './index.less';
//筛选搜索
import SearchOperations from './commonComponent/searchOperations'
//投资列表组件
import InvestListComponent from './commonComponent/investListComponent';
import { Tabs, Modal } from 'antd';
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

class InvestTypeList extends React.Component{
  constructor(props){
    super(props);
    this._showfilter = this.showfilter.bind(this);
    this._changeSearchConditionHander = this.changeSearchConditionHander.bind(this);
    this._getSearchData = this.getSearchData.bind(this);
    this._getListData = this.getListData.bind(this);
    this._reSetPage = this.reSetPage.bind(this);
    this.typeListTabHander = this.typeListTabHander.bind(this);
    this._getSearchConditionOpts = this.getSearchConditionOpts.bind(this);
    this.objVal = [];
    this.state = {
      searchData: null,
      boxOpen:false
    }
  }
  componentDidMount(){
    //类型切换
    this.typeListTabHander();
    //筛选条件
    function getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return unescape(r[2]); return null;
    }
    if(getQueryString('projectTypeId')!=null){
      this._getSearchData(getQueryString('projectTypeId'));

    }else{
      this._getSearchData(this.objVal.projectTypeId);
    }


  }
  //类型切换
  typeListTabHander(){
    let _this = this;

    $("body").on("click", "#typeListTab li", function(){
      if($(this).hasClass("active") || $(this).hasClass("NoClick")){return;}
      $(this).addClass("active").siblings().removeClass("active");
      let id = $(this).find("a").attr("data-id");
      //获取列表
      _this._getListData({"projectTypeId": id,"page.page": 1, "page.pageSize": 10});
      //获取筛选数据
      _this._getSearchData(id);
      //重置分页
      _this._reSetPage(1);
      _this.objVal.projectTypeId = id;
      //重置筛选数据高度
      _this.refs.investListComponent.reSetState();
    });
  }
  //获取筛选数据
  getSearchData(id){
    let _this = this;
    $.ajax({
        url: '/invest/queryCondition.html',
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
      this.setState({boxOpen:false});
    } else {
      filterBtn.className = "invest-index-filter on";
      this.setState({boxOpen:true});
      $(".filter-btn").find("span").html("关闭筛选");
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
    obj = {"projectTypeId": projectDiv.attr("data-id"),"page.page": 1, "page.pageSize": 10}
    for(let i=0; i<len; i++){
      let type = onSpans.eq(i).attr("data-type");
      let val = onSpans.eq(i).attr("data-val");
      if(type === "projectTypeList"){//产品类型
        obj = {"projectTypeId": onSpans.eq(i).attr("data-id")};
      }
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
  //重置分页
  reSetPage(page){
    this.refs.investListComponent._reSetPage(page);
  }
  render(){
    let _this = this;
    let navList = this.props.typeList;
    navList = navList.sort(function(a, b){//排序
      return a.sort - b.sort;
    });
    //显示第一类型数据
    let len = navList.length;
    for(let i=0; i<len; i++){
      if(navList[i].id == $("#projectTypeId").val()){
        this.objVal.projectTypeId = navList[i].id;
      }
    }
    this.objVal.projectTypeId = !this.objVal.projectTypeId ? navList[0].id : this.objVal.projectTypeId;
    function getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return unescape(r[2]); return null;
    }
    if(getQueryString('projectTypeId')!=null){
      this.objVal.projectTypeId=getQueryString('projectTypeId');
    }
    let searchData = this.state.searchData;
    if(!searchData){return false;}
    return (
      <div className="invest-index-main">
        <div className="invest-index-nav clearfix">
          <ul className="navList clearfix" id="typeListTab">
            {navList.map(function(item, k){
                let className = ((item.id == _this.objVal.projectTypeId) ? "active": "");
                return <li key={item.sort} className={className}><a href="javascript:;" data-id={item.id}>{item.typeName}</a></li>
            })}
            <li className="NoClick"><a href="/realize/index.html">变现通</a></li>
          </ul>
          <SearchOperations data={searchData} projectTypeId={this.objVal.projectTypeId} changeConditionHander = {this._changeSearchConditionHander} showfilter = {this._showfilter} />
        </div>
        <div className="invest-index-list">
          <InvestListComponent ref="investListComponent" getSearchConditionOpts={this._getSearchConditionOpts}  projectTypeId={this.objVal.projectTypeId} />
        </div>
      </div>
    )
  }
}

class Investindex extends React.Component{
  constructor(props){
    super(props);
    this._getInvestTypeListHander = this.getInvestTypeListHander.bind(this);
    this.state = {
      condition:null,
      typeList: null,
    }
  }
  componentDidMount(){
    this._getInvestTypeListHander();
  }
  getInvestTypeListHander(){
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
      <InvestTypeList typeList={this.state.typeList.list} />
    )
  }
};
ReactDOM.render(<Investindex />, document.getElementById("j-investIndex"));
