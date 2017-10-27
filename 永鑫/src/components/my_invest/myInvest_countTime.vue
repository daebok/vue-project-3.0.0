<!-- 支付倒计时 by fenglei -->
<template>
  <span>{{ countTime }}</span>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        countTime: '00:00'
      }
    },
    props: {
      remainTimes: {
        type: Number,
        default: 0
      },
      index: {
        type: Number,
        default: -1
      }
    },
    created() {
      this.countDown();
    },
    destroyed() {
      this.$nextTick(() => {
        clearInterval(this.interval);
      })
    },
    methods: {
      countDown() {
        var sys_sec = Number(this.remainTimes);
        if (sys_sec > 0) {
          clearInterval(this.interval);
          this.interval = setInterval(() => {
            if (sys_sec > 1) {
              sys_sec -= 1;
              let min = Math.floor(sys_sec / 60);
              let sec = Math.floor(sys_sec % 60);
              let showMin = min < 10 ? '0' + min : min;  // 计算分钟
              let showSec = sec < 10 ? '0' + sec : sec;  // 计算秒数
              this.countTime = showMin + ':' + showSec;
            } else {
              clearInterval(this.interval);
              this.$emit('contDownOver', this.index);
            }
          }, 1000)
        }
      }
    }
  }
</script>
