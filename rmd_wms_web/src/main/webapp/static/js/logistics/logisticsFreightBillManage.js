var basepath = $("#basepath").val();
var newurl = basepath + '/logisFreiBill/getLogisFreiBills';
$(function () {

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

    //初始化运费单列表
    init(newurl);
});
// 初始化方法
function init(url) {
    var queryParams = {};
    var pageNo = 1;
    var rows = 10;
    if (queryParams.pageNo && queryParams.pageNo != '') {
        pageNo = queryParams.pageNo;
        rows = queryParams.rows;
    }
    // 获取参数
    var paramArr = $("#logisFreiBillSearchForm").serializeArray();
    for (var temp in paramArr) {
        if (paramArr[temp].value == null || paramArr[temp].value == "" || paramArr[temp].value == undefined) {
            continue;
        }
        queryParams[paramArr[temp].name] = paramArr[temp].value;
    }
    $('#logistics_bill_list').datagrid({
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
            field: 'logisticsNo',
            title: '运单号',
            width: 100,
            //formatter: function (value, row, index) {
            //    return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
            //},
            align: 'center'
        }, {
            field: 'orderNo',
            title: '订单号',
            width: 100,
            //formatter: function (value, row, index) {
            //    return '<a style="color:#1556e8;" target="_blank" onclick="getInstockBiLLInfo(' + row.id + ')" class="ellipsis" >' + value + '</a>';
            //},
            align: 'center'
        }, {
            field: 'logisComName',
            title: '承运商',
            width: 120,
            align: 'center'
        }, {
            field: 'receiveAddress',
            title: '收货地址',
            width: 100,
            align: 'center'
        }, {
            field: 'basePrice',
            title: '运费(元)',
            width: 60,
            formatter: function (val, row, index) {
                if (row && row.extraCharges != null) {
                    return (val + row.extraCharges).toFixed(2);
                } else {
                    return val.toFixed(2);
                }
            },
            align: 'center'
        }, {
            field: 'doChange',
            title: '运费修改状态',
            width: 100,
            formatter: function (val, row, index) {
                if (val == "0") {
                    return "否";
                } else if (val == "1") {
                    return '<a style="color:#1556e8;" onclick="getFreiBiLLInfo(\'' + row.updateUserName + '\',' + row.extraCharges + ', ' + row.updateTime + ')" class="ellipsis" >是</a>';
                }
            },
            align: 'center'
        }, {
            field: 'createTime',
            title: '创建时间',
            width: 100,
            formatter: function (val, row, index) {
                return formatDatebox(val);
            },
            align: 'center'
        }, {
            field: 'updateUserName',
            title: '操作员',
            width: 100,
            hidden: true,
            align: 'center'
        }, {
            field: 'extraCharges',
            title: '增加费用',
            width: 100,
            hidden: true,
            align: 'center'
        }, {
            field: 'updateTime',
            title: '修改时间',
            width: 100,
            hidden: true,
            align: 'center',
            formatter: function (val, row, index) {
                return formatDatebox(new Date(val));
            }
        }]],
        pagination: true,// 显示分页栏。
        rownumbers: true,// 显示行号的列。
        pageSize: rows,
        pageNumber: pageNo,
        queryParams: queryParams,
        pageList: [10, 20, 30, 40]
    });
}

// 查看运费单修改信息
function getFreiBiLLInfo(updateUserName, extraCharges, updateTime) {
    if (extraCharges && updateTime) {
        var infoLi = $("#showLogisticsBillInfo ul li");
        infoLi.eq(0).find("label").eq(1).html(updateUserName);
        infoLi.eq(1).find("label").eq(1).html(extraCharges.toFixed(2));
        infoLi.eq(2).find("label").eq(1).html(formatDatebox(updateTime));
        $("#showLogisticsBillInfo").window({
            width: 500,
            height: 280,
            modal: true,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            title: "配送管理-运费明细单-查看运费修改信息",
            //onBeforeClose: function () {
            //    sort();
            //}
        });
    } else {
        $.messager.alert('提醒', '数据错误!');
        return;
    }
}

// 时间格式化
Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
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
    return dt.format("yyyy-MM-dd hh:mm:ss"); // 扩展的Date的format方法(上述插件实现)
};

//修改运费
$("#alterPrice").click(function () {
    var rows = $('#logistics_bill_list').datagrid('getSelections');
    if (rows.length > 1) {
        $.messager.alert('提醒', '只能选择一条数据 !');
        return;
    } else if (rows.length == 0) {
        $.messager.alert('提醒', '请选择一条数据 !');
        return;
    }
    if (rows) {
        if (rows[0].doChange == 1) {
            $.messager.alert('提醒', '该运费明细单的运费只允许修改一次！');
            return;
        }
        $("#alterId").attr("value", rows[0].id);
        $("#extraCharges").attr("value", rows[0].extraCharges);
        $("#editLogisticsBill").window({
            width: 500,
            height: 280,
            modal: true,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            title: "配送管理-运费明细单-修改运费",
            onBeforeClose: function () {
                sort();
            }
        });
    } else {
        $.messager.alert('提醒', '请选择一条数据!');
        return;
    }
});

//编辑确定按钮
$("#commitedit").click(function () {
    var extraCharges = $.trim($("#extraCharges").val());
    if (isNaN(extraCharges)) {
        $.messager.alert('提醒', "请输入数字");
        return;
    }
    var info = {
        id: $.trim($("#alterId").val()),
        extraCharges: extraCharges
    };
    if (info == null || info.extraCharges == '' || info.extraCharges == 0) {
        $.messager.alert('提醒', "请填写正确的增加费用");
        return;
    } else {
        $.ajax({
            type: "POST",
            url: basepath + '/logisFreiBill/alterExtraCharges',
            data: info,
            dataType: "json",
            success: function (result) {
                if (result && result.status == "200") {
                    $("#editLogisticsBill").window("close");
                } else {
                    $.messager.alert('提醒', result.message);
                }
            }
        });
    }
});


//取消按钮
$("#cancel").click(function () {
    $("#editLogisticsBill").window("close");
});
//排序
function sort() {
    location.reload();
}

//搜索事件
function logisticsListSearchBox() {
    init(newurl);
}

//重置按钮
function formReset() {
    location.reload();
}

//Excel导出
$("#lfBillExport").click(function () {
    //模拟表单提交
    var url = basepath + '/logisFreiBill/lfBillExport';
    var form = $("<form>");//定义一个form表单
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("method", "post");//请求类型
    form.attr("action", url);//请求地址
    $("body").append(form);//将表单放置在web中
    $("body").append("</form>");

    var input1 = $("<input>");
    input1.attr("type", "hidden");
    input1.attr("name", "columns");
    input1.attr("value", JSON.stringify(logisticsFreightBillHead));
    form.append(input1);
    form.submit();//表单提交
});
