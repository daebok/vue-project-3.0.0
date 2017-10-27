<template>
  <div class="page-tabbar">
    <rd-footer :value="activeType" fixed>
      <footer-item id="index">
        <img slot="icon" src="../assets/images/public/tabbar/tab_home_n.png">
        <img slot="iconActive" src="../assets/images/public/tabbar/tab_home_s.png">
        <div class="mint-tab-item-label">首页</div>
      </footer-item>
      <footer-item id="invest">
        <img slot="icon" src="../assets/images/public/tabbar/tab_product_n.png">
        <img slot="iconActive" src="../assets/images/public/tabbar/tab_product_s.png">
        <div class="mint-tab-item-label">理财</div>
      </footer-item>
      <footer-item id="account">
        <img slot="icon" src="../assets/images/public/tabbar/tab_asset_n.png">
        <img slot="iconActive" src="../assets/images/public/tabbar/tab_asset_s.png">
        <div class="mint-tab-item-label">资产</div>
      </footer-item>
      <footer-item id="shop/index">
        <img slot="icon" src="../assets/images/public/tabbar/tab_integral_n.png">
        <img slot="iconActive" src="../assets/images/public/tabbar/tab_integral_s.png">
        <div class="mint-tab-item-label">积分商城</div>
      </footer-item>
      <footer-item id="mine">
        <img slot="icon" src="../assets/images/public/tabbar/tab_me_n.png">
        <img slot="iconActive" src="../assets/images/public/tabbar/tab_me_s.png">
        <div class="mint-tab-item-label">我</div>
      </footer-item>
    </rd-footer>
  </div>
</template>

<script>
  import RdFooter from '../components/tabbar/Tabbar';
  import FooterItem from '../components/tabbar/TabItem';

  export default {
    name: 'page-tabbar',
    data() {
      return {
        activeType: '' //当前路由地址
      };
    },
    created() {
      this.activeType = this.$route.path.replace('/','');
    },
    watch: {
      //当路由变化时改变activeType的值并寄存到本地
      '$route'(to, from) {
        const toDepth = to.path.replace('/','');
        this.activeType = toDepth;
        sessionStorage.setItem('activeType', toDepth);
      }
    },
    mounted() {
      //监听从FooterItem组件中触发的单击事件，跳转到id页面
      this.$on("rd-tabItem-click", (id) => {
        this.$router.push(`/${id}`);
      });
    },
    components: { RdFooter, FooterItem }
  }
</script>

<style type="text/css" scoped>
  @import "../assets/scss/var.scss";

  .mint-tabbar {
    background: #fff;
  }
  .mint-tabbar > .mint-tab-item.is-selected {
    background-color: #fff;
    color: $main-color;
  }
</style>
