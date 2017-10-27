<!-- 设置支付密码 by fenglei -->
<template>
  <mt-popup v-model="flag" position="bottom" class="setPayPwd" :closeOnClickModal="false">
    <div class="pay-box">
      <div class="pay-title">
        <img @click="close" src="../../assets/images/fund/btn_close.png">
        <span class="title">设置支付密码</span>
      </div>
      <div class="pay-num">
        <span class="text">不少于8位，至少包含一个字母和一个数字</span>
      </div>
      <div class="pay-input">
        <input class="pwd" type="password" ref="input" v-model="password" maxlength="16">
      </div>
      <div class="operator">
        <mt-button v-show="!password" size="large" type="default" disabled>确认</mt-button>
        <mt-button v-show="password" size="large" type="danger" @click="submitPwd">
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
  import validator from '../../mixins/formValidator.js';
  import base64code from '../../mixins/base64code.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        loading: false,
        phone: this.$store.state.user.userName,
        password: '',
        changeType: 'password'
      };
    },
    props: {
      flag: {
        type: Boolean,
        default: false
      }
    },
    mixins: [ validator, base64code ],
    components: { Loading },
    methods: {
      showLoad() {
        this.password = '';
        this.loading = false;
      },
      close(){
        history.back();
      },
      submitPwd() {
        if (this.regexPwd(this.password)) {
          this.loading = true;
          let params = {
            newPayPwd: this.base64Encode(this.password),
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid
          };
          this.$http.post(ajaxUrl.updatePayPwd, qs.stringify(params)).then((res) => {
            if (res.data.resCode == 39321) {
              this.$emit('setPayPwdOver');
            } else {
              this.loading = false;
              this.$toast(res.data.resMsg);
            }
          })
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "setPayPwd.sass";
</style>
