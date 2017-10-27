import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Table,Modal} from 'antd';
import Accountmenu from '../../../component/accountmenu/menu';
import './projectCollection.less';
import {Publiclib} from '../../../common/common';


class ProjectRepayment extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      projectName:"",
      repaymentList :'',
      type:Publiclib.GetQueryString("type")
    }
    this.getinfo = this.getinfo.bind(this);
  }
  getinfo(current){
    let self = this;
    $.ajax({
      url: '/member/myInvest/getProjectCollectionList.html?investId=' + self.props.projectId,
      type: 'POST',
      dataType: 'json',
      data:{'page.page':current}
    })
    .done(function(data) {
      $.each(data.data.rows,function(index, el) {
        el.key = index;
      });

      self.setState({
        projectName: data.projectName,
        repaymentList: data.data
      });
    })
    .fail(function() {     
      console.log("error");
    })
  }
  componentDidMount() {
    this.getinfo('1');
  }
  render(){
      if(!this.state.repaymentList){
          return false
      }
      var _this=this
      const statusType = ["待收","已收"];
      const columns = [
          {
            title: '期数',
            dataIndex: 'periodsStr',
            render(text) {
              return text;
            },
          },
          {
            title: '预期回款日期',
            dataIndex: 'repayTime',
            render(text) {
              return Publiclib.formatDate(text);
            },             
          },
          {
            title: '预期回款金额(元)',
            dataIndex: 'payment',
          },
          {
            title: '实际回款日期',
            dataIndex: 'lastRepayTimeStr',
            render(text,record) {
                if(record.collectionType==1){
                    text='已转让'
                }
                  return text;
              }
            },
            {
              title:'实际回款金额',
              dataIndex:'repayedAmountStr',
              render(text,record) {
                  if(record.collectionType==1){
                      text='已转让'
                  }
                  return text
              },
            },
            {
            title: '类型',
            dataIndex: 'repayTypeStr',
            render(text,record) {
                  return text;
                }
            },
            {
              title:'状态',
              dataIndex:'status',
              render(text,record) {
                  if(record.collectionType==1){
                      return text='已转让'
                  }else{
                      return statusType[text];
                  }
              },              
            }   
          ];
      const pagination = {
          current:this.state.repaymentList.page,
          total:this.state.repaymentList.count,
          pageSize:this.state.repaymentList.pageSize,
          onChange(current) {
             _this.getinfo(current);

          }
      };
      let tableobj = <Table columns={columns} dataSource={this.state.repaymentList.rows} pagination={pagination} />;
      if(!this.state.repaymentList.rows.length){
        tableobj = <div className='nolist'><em className='iconfont'>&#xe62b;</em>暂无记录</div>;
      }
      let url="/member/myInvest/list.html?tab=" + Publiclib.GetQueryString("tab")
      if(this.state.type=="bond"){
          url="/member/myBond/list.html?tab=4"
      }
      return (
            <div className="account-main">
                <div className="main-title">回款计划<a href={url} className="backBtn">&lt; 返回</a></div>
                <div>
                  <div className="projectRepayment">
                    <div className="project">借款产品<span className="name">{this.state.projectName}</span></div>
                    {tableobj}
                  </div>
                    <div className={this.state.type=="bond"?"help":"help hide"}>
                        <div className="help-title"><em className="iconfont"></em>温馨提示</div>
                        <ul>
                            <li>您受让前该笔债权的回款信息不在此处显示。</li>
                        </ul>
                    </div>
                </div>
            </div>
        )
    }  
}



ReactDOM.render(<ProjectRepayment projectId={Publiclib.getQueryString("investId")}/>,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {Publiclib.GetQueryString("type")=="bond"?"4":"3"}/>,  document.getElementById("j-sider-menu"));