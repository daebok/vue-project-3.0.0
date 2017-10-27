import React from 'react';
class SearchOperationsList extends React.Component{
  render (){
    let lists = this.props.lists;
    let type = this.props.type;//判断类型
    lists = lists.sort(function(a,b){//递增排序
      return a.sort - b.sort;
    });
    return (
      <dl className='clearfix'>
          <dt>{this.props.title}</dt>
          <dd>
            <span data-type="one" className="on">不限</span>
            {lists.map(function(item, k){
              if(type === 0){
                return <span data-type={"projectTypeList"} data-id={item.id} key={k}>{item.typeName}</span>;
              } else if(type === 1){
                return <span data-type={item.dictType} data-val={item.itemValue} key={k}>{item.itemName}</span>;
              }
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
    let projectTypeHtml = "";     //产品类型
    let amountConditionHtml = ""; //产品金额
    let timeConditionHtml = "";   //产品期限
    let repayStyleHtml = "";      //收益方式
    //产品类型
    if(searchListData.projectTypeList){
        projectTypeHtml = <SearchOperationsList title={"产品类型"} type={0} lists = {searchListData.projectTypeList} />
    }
    //产品金额
    if(searchListData.amountCondition){
      amountConditionHtml = <SearchOperationsList title={"产品金额"} type={1} lists = {searchListData.amountCondition} />
    }
    //产品期限
    if(searchListData.timeCondition){
      timeConditionHtml = <SearchOperationsList title={"产品期限"} type={1} lists = {searchListData.timeCondition} />
    }
    //收益方式
    if(searchListData.repayStyle){
      repayStyleHtml = <SearchOperationsList title={"收益方式"} type={1} lists = {searchListData.repayStyle} />
    }
    return (
      <div className="invest-index-filter clearfix" ref="filterbtn">
        <a href="javascript:;" className="filter-btn" onClick={this._showfilter}>更多筛选<em className="iconfont">&#xe631;</em></a>
        <div className="invest-index-filter-cont" id="searchOperations" data-id={this.props.projectTypeId} onClick={this.props.changeConditionHander}>
          {projectTypeHtml}
          {amountConditionHtml}
          {timeConditionHtml}
          {repayStyleHtml}
        </div>
      </div>
    )
  }
}
