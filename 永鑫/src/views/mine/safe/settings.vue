<!-- 账户与安全 by fenglei -->
<template>
  <div>
    <div class="pic-area aui-flex aui-flex-col aui-flex-middle aui-border-b">
      <input type="file" class="photo-upload" @change="uploadAvatar">
      <div class="font-16 aui-flex-item-4">我的头像</div>
      <div class="aui-flex-item-8 ">
        <img v-if="!avatar" class="pull-right" src="../../../assets/images/me/account_pic_head.png" >
        <img v-else class="pull-right" :src="avatar" >
      </div>
    </div>
    <mt-cell v-if="resdata.vipOpen == 1" title="我的等级" to="/mine/safe/vip" :value="'VIP' + resdata.vipLevel" is-link></mt-cell>
    <div class="margin-t-10"></div>
    <div v-if="resdata.realnameStatus == 1">
      <mt-cell title="真实姓名" :value="realnameStr"></mt-cell>
      <mt-cell title="证件号码" :value="cardNoStr"></mt-cell>
    </div>
    <div v-else @click="realName">
      <mt-cell title="真实姓名" :value="realnameStr" is-link></mt-cell>
      <mt-cell title="证件号码" :value="cardNoStr" is-link></mt-cell>
    </div>
    <mt-cell title="身份证认证" v-if="resdata.idCardStatus != 1" @click.native="idCard">
      <i class="tip-color">{{ idNoStr }}<em class="red-dot" ></em></i>
    </mt-cell>
    <mt-cell v-else title="身份证认证">
      <i>{{ idNoStr }}</i>
    </mt-cell>
    <mt-cell v-if="resdata.mobilePhoneStatus == 1" title="绑定手机" @click.native="phone" :value="phoneStr" is-link></mt-cell>
    <mt-cell title="银行卡" @click.native="bank" :value="bankStr" is-link></mt-cell>
    <div class="margin-t-10"></div>
    <mt-cell title="支付密码" @click.native="payPwd" :value="payPwdStr" is-link></mt-cell>
    <mt-cell title="风险承受能力" @click.native="risk" :value="resdata.riskLevelStr" is-link></mt-cell>
    <div class="logout" @click="logout">退出登录</div>
    <mt-actionsheet class="payPwdSheet" :actions="actions" v-model="sheetVisible"></mt-actionsheet>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../../ajax.config';
  import qs from 'qs';

  export default {
    data() {
      return {
        resdata: '',
        avatar: '',
        phoneStr: '',
        realnameStr: '',
        cardNoStr: '',
        idNoStr: '',
        bankStr: '',
        payPwdStr: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        sheetVisible: false, // 是否显示支付密码遮罩层
        actions: [
          {
            name: '修改密码',
            method: this.setPwd,
            pwdSign: false
          },
          {
            name: '找回密码',
            method: this.modifyPwd
          }
        ]
      };
    },
    created() {
      this.$indicator.open({ spinnerType: 'fading-circle' });
      this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
        this.$indicator.close();
        this.resdata = res.data.resData;
        this.avatar = res.data.resData.avatarPhoto;
        this.phoneStr =  this.resdata.mobile;
        // 判断实名认证
        if (this.resdata.realnameStatus != 1) {
          this.realnameStr = this.cardNoStr =  '去认证';
        } else if (this.resdata.realnameStatus == 1) {
          this.realnameStr =  this.resdata.realname;
          this.cardNoStr =  this.resdata.idNo;
        }
        // 判断身份证认证
        if (this.resdata.idCardStatus == 0) {
          this.idNoStr = '上传身份证照';
        } else {
          this.idNoStr = '审核通过';
        }
        // 判断银行卡
        if (this.resdata.bankNum == 0) {
          this.bankStr =  '去绑定';
        } else {
          this.bankStr =  '已绑定';
        }
        // 判断支付密码
        if (this.resdata.hasSetPayPwd == 0) {
          this.payPwdStr = '去设置';
          this.actions[0].name = '去设置';
          this.actions[0].pwdSign = true;
        } else {
          this.payPwdStr = '已设置';
        }
      })
    },
    methods: {
      // 上传头像
      async uploadAvatar() {
        let input = document.querySelector('.photo-upload');
        let data = new FormData();
        data.append('upload', input.files[0]);
        if (input.files[0].size > (1024 * 1024 * 5)) {
          this.$toast('图片不能大于5M');
        } else {
          this.$indicator.open({ spinnerType: 'fading-circle' });
          this.$http.post(ajaxUrl.avatarMobile, data).then((res) => {
            this.$indicator.close();
            if (res.data.resCode == 39321) {
              let params = Object.assign(this.getParams, { imgUrl: res.data.resData.imgUrl });
              this.$http.post(ajaxUrl.uploadAvatar, qs.stringify(params)).then((res2) => {
                if (res2.data.resMsg == '头像上传成功') {
                  this.$toast(res2.data.resMsg);
                  this.avatar = ajaxUrl.assetsDomain + res.data.resData.imgUrl;
                }
              })
            } else {
              this.$toast(res.data.resMsg);
              input.value  = '';
            }
          },(error) => {
            this.$indicator.close();
            input.value  = '';
          })
        }
      },
      // 实名认证
      realName() {
        this.$router.push({ name: 'realname' });
      },
      // 身份证认证
      idCard() {
        if (this.resdata.realnameStatus != 1) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去认证',
            showCancelButton: true,
            message: '为了您的资金安全，请先实名认证'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'realname' });
            }
          });
        } else {
          this.$router.push({ name: 'idNo' });
        }
      },
      // 绑定手机
      phone() {
        this.$router.push({ name: 'modifyPhone', query: { 'phone': this.resdata.mobile }});
      },
      // 绑定银行卡
      bank() {
        if (this.resdata.realnameStatus != 1) {
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去认证',
            showCancelButton: true,
            message: '为了您的资金安全，请先实名认证'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'realname' });
            }
          });
        } else {
          this.$router.push({ name: 'bank' });
        }
      },
      // 支付密码
      payPwd() {
        if (this.actions[0].pwdSign) {
          this.$router.push({ name: 'setPwd',query:{ 'pwdSign': this.actions[0].pwdSign }});
        } else {
          this.sheetVisible = true;
        }
      },
      // 修改密码
      setPwd() {
        this.$router.push({ name: 'setPwd',query:{ 'pwdSign': this.actions[0].pwdSign }});
      },
      // 找回密码
      modifyPwd() {
        this.$router.push({ name: 'modifyPwd', query: { 'phone': this.resdata.mobile }});
      },
      // 风险评测
      risk() {
        if (this.resdata.riskLevel != '') {
          this.$messagebox({
            title: ' ',
            showCancelButton: true,
            confirmButtonText: '重新评测',
            message: '您当前风险承受能力为' + this.resdata.riskLevelStr + '，点击重新评测可重新评测'
          }).then(action => {
            if (action == 'confirm') {
              this.$router.push({ name: 'riskTips' });
            }
          });
        } else {
          this.$router.push({ name: 'riskTips' });
        }
      },
      // 退出登录
      logout(){
        this.$messagebox({
          title: ' ',
          showCancelButton: true,
          message: '确定要退出登录吗'
        }).then(action => {
          if(action == 'confirm'){
            // 退出清除所有缓存
            var i = sessionStorage.length
            while (i--){
              var key = sessionStorage.key(i)
              sessionStorage.removeItem(key)
            }
            // delete localStorage.user
            this.$store.dispatch('logout')
            this.$router.push('/login?from=logout')
            sessionStorage.activeType = 'account'
          }
        });
      }
    }
  }
</script>

<style type="text/css">
  .font-16 { font-size: .16rem; }
  .pic-area {
    background:#fff;
    padding:.1rem .15rem;
    position:relative;
  }
  .pic-area input {
    position: absolute;
    height: 100%;
    width: 90%;
    opacity: 0;
  }
  .red-dot {
    background: red;
    width: 5px;
    height: 5px;
    display: inline-block;
    border-radius: 50%;
    vertical-align: top;
  }
  .pic-area img { width: .54rem; height:.54rem; border-radius:50%; }
  .logout { text-align:center; line-height:.4rem; background:#fff; margin-top: .2rem; }
  .payPwdSheet.mint-actionsheet {
    width: 90%;
    background: none;
    bottom: 0.1rem
  }
  .payPwdSheet .mint-actionsheet-list li:last-child {
    border-top: solid 1px #e0e0e0;
  }
  .payPwdSheet .mint-actionsheet-listitem, .mint-actionsheet-button {
    color: #4085C2;
    background: none;
    border: none;
    font-size: 16px;
  }
  .payPwdSheet .mint-actionsheet-list {
    border-radius: 10px;
    background: #fff;
  }
  .payPwdSheet .mint-actionsheet-button {
    border-radius: 10px;
    background: #fff;
  }
</style>
