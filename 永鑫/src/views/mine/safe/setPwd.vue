<!-- 支付密码 -->
<template>
  <div class="page">
    <div class="form-area">
      <ul v-if="resData.hasSetPayPwd != 1 || $route.query.forgetPwd">
        <li style="margin-top: .2rem;">
          <rd-field :type="changeType2" v-model="newPayPwd" label="支付密码" placeholder="8-16位数字和字母组合" @changeEye="hideShowPwd2" specInput="pwdInput" switchEye :attr="{maxlength: 16}"></rd-field>
        </li>
      </ul>
      <ul v-else>
        <li class="margin-t-20">
          <rd-field :type="changeType" v-model="payPwd" label="原密码" placeholder="请输入原密码" @changeEye="hideShowPwd" specInput="pwdInput" switchEye :attr="{maxlength: 16}" name="pwd"></rd-field>
        </li>
        <li>
          <rd-field :type="changeType2" v-model="newPayPwd" label="新密码" placeholder="8-16位数字和字母组合" @changeEye="hideShowPwd2" specInput="pwdInput" switchEye :attr="{maxlength: 16}"></rd-field>
        </li>
      </ul>
      <div v-if="resData.hasSetPayPwd == 0 || $route.query.forgetPwd" class="margin-lr-15 margin-t-30">
        <mt-button v-if="newPayPwd" @click="confirmSet" type="danger" size="large" class="confirm-btn">确定</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确定</mt-button>
      </div>
      <div v-else class="margin-lr-15 margin-t-30">
        <mt-button v-if="payPwd && newPayPwd" @click="confirmSub" type="danger" size="large" class="confirm-btn">确定修改</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确定修改</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../../components/Loading';
  import RdField from '../../../components/FieldInput';
  import validator from '../../../mixins/formValidator';
  import base64code from '../../../mixins/base64code';
  import * as ajaxUrl from '../../../ajax.config';
  import qs from 'qs';

  export default {
    data() {
      return {
        resData: '',
        changeType: 'password',
        changeType2: 'password',
        changeType3: 'password',
        payPwd: '',
        newPayPwd: '',
        confirmPwd: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      };
    },
    created() {
      // 用户认证信息初始化
      this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
        if (res.data.resData) {
          this.resData = res.data.resData;
        }
      });
      if (this.resData.hasSetPayPwd != 1) {
        document.title = '设置支付密码';
      }
    },
    mixins: [ validator, base64code ],
    methods: {
      hideShowPwd(arg) {
        if (arg == '0') {
          this.changeType = 'password';
        } else {
          this.changeType = 'text';
        }
      },
      hideShowPwd2(arg) {
        if (arg == '0') {
          this.changeType2 = 'password';
        } else {
          this.changeType2 = 'text';
        }
      },
      // 确认提交
      confirmSub() {
        if (this.regexPwd(this.payPwd) && this.regexPwd(this.newPayPwd)) {
          if (this.payPwd == this.newPayPwd) {
            this.$toast('原密码和新密码不能一致');
            return false;
          }
          let params = {
           oldPayPwd: this.base64Encode(this.payPwd),
            newPayPwd: this.base64Encode(this.newPayPwd),
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid
          };
          this.$http.post(ajaxUrl.updatePayPwd, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg);
            if (res.data.resCode == 39321) {
              if (this.$route.query.fromBank) {
                this.$router.push('/account');
              } else {
                this.$router.go(-1);
              }
            }
          })
        }
      },
      confirmSet() {
        if (this.regexPwd(this.newPayPwd)) {
          let params = {
            newPayPwd: this.base64Encode(this.newPayPwd),
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid
          };
          this.$http.post(ajaxUrl.updatePayPwd, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg);
            if (res.data.resCode == 39321) {
              if (this.$route.query.from == 'investDetail') {
                this.$router.push({ name: 'investDetail', params: { projectId: this.$route.query.id }});
              } else if (this.$route.query.from == 'fund') {
                this.$router.push({ name: 'fund' });
              } else {
                this.$router.push({ name: 'account' });
              }
            }
          })
        }
      }
    },
    components: { Loading, RdField }
  }
</script>
