<!-- 帮助中心 -->
<template>
  <div class="page" id="help">
    <mt-header class="bar-nav" title="帮助中心">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <mt-cell v-for="item in list" :title="item.sectionName" :to="'/mine/help/help_column?code='+item.sectionCode" is-link></mt-cell>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    name: 'help',
    data() {
      return {
        list: ''
      };
    },
    created() {
      this.$http.get(ajaxUrl.helpCenter).then((res) => {
        this.list = res.data.resData.sectionlist;
      })
    }
  }
</script>
