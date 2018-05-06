// 基础连接
var basepath = $("#basepath").val();
var getCheckBillUrl = basepath + '/check/searchChecks';// 获取盘点单列表url
var getAreaUrl = basepath + "/warehouseArea/getAllWarehouseArea";// 回去库区url
var getBindsUrl = basepath + "/locationGoodsBind/searchBindsByCreateCheck";// 获取货位url
var createCheckUrl = basepath + "/check/createCheck";// 生成盘点单url

// 页面初始化
$(function () {
    // 初始化盘点单数据
    init(getCheckBillUrl);
    // 创建绑定事件
    createCheckBindEventByLocation();
    createCheckBindEventByGoods();
    var wareStatus=$('#wareStatus').val();
    if(wareStatus=='1'){
    	$('#orderOperate').html('关闭订单');
    }else if(wareStatus=='2'){
    	$('#orderOperate').html('开放订单');
    }else{
    	$('#orderOperate').hide();
    }
});

/**
 * 初始化方法
 * @param url
 */
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
    // 封装查询参数
    var pageNo = 1;
    var rows = 10;
    var queryParams = {};
    if (queryParams.pageNo && queryParams.pageNo != '') {
        pageNo = queryParams.pageNo;
        rows = queryParams.rows;
    }
    var paramArr = $("#checkBillSearchForm").serializeArray();
    for (var temp in paramArr) {
        if (paramArr[temp].value == null || paramArr[temp].value == "" || paramArr[temp].value == undefined) {
            continue;
        }
        queryParams[paramArr[temp].name] = paramArr[temp].value;
    }
    // 生成表格数据
    $('#checkBillList').datagrid({
        width: 'auto',
        height: 'auto',
        nowrap: true,// True 数据将在一行显示
        striped: true,// True 就把行条纹化
        collapsible: false,// True 可折叠
        url: url,
        remoteSort: true, // 定义是否从服务器给数据排序
        singleSelect: false, // True 只能选择一行
        fit: true,
        toolbar: '#box',
        scrollbarSize: 0,
        fitColumns: true,
        frozenColumns: [[{
            field: 'ck',
            checkbox: true
        },]],
        columns: [[{
            field: 'id',
            titel: 'id',
            hidden: true
        }, {
            field: 'checkNo',
            title: '盘点单号',
            width: 100,
            align: 'center',
            formatter : function(value, row, index) {
				return '<a style="color:#1556e8;" target="_blank" onclick="getCheckGoodsInfo(\''+value+'\')" class="ellipsis" >' + value + '</a>';
			}
        }, {
            field: 'type',
            title: '类型',
            width: 100,
            align: 'center',
            formatter: function (val, row, index) {
                //类型，1：日盘，2：大盘'
                if (val == "1") {
                    return "日盘";
                } else if(val == "2") {
                    return "大盘";
                }
            }
        }, {
            field: 'status',
            title: '盘点状态',
            width: 80,
            align: 'center',
            formatter: function (val, row, index) {
                //盘点状态，1：等待,2：盘点中,3：盘点中断（部分盘点），4：已完成
                if (val == "1") {
                    return "等待";
                } else if (val == "2") {
                    return "盘点中";
                } else if (val == "3") {
                    return "中断";
                } else if (val == "4") {
                    return "完成";
                }
            }
        }, {
            field: 'checkTimes',
            title: '盘点次数',
            width: 100,
            align: 'center'
        }, {
            field: 'wareName',
            title: '仓库',
            width: 100,
            align: 'center'
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 100,
            align: 'center',
            formatter: function (val, row, index) {
               return formatDatebox(new Date(val));
            }
        }, {
            field: 'createrName',
            title: '创建人',
            width: 100,
            align: 'center'
        }, {
            field: 'checkUserNames',
            title: '盘点人',
            width: 100,
            align: 'center'
        }]],
        pagination: true,// 显示分页栏。
        rownumbers: true,// 显示行号的列。
        pageNumber: pageNo,
        pageSize: rows,
        queryParams: queryParams,
        pageList: [10, 20, 30, 40]
    });

}

/**
 * 新建盘点单通过货位
 */
$("#createCheckByLocation").click(function () {
	$(".createPlanOption").hide();
    // 初始化数据
    initCreateCheckPopByLocation();
    // 初始化默认数据
    searchLocationGoodsBindByLocation();
    // 显示弹窗
    $("#createCheckByLocationPop").window({
        width: 900,
        height: 500,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        title: "盘点管理-新增盘点计划"
    });
});

/**
 * 新建盘点单通过商品
 */
$("#createCheckByGoods").click(function () {
	$(".createPlanOption").hide();
    // 初始化默认数据
    searchLocationGoodsBindByGoods();
    // 显示弹窗
    $("#createCheckByGoodsPop").window({
        width: 900,
        height: 500,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        title: "盘点管理-新增盘点计划"
    });
});

/**
 * 初始化弹框数据通过货位
 */
function initCreateCheckPopByLocation() {
    // 初始化库区下拉列表
    $.getJSON(getAreaUrl, function (data) {
        if (data) {
            $('#warehouseAreaSelect').combobox({
                data: data.result,
                valueField: 'id',
                textField: 'areaName'
            });
        }
    });
};

/**
 * 创建绑定事件
 */
function createCheckBindEventByLocation() {
    // 绑定创建计划
    $("#doCreateCheckBtn").bind("click", function () {
        var ids = [];
        var rows = $('#locationGoodsBindListByLoca').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].id == null || rows[i].id == "") {
                continue;
            }
            ids.push(rows[i].id);
        }
        // 生成盘点单
        var params = {};
        if (ids != null && ids.length > 0 && ids[0] != "") {
            params["ids"] = JSON.stringify(ids);
        }
        var type = $("#createCheckByLocationPop input[type='radio']:checked").val();
        if (type && type != undefined) {
            params["type"] = type;
        } else {
            return false;
        }
        $.post(createCheckUrl, params, function (data) {
            if (data.status == "200") {
                // 重新初始化盘点单数据
                init(getCheckBillUrl);
                $("#createCheckByLocationPop").window("close");
            } else {
                alert("创建失败");
            }
        });
    })
}
/**
 * 创建绑定事件通过商品
 */
function createCheckBindEventByGoods() {
    // 绑定创建计划
    $("#doCreateCheckByGoodsBtn").bind("click", function () {
        var ids = [];
        var rows = $('#locationGoodsBindListByGoods').datagrid('getSelections');
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].id == null || rows[i].id == "") {
                continue;
            }
            ids.push(rows[i].id);
        }
        // 生成盘点单
        var params = {};
        if (ids != null && ids.length > 0 && ids[0] != "") {
            params["ids"] = JSON.stringify(ids);
        }
        var type = $("#createCheckByGoodsPop input[type='radio']:checked").val();
        if (type && type != undefined) {
            params["type"] = type;
        } else {
            return false;
        }
        $.post(createCheckUrl, params, function (data) {
            if (data.status == "200") {
                // 重新初始化盘点单数据
                init(getCheckBillUrl);
                $("#createCheckByGoodsPop").window("close");
            } else {
                alert("创建失败");
            }
        });
    })
}

/**
 * 初始化货位信息通过货位
 */
function searchLocationGoodsBindByLocation() {
    var areaIds = $('#warehouseAreaSelect').combobox('getValues');
    var locationNo = $("#locationNo").val();
    // 封装查询参数
    var queryParams = {};
    if (areaIds != null && areaIds.length > 0 && areaIds[0] != "") {
        queryParams["areaIds"] = JSON.stringify(areaIds);
    }
    if (locationNo != null && locationNo != undefined && locationNo != "") {
        queryParams["locationNo"] = locationNo;
    }
    $.post(getBindsUrl, queryParams, function (data) {
        if (data && data.status == "200") {
            $('#locationGoodsBindListByLoca').datagrid({
                width: 'auto',
                height: 'auto',
                nowrap: true,// True 数据将在一行显示
                striped: true,// True 就把行条纹化
                collapsible: false,// True 可折叠
                //url: popUrl,
                //queryParams: queryParams,
                //remoteSort: true, // 定义是否从服务器给数据排序
                singleSelect: false, // True 只能选择一行
                select: false,
                fit: false,
                toolbar: '#box',
                scrollbarSize: 0,
                fitColumns: true,
                frozenColumns: [[{
                    field: 'ck',
                    checkbox: true
                },]],
                columns: [[{
                    field: 'id',
                    titel: 'id',
                    hidden: true
                }, {
                    field: 'locationNo',
                    title: '货位',
                    width: 100,
                    align: 'center'
                }, {
                    field: 'areaName',
                    title: '库区',
                    width: 100,
                    align: 'center'
                }, {
                    field: 'saleType',
                    title: '订单类型',
                    width: 80,
                    align: 'center',
                    formatter: function (val, row, index) {
                        // 0：不可卖，1：可卖', 2:损坏
                        if (val == 0) {
                            return "不可卖";
                        } else if (val == 1) {
                            return "可卖";
                        } else if (val == 2) {
                            return "损坏";
                        }
                    }
                }
                ]],
                pagination: false,// 显示分页栏。
                rownumbers: true,// 显示行号的列。
                data: data.result
            });
        } else {
            alert("查询失败");
        }
    });
}

/**
 * 初始化货位信息通过商品
 */
function searchLocationGoodsBindByGoods() {
    var goodsName = $('#goodsName').val();
    var spec = $("#spec").val();
    // 封装查询参数
    var queryParams = {};
    if (goodsName != null && goodsName != undefined && goodsName != "") {
        queryParams["goodsName"] = goodsName;
    }
    if (spec != null && spec != undefined && spec != "") {
        queryParams["spec"] = spec;
    }
    $.post(getBindsUrl, queryParams, function (data) {
        if (data && data.status == "200") {
            $('#locationGoodsBindListByGoods').datagrid({
                width: 'auto',
                height: 'auto',
                nowrap: true,// True 数据将在一行显示
                striped: true,// True 就把行条纹化
                collapsible: false,// True 可折叠
                //url: popUrl,
                //queryParams: queryParams,
                //remoteSort: true, // 定义是否从服务器给数据排序
                singleSelect: false, // True 只能选择一行
                select: false,
                fit: false,
                toolbar: '#box',
                scrollbarSize: 0,
                fitColumns: true,
                frozenColumns: [[{
                    field: 'ck',
                    checkbox: true
                },]],
                columns: [[{
                    field: 'id',
                    titel: 'id',
                    hidden: true
                }, {
                    field: 'goodsCode',
                    title: '商品编码',
                    width: 100,
                    align: 'center'
                }, {
                    field: 'locationNo',
                    title: '货位',
                    width: 100,
                    align: 'center'
                }, {
                    field: 'locationNum',
                    title: '盘点数量',
                    width: 80,
                    align: 'center'
                }
                ]],
                pagination: false,// 显示分页栏。
                rownumbers: true,// 显示行号的列。
                data: data.result
            });
        } else {
            alert("查询失败");
        }
    });
}

//创建盘点单窗口关闭按钮
$("#cancelCheckBtn").click(function () {
    $("#createCheckByLocationPop").window("close");
});
//创建盘点单窗口关闭按钮
$("#cancelCheckByGoodsBtn").click(function () {
    $("#createCheckByGoodsPop").window("close");
});

//时间格式化
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
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

/**
 * 查询盘点单
 */
function checkBillsSearch() {
    init(getCheckBillUrl);
}

//重置按钮
function formReset() {
    location.reload();
};
//盘点单详情
function getCheckGoodsInfo(checkNo){
	var l = (screen.availWidth - 900) / 2;
	var t = (screen.availHeight - 550) / 2;
	window.open(basepath + "/check/getCheckGoodsInfo?checkNo=" + checkNo, "", "height=550, width=900, top=" + t + ",left=" + l + ",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

}

//盘点打印
$('#checkPrint').click(function(){
	var selRows = $('#checkBillList').datagrid('getChecked');
	if(selRows.length>0){
		var ids='';
		for(var i=0;i<selRows.length;i++){
			ids+=selRows[i].id;
			if(i!=(selRows.length-1)){
				ids+=",";
			}

		}
		if(ids.length>0){
			if(ids.lastIndexOf(',')==ids.length-1){
				ids=ids.substring(0, ids.length-1);
			}
	    	var l=(screen.availWidth-900)/2;
			var t=(screen.availHeight-550)/2;
			window.open(basepath+"/check/checkBillPrint?Id="+ids, "盘点打印", "height=550, width=900, top="+t+",left="+l+",toolbar =no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");

		}

	}else {
		$.messager.alert('提醒', '请选择一条数据!');
		return;
	}
});

//开放订单、关闭订单
$('#orderOperate').click(function(){
	var wareStatus=$('#wareStatus').val();
    if(wareStatus=='1'){
    	//$('#orderOperate').html('关闭订单');
    	$.messager.confirm('确认','关闭订单后当前仓库不接受任何新订单？',function(r){    
    	    if (r){    
    	    	$.ajax({
    	    		url : basepath + '/check/orderOperate',
    	    		type : 'post',
    	    		data : {
    	    			'operateStatus' : '0'
    	    		},
    	    		success : function(result) {
    	    			if (result.status == "200") {
    	    				$('#orderOperate').html('开放订单');
    	    				$('#wareStatus').val(2);//仓库状态变更
    	    			} 
    	    		}
    	    	});
    	    }    
    	});  
    	
    	
    	
    }else if(wareStatus=='2'){
    	//$('#orderOperate').html('开放订单');
    	var flag=false;
    	$.ajax({
    		url : basepath + '/check/checkSubmitStatus',
    		async:false,
    		type : 'post',
    		success : function(result) {
    			if (result.status == "200") {
    				flag=true;;
    			} 
    		}
    	});
    	if(flag){
    		$.messager.confirm('确认','<font color="red">有未提报数据，点击【确认】开放订单，盘点数据将无法提报？</font>',function(r){    
    		    if (r){    
    		    	$.ajax({
        	    		url : basepath + '/check/orderOperate',
        	    		async:false,
        	    		type : 'post',
        	    		data : {
        	    			'operateStatus' : '1'
        	    		},
        	    		success : function(result) {
        	    			if (result.status == "200") {
        	    				$('#orderOperate').html('关闭订单');
        	    				$('#wareStatus').val(1);
        	    			} 
        	    		}
        	    	});
    		    }    
    		}); 
    	}else{
    		$.messager.confirm('确认','<font color="red">确认开放订单？</font>',function(r){    
    		    if (r){    
    		    	$.ajax({
        	    		url : basepath + '/check/orderOperate',
        	    		async:false,
        	    		type : 'post',
        	    		data : {
        	    			'operateStatus' : '1'
        	    		},
        	    		success : function(result) {
        	    			if (result.status == "200") {
        	    				$('#orderOperate').html('关闭订单');
        	    				$('#wareStatus').val(1);
        	    			} 
        	    		}
        	    	});
    		    }    
    		});
    	}
    }
});