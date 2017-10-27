<!-- 输入支付密码 by fenglei -->
<template>
  <mt-popup v-model="flag" position="bottom" class="payPwd" :closeOnClickModal="false">
    <div class="pay-box">
      <div class="pay-title">
        <img @click="close" src="../../assets/images/fund/btn_close.png">
        <span class="title">支付密码</span>
      </div>
      <div class="pay-num">
        <span class="text">需付款(元)</span>
        <span class="value">{{ tipMoney | currency('', 2) }}</span>
      </div>
      <div class="pay-input">
        <input class="pwd" type="password" ref="input" v-model="password" maxlength="16">
      </div>
      <div class="forget-pwd">
        <span @click="forgetPayPwd">忘记支付密码</span>
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
  import Loading from '../../components/Loading.vue';

  export default {
    data() {
      return {
        loading: false,
        password: ''
      }
    },
    props: {
      tipMoney: [ Number, String ],
      flag: {
        type: Boolean,
        default: false
      }
    },
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
        this.loading = true;
        this.$emit('submit', this.password);
        this.close();
      },
      forgetPayPwd() {
        this.$emit('forget');
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "payPwd.sass";
</style>
