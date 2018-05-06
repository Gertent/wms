var basepath = $("#basepath").val();

var newurl=basepath + '/outstock/ListOutStockBiLL?flag=2';
$(function() {
//打包复检
	init(newurl);

});
// 初始化方法
function init(url) {
	var queryParams = {};
	$('#recheckListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});

	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	
	$('#recheck_list').datagrid({
		width : 'auto',
		height : 'auto',
		nowrap : true,// True 数据将在一行显示
		striped : true,// True 就把行条纹化
		collapsible : false,// True 可折叠
		url : url,
		remoteSort : true, // 定义是否从服务器给数据排序
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
			field : 'orderNo',
			title : '订单号',
			width : 100,
			formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
			},
			align : 'center'
		}, {
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
				}
			}
		},{
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
			field : 'logisComName',
			title : '承运商',
			width : 100,
			align : 'center'
		}, {
			field : 'recheckStartTime',
			title : '复检时间',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		}, {
			field : 'recheckUserName',
			title : '复检员',
			width : 100,
			align : 'center'
		}, {
			field : 'freeze',
			title : '冻结',
			hidden:true,
			align : 'center'
		}  ] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber:pageNo,
		queryParams: queryParams,
		pageList : [ 10, 20, 30, 40 ],
		onLoadSuccess: function (data){
			if( 0 != data.total ){
				var rows=data.rows;
				var len=rows.length;
				for(var i=0;i<len;i++){
					if(rows[i].orderNo==$('#orderNo').val()){
						$('#recheck_list').datagrid('checkRow',i);
					}
				}
			}
		}
	});

}

// 根据订单号获取意向单详情
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

//打包复检搜索按钮
function recheckListSearchBox(){
	init(newurl);
}
//重置按钮
function formReset() {
	$('#recheckListSearch').find(':input').each(function() {
		$(this).val('');
	});
}
//打包复检
$('#packageReCheck').click(function(){
	var selRows = $('#recheck_list').datagrid('getChecked'); 
	if(selRows.length>0){
		if(selRows.length==1){
			var freeze=selRows[0].freeze;//冻结， 0 未冻结 1 冻结
			if(freeze==1){
				$.messager.alert('提醒', '该订单已冻结!');
			}else{
				var l=(screen.availWidth-900)/2;
				var t=(screen.availHeight-550)/2;
				$("#recheckInfo").window({
					href:basepath+"/outstock/recheckPage?orderNo="+selRows[0].orderNo,
					width:840,
					height:530,
					modal:true,
					shadow:false,
					collapsible:false,
					minimizable:false,
					maximizable:false,
					title:"打包复检"
				});
			}
		}else{
			$.messager.alert('提醒', '只能选择一条数据!');
		}
	}else {
		$.messager.alert('提醒', '请选择一条数据!');
	}
});
