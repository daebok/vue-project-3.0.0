webpackJsonp([34],{0:function(e,t,a){"use strict";function n(e){return e&&e.__esModule?e:{"default":e}}function l(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}function r(e,t){if(!e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return!t||"object"!=typeof t&&"function"!=typeof t?e:t}function u(e,t){if("function"!=typeof t&&null!==t)throw new TypeError("Super expression must either be null or a function, not "+typeof t);e.prototype=Object.create(t&&t.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),t&&(Object.setPrototypeOf?Object.setPrototypeOf(e,t):e.__proto__=t)}function i(e){o["default"].error({title:e,okText:"\u5173\u95ed",wrapClassName:"tiperror"})}var c=a(3),o=n(c),s=function(){function e(e,t){for(var a=0;a<t.length;a++){var n=t[a];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(e,n.key,n)}}return function(t,a,n){return a&&e(t.prototype,a),n&&e(t,n),t}}();a(8);var f=a(2),d=n(f),m=a(1),v=n(m);a(106);var g=(a(6),function(e){function t(e){l(this,t);var a=r(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e));return a._getData=a.getData.bind(a),a._getsjData=a.getsjData.bind(a),a._getflData=a.getflData.bind(a),a._tabHander=a.tabHander.bind(a),a._getSearchData=a.getSearchData.bind(a),a.state={data:null,sjdata:null,fldata:null},a}return u(t,e),s(t,[{key:"componentDidMount",value:function(){this._getData(),this._getsjData(),this._getflData(),this._tabHander()}},{key:"tabHander",value:function(){var e=this;$("body").on("click",".tab1 li",function(){if(!$(this).hasClass("active")){$(this).addClass("active").siblings().removeClass("active");var t=$(this).attr("data-val");e._getSearchData(t)}})}},{key:"getSearchData",value:function(e){var t=this;$.ajax({url:"/information/list.html",type:"POST",dataType:"json",data:{time:e}}).done(function(e){e.result?t.setState({data:e}):i(e.msg)}).fail(function(){})}},{key:"getData",value:function(){var e=this,t=e.state.time1;$.ajax({url:"/information/list.html",type:"POST",dataType:"json",data:{time:t}}).done(function(t){e.setState({data:t})}).fail(function(){})}},{key:"getsjData",value:function(){var e=this;$.ajax({url:"/information/list1.html",type:"POST",dataType:"json"}).done(function(t){e.setState({sjdata:t})}).fail(function(){})}},{key:"getflData",value:function(){var e=this;$.ajax({url:"/information/list1.html",type:"POST",dataType:"json"}).done(function(t){e.setState({fldata:t})}).fail(function(){})}},{key:"render",value:function(){var e=this.state.data;if(!e)return!1;var t=this.state.sjdata;if(!t)return!1;this.state.fldata;if(!t)return!1;var a=$(".serverUrl").val();return v["default"].createElement("div",{className:"complianceInfobox"},v["default"].createElement("div",{className:"banner"}),v["default"].createElement("div",{className:"navigation"},v["default"].createElement("ul",null,v["default"].createElement("li",null,v["default"].createElement("a",{href:""},"\u4ece\u4e1a\u7ed3\u6784\u4fe1\u606f")),v["default"].createElement("li",null,v["default"].createElement("a",{href:""},"\u5e73\u53f0\u8fd0\u8425\u6570\u636e")),v["default"].createElement("li",null,v["default"].createElement("a",{href:"",className:"active"},"\u5408\u89c4\u4fe1\u606f")))),v["default"].createElement("div",{className:"con1"},v["default"].createElement("div",{className:"title"},"\u5e74\u5ea6\u62a5\u544a"),v["default"].createElement("ul",{className:"tab1 clearfix"},v["default"].createElement("li",{className:"active","data-val":"2016"},"2016\u5e74"),v["default"].createElement("li",{"data-val":"2015"},"2015\u5e74"),v["default"].createElement("li",{"data-val":"2014"},"2014\u5e74")),v["default"].createElement("div",{className:"listCon1 clearfix"},v["default"].createElement("ul",{className:"clearfix"},e.list.map(function(e,t){var n=a+e.filePath;return v["default"].createElement("li",{key:t},v["default"].createElement("img",{src:n,className:"gray"}),v["default"].createElement("a",{href:""},e.title))})))),v["default"].createElement("div",{className:"con2"},v["default"].createElement("div",{className:"listCon2"},v["default"].createElement("div",{className:"title"},"\u5ba1\u8ba1\u62a5\u544a"),v["default"].createElement("ul",null,t.list.map(function(e,t){return v["default"].createElement("li",{key:t,className:"clearfix"},v["default"].createElement("span",null,e.title),v["default"].createElement("div",{className:"line"}),v["default"].createElement("a",{href:e.url},"\u8be6\u60c5"))})))),v["default"].createElement("div",{className:"con1"},v["default"].createElement("div",{className:"title"},"\u5408\u89c4\u8bc4\u4f30\u62a5\u544a"),v["default"].createElement("ul",{className:"tab1 clearfix"},v["default"].createElement("li",{className:"active","data-val":"2016"},"2016\u5e74"),v["default"].createElement("li",{"data-val":"2015"},"2015\u5e74"),v["default"].createElement("li",{"data-val":"2014"},"2014\u5e74")),v["default"].createElement("div",{className:"listCon1 clearfix"},v["default"].createElement("ul",{className:"clearfix"},e.list.map(function(e,t){var n=a+e.filePath;return v["default"].createElement("li",{key:t},v["default"].createElement("img",{src:n,className:"gray"}),v["default"].createElement("a",{href:""},e.title))})),v["default"].createElement("div",{className:"tips"},"\u6839\u636e\u4e92\u8054\u7f51\u91d1\u878d\u5e73\u53f0\u7684\u59d4\u6258\uff0c\u6307\u6d3e\u4e13\u4e1a\u5f8b\u5e08\u56e2\u961f\u5bf9\u4e92\u8054\u7f51\u91d1\u878d\u5e73\u53f0\u8fdb\u884c\u6cd5\u5f8b\u5c3d\u804c\u8c03\u67e5\u5e76\u6839\u636e\u6cd5\u5f8b\u6cd5\u89c4\u53ca\u653f\u7b56\u7684\u76f8\u5173\u89c4\u5b9a\uff0c\u4e3a\u4e92\u8054\u7f51\u91d1\u878d\u5e73\u53f0 \u63d0\u51fa\u6574\u6539\u610f\u89c1\u6216\u89c4\u8303\u65b9\u6848\uff0c\u5e76\u4e3a\u4e92\u8054\u7f51\u91d1\u878d\u5e73\u53f0\u51fa\u5177\u76f8\u5173\u6cd5\u5f8b\u610f\u89c1\u4e66\u3002"))),v["default"].createElement("div",{className:"con3"},v["default"].createElement("div",{className:"title"},"\u7f51\u8d37\u6cd5\u5f8b\u6cd5\u89c4"),v["default"].createElement("ul",null,t.list.map(function(e,t){return v["default"].createElement("li",{key:t},v["default"].createElement("a",{href:e.url},e.title))}))))}}]),t}(v["default"].Component));d["default"].render(v["default"].createElement(g,null),document.getElementById("complianceInfo"))},6:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a={isnumber:function(e){if(e&&""!=e&&!isNaN(e))return!0},setint:function(e){""!=e.value&&(e.value.search(/^\d+$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},setdec:function(e){""!=e.value&&(e.value.search(/^([1-9]\d*|0)(?:\.\d{0,2})?$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},rechargeCheck:function(e){""!=e.value&&(e.value.search(/^([1-9]\d{0,7}|0)(?:\.\d{0,2})?$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},setnegative:function(e){""!=e.value&&(e.value.search(/^(\-?)(\d*|0)+\.?\d{0,1}$/)==-1?(e.value="",e.value=e.value2?e.value2:""):e.value2=e.value)},getAge:function(e,t){var a=new Date(e),n=a.getFullYear(),l=new Date(t),r=l.getFullYear();return r-n+"\u5c81"},formatDate:function(e,t){if(""==e)return"--";var a=new Date(e),n=a.getFullYear(),l=a.getMonth()+1,r=a.getDate(),u=a.getHours(),i=a.getMinutes(),c=a.getSeconds();return l<10&&(l="0"+l),r<10&&(r="0"+r),2==t?(c<10&&(c="0"+c),i<10&&(i="0"+i),n+"-"+l+"-"+r+" "+u+":"+i+":"+c):3==t?(c<10&&(c="0"+c),i<10&&(i="0"+i),n+"-"+l+"-"+r+" "+u+":"+i):4==t?n+"\u5e74"+l+"\u6708"+r+"\u65e5":n+"-"+l+"-"+r},formatDateInvest:function(e){if(""==e)return"--";var t=new Date(e),a=t.getFullYear(),n=t.getMonth()+1,l=t.getDate(),r=t.getHours(),u=t.getMinutes(),i=t.getSeconds(),c=new Date,o=c.getFullYear(),s=c.getMonth()+1,f=c.getDate(),d=new Date;d.setDate(d.getDate()+1);var m=d.getFullYear(),v=d.getMonth()+1,g=d.getDate();return n<10&&(n="0"+n,s="0"+s,v="0"+v),l<10&&(l="0"+l,f="0"+f,g="0"+g),i<10&&(i="0"+i),u<10&&(u="0"+u),a==o&&n==s&&l==f?"\u4eca\u65e5 "+r+":"+u+":"+i:a==m&&n==v&&l==g?"\u660e\u65e5 "+r+":"+u+":"+i:n+"-"+l+" "+r+":"+u+":"+i},formatDatetype:function(e,t){var a=new Date(e),n=(a.getFullYear(),a.getMonth()+1),l=a.getDate(),r=a.getHours(),u=a.getMinutes(),i=a.getSeconds();return n<10&&(n="0"+n),l<10&&(l="0"+l),2==t?(i<10&&(i="0"+i),u<10&&(u="0"+u),n+"\u6708"+l+"\u65e5 "+r+":"+u+"\u5f00\u552e"):3==t?(i<10&&(i="0"+i),u<10&&(u="0"+u),r+"<em>\u65f6</em>"+u+"<em>\u5206</em>"+i+"<em>\u79d2</em>"):n+"-"+l},formatDatetype1:function(e,t){var a=t-e,n=Math.ceil(a/1e3/24/60/60),l=Math.ceil(a/1e3/60/60),r=Math.ceil(a/1e3/60),u=Math.ceil(a/1e3);return n>1?n+"\u5929\u524d":l>1?l+"\u5c0f\u65f6\u524d":r>1?r+"\u5206\u949f\u524d":u>1?u+"\u79d2\u524d":void 0},formatDatetype2:function(e,t){var a=t-e,n=Math.ceil(a/1e3/24/60/60),l=Math.ceil(a/1e3/60/60),r=Math.ceil(a/1e3/60),u=Math.ceil(a/1e3);return n>1?n+"\u5929":l>1?l+"\u5c0f\u65f6":r>1?r+"\u5206\u949f":u>1?u+"\u79d2":void 0},formatDatetype3:function(e){var t=Math.floor(e/864e5),a=e%864e5,n=Math.floor(a/36e5),l=a%36e5,r=Math.floor(l/6e4),u=l%6e4,i=Math.round(u/1e3);return t>0?t+"<em>\u5929</em>"+n+"<em>\u65f6</em>"+r+"<em>\u5206</em>"+i+"<em>\u79d2</em>":n+"<em>\u65f6</em>"+r+"<em>\u5206</em>"+i+"<em>\u79d2</em>"},formatDatetype4:function(e){var t=Math.floor(e/864e5),a=e%864e5,n=Math.floor(a/36e5),l=a%36e5,r=Math.floor(l/6e4),u=l%6e4,i=Math.round(u/1e3);return t>0?t+"\u5929"+n+"\u65f6"+r+"\u5206"+i+"\u79d2":n+"\u65f6"+r+"\u5206"+i+"\u79d2"},moneyFormat:function(e){var t=e;if(0==t)return t+".00";var a=2;t=parseFloat((t+"").replace(/[^\d\.-]/g,"")).toFixed(a)+"";for(var n=t.split(".")[0].split("").reverse(),l=t.split(".")[1],r="",u=0;u<n.length;u++)r+=n[u]+((u+1)%3==0&&u+1!=n.length?",":"");return r.split("").reverse().join("")+"."+l},numberFormat:function(e){var t=e;if(0==t)return t;var a=2;t=parseFloat((t+"").replace(/[^\d\.-]/g,"")).toFixed(a)+"";for(var n=t.split(".")[0].split("").reverse(),l=(t.split(".")[1],""),r=0;r<n.length;r++)l+=n[r]+((r+1)%3==0&&r+1!=n.length?",":"");return l.split("").reverse().join("")},subNameFun:function(e,t,a){var n=e.replace(/<[^>].*?>/g,""),l=n.replace(/&nbsp;/gi,""),r=l;return e.length>a&&(r=""+l.substring(t,a)+"..."),r},getQueryString:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)","i"),a=window.location.search.substr(1).match(t);return null!=a?unescape(a[2]):""},getSex:function(e){return"M"==e?"\u7537":"\u5973"},getEducation:function(e){return["\u5c0f\u5b66","\u521d\u4e2d","\u9ad8\u4e2d","\u4e2d\u4e13","\u5927\u4e13","\u672c\u79d1","\u7855\u58eb","\u535a\u58eb","\u5176\u4ed6"][parseInt(e)-1]},getWorkExperience:function(e){return["0-3\u5e74","3-5\u5e74","5-8\u5e74","8\u5e74\u4ee5\u4e0a"][parseInt(e)-1]},getMonthIncomeRange:function(e){return["3000\u5143\u4ee5\u4e0b","3000-5000\u5143","5000-8000\u5143","8000-10000\u5143","10000-130000\u5143","13000-15000\u5143","15000-20000\u5143","20000\u5143\u4ee5\u4e0a"][parseInt(e)-1]},getMaritalStatus:function(e){return["\u672a\u5a5a","\u5df2\u5a5a","\u79bb\u5f02","\u4e27\u5076"][parseInt(e)]},getCarStatus:function(e){return void 0!==e&&(0===parseInt(e)?"\u65e0":"\u6709")},getHouseStatus:function(e){return void 0!==e&&(0===parseInt(e)?"\u65e0":"\u6709")},getInvestType:function(e){return["\u7f51\u7ad9\u6295\u8d44","\u81ea\u52a8\u6295\u8d44","\u624b\u673a\u6295\u8d44"][parseInt(e)]},borrowNature:function(e){return["\u4e2a\u4eba\u501f\u6b3e","\u4f01\u4e1a\u501f\u6b3e"][parseInt(e)-1]},repayStyle:function(e){return["\u6708\u7b49\u989d\u672c\u606f","\u4e00\u6b21\u6027\u8fd8\u6b3e","\u6bcf\u6708\u8fd8\u606f\u5230\u671f\u8fd8\u672c","\u7b49\u989d\u672c\u91d1","\u6bcf\u5b63\u8fd8\u606f\u5230\u671f\u8fd8\u672c"][parseInt(e)-1]},vouchStatus:function(e){return["\u5f85\u5ba1\u6838","\u540c\u610f\u62c5\u4fdd","\u62d2\u7edd\u62c5\u4fdd"][parseInt(e)]},GetQueryString:function(e){var t=new RegExp("(^|&)"+e+"=([^&]*)(&|$)"),a=window.location.search.substr(1).match(t);return null!=a?unescape(a[2]):"1"}};t.Publiclib=a},106:534});