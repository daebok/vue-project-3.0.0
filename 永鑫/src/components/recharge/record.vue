<!-- 充值/提现记录 by fenglei -->
<template>
  <div class="page">
    <div class="menu-tag">
      <span v-for="(item, index) in menuList" @click="menuChange(index)">
        <i :class="{ 'current': currentTag == index }">{{ item.title }}</i>
      </span>
    </div>
    <transition name="fade">
      <recharge-record v-show="currentTag == 0" ref="recharge" class="page-loadmore-wrapper" :style="{ height: wrapperHeight + 'px' }"></recharge-record>
    </transition>
    <transition name="fade">
      <withdraw-record v-show="currentTag == 1" ref="withdraw" class="page-loadmore-wrapper" :style="{ height: wrapperHeight + 'px' }"></withdraw-record>
    </transition>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import rechargeRecord from '../../components/recharge/rechargeRecord.vue';
  import withdrawRecord from '../../components/recharge/withdrawRecord.vue';

  export default {
    data() {
      return {
        menuList: [
          {
            title: '充值记录'
          },
          {
            title: '提现记录'
          }
        ],
        currentTag: 0,
        wrapperHeight: 0
      };
    },
    components: { rechargeRecord, withdrawRecord },
    created() {
      this.$nextTick(() => {
        let menuTagHeight = document.querySelector('.menu-tag').getBoundingClientRect().height;
        this.wrapperHeight = document.documentElement.clientHeight - menuTagHeight;
      });
    },
    methods: {
      dataLoad() {
        this.$refs.recharge.dataLoad();
        this.$refs.withdraw.dataLoad();
      },
      menuChange(index) {
        this.currentTag = index;
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "record.sass";
</style>
