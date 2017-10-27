// JavaScript Document
(function($){
  $.fn.searchInput = function(opt) {	
		var defaults = {
			triggerID:this,
			autolist:"#keyword",
			isshow:true,
			parameter:'',
			hiddenInp:"",
			prop:"",
			callback: function(){}
		};		
		var opts = $.extend(defaults,opt);		
		$(opts.autolist).parent().css({"position":"relative","z-index":2});
		var close = function(){
			if(opts.isshow){
				$(opts.autolist).hide().html("");
			}
		}
		var updowncheck = function(searchInp,event){
			
			var nowtr = $(opts.autolist+" tr").index($(opts.autolist+" tr.tdover"));
			var trlength = $(opts.autolist+" tr").size()-1;
			var td;
			if(event.keyCode == 38){
				if(nowtr == -1)
				{
					$(opts.autolist+" tr").eq(0).addClass("tdover");
					td = $(opts.autolist+" tr").eq(0).find("td").eq(0);
					searchInp.val(td.html());						
					$(opts.autolist).scrollTop(0);						
				}
				else if(nowtr == 0)
				{	
					$(opts.autolist+" tr").eq(nowtr).removeClass("tdover")
					$(opts.autolist+" tr").eq(trlength).addClass("tdover");
					td = $(opts.autolist+" tr").eq(trlength).find("td").eq(0);
					searchInp.val(td.html());
					$(opts.autolist).scrollTop(trlength*24);						
				}
				else
				{
					$(opts.autolist+" tr").eq(nowtr).removeClass("tdover");
					$(opts.autolist+" tr").eq(nowtr-1).addClass("tdover");
					td = $(opts.autolist+" tr").eq(nowtr-1).find("td").eq(0);
					searchInp.val(td.html());
					if(Math.ceil($(opts.autolist).scrollTop() / 24)==nowtr){
						$(opts.autolist).scrollTop($(opts.autolist).scrollTop() - 24);
					}
				}					
			}
			else
			{
				if(nowtr == -1)
				{
					$(opts.autolist+" tr").eq(0).addClass("tdover");
					td = $(opts.autolist+" tr").eq(0).find("td").eq(0);
					searchInp.val(td.html());
					$(opts.autolist).scrollTop(0);
				}
				else if(nowtr == trlength)
				{	
					$(opts.autolist+" tr").eq(nowtr).removeClass("tdover")
					$(opts.autolist+" tr").eq(0).addClass("tdover");
					td = $(opts.autolist+" tr").eq(0).find("td").eq(0);
					searchInp.val(td.html());
					$(opts.autolist).scrollTop(0);
				}
				else
				{
					$(opts.autolist+" tr").eq(nowtr).removeClass("tdover");
					$(opts.autolist+" tr").eq(nowtr + 1).addClass("tdover");
					td = $(opts.autolist+" tr").eq(nowtr + 1).find("td").eq(0);
					searchInp.val(td.html());
					if(Math.ceil((trlength*24 - $(opts.autolist).height() - $(opts.autolist).scrollTop()) / 24) == (trlength - nowtr - 1)){
						$(opts.autolist).scrollTop($(opts.autolist).scrollTop() + 24);
					}
				}
			}
			if(opts.hiddenInp && td){				
				$(opts.hiddenInp).val(td.nextAll("input.prop").val());
			}
			
		};		
		var autocomplete = function(searchInp,event){
			var inputval = $.trim(searchInp.val()),autop = "",autotable = $('<table class="autotable">'),autoi=0;			
			$.getJSON(opts.url + "?keyword=" + encodeURI(inputval) + opts.parameter, function(jsontxt){				
			  	$.each(jsontxt,function(key,v){			  		
					if( v.userName.toLowerCase().indexOf(inputval.toLowerCase()) > -1 ){
						autop += "<tr><td>" + v.userName + "</td>";
						if(opts.prop){							
							autop += "<input type='hidden' class='prop' value="+v[opts.prop]+">";											
						}
						autop += "</tr>";
						autoi+=1;						
					}
				});
				if(autop != "")
					{						
						$(opts.autolist).html("").append(autotable.append(autop)).show();						
						$(opts.autolist+" tr").size() > 10 ? $(opts.autolist).css("height","200px") : $(opts.autolist).css("height","");		
						$(opts.autolist+" tr").mouseover(function(){
								opts.isshow = false;
								$(opts.autolist+" tr").removeClass("tdover");
								$(this).addClass("tdover");
							});
						$(opts.autolist+" tr").mouseout(function(){
								opts.isshow = true;
								$(this).removeClass("tdover");
							});		
						var that = this;
						$(opts.autolist+" tr").mousedown(function(event){
								opts.isshow = true;
								searchInp.val($(this).find("td").eq(0).html());								
								$(opts.autolist).hide();
								search();
							});	
					}
					else
					{
						close();
					}		  
			});			
		};		
		var search = opts.callback;
		$(opts.triggerID).bind("blur",function(){
			close();
		});
		$(opts.triggerID).parent().on("mouseleave",function(){
			close();
		});
		$(opts.triggerID).bind("keyup",function(event){
			event = event ? event :(window.event ? window.event : null);
			var val = $.trim($(this).val());
			
			if(((event.keyCode == 38) || (event.keyCode == 40)) && ($(opts.autolist).is(":visible"))){
				updowncheck($(this),event); 
			}
			else if((val != "") && (event.keyCode != 13) && (event.keyCode != 37) && (event.keyCode != 39) && (event.keyCode != 27) )
			{
				autocomplete($(this),event);
			}
			else if(event.keyCode == 27){
				$(opts.autolist).hide()
			}
			if((event.keyCode === 13) && (val != "")){
				$(opts.autolist).hide();
				search();
			} 
		});		
  };
})(jQuery);