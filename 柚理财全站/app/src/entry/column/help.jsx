import '../../common/lib';
import './help.less';
$(function(){
    $("#helpMainList").find("li").on("click", function(){
        if($(this).hasClass("on")) return false;
        $(this).addClass("on").siblings().removeClass("on");
    });
});