var Reg = {
	//真实姓名
	regexRealName: function(value){		
		return /[\u4E00-\u9FA5]{2,5}(?:·[\u4E00-\u9FA5]{2,5})*/.test(value);
	},

	//中文姓名
	regexChnName: function(value){
		return /^[\u4e00-\u9fa5]+$/.test(value)
	},

	//用户名校验
	regexUserName: function(value){
		return !/^(?![^a-zA-Z]+$)(?!\D+$).{6,20}$/.test(value);
	},

	//身份证验证
	regexIdCard: function(value){
		return validateIdCard(value);
		function validateIdCard(idCard) {  
		    //15位和18位身份证号码的正则表达式  
		    var regIdCard = /^(^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$)|(^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}[X])$)$/;  
		    //如果通过该验证，说明身份证格式正确，但准确性还需计算  
		    if (regIdCard.test(idCard)) {  
		        if (idCard.length == 18) {  
		            var idCardWi = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //将前17位加权因子保存在数组里  
		            var idCardY = new Array(1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2); //这是除以11后，可能产生的11位余数、验证码，也保存成数组  
		            var idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和  
		            for (var i = 0; i < 17; i++) {  
		                idCardWiSum += idCard.substring(i, i + 1) * idCardWi[i];  
		            }  
		            var idCardMod = idCardWiSum % 11;//计算出校验码所在数组的位置  
		            var idCardLast = idCard.substring(17);//得到最后一位身份证号码  
		            //如果等于2，则说明校验码是10，身份证号码最后一位应该是X  
		            if (idCardMod == 2) {  
		                if (idCardLast == "X" ) {  
		                    return true;  
		                    //alert("恭喜通过验证啦！");  
		                } else {  
		                    return false;  
		                    //alert("身份证号码错误！");  
		                }  
		            } else {  
		                //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码  
		                if (idCardLast == idCardY[idCardMod]) {  
		                    //alert("恭喜通过验证啦！");  
		                    return true;  
		                } else {  
		                    return false;  
		                    //alert("身份证号码错误！");  
		                }  
		            }  
		        }  
		    } else {  
		        //alert("身份证格式不正确!");  
		        return false;  
		    }  
		}  
		 
	},	
	//密码校验
	regexPassword: function(value){
		return !/^(?![^a-zA-Z]+$)(?!\D+$).{8,16}$/.test(value) || /\s/.test(value);
	},
	//手机号码
	isMobile:function(value){
		var length = value.length;
		return ! (length == 11 && /^((13|18|15)\d{9})|(145|147|170|171|173|175|176|177|178)\d{8}$/.test(value));
	},
	//充值金额
	isNumber:function(value){
		return ! /^[1-9]{1}\d*$/.test(value);
	},
	//只能是数字
	isFigure: function(value){		
		return ! /^([0-9\.]+)$/.test(value); 
	},
	isApr:function(value){
		return ! /^(([1-9]\d*)|0)(\.([1-9]{1}|\d{2}))?$/.test(value); 
	},
	//邮箱
	isEmail:function(value){		
		return ! /^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/.test(value);
	},
	islogname:function(value){
		var length = value.length;
		return !(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/.test(value) || (/^([a-zA-Z0-9]|[a-zA-Z]){6,20}$/.test(value)) || (length == 11 && /^1[34578]\d{9}$/.test(value)));
	},
	//以0开头
	isFixNumber:function(value){
		return ! /^0/.test(value);
	},
	isPhone:function (value) {
		return ! /([0-9]{3,4}-)?[0-9]{7,8}/.test(value)
	},
	isCompanyName:function(value){
		return ! /^([\u4e00-\u9fa5（）]){2,30}$/.test(value);
	}
};

export{Reg}