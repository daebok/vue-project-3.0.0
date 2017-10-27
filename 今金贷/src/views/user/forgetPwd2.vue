<!-- 设置新密码 -->
<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="设置新密码">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area margin-t-15">
      <form id="form_pwd">
        <ul>
          <li>
            <rd-field :type="changeType" v-model="newLoginPwd" label="新密码" placeholder="请输入新登录密码" @changeEye="hideShowPwd" specInput="pwdInput"
                      switchEye :attr="{maxlength: 16}"></rd-field>
          </li>
          <li>
            <rd-field :type="changeType2" v-model="confirmPwd" label="确认密码" placeholder="再次输入新登录密码" @changeEye="hideShowPwd2" specInput="pwdInput"
                      switchEye :attr="{maxlength: 16}"></rd-field>
          </li>
        </ul>
      </form>
      <div class="margin-lr-15 margin-t-30">
        <mt-button v-if=" newLoginPwd && confirmPwd" @click="confirmSub" type="danger" size="large" class="confirm-btn">完成</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>完成</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../components/Loading.vue'; // 引入loading组件
  import RdField from '../../components/FieldInput.vue'; // 引入公共表单控件组件
  import validator from '../../mixins/formValidator.js'; // 引入公共表单校验规则
  import base64code from '../../mixins/base64code.js'; // 引入密码加密插件
  import * as ajaxUrl from '../../ajax.config.js'; // 引入所有接口地址
  import qs from 'qs';

  export default {
    data() {
      return {
        newLoginPwd:'', // 新密码
        confirmPwd:'', // 确认密码
        changeType: 'password', // 密码输入框类型，password密码框，text文本框
        changeType2: 'password' // 密码输入框类型，password密码框，text文本框
      };
    },
    mixins: [validator, base64code],
    methods: {
      // 密码框明文密文转换
      hideShowPwd(arg) {
        if (arg === '0') {
          this.changeType = 'password';
        } else {
          this.changeType = 'text';
        }
      },
      // 密码框明文密文转换
      hideShowPwd2(arg) {
        if (arg === '0') {
          this.changeType2 = 'password';
        } else {
          this.changeType2 = 'text';
        }
      },
      // 完成按钮表单提交验证
      confirmSub() {
        if (this.regexPwd(this.newLoginPwd)) {
          if (this.confirmPwd !== this.newLoginPwd) {
            this.$toast('确认密码和新密码不一致');
            return;
          }
          let params = {
            __sid: this.$route.query.__sid,
            pathWay: this.$route.query.pathWay,
            password: this.base64Encode(this.newLoginPwd),
            confirmPassword: this.base64Encode(this.confirmPwd)
          };
          this.$http.post(ajaxUrl.confirmPwd, qs.stringify(params)).then((res) => {
            if (res.data.resMsg === '登录密码重置成功！') {
              this.$router.push('/login');
            } else {
              this.$toast(res.data.resMsg);
            }
          })
        }
      }
    },
    components: { Loading, RdField }
  }
</script>
