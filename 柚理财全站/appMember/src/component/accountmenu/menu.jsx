import React from 'react';
import ReactDOM from 'react-dom';
import { Menu, Icon } from 'antd';
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;
export default class Accountmenu extends React.Component{

  constructor(props) {
    super(props);
    this._getData = this.getData.bind(this);
    this.state = {data: "",current: props.current};
  }
  componentDidMount(){
   this._getData();
  }
  getData(){
    let self = this;
    $.ajax({
      url: '/member/baseInfo/getUserNature.html',
      type: 'POST',
      dataType: 'json'
    })
        .done(function(data) {
          if(data.result){
            self.setState({data: data});
          } else {
            // error(data.msg);
          }
        })
        .fail(function() {
          // error("网络异常，请重试！");
        })
  }
  render() {
    let tppName = $(".tppName").val();
    let userNatrue = $(".userNatrue").val();
    // let tppNameDom = tppName!="cbhb" ? <Menu.Item key="5" className="menu-link"><a href="/member/myRealize/list.html">变现</a></Menu.Item> : <Menu.Item key="5" className="menu-link" style={{display:"none"}}><a href="/member/myRealize/list.html">变现</a></Menu.Item>;
    let html='';
    if(!this.state.data){
      return false
    }
      if(tppName=="cbhb" && userNatrue == '2'){  //如果是企业用户+渤海银行
        html=<div>
          <Menu onClick={this.handleClick}
                style={{ width: 180 }}
                defaultOpenKeys={['sub1','sub2','sub3','sub4']}
                selectedKeys={[this.state.current]}
                mode="inline"
          >
            <SubMenu key="sub1">
              <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
              <Menu.Item key="2" className="menu-link"><a href="/member/fund/list.html">资金明细</a></Menu.Item>
              <Menu.Item key="7" className="menu-link"><a href="/member/myLoan/list.html">借款列表</a></Menu.Item>
              <Menu.Item key="8" className="menu-link"><a href="/member/myRepayment/list.html">还款列表</a></Menu.Item>
              <Menu.Item key="9" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
            </SubMenu>
          </Menu>
        </div>;
      }else if(tppName=="cbhb" && userNatrue == '1'){
        if(this.state.data.projectNum <= 0){
          html=<div>
            <Menu onClick={this.handleClick}
                  style={{ width: 180 }}
                  defaultOpenKeys={['sub1','sub2','sub3','sub4']}
                  selectedKeys={[this.state.current]}
                  mode="inline"
            >
              <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
                <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
                <Menu.Item key="2" className="menu-link"><a href="/member/fund/list.html">资金明细</a></Menu.Item>
                <Menu.Item key="12" className="menu-link"><a href="/member/recharge/index.html">充值提现</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe666;</em><span>我的资产</span></span>}>
                <Menu.Item key="3" className="menu-link"><a href="/member/myInvest/list.html">我的投资</a></Menu.Item>
                <Menu.Item key="4" className="menu-link"><a href="/member/myBond/list.html">债权转让</a></Menu.Item>
                <Menu.Item key="6" className="menu-link"><a href="/member/coupon/myCoupon.html">我的优惠</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe694;</em><span>我的商城</span></span>}>
              <Menu.Item key="15" className="menu-link"><a href="/member/myScore/scoreOut.html">我的兑换</a></Menu.Item>
              <Menu.Item key="13" className="menu-link"><a href="/member/myScore/scoreIn.html">积分记录</a></Menu.Item>
              <Menu.Item key="14" className="menu-link"><a href="/member/myScore/myReceivingPage.html">收货地址</a></Menu.Item>
            </SubMenu>
              <SubMenu key="sub4" disabled title={<span className="menu-title"><em className="iconfont">&#xe668;</em><span>账户管理</span></span>}>
                <Menu.Item key="9" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
                <Menu.Item key="10" className="menu-link"><a href="/member/bankCard/list.html">银行卡</a></Menu.Item>
                <Menu.Item key="11" className="menu-link"><a href="/member/invite/index.html">好友邀请</a></Menu.Item>
              </SubMenu>
            </Menu>
          </div>;
        }else{
          html=<div>
          <Menu onClick={this.handleClick}
                style={{ width: 180 }}
                defaultOpenKeys={['sub1','sub2','sub3','sub4','sub5']}
                selectedKeys={[this.state.current]}
                mode="inline"
          >
            <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
              <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
              <Menu.Item key="2" className="menu-link"><a href="/member/fund/list.html">资金明细</a></Menu.Item>
              <Menu.Item key="12" className="menu-link"><a href="/member/recharge/index.html">充值提现</a></Menu.Item>
            </SubMenu>
            <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe666;</em><span>我的资产</span></span>}>
              <Menu.Item key="3" className="menu-link"><a href="/member/myInvest/list.html">我的投资</a></Menu.Item>
              <Menu.Item key="4" className="menu-link"><a href="/member/myBond/list.html">债权转让</a></Menu.Item>
              <Menu.Item key="6" className="menu-link"><a href="/member/coupon/myCoupon.html">我的优惠</a></Menu.Item>
            </SubMenu>
            <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe667;</em><span>我的借款</span></span>}>
              <Menu.Item key="7" className="menu-link"><a href="/member/myLoan/list.html">借款列表</a></Menu.Item>
              <Menu.Item key="8" className="menu-link"><a href="/member/myRepayment/list.html">还款列表</a></Menu.Item>
            </SubMenu>
            <SubMenu key="sub4" disabled title={<span className="menu-title"><em className="iconfont">&#xe694;</em><span>我的商城</span></span>}>
              <Menu.Item key="15" className="menu-link"><a href="/member/myScore/scoreOut.html">我的兑换</a></Menu.Item>
              <Menu.Item key="13" className="menu-link"><a href="/member/myScore/scoreIn.html">积分记录</a></Menu.Item>
              <Menu.Item key="14" className="menu-link"><a href="/member/myScore/myReceivingPage.html">收货地址</a></Menu.Item>
            </SubMenu>
            <SubMenu key="sub5" disabled title={<span className="menu-title"><em className="iconfont">&#xe668;</em><span>账户管理</span></span>}>
              <Menu.Item key="9" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
              <Menu.Item key="10" className="menu-link"><a href="/member/bankCard/list.html">银行卡</a></Menu.Item>
              <Menu.Item key="11" className="menu-link"><a href="/member/invite/index.html">好友邀请</a></Menu.Item>
            </SubMenu>
          </Menu>
        </div>;
        }
      }else{
          if(this.state.data.projectNum <= 0){
            html=<div>
              <Menu onClick={this.handleClick}
                    style={{ width: 180 }}
                    defaultOpenKeys={['sub1','sub2','sub3','sub4']}
                    selectedKeys={[this.state.current]}
                    mode="inline"
              >
                <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
                  <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
                  <Menu.Item key="2" className="menu-link"><a href="/member/fund/list.html">资金明细</a></Menu.Item>
                  <Menu.Item key="12" className="menu-link"><a href="/member/recharge/index.html">充值提现</a></Menu.Item>
                </SubMenu>
                <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe666;</em><span>我的资产</span></span>}>
                  <Menu.Item key="3" className="menu-link"><a href="/member/myInvest/list.html">我的投资</a></Menu.Item>
                  <Menu.Item key="4" className="menu-link"><a href="/member/myBond/list.html">债权转让</a></Menu.Item>
                  {/*<Menu.Item key="5" className="menu-link"><a href="/member/myRealize/list.html">变现</a></Menu.Item>*/}
                  <Menu.Item key="6" className="menu-link"><a href="/member/coupon/myCoupon.html">我的优惠</a></Menu.Item>
                </SubMenu>
                <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe694;</em><span>我的商城</span></span>}>
                <Menu.Item key="15" className="menu-link"><a href="/member/myScore/scoreOut.html">我的兑换</a></Menu.Item>
                <Menu.Item key="13" className="menu-link"><a href="/member/myScore/scoreIn.html">积分记录</a></Menu.Item>
                <Menu.Item key="14" className="menu-link"><a href="/member/myScore/myReceivingPage.html">收货地址</a></Menu.Item>
              </SubMenu>
                <SubMenu key="sub4" disabled title={<span className="menu-title"><em className="iconfont">&#xe668;</em><span>账户管理</span></span>}>
                  <Menu.Item key="9" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
                  <Menu.Item key="16" className="menu-link"><a href="/member/credit/index.html">申请额度</a></Menu.Item>
                  {/*<Menu.Item key="10" className="menu-link"><a href="/member/bankCard/list.html">银行卡</a></Menu.Item>*/}
                  <Menu.Item key="11" className="menu-link"><a href="/member/invite/index.html">好友邀请</a></Menu.Item>
                </SubMenu>
              </Menu>
            </div>;
          }else{
          html=<div>
            <Menu onClick={this.handleClick}
                  style={{ width: 180 }}
                  defaultOpenKeys={['sub1','sub2','sub3','sub4','sub5']}
                  selectedKeys={[this.state.current]}
                  mode="inline"
            >
              <SubMenu key="sub1" disabled title={<span className="menu-title"><em className="iconfont">&#xe61b;</em><span>我的账户</span></span>}>
                <Menu.Item key="1" className="menu-link"><a href="/member/account/main.html">账户概览</a></Menu.Item>
                <Menu.Item key="2" className="menu-link"><a href="/member/fund/list.html">资金明细</a></Menu.Item>
                <Menu.Item key="12" className="menu-link"><a href="/member/recharge/index.html">充值提现</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub2" disabled title={<span className="menu-title"><em className="iconfont">&#xe666;</em><span>我的资产</span></span>}>
                <Menu.Item key="3" className="menu-link"><a href="/member/myInvest/list.html">我的投资</a></Menu.Item>
                <Menu.Item key="4" className="menu-link"><a href="/member/myBond/list.html">债权转让</a></Menu.Item>
               {/* <Menu.Item key="5" className="menu-link"><a href="/member/myRealize/list.html">变现</a></Menu.Item>*/}
                <Menu.Item key="6" className="menu-link"><a href="/member/coupon/myCoupon.html">我的优惠</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub3" disabled title={<span className="menu-title"><em className="iconfont">&#xe667;</em><span>我的借款</span></span>}>
                <Menu.Item key="7" className="menu-link"><a href="/member/myLoan/list.html">借款列表</a></Menu.Item>
                <Menu.Item key="8" className="menu-link"><a href="/member/myRepayment/list.html">还款列表</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub4" disabled title={<span className="menu-title"><em className="iconfont">&#xe694;</em><span>我的商城</span></span>}>
                <Menu.Item key="15" className="menu-link"><a href="/member/myScore/scoreOut.html">我的兑换</a></Menu.Item>
                <Menu.Item key="13" className="menu-link"><a href="/member/myScore/scoreIn.html">积分记录</a></Menu.Item>
                <Menu.Item key="14" className="menu-link"><a href="/member/myScore/myReceivingPage.html">收货地址</a></Menu.Item>
              </SubMenu>
              <SubMenu key="sub5" disabled title={<span className="menu-title"><em className="iconfont">&#xe668;</em><span>账户管理</span></span>}>
                <Menu.Item key="9" className="menu-link"><a href="/member/baseInfo/index.html">基本信息</a></Menu.Item>
                <Menu.Item key="16" className="menu-link"><a href="/member/credit/index.html">申请额度</a></Menu.Item>
                {/*<Menu.Item key="10" className="menu-link"><a href="/member/bankCard/list.html">银行卡</a></Menu.Item>*/}
                <Menu.Item key="11" className="menu-link"><a href="/member/invite/index.html">好友邀请</a></Menu.Item>
              </SubMenu>
            </Menu>
          </div>;
          }
      }
    return (
        <div>
            {html}
        </div>

    );
  }
}
