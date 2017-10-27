<template>
  <div class="page" id="myCurrent">
    <div class="page-loadmore-wrapper site-list" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
      <mt-loadmore :top-method="loadTop" :bottom-all-loaded="allLoaded" ref="loadmore">
        <section v-for="item in list" class="current-list">
          <ul class="top-info">
            <li class="current-title">{{ item.prodName }} {{ item.prodId }}</li>
            <li>
              <span>
                <i v-if="item.fundType == 'cjhx'">确认中</i>
                <i v-else>{{ item.share | currency('', 2) }}</i>
                <em>持仓份额</em>
              </span>

              <span>
                <i v-if="item.fundType == 'cjhx'">确认中</i>
                <i v-else>{{ item.frozenShare | currency('', 2) }}</i>
                <em>冻结份额</em>
              </span>
            </li>
          </ul>
          <div class="btn-wrapper">
            <a v-if="item.fundType == 'cjhx'" @click="Redeem(item.prodId,item.tradeAcco)" class="current-btn">
              <i>转出</i>
            </a>
            <a v-else @click="Disablesd()" class="current-btn disablesd">
              <i>转出</i>
            </a>
            <a @click="toUrl(item.fundType,item.prodId)" class="current-btn">交易记录</a>
          </div>
        </section>
      </mt-loadmore>

      <div class="text-center no-data" v-show="noData">
        <img src="../../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
        <p>暂无记录</p>
      </div>
    </div>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    name: 'myInvest',
    data() {
      return {
        wrapperHeight: 0,
        allLoaded: false,
        noData: false,
        list: [],
        openId: this.$route.query.openId,//微信openId
        getParams: {
          //'page.page': 1,
          //'page.pageSize': 10
          openId: this.$route.query.openId//微信openId
        }
      }
    },
    created() {
      this.$nextTick(()=> {
        this.projectList();
        this.wrapperHeight = document.documentElement.clientHeight - this.$refs.wrapper.getBoundingClientRect().top;
      })
    },
    methods: {
      projectList(type) {
        this.$indicator.open({spinnerType: 'fading-circle'}) //等待接口加载中状态
        //请求活期记录
        this.$http.get(ajaxUrl.currentList, { params: this.getParams }).then((res) => {
          this.$indicator.close();
          if(res.data.resMsg == '请先注册后再进入'){
            //未注册进入注册页面
            //this.$router.push({name:'register', params: {openId: this.params.openId}})
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去注册',
              showCancelButton: false,
              message: '请先注册后再进入'
            }).then(res => {
              this.$router.push({name:'register', params: {openId: this.getParams.openId}})
              //进入注册
            });
          }else{
            if (res.data.resData) {
              if(res.data.resData.respFail){//查询持仓记录失败
                this.$messagebox({
                  title: ' ',
                  confirmButtonText: '确定',
                  showCancelButton: false,
                  message: res.data.resData.respFail
                }).then(res => {
                  window.location.href = 'http://weixin.360mili.com/usercenter';
                  //跳转永鑫个人中心
                });
              }else{
                if (res.data.resData.holdSharelist.length <= 0) { // 无数据
                  this.noData = true;
                  return false;
                }else{
                  //回获取列表信息
                  this.list = this.list.concat(res.data.resData.holdSharelist);
                }
              }
            }
          }

        })
      },
      Redeem(prodId,tradeAcco){
        //赎回请求
        let params = { 
          prodId: prodId, 
          tradeAcco: tradeAcco,
          openId: this.$route.query.openId//微信openId
        }
        this.$http.get(ajaxUrl.projectRedeemAjax, { params: params }).then((res) => {
          document.write(res.data);
        })
      },
      Disablesd(){
        this.$toast('不能转出!');
      },
      toUrl(fundType,prodId){
        //this.$router.push('/account/transaction_record');
        //console.log(fundType+'+'+prodId);
        this.$router.push({name:'transactionRecord', params: {fundType: fundType, prodId: prodId, openId: this.openId}})
      },
      loadTop(id) {
        setTimeout(() => {
          this.$refs.loadmore.onTopLoaded(id);
          this.list = [];
          this.allLoaded = false;
          //this.getParams['page.page'] = 1;
          this.projectList('reload');
        },1000)
      },
      loadBottom(id) {
        setTimeout(() => {
          this.getParams['page.page']++;
          this.$refs.loadmore.onBottomLoaded(id);
          this.projectList('loadMore');
        }, 500);
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/var.scss";
  .disablesd{color:#999;}
  .current-list{color:#333; background-color:#fff; margin-bottom:.10rem;}
  .top-info{padding:0 .15rem;}
  .top-info li{padding:.15rem 0; overflow:auto;}
  .current-title{font-size:.16rem; border-bottom:1px solid #ddd;}
  .top-info li span{float:left; width:50%;}
  .top-info li i{display:block; margin-bottom: .05rem; font-size:.15rem; color:#666;}
  .top-info li em{color:#999;}

  .btn-wrapper{border-top:1px solid #ddd; overflow:auto;}
  .current-btn{display:block; width:50%; padding:.15rem 0; text-align:center; float:left;}
  .current-btn i{border-right:1px solid #ddd; display:block}
</style>