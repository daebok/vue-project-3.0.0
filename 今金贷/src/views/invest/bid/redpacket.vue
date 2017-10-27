<template>
  <div class="page redPacket-page" id="redPacket">
    <mt-header class="bar-nav" title="选择红包" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="text-center no-data" v-show="noData">
      <img src="../../../assets/images/public/default/default_icon_no_hb.png" alt="缺省图片">
      <p>暂无红包</p>
    </div>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
      <ul class="coupon-list">
        <li v-for="item in list">
          <div class="coupon-list-1">
            <div class="coupon-left">
              <i class="symbol main-color">¥</i><i class="coupon-num main-color">{{item.amount}}</i>
            </div>
            <div class="coupon-right">
              <p class="coupon-name">{{item.ruleName}}</p>
              <p v-if="item.useExpireTime == ''" class="coupon-limit">长期有效</p>
              <p v-else class="coupon-limit">到期日期 {{item.useExpireTime | dateFormatFun(4)}}</p>
              <p class="coupon-desc">使用条件 {{item.ruleRemark}}</p>
            </div>
            <label class="label-check">
              <input class="checkbox hide" :data-amount="item.amount" :ref="item.uuid" type="checkbox" :value="item.uuid" v-model="selectedList">
              <span class="select-icon"></span>
            </label>
          </div>
          <div class="coupon-list-2">
            <div v-if="item.useProjectType == 0">
              <div class="coupon-use">适用于所有产品</div>
              <div class="coupon-icon"></div>
            </div>
            <div v-else>
              <div class="coupon-use">点击查看适用产品</div>
              <div class="coupon-icon" @click.self="showHide">
                <img src="../../../assets/images/me/arrow_down.png" class="icon-show" />
              </div>
              <div class="show-remark" hidden>{{item.useProjectTypeName}}</div>
            </div>
          </div>
          <i class="left-icon"></i><i class="right-icon"></i>
        </li>
      </ul>
      </mt-loadmore>
    </div>
    <div class="redPacket-bottom">
      <div class="selected-redpacket">
        <span class="color-333">已选红包总额</span>
        <span class="main-color">{{selected_total_val}}元</span>
      </div>
      <p class="redPacket-tips"><span class="main-color">*</span>投资可使用红包限额投资*{{$route.query.rate}}%</p>
      <div class="padding-lr-15">
        <mt-button v-if="noData" size="large" type="default" disabled>确认选择</mt-button>
        <mt-button v-else size="large" type="danger" @click.native="confirm">确认选择</mt-button>
      </div>
    </div>

  </div>
</template>
<script>
  import { mapActions } from 'vuex'
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    name: 'redPacket',
    data() {
      return {
        list: [],
        allLoaded: false,
        noData: false,
        wrapperHeight: 0,
        page: 1,
        totalPage: 1,
        limit_num: sessionStorage.redpack_max_limit,
        selectedList: this.$store.state.redpack.selectedList,
        selected_total_val: this.$store.state.redpack.selected_total
      }
    },
    created() {
      this.projectList()
      this.$nextTick(() => {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top - 130;
      })
    },
    watch: {
      selectedList(newVal, oldVal){
        let total_a = 0
        newVal.forEach((val) => {
          //console.log(this.$refs[val][0])
          total_a += parseInt(this.$refs[val][0].dataset.amount, 10)
        })
        if(total_a > this.limit_num) {
          this.$toast('选择金额不能大于'+this.limit_num+'元')
          this.selectedList = oldVal
        }else{
          this.selectedList = newVal
        }
        if(total_a && total_a > 0) total_a = total_a.toFixed(2)
        this.selected_total_val = total_a

      }
    },
    methods: {
      projectList(page = 1){
        this.noData = false;
        let tenderMoney = sessionStorage.investMoney
        if(this.$route.query.copyAccount){ //判断份额购买
          tenderMoney = tenderMoney * this.$route.query.copyAccount
        }
        let urlParams = {
          'page.page': page,
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          tenderMoney: tenderMoney
        }
        this.$http.get(ajaxUrl.availableRedList, { params: urlParams }).then((res) => {
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){ this.allLoaded = true;} // 只有一页数据就不显示上拉加载
          if(page > res.data.resData.totalPage){
            if(res.data.resData.totalPage > 0){
              this.$toast('无更多数据加载哦~')
            }
            return ; // 请求页大于总页数就返回不执行
          }
          this.list = this.list.concat(res.data.resData.list)
        })
      },
      loadTop(id){
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.list = [];
          this.page = 1;
          this.allLoaded = false;
          this.projectList()
        },1000)
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
      ...mapActions([
        'selectRedpacket',
        'selectRedpacketTotal',
      ]),
      confirm(){ //点击确认选择
        this.selectRedpacket(this.selectedList)
        this.selectRedpacketTotal(this.selected_total_val)
        this.$router.go(-1)
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
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/invest_bid.scss";
  .checkbox {  position: absolute; top:.15rem; right: .15rem; width: .4rem; height: .4rem;}
  .checkbox + .select-icon {background:url('../../../assets/images/finance/invest_select_coupon_n.png') top left no-repeat;background-size: .24rem;}
  .checkbox:checked + .select-icon {background-image:url('../../../assets/images/finance/invest_select_coupon_s.png');}
  .label-check {position:absolute; top:0; background: transparent; height: 100%; width: 100%;display: block;}
  .redPacket-bottom{border-top:1px solid #E6E6E6;}
</style>
