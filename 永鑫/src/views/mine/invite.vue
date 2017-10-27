<!-- 邀请好友 by fenglei -->
<template>
  <div class="page">
    <div class="share-bg hide"></div>
    <div class="alert-tip hide">取消分享</div>
    <div class="content">
      <div class="top text-center">
        <img id="invitePic" src="../../assets/images/me/invite_pic.png" alt="邀请">
        <h1 class="color-333">邀好友 赚现金</h1>
        <p class="color-333">每成功邀请一位好友可得10元红包</p>
        <router-link to="/mine/invite/award_rule">
          <span class="rule color-666">查看奖励规则</span>
        </router-link>
      </div>
      <div class="icon-area clearfix text-center">
        <div v-if="wechatShow" @click="shareToWx" class="icon-con">
          <img src="../../assets/images/me/invite_weichat.png" alt="微信图标">
          <p>微信好友</p>
        </div>
        <div v-if="qqShow" @click="howToShareShow = true" class="icon-con">
          <img src="../../assets/images/me/invite_QQ.png" alt="qq">
          <p>QQ好友</p>
        </div>
        <div class="icon-con" @click="popupVisible = !popupVisible">
          <img src="../../assets/images/me/invite_QRcode.png" alt="扫一扫">
          <p>二维码扫一扫</p>
        </div>
      </div>
      <!--<div class="margin-lr-15 margin-b-30">
        <mt-button size="large" type="danger">更多邀请方式</mt-button>
      </div>-->
      <div ref="qrcode"></div>
    </div>
    <div class="bottom aui-flex-col aui-flex-middle text-center">
      <div class="aui-flex-item-4">
        <h3>共{{ resdata.redAmount }}元</h3>
        <p>已获红包</p>
      </div>
      <div class="aui-flex-item-4">
        <h3>共{{ resdata.rateCount }}张</h3>
        <p>已获加息券</p>
      </div>
      <div class="aui-flex-item-4">
        <router-link to="/mine/invite/myInvite_log">
          <p>我的邀请</p>
          <img src="../../assets/images/public/arrow_right.png">
        </router-link>
      </div>
    </div>
    <mt-popup v-model="popupVisible" position="bottom" class="pop-area">
      <div class="first text-center">
        <p class="color-666">二维码扫一扫</p>
        <div class="qrcode" ref="qrcode"></div>
      </div>
      <div @click="popupVisible = !popupVisible" class="cancel">取消</div>
    </mt-popup>
    <transition name="fade">
      <div @click="howToShareShow = false" class="howToShare" v-show="howToShareShow">
        <img src="../../assets/images/public/fsgpy2.png">
      </div>
    </transition>
  </div>
</template>

<script type="text/ecmascript-6">
  import QRCode from 'qrcode2'; // 生成二维码插件
  import weixin from 'weixin-js-sdk'; //  第三方微信分享js
  import * as ajaxUrl from '../../ajax.config';
  import '../../libs/bridge.min'

  export default {
    data() {
      return {
        resdata: '', // 接口数据对象
        popupVisible: false, // 是否显示我的二维码，true显示，false不显示,
        howToShareShow: false,
        getParams: { // 获取当前登录用户信息
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        wechatShow: false,
        qqShow: false
      };
    },
    created() {
      let ua = navigator.userAgent.toLowerCase();
      if(ua.match(/MicroMessenger/i)=="micromessenger") {
        this.wechatShow = true;
        this.qqShow = true;
      }
      this.$indicator.open({ spinnerType: 'fading-circle' }) // mint-ui加载中动画效果
      this.$http.get(ajaxUrl.userInvite, { params: this.getParams }).then((res) => {
        this.resdata = res.data.resData;
        // 生成微信分享二维码
        var qrcode = new QRCode(this.$refs.qrcode, {
          text: this.resdata.inviteUrl,
          width: 120,
          height: 120,
          colorDark: '#000'
        })
        if(this.resdata.inviteSource == 'app') {
          this.wechatShow = true;
        }
        this.$indicator.close();
      })
    },
    mounted() {
      this.$http.get(ajaxUrl.wxInvite, { params: this.getParams }).then((res) => {
        var data = res.data.resData;
        console.log('ticket:' + data.ticket); // 调试用，微信分享签名戳
        console.log('timestamp:' + data.timestamp); // 调试用，微信分享时间戳
        console.log('noncestr:' + data.noncestr); // 调试用，微信分享随机字符串
        weixin.config({
          debug: false,
          appId: data.appid, // 微信公众平台appid，视不同项目平台而定
          timestamp: data.timestamp,
          nonceStr: data.noncestr,
          signature: data.signature,
          jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage', 'onMenuShareQQ'] // '`分享到朋友圈','分享给朋友','分享到QQ'
        })
        var obj = {
          title: this.resdata.shareTitle, // 分享标题
          desc: this.resdata.shareContent, // 分享描述
          link: this.resdata.inviteUrl, // 分享链接
          imgUrl: '', // 分享图标
          success: function() {
            // 用户确认分享后执行的回调函数
            document.querySelector('.alert-tip').classList.add('hide')
          },
          cancel: function() {
            // 用户取消分享后执行的回调函数
            closeit(1.5);
            document.querySelector('.alert-tip').classList.add('hide');
          }
        }
        weixin.ready(function() {
          weixin.onMenuShareTimeline(obj);
          weixin.onMenuShareAppMessage(obj);
          weixin.onMenuShareQQ(obj);
        })
        weixin.error(function (res) {
          //alert(res.errMsg);
        });
      })
      // 分享回调
      function closeit(sec) {
        document.querySelector('.alert-tip').classList.remove('hide');
        sec--;
        if (sec>0) {
          setTimeout('closeit('+sec+')',500);
        } else {
          document.querySelector('.alert-tip').classList.add('hide');
        }
      };
    },
    methods: {
      shareToWx() {
        if(this.resdata.inviteSource == 'app'){
          var self = this
          $bridge(function (bridge) {
            var data = {
              data: {
                title: self.resdata.shareTitle, // 分享标题
                commonText: self.resdata.shareContent, // 分享描述
                webUrl: self.resdata.inviteUrl, // 分享链接
                imgUrl: document.querySelector('#invitePic').src, // 分享图标
              }
            }
            bridge.callHandler('shareToApp', data, function (res) {
              alert(json)
            })
          })
        }else {
          this.howToShareShow = true
        }
      }
    }
  }
</script>

<style type="text/css" scoped>
  .content {margin-bottom:.7rem;}
  .top {background: #fff;padding: .15rem 0;}
  .top img{ width:1.95rem; }
  .top h1 {font-size: .3rem; line-height:1; margin-top:.25rem; font-weight: bold;}
  .top p { margin: .12rem auto .3rem;font-size: .13rem; }
  .top .rule { border-bottom: 1px solid #666;}
  .icon-area { margin: .15rem 0; font-size: 0; }
  .icon-area .icon-con {
    display: inline-block;
    width: 33.33%;
    font-size: .14rem;
  }
  .icon-area img {width: .48rem;}
  .icon-area p {font-size: .12rem;color:#333;line-height:1;margin-top:.16rem;}
  .bottom {position: absolute;bottom: 0;width: 100%;background:#fff; height: .65rem;}
  .bottom .aui-flex-item-4 {position: relative;}
  .bottom p {font-size: .13rem; color: #999;}
  .bottom img { width: .14rem;position: absolute; top: 50%; right:.15rem; margin-top:-.06rem; }
  .pop-area {width: 100%;}
  .pop-area .first {height:1.85rem; background:#f2f4f8;padding:.16rem 0;}
  .pop-area .first img { width: 1.21rem; margin: .15rem;}
  .qrcode { display: inline-block; margin-top: .1rem; }
  .pop-area .cancel {line-height: .5rem; text-align: center; color: #333;}
  .share-bg {height: 100%; width: 100%; position: fixed; top: 0; left: 0; background:rgba(0, 0, 0, 0.7) url(../../assets/images/me/share_bg.png) center -5% no-repeat;
    background-size: 100%; z-index: 10002; }
  .alert-tip {width:40%;background:rgba(0, 0, 0, 0.85);position:fixed;top:30%;left:30%;z-index:100;border-radius:.05rem;text-align:center;line-height:.44rem;color:#fff;}
  .howToShare{
    position: fixed;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    text-align: center;
    z-index: 2;
  }
  .howToShare img{
    width: 100%;
  }
</style>
