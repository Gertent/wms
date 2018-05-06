var basepath = $("#basepath").val();
var newurl = './listLocationGoodsBind';
$(function() {

	init(newurl);

});
function init(url) {
	// 日期配置对象
	var singleConfig={
		language: 'cn',
		singleMonth: true,
		showShortcuts: false,
		singleDate : true,
		showTopbar: false
	}
	$('#timeRange').dateRangePicker(singleConfig).bind('datepicker-change', function (event, obj) {
		$("#createTime").val(obj.date1.format("yyyy-MM-dd"));
	});

	$('#timeRangeEx').dateRangePicker(singleConfig).bind('datepicker-change', function (event, obj) {
		$("#validityTime").val(obj.date1.format("yyyy-MM-dd"));
	});
	var queryParams = {};
	$('#locationGoodBindListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	queryParams.locationId=$('#locationId').combobox('getValues').join(',');
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#locationGoodsBind_list').datagrid({
		// title : '部门列表',
		width : 'auto',
		height : 'auto',
		border : true,
		nowrap : true,
		// True 数据将在一行显示
		striped : true,
		// True 就把行条纹化
		collapsible : true,
		// True 可折叠
		url : './listLocationGoodsBind',
		remoteSort : true, // 定义是否从服务器给数据排序
		singleSelect : false, // True 只能选择一行
		fit : false,
		toolbar : '#box',
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
			field : 'goodsCode',
			title : '商品编码',
			width : 100,
			align : 'center'
		}, {
			field : 'goodsBarCode',
			title : '条码',
			width : 100,
			align : 'center'
		}, {
			field : 'goodsName',
			title : '商品名称',
			width : 100,
			align : 'center'
		}, {
			field : 'areaName',
			title : '库区',
			width : 80,
			align : 'center'

		},  {
			field : 'locationNo',
			title : '货位号',
			width : 100,
			align : 'center'
			
		},{
			field : 'validityNum',
			title : '可卖数量',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if(row.saleType=="2"){
					return 0;
				}else{
					return val;
				}
			}
		},{
			field : 'orderNum',
			title : '订单占用数量',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if(row.saleType=="2"){
					return 0;
				}else{
					return row.locationNum-row.validityNum;
				}
			}
		},{
			field : 'locationNum',
			title : '库存数量',
			width : 100,
			align : 'center'
		},{
			field : 'unit',
			title : '单位',
			width : 100,
			align : 'center'
		},{
			field : 'packageNum',
			title : '包装数量',
			width : 100,
			align : 'center'
		},{
			field : 'saleType',
			title : '库区性质',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "可卖";
				} else if (val == "2") {
					return "不可卖";
				}
			}
		}, {
			field : 'createTime',
			title : '入库日期',
			width : 100,
			align : 'center',
			formatter : formatDatebox

		}, {
			field : 'validityTime',
			title : '有效期',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		} ] ],
		pagination : true,// 显示分页栏。
		rownumbers : true,// 显示行号的列。
		pageSize : rows,
		pageNumber : pageNo,
		queryParams : queryParams,
		pageList : [ 10, 20, 30, 40 ],
	});
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

	return dt.format("yyyy-MM-dd"); // 扩展的Date的format方法(上述插件实现)
};
function locationGoodsBindReset() {
	location.reload();
}
// 动态搜索方法
function locationGoodsBindSearchBox() {
	init(newurl);
}

$('#locationGoodsBindExport').click(function(){
	var columnViewData = $('#locationGoodsBind_list').datagrid('getColumnFields');
	var jsonArr=[];
	var opts = $('#locationGoodsBind_list').datagrid('getColumnFields'); //这是获取到所有的FIELD
	var colName=[];
	var parm=null;
	for(i=0;i<opts.length;i++){
		var col = $('#locationGoodsBind_list').datagrid( "getColumnOption" , opts[i] );
		parm={dataIndex:col.field,text:col.title,hidden:(col.hidden==undefined?false:true)};
		jsonArr.push(JSON.stringify(parm));
	}

	//模拟表单提交
	var url = basepath + '/locationGoodsBind/locationGoodsBindkExport';
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
//库区change事件
$('#areaId').combobox({
	onChange : function(newvalue,o){
		$.ajax({
			url : basepath + '/locationGoodsBind/getLocationListByArea',
			type : 'post',
			data : {
				'areaId' : newvalue
			},
			success : function(result) {
				console.log(result);
				if (result != null) {
					$("#locationId").combobox('clear');
					$("#locationId").combobox("loadData", result);
					// $("#locationId").combobox('select', result[0].id);
					// $.each(result, function(i, item){
					// 	$("#locationId").append("<option value='"+item.id+"'>"+item.locationNo+"</option>");
					// });
				}
			}
		});
	}
});