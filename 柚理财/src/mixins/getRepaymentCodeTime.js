let indexType = 0; //索引值
export default {
  methods: {
    // 原生js写法
    getCodeTime(evt, count) {
      let wait = count; //传入倒计时值
      indexType++; //索引值+1
      let timer = "timer"+indexType; //定时器变量
      if(indexType > 1){
        //console.log("timer"+parseInt(indexType-1));
        clearInterval("timer"+parseInt(indexType-1)); //清楚前一个计时器
      }
      timer = setInterval(() => {
        if(wait == 0){
          clearInterval(timer);
          evt.target.removeAttribute('disabled');
          evt.target.innerText = '获取验证码';
          evt.target.classList.remove('color-999');
          wait = 60;
        } else {
          evt.target.setAttribute('disabled', true);
          evt.target.innerText = wait + 's 重新获取';
          evt.target.classList.add('color-999');
          wait--;
        }
      }, 1000);
    }
  }
}