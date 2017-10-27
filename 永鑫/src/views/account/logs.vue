<template>
  <div class="page">
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="record-list">
          <div v-for="item in newList">
            <div class="record-date">{{item.timeF}}</div>
            <li class="aui-border-b" v-for="(item2, index) in item.item" @click="openRemark(`mark${index}`)">
              <span class="padding-l-15 text-left color-333 fz-16">{{item2.funName}}</span>
              <span :class="{
                  'color-999':item2.moneyStr == '',
                  'main-color': item2.moneyStr == '+',
                  'color-green': item2.moneyStr == '-',
                  }" class="padding-r-15 text-right fz-16">{{item2.moneyStr}}{{item2.money | currency('', 2)}}</span>
              <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item2.createTime | dateFormatFun(4)}}</span>
              <span class="padding-r-15 text-right color-999 fz-13 ellipsis">{{item2.accountTypeStr}}</span>
              <div class="remark-box clearfix hide" :ref="`mark${index}`">
                <label class="color-999">备注</label><p>{{item2.remark}}</p>
              </div>
            </li>
          </div>
        </ul>
        <div class="text-center no-data" v-show="noData">
          <img src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
          <p>暂无记录</p>
        </div>
      </mt-loadmore>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  export default {
    created(){
      this.projectList()
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
        setTimeout(() => {
          var els = document.getElementsByClassName('record-date')
          var top_arr = []
          for(var i = 0; i < els.length; i++){
            top_arr.push(els[i].offsetTop)
          }
//          window.addEventListener('scroll', function () {
//            var sy = window.scrollY
//            for(var j = 0; j < top_arr.length; j++){
//              var sy2 = 25*j + sy
//              sy2 >= top_arr[j] ? els[j].classList.add('vux-fixed') : els[j].classList.remove('vux-fixed')
//            }
//          })
      }, 1000)
      })
    },
    methods: {
      projectList( page = 1){
        this.noData = false;
        this.$indicator.open({spinnerType: 'fading-circle'})
        let getParams = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.page': page,
          'page.pageSize': 10
        }
        this.$http.get(ajaxUrl.logsDetail, {params: getParams}).then((res) => {
          this.$indicator.close()
        if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
        if(page > res.data.resData.totalPage){
          if(res.data.resData.totalPage > 0){
            this.$toast('无更多数据加载哦~')
          }
          return ; // 请求页大于总页数就返回不执行
        }
        let rawList = res.data.resData.list
        var index = 0;
        rawList.forEach(val => {
          let y = new Date(val.createTime).getFullYear()
          let m = new Date(val.createTime).getMonth() + 1
          let timeF =  y + '年' + m +'月'
          if(this.obj.hasOwnProperty(timeF)){
          this.newList[this.obj[timeF]].item.push(val)
        }else{
          this.obj[timeF] = index++
          this.newList.push({
            timeF: timeF,
            item: [val]
          })
        }
      })
        // this.list = this.list.concat(this.newList)
        this.totalPage = res.data.resData.totalPage
      })
      },
      loadTop(id){
        setTimeout(() => {
          this.obj = {}
        this.newList = []
        this.allLoaded = false;
        this.page = 1
        this.$refs.loadmore.onTopLoaded(id);
        this.projectList()
      }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          let page = ++this.page
          this.projectList(page)
        this.$refs.loadmore.onBottomLoaded(id);
        if(page > this.totalPage){
          this.allLoaded = true;// 若数据已全部获取完毕
        }
      }, 500);
      },
      openRemark(ref){
        var remark_box = this.$refs[ref][0]
        if(remark_box.classList.contains('hide')){
          remark_box.classList.remove('hide')
        }else{
          remark_box.classList.add('hide')
        }
      },
    },
    data(){
      return {
        list: [],
        noData: false,
        allLoaded: false,
        page: 1,
        totalPage:1,
        obj: {},
        newList: [],
        wrapperHeight: 0
      }
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  .vux-sticky {
    width: 100%;
    position: sticky;
    top: 0;
  }
  .vux-fixed {
    width: 100%;
    position: fixed;
    top: 0;
  }
  .record-title{
    float: left;
    width: 100%;
    padding: 0 .15rem;
  }
  .record-title li{
    float: left;
    width: 50%;
    height: .45rem;
    line-height: .45rem;
    color: #666;
  }
  .text-left{  text-align: left;  }
  .text-right{  text-align: right;  }
  .record-list li{
    background: #fff;
  }
  .record-list li span{
    width: 49%;
    display: inline-block;
    line-height: 1;
    padding-top: .14rem;
  }
  .rd-tab-title li{
    width: 50%;
  }
  .record-date{
    line-height: .3rem;
    color: #999;
    padding: 0 5%;
    background:#f8f8f8;
  }
  .remark-box {
    padding: 0 .15rem .1rem;
  }
  .remark-box p {
    padding-top:.1rem;
    width: 87%;
    display: inline-block;
    color: #666;
  }
  .remark-box label {
    width: 12%;
    display: inline-block;
    vertical-align: top;
    padding-top: .1rem;
  }
</style>
