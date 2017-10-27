<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="添加银行卡">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <div class="warm-tip">
        <h5 class="color-666">温馨提示：</h5>
        <p class="color-999 margin-t-10">请添加相同姓名的银行卡,</p>
        <p class="color-999 margin-t-10">且银行预留号码与绑定手机号保持一致。</p>
      </div>
      <ul class="margin-t-10">
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
      </ul>
      <div class="margin-t-15 margin-lr-15">
        <mt-button v-if="bankNo && smsCode" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">确认添加</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确认添加</mt-button>
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
        this.userInfo = res.data.resData;
        this.mobile = this.userInfo.mobile;
        if(this.userInfo == '') return;
        this.$indicator.close()
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
          srvTxCode: 'cardBindPlus', //业务交易代码 
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
        if(this.smsClick){ //点击验证码判断
          this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
          //添加银行卡
          let bindCardParams = { 
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid,
            cardId: this.bankNo, //银行卡号
            smsCode: this.smsCode
          }
          this.$http.get(ajaxUrl.bindCard, {params: bindCardParams}).then((res) => {
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
                  this.$router.push('/mine/safe/bank?from=addBank');
                }
              });
            }else {
              this.$toast(res.data.resMsg)
            }
          })
        }else{
          this.$toast({message:'请先点击获取验证码'})
        }
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