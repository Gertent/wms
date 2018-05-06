var basepath = $("#basepath").val();
var newurl='./listWarehouse';
$(function() {

	init(newurl);

});
function init(url){
	// init(newurl);
	var queryParams = {};
	$('#warehouseAreaListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#warehouseArea_list').datagrid({
		// title : '部门列表',
		width: 'auto',
		height: 'auto',
		border: true,
		nowrap: true,
		// True 数据将在一行显示
		striped: true,
		// True 就把行条纹化
		collapsible: true,
		// True 可折叠
		url : './listWarehouse',
		remoteSort: true, // 定义是否从服务器给数据排序
		singleSelect: false, // True 只能选择一行
		fit: false, 
		toolbar: '#box',
		fitColumns: true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, ] ],
		columns : [ [ {
			field : 'id',
			titel : 'id',
			hidden : true
		}, {
			field : 'areaName',
			title : '库区名称',
			width : 100,
			align : 'center'
		}, {
			field : 'status',
			title : '状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "启用";
				} else if (val == "0") {
					return "禁用";
				}
			}
		}, {
			field : 'code',
			title : '编码',
			width : 100,
			align : 'center',
		}, {
			field : 'type',
			title : '库区性质',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "可卖";
				} else if (val == "0") {
					return "不可卖";
				}
			}
		},{
			field : 'wareName',
			title : '所属仓库',
			width : 100,
			align : 'center',
		}] ],
		onLoadSuccess: function (data){
			if( 0 == data.total ){
				//$.messager.alert('信息提示','无数据!','error');
			}
		},
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber : pageNo,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});
}

//状态修改
function warehouseAreaStatusUpdate(status) {
	var rows = $('#warehouseArea_list').datagrid('getSelections');
	if (rows.length < 1) {
		$.messager.alert("提示", "请选择数据！", "info");
		return;
	}
	if (status == 0) {
		$.messager.confirm("确认消息", "是否禁用此库区?", function(r) {
			if (r) {
				var strIds = "";// id
				for (var i = 0; i < rows.length; i++) {
					strIds += rows[i].id + ",";
				}
				strIds = strIds.substr(0, strIds.length - 1);
				$.post("./updateStatusById", {
					ids : strIds,
					status : status
				}, function(data) {
					if(data =="true"){
						$.messager.alert("提示", "操作成功", "info");
						$("#warehouseArea_list").datagrid('reload');// 重新加载table
					}
					if(data =="updateNo"){
						$.messager.alert("提示", "此库区已有商品，不能被禁用!", "info");
						return ;
					}
					if(data =="false"){
						$.messager.alert("提示", "操作失败!", "info");
						return ;
					}
					
				});
			}
		});
	} else {
		$.messager.confirm("确认消息", "是否启用此库区?", function(r) {
			if (r) {
				var strIds = "";// id
				for (var i = 0; i < rows.length; i++) {
					strIds += rows[i].id + ",";
				}
				strIds = strIds.substr(0, strIds.length - 1);
				$.post("./updateStatusById", {
					ids : strIds,
					status : status
				}, function(data) {
					if(data == "true"){
						$.messager.alert("提示", "操作成功", "info");
						$("#warehouseArea_list").datagrid('reload');// 重新加载table
					}
					if(data =="false"){
						$.messager.alert("提示", "操作失败!", "info");
						return ;
					}
					
				});
			}
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
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
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
function warehouseAreaReset() {
	location.reload();
}
// 动态搜索方法
function warehouseAreaSearchBox() {
	init(newurl);
}