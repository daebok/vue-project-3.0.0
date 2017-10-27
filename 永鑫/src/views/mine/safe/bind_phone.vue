<!-- 绑定新手机号码 by fenglei -->
<template>
  <div class="page">
    <div class="form-area">
      <ul>
        <li class="margin-t-20">
          <rd-field type="tel" v-model="reg.mobileVal" :attr="{ maxlength: 11 }" label="手机号码" placeholder="请输入手机号码" name="mobile"></rd-field>
        </li>
        <li>
          <rd-field type="tel" v-model="reg.codeVal" :attr="{ maxlength: 6 }" label="验证码" placeholder="请输入验证码" name="code" specInput="codeInput">
            <button v-if="!reg.mobileVal" class="yzm color-999 aui-border-l ellipsis" disabled>发送验证码</button>
            <button v-else @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
          </rd-field>
        </li>
      </ul>
      <div class="margin-lr-15">
        <mt-button v-if="reg.mobileVal && reg.codeVal" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">确定</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确定</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../../components/Loading'
  import RdField from '../../../components/FieldInput'
  import validator from '../../../mixins/formValidator'
  import getCodeTime from '../../../mixins/getCodeTime'
  import * as ajaxUrl from '../../../ajax.config'
  import qs from 'qs'

  export default {
    data() {
      return {
        reg: {
          mobileVal: '',
          codeVal: ''
        },
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        mobileBindToken: '' //绑定手机标识
      };
    },
    mixins: [ validator, getCodeTime ],
    methods: {
      // 获取验证码
      getCode(evt) {
        let params = Object.assign(this.getParams, { mobile: this.reg.mobileVal });
        if (this.regexPhone(this.reg.mobileVal)) {
          this.$http.get(ajaxUrl.bindPhoneCode, { params: params }).then((res) => {
            if (res.data.resMsg == '已发送验证码') {
              this.getCodeTime(evt, 60);
              this.mobileBindToken = res.data.resData.mobileBindToken;
            } else {
              this.$toast(res.data.resMsg);
            }
          })
        }
      },
      // 确认提交
      confirmSub() {
        let phone_sign = this.$route.query.phoneSign;
        let params = Object.assign(this.getParams, {
          code: this.reg.codeVal,
          mobile: this.reg.mobileVal,
          mobileBindToken: this.mobileBindToken,
          modifyPhoneSign: this.$route.query.phoneSign,
          modifyType: 1
        });
        if (this.regexPhone(this.reg.mobileVal)) {
          if (!this.mobileBindToken) {
            this.$toast('请先获取验证码');
            return false;
          }
          this.$http.post(ajaxUrl.doBindPhone, qs.stringify(params)).then((res) => {
            this.$store.state.user.userName = this.mobileVal;
            this.$messagebox({
              title: ' ',
              confirmButtonText: '确认',
              showCancelButton: false,
              message: res.data.resMsg
            }).then(action => {
              if (action === 'confirm') {
                if (res.data.resCode == 39321) {
                  this.$router.push({ name: 'safe' });
                }
              }
            });
          })
        }
      }
    },
    components: { Loading, RdField }
  }
</script>
