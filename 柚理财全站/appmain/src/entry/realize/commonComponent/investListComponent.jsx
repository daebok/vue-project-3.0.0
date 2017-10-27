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

export default class InvestListComponent extends React.Component{
	constructor(props){
		super(props);
		this._getData = this.getData.bind(this);
		this._pageHander = this.pageHander.bind(this);
		this._reSetPage = this.reSetPage.bind(this);
        this.changeList = this.changeList.bind(this);
        this.getListState = this.getListState.bind(this);
		this.state = {
			data: null,
			current: null,
            rank:{
                value:0,
                name:''
            }
		};
	}
	componentDidMount(){            
		this._getData({"page.page": 1, "page.pageSize": 10});        
	}
	getData(dataObj){		
		let self = this;               
		$.ajax({
			url: '/realize/projectList.html',
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
    getListState(){
        return {
            name:this.state.rank.name,
            value:this.state.rank.value
        }
    }
	//分页
	pageHander(page){
		//筛选条件
		let obj = this.props.getSearchConditionOpts($("#searchOperations"));
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
		this._reSetPage(this.state.current);
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
		//系统时间
		let systemTime = data.systemTime;
		//当前分页
		let current = !this.state.current ? data.List.page : this.state.current;
		let pageHtml = "";
		if(data.List.rows){
			pageHtml = <Pagination current={data.List.page} pageSize={data.List.pageSize} total={data.List.count} onChange={this._pageHander} />;
		}		
		return (
			<div className="invest-index-cont clearfix">
				<div className="sort clearfix">
					<ul className="clearfix">
					<li className="w1"><span>产品名称</span></li>
						<li className="w2"><span onClick={this.changeList} data-name="apr" className="type">预期年化收益率<IconTip rank={this.state.rank.value} name='apr' nameData={this.state.rank.name}/></span></li>
						<li className="w3"><span onClick={this.changeList} data-name="timeLimit" className="type">产品期限<IconTip rank={this.state.rank.value} name='timeLimit'  nameData={this.state.rank.name}/></span></li>
						<li className="w3"><span onClick={this.changeList} data-name="lowestAccount" className="type">起投金额<IconTip rank={this.state.rank.value} name='lowestAccount'  nameData={this.state.rank.name}/></span></li>
						<li><span>金额</span></li>                            
					</ul>
				</div>
				<div className="result clearfix">
					<InvestindexList data = {data.List.rows} systemTime={systemTime} />
				</div>
				<div className="page page-center mt30">{pageHtml}</div>
			</div>
		);
	}
}