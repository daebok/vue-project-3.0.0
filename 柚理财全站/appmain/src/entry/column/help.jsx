import '../../common/lib';
import './help.less';
$(function(){
    $("#helpMainList").find("li").on("click", function(){
        if($(this).hasClass("on"))
        {
          $(this).removeClass("on")
        }
        else
        {
          $(this).addClass("on").siblings().removeClass("on");
        }        
    });
});

