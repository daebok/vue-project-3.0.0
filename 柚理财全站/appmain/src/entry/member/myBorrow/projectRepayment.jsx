import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Table,Modal} from 'antd';
import Accountmenu from '../../../component/accountmenu/menu';
import './projectRepayment.less';
import {Publiclib} from '../../../common/common';


class ProjectRepayment extends React.Component{
  constructor(props){
    super(props);
    this.state = {
      projectName:"",
      repaymentList : [],
    }
    this.getinfo = this.getinfo.bind(this);
  }
  getinfo(){
    let self = this;
    $.ajax({
      url: '/member/myLoan/getProjectRepaymentList.html?projectId=' + self.props.projectId,
      type: 'POST',
      dataType: 'json'     
    })
    .done(function(data) {
      $.each(data.data.rows,function(index, el) {
        el.key = index;
      });
      self.setState({
        projectName: data.projectName,
        repaymentList: data.data.rows,
      });
    })
    .fail(function() {     
      console.log("error");
    })
  }
  componentDidMount() {  
    this.getinfo();  
  }
  render(){
      const statusType =['未还','已还'];
      const columns = [
          {
            title: '期数',
            dataIndex: 'period',
            render(text,record) {
              return text+"/"+record.periods;
            }
          },
          {
            title: '本期还款日期',
            dataIndex: 'repayTime',
            render(text) {
              return Publiclib.formatDate(text);
            }             
          },
          {
            title: '应还款金额(元)',
            dataIndex: 'payment',
          },
          {
            title: '类型',
            dataIndex: 'repaymentTypeStr'             
          },
          {
            title: '实际还款日',
            dataIndex: 'realRepayTimeStr'
          },
           {
              title:'实际还款金额',
              dataIndex:'payedAmountStr'                
          },{
              title:'状态',
              dataIndex:'status',
              render(text,record) {
                return statusType[text];
              }              
            }   
          ];
      let tableobj = <Table columns={columns} dataSource={this.state.repaymentList} pagination={false} />;
      if(!this.state.repaymentList.length){
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



ReactDOM.render(<ProjectRepayment projectId={Publiclib.getQueryString("projectId")}/>,  document.getElementById("j-mybond-list"));

ReactDOM.render(<Accountmenu current = {"7"}/>,  document.getElementById("j-sider-menu"));