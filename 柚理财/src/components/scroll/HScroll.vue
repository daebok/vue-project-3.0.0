<template>
  <i-scroll
    :id="id"
    :ref="`${id}scroll`"
    h="0.5"
    :data="list"
  >
      <div class="menu-item ellipsis aui-flex-item-3" :class="{'current': active == 1}" @click="menuItem(1)" :id="`${id}Index1`">
        <p><span>1F</span>热门商品</p>
      </div>
      <div class="menu-item ellipsis aui-flex-item-3" :class="{'current': active == index+2 }" v-for="(item,index) in list" @click="menuItem(index+2)" :id="`${id}Index${index+2}`">
        <p><span>{{index+2}}F</span>{{item.goodsCategoryName}}</p>
      </div>
  </i-scroll>
</template>
<script>
  import IScroll from './ShopScroll.vue'
  export default {
    components: { IScroll },
    props: {
      id: {
        type: String,
        default: ''
      },
      active: {
        type: [Number, String]
      },
      list: {
        type: Array,
        default: []
      }
    },
    methods: {
      scrollToEle(index) {
        setTimeout(() => {
          let el = document.getElementById(`${this.id}Index${index}`)
          this.$refs[`${this.id}scroll`].scrollToElement(el)
          this.$refs[`${this.id}scroll`].refresh()
        }, 10)
      },

      // 向父组件派发menuItem事件，查看还款计划
      menuItem(index) {
        this.$emit('menuItem', index);
      }
    }
  }
</script>
<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/scss/var"
  .aui-flex-item-3
    &.current p
      color: $main-color
      border-bottom: 1px solid $main-color
      span
        color: $main-color
    p
      color: #333
      line-height: .2rem
      padding: .16rem .05rem .12rem
      margin: 0 .18rem 0 .06rem
      span
        font-size: .15rem; color: #ddd
</style>
