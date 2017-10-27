import React from 'react';
import ReactDOM from 'react-dom';
import { Menu, Icon } from 'antd';
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
export default class AccountVouchMenu extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      current: props.current
    };
  }
  render() {
    let tppName = $(".tppName").val();
    let html='';
    if(tppName=="cbhb"){
      html=<div>
        <Menu onClick={this.handleClick}
          style={{ width: 180 }}
          defaultOpenKeys={['sub1','sub2','sub3','sub4']}
          selectedKeys={[this.state.current]}
          mode="inline"
        >
          <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
          <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
          <Menu.Item key="2" className="menu-link"><a href="/member/fund/vouchList.html">资金明细</a></Menu.Item>
          </SubMenu>
          <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe60c;</em><span>担保管理</span></span>}>
            <Menu.Item key="4" className="menu-link"><a href="/member/vouch/verify.html">审核项目</a></Menu.Item>
            <Menu.Item key="5" className="menu-link"><a href="/member/vouch/vouchProject.html">担保项目</a></Menu.Item>
            <Menu.Item key="6" className="menu-link"><a href="/member/vouch/overdue.html">逾期记录</a></Menu.Item>
          </SubMenu>
          <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe61a;</em><span>账户管理</span></span>}>
            <Menu.Item key="7" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
          </SubMenu>
        </Menu>
      </div>
    }else{
      html=<div>
        <Menu onClick={this.handleClick}
          style={{ width: 180 }}
          defaultOpenKeys={['sub1','sub2','sub3','sub4']}
          selectedKeys={[this.state.current]}
          mode="inline"
        >
          <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
          <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
          <Menu.Item key="2" className="menu-link"><a href="/member/fund/vouchList.html">资金明细</a></Menu.Item>
          <Menu.Item key="3" className="menu-link"><a href="/member/recharge/vouchIndex.html">充值提现</a></Menu.Item>
          </SubMenu>
          <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe60c;</em><span>担保管理</span></span>}>
            <Menu.Item key="4" className="menu-link"><a href="/member/vouch/verify.html">审核项目</a></Menu.Item>
            <Menu.Item key="5" className="menu-link"><a href="/member/vouch/vouchProject.html">担保项目</a></Menu.Item>
            <Menu.Item key="6" className="menu-link"><a href="/member/vouch/overdue.html">逾期记录</a></Menu.Item>
          </SubMenu>
          <SubMenu key="sub4" disabled title={<span className="menu-title"><em className="iconfont">&#xe694;</em><span>我的商城</span></span>}>
            <Menu.Item key="15" className="menu-link"><a href="/member/myScore/scoreOut.html">我的兑换</a></Menu.Item>
            <Menu.Item key="13" className="menu-link"><a href="/member/myScore/scoreIn.html">积分记录</a></Menu.Item>
            <Menu.Item key="14" className="menu-link"><a href="/member/myScore/myReceivingPage.html">收货地址</a></Menu.Item>
          </SubMenu>
          <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe61a;</em><span>账户管理</span></span>}>
            <Menu.Item key="7" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
            <Menu.Item key="8" className="menu-link"><a href="/member/bankCard/vouchBank.html">对公账户</a></Menu.Item>
          </SubMenu>
        </Menu>
      </div>
    }
    return (
      <div>
          {html}
      </div>
    );
  }
}
