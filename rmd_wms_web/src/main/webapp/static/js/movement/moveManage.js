var basepath = $("#basepath").val();

var newurl = basepath + '/movement/ListMovement?flag=2';
$(function() {
	// 出库制单
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
	$('#datetimeRangeOut').dateRangePicker(configObject).bind('datepicker-change', function (event, obj) {
		$("#moveOutTimeStart").val(obj.date1.format("yyyy-MM-dd hh:mm"));
		$("#moveOutTimeEnd").val(obj.date2.format("yyyy-MM-dd hh:mm"));
	});
	$('#datetimeRangeIn').dateRangePicker(configObject).bind('datepicker-change', function (event, obj) {
		$("#moveInTimeStart").val(obj.date1.format("yyyy-MM-dd hh:mm"));
		$("#moveInTimeEnd").val(obj.date2.format("yyyy-MM-dd hh:mm"));
	});
	var queryParams = {};
	$('#MoveListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}

	$('#move_list').datagrid({
		view : detailview,
		width : 'auto',
		height : 'auto',
		nowrap : true,// True 数据将在一行显示
		striped : true,// True 就把行条纹化
		collapsible : false,// True 可折叠
		url : url,
		remoteSort : false, // 定义是否从服务器给数据排序
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
			titel : 'id',
			hidden : true
		}, {
			field : 'goodsCode',
			title : '商品编码',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				return row.movementInfo.goodsCode;
			}
		}, {
			field : 'goodsBarCode',
			title : '条码',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				return row.movementInfo.goodsBarCode;
			}
		// }, {
		// 	field : 'inStockTime',
		// 	title : '入库时间',
		// 	width : 80,
		// 	align : 'center',
		// 	formatter : formatDatebox
		}, {
			field : 'validityTime',
			title : '有效期',
			width : 80,
			align : 'center',
			formatter : function(value, row, index) {
				return formatDateboxEx(row.movementInfo.validityTime);
			}

		}, {
			field : 'locationNoOut',
			title : '移出货位',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				return row.movementInfo.locationNoOut;
			}
		// }, {
		// 	field : 'locationNo',
		// 	title : '移入货位',
		// 	width : 60,
		// 	align : 'center',
		// 	formatter : function(value, row, index) {
		// 		return row.locationGoodsBindIn.locationNo;
		// 	}
		}, {
			field : 'moveAmount',
			title : '移出数量',
			width : 60,
			align : 'center'
			
		// }, {
		// 	field : 'groundingNum',
		// 	title : '移入数量',
		// 	width : 60,
		// 	align : 'center',
		// 	formatter : function(value, row, index) {
		// 		return row.locationGoodsBindIn.groundingNum;
		// 	}
		}, {
			field : 'outUserName',
			title : '移出人员',
			width : 100,
			align : 'center'
		}, {
			field : 'inUserName',
			title : '移入人员',
			width : 100,
			align : 'center'
		}, {
			field : 'moveOutTime',
			title : '移出时间',
			width : 120,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'moveInTime',
			title : '移入时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox

		}
		
		] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber : pageNo,
		pageSize : rows,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ],
		detailFormatter:function(index,row){
			return '<div style="padding:2px"><table class="ddv"></table></div>';
		},
		onExpandRow: function(index,row){
			var ddv = $(this).datagrid('getRowDetail',index).find('table.ddv');
			ddv.datagrid({
				url:basepath + '/movement/ListGoodsBindIn?ginfoId='+row.movementInfo.id,
				fitColumns:true,
				singleSelect:true,
				rownumbers:true,
				loadMsg:'',
				height:'auto',
				columns:[[
					{field:'id',title:'ID',width:100,hidden:true},
					{field:'ginfoId',title:'上架单详情id',width:100,hidden:true},
					{field:'locationId',title:'货位id',width:100,hidden:true},
					{field:'locationNo',title:'移入货位号',width:100},
					// {field:'groundingNo',title:'上架单号',width:100},
					{field:'groundingNum',title:'移入数量',width:100},
					{field:'goodsCode',title:'商品编码',width:100},
					{field:'goodsBarCode',title:'商品条形码',width:100},
					{field:'validityTime',title:'有效期',width:100,formatter : function(value, row, index) {
						return formatDateboxEx(value);
					}},
					// {field:'soldOut',title:'已售完',width:100},
					// {field:'sortNum',title:'拣货权重',width:100},
					// {field:'wareId',title:'仓库id',width:100,hidden:true},
					// {field:'wareName',title:'仓库名称',width:100},
					{field:'areaId',title:'库区id',width:100,hidden:true},
					{field:'areaName',title:'库区名称',width:100},
					{field:'createTime',title:'创建时间',width:100,formatter : function(value, row, index) {
						return formatDatebox(value);
					}}
					
				]],
				onResize:function(){
					$('#move_list').datagrid('fixDetailRowHeight',index);
				},
				onLoadSuccess:function(){
					setTimeout(function(){
						$('#move_list').datagrid('fixDetailRowHeight',index);
					},0);
				}
			});
			$('#move_list').datagrid('fixDetailRowHeight',index);
		}
	});

}


// 根据订单号详情
function getInstockBiLLInfo(a) {
	var l = (screen.availWidth - 900) / 2;
	var t = (screen.availHeight - 550) / 2;
	window.open(basepath + "/outstock/OutstockBiLLInfo?Id=" + a, "", "height=550, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

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
function formatDateboxEx(value) {
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


// 动态收索
function MoveListSearchBox() {

	init(newurl);
}

//重置按钮
function formReset() {
	location.reload();
}

//Excel导出
$("#moveMentExport").click(function(){
	//模拟表单提交
	var url = basepath + '/movement/moveMentExport';
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");//请求类型
	form.attr("action",url);//请求地址
	$("body").append(form);//将表单放置在web中

	var input1=$("<input>");
	input1.attr("type","hidden");
	input1.attr("name","columns");
	input1.attr("value",JSON.stringify(moveMentExcelHead));

	var input2=$("<input>");
	input2.attr("type","hidden");
	input2.attr("name","flag");
	input2.attr("value","2");
	form.append(input1);
	form.append(input2);
	form.submit();//表单提交
});