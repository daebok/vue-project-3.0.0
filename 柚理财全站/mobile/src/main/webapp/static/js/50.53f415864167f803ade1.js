webpackJsonp([50],{182:function(t,a,s){s(623);var e=s(12)(s(388),s(836),"data-v-890ac7ea",null);t.exports=e.exports},268:function(t,a){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADIBAMAAABfdrOtAAAALVBMVEUAAADr6+vl5eXi4uLp6enp6enp6enm5ubp6eng4OD////t7e3m5ub7+/v5+fmOZTRgAAAACXRSTlMA/hPQXYPfN6O77zVhAAAC8klEQVR42u3aTWsTQRjA8TFNTL3pTcklhJReC0Jt6CWUDdVbKEkwN6EYE+phqcXSm1KVih4a69vYteA1olCEUn28eBFLxbuigkdf6GcwNGa7zUzMMzO7uI3P/wP0t9mnMztkw/7bhmqc359igRZpGk0lyYJskO82woJsoYU8YEFWbiH3WJBlWsgKCzL+pyAnH28jNguuWBvJs4Cas6wzbeSuZU2yAHIBF/LfiHG34O7ZgmAEsCRvishD35EaF/PbiHBJtvmfTXlKRmVINpLyllRe26X9Mx6UIVcG+L6qttrH6Jjz8mEZci7W+a+g9GEud17zERmyHOcdXVB9ynrLDsiQEcY9qT6YhQnYcengWUYYk8Hyttkl0TjFWMZgFyiL1yxPgPEPZvEC72B3zRU8woWBSldApMyFFBCxk5Jq3AhBRgghhBBCCCGEEEIIIf8AWf3RwPXTAGkayL5rI6sNdE+1kTU88lwbedTAp408xhvPwj0T/gmNfNRH1vBz10e48x5X6LcVQgghhBBC+h6p73xB9fWbAfKqge1luE8rdIIM30wc/AnyszbCP6DnvqWP8DfHcG2FfVshhBBCCDmIiLOD7FfIj0T1BrondCRSOhKF+3bxPjoSbQMuOhIRQgghhBxYBP/+JORvghTeaYX7uxWVI1G4D3cq565wz0TlfXzIf1nQR3sXIYQQQgghhBASPz1ugFyzEUT6BgAYIADViV7EVQBTBOD435h4CcAY2e283c0YmhG+I1POgVYbU10McDuhjdShnVSJwl6vtZFtcMtKkFvgSRuBvTZFIwYA5verDp7yAnII3PRH74C3o72QdR3FSfRAYuB7edngfW6TiUUTPiNZ+WL0tS5LPj3jH7HRdY+MlMA4YYMUS9/2g3g3gXhomVUVCbF4blxfeFGwGbLhnNbnqRZmmVKRubOjCitnffr6ZJJpNWwVFy9Cj96OFa1ZZlpq3soVlxZHK5WEe+GVyvTYUrFgzacYRVEURfVRvwGlXm0L/Q334wAAAABJRU5ErkJggg=="},277:function(t,a,s){t.exports={default:s(278),__esModule:!0}},278:function(t,a,s){var e=s(3),i=e.JSON||(e.JSON={stringify:JSON.stringify});t.exports=function(t){return i.stringify.apply(i,arguments)}},312:function(t,a,s){"use strict";Object.defineProperty(a,"__esModule",{value:!0}),a.default={data:function(){return{countTime:""}},props:{remainTimes:{type:Number,default:0}},methods:{countDown:function(){var t=this,a=Number(this.remainTimes);a>0&&(clearInterval(this.interval),this.interval=setInterval(function(){if(a>1){a-=1;var s=Math.floor(a/60),e=Math.floor(a%60),i=s<10?"0"+s:s,n=e<10?"0"+e:e;t.countTime=i+":"+n}else clearInterval(t.interval),window.location.reload()},1e3))}},created:function(){this.countDown()},destroyed:function(){this.$nextTick(function(){clearInterval(this.interval)})}}},328:function(t,a,s){a=t.exports=s(30)(!1),a.push([t.i,".count-down[data-v-4e3658d4]{font-size:.13rem;color:#ff9545}",""])},334:function(t,a,s){var e=s(328);"string"==typeof e&&(e=[[t.i,e,""]]),e.locals&&(t.exports=e.locals);s(173)("a95941ee",e,!0)},350:function(t,a,s){s(334);var e=s(12)(s(312),s(353),"data-v-4e3658d4",null);t.exports=e.exports},353:function(t,a){t.exports={render:function(){var t=this,a=t.$createElement;return(t._self._c||a)("span",{staticClass:"count-down"},[t._v("\n  "+t._s(t.countTime)+"\n")])},staticRenderFns:[]}},388:function(t,a,s){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var e=s(277),i=s.n(e),n=s(61),o=s(350),r=s.n(o);a.default={name:"myInvest",components:{CountDown:r.a},methods:{changeTab:function(){var t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;1==t?(this.active="tab-con1",this.list=[]):2==t?(this.active="tab-con2",this.list2=[]):3==t?(this.active="tab-con3",this.list3=[]):4==t&&(this.active="tab-con4",this.list4=[]),this.$router.push("/account/bond/bond?tab="+this.active),this.projectList()},backUrl:function(){this.$router.push("/account")},projectList:function(){var t=this,a=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;this.noData=!1,this.$indicator.open({spinnerType:"fading-circle"});var s=void 0;"tab-con1"==this.active?s=n.ableBondList:"tab-con2"==this.active?s=n.sellingBondList:"tab-con3"==this.active?s=n.soldBondList:"tab-con4"==this.active&&(s=n.boughtBondList);var e={"page.page":a,"page.size":10,userId:this.$store.state.user.userId,__sid:this.$store.state.user.__sid,randTime:(new Date).getTime()};this.$http.get(s,{params:e}).then(function(s){if(t.$indicator.close(),0==s.data.resData.list.length&&(t.noData=!0),s.data.resData.totalPage<=1)switch(t.active){case"tab-con1":t.allLoaded=!0;break;case"tab-con2":t.allLoaded2=!0;break;case"tab-con3":t.allLoaded3=!0;break;case"tab-con4":t.allLoaded4=!0}if(a>s.data.resData.totalPage)return void(s.data.resData.totalPage>0&&t.$toast("无更多数据加载哦~"));"tab-con1"==t.active?(t.list=t.list.concat(s.data.resData.list),t.totalPage1=s.data.resData.totalPage):"tab-con2"==t.active?(t.list2=t.list2.concat(s.data.resData.list),t.totalPage2=s.data.resData.totalPage):"tab-con3"==t.active?(t.list3=t.list3.concat(s.data.resData.list),t.totalPage3=s.data.resData.totalPage):"tab-con4"==t.active&&(t.list4=t.list4.concat(s.data.resData.list),t.totalPage4=s.data.resData.totalPage)})},loadBottom:function(t){var a=this;setTimeout(function(){if("tab-con1"==a.active){var s=++a.page1;s>a.totalPage1&&(a.allLoaded=!0),a.projectList(s),a.$refs.loadmore.onBottomLoaded(t)}else if("tab-con2"==a.active){var e=++a.page2;e>a.totalPage2&&(a.allLoaded2=!0),a.projectList(e),a.$refs.loadmore2.onBottomLoaded(t)}else if("tab-con3"==a.active){var i=++a.page3;i>a.totalPage3&&(a.allLoaded3=!0),a.projectList(i),a.$refs.loadmore3.onBottomLoaded(t)}else if("tab-con4"==a.active){var n=++a.page4;n>a.totalPage4&&(a.allLoaded4=!0),a.projectList(n),a.$refs.loadmore4.onBottomLoaded(t)}},1e3)},downloadTips:function(){this.$toast("请前往PC端查看并下载协议")},toPay:function(t){window.location.href=n.doBondPay+"?investOrderNo="+t+"&userId="+this.$store.state.user.userId+"&__sid="+this.$store.state.user.__sid},cancelBond:function(t){var a=this;this.$messagebox({title:" ",cancelButtonText:"取消",confirmButtonText:"确认",showCancelButton:!0,message:"确认要撤回未转让成功的资金？"}).then(function(s){if("confirm"==s){var e={id:t,userId:a.$store.state.user.userId,__sid:a.$store.state.user.__sid};a.$http.get(n.cancleBond,{params:e}).then(function(s){39321==s.data.resCode?(a.$toast("撤回成功"),a.$refs[t][0].hidden=!0):a.$toast(s.data.resMsg)})}})},toBondDeal:function(t,a,s){var e=this;a>0?this.$messagebox({title:" ",showCancelButton:!0,closeOnClickModal:!0,message:"此笔投资您拥有加息收益"+a+"元,若您进行转让,系统将视为您放弃此笔投资的加息收益。"}).then(function(i){"confirm"===i&&e.conFun(t,a,s)}):this.conFun(t,a,s)},conFun:function(t,a,s){var e=this,o={id:t,userId:this.$store.state.user.userId,__sid:this.$store.state.user.__sid};this.$http.get(n.toBondSet,{params:o}).then(function(a){for(var n=0,o=0,r=["0.0"],l=["0.0"];n<a.data.resData.max;)n+=.1,r.push(n.toFixed(1));for(;o<Math.abs(a.data.resData.min);)o+=.1,l.push(o.toFixed(1));sessionStorage.bondApr_max=i()(r),sessionStorage.bondApr_min=i()(l),e.$router.push("/account/bond/bond_deal/?uuid="+t+"&tab="+s)})}},created:function(){var t=this;this.active=this.$route.query.tab||"tab-con1",this.projectList(),this.$nextTick(function(){t.wrapperHeight=document.documentElement.clientHeight-t.$refs.wrapper.getBoundingClientRect().top})},data:function(){return{active:"tab-con1",wrapperHeight:0,noData:!1,list:[],list2:[],list3:[],list4:[],allLoaded:!1,allLoaded2:!1,allLoaded3:!1,allLoaded4:!1,page1:1,page2:1,page3:1,page4:1,totalPage1:1,totalPage2:1,totalPage3:1,totalPage4:1}}}},535:function(t,a,s){a=t.exports=s(30)(!1),a.push([t.i,".rd-tab-title[data-v-890ac7ea]{width:100%;height:.4rem;background:#fff}.rd-tab-title li[data-v-890ac7ea]{width:25%;line-height:.36rem;text-align:center;display:inline-block;float:left}.current[data-v-890ac7ea]{color:#ff9545;border-bottom:2px solid #ff9545}.rd-tab-title li span[data-v-890ac7ea]{width:.6rem;display:block;margin:0 auto}.invest-am-list[data-v-890ac7ea]{padding:0 .15rem;background:#fff;margin-top:.1rem}.invest-am-title[data-v-890ac7ea]{border-bottom:1px solid #dedede}.am-title[data-v-890ac7ea]{height:.42rem;line-height:.42rem;font-size:.16rem}.repayment-time[data-v-890ac7ea]{margin-bottom:.1rem;color:#666}.info-con[data-v-890ac7ea]{font-size:.12rem;padding-bottom:.15rem}.info-con li span[data-v-890ac7ea]:last-child{font-size:.14rem}.btn-box[data-v-890ac7ea]{height:.5rem}.red-btn[data-v-890ac7ea]{border:1px solid #ff9545;color:#ff9545}.plain-btn[data-v-890ac7ea]{height:.3rem;line-height:.28rem;width:.75rem;margin:.1rem 0;border-radius:.4rem;font-size:.14rem;float:right;margin-left:.1rem;padding:0}.mint-button[data-v-890ac7ea]:not(.is-disabled):active:after{opacity:0}.count-time[data-v-890ac7ea]{float:left;line-height:.5rem;color:#666;font-size:.12rem}",""])},623:function(t,a,s){var e=s(535);"string"==typeof e&&(e=[[t.i,e,""]]),e.locals&&(t.exports=e.locals);s(173)("678bf46e",e,!0)},836:function(t,a,s){t.exports={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"page",attrs:{id:"bond"}},[e("mt-header",{staticClass:"bar-nav",attrs:{title:"转让"}},[e("mt-button",{attrs:{icon:"back"},on:{click:t.backUrl},slot:"left"})],1),t._v(" "),e("ul",{staticClass:"rd-tab-title"},[e("li",{on:{click:function(a){t.changeTab(1)}}},[e("span",{class:{current:"tab-con1"==t.active}},[t._v("可转让")])]),t._v(" "),e("li",{on:{click:function(a){t.changeTab(2)}}},[e("span",{class:{current:"tab-con2"==t.active}},[t._v("转让中")])]),t._v(" "),e("li",{on:{click:function(a){t.changeTab(3)}}},[e("span",{class:{current:"tab-con3"==t.active}},[t._v("已转让")])]),t._v(" "),e("li",{on:{click:function(a){t.changeTab(4)}}},[e("span",{class:{current:"tab-con4"==t.active}},[t._v("已受让")])])]),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:t.noData,expression:"noData"}],staticClass:"text-center no-data"},[e("img",{attrs:{src:s(268),alt:"缺省图片"}}),t._v(" "),e("p",[t._v("暂无记录")])]),t._v(" "),e("div",{directives:[{name:"show",rawName:"v-show",value:!t.noData,expression:"!noData"}],staticClass:"page-tab-container"},[e("mt-tab-container",{staticClass:"page-tabbar-tab-container",model:{value:t.active,callback:function(a){t.active=a},expression:"active"}},[e("mt-tab-container-item",{attrs:{id:"tab-con1"}},[e("div",{ref:"wrapper",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[e("mt-loadmore",{ref:"loadmore",attrs:{"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded}},t._l(t.list,function(a){return e("section",{staticClass:"invest-am-list"},[e("router-link",{attrs:{to:"/investDetail/"+a.projectId}},[e("div",{staticClass:"invest-am-title"},[e("p",{staticClass:"am-title"},[t._v(t._s(t._f("hideBorrowName")(a.projectName)))]),t._v(" "),e("p",{staticClass:"repayment-time"},[t._v("本期回款日："+t._s(t._f("dateFormatFun")(a.nextRepayDate)))])])]),t._v(" "),e("ul",{staticClass:"info-con clearfix color-999 aui-border-b"},[e("li",{staticClass:"margin-t-10"},[e("span",{staticClass:"color-999"},[t._v("原预期年化收益率")]),t._v(" "),e("span",{staticClass:"font-arial main-color pull-right"},[t._v(t._s(a.apr)+"%")])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("剩余期限(天)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(a.projectRealRemainDays))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("可转让金额(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.amount,"",2)))])])]),t._v(" "),e("p",{staticClass:"btn-box clearfix"},[e("mt-button",{staticClass:"plain-btn red-btn",attrs:{size:"normal",plain:""},on:{click:function(s){t.toBondDeal(a.uuid,a.raiseInterest,"tab-con1")}}},[t._v("转让")])],1)],1)}))],1)]),t._v(" "),e("mt-tab-container-item",{attrs:{id:"tab-con2"}},[e("div",{ref:"",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[e("mt-loadmore",{ref:"loadmore2",attrs:{"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded2}},t._l(t.list2,function(a){return e("section",{ref:a.uuid,refInFor:!0,staticClass:"invest-am-list"},[e("router-link",{attrs:{to:"/bondDetail/"+a.uuid}},[e("div",{staticClass:"invest-am-title"},[e("p",{staticClass:"am-title"},[t._v(t._s(t._f("hideBorrowName")(a.bondName)))])])]),t._v(" "),e("ul",{staticClass:"info-con clearfix color-999 aui-border-b"},[e("li",{staticClass:"margin-t-10"},[e("span",{staticClass:"color-999"},[t._v("折溢价率")]),t._v(" "),e("span",{staticClass:"font-arial main-color pull-right"},[t._v(t._s(a.bondApr)+"%")])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("转让价格(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.bondPrice,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("已转出金额(元)")]),t._v(" "),a.soldCapita?e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.soldCapita,"",2)))]):e("span",{staticClass:"font-arial pull-right"},[t._v("0")])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("债权本金(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.bondMoney,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("转让申请时间")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("dateFormatFun")(a.createTime,4)))])])]),t._v(" "),e("p",{staticClass:"btn-box clearfix"},[e("mt-button",{staticClass:"plain-btn",attrs:{size:"normal",plain:""},on:{click:function(s){t.cancelBond(a.uuid)}}},[t._v("撤回")])],1)],1)}))],1)]),t._v(" "),e("mt-tab-container-item",{attrs:{id:"tab-con3"}},[e("div",{ref:"",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[e("mt-loadmore",{ref:"loadmore3",attrs:{"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded3}},t._l(t.list3,function(a){return e("section",{staticClass:"invest-am-list"},[e("router-link",{attrs:{to:"/bondDetail/"+a.uuid}},[e("div",{staticClass:"invest-am-title"},[e("p",{staticClass:"am-title"},[t._v(t._s(t._f("hideBorrowName")(a.bondName)))])])]),t._v(" "),e("ul",{staticClass:"info-con clearfix color-999 aui-border-b"},[e("li",{staticClass:"margin-t-10"},[e("span",{staticClass:"color-999"},[t._v("折溢价率")]),t._v(" "),a.bondApr>=0?e("span",{staticClass:"font-arial main-color pull-right"},[t._v("+"+t._s(t._f("currency")(a.bondApr,"",2))+"%")]):e("span",{staticClass:"font-arial main-color pull-right"},[t._v(t._s(t._f("currency")(a.bondApr,"",2))+"%")])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("转让价格(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.bondPrice,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("实收金额(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.receivedMoney,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("债权本金(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.bondMoney,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("转让成功时间")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("dateFormatFun")(a.successTime,4)))])])]),t._v(" "),e("p",{staticClass:"btn-box clearfix"},[e("mt-button",{staticClass:"plain-btn ",attrs:{size:"normal",plain:""},on:{click:t.downloadTips}},[t._v("下载协议")])],1)],1)}))],1)]),t._v(" "),e("mt-tab-container-item",{attrs:{id:"tab-con4"}},[e("div",{ref:"",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[e("mt-loadmore",{ref:"loadmore4",attrs:{"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded4}},t._l(t.list4,function(a){return e("section",{staticClass:"invest-am-list"},[e("router-link",{attrs:{to:"/bondDetail/"+a.bondId}},[e("div",{staticClass:"invest-am-title"},[e("p",{staticClass:"am-title"},[t._v(t._s(t._f("hideBorrowName")(a.bondName)))])])]),t._v(" "),e("ul",{staticClass:"info-con clearfix color-999 aui-border-b"},[e("li",{staticClass:"margin-t-10"},[e("span",{staticClass:"color-999"},[t._v("折溢价率")]),t._v(" "),a.bondApr>=0?e("span",{staticClass:"font-arial main-color pull-right"},[t._v("+"+t._s(t._f("currency")(a.bondApr,"",2))+"%")]):e("span",{staticClass:"font-arial main-color pull-right"},[t._v(t._s(t._f("currency")(a.bondApr,"",2))+"%")])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("支付价格(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.paidMoney,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("待收本息(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.waitMoney,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("债权本金(元)")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("currency")(a.amount,"",2)))])]),t._v(" "),e("li",{staticClass:"margin-t-10"},[e("span",[t._v("受让成功时间")]),t._v(" "),e("span",{staticClass:"font-arial pull-right"},[t._v(t._s(t._f("dateFormatFun")(a.createTime,4)))])])]),t._v(" "),0==a.status?e("div",{staticClass:"btn-box clearfix"},[e("p",{staticClass:"count-time"},[t._v("剩余时间 "),e("count-down",{attrs:{remainTimes:a.remainTimes}})],1),t._v(" "),e("mt-button",{staticClass:"plain-btn red-btn",attrs:{size:"normal",plain:""},on:{click:function(s){t.toPay(a.investOrderNo)}}},[t._v("去支付")])],1):e("div",{staticClass:"btn-box clearfix"},[e("mt-button",{staticClass:"plain-btn",attrs:{size:"normal",plain:""},on:{click:t.downloadTips}},[t._v("下载协议")]),t._v(" "),e("router-link",{attrs:{to:"/account/myInvest/repayment_plan?tab=tab-con4&investId="+a.investId}},[e("mt-button",{staticClass:"plain-btn red-btn",attrs:{size:"normal",plain:""}},[t._v("回款计划")])],1),t._v(" "),1==a.showBond?e("mt-button",{staticClass:"plain-btn red-btn",attrs:{type:"danger",size:"normal",plain:""},on:{click:function(s){t.toBondDeal(a.investId,a.raiseInterest,"tab-con4")}}},[t._v("转让")]):t._e()],1)],1)}))],1)])],1)],1)],1)},staticRenderFns:[]}}});