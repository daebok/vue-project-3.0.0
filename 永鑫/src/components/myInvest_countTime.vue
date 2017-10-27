<!-- 列表倒计时插件 -->
<template>
  <span class="count-down">
    {{countTime}}
  </span>
</template>

<script>
  import utils from  '../filters' //引入过滤器
  export default {
    data(){
      return {
        countTime: ''
      }
    },
    props: {
      saleTime: {
        type: Number,
        default: 0
      },
      countdownTime: {
        type: Number,
        default: 0
      }
    },
    methods: {
      //saleTime---开始时间,now---当前时间
      countDown(){
        //console.log(this.saleTime);
        //console.log(this.countdownTime);
        var now = Date.parse(new Date()); //当前时间
        var sys_sec = Number(this.countdownTime) //倒计时时间
        if(new Date(this.saleTime).getDate() != new Date().getDate()){ // 开售时间不是当天
          this.countTime = '该产品于' + utils.dateFormatFun(this.saleTime, 3) + ' 开始抢购'
        }else if(sys_sec > (3*3600)){ // 开售时间大于3小时
          this.countTime = '该产品于今日 ' + utils.dateFormatFun(this.saleTime, 5) + ' 开售'
        }else{
          if(sys_sec > 0){
            clearInterval(this.interval)
            this.interval = setInterval(() => {
              if(sys_sec > 1){
                sys_sec -= 1
                let day = Math.floor((sys_sec/3600)/24)
                let hour = Math.floor((sys_sec/3600)%24)
                let min = Math.floor((sys_sec/60)%60)
                let sec = Math.floor(sys_sec%60)
                let showday = day //计算天数
                let showHour = hour<10?'0'+hour : hour  //计算小时
                let showMin = min<10?'0'+min : min  //计算分钟
                let showSec = sec<10?'0'+sec : sec  //计算秒数
                this.countTime = '该产品距离开抢还有 '+showHour + ':' + showMin + ':' + showSec
              }else{
                clearInterval(this.interval)
                window.location.reload();  //刷新页面
              }
            }, 1000)
          }
        }
        //模板代码
        // var sys_sec = Number(this.saleTime)
        // if(sys_sec > 0){
        //   clearInterval(this.interval)
        //   this.interval = setInterval(() => {
        //     if(sys_sec > 1){
        //       sys_sec -= 1
        //       let min = Math.floor(sys_sec/60)
        //       let sec = Math.floor(sys_sec%60)
        //       let showMin = min<10?'0'+min : min  //计算分钟
        //       let showSec = sec<10?'0'+sec : sec  //计算秒数
        //       this.countTime = showMin + ':' + showSec
        //     }else{
        //       clearInterval(this.interval)
        //       window.location.reload()
        //     }
        //   }, 1000)
        // }
      }
    },
    created () {
      this.countDown()
    },
    destroyed () {
      this.$nextTick(function () {
        clearInterval(this.interval)
      })
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../assets/scss/var.scss";
  .count-down {
    color: #F5691C;
    margin-top:.05rem;
    display:block;
  }
</style>