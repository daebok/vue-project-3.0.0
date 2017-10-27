import '../../common/lib';
import ReactDOM from 'react-dom';
import React from 'react';
import { Modal } from "antd";
import './lotteryDraw.less';
//import { Publiclib } from '../../../common/common';
//中奖名单
//import WinnersList from './lotteryDraw/winnersList';

$(function(){

    function formatDate(value) {
        if(value==""){
            return "--";
        }
        let now = new Date(value),
            year=now.getFullYear(),
            month=now.getMonth()+1,
            date=now.getDate(),
            hour=now.getHours(),
            minute=now.getMinutes(),
            second=now.getSeconds();
        if(month < 10)
        {
            month = '0' + month;
        }
        if(date < 10)
        {
            date = '0' + date;
        }
        return year+"-"+month+"-"+date;
    }

    //获奖名单
    function getLotteryList(){
        var str="";
        $.ajax({
            type:"get",
            cache:false,
            url:'/score/lottery/list.html?randomTime=' + (new Date()).getTime(),
            dataType:"json",
            success:function(data){
                var prize = data.recordlist;
                var len = prize.length;
                for( var i = 0 ; i<len ; i++){
                    var createTimetyle=formatDate(prize[i].createTime);
                    str+='<li><em class="one">'+prize[i].mobile.replace(/(\d{3})\d{4}(\d{4})/,"$1****$2")+'</em><em class="two">'+prize[i].lotteryValue+'</em><em class="three">'+createTimetyle+'</em></li>';
                    /*if(prize[i].type ==1){
                      str+='<li><span class="name">'+prize[i].userName+'</span><span>抽中了</span>'+prize[i].value+'个积分</li>';
                    }else{
                      str+='<li><span class="name">'+prize[i].userName+'</span><span>抽中了</span>'+prize[i].value+'元现金</li>';
                    }*/
                }
                $(".winningListM ul").html(str);
                //console.log(2)
                $("#pointer").addClass("pointer");
            }
        });

    }
    getLotteryList();

     //游戏规则
    function getlotteryGameRule(){
        var str="";
        $.ajax({
            type:"get",
            cache:false,
            url:'/score/lottery/list.html',
            dataType:"json",
            success:function(data){
                var gameRule = data.lotteryGameRule;
                /*var reg=new RegExp("<br>","g"); //创建正则RegExp对象    
                var newstr=gameRule.replace(reg,"\n");     */
                $(".lottery_draw02 ul li").html(gameRule);
            }
        });

    }
    getlotteryGameRule();

    /* 获取用户信息 */
    var lotteryNum = $('.lottery-num');//当前剩余抽奖次数
    $.ajax({
        type:"get",
        cache:false,
        url:'/score/lottery/scoreLotteryInfo.html?randomTime=' + (new Date()).getTime(),
        dataType:"json",
        success:function(data){
            lotteryNum.text(data.lotteryNum);
        }
    });

    var rotateTimeOut = function (){
        $('#rotate').rotate({
            angle:0,
            animateTo:2160,
            duration:8000,
            callback:function (){
                tipsBox('网络超时，请检查您的网络设置！');
            }
        });
    };

    let bRotate = false;

    let rotateFn = function (awards, angles, txt){
        bRotate = !bRotate;
        $('#rotate').stopRotate();
        $('#rotate').rotate({
            angle:0,
            animateTo:angles+1800,
            duration:8000,
            callback:function (){
                if(txt=="谢谢参与，请您继续努力"){
                    tipsBoxFalse(txt);
                }else{
                    tipsBox(txt);
                }
                bRotate = !bRotate;
                $.ajax({
                    type:"get",
                    cache:false,
                    url:'/score/lottery/scoreLotteryInfo.html?randomTime=' + (new Date()).getTime(),
                    dataType:"json",
                    success:function(data){
                        lotteryNum.text(data.lotteryNum);
                    }
                });
            }
        })
    };

    $('#pointer').click(function(){
        if($(this).hasClass("pointer")){
            console.log(3)
            $.ajax({
                type:"get",
                cache:false,
                url:'/score/lottery/doLottery.html?randomTime=' + (new Date()).getTime(),
                dataType:"json",
                success:function(data){
                    //alert(2)
                    if(data.result==true){
                        //console.log(bRotate);
                        //console.log(data.lotteryArea);
                        if(bRotate)return;
                        switch (data.lotteryArea) {
                            case "0":
                                rotateFn(0, 337.5, '获得再来一次');
                                break;
                            case "1":
                                rotateFn(1, 292.5, '获得1%加息券');
                                break;
                            case "2":
                                rotateFn(2, 247.5, '获得20积分!');
                                break;
                            case "3":
                                rotateFn(3, 202.5, '获得10元投资红包！');
                                break;
                            case "4":
                                rotateFn(4, 157.5, '谢谢参与，请您继续努力');
                                break;
                            case "5":
                                rotateFn(5, 112.5, '获得0.5%加息券！');
                                break;
                            case "6":
                                rotateFn(6, 67.5, '获得10积分！');
                                break;
                            case "-1":
                                rotateFn(-1, 22.5, '获得30元投资红包');
                                break;
                        }


                    }else{
                        tipsBoxFalse(data.msg);

                    }
                }/*,error:function(){
                tipsBoxFalse("获得了古钱币");
            }*/
            });
        }
        $(this).removeClass("pointer");

    });

    var tipsBox = function(txt){
        //构造确认框DOM
        $.layer({
            type: 1,
            closeBtn: false,
            title: false,
            area: ['500px', '333px'],
            border: [0],
            background: 'none',
            fadeIn: 300,
            //time:2000000000,
            page: {
                html: '<div class="lotteryTipsWrap"><div class="lotteryTipsTitle"><div class="icondraw01"></div><a href="javascript:;" class="tipCloseBtn">X</a></div><p class="img_txt01"></p><p class="tipsTxt">'+txt+'</p></div>'
            },
            close: function(index){
                //ReactDOM.render(<WinnersList />, document.getElementById("winningList"));
                getLotteryList();
            }
        })
        $(".tipCloseBtn").click(function(){
            //ReactDOM.render(<WinnersList />, document.getElementById("winningList"));
            getLotteryList();
            layer.closeAll();
        });
    }

    var tipsBoxFalse = function(txt){
        //构造确认框DOM
        $.layer({
            type: 1,
            closeBtn: false,
            title: false,
            area: ['500px', '333px'],
            border: [0],
            background: 'none',
            fadeIn: 300,
            //time:2000000000,
            page: {
                html: '<div class="lotteryTipsWrap"><div class="lotteryTipsTitle"><div class="icondraw02"></div><a href="javascript:;" class="tipCloseBtn">X</a></div><p class="img_txt02"></p><p class="tipsTxt">'+txt+'</p></div>'
            },
            close: function(index){
                //ReactDOM.render(<WinnersList />, document.getElementById("winningList"));
                getLotteryList();
            }
        })
        $(".tipCloseBtn").click(function(){
            //ReactDOM.render(<WinnersList />, document.getElementById("winningList"));
            getLotteryList();
            layer.closeAll();
        });
    }
})



//ReactDOM.render(<WinnersList />, document.getElementById("winningList"));