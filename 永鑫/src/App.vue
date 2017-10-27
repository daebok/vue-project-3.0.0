<template>
  <div >
    <transition :name="transitionName">
      <router-view keep-alive></router-view>
    </transition>
    <!-- <img v-if="$route.path !='/' && $route.path !='/index' && $route.path !='/invest' && $route.path !='/account' && $route.path !='/gayInvite'" class="toIndex" @click="toIndex" src="./assets/images/index/toIndex.png"> -->
    <!-- <rd-footer v-if="$route.path =='/' || $route.path =='/index' || $route.path =='/invest' || $route.path =='/account'"></rd-footer> -->
  </div>
</template>

<script>
  import RdFooter from './views/footer.vue'
  export default {
    data(){
      return {
        transitionName: 'slide-right'
      }
    },
    created() {
      // 联合登录
      if (this.dealURl('resultCode') == 1) {
        let user = {
          userName: this.dealURl('mobile'),
          loginName: this.dealURl('username'),
          userId: this.dealURl('userId'),
          avatarPhoto: this.dealURl('avatarPhoto'),
          expiresIn: this.dealURl('expiresIn'),
          __sid: this.dealURl('_sid')
        };
        // 登入后先清除之前操作留下来的所有缓存
        Object.keys(sessionStorage).forEach((k) => {
          sessionStorage.removeItem(k)
        });
        // 存储user信息到本地
        localStorage.setItem('user', JSON.stringify(user));
        this.$store.dispatch('login', user);
      } else if (this.dealURl('resultCode') == 2) {
        this.$toast('您的账户已被冻结，请联系客服');
      }
    },
    watch: {
      $route (to, from) {
        const toDepth = to.path.split('/').length
        const fromDepth = from.path.split('/').length
        const toPath = to.path;
        const fromPath = from.path;
        let toPath_flag = 0;
        let fromPath_flag = 0;
        if(toPath == '/' || toPath == '/invest' || toPath == '/account'){
          toPath_flag = 1
        }
        if(fromPath == '/' || fromPath == '/invest' || fromPath == '/account'){
          fromPath_flag = 1
        }
        if(navigator.userAgent.match(/OS [0-9]_[0-2]/i)){
          // ios 版本小于9.2以下
          this.transitionName = 'fade'
        }
        else if(toPath_flag * fromPath_flag){
          this.transitionName = 'fade'
        }
        else{
          // console.log(toDepth, fromDepth)
          this.transitionName = toDepth < fromDepth ? 'slide-left' : 'slide-right'
        }
        sessionStorage.toPath = toPath
        sessionStorage.fromPath = fromPath
        sessionStorage.toName = to.name
        sessionStorage.fromName = from.name
      }
    },
    components: { RdFooter },
    methods: {
      toIndex() {
        this.$router.push({ name: 'Index2' });
      },
      //通过url获取id值
      dealURl(name) {
        let after = window.location.hash.split("?")[1];
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
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass">
  @import './assets/css/app.css'
  /*引入使用的变量*/
  @import "./assets/scss/var.scss"

  html, body
    -webkit-overflow-scrolling: touch
    -webkit-font-smoothing: antialiased
    -moz-osx-font-smoothing: grayscale
    user-select: none
    background: $page-bg
    font-family: 'PingFang SC', 'STHeitiSC-Light', 'Helvetica-Light', arial, sans-serif
  .page
    position: absolute
    top: 0
    left: 0
    width: 100%
    height: 100%
    box-sizing: border-box
    z-index: 1
    background: $page-bg
    transform: translate3d(0, 0, 0)
    -webkit-overflow-scrolling: touch

  .content
    margin-bottom: 50px
    position: absolute
    top: 0
    right: 0
    bottom: 0
    left: 0
    z-index: 2
    overflow: auto
    transform: translate3d(0, 0, 0)
    -webkit-overflow-scrolling: touch

  .page-back
    display: inline-block
    position: absolute
    left: 12px
    top: 10px
    width: 40px
    height: 40px
    text-align: center
    i
      font-size: 24px
      line-height: 40px
  .mint-tabbar > .mint-tab-item.is-selected
    background-color: #fff
    color: $main-color

  .fade-enter-active, .fade-leave-active
    transition: all .2s ease
  .fade-enter, .fade-leave-active
    opacity: 0
  .slide-right-enter-active,.slide-left-enter-active,.slide-right-leave-active,.slide-left-leave-active
    transition: all .35s linear
  .slide-right-enter, .slide-left-leave-active
    transform: translate3d(100%, 0, 0)
    opacity: 0
  .slide-left-enter, .slide-right-leave-active
    transform: translate3d(-100%, 0, 0)

  /*加载更多组件的样式*/
  .page-loadmore-wrapper
    overflow: scroll

  .mint-spinner
    display: inline-block
    vertical-align: middle

  .mint-loadmore-bottom span
    display: inline-block
    transition: .2s linear
    vertical-align: middle

  .mint-loadmore-bottom span.rotate
    transform: rotate(180deg)

  .toIndex
    position: fixed;
    width: 0.4rem;
    height: 0.4rem;
    left: 0;
    bottom: 0.9rem;
    z-index: 1;
</style>
