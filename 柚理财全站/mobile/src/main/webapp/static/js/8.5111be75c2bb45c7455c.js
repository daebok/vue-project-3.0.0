webpackJsonp([8],{271:function(t,e,s){"use strict";e.a={data:function(){return{hideHeader:!1}},created:function(){this.showOrHideHeader()},methods:{showOrHideHeader:function(){-1!=this.$route.path.indexOf("appH5")?this.hideHeader=!0:this.hideHeader=!1}}}},436:function(t,e,s){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=s(61),r=s(271);e.default={mixins:[r.a],data:function(){return{resdata:"",length:"",getParams:{userId:this.$store.state.user.userId||this.$route.query.userId,__sid:this.$store.state.user.__sid||this.$route.query.__sid}}},created:function(){var t=this;this.hideHeader&&(document.title=this.$route.meta.title),this.$indicator.open({spinnerType:"fading-circle"}),this.$http.get(a.userRiskPapers,{params:this.getParams}).then(function(e){t.resdata=e.data.resData,t.length=e.data.resData.configList.length,t.$indicator.close()})},methods:{toRisk:function(){var t="riskRating";this.hideHeader&&(t="appRisk"),this.$router.push({name:t,query:this.$route.query})}}}},486:function(t,e,s){e=t.exports=s(29)(!1),e.push([t.i,".rsk-content[data-v-38c4e2c0]{margin-top:.35rem;padding:0 .15rem}.rsk-content p[data-v-38c4e2c0]{font-size:.16rem;color:#666;line-height:1.5}.rsk-content-1[data-v-38c4e2c0]{text-indent:.32rem}.rsk-btn[data-v-38c4e2c0]{width:100%;background:#f95a28;margin-top:.3rem;height:.45rem;line-height:.45rem;padding:0}",""])},574:function(t,e,s){var a=s(486);"string"==typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);s(172)("0c8cafa3",a,!0)},787:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"page"},[t.hideHeader?t._e():s("mt-header",{staticClass:"bar-nav aui-border-b",attrs:{title:"风险评测"}},[s("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),t._v(" "),s("div",{staticClass:"rsk-content"},[s("p",[t._v("尊敬的用户：")]),t._v(" "),s("p",{staticClass:"rsk-content-1"},[t._v("\n      为了便于了解您自身的风险承受能力，我们将根据以下"+t._s(t.resdata.questionNum)+"个问题对您进行风险承受能力评测。根据评测结果，您的风险承受能力将属于以下"+t._s(t.length)+"种类型中的一种：\n      "),t._l(t.resdata.configList,function(e,a){return s("span",[t._v("\n        "+t._s(e.userRiskLevelName)),a+1!=t.length?s("em",[t._v("、")]):t._e()])}),t._v("。\n      为了给您提供更好的资产选择，请您认真作答，感谢您的配合。\n    ")],2),t._v(" "),s("mt-button",{staticClass:"rsk-btn",attrs:{type:"danger"},on:{click:t.toRisk}},[t._v("开始评测")])],1)],1)},staticRenderFns:[]}},81:function(t,e,s){s(574);var a=s(12)(s(436),s(787),"data-v-38c4e2c0",null);t.exports=a.exports}});