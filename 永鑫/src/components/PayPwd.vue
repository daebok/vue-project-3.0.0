<template>
  <mt-popup v-model="flag" position="bottom" class="box">
    <div class="pwd-box" >
      <div class="title aui-border-b"><span @click="close" class="float-left"><img src="../assets/images/fund/btn_close.png" ></span>支付密码</div>
      <div class="pay-num">{{tipTxt}}(元) <span class="float-right">{{tipMoney | currency('', 2)}}</span></div>
      <div>
        <input class="pwd" type="password" ref="input" :value="currentValue" @input="handleInput" >
      </div>
      <div class="forget-pwd">
        <router-link :to="`/mine/safe/modifyPwd?phone=${$store.state.user.userName}`">
        <span>忘记支付密码</span>
        </router-link>
      </div>
      <div class="operator">
        <mt-button v-show="!currentValue" size="large" type="default" disabled>确认</mt-button>
        <mt-button v-show="currentValue" size="large" type="danger" @click="submitPwd">
          <loading v-if="loading" :task="false"></loading>
          <span v-else>确认</span>
        </mt-button>
      </div>
    </div>
  </mt-popup>
</template>
<script>
  import Loading from '../components/Loading.vue'
  export default {
    data() {
      return {
        flag: false,
        loading: false,
        currentValue: this.value
      }
    },
    props: {
      tipTxt:{ type: String, default: '需付款'},
      tipMoney: [Number,String]
    },
    methods: {
      show(){
        this.currentValue = '' // 打开密码框清空原有的密码
        this.flag = true
      },
      close(){
        this.loading = false;
        this.flag = false
      },
      handleInput(evt) {
        let formatValue = evt.target.value;
        this.$refs.input.value = formatValue;
        this.currentValue = formatValue;
      },
      submitPwd() {
        this.loading = true;
        this.$emit('submit')
        this.close();
      }
    },
    watch: {
      value(val) {
        this.currentValue = val
      },
      currentValue(val) {
        this.$emit('input', val);
      }
    },
    components: { Loading }
  }
</script>
<style lang="sass" rel="stylesheet/sass" scoped>
  .box
    width: 100%
  .pwd-box
    height: 2.75rem
    background: #f5f5f5
    padding: 0 .15rem
    .title
      text-align: center
      line-height: .45rem
      font-size: .18rem
      color: #333
      img
        width: .14rem
    .pay-num
      line-height: .5rem
      color: #666
      font-size: .13rem
      span
        color: #333
        font-size: .21rem
    .pwd
      height: .56rem
      border: 1px solid #ccc
      width: 100%
      padding-left: .1rem
      -webkit-appearance: none
    .forget-pwd
      font-size: .13rem
      color: #00a0e9
      margin-top: .1rem
      text-align: right
    .operator
      margin-top: .2rem
</style>
