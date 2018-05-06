var basepath = $("#basepath").val();

var newurl = basepath + '/logistics/ListLogisticsCompany';
$(function() {
	// 出库制单
	init(newurl);

	
});
// 初始化方法
function init(url) {
	var queryParams = {};
	$('#logisticsListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}

	$('#logistics_list').datagrid({
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
			titel : '出库单id',
			hidden : true
		}, {
			field : 'name',
			title : '承运商名称',
			width : 80,
			align : 'center'
		}, {
			field : 'status',
			title : '状态',
			width : 60,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "0") {
					return "禁用";
				} else if (val == "1") {
					return "启用";
				}
			}
		}, {
			field : 'code',
			title : '编号',
			width : 60,
			align : 'center'
		}, {
			field : 'deliveryName',
			title : '配送范围',
			width : 160,
			align : 'center',
			formatter: function(value, row, index) {
				return '<a href="#" title='+value+'>'+value+'</a>';
			}
		}, {
			field : 'maxWeight',
			title : '承运重量(KG)',
			width : 60,
			align : 'center',
			formatter : function(value, row, index) {
				return (parseFloat(row.minWeight==null?0:row.minWeight).toFixed(2))+"-"+(parseFloat(row.maxWeight==null?0:row.maxWeight).toFixed(2));
			}
		}, {
			field : 'phone',
			title : '联系电话',
			width : 60,
			align : 'center'
		}, {
			field : 'contactName',
			title : '联系人',
			width : 60,
			align : 'center'
		} ] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber : pageNo,
		pageSize : rows,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

// 锁库跳转按钮页面
function lockLocation(id) {
	$.ajax({
		url : basepath + '/outstock/LockGoodBindNum',
		type : 'post',
		data : {
			'Id' : id
		},
		success : function(result) {
			if (result == "true") {
				$.messager.alert('提醒', '已经锁库!');
			} else {
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



//弹框--添加承运商
$("#addLogistics").bind("click", function(){
	// $("#addMemBox").load(basepath + "/logistics/saveLogistics");
    $("#addLogisticsAdFreightBox").window({
        href:basepath + "/logistics/saveLogistics",
    	width:1000,
    	height:530,
        modal:true,
        shadow:false,
        collapsible:false,
        minimizable:false,
        maximizable:false,
        title:"配送管理-承运商管理-添加承运商",
		onBeforeClose : function() {
			sort();
		}
    });
});


//【添加--修改】承运商
function savereg(){
	// var formData=$('#myFormPost').serialize();
	// console.log(formData);
	// return;
	var flag=checkCompany();
	if(!flag) {return;}
	//承运商配送范围编码
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
   if(provinceTemp==''){
	   $.messager.alert('提醒', '请选择配送范围!');
	   return;
   }
    var name=$("#Lname").val();
    var code=$("#code").val();
    var phone=$("#phone").val();
    var minWeight=$("#minWeight").val();
    var maxWeight=$("#maxWeight").val();
    var status=$('input[name="status"]:checked').val();
    var id=$("#id").val();
	var rangeId =$("#rangeId").val();
	var contactName=$('#contactName').val();
	if(id==0){//添加操作
		$.ajax({
			url:basepath+'/logistics/saveLogisticsCompany',
			type:'post',
			data:{'name':name,'code':code,'phone':phone,'minWeight':minWeight,'maxWeight':maxWeight,'status':status,'provinceTemp':provinceTemp,'provinceName':provinceName,'contactName':contactName},
			success:function(result){
				if(result == "true"){
					window.close(); 
					sort();
				}else{
					$.messager.alert('提醒', '保存失败!');
				}
				
			}
		});
	}
	else if(id>0){
		$.ajax({
			url : basepath + '/logistics/editLogisticsCompany',
			type : 'post',
			data : {'id':id,'name':name,'code':code,'phone':phone,'minWeight':minWeight,'maxWeight':maxWeight,'status':status,'provinceTemp':provinceTemp,'rangeId':rangeId,'provinceName':provinceName,'contactName':contactName},
			success : function(result) {
				if (result == "true") {
					window.close();
					sort();
				} else {
					$.messager.alert('提醒', '保存失败!');
				}

			}
		});
	}
}

function sort() {
	location.reload();
}

//修改承运商界面
$("#editLogistics").click(function() {
	var rows = $('#logistics_list').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert('提醒', '只能选择一条数据 !');
		return;
	} else if (rows.length == 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}

	if (rows) {
		var id=rows[0].id;
        $("#editLogisticsBox").window({
	        href:basepath + "/logistics/editLogistics?id="+id,
	    	width:1000,
	    	height:530,
	        modal:true,
	        shadow:false,
	        collapsible:false,
	        minimizable:false,
	        maximizable:false,
	        title:"配送管理-承运商管理-修改承运商",
	        onBeforeClose : function() {
				sort();
			}
	    });

	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});

//修改运费模板
$("#editFreightTemplate").click(function() {
	var rows = $('#logistics_list').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert('提醒', '只能选择一条数据 !');
		return;
	} else if (rows.length == 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}

	if (rows) {
		var id=rows[0].id;
		$("#editFreightTemplateBox").window({
			href:basepath + "/logistics/editFreightTemplate?logisticsId="+id,
			width:1000,
			height:530,
			modal:true,
			shadow:false,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			title:"配送管理-承运商管理-修改运费模板",
			onBeforeClose : function() {
				sort();
			}
		});

	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});
//启用
$('#openLogistics').click(function(){
	$.messager.confirm('确认','确认启用吗？',function(r){
		if (r){
			updateLogisStatus(1);
		}
	});

});
//禁用
$('#forbidLogistics').click(function(){
	$.messager.confirm('确认','确认禁用吗？',function(r){
		if (r){
			updateLogisStatus(0);
		}
	});
});
//更新承运商状态
function updateLogisStatus(status){
	var rows = $('#logistics_list').datagrid('getSelections');
	if (rows.length <=0) {
		$.messager.alert('提醒', '请选择数据 !');
		return;
	}
	var len=rows.length;
	var ids='';
	for(var i=0;i<len;i++){
		ids+=rows[i].id+',';
	}
	if(ids!=''){
		ids=ids.substring(0,ids.length-1);
	}
	$.ajax({
		url : basepath + '/logistics/updateLogisStatus',
		type : 'post',
		data : {'ids':ids,'status':status},
		success : function(result) {
			if (result.status == "200") {
				sort();
			} else {
				$.messager.alert('提醒', '失败!');
			}

		}
	});
}
//动态查询
function logisticsListSearchBox(){
	
		init(newurl);
	}

//重置按钮
function formReset() {
	location.reload();
}

