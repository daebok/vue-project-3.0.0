<template>
  <div class="page" id="settings">
    <mt-header class="bar-nav" title="账户与安全">
      <mt-button slot="left" icon="back" @click.native="backUrl('/mine')"></mt-button>
    </mt-header>
    <div class="pic-area aui-flex aui-flex-col aui-flex-middle aui-border-b">
      <input type="file" accept="image/png, image/jpeg" class="photo-upload" @change="uploadAvatar">
      <div class="font-16 aui-flex-item-4">我的头像</div>
      <div class="aui-flex-item-8 ">
        <img v-if="!avatar" class="pull-right" src="../../../assets/images/me/account_pic_head.png" >
        <img v-else class="pull-right" :src="avatar" >
      </div>
    </div>
    <mt-cell title="我的等级" to="/mine/safe/vip" :value="vipLevel" is-link></mt-cell>
    <div class="margin-t-10"></div>

    <!-- 是否允许修改手机号开关 -->
    <mt-cell v-if="features.EnableModifyPhoneFeature" title="绑定手机" :to="phoneLink" :value="phoneStr" is-link></mt-cell>
    <mt-cell v-else title="绑定手机" :value="phoneStr"></mt-cell>

    <mt-cell v-if="this.resdata.emailStatus == 0" title="绑定邮箱" :to="emailLink" :value="emailStr" is-link></mt-cell>
    <mt-cell v-else title="修改邮箱" @click.native="modifyEmail" :value="emailStr" is-link></mt-cell>

    <!-- 绑定托管账户后是否允许点击 -->
    <mt-cell v-if="this.resdata.realnameStatus == 0" title="托管账户" :to="realnameLink" :value="realnameStr" is-link></mt-cell>
    <mt-cell v-else-if="features.EnableClickRealnameFeature" title="托管账户" @click.native="hasRealName" :value="realnameStr" is-link></mt-cell>
    <mt-cell v-else title="托管账户" :value="realnameStr"></mt-cell>

    <!-- 免登签约判断+开关 -->
    <mt-cell v-if="features.EnableSignFeature && this.resdata.realnameStatus == 0" title="签约" :value="'请先开通托管账户'"></mt-cell>
    <mt-cell v-else-if="features.EnableSignFeature && this.appSignStatus == 0" title="签约" :to="appSignUrl" :value="'去签约'" is-link></mt-cell>
    <mt-cell v-else-if="features.EnableSignFeature && this.appSignStatus == 1" title="签约" :value="'处理中'"></mt-cell>
    <mt-cell v-else-if="features.EnableSignFeature && this.appSignStatus == 2" title="签约" :value="'已签约'"></mt-cell>

    <mt-cell title="银行卡" @click.native="bankEvent" :value="bankStr" is-link></mt-cell>
    <div class="margin-t-10"></div>
    <mt-cell title="登录密码" to="/mine/safe/setPwd" value="去修改" is-link></mt-cell>
    <mt-cell title="风险承受能力" @click.native="riskEvent" :value="resdata.riskLevelStr" is-link></mt-cell>
    <!--<mt-cell title="业务授权" @click.native="authEvent" :value="authorizateStr" is-link></mt-cell>-->
    <div class="logout" @click="logout">退出登录</div>
    <!--去授权-->
    <form :action="authSign" method="post" id="submitAuth">
      <input type="hidden" name="__sid" :value="getParams.__sid">
      <input type="hidden" name="userId" :value="getParams.userId">
    </form>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import * as features from '../../../features.config';
  import backUrl from '../../../mixins/backUrl'
  import qs from 'qs'
  export default {
    data(){
      return {
        resdata:'',
        avatar: '',
        vipLevel: 0,
        phoneLink: '',
        phoneStr: '',
        emailLink: '',
        emailStr: '',
        realnameLink: '',
        realnameStr: '',
        appSignStatus: '', //签约状态
        appSignUrl: '', //签约地址
        bankStr: '',
        authorizateStr: '',
        getParams:{
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        authSign: ajaxUrl.authSign,
        features: features // 将配置项features赋值给features
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      //签约信息判断
      this.$http.get(ajaxUrl.signInfo, {params:this.getParams}).then((res) => {
        this.appSignStatus = res.data.resData.appSignStatus; //签约状态
        //console.log(this.appSignStatus);
        this.appSignUrl = ajaxUrl.MobileServer + res.data.resData.appSignUrl+'?userId='+this.$store.state.user.userId+'&__sid='+this.$store.state.user.__sid; //签约地址
        //console.log(this.appSignUrl);
      })
      //账户信息判断
      this.$http.get(ajaxUrl.basicInfo, {params:this.getParams}).then((res) => {
        this.resdata = res.data.resData;
        this.avatar = res.data.resData.avatarPhoto;
        this.vipLevel = 'VIP' + this.resdata.vipLevel //级别
        //判断手机是否认证
        if(this.resdata.mobilePhoneStatus == 0){
          this.phoneLink =  '/mine/safe/bind_phone'
          this.phoneStr =  '去绑定'
        }else {
          this.phoneLink = `/mine/safe/modify_phone/${this.resdata.mobile}`
          this.phoneStr =  this.resdata.mobile
        }
        //判断邮箱是否认证
        if(this.resdata.emailStatus == 0){
          this.emailLink =  '/mine/safe/bind_email'
          this.emailStr =  '去绑定'
        }else {
          this.emailLink =  '/mine/safe/modify_email?email=' + this.resdata.email
          this.emailStr =  this.resdata.email
        }
        //判断实名认证
        if(this.resdata.realnameStatus == -1){
          this.realnameLink = '/mine/safe/realname'
          this.realnameStr =  '未通过'
        }else if(this.resdata.realnameStatus == 0){
          this.realnameLink = '/mine/safe/realname'
          this.realnameStr =  '去开通'
        }else if(this.resdata.realnameStatus == 1){
          let tppUser = this.resdata.tppUserAccId
          this.realnameStr =  tppUser.substring(0,3) + '****' + tppUser.substring((tppUser.length-4), tppUser.length)
          this.realnameLink = '/mine/safe/realname?realnameStatus=1&tppUser='+this.realnameStr
        }
        //判断银行卡是否添加
        if(this.resdata.bankNum == 0){
          this.bankStr =  '去绑定'
        }else {
          this.bankStr =  this.resdata.bankNum + '张'
        }
        //判断交易密码
//        if(this.resdata.hasSetPayPwd == 0){
//          this.payPwdStr =  '去设置'
//        }else {
//          this.payPwdStr =  '去修改'
//        }
        //判断是否授权
        if(this.resdata.authorizated == 0){
          this.authorizateStr =  '去授权'
        }else {
          this.authorizateStr =  '已授权'
        }
        this.$indicator.close()
      })
    },
    mixins: [backUrl],
    methods: {
      // 已实名则显示实名信息
      hasRealName() {
        this.$router.push({ name: "realname", query: { realnameStatus: 1, tppUser: this.realnameStr }});
      },
      // 跳转到修改邮箱页面
      modifyEmail() {
        this.$router.push({ name: "modifyEmail", query: { email: this.resdata.email }});
      },
      async uploadAvatar(){  //上传头像
        let input = document.querySelector('.photo-upload')
        let data = new FormData();
        let reader = new FileReader()
        // reader.onload = function (evt) {console.log(evt.target.result)}
        // reader.readAsDataURL(input.files[0])
        data.append('upload', input.files[0]);
        if (input.files[0].size > (1024 * 1024 * 5)) {
          this.$toast('图片不能大于5M');
        } else {
          this.$indicator.open({ spinnerType: 'fading-circle' });
          this.$http.post(ajaxUrl.avatarMobile, data).then((res) => {
            this.$indicator.close();
          if(res.data.resCode == 39321){
            let params =Object.assign(this.getParams, {imgUrl: res.data.resData.imgUrl})
            this.$http.post(ajaxUrl.uploadAvatar, qs.stringify(params)).then((res2) => {
              if(res2.data.resMsg == '头像上传成功'){
              this.$toast(res2.data.resMsg);
              this.avatar = ajaxUrl.StaticsServer + res.data.resData.imgUrl
            }
          })
          }else{
            this.$toast(res.data.resMsg);
          }
        })
        }
//        try{
//          let response = await fetch(ajaxUrl.avatarMobile, {method: 'POST', body: data})
//          let res = await response.json();
//          if(res.resData){
//            this.$toast('上传成功');
//            this.avatar = ajaxUrl.StaticsServer + res.resData.imgUrl
//          }else{
//            this.$toast(res.resMsg);
//          }
//        }catch (error) {
//          this.$toast('上传失败');
//          throw new Error(error);
//        }
      },
      bankEvent(){
        if(this.resdata.realnameStatus != 1){
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去开通',
            showCancelButton: true,
            message: '为了您的资金安全，请您开通第三方资金托管账户！'
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/mine/safe/realname')
            }
          });
          return ;
        }
        this.$router.push('/mine/safe/bank')
      },
      riskEvent(){
        if(this.resdata.riskLevel != ''){
          this.$messagebox({
            title: ' ',
            showCancelButton: true,
            confirmButtonText: '重新评测',
            message: '您当前风险承受能力为'+this.resdata.riskLevelStr+'，点击重新评测可重新评测'
          }).then(action => {
            if(action == 'confirm'){
              this.$router.push('/mine/safe/risk_tips')
            }
          });
        }else {
          this.$router.push('/mine/safe/risk_tips')
        }
      },
      authEvent(){
        if(this.resdata.realnameStatus != 1){
          this.$messagebox({
            title: ' ',
            confirmButtonText: '去开通',
            showCancelButton: true,
            message: '为了您的资金安全，请您开通第三方资金托管账户！'
          }).then(action => {
            if(action == 'confirm'){
            	this.$router.push('/mine/safe/realname')
          	}
        });
          return ;
        } else {
          //判断是否授权
          if(this.resdata.authorizated == 0){ //post方式去授权
            document.getElementById('submitAuth').submit()
          }
        }
      },
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
            this.$router.push('/login?from=logout&userName='+this.$store.state.user.userName)
            sessionStorage.activeType = 'account'
          }
        });
      }
    }
  }
</script>

<style scoped>
  .font-16 { font-size: .16rem;}
  .pic-area {background:#fff;padding:.1rem .15rem;margin-top:.1rem;position:relative;}
  .pic-area input {
    position: absolute;
    height: 100%;
    width: 90%;
    opacity: 0;
  }
  .pic-area img { width: .54rem; height:.54rem; border-radius:50%;}
  .logout { text-align:center; line-height:.4rem; background:#fff; margin-top: .2rem;}
</style>
