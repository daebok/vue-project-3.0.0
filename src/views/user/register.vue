<!-- 注册 -->
<template>
  <div class="">
    <div>
      <mt-header class="bar-nav" title="注册" >
        <mt-button slot="left" icon="back" v-back-link></mt-button>
        <router-link to="/login" slot="right">
          <mt-button class="main-color">登录</mt-button>
        </router-link>
      </mt-header>
      <div class="form-area">
        <form id="form_reg">
          <ul>
            <li class="margin-t-20">
              <rd-field type="tel" v-model="reg.mobile" label="手机号码" placeholder="请输入手机号码" name="mobile" class="rd-login"></rd-field>
            </li>
            <li>
              <rd-field type="tel" v-model="reg.code" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码" name="code" specInput="codeInput">
                <button v-if="!reg.mobile" class="yzm color-999 aui-border-l ellipsis" disabled>获取验证码</button>
                <button v-else @click.prevent="getCode" class="yzm aui-border-l ellipsis">获取验证码</button>
              </rd-field>
            </li>
            <li>
              <rd-field :type="changeType" v-model="reg.pwdVal" label="登录密码" placeholder="8-16位字符，包含数字和字母" @changeEye="hideShowPwd"
                        specInput="pwdInput" switchEye :attr="{maxlength: 16}" name="pwd"></rd-field>
            </li>
            <li>
              <rd-field type="tel" v-model="reg.inviter" label="邀请人" placeholder="请输入邀请人手机号(选填)" name="inviter"></rd-field>
            </li>
          </ul>
          <div class="btn-con">
            <div class="protocol-box">
            <span @click="switchImg">
                <span v-if="switch_status">
                  <img src="../../assets/images/public/protocol_s.png" />
                </span>
                <span v-else>
                  <img  src="../../assets/images/public/protocol_n.png" />
                </span>
            </span>
              我已阅读并同意
              <a v-for="item in protocolList" @click="readProtocol(item.id,item.name)" class="protocol main-color">《{{item.name}}》</a>
            </div>
            <mt-button v-if="reg.mobile && reg.code && reg.pwdVal" @click.prevent="checkForm" type="danger" size="large">
              <loading v-if="submitData" :task="false"></loading>
              <span v-else>注册</span>
            </mt-button>
            <mt-button v-else type="default" size="large" disabled>注册</mt-button>
          </div>
        </form>
      </div>
    </div>
    <!-- S注册协议内容隐藏域 -->
    <div class="protocol-con">
      <mt-popup v-model="popupVisible" position="bottom" class="mint-popup-3" :modal="false">
        <mt-header class="bar-nav" :title="protocolTitle" >
          <mt-button slot="right" @click="popupVisible = false">关闭</mt-button>
        </mt-header>
        <div class="con" :style="{ height: protocolHeight + 'px' }"><section v-html="protocolHtml"></section></div>
      </mt-popup>
    </div>
    <!-- E注册协议内容隐藏域 -->
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../components/Loading.vue'; // 引入loading组件
  import RdField from '../../components/FieldInput.vue'; // 引入公共表单控件组件
  import validator from '../../mixins/formValidator.js'; // 引入公共表单校验规则
  import getCodeTime from '../../mixins/getCodeTime.js'; // 引入获取验证码倒计时公共组件
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import * as ajaxUrl from '../../ajax.config.js'; // 引入所有接口地址
  import qs from 'qs';

  export default {
    data() {
      return {
        switch_status: true, //是否同意注册协议，true已同意，false不同意
        submitData: false, // 注册数据是否提交，true已提交，fasle未提交
        submitFlag: true, // 防重复提交，true正在提交，false提交完毕
        reg: { // 注册表单数据包对象
          mobile: '', // 手机号
          code: '', // 手机验证码
          pwdVal: '', // 密码
          inviter: '', // 邀请人
          agree: 1 // 是否同意注册协议，1已同意，0不同意
        },
        changeType: 'password', // 密码输入框类型，password密码框，text文本框
        popupVisible: false, // 注册协议内容隐藏域是否显示，true显示，false不显示
        protocolList:[], // 注册协议列表
        protocolTitle: '注册协议', // 注册协议内容隐藏域标题
        protocolHtml:'', // 注册协议内容隐藏域协议正文内容
        protocolHeight: 0
      };
    },
    computed: {
      agree() {
        return this.switch_status ? 1 : 0;
      }
    },
    created() {
      this.$http.get(ajaxUrl.registerProtocol).then((res) => {
        this.protocolList = res.data.resData.list;
      })
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
    },
    mixins: [validator, getCodeTime, base64code],
    methods: {
      // 密码框明文密文转换
      hideShowPwd(arg) {
        if(arg === '0'){
          this.changeType = 'password'
        }else{
          this.changeType = 'text'
        }
      },
      // 同意注册协议按钮点击切换
      switchImg() {
        this.switch_status = !this.switch_status;
      },
      // 获取手机短信验证码
      getCode(evt) {
        if (this.regexPhone(this.reg.mobile)) {
          let params = { 'mobile': this.reg.mobile };
          this.$http.get(ajaxUrl.registerPhoneCode, { params: { 'mobile': this.reg.mobile }}).then((res) => {
            if (res.data.resMsg === '验证码已发送') {
              this.getCodeTime(evt);
            } else if (res.data.resCode === 2) {
              this.$messagebox({
                title: ' ',
                cancelButtonText: '重新输入',
                confirmButtonText: '立即登录',
                showCancelButton: true,
                closeOnClickModal: false,
                message: res.data.resMsg
              }).then(action => {
                if (action === 'confirm') {
                  this.$router.push('/login');
                } else if (action === 'cancel') {
                  this.reg.mobile = '';
                  let mobileInput = document.querySelector('input[name="mobile"]');
                  mobileInput.focus();
                }
              });
            } else {
              this.$toast(res.data.resMsg);
            }
          })
        }
      },
      // 注册表单提交校验
      checkForm(e) {
        if (this.regexPhone(this.reg.mobile) && this.regexPwd(this.reg.pwdVal)) {
          if (!this.agree) {
            this.$toast('需要您同意注册协议才能注册');
            return;
          }
          if (!this.submitFlag) {
            return;
          }
          this.submitFlag = false; // 防重复提交
          this.submitData = true;
          let pwd = this.base64Encode(this.reg.pwdVal);
          let submitParams = Object.assign(this.reg, { pwd: pwd });
          let self = this;
          this.$http.post(ajaxUrl.registerFirst, qs.stringify(submitParams)).then((res) => {
            if (res.data.resData !== '') {
              let param = {
                code: self.reg.code,
                __sid: res.data.resData.__sid
              };
              // 第二步提交code和__sid
              self.$http.post(ajaxUrl.doRegister, qs.stringify(param)).then((res) => {
                if (res.data.resData !== '') {
                  let user = {
                    userName: this.reg.mobile,
                    loginName: res.data.resData.username,
                    userId: res.data.resData.userId,
                    avatarPhoto: res.data.resData.avatarPhoto,
                    __sid: res.data.resData.__sid
                  };
                  self.$router.push('/register/regSucc');
                  // 存储user信息到本地
                  localStorage.user = JSON.stringify(user);
                  self.$store.dispatch('login', user);
                } else {
                  self.submitData = false;
                  self.submitFlag = true;
                  self.$toast(res.data.resMsg);
                }
              })
             } else {
              self.submitData = false;
              self.submitFlag = true;
              if(res.data.resCode === 2) {
                this.$messagebox({
                  title: ' ',
                  cancelButtonText: '重新输入',
                  confirmButtonText: '立即登录',
                  showCancelButton: true,
                  closeOnClickModal: false,
                  message: res.data.resMsg
                }).then(action => {
                  if (action === 'confirm') {
                    self.$router.push('/login');
                  } else if (action === 'cancel') {
                    self.reg.mobile = '';
                    let mobileInput = document.querySelector('input[name="mobile"]');
                    mobileInput.focus();
                  }
                });
              }else{
                self.$toast(res.data.resMsg);
              }
            }
          })
        }
      },
      // 查看注册协议
      readProtocol(arg,arg2) {
        this.popupVisible = true;
        this.protocolTitle = arg2;
        this.$http.get(ajaxUrl.registerProtocolDetail,{ params: { protocolId: arg }}).then((res) => {
          this.protocolHtml = res.data.resData.content;
        })
      }
    },
    components: { Loading, RdField }
  }
</script>

<style type="text/css" scoped>
  .bar-nav .title {  font-size:18px; }
  .btn-con {padding:0 .15rem;margin-top: .35rem;}
  .protocol-box img {width: .14rem;  vertical-align: -2px;}
  .form-area ul li .yzm { color: #F95A28;}
</style>
