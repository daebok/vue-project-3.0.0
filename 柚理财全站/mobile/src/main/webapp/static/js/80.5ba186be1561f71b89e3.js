webpackJsonp([80],{187:function(t,e,a){a(591);var s=a(12)(a(393),a(804),"data-v-543d3dbd",null);t.exports=s.exports},393:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a(61);e.default={created:function(){var t=this;this.$indicator.open({spinnerType:"fading-circle"});var e={id:this.$route.query.id,userId:this.$store.state.user.userId,__sid:this.$store.state.user.__sid};this.$http.get(s.letterInfo,{params:e}).then(function(e){t.$indicator.close(),t.resdata=e.data.resData})},data:function(){return{resdata:""}}}},503:function(t,e,a){e=t.exports=a(29)(!1),e.push([t.i,".page[data-v-543d3dbd]{background:#fff}.message[data-v-543d3dbd]{width:100%;padding:0 .15rem .5rem}.message-top[data-v-543d3dbd]{width:100%;padding-top:.15rem}.message-title[data-v-543d3dbd]{font-size:.16rem;color:#333}.message-time[data-v-543d3dbd]{font-size:.13rem;color:#999;line-height:.24rem}.message-content[data-v-543d3dbd]{width:100%;margin-top:.15rem;color:#333;line-height:.24rem}",""])},591:function(t,e,a){var s=a(503);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);a(173)("184b166e",s,!0)},804:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"page",attrs:{id:"message"}},[a("mt-header",{staticClass:"bar-nav",attrs:{title:"消息详情"}},[a("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),t._v(" "),a("div",{staticClass:"message aui-border-t"},[a("div",{staticClass:"message-top"},[a("p",{staticClass:"message-title"},[t._v(t._s(t.resdata.title))]),t._v(" "),a("p",{staticClass:"message-time"},[t._v(t._s(t._f("dateFormatFun")(t.resdata.createTime,4)))])]),t._v(" "),a("div",{staticClass:"message-content",domProps:{innerHTML:t._s(t.resdata.content)}})])],1)},staticRenderFns:[]}}});