<template>
  <div>
    <mt-header class="bar-nav aui-border-b" title="积分记录">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="select-area aui-border-b">
      <li @click="showLayer('score', $event)" id="scoreLi" class="aui-border-r">日期范围 <img src="../../assets/images/shop/arrow_down.png"></li>
      <li @click="showLayer('order', $event)" id="orderLi">记录类型 <img src="../../assets/images/shop/arrow_down.png"></li>
    </ul>
    <div class="layer hide" @click="cancelLayer">
      <div class="score-area" id="scoreArea">
        <ul class="score-range" ref="scoreRange">
          <li @click="orderBy('','range')" class="aui-border-b" :class="{'main-color': rangeName == ''}">全部 <img src="../../assets/images/shop/check.png" v-show="rangeName == ''"></li>
          <li @click="orderBy('01','range')" class="aui-border-b" :class="{'main-color': rangeName == '01'}">近7天 <img src="../../assets/images/shop/check.png" v-show="rangeName == '01'"></li>
          <li @click="orderBy('02','range')" class="aui-border-b" :class="{'main-color': rangeName == '02'}">近1个月 <img src="../../assets/images/shop/check.png" v-show="rangeName == '02'"></li>
          <li @click="orderBy('03','range')" class="aui-border-b" :class="{'main-color': rangeName == '03'}">近3个月 <img src="../../assets/images/shop/check.png" v-show="rangeName == '03'"></li>
          <li @click="orderBy('04','range')" class="aui-border-b" :class="{'main-color': rangeName == '04'}">近1年 <img src="../../assets/images/shop/check.png" v-show="rangeName == '04'"></li>
          <!--<div class="custom">-->
            <!--<div class="padding-l-15">-->
              <!--<p>自定义范围</p>-->
              <!--<div class="input">-->
                <!--<input type="tel" placeholder="开始日期">-->
                <!--<span class="spilt">-</span>-->
                <!--<input type="tel" placeholder="结束日期">-->
              <!--</div>-->
            <!--</div>-->
            <!--<div class="btn aui-border-t">-->
              <!--<span class="reset">重置</span>-->
              <!--<span class="confirm">确定</span>-->
            <!--</div>-->
          <!--</div>-->
        </ul>
      </div>
      <div class="order-area" id="orderArea">
        <ul class="order" ref="orderRange">
          <li @click="orderBy('','order')" class="aui-border-b" :class="{'main-color': orderName == ''}">全部 <img src="../../assets/images/shop/check.png" v-show="orderName == ''"></li>
          <li @click="orderBy('1','order')" class="aui-border-b" :class="{'main-color': orderName == '1'}">获取记录 <img src="../../assets/images/shop/check.png" v-show="orderName == '1'"></li>
          <li @click="orderBy('0','order')" class="aui-border-b" :class="{'main-color': orderName == '0'}">使用记录 <img src="../../assets/images/shop/check.png" v-show="orderName == '0'"></li>
        </ul>
      </div>
    </div>
    <div class="log-list">
      <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
        <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
          <ul>
            <li v-for="item in list" class="aui-border-b">
              <div class="cell">
                <div class="name">{{item.typeName}}</div>
                <p >{{item.remark}}</p>
              </div>
              <div class="cell text-right">
                <div v-if="item.optType == 1" class="num main-color">+{{item.optValue}}</div>
                <div v-else="item.optType" class="num color-green">-{{item.optValue}}</div>
                <p >{{item.createTime | dateFormatFun(4)}}</p>
              </div>
            </li>
            <div class="text-center no-data" v-show="noData">
              <img class="invest-default-nodata" src="../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
              <p>暂无记录~</p>
            </div>
          </ul>
        </mt-loadmore>
      </div>
    </div>

  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import Calendar from '../../components/Calendar.vue'
  export default {
    components: { Calendar },
    data () {
      return {
        layerChange: '', // 点击选择排序
        orderName: '', // 综合排序的定位
        rangeName: '', // 积分范围的定位
        disabled: [],
        value: '',
        format: 'yyyy-MM-dd',
        placeholder: '开始日期',
        list: [],
        allLoaded: false,
        noData: false,
        wrapperHeight: 0,
        page: 1,
        totalPage:1,
        filterParams: {
          dateType: '',
          optType: ''
        },
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      }
    },
    created() {
      this.projectList()
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    watch: {
      layerChange(newV, oldV){
        if(!document.querySelector('.layer').classList.contains('hide')){
          if(oldV){
            document.querySelector(`#${oldV}Area`).style.height = '0px'
            document.querySelector(`#${oldV}Li`).classList.remove('show')
            document.querySelector(`#${oldV}Li`).children[0].className = '' // 三角形恢复
          }
        }
      }
    },
    methods: {
      projectList( page = 1, arg){
        this.noData = false;
        let params = {
          'page.page': page,
          'page.pageSize': 15,
        }
        Object.assign(params, this.getParams, arg)
        this.$http.get(ajaxUrl.getScoreLogs, {params: params}).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0) {
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          this.list = this.list.concat(res.data.resData.list)
          this.totalPage = res.data.resData.totalPage
        })
      },
      showLayer(arg,$evt) {
        this.layerChange = arg
        document.querySelector('.layer').className = 'layer'
        if($evt.target.classList.contains('show')){
          $evt.target.classList.remove('show')
          $evt.target.children[0].className = ''
          document.querySelector(`#${arg}Area`).style.height = '0px'
          setTimeout(() => {
            document.querySelector('.layer').classList.add('hide')
          }, 350)
        } else {
          $evt.target.classList.add('show')
          $evt.target.children[0].className = 'rotate-up'
          document.querySelector(`#${arg}Area`).style.height = this.$refs[`${arg}Range`].offsetHeight + 'px'
        }
      },
      cancelLayer(evt) {
        if(evt && evt.target && !evt.target.classList.contains('layer')) { // vue版本没升级前先这样判断点击对象
          return ;
        }
        document.querySelector('.layer').className = 'layer hide'
        document.querySelector(`#${this.layerChange}Area`).style.height = '0px'
        document.querySelector(`#${this.layerChange}Li`).classList.remove('show')
        document.querySelector(`#${this.layerChange}Li`).children[0].className = '' // 三角形恢复
      },
      orderBy(name, type) { // 排序类型
        this.cancelLayer()
        if(type == 'order'){ // 按记录排序
          this.orderName = name
          this.filterParams.optType = name
        }
        if(type == 'range'){ // 按日期范围筛选
          this.rangeName = name
          this.filterParams.dateType = name
        }
        this.list = []
        this.projectList(1, this.filterParams)
      },
      loadTop(id) {
        setTimeout(() => {
          this.list = [];
          this.page = 1;
          this.allLoaded = false;
          this.projectList(this.page, this.filterParams)
          this.$refs.loadmore.onTopLoaded(id)
        }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          this.projectList(this.page, this.filterParams)
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
      },
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss">
  @import "../../assets/scss/var";
  .select-area {
    background: #fff; height: .5rem; display: table; text-align: center;width: 100%;position: relative;
    li {
      display: table-cell;color: #666;font-size: .16rem; vertical-align: middle;
      img {
        width: .07rem;
        transform: rotateZ(0deg);
        transition: .2s ease-out;
        &.rotate-up { transform: rotateZ(180deg); }
      }
    }
  }
  .layer { position: absolute; background: rgba(0,0,0,.2); width: 100%; height: 100%;
    z-index: 2;
    .order-area,.score-area { height: 0; transition: height .5s; overflow: hidden; }
    ul {
      li { background: #fff;  line-height: .44rem;color: #666;padding: 0 .15rem;
        img { float: right; width: .1rem; margin-top: .17rem;}
      }
    }
    .custom {
      background: #fff; color: #999;
      p {  padding: .15rem 0 .1rem; font-size: .12rem; }
      input { border: 1px solid #ddd; border-radius: .03rem; height: .35rem;padding-left: .1rem; width: 1.2rem; }
      .spilt { margin: 0 .07rem; }
      .btn {
        font-size: 0; margin-top: .16rem;
        span { line-height: .5rem; text-align: center; width: 50%;display: inline-block;font-size: .15rem; color: #666;
          &.confirm { background: $main-color; color: #fff; }
        }
      }
    }
  }
  .log-list {
    margin-top: .15rem;
    ul {
      li {
        display: table; height: .75rem; width: 100%; background: #fff; padding: 0 .15rem;
        .cell {
          display: table-cell;
          width: 50%; vertical-align: middle;
          &.text-right { text-align: right; }
          .name { font-size: .16rem; color: #333; }
          p { font-size: .12rem; color: #999; }
        }
      }
    }
  }
</style>