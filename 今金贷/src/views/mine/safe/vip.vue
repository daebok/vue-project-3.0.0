<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="我的等级">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="vip">
      <div class="vip-top">
        <div class="vip-1">
          <img src="../../../assets/images/me/icon_VIP_level_V1.png" />
        </div>
        <div class="vip-level">
          当前等级 <i class="main-color">VIP{{resdata.userVipLevel}}</i>
        </div>
        <div class="vip-interest">
          当前成长值 <i class="main-color">{{resdata.growthValue}}</i>
        </div>
      </div>
      <div class="vip-2 margin-t-10">
        <div class="vip-3">
          <p class="vip-title">介绍</p>
          <p class="vip-desc">
            1.VIP等级是用户活跃和荣誉的见证，随着用户在平台的投资累计，等级会随之增长，等级越高，享有福利越丰厚！
          </p>
          <p class="vip-desc">
            2.VIP等级提升由成长值决定，成长值是由用户累计投资金额计算所得。
          </p>
          <p class="vip-desc">
            3.若用户一年内未有新的投资则会根据等级的“年扣成长值”扣除相应的成长值。
          </p>
        </div>
        <div class="vip-4">
          <p class="vip-title-2">成长说明</p>
          <table class="vip-table-list" border="1" borderColor="#e6e6e6">
            <tr class="title-bg">
              <td>等级</td>
              <td>成长值区间</td>
              <td>年扣除成长值</td>
            </tr>
            <tr v-for="item in resdata.vipLevelList">
              <td>VIP{{item.vipLevel}}</td>
              <td>{{item.growthValue}}{{item.growthLimitValue != 0 ? '-'+item.growthLimitValue : '以上'}}</td>
              <td>{{item.yearDeduction}}</td>
            </tr>
          </table>
          <p class="vip-title-2">福利说明</p>
          <table class="vip-table-list" border="1" borderColor="#e6e6e6">
            <tr class="title-bg">
              <td>等级</td>
              <td>红包福利</td>
              <td>加息券福利</td>
              <td>生日礼包</td>
              <td>专享礼包</td>
            </tr>
            <tr v-for="item in resdata.vipLevelList">
              <td>VIP{{item.vipLevel}}</td>
              <td>
                <img v-if="item.redenvelopeRuleId" src="../../../assets/images/me/icon_VIP_level_selected.png" class="selected-img"/>
              </td>
              <td>
                <img v-if="item.rateCouponRuleId" src="../../../assets/images/me/icon_VIP_level_selected.png" class="selected-img"/>
              </td>
              <td>
                <img v-if="item.birthdayGiftId" src="../../../assets/images/me/icon_VIP_level_selected.png" class="selected-img"/>
              </td>
              <td>
                <img v-if="item.exclusiveGiftId" src="../../../assets/images/me/icon_VIP_level_selected.png" class="selected-img"/>
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    data(){
      return {
        resdata: ''
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let getParams = {
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      this.$http.get(ajaxUrl.vip, {params:getParams}).then((res) => {
        this.resdata = res.data.resData;
        this.$indicator.close()
      })
    }
  }
</script>
<style scoped>
  .vip-icon{
    width: .19rem;
    vertical-align: middle;
  }
  .vip{
    width: 100%;
    height: 100%;
  }
  .vip-top{
    width: 100%;
    text-align: center;
    background: #fff;
  }
  .vip-1{
    padding-top: .2rem;
    padding-bottom: .1rem;
  }
  .vip-1 img{
    width: 1.2rem;
    height: .9rem;
  }
  .vip-level{
    font-size: .16rem;
    color: #666;
  }
  .vip-level i{
    width: .48rem;
    height: .19rem;
    border: 1px solid #F95A28;
    border-radius: .09rem;
    display: inline-block;
    line-height: 1;
  }
  .vip-interest{
    font-size: .16rem;
    color: #666;
    padding-top: .2rem;
    padding-bottom: .2rem;
  }
  .vip-2{
    background: #fff;
    padding: 0 .15rem .2rem .15rem;
  }
  .vip-desc{
    font-size: .12rem;
    line-height: .21rem;
    color: #999;
  }
  .vip-title,.vip-title-2{
    font-size: .15rem;
    color: #333;
    line-height: 1;
  }
  .vip-title{
    padding-top: .14rem;
    padding-bottom: .0755rem;
  }
  .vip-title-2{
    padding-top: .195rem;
    padding-bottom: .12rem;
  }
  .vip-table-list{
    border-collapse: collapse;
    width: 100%;
    border-color: #e6e6e6;
  }
  .vip-table-list tr{
    height: .35rem;
    text-align: center;
    font-size: .12rem;
    color: #666;
  }
  .title-bg{
    background: #f3f3f3;
    color: #333!important;
  }
  .selected-img{
    width: .12rem;
    height: .12rem;
    vertical-align: middle;
  }
</style>
