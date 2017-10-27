<!-- 更换银行卡 by fenglei -->
<template>
  <div id="addBank">
    <ul class="addBank-list margin-t-10">
      <li class="aui-border-b">
        <label>真实姓名</label>
        <span>{{ data.realName }}</span>
      </li>
      <li class="aui-border-b">
        <label>身份证号</label>
        <span>{{ data.idNo }}</span>
      </li>
      <li>
        <label>手机号码</label>
        <span>{{ data.hideMobile }}</span>
      </li>
    </ul>
    <ul class="addBank-list margin-t-10">
      <li class="aui-border-b">
        <label>原开户银行</label>
        <span>{{ data.oldBankName }}</span>
      </li>
      <li class="aui-border-b">
        <label>原银行卡号</label>
        <span>{{ data.oldBankNo }}</span>
      </li>
    </ul>
    <ul class="addBank-list margin-t-10">
      <li class="margin-t-10 aui-border-b">
        <label>新开户银行</label>
        <span :class="{ 'active': form.openBankNo == '' }" @click="pickerLoaded">{{ bankNameDisplay }}</span>
        <img src="../../../assets/images/public/arrow_down.png" class="arrow-down">
      </li>
      <li class="aui-border-b">
        <label>新银行卡号</label>
        <input type="tel" v-model="form.bankNo" placeholder="请输入银行卡号" maxlength="19" />
      </li>
      <li>
        <label>验证码</label>
        <input type="text" class="code" v-model="form.verifyCode" placeholder="请输入手机验证码" maxlength="6" />
        <button v-if="form.bankNo && form.openBankNo" @click="getCode" class="yzm ellipsis">获取验证码</button>
        <button v-else class="yzm color-999 ellipsis" disabled>获取验证码</button>
      </li>
    </ul>
    <div class="margin-lr-15 margin-t-30">
      <mt-button v-if="form.openBankNo && form.bankNo && form.verifyCode" type="danger" @click.native="confirmSub" class="confirm-btn" size="large">确定添加</mt-button>
      <mt-button v-else type="default" size="large" class="confirm-btn" disabled >确定添加</mt-button>
    </div>
    <div class="bank-tips">
      <p class="tips-1"><strong>温馨提示：</strong>仅支持实名认证本人名下的借记卡更换</p>
    </div>
    <!-- 银行列表 -->
    <mt-popup v-model="popupVisible" position="bottom" class="mint-popup">
      <mt-picker :slots="bankName_slots" @change="onValuesChange" :show-toolbar="true">
        <div class="picker-toolbar aui-border-b">
          <span class="mint-datetime-action mint-datetime-cancel" @click="popupVisible = false">取消</span>
          <span class="mint-datetime-action mint-datetime-confirm" @click="onValuesSure">确定</span>
        </div>
      </mt-picker>
    </mt-popup>
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../../ajax.config';
  import getCodeTime from '../../../mixins/getCodeTime.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        data: '', // 用户实名信息及开户银行列表,
        popupVisible: false,
        bankName: '',
        bankNameDisplay: '请选择开户银行',
        bankIndex: 0, // 当前选中的银行索引
        bankLoadDefaut: false, // 是否首次选择银行，true不是首次，false是首次
        valuesIndex: 0, // 记住用户所选银行列表索引
        pickerPostiion: '', // 记住用户所选银行列表位置
        getParams: {
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        },
        form: {
          openBankNo: '',
          bankNo: '',
          verifyCode: ''
        },
        bankName_slots: [
          {
            flex: 1,
            values: [],
            className: 'slot1',
            textAlign: 'center'
          }
        ]
      }
    },
    mixins: [ getCodeTime ],
    created() {
      // 实名信息及银行列表初始化
      this.$http.get(ajaxUrl.changeBankCard,{ params: this.getParams }).then((res) => {
        if (res.data.resData) {
          this.data = res.data.resData;
          for (var i = 0;i < res.data.resData.bankList.length;i++) {
            this.bankName_slots[0].values.push(res.data.resData.bankList[i].itemName);
          }
          this.$nextTick(()=> {
            this.pickerPostiion = document.querySelector('.picker-slot-wrapper').style.transform;
          });
          if (this.data.realNameStatus != 1) {
            this.$messagebox({
              title: ' ',
              confirmButtonText: '去开通托管账户',
              showCancelButton: true,
              message: '您尚未开通资金账户，为了顺利添加银行卡请先开通资金账户!'
            }).then(action => {
              if (action == 'confirm') {
                this.$router.push({ name: 'realname' });
              } else if (action == 'cancel') {
                this.$router.go(-1);
              }
            });
          }
        }
      });
    },
    methods: {
      confirmSub() {
        let parmams = {
          verifyCode: this.form.verifyCode,
          openBankNo: this.form.openBankNo,
          bankNo: this.form.bankNo,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.doChangeBank, qs.stringify(parmams)).then((res) => {
          this.$indicator.close();
          this.$messagebox({
            title: ' ',
            confirmButtonText: '确认',
            showCancelButton: false,
            message: res.data.resMsg
          }).then(action => {
            if (action === 'confirm') {
              if (res.data.resCode == 39321) {
                if (this.data.hasSetPayPwd == 0) {
                  if (this.$route.query.from == 'investDetail') {
                    this.$router.push({ name: 'setPwd', query: { 'from': 'investDetail', id: this.$route.query.id, fromBank: 1 }});
                  } else if (this.$route.query.from == 'fund') {
                    this.$router.push({ name: 'setPwd', query: { from: 'fund', fromBank: 1 }});
                  } else {
                    this.$router.push({ name: 'setPwd', query: { fromBank: 1 }});
                  }
                } else {
                  if (this.$route.query.from == 'investDetail') {
                    this.$router.push({ name: 'investDetail', params: { projectId: this.$route.query.id }});
                  } else if (this.$route.query.from == 'fund') {
                    this.$router.push({ name: 'setPwd', query: { from: 'fund', fromBank: 1 }});
                  } else {
                    this.$router.push({ name: 'safe' });
                  }
                }
              }
            }
          });
        })
      },
      // 银行卡选择
      onValuesChange(picker, values) {
        if (this.bankLoadDefaut) {
          if (values[0]) {
            this.bankName = values[0];
          }
        }
      },
      // 首次选择银行行为初始化
      pickerLoaded() {
        this.popupVisible = true;
        let pickerItem = document.getElementsByClassName('picker-item');
        for (var i = 0;i < pickerItem.length;i++) {
          pickerItem[i].className = 'picker-item';
        }
        document.getElementsByClassName('picker-item')[this.valuesIndex].className = 'picker-item picker-selected';
        document.querySelector('.picker-slot-wrapper').style.transform = this.pickerPostiion;
        if (this.bankLoadDefaut == false) {
          this.bankLoadDefaut = true;
          this.bankName = this.bankName_slots[0].values[0];
          this.bankNameDisplay = this.bankName_slots[0].values[0];
          this.lookBankNo();
        }
      },
      // 查询银行编号
      lookBankNo() {
        for (var i = 0;i < this.data.bankList.length;i++) {
          if (this.bankNameDisplay == this.data.bankList[i].itemName) {
            this.pickerPostiion = document.querySelector('.picker-slot-wrapper').style.transform;
            this.form.openBankNo = this.data.bankList[i].itemValue;
            this.valuesIndex = i;
            break;
          }
        }
      },
      // 确认选择开户银行
      onValuesSure() {
        this.bankNameDisplay = this.bankName;
        this.lookBankNo();
        this.popupVisible = false;
      },
      getCode (evt) {
        // 获取验证码
        let params = {
          openBankNo: this.form.openBankNo,
          bankNo: this.form.bankNo,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$http.get(ajaxUrl.changeBankCode, { params: params }).then((res) => {
          if (res.data.resCode == 39321) {
            this.getCodeTime(evt, 60);
          } else {
            this.$toast(res.data.resMsg)
          }
        })
      }
    }
  }
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
  .bank-tips {
    margin-top: .3rem;
    font-size: .12rem;
    color: #999;
    padding: 0 .15rem;
    strong {
      display: inline-block;
      width: 22%;
    }
    .tips-2 {
      padding-left: 22%;
    }
  }
  .addBank-list {
    padding-left: 5%;
    background: #fff;
  }
  .addBank-list li {
    height: .48rem;
    width: 100%;
    position: relative;
    label {
      display: inline-block;
      float: left;
      line-height: .48rem;
      width: 24%;
    }
    input {
      float: left;
      width: 70%;
      border: none;
      outline: none;
      height: .3rem;
      line-height: .3rem;
      margin-top: .09rem;
    }
    .code {
      width: 45%;
    }
    .yzm {
      padding-left: .15rem;
      line-height: .3rem;
      width: 0.9rem;
      background: #fff;
      border: 0;
      font-size: .13rem;
      color: #d73238;
      margin-top: 0.1rem;
      border-left: 1px solid #c8c7cc;
    }
    span {
      float: left;
      display: block;
      width: 70%;
      line-height: .48rem;
    }
  }
  .arrow-down {
    position: absolute;
    right: .15rem;
    top: 50%;
    width: 12px;
    margin-top: -6px;
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
  .mint-popup {
    width: 100%;
  }
  .picker-slot-wrapper, .picker-item {
    backface-visibility: hidden;
  }
  .active {
    color: #999;
  }
</style>
