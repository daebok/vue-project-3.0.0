<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" :title="titleStr">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <form action="">
        <ul>
          <li class="margin-t-20">
            <rd-field type="tel" v-model="reg.mobileVal" :attr="{maxlength: 11}" label="手机号码" placeholder="请输入手机号码" name="mobile"></rd-field>
          </li>
          <li>
            <rd-field type="tel" v-model="reg.codeVal" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码" name="code" specInput="codeInput">
              <button v-if="!reg.mobileVal" class="yzm color-999 aui-border-l ellipsis" disabled>发送验证码</button>
              <button v-else @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
            </rd-field>
          </li>
          </ul>
        </form>
        <div class="margin-lr-15">
          <mt-button v-if="reg.mobileVal && reg.codeVal" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">确定</mt-button>
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
          mobileVal: '',
          codeVal: ''
        },
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        mobileBindToken: '', //绑定手机标识
      }
    },
    created(){
      this.titleStr = this.$route.query.phoneSign ? '修改手机绑定' : '绑定手机'
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        // 获取验证码
        let params = Object.assign(this.getParams, {mobile: this.reg.mobileVal})
        if(this.regexPhone(this.reg.mobileVal)) {
          this.$http.get(ajaxUrl.bindPhoneCode, {params: params}).then((res) => {
            if (res.data.resMsg == '已发送验证码') {
              this.getCodeTime(evt, 60)
              this.mobileBindToken = res.data.resData.mobileBindToken
            } else {
              this.$toast(res.data.resMsg)
            }
          })
        }
      },
      confirmSub(){
        // 确认提交
        let phone_sign = this.$route.query.phoneSign
        let params = Object.assign(this.getParams, {
          code: this.reg.codeVal,
          mobile: this.reg.mobileVal,
          mobileBindToken: this.mobileBindToken
        })
        if(phone_sign){ //是否修改绑定手机号
          params = Object.assign(params, {modifyPhoneSign: phone_sign})
        }
        if(this.regexPhone(this.reg.mobileVal)) { // 获取验证码
          if(!this.mobileBindToken){
            this.$toast('请先获取验证码')
            return;
          }
          this.$http.post(ajaxUrl.doBindPhone, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg)
            if (res.data.resMsg.match('手机绑定成功')) {
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
