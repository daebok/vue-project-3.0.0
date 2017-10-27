'use strict';

import _ from 'lodash';
// import Timeago from 'timeago.js';

let getCheck = {
    checkEmail(val) {
        var filter = /^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    },
    checkPhone(val) {
        var filter = /^1\d{10}$/;

        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    }
};

/**
 * 从文本中提取出@username 标记的用户名数组
 * @param {String} text 文本内容
 * @return {Array} 用户名数组
 */
const fetchUsers = (text) => {
    if (!text) {
        return [];
    }

    var ignoreRegexs = [
        /```.+?```/g, // 去除单行的 ```
        /^```[\s\S]+?^```/gm, // ``` 里面的是 pre 标签内容
        /`[\s\S]+?`/g, // 同一行中，`some code` 中内容也不该被解析
        /^.*/gm, // 4个空格也是 pre 标签，在这里 . 不会匹配换行
        /\b\S*?@[^\s]*?\..+?\b/g, // somebody@gmail.com 会被去除
        /\[@.+?\\]\(\/.+?\)/g // 已经被 link 的 username
    ];

    ignoreRegexs.forEach((ignoreRegex) => {
        text = text.replace(ignoreRegex, '');
    });

    var results = text.match(/@[a-z0-9\-_]+\b/igm);
    var names = [];
    if (results) {
        for (var i = 0, l = results.length; i < l; i++) {
            var s = results[i];
            // remove leading char @
            s = s.slice(1);
            names.push(s);
        }
    }
    names = _.uniq(names);
    return names;
};

/**
 * 根据文本内容，替换为数据库中的数据
 * @param {String} text 文本内容
 * @param {Function} callback 回调函数
 */
const linkUsers = (text) => {
    var users = fetchUsers(text);
    for (var i = 0, l = users.length; i < l; i++) {
        var name = users[i];
        text = text.replace(new RegExp('@' + name + '\\b(?!\\])', 'g'), '[@' + name + '](/user/' + name + ')');
    }
    return text;
};

/**
 *   对Date的扩展，将 Date 转化为指定格式的String
 *   月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 *   年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 *   例子：
 *   (new Date()).Format('yyyy-MM-dd hh:mm:ss.S') ==> 2006-07-02 08:09:04.423
 *   (new Date()).Format('yyyy-M-d h:m:s.S')      ==> 2006-7-2 8:9:4.18
 */
const fmtDate = (date, fmt) => { // author: meizz
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

/**
 * 调用Timeago库显示到现在的时间
 */
const MillisecondToDate = (time) => {
    // var str = '';
    // if (time !== null && time !== '') {
    //     let timeagoInstance = new Timeago();
    //     str = timeagoInstance.format(time, 'zh_CN');
    // }
    // return str;
};

/**
 * 配置节流函数
 * @param  {[Function]}  fn     [要执行的函数]
 * @param  {[Number]}  delay    [延迟执行的毫秒数]
 * @param  {[Number]}  mustRun  [至少多久执行一次]
 * @return {[Function]}         [节流函数]
 */
exports.throttle = (fn, wait, mustRun) => {
    let timeout;
    let startTime = new Date();
    return function() {
        let context = this;
        let args = arguments;
        let curTime = new Date();

        clearTimeout(timeout);
        // 如果达到了规定的触发时间间隔，触发 handler
        if (curTime - startTime >= mustRun) {
            fn.apply(context, args);
            startTime = curTime;
            // 没达到触发间隔，重新设定定时器
        } else {
            timeout = setTimeout(fn, wait);
        }
    };
};
const digitsRE = /(\d{3})(?=\d)/g
export const currency = (value, currency, decimals) => {
  value = parseFloat(value)
  if (!isFinite(value) || (!value && value !== 0)) return ''
  currency = currency != null ? currency : '$'
  decimals = decimals != null ? decimals : 2
  var stringified = Math.abs(value).toFixed(decimals)
  var _int = decimals
    ? stringified.slice(0, -1 - decimals)
    : stringified
  var i = _int.length % 3
  var head = i > 0
    ? (_int.slice(0, i) + (_int.length > 3 ? ',' : ''))
    : ''
  var _float = decimals
    ? stringified.slice(-1 - decimals)
    : ''
  var sign = value < 0 ? '-' : ''
  return sign + currency + head +
    _int.slice(i).replace(digitsRE, '$1,') +
    _float
}
exports.linkUsers = linkUsers;
exports.fetchUsers = fetchUsers;
exports.getCheck = getCheck;
exports.fmtDate = fmtDate;
// exports.MillisecondToDate = MillisecondToDate;
