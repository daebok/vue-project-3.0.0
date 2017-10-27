<template>
  <a class="mint-tab-item" @click="clickTabItem" :class="{ 'is-selected': $parent.value === id }">
    <div v-if="$parent.value === id" class="mint-tab-item-icon">
      <slot name="iconActive"></slot>
    </div>
    <div v-else class="mint-tab-item-icon">
      <slot name="icon"></slot>
    </div>
    <div class="mint-tab-item-label"><slot></slot></div>
  </a>
</template>

<script type="text/ecmascript-6">
  import emitter from '../../mixins/emitter.js'; //引入vue1.0被废弃的dispatch和broadcast

  export default {
    name: 'rd-tab-item',
    componentName: 'rd-tab-item',
    props: ['id'],
    mixins: [emitter],
    methods: {
      // 向上派发此事件
      clickTabItem() {
        this.dispatch('rd-tabbar', 'rd-tabItem-click', this.id);
      }
    }
  };
</script>

<style lang="sass" rel="stylesheet/sass">
  .mint-tab-item
    display: block
    padding: 7px 0
    -webkit-box-flex: 1
    -webkit-flex: 1
    flex: 1
    text-decoration: none
    width: 25%
</style>
