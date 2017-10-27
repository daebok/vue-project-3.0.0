export default {
  data () {
    return {
      wait: 60
    }
  },
  methods: {
    // 原生js写法
    getCodeTime(evt, count) {
      if(count) this.wait = count
      if(this.wait === 0){
        evt.target.removeAttribute('disabled');
        evt.target.innerText = '获取验证码';
        evt.target.classList.remove('color-999');
        this.wait = 60;
      } else {
        evt.target.setAttribute('disabled', true);
        evt.target.innerText = this.wait + '秒后重试';
        evt.target.classList.add('color-999');
        this.wait--;
        let timer = setTimeout(() => {
          this.getCodeTime(evt)
        }, 1000);
      }
    }
  }
}
