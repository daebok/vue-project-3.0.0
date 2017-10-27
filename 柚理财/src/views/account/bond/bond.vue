<template>
<div class="page" id="bond">
  <mt-header class="bar-nav" title="转让" >
    <mt-button slot="left" icon="back" @click="backUrl"></mt-button>
  </mt-header>
  <ul class="rd-tab-title">
    <li @click="changeTab(1)">
      <span :class="{current: active == 'tab-con1'}">可转让</span>
    </li>
    <li @click="changeTab(2)">
      <span :class="{current: active == 'tab-con2'}">转让中</span>
    </li>
    <li @click="changeTab(3)">
      <span :class="{current: active == 'tab-con3'}">已转让</span>
    </li>
    <li @click="changeTab(4)">
      <span :class="{current: active == 'tab-con4'}">已受让</span>
    </li>
  </ul>
  <div class="text-center no-data" v-show="noData">
    <img src="../../../assets/images/public/default/default_icon_no_record.png" alt="缺省图片">
    <p>暂无记录</p>
  </div>
  <div class="page-tab-container" v-show="!noData">
    <mt-tab-container class="page-tabbar-tab-container" v-model="active">
      <mt-tab-container-item id="tab-con1">
        <div class="page-loadmore-wrapper" ref="wrapper" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded" ref="loadmore">
            <section v-for="item in list" class="invest-am-list">
              <router-link :to="'/investDetail/'+ item.projectId">
              <div class="invest-am-title">
                <p class="am-title">{{item.projectName | hideBorrowName}}</p>
                <p class="repayment-time">本期回款日：{{item.nextRepayDate | dateFormatFun}}</p>
              </div>
              </router-link>
              <ul class="info-con clearfix color-999 aui-border-b">
                <li class="margin-t-10">
                  <span class="color-999">原预期年化收益率</span>
                  <span class="font-arial main-color pull-right">{{item.apr}}%</span>
                </li>
                <li class="margin-t-10">
                  <span >剩余期限(天)</span>
                  <span class="font-arial pull-right">{{item.projectRealRemainDays}}</span>
                </li>
                <li class="margin-t-10">
                  <span >可转让金额(元)</span>
                  <span class="font-arial pull-right">{{item.amount | currency('',2)}}</span>
                </li>
              </ul>
              <p class="btn-box clearfix">
                  <mt-button class="plain-btn red-btn" @click="toBondDeal(item.uuid, item.raiseInterest, 'tab-con1')" size="normal" plain>转让</mt-button>
              </p>
            </section>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <!--转让中-->
      <mt-tab-container-item id="tab-con2">
        <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded2" ref="loadmore2">
          <section v-for="item in list2" :ref="item.uuid" class="invest-am-list">
            <router-link :to="'/bondDetail/'+ item.uuid">
            <div class="invest-am-title">
              <p class="am-title">{{item.bondName | hideBorrowName}}</p>
            </div>
            </router-link>
            <ul class="info-con clearfix color-999 aui-border-b">
              <li class="margin-t-10">
                <span class="color-999">折溢价率</span>
                <span class="font-arial main-color pull-right">{{item.bondApr}}%</span>
              </li>
              <li class="margin-t-10">
                <span >转让价格(元)</span>
                <span class="font-arial pull-right">{{item.bondPrice | currency('',2)}}</span>
              </li>
              <li class="margin-t-10">
                <span >已转出金额(元)</span>
                <span v-if="item.soldCapita" class="font-arial pull-right">{{item.soldCapita | currency('',2)}}</span>
                <span v-else class="font-arial pull-right">0</span>
              </li>
              <li class="margin-t-10">
                <span >债权本金(元)</span>
                <span class="font-arial pull-right">{{item.bondMoney | currency('',2)}}</span>
              </li>
              <li class="margin-t-10">
                <span >转让申请时间</span>
                <span class="font-arial pull-right">{{item.createTime | dateFormatFun(4)}}</span>
              </li>
            </ul>
            <p class="btn-box clearfix"><mt-button class="plain-btn" @click="cancelBond(item.uuid)" size="normal" plain>撤回</mt-button></p>
          </section>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="tab-con3">
        <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded3" ref="loadmore3">
            <section v-for="item in list3" class="invest-am-list">
              <router-link :to="'/bondDetail/'+ item.uuid">
              <div class="invest-am-title">
                <p class="am-title">{{item.bondName | hideBorrowName}}</p>
              </div>
              </router-link>
              <ul class="info-con clearfix color-999 aui-border-b">
                <li class="margin-t-10">
                  <span class="color-999">折溢价率</span>
                  <span v-if="item.bondApr >= 0" class="font-arial main-color pull-right">+{{item.bondApr | currency('', 2)}}%</span>
                  <span v-else class="font-arial main-color pull-right">{{item.bondApr | currency('', 2)}}%</span>
                </li>
                <li class="margin-t-10">
                  <span >转让价格(元)</span>
                  <span class="font-arial pull-right">{{item.bondPrice | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >实收金额(元)</span>
                  <span class="font-arial pull-right">{{item.receivedMoney | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >债权本金(元)</span>
                  <span class="font-arial pull-right">{{item.bondMoney | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >转让成功时间</span>
                  <span class="font-arial pull-right">{{item.successTime | dateFormatFun(4)}}</span>
                </li>
              </ul>
              <p class="btn-box clearfix"><mt-button @click="downloadTips" class="plain-btn " size="normal" plain>下载协议</mt-button></p>
            </section>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
      <mt-tab-container-item id="tab-con4">
        <div class="page-loadmore-wrapper" ref="" :style="{ height: wrapperHeight + 'px' }">
          <mt-loadmore :bottom-method="loadBottom" :bottom-all-loaded="allLoaded4" ref="loadmore4">
            <section v-for="item in list4" class="invest-am-list">
              <router-link :to="'/bondDetail/'+ item.bondId">
              <div class="invest-am-title">
                <p class="am-title">{{item.bondName | hideBorrowName}}</p>
              </div>
              </router-link>
              <ul class="info-con clearfix color-999 aui-border-b">
                <li class="margin-t-10">
                  <span class="color-999">折溢价率</span>
                  <span v-if="item.bondApr >= 0" class="font-arial main-color pull-right">+{{item.bondApr | currency('',2)}}%</span>
                  <span v-else class="font-arial main-color pull-right">{{item.bondApr | currency('',2)}}%</span>
                </li>
                <li class="margin-t-10">
                  <span >支付价格(元)</span>
                  <span class="font-arial pull-right">{{item.paidMoney | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >待收本息(元)</span>
                  <span class="font-arial pull-right">{{item.waitMoney | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >债权本金(元)</span>
                  <span class="font-arial pull-right">{{item.amount | currency('',2)}}</span>
                </li>
                <li class="margin-t-10">
                  <span >受让成功时间</span>
                  <span class="font-arial pull-right">{{item.createTime | dateFormatFun(4)}}</span>
                </li>
              </ul>
              <div v-if="item.status == 0" class="btn-box clearfix">
                <p class="count-time">剩余时间 <count-down :remainTimes="item.remainTimes"></count-down></p>
                <mt-button size="normal" class="plain-btn red-btn" @click="toPay(item.investOrderNo)" plain>去支付</mt-button>
              </div>
              <div v-else class="btn-box clearfix">
                <mt-button @click="downloadTips" class="plain-btn" size="normal" plain>下载协议</mt-button>
                <router-link :to="'/account/myInvest/repayment_plan?tab=tab-con4&investId='+item.investId">
                  <mt-button class="plain-btn red-btn" size="normal" plain>回款计划</mt-button>
                </router-link>
                <mt-button v-if="item.showBond == 1" class="plain-btn red-btn color-white" type="danger" @click="toBondDeal(item.investId, item.raiseInterest, 'tab-con4')" size="normal" plain>转让</mt-button>
              </div>
            </section>
          </mt-loadmore>
        </div>
      </mt-tab-container-item>
    </mt-tab-container>
  </div>
</div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import CountDown from '../../../components/myInvest_countTime.vue'
  export default {
    name: 'myInvest',
    components: {CountDown},
    methods: {
      changeTab(index=1){
        if(index == 1){
          this.active = 'tab-con1'
          this.list = []
        }else if(index == 2){
          this.active = 'tab-con2'
          this.list2 = []
        }else if(index == 3){
          this.active = 'tab-con3'
          this.list3 = []
        }else if(index == 4){
          this.active = 'tab-con4'
          this.list4 = []
        }
        this.$router.push('/account/bond/bond?tab='+ this.active)
        this.projectList()
      },
      backUrl(){
//        if(this.$route.query.from){
//          this.$router.push({name: 'investBid', params: {projectId: this.$route.query.uuid}, query: {from: 'myInvest'}})
//        }else{}
        this.$router.push('/account')
      },
      projectList(page=1){
        this.noData = false; // 数据为空判断
        this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
        let url
        if(this.active == 'tab-con1'){
          url = ajaxUrl.ableBondList
        }else if(this.active == 'tab-con2'){
          url = ajaxUrl.sellingBondList
        }else if(this.active == 'tab-con3'){
          url = ajaxUrl.soldBondList
        }else if(this.active == 'tab-con4'){
          url = ajaxUrl.boughtBondList
        }
        let urlParams = {
          'page.page': page,
          'page.size': 10,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid,
          randTime: new Date().getTime()
        }
        this.$http.get(url, { params: urlParams }).then((res) => {
          this.$indicator.close() // 关闭提示
          if(res.data.resData.list.length == 0){this.noData = true;}// 数据为空判断
          if(res.data.resData.totalPage <= 1){  // 只有一页数据就不显示上拉加载
            switch (this.active){
              case 'tab-con1':
                this.allLoaded = true;
                break;
              case 'tab-con2':
                this.allLoaded2 = true;
                break;
              case 'tab-con3':
                this.allLoaded3 = true;
                break;
              case 'tab-con4':
                this.allLoaded4 = true;
                break;
            }
          }
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
          }else if(this.active == 'tab-con3'){
            this.list3 = this.list3.concat(res.data.resData.list)
            this.totalPage3 = res.data.resData.totalPage
          }else if(this.active == 'tab-con4'){
            this.list4 = this.list4.concat(res.data.resData.list)
            this.totalPage4 = res.data.resData.totalPage
          }
        })
      },
      loadBottom(id) {
        setTimeout(() => {
          if(this.active == 'tab-con1'){
            let page = ++this.page1
            if( page > this.totalPage1){
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
          }else if(this.active == 'tab-con3'){
            let page = ++this.page3
            if(page > this.totalPage3){
              this.allLoaded3 = true;// 若数据已全部获取完毕
            }
            this.projectList(page)
            this.$refs.loadmore3.onBottomLoaded(id);
          }else if(this.active == 'tab-con4'){
            let page = ++this.page4
            if(page > this.totalPage4){
              this.allLoaded4 = true;// 若数据已全部获取完毕
            }
            this.projectList(page)
            this.$refs.loadmore4.onBottomLoaded(id);
          }
        }, 1000);
      },
      downloadTips(){
        this.$toast('请前往PC端查看并下载协议')
      },
      toPay(uuid){
        window.location.href = ajaxUrl.doBondPay + '?investOrderNo=' + uuid + '&userId=' + this.$store.state.user.userId + '&__sid=' + this.$store.state.user.__sid
      },
      cancelBond(uuid) {
        this.$messagebox({
          title: ' ',
          cancelButtonText: '取消',
          confirmButtonText: '确认',
          showCancelButton: true,
          message: '确认要撤回未转让成功的资金？'
        }).then(action => {
          if(action == 'confirm'){
            let urlParams = {
              id: uuid,
              userId: this.$store.state.user.userId,
              __sid: this.$store.state.user.__sid,
            }
            this.$http.get(ajaxUrl.cancleBond, { params: urlParams }).then((res) => {
              if(res.data.resCode == 39321){ // resMsg 债权撤回成功
                this.$toast('撤回成功')
                this.$refs[uuid][0].hidden = true
              }else{
                this.$toast(res.data.resMsg)
              }
            })
          }
        });
      },
      toBondDeal(uuid, interest, tab){
        if(interest > 0) {
          this.$messagebox({
            title: ' ',
            showCancelButton: true,
            closeOnClickModal: true,
            message: '此笔投资您拥有加息收益' + interest + '元,若您进行转让,系统将视为您放弃此笔投资的加息收益。'
          }).then(action => {
            if (action === 'confirm') {
              this.conFun(uuid, interest, tab)
            }
          })
        }else{
          this.conFun(uuid, interest, tab)
        }
      },
      conFun(uuid, interest, tab){
        let urlParams = {
          id: uuid,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
        this.$http.get(ajaxUrl.toBondSet, { params: urlParams }).then((res) => {
          let i = 0
          let j = 0
          let max = ['0.0']
          let min = ['0.0']
          while (i < res.data.resData.max) {
            i += 0.1
            max.push(i.toFixed(1))
          }
          while (j < Math.abs(res.data.resData.min)) {
            j += 0.1
            min.push(j.toFixed(1))
          }
          sessionStorage.bondApr_max = JSON.stringify(max)
          sessionStorage.bondApr_min = JSON.stringify(min)
          this.$router.push('/account/bond/bond_deal/?uuid='+ uuid +'&tab='+tab)
        })
      }
    },
    created() {
      this.active = this.$route.query.tab || 'tab-con1'
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
        list3: [],
        list4: [],
        allLoaded: false,
        allLoaded2: false,
        allLoaded3: false,
        allLoaded4: false,
        page1: 1,
        page2: 1,
        page3: 1,
        page4: 1,
        totalPage1:1, //tab1默认总页数为1
        totalPage2:1, //tab2默认总页数为1
        totalPage3:1, //tab3默认总页数为1
        totalPage4:1, //tab4默认总页数为1
      };
    }
  }
</script>
<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../../assets/scss/var.scss";
  .color-white{color: #fff!important;}
  .rd-tab-title {
    width: 100%;
    height: .4rem;
    background: #fff;
  }
  .rd-tab-title li{
    width: 25%;
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
  .invest-am-list {
    padding: 0 .15rem;
    background: #fff;
    margin-top: .1rem;
  }
  .invest-am-title {
    border-bottom: 1px solid #DEDEDE;
  }
  .am-title {
    height: .42rem;
    line-height: .42rem;
    font-size: .16rem;
  }
  .repayment-time {
    margin-bottom: .1rem;
    color: #666;
  }
  .info-con {font-size: .12rem;padding-bottom:.15rem;}
  .info-con li span:last-child { font-size: .14rem;}
  .btn-box {height: .5rem;}
  .red-btn {
    border: 1px solid $main-color;
    color: $main-color;
  }
  .plain-btn {
    height: .3rem;
    line-height: .28rem;
    width: .75rem;
    margin: .1rem 0;
    border-radius: .40rem;
    font-size: .14rem;
    float:right;
    margin-left: .1rem;
    padding:0;
  }
  .mint-button:not(.is-disabled):active::after {opacity: 0;}
  .count-time {
    float: left;
    line-height: .5rem;
    color: #666;
    font-size: .12rem;
  }
</style>
