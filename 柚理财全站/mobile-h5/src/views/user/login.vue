<!-- 登录 -->
<template>
  <div class="page" id="pageLogin">
    <mt-header class="bar-nav" title="">
      <mt-button icon="back" slot="left" @click.native="backUrl('/index')"></mt-button>
    </mt-header>

    <div class="content">
      <div class="outer-box">
        <div class="form-area">
          <div class="text-center"><img src="../../assets/images/logReg/login_logo.png" alt="company-logo"></div>
          <form id="doLogin">
            <ul>
              <li class="aui-border">
                <rd-field type="tel" :attr="{id: 'telInput'}" v-model="mobile" specInput="loginInput" placeholder="请输入手机号"></rd-field>
              </li>
              <li class="aui-border">
                <rd-field :type="changeType" v-model="loginPwd" specInput="loginInput spec" placeholder="请输入密码" @changeEye="hideShowPwd" switchEye :attr="{maxlength: 16}"></rd-field>
              </li>
            </ul>
          </form>
          <div class="login-btn">
            <div v-if="mobile && loginPwd" class="button button-fill main-btn-bg" @click="checkForm">
              <loading v-if="submitData" :task="false"></loading>
              <span v-else>登录</span>
            </div>
            <div v-else class="button button-fill">
              <span>登录</span>
            </div>
            <div class="clearfix pos-a">
              <router-link to="/login/forgetPwd">
                <span class="pull-left color-666">忘记密码？</span>
              </router-link>
              <router-link to="/register">
                <span class="pull-right main-color">注册新用户</span>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import validator from '../../mixins/formValidator.js'; // 引入公共表单校验规则
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import backUrl from '../../mixins/backUrl.js'; // 引入返回上一页公共方法
  import Loading from '../../components/Loading'; // 引入loading组件
  import RdField from '../../components/FieldInput'; // 引入公共表单控件组件
  import { loginAjax } from '../../ajax.config.js'; // 引入登录接口地址

  export default {
    name: 'page-login',
    data() {
      return {
        mobile: '', // 手机号
        loginPwd: '', // 密码
        changeType: 'password', // 密码明文密文切换，password密文，text明文
        submitData: false // 表单提交状态，false没有提交，ture已提交并显示loading
      };
    },
    created() {
       this.mobile = this.$store.state.user.userName || '';
    },
    mixins: [validator, base64code, backUrl],
    methods: {
      // 密码明文密文切换
      hideShowPwd(arg) {
        if (arg === '0') {
          this.changeType = 'password';
        }else{
          this.changeType = 'text';
        }
      },
      // 表单提交
      checkForm() {
        if (this.regexPhone(this.mobile) && this.regexPwd(this.loginPwd)) {
          var formData = new FormData();
          formData.append('loginName', this.mobile);
          formData.append('loginPwd', this.base64Encode(this.loginPwd));
          this.$http.post(loginAjax, formData).then((res) => {
            let errorMsg = res.data.resMsg;
            if (errorMsg !== 'SUCCESS') {
              if (errorMsg === '您的账户已被锁定，请联系客服') {
                this.$messagebox({
                  title: ' ',
                  cancelButtonText: '知道了',
                  confirmButtonText: '联系客服',
                  showCancelButton: true,
                  message: errorMsg
                }).then(action => {
                  if (action === 'confirm') {
                    location.href = 'tel:10086';
                  }
                });
              } else if (errorMsg.match('账户不存在')) {
                this.$messagebox({
                  title: ' ',
                  cancelButtonText: '重新输入',
                  confirmButtonText: '立即注册',
                  showCancelButton: true,
                  message: errorMsg
                }).then(action => {
                  if (action === 'confirm') {
                    this.$router.push('/register');
                  } else if (action === 'cancel') {
                    var tel = document.getElementById('telInput');
                    this.mobile = '';
                    tel.focus();
                  }
                });
              } else {
                this.$toast(errorMsg);
              }
              return ;
            }
            // 将登录转成加载提示
            this.submitData = true;
            let user = {
              userName: this.mobile,
              loginName: res.data.resData.username,
              userId: res.data.resData.userId,
              avatarPhoto: res.data.resData.avatarPhoto,
              expiresIn: res.data.resData.expiresIn,
              __sid: res.data.resData.__sid
            };
            // 登入后先清除之前操作留下来的所有缓存
            Object.keys(sessionStorage).forEach((k) => {
              sessionStorage.removeItem(k)
            });
            // 存储user信息到本地
            localStorage.setItem('user', JSON.stringify(user));
            this.$store.dispatch('login', user);
            let redirect = this.$route.query.redirect || '/account';
            this.$router.replace({ path: redirect });
            // 后台新建的账户要提示去修改登录密码
            if (res.data.resData.pwdResetFlag === 1) {
              this.$messagebox({
                title: ' ',
                showCancelButton: true,
                message: '是否立即去修改登录密码'
              }).then(action => {
                if (action === 'confirm') {
                  this.$router.push('/mine/safe/setPwd');
                }
              });
            }
          })
        }
      },
      update(val) {
        var formatVal = val.trim().replace(/\D*/, '');
        this.$refs.tel.value = formatVal;
      }
    },
    components: { Loading, RdField }
}
</script>

<style type="text/css" scoped>
  .page {background:url(../../assets/images/logReg/login_bg.png) no-repeat;}
  .content {margin:.44rem  .15rem 0; border-radius:6px;}
  .bar-nav {background:transparent;z-index: 10;}
  .form-area {background:#fff;border-radius:6px;overflow:hidden;}
  .form-area img {height: 1.175rem;margin-top:.3rem;}
  .form-area ul {margin-top: .5rem; text-align:center;}
  .form-area ul li {margin: .2rem;  border-radius: 44px;}
  .aui-border:after {border-radius: 44px;}
  .login-btn {padding:.3rem 0 .97rem;position:relative;}
  .pos-a {position: absolute;bottom:26px;width:100%;padding:0 15px;font-size:14px;box-sizing: border-box;}
  .button.button-fill {
    width: 2.98rem;
    margin:auto;color:#fff;
    text-align:center;
    font-size:18px;height:.44rem;
    line-height:.44rem;border-radius: 50px;display:block;
    background-color: #ccc;
    box-shadow: 0 0 1px #b8bbbf;
  }
</style>
