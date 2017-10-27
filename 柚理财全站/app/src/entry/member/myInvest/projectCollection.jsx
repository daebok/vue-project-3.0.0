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
      console.log(data);
      self.setState({
        projectName: data.projectName,
        repaymentList: data.data,
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
            title: '类型',
            dataIndex: 'interest',
            render(text,record) {
              let str = "";
              if( text == record.payment){
                str = "利息";
              }
              else{
                str = "本金+利息";
              }
              return str;
            },             
          },
          {
            title: '实际回款日期',
            dataIndex: 'lastRepayTimeStr',
            render(text,record) {
                  return text;
              }
            },
            {
              title:'实际回款金额',
              dataIndex:'repayedAmountStr',
              render(text,record) {
                  return text
              },
            },{
              title:'状态',
              dataIndex:'status',
              render(text,record) {
                return statusType[text];
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
      return (
        <div className="projectRepayment">
          <div className="project">借款产品<span className="name">{this.state.projectName}</span></div>
          {tableobj}
        </div>
        )
    }  
}



ReactDOM.render(<ProjectRepayment projectId={Publiclib.getQueryString("investId")}/>,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"3"}/>,  document.getElementById("j-sider-menu"));