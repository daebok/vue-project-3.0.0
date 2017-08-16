<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .wrapper-scroll {
    height: .44rem;
    position:relative;
  }
  #wrapper2 {
    position: absolute;
    z-index: 1;
    top: 0;
    left: 0;
    width: 100%;
    background: #ccc;
    overflow: hidden;
    height: 100%;
  }
  #scroller {
    position: absolute;
    z-index: 1;
    min-width: 400px;
    height: 100%;
    background-color: #fff;
    text-size-adjust: none;
    padding-left: .15rem;
  }
  .aui-flex-item-3 {
    &.current p {
      color: $main-color;border-bottom: 2px solid $main-color;
      span { color: $main-color; }
    }
    p {
      color: #333;
      line-height:.2rem;
      padding: .12rem .05rem .1rem;
      margin: 0 .28rem 0 0;
    }
  }
  .bg-fff { background: #fff; }
  .select-area {
    background: #fff;margin-top: .1rem; height: .5rem; display: table; text-align: center;width: 100%;position: relative;
    li {
      display: table-cell;color: #666;font-size: .15rem; vertical-align: middle;
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
  .product {
    ul {
      padding: .22rem .15rem; font-size: 0;
      li {
        display: inline-block; width: 50%;padding-right: .12rem;padding-bottom: .05rem; margin-bottom: .05rem; vertical-align: top; line-height: .24rem;
        &:nth-child(2n) { margin-right: 0; }
        &:nth-last-child(2):after { border: 0; }
        /*当商品数为偶数时候倒数第二个下边框去掉*/
        &.even:nth-last-child(3):after { border: 0; }
        .pro-bg { position: relative; }
        img {
          width: 1.665rem; height: 1.11rem; display: block;
          &.sellout { position: absolute; width: .62rem; height: .62rem; top: 50%; left: 50%; margin-top: -.31rem; margin-left: -.31rem; }
        }
        p { color: #333; font-size: .12rem; padding-left: .05rem;}
        .pro-info {
          font-size: .12rem; color: #999; padding-left: .05rem;
          i { font-size: .14rem; }
          .right { float: right; font-size: .11rem; }
        }
      }
    }
  }
</style>
<template>
<div class="page">
  <div class="wrapper-scroll">
    <div id="wrapper2">
      <div id="scroller" class="aui-border-b">
        <div @click="tabChange('', 1)" :class="{current: active == 'tab-1'}" class="menu-item ellipsis aui-flex-item-3" id="menuItem1">
            <p>全部商品</p>
        </div>
        <div v-for="(item, index) in goodsCategoryList" @click="tabChange(item.id, index+2)" :id="`menuItem${index+2}`" :class="{current: active == 'tab-'+(index+2)}"  class="menu-item ellipsis aui-flex-item-3">
            <p>{{item.goodsCategoryName}}</p>
        </div>
      </div>
    </div>
  </div>
  <ul class="select-area aui-border-b">
    <li @click="showLayer('order', $event)" id="orderLi" class="aui-border-r">综合排序 <img src="../../assets/images/shop/arrow_down.png"></li>
    <li @click="showLayer('score', $event)" id="scoreLi">积分范围 <img src="../../assets/images/shop/arrow_down.png"></li>
  </ul>
  <div class="layer hide" @click="cancelLayer">
    <div class="order-area" id="orderArea">
      <ul class="order" ref="orderRange">
        <li @click.stop="orderBy('','order')" class="aui-border-b" :class="{'main-color': orderName == ''}">默认排序 <img src="../../assets/images/shop/check.png" v-show="orderName == ''"></li>
        <li @click="orderBy('score','order')" class="aui-border-b" :class="{'main-color': orderName == 'score'}">兑换积分从高到底 <img src="../../assets/images/shop/check.png" v-show="orderName == 'score'"></li>
        <li @click="orderBy('lessNum','order')" class="aui-border-b" :class="{'main-color': orderName == 'lessNum'}">剩余数量从高到低 <img src="../../assets/images/shop/check.png" v-show="orderName == 'lessNum'"></li>
      </ul>
    </div>
    <div class="score-area" id="scoreArea">
      <ul class="score-range" ref="scoreRange">
        <li @click="orderBy('','range')" class="aui-border-b" :class="{'main-color': rangeName == ''}">全部 <img src="../../assets/images/shop/check.png" v-show="rangeName == ''"></li>
        <li @click="orderBy('01','range')" class="aui-border-b" :class="{'main-color': rangeName == '01'}">1000以下 <img src="../../assets/images/shop/check.png" v-show="rangeName == '01'"></li>
        <li @click="orderBy('02','range')" class="aui-border-b" :class="{'main-color': rangeName == '02'}">1000-4999 <img src="../../assets/images/shop/check.png" v-show="rangeName == '02'"></li>
        <li @click="orderBy('03','range')" class="aui-border-b" :class="{'main-color': rangeName == '03'}">5000-9999 <img src="../../assets/images/shop/check.png" v-show="rangeName == '03'"></li>
        <li @click="orderBy('04','range')" class="aui-border-b" :class="{'main-color': rangeName == '04'}">10000以上 <img src="../../assets/images/shop/check.png" v-show="rangeName == '04'"></li>
        <div class="custom">
          <div class="padding-l-15">
            <p>自定义范围</p>
            <div class="input">
              <input type="tel" placeholder="0" v-model="scoreMin">
              <span class="spilt">-</span>
              <input type="tel" placeholder="10000" v-model="scoreMax"> 积分
            </div>
          </div>
          <div class="btn aui-border-t">
            <span class="reset" @click="reset">重置</span>
            <span class="confirm" @click="confirm">确定</span>
          </div>
        </div>
      </ul>
    </div>
  </div>
  <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
    <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
      <div class="product bg-fff">
        <ul>
          <li class="aui-border-b" :class="{'even': bidList.length % 2 == 0}" v-for="item in bidList">
            <router-link :to="`/shop/detail/?id=${item.id}&from=list`">
              <div class="pro-bg">
                <img :src="assetsDomain + item.picSmall">
                <img v-if="item.lessNum == 0" class="sellout" src="../../assets/images/shop/sellOut.png">
              </div>
              <p class="ellipsis">{{ item.goodsName }}</p>
              <div class="pro-info"><span><i class="main-color">{{ item.score | currency('',0) }}</i> 积分</span><span class="right">数量 {{ item.lessNum }}</span></div>
            </router-link>
          </li>
          <div class="text-center no-data" style="padding: 0;" v-show="noData">
            <img class="invest-default-nodata" src="../../assets/images/public/default/nodata_trilateral.png" alt="缺省图片">
            <p v-text="nodataTxt"></p>
          </div>
        </ul>
      </div>
    </mt-loadmore>
  </div>
</div>
</template>
<script>
  import * as ajaxUrl from '../../ajax.config.js';
  import iScroll from 'iscroll/build/iscroll'
  export default {
    name: 'shopList',
    data() {
      return {
        list: [], // 首页banner图列表数组
        menuScroll: '',
        layerChange: '', // 点击选择排序
        active: 'tab-1',
        bidList: [],
        goodsCategoryList: [],
        assetsDomain: ajaxUrl.StaticsServer,
        allLoaded: false,
        noData: false,
        wrapperHeight: 0,
        page: 1,
        totalPage:1,
        orderName: '', // 综合排序的定位
        rangeName: '', // 积分范围的定位
        scoreMin: '',
        scoreMax: '',
        filterParams: {
          queryScoreType: '',
          'page.sort': '',
          'page.order': 'asc' // 列表倒序
        },
        nodataTxt: '商品准备中，先去看看其他的吧~'
      }
    },
    created() {
      this.scrollWidth = 578 // 先默认宽度
      let indexN = this.$route.query.order || 1;
      this.active = 'tab-' + indexN;
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.goodsCategoryList).then((res) => { // 商品类别
        this.goodsCategoryList = res.data.resData
        this.$indicator.close()
      })
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - 104;
        let uuid = this.$route.query.uuid || ''
        this.projectList(uuid)
      })
    },
    mounted(){
      document.querySelector('#scroller').style.width = this.scrollWidth + 'px'
      this.menuScroll = new iScroll('#wrapper2',{
        scrollY: false,
        scrollX: true,
        mouseWheel: false,
        bounce: false
      })
      let flag = true
      this.menuScroll.on('beforeScrollStart', () => {
        if(flag){ // 执行一次计算宽度
          flag = false
          let swipe_item = document.querySelectorAll('.menu-item')
          let total_w = document.querySelector('.menu-item').clientWidth // 先默认宽度为第一个选项的宽度
          swipe_item.forEach( val => {
            total_w += val.clientWidth
          })
          this.scrollWidth = total_w
          document.querySelector('#scroller').style.width = total_w + 'px'
          setTimeout(() => {
            this.menuScroll.refresh()
          }, 10)
        }
      })
      document.getElementById('wrapper2').addEventListener('touchmove', function (e) {
        e.preventDefault()
      },false)

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
      },
      goodsCategoryList() {
        // 初始化指定iscroll位置
        if(this.$route.query.order) {
          let index = this.$route.query.order
          setTimeout(() => {
            let el = document.querySelector(`#menuItem${index}`)
            this.menuScroll && this.menuScroll.scrollToElement(el)
            this.menuScroll && this.menuScroll.refresh()
          }, 10)
        }
      }
    },
    methods: {
      tabChange(uuid, index){
        this.active = 'tab-' + index;
        this.allLoaded = false; //切换时候重新启用底部加载
        this.bidList = [];
        this.filterParams.queryScoreType = ''
        this.filterParams['page.sort'] = ''
        //积分和综合排序统一改“全部”
        this.rangeName = this.orderName = ''
        this.projectList(uuid, 1, this.filterParams)
        this.$router.push({query: {
          order: index,
          uuid: uuid
        }})
      },
      projectList(uuid = '', page = 1, arg){
        this.noData = false;
        let params = {
          'page.page': page,
          'page.pageSize': 15,
          goodsCategoryId: uuid
        }
        Object.assign(params, arg)
        if(arg && (arg.queryScoreType || arg['page.sort']) ) {
          this.nodataTxt = '没有搜索结果'
        } else {
          this.nodataTxt = '商品准备中，先去看看其他的吧~'
        }
        this.$http.get(ajaxUrl.getGoodsList, {params: params}).then((res) => {
          if(res.data.resData.list.length == 0 && this.bidList.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0) {
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          this.bidList = this.bidList.concat(res.data.resData.list)
          this.totalPage = res.data.resData.totalPage
        })
      },
      loadTop(id) {
        setTimeout(() => {
          this.bidList = [];
          this.page = 1;
          this.allLoaded = false;
          let uuid = this.$route.query.uuid || sessionStorage.init_invest_list_uuid
          this.projectList(uuid, this.page, this.filterParams)
          this.$refs.loadmore.onTopLoaded(id)
        }, 1000);
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          let uuid = this.$route.query.uuid || sessionStorage.init_invest_list_uuid
          this.projectList(uuid, this.page, this.filterParams)
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500);
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
        if(type == 'order'){ // 综合排序
          this.orderName = name
          this.filterParams['page.sort'] = name
          this.filterParams['page.order'] = 'desc' // 从高到低需要设置倒序排列
          if(!name){ this.filterParams['page.order'] = 'asc' } //默认排序恢复到正向排序
        }
        if(type == 'range'){ // 按积分范围排序
          this.rangeName = name
          this.filterParams['page.sort'] = 'score'
          this.filterParams['queryScoreType'] = name
        }
        let uuid = this.$route.query.uuid || ''
        this.bidList = [] //先清空列表数组
        this.projectList(uuid, 1, this.filterParams)
      },
      reset () {
        this.cancelLayer()
        this.scoreMin = ''
        this.scoreMax = ''
        this.rangeName = '' // 积分范围重置默认显示全部
        this.filterParams['page.sort'] = 'score'
        this.filterParams['queryScoreType'] = ''
        let uuid = this.$route.query.uuid || ''
        this.bidList = [] //先清空列表数组
        var cusObj = {
          scoreMin: 0,
          scoreMax: 0
        }
        Object.assign(cusObj, this.filterParams)
        this.projectList(uuid, 1, cusObj)
      },
      confirm () {
        var pattern = /^[0-9]*$/;
        var minFlag = pattern.test(this.scoreMin)
        var maxFlag = pattern.test(this.scoreMax)
        if(this.scoreMin == '' || this.scoreMax == ''){
          this.$toast('请输入自定义范围')
        }
        else if(!(minFlag && maxFlag)) {
          this.$toast('请输入正确的自定义范围')
        }
        else if(this.scoreMin - this.scoreMax >= 0) {
          this.$toast('请输入正确的自定义范围')
        }
        else{
          this.cancelLayer()
          this.filterParams['page.sort'] = 'score'
          this.filterParams['queryScoreType'] = ''
          let uuid = this.$route.query.uuid || ''
          this.bidList = [] //先清空列表数组
          var cusObj = {
            scoreMin: this.scoreMin,
            scoreMax: this.scoreMax
          }
          Object.assign(cusObj, this.filterParams)
          this.projectList(uuid, 1, cusObj)
        }
      }
    }
  }
</script>
