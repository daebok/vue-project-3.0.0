<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="修改登录密码">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <form id="form_pwd">
        <ul>
          <li class="margin-t-20">
            <rd-field :type="changeType" v-model="loginPwd" label="原密码" placeholder="原登录密码" @changeEye="hideShowPwd" specInput="pwdInput" switchEye :attr="{maxlength: 16}" name="pwd"></rd-field>
          </li>
          <li>
            <rd-field :type="changeType2" v-model="newLoginPwd" label="新密码" placeholder="8-16位字符，包含数字和字母" @changeEye="hideShowPwd2" specInput="pwdInput" switchEye :attr="{maxlength: 16}"></rd-field>
          </li>
          <li>
            <rd-field :type="changeType3" v-model="confirmPwd" label="确认密码" placeholder="再次输入新登录密码" @changeEye="hideShowPwd3" specInput="pwdInput" switchEye :attr="{maxlength: 16}"></rd-field>
          </li>
        </ul>
      </form>
      <div class="margin-lr-15 margin-t-30">
        <mt-button v-if="loginPwd && newLoginPwd && confirmPwd" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">确定修改</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确定修改</mt-button>
      </div>
    </div>
  </div>
</template>
<script>
  import Loading from '../../../components/Loading.vue'
  import RdField from '../../../components/FieldInput.vue'
  import validator from '../../../mixins/formValidator'
  import base64code from '../../../mixins/base64code'
  import * as ajaxUrl from '../../../ajax.config'
  import qs from 'qs'
  export default {
    data(){
      return {
        changeType: 'password',
        changeType2: 'password',
        changeType3: 'password',
        loginPwd:'',
        newLoginPwd:'',
        confirmPwd:'',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    mixins: [validator,base64code],
    methods: {
      hideShowPwd(arg){
        if(arg == '0'){
          this.changeType = 'password'
        }else{
          this.changeType = 'text'
        }
      },
      hideShowPwd2(arg){
        if(arg == '0'){
          this.changeType2 = 'password'
        }else{
          this.changeType2 = 'text'
        }
      },
      hideShowPwd3(arg){
        if(arg == '0'){
          this.changeType3 = 'password'
        }else{
          this.changeType3 = 'text'
        }
      },
      confirmSub(){// 确认提交
        if(this.regexPwd(this.newLoginPwd)){
          if(this.confirmPwd != this.newLoginPwd){
            this.$toast('确认密码和新密码不一致')
            return ;
          }
          let params = {
            confirmPwd: this.base64Encode(this.confirmPwd),
            loginPwd: this.base64Encode(this.loginPwd),
            newLoginPwd: this.base64Encode(this.newLoginPwd),
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid
          }
          this.$http.post(ajaxUrl.doModifyPwd, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg)
            if (res.data.resMsg == '登录密码修改成功') {
              this.$router.go(-1)
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
