<template>
  <div class="bg-fff">
    <div class="result">
      <div class="result-img">
        <img src="../../assets/images/shop/score_change_succ.png" />
      </div>
      <div class="result-msg">
        <h2>-{{ $route.query.score | currency('') }}</h2>
        <p>兑换成功，等待平台审核</p>
      </div>
      <div class="result-btn">
        <router-link :to="keepOn">
          <span class="aui-border left">继续兑换</span>
        </router-link>
        <span class="white-space"></span>
        <router-link to="/shop/myScore">
          <span class="aui-border right">查看兑换记录</span>
        </router-link>
      </div>
      <div class="product aui-border-t">
        <span class="left">兑换产品</span>
        <span class="right ellipsis">{{$route.query.name}}</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    data(){
      return {
        title: '',
        keepOn: '/shop/index'
      }
    },
    created(){
      if(this.$route.query.from){
        this.keepOn = `/shop/${this.$route.query.from}`
      }
      switch(sessionStorage.fromName){
        case 'shopList':
          this.$router.replace({name: 'shopIndex'});
          break;
        case 'shopIndex':
          this.$router.replace({name: 'index'});
          break;
        case 'myScore':
          this.$router.replace({name: 'shopIndex'});
          break;
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import '../../assets/scss/var.scss';
  .bg-fff { background: #fff; }
  .box-msg {  padding: 0 10%;  }
  .msg-text { line-height: .24rem; }
  .result-img {  text-align: center; padding-top: 10%;  }
  .result-img img {
    width: .9rem;
    height: .9rem;
    margin: 0 auto;
  }
  .result-msg {
    width: 80%;
    margin-left: 10%;
    font-size: .16rem;
    color: #666;
    text-align: center;
    padding-bottom: .5rem;
    line-height:.2rem;
  }
  .result-btn {
    padding-bottom: .25rem;
    text-align: center;
    font-size: 0;
    .left,.right {
      width: 1.15rem;
      display: inline-block;
      font-size: .14rem;
      line-height: .4rem;
      color: #999;
      &:after {
        border-radius: .4rem;
      }
    }
    .white-space { display: inline-block; background: #fff; line-height: .4rem; width: .12rem; }
  }
.product {
  display: table;
  width: 100%;
  height: .5rem; padding: 0 .15rem;
  span {
    display: table-cell;
    vertical-align: middle;
    color: #999;
    &.left { width: .7rem; }
    &.right { color: #333; }
  }

}
</style>
