import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import Accountmenu from '../../../component/accountmenu/menu';
import './vip.less';

const VIPLevel = ['<em class="iconfont">&#xe669;</em>','<em class="iconfont">&#xe63b;</em>','<em class="iconfont">&#xe641;</em>','<em class="iconfont">&#xe642;</em>','<em class="iconfont">&#xe640;</em>','<em class="iconfont">&#xe63c;</em>','<em class="iconfont">&#xe63d;</em>','<em class="iconfont">&#xe63e;</em>','<em class="iconfont">&#xe63f;</em>','<em class="iconfont">&#xe643;</em>'];
const vipicon = VIPLevel[$("#j-vip").data("vip")];

$("#j-vip").html(vipicon);

ReactDOM.render(<Accountmenu current = {"1"}/>,  document.getElementById("j-sider-menu"));