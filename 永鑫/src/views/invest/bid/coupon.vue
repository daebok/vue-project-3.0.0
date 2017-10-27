<template>
  <div class="page redPacket-page" id="redPacket">
    <div class="text-center no-data" v-show="noData">
      <img src="../../../assets/images/public/default/default_icon_no_jxq.png" alt="缺省图片">
      <p>暂无加息券</p>
    </div>
    <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
      <ul class="coupon-list">
        <li v-for="item in list" :class="{'color-999':investMoney < item.useTenderMoney}">
          <div class="coupon-list-1">
            <div class="coupon-left" :class="{'color-999':investMoney < item.useTenderMoney}">
              <i class="coupon-num" :class="{'size-small': item.upApr.length == 5}">+{{item.upApr}}</i><i class="symbol font-arial">%</i>
            </div>
            <div class="coupon-right">
              <p class="coupon-name" :class="{'color-999':investMoney < item.useTenderMoney}">{{item.ruleName}}</p>
              <p v-if="item.useExpireTime == ''" class="coupon-limit">长期有效</p>
              <p v-else class="coupon-limit">到期日期 {{item.useExpireTime | dateFormatFun(4)}}</p>
              <p class="coupon-desc">使用条件 {{item.ruleRemark}}</p>
            </div>
            <label v-if="investMoney < item.useTenderMoney" class="label-check">
              <input class="checkbox hide" :data-up-apr="item.upApr" :ref="item.uuid" type="radio" :value="item.uuid" v-model="selected_coupon" :disabled="investMoney < item.useTenderMoney">
              <span class="select-icon"></span>
            </label>
            <label v-else class="label-check" @click.prevent="cancelChoose">
              <input class="checkbox hide" :data-up-apr="item.upApr" :ref="item.uuid" type="radio" :value="item.uuid" v-model="selected_coupon" :disabled="investMoney < item.useTenderMoney">
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
        <span class="color-333">已选加息券利率</span>
        <span class="main-color">{{selected_upApr}}%</span>
      </div>
      <p class="redPacket-tips"><i class="main-color">*</i>每次投资只可使用一张加息券</p>
      <div class="padding-lr-15">
        <mt-button v-if="noData" size="large" type="default" disabled>确认选择</mt-button>
        <mt-button v-else size="large" type="danger" @click.native="confirm">确认选择</mt-button>
      </div>
    </div>

  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    name: 'coupon',
    data() {
      return {
        list: [],
        noData: false,
        allLoaded: false,
        wrapperHeight: 0,
        page: 1,
        totalPage: 1,
        selected_coupon: sessionStorage.selected_coupon,
        selected_upApr: '0',
        investMoney: 0
      }
    },
    created() {
      this.investMoney = sessionStorage.investMoney;
      let upapr = sessionStorage.selected_upApr > 0 ? sessionStorage.selected_upApr : '0'
      this.selected_upApr = upapr
      this.projectList()
      this.$nextTick(function () {
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top - 130;
      })
    },
    watch: {
      selected_coupon(val){
        // console.log(val)
        // this.selected_upApr = this.$refs[val][0].dataset.upApr
      }
    },
    methods: {
      cancelChoose(evt){ //点击选择优惠券
        if(evt.currentTarget.children[0].checked){
          this.selected_upApr = '0'
          this.selected_coupon = ''
        }else{
          this.selected_upApr = evt.currentTarget.children[0].dataset.upApr
          this.selected_coupon = evt.currentTarget.children[0].value
        }
      },
      projectList(page = 1){
        this.noData = false
        let urlParams = {
          'page.page': page,
          projectId: this.$route.params.projectId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          tenderMoney: sessionStorage.investMoney
        }
        this.$http.get(ajaxUrl.availableRateList, { params: urlParams }).then((res) => {
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
      confirm(){ //点击确认选择
        sessionStorage.selected_coupon = this.selected_coupon
        sessionStorage.selected_upApr = this.selected_upApr
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
