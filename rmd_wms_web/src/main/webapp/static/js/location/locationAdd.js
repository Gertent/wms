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
function addlocationBtn(){
	var isValidatePass = $('#add').form('validate');
	if (isValidatePass){
		var advInfo = $('#add').serializeJson();
		if(advInfo.locationNo == null ||advInfo.locationNo ==undefined){
			$.messager.alert('提醒', "请填写货位编号!","info");
			return;
		}
		if(advInfo.wareId == null ||advInfo.wareId ==undefined||advInfo.wareId == 0){
			$.messager.alert('提醒', "请选择所属仓库!","info");
			return;
		}
		if(advInfo.areaId == null ||advInfo.areaId ==undefined||advInfo.areaId == 0){
			$.messager.alert('提醒', "请选择所属库区!","info");
			return;
		}
		if(advInfo.status == null ||advInfo.status ==undefined){
			$.messager.alert('提醒', "请填写状态!","info");
			return;
		}
		$.ajax({
			type : "POST",
			url : './addLocation',
			data : advInfo,
			success : function(data) {
				if (data) {
					$.messager.alert("提示", data.desc, "info");
					$("#code").val('');
					// $("#wareId").val('');
					// $("#areaId").val('');
					// $("#status").val('');
					$("#location_list").datagrid('reload');//重新加载table  */
				}else if(data.code =='030001'){
					$.messager.alert("提示", "此货位号已存在", "info");
				}else{
	        		$.messager.alert("提示", data.desc, "info");
	        	}
			}
		});
	}
}
function wareChange(value){
	//$("#areaId").empty(); 
	$("#areaId").find("option").remove(); 
	$.ajax({
		type : "POST",
		url : './getWareArea',
		data : {wareId:value},
		success : function(data) {
			if(data!=null&&data.length>0){
				
				for(var i=0;i<data.length;i++){
					$("#areaId").append("<option value="+data[i].id+">"+data[i].areaName+"</option>"); 
				}
				
			}
		}
	});
}
//取消按钮
function cancelLocationBtn(){
//	alert("a");
	$(".addNoticBox").window("close");
}