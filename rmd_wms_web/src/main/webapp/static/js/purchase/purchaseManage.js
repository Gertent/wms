var basepath = $("#basepath").val();

var newurl=basepath + '/purchase/ListPurchaseBiLL';
$(function() {

	init(newurl);

});
// 初始化方法
function init(url) {
	// 日期配置对象
	var configObject = {
		language: 'cn',
		startOfWeek: 'monday',
		separator: ' 至 ',
		format: 'YYYY-MM-DD HH:mm',
		autoClose: false,
		time: {
			enabled: true
		}
	};
	$('#datetimeRange').dateRangePicker(configObject).bind('datepicker-change', function (event, obj) {
		$("#inDbData").val(obj.date1.format("yyyy-MM-dd hh:mm"));
		$("#inDbDataEnd").val(obj.date2.format("yyyy-MM-dd hh:mm"));
	});
	var queryParams = {};
	$('#purchaseListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#purchasebill_list').datagrid({
		width : 'auto',
		height : 'auto',
		nowrap : true,// True 数据将在一行显示
		striped : true,// True 就把行条纹化
		collapsible : false,// True 可折叠
		url : url,
		remoteSort : true, // 定义是否从服务器给数据排序
		singleSelect : false, // True 只能选择一行
		fit : true,
		toolbar : '#box',
		scrollbarSize : 0,
		fitColumns : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, ] ],
		columns : [ [ {
			field : 'id',
			titel : '采购单id',
			hidden : true
		}, {
			field : 'purchaseNo',
			title : '采购单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getPurchaseBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
			field : 'inDbData',
			title : '计划入库日期',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'inType',
			title : '入库类型',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "采购";
				} else if (val == "0") {
					return "售后";
				}
			}
		}, {
			field : 'supplierName',
			title : '供应商',
			width : 100,
			align : 'center'
		}, {
			field : 'wareName',
			title : '收货单位',
			width : 100,
			align : 'center'
		}, {
			field : 'ouserName',
			title : '采购员',
			width : 100,
			align : 'center'
		}, {
			field : 'status',
			title : '入库状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "等待";
				} else if (val == "2") {
					return "部分";
				} else if (val == "3") {
					return "完成";
				}
			}
		}
		] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber:pageNo,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

// 根据订单号获取意向单详情
function getPurchaseBiLLInfo(a) {
	var l = (screen.availWidth - 900) / 2;
	var t = (screen.availHeight - 450) / 2;
	window.open(basepath + "/purchase/PurchaseBiLLInfo?Id=" + a, "入库管理-采购收货-采购单详情", "height=450, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

}
// 新增采购单下商品
function addPurchaseBiLLInfo(a) {
	if (a > 0) {
		$("#cgNo").val(a);
		$("#addPurchaseBiLLInfo").window({
			width : 900,
			height : 500,
			modal : true,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			title : "入库管理-采购收货-新增采购单商品"
		});
	}

}

// 时间格式化
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};
function formatDatebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		dt = new Date(value);
	}

	return dt.format("yyyy-MM-dd"); // 扩展的Date的format方法(上述插件实现)
};
// 新增采购单
$("#purchaseAdd").click(function() {
	$("#addPurchaseBill").window({
		width : 600,
		height : 200,
		modal : true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		title : "出库管理-采购收货-新增采购单"
	});

});
// 添加采购单商品
$("#addPurchaseBtn").click(function() {
	var info = {
		cgNo : $.trim($("#cgNo").val()),
		goodsCode : $.trim($("#goodsCode").val()),
		goodsBarCode : $.trim($("#goodsBarCode").val()),
		goodsName : $.trim($("#goodsName").val()),
		spec : $.trim($("#spec").val()),
		packageNum : $.trim($("#packageNum").val()),
		unit : $.trim($("#unit").val()),
		purchaseNum : $.trim($("#purchaseNum").val()),
		purchasePrice : $.trim($("#purchasePrice").val()),
		validityTime : $.trim($("#validityTime").datetimebox('getText')),

	};
	if (info.goodsCode == '') {
		$.messager.alert('提醒', "请填写商品名称");
		return;
	} else if (info.goodsCode == '') {
		$.messager.alert('提醒', "请填写商品编码");
		return;
	} else if (info.goodsName == '') {
		$.messager.alert('提醒', "请填写商品名称");
		return;
	} else if (info.spec == '') {
		$.messager.alert('提醒', "请填写商品规格");
		return;
	} else if (info.packageNum == '') {
		$.messager.alert('提醒', "请填写商品包装数量");
		return;
	} else if (info.unit == '') {
		$.messager.alert('提醒', "请填写商品单位");
		return;
	} else if (info.purchaseNum == '') {
		$.messager.alert('提醒', "请填写商品采购数量");
		return;
	} else if (info.purchasePrice == '') {
		$.messager.alert('提醒', "请填写商品采购单价");
		return;
	} else if (info.validityTime == '') {
		$.messager.alert('提醒', "请填写商品有效期");
		return;
	} else {
		$.ajax({
			type : "POST",
			url : basepath + '/purchase/AddPurchaseInfo',
			data : info,
			success : function(result) {
				if (result) {
					$.messager.alert('提醒', '商品添加成功!');
					$("#goodsCode").val('');
					$("#goodsName").val('');
					$("#goodsBarCode").val('');
					$("#spec").val('');
					$("#packageNum").val('');
					$("#unit").val('');
					$("#purchaseNum").val('');
					$("#purchasePrice").val('');
					$("#validityTime").val('');
				} else {
					$.messager.alert('提醒', '数据传输失败!');
				}
			}
		});

	}
});
// 确定按钮
$("#commitedit").click(function() {
	
	var info = {
		supplierName : $.trim($("#asupplierName").combobox('getText')),
		inDbData : $.trim($("#ainDbData").datetimebox('getText')),

	};
	if (info.supplierName == '') {
		$.messager.alert('提醒', "请填写供应商");
		return;
	} else {
		$.ajax({
			type : "POST",
			url : basepath + '/purchase/AddPurchase',
			data : info,
			success : function(result) {
				if (result) {
					sort();
				} else {
					$.messager.alert('提醒', '数据传输失败!');
				}
			}
		});

	}
});

//添加采购单商品窗口关闭按钮
$("#cancelPurchaseBtn").click(function() {
	$("#addPurchaseBiLLInfo").window("close");
});
// 取消按钮
$("#cancel").click(function() {
	$("#editLogistics").window("close");
});
function sort() {
	location.reload();
}
//动态搜索方法
function purchaseSearchBox(){

	init(newurl);

}

//重置按钮
function formReset() {
	location.reload();
}