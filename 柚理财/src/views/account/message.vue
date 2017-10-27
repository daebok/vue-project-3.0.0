<template>
  <div class="page" id="message">
    <mt-header class="bar-nav" title="消息">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
      <mt-button v-if="list.length > 0" slot="right" @click.native="readAll">已读</mt-button>
    </mt-header>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="message-list">
            <li class="aui-border-b" v-for="item in list">
              <router-link :to="'/account/message/detail/?id='+item.uuid">
              <p class="message-title">{{ item.title }}<i v-if="item.readFlag == 0" class="circle-icon"></i></p>
              <p class="message-time">{{item.createTime | dateFormatFun(4)}}</p>
              </router-link>
            </li>

        </ul>
        <div class="text-center no-data" v-show="noData">
          <img src="../../assets/images/public/default/default_icon_no_message.png" alt="缺省图片">
          <p>暂无消息</p>
        </div>
      </mt-loadmore>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config'
  export default {
    data(){
      return {
        list: [],
        noData: false,
        allLoaded: false,
        page: 1,
        totalPage: 1,
        wrapperHeight: 0
      }
    },
    created(){
      this.projectList()
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      projectList( page = 1){
        this.noData = false;
        this.$indicator.open({spinnerType: 'fading-circle'})
        let getParams = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          'page.pageSize': 10,
          'page.page': page
        }
        this.$http.get(ajaxUrl.messageList, {params: getParams}).then((res) => {
          this.$indicator.close()
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){this.allLoaded = true;}
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          this.list = this.list.concat(res.data.resData.list)
          this.totalPage = res.data.resData.totalPage
        })
      },
      loadTop(id){
        setTimeout(() => {
          this.list = []
          this.page = 1
          this.allLoaded = false;
          this.projectList()
          this.$refs.loadmore.onTopLoaded(id);
        }, 1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          this.projectList(this.page)
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      },
      readAll(){ //全部标记已读
        this.$messagebox({
          title: ' ',
          showCancelButton: true,
          message: '将全部消息标记为已读？'
        }).then(action => {
          if(action == 'confirm'){
            let getParams = {
              userId: this.$store.state.user.userId,
              __sid: this.$store.state.user.__sid,
              batchType:2
            }
            this.$http.get(ajaxUrl.batchSet, {params: getParams}).then((res) => {
              this.list = []
              this.projectList()
            })
          }
        });

      }
    }
  }
</script>
<style scoped>
  .message,.message-list{
    width: 100%;
  }
  .message-list{
    padding-left:5%;
    margin-top: .1rem;
    background: #fff;
  }
  .message-list li{
    height: .65rem;
  }
  .message-list li:last-child:after {border-bottom: 0;}
  .message-list li p{
    float: left;
    width: 100%;
    margin-top: .12rem;
    line-height: 1;
  }
  .message-title{
    font-size: .16rem;
  }
  .message-time{
    color: #999;
    font-size: .13rem;
  }
  .circle-icon{
    width: .07rem;
    height: .07rem;
    background: #f95a28;
    display: inline-block;
    border-radius: .07rem;
    margin-left: .05rem;
  }
</style>
