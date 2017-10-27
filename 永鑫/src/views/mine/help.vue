<!-- 帮助中心一级列表 by fenglei -->
<template>
  <div class="page">
    <ul v-if="list.length > 0" class="help_f1">
      <li v-for="(item1, index) in list" class="help_f1_li">
        <div class="top" :class="{ showed: item1.sectionList.length > 0 }" @click="showButtom(item1.sectionList.length, index + 1, item1.sectionCode)" :ref="index + 1">
          <span>{{ item1.sectionName }}</span>
          <img v-if="item1.sectionList.length > 0" src="./../../assets/images/public/arrow_right.png" class="right-img">
        </div>
        <div class="buttom" :id="'but' + (index + 1)">
          <ul :id="'ul' + (index + 1)">
            <li v-for="item2 in item1.sectionList" @click="linkTo(item2.sectionCode)">{{ item2.sectionName }}</li>
          </ul>
        </div>
      </li>
    </ul>
    <div class="no-data" v-show="noData">
      <img src="../../assets/images/public/default/default_icon_no_notice.png">
      <p>暂无信息</p>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';

  export default {
    name: 'help',
    data() {
      return {
        list: [],
        noData: false
      };
    },
    created() {
      this.$http.get(ajaxUrl.helpCenter, { params: { sectionCode: 'help' }}).then((res) => {
        if (res.data.resData) {
          if (res.data.resData.sectionList.length > 0) {
            this.list = res.data.resData.sectionList;
          } else {
            this.noData = true;
          }
        }
      })
    },
    methods: {
      // 箭头翻转与下拉动画
      showButtom(length, ref, sectionCode) {
        if (length > 0) {
          let target = this.$refs[ref][0];
          let height = document.getElementById('ul' + ref).offsetHeight;

          if (target.className.indexOf('showed') >= 0) {
            target.className = 'top';
            target.children[1].className = 'right-img rotate-up';
            document.getElementById('but' + ref).style.height = height + 'px';
          } else {
            target.className = 'top showed';
            target.children[1].className = 'right-img';
            document.getElementById('but' + ref).style.height = 0;
          }
        } else {
          this.linkTo(sectionCode);
        }
      },
      linkTo(sectionCode) {
        this.$router.push({ name: 'helpColumn', query: { 'code': sectionCode }});
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/sass/help.sass";
</style>
