let wait = 60;
export default {
  methods: {
    // 原生js写法
    getCodeTime(evt, count) {
      wait = count || wait;
      if(wait === 0){
        evt.target.removeAttribute('disabled');
        evt.target.innerText = '获取验证码';
        evt.target.classList.remove('color-999');
        wait = 60;
      } else {
        evt.target.setAttribute('disabled', true);
        evt.target.innerText = wait + 's 重新获取';
        evt.target.classList.add('color-999');
        wait--;
        let timer = setTimeout(() => {
          this.getCodeTime(evt)
        }, 1000);
      }
    }
  }
}
