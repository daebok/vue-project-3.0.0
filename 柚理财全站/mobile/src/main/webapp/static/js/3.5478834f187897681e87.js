webpackJsonp([3],{273:function(e,t,s){"use strict";t.a={data:function(){return{hideHeader:!1}},created:function(){this.showOrHideHeader()},methods:{showOrHideHeader:function(){-1!=this.$route.path.indexOf("appH5")?this.hideHeader=!0:this.hideHeader=!1}}}},330:function(e,t,s){t=e.exports=s(31)(!1),t.push([e.i,".intro-content img,.message-content img{width:100%}",""])},331:function(e,t,s){var a=s(330);"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);s(175)("75effd3c",a,!0)},441:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=s(62),i=s(273),n=s(331);s.n(n);t.default={data:function(){return{resdata:""}},created:function(){var e=this;this.hideHeader&&(document.title=this.$route.meta.title),this.$indicator.open({spinnerType:"fading-circle"});var t={uuid:this.$route.params.uuid};this.$http.get(a.articleList,{params:t}).then(function(t){e.resdata=t.data.resData,e.$indicator.close()})},methods:{toStep:function(e){var t="/mine";this.hideHeader&&(t="/appH5"),this.$router.push(t+"/help/help_column?code="+e)}},mixins:[i.a]}},519:function(e,t,s){t=e.exports=s(31)(!1),t.push([e.i,"p{line-height:.24rem}.message{width:100%;padding:0 .15rem .5rem;background:#fff}.message-top{width:100%;padding-top:.15rem}.message-title{font-size:.16rem;color:#333}.message-time{font-size:.13rem;color:#999}.message-content,.message-content img{width:100%}.message-content{line-height:.24rem;color:#333}.message-content p{padding:0!important}.mar-t-15{margin-top:.15rem}",""])},606:function(e,t,s){var a=s(519);"string"==typeof a&&(a=[[e.i,a,""]]),a.locals&&(e.exports=a.locals);s(175)("6d9198c8",a,!0)},815:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"page",attrs:{id:"message"}},[e.hideHeader?e._e():s("mt-header",{staticClass:"bar-nav",attrs:{title:"平台公告详情"}},[s("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),e._v(" "),s("div",{staticClass:"message aui-border-t"},[s("div",{staticClass:"message-top"},[s("p",{staticClass:"message-title"},[e._v(e._s(e.resdata.title))]),e._v(" "),s("p",{staticClass:"message-time"},[e._v(e._s(e._f("dateFormatFun")(e.resdata.createTime,4)))])]),e._v(" "),s("div",{staticClass:"message-content mar-t-15",attrs:{id:"con"},domProps:{innerHTML:e._s(e.resdata.content)}})])],1)},staticRenderFns:[]}},85:function(e,t,s){s(606);var a=s(12)(s(441),s(815),null,null);e.exports=a.exports}});