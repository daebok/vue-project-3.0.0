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
export default function (nav) {
  if (isSupportSticky()) {
    // 大于等于iOS6版本使用sticky
    nav.classList.add('vux-sticky')
  } else {
    var navOffsetY = nav.offsetTop
    window.addEventListener('scroll', function () {
      window.scrollY >= navOffsetY ? nav.classList.add('vux-fixed') : nav.classList.remove('vux-fixed')
    })
  }
}
