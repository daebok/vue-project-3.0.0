export default {
  methods: {
    // 校验用户名
    regexUserName(value = '') {
      if (/^(?![0-9]+$)[0-9A-Za-z]{4,16}$/.test(value) === false) {
        this.$toast({message:'用户名由英文字母、数字组成，且不能为纯数字'});
        return;
      }
    },
    // 校验密码
    regexPwd(value = '') {
      if (value === '') {
        this.$toast({ message: '请输入密码' });
        return;
      }
      if (/^(?![^a-zA-Z]+$)(?!\D+$).{8,16}$/.test(value) === false) {
        this.$toast({ message: '密码为8-16位字符，至少包含1位字母和1位数字' });
        return;
      }
      return true;
    },
    // 校验手机号
    regexPhone(value = '') {
      if (value === ''){
        this.$toast({ message: '请输入手机号' });
        return;
      }
      if (value.length === 11 && /(13\d|14[57]|15\d|17[0135678]|18\d)\d{8}$/.test(value)) {
        return true;
      } else {
        this.$toast({ message: '请输入正确的手机号码' });
        let telInput = document.querySelector('input[name="mobile"]');
        if(telInput) telInput.focus();
        return;
      }
    },
    // 校验邮箱
    regexEmail(val = '') {
      if (val === '') {
        this.$toast({ message: '请输入邮箱' });
        return;
      }
      let reg = /^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}/gi;
      if (!reg.test(val)) {
        this.$toast({ message: '请输入正确的邮箱' });
        return;
      }
      return true;
    },
    // 判断中文姓名且字符必须大于两位
    chName(value) {
      if (/^[\u4e00-\u9fa5]{2,}$/.test(value) === false) {
        this.$toast({ message: '请输入真实姓名' });
        return;
      }
      return true;
    },
    // 身份证校验
    regexIdCard(value) {
      return validateIdCard(value);
      function validateIdCard(idCard) {
        //15位和18位身份证号码的正则表达式
        var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[X])$)$/;
        //如果通过该验证，说明身份证格式正确，但准确性还需计算
        if (regIdCard.test(idCard)) {
          if (idCard.length === 18) {
            var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); // 将前17位加权因子保存在数组里
            var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); // 这是除以11后，可能产生的11位余数、验证码，也保存成数组
            var idCardWiSum = 0; // 用来保存前17位各自乖以加权因子后的总和
            for (var i = 0; i < 17; i++) {
              idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];
            }
            var idCardMod = idCardWiSum % 11; // 计算出校验码所在数组的位置
            var idCardLast = idCard.substring(17); // 得到最后一位身份证号码
            // 如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if (idCardMod === 2) {
              if (idCardLast === "X" ) {
                return true;
              } else {
                return false;
              }
            } else {
              //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
              if (idCardLast == idCardY[idCardMod]) {
                return true;
              } else {
                return false;
              }
            }
          }
        } else {
          return false;
        }
      }
    }
  }
}
