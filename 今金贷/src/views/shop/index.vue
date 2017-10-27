<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .mint-swipe {
    height: 1.64rem;
    color: #fff;
    font-size: 30px;
    text-align: center;
  }
  .mint-swipe-item {
    line-height: 1.64rem;
    background:#ccc center no-repeat;
    background-size: contain;
    display:block;
    height:100%;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .score-info {
    background: #fff; padding: .15rem; display: table; text-align: center;width: 100%;
    li {
      display: table-cell; color: #666; font-size: .16rem; width: 50%;
      img { width: .2rem; vertical-align: -3px; margin-right: .08rem;}
      span { color: $main-color;}
    }
  }
  .news {
    padding: .1rem .12rem; display: table; width: 100%;background: #fff;
    .left {
      padding-left: .09rem;
      img { width: .7rem; }
    }
    .cell {
      display: table-cell; vertical-align: middle; height: .4rem; background: #f9f9f9;overflow: hidden;
    }
    .right {
      padding-left: .1rem; font-size: .12rem;
      .scroll-wrap {
        height: 20px; overflow: hidden;
      }
      ul {
        position: relative;
        li {
          /*display: table; */
          width: 2.6rem;
          span {display: table-cell; vertical-align: middle;}
          .head-ico {
            img { width: 20px; height: 20px; display: block; border-radius: 50%; }
          }
        }
      }

    }
  }
  .game {
    margin-top: .1rem;
    img { width: 100%; }
  }
  .wrapper-scroll {
    height: .5rem;
    position:relative;
  }
  .wrapper {
    position: absolute;
    z-index: 1;
    top: 0;
    left: 0;
    width: 100%;
    background: #ccc;
    overflow: hidden;
    height: 100%;
  }
  .scroller {
    position: absolute;
    z-index: 1;
    min-width: 400px;
    height: 100%;
    background-color: #a00;
    text-size-adjust: none;
    background:#fff;
  }
  .aui-flex-item-3 {
    &.current p {
      color: $main-color;border-bottom: 1px solid $main-color;
      span { color: $main-color; }
    }
    p {
      color: #333;
      line-height:.2rem;
      padding: .15rem .05rem .12rem;
      margin: 0 .18rem 0 .06rem;
      span { font-size: .15rem; color: #ddd; }
    }
  }
  .bg-fff { background: #fff; }
  .product {
    padding-top: .2rem;
    margin-bottom: .1rem;
    h2 {
      color: #333;font-size: .17rem; line-height: 1;
      img { width: .2rem; vertical-align: -3px; margin-right: .03rem;}
      span {
        display: inline-block; width: .2rem; height: .2rem; background-size:.2rem auto;background-repeat: no-repeat; vertical-align: -3px; margin-right: .03rem;
        &.f-2 { background-image: url(../../assets/images/shop/2F.png)}
        &.f-3 { background-image: url(../../assets/images/shop/3F.png)}
        &.f-4 { background-image: url(../../assets/images/shop/4F.png)}
        &.f-5 { background-image: url(../../assets/images/shop/5F.png)}
      }
    }
    h5 { font-size: .13rem; line-height: 1;margin-top: .04rem; }
  }
  #floor1 {
    ul {
      padding: .22rem .1rem .28rem;
      li {
        width: 1rem;margin-right: .2rem;vertical-align: top;
        &:last-child { margin-right: 0; }
        display: inline-block;
        img {
          width: 1rem; height: .67rem;display: block;
        }
        p { color: #333; font-size: .12rem; padding-left: .03rem;}
      }
    }
  }
  .common-area {
    ul {
      padding: .22rem .15rem 0; font-size: 0;
    }
  }
  .more { line-height: .45rem; }
  .con {
    z-index: 2;
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    overflow: auto;
    &.top-50 { top: .5rem; }
  }
</style>
<template>
  <div class="page" ref="page">
    <h-scroll v-show="topShow" ref="topScroller" @menuItem="menuItem" :active="active" :id="'top'" :list="goodsCategoryList"></h-scroll>
    <div class="con" :class="{'top-50': topShow }" @scroll="onScroll" @touchmove.stop="onTouchMove">
      <mt-loadmore :top-method="loadTop" ref="loadmore">
      <!-- S首页banner图 -->
      <div id="banner">
        <mt-swipe :auto="3000">
          <mt-swipe-item v-if="bannerList && bannerList.length === 0">
            <img src="../../assets/images/banner_default.png">
          </mt-swipe-item>
          <mt-swipe-item v-else v-for="banner in bannerList" :key="banner.id">
            <a :href="banner.url">
              <img :src="banner.picPath">
            </a>
          </mt-swipe-item>
        </mt-swipe>
      </div>
      <!-- E首页banner图 -->
      <ul class="score-info">
        <li class="aui-border-r">
          <router-link to="/shop/myScore">
          <img src="../../assets/images/shop/coin.png">积分 <span v-if="scoreData && scoreData.useScore">{{scoreData.useScore && scoreData.useScore | currency('', 0)}}</span>
          </router-link>
        </li>
        <li>
          <router-link to="/shop/myScore/change">
          <img src="../../assets/images/shop/order.png">我的兑换
          </router-link>
        </li>
      </ul>
      <div class="news">
        <div class="left cell"><img src="../../assets/images/shop/newest_exchange.png" /></div>
        <div class="right cell" >
          <div class="scroll-wrap" ref="scrollWrap">
          <ul :style="{ top, transition }">
            <!--<ul>-->
            <li class="ellipsis" v-for="item in noticeList" >
              <span class="head-ico"><img :src="assetsDomain+item.avatarPhoto"></span>
              <span class="color-666" v-if="item.receivePhone"> {{item.receivePhone.substring(0,3)}}****{{item.receivePhone.substring(7,11)}}</span>
              <span class="color-666" v-else> {{item.userName.substring(0,4)}}****{{item.userName.substring(7,11)}}</span>
              <span class="color-999">成功兑换了</span>
              <span class="main-color">{{item.goodsName}}</span>
            </li>
          </ul>
          </div>
        </div>
      </div>
      <div class="game"><img src="../../assets/images/shop/score_game.png"></div>
        <h-scroll :active="active" @menuItem="menuItem" :list="goodsCategoryList" ref="scroller"></h-scroll>
      <div id="floor1" class="product bg-fff">
        <h2 class="text-center"><img src="../../assets/images/shop/1F.png">热门商品</h2>
        <h5 class="text-center color-999">大家都在兑换</h5>
        <ul class="hot-product clearfix">
          <li v-for="item in bestSellerList">
            <router-link :to="`/shop/detail/?id=${item.id}&from=index`">
              <div class="pro-bg"><img :src="assetsDomain + item.picSmall"></div>
              <p>{{ item.goodsName }}</p>
            </router-link>
          </li>
        </ul>
      </div>
      <div :id="`floor${index+2}`" v-for="(item,index) in goodsCategoryList" class="product bg-fff common-area" :ref="`floor${index+2}`">
        <h2 class="text-center">
          <span :class="`f-${index+2}`"></span>{{item.goodsCategoryName}}
        </h2>
        <h5 v-if="index+2 == 2" class="text-center color-999">创意灵感 点亮生活</h5>
        <h5 v-if="index+2 == 3" class="text-center color-999">智能家电 欢乐共享</h5>
        <h5 v-if="index+2 == 4" class="text-center color-999">新潮数码 大牌精选</h5>
        <h5 v-if="index+2 == 5" class="text-center color-999">优惠福利 助力投资</h5>
        <ul class="hot-product clearfix">
          <score-product :goodsCategoryId="item.id"></score-product>
          <!--<li v-for="item2 in item.item">-->
          <!--<router-link :to="`/shop/detail/?id=${item2.id}&from=index`">-->
          <!--<div class="pro-bg">-->
          <!--<img :src="assetsDomain + item2.picSmall">-->
          <!--<img v-if="item2.lessNum == 0" class="sellout" src="../../assets/images/shop/sellOut.png">-->
          <!--</div>-->
          <!--<p class="ellipsis">{{ item2.goodsName }}</p>-->
          <!--<div class="pro-info"><span><i class="main-color">{{ item2.score | currency('',0) }}</i> 积分</span><span class="right">数量 {{ item2.lessNum }}</span></div>-->
          <!--</router-link>-->
          <!--</li>-->
        </ul>
        <router-link :to="{name: 'shopList', query: {order: index+2, uuid: item.id}}">
          <div class="more aui-border-t main-color text-center">查看更多</div>
        </router-link>
      </div>
      </mt-loadmore>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import HScroll from '../../components/scroll/HScroll.vue'
  import ScoreProduct from '../../components/ScoreProduct.vue'
  const { origin, pathname } = window.location
  const getPageKey = () => {
    return is.object(history.state) ? history.state.key : 0
  }
  export default {
    components: { ScoreProduct, HScroll },
    data() {
      return {
        bannerList: [], // 首页banner图列表数组
        activeIndex: 0, // 通告数量计数单位
        scrollWrapHeight: 0, // 通告列表滚动高度
        assetsDomain: ajaxUrl.StaticsServer,
        noticeList: [],
        goodsCategoryList: [], // 商品类别
        obj: {}, // 临时对象存储
        newList: [], // 将商品各个类的商品存储
        scoreData: '', //积分数据
        bestSellerList: [], // 最热商品
        menuScroll: '',
        active: 1,
        topShow: false,
      }
    },
    computed: {
      top() {
        return -this.activeIndex * this.scrollWrapHeight + 'px';
      },
      transition() {
        return this.activeIndex === 0 ? 'none' : 'top 0.5s';
      }
    },
    created() {
      // 没有记录滚动条，此方法待定; 暂时统一回到第一个商品区
      // if(sessionStorage.refreshToShopIndex == true) { // 在index.html首页里监听刷新
        // location.hash = '#floor1' // 页面初始化统一默认
        // sessionStorage.refreshToShopIndex = false
      // }
      this.$http.get(ajaxUrl.bannerAjax, {params: {code: 'scoreshopBanner'}}).then((res) => {
        this.bannerList = res.data.resData.list;
        let getParams = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
        this.$http.get(ajaxUrl.getUserScore, {params: getParams }).then((res) => { // 用户积分
          this.scoreData = res.data.resData.userScore;
          // 滚动数据获取
          this.$http.get(ajaxUrl.getNewOrders).then((res) => {
            this.noticeList = res.data.resData;
            this.$nextTick(() => {
              this.scrollWrapHeight = this.$refs.scrollWrap.offsetHeight;
            });
          });
        })
        // 加载商品列表数据
        this.initLoad()
      })
    },
    mounted() {
      this.scrollWrapHeight = this.$refs.scrollWrap.offsetHeight;
      // 滚动通告效果
      var t = setInterval(() => {
        if (this.noticeList.length == 1) {
          clearInterval(t);
        }
        if (this.activeIndex < this.noticeList.length - 1) {
          this.activeIndex += 1;
        } else {
          this.activeIndex = 0;
        }
      }, 3000);
      this.arr_h = [] // 收集商品分类区域高度
      // 监听滚动条高度
      this.top_dis = this.$refs.scroller.$el.getBoundingClientRect().top + this.$refs.scroller.$el.clientHeight + 50
      // console.log(this.top_dis)
    },
    methods: {
      menuItem(index){
        window.location.hash = '#floor' + index
        this.active = index
        this.scrollflag = false // 不执行scroll里面代码
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          window.location.reload();
        }, 1000)
      },
      initLoad () {
        this.$http.get(ajaxUrl.getBestseller).then((res) => { // 热门商品
          if(res.data.resData.length) {
            this.bestSellerList = res.data.resData.slice(0,3)
          }
          this.$http.get(ajaxUrl.goodsCategoryList).then((res) => { // 商品类别
            this.goodsCategoryList = res.data.resData
          })
        })
      },
      onTouchMove() {
        this.scrollflag = true
      },
      onScroll() {
        this.sTop = document.querySelector('.con').scrollTop
        // console.log(this.sTop, this.arr_h)
        if(this.sTop > this.top_dis) {
          this.topShow = true
        } else {
          this.topShow = false
        }
        if(!this.scrollflag) return  //this.scrollflag为false下面代码不执行
        // 判断滚动条位置在哪个商品区里
        if(this.arr_h.length) {
          for(var i=0; i < this.arr_h.length; i++){
            if(this.arr_h[i] <= this.sTop && this.sTop < this.arr_h[i+1]) {
              // 菜单导航跳转位置
              let index = i + 1;
              this.active = index
              this.$refs.topScroller.scrollToEle(index)
              this.$refs.scroller.scrollToEle(index)
              // console.log(i)
              return
            }
          }
        }
      }
    },
    watch: {
      goodsCategoryList() {
        if(window.location.hash) {
          let index = window.location.hash.slice(-1)
          this.active = index
          // 刷新位置
          this.$refs.scroller.scrollToEle(index)
        }
      },
      topShow(flag) {
        let con = document.querySelector('.con')
        if(flag) {
          con.scrollTop = this.sTop + 30
          document.querySelectorAll('.product').forEach( val => {
            let v_h = val.getBoundingClientRect().top + val.clientHeight
            // console.log(val.getBoundingClientRect().top, val.clientHeight)
            this.arr_h.push(v_h)
          })
          // console.log(this.arr_h)
        } else {
          this.arr_h = []
          con.scrollTop = this.sTop - 30
        }

      }
    }
  }
</script>

