var basepath = $("#basepath").val();
var newurl=basepath + '/instock/ServerList';
$(function() {

	init(newurl);

});
//初始化方法
function init(url) {
	var pageNo = 1;
	var rows = 10;
	var queryParams = {};
	$('#serverManageSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#serverStock_list').datagrid({
		width : 'auto',
		height : 'auto',
		nowrap : true,// True 数据将在一行显示
		striped : true,// True 就把行条纹化
		collapsible : false,// True 可折叠
		url : url,
		remoteSort : true, // 定义是否从服务器给数据排序
		singleSelect : true, // True 只能选择一行
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
			field : 'serverNo',
			title : '服务单号',
			width : 100,
			align : 'center',
			formatter : function(value, row, index) {
                if(row.type=="2"||row.type=="3"){
                    return '<a style="color:#1556e8;" target="_blank" onclick="getServerGoodsInfo(' + row.serverNo + ','+row.orderNo+')" class="ellipsis" >' + value + '</a>';
                }else{
                    return value;
                }

			}
		}, {
			field : 'orderNo',
			title : '订单号',
			width : 100,
			align : 'center'
		},{
			field : 'type',
			title : '订单类型',
			width : 80,
			align : 'center',
			formatter : function(val, row, index) {
				//1：采购单入库，2：退货服务单入库(订单取消作为特殊服务单处理) 3：换货服务单入库
				if (val == "2") {
					return "退货";
				} else if (val == "3") {
					return "换货";
				}
			}
		}, {
			field : 'wareName',
			title : '收货单位',
			width : 100,
			align : 'center'
		}, {
			field : 'status',
			title : '状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "202"||val=="302") {
					return "等待";
				} else{
					return "完成";
				}
			}
		}
		] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageNumber: pageNo,
		pageSize: rows,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});

}

//根据订单号获取意向单详情
function getInstockBiLLInfo(a) {
	var l=(screen.availWidth-900)/2;
	var t=(screen.availHeight-550)/2;       
	window.open(basepath+"/instock/InstockBiLLInfo?Id="+a, "入库管理-入库列表-入库单打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

}

//订单详情验货入库
$("#checkServer").click(function() {
	var rows = $('#serverStock_list').datagrid('getSelections');
	if (rows.length > 1) {
		$.messager.alert('提醒', '只能选择一条数据 !');
		return;
	} else if (rows.length == 0) {
		$.messager.alert('提醒', '请选择一条数据 !');
		return;
	}

	if (rows) {
		 var orderNO=rows[0].orderNo;//订单号
         var serverNo=rows[0].serverNo;//服务单号
     	var l=(screen.availWidth-900)/2;
    	var t=(screen.availHeight-550)/2;   
		// window.open(basepath+"/instock/ServerListInfo?serverNo="+serverNo+"&orderNo="+orderNO, "", "height=650, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

		$("#validateInstock").window({
			href:basepath+"/instock/ServerListInfo?serverNo="+serverNo+"&orderNo="+orderNO,
			width:900,
			height:530,
			modal:true,
			shadow:false,
			collapsible:false,
			minimizable:false,
			maximizable:false,
			title:"入库管理-售后收货-手动确认"
		});

	} else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}

});

//时间格式化
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

	return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
};
//导出
$('#serverManageExport').click(function(){
	//模拟表单提交
	var url = basepath + '/instock/afterReceiptInStockExport';
	var form=$("<form>");//定义一个form表单
	form.attr("style","display:none");
	form.attr("target","");
	form.attr("method","post");//请求类型
	form.attr("action",url);//请求地址
	$("body").append(form);//将表单放置在web中

	var input1=$("<input>");
	input1.attr("type","hidden");
	input1.attr("name","columns");
	input1.attr("value",JSON.stringify(afterReceiptInStockExcelHead));
	form.append(input1);

	form.submit();//表单提交
});
//搜索
function serverManageSearch(){
	init(newurl);
}
//重置
function serverManageReset(){
	$('#serverManageSearch').find(':input').each(function() {
		$(this).val('');
	});
}
//测试
$('#testInfo').click(function(){
	var serverNo='11705231356034';
	var orderNO='11705231356034';
	$("#validateInstock").window({
		href:basepath+"/instock/ServerGoodsInfo?serverNo="+serverNo+"&orderNo="+orderNO,
		width:900,
		height:530,
		modal:true,
		shadow:false,
		collapsible:false,
		minimizable:false,
		maximizable:false,
		title:"入库管理-售后收货-商品信息"
	});
});

//根据服务单号获取商品信息
function getServerGoodsInfo(serverNo,orderNo) {
	var l=(screen.availWidth-900)/2;
	var t=(screen.availHeight-550)/2;
	window.open(basepath+"/instock/ServerGoodsInfo?serverNo="+serverNo+"&orderNo="+orderNo, "入库管理-售后收货-商品信息", "height=550, width=1000, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
}