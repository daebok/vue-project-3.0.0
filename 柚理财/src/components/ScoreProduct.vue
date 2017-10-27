<template>
  <section>
  <li v-for="item in list">
    <router-link :to="`/shop/detail/?id=${item.id}&from=index`">
      <div class="pro-bg">
        <img :src="assetsDomain + item.picSmall">
        <img v-if="item.lessNum == 0" class="sellout" src="../assets/images/shop/sellOut.png">
      </div>
      <p class="ellipsis" :class="{'color-999': item.lessNum == 0}">{{ item.goodsName }}</p>
      <div class="pro-info"><span><i :class="{'main-color': item.lessNum > 0}">{{ item.score | currency('',0) }}</i> 积分</span><span class="right">数量 {{ item.lessNum }}</span></div>
    </router-link>
  </li>
  </section>
</template>
<script>
  import * as ajaxUrl from '../ajax.config.js';
  export default {
    name: 'score-product',
    data() {
      return {
        list: [],
        assetsDomain: ajaxUrl.StaticsServer,
      }
    },
    props: {
      goodsCategoryId: { type: String }
    },
    mounted() {
      this._init()
    },
    methods: {
      _init() {
        let param = { goodsCategoryId: this.goodsCategoryId }
        this.$http.get(ajaxUrl.getBestseller,{params: param}).then((res) => { // 热门商品
          this.list = res.data.resData
        })
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  li {
    display: inline-block; width: 1.665rem;margin-right: .12rem;margin-bottom: .18rem; vertical-align: top; font-size: .14rem; line-height: .24rem;
    &:nth-child(2n) { margin-right: 0; }
    .pro-bg { position: relative; }
    img {
      width: 1.665rem; height: 1.11rem; display: block;
      &.sellout { position: absolute; width: .62rem; height: .62rem; top: 50%; left: 50%; margin-top: -.31rem; margin-left: -.31rem; }
    }
    p { color: #333; font-size: .12rem; padding-left: .05rem;}
    .pro-info {
      font-size: .12rem; color: #999; padding-left: .05rem;
      i { font-size: .14rem; }
      .right { float: right; font-size: .11rem; }
    }
  }
</style>
