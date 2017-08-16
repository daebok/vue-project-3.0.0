<template>
  <div class="page" >
    <mt-header v-if="!hideHeader" class="bar-nav aui-border-b" title="风险评测">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="rsk-content">
      <p>尊敬的用户：</p>
      <p class="rsk-content-1">
        为了便于了解您自身的风险承受能力，我们将根据以下{{resdata.questionNum}}个问题对您进行风险承受能力评测。根据评测结果，您的风险承受能力将属于以下{{length}}种类型中的一种：
        <span v-for="(item,index) in resdata.configList">
          {{item.userRiskLevelName}}<em v-if="(index+1) != length">、</em>
        </span>。
        为了给您提供更好的资产选择，请您认真作答，感谢您的配合。
      </p>

      <mt-button type="danger" @click="toRisk" class="rsk-btn">开始评测</mt-button>

    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import  hideHead from '../../../mixins/hideHeader'
  export default {
    mixins: [ hideHead ],
    data(){
      return {
        resdata: '',
        length: '',
        getParams: {
          userId: this.$store.state.user.userId || this.$route.query.userId,
          __sid: this.$store.state.user.__sid || this.$route.query.__sid
        }
      }
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
      this.$http.get(ajaxUrl.userRiskPapers, {params: this.getParams}).then((res) => {
        this.resdata = res.data.resData;
        this.length = res.data.resData.configList.length;
        this.$indicator.close() // 关闭提示
      })
    },
    methods: {
      toRisk() {
        let name = 'riskRating'
        if(this.hideHeader){
          name = 'appRisk'
        }
        this.$router.push({name: name, query: this.$route.query})
      }
    }
  }
</script>
<style scoped>
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
