<template>
  <div class="page">
    <mt-header class="bar-nav" title="产品详情" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
      <router-link :to="'/investDetail/'+$route.query.oldProjectId" slot="right">
        <mt-button v-if="$route.query.oldProjectId != 'undefined'">查看原项目</mt-button>
      </router-link>
    </mt-header>
    <ul class="describe-title" v-if="$route.query.borrowFlag == 1">
      <li @click="active = 'tab-container1'"><span :class="{current: active == 'tab-container1'}">借款详情</span></li>
      <li @click="active = 'tab-container2'"><span :class="{current: active == 'tab-container2'}">借款方信息</span></li>
      <li @click="active = 'tab-container3'"><span :class="{current: active == 'tab-container3'}">借款资料</span></li>
    </ul>
    <div class="page-tab-container">
      <mt-tab-container class="page-tabbar-tab-container" v-model="active" swipeable>
        <mt-tab-container-item id="tab-container1">
          <div class="describe-info borrower-info margin-t-15">
            <div v-if="$route.query.borrowFlag == 1">
              <div class="borrow-title"><i class="radious"></i>借款标题</div>
              <div class="borrow-name">
                {{ borrowerDetail.projectName }}
              </div>
              <div class="borrow-title margin-t-20"><i class="radious"></i>借款用途</div>
              <div class="borrow-use">
                {{ borrowerDetail.borrowUse }}
                <!--<p>为了扩大经营，急需资金周转</p>-->
              </div>
              <div class="borrow-title margin-t-20"><i class="radious"></i>借款说明</div>
              <div class="borrow-intro" v-html="borrowerDetail.content"></div>
            </div>
            <div v-else>
              <div class="borrow-title">产品详情</div>
              <div class="borrow-use" v-html="borrowerDetail.content"></div>
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-container2">
          <div v-if="borrowerInfo.userNature == 1" class="describe-info borrow-describe margin-t-15">
            <div class="borrow-describe-title aui-border-b border-color margin-b-20">
              <img src="../../../assets/images/finance/details_icon_individual.png" />
              <span>个人信息</span>
            </div>
            <ul class="borrow-describe-content margin-b-20">
              <li>
                <label>姓　　名</label>
                <span>{{ borrowerInfo.userName }}</span>
              </li>
              <li>
                <label>证件号码</label>
                <span>{{ borrowerInfo.idNo }}</span>
              </li>
              <li>
                <label>婚姻状况</label>
                <span>{{ borrowerInfo.maritalStatus }}</span>
              </li>
              <li>
                <label>收入范围</label>
                <span>{{ borrowerInfo.monthIncomeRange }}</span>
              </li>
              <li>
                <label>所在地区</label>
                <span>{{ borrowerInfo.provinceCity }}</span>
              </li>
              <li>
                <label>最高学历</label>
                <span>{{ borrowerInfo.education }}</span>
              </li>
              <li>
                <label>工作年限</label>
                <span>{{ borrowerInfo.workExperience }}</span>
              </li>
              <li>
                <label>有无车产</label>
                <span v-if="borrowerInfo.carStatus == 1">有</span>
                <span v-if="borrowerInfo.carStatus == '0'">无</span>
                <span v-if="borrowerInfo.carStatus == ''"></span>
              </li>
              <li>
                <label>有无房产</label>
                <span v-if="borrowerInfo.houseStatus == 1">有</span>
                <span v-if="borrowerInfo.houseStatus == '0'">无</span>
                <span v-if="borrowerInfo.houseStatus == ''"></span>
              </li>
            </ul>
            <div v-if="borrowerInfo.qualificationList && borrowerInfo.qualificationList.length > 0 && borrowerInfo.flag == 1">
              <div class="borrow-describe-title aui-border-b border-color margin-b-20">
                <img src="../../../assets/images/finance/details_icon_material.png" />
                <span>资料审核状态</span>
              </div>
              <ul class="audit-state margin-b-20">
                <li class="audit-state-title">
                  <span>审核项目</span>
                  <span>状态</span>
                  <span>通过时间</span>
                </li>
                <li v-for="item in borrowerInfo.qualificationList" class="audit-content" v-if="item.status == 1">
                  <span>{{item.name}}</span>
                  <span>已通过</span>
                  <span>{{item.verifyTime | dateFormatFun}}</span>
                </li>
              </ul>
            </div>
          </div>
          <div v-else class="describe-info borrow-describe margin-t-15">
            <div class="borrow-describe-title aui-border-b border-color margin-b-20">
              <img src="../../../assets/images/finance/details_icon_individual.png" />
              <span>企业信息</span>
            </div>
            <ul class="borrow-describe-content margin-b-20">
              <li>
                <label>企业名称</label>
                <span>{{ borrowerInfo.companyName }}</span>
              </li>
              <li>
                <label>企业简称</label>
                <span>{{ borrowerInfo.simpleName }}</span>
              </li>
              <li>
                <label>注册资本</label>
                <span>{{ borrowerInfo.registeredCapital }}元</span>
              </li>
              <li>
                <label>注册地区</label>
                <span>{{ borrowerInfo.address }}</span>
              </li>
              <li>
                <label>办公地点</label>
                <span>{{ borrowerInfo.officeAddress }}</span>
              </li>
              <li>
                <label>成立时间</label>
                <span>{{ borrowerInfo.establishDate | dateFormatFun }}</span>
              </li>
              <li>
                <label>法人代表</label>
                <span>{{ borrowerInfo.legalDelegate }}</span>
              </li>
              <li>
                <label>自然人　</label>
                <span>{{ borrowerInfo.naturalPerson  }}</span>
              </li>
              <li>
                <label>企业法人</label>
                <span>{{ borrowerInfo.lgalPerson}}</span>
              </li>
            </ul>
            <div v-if="borrowerInfo.qualificationList && borrowerInfo.qualificationList.length > 0 && borrowerInfo.flag == 1">
              <div class="borrow-describe-title aui-border-b border-color margin-b-20">
                <img src="../../../assets/images/finance/details_icon_material.png" />
                <span>平台认证</span>
              </div>
              <ul class="audit-state margin-b-20">
                <li class="audit-state-title">
                  <span>审核项目</span>
                  <span>状态</span>
                  <span>通过时间</span>
                </li>
                <li v-for="item in borrowerInfo.qualificationList" class="audit-content">
                  <div v-if="item.status == 1">
                    <span>{{item.name}}</span>
                    <span>已通过</span>
                    <span>{{item.verifyTime | dateFormatFun}}</span>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </mt-tab-container-item>
        <mt-tab-container-item id="tab-container3">
          <div class="describe-info borrow-info margin-t-15">
            <div class="borrow-info-show">
              <img v-for="item in borrowPic" :src="item.filePath" />
              <!--<img src="../../../assets/images/finance/pic_loan_material.png" />-->
            </div>
            <div class="text-center no-data" v-if="borrowPic && borrowPic.length && borrowPic.length == 0">
              <img src="../../../assets/images/finance/pic_loan_material.png">
            </div>
          </div>
        </mt-tab-container-item>
      </mt-tab-container>
    </div>
    <div class="invest-info">
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    data(){
      return {
        active: 'tab-container1',
        borrowerDetail: '',
        borrowerInfo: '',
        borrowPic: []
      }
    },
    created(){
      let getParams = {
        projectId: this.$route.params.projectId,
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid
      }
      // 借款方信息
      this.$http.get(ajaxUrl.borrowerDetail, {params:getParams}).then((res) => {
        this.borrowerDetail = res.data.resData
      })
      // 借款描述
      this.$http.get(ajaxUrl.borrowerInfo, {params:getParams}).then((res) => {
        this.borrowerInfo = res.data.resData
      })
      // 借款资料
      this.$http.get(ajaxUrl.borrowPic, {params:getParams}).then((res) => {
        this.borrowPic = res.data.resData.list
      })
    },
  }
</script>
<style scoped>
  @import "../../../assets/scss/var.scss";
  i.radious{background-color: #ff9545; width: .04rem; height: .04rem; border-radius: .04rem; display: inline-block; margin-right: .08rem; position: relative; bottom: .03rem;}
  .current {
    color: #ff9545;
    border-bottom: 2px solid #ff9545;
  }
  .describe-title {
    width: 100%;
    height: .4rem;
    background: #fff;
  }
  .describe-title li{
    width: 33.33%;
    line-height: .36rem;
    text-align: center;
    display: inline-block;
    float: left;
  }
  .describe-title li span{
    width: .75rem;
    display: block;
    margin: 0 auto;
  }
  .describe-info{
    background: #fff;
    width: 100%;
    padding: 0 5% .15rem;
    float: left;
  }
  .borrow-title{
    padding: .15rem 0 .1rem 0;
    color: #ff9545;
  }
  .borrow-intro { padding-bottom: .2rem; }
  .borrow-name p,.borrow-use p,.borrow-intro p{
    line-height: .28rem;
    color: #333;
  }
  .borrow-describe-title{
    height: .45rem;
    color: #666;
  }
  .borrow-describe-title img{
    height: .2rem;
    float: left;
    margin-top: .125rem;
    margin-right: .1rem;
  }
  .borrow-describe-title span{
    line-height: .45rem;
    float: left;
    color: #ff9545;
  }
  .borrow-describe-content li{
    height: .3rem;
    line-height: .3rem;
  }
  .borrow-describe-content li label{
    margin-right: .15rem;
    color: #666;
  }
  .borrow-describe-content li span{
    color: #333;
  }
  .audit-state{
    float: left;
    width: 100%;
  }
  .audit-state li{
    float: left;
    width: 100%;
    height: .34rem;
    line-height: .34rem;
  }
  .audit-state li span{
    display: inline-block;
    float: left;
    text-align: center;
    width: 33.33%;
    border: 1px solid #fff;
  }
  .audit-state-title{
    background: #f4f3f3;
  }
  .audit-content{
    background: #f9f9f9;
  }
  .borrow-info-show img{
    width: 100%;
    margin-bottom: .1rem;
  }
  .borrow-info-show{
    margin-top: .2rem;
  }
  .no-data { padding:.5rem 0 .3rem;}
</style>
