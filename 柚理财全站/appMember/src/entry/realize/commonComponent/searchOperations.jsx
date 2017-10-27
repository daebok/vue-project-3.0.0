import React from 'react';
class SearchOperationsList extends React.Component{
  render (){
    let lists = this.props.lists;
    let type = this.props.type;//判断类型
    lists = lists.sort(function(a,b){//递增排序
      return a.sort - b.sort;
    });    
    return (
      <dl>
          <dt>{this.props.title}</dt>
          <dd>
            <span data-type="one" className="on">不限</span>
            {lists.map(function(item, k){             
                return <span data-type={item.dictType} data-val={item.itemValue} key={k}>{item.itemName}</span>;                           
            })}
          </dd>
      </dl>
    )
  }
}

export default class SearchOperations extends React.Component{
  constructor(props){
    super(props);
    this._showfilter = this.showfilter.bind(this);
  }
  showfilter(){
    this.props.showfilter(this.refs.filterbtn);
  }  
  render(){
    let searchListData = this.props.data.condition;    
    let realizeAmountConditionHtml = ""; //产品金额    
    let realizeDayConditionHtml = "";    //剩余期限
    let realizeAprConditionHtml = "";    //预期年化
    
    //产品金额
    if(searchListData.realizeAmountCondition){
        realizeAmountConditionHtml = <SearchOperationsList title={"产品金额"} type={0} lists = {searchListData.realizeAmountCondition} />
    }
    //剩余期限
    if(searchListData.realizeDayCondition){
      realizeDayConditionHtml = <SearchOperationsList title={"产品期限"} type={1} lists = {searchListData.realizeDayCondition} />
    }
    //预期年化
    if(searchListData.realizeAprCondition){
      realizeAprConditionHtml = <SearchOperationsList title={"预期年化"} type={1} lists = {searchListData.realizeAprCondition} />
    }
    return (
      <div className="invest-index-filter" ref="filterbtn">
        <a href="javascript:;" className="filter-btn" onClick={this._showfilter}>更多筛选<em className="iconfont">&#xe631;</em></a>
        <div className="invest-index-filter-cont" id="searchOperations" onClick={this.props.changeConditionHander}>
          {realizeAmountConditionHtml}
          {realizeDayConditionHtml}
          {realizeAprConditionHtml}      
        </div>
      </div>
    )
  }
}