webpackJsonp([28],{0:function(e,t,a){"use strict";function r(e){return e&&e.__esModule?e:{"default":e}}function n(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function l(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}function i(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}Object.defineProperty(t,"__esModule",{value:!0}),t["default"]=void 0;var u=a(14),o=r(u),s=a(3),c=(r(s),a(10)),m=r(c),f="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},d=function(){function e(e,t){for(var a=0;a<t.length;a++){var r=t[a];r.enumerable=r.enumerable||!1,r.configurable=!0,"value"in r&&(r.writable=!0),Object.defineProperty(e,r.key,r)}}return function(t,a,r){return a&&e(t.prototype,a),r&&e(t,r),t}}();a(8);var p=a(2),v=r(p),g=a(1),h=r(g),b=(a(5),a(6));a(567);var E=(m["default"].create,m["default"].Item,function(e){function t(e){n(this,t);var a=l(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));if(a._getData=a.getData.bind(a),a.jump=a.jump.bind(a),"function"!=typeof a.jump)throw new TypeError('Value of "this.jump" violates contract, expected () => any got '+(null===a.jump?"null":"object"===f(a.jump)&&a.jump.constructor?a.jump.constructor.name||"[Unknown Object]":f(a.jump)));if(a.jumpvirtual=a.jumpvirtual.bind(a),"function"!=typeof a.jumpvirtual)throw new TypeError('Value of "this.jumpvirtual" violates contract, expected () => any got '+(null===a.jumpvirtual?"null":"object"===f(a.jumpvirtual)&&a.jumpvirtual.constructor?a.jumpvirtual.constructor.name||"[Unknown Object]":f(a.jumpvirtual)));return a.state={data:null},a}return i(t,e),d(t,[{key:"componentDidMount",value:function(){this._getData()}},{key:"getData",value:function(){var e=this;$.ajax({url:"/scoreshop/getGoodsInfo.html",type:"POST",dataType:"json",data:{id:$(".id").val()}}).done(function(t){e.setState({data:t});var a=e.state.data.exchangeLimit,r=e.state.data.score,n=$(".useScore").val(),l=e.state.data.lessNum,i=Math.floor(n/r),u=$(".exchangedOrders").val(),o=a-u,s=0,c=n-r,m=n.toString().replace(/(\d)(?=(?:\d{3})+$)/g,"$1,");$(".residueScore").find("em").html(m),o>0?($(".submit-btn").attr("disabled",!1),$(".error-tips").find("span").html(""),$(".error-tips").hide(),$(".nums").keyup(function(){var e=$(this).val(),t=e.replace(/[^\d]/g,"").replace(/(\d{4})(?=\d)/g,"$1 ");$(this).val(t),$(this).val()>a?($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u4e0d\u80fd\u8d85\u8fc7\u6bcf\u4eba\u9650\u5151\u4e2a\u6570"),$(".error-tips").show()):$(this).val()>o?($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u5151\u6362\u6570\u91cf\u4e0d\u80fd\u8d85\u8fc7\u4e2a\u4eba\u9650\u5151\u6570\u91cf"),$(".error-tips").show()):$(this).val()>i?($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u79ef\u5206\u4e0d\u8db3"),$(".error-tips").show()):$(this).val()>l?($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u4e0d\u80fd\u8d85\u8fc7\u5269\u4f59\u6570\u91cf"),$(".error-tips").show()):($(".submit-btn").attr("disabled",!1),$(".error-tips").find("span").html(""),$(".error-tips").hide())}),c<0?($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u79ef\u5206\u4e0d\u8db3"),$(".error-tips").show()):($(".submit-btn").attr("disabled",!1),$(".error-tips").find("span").html(""),$(".error-tips").hide()),$(".add").click(function(){s<o&&s<l&&s<a&&s<i&&(s+=1,$(".nums").val(s))}),$(".down").click(function(){s>0&&(s-=1,$(".nums").val(s))})):($(".submit-btn").attr("disabled",!0),$(".error-tips").find("span").html("\u5151\u6362\u6570\u91cf\u4e0d\u80fd\u8d85\u8fc7\u6bcf\u4eba\u9650\u5151\u6570\u91cf"),$(".error-tips").show())}).fail(function(){})}},{key:"jump",value:function(){0!=$(".nums").val()&&(window.location.href="http://"+window.location.host+"/scoreshop/order.html?id="+$(".id").val()+"&num="+$(".nums").val())}},{key:"jumpvirtual",value:function(){0!=$(".nums").val()&&(window.location.href="http://"+window.location.host+"/scoreshop/virtualOrder.html?id="+$(".id").val()+"&num="+$(".nums").val())}},{key:"check",value:function(e){var t=e.target;b.Publiclib.setint(t)}},{key:"render",value:function(){var e=this.state.data;if(!e)return!1;var t=e.exchangeLimit,a=$(".exchangedOrders").val(),r=t-a,n=e.isVirtual,l=$(".serverUrl").val(),i=l+e.picSmall,u="0"==n?h["default"].createElement("a",{className:"submit-btn",onClick:this.jump},"\u7acb\u5373\u5151\u6362"):h["default"].createElement("a",{className:"submit-btn",onClick:this.jumpvirtual},"\u7acb\u5373\u5151\u6362"),s=h["default"].createElement("a",{className:"nobtn"},"\u5df2\u5151\u5b8c"),c=e.lessNum>0?u:s,m=e.score.toString().replace(/(\d)(?=(?:\d{3})+$)/g,"$1,"),f=r==t?h["default"].createElement("em",null,"\uff08\u6bcf\u4eba\u9650\u5151",e.exchangeLimit,"\u4e2a\uff09"):h["default"].createElement("em",null,"\uff08\u6bcf\u4eba\u9650\u5151",e.exchangeLimit,"\u4e2a,\u60a8\u8fd8\u53ef\u4ee5\u5151\u6362",r,"\u4e2a\uff09");return h["default"].createElement("div",null,h["default"].createElement("div",{className:"buyGoods clearfix"},h["default"].createElement("div",{className:"left"},h["default"].createElement("img",{src:i})),h["default"].createElement("div",{className:"right"},h["default"].createElement("div",{className:"ptitle"},h["default"].createElement("span",null,e.goodsName),h["default"].createElement("p",null,h["default"].createElement("em",{className:"iconfont icontips"},"\ue685"),h["default"].createElement("em",null,e.goodsCategoryName),h["default"].createElement("em",{className:"iconfont icontips"},"\ue684"),h["default"].createElement("em",null,e.remark))),h["default"].createElement("ul",{className:"datadetail clearfix"},h["default"].createElement("li",{className:"score"},m,h["default"].createElement("p",null,"\u6240\u9700\u79ef\u5206")),h["default"].createElement("li",{className:"price clearfix"},h["default"].createElement("em",null,"\uffe5",e.marketPrice),h["default"].createElement("p",null,"\u5e02\u573a\u4ef7\u683c")),h["default"].createElement("li",{className:"residue"},e.lessNum,h["default"].createElement("p",null,"\u5269\u4f59\u6570\u91cf"))),h["default"].createElement("div",{className:"exchangeNum clearfix"},h["default"].createElement("div",{className:"copies clearfix"},h["default"].createElement("span",{className:"down"},"-"),h["default"].createElement(o["default"],{type:"text",className:"nums",maxLength:"100",autoComplete:"off",defaultValue:"0",onKeyUp:this.check}),h["default"].createElement("span",{className:"add"},"+"),f),h["default"].createElement("div",{className:"residueScore"},"\u53ef\u7528\u79ef\u5206\uff1a",h["default"].createElement("em",null,"50000"))),c,h["default"].createElement("span",{className:"error-tips"},h["default"].createElement("em",{className:"iconfont"},"\ue62e"),"\u79ef\u5206\u4e0d\u8db3"))),h["default"].createElement("div",{className:"goodsDetails"},h["default"].createElement("div",{className:"title"},"\u5546\u54c1\u8be6\u60c5"),h["default"].createElement("div",{className:"con"},e.content)))}}]),t}(h["default"].Component));t["default"]=E,v["default"].render(h["default"].createElement(E,null),document.getElementById("goodsdetail")),e.exports=t["default"]},5:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.ValidateFn=void 0;var r=a(7),n={regexRealName:function(e,t,a){t?r.Reg.regexRealName(t)?a():a([new Error("\u7528\u6237\u540d\u683c\u5f0f\u4e0d\u6b63\u786e")]):a()},regexChnName:function(e,t,a){t?r.Reg.regexChnName(t)?a():a([new Error("\u4ec5\u4e3a\u4e2d\u6587,\u4e14\u4e0d\u5305\u542b\u7a7a\u683c")]):a()},regexUserName:function(e,t,a){t&&r.Reg.regexUserName(t)?a([new Error("6-20\u4f4d\u5b57\u7b26\uff0c\u7531\u5b57\u6bcd\u6216\u5b57\u6bcd\u52a0\u6570\u5b57\u7ec4\u6210")]):a()},regexUserNameAjax:function(e,t,a){if(t)if(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}|[a-zA-Z]{6,20}$/.test(t)){var r=this;if(this.state.regexUserNameflag==t)return void a();$.ajax({url:"/user/checkUserName.html",type:"POST",dataType:"json",data:{userName:t}}).done(function(e){e.result?(r.state.regexUserNameflag=t,a()):a([new Error(e.msg)])}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a([new Error("6-20\u4f4d\u5b57\u7b26\uff0c\u7531\u5b57\u6bcd\u6216\u5b57\u6bcd\u52a0\u6570\u5b57\u7ec4\u6210")]);else a()},regexIdCard:function(e,t,a){t?r.Reg.regexIdCard(t)?a():a([new Error("\u8eab\u4efd\u8bc1\u683c\u5f0f\u4e0d\u6b63\u786e,\u4ec5\u9650\u6570\u5b57\u548c\u5927\u5199X")]):a()},regexPW:function(e,t,a){t&&r.Reg.regexPassword(t)?a([new Error("8-16\u4f4d\u5b57\u7b26\uff0c\u5176\u4e2d\u5305\u62ec\u81f3\u5c11\u4e00\u4e2a\u5b57\u6bcd\u548c\u4e00\u4e2a\u6570\u5b57")]):a()},isMobile:function(e,t,a){t&&r.Reg.isMobile(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801")]):a()},isNumber:function(e,t,a){t&&r.Reg.isNumber(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u6574\u6570")]):a()},isApr:function(e,t,a){t&&r.Reg.isApr(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u6570\u5b57")]):a()},decimal:function(e,t,a){t&&r.Reg.isFigure(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u6570\u5b57")]):a()},isFigure:function(e,t,a){t&&(r.Reg.isFigure(t)||15!=t.length)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u516515\u4f4d\u6570\u5b57")]):a()},isEmail:function(e,t,a){t&&r.Reg.isEmail(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u90ae\u7bb1")]):a()},isFixNumber:function(e,t,a){t&&(r.Reg.isFixNumber(t)||r.Reg.isFigure(t)||t.length>12||r.Reg.isPhone(t))?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u5ea7\u673a\u53f7\u7801")]):a()},isloginName:function(e,t,a){t&&r.Reg.islogname(t)?a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801\u6216\u90ae\u7bb1")]):a()},checkMobile:function(e,t,a){if(t)if(r.Reg.isMobile(t))a([new Error("\u62b1\u6b49\uff0c\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u624b\u673a\u53f7\u7801")]);else{var n=this;if(this.state.mobileflag==t)return void a();$.ajax({url:"/user/checkMobile.html",type:"POST",dataType:"json",data:{mobile:t}}).done(function(e){e.result?(n.setState({mobileflag:t}),a()):a([new Error(e.msg)])}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a()},checkValidCode:function(e,t,a){if(t){var r=this;if(this.state.ValidCodeflag==t)return void a();$.ajax({url:"/valicode.html",type:"POST",dataType:"json",data:{validCode:t}}).done(function(e){e.result?(r.setState({ValidCodeflag:t}),a()):(a([new Error(e.msg)]),$(".codeimg")[0].click())}).fail(function(){a([new Error("\u7f51\u7edc\u5f02\u5e38\uff0c\u8bf7\u91cd\u8bd5")])})}else a()}};t.ValidateFn=n},6:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={isnumber:function(e){if(e&&""!=e&&!isNaN(e))return!0},setint:function(e){""!=e.value&&(e.value.search(/^\d+$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},setdec:function(e){""!=e.value&&(e.value.search(/^([1-9]\d*|0)(?:\.\d{0,2})?$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},rechargeCheck:function(e){""!=e.value&&(e.value.search(/^([1-9]\d{0,7}|0)(?:\.\d{0,2})?$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},setnegative:function(e){""!=e.value&&(e.value.search(/^(\-?)(\d*|0)+\.?\d{0,1}$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},getAge:function(e,t){var a=new Date(e),r=a.getFullYear(),n=new Date(t),l=n.getFullYear();return l-r+"\u5c81"},formatDate:function(e,t){if(""==e)return"--";var a=new Date(e),r=a.getFullYear(),n=a.getMonth()+1,l=a.getDate(),i=a.getHours(),u=a.getMinutes(),o=a.getSeconds();return n<10&&(n="0"+n),l<10&&(l="0"+l),2==t?(o<10&&(o="0"+o),u<10&&(u="0"+u),r+"-"+n+"-"+l+" "+i+":"+u+":"+o):3==t?(o<10&&(o="0"+o),u<10&&(u="0"+u),r+"-"+n+"-"+l+" "+i+":"+u):4==t?r+"\u5e74"+n+"\u6708"+l+"\u65e5":r+"-"+n+"-"+l},formatDateInvest:function(e){if(""==e)return"--";var t=new Date(e),a=t.getFullYear(),r=t.getMonth()+1,n=t.getDate(),l=t.getHours(),i=t.getMinutes(),u=t.getSeconds(),o=new Date,s=o.getFullYear(),c=o.getMonth()+1,m=o.getDate(),f=new Date;f.setDate(f.getDate()+1);var d=f.getFullYear(),p=f.getMonth()+1,v=f.getDate();return r<10&&(r="0"+r,c="0"+c,p="0"+p),n<10&&(n="0"+n,m="0"+m,v="0"+v),u<10&&(u="0"+u),i<10&&(i="0"+i),a==s&&r==c&&n==m?"\u4eca\u65e5 "+l+":"+i+":"+u:a==d&&r==p&&n==v?"\u660e\u65e5 "+l+":"+i+":"+u:r+"-"+n+" "+l+":"+i+":"+u},formatDatetype:function(e,t){var a=new Date(e),r=(a.getFullYear(),a.getMonth()+1),n=a.getDate(),l=a.getHours(),i=a.getMinutes(),u=a.getSeconds();return r<10&&(r="0"+r),n<10&&(n="0"+n),2==t?(u<10&&(u="0"+u),i<10&&(i="0"+i),r+"\u6708"+n+"\u65e5 "+l+":"+i+"\u5f00\u552e"):3==t?(u<10&&(u="0"+u),i<10&&(i="0"+i),l+"<em>\u65f6</em>"+i+"<em>\u5206</em>"+u+"<em>\u79d2</em>"):r+"-"+n},formatDatetype1:function(e,t){var a=t-e,r=Math.ceil(a/1e3/24/60/60),n=Math.ceil(a/1e3/60/60),l=Math.ceil(a/1e3/60),i=Math.ceil(a/1e3);return r>1?r+"\u5929\u524d":n>1?n+"\u5c0f\u65f6\u524d":l>1?l+"\u5206\u949f\u524d":i>1?i+"\u79d2\u524d":void 0},formatDatetype2:function(e,t){var a=t-e,r=Math.ceil(a/1e3/24/60/60),n=Math.ceil(a/1e3/60/60),l=Math.ceil(a/1e3/60),i=Math.ceil(a/1e3);return r>1?r+"\u5929":n>1?n+"\u5c0f\u65f6":l>1?l+"\u5206\u949f":i>1?i+"\u79d2":void 0},formatDatetype3:function(e){var t=Math.floor(e/864e5),a=e%864e5,r=Math.floor(a/36e5),n=a%36e5,l=Math.floor(n/6e4),i=n%6e4,u=Math.round(i/1e3);return t>0?t+"<em>\u5929</em>"+r+"<em>\u65f6</em>"+l+"<em>\u5206</em>"+u+"<em>\u79d2</em>":r+"<em>\u65f6</em>"+l+"<em>\u5206</em>"+u+"<em>\u79d2</em>"},formatDatetype4:function(e){var t=Math.floor(e/864e5),a=e%864e5,r=Math.floor(a/36e5),n=a%36e5,l=Math.floor(n/6e4),i=n%6e4,u=Math.round(i/1e3);return t>0?t+"\u5929"+r+"\u65f6"+l+"\u5206"+u+"\u79d2":r+"\u65f6"+l+"\u5206"+u+"\u79d2"},moneyFormat:function(e){var t=e;if(0==t)return t+".00";var a=2;t=parseFloat((t+"").replace(/[^\d\.-]/g,"")).toFixed(a)+"";for(var r=t.split(".")[0].split("").reverse(),n=t.split(".")[1],l="",i=0;i<r.length;i++)l+=r[i]+((i+1)%3==0&&i+1!=r.length?",":"");return l.split("").reverse().join("")+"."+n},numberFormat:function(e){var t=e;if(0==t)return t;var a=2;t=parseFloat((t+"").replace(/[^\d\.-]/g,"")).toFixed(a)+"";for(var r=t.split(".")[0].split("").reverse(),n=(t.split(".")[1],""),l=0;l<r.length;l++)n+=r[l]+((l+1)%3==0&&l+1!=r.length?",":"");return n.split("").reverse().join("")},subNameFun:function(e,t,a){var r=e.replace(/<[^>].*?>/g,""),n=r.replace(/&nbsp;/gi,""),l=n;return e.length>a&&(l=""+n.substring(t,a)+"..."),l},getQueryString:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)","i"),a=window.location.search.substr(1).match(t);return null!=a?unescape(a[2]):""},getSex:function(e){return"M"==e?"\u7537":"\u5973"},getEducation:function(e){return["\u5c0f\u5b66","\u521d\u4e2d","\u9ad8\u4e2d","\u4e2d\u4e13","\u5927\u4e13","\u672c\u79d1","\u7855\u58eb","\u535a\u58eb","\u5176\u4ed6"][parseInt(e)-1]},getWorkExperience:function(e){return["0-3\u5e74","3-5\u5e74","5-8\u5e74","8\u5e74\u4ee5\u4e0a"][parseInt(e)-1]},getMonthIncomeRange:function(e){return["3000\u5143\u4ee5\u4e0b","3000-5000\u5143","5000-8000\u5143","8000-10000\u5143","10000-130000\u5143","13000-15000\u5143","15000-20000\u5143","20000\u5143\u4ee5\u4e0a"][parseInt(e)-1]},getMaritalStatus:function(e){return["\u672a\u5a5a","\u5df2\u5a5a","\u79bb\u5f02","\u4e27\u5076"][parseInt(e)]},getCarStatus:function(e){return void 0!==e&&(0===parseInt(e)?"\u65e0":"\u6709")},getHouseStatus:function(e){return void 0!==e&&(0===parseInt(e)?"\u65e0":"\u6709")},getInvestType:function(e){return["\u7f51\u7ad9\u6295\u8d44","\u81ea\u52a8\u6295\u8d44","\u624b\u673a\u6295\u8d44"][parseInt(e)]},borrowNature:function(e){return["\u4e2a\u4eba\u501f\u6b3e","\u4f01\u4e1a\u501f\u6b3e"][parseInt(e)-1]},repayStyle:function(e){return["\u6708\u7b49\u989d\u672c\u606f","\u4e00\u6b21\u6027\u8fd8\u6b3e","\u6bcf\u6708\u8fd8\u606f\u5230\u671f\u8fd8\u672c","\u7b49\u989d\u672c\u91d1","\u6bcf\u5b63\u8fd8\u606f\u5230\u671f\u8fd8\u672c"][parseInt(e)-1]},vouchStatus:function(e){return["\u5f85\u5ba1\u6838","\u540c\u610f\u62c5\u4fdd","\u62d2\u7edd\u62c5\u4fdd"][parseInt(e)]},GetQueryString:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)"),a=window.location.search.substr(1).match(t);return null!=a?unescape(a[2]):"1"}};t.Publiclib=a},567:534});