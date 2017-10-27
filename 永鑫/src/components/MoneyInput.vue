<template>
  <div class="rd-field" v-clickoutside="doCloseActive">
    <span class="rd-label" v-text="label"></span>
    <input
      ref="input"
      class="rd-field-core"
      :class="[specInput]"
      :placeholder="placeholder"
      :maxlength="maxlength"
      :pattern="pattern"
      :name="name"
      :type="type"
      @focus="active = true"
      :disabled="disabled"
      :readonly="readonly"
      :value="currentValue"
      @input="handleInput"
      @blur="handleBlur"
      imeMode="inactive"
      autocapitalize="on"
    >
    <div
      @click="handleClear"
      class="rd-field-clear"
      :class="[specInput + '-clear']"
      v-if="!disableClear"
      v-show="currentValue && type !== 'textarea' && active">
      <i class="mintui mintui-field-error"></i>
    </div>
    <div class="rd-field-other">
      <slot></slot>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Clickoutside from '../directives/clickOutside.js';
  import ctrlInput from '../libs/common'
  export default {
    name: 'rd-money-input',
    data() {
      return {
        active: false,
        currentValue: this.value
      };
    },
    directives: { Clickoutside },
    props: {
      type: {
        type: String,
        default: 'text'
      },
      label: String,
      placeholder: String,
      directive: String,
      maxlength: String,
      pattern: String,
      name: String,
      readonly: Boolean,
      disabled: Boolean,
      disableClear: Boolean,
      specInput: String,
      inputType: {type: String, default: 'money'},
      value: {},
      attr: Object,
    },
    methods: {
      doCloseActive() {
        this.active = false;
      },
      handleInput(evt) {
        if(this.inputType == 'number'){
          ctrlInput.setNumber(evt.target)  // 控制输入框为数字
        }else{
          ctrlInput.setMoney(evt.target)  // 控制输入框为金额带小数点
        }
        let formatValue = evt.target.value;
        this.$refs.input.value = formatValue;
        this.currentValue = formatValue;
      },
      handleBlur(evt){ // 失去焦点做一次处理
        let formatValue = evt.target.value;
        this.$refs.input.value = formatValue;
        this.currentValue = formatValue;
      },
      handleClear() {
        if (this.disabled || this.readonly) return;
        this.currentValue = '';
        this.$refs.input.value = '';
        this.$refs.input.value2 = '';
        this.$refs.input.focus();
      },
    },
    watch: {
      value(val) {
        this.currentValue = val.toString().replace(/\s*$/, '');
      },
      currentValue(val) {
        this.$emit('input', val);
      },
      attr: {
        immediate: true,
        handler(attrs) {
          this.$nextTick(() => {
            const target = [this.$refs.input, this.$refs.textarea];
            target.forEach(el => {
              if (!el || !attrs) return;
              Object.keys(attrs).map(name => el.setAttribute(name, attrs[name]));
            })
          })
        }
      }
    }
  }
</script>

<style lang="sass" rel="stylesheet/sass">
  .rd-field
    line-height: .44rem;
    position: relative;
    background: #fff;
    .rd-label
      display: inline-block
      padding-left: 15px
    .rd-field-core
      -webkit-appearance: none
      border-radius: 0
      border: 0
      outline: 0
      width: 60%
      position: initial
      /*left: 20%*/
      /*top: .12rem*/
      padding-left: 5%
    .rd-field-clear
      opacity: .2
      position: absolute
      right: 15px
      top: 3px
    .spec-clear.rd-field-clear
      right: 40px
      top: 0
    .rd-field-other
      position: absolute
      right: 0
      top: 0
</style>
