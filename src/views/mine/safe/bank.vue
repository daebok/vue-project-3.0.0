<template>
  <div class="page" id="pageDetail">
    <mt-header class="bar-nav" title="银行卡">
      <mt-button slot="left" icon="back" @click="backUrl()"></mt-button>
      <a v-if="list.length > 0 && !fastPayFlag && list.length < bankNum" @click="addBank" slot="right">
        <img style="width: .16rem" src="../../../assets/images/me/bank_card_icon_add.png" />
      </a>
    </mt-header>
    <div class="bank">
      <div v-if="list && list.length == 0" class="add-bank-modules">
        <div class="add-box" @click="addBank">
          <div class="add-icon">
            <img src="../../../assets/images/me/bank_card_icon_add.png" />
          </div>
          <p class="bank-title">添加银行卡</p>
        </div>
      </div>

      <div v-for="(item,index) in list" :class="item.bankCode" :style="'background:'+bgList[index]" class="bank-list">
        <div class="bank-left">
          <img :src="item.picPath" />
        </div>
        <div class="bank-right">
          <p class="bank-name">{{item.bankName}}</p>
          <p class="bank-type">{{item.bankCardType}}</p>
          <p class="bank-No">{{item.bankNo.substring(0,4)}} **** **** {{item.hideBankNo}}</p>
          <mt-button v-if="item.canDisable" type="default" class="unwrap-btn" @click.native="isShow(item.bankCode, item.bankNo)">解绑</mt-button>
        </div>
      </div>
    </div>
    <div class="shade-modules" v-show="show">
      <mt-button type="default" class="relieve-unwrap-btn" @click.native="delSet">解除绑定</mt-button>
      <mt-button type="default" class="cancle-btn" @click="isHide">取消</mt-button>
    </div>
    <form :action="submitUrl" method="post" id="delForm">
      <input type="hidden" name="__sid" :value="$store.state.user.__sid">
      <input type="hidden" name="userId" :value="$store.state.user.userId">
      <input type="hidden" name="bankCode" :value="bankCode">
      <input type="hidden" name="cardId" :value="cardId">
    </form>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import '../../../libs/rgbaster.min'
  import qs from 'qs'
  export default {
    data(){
      return {
        show: false,
        list:[],
        bgList: [],
        bindUrl: ajaxUrl.bindCard,
        submitUrl: ajaxUrl.unBind,
        fastPayFlag: false,
        bankCode:'',  // 提交使用
        cardId:'',  // 提交使用
        bankNum: '' //允许绑定银行卡张数
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      let getParams = {
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid,
        randTime: new Date().getTime()
      }
      this.$http.get(ajaxUrl.getBankCardList, {params: getParams}).then((res) => {
        this.$indicator.close()
        this.list = res.data.resData.bankList
        this.bankNum = res.data.resData.bankNum
        for(var i in this.list){
          if(this.list[i].fastPayFlag == 'Y'){
            return this.fastPayFlag = true;
          }
        }
        if(this.$route.query.from == 'msg_result'){ //从解绑成功页跳过来
          this.$toast('解绑成功')
        }
//        this.list.forEach(val => {
//          console.log(val.bankCode)
//          var that = this
//           var img = val.picPath
//          RGBaster.colors(img, {  // 跨域不起作用
//            paletteSize: 30, // 调色板大小
//            exclude: [ 'rgb(255,255,255)' ],  // 不包括白色
//            success: function(payload) {
//              var str = payload.dominant.slice(4, -1)
//              var rgba = 'rgba('+ str +',0.75)'
//              that.bgList.push(rgba)
//              // console.log(payload.secondary);
//              // console.log(payload.palette);//是调色板，含多个主要颜色，数组
//            }
//          });
//        })
      })
    },
    methods: {
      isShow(code, id) {
        this.show = true;
        this.bankCode = code
        this.cardId = id
      },
      backUrl(){
        if(this.$route.query.from == 'account'){ //从解绑成功页跳过来
          this.$router.push('/account')
        }else if(this.$route.query.from == 'msg_result'){
          this.$router.push('/mine/safe?from=msg_result')
        }else{
          this.$router.go(-1)
        }
      },
      isHide() {
        this.show = false;
        this.bankCode = ''
        this.cardId = ''
      },
      addBank(){
          window.location.href = ajaxUrl.bindCard +'?__sid='+this.$store.state.user.__sid+'&userId='+this.$store.state.user.userId
      },
      delSet(){
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        this.isHide()
        document.getElementById('delForm').submit()
      }
    }
  }
</script>

<style type="text/css" scoped>
  @import "../../../assets/scss/var.scss";
  @import "../../../assets/css/bankLogo.css";
  .bank {
    margin-bottom: .5rem;
    background-color: #c9a978;
  }
  .add-bank-modules,.bank-list{
    width: 3.45rem;
    margin: 0 auto;
    color:#fff;
    border-radius: .1rem;
    margin-top: .15rem;
  }
  .add-bank-modules{
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
    height: 1.2rem;
  }
  .bank-left{
    width: 25%;
    height: 100%;
    float: left;
  }
  .bank-left img{
    width: .41rem;
    height: .41rem;
    margin-left: .2rem;
    margin-top: .28rem;
  }
  .bank-right{
    float: left;
    display: inline;
    width: 75%;
    position: relative;
  }
  .bank-name{
    margin-top: .28rem;
    font-size: .18rem;
    line-height: 1;
  }
  .bank-type{
    line-height: .28rem;
    font-size: .12rem;
  }
  .bank-No{
    line-height: .4rem;
    font-size: .18rem;
  }
  .unwrap-btn{
    width: .44rem;
    height: .24rem;
    line-height: .24rem;
    text-align: center;
    position: absolute;
    top: .28rem;
    right: .22rem;
    font-size: .12rem;
    padding: 0;
    background: none;
    border:1px solid #fff;
  }
  .mint-button--default{
    box-shadow: 0 0 1px #fff;
  }
  .shade-modules{
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.4);
  }
  .relieve-unwrap-btn,.cancle-btn{
    position: fixed;
    width: 90%;
    left: 5%;
    height: .48rem;
    background: #fff;
    border-radius: .12rem;
  }
  .relieve-unwrap-btn{
    bottom: .68rem;
    color: #F95B29;
  }
  .cancle-btn{
    bottom: .08rem;
    color: #027AFF;
  }
</style>
