var imgLimit = 15;//允许上传图片数量

//上传
function ajaxFileUpload(sId, _url, name) {
	var image_server_url = $("#image_server_url").val();	
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var _ul = $("#"+sId).parents("ul.photoUpload"),
					sHtml = "",
					num = parseInt(_ul.find('li.addFileBtn').siblings().length);
				//上传张数限制
				if(_ul && num >= imgLimit){
					top.layer.msg("上传文件数量不能超过"+imgLimit+"张", {icon: 5});						
					_ul.find('.addFileBtn input').css('z-index','-1');					
				} else {
					sHtml += '<li data-mum="' + data.tmpId + '">';
					//sHtml += '<a id="#' + data.tmpId + '" href="' + image_server_url + data.url + '" class="fileshow"><img src="' + image_server_url + data.url + '" alt=""></a>';
					sHtml += '<a href="'+image_server_url+data.url+'"><img src="' + image_server_url + data.url + '" alt=""></a>';
					sHtml += '<div style="display:none;"><img id="' + data.tmpId + '" src="' +image_server_url+ data.url + '"></div>';
					sHtml += '<input name="'+name+'" value="'+ data.url+'" type="hidden">';
					sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
					sHtml += '</li>';				
					_ul.append(sHtml);		
				}
			}else{
				if (data.state != null ) {					
					top.layer.msg(data.state, {icon: 5});
				} else {
					top.layer.msg('上传文件失败，请重试!', {icon: 5});					
				}
			}			
		},
		error: function(data,status, e) {
			if(status=='error'){
				top.layer.msg(data.responseXML.title==null?'上传请求失败':data.responseXML.title, {icon: 5});				
			}
			//alert("上传请求失败！");
		}
	});
	//$(".fileshow").fancybox();
}

//上传档案照
function ajaxFileUpload2(sId, _url,name) {
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var sHtml = "";
				sHtml += '<li data-mum="' + data.tmpId + '">';
				sHtml += '<img src="' + data.url + '" alt="" style="width:100px;height:100px;float:left;">';
				sHtml += '<input name="'+name+'" value="'+data.url+'" type="hidden">';
				sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
				sHtml += '</li>';
				var targetObj  = $("#"+sId).parents("ul").append(sHtml);
				var physicalCount = $("#physicalCount").val();
				var archivesCount = $("#archivesCount").val();
				$("#physicalCount").val(physicalCount+1);
				$("#archivesCount").val(archivesCount+1);
				$.parser.parse(targetObj);
			}else{
				if (data.state != null ) {
					alert(data.state);
				} else {
					alert("上传文件失败，请重试!");
				}
			}
		},
		error: function(data,status, e) {
			if(status=='error'){
				top.layer.msg(data.responseXML.title==null?'上传请求失败':data.responseXML.title, {icon: 5});				
			}
		}
	});
}

//头像上传
function ajaxFileUpload3(sId, _url, name) {
	var image_server_url = $("#image_server_url").val();
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {				
			if(data.state=="SUCCESS"){
				var _ul = $("#"+sId).parents("ul");
				if(_ul.children().length>1){
					_ul.children().last().remove();					
				}
				var sHtml = "";
				sHtml += '<li data-mum="' + data.tmpId + '">';
				sHtml += '<a href="' + image_server_url + data.url + '" class="fileshow"><img src="' + image_server_url + data.url + '" alt=""></a>';
				sHtml += '<div style="display:none;"><img id="' + data.tmpId + '" src="' +image_server_url+ data.url + '"></div>';
				sHtml += '<input name="'+name+'" value="'+ data.url+'" type="hidden">';
				sHtml += '<a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a>';
				sHtml += '</li>';
				_ul.append(sHtml);
			}else{
				if (data.state != null ) {					
					alert(data.state);
				} else {
					alert("上传文件失败，请重试!");
				}
			}
		},
		error: function(data,status, e) {
			if(status=='error'){
				top.layer.msg(data.responseXML.title==null?'上传请求失败':data.responseXML.title, {icon: 5});				
			}
		}
	});
}


//上传
function ajaxFileUpload4(sId, _url, name) {
	var image_server_url = $("#image_server_url").val();	
	$.ajaxFileUpload({
		url: _url,
		secureuri: false,
		fileElementId: sId,
		dataType: 'json',
		data:{typeCode:'aaa'},
		success: function(data) {
			if(data.state=="SUCCESS"){
				var _ul = $("#"+sId).parents("ul.photoUpload"),
					sHtml = "",
					num = parseInt(_ul.find('li.addFileBtn').siblings().length);
					sHtml = '<li data-mum="' + data.tmpId + '"><a href="'+image_server_url+data.url+'"><img src="' + image_server_url + data.url + '" alt=""></a><div style="display:none;"><img id="' + data.tmpId + '" src="' +image_server_url+ data.url + '"></div><input name="'+name+'" value="'+ data.url+'" type="hidden"><a class="closePic" style="float:left;" href="javascript:;" title="删除图片">x</a></li>';				
					console.log(sHtml)
					_ul.find(".imgdom").html(sHtml);		
			}else{
				if (data.state != null ) {					
					top.layer.msg(data.state, {icon: 5});
				} else {
					top.layer.msg('上传文件失败，请重试!', {icon: 5});					
				}
			}			
		},
		error: function(data,status, e) {
			if(status=='error'){
				top.layer.msg(data.responseXML.title==null?'上传请求失败':data.responseXML.title, {icon: 5});				
			}
			//alert("上传请求失败！");
		}
	});
	//$(".fileshow").fancybox();
}

//进入页面，判断图片上传数量
var num = $('#borrowPagePic').find('li.addFileBtn').siblings().length;
if(num == imgLimit){
	$('#borrowPagePic').find('.addFileBtn input').css('z-index','-1');
}

//点击添加按钮时，判断图片上传数量
$('#borrowPagePic').find('.addFileBtn').click(function(){
	num = $('#borrowPagePic').find('li.addFileBtn').siblings().length;
	if(num == imgLimit){
		alert("上传文件数量不能超过"+imgLimit+"张");
	}
});

//--------------------------------删除图片操作-------------------

$("body").off('click.del').on("click.del",".closePic",function(){

	var $li = $(this).parent("li");
	var $addBtn = $(this).parents().parents().find('input[type="file"]');
	var imgPath = $li.children("input").attr("value");
	var id = $li.attr("data-mum");
	var href = '/loan/borrow/deleteBorrowPic.html?picPath=' + imgPath + '&id=' + id;
	top.layer.confirm('确定删除吗？', {icon: 3, title:'提示'}, function(index){
		if (id != 0) {
		    $("#form").append('<input name="delIds" type="hidden" value="'+id+'" />')
		}
		$.ajax({
			type:'post',
			url:href + '&randomTime=' + (new Date()).getTime(),
			async:false,
			dataType:"json",
			success:function(data){
				$li.remove();
				$addBtn.css('z-index','0');				
			},
			error:function(data) {
				top.layer.msg('删除失败', {icon: 5});
			}
		});
	  top.layer.close(index);
	});	
});

$("document").on("delMortgage","click",function(){
	var _this =$(this);
	var id = $(this).attr("data-val");
	top.layer.confirm('确认对话框","确定删除吗？', {icon: 3, title:'提示'}, function(index){
	  	_this.parents(".car_type").remove();
	 	$("#form").append('<input name="delMortgageIds" type="hidden" value="'+id+'" />')
	  	top.layer.close(index);
	});	
});

