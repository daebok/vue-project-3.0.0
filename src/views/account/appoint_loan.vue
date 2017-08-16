<template>
  <div class="" id="appoint_repay">
    <div class="page">
      <mt-header class="bar-nav" title="预约借款">
        <mt-button slot="left" icon="back" v-back-link></mt-button>
      </mt-header>
      <div class="appointment">
        <ul class="apt-top">
          <li class="submit-apt aui-border">
            <img src="../../assets/images/money/reserve_icon_submit_red.png" />
            <p>提交申请</p>
            <i class="current-trigon"></i>
          </li>
          <li class="aui-border">
            <img src="../../assets/images/money/reserve_icon_audit_gray.png" />
            <p>审核材料</p>
            <i class="trigon"></i>
          </li>
          <li class="aui-border">
            <img src="../../assets/images/money/reserve_icon_sign_gray.png" />
            <p>签署协议</p>
            <i class="trigon"></i>
          </li>
          <li class="aui-border">
            <img src="../../assets/images/money/reserve_icon_issue_gray.png" />
            <p>发布借款</p>
            <i class="trigon"></i>
          </li>
          <li class="aui-border">
            <img src="../../assets/images/money/reserve_icon_success_gray.png" />
            <p>借款成功</p>
          </li>
        </ul>
        <div class="apt-form margin-t-15">
          <div class="apt-input aui-border-b">
            <label>姓名</label>
            <input type="text" v-model="form.name" name="" maxlength="10" placeholder="请输入姓名" />
          </div>
          <div class="apt-input aui-border-b">
            <label>性别</label>
            <label class="rd-radio-box">
              <input type="radio" class="radio-input" value="M" v-model="form.sex" />
              <b class="pseudo-radio aui-border"><i></i></b>
              <em>男</em>
            </label>
            <label class="rd-radio-box">
                <input type="radio" class="radio-input" name="" value="F" v-model="form.sex" />
                <b class="pseudo-radio aui-border"><i></i></b>
                <em>女</em>
            </label>
          </div>
          <div class="apt-input aui-border-b">
            <label>手机号码</label>
            <input type="tel" name="" maxlength="11" v-model="form.tel" placeholder="请输入手机号码" />
          </div>
          <div class="apt-input aui-border-b">
            <label>所在城市</label>
            <input type="text" name="" v-model="form.area" placeholder="请选择您所在城市" @click="open('picker1')" readonly onfocus="this.blur()"/>
            <img src="../../assets/images/public/arrow_down.png" class="arrow-down">
          </div>
          <div class="apt-input aui-border-b">
            <label>借款金额</label>
            <input type="tel" maxlength="10" @input="numberType" v-model="form.money" placeholder="请输入借款金额" />
          </div>
          <div class="apt-input">
            <label>借款期限</label>
            <input type="text" name="" v-model="form.timeLimit" @click="openLimit" placeholder="请选择借款期限" readonly onfocus="this.blur()"/>
            <img src="../../assets/images/public/arrow_down.png" class="arrow-down">
          </div>
        </div>
        <div class="btn-box">
          <mt-button v-if="!form.name||!form.tel||!form.area||!form.money||!form.timeLimit" type="default" size="large" disabled >确定</mt-button>
          <mt-button v-else type="danger" @click.native="checkForm" size="large">确定</mt-button>
        </div>
      </div>
    </div>
    <city-picker
      ref="picker1"
      @confirm="handleChange">
    </city-picker>
    <mt-popup v-model="popupVisible2" position="bottom" class="mint-popup">
      <mt-picker :slots="timeLimit_slots" @change="onValuesChange" show-toolbar>
        <div class="picker-toolbar aui-border-b">
          <span class="mint-datetime-action mint-datetime-cancel" @click="popupVisible2=false">取消</span>
          <span class="mint-datetime-action mint-datetime-confirm" @click="showhide">确定</span>
        </div>
      </mt-picker>
    </mt-popup>
  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config'
  import cityPicker from '../../components/CityPicker.vue'
  import validator from '../../mixins/formValidator'
  import qs from 'qs'
  export default {
    mixins: [validator],
    methods: {
      open(picker) {
        setTimeout(() => {  // 解决与手机自动弹出键盘的冲突
          this.$refs[picker].open();
        }, 500)
      },
      openLimit(){
        setTimeout(() => {  // 解决与手机自动弹出键盘的冲突
          this.popupVisible2 = true
        }, 500)
      },
      numberType(evt){
        let formatValue = evt.target.value;
        this.form.money = formatValue.replace(/\D*$/, '');
      },
      handleChange(value) {
        this.form.area = value
      },
      onValuesChange(picker,values) {this.form.timeLimit = values[0]},
      showhide() {
        this.popupVisible2 = !this.popupVisible2;
        if(this.form.timeLimit == ''){
          this.form.timeLimit = '1-3个月'
        }
      },
      checkForm(){
        if(this.chName(this.form.name) && this.regexPhone(this.form.tel)){
          if(this.form.money < this.resdata.borrowMinAccount || this.form.money > this.resdata.borrowMaxAccount){
            this.$toast('借款金额必须大于等于'+this.resdata.borrowMinAccount+'小于等于'+this.resdata.borrowMaxAccount)
            return
          }
          let params = {
            '__sid': this.resdata.__sid,
            contactName: this.form.name,
            limitTime: this.timeLimitValue,
            mobile: this.form.tel,
            money: this.form.money,
            sex: this.form.sex,
            zone: this.areaValue,
            borrowBespeakAddToken: this.resdata.borrowBespeakAddToken
          }
          this.$http.post(ajaxUrl.bespeakAdd, qs.stringify(params)).then((res) => {
            if(res.data.resMsg.match('您已提交借款申请')){
              this.$messagebox({
                title: '提交成功',
                message: res.data.resMsg
              }).then(action => {
                this.$router.go(-1)
              });
            }else{
              this.$toast(res.data.resMsg)
            }
          })
        }
      },
    },
    created(){
      this.$indicator.open({spinnerType: 'fading-circle'})
      this.$http.get(ajaxUrl.bespeak).then((res) => {
        this.$indicator.close()
        this.resdata = res.data.resData
      })
    },
    computed: {
      timeLimitValue(){  //获取期限的value值
        let temp = ''
        if(this.form.timeLimit) {
          var mockArr = JSON.parse(this.resdata.bespeakTimeList)
          mockArr.forEach(val => {
            if (val.itemName == this.form.timeLimit) {  //找省或直辖市的value
              temp = val.itemValue
            }
          })
        }
        return temp
      },
      areaValue(){  // 获取省市区的value值
        let temp = []
        if(this.form.area){
          var mockArr = JSON.parse(this.resdata.areaJson)
          var zoneArr = document.getElementById('zone').value.split(',')
          mockArr.forEach(val => {
             if(val.label == zoneArr[0]){  //找省或直辖市的value
               temp[0] = val.value
             }
             val.children.forEach(child => {
               if(child.label == zoneArr[1]){
                 temp[1] = child.value
                 if(zoneArr[2]){ //判断是否有第三个
                   child.children.forEach(child2 => {
                     if(child2.label == zoneArr[2]){
                       temp[2] = child2.value
                     }
                   })
                 }
               }
             })
          })
        }
        return temp.join(',')
      }
    },
    data(){
      return {
        form:{
          name:'',
          tel:'',
          area:'',
          money:'',
          timeLimit:'',
          sex: 'M',
        },
        resdata: '',
        popupVisible2: false,
        timeLimit_slots: [{
          flex: 1,
          values: ['1-3个月','3-6个月','6-12个月','12个月以上'],
          className: 'slot1',
          textAlign: 'center'
        }]
      }
    },
    components: {cityPicker}
  }
</script>

<style scoped>
  .appointment{
    width: 100%;
    height: 100%;
  }
  .apt-top{
    width: 100%;
    height: .5rem;
  }
  .apt-top li{
    float: left;
    display: inline-block;
    width: 20%;
    height: 100%;
    position: relative;
    text-align: center;
    font-size: .11rem;
    color: #BBB;
    background: #eaeaea;
  }
  .submit-apt{
    background: #fff!important;
    color: #666!important;
  }
  .apt-top li img{
    height: .19rem;
    width: .19rem;
    margin-top: .07rem;
  }
  .current-trigon{
    position: absolute;
    top: .19rem;
    right: -0.05rem;
    width: .11rem;
    height: .11rem;
    background: #fff;
    transform: rotate(45deg);
    border-top: 1px solid #DEDEDE;
    border-right: 1px solid #DEDEDE;
    z-index: 1;
  }
  .trigon{
    position: absolute;
    top: .19rem;
    right: -0.05rem;
    width: .11rem;
    height: .11rem;
    background: #eaeaea;
    transform: rotate(45deg);
    border-top: 1px solid #DEDEDE;
    border-right: 1px solid #DEDEDE;
    z-index: 1;
  }
  .apt-form{
    width: 100%;
    padding: 0 .15rem;
    background: #fff;
  }
  .apt-input{
    height: .48rem;
    width: 100%;
    position: relative;
  }
  .arrow-down {
    position: absolute;
    right: 0;
    top: 50%;
    width: 12px;
    margin-top: -6px;
  }
  .apt-input label{
    display: inline-block;
    float: left;
    line-height: .48rem;
    width: 30%;
  }
  .apt-input input:not([type="radio"]) {
    float: left;
    width: 70%;
    border: none;
    outline: none;
    height: .3rem;
    line-height: .3rem;
    margin-top: .09rem;
  }
  .rd-radio-box {
    line-height: .48rem;
    display: inline-block;
    width: .6rem;
  }
  .radio-input{  display: none;  }
  .radio-input+ .pseudo-radio {
    width: .15rem;
    height: .15rem;
    display: inline-block;
    text-align: center;
    line-height: .1rem;
    vertical-align: middle;
  }
  .radio-input+ .pseudo-radio i {
    width: .11rem;
    height: .11rem;
    display: inline-block;
    background: #fff;
    border-radius: 50%;
    margin-top: .02rem;
  }
  .btn-box {margin: .3rem .15rem 0;}
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
  /*下拉组件样式*/
  .mint-datetime-confirm,.mint-datetime-cancel{
    color: #666;
  }
  .mint-datetime-cancel {
    text-align: left;
    padding-left: .25rem;
  }
  .mint-datetime-confirm {
    text-align: right;
    padding-right: .25rem;
  }
  .mint-popup {
    width: 100%;
  }
  .picker-slot-wrapper, .picker-item {
    backface-visibility: hidden;
  }
  .mint-button-text{
    width: 100%!important;
    text-align: left;
  }
</style>
