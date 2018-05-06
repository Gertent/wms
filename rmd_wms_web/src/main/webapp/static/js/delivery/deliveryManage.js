var basepath = $("#basepath").val();

var newurl=basepath + '/delivery/ListDeliveryBiLL';
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
		$("#starTime").val(obj.date1.format("yyyy-MM-dd hh:mm"));
		$("#endTime").val(obj.date2.format("yyyy-MM-dd hh:mm"));
	});
	var queryParams = {};
	$('#deliveryListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	if ($("#doPrint").is(":checked")) {
		queryParams['doPrint']="0";
	}
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#delivery_list').datagrid({
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
			titel : '发货单id',
			hidden : true
		}, {
			field : 'deliveryNo',
			title : '发货单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
			field : 'orderSum',
			title : '订单数',
			width : 120,
			align : 'center'
		}, {
			field : 'dodeliveryPrint',
			title : '交接单打印',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "未打印";
				} else if (val == "1") {
					return "<span style='color:green;'>√</span>";
				}else{
					return "未打印";
				}
			}
		},{
			field : 'logisComName',
			title : '承运商',
			width : 100,
			align : 'center'
		}, {
			field : 'deliveryEndTime',
			title : '完成时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'deliveryUserName',
			title : '发货员',
			width : 100,
			align : 'center'
		}  ] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber:pageNo,
		queryParams:queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

// 根据订单号获取意向单详情
function getInstockBiLLInfo(a) {
	var l = (screen.availWidth - 900) / 2;
	var t = (screen.availHeight - 550) / 2;
	window.open(basepath + "/delivery/DeliveryInfo?Id=" + a, "", "height=550, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

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

	return dt.format("yyyy-MM-dd hh:mm:ss"); // 扩展的Date的format方法(上述插件实现)
};

//搜索事件
function deliveryListSearchBox(){
	init(newurl);
}


//打印发运交接单
$("#printdeliveryBill").click(function () {
	var selRows = $('#delivery_list').datagrid('getChecked');
	if (selRows && selRows.length > 0) {
		var logisId = selRows[0].logisComId;
		var ids = '';
		// 封装id集合字符串
		for (var i = 0; i < selRows.length; i++) {
			if (logisId != selRows[i].logisComId) {
				$.messager.alert('提醒', '请选择同一承运商 !');
				return;
			}
			ids += selRows[i].id;
			if (i != (selRows.length - 1)) {
				ids += ",";
			}
		}
		if (ids.length > 0) {
			var l = (screen.availWidth - 900) / 2;
			var t = (screen.availHeight - 550) / 2;
			if (updatePrintStatus(ids)) {
				// 打开打印框
				window.open(basepath + "/delivery/deliveryBillPrint?Id=" + ids, "发运交接单打印", "height=550, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
				// 设置所有以打印的数据状态
				for (var i = 0; i < selRows.length; i++) {
					var rowIndex = $('#delivery_list').datagrid('getRowIndex', selRows[i]);
					$("#delivery_list").datagrid("getRows")[rowIndex]["dodeliveryPrint"] = '1';
					$('#delivery_list').datagrid('refreshRow', rowIndex);
				}
			}
		}
	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}
});

//重置按钮
function formReset() {
	location.reload();
}

//更新打印状态
function updatePrintStatus(ids){
	var flag = false;
	$.ajax({
		url : basepath + '/delivery/updatePrintStatus',
		type : 'post',
		async : false,
		data : {'ids':ids},
		success : function(result) {
			if (result.status == "200") {
				$.messager.alert('提醒', '生成运费单成功');
				flag = true;
			} else {
				$.messager.alert('提醒', result.message);
			}
		}
	});
	return flag;
}