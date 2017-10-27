import React from 'react';

class SearchOperationsList extends React.Component{
  render (){
    let lists = this.props.lists;
    let type = this.props.type;//判断类型
    lists = lists.sort(function(a,b){//递增排序
      return a.sort - b.sort;
    });
    let categoryId = $(".categoryId").val();
    let classstyle = $(".categoryId").val()=="" ? "on" : "";
    let typeDom = type==0 ? <span data-type="one" className={classstyle}>不限</span> : <span data-type="one" className="on">不限</span>  
    return (
      <dl>
          <dt>{this.props.title}</dt>
          <dd>
            {typeDom}
            {lists.map(function(item, k){
            if(type === 0){
                if(categoryId==item.id){
                  return <span data-type={"goodsCategoryId"} data-val={item.id} key={k} className="on">{item.goodsCategoryName}</span>;
                }
                else{
                  return <span data-type={"goodsCategoryId"} data-val={item.id} key={k} >{item.goodsCategoryName}</span>;
                }
              } else if(type === 1){
                return <span data-type={"queryScoreType"} data-val={item.itemValue} key={k}>{item.itemName}</span>;
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
  } 
  render(){
    let searchListData = this.props.data.condition;    
    let goodsCategoryListTypeHtml = "";     //商品类别    
    let scoreConditionTypeHtml = "";    //积分范围
    //商品类别
    if(searchListData.goodsCategoryList){
        goodsCategoryListTypeHtml = <SearchOperationsList title={"商品类别"} type={0} lists = {searchListData.goodsCategoryList} />
    }
    //积分范围
    if(searchListData.scoreCondition){
      scoreConditionTypeHtml = <SearchOperationsList title={"积分范围"} type={1} lists = {searchListData.scoreCondition} />
    }
    return (
      <div className="bond-index-select on" ref="filterbtn">
        <div className="bond-index-filter-cont clearfix" id="searchOperations" onClick={this.props.changeConditionHander}>
          {goodsCategoryListTypeHtml}
          {scoreConditionTypeHtml}                         
        </div>
      </div>
    )
  }
}