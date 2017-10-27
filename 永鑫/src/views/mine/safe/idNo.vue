<!-- 身份证认证 by fenglei -->
<template>
  <div>
    <div class="padding-lr-15 bg-fff">
      <div class="tips text-center">请上传开户人<span class="main-color">{{ data.realName }}</span>的有效二代身份证</div>
      <div class="box">
          <div class="pic-box" :class="{ 'success': imgFront }">
            <span class="card">
              <img class="pic" src="../../../assets/images/me/shenfen_01.png">
              <span v-if="!imgFront" class="add">
                <img class="shadow" src="../../../assets/images/me/shenfen_add.png">
                <p>上传身份证正面</p>
              </span>
              <span v-else class="add">
                <img src="../../../assets/images/me/shenfen_gou.png">
                <p>上传成功</p>
              </span>
            </span>
            <input type="file" class="photo-upload cardFront" @change="uploadAvatar('cardFront')">
          </div>
          <div class="pic-box margin-t-15" :class="{ 'success': imgBack }">
            <span class="card">
              <img class="pic" src="../../../assets/images/me/shenfen_02.png">
              <span v-if="!imgBack" class="add">
                <img class="shadow" src="../../../assets/images/me/shenfen_add.png">
                <p>上传身份证反面</p>
              </span>
              <span v-else class="add">
                <img src="../../../assets/images/me/shenfen_gou.png">
                <p>上传成功</p>
              </span>
            </span>
            <input type="file" class="photo-upload cardBack" @change="uploadAvatar('cardBack')">
          </div>
        </div>
    </div>
    <div class="demo-box margin-t-15 bg-fff">
      <div class="txt">拍摄示例</div>
      <ul class="aui-border-t">
        <li>
          <img class="big" src="../../../assets/images/me/shenfen_pic01.png">
          <div class="small"><img src="../../../assets/images/me/shenfen_right.png"></div>
          <p>标准</p>
        </li>
        <li>
          <img class="big" src="../../../assets/images/me/shenfen_pic02.png">
          <div class="small"><img src="../../../assets/images/me/shenfen_wrong.png"></div>
          <p>边界缺失</p>
        </li>
        <li>
          <img class="big" src="../../../assets/images/me/shenfen_pic03.png">
          <div class="small"><img src="../../../assets/images/me/shenfen_wrong.png"></div>
          <p>照片模糊</p>
        </li>
        <li>
          <img class="big" src="../../../assets/images/me/shenfen_pic04.png">
          <div class="small"><img src="../../../assets/images/me/shenfen_wrong.png"></div>
          <p>闪光强烈</p>
        </li>
      </ul>
    </div>
    <div class="operator">
      <mt-button v-if="imgFront && imgBack" size="large" type="danger" class="confirm-btn" @click.native="confirmSub">确认上传</mt-button>
      <mt-button v-else type="default" size="large" class="confirm-btn" disabled>确认上传</mt-button>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import qs from 'qs';

  export default {
    data() {
      return {
        data: '', // 用户实名认证信息
        imageServerUrl: '', // 图片服务器地址
        imgFront: '', // 身份证正面图片
        imgBack: '', // 身份证反面图片
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      };
    },
    created() {
      // 实名信息初始化
      this.$http.get(ajaxUrl.idCardUpload,{ params: this.getParams }).then((res) => {
        if (res.data.resData) {
          this.data = res.data.resData;
          this.imageServerUrl = res.data.resData.imageServerUrl;
          if (this.data.realNameStatus != 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去开通托管账户',
              showCancelButton: true,
              message: '您尚未开通资金账户，为了顺利进行身份证认证请先开通资金账户!',
              closeOnClickModal: false
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push({ name: 'realname' });
              } else if (action == 'cancel') {
                this.$router.go(-1);
              }
            });
          } else if(this.data.idCardStatus == 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '确认',
              showCancelButton: false,
              message: '您已经开通了身份证认证，无需再次开通',
              closeOnClickModal: false
            }).then(action => {
              this.$router.push({ name: 'account' });
            });
          }
        }
      });
    },
    methods: {
      // 上传图片
      async uploadAvatar(className) {
        let input = document.querySelector('.' + className);
        if (input.files[0]) {
          let data = new FormData();
          data.append('upload', input.files[0]);
          if (input.files[0].size > (1024 * 1024 * 5)) {
            this.$toast('图片不能大于5M');
          } else {
            this.$indicator.open({ spinnerType: 'fading-circle' });
            document.querySelector('.txt').style.zIndex = 0;
            this.$http.post(this.imageServerUrl + ajaxUrl.uploadifySave, data).then((res) => {
              this.$indicator.close();
              document.querySelector('.txt').style.zIndex = 2;
              if (res.data != 'error') {
                if (className == 'cardFront') {
                  this.imgFront = res.data;
                } else if (className == 'cardBack') {
                  this.imgBack = res.data;
                }
              } else {
                this.$toast('上传图片失败，请重新上传');
                input.value  = '';
                if (className == 'cardFront') {
                  this.imgFront = '';
                } else if (className == 'cardBack') {
                  this.imgBack = '';
                }
              }
            },(error) => {
              this.$indicator.close();
              document.querySelector('.txt').style.zIndex = 2;
              input.value  = '';
              if (className == 'cardFront') {
                this.imgFront = '';
              } else if (className == 'cardBack') {
                this.imgBack = '';
              }
            })
          }
        }
      },
      // 提交验证
      confirmSub() {
        let params = {
          frontCard: this.imgFront,
          backCard: this.imgBack,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.saveCardInfo, qs.stringify(params)).then((res) => {
          this.$indicator.close();
          this.$messagebox({
            title: ' ',
            confirmButtonText: '确认',
            showCancelButton: false,
            message: res.data.resMsg
          }).then(action => {
            if (action === 'confirm') {
              if (res.data.resCode == 39321) {
                if (this.$route.query.from == 'withdraw') {
                  this.$router.go(-1);
                } else {
                  this.$router.push({ name: 'safe' });
                }
              }
            }
          });
        })
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .tips { padding: .15rem 0; }
  .box { padding:0 .1rem .15rem;}
  .pic-box {
    position: relative;
    background: #fe979b;
    display: table;
    width: 100%;
    height: 1.2rem;
    text-align: center;
    border-radius: .05rem;
    &.success { background: #fe686d;}
    .card {
      display: table-cell;
      position: relative;
      vertical-align: middle;
      .pic { margin-top: -.15rem; width: .9rem; }
      .add {
        width: 50%;
        color: #fff;
        left: 25%;
        font-size: .15rem;
        position: absolute;
        img {
          margin: -.1rem 0 .05rem; width: .49rem;
          &.shadow {box-shadow: 0 2px 2px #666;border-radius: 50%;}
        }
      }
    }
  }
  .demo-box {
    padding: .15rem .15rem 0;
    .txt {width:23%; text-align: center; background: #fff;margin:0 auto -.1rem;position: relative; z-index: 2;}
    ul {
      padding: .3rem 0 .2rem;
      display: flex;
      justify-content: space-between;
      li {
        text-align: center;
        .big { width: .67rem; }
        .small { margin-top:-.12rem; img {width: .17rem;}}
      }
    }
  }
  .operator {margin: .15rem;}
  .photo-upload {
    opacity: 0;
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
  }
</style>
