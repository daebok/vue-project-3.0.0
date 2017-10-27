<template>
  <div >
    <transition :name="transitionName">
      <router-view ></router-view>
    </transition>
    <rd-footer v-if="footerShow"></rd-footer>
  </div>
</template>

<script>
  import RdFooter from './views/footer.vue'
  import { } from './ajax.config'
  export default {
    data(){
      return {
        transitionName: 'slide-right',
        showPath: ['index', 'invest', 'account', 'mine'], // 需要显示底部footer的页面地址
        footerShow: false
      }
    },
    watch: {
      '$route' (to, from) {
        const toDepth = to.path.split('/').length
        const fromDepth = from.path.split('/').length
        let to_flag = 0;
        let from_flag = 0;
        if (this.showPath.indexOf(to.name) >= 0) {
          // 根据不同页面需要，隐藏显示底部footer
          this.footerShow = true;
          to_flag = 1
        }else{
          this.footerShow = false;
        }
        if (this.showPath.indexOf(from.name) >= 0) {
          from_flag = 1
        }
        if(navigator.userAgent.match(/OS [0-9]_[0-2]/i)){
          // ios 版本小于9.2以下
          this.transitionName = 'fade'
        }
        else if(to_flag * from_flag){
          this.transitionName = 'fade'
        }
        else{
          // console.log(toDepth, fromDepth)
          this.transitionName = toDepth < fromDepth ? 'slide-left' : 'slide-right'
        }
        sessionStorage.toName = to.name
        sessionStorage.fromName = from.name
      }
    },
    components: { RdFooter }
  }
</script>

<style lang="sass" rel="stylesheet/sass">
  @import './assets/css/app.css'
  @import './assets/css/animate.min.css';
  /*引入使用的变量*/
  @import "./assets/scss/var.scss"

  html, body
    overflow: hidden
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

  .content
    margin-bottom: 50px
    position: absolute
    top: 0
    right: 0
    bottom: 0
    left: 0
    z-index: 2
    overflow: auto
    /*transform: translate3d(0, 0, 0)*/
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
    -webkit-overflow-scrolling: touch

  .mint-spinner
    display: inline-block
    vertical-align: middle

  .mint-loadmore-bottom span
    display: inline-block
    transition: .2s linear
    vertical-align: middle

  .mint-loadmore-bottom span.rotate
    transform: rotate(180deg)

</style>
