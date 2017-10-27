<!-- 风险评测开始 -->
<template>
  <div class="page" >
    <div class="rsk-content">
      <p>尊敬的用户：</p>
      <p class="rsk-content-1">
        为了便于了解您自身的风险承受能力，我们将根据以下{{ resdata.questionNum }}个问题对您进行风险承受能力评测。根据评测结果，您的风险承受能力将属于以下{{ length }}种类型中的一种：
        <span v-for="(item,index) in resdata.configList">
          {{ item.userRiskLevelName }}<em v-if="(index+1) != length">、</em>
        </span>。
        为了给您提供更好的资产选择，请您认真作答，感谢您的配合。
      </p>
      <router-link :to="{ name: 'riskRating', query: $route.query }">
        <mt-button type="danger" class="rsk-btn">开始评测</mt-button>
      </router-link>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';

  export default {
    data() {
      return {
        resdata: '',
        length: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        }
      };
    },
    created() {
      this.$http.get(ajaxUrl.userRiskPapers, {params: this.getParams}).then((res) => {
        this.resdata = res.data.resData;
        this.length = res.data.resData.configList.length;
      })
    }
  }
</script>

<style type="text/css" scoped>
  .rsk-content{
    margin-top: .35rem;
    padding: 0 .15rem;
  }
  .rsk-content p{
    font-size: .16rem;
    color: #666;
    line-height: 1.5;
  }
  .rsk-content-1{
    text-indent: .32rem;
  }
  .rsk-btn{
    width: 100%;
    background: #F95A28;
    margin-top: .3rem;
    height: .45rem;
    line-height: .45rem;
    padding: 0;
  }
</style>
