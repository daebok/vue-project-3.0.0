<!-- 帮助中心 -->
<template>
  <div class="page" id="help">
    <mt-header v-if="!hideHeader" class="bar-nav" title="帮助中心">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <mt-cell v-for="item in list" :key="item.id" :title="item.sectionName" @click.native="toStep(item.sectionCode)" is-link></mt-cell>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'
  import hideHead from '../../mixins/hideHeader'
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
    },
    methods: {
      toStep(code) {
        let str = '/mine'
        if(this.hideHeader) {
          str = '/appH5'
        }
        this.$router.push(str + '/help/help_column?code=' + code)
      }
    },
    mixins: [ hideHead ]
  }
</script>
