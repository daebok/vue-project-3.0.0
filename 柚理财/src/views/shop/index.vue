<template>
  <div>
    <!-- 顶部导航 -->
    <div id="invest_type_fix" class="invest-type amimate" :class="{ 'setTop': setTop }">
      <ul>
        <li :class="{ 'current': categoryCurrentIndex == 0 }" @click="goodsTypeChange(0)">
          <span>
            <i>1F</i>热门商品
          </span>
        </li>
        <li v-for="(item, index) in goodsCategoryList" :class="{ 'current': (index + 1) == categoryCurrentIndex }" @click="goodsTypeChange(index + 1)">
          <span>
            <i>{{ item.sort + 1 }}F</i>{{ item.goodsCategoryName }}
          </span>
        </li>
      </ul>
    </div>

    <!-- 内容区域 -->
    <mt-loadmore :top-method="loadTop" ref="loadmore">
      <!-- S首页banner图 -->
      <div id="banner">
        <mt-swipe :auto="3000">
          <mt-swipe-item v-if="bannerList && bannerList.length === 0">
            <img src="../../assets/images/banner_default.png">
          </mt-swipe-item>
          <mt-swipe-item v-else v-for="banner in bannerList" :key="banner.id">
            <!-- <a :href="banner.url"> -->
              <img :src="banner.picPath">
            <!-- </a> -->
          </mt-swipe-item>
        </mt-swipe>
      </div>
      <!-- E首页banner图 -->

      <!-- 积分信息 -->
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

      <!-- 最新兑换/动态滚动列表 -->
      <div v-if="noticeList.length > 0" class="latest-exchange-scroll-list">
        <div class="imgIcon">
          <img src="../../assets/images/shop/newest_exchange.png" />
        </div>
        <div class="scroll-list">
          <ul id="scroll_list">
            <li v-for="(item, index) in noticeList">
              <img :src="assetsDomain+item.avatarPhoto">
              <span v-if="item.receivePhone">{{item.receivePhone.substring(0,3)}}****{{item.receivePhone.substring(7,11)}}</span>
              <span v-else>{{item.userName.substring(0,4)}}****{{item.userName.substring(7,11)}}</span>
              <label>兑换了</label>
              <i>{{ item.goodsName }}</i>
            </li>
          </ul>
        </div>
      </div>

      <!-- 大转盘 -->
      <div class="game">
        <router-link to="/shop/lottery">
          <img src="../../assets/images/shop/score_game.png">
        </router-link>
      </div>

      <!-- 中部商品类别导航条 -->
      <div id="invest_type_rel" class="invest-type">
        <ul>
          <li :class="{ 'current': categoryCurrentIndex == 0 }" @click="goodsTypeChange(0)">
            <span>
              <i>1F</i>热门商品
            </span>
          </li>
          <li v-for="(item, index) in goodsCategoryList" :class="{ 'current': (index + 1) == categoryCurrentIndex }" @click="goodsTypeChange(index + 1)">
            <span>
              <i>{{ item.sort + 1 }}F</i>{{ item.goodsCategoryName }}
            </span>
          </li>
        </ul>
      </div>

      <!-- 产品列表 -->
      <div id="goods_list" class="goods-list">
        <!-- 一楼写死 -->
        <div id="goods_list_0" class="product bg-fff type">
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

        <!-- 二楼以后取接口 -->
        <div :id="'goods_list_' + (index + 1)" v-for="(item,index) in goodsCategoryList" class="product bg-fff common-area type" :ref="`floor${index+2}`">
          <h2 class="text-center">
            <span :class="`f-${index+2}`"></span>{{item.goodsCategoryName}}
          </h2>
          <h5 v-if="index+2 == 2" class="text-center color-999">创意灵感 点亮生活</h5>
          <h5 v-if="index+2 == 3" class="text-center color-999">智能家电 欢乐共享</h5>
          <h5 v-if="index+2 == 4" class="text-center color-999">新潮数码 大牌精选</h5>
          <h5 v-if="index+2 == 5" class="text-center color-999">优惠福利 助力投资</h5>
          <ul class="hot-product clearfix">
            <score-product :goodsCategoryId="item.id"></score-product>
          </ul>
          <router-link :to="{name: 'shopList', query: {order: index+2, uuid: item.id}}">
            <div class="more aui-border-t main-color text-center">查看更多</div>
          </router-link>
        </div>
      </div>
    </mt-loadmore>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import ScoreProduct from '../../components/ScoreProduct.vue'
  import vsa from '../../mixins/vertical-scrolling-animation.js'; // 垂直滚动动画

  const { origin, pathname } = window.location
  const getPageKey = () => {
    return is.object(history.state) ? history.state.key : 0
  }

  export default {
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
        scrollflag: true, //默认滚动对应类别

        //滚动条变量
        setTop: false, // 商品类别导航条是否置顶，true 置顶，false 不置顶
        categoryCurrentIndex: 0, // 当前选中的商品分类项
        top: '', // 商品列表距离窗口顶部距离
        clientHeight: 0,
      }
    },
    mixins: [ vsa ],
    components: { ScoreProduct},
    watch: {
      // 监听商品列表距离窗口顶部的距离，设置商品类别导航条置顶
      top(val) {
        if (val <= 0) {
          this.setTop = true;
        } else {
          this.setTop = false;
        }
      },
      // 监听当前选中的商品分类项，自动滚动到指定位置
      categoryCurrentIndex(newVal, oldVal) {
        let domID = this.setTop ? 'invest_type_fix' : 'invest_type_rel';
        let dom = document.querySelectorAll('#' + domID + ' li')[newVal];
        let beforeDom = document.querySelectorAll('#' + domID + ' li')[newVal - 1];
        let afterDom = document.querySelectorAll('#' + domID + ' li')[newVal + 1];
        let beforeLeft = beforeDom ? document.getElementById(domID).getElementsByTagName('li')[newVal - 1].getBoundingClientRect().left : 0;
        let afterLeft = afterDom ? document.getElementById(domID).getElementsByTagName('li')[newVal + 1].getBoundingClientRect().left : 0;
        let beforeWidth = Number(this.rdCss(beforeDom, 'height'));
        let afterWidth = Number(this.rdCss(afterDom, 'height'));
        let clientWidth = document.documentElement.clientWidth;

        if (newVal > oldVal) {
          if (afterDom !== undefined && (afterLeft >= clientWidth - afterWidth)) {
            this.scrollAmimate(document.querySelector('#' + domID + ' ul'), dom.offsetLeft);
          }
        } else if (newVal < oldVal) {
          if (newVal === 0) {
            this.scrollAmimate(document.querySelector('#' + domID + ' ul'), 0, 1);
          }
          if (beforeDom !== undefined && (beforeLeft <= clientWidth - beforeWidth)) {
            this.scrollAmimate(document.querySelector('#' + domID + ' ul'), beforeDom.offsetLeft, 1);
          }
        }
      }
    },
    computed: {
      wrapperHeight() {
        return this.clientHeight - this.headerHeight - this.footerHieght;
      },
      wrapperNoHeaderHeight() {
        return this.clientHeight - this.footerHieght;
      },
      wrapperNoFooterHeight() {
        return this.clientHeight - this.headerHeight;
      }
    },
    created() {
      this.$nextTick(() => {
        this.clientHeight = document.documentElement.clientHeight;
      });
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
              if (this.noticeList.length > 0) {
                this.startNewOrderScroll();
              }
            });
          });
        })
        // 加载商品列表数据
        this.initLoad();
      })
    },
    mounted() {
      window.onresize = () => {
        this.clientHeight = document.documentElement.clientHeight;
      };
      // 监听滚动条
      window.addEventListener('scroll', this.scrollFunc, false);
      document.querySelector('#invest_type_rel ul').addEventListener('scroll', this.scrollA, false);
      document.querySelector('#invest_type_fix ul').addEventListener('scroll', this.scrollB, false);
    },
    methods: {
      // 最新兑换上下无缝滚动效果
      startNewOrderScroll() {
        let scrollHeight = Number(this.rdCss((document.querySelector('#scroll_list li')), 'height'));
        this.startmarquee('scroll_list', scrollHeight, 40, 3000);
      },
      // 商品分类切换
      goodsTypeChange(index) {
        let height = Number(this.rdCss((document.querySelector('.invest-type')), 'height'));
        let dom = document.querySelector('#goods_list_' + index);
        document.body.scrollTop = dom.offsetTop - height;
        this.categoryCurrentIndex = index;
      },
      // 商品列表滚动同步导航项
      scrollFunc() {
        this.top = document.getElementById('goods_list').getBoundingClientRect().top;
        let goodsList = document.querySelectorAll('#goods_list .type');
        for (var i = 0; i < goodsList.length; i++) {
          let top = document.getElementById('goods_list_' + i).getBoundingClientRect().top;
          if (top > 0 && top < (this.clientHeight * 0.5)) {
            this.categoryCurrentIndex = i;
            //console.log(this.categoryCurrentIndex);
            break;
          }
        }
      },
      // 同步滚动条
      scrollA() {
        let scrollleft = document.querySelector('#invest_type_rel ul').scrollLeft;
        document.querySelector('#invest_type_fix ul').scrollLeft = scrollleft;
      },
      scrollB() {
        let scrollleft = document.querySelector('#invest_type_fix ul').scrollLeft;
        document.querySelector('#invest_type_rel ul').scrollLeft = scrollleft;
      },
      // 滚动条滚动人性化动画
      scrollAmimate(dom, value, direction, speed = 10) {
        let timeBack;
        let timeGo;
        let scroll = dom.scrollLeft;
        let fast = 0;
        clearTimeout(timeBack);
        clearTimeout(timeGo);
        if (direction === 1) {
          fast = (scroll - value) / speed;
          scrollBack();
        } else {
          fast = (value - scroll) / speed;
          scrollGo();
        }
        function scrollBack() {
          if (scroll <= value) {
            dom.scrollLeft = value;
          } else {
            scroll = scroll - fast;
            dom.scrollLeft = scroll;
            timeBack = setTimeout(() => {
              scrollBack();
            }, speed);
          }
        }
        function scrollGo() {
          if (scroll >= value) {
            dom.scrollLeft = value;
          } else {
            scroll = scroll + 10;
            dom.scrollLeft = scroll;
            timeGo = setTimeout(() => {
              scrollGo();
            }, speed);
          }
        }
      },
      /* 设置或获取CSS样式
       *设置：css(obj,{display:'block',color:'red'});
       *获取：css(obj,'color');
      */
      rdCss() {
        var obj = arguments[0];
        var options = arguments[1];
        if (!!obj === false || typeof options === 'undefined') {
          return null;
        }
        if (typeof options === 'string') {
          let res = document.defaultView.getComputedStyle(obj, null)[options];
          res = Number(res.replace('px', ''));
          return res;
        }
        if (typeof options === 'object') {
          for (var i in options) {
            obj.style[i] = options[i];
          }
        }
      },
      //e-----滚动条部分
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
    },
    destroyed() {
      window.removeEventListener('scroll', this.scrollFunc, false);
      document.querySelector('#invest_type_rel ul').removeEventListener('scroll', this.scrollA, false);
      document.querySelector('#invest_type_fix ul').removeEventListener('scroll', this.scrollB, false);
    }
  }
</script>

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
  #goods_list_0 {
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

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/sass/border_1px.sass"
  @import "../../assets/sass/var.sass"
  @import "../../assets/sass/index.sass"
</style>