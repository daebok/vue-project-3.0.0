import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from 'axios'
import { sync } from 'vuex-router-sync'
import MintUI from 'mint-ui' // 引入第三方UI库
import './assets/css/mint-ui.css' // 引入mint-ui样式库
import routes from './routers' // 路由配置
import store from './store'
import filters from './filters'
import { scrollRecord } from 'route-data'
import './libs/px2rem'  // 设置1rem为100px
import App from './App.vue'  // 初始化组件注入
import FastClick from 'fastclick'
Vue.use(VueRouter)
Vue.use(MintUI)

// 设置axios的数据请求方式
Vue.prototype.$http = axios
// post全局配置
// axios.defaults.headers.common['Cookie'] = 'micromessenger'
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
// Add a response interceptor
axios.interceptors.response.use(function (res) {
  let code = res.data.resCode;
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
      // console.log(code)
      Vue.$toast('请先登录再尝试')
      store.dispatch('login', '')
      delete localStorage.user
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
Vue.directive('scroll-record', scrollRecord)
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
// - only available in html5 history mode
const scrollBehavior = (to, from, savedPosition) => {
  if (savedPosition) {
    // savedPosition is only available for popstate navigations.
    return savedPosition
  } else {
    const position = {}
    // new navigation.
    // scroll to anchor by returning the selector
    if (to.hash) {
      position.selector = to.hash
    }
    // check if any matched route config has meta that requires scrolling to top
    if (to.matched.some(m => m.meta.scrollToTop)) {
      position.x = 0
      position.y = 0
    }
    // if the returned position is falsy or an empty object,
    // will retain current scroll position.
    return position
  }
}
const router = new VueRouter({
  mode: 'history',
  base: __dirname,
  scrollBehavior,
  routes
})

sync(store, router)

const vm = new Vue({
  store,
  router,
  template: '<App/>',
  components: { App }
}).$mount('#app')

// 登录验证，页面需要登录而没有登录的情况直接跳转登录
router.beforeEach((to, from, next) => {
  document.title = to.meta.title || document.title;
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // if (store.state.user) {
    if (localStorage.user) {
      next();
    } else {
      next({
        path: '/login',
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
