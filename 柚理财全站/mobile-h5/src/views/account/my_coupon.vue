<template>
  <div class="page page-loadmore">
    <mt-header class="bar-nav" title="我的优惠" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <ul class="rd-tab-title">
      <li @click="changeTab(1)">
        <span :class="{current: active == 'tab-con1'}">红包</span>
      </li>
      <li @click="changeTab(2)">
        <span :class="{current: active == 'tab-con2'}">加息券</span>
      </li>
    </ul>
    <div class="text-center no-data" v-show="noData">
      <img v-if="active == 'tab-con1'" src="../../assets/images/public/default/default_icon_no_hb.png" alt="缺省图片">
      <img v-else src="../../assets/images/public/default/default_icon_no_jxq.png" alt="缺省图片">
      <p>暂无记录</p>
    </div>
    <div class="page-tab-container">
      <mt-tab-container class="page-tabbar-tab-container" v-model="active">
        <mt-tab-container-item id="tab-con1">
          <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
            <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
              <ul class="coupon-list">
                <li v-for="item in list">
                  <div class="coupon-list-1">
                    <div class="coupon-left main-color">
                      <i class="symbol font-arial">￥</i><i class="coupon-num">{{item.amount}}</i>
                    </div>
                    <div class="coupon-right">
                      <p class="coupon-name">{{item.ruleName}}</p>
                      <p v-if="item.useExpireTime == ''" class="coupon-limit">长期有效</p>
                      <p v-else class="coupon-limit">到期日期 {{item.useExpireTime | dateFormatFun(4)}}</p>
                      <p class="coupon-desc">使用条件 {{item.ruleRemark}}</p>
                    </div>
                  </div>
                  <div class="coupon-list-2">
                    <div v-if="item.useProjectType == 0">
                      <div class="coupon-use">适用于所有产品</div>
                      <div class="coupon-icon"></div>
                    </div>
                    <div v-else>
                      <div class="coupon-use">点击查看适用产品</div>
                      <div class="coupon-icon" @click.self="showHide">
                        <img src="../../assets/images/me/arrow_down.png" class="icon-show" />
                      </div>
                      <div class="show-remark" hidden>{{item.useProjectTypeName}}</div>
                    </div>
                  </div>
                  <i class="left-icon"></i><i class="right-icon"></i>
                  <img v-if="item.status == 1" src="../../assets/images/money/coupon_used.png" class="coupon-label">
                  <img v-if="item.status == 2" src="../../assets/images/money/coupon_expired.png" class="coupon-label">
                  <img v-if="item.status == 3" src="../../assets/images/money/coupon_invalid.png" class="coupon-label">
                </li>
              </ul>
            </mt-loadmore>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-con2">
          <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
            <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded2" ref="loadmore2">
              <ul class="coupon-list">
                <li v-for="item in list2">
                  <div class="coupon-list-1">
                    <div class="coupon-left main-color">
                      <i class="coupon-num">{{item.upApr}}</i><i class="symbol font-arial">%</i>
                    </div>
                    <div class="coupon-right">
                      <p class="coupon-name">{{item.ruleName}}</p>
                      <p v-if="item.useExpireTime == ''" class="coupon-limit">长期有效</p>
                      <p v-else class="coupon-limit">到期日期 {{item.useExpireTime | dateFormatFun(4)}}</p>
                      <p class="coupon-desc">使用条件 {{item.ruleRemark}}</p>
                    </div>
                  </div>
                  <div class="coupon-list-2">
                    <div v-if="item.useProjectType == 0">
                      <div class="coupon-use">适用于所有产品</div>
                      <div class="coupon-icon"></div>
                    </div>
                    <div v-else>
                      <div class="coupon-use">点击查看适用产品</div>
                      <div class="coupon-icon" @click.self="showHide">
                        <img src="../../assets/images/me/arrow_down.png" class="icon-show" />
                      </div>
                      <div class="show-remark" hidden>{{item.useProjectTypeName}}</div>
                    </div>
                  </div>
                  <i class="left-icon"></i><i class="right-icon"></i>
                  <img v-if="item.status == 1" src="../../assets/images/money/coupon_used.png" class="coupon-label">
                  <img v-if="item.status == 2" src="../../assets/images/money/coupon_expired.png" class="coupon-label">
                  <img v-if="item.status == 3" src="../../assets/images/money/coupon_invalid.png" class="coupon-label">
                </li>
              </ul>
            </mt-loadmore>
          </div>
        </mt-tab-container-item>
      </mt-tab-container>
    </div>
  </div>
</template>
<script type="text/babel">
  import * as ajaxUrl from '../../ajax.config'
  export default {
    methods: {
      changeTab(index=1){
        if(index == 1){
          this.active = 'tab-con1'
          this.list = []
        }else{
          this.active = 'tab-con2'
          this.list2 = []
        }
        this.projectList()
      },
      projectList(page=1){
        let url
        if(this.active == 'tab-con1'){
          url = ajaxUrl.userRedenvelopeList
        }else if(this.active == 'tab-con2'){
          url = ajaxUrl.userRateCouponList
        }
        let urlParams = {
          'page.page': page,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
        }
        this.noData = false;
        this.$http.get(url, { params: urlParams }).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          if(this.active == 'tab-con1'){
            this.list = this.list.concat(res.data.resData.list)
            this.totalPage1 = res.data.resData.totalPage
          }else if(this.active == 'tab-con2'){
            this.list2 = this.list2.concat(res.data.resData.list)
            this.totalPage2 = res.data.resData.totalPage
          }
        })
      },
      loadBottom(id) {
        console.log(id)
        setTimeout(() => {
          if(this.active == 'tab-con1'){
            let page = ++this.page1
            if(page > this.totalPage1){
              this.allLoaded = true;// 若数据已全部获取完毕
            }
            this.projectList(page)
            this.$refs.loadmore.onBottomLoaded(id);
          }else if(this.active == 'tab-con2'){
            let page = ++this.page2
            if(page > this.totalPage2){
              this.allLoaded2 = true;// 若数据已全部获取完毕
            }
            this.projectList(page)
            this.$refs.loadmore2.onBottomLoaded(id);
          }
        }, 1000);
      },
      showHide(evt){
        // console.log(evt)
        if(evt.target.nextElementSibling.hidden){
          evt.target.nextElementSibling.hidden = false
          evt.target.children[0].className = 'icon-show rotate-up'
        }
        else{
          evt.target.nextElementSibling.hidden = true
          evt.target.children[0].className = 'icon-show'
        }
      }
    },
    created() {
      this.projectList()
      this.$nextTick(()=>{
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    data() {
      return {
        active: 'tab-con1',
        wrapperHeight: 0,
        noData: false,
        list: [],
        list2: [],
        allLoaded: false,
        allLoaded2: false,
        page1: 1,
        page2: 1,
        totalPage1:1, //tab1默认总页数为1
        totalPage2:1, //tab2默认总页数为1
      };
    }
  };
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  .rd-tab-title {
    width: 100%;
    height: .4rem;
    background: #fff;
  }
  .rd-tab-title li{
    width: 50%;
    line-height: .36rem;
    text-align: center;
    display: inline-block;
    float: left;
  }
  .current {
    color: $main-color;
    border-bottom: 2px solid $main-color;
  }
  .rd-tab-title li span {
    width: .6rem;
    display: block;
    margin: 0 auto;
  }
  .content { margin: .8rem 0 0;}
  .coupon-list{
    width: 100%;
    padding: 0 5%;
    float: left;
  }
  .coupon-list li{
    float: left;
    width: 100%;
    background: #fff;
    margin-top: .1rem;
    position: relative;
  }
  .coupon-list-1{
    width: 100%;
    height: .88rem;
    padding: .1rem 0;
  }
  .coupon-left{
    float: left;
    display: inline;
    width: 35%;
    height: 100%;
    line-height: .68rem;
    text-align: center;
    border-right: 1px dotted #DDD;
  }
  .symbol{
    font-size: .21rem;
  }
  .symbol-plus{
    font-size: .36rem;
  }
  .coupon-num{
    font-size: .36rem;
    font-family: arial;
  }
  .coupon-right{
    float: left;
    width: 64%;
    height: 100%;
    padding-left: .23rem;
  }
  .coupon-name{
    font-size: .15rem;
    color: #333;
    margin-top:.05rem;
  }
  .coupon-right p{
    line-height: 1;
  }
  .coupon-limit,.coupon-desc{
    font-size: .12rem;
    color: #999;
  }
  .coupon-limit{
    margin-top: .15rem;
  }
  .coupon-desc{
    margin-top: .08rem;
  }
  .coupon-list-2{
    float: left;
    width: 100%;
    background: $page-bg;
  }
  .coupon-use{
    width: 50%;
    height: .37rem;
    line-height: .37rem;
    padding-left: .2rem;
    float: left;
    display: inline;
    border-top: 1px dotted #DDD;
    background: #fff;
  }
  .coupon-icon{
    width: 50%;
    height: .37rem;
    padding-right: .2rem;
    float: left;
    border-top: 1px dotted #DDD;
    background: #fff;
  }
  .icon-show{
    width: .1rem;
    float: right;
    margin-top: .14rem;
    transition: .2s ease-out;
  }
  .rotate-up {transform: rotateZ(-180deg);}
  .left-icon,.right-icon{
    position: absolute;
    height: .2rem;
    width: .2rem;
    border-radius: 100%;
    background: #f8f8f8;
    top: .78rem;
  }
  .left-icon{  left: -0.1rem;  }
  .right-icon{  right: -0.1rem;  }
  .show-remark{
    float: left;
    line-height: .2rem;
    font-size: .12rem;
    color: #999;
    margin: 0 2%;
    width: 96%;
    padding: 0 .05rem;
    background: #fff;
  }
  .coupon-label{
    width: .47rem;
    position: absolute;
    top: 0;
    right: 0;
  }
</style>
