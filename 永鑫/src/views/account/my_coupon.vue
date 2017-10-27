<template>
  <div>
    <div class="menu-tag">
      <span v-for="(item, index) in menuList" @click="menuChange(index)">
        <i :class="{ 'current': currentTag == index }">{{ item.title }}</i>
      </span>
    </div>
    <transition name="fade">
      <red-envelope v-show="currentTag == 0" class="page-loadmore-wrapper" :style="{ height: wrapperHeight + 'px' }"></red-envelope>
    </transition>
    <transition name="fade">
      <rate-coupon v-show="currentTag == 1" class="page-loadmore-wrapper" :style="{ height: wrapperHeight + 'px' }"></rate-coupon>
    </transition>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import redEnvelope from '../../components/my_coupon/redEnvelope.vue'; // 红包列表组件
  import rateCoupon from '../../components/my_coupon/rateCoupon.vue'; // 加息劵列表组件

  export default {
    data() {
      return {
        menuList: [
          {
            title: '红包'
          },
          {
            title: '加息券'
          }
        ],
        currentTag: 0,
        wrapperHeight: 0 // 数据列表容器高度，结合mint-ui的loadmore组件使用
      };
    },
    components: { redEnvelope, rateCoupon },
    created() {
      this.$nextTick(() => {
        let menuTagHeight = document.querySelector('.menu-tag').getBoundingClientRect().height;
        this.wrapperHeight = document.documentElement.clientHeight - menuTagHeight;
      });
    },
    methods: {
      // 红包列表与加息劵列表切换，currentTag为0时显示红包列表，为1时显示加息劵列表
      menuChange(index) {
        this.currentTag = index;
      }
    }
  };
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/sass/my_coupon.sass";
</style>
