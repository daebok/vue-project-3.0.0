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
        // scrollWidth: 578
      }
    },
    computed: {
      scrollWidth() {
        var len = this.data.length
        var paddingPlus = 20 * len   // 每个标题padding左右为10
        var fontWidth = 0 // 先初始字体宽为0
        var totalW
        for(var i = 0; i < len; i++) {
          var name = this.data[i].typeName.toString()  // 每个标题的长度
          fontWidth += name.length * 14
        }
        totalW = paddingPlus + fontWidth
        if(totalW < document.documentElement.clientWidth){ //小于屏幕宽
          totalW = document.documentElement.clientWidth
        }
        return totalW
      }
    },
    mounted() {
      setTimeout(() => {
        this._initScroll()
      }, 20)
      setTimeout(() => {
        if(this.scrollWidth == document.documentElement.clientWidth){
          var itemArr = document.querySelectorAll('.menu-item')
          // console.log(itemArr)
          itemArr.forEach(val => {
            val.style.flex = 1
          })
        }
      }, 100)
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
          click: true, //手机点击无效bug
          mouseWheel: false
        })
//        this[`flag${this.id}`] = true
//        this[`scroll${this.id}`].on('beforeScrollStart', () => {
//          if(this[`flag${this.id}`]){ // 执行一次计算宽度
//            this[`flag${this.id}`] = false
//            let swipe_item = document.getElementById(`${this.id}wrapper`).querySelectorAll('.menu-item')
//            let total_w = 0
//            swipe_item.forEach( val => {
//              total_w += val.clientWidth
//            })
//            if(total_w > this.scrollWidth){
//              this.scrollWidth = total_w
//              setTimeout(() => {
//                this.refresh()
//              }, 10)
//            }
//          }
//        })
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
  .wrapper-scroll
    position: relative

  .wrapper
    z-index: 1
    position: absolute
    top: 0
    left: 0
    width: 100%
    height: 100%
    /*background: #ccc*/
    overflow: hidden

    .scroller
      z-index: 1
      position: absolute
      height: 100%
      text-size-adjust: none
      background: #fff
      display: flex
</style>
