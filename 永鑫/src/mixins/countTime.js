import utils from  '../filters'
export default {
  methods: {
    countDown(end, start){
      var sys_sec = parseFloat((end - start)/1000)
      if(sys_sec > (24*3600)){
        let day = Math.ceil((sys_sec/3600)/24)
        this.countTime = day + '天'
      }else if(sys_sec > 0){
        var timer = setInterval(() => {
          if(sys_sec > 1){
            sys_sec -= 1
            let day = Math.floor((sys_sec/3600)/24)
            let hour = Math.floor((sys_sec/3600)%24)
            let min = Math.floor((sys_sec/60)%60)
            let sec = Math.floor(sys_sec%60)
            let showday = day
            let showHour = hour<10?'0'+hour : hour  //计算小时
            let showMin = min<10?'0'+min : min  //计算分钟
            let showSec = sec<10?'0'+sec : sec  //计算秒数
            this.countTime = showHour + '时' + showMin + '分' + showSec + '秒'
          }else{
            clearInterval(timer)
            window.location.reload()
          }
        }, 1000)
      }
    },
    //end--开始时间,start代表当前时间
    countDownSale(end, start){  //用在小于3小时
      var sys_sec = parseFloat((end - start)/1000)
      if(new Date(end).getDate() !== new Date(start).getDate()){ // 不是当天
        this.countTimeSale = utils.dateFormatFun(end, 3) + ' 开始抢购'
      }else if(sys_sec > (3*3600)){ // 大于3小时
        this.countTimeSale = utils.dateFormatFun(end, 5) + ' 即将开售'
      }
      else{
        var timer = setInterval(() => {
          if(sys_sec > 1){
            sys_sec -= 1
            let day = Math.floor((sys_sec/3600)/24)
            let hour = Math.floor((sys_sec/3600)%24)
            let min = Math.floor((sys_sec/60)%60)
            let sec = Math.floor(sys_sec%60)
            let showday = day
            let showHour = hour<10?'0'+hour : hour  //计算小时
            let showMin = min<10?'0'+min : min  //计算分钟
            let showSec = sec<10?'0'+sec : sec  //计算秒数
            this.countTimeSale = '距离开抢 '+showHour + ':' + showMin + ':' + showSec
          }else{
            clearInterval(timer)
            // window.location.reload()
          }
        }, 1000)
      }
    }
  }
}