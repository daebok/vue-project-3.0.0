/** 格式化时间
 *  @param {string} time 需要格式化的时间
 *  @param {bool} friendly 是否是fromNow
 */
const fmtDate = (date, fmt) => {
  var o = {
    'M+': date.getMonth() + 1, // 月份
    'd+': date.getDate(), // 日
    'h+': date.getHours(), // 小时
    'm+': date.getMinutes(), // 分
    's+': date.getSeconds(), // 秒
    'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
    'S': date.getMilliseconds() // 毫秒
  };
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    }
  }
  return fmt;
};
exports.dateFormatFun = (time,fmt = 1) => {
  if (time > 0) {
    switch (fmt) {
      case 1:
        return fmtDate(new Date(time), 'yyyy-MM-dd');
      case 2:
        return fmtDate(new Date(time), 'yyyy-MM-dd hh:mm');
      case 3:
        return fmtDate(new Date(time), 'MM月dd日 hh:mm');
      case 4:
        return fmtDate(new Date(time), 'yyyy-MM-dd hh:mm:ss');
      case 5:
        return fmtDate(new Date(time), 'hh:mm');
      default:
        return fmtDate(new Date(time), 'yyyy-MM-dd');
    }
  }
};
const digitsRE = /(\d{3})(?=\d)/g;
exports.currency = (value, currency, decimals) => {
  value = parseFloat(value);
  if (!isFinite(value) || (!value && value !== 0)) return '';
  currency = currency != null ? currency : '$';
  decimals = decimals != null ? decimals : 2;
  var stringified = Math.abs(value).toFixed(decimals);
  var _int = decimals
    ? stringified.slice(0, -1 - decimals)
    : stringified;
  var i = _int.length % 3;
  var head = i > 0
    ? (_int.slice(0, i) + (_int.length > 3 ? ',' : ''))
    : '';
  var _float = decimals
    ? stringified.slice(-1 - decimals)
    : '';
  var sign = value < 0 ? '-' : '';
  return sign + currency + head +
    _int.slice(i).replace(digitsRE, '$1,') +
    _float;
};
exports.scalesFun = (remainAccount, account) => {
  var scales = parseInt((account - remainAccount) * 100/account);
  if (scales == 100) scales = parseInt(scales);
  return scales;
};
exports.saleTime = (end, start) => {
  var m = new Date(end).getMonth() + 1;
  var d = new Date(end).getDate();
  var h = new Date(end).getHours();
  var min = new Date(end).getMinutes();
  var d2 = new Date(start).getDate();
  var str = '';
  m = m < 10 ? '0' + m : m;
  d = d < 10 ? '0' + d : d;
  h = h < 10 ? '0' + h : h;
  min = min < 10 ? '0' + min : min;
  if (d !== d2) { // 不是当天
    str = m + '月' + d + '日' + h + ':' + min;
  } else {
    str = h + ':' + min + '<br/>即将开售';
  }
  return str;
};
exports.hideBorrowName = (name, len=20) => {
  if (name!=undefined&&name.length > len) {
    return (name.substr(0,len)+'…');
  } else {
    return name;
  }
};
exports.decimals = (value, decimal=2) => {
  value = parseFloat(value);
  if (!isFinite(value) || (!value && value !== 0)) return '';
  return value.toFixed(decimal);
};
exports.cutStr = (str, cut=7) => {
  str = str + ''
  cut = cut + 2 //加两个字符来用'..'填充
  if(str.length > cut) {
    return str.substring(0,cut) + '..'
  }
  return str
};

//将接口传过来的16进制颜色改成rgb格式
exports.colorRgba = (sColor) => {
  let a = sColor.substring(1, 3) // #号的前两位是16进制的透明度
  a = parseInt('0x' + a) // 16进制转10进制，需要加0x
  let transparent = (a / 256).toFixed(2)
  let color = '#' + sColor.substring(3)
  color = color.toLowerCase();
  let reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
  if(color && reg.test(color)){
    if(color.length === 4){ // 判断3位的颜色值
      var sColorNew = "#";
      for(var i=1; i<4; i+=1){
        sColorNew += color.slice(i,i+1).concat(color.slice(i,i+1));
      }
      color = sColorNew;
    }
    //处理六位的颜色值
    var sColorChange = [];
    for(var i=1; i<7; i+=2){
      sColorChange.push(parseInt("0x"+color.slice(i,i+2)));
    }
    // 再最后放入透明度值
    sColorChange.push(transparent)
    return "background: rgba(" + sColorChange.join(",") + ")";
  }else{
    return "background:" + color;
  }
}
