webpackJsonp([1],{250:function(e,t,r){"use strict";var i=String.prototype.replace,a=/%20/g;e.exports={default:"RFC3986",formatters:{RFC1738:function(e){return i.call(e,a,"+")},RFC3986:function(e){return e}},RFC1738:"RFC1738",RFC3986:"RFC3986"}},251:function(e,t,r){"use strict";var i=Object.prototype.hasOwnProperty,a=function(){for(var e=[],t=0;t<256;++t)e.push("%"+((t<16?"0":"")+t.toString(16)).toUpperCase());return e}();t.arrayToObject=function(e,t){for(var r=t&&t.plainObjects?Object.create(null):{},i=0;i<e.length;++i)void 0!==e[i]&&(r[i]=e[i]);return r},t.merge=function(e,r,a){if(!r)return e;if("object"!=typeof r){if(Array.isArray(e))e.push(r);else{if("object"!=typeof e)return[e,r];(a.plainObjects||a.allowPrototypes||!i.call(Object.prototype,r))&&(e[r]=!0)}return e}if("object"!=typeof e)return[e].concat(r);var n=e;return Array.isArray(e)&&!Array.isArray(r)&&(n=t.arrayToObject(e,a)),Array.isArray(e)&&Array.isArray(r)?(r.forEach(function(r,n){i.call(e,n)?e[n]&&"object"==typeof e[n]?e[n]=t.merge(e[n],r,a):e.push(r):e[n]=r}),e):Object.keys(r).reduce(function(e,n){var o=r[n];return i.call(e,n)?e[n]=t.merge(e[n],o,a):e[n]=o,e},n)},t.assign=function(e,t){return Object.keys(t).reduce(function(e,r){return e[r]=t[r],e},e)},t.decode=function(e){try{return decodeURIComponent(e.replace(/\+/g," "))}catch(t){return e}},t.encode=function(e){if(0===e.length)return e;for(var t="string"==typeof e?e:String(e),r="",i=0;i<t.length;++i){var n=t.charCodeAt(i);45===n||46===n||95===n||126===n||n>=48&&n<=57||n>=65&&n<=90||n>=97&&n<=122?r+=t.charAt(i):n<128?r+=a[n]:n<2048?r+=a[192|n>>6]+a[128|63&n]:n<55296||n>=57344?r+=a[224|n>>12]+a[128|n>>6&63]+a[128|63&n]:(i+=1,n=65536+((1023&n)<<10|1023&t.charCodeAt(i)),r+=a[240|n>>18]+a[128|n>>12&63]+a[128|n>>6&63]+a[128|63&n])}return r},t.compact=function(e,r){if("object"!=typeof e||null===e)return e;var i=r||[],a=i.indexOf(e);if(-1!==a)return i[a];if(i.push(e),Array.isArray(e)){for(var n=[],o=0;o<e.length;++o)e[o]&&"object"==typeof e[o]?n.push(t.compact(e[o],i)):void 0!==e[o]&&n.push(e[o]);return n}return Object.keys(e).forEach(function(r){e[r]=t.compact(e[r],i)}),e},t.isRegExp=function(e){return"[object RegExp]"===Object.prototype.toString.call(e)},t.isBuffer=function(e){return null!==e&&void 0!==e&&!!(e.constructor&&e.constructor.isBuffer&&e.constructor.isBuffer(e))}},252:function(e,t,r){"use strict";var i=r(254),a=r(253),n=r(250);e.exports={formats:n,parse:a,stringify:i}},253:function(e,t,r){"use strict";var i=r(251),a=Object.prototype.hasOwnProperty,n={allowDots:!1,allowPrototypes:!1,arrayLimit:20,decoder:i.decode,delimiter:"&",depth:5,parameterLimit:1e3,plainObjects:!1,strictNullHandling:!1},o=function(e,t){for(var r={},i=t.ignoreQueryPrefix?e.replace(/^\?/,""):e,o=t.parameterLimit===1/0?void 0:t.parameterLimit,s=i.split(t.delimiter,o),l=0;l<s.length;++l){var c,u,d=s[l],f=d.indexOf("]="),p=-1===f?d.indexOf("="):f+1;-1===p?(c=t.decoder(d,n.decoder),u=t.strictNullHandling?null:""):(c=t.decoder(d.slice(0,p),n.decoder),u=t.decoder(d.slice(p+1),n.decoder)),a.call(r,c)?r[c]=[].concat(r[c]).concat(u):r[c]=u}return r},s=function(e,t,r){if(!e.length)return t;var i,a=e.shift();if("[]"===a)i=[],i=i.concat(s(e,t,r));else{i=r.plainObjects?Object.create(null):{};var n="["===a.charAt(0)&&"]"===a.charAt(a.length-1)?a.slice(1,-1):a,o=parseInt(n,10);!isNaN(o)&&a!==n&&String(o)===n&&o>=0&&r.parseArrays&&o<=r.arrayLimit?(i=[],i[o]=s(e,t,r)):i[n]=s(e,t,r)}return i},l=function(e,t,r){if(e){var i=r.allowDots?e.replace(/\.([^.[]+)/g,"[$1]"):e,n=/(\[[^[\]]*])/,o=/(\[[^[\]]*])/g,l=n.exec(i),c=l?i.slice(0,l.index):i,u=[];if(c){if(!r.plainObjects&&a.call(Object.prototype,c)&&!r.allowPrototypes)return;u.push(c)}for(var d=0;null!==(l=o.exec(i))&&d<r.depth;){if(d+=1,!r.plainObjects&&a.call(Object.prototype,l[1].slice(1,-1))&&!r.allowPrototypes)return;u.push(l[1])}return l&&u.push("["+i.slice(l.index)+"]"),s(u,t,r)}};e.exports=function(e,t){var r=t?i.assign({},t):{};if(null!==r.decoder&&void 0!==r.decoder&&"function"!=typeof r.decoder)throw new TypeError("Decoder has to be a function.");if(r.ignoreQueryPrefix=!0===r.ignoreQueryPrefix,r.delimiter="string"==typeof r.delimiter||i.isRegExp(r.delimiter)?r.delimiter:n.delimiter,r.depth="number"==typeof r.depth?r.depth:n.depth,r.arrayLimit="number"==typeof r.arrayLimit?r.arrayLimit:n.arrayLimit,r.parseArrays=!1!==r.parseArrays,r.decoder="function"==typeof r.decoder?r.decoder:n.decoder,r.allowDots="boolean"==typeof r.allowDots?r.allowDots:n.allowDots,r.plainObjects="boolean"==typeof r.plainObjects?r.plainObjects:n.plainObjects,r.allowPrototypes="boolean"==typeof r.allowPrototypes?r.allowPrototypes:n.allowPrototypes,r.parameterLimit="number"==typeof r.parameterLimit?r.parameterLimit:n.parameterLimit,r.strictNullHandling="boolean"==typeof r.strictNullHandling?r.strictNullHandling:n.strictNullHandling,""===e||null===e||void 0===e)return r.plainObjects?Object.create(null):{};for(var a="string"==typeof e?o(e,r):e,s=r.plainObjects?Object.create(null):{},c=Object.keys(a),u=0;u<c.length;++u){var d=c[u],f=l(d,a[d],r);s=i.merge(s,f,r)}return i.compact(s)}},254:function(e,t,r){"use strict";var i=r(251),a=r(250),n={brackets:function(e){return e+"[]"},indices:function(e,t){return e+"["+t+"]"},repeat:function(e){return e}},o=Date.prototype.toISOString,s={delimiter:"&",encode:!0,encoder:i.encode,encodeValuesOnly:!1,serializeDate:function(e){return o.call(e)},skipNulls:!1,strictNullHandling:!1},l=function e(t,r,a,n,o,l,c,u,d,f,p,b){var m=t;if("function"==typeof c)m=c(r,m);else if(m instanceof Date)m=f(m);else if(null===m){if(n)return l&&!b?l(r,s.encoder):r;m=""}if("string"==typeof m||"number"==typeof m||"boolean"==typeof m||i.isBuffer(m)){if(l){return[p(b?r:l(r,s.encoder))+"="+p(l(m,s.encoder))]}return[p(r)+"="+p(String(m))]}var y=[];if(void 0===m)return y;var h;if(Array.isArray(c))h=c;else{var v=Object.keys(m);h=u?v.sort(u):v}for(var g=0;g<h.length;++g){var O=h[g];o&&null===m[O]||(y=Array.isArray(m)?y.concat(e(m[O],a(r,O),a,n,o,l,c,u,d,f,p,b)):y.concat(e(m[O],r+(d?"."+O:"["+O+"]"),a,n,o,l,c,u,d,f,p,b)))}return y};e.exports=function(e,t){var r=e,o=t?i.assign({},t):{};if(null!==o.encoder&&void 0!==o.encoder&&"function"!=typeof o.encoder)throw new TypeError("Encoder has to be a function.");var c=void 0===o.delimiter?s.delimiter:o.delimiter,u="boolean"==typeof o.strictNullHandling?o.strictNullHandling:s.strictNullHandling,d="boolean"==typeof o.skipNulls?o.skipNulls:s.skipNulls,f="boolean"==typeof o.encode?o.encode:s.encode,p="function"==typeof o.encoder?o.encoder:s.encoder,b="function"==typeof o.sort?o.sort:null,m=void 0!==o.allowDots&&o.allowDots,y="function"==typeof o.serializeDate?o.serializeDate:s.serializeDate,h="boolean"==typeof o.encodeValuesOnly?o.encodeValuesOnly:s.encodeValuesOnly;if(void 0===o.format)o.format=a.default;else if(!Object.prototype.hasOwnProperty.call(a.formatters,o.format))throw new TypeError("Unknown format option provided.");var v,g,O=a.formatters[o.format];"function"==typeof o.filter?(g=o.filter,r=g("",r)):Array.isArray(o.filter)&&(g=o.filter,v=g);var j=[];if("object"!=typeof r||null===r)return"";var w;w=o.arrayFormat in n?o.arrayFormat:"indices"in o?o.indices?"indices":"repeat":"indices";var x=n[w];v||(v=Object.keys(r)),b&&v.sort(b);for(var k=0;k<v.length;++k){var _=v[k];d&&null===r[_]||(j=j.concat(l(r[_],_,x,u,d,f?p:null,g,b,m,y,O,h)))}var A=j.join(c),C=!0===o.addQueryPrefix?"?":"";return A.length>0?C+A:""}},273:function(e,t,r){"use strict";t.a={data:function(){return{hideHeader:!1}},created:function(){this.showOrHideHeader()},methods:{showOrHideHeader:function(){-1!=this.$route.path.indexOf("appH5")?this.hideHeader=!0:this.hideHeader=!1}}}},432:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=r(63),a=r.n(i),n=r(62),o=r(273),s=r(252),l=r.n(s);t.default={data:function(){return{list:[],papers:"",getParams:{userId:this.$store.state.user.userId||this.$route.query.userId,__sid:this.$store.state.user.__sid||this.$route.query.__sid},submitFlag:!1}},created:function(){var e=this;this.$indicator.open({spinnerType:"fading-circle"}),this.$http.get(n.userRiskPapers,{params:this.getParams}).then(function(t){e.list=t.data.resData.list,e.papers=t.data.resData.papers,e.$indicator.close()})},mixins:[o.a],methods:{countItem:function(e){for(var t=document.getElementsByTagName("input"),r=0,i=0;i<t.length;i++)t[i].checked&&r++;r>=this.list.length&&(this.submitFlag=!0)},quesSubmit:function(){for(var e=this,t=document.getElementsByTagName("input"),r="",i=0;i<t.length;i++)t[i].checked&&(r+=t[i].value+"concat");var o=a()(this.getParams,{papersId:this.papers.id,questionContent:r});if(this.hideHeader){return void(navigator.userAgent.indexOf("android-h5")>-1?window.webReturn.webReturn(o.papersId,o.questionContent):window.webkit.messageHandlers.showNativeAction.postMessage({papersId:o.papersId,questionContent:o.questionContent}))}this.$http.post(n.doUserRiskPapers,l.a.stringify(o)).then(function(t){var r="/mine/safe/risk_result?levelName="+t.data.resData.levelName+"&level="+t.data.resData.level;e.$route.query.from&&(r=r+"&from="+e.$route.query.from+"&id="+e.$route.query.id),e.$router.push(r)})}}}},543:function(e,t,r){t=e.exports=r(31)(!1),t.push([e.i,".mt-progress[data-v-e9abeab6]{height:3px}.mt-progress-progress[data-v-e9abeab6]{background:#fa5c2a}.questions[data-v-e9abeab6]{font-size:.18rem;padding:.25rem .15rem .2rem;line-height:1.3;color:#333;text-align:justify}.questions-list li[data-v-e9abeab6]{padding:0 .15rem;height:.64rem;background:#fff;position:relative;margin-top:.1rem}.questions-list li[data-v-e9abeab6]:first-child{margin-top:0}.questions-list li label[data-v-e9abeab6]{position:absolute;height:100%;width:95%}.questions-list li span[data-v-e9abeab6]{font-size:.15rem;color:#333}.radio-input[data-v-e9abeab6]{display:none}.radio-input+.pseudo-radio[data-v-e9abeab6]{width:.15rem;height:.15rem;display:inline-block;text-align:center;line-height:.1rem}.radio-input+.pseudo-radio i[data-v-e9abeab6]{width:.11rem;height:.11rem;display:inline-block;background:#fff;border-radius:50%;margin-top:.02rem}.radio-input:checked+.pseudo-radio i[data-v-e9abeab6]{background:#fa5c2a}.pseudo-radio.aui-border[data-v-e9abeab6]:after{border-radius:50%;border:1px solid #666}.radio-input:checked+.pseudo-radio.aui-border[data-v-e9abeab6]:after{border-color:#fa5c2a}.btn-box[data-v-e9abeab6]{margin:.3rem .15rem}",""])},630:function(e,t,r){var i=r(543);"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);r(175)("fd320b9a",i,!0)},81:function(e,t,r){r(630);var i=r(12)(r(432),r(841),"data-v-e9abeab6",null);e.exports=i.exports},841:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"page"},[e.hideHeader?e._e():r("mt-header",{staticClass:"bar-nav aui-border-b",attrs:{title:"开始评测"}},[r("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),e._v(" "),r("form",{attrs:{action:"",id:"quesForm"}},e._l(e.list,function(t,i){return r("div",{staticClass:"ques-list"},[r("div",{staticClass:"questions"},[e._v("\n        "+e._s(i+1)+"."+e._s(t.questionName)+"\n    ")]),e._v(" "),r("ul",{ref:t.id,refInFor:!0,staticClass:"questions-list"},e._l(t.list,function(a){return r("li",[r("label",{staticClass:"aui-flex aui-flex-col aui-flex-middle"},[r("div",{staticClass:"aui-flex-item-1"},[r("input",{ref:a.id,refInFor:!0,staticClass:"radio-input",attrs:{type:"radio",name:"ques"+(i+1)},domProps:{value:"{"+t.id+","+a.id+"}"},on:{click:e.countItem}}),e._v(" "),e._m(0,!0)]),e._v(" "),r("div",{staticClass:"aui-flex-item-11 padding-r-15"},[r("span",[e._v(e._s(a.content))])])])])}))])})),e._v(" "),r("div",{staticClass:"btn-box"},[e.submitFlag?r("mt-button",{attrs:{size:"large",type:"danger"},nativeOn:{click:function(t){e.quesSubmit(t)}}},[e._v("提交问卷")]):r("mt-button",{attrs:{size:"large",type:"default",disabled:""}},[e._v("提交问卷")])],1)],1)},staticRenderFns:[function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("b",{staticClass:"pseudo-radio aui-border"},[r("i")])}]}}});