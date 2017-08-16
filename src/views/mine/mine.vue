<!-- 我的 -->
<template>
  <div class="page">
    <div class="content">
      <!-- S用户头像登录信息 -->
      <section class="text-center" style="margin: .45rem 0 .3rem;">
        <span class="head-wrapper" @click="toLogin">
          <!-- S未登录 -->
          <div v-if="!isLogin">
            <span  class="headImg-wrapper"></span>
            <i>登录/注册</i>
          </div>
          <!-- E未登录 -->
          <!-- S已登录 -->
          <div v-else>
            <!-- 已登录未设置头像 -->
            <span v-if="!avatar" class="headImg-wrapper status-login">
              <!-- 登陆状态等级 -->
              <img v-if="resdata.vipLevel > 0" src="./../../assets/images/me/icon_V1.png" class="grade-img">
            </span>
            <!-- 已登录已设置头像 -->
            <span v-else class="headImg-wrapper" :style="'background-image:url('+avatar+')'">
              <!-- 登陆状态等级 -->
              <img v-if="resdata.vipLevel == 1" src="./../../assets/images/me/icon_V1.png" class="grade-img">
              <img v-else-if="resdata.vipLevel == 2" src="./../../assets/images/me/icon_V2.png" class="grade-img">
              <img v-else-if="resdata.vipLevel == 3" src="./../../assets/images/me/icon_V3.png" class="grade-img">
              <img v-else-if="resdata.vipLevel == 4" src="./../../assets/images/me/icon_V4.png" class="grade-img">
              <img v-else-if="resdata.vipLevel == 5" src="./../../assets/images/me/icon_V5.png" class="grade-img">
              <img v-else-if="resdata.vipLevel == 6" src="./../../assets/images/me/icon_V6.png" class="grade-img">
            </span>
            <i>{{ resdata.hideUserName }}</i>
          </div>
          <!-- E已登录 -->
        </span>
      </section>
      <!-- E用户头像登录信息 -->
      <!-- S菜单 -->
      <ul class="mineLink-list">
        <li>
          <router-link to="/mine/siteIntro">
            <p class="aui-border-b">
              <img src="./../../assets/images/me/me_icon_notice.png" class="me-img">
              平台公告
              <!-- 未读消息圆点 -->
              <i v-if="showDot" class="no-read"></i>
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
        <li>
          <router-link to="/mine/invite">
            <p class="aui-border-b">
              <img src="./../../assets/images/me/me_icon_ivitation.png" class="me-img">
              邀请好友
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
        <li>
          <router-link to="/mine/siteData">
            <p>
              <img src="./../../assets/images/me/me_icon_data.png" class="me-img">
              平台数据
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
      </ul>
      <ul class="mineLink-list margin-t-10">
        <li>
          <router-link to="/mine/help">
            <p class="aui-border-b">
              <img src="./../../assets/images/me/me_icon_help.png" class="me-img">
              帮助中心
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
        <li>
          <router-link to="/mine/question_feedback">
            <p class="aui-border-b">
              <img src="./../../assets/images/me/me_icon_feedback.png" class="me-img">
              问题反馈
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
        <li>
          <router-link to="/mine/about_us">
            <p>
              <img src="./../../assets/images/me/me_icon_introduction.png" class="me-img">
              关于我们
              <img src="./../../assets/images/public/arrow_right.png" class="right-img">
            </p>
          </router-link>
        </li>
      </ul>
      <!-- E菜单 -->
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js'; // 引入所有接口地址

  export default {
    data() {
      return {
        isLogin: localStorage.user, // 是否登录
        resdata: '', // 当前登录用户信息
        avatar: '', // 用户头像
        showDot: false // 平台公告是否有新消息
      };
    },
    created() {
      if (localStorage.user) {
        let getParams = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.get(ajaxUrl.basicInfo, { params: getParams }).then((res) => {
          this.resdata = res.data.resData;
          this.avatar = res.data.resData.avatarPhoto;
        })
      }
      /*this.$http.get(ajaxUrl.getArticleList, {params: {sectionCode: 'notice','page.page': 1}}).then((res) => {
        if(res.data.resData.list.length > 0){ // 检查是否有新的公告
          let c_time = res.data.resData.list[0].createTime
          if(localStorage.noticeTime && localStorage.noticeTime != c_time){ //显示平台公告红点
            this.showDot = true;
          }else{
            localStorage.noticeTime = c_time
          }
        }
      })*/
    },
    methods: {
      toLogin() {
        if (this.$store.state.user.userId) {
          this.$router.push('/mine/safe');
        } else {
          this.$router.push('/login?redirect=/mine');
        }
      }
    }
  }
</script>

<style type="text/css" scoped>
  .head-wrapper{text-align: center; padding: .3rem 0;}
  .headImg-wrapper{position: relative; display: inline-block;background: url(./../../assets/images/me/me_pic_head_gray.png) no-repeat;
    width: .68rem; height: .68rem; background-size: .68rem .68rem; border-radius:50%;}
  /* 登陆状态样式覆盖 */
  .headImg-wrapper.status-login{background-image: url(./../../assets/images/me/me_pic_head.png);}
  .grade-img{width: .15rem; position: absolute; bottom: 0; right: .06rem;}
  .head-wrapper i{display: block; margin-top: .18rem;}
  .mineLink-list li{padding-left: .15rem; background-color: #fff;}
  .mineLink-list li p{padding: .15rem .15rem .15rem 0; font-size: .16rem;}
  .me-img{width: .21rem; float: left; margin-right: .13rem; position: relative; bottom: .025rem;}
  .right-img{float: right; width: .14rem; position: relative; top:.02rem;}
  .no-read{background-color: #f95a28; display: inline-block; width: .06rem; height: .06rem;
    border-radius: .06rem; position: relative; bottom: .02rem;}
</style>
