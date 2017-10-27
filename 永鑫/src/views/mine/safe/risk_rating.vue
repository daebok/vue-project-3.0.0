<!-- 风险评测答题 -->
<template>
  <div class="page" >
    <div v-for="(item, index) in list" class="ques-list">
      <div class="questions">
          {{ index+1 }}.{{ item.questionName }}
      </div>
      <ul class="questions-list" :ref="item.id">
        <li v-for="child in item.list" >
          <label class="aui-flex aui-flex-col aui-flex-middle" >
          <div class="aui-flex-item-1">
            <input type="radio" :name="'ques'+ (index+1)" class="radio-input" :value="'{'+item.id+','+child.id+'}'" :ref="child.id" @click="countItem"/>
            <b class="pseudo-radio aui-border"><i></i></b>
          </div>
          <div class="aui-flex-item-11 padding-r-15">
            <span>{{ child.content }}</span>
          </div>
          </label>
        </li>
      </ul>
    </div>
    <div class="btn-box">
      <mt-button v-if="!submitFlag" size="large" type="default" disabled>提交问卷</mt-button>
      <mt-button v-else size="large" type="danger" @click.native="quesSubmit">提交问卷</mt-button>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import qs from 'qs';

  export default {
    data() {
      return {
        list: [],
        papers: '',
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        submitFlag: false
      };
    },
    created() {
      this.$http.get(ajaxUrl.userRiskPapers, {params: this.getParams}).then((res) => {
        this.list = res.data.resData.list;
        this.papers = res.data.resData.papers;
      })
    },
    methods: {
      countItem(evt) {
        let inputs = document.getElementsByTagName('input');
        let count = 0;
        for (let i = 0; i < inputs.length; i++) {
          if (inputs[i].checked) {
            count++;
          }
        }
        if (count >= this.list.length) {
          this.submitFlag = true;
        }
      },
      quesSubmit(){
        let inputs = document.getElementsByTagName('input');
        let questionContent = '';
        for (let i = 0; i < inputs.length; i++) {
          if (inputs[i].checked) {
            questionContent += inputs[i].value + 'concat';
          }
        }
        let urlParams = Object.assign(this.getParams, { papersId: this.papers.id, questionContent: questionContent });
        this.$http.post(ajaxUrl.doUserRiskPapers, qs.stringify(urlParams)).then((res) => {
          let path = '/mine/safe/risk_result?levelName=' + res.data.resData.levelName + '&level=' + res.data.resData.level;
          if (this.$route.query.from) {
            path = path + '&from=' + this.$route.query.from + '&id=' + this.$route.query.id
          }
          this.$router.push(path);
        })
      }
    }
  }
</script>

<style type="text/css" scoped>
  .mt-progress{
    height: 3px;
  }
  .mt-progress-progress{
    background: #fa5c2a;
  }
  .questions{
    font-size: .18rem;
    padding: .25rem .15rem .2rem .15rem;
    line-height: 1.3;
    color: #333;
    text-align: justify;
  }
  .questions-list li{
    padding: 0 .15rem;
    height: .64rem;
    background: #fff;
    position: relative;
    margin-top: .1rem;
  }
  .questions-list li:first-child { margin-top: 0;}
  .questions-list li label {
    position: absolute;
    height: 100%;
    width: 95%;
  }
  .questions-list li span{
    font-size: .15rem;
    color: #333;
  }
  .radio-input{
    display: none;
  }
  .radio-input+ .pseudo-radio {
    width: .15rem;
    height: .15rem;
    display: inline-block;
    text-align: center;
    line-height: .1rem;
  }
  .radio-input+ .pseudo-radio i {
    width: .11rem;
    height: .11rem;
    display: inline-block;
    background: #fff;
    border-radius: 50%;
    margin-top: .02rem;
  }
  .radio-input:checked + .pseudo-radio i {
    background: #fa5c2a;
  }
  .pseudo-radio.aui-border:after {
    border-radius: 50%;
    border: 1px solid #666;
  }
  .radio-input:checked + .pseudo-radio.aui-border:after {
    border-color: #fa5c2a;
  }
  .btn-box{
    margin:.3rem .15rem;
  }
</style>
