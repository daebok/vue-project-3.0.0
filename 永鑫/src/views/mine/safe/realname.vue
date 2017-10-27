<!-- 实名认证 by fenglei -->
<template>
  <div>
    <div class="form-area">
      <div class="warm-tip">
        <h5 class="color-666">温馨提示：</h5>
        <p class="color-999">仅限本人，开通后不可更改，信用卡仅作为身份查询，并不用于购买理财产品</p>
      </div>
      <form>
        <ul class="margin-t-10">
          <li>
            <rd-field type="text" name="realName" v-model="realname" label="真实姓名" :attr="{ maxlength: 10 }" placeholder="请输入真实姓名"></rd-field>
          </li>
          <li>
            <rd-field type="text" name="idNo" v-model="idNo" label="身份证号" :attr="{ maxlength: 18 }" placeholder="请输入身份证号码"></rd-field>
          </li>
          <li>
            <rd-field type="tel" name="cardNum" v-model="creditNo" label="信用卡号" :attr="{ maxlength: 16 }" placeholder="仅支持本人持有的信用卡"></rd-field>
          </li>
          <li class="area">
            <span class="rd-label">银行地址</span>
            <div @click="pickerLoaded" class="province">
              <label :class="{ 'active': provinceCode != ''}">{{ provinceDisplay }}</label>
              <img src="../../../assets/images/invest/btn_paixu_01.png">
            </div>
            <div @click="pickerLoaded" class="city">
              <label :class="{ 'active': provinceCode != ''}">{{ cityDisplay }}</label>
              <img src="../../../assets/images/invest/btn_paixu_01.png">
            </div>
          </li>
          <li>
            <rd-field type="tel" name="mobile" v-model="mobileVal" label="预留手机" :attr="{ maxlength: 11 }" placeholder="信用卡和绑卡预留手机号必须一致" specInput="longInput"></rd-field>
          </li>
          <li>
            <rd-field type="tel" name="code" v-model="codeVal" :attr="{ maxlength: 6 }" label="验证码　 " placeholder="请输入验证码" specInput="codeInput">
              <button v-if="!mobileVal" class="yzm color-999 aui-border-l ellipsis" disabled>获取验证码</button>
              <button v-else @click.prevent="getCode" class="yzm aui-border-l ellipsis">获取验证码</button>
            </rd-field>
          </li>
        </ul>
      </form>
      <div class="protocol-box">
        <span @click="switchImg">
            <span v-if="switch_status">
              <img src="../../../assets/images/fund/xieyi_check_02.png" />
            </span>
            <span v-else>
              <img src="../../../assets/images/fund/xieyi_check_01.png" />
            </span>
        </span>
        我已阅读并同意
        <a v-for="item in protocolList" @click="readProtocol(item.id, item.name)" class="protocol">《{{ item.name }}》</a>
      </div>
      <div class="margin-lr-15">
        <mt-button v-if="realname && idNo && creditNo && codeVal && mobileVal && provinceCode" @click.native="confirmSub" type="danger" size="large" class="confirm-btn">下一步</mt-button>
        <mt-button v-else type="default" size="large" class="confirm-btn" disabled>下一步</mt-button>
      </div>
      <mt-popup v-model="popupVisible" position="bottom" class="popup">
        <mt-picker :slots="slots" @change="onValuesChange" :show-toolbar="true">
          <div class="picker-toolbar aui-border-b">
            <span class="mint-datetime-action mint-datetime-cancel" @click="popupVisible = false">取消</span>
            <span class="mint-datetime-action mint-datetime-confirm" @click="onValuesSure">确定</span>
          </div>
        </mt-picker>
      </mt-popup>
      <!-- S协议内容隐藏域 -->
      <div class="protocol-con">
        <mt-popup v-model="popupVisibleProtocol" position="bottom" class="mint-popup-3" :modal="false">
          <mt-header class="bar-nav" :title="protocolTitle">
            <mt-button slot="right" @click="popupVisibleProtocol = false">关闭</mt-button>
          </mt-header>
          <div class="con" :style="{ height: protocolHeight + 'px' }"><section v-html="protocolHtml"></section></div>
        </mt-popup>
      </div>
      <!-- E协议内容隐藏域 -->
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import RdField from '../../../components/FieldInput';
  import validator from '../../../mixins/formValidator';
  import getCodeTime from '../../../mixins/getCodeTime';
  import qs from 'qs';

  export default {
    data() {
      return {
        realname: '', // 真实姓名
        idNo: '', // 身份证号
        creditNo: '', //  信用卡号
        mobileVal: '', // 手机号
        codeVal: '', // 验证码
        address: '', // 银行地址
        province: '', // 省份地址缓存
        city: '', // 城市地址缓存
        provinceDisplay: '省份', // 省份地址显示
        cityDisplay: '城市', // 城市地址显示
        provinceCode: '', // 省份编号
        cityCode: '', // 城市编号
        provinceIndex: 0, // 当前选中的省份索引
        cityIndex: 0, // 当前选中的城市索引
        pickerLoadDefaut: false, // 是否首次选择银行地址，true不是首次，false是首次
        areaJson: [], // 省市区数据列表
        popupVisible: false, // 省市区下拉菜单显示
        popupVisibleProtocol: false, // 协议下拉菜单显示
        protocolList: [], // 协议列表
        switch_status: true, // 是否同意协议，true已同意，false不同意
        protocolTitle: '', // 协议标题
        protocolHtml: '', // 协议内容
        slots: [
          {
            flex: 1,
            values: [],
            className: 'slot1',
            textAlign: 'right'
          },
          {
            divider: true,
            content: '-',
            className: 'slot2'
          },
          {
            flex: 1,
            values: [],
            className: 'slot3',
            textAlign: 'left'
          }
        ],
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        protocolHeight: 0
      }
    },
    mixins: [validator, getCodeTime],
    created() {
      this.$nextTick(() => {
        this.protocolHeight = document.documentElement.clientHeight - 40;
      })
      // 用户认证信息初始化
      this.$http.get(ajaxUrl.basicInfo, { params: this.getParams }).then((res) => {
        if (res.data.resData) {
          if (res.data.resData.realnameStatus == 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '确认',
              showCancelButton: false,
              message: '您已经开通了实名认证，无需再次开通',
              closeOnClickModal: false
            }).then(action => {
              this.$router.push({ name: 'account' });
            });
          }
        }
      });
      // 协议初始化
      this.$http.get(ajaxUrl.userGuide,{ params: this.getParams }).then((res) => {
        if (res.data.resData) {
          this.protocolList = res.data.resData.list;
        }
      });
      // 省市区数据初始化
      this.$http.get(ajaxUrl.getAreaJson).then((res) => {
        if (res.data.resData) {
          this.areaJson = JSON.parse(res.data.resData.json);
          for (var i = 0;i < this.areaJson.length;i++) {
            this.slots[0].values.push(this.areaJson[i].label);
          }
        }
      })
    },
    methods: {
      // 获取手机验证码
      getCode(evt) {
        if (this.regexPhone(this.mobileVal)) {
          let params = {
            'mobilePhone': this.mobileVal,
            userId: this.$store.state.user.userId,
            __sid: this.$store.state.user.__sid
          };
          this.$http.get(ajaxUrl.realnamePhoneCode, { params: params }).then((res) => {
            if (res.data.resMsg === '验证码已经发送到您的手机，请注意查收！') {
              this.getCodeTime(evt);
            } else {
              this.$messagebox({
                title: ' ',
                confirmButtonText: '确认',
                showCancelButton: false,
                message: res.data.resMsg
              });
            }
          })
        }
      },
      // 表单提交验证
      confirmSub() {
        if (this.chName(this.realname)) {
          if (this.regexIdCard(this.idNo)) {
            if (this.regexBankNo(this.creditNo)) {
              if (this.regexPhone(this.mobileVal)) {
                if (this.switch_status) {
                  if (this.$route.query.from) {
                    if (this.$route.query.from == 'account') {
                      sessionStorage.from_account = this.$route.query.from;
                    } else {
                      sessionStorage.from_id = this.$route.query.from + ',' + this.$route.query.id;
                    }
                  }
                  let realnameParams = {
                    userId: this.$store.state.user.userId,
                    __sid: this.$store.state.user.__sid,
                    address: this.address,
                    cardNum: this.creditNo,
                    code: this.codeVal,
                    idNo: this.idNo,
                    mobile: this.mobileVal,
                    realName: this.realname,
                    agree: 1
                  };
                  this.$indicator.open({ spinnerType: 'fading-circle' });
                  this.$http.post(ajaxUrl.tppRegister, qs.stringify(realnameParams)).then((res) => {
                    this.$indicator.close();
                    if (res.data.resCode != 39321) {
                      this.$toast(res.data.resMsg);
                    } else {
                      this.$messagebox({
                        title: ' ',
                        confirmButtonText: '确认',
                        showCancelButton: false,
                        message: res.data.resMsg
                      }).then(action => {
                        if (action === 'confirm') {
                          if (this.$route.query.from == 'investDetail') {
                            this.$router.push({ name: 'addBank', query: { from: 'investDetail', id: this.$route.query.id }});
                          } else if (this.$route.query.from == 'fund') {
                            this.$router.push({ name: 'addBank', query: { from: 'fund' }});
                          } else {
                            this.$router.push({ name: 'addBank' });
                          }
                        }
                      });
                    }
                  })
                } else {
                  this.$toast("需要您同意协议才能实名认证");
                }
              }
            }
          }
        }
      },
      // 省市区二级选项初始化
      cityLoad() {
        var i,j;
        for (i = 0;i < this.areaJson.length;i++) {
          if (this.areaJson[i].label == this.province) {
            this.provinceIndex = i;
            break;
          }
        }
        this.slots[2].values = [];
        for (j = 0;j < this.areaJson[i].children.length;j++) {
          this.slots[2].values.push(this.areaJson[i].children[j].label);
        }
      },
      // 省市区选择
      onValuesChange(picker, values) {
        if (this.pickerLoadDefaut) {
          if (values[0]) {
            this.province = values[0];
            this.cityLoad();
          }
          if (values[1]) {
            this.city = values[1];
          }
        }
      },
      // 首次选择银行地址行为初始化
      pickerLoaded() {
        this.popupVisible = true;
        if (this.pickerLoadDefaut == false) {
          for (var i = 0;i < this.areaJson[0].children.length;i++) {
            this.slots[2].values.push(this.areaJson[0].children[i].label);
          }
          this.pickerLoadDefaut = true;
          this.province = this.areaJson[0].label;
          this.provinceDisplay = this.areaJson[0].label;
          this.city = this.areaJson[0].children[0].label;
          this.cityDisplay = this.areaJson[0].children[0].label;
          this.addressCode();
        }
      },
      // 确认选择银行地址
      onValuesSure() {
        this.provinceDisplay = this.province;
        this.cityDisplay = this.city;
        this.popupVisible = false;
        this.addressCode();
      },
      // 查询当前用户所选择银行地址的code
      addressCode() {
        var i, j;
        for (i = 0;i < this.areaJson.length;i++) {
          if (this.areaJson[i].label == this.provinceDisplay) {
            this.provinceCode = this.areaJson[i].value;
            this.provinceIndex = i;
            break;
          }
        }
        for (j = 0;i < this.areaJson[this.provinceIndex].children.length;j++) {
          if (this.areaJson[this.provinceIndex].children[j].label == this.cityDisplay) {
            this.cityCode = this.areaJson[this.provinceIndex].children[j].value;
            break;
          }
        }
        this.address = this.provinceCode + ',' + this.cityCode;
      },
      // 同意协议按钮点击切换
      switchImg() {
        this.switch_status = !this.switch_status;
      },
      // 查看协议
      readProtocol(id, name) {
        this.popupVisibleProtocol = true;
        this.protocolHtml = '';
        this.protocolTitle = name;
        this.$http.get(ajaxUrl.registerProtocolDetail,{ params: { protocolId: id }}).then((res) => {
          this.protocolHtml = res.data.resData.content;
        })
      }
    },
    components: { RdField }
  }
</script>

<style type="text/css" scoped>
  .bg-fff {
    background:#fff;
  }
  .warm-tip {
    padding: .05rem .15rem .05rem;
    font-size: .12rem;
  }
  .rel-img{
    text-align: center;
    padding-top: .7rem;
  }
  .rel-img img{
    width: .9rem;
  }
  .rel-msg,.rel-account{
    text-align: center;
    line-height: 1;
    color: #333;
  }
  .rel-msg{
    font-size: .15rem;
    margin-top: .2rem;
  }
  .rel-account{
    font-family: arial;
    font-size: .18rem;
    margin-top: .16rem;
    font-weight: 600;
  }
  .btn-box {
    margin-top: .3rem;
  }
  .form-area ul li {
    margin-bottom: .1rem;
  }
  .province, .city{
    display: inline-block;
    margin-left: 5%;
    color: #999;
  }
  .area img{
    width: 0.08rem;
    height: 0.08rem;
  }
  .area label.active{
    color: #333;
  }
  /*下拉组件样式*/
  .mint-datetime-confirm,.mint-datetime-cancel {
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
  .mint-datetime-action {
    font-size: 14px;
  }
  .popup{
    width: 100%;
  }
  .protocol-box{
    padding: 0.1rem 5%;
    margin-bottom: 0.1rem;
    font-size: 0.12rem;
    color: #333333;
  }
  .protocol-box img{
    width: .14rem;
    vertical-align: -2px;
  }
</style>
