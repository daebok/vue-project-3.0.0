import 'antd/style/index.less';
import '../style/layout.css';
import '../style/common.less';
import '../../themes/js/IePlaceholder'
import './base.js';
import React from 'react';
import ReactDOM from 'react-dom';
//快捷方式
import Quickbtn from '../entry/home/quickBth';
if(document.getElementById("quickBtn")){
  ReactDOM.render(<Quickbtn />, document.getElementById("quickBtn"));
}

Array.prototype.max = function() {
return Math.max.apply({},this)

}
Array.prototype.min = function() {
return Math.min.apply({},this)

}

$(function(){
  let url = window.location.href;
  $.fn.extend({
    fullScreen:function(options){
      var defaults = {
        footObj:"#footer",
        diffentVal:""
      };
      var options = $.extend(defaults,options);
      this.each(function(){
        var o = options;
        var footer = $(o.footObj);
        var fTopHeight,footVal,allHeight,diffVal,footDiff,brotherObj,stpVal;
        $(window).bind("resize load scroll",function(){
          fTopHeight =  footer.height()
          footVal = footer.offset().top;
          allHeight = $(window).height();
          footDiff = allHeight - footVal -200;
          if(footDiff > 0){
            footer.css({"top":footDiff +"px"});
          }
          else{
            footer.css({"top":"0"});
          }
        })
      })
    }
  });

  if($("#footer").length){
    $("body").fullScreen();
  }

    if(navigator.userAgent.toLowerCase().indexOf('trident/4.0')>0||navigator.userAgent.toLowerCase().indexOf('trident/5.0')>0){

            $(":input[placeholder]").each(function(){
                $(this).placeholder();
            });

    }

});