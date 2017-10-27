import '../../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import './userriskpapers.less';


 $(".answerRadio dd").click(function(){
  var val = "{"+$(this).parents(".cnt").find(".quuid").val()+","+$(this).find("input").val()+"}";
  $(this).parents(".cnt").find(".question").val(val);
  $(this).parents(".cnt").find(".error").hide();
  $(this).addClass("hover");
  $(this).siblings().removeClass("hover");
  $(this).siblings().find("input").removeAttr("checked");
  $(this).find("input")[0].click();
})

$(".answerCheck dd").click(function(){
  var val="{"+$(this).parents(".cnt").find(".quuid").val();
  if($(this).find("input").is(':checked')){
    $(this).removeClass("hover");
    $(this).find("input").prop("checked", false);
  }
  else{
    $(this).parents(".cnt").find(".error").hide();
    $(this).addClass("hover");
    $(this).find("input").prop("checked", true); 
  }
  $(this).parents(".cnt").find(".answerCheck dd").each(function(){
    if($(this).find("input").is(':checked')){
      val += ","+$(this).find("input").val();
    }
  })
  val+="}";
  $(this).parents(".cnt").find(".question").val(val);
})

$(".sub").click(function(){
    var papersIdval = $(".papersId").val();
    var x=$("#f1").serializeArray();
    var sj ="";
    $.each(x, function(i, field){
      if(i<($(".questionNum").val()-1)){
          sj+=field.value+"concat";
      }
      else
      {
        sj+=field.value;
      }
      });
   var j=0;
   $.each($(".qs .cnt"),function(i,item){
            if(!$(this).find("dl input:checked").size()){
                $(this).find(".error").show();
            }
            else{
              j++; 
            }
        });
  if(j==$(".questionNum").val()){
       $.ajax({
          url: '/risk/doUserRiskPapers.html',
          type: 'POST',
          dataType: 'json',
          data:{papersId:papersIdval,questionContent:sj},
          success: function(data) {
            $(".ok-con").find("font").html(data.msg);
            $("#f1").hide();
            $(".evaluation").show();
          },
          error: function(request) {
              alert("Connection error");
          }
        });
  }
})





