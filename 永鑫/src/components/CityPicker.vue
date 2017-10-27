<template>
  <mt-popup v-model="visible" position="bottom" class="rd-city">
    <mt-picker
      :slots="addressSlots"
      @change="onAddressChange"
      :visible-item-count="5"
      class="mint-city-picker"
      ref="picker"
      show-toolbar>
      <span class="mint-datetime-action mint-datetime-cancel" @click="visible = false">{{ cancelText }}</span>
      <span class="mint-datetime-action mint-datetime-confirm" @click="confirm">{{ confirmText }}</span>
    </mt-picker>
    <input type="hidden" id="zone" :value="zone">
  </mt-popup>
</template>
<script>
  import { address, area } from '../libs/city-name2'
  // import { address } from '../libs/city-name'
  export default {
    name: 'city-picker',
    props: {
      cancelText: {
        type: String,
        default: '取消'
      },
      confirmText: {
        type: String,
        default: '确定'
      }
    },
    data(){
      return {
        visible: false,
        currentValue: null,
        zone: '',
        addressSlots: [
          {
            flex: 1,
            values: Object.keys(address),
            className: 'slot1',
            textAlign: 'center'
          }, {
            divider: true,
            content: ' ',
            className: 'slot2'
          }, {
            flex: 1,
            values: Object.keys(area),
            className: 'slot3',
            textAlign: 'center'
          },
          {
            divider: true,
            content: ' ',
            className: 'slot4'
          },
          {
            flex: 1,
            values: [''],
            className: 'slot5',
            textAlign: 'center'
          }
        ]
      }
    },
    methods: {
      open() { this.visible = true;},
      close() { this.visible = false; },
      onAddressChange(picker, values) {
        picker.setSlotValues(1, address[values[0]]);
        picker.setSlotValues(2, area[values[1]]);
        let str2 = values[2] != '' ? ',' + values[2] : values[2]
        this.zone = values[0] + ',' + values[1] + str2
        this.currentValue = values[0] + values[1] + values[2]
      },
      confirm() {
        this.visible = false;
        if(!this.currentValue){  // 没change点击确定时候默认选择第一个
          this.currentValue = Object.keys(address)[0] + '' +Object.keys(area)[0]
          this.zone = Object.keys(address)[0] + ',' + Object.keys(area)[0]
        }
        this.$emit('confirm', this.currentValue);
      },
      handleValueChange(picker) {
        this.$emit('input', this.currentValue);
      }
    }
  }
</script>
<style scoped>
  .mint-datetime-cancel {text-align: left; padding-left: .25rem;color:#666;}
  .mint-datetime-confirm {text-align: right; padding-right: .25rem;color:#666;}
  .rd-city {width:  100%;}
</style>
