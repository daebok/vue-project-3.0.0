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
    str = '　' + h + ':' + min + '　即将开售';
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
