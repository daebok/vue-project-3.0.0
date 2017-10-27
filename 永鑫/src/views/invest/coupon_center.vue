<template>
  <div class="page page-invest">
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="page-infinite-list coupon-wrapper">
          <div v-for="item in couponList">
            <li :class="item.lssueNum == item.totalNum ? 'disabled' : ''">
              <section class="info-left">
                <span><strong>{{ item.amount }}</strong> 元</span>
                <span>满{{ item.useTenderMoney}}元可用</span>
              </section>

              <section class="info-center">
                <span>{{ item.ruleName }}</span>
                <span class="use-info">适用:{{ item.useProjectTypeName | hideBorrowName(15) }}</span>
              </section>

              <section class="info-right">
                <div v-if="item.lssueNum == item.totalNum" class="loot-all">已<br>抢<br>光</div>
                <div v-else-if="item.collect == 0" class="receive" @click="toReceive(item.ruleId)">立<br>即<br>领<br>取</div>
                <img v-else-if="item.collect == 1" class="have-received" src="../../assets/images/coupon/lingquan_ylq.png" alt="已领取图片">
              </section>
            </li>
          </div>

          <!-- 无数据 -->
          <div class="text-center no-data" v-show="noData">
            <img class="invest-default-nodata" src="../../assets/images/public/default/nodata_trilateral.png" alt="无数据图片">
            <p>优惠券准备中，敬请期待哦~</p>
          </div>
        </ul>
      </mt-loadmore>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config'; // 引入所有接口地址
  export default {
    data() {
      return {
        wrapperHeight: 0, // 产品列表高度
        allLoaded: false, // 是否全部数据加载完毕，true已全部加载完毕，false否
        noData: false, // 是否有无产品，true无产品信息，false有
        openId: this.$route.query.openId,//微信openId
        couponList: [],
        page: 1,
        totalPage: 1
      }
    },
    created() {
      //console.log(this.openId);
      // 理财列表数据初始化
      this.couponRequest();
      // 计算产品列表高度
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      });
    },
    methods: {
      //接口请求
      couponRequest(page = 1) {
        this.noData = false;
        let couponParams = {openId: this.$route.query.openId, 'page.page': page}
        this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
        this.$http.get(ajaxUrl.getDiscountList, { params: couponParams }).then((res) => {
          this.$indicator.close();
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          if (res.data.resData) {
            this.couponList = this.couponList.concat(res.data.resData.list); //组合数据
            this.totalPage = res.data.resData.totalPage
          }
        });
      },
      toReceive(discountCouponRuleId){
        let receiveParams = {
          openId: this.$route.query.openId, //微信openId
          discountCouponRuleId: discountCouponRuleId //优惠券id
        }
        this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
        this.$http.get(ajaxUrl.doCollect, { params: receiveParams }).then((res) => {
          this.$indicator.close();
          if(res.data.result == true){
            this.$messagebox({
              title: '',
              confirmButtonText: '确定',
              showCancelButton: false,
              message: '恭喜您领取成功啦！'
            }).then(res => {
              this.couponList = [];
              this.page = 1;
              this.allLoaded = false;
              this.couponRequest(); //刷新数据
            });
          }else{
            this.$toast(res.data.msg);
          }
        });
      },
      //上拉加载
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.couponList = [];
          this.page = 1;
          this.allLoaded = false;
          this.couponRequest();
        }, 1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.page++
          //let uuid = this.$route.query.uuid || sessionStorage.init_invest_list_uuid
          this.couponRequest(this.page);
          this.$refs.loadmore.onBottomLoaded(id);
          if(this.page > this.totalPage){
            this.allLoaded = true;// 若数据已全部获取完毕
          }
        }, 500)
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var.scss";
  // 领券中心
  .coupon-wrapper{
    padding: 0 .10rem .10rem .10rem;
  }
  .coupon-wrapper li{
    background: url(../../assets/images/coupon/lingquan_bg_yellow.png) no-repeat center;
    background-size: 100% auto;
    height: 1rem;
    margin-top: .10rem;
    overflow: auto;
    position: relative;
  }
  .coupon-wrapper li.disabled{
    background: url(../../assets/images/coupon/lingquan_bg_grey.png) no-repeat center;
    background-size: 100% auto;
  }
  .info-left{
    color: #fff;
    width: .95rem;
    text-align: center;
    padding-top: .25rem;
    float: left;
    font-size: .12rem;
  }
  .coupon-wrapper li span{
    display: block;
  }
  .info-left strong{
    font-size: .25rem;
    font-weight: normal;
  }
  .info-center{
    float: left;
    padding: .26rem 0 0 .14rem;
    color: #333;
  }
  .use-info{
    color: #666;
    margin-top: .12rem;
    font-size:.12rem;
  }
  .info-right{
    height: 100%;
    float:right;
    //font-size: .12rem;
  }
  .info-right .receive,.info-right .loot-all{
    color: #f78c24;
    width: .45rem;
    text-align: center;
    padding-top: .14rem;
    border-left: 1px solid #f0f0f0;
    height: 100%;
  }
  .info-right .loot-all{
    color: #cbcbcb;
    padding-top: .21rem;
  }
  img.have-received{
    position: absolute;
    right: .05rem;
    top: .14rem;
    width: .71rem;
  }
</style>