webpackJsonp([67,69],{1:function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var a=t.domain="http://172.16.90.30:8088";t.investTotalAjax=a+"/app/open/index/countInfo.html",t.bannerAjax=a+"/app/open/index/banner.html",t.recommandProjectAjax=a+"/app/open/index/getProjectList.html",t.projectTypeList=a+"/app/open/invest/projectTypeList.html",t.productListAjax=a+"/app/open/invest/projectList.html",t.bondListData=a+"/app/open/bond/bondListData.html",t.realizeListData=a+"/app/open/index/realize/projectList.html",t.projectDetail=a+"/app/member/project/detail.html",t.investBespeak=a+"/app/member/index/investBespeak.html",t.getBondProjectDetail=a+"/app/member/bond/getBondProjectDetail.html",t.realizeDetail=a+"/app/member/realize/detailInfo.html",t.checkPwd=a+"/app/open/project/checkPwd.html",t.recordList=a+"/app/member/invest/recordList.html",t.bondRecordList=a+"/app/member/bond/getBondInvestPage.html",t.borrowerInfo=a+"/app/member/project/borrower.html",t.borrowPic=a+"/app/member/project/borrowPic.html",t.borrowerDetail=a+"/app/member/project/content.html",t.realizeInfo=a+"/app/member/realize/wxContent.html",t.initBond=a+"/app/member/bond/toBondInvest.html",t.initInvest=a+"/app/member/invest/toInvest.html",t.doInvest=a+"/app/member/invest/doInvest.html",t.doRealizeInvest=a+"/app/member/realize/doRealizeInvest.html",t.doBondInvest=a+"/app/member/bond/doBondInvest.html",t.interest=a+"/app/member/invest/interest.html",t.availableRedList=a+"/app/member/coupon/availableRedList.html",t.availableRateList=a+"/app/member/coupon/availableRateList.html",t.getAccountInfo=a+"/app/member/account/getAccountInfo.html",t.userRedenvelopeList=a+"/app/member/coupon/userRedenvelopeList.html",t.userRateCouponList=a+"/app/member/coupon/userRateCouponList.html",t.toRecharge=a+"/app/member/recharge/toRecharge.html",t.toCash=a+"/app/member/cash/toCash.html",t.getRechargeList=a+"/app/member/recharge/getLogList.html",t.getCashList=a+"/app/member/cash/getLogList.html",t.getBorrowHolding=a+"/app/member/myInvest/getBorrowHolding.html",t.getInvestApply=a+"/app/member/myInvest/getInvestApply.html",t.getInvestClosed=a+"/app/member/myInvest/getInvestClosed.html",t.getProjectCollectionList=a+"/app/member/myInvest/getProjectCollectionList.html",t.ableBondList=a+"/app/member/myBond/ableBondList.html",t.sellingBondList=a+"/app/member/myBond/sellingBondList.html",t.boughtBondList=a+"/app/member/myBond/boughtBondList.html",t.soldBondList=a+"/app/member/myBond/soldBondList.html",t.cancleBond=a+"/app/member/myBond/cancleBond.html",t.toBondSet=a+"/app/member/myBond/toBondSet.html",t.bondSetCommit=a+"/app/member/myBond/bondSetCommit.html",t.getRealizeAbleList=a+"/app/member/myRealize/getRealizeAbleList.html",t.getRealizingList=a+"/app/member/myRealize/getRealizingList.html",t.getRealizedList=a+"/app/member/myRealize/getRealizedList.html",t.toRealizeSet=a+"/app/member/myRealize/toSet.html",t.doRealizeSet=a+"/app/member/myRealize/doSet.html",t.myLoanList=a+"/app/member/myLoan/getLogList.html",t.myRepaymentList=a+"/app/member/myRepayment/getLogList.html",t.myLoanPlanList=a+"/app/member/myLoan/getProjectRepaymentList.html",t.toRepay=a+"/app/member/myRepayment/toRepay.html",t.doRepay=a+"/app/member/myRepayment/doRepay.html",t.getRepayCode=a+"/app/member/myRepayment/getRepayCode.html",t.bespeak=a+"/app/open/borrow/bespeak.html",t.bespeakAdd=a+"/app/open/borrow/bespeakAdd.html",t.autoInit=a+"/app/member/auto/toSet.html",t.autoInvestDetails=a+"/app/member/autoInvestDetails.html",t.interestStyle=a+"/app/open/index/servers.html",t.autoInvestRule=a+"/app/member/autoInvestRule.html",t.addAutoInvest=a+"/app/member/addAutoInvest.html",t.basicInfo=a+"/app/member/account/basicInfo.html",t.messageList=a+"/app/member/letter/list.html",t.letterInfo=a+"/app/member/letter/letterInfo.html",t.batchSet=a+"/app/member/letter/batchSet.html",t.logsDetail=a+"/app/member/fund/getLogList.html",t.vip=a+"/app/member/account/vip.html",t.tppRegister=a+"/app/member/security/tppRegister.html",t.modifyPhoneCode=a+"/app/member/security/modifyPhoneCode.html",t.doModifyPhone=a+"/app/member/security/doModifyPhone.html",t.bindPhoneCode=a+"/app/member/security/bindPhoneCode.html",t.doBindPhone=a+"/app/member/security/doBindPhone.html",t.modifyEmailCode=a+"/app/member/security/modifyEmailCode.html",t.doModifyEmail=a+"/app/member/security/doModifyEmail.html",t.bindEmailCode=a+"/app/member/security/bindEmailCode.html",t.doBindEmail=a+"/app/member/security/doBindEmail.html",t.getBankCardList=a+"/app/member/bankCard/getBankCardList.html",t.unBind=a+"/app/member/bankCard/unBind.html",t.bindCard=a+"/app/member/bankCard/bind.html",t.userRiskPapers=a+"/app/member/risk/userRiskPapers.html",t.doUserRiskPapers=a+"/app/member/risk/doUserRiskPapers.html",t.getArticleList=a+"/app/open/column/getArticleList.html",t.articleList=a+"/app/open/column/articleList.html",t.userInvite=a+"/app/member/invite/userInvite.html",t.inviteLogList=a+"/app/member/invite/getLogList.html",t.loginAjax=a+"/app/open/user/doLogin.html",t.registerPhoneCode=a+"/app/open/user/registerPhoneCode.html",t.registerFirst=a+"/app/open/user/registerFirst.html",t.doRegister=a+"/app/open/user/doRegister.html",t.sendValidCode=a+"/app/open/user/security/sendValidCode.html",t.validation=a+"/app/open/user/retrievepwd/validation.html",t.confirmPwd=a+"/app/open/user/retrievepwd/confirm.html",t.doModifyPwd=a+"/app/member/security/doModifyPwd.html"},205:function(e,t,a){"use strict";function i(e){if(e&&e.__esModule)return e;var t={};if(null!=e)for(var a in e)Object.prototype.hasOwnProperty.call(e,a)&&(t[a]=e[a]);return t.default=e,t}Object.defineProperty(t,"__esModule",{value:!0});var o=a(1),m=i(o);t.default={data:function(){return{list:[]}},created:function(){this.projectList()},mounted:function(){this.wrapperHeight=document.documentElement.clientHeight-this.$refs.wrapper.getBoundingClientRect().top},methods:{projectList:function(){var e=this,t=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;this.noData=!1,this.$indicator.open({spinnerType:"fading-circle"});var a={sectionCode:"notice","page.page":t};this.$http.get(m.getArticleList,{params:a}).then(function(t){e.$indicator.close(),e.list=e.list.concat(t.data.resData.list),0==e.list.length&&(e.noData=!0)})},loadMore:function(){var e=this;this.loading=!0,this.page+=1,setTimeout(function(){e.projectList(e.page),e.loading=!1},500)}}}},557:function(e,t,a){var i,o;i=a(205);var m=a(594);o=i=i||{},"object"!=typeof i.default&&"function"!=typeof i.default||(o=i=i.default),"function"==typeof o&&(o=o.options),o.render=m.render,o.staticRenderFns=m.staticRenderFns,e.exports=i},594:function(e,t){e.exports={render:function(){var e=this;return e._h("div",{staticClass:"page",attrs:{id:"siteIntro"}},[e._h("mt-header",{staticClass:"bar-nav",attrs:{title:"平台公告"}},[e._h("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})])," ",e._h("div",{ref:"wrapper",staticClass:"page-infinite-wrapper",style:{height:e.wrapperHeight+"px"}},[e._h("div",{directives:[{name:"infinite-scroll",rawName:"v-infinite-scroll",value:e.loadMore,expression:"loadMore"}],staticClass:"list-box margin-t-10",attrs:{"infinite-scroll-disabled":"loading","infinite-scroll-immediate-check":"false","infinite-scroll-distance":"50"}},[e._l(e.list,function(t){return e._h("mt-cell",{attrs:{title:t.title,label:t.remark,to:"/mine/siteIntro/detail/"+t.uuid,"is-link":""}})})])])])},staticRenderFns:[]}}});
//# sourceMappingURL=67.d1b1fd27eeb00f170254.js.map