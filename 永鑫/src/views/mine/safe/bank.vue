<!-- 银行卡 by fenglei -->
<template>
  <div class="page">
    <div class="bank">
      <div v-if="list && list.length == 0" class="add-bank-modules">
        <div class="add-box" @click="addBank">
          <div class="add-icon">
            <img src="../../../assets/images/me/bank_card_icon_add.png">
          </div>
          <p class="bank-title">添加银行卡</p>
        </div>
      </div>
      <div v-else v-for="(item,index) in list" class="bank-list">
        <div class="top">
          <div class="bank-left">
            <img :src="item.picPath">
          </div>
          <div class="bank-right">
            <p class="bank-name">{{ item.bankName }}</p>
            <p class="bank-type">储蓄卡</p>
            <span v-if="canDisable" class="unwrap-btn" @click="modifyBank">更换银行卡</span>
          </div>
        </div>
        <div class="bottom">
          <p class="bank-No">{{ item.hideBankNo }}</p>
        </div>
      </div>
      <div v-if="!canDisable" class="noBind">默认提现银行卡，不允许更换</div>
    </div>
    <mt-actionsheet class="delBankSheet" :actions="actions" v-model="show"></mt-actionsheet>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import '../../../libs/rgbaster.min';
  import qs from 'qs';

  export default {
    data() {
      return {
        list: [],
        show: false, // 是否显示解绑遮罩层，true显示，false不显示
        canDisable: false, // 是否显示解绑按钮，true显示，false不显示
        cardId: '',  // 解绑银行卡卡号
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        actions: [
          {
            name: '解除绑定',
            method: this.delSet
          }
        ]
      };
    },
    created() {
      this.$http.get(ajaxUrl.getBankCardList, { params: this.getParams }).then((res) => {
        this.list = res.data.resData.bankList;
        this.canDisable = res.data.resData.canDisable;
      })
    },
    methods: {
      isShow(bankNo) {
        this.show = true;
        this.cardId = bankNo;
      },
      isHide() {
        this.show = false;
        this.cardId = '';
      },
      addBank() {
        this.$router.push({ name: 'addBank' });
      },
      modifyBank() {
        this.$router.push({ name: 'modifyBank' });
      },
      delSet() {
        let params = {
          cardId: this.cardId,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({spinnerType: 'fading-circle'});
        this.$http.post(ajaxUrl.unBind, qs.stringify(params)).then((res) => {
          this.$indicator.close();
          this.isHide();
          this.$messagebox({
            title: ' ',
            confirmButtonText: '确认',
            showCancelButton: false,
            message: res.data.resMsg
          }).then(action => {
            if (action === 'confirm') {
              if (res.data.resCode == 39321) {
                location.reload();
              }
            }
          });
        })
      }
    }
  }
</script>

<style type="text/css">
  .bank {
    margin-bottom: .5rem;
  }
  .add-bank-modules{
    width: 3.45rem;
    margin: 0 auto;
    color: #666;
    border-radius: .1rem;
    margin-top: .15rem;
    border: 1px dotted #DDD;
    background: #fff;
  }
  .add-bank-modules .add-box {
    height: 1.2rem;
  }
  .add-icon{
    padding-top: .32rem;
    padding-bottom: .15rem;
    text-align: center;
  }
  .add-icon img{
    height: .24rem;
    width: .24rem;
  }
  .bank-title{
    text-align: center;
    font-size: .18rem;
    color: #333;
  }
  .bank-list{
    display: inline-block;
    width: 90%;
    height: 1.2rem;
    margin-top: 0.2rem;
    margin-left: 5%;
    background: url(../../../assets/images/me/bank-bg.png) no-repeat center center;
    background-size: 100% 100%;
    font-size: 0;
  }
  .bank-list .top{
    display: inline-block;
    width: 100%;
    height: 0.6rem;
  }
  .bank-list .bottom{
    display: inline-block;
    width: 100%;
    height: 0.6rem;
  }
  .bank-list .top .bank-left{
    display: inline-block;
    width: 20%;
    height: 0.6rem;
    padding-left: 5%;
    vertical-align: top;
    text-align: center;
  }
  .bank-list .top .bank-left img{
    width: 0.36rem;
    height: 0.36rem;
    padding: 0.04rem;
    margin-top: 0.12rem;
    background: #fff;
    border-radius: 100%;
  }
  .bank-list .top .bank-right{
    position: relative;
    display: inline-block;
    width: 80%;
    height: 0.6rem;
    vertical-align: top;
  }
  .bank-list .top .bank-right .unwrap-btn{
    position: absolute;
    right: 5%;
    top: 0.175rem;
    height: 0.25rem;
    padding: 0 0.08rem;
    border: 1px solid #FFFFFF;
    border-radius: 4px;
    line-height: 0.25rem;
    font-size: 0.13rem;
    color: #FFFFFF;
    opacity: 0.8;
  }
  .bank-list .top .bank-right .bank-name{
    display: inline-block;
    height: 0.6rem;
    padding-left: 5%;
    line-height: 0.6rem;
    font-size: 0.18rem;
    color: #fff;
  }
  .bank-list .top .bank-right .bank-type{
    display: inline-block;
    height: 0.6rem;
    padding-left: 5%;
    line-height: 0.6rem;
    font-size: 0.12rem;
    color: #FEFEFE;
  }
  .bank-list .bottom .bank-No{
    display: inline-block;
    width: 100%;
    height: 0.6rem;
    padding-left: 8%;
    line-height: 0.6rem;
    font-size: 0.2rem;
    color: #666;
  }
  .noBind{
    display: inline-block;
    width: 100%;
    text-align: center;
    color: #999;
    margin-top: 0.1rem;
  }
  .delBankSheet.mint-actionsheet {
    width: 90%;
    background: none;
    bottom: 0.1rem
  }
  .delBankSheet .mint-actionsheet-listitem, .mint-actionsheet-button {
    background: none;
    border: none;
    font-size: 16px;
  }
  .delBankSheet .mint-actionsheet-listitem {
    color: #F95B29;
  }
  .delBankSheet .mint-actionsheet-list {
    border-radius: 10px;
    background: #fff;
  }
  .delBankSheet .mint-actionsheet-button {
    border-radius: 10px;
    background: #fff;
    color: #4085C2;
  }
</style>
