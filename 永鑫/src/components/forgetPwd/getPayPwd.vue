<!-- 忘记支付密码 by fenglei -->
<template>
  <mt-popup v-model="flag" position="bottom" class="getPayPwd" :closeOnClickModal="false">
    <div class="pay-box">
      <div class="pay-title">
        <img @click="close" src="../../assets/images/fund/btn_close.png">
        <span class="title">找回支付密码</span>
      </div>
      <div class="pay-num">
        <span class="text">手机号</span>
        <span class="value">{{ phone }}</span>
      </div>
      <div class="pay-input">
        <input class="pwd" type="tel" ref="input" v-model="code" maxlength="6">
      </div>
      <div class="forget-pwd">
        <button @click="getCode" class="yzm">获取验证码</button>
      </div>
      <div class="operator">
        <mt-button v-show="!code" size="large" type="default" disabled>确认</mt-button>
        <mt-button v-show="code" size="large" type="danger" @click="submitPwd">
          <loading v-if="loading" :task="false"></loading>
          <span v-else>确认</span>
        </mt-button>
      </div>
    </div>
  </mt-popup>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import Loading from '../../components/Loading.vue';
  import getCodeTime from '../../mixins/getCodeTime.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        loading: false,
        phone: this.$store.state.user.userName,
        code: ''
      }
    },
    props: {
      flag: {
        type: Boolean,
        default: false
      }
    },
    mixins: [ getCodeTime ],
    components: { Loading },
    methods: {
      showLoad() {
        this.code = '';
        this.loading = false;
      },
      close(){
        history.back();
      },
      submitPwd() {
        this.loading = true;
        let params = {
          mobile: this.phone,
          code: this.code,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.post(ajaxUrl.checkPayPwdPhoneCode, qs.stringify(params)).then((res) => {
          if (res.data.resCode == 39321) {
            this.$emit('getPayPwdOver');
          } else {
            this.loading = false;
            this.$toast(res.data.resMsg);
          }
        })
      },
      getCode(evt) {
        let params = {
          mobile: this.phone,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.get(ajaxUrl.findPayPwdPhoneCode, { params: params }).then((res) => {
          if (res.data.resCode == 39321) {
            this.getCodeTime(evt, 60);
          } else {
            this.$toast(res.data.resMsg);
          }
        })
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "getPayPwd.sass";
</style>
