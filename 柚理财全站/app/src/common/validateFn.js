import  { Reg } from './regularFun';
const ValidateFn = {
  //真实姓名
  regexRealName(rule, value, callback) {
    if (!value) {
      callback();
    } else if (!Reg.regexRealName(value)) {
      callback([new Error('用户名格式不正确')]);
    } else {
      callback();
    }
  },
  //中文姓名
  regexChnName(rule, value, callback) {
    if(!value){
      callback();
    }else{
      if(!Reg.regexChnName(value)){        
        callback([new Error('仅为中文,且不包含空格')]);        
      }else{
        callback();
      }
    }
  },
  //用户名校验
  regexUserName(rule, value, callback) {
    if(!value){
      callback();
    }else{
      if(Reg.regexUserName(value)){        
        callback([new Error('6-20位字符，由字母或字母加数字组成')]);        
      }else{
        callback();
      }
    }
  },
  //用户名异步校验
  regexUserNameAjax(rule, value, callback) {
    if(!value){
      callback();
    }else if(!/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}|[a-zA-Z]{6,20}$/.test(value)){        
        callback([new Error('6-20位字符，由字母或字母加数字组成')]);        
    }else{
      var that = this;
      if(this.state.regexUserNameflag == value){
        callback();
        return;
      }
      $.ajax({
        url: '/user/checkUserName.html',
        type: 'POST',
        dataType: 'json',
        data:{userName:value}
      })
      .done(function(data) {
        if(data.result){
          that.state.regexUserNameflag = value;
          callback();   
        }
        else
        {
          callback([new Error(data.msg)]); 
        }  
      })
      .fail(function() {
          callback([new Error('网络异常，请重试')]);
      });
    }
  },
  //身份证验证
  regexIdCard(rule, value, callback) {
    if(!value){
      callback();
    }else{
      if(!Reg.regexIdCard(value)){        
        callback([new Error('身份证格式不正确,仅限数字和大写X')]);        
      }else{
        callback();
      }
    }
  },
  //密码校验
  regexPW(rule, value, callback) {
    if(!value){
      callback();
    }else{
      if(Reg.regexPassword(value)){        
        callback([new Error('8-16位字符，其中包括至少一个字母和一个数字')]);        
      }else{
        callback();
      }
    }
  },
  isMobile(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isMobile(value)){        
        callback([new Error('抱歉，请输入正确的手机号码')]);        
    }else{
      callback();
    }    
  },
  isNumber(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isNumber(value)){        
        callback([new Error('抱歉，请输入正整数')]);        
    }else{
      callback();
    }    
  },
  isApr(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isApr(value)){        
        callback([new Error('抱歉，请输入正确的数字')]);        
    }else{
      callback();
    }    
  },  
  decimal(rule, value, callback){    
    if(!value){
      callback();
    } else if(Reg.isFigure(value)){
      callback([new Error('抱歉，请输入正确的数字')]);
    } else {
      callback();
    }
  }, 
  isFigure(rule, value, callback){    
    if(!value){
      callback();
    } else if(Reg.isFigure(value) || value.length != 15){
      callback([new Error('抱歉，请输入15位数字')]);
    } else {
      callback();
    }
  },
  isEmail(rule, value, callback) {
    if(!value){
      callback();
    }else if(Reg.isEmail(value)){        
        callback([new Error('抱歉，请输入正确的邮箱')]);        
    }else{
      callback();
    }    
  },
  isFixNumber(rule, value, callback){//座机验证
    if(!value){
      callback();
    } else if(Reg.isFixNumber(value) || Reg.isFigure(value) || value.length > 12 || Reg.isPhone(value)){
      callback([new Error('抱歉，请输入正确的座机号码')]);
    } else {
      callback();
    }
  },
  isloginName(rule, value, callback){//登录名检测
    if(!value){
      callback();
    }else if(Reg.islogname(value)){        
        callback([new Error('抱歉，请输入正确的手机号码或邮箱')]);        
    }else{
      callback();
    }
  },checkMobile(rule, value, callback) {//手机号是否存在
    if (!value) {
      callback();
    }else if(Reg.isMobile(value)){        
        callback([new Error('抱歉，请输入正确的手机号码')]);        
    }else{
      var that = this;
      if(this.state.mobileflag == value){
        callback();
        return;
      }
      $.ajax({
        url: '/user/checkMobile.html',
        type: 'POST',
        dataType: 'json',
        data:{mobile:value}
      })
      .done(function(data) {
        if(data.result){
          that.setState({
            mobileflag : value
          });
          callback();   
        }
        else
        {
          callback([new Error(data.msg)]); 
        }  
      })
      .fail(function() {
          callback([new Error('网络异常，请重试')]);
      });
    }
  },checkValidCode(rule, value, callback) {//检测验证码是否正确
    if (!value) {
      callback();
    }else{
      var that = this;
      if(this.state.ValidCodeflag == value){
        callback();
        return;
      }
      $.ajax({
        url: '/valicode.html',
        type: 'POST',
        dataType: 'json',
        data:{validCode:value}
      })
      .done(function(data) {
        if(data.result){
          that.setState({
            ValidCodeflag : value
          });
          callback();   
        }
        else
        {
          callback([new Error('验证码不正确')]);
          $(".codeimg")[0].click(); 
        }  
      })
      .fail(function() {
          callback([new Error('网络异常，请重试')]);
      });
    }
  }

}
export{ValidateFn}