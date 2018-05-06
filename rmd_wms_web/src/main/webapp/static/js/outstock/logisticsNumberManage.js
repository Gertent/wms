var basepath = $("#basepath").val();

var newurl=basepath + '/outstock/ListOutStockBiLL?flag=3';
$(function() {
	//录入运单号
	init(newurl);

});
// 初始化方法
function init(url) {
	var queryParams = {};
	$('#logisticsListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	if ($("#dowaybillPrint").is(":checked")) {
		queryParams['dowaybillPrint']="0";
	}
	if ($("#noLogisticsNo").is(":checked")) {
		queryParams['noLogisticsNo']="0";
	}
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#logistics_list').datagrid({
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
			titel : '出库单id',
			hidden : true
		}, {
			field : 'orderNo',
			title : '订单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
			field : 'orderdate',
			title : '订单日期',
			width : 120,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'wareName',
			title : '发货仓库',
			width : 100,
			align : 'center'
		}, {
			field : 'parcelWeight',
			title : '包装重量(KG)',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				return parseFloat(value).toFixed(2);
			}
		}, {
			field : 'logisComId',
			title : '承运商ID',
			width : 100,
			hidden:true,
			align : 'center'
		}, {
			field : 'logisComName',
			title : '承运商',
			width : 100,
			align : 'center'
		}, {
			field : 'dowaybillPrint',
			title : '运单打印',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "未打印";
				} else if (val == "1") {
					return "<span style='color:green;'>√</span>";
				}
			}
		}, {
			field : 'freeze',
			title : '冻结',
			hidden:true,
			align : 'center'
		}
		// , {
		// 	field : 'logisticsNo',
		// 	title : '运单号',
		// 	width : 100,
		// 	align : 'center'
		// }, {
		// 	field : 'inLogisticsTime',
		// 	title : '录入日期',
		// 	width : 120,
		// 	align : 'center',
		// 	formatter : formatDatebox
		// }
		] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber:pageNo,
		queryParams:queryParams,
		pageList : [ 10, 20, 30, 40 ],
		onLoadSuccess: function (data){
			if( 0 != data.total ){
				var rows=data.rows;
				var len=rows.length;
				for(var i=0;i<len;i++){
					if(rows[i].orderNo==$('#orderNo').val()){
						$('#logistics_list').datagrid('checkRow',i);
					}
				}
			}
		}
	});

}

// 根据订单号获取意向单详情
function getInstockBiLLInfo(a) {
	var l = (screen.availWidth - 900) / 2;
	var t = (screen.availHeight - 550) / 2;
	window.open(basepath + "/outstock/OutstockBiLLInfo?Id=" + a, "入库管理-入库列表-入库单打印", "height=550, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

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

//录入运单号数据加载窗口
$("#logisticsNoAdd").click(function() {
	var rows = $('#logistics_list').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert('提醒', '只能选择一条数据 !');
		return;
	} else if (rows.length == 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}
	var freeze=rows[0].freeze;//冻结， 0 未冻结 1 冻结
	if(freeze==1){
		$.messager.alert('提醒', '该订单已冻结!');
		return;
	}
	if (rows[0].logisComId != null && rows[0].logisComName) {
		$("#LorderNo").html(rows[0].orderNo);//订单号赋值
		$("#logisComName").html(rows[0].logisComName);//承运商复制
		$("#Lid").val(rows[0].id);//承运商复制
		$("#editLogistics").window({
			width : 500,
			height : 280,
			modal : true,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			title : "出库管理-运单打印-录入运单号",
			onBeforeClose : function() {
				sort();
			}
		});
	} else {
		$.messager.alert('提醒', '承运商为空，请通知相关人员修改承运商！');
	}

});

//确定按钮
$("#commitedit").click(function() {
	var info = {
		orderNo : $.trim($("#LorderNo").html()),
		logisticsNo : $.trim($("#logisticsNo").val()),
		id : $.trim($("#Lid").val()),

	};
	if (info.logisticsNo == '') {
		$.messager.alert('提醒', "请填写运单号");
		return;
	} else {
		
		$.ajax({
			type : "POST",
			url : basepath + '/outstock/CheckLogistics',
			data : info,
			dataType : "json",
			success : function(result) {
				if (result) {
					$.messager.alert('提醒', '运单号已存在，请重新录入!');
				} else {
					$.ajax({
						type : "POST",
						url : basepath + '/outstock/EditLogistics',
						data : info,
						dataType : "json",
						success : function(result) {
							if (result) {
								sort();
							} else {
								$.messager.alert('提醒', '数据传输失败!');
							}
						}
					});
				}
			}
		});
		
		/**/
	}
});
//取消按钮
$("#cancel").click(function() {
	$("#editLogistics").window("close");
});
//排序
function sort() {
	location.reload();
}

//搜索事件
function logisticsListSearchBox (){
	init(newurl);
}

//重置按钮
function formReset() {
	$('#logisticsListSearch').find(':input').each(function() {
		$(this).val('');
	});
}

