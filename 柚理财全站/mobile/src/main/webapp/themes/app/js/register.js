//注册登录密码
fnInputLimit('#password',8,'keyup');
fnInputLimit('#confirmPassword',8,'keyup');
//用户协议勾选判断
$("#dianji").click(function(){
	var that=$(this);
	if(that.hasClass("agree")){
		that.removeClass("agree");
		that.attr("src","/themes/app/images/me/protocol_n.png");
		$("#agree").val(0);
	}else{
		that.addClass("agree");
		that.attr("src","/themes/app/images/me/protocol_s.png");
		$("#agree").val(1);
	}
})
//获取协议名称及uuid
$.ajax({
	type:"post",
	url:"/app/open/user/registerProtocol.html",
	dataType:'json',
	success:function(data){					
		if((data.resData.list!="") && (data.resData.list.length > 0)){
			var html = '';
			for(var i=0;i<data.resData.list.length;i++){
				var name = data.resData.list[i].name;
				var id = data.resData.list[i].id;
				
				html += '<a href="/app/open/user/registerProtocolDetail.html?protocolId='+id+'" class="protocol" >&lt;&lt;'+name+'&gt;&gt;</a>';
			}
			$("#service_contract").html(html);
		}
	}
});


//注册第一步表单提交验证
$("#regFirstForm").validate({
	rules: {
		mobile:{
			required:true,
			isMobile:true
		},
		pwd: {
			required: true,
			regexPassword:true
		},
		confirmPassword: {
			required: true,
			equalTo: "#password"
		},
		validCode:{
			required:true,
			minlength:4
		}
	},
	messages:{
		mobile:{
			required:"请输入手机号码",
			isMobile:"手机号码格式错误"
		},
		pwd: {
			required: "8-16位字符，其中包括至少一个字母和一个数字" ,
			regexPassword:"8-16位字符，其中包括至少一个字母和一个数字"
		},
		confirmPassword: {
			required: "请输入确认密码",
			equalTo: "两次输入密码不一致"
		},
		validCode:{
			required:"请输入验证码",
			minlength:"验证码格式错误"
		}
	},
	errorElement:'em',
	showErrors:function(errorMap, errorList){
		this.defaultShowErrors();
		$(".showErrorTips").css("display","none");
		
		if(errorMap.mobile){
			$(".showErrorTips").css("display","block");
			$(".errorTips b").html(errorMap.mobile);
		}else if(errorMap.pwd){
			$(".showErrorTips").css("display","block");
			$(".errorTips b").html(errorMap.pwd);
		}else if(errorMap.confirmPassword){
			$(".showErrorTips").css("display","block");
			$(".errorTips b").html(errorMap.confirmPassword);
		}else if(errorMap.validCode){
			$(".showErrorTips").css("display","block");
			$(".errorTips b").html(errorMap.validCode);
		}else{
			$(".errorTips b").html('');
		}
	},
	errorPlacement:function(error,element){},
	submitHandler:function(form,event,validator){
		if($("#agree").val()==1){
			$(".form-btn").attr("disabled","disabled");
			//base64加密
			var dec = $('#password'),
				enc = $('#confirmPassword');
			$.base64.utf8encode = true;
			dec.val($.base64('encode', dec.val()));
			enc.val($.base64('encode', enc.val()));
			$(form).ajaxSubmit({
				type:"post",
				dataType:'json',
				success:function(data){
					if(data.resMsg == "SUCCESS"){
						$(".form-btn").removeAttr("disabled");
						$(".reg_content_1").slideUp();
						$(".reg_content_2").slideDown();
						$("#regPhone").val($("#mobilePhone").val());
						$("#sid").val(data.resData.__sid);
					}else{
						toast(data.resMsg,function(){
							$(".form-btn").removeAttr("disabled");
						});
						$(".validImg").trigger("click");
					}
				}
			});	
		}else{
			toast("请勾选服务协议条款");
		}		
	}  
});

//验证码倒计时
var wait = 60;
get_code_time = function (o) {
	if (wait == 0) {
		o.removeAttribute("disabled");
		o.value = "重新发送";
		wait = 60;
	} else {
		o.setAttribute("disabled", true);
		o.value = wait + "S";
		wait--;
		setTimeout(function() {
			get_code_time(o);
		}, 1000)
	}
}

//获取手机验证码
$("#getPhoneCode").click(function(){
	var o = this;
	$.ajax({
		url:'/app/open/user/registerPhoneCode.html?mobile=' + $('#regPhone').val() + '&randomTime=' + (new Date()).getTime(),
		type:'post',
		success:function(data){
			if(data.resMsg != "SUCCESS"){						
				//toast(data.resMsg);
				get_code_time(o);
				return false;
			}else{
				get_code_time(o);
				return true;
			}					
		}
	});			
})


//注册第二步表单提交验证
$("#regSecondForm").validate({
	rules:{
		code:{
			required:true,
			minlength:4
		}
	},
	messages:{
		code:{
			required:"请输入验证码",
			minlength:"验证码格式错误",
		}
	},
	errorElement:'em',
	showErrors:function(errorMap, errorList){
		this.defaultShowErrors();
		$(".showErrorTips").css("display","none");
		if(errorMap.code){
			$(".showErrorTips").css("display","block");
			$(".errorTips b").html(errorMap.code);		
		}else{
			$(".errorTips b").html('');
		}
	},
	errorPlacement:function(error,element){},
	submitHandler: function(form,event,validator){      				
		$(".form-btn").attr("disabled","disabled");
		$(form).ajaxSubmit({
			type:"post",
			dataType:'json',
			success:function(data){
				if(data.resMsg=="SUCCESS"){
					$(".form-btn").removeAttr("disabled");
					location.href="/app/open/wechat/registerSuccess.html";
				}else{
					$(".form-btn").removeAttr("disabled");
					$(".showErrorTips").css("display","block");
					$(".errorTips b").html(data.resMsg);					
				}			
			}
		})
	}
});	
