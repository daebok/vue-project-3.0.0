<template>
  <div class="page" :class="{'bg-fff': $route.query.realnameStatus}">
    <mt-header class="bar-nav aui-border-b" title="托管账户">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area" v-if="!$route.query.realnameStatus">
      <div class="warm-tip">
        <h5 class="color-666">温馨提示：</h5>
        <p class="color-999 margin-t-10">为了保障您的资金安全，请填写真实有效的证件信息。</p>
      </div>
      <form :action="submitUrl" id="formId" method="post">
        <ul class="margin-t-10">
          <li>
            <!--<div class="rd-field">
              <span class="rd-label">真实姓名</span>
              <input placeholder="请输入真实姓名" name="realName" type="text" class="rd-field-core" v-model="realname" maxlength="10" style="top:.1rem;">
            </div>-->
            <rd-field type="text" name="realName" v-model="realname" label="真实姓名" :attr="{maxlength: 10}" placeholder="请输入真实姓名"></rd-field>
          </li>
          <li>
            <rd-field type="text" name="idNo" v-model="idNo" label="身份证号" :attr="{maxlength: 18}" placeholder="请输入身份证号码"></rd-field>
          </li>
          <input type="hidden" name="userId" :value="getParams.userId">
          <input type="hidden" name="__sid" :value="getParams.__sid">
        </ul>
      </form>
      <div class="margin-t-15 margin-lr-15">
        <mt-button v-if="realname && idNo" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">开通托管账户</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>开通托管账户</mt-button>
      </div>
    </div>
    <!--已经实名认证-->
    <div class="realname-result" v-else>
      <div class="rel-img">
        <img src="../../../assets/images/me/tips_binded.png" />
      </div>
      <div class="rel-msg">已经绑定汇付天下账户</div>
      <div class="rel-account">{{$route.query.tppUser}}</div>
      <div class="btn-box margin-lr-15">
          <mt-button type="danger" size="large" @click.native="tppLogin" class="confirm-btn">进入汇付天下</mt-button>
      </div>
    </div>
  </div>
</template>
<script>
  import Loading from '../../../components/Loading.vue'
  import RdField from '../../../components/FieldInput.vue'
  import validator from '../../../mixins/formValidator'
  import * as ajaxUrl from '../../../ajax.config'
  import qs from 'qs'
  export default {
    data(){
      return {
        submitUrl: ajaxUrl.tppRegister,
        realname: '',
        idNo: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
      }
    },
    mixins: [validator],
    methods: {
      confirmSub(){
        if(this.chName(this.realname)){
          if(this.regexIdCard(this.idNo)){
            document.getElementById('formId').submit()
            if(this.$route.query.from){
              if(this.$route.query.from == 'account'){
                sessionStorage.from_account = this.$route.query.from
              }else{
                sessionStorage.from_id = this.$route.query.from +','+ this.$route.query.id
              }

            }
          }else{
            this.$toast({message:'请输入正确的身份证号'})
          }
        }
      },
      tppLogin(){
        window.location.href = ajaxUrl.apiLogin + '?__sid=' + this.getParams.__sid + '&userId='+this.getParams.userId
      }
    },
    components: { Loading, RdField }
  }
</script>

<style scoped>
  .bg-fff {background:#fff;}
  .warm-tip {padding: .1rem .15rem 0;font-size: .12rem; }
  .warm-tip h5 {line-height:1;}
  .rel-img{
    text-align: center;
    padding-top: .7rem;
  }
  .rel-img img{
    width: .9rem;
  }
  .rel-msg,.rel-account{
    text-align: center;
    line-height: 1;
    color: #333;
  }
  .rel-msg{
    font-size: .15rem;
    margin-top: .2rem;
  }
  .rel-account{
    font-family: arial;
    font-size: .18rem;
    margin-top: .16rem;
    font-weight: 600;
  }
  .btn-box { margin-top: .3rem;}
</style>
