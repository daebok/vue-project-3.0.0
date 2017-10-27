// 判断是否支持sticky属性
function isSupportSticky () {
  var prefixTestList = ['', '-webkit-', '-ms-', '-moz-', '-o-']
  var stickyText = ''
  for (var i = 0; i < prefixTestList.length; i++) {
    stickyText += 'position:' + prefixTestList[i] + 'sticky'
  }
  // 创建一个dom来检查
  var div = document.createElement('div')
  var body = document.body
  div.style.cssText = 'display:none' + stickyText
  body.appendChild(div)
  var isSupport = /sticky/i.test(window.getComputedStyle(div).position)
  body.removeChild(div)
  div = null
  return isSupport
}
export default  {
  bind(el, binding){
    if (isSupportSticky()) {
      // 大于等于iOS6版本使用sticky
      el.classList.add('vux-sticky')
    } else {
      var navOffsetY = el.offsetTop
      // window.addEventListener('scroll', function () {
      //   window.scrollY >= 100 ? el.classList.add('vux-fixed') : el.classList.remove('vux-fixed')
      // })
    }
  },
  install(Vue) {
    Vue.directive('sticky', {
      bind: this.bind
    })
  }

}
