webpackJsonp([70],{232:function(t,e,a){a(563);var i=a(12)(a(442),a(775),null,null);t.exports=i.exports},442:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a(62);e.default={data:function(){return{list:[],noData:!1,allLoaded:!1,page:1,totalPage:1,wrapperHeight:0}},created:function(){var t=this;this.projectList(),this.$nextTick(function(){t.wrapperHeight=document.documentElement.clientHeight-t.$refs.wrapper.getBoundingClientRect().top})},methods:{projectList:function(){var t=this,e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:1;this.noData=!1,this.$indicator.open({spinnerType:"fading-circle"});var a={sectionCode:"notice","page.page":e};this.$http.get(i.getArticleList,{params:a}).then(function(a){if(t.$indicator.close(),0===a.data.resData.list.length&&(t.noData=!0),e>a.data.resData.totalPage)return void(a.data.resData.totalPage>0&&t.$toast("无更多数据加载哦~"));a.data.resData.totalPage<=1&&(t.allLoaded=!0),t.list=t.list.concat(a.data.resData.list)})},loadBottom:function(t){var e=this;setTimeout(function(){e.page++,e.projectList(e.page),e.$refs.loadmore.onBottomLoaded(t),e.page>e.totalPage&&(e.allLoaded=!0)},500)}}}},475:function(t,e,a){e=t.exports=a(31)(!1),e.push([t.i,".mint-cell{min-height:.55rem}.mint-cell-text{font-size:.14rem}.mint-cell-label{font-size:.12rem}",""])},563:function(t,e,a){var i=a(475);"string"==typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);a(175)("1638edac",i,!0)},725:function(t,e){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAOVBMVEUAAADl5eXp6enq6urg4ODq6urk5OTh4eHi4uLi4uLn5+fr6+vm5ubt7e3g4ODt7e3////k5OT4+PhtVVbGAAAADnRSTlMAc9Ja6v6FqiHAO5YR45A/x5gAAATTSURBVHja7NfBDoMgEEXRqVIUNI/i/39sYy1l03SFZca8s3TFzUQmyNnmxcEts1gXcYhi24piFdM8Ci+mBRRBTBtQDGIaQ7RhiArx9oGqfjSzHD2+srdSGKINQ7RhiDYM0eYyIURk2zr6Kfzb0vz1EgM6cWPb3dBRWKWVCV25+Qrz2A1tZhLRnZcWAvprMZIZCtwahIxQIFzhV985+3fvG0MYcmAIQxjyG0MYwpAT5ZxS2rZHQmErJOf76/yFtZDj/DWgyih0hzy5r2NUiWEgBqCVW83O3P+wv4sh4lt4WRjZqkKKkIcda/J6/3Mh9RHBE2+IkhSemENQt0AQt0CWaxJ44g9B3ALJ/yUDTw6AAKFrBEdAMnSNHAEB4hYIQp2+p0CyLoHUJStSskbOgNTnDkjpPkQHJOIrByd7ITH3xJaDg1ZI0O5W564nJOg73XKMmNfVAOGGjq8cCYQFJOjs3HUAYz6hDRLUAvsOYLRDgieMfceUjC5I0az0jWNKsglSasbQjilpguj5lR3c4m8JGiDTISTSMSXVACEHSbSDJC2Q6WCJdlRC5fcQ7WCJdphAaF4iiXCYQN4ObkbhMIGwAxgkWThMIOxYSthhAmHHUjLIYQJhx1LCDhMIz0tLCd/ygCQ7VhK+YQIRDpa8HSYQdlByWfgmEOFQkoIJRDiUpGACEQ4lKZhAhENJCiYQ8V+kJAUTiHAoScEEsj33JbktINrBEnb0Q/7au7fdSGEYAMNOAiZHW8v7P+zSrlYI6HTikhlMlU+9qkDibwiIqAJpx3pxkHe0D5F3HEuOHZeEyDuOJcftrwuZxR1ribDjtSE0izvWEmHHa0NolnasJdKOl4asJTOJ6QqhWdqhNYRmaYfWEJqlHVpDiElIa0iFHtJDesj3NIYk0gDOM6SAhUdu9TYRIoQGBrpehIdu9RqOAN+40xtePDThB7qWA/gVJQ6ayUiXCR5a8ibQBYYUob3s3+zmX2TouluJOF0NI5zmLWlgPZyTB9JhyHB0t7e2fcL7P4n8M8AppEcfEUULKB8SfOFmL8385H/DCkqTtYeMYahBe5Vb1QiY4W0MbWHVfcmAOkPVOe0P1yJtIm2FqoUlhZ8XDLTlqtb69H3Lzlffh2lH2bcrkbawckt13/CgHS8YO00cbU2i2aSI5FoUFU932d1h0DvdZfdro3a6Z9rJks0Vfam2bqqvJq3TPdCWXZRS+AvLr621yqa7j85gCrbQDj9DO2OxIaFx0cMb+egwhTLyf39o6w8/ULPHWJakFxflaJYAPhAMiGCXJcjEDI0tCZYfGMUDchySkR+xyUVowrtUZCc8PzfKRrEk5+GM7KbxxDE1rR8nl+FHIhZueJbIz8ejghGEsilch3ZeuxsXkyUZiXfEV1L5ftVSdYph/nEI16IdFjBQZeIj+bkun1sCE1RAXpw/HPmfQALhOZYZNz9v2xWeG/kGakKQbwChQmL1ElRxrNvooFJWfXqh6N5uLKtkTQYhj+paLHr4keySmotxSS6ffbC6fGRsch6aiFfVjOujbjs5OgyF36QEdDGDhLzHpGBHfonRhmQEBY0WuJakKdjCJxUbpmSci9cvzGcfP7IMYkoh7NZM9yumNoSUEM3Hocf+r71d13Vd13Vd13VK/AVZVDNK1VRvHgAAAABJRU5ErkJggg=="},775:function(t,e,a){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"page",attrs:{id:"siteIntro"}},[i("mt-header",{staticClass:"bar-nav margin-b-10",attrs:{title:"平台公告"}},[i("mt-button",{directives:[{name:"back-link",rawName:"v-back-link"}],attrs:{icon:"back"},slot:"left"})],1),t._v(" "),i("div",{ref:"wrapper",staticClass:"page-loadmore-wrapper",style:{height:t.wrapperHeight+"px"}},[i("mt-loadmore",{ref:"loadmore",attrs:{"bottom-method":t.loadBottom,"bottom-all-loaded":t.allLoaded}},t._l(t.list,function(e){return i("mt-cell",{key:e.id,attrs:{title:t._f("hideBorrowName")(e.title,18),label:t._f("hideBorrowName")(e.remark,20),to:"/mine/siteIntro/detail/"+e.uuid,"is-link":""}})})),t._v(" "),i("div",{directives:[{name:"show",rawName:"v-show",value:t.noData,expression:"noData"}],staticClass:"text-center no-data"},[i("img",{attrs:{src:a(725),alt:"缺省图片"}}),t._v(" "),i("p",[t._v("暂无公告")])])],1)],1)},staticRenderFns:[]}}});