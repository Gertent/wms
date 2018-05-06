var basepath = $("#basepath").val();

var newurl=basepath + '/grounding/ListGroundingBiLL';
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
		$("#createTime").val(obj.date1.format("yyyy-MM-dd"));
	});

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
	$('#groundListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	if($('#twostatus').is(':checked')){
		queryParams['twostatus']=1;
	}else{
		queryParams['twostatus']=0;
	}
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#grounding_list').datagrid({
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
			titel : '上架单id',
			hidden : true
		}, {
			field : 'inStockNo',
			title : '入库单号',
			width : 100,
			align : 'center',
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getGroundingBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
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
			field : 'status',
			title : '上架状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {//1:等待,2:上架中,3已完成
				if (val == "1") {
					return "等待";
				} else if (val == "2") {
					return "上架中";
				} else if (val == "3") {
					return "已完成";
				} 
			}
		},{
			field : 'startTime',
			title : '开始时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		},
		{
			field : 'endTime',
			title : '完成时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		},
		{
			field : 'ouserName',
			title : '上架员',
			width : 100,
			align : 'center'
		},] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber: pageNo,
		pageSize: rows,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

//根据订单号获取意向单详情
function getGroundingBiLLInfo(a) {
	var l=(screen.availWidth-900)/2;
	var t=(screen.availHeight-550)/2;       
	window.open(basepath+"/grounding/GroundingBiLLInfo?Id="+a, "入库管理-收货计划-上架详情", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
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

function groundListSearchBox(){
	init(newurl);
}
//Excel导出
$("#excelGroupingBill").click(function(){
	//模拟表单提交
	var url = basepath + '/grounding/groundingBillExport';
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");//请求类型
	form.attr("action",url);//请求地址
	$("body").append(form);//将表单放置在web中

	var input1=$("<input>");
	input1.attr("type","hidden");
	input1.attr("name","columns");
	input1.attr("value",JSON.stringify(groundingBillExcelHead));
	form.append(input1);

	form.submit();//表单提交
});
//重置按钮
function formReset() {
	// $('#groundListSearch').find(':input').each(function() {
	// 	$(this).val('');
	// });
	location.reload();
}