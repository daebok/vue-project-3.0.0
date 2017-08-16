<template>
  <span class="count-down">
    {{countTime}}
  </span>
</template>

<script>
  export default {
    data(){
      return {
        countTime: ''
      }
    },
    props: {
      remainTimes: {
        type: Number,
        default: 0
      }
    },
    methods: {
      countDown(){
        var sys_sec = Number(this.remainTimes)
        if(sys_sec > 0){
          clearInterval(this.interval)
          this.interval = setInterval(() => {
            if(sys_sec > 1){
              sys_sec -= 1
              let min = Math.floor(sys_sec/60)
              let sec = Math.floor(sys_sec%60)
              let showMin = min<10?'0'+min : min  //计算分钟
              let showSec = sec<10?'0'+sec : sec  //计算秒数
              this.countTime = showMin + ':' + showSec
            }else{
              clearInterval(this.interval)
              window.location.reload()
            }
          }, 1000)
        }
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
    font-size: .13rem;
    color: $main-color;
  }
</style>
