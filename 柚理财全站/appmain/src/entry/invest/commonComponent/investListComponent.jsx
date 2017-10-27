import React from 'react';
import ReactDOM from 'react-dom';
import InvestindexList from './investindexList';
import { Modal, Pagination } from "antd";

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
			icon=<i className="iconfont" style={{'color':'#ff9545'}}>&#xe671;</i>
		}else if(this.props.rank==2&&this.props.name==this.props.nameData){
			icon=<i className="iconfont" style={{'color':'#ff9545'}}>&#xe673;</i>
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

export default class InvestListComponent extends React.Component{
	constructor(props){
    super(props);
    this._pageHander = this.pageHander.bind(this);
	this._getData = this.getData.bind(this);
	this.getListState = this.getListState.bind(this);
	this.changeList = this.changeList.bind(this);
	this.reSetState = this.reSetState.bind(this);
	this._reSetPage = this.reSetPage.bind(this); 
	this._getInvestBespeak = this.getInvestBespeak.bind(this);//预约
    this.state = {      
    	 data: null,
		 current: 1,
		 rank:{
    	 	value:0,
			name:''
		 }
    }
  }
	componentDidMount(){	
    //列表
		function getQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if (r != null) return unescape(r[2]); return null;
		}
		if(getQueryString('projectTypeId')!=null){
			this._getData({"projectTypeId": getQueryString('projectTypeId'),"page.page": 1, "page.pageSize": 10});
		}else{
				console.log(this.props.projectTypeId)
    		this._getData({"projectTypeId": this.props.projectTypeId,"page.page": 1, "page.pageSize": 10});
		}
		$('.stop-propagation,.iconfont').on('click', function(e){
   			 e.stopPropagation();
		});
  }	
	getData(dataObj){
		let self = this;               
		$.ajax({
			url: '/invest/projectList.html',
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
	//分页
	pageHander(page){
		//筛选条件
		let obj = this.props.getSearchConditionOpts($("#searchOperations"));
		obj["projectTypeId"] = $("#typeListTab li.active").find('a').attr('data-id')
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
	//预约成功刷新数据
	getInvestBespeak(){		
		//筛选条件
		let obj = this.props.getSearchConditionOpts($("#searchOperations"));	
		obj["page.page"] = this.state.current;
		//获取列表数据
		this._getData(obj);
		//设置当前分页
		this._reSetPage(this.state.current);
	}
	//列表排序
	getListState(){
		return {
			name:this.state.rank.name,
			value:this.state.rank.value
		}
	}
	reSetState(){
		this.setState({
			rank:{
				value:0,
				name:''
			}
		})
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
		//系统时间
		let systemTime = data.systemTime;
		//当前分页			
		let current = !this.state.current ? data.page.page : this.state.current;
		let pageHtml = "";
		if(data.page.rows){
			pageHtml = <Pagination current={current} pageSize={data.page.pageSize} total={data.page.count} onChange={this._pageHander} />;
		}		
		return (
			<div className="invest-index-cont clearfix">
				<div className="sort clearfix">
					<ul className="clearfix">
					<li className="w1"><span>产品名称</span></li>
						<li className="w2"><span className="type" onClick={this.changeList} data-name="apr" >预期年化收益率<IconTip rank={this.state.rank.value} name='apr' nameData={this.state.rank.name}/></span></li>
						<li className="w3"><span className="type" onClick={this.changeList} data-name="timeLimit">产品期限<IconTip rank={this.state.rank.value} name='timeLimit'  nameData={this.state.rank.name}/></span></li>
						<li className="w4"><span className="type" onClick={this.changeList} data-name="lowestAccount">起投金额<IconTip rank={this.state.rank.value} name='lowestAccount'  nameData={this.state.rank.name}/></span></li>
						<li><span>金额</span></li>                            
					</ul>
				</div>
				<div className="result clearfix">
					<InvestindexList data={data.page.rows} systemTime={systemTime}  getInvestBespeak={this._getInvestBespeak}/>
				</div>
					<div className="page page-center mt30">{pageHtml}</div>				
			</div>
		);
	}
}