<template>
  <div class="page" id="transactionRecord">
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <ul class="record-list">
          <!-- 产品记录1 -->
          <li v-for="item in chjxList" class="aui-border-b">
            <span class="padding-l-15 text-left color-333 fz-16">{{item.businessType}}</span>

            <span v-if="item.businessType == '赎回'" class="padding-r-15 text-right fz-16">{{item.money}}份</span>
            <span v-else class="padding-r-15 text-right fz-16">{{item.money | currency('', 2)}}</span>

            <span class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.buyDate}}</span>

            <span v-if="item.isRevoke == 0" class="padding-r-15 text-right color-999 fz-13 ellipsis">确认成功</span>
            <span v-else-if="item.isRevoke == 1" class="padding-r-15 text-right color-999 fz-13 ellipsis">撤单成功</span>
            <span v-else-if="item.isRevoke == 3" class="padding-r-15 text-right color-999 fz-13 ellipsis">待确认</span>
            <span v-else-if="item.isRevoke == 'zt1'" class="padding-r-15 text-right color-999 fz-13 ellipsis">可撤单</span>
            <span v-else-if="item.isRevoke == 'zt0'" class="padding-r-15 text-right color-999 fz-13 ellipsis">待确认</span>
            <span v-else-if="item.isRevoke == 'zt01'" class="padding-r-15 text-right color-999 fz-13 ellipsis">未校检</span>
            <span v-else-if="item.isRevoke == 'zt11'" class="padding-r-15 text-right color-999 fz-13 ellipsis">无效</span>
            <span v-else-if="item.isRevoke == 'zt31'" class="padding-r-15 text-right color-999 fz-13 ellipsis">待扣款</span>
            <span v-else class="padding-r-15 text-right color-999 fz-13 ellipsis">确认失败</span>
          </li>

          <!-- 产品记录2 -->
          <li v-for="item in hxList" class="aui-border-b">
            <span v-if="item.apkind == '020'" class="padding-l-15 text-left color-333 fz-16">认购</span>
            <span v-else-if="item.apkind == '022'" class="padding-l-15 text-left color-333 fz-16">申购</span>
            <span v-else-if="item.apkind == '024'" class="padding-l-15 text-left color-333 fz-16">赎回</span>
            <span v-else-if="item.apkind == '150'" class="padding-l-15 text-left color-333 fz-16">兑付</span>
           
            <span v-if="item.apkind == '024'" class="padding-r-15 text-right fz-16">{{item.amount}}份</span>
            <span v-else class="padding-r-15 text-right fz-16">{{item.amount | currency('', 2)}}</span>

            <span v-if="item.confirmDate !== ''" class="padding-l-15 padding-b-15 text-left color-999 fz-13">{{item.confirmDate | timeFormat('')}}</span>
            <span v-else class="padding-l-15 padding-b-15 text-left color-999 fz-13">...</span>

            <span v-if="item.orderStatus == 'Y'" class="padding-r-15 text-right color-999 fz-13 ellipsis">成功</span>
            <span v-else-if="item.orderStatus == 'F'" class="padding-r-15 text-right color-999 fz-13 ellipsis">失败</span>
            <span v-else-if="item.orderStatus == 'P'" class="padding-r-15 text-right color-999 fz-13 ellipsis">部分成功</span>
            <span v-else-if="item.orderStatus == 'I'" class="padding-r-15 text-right color-999 fz-13 ellipsis">处理中</span>
            <span v-else-if="item.orderStatus == 'A'" class="padding-r-15 text-right color-999 fz-13 ellipsis">已受理</span>
            <span v-else-if="item.orderStatus == 'C'" class="padding-r-15 text-right color-999 fz-13 ellipsis">已撤销</span>
            <span v-else-if="item.orderStatus == 'G'" class="padding-r-15 text-right color-999 fz-13 ellipsis">撤销中</span>
          </li>
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
    data(){
      return {
        chjxList: [],
        hxList: [],
        noData: false,
        allLoaded: false,
        page: 1,
        //totalPage:1,
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
          //'page.page': page,
          //'page.pageSize': 10,
          fundType: this.$route.params.fundType,//产品类型
          prodId: this.$route.params.prodId,//产品代码
          openId: this.$route.params.openId,//微信openid
        }
        this.$http.get(ajaxUrl.tradingList, {params: getParams}).then((res) => {
          this.$indicator.close()
          //产品类型判断
          console.log(res.data.resData.fundType);
          if(res.data.resData.fundType == 'cjhx'){
            if(res.data.resData.CjhxTradingRecordList.length == 0){
              // 数据为空判断
              this.noData = true;
            }
            this.chjxList = this.chjxList.concat(res.data.resData.CjhxTradingRecordList)
          }else if(res.data.resData.fundType == 'hx'){
            if(res.data.resData.orderDetailsModel.length == 0){
              // 数据为空判断
              this.noData = true;
            }
            this.hxList = this.hxList.concat(res.data.resData.orderDetailsModel)
          }
        })
      },
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.chjxList = [];
          this.hxList = [];
          this.allLoaded = false;
          //this.page = 1
          this.projectList('reload');
        }, 1000);
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