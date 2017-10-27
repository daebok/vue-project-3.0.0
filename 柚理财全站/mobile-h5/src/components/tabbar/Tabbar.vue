<template>
  <div class="mint-tabbar" :class="{ 'is-fixed': fixed }">
    <slot></slot>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    name: 'rd-tabbar',
    componentName: 'rd-tabbar',
    props: {
      fixed: Boolean,
      value: {}
    },
    mounted() {
      //监听从FooterItem组件中触发的单击事件，并向父组件派发此事件
      this.$on('rd-tabItem-click', (...params) => {
        let [id] = params;
        this.$parent.$emit('rd-tabItem-click', id);
      })
    }
  }
</script>