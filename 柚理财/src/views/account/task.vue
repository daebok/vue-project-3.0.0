<template>
  <div class="page">
    <div class="head">
      <img src="../../assets/images/money/task_integral.png" />
      <p>积分：<span>0</span></p>
    </div>
    <div class="new-task">
      <p>新手任务</p>
      <div class="module-list">
        <ul>
          <li class="aui-border-b finish" @click="toUrl('register', 1)">
            <div class="con ">
              <img src="./../../assets/images/money/task_yonghu.png"> 用户注册
            </div>
          </li>
          <li class="aui-border-b" :class="{'finish': resdata.realNameStatus == 1}" @click="toUrl('realname', resdata.realNameStatus)">
            <div class="con ">
              <img src="./../../assets/images/money/task_renzheng.png"> 用户认证
            </div>
          </li>
          <li class="aui-border-b" :class="{'finish': resdata.emailStatus == 1}" @click="toUrl('bindEmail', resdata.emailStatus)">
            <div class="con ">
              <img src="./../../assets/images/money/task_youxiang.png"> 邮箱认证
            </div>
          </li>
          <li class="" :class="{'finish': resdata.investStatus == 1}" @click="toUrl('invest', resdata.investStatus)">
            <div class="con " >
              <img src="./../../assets/images/money/task_touzi.png"> 首次投资
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="daily-task">
      <p>日常任务</p>
      <ul>
        <li @click="signIn"><img src="../../assets/images/money/task_sign.png" > 每日签到</li>
        <li @click="dailyInvest"><img src="../../assets/images/money/task_investment.png" > 每日投资</li>
      </ul>
    </div>
    <div class="other-task">
      <p>其他任务</p>
      <router-link to="/mine/invite"><div class="bg task-score"></div></router-link>
      <router-link to="/shop/lottery"><div class="bg task-game"></div></router-link>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  export default {
    data() {
      return {
        resdata: '',
        params: {
          __sid: this.$store.state.user.__sid
        }
      }
    },
    created() {
      this.init()
    },
    methods: {
      init() {
        this.$http.get(ajaxUrl.taskStatusAjax, { params: this.params }).then((res) => {
          this.resdata = res.data.resData
        })
      },
      toUrl(name, val) {
        if(val == 1) return
        this.$router.push({name: name})
      },
      signIn() {
        // if(this.resdata.dailySignIn == 0){
          this.$http.get(ajaxUrl.signInAjax, { params: this.params }).then((res) => {
            this.$toast(res.data.resData.msg)
          })
        // }
      },
      dailyInvest() {
        if(this.resdata.dailyInvest == 0){
          this.$router.push('/invest')
        }
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss">
  .head {
    height: 1.05rem;
    text-align: center;
    padding-top: .2rem;
    color: #fff;
    background: url(../../assets/images/money/task_bg.png) center/contain no-repeat;
    img { width: .35rem; }
    p { margin-top: .05rem; }
  }
  .new-task {
    p { padding: .1rem; color: #666;  }
    .module-list {
      ul {
        background: #fff;
        padding-left: .1rem;
        li {
          font-size: 0;
          &.finish {
            background: url(../../assets/images/money/task_gaizhang.png) 88% center/.39rem auto no-repeat;
          }
          .con {
            img {
              width: .21rem;
              vertical-align: -4px;
              margin-right: .05rem;
            }
            height: .4rem;
            line-height: .4rem;
            background: url(../../assets/images/public/arrow_right.png) 95% center/auto 35% no-repeat;
            font-size: .15rem;
            color: #333;
            position: relative;
          }
        }
      }
    }
  }
  .daily-task {
    p { padding: .1rem; color: #666;  }
    ul {
      background: #fff;
      padding: .2rem 0;
      display: -webkit-box;
      display: flex;
      text-align: center;
      li {
        display: inline-flex;
        flex: 1;
        justify-content: center;
        line-height: .4rem;
        color: #666;
        img { width: .4rem; height: .4rem; margin-right: .1rem; }
      }
    }
  }
  .other-task {
    p {
      padding: .1rem;
      color: #666;
    }
    .bg {
      height: 1.1rem;
      background: center/contain no-repeat;
      &.task-score {
        background-image: url(../../assets/images/money/task_integral_bg.png)
      }
      &.task-game {
        margin-top: .1rem;
        background-image: url(../../assets/images/money/task_turntable_bg.png)
      }
    }
  }
</style>
