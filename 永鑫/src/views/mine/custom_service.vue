<!-- 客户服务 by fenglei -->
<template>
  <div class="page">
    <div class="service">
      <div class="service-tel margin-t-10">
        <div class="tel-left">客服热线</div>
        <div class="tel-center">
          <p class="tel-number">{{ data.customerHotline }}</p>
          <p class="service-time">服务时间8:00-21:00</p>
        </div>
        <div class="tel-right">
          <a class="tel-btn" :href="'tel:'+ data.customerHotline">
            <img src="../../assets/images/me/icon_phone.png">
            <i>立即拨打</i>
          </a>
        </div>
      </div>
      <ul class="service-list margin-t-10">
        <li class="aui-border-b">
          <label>微信公众号</label>
          <span>{{ data.webIndexWechat }}</span>
        </li>
        <li class="aui-border-b">
          <label>新浪微博</label>
          <span>{{ data.webIndexSinaMicroblog }}</span>
        </li>
        <li>
          <label>客服QQ</label>
          <span>{{ data.webIndexQq }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config';

  export default {
    data() {
      return {
        data: ''
      };
    },
    created() {
      this.$http.get(ajaxUrl.customerService).then((res) => {
        this.data = res.data.resData;
      })
    }
  }
</script>

<style type="text/css" scoped>
  .service-tel{
    background: #fff;
    height: .78rem;
    padding: 0 .15rem;
    display: flex;
  }
  .tel-left{
    font-size: .16rem;
    color: #333;
    padding-top: .2rem;
    margin-right: .1rem;
    line-height: 1;
  }
  .tel-center{
    margin-right: .2rem;
  }
  .tel-number{
    font-size: .16rem;
    color: #D43238;
    margin-top: .2rem;
    line-height: 1;
  }
  .service-time{
    line-height: 1;
    font-size: .13rem;
    color: #999;
    margin-top: .1rem;
  }
  .tel-btn{
    display: block;
    width: 1.03rem;
    background: #D43238;
    height: .33rem;
    line-height: .33rem;
    margin-top: .225rem;
    border-radius: .15rem;
    color: #fff;
    font-size: .15rem;
  }
  .tel-btn img{
    width: .18rem;
    height: .18rem;
    vertical-align: middle;
    margin-left: .07rem;
  }
  .service-list{
    padding: 0 .15rem;
    background: #fff;
  }
  .service-list li{
    height: .48rem;
    line-height: .48rem;
  }
  .service-list li label{
    font-size: .16rem;
    color: #333;
  }
  .service-list li span{
    float: right;
    color: #999;
    font-size: .13rem;
  }
</style>
