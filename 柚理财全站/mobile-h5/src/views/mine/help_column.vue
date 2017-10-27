<!-- 帮助中心详情 -->
<template>
  <div class="page" id="column">
    <mt-header class="bar-nav" :title="titleName">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="link-list aui-border-t">
      <li v-for="(item,index) in list">
        <p class="aui-border-b" @click="showHide(index+1)" :ref="index+1">
          <img src="./../../assets/images/me/icon_question.png" class="left-img">
          <span class="ellipsis">{{item.title}}</span>
          <img src="./../../assets/images/public/arrow_right.png" class="right-img">
        </p>
        <div class="con" hidden>
          <div v-html="item.content"></div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'

  export default {
    data() {
      return {
        list: [],
        titleName: '详情'
      };
    },
    created() {
      this.$http.get(ajaxUrl.helpCenterDetail, { params: { sectionCode: this.$route.query.code }}).then((res) => {
        this.list = res.data.resData.articleList;
        this.titleName = res.data.resData.sectionName;
      })
    },
    methods: {
      showHide(ref){
        let target = this.$refs[ref][0]
        if (target.nextElementSibling.hidden) {
          target.nextElementSibling.hidden = false;
          target.children[2].className = 'right-img rotate-up';
        } else {
          target.nextElementSibling.hidden = true;
          target.children[2].className = 'right-img';
        }
      }
    }
  }
</script>

<style type="text/css" scoped>
  .link-list li p{background-color: #fff;padding: .15rem; font-size: .16rem;}
  .link-list .left-img{width: .15rem; margin-right: .1rem; vertical-align: -4px; }
  .link-list .right-img{float: right; width: .14rem; margin-top: 4px; transform: rotateZ(90deg); transition: .2s ease-out;}
  .link-list .right-img.rotate-up {transform: rotateZ(-90deg);}
  .link-list .con {background:#f5f5f5;padding:.15rem; overflow:hidden;}
</style>
