<template>
  <div class="page">
    <div class="content">
      <mt-header class="bar-nav aui-border-b" title="确认订单">
        <mt-button slot="left" icon="back" v-back-link></mt-button>
      </mt-header>
      <div class="address-box" v-if="resdata.isVirtual == 0">
        <div v-if="addressListObj" class="add-address" @click="$router.push('/shop/address/')">
          <div class="text">
            <p class="user">{{addressListObj.name | cutStr(8)}}<span>{{addressListObj.mobile && addressListObj.mobile.substring(0,3)}}****{{addressListObj.mobile && addressListObj.mobile.substring(7,11)}}<i><img v-if="addressListObj.isDefult == 1" src="../../assets/images/shop/default_address.png"></i></span></p>
            <p class="address">{{addressListObj.provinceStr}}{{addressListObj.cityStr}}{{addressListObj.areaStr}}{{addressListObj.address | cutStr(22)}}</p>
            <input type="hidden" class="receivingInfoId" :value="addressListObj.id" />
          </div>
          <!--<div class="text" v-for="item in addressList" v-if="item.isDefult == 1">-->
            <!--&lt;!&ndash;有默认地址显示默认地址，没有就显示第一个地址&ndash;&gt;-->
            <!--<p class="user">{{item.name}} <span>{{item.mobile.substring(0,3)}}****{{item.mobile.substring(7,11)}}<i><img src="../../assets/images/shop/default_address.png"></i></span></p>-->
            <!--<p class="address">{{item.provinceStr}}{{item.cityStr}}{{item.areaStr}}{{item.address}}</p>-->
            <!--<input type="hidden" class="receivingInfoId" :value="item.id" />-->
          <!--</div>-->
          <div class="arrow"><img src="../../assets/images/shop/arrow.png" class="right-arrow"></div>
        </div>
        <div v-else class="add" @click="$router.push('/shop/address/add/')">
          <span><img src="../../assets/images/shop/add_address.png"></span>
          <span>添加收货地址</span>
        </div>
      </div>
      <div class="cart-list">
        <ul>
          <li class="aui-border-b">
            <div class="pic"><img :src=" assetsDomain + resdata.picSmall"></div>
            <div class="text">
              <p class="product color-333">{{resdata.goodsName}}</p>
              <p class="price color-999 size-12"><i class="main-color size-15">{{resdata.score | currency('',0)}}</i> 积分<span class="number pull-right color-333 size-12">x{{buyNum}}</span></p>
            </div>
          </li>
        </ul>
      </div>
      <div class="mark color-333 aui-border-b">备注 <span class="pull-right color-999">3-7个工作日内审核发货</span></div>
      <div class="message color-333 aui-border-b">兑换留言：<span><input type="text" placeholder="（选填）输入您对本次兑换的留言" v-model="message" maxlength="200"></span></div>
      <div class="pro-info">共{{buyNum}}件商品<span>消耗积分 <i class="main-color">{{ totalScore | currency('',0) }}</i></span></div>
    </div>
    <div v-if="!addressListObj" class="bottom disabled">确认兑换</div>
    <div v-else @click="confirm" class="bottom">确认兑换</div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import qs from 'qs'
  export default {
    data () {
      return {
        assetsDomain: ajaxUrl.StaticsServer,
        resdata: '',
        buyNum: this.$route.query.num,
        totalScore: 0,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        addressListObj: '',
        message: ''
      }
    },
    created () {
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      const data = Object.assign({}, this.getParams, {id: this.$route.query.id })
      this.$http.get(ajaxUrl.getGoodsInfo, {params: data}).then((res) => {
        this.resdata = res.data.resData.scoreGoods
        this.totalScore = this.resdata.score * this.buyNum
        this.$indicator.close()
        this.$http.get(ajaxUrl.getReceivingInfo, {params: this.getParams}).then((res) => {
          if (res.data.resData.list && res.data.resData.list.length == 0) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去添加',
              cancelButtonText: '下次吧',
              showCancelButton: true,
              message: '您还未添加收货地址'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push('/shop/address/add/');
              }
            });
          } else {
            this.addressListObj = res.data.resData.list[0]
            if(sessionStorage.fromName == 'address' && sessionStorage.selectedAddress) {
              this.addressListObj = JSON.parse(sessionStorage.selectedAddress)
            }
          }
        })
      })
    },
    methods: {
      confirm () {
        let addressEl = document.querySelector('.address')
        let receivingInfoIdEl = document.querySelector('.receivingInfoId')
        let receivingAddress = addressEl ? addressEl.innerText : ''
        let receivingInfoId = receivingInfoIdEl ? receivingInfoIdEl.value : ''
        let params = Object.assign({}, this.getParams, {
          goodsId: this.resdata.id,
          // goodsName: this.resdata.goodsName,
          num: this.buyNum,
          receiveRemark: this.message,
          receivingAddress: receivingAddress,
          receivingInfoId: receivingInfoId,
          score: this.totalScore
        })
        this.$indicator.open({spinnerType: 'fading-circle'})
        this.$http.post(ajaxUrl.exchangeGoods, qs.stringify(params)).then((res) => {
          this.$indicator.close()
          this.$toast(res.data.resMsg)
          if(res.data.resCode == 39321){
            this.$router.replace({name: 'success', query: {name: this.resdata.goodsName, score: this.totalScore, from: this.$route.query.from }})
          }
        })
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .address-box {
    margin: 0 0 .12rem;
    .add {
      background: #fff;
      padding: .2rem .15rem .2rem .29rem;
      text-align: center;
      color: #666;
      font-size: .15rem;
      img { width: .14rem; margin-right: .08rem; vertical-align: -2px;}
    }
    .add-address {
      background: #fff;
      display: table;
      width: 100%;
      padding: .2rem .15rem .2rem .29rem;
      position: relative;
      &:after {
        display: block;
        content: '';
        position: absolute;
        right: 0;
        bottom: 0;
        left: 0;
        background: url(../../assets/images/shop/envelope.png);
        background-size: auto 2px;
        height: 3px;
      }
      div {
        display: table-cell;
        vertical-align: middle;
        .user {
          font-size: .16rem;
          margin-bottom: .05rem;
          span { padding-left: .25rem; }
          img { width: .5rem; vertical-align: -3px;margin-left: .12rem; }
        }
        .address {
          color: #666;
          line-height: .2rem;
        }
        &.arrow {
          width: 8%;
          text-align: right;
          img { width: .17rem; }
        }
      }
    }
  }
  .cart-list {
    li {
      background: #fff;
      display: table;
      width: 100%;
      padding: 0 .15rem;
      height: 1rem;
      margin-bottom: .1rem;
      div {
        display: table-cell;
        vertical-align: middle;
        img { width: 1.05rem; height: .71rem; border: 1px solid #eee; }
        p { line-height: .35rem;}
        &.pic { width: 1.2rem; margin-right: .1rem;}
        .size-12 { font-size: .12rem; }
        .size-15 { font-size: .15rem; }
      }
    }
  }
  .mark,.message,.pro-info {line-height: .44rem; background: #fff;padding: 0 .15rem; }
  .message {
    span {
      input {width: 70%; border: 0; outline: none; }
    }
  }
  .pro-info {
    text-align: right;
    span {
      margin-left: .3rem;
      font-size: .13rem;
      i { font-size: .17rem; }
    }
  }
  .bottom {
    position: absolute;
    bottom: 0;
    width: 100%;
    line-height: .5rem;
    background: $main-color;
    font-size: .17rem;
    color: #fff;
    text-align: center;
    &.disabled { background: #ccc; }
  }
</style>