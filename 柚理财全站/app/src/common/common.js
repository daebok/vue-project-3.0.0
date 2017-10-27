var Publiclib = {
  isnumber:function(text){
    if(text && text != "" && !isNaN(text)){
      return true;
    }
  },
  setint:function(that){
      if(that.value != '' )
      {
        if(that.value.search(/^\d+$/)==-1)
        {
          that.value=(that.value2)?that.value2:'';
        }
        else
        {
          that.value2=that.value
        }
      }
  },
  setdec:function(that){
      if(that.value != '' )
      {
        if(that.value.search(/^([1-9]\d*|0)(?:\.\d{0,2})?$/)==-1)
        {
          that.value=(that.value2)?that.value2:'';
        }
        else
        {
          that.value2=that.value
        }
      }
  },
  rechargeCheck:function(that){
      if(that.value != '' )
      {
        if(that.value.search(/^([1-9]\d{0,7}|0)(?:\.\d{0,2})?$/)==-1)
        {
          that.value=(that.value2)?that.value2:'';
        }
        else
        {
          that.value2=that.value
        }
      }
  },  
  setnegative:function(that){
      if(that.value != '' )
      {
        if(that.value.search(/^(\-?)(\d*|0)+\.?\d{0,1}$/)==-1)
        {
          that.value=(that.value2)?that.value2:'';
        }
        else
        {
          that.value2=that.value
        }
      }
  },
  getAge:function(value,systemTime){
    let age = new Date(value),
    year=age.getFullYear();

    let now = new Date(systemTime),
    nowYear = now.getFullYear();
    return (nowYear - year)+"岁";
  },
  //时间格式
  formatDate:function(value,type) {
    if(value==""){
      return "--";
    }
    let now = new Date(value),
    year=now.getFullYear(),
    month=now.getMonth()+1,
    date=now.getDate(),
    hour=now.getHours(),
    minute=now.getMinutes(),
    second=now.getSeconds();
    if(month < 10)
    {
      month = '0' + month;
    }
    if(date < 10)
    {
      date = '0' + date;
    }
    if(type == 2){
      if(second < 10)
      {
        second = '0' + second;
      }
      if(minute < 10)
      {
        minute = '0' + minute;
      }
      return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
    }
    else if(type == 3){
      if(second < 10)
      {
        second = '0' + second;
      }
      if(minute < 10)
      {
        minute = '0' + minute;
      }
      return year+"-"+month+"-"+date+" "+hour+":"+minute;
    }
    else if(type == 4){
      return year+"年"+month+"月"+date+"日";
    }
    else
    {
      return year+"-"+month+"-"+date;
    }
  },
  formatDateInvest:function(value) {
    if(value==""){
      return "--";
    }
    var now = new Date(value),
        year=now.getFullYear(),
        month=now.getMonth()+1,
        date=now.getDate(),
        hour=now.getHours(),
        minute=now.getMinutes(),
        second=now.getSeconds();
    var now1 = new Date(),
        NowYear=now1.getFullYear(),
        NowMonth=now1.getMonth()+1,
        NowDate=now1.getDate();
    var now2 = new Date();
    now2.setDate(now2.getDate()+1);
    var Tyear=now2.getFullYear(),
        Tmonth=now2.getMonth()+1,
        Tdate=now2.getDate();

    if(month < 10)
    {
      month = '0' + month;
      NowMonth = '0' + NowMonth;
      Tmonth = '0' + Tmonth;
    }
    if(date < 10)
    {
      date = '0' + date;
      NowDate = '0' + NowDate;
      Tdate = '0' + Tdate
    }
    if(second < 10)
    {
      second = '0' + second;
    }
    if(minute < 10)
    {
      minute = '0' + minute;
    }
    if(year==NowYear&&month==NowMonth&&date==NowDate){
      return "今日"+" "+hour+":"+minute+":"+second;
    }else if(year==Tyear&&month==Tmonth&&date==Tdate){
      return "明日"+" "+hour+":"+minute+":"+second;
    }else{
      return month+"-"+date+" "+hour+":"+minute+":"+second;
    }


  },
  //时间格式 月-日  hh:ss
  formatDatetype:function(value,type) {
    let now = new Date(value),
    year=now.getFullYear(),
    month=now.getMonth()+1,
    date=now.getDate(),
    hour=now.getHours(),
    minute=now.getMinutes(),
    second=now.getSeconds();
    if(month < 10)
    {
      month = '0' + month;
    }
    if(date < 10)
    {
      date = '0' + date;
    }
    if(type == 2){
      if(second < 10)
      {
        second = '0' + second;
      }
      if(minute < 10)
      {
        minute = '0' + minute;
      }
      return month+"月"+date+"日 "+hour+":"+minute+"开售";
    } if(type == 3){
        if(second < 10){
          second = '0' + second;
        }if(minute < 10){
          minute = '0' + minute;
        }
        return hour+"<em>时</em>"+minute+"<em>分</em>"+second+"<em>秒</em>";
    }
    else
    {
      return month+"-"+date;
    }
  },
  //时间差计算
  formatDatetype1:function(value,value2) {
    let time = value2 - value,
    d = Math.ceil(time/1000/24/60/60),
    h = Math.ceil(time/1000/60/60),
    m = Math.ceil(time/1000/60),
    s = Math.ceil(time/1000);
    if(d > 1)
    {
      return d+"天前";
    }
    else if(h > 1)
    {
      return h+"小时前";
    }
    else if(m > 1)
    {
      return m+"分钟前";
    }
    else if(s > 1)
    {
      return s+"秒前";
    }
  },
  //时间差计算
  formatDatetype2:function(value,value2) {
    let time = value2 - value,
    d = Math.ceil(time/1000/24/60/60),
    h = Math.ceil(time/1000/60/60),
    m = Math.ceil(time/1000/60),
    s = Math.ceil(time/1000);
    if(d > 1)
    {
      return d+"天";
    }
    else if(h > 1)
    {
      return h+"小时";
    }
    else if(m > 1)
    {
      return m+"分钟";
    }
    else if(s > 1)
    {
      return s+"秒";
    }
  },
   //计算剩余时间
  formatDatetype3:function(value) {
    var days=Math.floor(value/(24*3600*1000));
    //计算出小时数
    var leave1=value%(24*3600*1000)  ;  //计算天数后剩余的毫秒数
    var hours=Math.floor(leave1/(3600*1000));
    //计算相差分钟数
    var leave2=leave1%(3600*1000)  ;      //计算小时数后剩余的毫秒数
    var minutes=Math.floor(leave2/(60*1000));
    //计算相差秒数
    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
    var seconds=Math.round(leave3/1000);
    if(days>0){
      return days+"<em>天</em>"+hours+"<em>时</em>"+minutes+"<em>分</em>"+seconds+"<em>秒</em>";
    }else {
      return hours+"<em>时</em>"+minutes+"<em>分</em>"+seconds+"<em>秒</em>";
    }

  },
  formatDatetype4:function(value) {
    var days=Math.floor(value/(24*3600*1000));
    //计算出小时数
    var leave1=value%(24*3600*1000)  ;  //计算天数后剩余的毫秒数
    var hours=Math.floor(leave1/(3600*1000));
    //计算相差分钟数
    var leave2=leave1%(3600*1000)  ;      //计算小时数后剩余的毫秒数
    var minutes=Math.floor(leave2/(60*1000));
    //计算相差秒数
    var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
    var seconds=Math.round(leave3/1000);
    if(days>0){
      return days+"天"+hours+"时"+minutes+"分"+seconds+"秒";
    }else {
      return hours+"时"+minutes+"分"+seconds+"秒";
    }

  },
   //金额格式
  moneyFormat:function(value) {
  let money = value;
    if(money == 0)
    {
      return money+".00";
    }
    else
    {
      let n = 2;
      money = parseFloat((money + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
      let l = money.split(".")[0].split("").reverse(), r = money.split(".")[1];
      let t = "";
      for (let i = 0; i < l.length; i++) {
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
      }
      return  t.split("").reverse().join("") + "." + r;
    }
  },
  //数字格式
  numberFormat:function(value) {
  let money = value;
    if(money == 0)
    {
      return money;
    }
    else
    {
      let n = 2;
      money = parseFloat((money + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
      let l = money.split(".")[0].split("").reverse(), r = money.split(".")[1];
      let t = "";
      for (let i = 0; i < l.length; i++) {
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
      }
      return  t.split("").reverse().join("");
    }
  },
  //文章截取
  subNameFun:function(value,length1,length2) {
    let content = value.replace(/<[^>].*?>/g,"");
    let str = content.replace(/&nbsp;/ig,'');
    let contents = str;
    if(value.length>length2){
      contents = ""+str.substring(length1,length2)+"...";
    }
    return contents;
  },
  getQueryString:function(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return '';
  },
  //性别
  getSex: function(val){
    return val == "M" ? "男" : "女";
  },
  //学历
  getEducation: function(index){
    return ["小学","初中","高中","中专","大专","本科","硕士","博士","其他"][(parseInt(index)-1)];
  },
  //工作年限
  getWorkExperience: function(index){
    return ["0-3年","3-5年","5-8年","8年以上"][(parseInt(index)-1)];
  },
  //月收入范围
  getMonthIncomeRange: function(index){
    return ["3000元以下","3000-5000元","5000-8000元","8000-10000元","10000-130000元","13000-15000元","15000-20000元","20000元以上"][(parseInt(index)-1)];
  },
  //婚姻状况
  getMaritalStatus: function(index){
    return ["未婚","已婚","离异","丧偶"][(parseInt(index))];
  },
  //有无车产
  getCarStatus: function(val){
    if(val === undefined) return false;
    return (parseInt(val) === 0) ? "无" : "有";
  },
  //有无房产
  getHouseStatus: function(val){
    if(val === undefined) return false;
    return (parseInt(val) === 0) ? "无" : "有";
  },
  //投资方式
  getInvestType: function(type){
    return ["网站投资","自动投资","手机投资"][(parseInt(type))];
  },
  //借款性质
  borrowNature: function(index){
    return ["个人借款","企业借款"][(parseInt(index)-1)];
  },
  //还款方式
  repayStyle: function(index){
    return ["月等额本息","一次性还款","每月还息到期还本","等额本金"][(parseInt(index)-1)];
  },
  //审核状态
  vouchStatus: function(index){
    return ["待审核","同意担保","拒绝担保"][(parseInt(index))];
  }


};

export{Publiclib}
