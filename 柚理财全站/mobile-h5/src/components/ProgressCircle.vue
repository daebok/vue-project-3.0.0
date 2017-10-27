<template>
  <div class="circle" :style="{width: diameter + 'px', height: diameter + 'px'}">
    <svg :width="diameter" :height="diameter" :viewPort="'0 0' + diameter + ' ' + diameter" version="1.1" xmlns="http://www.w3.org/2000/svg" class="svg-circle">
      <circle
        :r="radius-lineWidth"
        :cx="radius"
        :cy="radius"
        fill="transparent"
        :stroke="backgroundColor"
        :stroke-width="lineWidth"
        :stroke-dasharray="dasharray"
        stroke-dashoffset="0"
      ></circle>
      <circle
        class="bar"
        :r="radius-lineWidth"
        :cx="radius"
        :cy="radius"
        fill="transparent"
        :stroke="foregroundColor"
        :stroke-width="lineWidth"
        :stroke-dasharray="dasharray"
        :stroke-dashoffset="(100-circlePercent)/100*(diameter-lineWidth*2)*3.141592658 + 'px'"
        :style="{'transition-duration': (duration/1000)+'s', '-webkit-transition-duration': (duration/1000)+'s'}"
      ></circle>
    </svg>
    <div class="text" :class="{'sale-time': showTime}"
         :style="{color: textColor, 'font-size': textSize+'px'}">{{textFormat.replace('{percent}', textPercent)}}<span v-if="showFlag" style="font-size:12px;">%</span></div>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data() {
      return {
        duration: 0,
        textPercent: 0, // the really percent used in text
        circlePercent: 0 // the really percent used in circle
      };
    },
    props: {
      percent: { // 进度百分比, 0 ~ 100
        type: [Number, String],
        default: 0,
        required: true
      },
      showFlag: { // 是否显示百分号
        type: Boolean,
        default: true,
      },
      showTime: { // 是否显示园中心文字
        type: Boolean,
        default: false,
      },
      diameter: { // the diameter of circle, include line width
        type: Number,
        default: 100
      },
      lineWidth: { // the line width of circle
        type: Number,
        default: 8
      },
      foregroundColor: { //圆边框色
        type: String,
        default: '#04BE02'
      },
      backgroundColor: { //圆边框背景色
        type: String,
        default: 'rgba(0, 0, 0, .3)'
      },
      textFormat: { //圆中心文字
        type: String,
        default: '{percent}' // format the text in circle
      },
      textColor: { //圆中心文字颜色
        type: String,
        default: '#999'
      },
      textSize: { //圆中心文字颜色大小
        type: Number,
        default: 24
      }
    },
    computed: {
      dasharray() {
        return (this.diameter - this.lineWidth * 2) * Math.PI;
      },
      radius() {
        return this.diameter / 2;
      }
    },
    watch: {
      percent(v, ov) {
        this.percent = v;
        this.$nextTick(this.sync);
      }
    },
    methods: {
      sync() { // sync p with percent in 'duration' time
        let t = this.textPercent;
        let i = 0;
        clearInterval(this.interval);
        // increase text percent with interval
        this.interval = setInterval(() => {
          if (i >= this.duration) {
            clearInterval(this.interval);
            this.textPercent = this.percent;
            return;
          }
          t += (this.percent - this.textPercent) / 10; // increase t
          this.textPercent = parseInt(t);
          i += 20;
        }, 20);
        // increase circle
        this.circlePercent = this.percent;
      }
    },
    created() {
      if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {  //判断iPhone|iPad|iPod|iOS
        this.duration = 200;
      }
    },
    mounted() {
      this.$nextTick(function() {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
          this.sync();
        }, 200); // delay for initial value
      })
    },
    destroyed() {
      this.$nextTick(function() {
        clearTimeout(this.timeout);
        clearInterval(this.interval);
      })
    }
  }
</script>

<style type="text/css" scoped>
  .circle svg {
    -webkit-transition-property: stroke-dashoffset;
    -moz-transition-property: stroke-dashoffset;
    -ms-transition-property: stroke-dashoffset;
    -o-transition-property: stroke-dashoffset;
    transition-property: stroke-dashoffset;
    -webkit-transform: rotate(-90deg);
    -moz-transform: rotate(-90deg);
    -ms-transform: rotate(-90deg);
    -o-transform: rotate(-90deg);
    transform: rotate(-90deg);
  }
  .circle {
    position: relative;
    margin: .2rem auto 0;
  }
  .text {
    font-family:'arial';
    position: absolute;
    height: 32px;
    line-height: 32px;
    top: 50%;
    left: 0;
    margin-top: -16px;
    text-align: center;
    width: 100%;
  }
  .text.sale-time {
    line-height: 14px;
    margin-top: -12px;
  }
</style>
