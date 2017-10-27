//—E—定义全局变量
$.ajaxSetup({
	cache: false,
	error: function(textStatus,responseText,status){
		top.window.location.href = '/admin/login.html';
		//top.layer.alert(textStatus.responseText.substring(0,50), {icon: 5});
	}
});
//custom exception prefix
var exceptionPrefix ="exception:";

//$.jgrid.defaults.width = 780;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';
$.fn.jqTreeGrid = function(option) {
	var self = $(this),
		defaultOption = {
			datatype: "json",
			mtype:'post',
			rownumbers: true, //序号
			rownumWidth:60,
			autowidth: true,
			multiselect: true,
			height: 860,
			loadonce: false,
			scrollOffset: 20,
			multiboxonly:true,
			shrinkToFit: true, // must be set with frozen columns, otherwise columns will be shrank to fit the grid width
			prmNames: {
				page: "page.page",
				rows: "page.pageSize",
				sort: "page.sort",
				order: "page.order",
				search: "_search",
				nd: "nd",
				npage: null
			},
			jsonReader: {
				root: "rows", // json中代表实际模型数据的入口
				page: "page", // json中代表当前页码的数据
				total: "totalPage", // json中代表页码总数的数据
				records: "count", // json中代表数据行总数的数据
				repeatitems: true, // 如果设为false，则jqGrid在解析json时，会根据name来搜索对应的数据元素（即可以json中元素可以不按顺序）；而所使用的name是来自于colModel中的name设定。
				cell: "cell",
				id: "id",
				userdata: "userdata",
				subgrid: {
					root: "rows",
					repeatitems: true,
					cell: "cell"
				}
			},
			rowNum: 15,
			rowList: [15, 30, 50],
			viewrecords: true,
			sortorder: "desc",
			pager: "#jqGridPager",
			loadError:function(xhr,status,error){
				top.window.location.href = '/admin/login.html';
			}
		};
	$.extend(defaultOption, option);
	self.jqGrid(defaultOption);
	return this;
}

//表单增、删、查、改处理方法
$.fn.treeGridOptions = function() {}

//数据新增
/*
	fn父页回调方法
	runFn请求弹窗前事件
*/
$.fn.treeGridOptions.addFun = function(that,fn,runFn) {
	var self = $(that),
		tableId = "#" + self.attr("data-tid"),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height");
		top.gridobj = $(tableId);
	var title = self.attr("data-title")?self.attr("data-title"):"新增";
	if(fn){
		top.addfn = fn;
	}
	if(runFn){
		if(!runFn()){
			return false;
		}
	}
	var areaArray = [];
	if((widthval==undefined)&&(heightval==undefined)){
		areaArray = ['960px', '75%'];
	}
	else{
		areaArray = [widthval,heightval];
	}
	$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: title,
				type: 1,
				closeBtn: 1,
                anim: -1,
				area: areaArray,
				shadeClose: false,
				id:"addFun",
				content: '',
				zIndex:100,
				btn: ['提交', '取消'],
				success:function (layero,index) {
					top.$(layero['selector']).find('.layui-layer-content').html(str)
				},
				yes: function(index, layero) {
					var pass=0;
					if($('.questionType-con')){
					if(top.$("input[name='papersName']").val()==""){
						top.$("input[name='papersName']").parent('.input-box').find('span').text('请填写');
					}
					top.$('.questionType-con .input-box input[name="startScore"],.questionType-con .input-box input[name="endScore"]').each(function () {
						if(top.$(this).val()==""){
							top.$(this).parent('.input-box').find('span').text('请填写');
							pass=pass+1
						}else{
							pass=pass+0
						}
					})
					}
					if(pass!=0){
						return false
					}
					top.$(layero['selector']).find("form").submit();
				},
				btn2: function(index, layero) {
					//top.$(layero['selector']).find("form")[0].reset();
					top.layer.closeAll();
					return false;
				}
			});
		}
	});
}



$.fn.treeGridOptions.batch = function(that,fn,runFn) {
	var self = $(that),
		tableId = "#" + self.attr("data-tid"),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height");
		top.gridobj = $(tableId);
	if(fn){
		top.addfn = fn;
	}
	if(runFn){
		if(!runFn()){
			return false;
		}
	}
	var areaArray = [];
	if((widthval==undefined)&&(heightval==undefined)){
		areaArray = ['960px', '75%'];
	}
	else{
		areaArray = [widthval,heightval];
	}
	$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: "批量处理",
				type: 1,
				closeBtn: 1,
                anim: -1,
				area: areaArray,
				shadeClose: false,
				id:"addFun",
				content: '',
				zIndex:100,
				btn: [],
				success:function (layero,index) {
					top.$(layero['selector']).find('.layui-layer-content').html(str)
				}
			});
		}
	});
}


$.fn.treeGridOptions.addFuncompany = function(that,fn,runFn) {
	var self = $(that),
		tableId = "#" + self.attr("data-tid"),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height");
		top.gridobj = $(tableId);

	if(fn){
		top.addfn = fn;
	}
	if(runFn){
		if(!runFn()){
			return false;
		}
	}
	var areaArray = [];
	if((widthval==undefined)&&(heightval==undefined)){
		areaArray = ['75%', '75%'];
	}
	else{
		areaArray = [widthval,heightval];
	}
	$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: "新增",
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: areaArray,
				id:"addFuncompany",
				shadeClose: false,
				content: str,
				btn: ['提交', '取消'],
				yes: function(index, layero) {
					var pass=0;
					if($('.questionType-con')){
					if(top.$("input[name='papersName']").val()==""){
						top.$("input[name='papersName']").parent('.input-box').find('span').text('请填写');
					}
					top.$('.questionType-con .input-box input[name="startScore"],.questionType-con .input-box input[name="endScore"]').each(function () {
						if(top.$(this).val()==""){
							top.$(this).parent('.input-box').find('span').text('请填写');
							pass=pass+1
						}else{
							pass=pass+0
						}
					})
					}
					if(pass!=0){
						return false
					}
					top.$(layero['selector']).find("form").submit();
				},
				btn2: function(index, layero) {
					//top.$(layero['selector']).find("form")[0].reset();
					top.layer.closeAll();
					return false;
				}
			});
		}
	});
}

$.fn.treeGridOptions.SaddFun = function(that,fn,runFn) {
	var self = $(that),
		tableId = "#" + self.attr("data-tid"),
		areaW = self.attr("data-w") ? self.attr("data-w") : '30%',
		areaH = self.attr("data-h") ? self.attr("data-h") : '30%',
		title = self.attr("data-title") ? self.attr("data-title") : '新增',
		url = self.attr("data-url");
		top.gridobj = $(tableId);
	if(fn){
		top.addfn = fn;
	}
	if(runFn){
		if(!runFn()){
			return false;
		}
	}
	$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: title,
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: [areaW, areaH],
				shadeClose: false,
				content: str,
				id:"SaddFun",
				btn: ['提交', '重置'],
				yes: function(index, layero) {
					var pass=0;
					if($('.questionType-con')){
					if(top.$("input[name='papersName']").val()==""){
						top.$("input[name='papersName']").parent('.input-box').find('span').text('请填写');
					}
					top.$('.questionType-con .input-box input[name="startScore"],.questionType-con .input-box input[name="endScore"]').each(function () {
						if(top.$(this).val()==""){
							top.$(this).parent('.input-box').find('span').text('请填写');
							pass=pass+1
						}else{
							pass=pass+0
						}
					})
					}
					if(pass!=0){
						return false
					}
					top.$(layero['selector']).find("form").submit();
				},
				btn2: function(index, layero) {
					top.$(layero['selector']).find("form")[0].reset();
					return false;
				}
			});
		}
	});
}

//数据编辑
$.fn.treeGridOptions.editFun = function(that, fn, id) {
	var self = $(that),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height"),
		titleval = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	if(fn){
		top.editfn = fn;
	}
	var areaArray = ['75%', '75%'];
	if((widthval!=undefined)&&(heightval!=undefined)){
		areaArray = [widthval,heightval];
	}
	var titleName="编辑";
	if(titleval!=undefined){
		titleName = titleval;
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			var index =	top.layer.open({
					title: titleName,
					type: 1,
					closeBtn: 1,
					shift: -1,
					area: areaArray,
					id:'editFun',
					content: '',
					shadeClose: false,
					success:function (layero,index) {
						top.$(layero['selector']).find('.layui-layer-content').html(str)
					},
					btn: ['提交', '重置'],
					yes: function(index, layero) {
						top.$(layero['selector']).find("form").submit();
					},
					btn2: function(index, layero) {
						top.$(layero['selector']).find("form")[0].reset();
						return false;
					}
				});
				self.data("index",index);
		}
	});
}


//文章编辑
$.fn.treeGridOptions.ArticleeditFun = function(that, fn, id) {
	var self = $(that),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height"),
		titleval = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	if(fn){
		top.editfn = fn;
	}
	var areaArray = ['75%', '75%'];
	if((widthval!=undefined)&&(heightval!=undefined)){
		areaArray = [widthval,heightval];
	}
	var titleName="编辑";
	if(titleval!=undefined){
		titleName = titleval;
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{  top.newhtml = "";
				top.layer.open({
					title: titleName,
					type: 1,
					closeBtn: 1,
					shift: -1,
					area: areaArray,
					content: "",
					id:"ArticleeditFun",
					shadeClose: false,
					success: function(layero, index){
                        top.$(layero['selector']).find('.layui-layer-content').html(str)
						top.newhtml = top.$("#articledom").clone();
					},
					btn: ['提交', '重置'],
					yes: function(index, layero) {
						top.$(layero['selector']).find("form").submit();
					},
					btn2: function(index, layero) {
						top.$(layero['selector']).find("form")[0].reset();
						top.ue.setContent(top.thisContent);
						top.newhtml = top.$("#articledom").clone();
						return false;
					}
				});
		}
	});
}
//文章推荐、置顶
$.fn.treeGridOptions.articleRecommendFun = function(that,id, fn ){
	var self = $(that),
	url = self.attr("data-url"),
	tableId = "#" + self.attr("data-tid");
if(id == null){
	id = $(tableId).jqGrid("getGridParam", "selarrrow");
	if (!id.length) {
		top.layer.alert("请勾选要操作的记录!", {
			icon: 5,
			shadeClose: true
		});
		return false;
	}
	id = id.toString();
}
	top.layer.confirm('是否确定？', {
		icon: 3,
		title: '提示'
	}, function(index) {
		top.$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			data: {ids: id},
			success: function(str) {
				if (str.result) {
					top.layer.alert(str.msg, {
						icon: 6
					}, function() {
						top.layer.closeAll();
						$(tableId).trigger("reloadGrid"); //重新载入
						if(fn){
							fn(url.split("parentId=")[1]);
						}
					});
				} else {
					top.layer.alert(str.msg, {
						icon: 5
					});
				}
			}
		});
		top.layer.close(index);
	});
}
//数据编辑
$.fn.treeGridOptions.DiscounteditFun = function(that, fn, id) {
	var self = $(that),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height"),
		titleval = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	if(fn){
		top.editfn = fn;
	}
	var areaArray = ['960px', '75%'];
	if((widthval!=undefined)&&(heightval!=undefined)){
		areaArray = [widthval,heightval];
	}
	var titleName="编辑";
	if(titleval!=undefined){
		titleName = titleval;
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{  top.newhtml = "";
				top.layer.open({
					title: titleName,
					type: 1,
					closeBtn: 1,
					shift: -1,
					area: areaArray,
					content: "",
					shadeClose: false,
					success: function(layero, index){
                        top.$(layero['selector']).find('.layui-layer-content').html(str)
						top.newhtml = top.$("#redpackage").clone();
					},
					btn: ['提交', '重置'],
					yes: function(index, layero) {
						top.$(layero['selector']).find("form").submit();
					},
					btn2: function(index, layero) {
						top.$(layero['selector']).find("form")[0].reset();
						top.$("#j-content").html(top.newhtml);
						top.newhtml = top.$("#redpackage").clone();
						top.redrule();
						top.activityCodeChange();
						return false;
					}
				});
		}
	});
}

//数据冻结
$.fn.treeGridOptions.freezeFun = function(that,id,fn) {
	var self = $(that),
		url = self.attr("data-url"),
		widthval = self.attr("data-width"),
		heightval = self.attr("data-height"),
		titleval = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	if(fn){
		top.editfn = fn;
	}
	var areaArray = ['75%', '75%'];
	if((widthval!=undefined)&&(heightval!=undefined)){
		areaArray = [widthval,heightval];
	}
	var titleName="编辑";
	if(titleval!=undefined){
		titleName = titleval;
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: titleName,
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: areaArray,
				content: str,
				shadeClose: false,
				btn: ['提交', '取消'],
				yes: function(index, layero) {
					top.$(layero['selector']).find("form").submit();
				},
				btn2: function(index, layero) {
					top.layer.closeAll();
					return false;
				}
			});
		}
	});
}

//数据行编辑
$.fn.treeGridOptions.EditFun = function(that,id,fn) {
	var self = $(that),
		area = self.attr("data-area"),
		url = self.attr("data-url"),
		dialogTitle=self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(fn){
		top.editfn = fn;
	}
	top.gridobj = $(tableId);
	var areaArray = ['65%', '80%'],areaList = [];
	if(area){
		areaList = area.split(",");
		areaArray = [areaList[0],areaList[1]];
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
				top.newhtml = "";
				top.layer.open({
					title:  dialogTitle ? dialogTitle : "编辑",
					type: 1,
					closeBtn: 1,
					shift: -1,
					id:'lineEditFun',
					area: areaArray,
					content: "",
					success:function (layero,index) {
						top.$(layero['selector']).find('.layui-layer-content').html(str)
						top.newhtml = top.$("#con").clone();
					},
					shadeClose: false,
					btn: ['提交', '重置'],
					yes: function(index, layero) {
						if(top.$('.photoUpload').length > 0 &&top.$('.photoUpload li').length<3){
							top.layer.alert('请上传借款资料', {
								icon: 2,zIndex:19899999
							});
							return false
						}
						top.$(layero['selector']).find("form").submit();
					},
					btn2: function(index, layero) {
						top.$(layero['selector']).find("form")[0].reset();
						//top.$("#j-content").html(top.newhtml);
						top.ue.setContent('');
						top.newhtml = top.$("#con").clone();
						top.fun1();
						top.fun2();
						top.saleStyle();
						top.fun3();
						return false;
					}
				});
		}
	});
}

//数据行编辑
$.fn.treeGridOptions.lineEditFun = function(that,id,fn) {
	var self = $(that),
		area = self.attr("data-area"),
		url = self.attr("data-url"),
		dialogTitle=self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(fn){
		top.editfn = fn;
	}
	top.gridobj = $(tableId);
	var areaArray = ['65%', '80%'],areaList = [];
	if(area){
		areaList = area.split(",");
		areaArray = [areaList[0],areaList[1]];
	}
	$.ajax({
		url: url,
		data:{id: id},
		type: 'POST',
		dataType: 'text'
	})
	.done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
				top.layer.open({
					title:  dialogTitle ? dialogTitle : "编辑",
					type: 1,
					closeBtn: 1,
					shift: -1,
					id:'lineEditFun',
					area: areaArray,
					content: "",
					success:function (layero,index) {
						top.$(layero['selector']).find('.layui-layer-content').html(str)
					},
					shadeClose: false,
					btn: ['提交', '重置'],
					yes: function(index, layero) {
						if(top.$('.photoUpload').length > 0 &&top.$('.photoUpload li').length<3){
							top.layer.alert('请上传借款资料', {
								icon: 2,zIndex:19899999
							});
							return false
						}
						top.$(layero['selector']).find("form").submit();
					},
					btn2: function(index, layero) {
						top.$(layero['selector']).find("form")[0].reset();
						return false;
					}
				});
		}
	});
}



$.fn.treeGridOptions.lineSetFun = function(that, id) {
	var self = $(that),
		url = self.attr("data-url"),
		title = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	top.layer.confirm(title, {
		icon: 3,
		title: '提示'
	}, function(index) {
		top.$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			data: {id: id.toString()},
			success: function(str) {
				if (str.result) {
					top.layer.alert(str.msg, {
						icon: 6,
						scrollbar:false
					}, function() {
						top.layer.closeAll();
						$(tableId).trigger("reloadGrid"); //重新载入
					});
				} else {
					top.layer.alert(str.msg, {
						icon: 5
					});
				}
			}
		});
		top.layer.close(index);
	});
}

//借贷审核
$.fn.treeGridOptions.setFun = function(that, id) {
	var self = $(that),
		url = self.attr("data-url"),
		title = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	$.post(url, {
		id: id
	}, function(str) {
		top.layer.open({
			title: title?title: "设置",
			type: 1,
			closeBtn: 1,
			area: ['75%', '75%'],
			content: '',
			shift: -1,
			shadeClose: false,
			success:function (layero,index) {
				top.$(layero['selector']).find('.layui-layer-content').html(str)
			},
			btn: ['提交', '重置'],
			yes: function(index, layero) {
				top.$(layero['selector']).find("form").submit();
			},
			btn2: function(index, layero) {
				top.$(layero['selector']).find("form")[0].reset()
				return false;
			}
		});
	});
}

//数据授权
$.fn.treeGridOptions.authorize = function(that, id) {
	var self = $(that),
		url = self.attr("data-url"),
		title = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	top.gridobj = $(tableId);
	$.post(url, {
		id: id
	}, function(str) {
		top.layer.open({
			title: title?title: "设置",
			type: 1,
			closeBtn: 1,
			shift: -1,
			area: ['30%', '60%'],
			content: str,
			shadeClose: false,
			btn: ['提交', '重置'],
			yes: function(index, layero) {
				top.$(layero['selector']).find("form").submit();
			},
			btn2: function(index, layero) {
				top.treeGreat();
				return false;
			}
		});
	});
}

//查看数据
$.fn.treeGridOptions.checkFun = function(that, id) {
	var self = $(that),
		url = self.attr("data-url"),
		title = self.attr("data-title"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length || id.length > 1) {
			top.layer.alert("请选择一条记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id[0];
	}
	$.post(url, {
		id: id
	}, function(str) {
		var index = top.layer.open({
			title: title?title: "查看",
			type: 1,
			closeBtn: 1,
			id:'box',
			shift: -1,
			area: ['65%', '70%'],
			content: str,
			shadeClose: false,
			btn: ['关闭'],
			yes: function(index, layero) {
				top.layer.close(index);
				Blayer1 = true
			},
			cancel :function () {
				Blayer1 = true
			}
		});
	});
}

//数据删除
$.fn.treeGridOptions.deleteFun = function(that, fn, id) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length) {
			top.layer.alert("请勾选要删除的记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id.toString();
	}
	top.layer.confirm('你确定要删除当前选中的记录?', {
		icon: 3,
		title: '提示'
	}, function(index) {
		top.$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			data: {ids: id},
			success: function(str) {
				if (str.result) {
					top.layer.alert(str.msg, {
						icon: 6
					}, function() {
						top.layer.closeAll();
						$(tableId).trigger("reloadGrid"); //重新载入
						if(fn){
							fn(url.split("parentId=")[1]);
						}
					});
				} else {
					top.layer.alert(str.msg, {
						icon: 5
					});
				}
			}
		});
		top.layer.close(index);
	});
}

//禁用后台用户
$.fn.treeGridOptions.forbiddenFun = function(that, id,fn ) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid");
	if(id == null){
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
		if (!id.length) {
			top.layer.alert("请勾选要操作的记录!", {
				icon: 5,
				shadeClose: true
			});
			return false;
		}
		id = id.toString();
	}
		top.layer.confirm('是否确定？', {
			icon: 3,
			title: '提示'
		}, function(index) {
			top.$.ajax({
				url: url,
				type: 'POST',
				dataType: 'json',
				data: {ids: id},
				success: function(str) {
					if (str.result) {
						top.layer.alert(str.msg, {
							icon: 6
						}, function() {
							top.layer.closeAll();
							$(tableId).trigger("reloadGrid"); //重新载入
							if(fn){
								fn(url.split("parentId=")[1]);
							}
						});
					} else {
						top.layer.alert(str.msg, {
							icon: 5
						});
					}
				}
			});
			top.layer.close(index);
		});
	}
//数据操作
$.fn.treeGridOptions.operFun = function(that, fn, id) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid");
		if(id == null){
				id = $(tableId).jqGrid("getGridParam", "selarrrow");
				if (!id.length) {
					top.layer.alert("请勾选记录!", {
						icon: 5,
						shadeClose: true
					});
					return false;
				}
				id = id.toString();
			}

	top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'json',
		data: {ids: id},
		success: function(str) {
			if (str.result) {
				top.layer.alert(str.msg, {
					icon: 6
				}, function() {
					top.layer.closeAll();
					$(tableId).trigger("reloadGrid"); //重新载入
					if(fn){
						fn();
					}
				});
			} else {
				top.layer.alert(str.msg, {
					icon: 5
				});
			}
		}
	});
	top.layer.close();

}

//数据操作
$.fn.treeGridOptions.TDoperFun = function(that, fn, id) {

	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid");
	var lilength =  $(".ulval input").length;
	var noval = $(".noval").val();
	var Dids =[];
	if((lilength>0)&&(noval==0)){
		for(var j = 0;j<lilength;j++){
			Dids[j]=$(".ulval input").eq(j).val();
		}
		id = Dids.join(",");
	}
	else if((lilength==0)&&(noval==0)){
		top.layer.alert("所有数据都已经处理成功!", {
			icon: 5,
			shadeClose: true
		});
		return false;
	}
	else{
		if(id == null){
			id = $(tableId).jqGrid("getGridParam", "selarrrow");
			if (!id.length) {
				top.layer.alert("请勾选记录!", {
					icon: 5,
					shadeClose: true
				});
				return false;
			}
			id = id.toString();
		}
	}

	top.layer.confirm('请您先和第三方核对下该笔订单是否已经处理，然后确认下是否重新触发?', {
	  btn: ['确定','取消'] //按钮
	}, function(){
	  top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'json',
		data: {ids: id},
		success: function(str) {
			if (str.result) {
				top.layer.alert(str.msg, {
					icon: 6
				}, function() {
					top.layer.closeAll();
					$(tableId).trigger("reloadGrid"); //重新载入
					if(fn){
						fn();
					}
				});
			} else {
				top.layer.alert(str.msg, {
					icon: 5
				});
			}
		}
	});
	top.layer.close();
	}, function(){
	  top.layer.closeAll();
	});
}


//数据行删除
$.fn.treeGridOptions.lineDeleteFun = function(that, id, fn) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid"),
		title = self.attr("data-title");
	if(!title){
		title ='你确定要删除当前选中的记录?';
	}
	top.layer.confirm(title, {
		icon: 3,
		title: '提示'
	}, function(index) {
		top.$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			data: {ids: id.toString()},
			success: function(str) {
				if (str.result) {
					top.layer.alert(str.msg, {
						icon: 6
					}, function() {
						top.layer.closeAll();
						$(tableId).trigger("reloadGrid"); //重新载入
						if(fn){
							fn();
						}
					});
				} else {
					top.layer.alert(str.msg, {
						icon: 5
					});
				}
			}
		});
		top.layer.close(index);
	});
}

//数据行操作
$.fn.treeGridOptions.lineOperFun = function(that, id, fn) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid"),
		title = self.attr("data-title");
	if(!title){
		title ='你确定要继续操作吗?';
	}
	if(id == null){
        id = $(tableId).jqGrid("getGridParam", "selarrrow");
        if (!id.length || id.length > 1) {
            top.layer.alert("请选择一条记录!", {
                icon: 5,
                shadeClose: true
            });
            return false;
        }
        id = id[0];
    }
	top.layer.confirm(title, {
		icon: 3,
		title: '提示'
	}, function(index) {
		top.$.ajax({
			url: url,
			type: 'POST',
			dataType: 'json',
			data: {id: id.toString()},
			success: function(str) {
				if (str.result) {
					top.layer.alert(str.msg, {
						icon: 6
					}, function() {
						top.layer.closeAll();
						$(tableId).trigger("reloadGrid"); //重新载入
						if(fn){
							fn();
						}
					});
				} else {
					top.layer.alert(str.msg, {
						icon: 5
					});
				}
			}
		});
		top.layer.close(index);
	});
}


//数据搜索
$.fn.treeGridOptions.searchFun = function(that) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid"),
		data = self.parents("form").serialize();
	var url_w = url.indexOf('?') > -1 ? "&" : "?";
		$(tableId).jqGrid('setGridParam', {
		url: url + url_w + data,
		page: 1
	}).trigger("reloadGrid");

	//记录查询类型，为导出服务
	if(self.attr('id')=='keywordsSearch'){
		searchType = 1;
	}else if(self.attr('id')=='conditionSearch'){
		searchType = 2;
	}
	return false;
}

//刷新数据
$.fn.treeGridOptions.refreshFun = function(that) {
	var self = $(that),
		tableId = "#" + self.attr("data-tid");
		$(tableId).trigger("reloadGrid"); //重新载入
}

//数据管理
$.fn.treeGridOptions.manageFun = function(that) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid"),
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
	if (!id.length || id.length > 1) {
		top.layer.alert("请选择一条记录!", {
			icon: 5,
			shadeClose: true
		});
		return false;
	}
	top.gridobj = $("#jqGrid_02");
	top.$.post(url, {
		id: id[0]
	}, function(str) {
		top.layer.open({
			title: "角色用户设置",
			type: 1,
			closeBtn: 1,
			shift: -1,
			area: ['70%', '70%'],
			content: str,
			shadeClose: false,
			btn: ['提交','重置'],
			yes: function(index, layero) {
				top.$(layero['selector']).find("#form").submit();
			},
			btn2: function(index, layero) {
				return false;
			}
		});
	});
}

$.fn.treeGridOptions.grantSelectUsers = function(that, fn,userIds){
	var self = $(that);
		url = self.attr("data-url")+"?userIds="+userIds;
	top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	}).done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			var index = top.layer.open({
				title: "发放用户选择",
				type: 1,
				closeBtn: 1,
				shift: -1,
				id:'grantSelectUsers',
				area: ['75%', '75%'],
				shadeClose: false,
				content: str,
				btn: ['确定'],
				yes: function(index, layero) {
					var ids = $('#jqGrid_04').jqGrid('getDataIDs').toString();
					$("input[name='userIds']").val(ids);
					var userIds=$('#userIds').val();
					if (!userIds) {
						top.layer.alert("请先选择发放的用户!", {
							icon: 5,
							shadeClose: true
						});
						return false;
					} else {
						fn(userIds);
						top.layer.close(index);
					}
				}
			});
		}

	});
};

$.fn.treeGridOptions.viewHistory = function(that,fn){
	var self = $(that),
	url = self.attr("data-url"),
	title = self.attr("data-title"),
	tableId = "#" + self.attr("data-tid");
	top.$.post(url, function(str) {
		var index = top.layer.open({
			title: title?title: "查看",
			type: 1,
			closeBtn: 1,
			id:'box',
			shift: -1,
			area: ['80%', '70%'],
			content: str,
			shadeClose: false,
			btn: ['关闭'],
			yes: function(index, layero) {
				top.layer.close(index);
				Blayer1 = true
			},
			cancel :function () {
				Blayer1 = true
			}
		});
	});
};

//发放优惠券
$.fn.treeGridOptions.grantCoupon = function(that) {
	var self = $(that),
		url = self.attr("data-url"),
		tableId = "#" + self.attr("data-tid"),
		id = $(tableId).jqGrid("getGridParam", "selarrrow");
	top.gridobj = $("#jqGrid");
	top.$.ajax({
		url: url,
		type: 'POST',
		data:{id: id[0]},
		dataType: 'text'
	  }).done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			top.layer.open({
				title: self.attr("data-title"),
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: ['70%', '85%'],
				id:'grantCoupon',
				content: str,
				shadeClose: false,
				btn: ['提交'],
				yes: function(index, layero) {
					top.$(layero['selector']).find("#form").submit();
				}/*,
				btn2: function(index, layero) {
					return false;
				}*/
			});
        }
	});
}

/**
 * 弹框页面的信息选择：组织树、项目类型
 * @param that 主页面的触发html元素：button,input
 * @param callback  点击完成后回调函数
 */
$.fn.treeGridOptions.treeselect = function(that,callback){
	var self = $(that),
		url = self.attr("data-url");
	top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	}).done(function(str) {
		var index = top.layer.open({
				title: "选择",
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: ['30%', '60%'],
				shadeClose: false,
				content: str
		});
		self.data("index",index);
		if(callback){
			callback();
		}
	});
}

//借款方选择
$.fn.treeGridOptions.borrowSelect = function(that, fn){
	var self = $(that);
		url = self.attr("data-url");
		titleval = self.attr("data-title");
	var titleName="资金账户选择";
	if(titleval!=undefined){
		titleName = titleval;
	}
	top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	}).done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			var index = top.layer.open({
				title: titleName,
				type: 1,
				closeBtn: 1,
				shift: -1,
				id:'borrowSelect',
				area: ['65%', '70%'],
				shadeClose: false,
				content: str,
				btn: ['确定'],
				yes: function(index, layero) {
					var id = top.$("#jqGrid").jqGrid('getGridParam', 'selrow');
					if (!id) {
						top.layer.alert("请选择一条记录!", {
							icon: 5,
							shadeClose: true
						});
						return false;
					} else {
						var ret = top.$("#jqGrid").jqGrid('getRowData', id);
						fn(id, ret.userName);
						top.layer.close(index);
					}
				}
			});
		}

	});
};
var Blayer = true;
$.fn.treeGridOptions.ListShow = function(that,key){
	var self = $(that);
		url = self.attr("data-url");
		widthval = self.attr("data-width");
		heightval = self.attr("data-height");
	if(Blayer==false){
		return false
	}
	Blayer = false;
	var areaArray = [];
	if((widthval==undefined)&&(heightval==undefined)){
		areaArray = ['30%', '30%'];
	}
	else{
		areaArray = [widthval,heightval];
	}
	top.$.ajax({
		url: url,
		type: 'POST',
		cache :false,
		dataType: 'text'
	}).done(function(str) {

		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			var index = top.layer.open({
				title: self.attr("data-title"),
				type: 1,
				closeBtn: 1, //不显示关闭按钮
				shift: -1,
				id:'ListShow',
				area: areaArray,
				shadeClose: false,
				content: "",
				btn: ['关闭'],
				success:function (layero,index) {
                    top.$(layero['selector']).find('.layui-layer-content').html(str)
                },
				yes: function(index, layero) {
						top.layer.close(index);
					Blayer = true
				},
				cancel :function () {
					Blayer = true
				}
			});
		}

	});
};

//借款方选择
$.fn.treeGridOptions.protocolSelect = function(that, fn){
	var self = $(that);
		url = self.attr("data-url");
	top.$.ajax({
		url: url,
		type: 'POST',
		dataType: 'text'
	}).done(function(str) {
		if(str.indexOf(exceptionPrefix) > -1){
			top.layer.alert(str.substring(exceptionPrefix.length), {
				icon: 5,
				shadeClose: true
			});
		}else{
			var index = top.layer.open({
				title: "协议选择",
				type: 1,
				closeBtn: 1,
				shift: -1,
				area: ['65%', '70%'],
				shadeClose: false,
				content: str,
				id:'protocolSelect',
				btn: ['确定'],
				yes: function(index, layero) {
					var id = top.$("#jqGrid").jqGrid('getGridParam', 'selrow');
					if (!id) {
						top.layer.alert("请选择一条记录!", {
							icon: 5,
							shadeClose: true
						});
						return false;
					} else {
						var ret = top.$("#jqGrid").jqGrid('getRowData', id);
						fn(id, ret.name);
						top.layer.close(index);
					}
				}
			});
		}

	});
};
//页面对象
$.fn.page = {};
// 条件查询下拉框效果
$.fn.page.dropdownSelectHoverFun = function(that) {
  var self = $(that);
  self.addClass("open");
  self.hover(
	function(){
	  self.addClass("open");
	},
	function(e) {
	if(e.target.nodeName != "INPUT"){
	  if( e.target.nodeName != "SELECT" && e.target.nodeName != "OPTION"){
		self.removeClass("open");
	  }
	}

  });
};


//去除符合条件的最后一个字符
$.fn.page.disLastChar = function(str, char) {
	var len = str.length,
		lastChar = str.charAt(len - 1);
	if( lastChar == char){
		str = str.substring(0, len-1);
	}
	return str;
};

//两位小数格式
$.fn.page.charTwoDecimals = function(str) {
	var str = str.toString(),
		len = str.length;
	if(str.indexOf(".") < 0){
		str = str + ".00";
	} else {
		var sArr = str.split(".");
		if(sArr[1].length == 1){
			str = str + "0";
		} else if(sArr[1].length == 0) {
			str = str + "00";
		}
	}
	return str;
};

//只能是数字包括小数
$.fn.page.decimalsNumber = function(number){
	var number2 = '';
	if(number != '' ){
		if(number.search(/^([1-9]\d*|0)(?:\.\d{0,2})?$/) == -1) {
			number = (number2) ? number2 : '';
		} else {
			number2 = number;
		}
	}
	return number;
};
//只能数字包括三位小数
$.fn.page.decimalsNumber1 = function(number){
	var number2 = '';
	if(number != '' ){
		number = number.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
		number = number.replace(/^\./g,"");  //验证第一个字符是数字而不是.
		number = number.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
		number = number.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		number = number.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
	}
	return number;
};

//日期格式转换
$.fn.page.transFormatDate = function(value){
	function formatDate(now) {
		var year=now.getFullYear();
		var month=now.getMonth()+1;
		var date=now.getDate();
		var hour=now.getHours();
		var minute=now.getMinutes();
		var second=now.getSeconds();
		if(second < 10)
		{
			second = '0' + second;
		}
		if(minute < 10)
		{
			minute = '0' + minute;
		}
		return year+"-"+month+"-"+date+" "+hour+":"+minute+":"+second;
	}
	if(value==null ||value==''){
		return '-';
	}
	var d = new Date(value);
	return formatDate(d);
};


var searchType = 1;
//导出excel
$.fn.page.exportExcel = function(that){
			var records = $("#jqGrid").jqGrid('getGridParam','records');
			if(records > 1000000){
				top.layer.alert("导出记录不能超过1000000条，请分批次导出!", {
					icon: 5,
					shadeClose: true
				});
				return false;
			}
			var self = $(that);
			var url = self.data("url");
			var exportTitle = self.data("title");
			var params = (url.indexOf('?')!=-1?'&':'?') + 'exportTitle=' + encodeURI(exportTitle) + getFieldsAndHearders();
			var keywords =  $("#keywords").val();
			if(searchType == 1){
				params += '&keywords='+encodeURI(keywords);
			}else{
				params += '&'+$('#advSearchForm').serialize();
			}
			self.attr("href",url + params);


			function getFieldsAndHearders(){
				var hearders = '', fields = '';
				var colModel = $("#jqGrid").jqGrid("getGridParam","colModel");
				$.each(colModel,function(index,obj){
					if(obj.name=='others'  || obj.name=='cz' || obj.name=='cz1' || obj.name=='cz2'){

					}else if(obj.colmenu && !obj.hidden){//自定义非隐藏列
						hearders += obj.label+",";
						fields  += obj.name+",";
					}
				});
				return '&hearders='+encodeURI(hearders)+"&fields="+encodeURI(fields);
			}

}

//日期显示的格式化
Date.prototype.format = function (format)
{
	var o = {
		"M+": this.getMonth() + 1, //month
		"d+": this.getDate(),    //day
		"h+": this.getHours(),   //hour
		"m+": this.getMinutes(), //minute
		"s+": this.getSeconds(), //second
		"q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
		"S": this.getMilliseconds() //millisecond
	}
	if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
	(this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o) if (new RegExp("(" + k + ")").test(format))
		format = format.replace(RegExp.$1,
	  RegExp.$1.length == 1 ? o[k] :
		("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

//时间戳转换
var getLocalTime = function(value,type) {
	if (value == null || value == '') {
		return '--';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	}
	else {
		dt = new Date(value);
		if (isNaN(dt)) {
			value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); //将那个长字符串的日期值转换成正常的JS日期格式
			dt = new Date();
			dt.setTime(value);
		}
	}
	 switch (type){
	 case 1:
		 return dt.format("yyyy年MM月dd日");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 2:
		 return dt.format("yyyy年MM月dd日 hh:mm:ss");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 3:
		 return dt.format("yyyy-MM-dd");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 case 4:
		 return dt.format("yyyy-MM-dd hh:mm:ss");   //这里用到一个javascript的Date类型的拓展方法
		 break;
	 }
}
//jqGrid日期+时间 转换Formatter
function datetimeFormatter  (value,row,index){
	 return getLocalTime(value,4);
 }

//jqGrid日期转换Formatter
function dateFormatter  (value,row,index){
	 return getLocalTime(value,3);
}

$(function(){
	if( $(".search-form form").length ){
		$(".search-form form").submit(function() {
			$(this).find(".search-span button").click();
			return false;
		});
	}
});

/*获取光标位置*/
function getCursorPos(obj)
{
    var CaretPos = 0;
    // IE Support
    if (document.selection) {
        obj.focus (); //获取光标位置函数
        var Sel = document.selection.createRange ();
        Sel.moveStart ('character', -obj.value.length);
        CaretPos = Sel.text.length;
    }
    // Firefox/Safari/Chrome/Opera support
    else if (obj.selectionStart || obj.selectionStart == '0')
        CaretPos = obj.selectionEnd;
    return (CaretPos);
}
/*
定位光标
*/
function setCursorPos(obj,pos)
{
    if(obj.setSelectionRange) { //Firefox/Safari/Chrome/Opera
        obj.focus(); //
        obj.setSelectionRange(pos,pos);
    } else if (obj.createTextRange) { // IE
        var range = obj.createTextRange();
        range.collapse(true);
        range.moveEnd('character', pos);
        range.moveStart('character', pos);
        range.select();
    }
}

function replaceAndSetPos(obj,pattern,text){
    var pos=getCursorPos(obj);//保存原始光标位置
    var temp=obj.value; //保存原始值
    obj.value=temp.replace(pattern,text);//替换掉非法值

    //截掉超过长度限制的字串（此方法要求已设定元素的maxlength属性值）
    var max_length = obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : "";
    if( obj.value.length > max_length){
        var str1 = obj.value.substring( 0,pos-1 );
        var str2 = obj.value.substring( pos,max_length+1 );
        obj.value = str1 + str2;
    }

    pos=pos-(temp.length-obj.value.length);//当前光标位置
    setCursorPos(obj,pos);//设置光标
}


var exportData;
var exporturl;

function getFieldsAndHearders(){
	var hearders = '', fields = '';
	var colModel = $("#jqGrid").jqGrid("getGridParam","colModel");
	$.each(colModel,function(index,obj){
		if(obj.name=='others'  || obj.name=='cz' || obj.name=='cz1' || obj.name=='cz2'){
		}else if(obj.colmenu && !obj.hidden){//自定义非隐藏列
			hearders += obj.label+",";
			fields  += obj.name+",";
		}
	});
	return '&hearders='+hearders+"&fields="+fields;
}

function ajaxProgress(){
	$.ajax({
	        type: "POST",
	        url:"/sys/export/progress.html",
	        data:{'exportUrl':exporturl},
	        async: false,
	        error: function(request) {
	          	 clearTimeout(exportData)
	        },
	        success: function(data) {
	        	exportData=setTimeout("ajaxProgress()",2000);
	           if(data.msg >= 100){
	           	 layer.closeAll('loading')
	           	 clearTimeout(exportData)
	           }
	        }
	    });
	}

function exportExcel(that){
	var records = $("#jqGrid").jqGrid('getGridParam','records');
	if(records > 1000000){
		top.layer.alert("导出记录不能超过1000000条，请分批次导出!", {
			icon: 5,
			shadeClose: true
		});
		return false;
	}
	var url = $(that).data("url");
	var exportTitle = $(that).data("title");
	var params = (url.indexOf('?')!=-1?'&':'?') + 'exportTitle=' + exportTitle + getFieldsAndHearders();
	var keywords =  $("#keywords").val();
	exporturl = url;
	if(searchType == 1){
		params += '&keywords='+encodeURI(keywords);
	}else{
		params += '&'+$('#advSearchForm').serialize();
	}
	var hrefval = url + params;
	window.location.href=hrefval;
	layer.load(3,{
		shade: 0.3,
		content:'<span class="exportData">请等待，正在导出中...</span>'
	});

	ajaxProgress();

}

  	(function($){
	$.fn.placeholder=function (options) {
        var defaults = {
            pColor: "#ccc",
            pActive: "#999",
            pFont: "14px",
            activeBorder: "#080",
            posL: 18,
            zIndex: "99"
        },
        opts = $.extend(defaults, options);
        return this.each(function () {
            if ("placeholder" in document.createElement("input")) return;
            $(this).parent().css("position", "relative");

            //不支持placeholder的浏览器
            var $this = $(this),
                msg = $this.attr("placeholder"),
                iH = $this.outerHeight(),
                iW = $this.outerWidth(),
                iX = $this.position().left,
                iY = $this.position().top,
                oInput = $("<label>", {
                "class": "placeholderCss",
                "text": msg,
                "css": {
                    "position": "absolute",
                    "left": iX + "px",
                    "top": iY + "px",
                    "width": iW - opts.posL + "px",
                    "padding-left": opts.posL + "px",
                    "height": iH + "px",
                    "line-height": iH + "px",
                    "color": opts.pColor,
                    "font-size": opts.pFont,
                    "z-index": opts.zIndex,
                    "cursor": "text"
                }
                }).insertBefore($this);
            //初始状态就有内容
            var value = $this.val();
            if (value.length > 0) {
            oInput.hide();
            };

            //
            $this.on("focus", function () {
                var value = $(this).val();
                if (value.length > 0) {
                    oInput.hide();
                }
                oInput.css("color", opts.pActive);
                //

                if(navigator.userAgent.indexOf("Trident/4.0") > 0){
                    var myEvent = "propertychange";
                }else{
                    var myEvent = "input";
                }

                $(this).on(myEvent, function () {
                    var value = $(this).val();
                    if (value.length == 0) {
                        oInput.show();
                    } else {
                        oInput.hide();
                    }
                });

            }).on("blur", function () {
                var value = $(this).val();
                if (value.length == 0) {
                    oInput.css("color", opts.pColor).show();
                }
            });
            //
            oInput.on("click", function () {
                $this.trigger("focus");
                $(this).css("color", opts.pActive)
            });
            //
            $this.filter(":focus").trigger("focus");
        });
    }
})(jQuery);

$(":input[placeholder]").each(function(){
	$(this).placeholder();
});
