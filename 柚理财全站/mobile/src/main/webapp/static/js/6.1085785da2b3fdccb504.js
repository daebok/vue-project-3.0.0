webpackJsonp([6],{271:function(e,t,a){"use strict";t.a={data:function(){return{hideHeader:!1}},created:function(){this.showOrHideHeader()},methods:{showOrHideHeader:function(){-1!=this.$route.path.indexOf("appH5")?this.hideHeader=!0:this.hideHeader=!1}}}},405:function(e,t,a){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=a(61),r=a(271);t.default={data:function(){return{resdata:""}},created:function(){var e=this;this.$http.get(i.customerService).then(function(t){e.resdata=t.data.resData})},mixins:[r.a]}},529:function(e,t,a){t=e.exports=a(29)(!1),t.push([e.i,".service-tel[data-v-774c0f81]{background:#fff;height:.78rem;padding:0 .15rem;display:flex}.tel-left[data-v-774c0f81]{font-size:.16rem;color:#333;padding-top:.2rem;margin-right:.1rem;line-height:1}.tel-center[data-v-774c0f81]{margin-right:.2rem}.tel-number[data-v-774c0f81]{font-size:.16rem;color:#ff9545;margin-top:.2rem;line-height:1}.service-time[data-v-774c0f81]{line-height:1;font-size:.13rem;color:#999;margin-top:.1rem}.tel-btn[data-v-774c0f81]{display:block;width:1.03rem;background:url("+a(173)+") no-repeat 50%;height:.33rem;line-height:.33rem;margin-top:.225rem;border-radius:.15rem;color:#fff;font-size:.15rem}.tel-btn img[data-v-774c0f81]{width:.18rem;height:.18rem;vertical-align:middle;margin-left:.07rem}.service-list[data-v-774c0f81]{padding:0 .15rem;background:#fff}.service-list li[data-v-774c0f81]{height:.48rem;line-height:.48rem}.service-list li label[data-v-774c0f81]{font-size:.16rem;color:#333}.service-list li span[data-v-774c0f81]{float:right;color:#999;font-size:.13rem}",""])},617:function(e,t,a){var i=a(529);"string"==typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);a(172)("7b1e06f9",i,!0)},675:function(e,t){e.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACYAAAAmCAMAAACf4xmcAAAAYFBMVEUAAAD///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////98JRy6AAAAH3RSTlMAMBlx+yP1CgToTK3ebGXTRcEQlIq22VShy1qaKYQ7x2xfGgAAAcJJREFUOMt1lOmCgyAMhFEQxPvoua37vf9bboFWFLfzi+AwSYaI2CGT9UWButQyE19gZAPYsa5HCzTSiCPKVqHnwbyPDLNGyTJlFT222+2WneVS7FkVLCYyisIF5gTVlnVF/0bhs6uszF/LQXHdatmonmsccuZXDU/NsB5HR5ZpcLAZdM4ixftb2RMzihmPWweLiwf60FnLss0fcL2B9DsnWp9FWZMWhs6BLFShlfFiXRQ7E/CzwF0EtF62UWWS8tLlk/ViQY7m1TJztNU6Vm98+/m6/SAT0hmzykNU2eSQosaslvn6Z5FiohaNXcMu2C8O0I1Q4xrZIHbEHcH5Eww4FOKI85ZWO5bXPtJiUhN8Ff8mjS1UmwaOLdRMHxMdwo0cDZFU7yNfk16RIuP2nk2+yd14CtGQRzu4V4+UaGi896fYAVXNI6FJ2jAWua/TYXwerJu0MiFdHSoA/exB7mkzbVgsrr+CBpvVYE0yRf2n2JPKxYkpy8fDuGXJLY+unZ++T1iaKrleIw6o4LqNgy8JpgcM6ft2YTY7klT0x+nLe9TyW7457q/Xnfj3vdSAvY/j3S36NoonxN/lbPFPdLtP9wd8Wis360bYyAAAAABJRU5ErkJggg=="},77:function(e,t,a){a(617);var i=a(12)(a(405),a(828),"data-v-774c0f81",null);e.exports=i.exports},828:function(e,t,a){e.exports={render:function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"page",attrs:{id:"intro"}},[e.hideHeader?e._e():i("mt-header",{staticClass:"bar-nav aui-border-b",attrs:{title:"客户服务"}},[i("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),e._v(" "),i("div",{staticClass:"service"},[i("div",{staticClass:"service-tel margin-t-10"},[i("div",{staticClass:"tel-left"},[e._v("客服热线")]),e._v(" "),i("div",{staticClass:"tel-center"},[i("p",{staticClass:"tel-number"},[e._v(e._s(e.resdata.customerHotline))]),e._v(" "),i("p",{staticClass:"service-time"},[e._v("服务时间9:00-21:00")])]),e._v(" "),i("div",{staticClass:"tel-right"},[i("a",{staticClass:"tel-btn",attrs:{href:"tel:"+e.resdata.customerHotline}},[i("img",{attrs:{src:a(675)}}),e._v(" "),i("i",[e._v("立即拨打")])])])]),e._v(" "),i("ul",{staticClass:"service-list margin-t-10"},[i("li",{staticClass:"aui-border-b"},[i("label",[e._v("微信公众号")]),e._v(" "),i("span",[e._v(e._s(e.resdata.webIndexWechat))])]),e._v(" "),i("li",{staticClass:"aui-border-b"},[i("label",[e._v("新浪微博")]),e._v(" "),i("span",[e._v(e._s(e.resdata.webIndexSinaMicroblog))])]),e._v(" "),i("li",[i("label",[e._v("客服QQ")]),e._v(" "),i("span",[e._v(e._s(e.resdata.webIndexQq))])])])])],1)},staticRenderFns:[]}}});