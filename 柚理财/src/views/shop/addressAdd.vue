<template>
  <div class="page">
    <mt-header class="bar-nav aui-border-b" title="编辑地址">
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="">
      <div class="apt-form">
        <div class="form-area">
          <ul>
            <li class="aui-border-b">
              <rd-field type="text" v-model="form.name" label="收货人" placeholder="姓名不超过20个字符" :attr="{maxlength: 20}"></rd-field>
            </li>
            <li class="aui-border-b">
              <rd-field type="tel" v-model="form.tel" label="联系方式" placeholder="请输入手机号码" name="mobile" class="rd-login"></rd-field>
            </li>
          </ul>
        </div>
        <div class="apt-input aui-border-b">
          <label>所在位置</label>
          <input type="text" name="" v-model="form.area" placeholder="请选择" @click="open('picker')" readonly onfocus="this.blur()"/>
          <img src="../../assets/images/public/arrow_down.png" class="arrow-down">
        </div>
        <div class="address">
          <textarea v-model="form.address" maxlength="50" placeholder="请填写详细地址，不超过50个字符"></textarea>
        </div>
      </div>
      <mt-cell title="设为默认收货地址" class="margin-t-15">
        <label class="mint-switch">
          <input class="mint-switch-input" type="checkbox" v-model="switchVal">
          <span class="mint-switch-core"></span>
        </label>
      </mt-cell>
    </div>
    <div ref="btn" v-if="!form.name||!form.tel||!form.area||!form.address" class="bottom disabled" :style="{marginTop: marginTopH + 'px'}">保存并使用</div>
    <div ref="btn" v-else class="bottom" @click="checkForm" :style="{marginTop: marginTopH + 'px'}">保存并使用</div>
    <city-picker
      ref="picker"
      @confirm="handleChange">
    </city-picker>

  </div>
</template>

<script>
  import * as ajaxUrl from '../../ajax.config'
  import RdField from '../../components/FieldInput.vue'
  import cityPicker from '../../components/CityPicker.vue'
  import validator from '../../mixins/formValidator'
  import qs from 'qs'
  export default {
    mixins: [validator],
    data () {
      return {
        areaJson: '',
        form: {
          name: '',
          tel: '',
          area: '',
          address: ''
        },
        switchVal: true,
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        marginTopH: 0
      }
    },
    created () {
      this.$http.get(ajaxUrl.bespeak).then((res) => {
        this.areaJson = res.data.resData.areaJson
      })
      var obj = this.$route.query
      if(this.$route.query.name) {
        document.title = '编辑地址'
        this.form.name = obj.name
        this.form.tel = obj.mobile
        this.form.address = obj.address
        this.form.area = obj.provinceStr + obj.cityStr + obj.areaStr
        if(obj.isDefult == 1) this.switchVal = true
        if(obj.isDefult == 0) this.switchVal = false
      }
    },
    mounted() {
      this.marginTopH = document.documentElement.clientHeight - this.$refs.btn.getBoundingClientRect().top - this.$refs.btn.clientHeight;
    },
    methods: {
      open(picker) {
        setTimeout(() => {  // 解决与手机自动弹出键盘的冲突
          this.$refs[picker].open();
        }, 500)
      },
      handleChange(value) {
        this.form.area = value
      },
      checkForm(){
        if(this.regexPhone(this.form.tel)){
          let val = this.switchVal ? 1 : 0
          let params = {
            name: this.form.name,
            mobile: this.form.tel,
            address: this.form.address,
            'zone[]': this.areaValue,
            isDefult: val
          }
          Object.assign(params, this.getParams)
          let reqUrl = ajaxUrl.addReceivingInfo
          if(this.$route.query.name) {
            reqUrl = ajaxUrl.editReceivingInfo
            Object.assign(params, {id: this.$route.query.id})
          }
          this.$http.post(reqUrl, qs.stringify(params)).then((res) => {
            this.$toast(res.data.resMsg)
            if(res.data.resCode == 39321){
              this.$router.go(-1)
            }
          })
        }
      }
    },
    computed: {
      areaValue(){  // 获取省市区的value值
        let temp = []
        if(this.form.area){
          var mockArr = JSON.parse(this.areaJson)
          var zoneArr = document.getElementById('zone').value.split(',')
          var obj = this.$route.query
          if(!document.getElementById('zone').value && obj ){ // 修改地址时先判断初始值
            return obj.province + ',' + obj.city + ',' + obj.area
          }
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
    components: {RdField, cityPicker}
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  @import "../../assets/scss/var";
  .apt-form {
    margin: 0 0 .15rem;
    background: #fff;
    .apt-input {
      line-height: .5rem;
      padding: 0 .15rem;
      position: relative;
      label { color: #333; display: inline-block; width: .76rem; }
      input { width: 65%; border: 0; }
      .arrow-down {
        position: absolute;
        right: 15px;
        top: 50%;
        width: 12px;
        margin-top: -6px;
      }
    }
    .address {
      height: 1.25rem;
      padding: .18rem .25rem .1rem .15rem;
      textarea { width: 100%; height: 100%; resize: none; border: 0; }
    }
  }
  .bottom {
    /*position: absolute;*/
    bottom: 0;
    width: 100%;
    line-height: .5rem;
    background: $main-color;
    font-size: .17rem;
    color: #fff;
    text-align: center;
    &.disabled { background: #ccc; }
  }
</style>