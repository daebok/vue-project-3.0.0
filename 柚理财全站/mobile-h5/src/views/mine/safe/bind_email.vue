<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="绑定邮箱">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
        <ul>
          <li class="margin-t-20">
            <rd-field type="email" v-model="reg.emailVal" label="电子邮箱" placeholder="请输入电子邮箱" :attr="{maxlength: 50}"></rd-field>
          </li>
          <li>
            <rd-field type="tel" v-model="reg.codeVal" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码">
              <button v-if="!reg.emailVal" class="yzm color-999 aui-border-l ellipsis" disabled>发送验证码</button>
              <button v-else @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
            </rd-field>
          </li>
        </ul>
        <div class="margin-lr-15">
          <mt-button v-if="reg.emailVal && reg.codeVal" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">确定</mt-button>
          <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确定</mt-button>
        </div>
      </div>
  </div>
</template>
<script>
  import Loading from '../../../components/Loading.vue'
  import RdField from '../../../components/FieldInput.vue'
  import validator from '../../../mixins/formValidator'
  import getCodeTime from '../../../mixins/getCodeTime'
  import * as ajaxUrl from '../../../ajax.config'
  import qs from 'qs'
  export default {
    data(){
      return {
        reg: {
          emailVal: '',
          codeVal: ''
        },
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        emailBindToken: '', //绑定邮箱标识
      }
    },
    created(){
      this.titleStr = this.$route.query.emailSign ? '修改邮箱绑定' : '绑定邮箱'
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        if(this.regexEmail(this.reg.emailVal)){ // 获取验证码
          let params = Object.assign(this.getParams, {email: this.reg.emailVal})
          this.$http.get(ajaxUrl.bindEmailCode, {params: params}).then((res) => {
            if (res.data.resMsg == '已发送验证码') {
              this.getCodeTime(evt, 60)
              this.emailBindToken = res.data.resData.emailBindToken
            }else {
              this.$toast(res.data.resMsg)
            }
          })
        }
      },
      confirmSub(){
        // 确认提交
        let email_sign = this.$route.query.emailSign
        let params = Object.assign(this.getParams, {
          code: this.reg.codeVal,
          email: this.reg.emailVal,
          emailBindToken: this.emailBindToken,
          modifyType:0
        })
        if(email_sign){ //是否修改绑定邮箱
          params = Object.assign(params, {modifyEmailSign: email_sign, modifyType: 1})
        }
        if(this.regexEmail(this.reg.emailVal)) { // 获取验证码
          if(!this.emailBindToken){
            this.$toast('请先获取验证码')
            return;
          }
          this.$http.post(ajaxUrl.doBindEmail, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg)
            if (res.data.resMsg == '邮箱绑定成功！') {
              this.$router.go(-2)
            }
          })
        }

      }
    },
    components: { Loading, RdField }
  }
</script>

<style scoped>

</style>
