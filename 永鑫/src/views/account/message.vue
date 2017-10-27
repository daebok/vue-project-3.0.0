<template>
  <div class="page" id="message">
    <p class="read-all" v-if="list.length > 0" @click="readAll">全部标为已读</p>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="message-list">
            <li class="aui-border-b" v-for="item in list" @click="showHide(item.uuid)" :ref="item.uuid">
              <p class="ellipsis message-title">
                {{ item.title }}<i v-if="item.readFlag == 0" class="circle-icon"></i>
                <img src="./../../assets/images/public/arrow_right.png" class="right-img">
              </p>
              <p class="message-time">{{item.createTime | dateFormatFun(4)}}</p>
              <div class="con" hidden>
                <div v-html="item.content"></div>
              </div>
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
        wrapperHeight: 0,
        page: 1,
        totalPage: 1
      }
    },
    created(){
      this.projectList()
      this.$nextTick(() => {
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
      showHide(uuid){
        let target = this.$refs[uuid][0]
        let con = target.querySelector('.con')
        let arrow = target.querySelector('.right-img')
        let dot = target.querySelector('.circle-icon')
        if (arrow.classList.contains('rotate-up')) {
          con.hidden = true;
          arrow.classList.remove('rotate-up')
        } else {
          con.hidden = false;
          arrow.classList.add('rotate-up')
        }
        let getParams = {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          id: uuid,
        }
        this.$http.get(ajaxUrl.letterInfo, {params: getParams}).then((res) => {
          dot.style.display = 'none'
        })
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
<style lang="scss" rel="stylesheet/scss" scoped>
  .read-all {color:#666;margin:.1rem .15rem;text-align:right;}
  .message-list{
    background: #fff;
    li {
      padding: .15rem; font-size: .16rem;
      &:last-child:after {border-bottom: 0;}
      .message-title{
        font-size: .16rem;
        .right-img{
          float: right; width: .12rem; margin-top: 4px; transform: rotateZ(90deg); transition: .2s ease-out;
          &.rotate-up {transform: rotateZ(-90deg);}
        }
      }
      .message-time{
        color: #999;
        font-size: .13rem;
      }
      .con {transition: all .5s;color:#666;padding-top: .1rem; font-size: .13rem; overflow:hidden;}
    }

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
