var basepath = $("#basepath").val();

var newurl=basepath + '/instock/ListInStockBiLL';
$(function() {

	init(newurl);

});
//初始化方法
function init(url) {
	// 日期配置对象
	var singleConfig={
		language: 'cn',
		singleMonth: true,
		showShortcuts: false,
		singleDate : true,
		showTopbar: false
	}
	$('#timeRange').dateRangePicker(singleConfig).bind('datepicker-change', function (event, obj) {
		$("#instockTime").val(obj.date1.format("yyyy-MM-dd"));
	});
	var queryParams = {};
	$('#instockListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#instock_list').datagrid({
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
			titel : '入库单id',
			hidden : true
		}, {
			field : 'inStockNo',
			title : '入库单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
			field : 'purchaseNo',
			title : '采购单号',
			width : 100,
			align : 'center'
		}, {
			field : 'createTime',
			title : '入库日期',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'wareName',
			title : '收货单位',
			width : 100,
			align : 'center'
		}, {
			field : 'supplierName',
			title : '供货商',
			width : 100,
			align : 'center'
		}, {
			field : 'doPrint',
			title : '入库单打印',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "已打印";
				} else{
					return "未打印";
				} 
			}
		},
		{
			field : 'inStockTime',
			title : '完成入库时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		},
		{
			field : 'ouserName',
			title : '入库员',
			width : 100,
			align : 'center'
		},{
				field : 'status',
				title : '状态',
				width : 100,
				align : 'center',
				formatter : function(val, row, index) {
					if (val == "1") {
						return "良品";
					} else{
						return "残次品";
					}
				}
			}] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber: pageNo,
		pageSize: rows,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

//根据订单号获取意向单详情
function getInstockBiLLInfo(a) {
	var l=(screen.availWidth-900)/2;
	var t=(screen.availHeight-550)/2;       
	window.open(basepath+"/instock/InstockBiLLInfo?Id="+a, "入库管理-入库列表-入库单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}

//时间格式化
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

	return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
};


function instockSearchBox(){
	
	init(newurl);
	
}

//入库单打印接口
$("#printInstockBill").click(function() {
	var rows = $('#instock_list').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert('提醒', '只能选择一条数据 !');
		return;
	} else if (rows.length == 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}
	var purchaseNo=rows[0].purchaseNo;
	if(purchaseNo==''||purchaseNo.indexOf('CG')!=0){
		$.messager.alert('提醒', '请打印采购入库单 !');
		return;
	}
	var rowIndex=$('#instock_list').datagrid('getRowIndex',rows[0]);
	$("#instock_list").datagrid("getRows")[rowIndex]["doPrint"]='1';
	$('#instock_list').datagrid('refreshRow', rowIndex);
	if (rows) {
		var id=rows[0].id;
		var instockNo=rows[0].inStockNo;
	    if(id>0&&instockNo!=''){
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;       
			window.open(basepath+"/instock/InstockBillPrint?Id="+id+"&instockNo="+instockNo, "入库管理-入库列表-入库单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			updatePrintStatus(id,1);
	     }
		
	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});
//Excel导出
$("#excelInstockBill").click(function(){
	//模拟表单提交
	var url = basepath + '/instock/inStockExport';
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");//请求类型
	form.attr("action",url);//请求地址
	$("body").append(form);//将表单放置在web中

	var input1=$("<input>");
	input1.attr("type","hidden");
	input1.attr("name","columns");
	input1.attr("value",JSON.stringify(inStockExcelHead));
	form.append(input1);

	form.submit();//表单提交
});

//重置按钮
function formReset() {
	// $('#instockListSearch').find(':input').each(function() {
	// 	$(this).val('');
	// });
	location.reload();
}

//更新打印状态
function updatePrintStatus(ids,status){
	$.ajax({
		url : basepath + '/instock/updatePrintStatus',
		type : 'post',
		data : {'ids':ids,'status':status},
		success : function(result) {
			if (result.status == "200") {
				//location.reload();
			} else {

			}

		}
	});
}