<!-- 添加银行卡 by fenglei -->
<template>
  <div class="page">
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
        <li class="margin-t-10 aui-border-b">
          <label>开户银行</label>
          <span :class="{ 'active': form.openBankNo == '' }" @click="pickerLoaded">{{ bankNameDisplay }}</span>
          <img src="../../assets/images/public/arrow_down.png" class="arrow-down">
        </li>
        <li class="aui-border-b">
          <label>银行卡号</label>
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
  </div>
</template>

<script type="text/ecmascript-6">
  import * as ajaxUrl from '../../ajax.config.js';
  import getCodeTime from '../../mixins/getCodeTime.js';
  import qs from 'qs';

  export default {
    data() {
      return {
        data: '', // 用户实名信息及开户银行列表,
        popupVisible: false,
        bankName: '',
        bankNameDisplay: '请选择开户银行',
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
      };
    },
    mixins: [ getCodeTime ],
    created() {
      this.dataLoad();
    },
    methods: {
      dataLoad() {
        this.popupVisible = false;
        // 实名信息及银行列表初始化
        this.$http.get(ajaxUrl.bindBankCard,{ params: this.getParams }).then((res) => {
          if (res.data.resData) {
            this.data = res.data.resData;
            for (var i = 0;i < res.data.resData.bankList.length;i++) {
              this.bankName_slots[0].values.push(res.data.resData.bankList[i].itemName);
            }
            this.$nextTick(()=> {
              this.pickerPostiion = document.querySelector('.picker-slot-wrapper').style.transform;
            });
          }
        });
      },
      confirmSub() {
        let parmams = {
          verifyCode: this.form.verifyCode,
          openBankNo: this.form.openBankNo,
          bankNo: this.form.bankNo,
          userId: this.$store.state.user.userId,
          __sid: this.$store.state.user.__sid
        };
        this.$indicator.open({ spinnerType: 'fading-circle' });
        this.$http.post(ajaxUrl.doBindBankCard, qs.stringify(parmams)).then((res) => {
          this.$indicator.close();
          this.$messagebox({
            title: ' ',
            confirmButtonText: '确认',
            showCancelButton: false,
            message: res.data.resMsg,
            closeOnClickModal: false
          }).then(action => {
            if (action === 'confirm') {
              if (res.data.resCode == 39321) {
                this.$emit('addBankOver');
                history.back();
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
        this.$http.get(ajaxUrl.bindBankCode, { params: params }).then((res) => {
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

<style lang="sass" rel="stylesheet/sass" scoped>
  @import "addBank.sass";
</style>
