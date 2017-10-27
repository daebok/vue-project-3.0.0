<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="修改邮箱绑定">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <form action="">
        <ul>
          <p class="bind-box">已绑定邮箱  {{$route.query.email}}</p>
          <li>
            <rd-field type="tel" class="get-code" v-model="code" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码" name="code" specInput="codeInput">
              <button ref="sendCode" @click.prevent="getCode" class="yzm aui-border-l ellipsis">获取验证码</button>
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
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        // 获取验证码
        let modifyParams = Object.assign(this.getParams, {checkType: '02'})
        //this.$http.get(ajaxUrl.modifyEmailCode, {params: this.modifyParams}).then((res) => {
        this.$http.post(ajaxUrl.modifyEmailCode, qs.stringify(modifyParams)).then((res) => {
          if (res.data.resMsg == '验证码已发送') {
          this.getCodeTime(evt, 60)
        } else {
          this.$toast(res.data.resMsg)
        }
      })
      },
      confirmSub(){ // 修改校验
        let params = Object.assign(this.getParams, {code: this.code, checkType: '02'})
        this.$http.post(ajaxUrl.doModifyEmail, qs.stringify(params)).then((res) => {
          if (res.data.resData != '') {
            this.$router.push('/mine/safe/bind_email?emailSign='+res.data.resData.modifyEmailSign)
          }else {
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
