<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>打包复检</title>
    <%--<link rel="stylesheet" href="<%=basePath %>/static/css/common/reset.css" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="<%=basePath %>/static/css/goodsManage.css" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="<%=basePath %>/static/css/changeNoInfo.css" type="text/css"/>--%>
    <%--<link rel="stylesheet" href="<%=basePath %>/static/css/addGoods.css" type="text/css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/default/easyui.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/themes/icon.css">--%>
    <%--<link rel="stylesheet" type="text/css" href="<%=basePath %>/static/js/easyui-1.4.5/demo.css">--%>
    <%--<script type="text/javascript" src="<%=basePath %>/static/js/ckeditor/ckeditor.js"></script>--%>
    <style>
        .tableCon {
            border: 1px #dcdcdc solid;
        }

        .tableCon tr {
            border-bottom: 1px #dcdcdc solid;
        }

        .tableCon td, .tableCon th {
            text-align: center;
            border-right: 1px #dcdcdc solid;
        }
    </style>
</head>

<body>
<div id="orderDetailsBox" style="margin:10px">
    <input type="hidden" name='orderNo' id='orderNo' value="${orderNo }" />
    <div style="padding: 10px 1px">
        <%--数据操作区--%>
        <form id="packageForm">
            <table width="800">
                <tr style="height: 40px">
                    <td colspan="2">商品编码</td>
                    <td colspan="2">
                        <input class="easyui-textbox" name='goodsCode' id='goodsCode' style="width:300px;border:1px;bordercolor:'#000000'">
                    </td>
                    <td colspan="2" style="width: 45px">商品数量</td>
                    <td colspan="2" style="width: 100px">
                        <input type="text" class="easyui-numberbox" name='goodsNum' id='goodsNum' data-options="min:0"/>
                    </td>
                    <td>
                        <a class="btnG btnBl" onclick="recheckItem()">确定</a>
                    </td>
                </tr>
            </table>
        </form>
        <%--数据展示区--%>
        <table width="800" class="tableCon" id="goodsInfo">
            <tr style="height: 40px">
                <th style="width: 40px;">序号</th>
                <th>商品编码</th>
                <th>商品名称</th>
                <th>待检</th>
                <th>已检</th>
                <th>应检</th>
            </tr>
            <c:forEach items="${StockOutGoodsList}" var="goodOut" varStatus="vstatus">
                <tr style="height: 40px">
                    <td>${vstatus.index+1}</td>
                    <td>${goodOut.goodsCode}</td>
                    <td>${goodOut.goodsName}</td>
                    <td>${goodOut.stockOutNum}</td>
                    <td>0</td>
                    <td>${goodOut.stockOutNum}</td>
                </tr>
            </c:forEach>
        </table>
        <table width="800">
            <tr style="height: 40px">
                <td style="width: 20px"></td>
                <td colspan='4'></td>
                <td colspan='4' style="float:right;margin-top:10px"><a class="btnG btnBl" onclick="recheckFinsh()">完成复检</a></td>
            </tr>
        </table>
    </div>

    <%--弹框区--%>
    <div id="winPackageHeight" style="display:none;">
        <input type="text" name='orderNo' id='orderNoEx' value="${orderNo }" hidden="true"/>
        <table>
            <tr style="height: 40px;margin:20px">
                <td colspan="2" style="width: 50px;margin:20px"></td>
                <td colspan="2" style="float:right;">包裹重量:</td>
                <td style="margin-left:5px">
                    <input type="text" class="easyui-numberbox" name='parcelWeight' id='parcelWeight' data-options="min:0,precision:2"/>
                </td>
            </tr>
            <tr style="height: 40px;margin:20px;display:none">
                <td colspan="2" style="width: 50px;margin:20px"></td>
                <td colspan="2" style="float:right;">重量单位:</td>
                <td style="margin-left:5px">
                    <input type="text" name='weightUnit' id='weightUnit' class="easyui-textbox"/>
                </td>
            </tr>
            <tr style="height: 40px">
                <td colspan="2" style="width: 100px;margin:20px"></td>
                <td colspan="2">
                    <a class="btnG btnBl" onclick="recheckCancel()" style="padding-right:10px">取消</a>
                    <a class="btnG btnBl" onclick="recheckConfirm()" style="float:right;padding-left:10px">确定</a>
                </td>
                <td></td>
            </tr>
        </table>
    </div>
</div>
<%--<script type="text/javascript" src="<%=basePath %>/static/js//jquery-1.8.3.min.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.min.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath %>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>	--%>
<script type="text/javascript">
    function recheckItem() {
        var v_goodsCode = $('#goodsCode').val();
        var v_goodsNum = $('#goodsNum').val();
        if (v_goodsCode == '' || v_goodsCode.length == 0) {
            $.messager.alert('提醒', '请输入商品编码!');
            return;
        }
        if (v_goodsNum == '' || v_goodsNum.length == 0 || parseInt(v_goodsNum) <= 0) {
            $.messager.alert('提醒', '请输入商品数量!');
            return;
        }
        var isExistGoods = false;
        $("#goodsInfo tr").each(function (i) {
            var goodsCode = $(this).find("td").eq(1).text();//商品编码
            // 判断是否存在
            if (v_goodsCode == goodsCode) {
                var recheckedNum = $(this).find("td").eq(4).text();//已检数量
                var stockOutNum = $(this).find("td").eq(5).text();//应检数量
                isExistGoods = true;
                // 修改-- 商品数量需要小于等于待拣数量
                if (parseInt(v_goodsNum) > parseInt(stockOutNum) - parseInt(recheckedNum)) {
                    $.messager.alert('提醒', '验货数量超出，请检查!');
                    return false;
                }
                $.messager.confirm('确认', '确认验货？', function (r) {
                    if (r) {
                        var nowRecheckedNum = parseInt(v_goodsNum) + parseInt(recheckedNum);
                        var nowWaitRecheckNum = parseInt(stockOutNum) - nowRecheckedNum;
                        var currRow = document.getElementById("goodsInfo").rows[i];
                        currRow.cells[3].innerHTML = nowWaitRecheckNum;//待检
                        currRow.cells[4].innerHTML = nowRecheckedNum;//已检
                        if (nowWaitRecheckNum == 0) {
                            var trStr = "<tr style=\"height: 40px\">" + $(currRow).html() + "</tr>";
                            $("#goodsInfo").append(trStr);
                            $(currRow).remove();
                            // 序号需要重排
                            $("#goodsInfo tr:not(:first)").each(function(index, element) {
                                element.cells[0].innerHTML = index + 1;
                            });
                        }
                        $("#packageForm").form('clear');
                    }
                });
            }
        });
        if (!isExistGoods) {
            $.messager.alert('提醒', '该商品编码不存在，请重新输入!');
        }
    }
    //完成复检
    function recheckFinsh() {
        var flag = true;
        $("#goodsInfo tr").each(function (i) {
            if (i == 0) return true;
            var stockOutNum = $(this).find("td").eq(5).text();//应检数量
            var chkNum = $(this).find("td").eq(4).text();//已检
            if (parseInt(stockOutNum) != parseInt(chkNum)) {
                var goodsCode = document.getElementById("goodsInfo").rows[i].cells[1].innerHTML;
                $.messager.alert('提醒', '商品编码为' + goodsCode + '的记录，复检未完成!');
                flag = false;
                return false;
            }
        });
        if (flag) {
            $('#winPackageHeight').window({
                width: 400,
                height: 200,
                title: '打包复检',
                collapsible: false,
                minimizable: false,
                maximizable: false,
                modal: true
            });
        }
    }
    //取消
    function recheckCancel() {
        $('#winPackageHeight').window('close');
    }

    // 打包复检提交
    function recheckConfirm() {
        var parcelWeight = $('#parcelWeight').val();
        var weightUnit = $('#weightUnit').val();
        if (parcelWeight == null || parcelWeight == '') {
            $.messager.alert('提醒', '请输入包裹重量!');
            return;
        }
//		if(weightUnit==null||weightUnit==''){
//			$.messager.alert('提醒', '请输入重量单位!');
//			return;
//		}
        var orderNo = $('#orderNoEx').val();
        var parms = {};
        parms["orderNo"] = orderNo;
        parms["parcelWeight"] = parcelWeight;
        parms["weightUnit"] = weightUnit;
        $.ajax({
            //type : "POST",
            url: '<%=basePath %>/outstock/recheckConfirm',
            data: parms,
            dataType: "json",
            success: function (result) {
                if (result.status == 200) {
                    $('#winPackageHeight').window('close');
                    $('#recheckInfo').window('close');
                    location.reload();
                } else {
                    $.messager.alert('提醒', result.message);
                }
            }
        });
    }
</script>
</body>
</html>