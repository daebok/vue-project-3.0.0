<template>
  <div class="page">
    <div class="content" :class="{'margin-85': limited}" >
      <mt-header class="bar-nav aui-border-b" title="我的兑换">
        <mt-button slot="left" icon="back" v-back-link></mt-button>
      </mt-header>
      <mt-loadmore :top-method="loadTop" ref="loadmore">
      <div class="banner"><img :src=" assetsDomain + resdata.picSmall "></div>
      <div class="flex box aui-border-b">
        <div class="cell left">
          <img v-if="resdata.goodsCategoryName == '箱包'" src="../../assets/images/shop/home_goods.png" >
          <img v-if="resdata.goodsCategoryName == '电器'" src="../../assets/images/shop/home_electrical.png" >
          <img v-if="resdata.goodsCategoryName == '手机'" src="../../assets/images/shop/digital_entertainment.png" >
          <img v-if="resdata.goodsCategoryName == '虚拟物品'" src="../../assets/images/shop/coupon.png" >
        </div>
        <div class="cell right ellipsis-2 color-333">{{resdata.goodsName}}</div>
      </div>
      <div class="box aui-border-b pro-info">
        <p class="color-999 size-12" v-if="resdata.isVirtual == 0">市场价格<em class="aui-border-b">￥{{resdata.marketPrice | currency('',2)}}</em></p>
        <p class="color-666 size-13">所需积分<i class="main-color size-20">{{resdata.score | currency('',0)}}</i></p>
        <p><span class="color-666 size-13">剩余数量<i class="color-333 size-14">{{resdata.lessNum}}</i></span><span class="pull-right color-999 size-13">可用积分<i class="color-333 size-15">{{scoreData | currency('',0)}}</i></span></p>
      </div>
      <div class="color-999 box address-info" v-if="resdata.isVirtual == 0" :class="{'line-h': exchangedOrders > 0}">
        <span class="left"><img src="../../assets/images/shop/dot.png"> {{resdata.remark}}</span>
        <span class="right" :class="{'second-line': exchangedOrders > 0}"><img src="../../assets/images/shop/dot.png"> 每人限购 <i class="color-666">{{resdata.exchangeLimit}} </i>个<i v-if="exchangedOrders > 0">, 您还可以兑换{{resdata.exchangeLimit - exchangedOrders}}个</i></span>
      </div>
      <!--虚拟商品 红包样式-->
      <div class="color-999 box coupon-info" v-else>
        <p class="left"><img src="../../assets/images/shop/dot.png"> {{resdata.remark}}</p> <p class="right"><img src="../../assets/images/shop/dot.png"> 每人限购 <i class="color-666">{{resdata.exchangeLimit}} </i>个 <em v-if="exchangedOrders > 0">, 您还可以兑换 <i class="color-666">{{resdata.exchangeLimit - exchangedOrders}}</i> 个</em></p>
      </div>
      <div class="con-detail">
        <h3 class="box color-333 aui-border-b">商品详情</h3>
        <div class="con box">
          {{ resdata.content }}
        </div>
      </div>
      </mt-loadmore>
    </div>
    <p v-if="limited" class="abs-tip" v-text="limitTxt"></p>
    <div class="bottom" v-if="resdata.lessNum > 0 && resdata.exchangeLimit > exchangedOrders">
      <span class="left">
        <em class="pull-left">
          <img v-if="minusFlag" src="../../assets/images/shop/minus_disabled.png" data-flag="false">
          <img v-else @click="minus" src="../../assets/images/shop/minus.png">
        </em>
        <input type="tel" placeholder="数量" v-model="buyNum" maxlength="6" />
        <em class="pull-right">
          <img v-if="addFlag" src="../../assets/images/shop/add_disabled.png">
          <img v-else @click="add" src="../../assets/images/shop/add.png">
        </em>
      </span>
      <span v-if="!scoreData || scoreData <= 0 || resdata.score > scoreData" class="right disabled">积分不足</span>
      <span v-else @click="confirm" class="right">立即兑换</span>
    </div>
    <div v-else class="bottom disabled">
      <p v-if="resdata.lessNum == 0">已兑完</p>
      <p v-if="resdata.exchangeLimit == exchangedOrders">您已达到限购数量</p>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    data () {
      return {
        assetsDomain: ajaxUrl.StaticsServer,
        resdata: '',
        scoreData: '',
        buyNum: 1,
        exchangedOrders: 0,
        limited: false, //用户是否限购商品了
        limitTxt: '兑换数量不能超过每人限兑数量',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        minusFlag: false,
        addFlag: false
      }
    },
    created () {
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      const data = Object.assign({}, this.getParams, {id: this.$route.query.id })
      this.$http.get(ajaxUrl.getGoodsInfo, {params: data}).then((res) => {
        this.resdata = res.data.resData.scoreGoods
        this.exchangedOrders = res.data.resData.exchangedOrders
        if(this.exchangedOrders >= this.resdata.exchangeLimit) { // 判断用户购买数量和该商品限购的数量
          this.limited = true
        }
        this.$indicator.close()
      })
      this.$http.get(ajaxUrl.getUserScore, {params: this.getParams }).then((res) => { // 用户积分
        this.scoreData = res.data.resData.userScore.useScore
        // 积分不足购买数量为0
        if(!this.scoreData || this.scoreData <= 0 || this.resdata.score > this.scoreData){
          this.buyNum = 0
          this.minusFlag = this.addFlag = true
        }
      })
    },
    watch: {
      buyNum (newV, oldV) {
        var pattern = /^[0-9]*$/
        // 只能输入数字
        if(!pattern.test(newV)){
          this.buyNum = oldV
          return
        }
        if (newV) {
          newV = Math.ceil(newV);
          var scoreData = this.scoreData || 0
          if(newV > this.resdata.exchangeLimit) {
            this.limitTxt = '不能超过每人限兑个数'
            this.limited = true
          }
          else if(newV > (this.resdata.exchangeLimit - this.exchangedOrders)) {
            this.limitTxt = '兑换数量不能超过每人限兑数量'
            this.limited = true
          }
          else if((newV * this.resdata.score) > scoreData){
            this.limitTxt = '积分不足'
            this.limited = true
          }
          else {
            this.limitTxt = ''
            this.limited = false
          }
          // 判断加减按钮是否可点击
          let num = parseInt(this.scoreData / this.resdata.score)
          if(newV <= 1) {
            this.addFlag = false
            this.minusFlag = true
          } else if(1 < newV < num) {
            this.addFlag = this.minusFlag = false
          }
          if(newV >= num){
            this.addFlag = true
            this.minusFlag = false
          }
        }else{
          this.limitTxt = ''
          this.limited = false
        }
      }
    },
    methods: {
      minus () {
        if(!this.isNumber(this.buyNum)) {
          this.$toast('请输入正确的兑换数量')
          return ;
        }
        if (this.buyNum < 1) return
        this.buyNum--
      },
      add () {
        if(!this.isNumber(this.buyNum)) {
          this.$toast('请输入正确的兑换数量')
          return ;
        }
        if (this.buyNum >= this.resdata.exchangeLimit) return
        if(this.resdata.score !='' || this.resdata.score > 0) {
          let num = parseInt(this.scoreData / this.resdata.score)
          if (this.buyNum >= num) return
        }
        this.buyNum++
      },
      confirm () {
        if(this.limited) return ;  //购买数等于限购数量后不能再兑换
        if(!this.isNumber(this.buyNum) || this.buyNum == 0) {
          this.$toast('请输入正确的兑换数量')
          return ;
        }
        this.$router.push({ name: 'order', query: {
          id: this.$route.query.id,
          num: this.buyNum,
          from: this.$route.query.from,
        }})
      },
      isNumber(val) {
        var pattern = /^[0-9]*$/;
        if(pattern.test(val)) {
          return true
        } else {
          return false
        }
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          window.location.reload();
        }, 1000)
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .content { margin-bottom: .5rem; }
  .margin-85 { margin-bottom: .85rem; }
  .banner img { width: 100%; height: 2.5rem;}
  .box { background: #fff; padding: 0 .15rem; }
  .flex {
    display: flex;
    height: .5rem;
    align-items: center;
    .left{
      width: .5rem;
      margin-right: .05rem;
      img { width: .5rem; display: block; }
    }
  }
  .pro-info {
    padding-bottom: .15rem;
    p { line-height: 1; padding-top: .15rem;
      em {
        margin-left: .11rem; padding-right: .02rem;
        &:after { top: -.18rem; border-color: #999; }
      }
      i { margin-left: .09rem; }
    }
    .size-12 { font-size: .12rem; }
    .size-13 { font-size: .13rem; }
    .size-14 { font-size: .14rem; }
    .size-15 { font-size: .15rem; }
    .size-20 { font-size: .2rem; }
  }
  .address-info {
    margin-bottom: .15rem; padding: .1rem .15rem; line-height: .24rem;
    &.line-h { height: .7rem; }
    img { width: .03rem; vertical-align: 2px; }
    span {
      &.right { float: right;
        &.second-line { width: 100%; }
      }
    }
  }
  .coupon-info {
    margin-bottom: .15rem; padding: .1rem .15rem; line-height: .24rem;
    img { width: .03rem; vertical-align: 2px; }
  }
  .con-detail {
    h3 { line-height: .5rem; }
    .con { color: #666; line-height: .21rem; padding: .15rem; }
  }
  .abs-tip {
    position: absolute; bottom: .5rem; width: 100%; line-height: .35rem;
    background: #fdf0de; color: red; padding: 0 .15rem;
  }
  .bottom {
    position: absolute; bottom: 0; width: 100%; height: .5rem; font-size: 0;
    span {
      display: inline-block; width: 50%;height: 100%; text-align: center;
      &.left {
        float: left;
        background: #eee;
        input { background: #fff; height: 100%;  color: #666; font-size: .17rem; width: calc( 100% - .9rem); width: -webkit-calc( 100% - .9rem); text-align: center; border: 0; outline: none;
        }
        em {
          display: inline-block; width: .45rem; height: 100%;
          img { width: .12rem; margin-top: .19rem; }
        }
      }
      &.right { background: $main-color; color: #fff; font-size: .17rem; line-height: .5rem; }
      &.disabled { background: #aaa; }
    }
    p { color: #fff; font-size: .17rem; line-height: .5rem; text-align: center; }
    &.disabled { background: #aaa; }
  }
</style>