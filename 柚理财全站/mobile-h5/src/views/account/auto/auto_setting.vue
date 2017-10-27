<template>
  <div class="page">
    <mt-header class="bar-nav" title="自动投资参数">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="margin-t-10">
      <rd-money type="tel" v-model="inputMoney" maxlength="10" pattern="\d*" decimal label="单日最高可投(元)" placeholder="请输入金额" specInput="rd-money" inputType="number"></rd-money>
    </div>
    <div class="earning-mode types margin-t-10">
      <p>收益方式</p>
      <div class="mode-container">
        <label v-for="item in types">
          <input type="checkbox" class="checkbox hide" name="" v-model="check" :value="item.itemValue">
          <span class="mode-col">{{item.itemName}}</span>
        </label>
        <!--<label>-->
          <!--<input type="checkbox" class="checkbox hide" name="" v-model="check" value="2">-->
          <!--<span class="mode-col">一次性还款</span>-->
        <!--</label>-->
      </div>
    </div>
    <div class="earning-mode margin-t-10">
      <p>投资期限</p>
      <div class="date-form clearfix margin-b-15">
        <div @click="switchType('monthType')" class="month-div" :class="{'active': monthType == 1}">月范围</div>
        <input type="tel" pattern="\d*" placeholder="请输入" v-model="limitMin" class="month-range" :class="{'bg-f5': monthType == 0}" maxlength="2" :readonly="monthType == 0" :disabled="monthType == 0" @input="handleInput('limitMin', $event)"/>
        <div class="center-line"></div>
        <input type="tel" pattern="\d*" placeholder="请输入" v-model="limitMax" class="month-range" :class="{'bg-f5': monthType == 0}" maxlength="2" :readonly="monthType == 0" :disabled="monthType == 0" @input="handleInput('limitMax', $event)" />
        <i>个月</i>
      </div>
      <div class="date-form clearfix">
        <div @click="switchType('dayType')" class="day-div" :class="{'active': dayType == 1}">天范围</div>
        <input type="tel" pattern="\d*" placeholder="请输入" v-model="dayLimitMin" class="month-range" :class="{'bg-f5': dayType == 0}" maxlength="3" :readonly="dayType == 0" :disabled="dayType == 0" @input="handleInput('dayLimitMin', $event)"/>
        <div class="center-line"></div>
        <input type="tel" pattern="\d*" placeholder="请输入" v-model="dayLimitMax" class="month-range" :class="{'bg-f5': dayType == 0}" maxlength="3" :readonly="dayType == 0" :disabled="dayType == 0" @input="handleInput('dayLimitMax', $event)"/>
        <i>天</i>
      </div>
    </div>
    <div class="margin-t-10">
      <rd-apr type="text" maxlength="6" v-model="inputApr" pattern="\.*" label="投资收益(%)" placeholder="请输入收益" ></rd-apr>
    </div>
    <div class="ear-b">
      <div class="ear-col">
        <label>
          <input type="checkbox" class="checkbox2 hide" value="1" name="realizeUseful" v-model="realizeUseful" />
          <em></em>
        </label>
        <span>仅可变现产品</span>
      </div>
      <div class="ear-col">
        <label>
          <input type="checkbox" class="checkbox2 hide" value="1" name="bondUseful" v-model="bondUseful" />
          <em></em>
        </label>
        <span>仅可转让产品</span>
      </div>
    </div>
    <div class="margin-t-30 margin-lr-15 margin-b-15">
      <mt-button v-if="!inputFinish" type="default" size="large" class="update-btn" disabled>保存设置</mt-button>
      <mt-button v-else type="danger" @click.native="submitAjax" size="large" class="update-btn">
        <loading v-if="submitData" :task="false"></loading>
        <span v-else>保存设置</span>
      </mt-button>
    </div>
  </div>
</template>
<script>
  import * as ajaxUrl from '../../../ajax.config'
  import Loading from '../../../components/Loading.vue'
  import RdMoney from '../../../components/MoneyInput'; // 引入公共表单控件组件
  import RdApr from '../../../components/AprInput'; // 引入公共表单控件组件
  import qs from 'qs'
export default {
  created(){
    // this.$indicator.open({spinnerType: 'fading-circle'}) //提示初始化加载
    let getParams = {
      userId: this.$store.state.user.userId,
      __sid: this.$store.state.user.__sid
    }
    //获取收益方式类型
    this.$http.get(ajaxUrl.interestStyle).then((res) => {
      this.types = res.data.resData.repayStyles
    })
    //获取自动投标设置
    this.$http.get(ajaxUrl.autoInvestRule, {params: getParams}).then((res) => {
      this.rule = res.data.resData.rule
      if(this.rule){
        this.inputMoney = this.rule.amountDayMax
        let styles = this.rule.repayStyles.split(',')
        this.check = styles;
        this.monthType = this.rule.monthType;
        this.dayType = this.rule.dayType;
        this.limitMin = this.rule.monthLimitMin;
        this.limitMax = this.rule.monthLimitMax;
        this.dayLimitMin = this.rule.dayLimitMin;
        this.dayLimitMax = this.rule.dayLimitMax;
        this.inputApr = this.rule.aprMin;
        this.bondUseful = this.rule.bondUseful;
        this.realizeUseful = this.rule.realizeUseful;
      }
    })
  },
  methods: {
    switchType(arg){
      this[arg] = this[arg] == 1 ? '0' : 1
      if(arg == 'monthType'){
        this.limitMin = ''
        this.limitMax = ''
      }else if(arg == 'dayType'){
        this.dayLimitMin = ''
        this.dayLimitMax = ''
      }
    },
    handleInput(item, evt) {
      let formatValue = evt.target.value;
      formatValue = formatValue.replace(/\D*$/, '');
      this[item] = formatValue;
    },
    submitAjax(){
      // 判断投资期限
      if(this.monthType == 1){
        if((this.limitMin != '' && this.limitMin > 0) && (this.limitMax != '' && this.limitMax > 0)){
        }else{
          this.$toast('请输入月期限')
          return
        }
      }
      if(this.dayType == 1){
        if((this.dayLimitMin != '' && this.dayLimitMin > 0) && (this.dayLimitMax != '' && this.dayLimitMax > 0)){
        }else{
          this.$toast('请输入日期限')
          return
        }
      }
      let bondUserVal = this.bondUseful === true ? 1 : 0
      let realizeUserVal = this.realizeUseful === true ? 1 : 0
      let params = {
        userId: this.$store.state.user.userId,
        __sid: this.$store.state.user.__sid,
        amountDayMax: this.inputMoney,
        aprMin: this.inputApr,
        bondUseful: bondUserVal,
        dayLimitMax: this.dayLimitMax,
        dayLimitMin: this.dayLimitMin,
        dayType: this.dayType,
        monthLimitMax: this.limitMax,
        monthLimitMin: this.limitMin,
        monthType: this.monthType,
        realizeUseful: realizeUserVal,
        'repayStyle[]': this.check.join(',')
      }
      this.submitData = true;
      this.$http.post(ajaxUrl.addAutoInvest, qs.stringify(params)).then((res) => {
        if(res.data.resMsg == '开启成功'){
          this.$toast('自动投资设置成功，5分钟后起效')
          this.$router.go(-1)
        }else{
          this.$toast(res.data.resMsg)
        }
        this.submitData = false;
      })
    }
  },
  watch: {
    inputMoney(newV, oldV){
      if(newV.toString().search(/^([1-9]\d*|0)/) == -1){ //解决刚开始输入不符合规则的bug
        this.inputMoney = ''
      }
    },
    inputApr(newV, oldV){
      if(newV.toString().search(/^([1-9]\d*|0)/) == -1){ //解决刚开始输入不符合规则的bug
        this.inputApr = ''
      }
    }
  },
  data(){
    return {
      rule: '',
      types: '',
      inputMoney: '',
      check: [],
      monthType:0,
      dayType:0,
      limitMin: '',
      limitMax: '',
      dayLimitMin: '',
      dayLimitMax: '',
      bondUseful: '',
      realizeUseful: '',
      inputApr: '',
      submitData: false
    }
  },
  computed: {
    inputFinish(){
      let flag = false;
      if(this.inputMoney != '' && this.check.length != 0 && this.inputApr != '')
      {
        if ((this.limitMin != '' && this.limitMax != '') || (this.dayLimitMin != '' && this.dayLimitMax != ''))
        {
            flag = true;
        }
      }
      return flag
    }
  },
  components:{Loading,RdMoney,RdApr}
}
</script>

<style scoped>
  .auto-money-title{  width: 35%;  }
  .auto-money{  width: 60%;  }
  .earning-mode{
    width: 100%;
    height: 1.45rem;
    background: #fff;
    padding: 0 .15rem;
  }
  .earning-mode.types {
    height:auto;
    padding: 0 .15rem .15rem;
  }
  .earning-mode p{
    line-height: .45rem;
    color: #666;
  }
  .mode-container,.ear-b{
    display: flex;
    flex-flow: row wrap;
    align-items: stretch;
  }
  .ear-b{ justify-content: space-between; }
  .mode-container{
    width: 100%;
    height: .9rem;
  }
  .mode-container label { margin-right: .1rem; }
  .mode-col{
    padding: 0 .06rem;
    line-height: .3rem;
    border: 1px solid #DDD;
    border-radius: .05rem;
    display: block;
  }
  .checkbox:checked + .mode-col {
    border-color: #F95A28;
    color: #F95A28;
  }
  .none-div{
    width: .7rem;
    border: none;
  }
  .date-form{
    float: left;
    width: 100%;
  }
  .date-form .active {
    border-color: #F95A28;
    color: #F95A28;
  }
  .month-div,.day-div{
    height: .31rem;
    line-height: .31rem;
    width: .54rem;
    border: 1px solid #DDD;
    text-align: center;
    color: #333;
    border-radius: .05rem;
    display: inline;
    float: left;
    margin-right: .15rem;
  }
  .month-range{
    width: .95rem;
    height: .33rem;
    display: inline;
    float: left;
    border: 1px solid #F5F5F5;
    outline: none;
    text-align: center;
    -webkit-appearance: none;
  }
  .bg-f5{background: #F5F5F5;}
  .center-line{
    height: 1px;
    width: .2rem;
    float: left;
    display: inline;
    margin: .16rem .07rem;
    background: #CDCDCD;
  }
  .date-form i{
    display: inline-block;
    float: left;
    height: .33rem;
    line-height: .33rem;
    padding: 0 .08rem;
  }
  .earning-rate{  width: 60%;  }
  .form-text i{
    float: right;
    line-height: .45rem;
  }
  .ear-b{
    width: 100%;
    padding: 0 .15rem;
    margin-top: .15rem;
  }
  .ear-col{
    height: .28rem;
    line-height: .28rem;
  }
  .ear-col em{
    width: .15rem;
    height: .15rem;
    background:url('../../../assets/images/public/protocol_n.png') no-repeat;
    background-size: contain;
    display: inline-block;
    vertical-align: middle;
  }
  .checkbox2:checked + em {background-image: url('../../../assets/images/public/protocol_s.png')}
  .ear-col img{
    width: .15rem;
    vertical-align: middle;
  }
  .form-text {position:relative;}
  .rd-field-clear {position:absolute;right: .15rem;opacity:.2;top: 0;}
  .rd-field-clear.special-error {right: .35rem;}
</style>
