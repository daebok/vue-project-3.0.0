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
    let bondMoneyTypeHtml = "";     //剩余债权    
    let remainDaysTypeHtml = "";    //剩余期限
    let aprTypeHtml = "";           //预期年化
    let repayStyleHtml = "";        //收益方式   
    //剩余债权
    if(searchListData.bondMoneyType){
        bondMoneyTypeHtml = <SearchOperationsList title={"剩余债权"} type={0} lists = {searchListData.bondMoneyType} />
    }
    //剩余期限
    if(searchListData.remainDaysType){
      remainDaysTypeHtml = <SearchOperationsList title={"剩余期限"} type={1} lists = {searchListData.remainDaysType} />
    }
    //预期年化
    if(searchListData.aprType){
      aprTypeHtml = <SearchOperationsList title={"预期年化"} type={1} lists = {searchListData.aprType} />
    }    
    //收益方式
    if(searchListData.repayStyle){
      repayStyleHtml = <SearchOperationsList title={"收益方式"} type={1} lists = {searchListData.repayStyle} />
    }    
    return (
      <div className="bond-index-filter" ref="filterbtn">
        <a href="javascript:;" className="filter-btn" onClick={this._showfilter}>更多筛选<em className="iconfont">&#xe631;</em></a>
        <div className="bond-index-filter-cont" id="searchOperations" onClick={this.props.changeConditionHander}>
          {bondMoneyTypeHtml}
          {remainDaysTypeHtml}
          {aprTypeHtml}          
          {repayStyleHtml}                          
        </div>
      </div>
    )
  }
}