<!-- 问题反馈 by fenglei -->
<template>
  <div class="page" id="question">
    <div class="question">
      <div class="item-title">意见标题</div>
      <input type="text" class="question-title" maxlength="50" v-model="params.title" placeholder="请输入意见标题">
      <div class="item-title">意见描述</div>
      <mt-field placeholder="请详细描述您的问题或建议，我们将及时跟进解决" v-model="params.remark" type="textarea" rows="4" :attr="{ maxlength: 200 }">
        <div class="mint-cell-right">{{ params.remark.length }}/200</div>
      </mt-field>
      <!--<div class="item-title">验证码</div>
      <input type="text" class="question-code" maxlength="4" v-model="params.imgValidCode" placeholder="请输入验证码">
      <div class="codeImg">
        <img @click="refreshCodeImg" :src="codeImgUrl">
      </div>-->
      <mt-button v-if="params.title && params.remark" @click="confirmSub" type="danger" size="large" class="confirm-btn">提交</mt-button>
      <mt-button v-else type="default" size="large" class="confirm-btn" disabled>提交</mt-button>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';
  import RdField from '../../components/FieldInput'; // 引入公共表单控件组件
  import qs from 'qs';

  export default {
    name: 'question',
    data() {
      return {
        //codeImgUrl: 'http://10.10.1.115:8082/app/open/validimg.html',
        params: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          title: '', // 意见标题
          remark: '' // 意见描述
          //imgValidCode: '' // 验证码
        }
      }
    },
    mounted() {
      let st = document.getElementsByClassName('mint-field-core')[0];
      st.style.height = '1.6rem';
      st.style.fontFamily = 'inherit';
    },
    methods: {
      // 单击刷新验证码
      /*refreshCodeImg() {
        var timestamp = Date.parse(new Date());
        this.codeImgUrl = this.codeImgUrl + '?timestamp=' + timestamp;
      },*/
      // 提交验证
      confirmSub() {
        if (!this.params.title) {
          this.$toast('请输入意见标题');
        } else if(!this.params.remark) {
          this.$toast('请输入意见描述');
        /*} else if(!this.params.imgValidCode) {
          this.$toast('请输入验证码');*/
        } else {
          this.$http.post(ajaxUrl.opinionAddSave,qs.stringify(this.params)).then((res) => {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '确认',
              showCancelButton: false,
              message: res.data.resMsg
            }).then(action => {
              if (action == 'confirm') {
                if (res.data.resCode == 39321) {
                  this.$router.go(-1);
                }
              }
            });
          })
        }
      }
    },
    components: { RdField }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/css/question_feedback.sass";
</style>
