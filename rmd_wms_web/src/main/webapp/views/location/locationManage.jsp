<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>库位管理</title>
    <link rel="stylesheet" href="<%=basePath%>/static/css/common/reset.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/goodsManage.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/changeNoInfo.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/static/css/addGoods.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/static/js/easyui-1.4.5/demo.css">
    <style>
        .datagrid-btable .datagrid-cell {
            padding: 6px 4px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
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
        .inBox{
        	line-height: 26px;
		    width: 447px;
		    margin: 30px auto;
        }
        .textbox{margin-left: -4px!important;}
        .radioBox input[type="radio"]{
        	margin: 0 5px 0 15px;
        }
        .radioBox input[type="radio"]:first-child{
        	margin-left: 0;
        }
    </style>
    <script type="text/javascript">
        window.basePath = "<%=basePath%>";
    </script>
</head>
<body class="easyui-layout">
<div class="goodsmBox smBox" data-options="region:'center'"
     style="border: 0">
    <div class="easyui-layout" data-options="fit:true">
        <div class="header" id="locationListSearch"
             data-options="region:'north'" style="border: 0">
            <p class="navBar mgB16">
                <a class="btnG btnBl" onclick="locationSearchBox()">搜索</a>
                <a class="btnG btnBl" onclick="locationAreaReset()">重置</a>
            </p>
            <table class="gList">
                <td><label for="" class="dib w77">货位号</label><input
                        class="easyui-validatebox combo" id="locationNo" name="locationNo" maxlength="20"/></td>
                <td><label for="" class="dib tar">所属库区</label><input
                        class="easyui-validatebox combo" id="areaName" name="areaName"
                        style="width: 184px; height: 28px; line-height: 28px;"/></td>
                <td>
                <td><label for="" class="dib tar">状态</label>
                    <select id="status" name="status" class="easyui-combobox"
                            style="width: 194px; height: 28px; line-height: 28px;">
                        <option value="">全部状态</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </td>
                <td><label for="" class="dib w77">所属仓库</label>
                    <select id="wareId" name="wareId" class="easyui-combobox"
                            style="width: 194px; height: 28px; line-height: 28px;">
                        <option value="0">全部仓库</option>
                        <c:forEach items="${warehouseList}" var="houseList">
                            <option value="${houseList.id}">${houseList.wareName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td><label for="" class="dib tar">库区性质</label>
                    <select id="type" name="type" class="easyui-combobox"
                            style="width: 194px; height: 28px; line-height: 28px;">
                        <option value="">全部性质</option>
                        <option value="0">不可卖</option>
                        <option value="1">可卖</option>
                    </select>
                </td>
            </table>
        </div>
        <div class="center" data-options="region:'center'" style="border: 0">
            <div class="easyui-layout" data-options="fit:true">
                <div data-options="region:'north'" style="padding: 20px 30px 12px; border: 0">
                    <p class="cenBtn">
                        <shiro:hasPermission name="WMS_LOCATION_OPEN">
                            <a class="btnG btnBl" onclick="locationStatusUpdate(1)">启用</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="WMS_LOCATION_FORBID">
                            <a class="btnG btnBl" onclick="locationStatusUpdate(0)">禁用</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="WMS_LOCATION_EDIT">
                            <a class="btnG btnBl" id="locationEdit">编辑</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="WMS_LOCATION_ADD">
                            <a class="btnG btnBl" id="locationAdd">+添加货位</a>
                        </shiro:hasPermission>
                        <a class="btnG btnBl" id="locationImport">导入</a>
<%--
                        <a class="btnG btnBl" id="locationExport">导出</a>
--%>
                    </p>
                </div>
                <!-- datagrid列表  -->
                <div data-options="region:'center'" style="padding: 1px 30px 20px; border: 0">
                    <table id="location_list" width="100%"></table>
                </div>
            </div>
            <!--编辑库区弹窗页面 -->
            <div id="editLocation" class="editNoticBox"></div>
            <!-- 添加库区弹窗页面-->
            <div id="addLocation" class="addNoticBox"></div>
            <%-- 导入框 --%>
            <div class="tabContentBox" style="display: none;" id="importFrame">
                <form id="importFrameForm">
                    <div class="inBox" style="width: 250px;margin: 30px auto;text-align: center;">
                        <p>请选择excel格式的文件</p>
                        <p><input type="file" name="locationExcel" id="locationExcel"/></p>
                        <label id="uploadInfo"></label>
                    </div>
                    <div class="edBox tac">
                        <a href="javascript:;" class="btnG btnBl" id="commit">上传</a>
                        <a href="javascript:;" class="btnG btnOg" id="cancel">关闭</a>
                    </div>
                </form>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript"
        src="<%=basePath%>/static/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/jquery.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/jquery.easyui.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/easyui-1.4.5/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/LocalResizeIMG.js"></script>
<script type="text/javascript"
        src="<%=basePath%>/static/js/LocalResizeDOC.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/js/location/locationManage.js?v=0.01"></script>

<script type="text/javascript">
    //主页面表单
    $('.cenList .list li').click(function () {
        var index = $(this).index();
        $(this).addClass("active").siblings().removeClass("active");
    });
    //新增仓库
    $('#locationAdd').click(function () {
        $(".addNoticBox").window({
            href: '<%=basePath%>/location/locationAddPage',
            width: 580,
            height: 435,
            modal: true,
            shadow: false,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            title: "添加库位"
        });
    });
    //编辑仓库
    //修改弹窗
    $('#locationEdit').click(function () {
        var rows = $('#location_list').datagrid('getSelections');
        if (rows == "") {
            $.messager.alert("提示", "请选择一条数据 ！", "info");
            return;
        }
        if (rows.length > 1) {
            $.messager.alert("提示", "请选择一条数据 ！", "info");
            return;
        }
        var id = rows[0].id;
        $('.editNoticBox').window({
            href: "<%=basePath%>/location/locationEditPage?id=" + id,
            width: 580,
            height: 435,
            modal: true,
            shadow: false,
            collapsible: false,
            minimizable: false,
            maximizable: false,
            minimizable: false,
            maximizable: false,
            title: "编辑库区"

        });
    });


</script>
</body>
</html>