import Vue from 'vue'
import axios from 'axios'

import MintUI from 'mint-ui' // 引入第三方UI库
import './assets/css/mint-ui.css' // 引入mint-ui样式库
import router from './routers' // 路由配置
import store from './store' // store集成管理，用户管理登录用户信息
import filters from './filters'
import './libs/px2rem'  // 设置1rem为100px
import App from './App.vue'  // 初始化组件注入
import FastClick from 'fastclick'

Vue.use(MintUI)

// 设置axios的数据请求方式
Vue.prototype.$http = axios
// post全局配置
// axios.defaults.withCredentials = true; // 开启接收header中的cookie
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// axios.interceptors.request.use(function (config) {
//   if(config.url.indexOf('/app/member') > 0) {
//     config.params.__sid = store.state.user.__sid
//   }
//   return config;
// }, function (error) {
//   return Promise.reject(error);
// });
axios.interceptors.response.use(function (res) {
  let code = res.data.resCode;
  /*0x5001账户被锁定，需要退出；0x5002 充值冻结； 0x5003 提现冻结；0x5004 投资功能被冻结；0x5005 变现功能被冻结；0x5006 债权功能被冻结； 0x5007 借款功能被冻结
   */
  switch (code) { // 去掉判断账户被锁定，已在登录页做判断
    case 0x5002:case 0x5003:case 0x5004:case 0x5005:case 0x5006:case 0x5007:
    Vue.$messagebox({
      title: ' ',
      confirmButtonText: '确认',
      showCancelButton: false,
      message: res.data.resMsg
    }).then(action => {
      if(action == 'confirm'){
        history.back();
      }
    });
    break;
    case 32771:  // 状态码为请先登录后再尝试
      Vue.$toast('请先登录再尝试')
      store.dispatch('logout')
      router.push({path: '/login', query: { redirect: window.location.pathname }})
      // window.location.reload() // 自动刷新跳登录页面
      delete localStorage.user
      break;
  }
  return res;
}, function (error) {
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

// 登录验证，页面需要登录而没有登录的情况直接跳转登录
// router.beforeEach((to, from, next) => {
//   document.title = to.meta.title || document.title;
//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     // if (store.state.user) {
//     if (localStorage.user) {
//       next();
//     } else {
//       next({
//         path: '/login',
//         query: { redirect: to.fullPath }
//       });
//     }
//   } else {
//     next();
//   }
// });

// 登录验证，页面需要登录而没有登录的情况直接跳转登录---app商城免登陆
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || document.title; // 修改页面head的内容跟随路由变化
  if (to.matched.some(record => record.meta.requiresAuth)) {
    let __sid = getQuery('__sid');
    let userId = getQuery('userId');
    //console.log(__sid);
    //console.log(userId);
    //console.log(window.localStorage.user)
    if (window.localStorage.user || __sid) {
      if (__sid) {
        let user = {
          __sid: __sid,
          userId: userId
        };
        // 存储user信息到本地
        localStorage.user = JSON.stringify(user);
        store.dispatch('login', user);
      }
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

// 获取url参数
function getQuery(name) {
  var url = window.location.href;
  var params = url.substring(url.indexOf('?'));
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
  var r = params.substr(1).match(reg);
  if (r !== null) return unescape(r[2]); return null;
}

// 监听浏览器返回事件
window.addEventListener('popstate', () => {
  // 页面切换时候键盘消失
  document.activeElement.blur(); // 路由切换时候退出键盘
  Vue.$messagebox.close(); // 路由切换时关闭mint-ui的messagebox提示框
}, false)

// 监听dom被插入
document.addEventListener("DOMNodeInserted", (e) => {
  mintToastRemove();
});

// 控制mint-ui的toast同时只显示一个
function mintToastRemove() {
  var domList = document.getElementsByClassName('mint-toast');
  var length = document.getElementsByClassName('mint-toast').length;
  if (length > 1) {
    for (var i = 0; i < length; i++) {
      if (i != length) {
        if (domList[i]) {
          domList[i].remove();
        }
      }
    }
  }
}
