/*
 * 垂直滚动动画
 * 参数
 * dom 挂载dom节点 id名
 * lh 滚动高度
 * speed 滚动速度
 * delay 间隔时间
 */
export default {
  methods: {
    startmarquee(dom, lh, speed, delay) {
      var p = false;
      var t;
      var o = document.getElementById(dom);
      o.innerHTML += o.innerHTML;
      o.style.marginTop = 0;
      o.onmouseover = () => {
        p = true;
      };
      o.onmouseout = () => {
        p = false;
      };
      var start = () => {
        t = setInterval(scrolling, speed);
        if (!p) {
          o.style.marginTop = parseInt(o.style.marginTop) - 1 + 'px';
        }
      };
      var scrolling = () => {
        if (parseInt(o.style.marginTop) % lh !== 0) {
          o.style.marginTop = parseInt(o.style.marginTop) - 1 + 'px';
          if (Math.abs(parseInt(o.style.marginTop)) >= o.scrollHeight / 2) {
            o.style.marginTop = 0;
          }
        } else {
          clearInterval(t);
          setTimeout(start, delay);
        }
      };
      setTimeout(start, delay);
    }
  }
};
