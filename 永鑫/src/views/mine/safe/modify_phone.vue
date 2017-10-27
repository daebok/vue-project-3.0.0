<!-- 修改绑定手机 by fenglei -->
<template>
  <div class="page">
    <div class="form-area">
      <ul>
        <p class="bind-box">已绑定手机号  {{ $route.query.phone }}</p>
        <li>
          <rd-field type="tel" class="get-code" v-model="code" :attr="{ maxlength: 6 }" label="验证码" placeholder="请输入验证码">
            <button @click.prevent="getCode" class="yzm main-color aui-border-l ellipsis">获取验证码</button>
          </rd-field>
        </li>
      </ul>
      <div class="margin-lr-15">
        <mt-button v-if="code" type="danger" size="large" @click.native="confirmSub" class="confirm-btn">下一步</mt-button>
        <mt-button v-else type="default" disabled size="large" class="confirm-btn">下一步</mt-button>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Loading from '../../../components/Loading';
  import RdField from '../../../components/FieldInput';
  import validator from '../../../mixins/formValidator';
  import getCodeTime from '../../../mixins/getCodeTime';
  import * as ajaxUrl from '../../../ajax.config';
  import qs from 'qs';

  export default {
    data() {
      return {
        code: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      };
    },
    mixins: [ validator, getCodeTime ],
    methods: {
      // 获取验证码
      getCode(evt) {
        this.$http.get(ajaxUrl.modifyPhoneCode, { params: this.getParams }).then((res) => {
          if (res.data.resMsg == '验证码已发送') {
            this.getCodeTime(evt, 60);
          } else {
            this.$toast(res.data.resMsg);
          }
        })
      },
      // 下一步提交验证
      confirmSub() {
        let params = Object.assign(this.getParams, { code: this.code });
        this.$http.post(ajaxUrl.doModifyPhone, qs.stringify(params)).then((res) => {
          if (res.data.resData != '') {
            this.$router.push({ name: 'bindPhone', query: { 'phoneSign': res.data.resData.modifyPhoneSign }});
          } else {
            this.$toast(res.data.resMsg);
          }
        })
      }
    },
    components: { Loading, RdField }
  }
</script>

<style type="text/css" scoped>
  .bind-box { margin: .2rem .15rem; }
</style>
