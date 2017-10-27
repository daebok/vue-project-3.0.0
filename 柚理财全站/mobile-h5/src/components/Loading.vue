<template>
  <div :class="{'pos-rel': !task}">
    <div v-if="task" class="loading weui_loading_toast" >
      <div class="weui_toast">
        <div class="weui_loading">
          <div class="weui_loading_leaf weui_loading_leaf_0"></div>
          <div class="weui_loading_leaf weui_loading_leaf_1"></div>
          <div class="weui_loading_leaf weui_loading_leaf_2"></div>
          <div class="weui_loading_leaf weui_loading_leaf_3"></div>
          <div class="weui_loading_leaf weui_loading_leaf_4"></div>
          <div class="weui_loading_leaf weui_loading_leaf_5"></div>
          <div class="weui_loading_leaf weui_loading_leaf_6"></div>
          <div class="weui_loading_leaf weui_loading_leaf_7"></div>
          <div class="weui_loading_leaf weui_loading_leaf_8"></div>
          <div class="weui_loading_leaf weui_loading_leaf_9"></div>
          <div class="weui_loading_leaf weui_loading_leaf_10"></div>
          <div class="weui_loading_leaf weui_loading_leaf_11"></div>
        </div>
        <p class="weui_toast_content">加载中</p>
      </div>
    </div>
    <div v-else class="weui_loading" :class="{'bg_white': toFff}">
      <div class="weui_loading_leaf weui_loading_leaf_0"></div>
      <div class="weui_loading_leaf weui_loading_leaf_1"></div>
      <div class="weui_loading_leaf weui_loading_leaf_2"></div>
      <div class="weui_loading_leaf weui_loading_leaf_3"></div>
      <div class="weui_loading_leaf weui_loading_leaf_4"></div>
      <div class="weui_loading_leaf weui_loading_leaf_5"></div>
      <div class="weui_loading_leaf weui_loading_leaf_6"></div>
      <div class="weui_loading_leaf weui_loading_leaf_7"></div>
      <div class="weui_loading_leaf weui_loading_leaf_8"></div>
      <div class="weui_loading_leaf weui_loading_leaf_9"></div>
      <div class="weui_loading_leaf weui_loading_leaf_10"></div>
      <div class="weui_loading_leaf weui_loading_leaf_11"></div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import 'assets/scss/loading.scss';

  export default {
    props: {
      toFff: {
        type: Boolean,
        default: true
      },
      task: {
        type: Boolean,
        default: true
      }
    }
  }
</script>

