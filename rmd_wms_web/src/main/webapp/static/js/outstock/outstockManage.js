var basepath = $("#basepath").val();

var newurl=basepath + '/outstock/ListOutStockBiLL?flag=1';
$(function() {
//出库制单
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
	$('#datetimeRange').dateRangePicker(configObject).bind('datepicker-change', function (event, obj) {
		$("#starTime").val(obj.date1.format("yyyy-MM-dd hh:mm"));
		$("#endTime").val(obj.date2.format("yyyy-MM-dd hh:mm"));
	});
	var queryParams = {};
	$('#outstockListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	//选中checkbox的值
	if ($("#dobinningPrint").is(":checked")) {
		queryParams['dobinningPrint']="0";
	}
	if ($("#dopickingPrint").is(":checked")) {
		queryParams['dopickingPrint']="0";
	}
	if ($("#dowaybillPrint").is(":checked")) {
		queryParams['dowaybillPrint']="0";
	}
	if ($("#pickingStatus").is(":checked")) {
		queryParams['pickingStatus']="4";
	}
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	
	$('#outstock_list').datagrid({
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
			title : '出库单id',
			hidden : true
		}, {
			field : 'orderNo',
			title : '订单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
			field : 'orderdate',
			title : '订单日期',
			width : 120,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'wareName',
			title : '发货仓库',
			width : 100,
			align : 'center'
		}, {
			field : 'weight',
			title : '重量(KG)',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				if(value!=null&&value>=0){
					return parseFloat(value).toFixed(2);
				}
				
			}
		}, {
			field : 'parcelWeight',
			title : '包装重量(KG)',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				if(value!=null&&value>=0){
					return parseFloat(value).toFixed(2);
				}

			}
		}, {
			field : 'goodsAmount',
			title : '订单数量',
			width : 60,
			align : 'center'
		},{
			field : 'logisComName',
			title : '承运商',
			width : 100,
			align : 'center'
		/*},{
			field : 'code',
			title : '承运商编码',
			width : 100,
			align : 'center'*/
		}, {
			field : 'logisticsNo',
			title : '运单号',
			width : 100,
			align : 'center'
		}, {
			field : 'dobinningPrint',
			title : '装箱单打印',
			width : 60,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "未打印";
				} else if (val == "1") {
					return "<span style='color:green;'>√</span>";
				}
			}
		}, {
			field : 'dopickingPrint',
			title : '拣货单打印',
			width : 60,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "未打印";
				} else if (val == "1") {
					return "<span style='color:green;'>√</span>";
				}
			}
		}, {
			field : 'dowaybillPrint',
			title : '运单打印',
			width : 60,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "未打印";
				} else if (val == "1") {
					return "<span style='color:green;'>√</span>";
				}
			}
		}, {
			field : 'pickingStatus',
			title : '拣货',
			width : 60,
			align : 'center',
			formatter : function(val, row, index) {//0：异常，1：待拣货，2：拣货中，3：已完成，4：缺货
				if (val == "0") {
					return "异常";
				} else if (val == "1") {
					return "待拣货";
				}else if (val == "2") {
					return "拣货中";
				}else if (val == "3") {
					return "已完成";
				}else if (val == "4") {
					return "缺货";
				}
			}
		}, {
			field : 'recheckStatus',
			title : '打包复检',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {//0：复检异常（不用），1：等待复检，2：复检中，3：已完成
				if (val == "0") {
					return "复检异常";
				} else if (val == "1") {
					return "等待复检";
				}else if (val == "2") {
					return "复检中";
				}else if (val == "3") {
					return "已完成";
				}
			}
		},  {
			field : 'status',
			title : '状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {//当前节点状态，12101-拣货，12102-打包复检，12103-录入运单号，12104-交接发货
				if (val == "12101") {
					return "拣货";
				} else if (val == "12102") {
					return "打包复检";
				}else if (val == "12103") {
					return "录入运单号";
				}else if (val == "12104") {
					return "交接发货";
				}else if(val == "0"){
					return "异常";
				}
			}
		},{
			field : 'deliveryNo',
			title : '发货单号',
			width : 100,
			align : 'center'
		}, /*{
			field : '锁库操作',
			title : '锁库操作',
			width : 100,
			align : 'center',
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="lockLocation(' + row.id + ')" class="ellipsis" >锁库</a>';
			}
		},*/  ] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber: pageNo,
		pageSize: rows,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

// 锁库跳转按钮页面
function lockLocation(id){
	$.ajax({
		url:basepath+'/outstock/LockGoodBindNum',
		type:'post',
		data:{'Id':id},
		success:function(result){
			if(result == "true"){
				$.messager.alert('提醒', '已经锁库!');
			}else{
				$.messager.alert('提醒', '锁库成功!');
			}
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

//批量生成测试数据
$("#insertDemo").click(function(){
	$.ajax({
		url : basepath + '/outstock/insertDemo',
		success : function(result) {
			if (result) {
				$.messager.alert('提醒', '批量添加成功!');
				init(basepath + '/outstock/ListOutStockBiLL?flag=1');
			} else {
				$.messager.alert('提醒', '数据传输失败!');
			}
		}
	});
});

//出库单--修改承运商
$("#updateLogistics").click(function(){

	var rows = $('#outstock_list').datagrid('getSelections');
	var len=rows.length;
	if (len== 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}
	var id='';
	for(var i=0;i<len;i++){
		if(rows[i].logisticsNo&&rows[i].logisticsNo!=''&&rows[i].logisticsNo&&rows[i].logisticsNo!=''){
			$.messager.alert('提醒', '存在承运商运单号已录入完成的订单！');
			return;
		}
		id+=rows[i].id+',';
	}
	if(id!=0){
		id=id.substring(0, id.length-1);
	}
	
	if (rows) {
		/*$("#LorderNo").html(rows[0].orderNo);//订单号赋值
		$("#logisComName").html(rows[0].logisComName);//承运商复制*/
		$("#Lid").val(id);//承运商复制
		
		var eType = $("#logisticsCompany");
	    eType.empty();
	    eType.append("<option value=0>请选择</option>");
		$.ajax({
			type : 'get' ,
			url : "getLogisticsCompany" ,
			success : function(result) {
				$(result).each(function(i,e){
					var code = e.id;
					var name = e.name;
					var op = $("<option>").val(code).text(name);
					eType.append(op);
				});
			}
		});	
		$("#editLogisticsCompany").window({
			width : 500,
			height : 280,
			modal : true,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			title : "承运商修改",
			onBeforeClose : function() {
				sort();
			}
		});
	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});


//确定按钮
$("#commitedit").click(function() {
	var info = {
		//orderNo : $.trim($("#LorderNo").html()),
		logisComId : $.trim($("#logisticsCompany option:selected").val()),
		logisComName : $.trim($("#logisticsCompany option:selected").text()),
		id : $.trim($("#Lid").val()),

	};
	if (info.logisticsCompany == 0) {
		$.messager.alert('提醒', "请选择承运商");
		return;
	} else {
		$.ajax({
			type : "POST",
			url : basepath + '/outstock/editLogisticsCompany',
			data : info,
			dataType : "json",
			success : function(result) {
				if (result) {
					sort();
				} else {
					$.messager.alert('提醒', '失败!');
				}
			}
		});
	}
});
//取消按钮
$("#cancel").click(function() {
	$("#editLogisticsCompany").window("close");
});

//排序
function sort() {
	location.reload();
}

//动态收索
function outstockListSearch(){
	
	init(newurl);
}

//拣货单打印
$("#printPackBill").click(function() {
	var selRows = $('#outstock_list').datagrid('getChecked'); 
	if(selRows.length>0){

		var ids='';
		for(var i=0;i<selRows.length;i++){

			ids+=selRows[i].id;
			if(i!=(selRows.length-1)){
				ids+=",";
			}
			var rowIndex=$('#outstock_list').datagrid('getRowIndex',selRows[i]);
			$("#outstock_list").datagrid("getRows")[rowIndex]["dopickingPrint"]='1';
			$('#outstock_list').datagrid('refreshRow', rowIndex);
		}	
		if(ids.length>0){
			if(ids.lastIndexOf(',')==ids.length-1){
				ids=ids.substring(0, ids.length-1);
			}
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;       
			window.open(basepath+"/outstock/packBillPrint?Id="+ids, "拣货单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			updatePrintStatus(ids,1,2);
		}
		
	}

	if (selRows) {
/*		*/
		
	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});
//装箱单打印
$('#printInBoxBill').click(function(){
	var selRows = $('#outstock_list').datagrid('getChecked'); 
	if(selRows.length>0){
		var ids='';
		for(var i=0;i<selRows.length;i++){
			ids+=selRows[i].id;
			if(i!=(selRows.length-1)){
				ids+=",";
			}
			var rowIndex=$('#outstock_list').datagrid('getRowIndex',selRows[i]);
			$("#outstock_list").datagrid("getRows")[rowIndex]["dobinningPrint"]='1';
			$('#outstock_list').datagrid('refreshRow', rowIndex);
		}	
		if(ids.length>0){
			if(ids.lastIndexOf(',')==ids.length-1){
				ids=ids.substring(0, ids.length-1);
			}
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;       
			window.open(basepath+"/outstock/inBoxBillPrint?Id="+ids, "装箱单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			updatePrintStatus(ids,1,1);
		}
	}else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

	
});

$('#printLogicBill').click(function(){
	var selRows = $('#outstock_list').datagrid('getChecked'); 
	var len=selRows.length;
	if(len>0){
		var isSameLogis=true;
		var code=selRows[0].logisComName;
		if(len>1){
			for(var i=1;i<len;i++){
				if(code!=selRows[i].logisComName){
					isSameLogis=false;
					break;
				}
			}
		}
		if(!isSameLogis){
			$.messager.alert('提醒', '请选择相同承运商!');
			return;
		}
		var ids='';
		for(var i=0;i<len;i++){
			ids+=selRows[i].id;
			if(i!=(len-1)){
				ids+=",";
			}

		}	
		if(ids.length>0){
			if(ids.lastIndexOf(',')==ids.length-1){
				ids=ids.substring(0, ids.length-1);
			}
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;       
			window.open(basepath+"/printpdf/logicGeneratePdf?ids="+ids, "运单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			updatePrintStatus(ids,1,3);
		}
				
	}else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}
});
//iReport打印
$('#printLogisticsBillIpt').click(function(){
	var selRows = $('#outstock_list').datagrid('getChecked'); 
	var len=selRows.length;
	if(len>0){
		var isSameLogis=true;
		var code=selRows[0].logisComName;
		if(len>1){
			for(var i=1;i<len;i++){
				if(code!=selRows[i].logisComName){
					isSameLogis=false;
					break;
				}
			}
		}
		if(!isSameLogis){
			$.messager.alert('提醒', '请选择相同承运商!');
			return;
		}
		var ids='';
		for(var i=0;i<len;i++){
			ids+=selRows[i].id;
			if(i!=(len-1)){
				ids+=",";
			}
			var rowIndex=$('#outstock_list').datagrid('getRowIndex',selRows[i]);
			$("#outstock_list").datagrid("getRows")[rowIndex]["dowaybillPrint"]='1';
			$('#outstock_list').datagrid('refreshRow', rowIndex);
		}	
		if(ids.length>0){
			if(ids.lastIndexOf(',')==ids.length-1){
				ids=ids.substring(0, ids.length-1);
			}
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;       
			window.open(basepath+"/outstock/logisticsBillPrintIpt?ids="+ids, "运单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
			updatePrintStatus(ids,1,3);
		}
				
	}else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}
});
//重置按钮
function formReset() {
	location.reload();
}

$('#outStockExport').click(function(){
	var columnViewData = $('#outstock_list').datagrid('getColumnFields'); 
	var jsonArr=[];
	var opts = $('#outstock_list').datagrid('getColumnFields'); //这是获取到所有的FIELD
	var colName=[];
	var parm=null;
	for(i=0;i<opts.length;i++){
		var col = $('#outstock_list').datagrid( "getColumnOption" , opts[i] );
		parm={dataIndex:col.field,text:col.title,hidden:(col.hidden==undefined?false:true)};
		jsonArr.push(JSON.stringify(parm));
	}
	
	//模拟表单提交
    var url = basepath + '/outstock/outStockExport';
    var form=$("<form>");//定义一个form表单
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");//请求类型
    form.attr("action",url);//请求地址
    $("body").append(form);//将表单放置在web中

    var input1=$("<input>");
    input1.attr("type","hidden");
    input1.attr("name","columns");
    input1.attr("value","["+jsonArr.toString()+"]");
    form.append(input1);
    
    form.submit();//表单提交
	
});

//Excel导出
$("#outStockBillExport").click(function(){
    //模拟表单提交
    var url = basepath + '/outstock/outStockBillExport';
    var form=$("<form>");//定义一个form表单
    form.attr("style","display:none");
    form.attr("target","");
    form.attr("method","post");//请求类型
    form.attr("action",url);//请求地址
    $("body").append(form);//将表单放置在web中

    var input1=$("<input>");
    input1.attr("type","hidden");
    input1.attr("name","columns");
    input1.attr("value",JSON.stringify(outStockBillExcelHead));

    var input2=$("<input>");
    input2.attr("type","hidden");
    input2.attr("name","flag");
    input2.attr("value","1");
    form.append(input1);
    form.append(input2);
    form.submit();//表单提交
});
//更新打印状态
function updatePrintStatus(ids,status,printType){
	$.ajax({
		url : basepath + '/outstock/updatePrintStatus',
		type : 'post',
		data : {'ids':ids,'status':status,'printType':printType},
		success : function(result) {
			if (result.status == "200") {
				//sort();
			} else {

			}

		}
	});
}