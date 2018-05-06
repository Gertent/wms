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
function editWarehouseAreaBtn(){
	var isValidatePass = $('#edit').form('validate');
	if (isValidatePass){
		var advInfo = $('#edit').serializeJson();
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
			url : './editWarehouseArea',
			data : advInfo,
			success : function(data) {
				if (data == "true") {
					$.messager.alert("提示", "操作成功", "info");
					$('.editNoticBox').dialog('close');//关闭弹出窗口
					$("#warehouseArea_list").datagrid('reload');//重新加载table 
				}
				if(data == "warehouseAreaCodeFalse"){
					$.messager.alert("提示", "此库区编码已存在", "info");
					return;
				}
				if(data == "warehouseAreaNameFalse"){
					$.messager.alert("提示", "此库区名称已存在", "info");
					return;
				}
				if(data == "warehouseAreaType"){
					$.messager.alert("提示", "此库区下还有商品， 不能修改为不可买状态!", "info");
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
	$("#editWarehouseArea").window("close");
}
