 var basepath = $("#basepath").val();

$(function() {

	// init(newurl);
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
	$('#warehouse_list').datagrid({
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
		url : './ListWarehouse',
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
			field : 'wareName',
			title : '仓库名称',
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
		},{
			field : 'code',
			title : '库区编号',
			width : 100,
			align : 'center',
		}, {
			field : 'address',
			title : '地址',
			width : 100,
			align : 'center',
		}, {
			field : 'contactName',
			title : '联系人',
			width : 100,
			align : 'center'
		}, {
			field : 'contactTel',
			title : '联系电话',
			width : 100,
			align : 'center'
		}, {
			field : 'contactEmail',
			title : '邮箱',
			width : 100,
			align : 'center',
		} ] ],
//	    onLoadSuccess: function (data){
//			if( 0 == data.total ){
//				$.messager.alert('信息提示','无数据!','error');
//			}
//		},
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber : pageNo,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ]
		
	});

});
//状态修改
function warehouseStatusUpdate(status) {
	var rows = $('#warehouse_list').datagrid('getSelections');
	if (rows.length < 1) {
		$.messager.alert("提示", "请选择数据！", "info");
		return;
	}
	if (status == 0) {
		$.messager.confirm("确认消息", "是否禁用此仓库?", function(r) {
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
						$("#warehouse_list").datagrid('reload');// 重新加载table
					}
					if(data =="updateNo"){
						$.messager.alert("提示", "此仓库有关联商品,不能被禁用!", "info");
						return;
					}
					if(data =="false"){
						$.messager.alert("提示", "操作成功", "info");
					}
				});
			}
		});
	} else {
		$.messager.confirm("确认消息", "是否启用此仓库?", function(r) {
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
						$("#warehouse_list").datagrid('reload');// 重新加载table
					}
					if(data =="false"){
						$.messager.alert("提示", "操作失败", "info");
					}
				});
			}
		});
	}
}

// 添加仓库
function addWarehouseBtn(){
	//配送范围编码
    var provinceTemp = "";
    var provinceName="";
    $('input:checkbox[name=province]:checked').each(function(i){
     if(0==i){
    	 provinceTemp = $(this).val();
    	 provinceName =$("#prv_"+provinceTemp).html();
     }else{
    	 provinceTemp += (","+$(this).val());
    	 provinceName +=","+$("#prv_"+$(this).val()).html();
     }
    });
	var info = {
			code : $.trim($("#code").val()),
			wareName : $.trim($("#wareName").val()),
			address : $.trim($("#address").val()),
			status : $.trim($("#status").val()),
			contactName : $.trim($("#contactName").val()),
			contactTel : $.trim($("#contactTel").val()),
			contactEmail : $.trim($("#contactEmail").val()),
			areaCode:provinceTemp,
			areaName:provinceName
		};
		if(info.code == ''){
			$.messager.alert('提醒', "请填写仓库编码");
			return;
		}
		if(info.wareName == ''){
			$.messager.alert('提醒', "请填写仓库名称");
			return;
		}
		$.ajax({
			type : "POST",
			url : './AddWarehouse',
			data : info,
			success : function(data) {
				if (data=="true") {
					$.messager.alert("提示", "操作成功", "info");
					$("#code").val('');
					$("#wareName").val('');
					$("#address").val('');
					$("#status").val('');
					$("#contactName").val('');
					$("#contactTel").val('');
					$("#contactEmail").val('');
					$("#warehouse_list").datagrid('reload');//重新加载table 
				}
				if(data =='warehouseNameFalse'){
					$.messager.alert("提示", "此仓库名称已存在", "info");
					return;
				}
				if(data =='warehouseCodeFalse'){
					$.messager.alert("提示", "此仓库编号已存在", "info");
					return;
				}
				if(data =="false"){
	        		$.messager.alert("提示", "操作失败", "info");
	        		return;
	        	}
			}
		});
	
}
function editWarehouseBtn(){
	//配送范围编码
    var provinceTemp = "";
    var provinceName="";
    $('input:checkbox[name=province]:checked').each(function(i){
     if(0==i){
    	 provinceTemp = $(this).val();
    	 provinceName =$("#prv_"+provinceTemp).html();
     }else{
    	 provinceTemp += (","+$(this).val());
    	 provinceName +=","+$("#prv_"+$(this).val()).html();
     }
    });
	var info = {
		id : $.trim($("#id").val()),
		code : $.trim($("#code").val()),
		wareName : $.trim($("#wareName").val()),
		address : $.trim($("#address").val()),
		status : $.trim($("#status").val()),
		contactName : $.trim($("#contactName").val()),
		contactTel : $.trim($("#contactTel").val()),
		contactEmail : $.trim($("#contactEmail").val()),
		areaCode:provinceTemp,
		areaName:provinceName
	};
	if(info.code == ''){
		$.messager.alert('提醒', "请填写仓库编码");
		return;
	}
	if(info.wareName == ''){
		$.messager.alert('提醒', "请填写仓库名称");
		return;
	}
	$.ajax({
		type : "POST",
		url : './editWarehouse',
		data : info,
		success : function(data) {
			if (data=="true") {
				$.messager.alert("提示", "操作成功", "info");
				$('#edit').dialog('close');//关闭弹出窗口
				$("#warehouse_list").datagrid('reload');//重新加载table 
			}
			if(data=="warehouseNameFalse"){
				$.messager.alert("提示", "此仓库名称已存在!", "info");
				return;
			}
			if(data == "warehouseCodeFalse"){
				$.messager.alert("提示", "此仓库编号已存在!", "info");
				return;
			}
			if(data =="false"){
        		$.messager.alert("提示", "操作失败", "info");
        		return;
        	}
		}
	});
}
//分配员工
/*function allocationBtn(){
	var id = "";
  	var rows = $('#warehouse_list').datagrid('getSelections');
  	if(rows == ""){
  		$.messager.alert("提示", "请选择一条数据 ！", "info");   
  		return;
  	}
  	if(rows.length > 1){
  		$.messager.alert("提示", "只能选择一条数据 ！", "info");   
  		return;
  	}
  	var arr = rows[0].id;
  	window.location.href="./allocationWarehouse?id="+arr;
}
*/
	

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

// 取消按钮
function cancelWarehouseBtn(row){
	if(row ==0){
		$("#add").window("close");
	}else{
		$("#edit").window("close");
	}
	
}

function sort() {
	location.reload();
}
// 动态搜索方法
function purchaseSearchBox() {

	init(newurl);

}