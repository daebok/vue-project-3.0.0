
/*参数说明：
 * *******用于金额***************
 * type=0 : 可输入正整数，浮点数（最多能输入两位小数）
 * type=1 : 只能输入正整数
 *
 *
 *
 * *******用于图形验证码***********
 * type=2 : 只能输入数字、字母（含大写）
 *
 *
 * *******用于输入姓名***********
 * type=3 : 只能输入中文和英文（由于输入法兼容问题，不能设置为只输入中文）
 *
 * *******用于输入手机号、固话（如051788886666）等  非金额类数字***********
 * type=4 : 只能输入数字
 *
 * *******用于输入身份证号***********
 * type=5 :	只能输入数字和最后一位大写X
 *
 *
 * *******用于固话  有区号（如0517-88886666）***********
 * type=6 :	只能输入数字和-
 *
 * *******用于标名称、标详情、标描述等***********
 * type=7 ： 用于标名或标详情等文字类输入（不全为文字）
 *
 * *******用于注册页面***********
 *
 * type=8 ：不能输入空格
 *
 * *******用于邮箱***********
 *
 * type=9 ：只能输入数字、字母、下划线、@、.
 *
 *
 * *******使用案例：***********
 *
 * 添加左右箭头判断，防止光标不能通过键盘左右移动
 *
 * $('input').on('keyup',function(ev){
 * 		var keycode = ev.which;
 *
 *		if(keycode == 37 || keycode == 39){
 *
 *		}else{
 * 			inputLimit(this,0);
 * 		}
 * });
 *
 *
 * 或
 *
 * fnInputLimit('input',0,'keyup');
 *
 * */
exports.inputLimit = (value,type) => {
  value = value.toString();
  if(type==0){
    value = value.replace(/^0+(\d+)/, '$1'); //不能输入 类似00、01、0000001的数值
    value = value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
    value = value.replace(/^\./g,""); //验证第一个字符是数字而不是
    value = value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
    value = value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
  }
  if(type==1){
    value=value.replace(/\D/g,'');
    value = value.replace(/^0+(\d+)/, '$1'); //不能输入 类似00、01、0000001的数值
  }
  if(type==2){
    value=value.replace(/[^0-9a-zA-Z]/g,'');
  }
  if(type==3){
    value=value.replace(/[^\a-\z\A-\Z\u4E00-\u9FA5]/g,'');
  }
  if(type==4){
    value=value.replace(/\D/g,'');
  }
  if(type==5){
    value=value.replace(/[^0-9X]/g,'');
  }
  if(type==6){
    value = value.replace(/[^\d-]/g,''); //清除"数字"和"-"以外的字符
    value = value.replace(/^\-/g,''); //验证第一个字符是数字而不是-
  }
  if(type==7){
    value = value.replace(/(?=[\x21-\x7e]+)[^A-Za-z0-9]/, '');
  }
  if(type==8){
    value = value.replace(/^ +| +$/g, '');//不能输入空格
    value = value.replace(/[\u4e00-\u9fa5]/g, '');//不能输入中文
  }
  if(type==9){
    value = value.replace(/[^A-Za-z0-9_@.]/g, '');//只能输入数字、字母、下划线、@、.
  }
}

function fnInputLimit(obj,type,event){
  $(obj).on(event,function(ev){
    var keycode = ev.which;

    if(keycode == 37 || keycode == 39 || keycode == 8 || keycode == 46){

    }else{
      inputLimit(this,type);
      if(type == 0){
        if($(this).val() == '.'){
          $(this).val('');
        }
      }
    }
  });
}

