<template>
  <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="mtloadmore" :bottomDistance="0">
      <slot></slot>
    </mt-loadmore>
  </div>
</template>
<script type="text/ecmascript-6">
  export default {
    props: {
      allLoaded: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        wrapperHeight: 0
      }
    },
    created() {
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top - 50;
      })
    },
    methods: {
      onTop(id) {
        this.$refs.mtloadmore.onTopLoaded(id);
      },
      loadTop(id) {
        this.$emit('loadTop', id)
      },
      onBottom(id) {
        this.$refs.mtloadmore.onBottomLoaded(id);
      },
      loadBottom(id) {
        this.$emit('loadBottom', id)
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass">
  /*加载更多组件的样式*/
  .page-loadmore-wrapper
    overflow: scroll

  .mint-spinner
    display: inline-block
    vertical-align: middle

  .mint-loadmore-bottom span
    display: inline-block
    transition: .2s linear
    vertical-align: middle

  .mint-loadmore-bottom span.rotate
    transform: rotate(180deg)
</style>
