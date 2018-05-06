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
function editlocationBtn(){
	var isValidatePass = $('#edit').form('validate');
	if (isValidatePass){
		var advInfo = $('#edit').serializeJson();
		if(advInfo.locationNo == null ||advInfo.locationNo ==undefined){
			$.messager.alert('提醒', "请填写货位号!","info");
			return;
		}
		if(advInfo.areaId == 0){
			$.messager.alert('提醒', "请填写库区名称");
			return;
		}
		if(advInfo.wareId == 0){
			$.messager.alert('提醒', "请填写所属仓库");
			return;
		}
		if(advInfo.status == null ||advInfo.status ==undefined){
			$.messager.alert('提醒', "请填写状态");
			return;
		}
		$.ajax({
			type : "POST",
			url : './editLocation',
			data : advInfo,
			success : function(data) {
				if (data) {
					$.messager.alert("提示", data.desc, "info");
	        		$('#editLocation').dialog('close');//关闭弹出窗口
		        	$("#location_list").datagrid('reload');//重新加载table  */
				}else if(data.code =='030001'){
					$.messager.alert("提示", data.desc, "info");
				}else{
	        		$.messager.alert("提示", data.desc, "info");
	        	}
			}
		});
	}
}
//取消按钮
function cancelLocationBtn(){
//	alert("a");
	$(".editNoticBox").window("close");
}