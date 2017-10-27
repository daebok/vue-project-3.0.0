import React from 'react';
import ReactDOM from 'react-dom';
import { Modal } from "antd";
import Tabs from '../../../component/tablelist/tablelist';
import {Publiclib} from '../../../common/common';
import { Tooltip } from 'antd';

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

const columns_second = [
          {
            title: '编号',
            dataIndex: 'bh'
          },
          {
            title: '内容',
            dataIndex: 'content'
          },
          {
            title: '投诉时间',
            dataIndex: 'createTime1',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },
          {
            title: '投诉时间',
            dataIndex: 'createTime2',
            render(text) {
              return Publiclib.formatDate(text,2);
            },
          },{
            title: '更多',
            render: (text, record) => { 
                  return "查看详情";
              },
          }];

const config = {single:true,record:true,datetime:true,select:false};
// const select = {url:"/public/getSelectList.html?dictType=projectStatus"};

export default class Data9Info extends React.Component{
	constructor(props) {
	    super(props);
  	}
	componentDidMount(){
	}
	
	render() {
		return (
			<div>
				<div className="data9Info clearfix">
          <div className="tableTitle">投诉信息</div>
          <div className="search clearfix"></div>
					<Tabs config={config} url='/member/myScore/getScoreLogs.html' tableid="tablelist-second" columns={columns_second}/>
				</div>
			</div>
		);
	}
}

