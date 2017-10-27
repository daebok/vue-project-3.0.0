import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../component/accountmenu/menu';
import Tablelist from '../../component/tablelist/tablelist';
import './list.less';
import {Tabs} from 'antd';
const TabPane = Tabs.TabPane;

function callback(key) {
  console.log(key);
}

let Investlist = React.createClass({
  render(){
    return (
          <div className="account-main">
            <div className="main-title">我的投资</div>
            <div className="list tabs-meun">
              <Tabs defaultActiveKey="1" onChange={callback}>
                <TabPane tab="持有中" key="1">
                  <Tablelist  />
                </TabPane>
                <TabPane tab="投资申请" key="2">
                  <Tablelist />
                </TabPane>
                <TabPane tab="赎回申请" key="3">
                  <Tablelist />
                </TabPane>
                <TabPane tab="已结束" key="4">
                  <Tablelist />
                </TabPane>
              </Tabs>
            </div>
          </div>  
      );
  }
});
ReactDOM.render(<Investlist />,  document.getElementById("j-investlistmain"));
ReactDOM.render(<Accountmenu current = {"3"}/>,  document.getElementById("j-sider-menu"));