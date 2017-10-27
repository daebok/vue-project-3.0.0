<!-- 问题反馈 -->
<template>
  <div class="page" id="question">
    <mt-header class="bar-nav" title="问题反馈" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
      <mt-button slot="right" class="right-icon main-color" @click.native="submitAjax">提交</mt-button>
    </mt-header>
    <div class="question">
      <mt-field placeholder="请在这里描述你所遇到的问题，非常感谢您的反馈！" v-model="con" type="textarea" rows="4" :attr="{maxlength: 200}">
        <div class="mint-cell-right">{{ con.length }}/200</div>
      </mt-field>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import qs from 'qs';

  export default {
    name: 'question',
    data() {
      return {
        con: ''
      }
    },
    mounted() {
      let st = document.getElementsByClassName('mint-field-core')[0];
      st.style.height = '1.6rem';
    },
    methods: {
      submitAjax() {
        if (!this.con) {
          this.$toast('请输入反馈信息');
          return;
        }
        if (this.con.trim() == '') {
          this.$toast('请输入反馈信息');
          return;
        }
        let params = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          remark: this.con
        };
        this.$http.post(ajaxUrl.opinionAddSave,qs.stringify(params)).then((res) => {
          this.$toast('问题反馈成功！');
          this.$router.go(-1);
        })
      }
    }
  }
</script>

<style type="text/css" scoped>
  .question .mint-field-core {
    font-size: .15rem;
  }
  .mint-cell-right {
    height: auto;
    right: .7rem;
    top: 1.5rem;
    font-size: .15rem;
    color: #999;
  }
  .mint-button-text{
    font-size: .15rem;
    color: #999;
  }
</style>
