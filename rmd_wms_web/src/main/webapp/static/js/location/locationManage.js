var newurl = './listLocation';
$(function () {
    init(newurl);
});
function init(url) {
    var queryParams = {};
    $('#locationListSearch').find('*').each(function () {
        queryParams[$(this).attr('name')] = $(this).val();
    });
    var pageNo = 1;
    var rows = 10;
    if (queryParams.pageNo && queryParams.pageNo != '') {
        pageNo = queryParams.pageNo;
        rows = queryParams.rows;
    }
    $('#location_list').datagrid({
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
        url: url,
        remoteSort: true, // 定义是否从服务器给数据排序
        singleSelect: false, // True 只能选择一行
        fit: false,
        toolbar: '#box',
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
            title: '货位号',
            width: 100,
            align: 'center'
        }, {
            field: 'status',
            title: '状态',
            width: 100,
            align: 'center',
            formatter: function (val, row, index) {
                if (val == "0") {
                    return "禁用";
                } else if (val == "1") {
                    return "启用";
                }
            }
        }, {
            field: 'wareName',
            title: '所属仓库',
            width: 100,
            align: 'center'
        }, {
            field: 'areaName',
            title: '所属库区',
            width: 100,
            align: 'center',
        }, {
            field: 'type',
            title: '库区性质',
            width: 100,
            align: 'center',
            formatter: function (val, row, index) {
                if (val == "0") {
                    return "不可卖";
                } else if (val == "1") {
                    return "可卖";
                }
            }
        }]],
        onLoadSuccess: function (data) {
            if (0 == data.total) {
                //$.messager.alert('信息提示','无数据!','error');
            }
        },
        pagination: true,// 显示分页栏。
        rownumbers: true,// 显示行号的列。
        pageSize: rows,
        pageNumber: pageNo,
        queryParams: queryParams,
        pageList: [10, 20, 30, 40],
    });
}

//状态修改
function locationStatusUpdate(status) {
    var rows = $('#location_list').datagrid('getSelections');
    if (rows.length < 1) {
        $.messager.alert("提示", "请选择数据！", "info");
        return;
    }
    if (status == 0) {
        $.messager.confirm("确认消息", "是否禁用此货位?", function (r) {
            if (r) {
                var strIds = "";// id
                for (var i = 0; i < rows.length; i++) {
                    strIds += rows[i].id + ",";
                }
                strIds = strIds.substr(0, strIds.length - 1);
                $.post("./updateStatusById", {
                    ids: strIds,
                    status: status
                }, function (data) {
                    if (data == "true") {
                        $.messager.alert("提示", "操作成功", "info");
                        $("#location_list").datagrid('reload');// 重新加载table
                    }
                    if (data == "updateNo") {
                        $.messager.alert("提示", "此货位下有商品，不能被修禁用", "info");
                        return;
                    }
                    if (data == "false") {
                        $.messager.alert("提示", "操作失败", "info");
                        return;
                    }
                });
            }
        });
    } else {
        $.messager.confirm("确认消息", "是否启用此货位?", function (r) {
            if (r) {
                var strIds = "";// id
                for (var i = 0; i < rows.length; i++) {
                    strIds += rows[i].id + ",";
                }
                strIds = strIds.substr(0, strIds.length - 1);
                $.post("./updateStatusById", {
                    ids: strIds,
                    status: status
                }, function (data) {
                    if (data == "true") {
                        $.messager.alert("提示", "操作成功", "info");
                        $("#location_list").datagrid('reload');// 重新加载table
                    }
                    if (data == "false") {
                        $.messager.alert("提示", "操作失败", "info");
                    }

                });
            }
        });
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

    return dt.format("yyyy-MM-dd"); // 扩展的Date的format方法(上述插件实现)
};
function locationAreaReset() {
    location.reload();
}
// 动态搜索方法
function locationSearchBox() {
    init(newurl);
}


//展示导入框
$("#locationImport").click(function () {
    $("#importFrameForm").form('clear');
    $("#uploadInfo").empty();
    $("#importFrame").window({
        width: 500,
        height: 280,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        title: "基本档案-库位管理-导入货位",
        onBeforeClose: function () {
            location.reload();
        }
    });
});

//上传确定按钮
$("#commit").click(function () {
    var file = document.getElementById("locationExcel").files[0];
    //判断控件中是否存在文件内容，如果不存在，弹出提示信息，阻止进一步操作
    if (file == null) {
        $.messager.alert("提示", "请选择文件！", "info");
        return;
    }
    //获取文件名称
    var fileName = file.name;
    //获取文件类型名称
    var file_typename = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
    //这里限定上传文件文件类型必须为.xlsx，如果文件类型不符，提示错误信息
    if (file_typename == '.xls' || file_typename == '.xlsx') {
        //document.getElementById('fileName').innerHTML = "<span style='color:Blue'>文件名: " + file.name + "</span>";
        var formData = new FormData();
        formData.append("excelFile", file);
        $.ajax({
            url: window.basePath + '/location/locaExcelImport',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            success: function (result) {
                //上传成功后将控件内容清空，并显示上传成功信息
                $("#uploadInfo").html("<span style='color:Red'>" + result.message + "</span>");
            }
        });
    } else {
        $.messager.alert('错误', '文件类型错误！', 'error');
        //将错误信息显示在前端label文本中
        $("#fileName").html("<span style='color:Red'>错误提示:上传文件应该excel格式而不应该是" + file_typename + ",请重新选择文件</span>");
    }
    //location.reload();
});

$("#cancel").click(function () {
    closeImportBtn();
});

//取消按钮
function closeImportBtn(){
    $("#importFrame").window("close");
}