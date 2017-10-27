<template>
  <div class="page">
    <div class="form-area">
      <form action="">
        <ul>
          <li class="margin-t-20">
            <rd-field type="tel" v-model="getParams.mobile" label="手机号码" placeholder="请输入手机号码" name="mobile" class="rd-login"></rd-field>
          </li>
          <li>
            <rd-field type="tel" class="get-code" v-model="code" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码">
              <button v-if="!getParams.mobile" class="yzm color-999 aui-border-l ellipsis" disabled>获取验证码</button>
              <button v-else @click.prevent="getCode" class="yzm aui-border-l ellipsis">获取验证码</button>
            </rd-field>
          </li>
        </ul>
      </form>
      <div class="margin-lr-15">
        <mt-button v-if="code" type="danger" size="large" @click.native="confirmSub" class="confirm-btn">下一步</mt-button>
        <mt-button v-else type="default" disabled size="large" class="confirm-btn">下一步</mt-button>
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
        code: '',
        getParams: {
          mobile: '',
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        // 获取验证码
        if (this.regexPhone(this.getParams.mobile)) {
          this.$http.get(ajaxUrl.findPayPwdPhoneCode, {params: this.getParams}).then((res) => {
            if (res.data.resCode == 39321) {
              this.getCodeTime(evt, 60);
            } else {
            this.$toast(res.data.resMsg)
            }
          })
        }
      },
      confirmSub(){
        // 修改校验
        let params = Object.assign(this.getParams, {code: this.code})
        this.$http.post(ajaxUrl.checkPayPwdPhoneCode, qs.stringify(params)).then((res) => {
          if (res.data.resCode == 39321) {
            this.$router.push({ name: 'setPwd', query: { forgetPwd: true }})
          } else {
            this.$toast(res.data.resMsg)
          }
        })
      }
    },
    components: { Loading, RdField }
  }
</script>

<style scoped>
  .bind-box { margin: .2rem .15rem;}
</style>
