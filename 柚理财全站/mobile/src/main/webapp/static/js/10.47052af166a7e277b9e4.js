webpackJsonp([10],{271:function(t,e,i){"use strict";e.a={data:function(){return{hideHeader:!1}},created:function(){this.showOrHideHeader()},methods:{showOrHideHeader:function(){-1!=this.$route.path.indexOf("appH5")?this.hideHeader=!0:this.hideHeader=!1}}}},420:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(61),a=i(271);e.default={name:"help",data:function(){return{list:""}},created:function(){var t=this;this.hideHeader&&(document.title=this.$route.meta.title),this.$http.get(n.helpCenter).then(function(e){t.list=e.data.resData.sectionlist})},methods:{toStep:function(t){var e="/mine";this.hideHeader&&(e="/appH5"),this.$router.push(e+"/help/help_column?code="+t)}},mixins:[a.a]}},78:function(t,e,i){var n=i(12)(i(420),i(848),null,null);t.exports=n.exports},848:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"page",attrs:{id:"help"}},[t.hideHeader?t._e():i("mt-header",{staticClass:"bar-nav",attrs:{title:"帮助中心"}},[i("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),t._v(" "),t._l(t.list,function(e){return i("mt-cell",{key:e.id,attrs:{title:e.sectionName,"is-link":""},nativeOn:{click:function(i){t.toStep(e.sectionCode)}}})})],2)},staticRenderFns:[]}}});