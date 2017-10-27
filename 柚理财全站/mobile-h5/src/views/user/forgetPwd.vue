<!-- 忘记密码 -->
<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="忘记密码">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="form-area">
      <form>
        <ul>
          <li class="margin-t-20">
            <rd-field type="tel" v-model="reg.mobileVal" label="手机号码" placeholder="请输入手机号码" ></rd-field>
          </li>
          <li>
            <rd-field type="tel" v-model="reg.codeVal" :attr="{maxlength: 6}" label="验证码" placeholder="请输入验证码" specInput="codeInput">
              <button v-if="!reg.mobileVal" class="yzm color-999 aui-border-l ellipsis" disabled>发送验证码</button>
              <button v-else @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">发送验证码</button>
            </rd-field>
          </li>
        </ul>
      </form>
      <div class="margin-lr-15">
        <mt-button v-if="reg.mobileVal && reg.codeVal" @click="confirmSub" type="danger" size="large" class="confirm-btn">下一步</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>下一步</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../components/Loading.vue'; // 引入loading组件
  import RdField from '../../components/FieldInput.vue'; // 引入公共表单控件组件
  import validator from '../../mixins/formValidator.js'; // 引入公共表单校验规则
  import getCodeTime from '../../mixins/getCodeTime.js'; // 引入获取验证码倒计时公共组件
  import * as ajaxUrl from '../../ajax.config.js'; // 引入所有接口地址
  import qs from 'qs';

  export default {
    data() {
      return {
        reg: { // 忘记密码表单数据对象
          mobileVal: '', // 手机号
          codeVal: '', // 手机验证码
          instance: '' // 记录sendValidCode接口返回的异常信息
        }
      };
    },
    mixins: [validator, getCodeTime],
    methods: {
      // 获取手机短信验证码
      getCode(evt) {
        if (this.regexPhone(this.reg.mobileVal)) {
          let params = { pathWay: this.reg.mobileVal };
          this.$http.get(ajaxUrl.sendValidCode, { params: params }).then((res) => {
            if (res.data.resMsg === '验证码已发送') {
              this.getCodeTime(evt);
            } else {
              this.instance = this.$toast(res.data.resMsg);
            }
          })
        }
        else{
          document.querySelector('input[type="tel"]').focus();
        }
      },
      // 下一步按钮表单提交校验
      confirmSub() {
        if (this.regexPhone(this.reg.mobileVal)) {
          let params = {
            dynamicValidCode: this.reg.codeVal,
            pathWay: this.reg.mobileVal
          };
          this.$http.post(ajaxUrl.validation, qs.stringify(params)).then((res) => {
            if (res.data.resData !== '') {
              this.$router.push({
                path: '/login/forgetPwd/forgetPwd2',
                query: {
                  __sid: res.data.resData.__sid,
                  pathWay: res.data.resData.pathWay
                }
              })
            } else {
              if (this.instance) this.instance.close();
              this.$toast(res.data.resMsg);
              document.querySelector('input[type="tel"]').focus();
            }
          })
        }
        else{
          document.querySelector('input[type="tel"]').focus();
        }
      }
    },
    components: { Loading, RdField }
  }
</script>
