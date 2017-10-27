<template>
  <div class="wrapper-scroll" :style="{height: h + 'rem'}">
    <div :id="`${id}wrapper`" class="wrapper">
      <div :id="`${id}scroller`" :style="{width: scrollWidth + 'px'}" class="scroller aui-border-b" >
        <slot></slot>
      </div>
    </div>
  </div>
</template>
<script>
  import iScroll from 'iscroll/build/iscroll'
  const DIRECTION_H = 'horizontal'
  const DIRECTION_V = 'vertical'
  export default {
    props: {
      id: {
        type: String,
        default: ''
      },
      h: {
        type: [Number, String],
        default: 36
      },
      data: {
        type: Array,
        default: null
      },
      refreshDelay: {
        type: Number,
        default: 20
      },
      direction: {
        type: String,
        default: DIRECTION_H
      }
    },
    data() {
      return {
        scrollWidth: document.documentElement.clientWidth
      }
    },
    mounted() {
      this.scrollWidth = document.documentElement.clientWidth + 100 // 根据个数先默认宽度
      setTimeout(() => {
        this._initScroll()
      }, 20)
    },
    methods: {
      _initScroll() {
        if(!document.getElementById(`${this.id}wrapper`)) {
          return
        }
        this[`scroll${this.id}`] = new iScroll(`#${this.id}wrapper`, {
          scrollY: false,
          scrollX: true,
          bounce: false,
          click: true,
          mouseWheel: false
        })
        // document.getElementById(`${this.id}scroller`).style.width = this.scrollWidth + 'px'
        this[`flag${this.id}`] = true
        this[`scroll${this.id}`].on('beforeScrollStart', () => {
          if(this[`flag${this.id}`]){ // 执行一次计算宽度
            this[`flag${this.id}`] = false
            let swipe_item = document.getElementById(`${this.id}wrapper`).querySelectorAll('.menu-item')
            //let total_w = 0
            let total_w = 1
            swipe_item.forEach( val => {
              total_w += val.clientWidth
            })
            this.scrollWidth = total_w
            setTimeout(() => {
              this.refresh()
            }, 10)
          }
        })
        document.getElementById(`${this.id}wrapper`).addEventListener('touchmove', function (e) {
          e.preventDefault()
        },false)
      },
      scrollToElement(el) {
        this[`scroll${this.id}`].scrollToElement(el)
      },
      refresh() {
        this[`scroll${this.id}`].refresh()
      }
    },
    watch: {
      data() {
        setTimeout(() => {
          this.refresh()
        }, this.refreshDelay)
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "../../assets/scss/var"
  .aui-border-b:after
    border:none!important
  .wrapper-scroll
    position: relative

  .wrapper
    z-index: 1
    position: absolute
    top: 0
    left: 0
    width: 100%
    height: 100%
    background: #fff
    overflow: hidden
    border-bottom: 1px solid #ddd;

    .scroller
      z-index: 1
      position: absolute
      height: 100%
      text-size-adjust: none
      background: #fff
</style>