 var basepath = $("#basepath").val();

$(function() {

	// init(newurl);
	var queryParams = {};
	/*$('#purchaseListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});*/
	queryParams["id"]=$('#wareId').val();
	/*var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}*/
	$('#allocation_list').datagrid({
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
		url : './listAllocationWarehouse',
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
			field : 'realname',
			title : '姓名',
			width : 100,
			align : 'center'
		}, {
			field : 'loginname',
			title : '账号',
			width : 100,
			align : 'center'
		}, {
			field : 'jobNum',
			title : '工号',
			width : 100,
			align : 'center'
		}, {
			field : 'deptNames',
			title : '部门',
			width : 100,
			align : 'center'
		}, {
			field : 'position',
			title : '职级',
			width : 100,
			align : 'center'
		}, {
			field : 'telCode3',
			title : '组别',
			width : 100,
			align : 'center'
		}, {
			field : 'email',
			title : '电子邮箱',
			width : 100,
			align : 'center'
		}, {
			field : 'mobile',
			title : '联系电话',
			width : 100,
			align : 'center'
		}, {
			field : 'isWareUser',
			title : '是否仓库人员',
			width : 100,
			hidden:true,
			align : 'center'
		}] ],
	    onLoadSuccess: function (data){
			if( 0 != data.total ){
				var rows=data.rows;
				var len=rows.length;
				for(var i=0;i<len;i++){
					if(rows[i].isWareUser==1){
						$('#allocation_list').datagrid('checkRow',i);
					}
				}
			}
		},
		queryParams : queryParams
		/*pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber : pageNo,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ],*/
	});

});
//分配员工
function allocationConfirm(){
	var rows = $('#allocation_list').datagrid('getSelections');
	if(rows.length<=0){
		$.messager.alert('提醒','请选择员工!');
		return;
	}
	var wareId = $("#wareId").val();
	var strIds = "";// id
	for (var i = 0; i < rows.length; i++) {
		strIds += rows[i].id + ",";
	}
	strIds = strIds.substr(0, strIds.length - 1);
	$.post("./allocationWarehouseInfo", {
		ids : strIds,
		wareId : wareId
	}, function(data) {
		$.messager.alert("提示", data.desc, "info",function(){
			$('#allot').window('close');
		});
		$("#location_list").datagrid('reload');// 重新加载table
	});
}
function allocationCancel(){
	$('#allot').window('close');
}
// 时间格式化
/*Date.prototype.format = function(format) {
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
};*/
function sort() {
	location.reload();
}
function cancelAllocationBtn(){
	alert("a");
	$(".allocationNoticBox").window("close");
}