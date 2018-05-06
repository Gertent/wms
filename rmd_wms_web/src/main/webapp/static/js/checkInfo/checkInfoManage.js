var basepath = $("#basepath").val();
var newurl='./listCheckInfo';
$(function() {

	init(newurl);

});
function init(url){
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
	// init(newurl);
	var queryParams = {};
	$('#checkInfoListSearch').find('*').each(function() {
		queryParams[$(this).attr('name')] = $(this).val();
	});
	var pageNo = 1;
	var rows = 10;
	if (queryParams.pageNo && queryParams.pageNo != '') {
		pageNo = queryParams.pageNo;
		rows = queryParams.rows;
	}
	$('#checkInfo_list').datagrid({
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
		url : './listCheckInfo',
		remoteSort: true, // 定义是否从服务器给数据排序
		singleSelect: false, // True 只能选择一行
		fit: true,
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
		},{
			field : 'doChecked',
			titel : '是否已盘',
			hidden : true
		}, {
			field : 'goodsCode',
			title : '商品编号',
			width : 100,
			align : 'center'
		}, {
			field : 'checkNo',
			title : '盘点任务号',
			width : 100,
			align : 'center',
		}, {
			field : 'submitStatus',
			title : '提报状态',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return "已完成";
				} else if (val == "0") {
					return "待处理";
				}
			}
		}, {
			field : 'doAudit',
			title : '财务审核',
			width : 100,
			align : 'center',
			formatter : function(val, row, index) {
				if (val == "1") {
					return '<input type="checkbox" class="bE5 pdL5"  checked=true disabled="disabled"/>';
				}else{
					return '<input type="checkbox" class="bE5 pdL5" disabled="disabled"/>';
				}
			}
		},{
			field : 'firstCheckNum',
			title : '初盘数量',
			width : 100,
			align : 'center'
		
		},{
			field : 'firstCheckLockDiff',
			title : '初盘锁定差异',
			width : 100,
			align : 'center'
		},{
			field : 'firstCheckValidDiff',
			title : '初盘有效差异',
			width : 100,
			align : 'center'
		},{
			field : 'secondCheckNum',
			title : '复盘数量',
			width : 100,
			align : 'center'
		},{
			field : 'secondCheckLockDiff',
			title : '复盘锁定差异',
			width : 100,
			align : 'center'
		},{
			field : 'secondCheckValidDiff',
			title : '复盘有效差异',
			width : 100,
			align : 'center'
		},{
			field : 'goodsName',
			title : '产品名称',
			width : 100,
			align : 'center'
		},{
			field : 'spec',
			title : '型号',
			width : 100,
			align : 'center'
		},{
			field : 'packageNum',//WMS里面的型号在商品里面叫规格，WMS里面规格在商品系统里面叫包装数量
			title : '规格',
			width : 100,
			align : 'center'
		},{
			field : 'unit',
			title : '单位',
			width : 100,
			align : 'center'
		},{
			field : 'locationNum',
			title : '数量',
			width : 100,
			align : 'center'
		},{
			field : 'wareName',
			title : '仓库名称',
			width : 100,
			align : 'center'
		},{
			field : 'locationNo',
			title : '货位号',
			width : 100,
			align : 'center'
		},{
			field : 'validityTime',
			title : '有效期',
			width : 100,
			align : 'center',
			formatter : formatDatebox
		},{
			field : 'createrName',
			title : '创建人',
			width : 100,
			align : 'center'
		},{
			field : 'checkUserName',
			title : '盘点人',
			width : 100,
			align : 'center'
		}] ],
		onLoadSuccess: function (data){
			if( 0 == data.total ){
				//$.messager.alert('信息提示','无数据!','error');
			}
		},
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
function checkInfoReset() {
	location.reload();
}
// 动态搜索方法
function checkInfoSearchBox() {
	init(newurl);
}

//提报
$('#handInOperate').click(function(){
	var rows = $('#checkInfo_list').datagrid('getSelections');
	var len=rows.length;
	if (len <= 0) {
		$.messager.alert('提醒', '请选择数据 !');
		return;
	}
	var rw=null;
	var ids='';
	var flag=true;
	for(var i=0;i<len;i++){
		rw=rows[i];
		if(rw.submitStatus=="1"||rw.doChecked!="1"){			
			flag=false;
			$.messager.alert('提醒', '选择了已提报或未盘数据 !');
			return false;
		}
		if(rw.submitStatus=="-1"){			
			flag=false;
			$.messager.alert('提醒', '选择了不可提报数据 !');
			return false;
		}
		ids+=rw.id+",";
	}
	if(ids!=''){
		ids=ids.substring(0,ids.length-1);
	}
	/*if(!flag){
		$.messager.alert('提醒', '选择了已提报或已盘数据 !');
		return;
	}*/
	$(".addHandIn").window({
  		href:basepath + "/check/getHandInPage?ids="+ids,
  		 width:700,
         height:520,
         modal:true,
         shadow:false,
         collapsible:false,
         minimizable:false,
         maximizable:false,
         title:"盘点记录-提报"
      }); 
  });


$('#checkInfoExport').click(function(){
	var columnViewData = $('#checkInfo_list').datagrid('getColumnFields');
	var jsonArr=[];
	var opts = $('#checkInfo_list').datagrid('getColumnFields'); //这是获取到所有的FIELD
	var colName=[];
	var parm=null;
	for(i=0;i<opts.length;i++) {
		var col = $('#checkInfo_list').datagrid("getColumnOption", opts[i]);
		if ("checkUser" != col.field) {
			parm = {dataIndex: col.field, text: col.title, hidden: (col.hidden == undefined ? false : true)};
			jsonArr.push(JSON.stringify(parm));
		}
	}

	//模拟表单提交
	var url = basepath + '/check/checkInfoExport';
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