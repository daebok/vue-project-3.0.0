import Vue from 'vue'
import axios from 'axios'
import Mint from 'mint-ui' // 引入第三方UI库
import './assets/css/mint-ui.css' // 引入mint-ui样式库
import router from './router' // 路由配置
import store from './store'
import filters from './filters'
import './libs/px2rem'  // 设置1rem为100px
import App from './App.vue'  // 初始化组件注入
import FastClick from 'fastclick'
import * as ajaxUrl from './ajax.config.js';
// import scrollRecord from './directives/scrollRecord'
import scrollRecord from './directives/scrollRecord'
Vue.use(Mint)
Vue.directive('scroll-record', scrollRecord)
Vue.config.productionTip = false
// 设置axios的数据请求方式
Vue.prototype.$http = axios
// post全局配置
// axios.defaults.headers.common['Cookie'] = 'micromessenger'
//axios.defaults.withCredentials = true; // 开启接收header中的cookie
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// 在发送请求之前做些什么
axios.interceptors.request.use(function (config) {
  //Vue.$indicator.open({ spinnerType: 'fading-circle' });
  return config;
});
// Add a response interceptor
axios.interceptors.response.use(function (res) {
  Vue.$indicator.close();
  let code = res.data.resCode;
  //let url = window.location.href;
  /*0x5001账户被锁定，需要退出；0x5002 充值冻结； 0x5003 提现冻结；0x5004 投资功能被冻结；0x5005 变现功能被冻结；0x5006 债权功能被冻结； 0x5007 借款功能被冻结
   */
  switch (code) {
    case 0x5001:case 0x5002:case 0x5003:case 0x5004:case 0x5005:case 0x5006:case 0x5007:
    Vue.$messagebox({
      title: ' ',
      confirmButtonText: '联系客服',
      showCancelButton: true,
      message: res.data.resMsg
    }).then(action => {
      if(action == 'confirm'){
        window.location.href = 'tel:10086'
      }
    });
    break;
    case 32771:  // 状态码为请先登录后再尝试
      let url = window.location.hash;
      //console.log(url);
      //Vue.$toast('请先登录再尝试')
      store.dispatch('logout')
      //window.location.reload() // 自动刷新跳登录页面
      delete localStorage.user
      Vue.$messagebox({
        title: ' ',
        confirmButtonText: '去登录',
        showCancelButton: false,
        message: '请先登录再尝试'
      }).then(res => {
        if(url.indexOf('myInvest') >-1){
          window.location.href = 'http://weixin.360mili.com/usercenter';//跳转永鑫个人中心
        }else{
          //window.location.href = 'http://weixin-test.360mili.com/shoppingmall/index';//跳转永鑫测试首页
          window.location.href = 'http://weixin.360mili.com/shoppingmall';//跳转永鑫正式首页
        }
      });
      break;
  }
  return res;
}, function (error) {
  // Do something with response error
  Vue.$toast('网络异常')
  return Promise.reject(error);
});
// 实例化Vue的filter
Object.keys(filters).forEach(k => Vue.filter(k, filters[k]));
//注册全局指令
Vue.directive('back-link', {
  bind: function (el) {
    el.addEventListener('click', function () {
      window.history.back()
    })
  }
})
// 页面返回记录保存
document.addEventListener('DOMContentLoaded', function() {
  if (window.FastClick) window.FastClick.attach(document.body);
}, false);
if (/Android/gi.test(navigator.userAgent)) {
  window.addEventListener('resize', function () {
    if (document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA') {
      window.setTimeout(function () {
        document.activeElement.scrollIntoViewIfNeeded();
      }, 0);
    }
  })
}
//注册键盘事件
document.onkeydown = function(e) {
  //捕捉回车事件
  var ev = (typeof event!= 'undefined') ? window.event : e;
  // console.dir(ev)
  if(ev.keyCode == 13) {
    return false
  }
}
// 处理刷新的时候vuex被清空但是用户已经登录的情况
if (localStorage.user) {
  store.dispatch('login', JSON.parse(localStorage.user));
}

const vm = new Vue({
  store,
  router,
  template: '<App/>',
  components: { App }
}).$mount('#app')

//通过url获取id值
function dealURl(name) {
  var after = window.location.hash.split("?")[1];
  if (after) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = after.match(reg);
    if (r != null) {
      return decodeURIComponent(r[2]);
    }
  } else {
    return "";
  }
}

// 登录验证，页面需要登录而没有登录的情况直接跳转登录
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || document.title; // 修改页面head的内容跟随路由变化
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (localStorage.user) {
      next();
    } else {
     next({
      name: 'login',
      query: { redirect: to.fullPath }
     });
    }
  } else {
    next();
  }
});

// 监听浏览器返回事件
window.addEventListener('popstate', () => {
  // 页面切换时候键盘消失
  document.activeElement.blur()
  Vue.$messagebox.close(); //路由切换时解决弹框仍然存在的问题
}, false)
