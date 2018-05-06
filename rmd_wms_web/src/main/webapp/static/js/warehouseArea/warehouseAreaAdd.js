(function($) {
	$.fn.serializeJson = function() {
		var serializeObj = new Object();
		var array = this.serializeArray();
		$(array).each(function() {
			if (serializeObj[this.name]) {
				if ($.isArray(serializeObj[this.name])) {
					serializeObj[this.name].push(this.value);
				} else {
					serializeObj[this.name] = [serializeObj[this.name], this.value];
				}
			} else {
				serializeObj[this.name] = $.trim(this.value);
			}
		});
		return serializeObj;
	};
})(jQuery);
function addWarehouseAreaBtn(){
	var isValidatePass = $('#add').form('validate');
	if (isValidatePass){
		var advInfo = $('#add').serializeJson();
		if(advInfo.code == ''){
			$.messager.alert('提醒', "请填写库区编码");
			return;
		}
		if(advInfo.areaName == ''){
			$.messager.alert('提醒', "请填写库区名称");
			return;
		}
		if(advInfo.wareId == 0){
			$.messager.alert('提醒', "请选择所属仓库");
			return;
		}
		if(advInfo.type == ''){
			$.messager.alert('提醒', "请选择库区性质");
			return;
		}
		$.ajax({
			type : "POST",
			url : './addWarehouseArea',
			data : advInfo,
			success : function(data) {
				if (data == "true") {
					$.messager.alert("提示", "操作成功", "info");
					$("#code").val('');
					$("#areaName").val('');
					$("#status").val('');
					$("#wareId").val('');
					$("#type").val('');
					$("#warehouseArea_list").datagrid('reload');//重新加载table 
				}
				if(data == "warehouseAreaNameFalse"){
					$.messager.alert("提示", "此库区名称已存在!", "info");
					return;
				}
				if(data == "warehouseAreaCodeFalse"){
	        		$.messager.alert("提示", "此库区编号已存在!", "info");
	        		return;
	        	}
				if(data == "false"){
					$.messager.alert("提示", "操作失败", "info");
					return;
				}
			}
		});
	}
}
//取消按钮
function cancelWarehouseAreaBtn(){
//	alert("a");
	$("#addWareaHouseArea").window("close");
}