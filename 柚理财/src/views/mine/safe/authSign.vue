<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="业务授权">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <form :action="submitUrl" method="post" id="signForm">
        <ul>
          <p class="bind-box">已绑定手机号  {{userInfo.mobile}}</p>
          <li>
            <rd-field type="tel" class="get-code" v-model="code" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码" name="smsCode" specInput="codeInput">
              <button ref="sendCode" @click.prevent="getCode" class="yzm aui-border-l ellipsis">获取验证码</button>
            </rd-field>
          </li>
          </ul>
          <input type="hidden" name="__sid" :value="getParams.__sid">
          <input type="hidden" name="userId" :value="getParams.userId">
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
        submitUrl: ajaxUrl.authSign,
        code: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        userInfo: '',
        repeatFlag: true,
        smsClick: false
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.basicInfo, {params:this.getParams}).then((res) => {
        this.$indicator.close();
        this.userInfo = res.data.resData;
      })
    },
    mixins: [validator, getCodeTime],
    methods: {
      getCode (evt) {
        // 获取验证码
        this.smsClick = true;
        let smsParams = { 
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          srvTxCode: 'autoBidAuthPlus', //业务交易代码 
          reqType: 1 //请求类型
        }
        this.$http.get(ajaxUrl.smsCodeApply, {params: smsParams}).then((res) => {
          if (res.data.resCode == '39321') { //成功
            this.getCodeTime(evt, 60)
          } else {
            this.$toast(res.data.resMsg)
          }
        })
      },
      confirmSub(){ // 业务授权
        if(this.smsClick){
          if(this.repeatFlag){
            this.repeatFlag = false  // 防重复提交
            document.getElementById('signForm').submit()
          }
        }else{
          this.$toast({message:'请先点击获取验证码'})
        }
      }
    },
    components: { Loading, RdField }
  }
</script>

<style scoped>
  .bind-box { margin: .2rem .15rem;}
</style>