webpackJsonp([73],{185:function(t,e,a){a(577);var s=a(12)(a(378),a(788),"data-v-3d0e52fa",null);t.exports=s.exports},378:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var s=a(61);e.default={data:function(){return{list:[],noData:!1,allLoaded:!1,page:1,totalPage:1,wrapperHeight:0}},created:function(){this.projectList(),this.$nextTick(function(){this.wrapperHeight=document.documentElement.clientHeight-this.$refs.wrapper.getBoundingClientRect().top})},methods:{projectList:function(){var t=this,e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;this.noData=!1,this.$indicator.open({spinnerType:"fading-circle"});var a={userId:this.$store.state.user.userId,__sid:this.$store.state.user.__sid,"page.pageSize":10,"page.page":e};this.$http.get(s.messageList,{params:a}).then(function(a){if(t.$indicator.close(),0==a.data.resData.list.length&&(t.noData=!0),a.data.resData.totalPage<=1&&(t.allLoaded=!0),e>a.data.resData.totalPage)return void(a.data.resData.totalPage>0&&t.$toast("无更多数据加载哦~"));t.list=t.list.concat(a.data.resData.list),t.totalPage=a.data.resData.totalPage})},loadTop:function(t){var e=this;setTimeout(function(){e.list=[],e.page=1,e.allLoaded=!1,e.projectList(),e.$refs.loadmore.onTopLoaded(t)},1e3)},loadBottom:function(t){var e=this;setTimeout(function(){e.page++,e.projectList(e.page),e.$refs.loadmore.onBottomLoaded(t),e.page>e.totalPage&&(e.allLoaded=!0)},500)},readAll:function(){var t=this;this.$messagebox({title:" ",showCancelButton:!0,message:"将全部消息标记为已读？"}).then(function(e){if("confirm"==e){var a={userId:t.$store.state.user.userId,__sid:t.$store.state.user.__sid,batchType:2};t.$http.get(s.batchSet,{params:a}).then(function(e){t.list=[],t.projectList()})}})}}}},489:function(t,e,a){e=t.exports=a(29)(!1),e.push([t.i,".message-list[data-v-3d0e52fa],.message[data-v-3d0e52fa]{width:100%}.message-list[data-v-3d0e52fa]{padding-left:5%;margin-top:.1rem;background:#fff}.message-list li[data-v-3d0e52fa]{height:.65rem}.message-list li[data-v-3d0e52fa]:last-child:after{border-bottom:0}.message-list li p[data-v-3d0e52fa]{float:left;width:100%;margin-top:.12rem;line-height:1}.message-title[data-v-3d0e52fa]{font-size:.16rem}.message-time[data-v-3d0e52fa]{color:#999;font-size:.13rem}.circle-icon[data-v-3d0e52fa]{width:.07rem;height:.07rem;background:#f95a28;display:inline-block;border-radius:.07rem;margin-left:.05rem}",""])},577:function(t,e,a){var s=a(489);"string"==typeof s&&(s=[[t.i,s,""]]),s.locals&&(t.exports=s.locals);a(172)("66c6777b",s,!0)},721:function(t,e){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADIBAMAAABfdrOtAAAAMFBMVEUAAADi4uLh4eHj4+Pi4uLq6urp6eno6Ojp6enq6urk5OTp6eng4ODt7e3////39/fvAm6WAAAADHRSTlMAqFW6MfqHZdsZCT2GXv+yAAADWElEQVR42u3av2sTYRjA8ddWcyQ6tKjVQUFE6OCkXUqJ0KEI1g46FDoYiUvSOoUihVaHSrFUsZBBlNoliOjiICKUQpe2Jkb6xlKk0CFPndzM9R8oojZNX0Pu1/s+3oOW57s9Sz6Xu8t7L+QEx3Ecx3Ecx5l1RBpXPNEdHEEwYwSILL4lQGQhR4DILgpkOUeAyCQFskSBFHMEiMxQIOMUyAoFUqBAlikQuX+Q3D+HXDxrlhbSIgxjJEzEajWv7U5ApFlimswSIHIxS4DIJQpEJimQAgUiMxTICgVSpEBkhgIZp0A+UCAFCmSZAilSIJIRRhhhhBFGGGGEEvlqb5W9Rzyyaf+q7DnewCIl+3cV7xGL3LR3KqsjV8euRiTyw95p23m01YhA8na1Su30VPvWMGKQVbvaludYoUC2GKm/tBXvEYfI2v1Tf7N9bxhRyKbj76TcMKKQ1fqT/tl5RCDqYLddxrwaUUhJXVjXEY3IEoD3+N88GRlhhBFGGAkTaaZAIhRIEwUSo0DEKQqkjwKJUCCxxwSI6KNAmigQsaCQA6Eh0TgBIqw9JII3isKlF/FdpAmPLAq3YnOpVOqMEFEpCV6Pi6ORT/7IPTTS4o+8QSPd/kiE4L01tZKZ9kgEaAGJZIMg1l/4ozzk+6uYDYYcxiC3RcBemRvHROBexw1XrVGh05OenS7LPzrX49NzYZQlVR+Fb3gkKULK0npVG488FGFl6ayteGRFqEJDMiK0LI2HBB7pEuFlqbU1tGZqy0oe9uofmOpoTaWH318RyGKz7amjDwDWd5EEODVwITU8nzMConO3Omsfuq6+iFvrE213NU/m7Egn1FJIAnyaSM8HFV6OTEOtOgQCtJHuDUA8OwmqOqQEwbo/6kccB3BDIHDXvZiDgwCuSB40mux2faZPgweSAJ02xpyNpwAeSB40G3IyLPBESqBbxgE57Y2AdmuNRgQ8kRLol2xADnkjCQPkmiaSBwQS9HQlTJCk5oU3MdZEY1bCHSmZIBnNH6OJMaSxrKhQy4oqOgjI1AKptdSr8Eu9YgbR32I00OP3krnwJd1rtJFQITYSzkXfqS2RymtLdD4rjKpu7lTumzv8NvVq+0iqtaNzqj+xd+D9UxPVbeqM4DiO47h91E9DzYA9CrPuMQAAAABJRU5ErkJggg=="},788:function(t,e,a){t.exports={render:function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"page",attrs:{id:"message"}},[s("mt-header",{staticClass:"bar-nav",attrs:{title:"消息"}},[s("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"}),t._v(" "),t.list.length>0?s("mt-button",{nativeOn:{click:function(e){t.readAll(e)}},slot:"right"},[t._v("已读")]):t._e()],1),t._v(" "),s("div",{ref:"wrapper",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[s("mt-loadmore",{ref:"loadmore",attrs:{"top-method":t.loadTop,"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded}},[s("ul",{staticClass:"message-list"},t._l(t.list,function(e){return s("li",{staticClass:"aui-border-b"},[s("router-link",{attrs:{to:"/account/message/detail/?id="+e.uuid}},[s("p",{staticClass:"message-title"},[t._v(t._s(e.title)),0==e.readFlag?s("i",{staticClass:"circle-icon"}):t._e()]),t._v(" "),s("p",{staticClass:"message-time"},[t._v(t._s(t._f("dateFormatFun")(e.createTime,4)))])])],1)})),t._v(" "),s("div",{directives:[{name:"show",rawName:"v-show",value:t.noData,expression:"noData"}],staticClass:"text-center no-data"},[s("img",{attrs:{src:a(721),alt:"缺省图片"}}),t._v(" "),s("p",[t._v("暂无消息")])])])],1)],1)},staticRenderFns:[]}}});