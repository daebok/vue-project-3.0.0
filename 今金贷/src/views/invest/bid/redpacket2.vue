<template>
  <div class="page redPacket-page" id="redPacket">
    <mt-header class="bar-nav" title="选择红包" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="redPacket">
      <ul class="redPacket-list">
        <li >
          <div class="redPacket-list-left main-color">
            <i class="symbol">¥</i>
            <em class="ticket-money">40</em>
            <i class="top-circle"></i>
            <i class="bottom-circle"></i>
          </div>
          <div class="redPacket-list-right">
            <p class="ticket-name">投资送豪礼</p>
            <p class="ticket-timelimit">长期有效</p>
            <p class="ticket-declare">使用条件 投资满1500元可用</p>
          </div>
          <label class="label-check">
            <input class="checkbox hide" data-amount="40" type="checkbox" value="1" ref="1" v-model="selectedList">
            <span class="select-icon"></span>
          </label>
        </li>
        <li class="redpacketLi" data-val="30">
          <div class="redPacket-list-left main-color">
            <i class="symbol">¥</i>
            <em class="ticket-money">40</em>
            <i class="top-circle"></i>
            <i class="bottom-circle"></i>
          </div>
          <div class="redPacket-list-right">
            <p class="ticket-name">投资送豪礼</p>
            <p class="ticket-timelimit">长期有效</p>
            <p class="ticket-declare">使用条件 投资满1500元可用</p>
          </div>
          <label class="label-check">
            <input class="checkbox hide" data-amount="40" type="checkbox" value="2" ref="2" v-model="selectedList">
            <span class="select-icon"></span>
          </label>
        </li>
      </ul>
      <div class="redPacket-bottom">
        <div class="selected-redpacket">
          <span class="color-333">已选红包总额</span>
          <span class="main-color">{{selected_total_val}}元</span>
        </div>
        <p class="redPacket-tips">＊投资可使用红包限额投资＊5%</p>
        <div class="padding-lr-15">
          <mt-button size="large" type="danger" >确认选择</mt-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { mapGetters, mapActions } from 'vuex'
  export default {
    data() {
      return {
        selectedList: this.$store.state.redpack.selectedList,
        value: [],
      }
    },
    computed: {
      ...mapGetters({
        selected_total_val: 'selected_total'
      }),
    },
    watch: {
      selectedList(newVal, oldVal){
        let total_a = 0
        newVal.forEach((val) => {
          total_a += parseInt(this.$refs[val].dataset.amount, 10)
        })
        if(total_a > 50) {
          this.$toast('选择金额不能大于50')
          this.selectedList = oldVal
        }
        this.selectRedpacket(newVal)
        this.selectRedpacketTotal(total_a)
      }
    },
    methods: {
      ...mapActions([
        'selectRedpacket',
        'selectRedpacketTotal',
      ])
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/invest_bid.scss";
  .checkbox {  position: absolute; top:.15rem; right: .15rem; width: .4rem; height: .4rem;}
  .checkbox + .select-icon {background:url('../../../assets/images/finance/invest_select_coupon_n.png') top left no-repeat;background-size: .24rem;}
  .checkbox:checked + .select-icon {background-image:url('../../../assets/images/finance/invest_select_coupon_s.png');}
  .label-check {position:absolute;background: transparent; height: 100%; width: 100%;display: block;}
</style>
