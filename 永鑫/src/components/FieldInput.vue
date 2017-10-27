<template>
  <div class="rd-field rd-login-pwd" v-clickoutside="doCloseActive">
    <span class="rd-label" v-text="label"></span>
    <input
      ref="input"
      class="rd-field-core"
      :class="[specInput]"
      :placeholder="placeholder"
      :name="name"
      :number="type === 'number'"
      :type="type"
      @focus="active = true"
      :disabled="disabled"
      :readonly="readonly"
      :value="currentValue"
      @input="handleInput">
    <div
      @click="handleClear"
      class="rd-field-clear"
      :class="[specInput + '-clear']"
      v-if="!disableClear"
      v-show="currentValue && type !== 'textarea' && active">
      <i class="mintui mintui-field-error"></i>
    </div>
    <span class="rd-field-state" v-if="state" :class="['is-' + state]">
      <i class="mintui" :class="['mintui-field-' + state]"></i>
    </span>
    <div class="rd-field-other">
      <div v-show="switchEye">
        <img v-show="showEye" src="../assets/images/logReg/login_icon_show_pwd.png" class="show-hide-pwd" :class="[specInput + '-eye']" @click="hideShowPwd" data-flag="0">
        <img v-show="!showEye" src="../assets/images/logReg/login_icon_hide_pwd.png" class="show-hide-pwd" :class="[specInput + '-eye']" @click="hideShowPwd" data-flag="1">
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Clickoutside from '../directives/clickOutside.js';
  /**
   * mt-field
   * @desc 编辑器，依赖 cell
   * @module components/field
   *
   * @param {string} [type=text] - field 类型，接受 text, textarea 等
   * @param {string} [label] - 标签
   * @param {string} [rows] - textarea 的 rows
   * @param {string} [placeholder] - placeholder
   * @param {string} [disabled] - disabled
   * @param {string} [readonly] - readonly
   * @param {string} [state] - 表单校验状态样式，接受 error, warning, success
   *
   * <mt-field v-model="value" label="自我介绍" placeholder="自我介绍" type="textarea" rows="4"></mt-field>
   * <mt-field v-model="value" label="邮箱" placeholder="成功状态" state="success"></mt-field>
   */
  export default {
    name: 'rd-field',
    data() {
      return {
        active: false,
        currentValue: this.value,
        showEye: false
      };
    },
    directives: { Clickoutside },
    props: {
      type: {
        type: String,
        default: 'text'
      },
      rows: String,
      label: String,
      placeholder: String,
      name: String,
      readonly: Boolean,
      disabled: Boolean,
      disableClear: Boolean,
      state: {
        type: String,
        default: 'default'
      },
      specInput: String,
      value: {},
      attr: Object,
      switchEye:{
        type: Boolean,
        default: false
      }
    },
    methods: {
      doCloseActive() {
        this.active = false;
      },
      handleInput(evt) {
        let formatValue = evt.target.value;
        if (this.type === 'tel') {
          formatValue = formatValue.replace(/\D*$/, '');
        }
        this.currentValue = formatValue;
      },
      handleClear() {
        if (this.disabled || this.readonly) return;
        this.currentValue = '';
        this.$refs.input.focus();
      },
      hideShowPwd(e) {
        let flag = e.target.dataset.flag;
        this.showEye = !this.showEye;
        this.$refs.input.focus();
        this.$emit('changeEye', flag);
      }
    },
    watch: {
      value(val) {
        this.currentValue = val.replace(/\s*$/, '');
      },
      currentValue(val) {
        this.$emit('input', val);
      },
      attr: {
        immediate: true,
        handler(attrs) {
          this.$nextTick(() => {
            if (this.type === 'tel') {
              this.$refs.input.setAttribute('maxlength', 11);
            }
            if (this.type === 'password') {
              this.$refs.input.setAttribute('maxlength', 16);
            }
            const target = [this.$refs.input, this.$refs.textarea];
            target.forEach(el => {
              if (!el || !attrs) return;
              Object.keys(attrs).map(name => el.setAttribute(name, attrs[name]));
            })
          })
        }
      }
    }
  };
</script>

<style lang="sass" rel="stylesheet/sass">
  .rd-field
  /*display: flex;*/
  .rd-label
    display: inline-block
    padding-left: 15px
  .rd-field-core
    -webkit-appearance: none
    border-radius: 0
    border: 0
    outline: 0
    width: 60%
    position: absolute
    left: 20%
    top: .12rem
    padding-left: 5%
    &.pwdInput
      width: 62%
    &.longInput
      width: 70%
    &.loginInput
      text-align: center
      margin-top: -4px
      font-size: 15px
      padding-left: 0
  .rd-field-clear
    opacity: .2
    position: absolute
    right: 15px
    top: 3px
  .pwdInput-clear.rd-field-clear
    right: 35px
  .codeInput-clear.rd-field-clear
    right: 110px
  .loginInput-clear.rd-field-clear
    top: 0
  .spec-clear.rd-field-clear
    right: 40px
    top: 0
  .show-hide-pwd
    width: .2rem
    margin: .1rem 0 0 0
    position: absolute
    top: .05rem
    right: .12rem
  .loginInput.show-hide-pwd
    top: 2px
  .rd-field-state
    color: inherit
    margin-left: 20px
    .mintui
      font-size: 20px
  .rd-field-other
    position: absolute
    right: 0
    top: 0
</style>
