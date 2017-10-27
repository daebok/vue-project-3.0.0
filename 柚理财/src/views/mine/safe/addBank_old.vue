<template>
  <div class="page" id="addBank">
    <mt-header class="bar-nav" title="添加银行卡" >
      <mt-button slot="left" icon="back" v-back-link></mt-button>
    </mt-header>
    <div class="bank-tips">
      <p class="tips-1"><strong>温馨提示：</strong>请添加相同姓名的银行卡，</p>
      <p >且银行预留号码与绑定手机号保持一致。</p>
    </div>
    <ul class="addBank-list">
      <li class="aui-border-b">
        <label>真实姓名</label>
        <span>林小小</span>
      </li>
      <li class="aui-border-b">
        <label>身份证号</label>
        <span>3208881990****1122</span>
      </li>
      <li >
        <label>手机号码</label>
        <span>188****8888</span>
      </li>
      <li class="margin-t-10 aui-border-b">
        <label>开户银行</label>
        <input type="text" name="" v-model="form.bankName" @click="popupVisible = true" placeholder="请选择开户银行"  readonly/>
      </li>
      <li class="aui-border-b">
        <label>银行卡号</label>
        <input type="number" name="" placeholder="请输入银行卡号" />
      </li>
      <li >
        <label>所在地区</label>
        <input type="text" name="" v-model="form.area" @click="popupAddress = true" placeholder="请选择您所在城市"  readonly/>
      </li>
    </ul>
    <div class="margin-lr-15">
      <mt-button type="danger" size="large" class="confirm-btn margin-t-30">确定添加</mt-button>
    </div>

    <!-- 银行列表 -->
    <mt-popup v-model="popupVisible" position="bottom" popup-transition="popup-fade" class="mint-popup" id="name">
      <mt-picker :slots="bankName_slots" @change="onBankChange" :visible-item-count="5" show-toolbar rotate-effect>
        <div class="picker-toolbar aui-border-b">
          <span class="mint-datetime-action mint-datetime-cancel" @click="cancel">取消</span>
          <span class="mint-datetime-action mint-datetime-confirm" @click="cancel">确定</span>
        </div>
      </mt-picker>
    </mt-popup>
    <!-- 地址 -->
    <mt-popup v-model="popupAddress" position="bottom" popup-transition="popup-fade" class="mint-popup" id="address">
      <mt-picker :slots="addressSlots" @change="onAddressChange" :visible-item-count="5" show-toolbar >
        <div class="picker-toolbar aui-border-b">
          <span class="mint-datetime-action mint-datetime-cancel" @click="address_cancel">取消</span>
          <span class="mint-datetime-action mint-datetime-confirm" @click="address_cancel">确定</span>
        </div>
      </mt-picker>
    </mt-popup>
  </div>
</template>
<script>
  import { address } from '../../../libs/city-name'
  import * as ajaxUrl from '../../../ajax.config'
  export default {
    methods: {
      onBankChange(picker, values){
        this.form.bankName = values[0] ;
      },
      onAddressChange(picker, values) {
        picker.setSlotValues(1, address[values[0]]);
        this.form.area = values[0] + values[1];
      },
      address_cancel(){
        this.popupAddress = false;
      },
      cancel(){
        this.popupVisible = false;
      },
    },
    data(){
      return {
        form:{
          bankName:'',
          bankNo:'',
          area:'',
        },
        popupVisible: false,
        popupAddress: false,
        bankName_slots: [{
          flex: 1,
          values: ['工商银行', '中国银行', '建设银行', '农业银行', '交通银行'],
          className: 'slot1',
          textAlign: 'center'
        }],
        addressSlots: [
          {
            flex: 1,
            values: Object.keys(address),
            className: 'slot1',
            textAlign: 'center'
          }, {
            divider: true,
            content: '',
            className: 'slot2'
          }, {
            flex: 1,
            values: ['--'],
            className: 'slot3',
            textAlign: 'center'
          }
        ]
      }
    }
  }
</script>
<style scoped>
  .bank-tips{
    height: .6rem;
    font-size: .12rem;
    color: #666;
    padding: 0 .15rem;
  }
  .bank-tips p{
    line-height: 1;
  }
  .tips-1{
    padding-top: .15rem;
    padding-bottom: .08rem;
  }
  .addBank-list li{
    height: .48rem;
    width: 100%;
    padding: 0 .15rem;
    background: #fff;
  }
  .addBank-list li label{
    display: inline-block;
    float: left;
    line-height: .48rem;
    width: 24%;
  }
  .addBank-list li span{
    float: left;
    display: block;
    width: 70%;
    line-height: .48rem;
  }
  .addBank-list li input{
    float: left;
    width: 70%;
    border: none;
    outline: none;
    height: .3rem;
    line-height: .3rem;
    margin-top: .09rem;
  }
  /*下拉组件样式*/
  .picker{
    position: absolute;
    width: 100%;
    bottom: 0;
    left: 0;
    background: #fff;
  }
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
  .mint-popup {width: 100%;}
  .picker-slot-wrapper, .picker-item {
    backface-visibility: hidden;
  }
</style>