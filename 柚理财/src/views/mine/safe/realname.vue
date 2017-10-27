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
      <form id="formId" method="post">
        <ul class="margin-t-10">
          <li>
            <rd-field type="text" name="realName" v-model="realName" label="真实姓名" :attr="{maxlength: 10}" placeholder="请输入真实姓名"></rd-field>
          </li>
          <li>
            <rd-field type="text" name="idNo" v-model="idNo" label="身份证号" :attr="{maxlength: 18}" placeholder="请输入身份证号码"></rd-field>
          </li>
          <li>
            <rd-field type="text" name="bankNo" v-model="bankNo" label="银行卡号" placeholder="仅支持本人持有的储蓄卡"></rd-field>
          </li>
          <li>
            <rd-field type="text" label="绑定手机" :attr="{value: mobile}" readonly></rd-field>
          </li>
          <li>
            <rd-field type="tel" name="smsCode" v-model="smsCode" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码">
              <button @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
            </rd-field>
          </li>
          <input type="hidden" name="userId" :value="getParams.userId">
          <input type="hidden" name="__sid" :value="getParams.__sid">
          <!-- 账户用途 -->
          <input type="hidden" name="acctUse" :value="00000">
        </ul>
      </form>
      <div class="margin-t-15 margin-lr-15">
        <mt-button v-if="realName && idNo && bankNo && smsCode" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">开通托管账户</mt-button>
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
  import getCodeTime from '../../../mixins/getCodeTime'
  import * as ajaxUrl from '../../../ajax.config'
  import qs from 'qs'
  export default {
    data(){
      return {
        submitUrl: ajaxUrl.tppRegister,
        realName: '',
        idNo: '',
        bankNo: '',
        smsCode: '',
        smsClick: false,
        userInfo: '',
        mobile: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
      }
    },
    mixins: [validator, getCodeTime],
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.basicInfo, {params:this.getParams}).then((res) => {
        this.$indicator.close()
        if(res.data.resData != ''){
          this.userInfo = res.data.resData;
          this.mobile = this.userInfo.mobile;
        }else{
          this.$toast(res.data.resMsg)
        }
      })
    },
    methods: {
      //获取验证码
      getCode (evt) {
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.smsClick = true;
        let smsParams = { 
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          srvTxCode: 'accountOpenPlus', //业务交易代码 
          reqType: 1 //请求类型
        }
        this.$http.get(ajaxUrl.smsCodeApply, {params: smsParams}).then((res) => {
          this.$indicator.close()
          if (res.data.resCode == '39321') { //成功
            this.getCodeTime(evt, 60)
          }else {
            this.$toast(res.data.resMsg)
          }
        })
      },
      confirmSub(){
        if(this.chName(this.realName)){
          if(this.regexIdCard(this.idNo)){
            if(this.smsClick){ //点击验证码判断
              this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
              //开户请求
              let tppParams = { 
                userId: this.$store.state.user.userId,
                __sid: this.$store.state.user.__sid,
                acctUse: '00000',
                bankNo: this.bankNo,
                idNo: this.idNo,
                realName: this.realName,
                smsCode: this.smsCode
              }
              this.$http.get(ajaxUrl.tppRegister, {params: tppParams}).then((res) => {
                this.$indicator.close()
                if (res.data.resCode == '39321') {
                  this.$messagebox({
                    title: ' ',
                    showCancelButton: false,
                    confirmButtonText: '知道了',
                    closeOnClickModal: false,
                    message: res.data.resMsg
                  }).then(action => {
                    if(action == 'confirm'){
                      this.$router.push('/mine/safe?from=realName');
                    }
                  });
                }else {
                  this.$toast(res.data.resMsg)
                }
              })
              //从哪里来
              if(this.$route.query.from){
                if(this.$route.query.from == 'account'){
                  sessionStorage.from_account = this.$route.query.from
                }else{
                  sessionStorage.from_id = this.$route.query.from +','+ this.$route.query.id
                }
              }
            }else{
              this.$toast({message:'请先点击获取验证码'})
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